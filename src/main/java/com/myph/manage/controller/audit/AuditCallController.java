/**   
 * @Title: JkApplyAuditController.java 
 * @Package: com.myph.manage.audit.controller
 * @company: 麦芽金服
 * @Description: TODO
 * @date 2016年9月20日 下午9:17:42 
 * @version V1.0   
 */
package com.myph.manage.controller.audit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.myph.apply.constant.LinkmanTypeEnum;
import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.dto.ApplyUserDto;
import com.myph.apply.dto.JkApplyJobDto;
import com.myph.apply.dto.JkApplyLinkmanDto;
import com.myph.apply.dto.MemberJobDto;
import com.myph.apply.dto.MemberLinkmanDto;
import com.myph.apply.jobinfo.service.JobInfoService;
import com.myph.apply.jobinfo.service.MemberJobService;
import com.myph.apply.linkman.service.ContactsService;
import com.myph.apply.linkman.service.MemberLinkmanService;
import com.myph.apply.service.ApplyInfoService;
import com.myph.apply.service.ApplyUserService;
import com.myph.auditCall.constant.AuditCallHisType;
import com.myph.auditCall.dto.AuditCallDto;
import com.myph.auditCall.dto.AuditCallHisDto;
import com.myph.auditCall.service.AuditCallHisService;
import com.myph.common.log.MyphLogger;
import com.myph.common.redis.CacheService;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.constant.ApplyFirstReportEnum;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.controller.BaseController;
import com.myph.member.base.dto.MemberInfoDto;
import com.myph.member.base.service.MemberInfoService;
import com.myph.position.dto.OrgPositionDto;
import com.myph.common.cache.RedisRootNameSpace;
import com.myph.common.constant.Constants;

@Controller
@RequestMapping("/audit")
public class AuditCallController extends BaseController {

    @Autowired
    private ApplyUserService applyUserService;

    @Autowired
    private JobInfoService jobInfoService;

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private AuditCallHisService auditCallHisService;

    @Autowired
    private MemberInfoService memberInfoService;

    @Autowired
    private MemberJobService memberJobService;

    @Autowired
    private MemberLinkmanService memberLinkmanService;

    @Autowired
    private ApplyInfoService applyInfoService;

    @RequestMapping("/call")
    public String visitDetail(Model model, String applyLoanNo, String cType) {
        try {
            model.addAttribute("applyLoanNo", applyLoanNo);
            model.addAttribute("cType", cType);
            ServiceResult<ApplyInfoDto> applyLoanInfo = applyInfoService.queryInfoByLoanNo(applyLoanNo);
            model.addAttribute("apply", applyLoanInfo.getData());
            List<AuditCallDto> auditCallDtoList = new ArrayList<AuditCallDto>();
            // 查询本人电话
            AuditCallDto auditCallDtoMy = new AuditCallDto();
            Long memberId = selectPhoneMy(applyLoanNo, auditCallDtoMy);
            model.addAttribute("memberId", memberId);
            auditCallDtoList.add(auditCallDtoMy);
            // 查询单位电话、分机号
            AuditCallDto auditCallDtoJob = selectPhoneJob(applyLoanNo);
            if (auditCallDtoJob != null) {
                auditCallDtoList.add(auditCallDtoJob);
            }
            // 查询联系人电话
            List<AuditCallDto> auditCallDtoLinkManList = selectPhoneLinkMan(applyLoanNo);
            if (!CollectionUtils.isEmpty(auditCallDtoLinkManList)) {
                auditCallDtoList.addAll(auditCallDtoLinkManList);
            }
            // 查询电调记录
            selectAuditCallHis(applyLoanNo, auditCallDtoList);
            model.addAttribute("auditCallDtoList", auditCallDtoList);
            return "/apply/audit/audit_call";
        } catch (Exception e) {
            MyphLogger.error(e, "电调异常");
            return "error/500";
        }
    }

    @RequestMapping("/saveAuditCall")
    @ResponseBody
    public AjaxResult saveAuditCall(Model model, @RequestParam("auditCallJson") String auditCallJson,
            @RequestParam("memberId") Long memberId, @RequestParam("applyLoanNo") String applyLoanNo) {
        String operatorName = ShiroUtils.getCurrentUserName();
        Long operatorId = ShiroUtils.getCurrentUserId();
        MyphLogger.info("保存电调记录,请求参数:{},{},{},当前操作人:{},操作人编号:{}", auditCallJson, memberId, applyLoanNo, operatorName,
                operatorId);
        try {
            List<AuditCallDto> auditCallDtoList = JSONObject.parseArray(auditCallJson, AuditCallDto.class);
            // 补充ifchange,ifChangeRemark
            for (AuditCallDto auditCallDto : auditCallDtoList) {
                if (!auditCallDto.getPhone().equals(auditCallDto.getOldPhone())
                        || !auditCallDto.getPhoneExtension().equals(auditCallDto.getOldPhoneExtension())) {
                    auditCallDto.setIfChangePhone(Constants.YES_INT);
                }
            }
            // 检查变动的号码是否有重复
            String returnMsg = checkPhone(auditCallDtoList, applyLoanNo);
            if (StringUtils.isNotBlank(returnMsg)) {
                return AjaxResult.success(returnMsg);
            }

            // 检查各个号码是否有变动，有变动的更新各个表
            boolean savePhoneResult = savePhone(auditCallDtoList, memberId, applyLoanNo);

            // 检查电调记录表是否有记录，有记录更新
            boolean saveCallHisResult = saveCallHis(auditCallDtoList, applyLoanNo);

            // 操作成功保存缓存，供初审提交时校验是否有过操作
            CacheService.StringKey.set(ApplyFirstReportEnum.mobile_investigation.getKey(applyLoanNo), applyLoanNo,
                    RedisRootNameSpace.UnitEnum.ONE_MONTH);

            if (savePhoneResult && saveCallHisResult) {
                return AjaxResult.success();
            } else {
                return AjaxResult.failed("保存或更新记录失败");
            }
        } catch (Exception e) {
            MyphLogger.error(e, "保存电调记录异常,入参:{},{},{}", auditCallJson, memberId, applyLoanNo);
            return AjaxResult.failed("保存电调记录异常");
        }
    }

    @RequestMapping("/addAuditCall")
    @ResponseBody
    public AjaxResult addAuditCall(Model model, @RequestParam("auditCallJson") String auditCallJson,
            @RequestParam("applyLoanNo") String applyLoanNo) {
        String operatorName = ShiroUtils.getCurrentUserName();
        Long operatorId = ShiroUtils.getCurrentUserId();
        MyphLogger.info("新增电调记录,请求参数:{},{},当前操作人:{},操作人编号:{}", auditCallJson, applyLoanNo, operatorName, operatorId);
        try {
            List<AuditCallDto> auditCallDtoList = JSONObject.parseArray(auditCallJson, AuditCallDto.class);
            for (AuditCallDto auditCallDto : auditCallDtoList) {
                saveLinkManInfo(applyLoanNo, auditCallDto);
            }
            saveCallHis(auditCallDtoList, applyLoanNo);
            return AjaxResult.success();
        } catch (Exception e) {
            MyphLogger.error(e, "新增电调记录异常,入参:{},{}", auditCallJson, applyLoanNo);
            return AjaxResult.failed("新增电调记录异常");
        }
    }

    private void saveLinkManInfo(String applyLoanNo, AuditCallDto auditCallDto) {
        MyphLogger.info("电调新增联系人,请求参数:{},{}", applyLoanNo, auditCallDto);
        ApplyUserDto applyUser = applyUserService.queryInfoByLoanNo(applyLoanNo).getData();
        MemberInfoDto memberInfo = memberInfoService.queryInfoByIdCard(applyUser.getIdCarNo()).getData();
        Long memberId = memberInfo.getId();
        String operatorUser = ShiroUtils.getCurrentUserName();
        MemberLinkmanDto memberLinkmanDto = new MemberLinkmanDto();
        memberLinkmanDto.setMemberId(memberId);
        memberLinkmanDto.setClientType(Constants.NO_INT);
        memberLinkmanDto.setLinkManName(auditCallDto.getName());
        memberLinkmanDto.setLinkManPhone(auditCallDto.getPhone());
        memberLinkmanDto.setAlternatePhone(auditCallDto.getPhone());

        JkApplyLinkmanDto jkApplyLinkmanDto = new JkApplyLinkmanDto();
        jkApplyLinkmanDto.setApplyLoanNo(applyLoanNo);
        jkApplyLinkmanDto.setLinkManName(auditCallDto.getName());
        jkApplyLinkmanDto.setLinkManPhone(auditCallDto.getPhone());
        if (auditCallDto.getType() == AuditCallHisType.MATE.getType()) {
            jkApplyLinkmanDto.setLinkManType(LinkmanTypeEnum.FAMILY_CONTACT.getType());
            jkApplyLinkmanDto.setLinkManRelation(AuditCallHisType.MATE.getName());
            memberLinkmanDto.setLinkManType(LinkmanTypeEnum.FAMILY_CONTACT.getType());
            memberLinkmanDto.setLinkManRelation(AuditCallHisType.MATE.getName());
        }
        if (auditCallDto.getType() == AuditCallHisType.PARENT.getType()) {
            jkApplyLinkmanDto.setLinkManType(LinkmanTypeEnum.FAMILY_CONTACT.getType());
            jkApplyLinkmanDto.setLinkManRelation(AuditCallHisType.PARENT.getName());
            memberLinkmanDto.setLinkManType(LinkmanTypeEnum.FAMILY_CONTACT.getType());
            memberLinkmanDto.setLinkManRelation(AuditCallHisType.PARENT.getName());
        }
        if (auditCallDto.getType() == AuditCallHisType.CHILDREN.getType()) {
            jkApplyLinkmanDto.setLinkManType(LinkmanTypeEnum.FAMILY_CONTACT.getType());
            jkApplyLinkmanDto.setLinkManRelation(AuditCallHisType.CHILDREN.getName());
            memberLinkmanDto.setLinkManType(LinkmanTypeEnum.FAMILY_CONTACT.getType());
            memberLinkmanDto.setLinkManRelation(AuditCallHisType.CHILDREN.getName());
        }
        if (auditCallDto.getType() == AuditCallHisType.WORK.getType()) {
            jkApplyLinkmanDto.setLinkManType(LinkmanTypeEnum.WORK_CONTACT.getType());
            jkApplyLinkmanDto.setLinkManRelation(AuditCallHisType.WORK.getName());
            memberLinkmanDto.setLinkManType(LinkmanTypeEnum.WORK_CONTACT.getType());
            memberLinkmanDto.setLinkManRelation(AuditCallHisType.WORK.getName());
        }
        if (auditCallDto.getType() == AuditCallHisType.FRIEND.getType()) {
            jkApplyLinkmanDto.setLinkManType(LinkmanTypeEnum.FRIEND_CONTACT.getType());
            jkApplyLinkmanDto.setLinkManRelation(AuditCallHisType.FRIEND.getName());
            memberLinkmanDto.setLinkManType(LinkmanTypeEnum.FRIEND_CONTACT.getType());
            memberLinkmanDto.setLinkManRelation(AuditCallHisType.FRIEND.getName());
        }
        if (auditCallDto.getType() == AuditCallHisType.OTHER_MY.getType()) {
            jkApplyLinkmanDto.setLinkManType(LinkmanTypeEnum.OTHER_CONTACT.getType());
            jkApplyLinkmanDto.setLinkManRelation(AuditCallHisType.OTHER_MY.getName());
            memberLinkmanDto.setLinkManType(LinkmanTypeEnum.OTHER_CONTACT.getType());
            memberLinkmanDto.setLinkManRelation(AuditCallHisType.OTHER_MY.getName());
        }
        if (auditCallDto.getType() == AuditCallHisType.OTHER_CONTACT.getType()) {
            jkApplyLinkmanDto.setLinkManType(LinkmanTypeEnum.OTHER_CONTACT.getType());
            jkApplyLinkmanDto.setLinkManRelation(AuditCallHisType.OTHER_CONTACT.getName());
            memberLinkmanDto.setLinkManType(LinkmanTypeEnum.OTHER_CONTACT.getType());
            memberLinkmanDto.setLinkManRelation(AuditCallHisType.OTHER_CONTACT.getName());
        }

        contactsService.saveLinkman(jkApplyLinkmanDto, operatorUser);
        memberLinkmanService.saveLinkman(memberLinkmanDto, operatorUser);
    }

    /**
     * 
     * @名称 checkAddAuditCall
     * @描述 新增联系人时校验号码是否录入过
     * @返回类型 AjaxResult
     * @日期 2016年12月21日 上午9:34:42
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/checkAddAuditCall")
    @ResponseBody
    public AjaxResult checkAddAuditCall(Model model, @RequestParam("phone") String phone,
            @RequestParam("applyLoanNo") String applyLoanNo) {
        try {
            ServiceResult<ApplyInfoDto> applyInfoDtoResult = applyInfoService.queryInfoByLoanNo(applyLoanNo);
            if (phone.equals(applyInfoDtoResult.getData().getPhone())) {
                return AjaxResult.success(Constants.YES_INT);
            }
            ServiceResult<JkApplyJobDto> phoneJobResult = jobInfoService.selectJobInfoByAppNO(applyLoanNo);
            if (phoneJobResult.getData() != null) {
                if (phone.equals(phoneJobResult.getData().getUnitTelephone())) {
                    return AjaxResult.success(Constants.YES_INT);
                }
            }
            ServiceResult<Integer> result = contactsService.checkInfoByApplyLoanNoAndPhone(applyLoanNo, phone);
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "新增联系人时校验号码异常,入参:{},{}", phone, applyLoanNo);
            return AjaxResult.failed("新增联系人时校验号码异常");
        }
    }

    // 查询本人电话
    private Long selectPhoneMy(String applyLoanNo, AuditCallDto auditCallDto) {
        ServiceResult<ApplyUserDto> phoneMyResult = applyUserService.queryInfoByLoanNo(applyLoanNo);
        if (phoneMyResult.getData() != null) {
            auditCallDto.setName(phoneMyResult.getData().getMemberName());
            auditCallDto.setPhone(phoneMyResult.getData().getPhone());
            auditCallDto.setType(AuditCallHisType.MY.getType());
            auditCallDto.setTypeDescription(AuditCallHisType.MY.getName());
        }
        ServiceResult<MemberInfoDto> MemberInfoDtoResult = memberInfoService
                .queryInfoByIdCard(phoneMyResult.getData().getIdCarNo());
        Long memberId = MemberInfoDtoResult.getData().getId();
        return memberId;
    }

    // 查询工作电话
    private AuditCallDto selectPhoneJob(String applyLoanNo) {
        ServiceResult<JkApplyJobDto> phoneJobResult = jobInfoService.selectJobInfoByAppNO(applyLoanNo);
        AuditCallDto auditCallDto = null;
        if (phoneJobResult.getData() != null) {
            auditCallDto = new AuditCallDto();
            auditCallDto.setName(phoneJobResult.getData().getUnitName());
            auditCallDto.setPhone(phoneJobResult.getData().getUnitTelephone());
            auditCallDto.setPhoneExtension(phoneJobResult.getData().getExtensionNum());
            auditCallDto.setType(AuditCallHisType.JOB.getType());
            auditCallDto.setTypeDescription(AuditCallHisType.JOB.getName());
        }
        return auditCallDto;
    }

    // 查询联系人电话
    private List<AuditCallDto> selectPhoneLinkMan(String applyLoanNo) {
        ServiceResult<List<JkApplyLinkmanDto>> linkManResult = contactsService.getApplyLinkmansByAppNo(applyLoanNo);
        List<AuditCallDto> data = new ArrayList<AuditCallDto>();
        Set<AuditCallDto> result = new TreeSet<AuditCallDto>(new Comparator<AuditCallDto>() {
            @Override
            public int compare(AuditCallDto dto1, AuditCallDto dto2) {
                return dto1.getPhone().compareTo(dto2.getPhone());
            }
        });
        if (linkManResult.getData() == null) {
            return data;
        }
        for (JkApplyLinkmanDto jkApplyLinkmanDto : linkManResult.getData()) {
            AuditCallDto auditCallDto = new AuditCallDto();
            auditCallDto.setPhoneId(jkApplyLinkmanDto.getId());
            auditCallDto.setName(jkApplyLinkmanDto.getLinkManName());
            auditCallDto.setPhone(jkApplyLinkmanDto.getLinkManPhone());
            if (LinkmanTypeEnum.FAMILY_CONTACT.getType() == jkApplyLinkmanDto.getLinkManType()
                    && AuditCallHisType.MATE.getName().equals(jkApplyLinkmanDto.getLinkManRelation())) {
                auditCallDto.setType(AuditCallHisType.MATE.getType());
                auditCallDto.setTypeDescription(AuditCallHisType.MATE.getName());
                result.add(auditCallDto);
                continue;
            }
            if (LinkmanTypeEnum.FAMILY_CONTACT.getType() == jkApplyLinkmanDto.getLinkManType()
                    && AuditCallHisType.PARENT.getName().equals(jkApplyLinkmanDto.getLinkManRelation())) {
                auditCallDto.setType(AuditCallHisType.PARENT.getType());
                auditCallDto.setTypeDescription(AuditCallHisType.PARENT.getName());
                result.add(auditCallDto);
                continue;
            }
            if (LinkmanTypeEnum.FAMILY_CONTACT.getType() == jkApplyLinkmanDto.getLinkManType()
                    && AuditCallHisType.CHILDREN.getName().equals(jkApplyLinkmanDto.getLinkManRelation())) {
                auditCallDto.setType(AuditCallHisType.CHILDREN.getType());
                auditCallDto.setTypeDescription(AuditCallHisType.CHILDREN.getName());
                result.add(auditCallDto);
                continue;
            }
            if (LinkmanTypeEnum.OTHER_CONTACT.getType() == jkApplyLinkmanDto.getLinkManType()
                    && AuditCallHisType.OTHER_MY.getName().equals(jkApplyLinkmanDto.getLinkManRelation())) {
                auditCallDto.setType(AuditCallHisType.OTHER_MY.getType());
                auditCallDto.setTypeDescription(AuditCallHisType.OTHER_MY.getName());
                result.add(auditCallDto);
                continue;
            }
            if (LinkmanTypeEnum.OTHER_CONTACT.getType() == jkApplyLinkmanDto.getLinkManType()) {
                auditCallDto.setType(AuditCallHisType.OTHER_CONTACT.getType());
                auditCallDto.setTypeDescription(AuditCallHisType.OTHER_CONTACT.getName());
                result.add(auditCallDto);
                continue;
            }
            if (LinkmanTypeEnum.WORK_CONTACT.getType() == jkApplyLinkmanDto.getLinkManType()) {
                auditCallDto.setType(AuditCallHisType.WORK.getType());
                auditCallDto.setTypeDescription(AuditCallHisType.WORK.getName());
                result.add(auditCallDto);
                continue;
            }
            if (LinkmanTypeEnum.FRIEND_CONTACT.getType() == jkApplyLinkmanDto.getLinkManType()) {
                auditCallDto.setType(AuditCallHisType.FRIEND.getType());
                auditCallDto.setTypeDescription(AuditCallHisType.FRIEND.getName());
                result.add(auditCallDto);
                continue;
            }
        }
        //为避免查出重复联系人照成前台校验卡住，对联系人做去重处理。 add by wuyc 20170718
        data.addAll(result);
        return data;
    }

    // 查询电调记录
    private void selectAuditCallHis(String applyLoanNo, List<AuditCallDto> auditCallDtoList) {
        ServiceResult<List<AuditCallHisDto>> auditCallHisDtoListResult = auditCallHisService
                .selectByApplyNo(applyLoanNo);
        if (auditCallHisDtoListResult.getData() == null) {
            return;
        }
        for (AuditCallHisDto auditCallHisDto : auditCallHisDtoListResult.getData()) {
            for (AuditCallDto auditCallDto : auditCallDtoList) {
                if (auditCallDto.getPhone().equals(auditCallHisDto.getPhone())) {
                    auditCallDto.setRemark(auditCallHisDto.getRemark());
                    auditCallDto.setRemarkId(auditCallHisDto.getId());
                    break;
                }
            }
        }
    }

    /**
     * 
     * @名称 checkAndSavePhone
     * @描述 检查各个号码是否有变动，有变动的更新各个表
     * @返回类型 void
     * @日期 2016年10月8日 下午2:48:07
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    private boolean savePhone(List<AuditCallDto> auditCallDtoList, Long memberId, String applyLoanNo) {
        try {
            // 查询工作电话是否变动
            saveMemberJobPhone(auditCallDtoList, memberId, applyLoanNo);
            // 查询联系人号码是否变动
            saveLinkManPhone(auditCallDtoList, memberId, applyLoanNo);
            return true;
        } catch (Exception e) {
            MyphLogger.error(e, "更新号码异常,入参:{},{},{}", auditCallDtoList, memberId, applyLoanNo);
            return false;
        }
    }

    /**
     * 
     * @名称 checkPhone
     * @描述 检查变动的号码是否有重复
     * @返回类型 String
     * @日期 2017年1月4日 下午3:29:25
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    private String checkPhone(List<AuditCallDto> auditCallDtoList, String applyLoanNo) {
        String returnMsg = "";
        for (AuditCallDto auditCallDto : auditCallDtoList) {
            Integer ifChangePhone = auditCallDto.getIfChangePhone();
            if (ifChangePhone == Constants.YES_INT) {
                ServiceResult<ApplyInfoDto> applyLoanInfo = applyInfoService.queryInfoByLoanNo(applyLoanNo);
                ServiceResult<JkApplyJobDto> phoneJobResult = jobInfoService.selectJobInfoByAppNO(applyLoanNo);
                ServiceResult<List<JkApplyLinkmanDto>> linkManResult = contactsService
                        .getApplyLinkmansByAppNo(applyLoanNo);
                if (auditCallDto.getType() == AuditCallHisType.JOB.getType()
                        || (auditCallDto.getType() != AuditCallHisType.MY.getType()
                                && auditCallDto.getType() != AuditCallHisType.JOB.getType())) {
                    if (applyLoanInfo.getData() != null) {
                        if (auditCallDto.getPhone().equals(applyLoanInfo.getData().getPhone())) {
                            returnMsg = "号码" + auditCallDto.getPhone() + "已存在，请勿重复添加";
                            return returnMsg;
                        }
                    }
                    if (linkManResult.getData() != null) {
                        for (JkApplyLinkmanDto jkApplyLinkmanDto : linkManResult.getData()) {
                            if (auditCallDto.getPhone().equals(jkApplyLinkmanDto.getLinkManPhone())) {
                                returnMsg = "号码" + auditCallDto.getPhone() + "已存在，请勿重复添加";
                                return returnMsg;
                            }
                        }
                    }
                }
                if (auditCallDto.getType() != AuditCallHisType.MY.getType()
                        && auditCallDto.getType() != AuditCallHisType.JOB.getType()) {
                    if (phoneJobResult.getData() != null) {
                        if (auditCallDto.getPhone().equals(phoneJobResult.getData().getUnitTelephone())) {
                            returnMsg = "号码" + auditCallDto.getPhone() + "已存在，请勿重复添加";
                            return returnMsg;
                        }
                    }
                }
            }
        }
        return returnMsg;
    }

    /**
     * 
     * @名称 checkAndSaveCallHis
     * @描述 检查电调记录表是否有记录，有记录更新
     * @返回类型 void
     * @日期 2016年10月8日 下午2:48:23
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    private boolean saveCallHis(List<AuditCallDto> auditCallDtoList, String applyLoanNo) {
        try {
            ServiceResult<List<AuditCallHisDto>> auditCallHisDtoList = auditCallHisService.selectByApplyNo(applyLoanNo);
            Map<Long, AuditCallHisDto> auditCallHisDtoMap = new HashMap<Long, AuditCallHisDto>();
            for (int i = 0; i < auditCallHisDtoList.getData().size(); i++) {
                auditCallHisDtoMap.put(auditCallHisDtoList.getData().get(i).getId(),
                        auditCallHisDtoList.getData().get(i));
            }
            for (AuditCallDto auditCallDto : auditCallDtoList) {
                if(auditCallHisDtoMap.containsKey(auditCallDto.getRemarkId())){
                    AuditCallHisDto auditCallHisDto = new AuditCallHisDto();
                    auditCallHisDto.setRemark(auditCallDto.getRemark());
                    auditCallHisDto.setPhone(auditCallDto.getPhone());
                    auditCallHisDto.setId(auditCallDto.getRemarkId());
                    auditCallHisService.updateByPrimaryKeySelective(auditCallHisDto);
                }else{
                    AuditCallHisDto auditCallHisDto = new AuditCallHisDto();
                    auditCallHisDto.setApplyLoanNo(applyLoanNo);
                    auditCallHisDto.setPhone(auditCallDto.getPhone());
                    auditCallHisDto.setRemark(auditCallDto.getRemark());
                    auditCallHisDto.setType(auditCallDto.getType());
                    auditCallHisService.insertSelective(auditCallHisDto);
                }
            }
            return true;
        } catch (Exception e) {
            MyphLogger.error(e, "更新电调记录异常,入参:{},{}", auditCallDtoList, applyLoanNo);
            return false;
        }
    }

    private void saveMemberJobPhone(List<AuditCallDto> auditCallDtoList, Long memberId, String applyLoanNo) {
        Integer ifChangePhone = 0;
        String phone = "";
        String phoneExtension = "";
        for (AuditCallDto auditCallDto : auditCallDtoList) {
            if (auditCallDto.getType() == AuditCallHisType.JOB.getType()) {
                ifChangePhone = auditCallDto.getIfChangePhone();
                phone = auditCallDto.getPhone();
                phoneExtension = auditCallDto.getPhoneExtension();
                break;
            }
        }
        // 0未更改
        if (ifChangePhone == Constants.NO_INT) {
            return;
        }
        JkApplyJobDto jkApplyJobDto = new JkApplyJobDto();
        jkApplyJobDto.setApplyLoanNo(applyLoanNo);
        jkApplyJobDto.setUnitTelephone(phone);
        jkApplyJobDto.setExtensionNum(phoneExtension);
        jobInfoService.updateSelective(jkApplyJobDto);
        // 更新member_job表
        MemberJobDto memberJobDto = new MemberJobDto();
        ServiceResult<MemberJobDto> memberJobDtoResult = memberJobService.selectByMemberId(memberId);
        memberJobDto.setMemberId(memberId);
        memberJobDto.setUnitTelephone(phone);
        memberJobDto.setExtensionNum(phoneExtension);
        memberJobDto.setAlternatePhone(
                memberJobDtoResult.getData().getAlternatePhone() + "|" + phone + "," + phoneExtension);
        memberJobService.updateSelective(memberJobDto);
    }

    private void saveLinkManPhone(List<AuditCallDto> auditCallDtoList, Long memberId, String applyLoanNo) {
        for (AuditCallDto auditCallDto : auditCallDtoList) {
            if (auditCallDto.getType() == AuditCallHisType.MY.getType()
                    || auditCallDto.getType() == AuditCallHisType.JOB.getType()) {
                continue;
            }
            Integer ifChangePhone = auditCallDto.getIfChangePhone();
            if (ifChangePhone == Constants.YES_INT) {
                String phone = auditCallDto.getPhone();
                JkApplyLinkmanDto record = new JkApplyLinkmanDto();
                record.setId(auditCallDto.getPhoneId());
                record.setLinkManPhone(phone);
                contactsService.updateByIdSelective(record);
                // 更新member_linkman表
                MemberLinkmanDto memberLinkmanDto = new MemberLinkmanDto();
                ServiceResult<MemberLinkmanDto> memberLinkmanDtoResult = memberLinkmanService
                        .selectLinkmanByMemberIdAndPhone(memberId, auditCallDto.getOldPhone());
                memberLinkmanDto.setId(memberLinkmanDtoResult.getData().getId());
                memberLinkmanDto.setLinkManPhone(phone);
                memberLinkmanDto.setAlternatePhone(memberLinkmanDtoResult.getData().getAlternatePhone() + "|" + phone);
                memberLinkmanService.updateSelectiveById(memberLinkmanDto);
            }
        }
    }
}
