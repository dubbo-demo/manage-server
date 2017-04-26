package com.myph.manage.controller.apply;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.myph.apply.constant.LinkmanTypeEnum;
import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.dto.ApplyUserDto;
import com.myph.apply.dto.JkApplyLinkmanDto;
import com.myph.apply.linkman.service.ContactsService;
import com.myph.apply.service.ApplyInfoService;
import com.myph.apply.service.ApplyUserService;
import com.myph.audit.service.JkApplyAuditService;
import com.myph.auditlog.dto.AuditLogDto;
import com.myph.auditlog.service.AuditLogService;
import com.myph.common.activemq.ActivemqUtil;
import com.myph.common.activemq.ConstantKey;
import com.myph.common.constant.Constants;
import com.myph.common.exception.DataValidateException;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.bis.ApplyBisStateEnum;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.flow.dto.RejectActionDto;
import com.myph.manage.common.constant.ClientType;
import com.myph.manage.common.constant.Constant;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.facadeService.FacadeFlowStateExchangeService;
import com.myph.manage.param.ApplyOpinionParam;
import com.myph.manage.param.ThirdBlackMqParam;
import com.myph.member.assets.dto.MemberAssetsDto;
import com.myph.member.assets.service.AssetsService;
import com.myph.member.base.dto.MemberInfoDto;
import com.myph.member.base.service.MemberInfoService;
import com.myph.member.blacklist.dto.BlackQueryDto;
import com.myph.member.blacklist.dto.InnerBlackDto;
import com.myph.member.blacklist.service.InnerBlackService;
import com.myph.mqSendLog.dto.JkMqLogDto;
import com.myph.mqSendLog.service.JkMqService;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.personassets.dto.ApplyPersonassetsDto;
import com.myph.personassets.service.ApplyPersonassetsService;
import com.myph.reception.dto.ApplyReceptionDto;
import com.myph.reception.service.ApplyReceptionService;

/**
 * 
 * @ClassName: ReceptionController
 * @Description: 申请件
 * @author heyx
 * @date 2016年9月6日 下午3:56:07
 *
 */
@Controller
@RequestMapping("/apply")
public class ApplyInfoController {

    @Autowired
    ApplyInfoService applyInfoService;

    @Autowired
    ApplyReceptionService applyReceptionService;

    @Autowired
    ApplyUserService applyUserService;

    @Autowired
    ApplyPersonassetsService applyPersonassetsService;

    @Autowired
    AssetsService assetsService;

    @Autowired
    EmployeeInfoService employeeInfoService;

    @Autowired
    MemberInfoService memberService;

    @Autowired
    ContactsService contactsService;

    @Autowired
    FacadeFlowStateExchangeService facadeFlowStateExchangeService;

    @Autowired
    JkApplyAuditService jkApplyAuditService;

    @Autowired
    InnerBlackService innerBlackService;
    @Autowired
    ActivemqUtil mqUtil;
    @Autowired
    private JkMqService jkMqService;

    @Autowired
    private OrganizationService OrganizationService;

    @Autowired
    AuditLogService auditLogService;

    /**
     * 根据团队ID加载团队信息服务
     * 
     * @param idCard
     * @return
     */
    @RequestMapping("/applyInfoQueryById/{idCard}")
    @ResponseBody
    public AjaxResult teamInfoQueryById(@PathVariable String idCard) {
        try {
            ServiceResult<List<ApplyInfoDto>> data = applyInfoService.listInfoByIdCard(idCard);
            return AjaxResult.success(data.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "加载对应身份证的信息异常,ID:{}", idCard);
            return AjaxResult.failed("加载身份证的信息异常");
        }
    }

    // 信息录入公有页面
    @RequestMapping("/newInfoIndex")
    public String newInfoIndex(Model model, String applyLoanNo) {
        MyphLogger.info("ApplyInfoInputController.applyPersonassets 输入参数{}", applyLoanNo);
        ApplyBisStateEnum[] values = ApplyBisStateEnum.values();
        model.addAttribute("constant", values);
        model.addAttribute("applyLoanNo", applyLoanNo);
        ServiceResult<ApplyInfoDto> applyInfoRes = applyInfoService.queryInfoByAppNo(applyLoanNo);
        ApplyInfoDto applyInfo = applyInfoRes.getData();
        model.addAttribute("applyInfo", applyInfo);
        return "/apply/newInfoIndex";

    }

    // 查看详情
    @RequestMapping("/applyDetailBack")
    public String applyDetailBack(Model model, String applyLoanNo) {
        MyphLogger.debug("ApplyInfoInputController.applyDetailBack 输入参数{}", applyLoanNo);
        ApplyBisStateEnum[] values = ApplyBisStateEnum.values();
        model.addAttribute("constant", values);
        ServiceResult<ApplyReceptionDto> receptiondata = applyReceptionService.queryInfoByApplyLoanNo(applyLoanNo);
        if (null != receptiondata.getData()) {
            receptiondata.getData().setAreaName(getOrgName(receptiondata.getData().getAreaId()));
            receptiondata.getData().setStoreName(getOrgName(receptiondata.getData().getStoreId()));
        }
        model.addAttribute("reception", receptiondata.getData());
        ServiceResult<ApplyUserDto> userinfodata = applyUserService.queryInfoByLoanNo(applyLoanNo);
        model.addAttribute("userinfo", userinfodata.getData());
        List<AuditLogDto> auditData = auditLogService.selectByApplyLoanNo(applyLoanNo).getData();
        model.addAttribute("auditLog", auditData == null ? null : auditData.get(auditData.size() - 1));
        model.addAttribute("applyLoanNo", applyLoanNo);
        return "/apply/detail_back";

    }

    /**
     * 
     * @名称 getOrgName
     * @描述 根据id获取组织name
     * @返回类型 String
     * @日期 2016年11月14日 下午3:05:12
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    private String getOrgName(Long id) {
        ServiceResult<OrganizationDto> oDto = OrganizationService.selectOrganizationById(id);
        if (null != oDto.getData()) {
            return oDto.getData().getOrgName();
        }
        return null;
    }

    /**
     * 
     * @名称 applyDetail
     * @描述 TODO(这里用一句话描述这个方法的作用)
     * @返回类型 String
     * @日期 2016年10月9日 下午2:45:32
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    @RequestMapping("/applyDetail")
    public String applyDetail(Model model, String applyLoanNo, String isView, String cType) {
        MyphLogger.debug("ApplyInfoInputController.applyDetail 输入参数{}", applyLoanNo);
        ServiceResult<ApplyReceptionDto> receptiondata = applyReceptionService.queryInfoByApplyLoanNo(applyLoanNo);
        if (null != receptiondata.getData()) {
            receptiondata.getData().setAreaName(getOrgName(receptiondata.getData().getAreaId()));
            receptiondata.getData().setStoreName(getOrgName(receptiondata.getData().getStoreId()));
        }
        model.addAttribute("reception", receptiondata.getData());
        ServiceResult<ApplyUserDto> userinfodata = applyUserService.queryInfoByLoanNo(applyLoanNo);
        model.addAttribute("userinfo", userinfodata.getData());
        model.addAttribute("applyLoanNo", applyLoanNo);
        if (StringUtils.isNotEmpty(isView)) {
            return "/apply/audit/common/detail_content";
        }
        model.addAttribute("cType", cType);
        return "/apply/detail";
    }

    /**
     * 
     * @名称 personassets
     * @描述 个人资产页(这里用一句话描述这个方法的作用)
     * @返回类型 String
     * @日期 2016年9月12日 下午3:46:28
     * @创建人 罗荣
     * @更新人 罗荣
     *
     */
    @RequestMapping("/newInfoIndex/personassets")
    public String personassets(@RequestParam String applyLoanNo, Model model) {
        MyphLogger.debug("ApplyInfoInputController.applyPersonassets 输入参数[]");

        ServiceResult<ApplyPersonassetsDto> result = applyPersonassetsService.getByAppNo(applyLoanNo);
        ApplyPersonassetsDto dto = null;
        if (null == result.getData()) {
            ServiceResult<ApplyInfoDto> applyInfoRes = applyInfoService.queryInfoByAppNo(applyLoanNo);
            ApplyInfoDto applyInfo = applyInfoRes.getData();
            if (null == applyInfo) {
                MyphLogger.error("申请件信息查询失败:" + applyLoanNo);
            }
            ServiceResult<MemberInfoDto> memberRs = memberService.queryInfoByIdCard(applyInfo.getIdCard());
            if (null == memberRs.getData()) {
                MyphLogger.error("没有查询到该客户的信息:" + applyInfo.getPhone());
            }
            ServiceResult<MemberAssetsDto> rs = assetsService.getAssetsByMemId(memberRs.getData().getId());
            MemberAssetsDto memberDto = rs.getData();
            if (null != memberDto) {
                dto = new ApplyPersonassetsDto();
                dto.setApplyLoanNo(applyLoanNo);
                dto.setLoanCarAmount(memberDto.getCarMortgageMoney());
                // 数量
                dto.setCarNum(memberDto.getCarAmount());
                // 按揭数量
                dto.setLoanCarNum(memberDto.getMcarAmount());

                dto.setLoanHouseNum(memberDto.getMHouseAmount());
                dto.setHouseNum(memberDto.getHouseAmount());
                dto.setLoanHouseAmount(memberDto.getHouseMortgageMoney());
            }
        } else {
            dto = result.getData();
        }
        model.addAttribute("record", dto);

        return "/apply/personassets";

    }

    /**
     * 
     * @名称 compositeOpinion
     * @描述 综合意见(这里用一句话描述这个方法的作用)
     * @返回类型 String
     * @日期 2016年9月12日 下午3:46:07
     * @创建人 罗荣
     * @更新人 罗荣
     *
     */
    @RequestMapping("/newInfoIndex/compositeOpinion")
    public String compositeOpinion(@RequestParam String applyLoanNo, Model model) {
        MyphLogger.debug("ApplyInfoInputController.compositeOpinion 输入参数[]");
        model.addAttribute("COMPOSITE_OPINION", ApplyBisStateEnum.COMPOSITE_OPINION.getCode());
        model.addAttribute("REFUSE", ApplyBisStateEnum.REFUSE.getCode());
        model.addAttribute("FINISH", ApplyBisStateEnum.FINISH.getCode());
        ServiceResult<ApplyInfoDto> applyInfoRes = applyInfoService.queryInfoByAppNo(applyLoanNo);
        ApplyInfoDto applyInfo = applyInfoRes.getData();
        model.addAttribute("record", applyInfo);
        return "/apply/compositeOpinion";

    }

    /**
     * 
     * @名称 personassetsSave
     * @描述 个人资产保存(这里用一句话描述这个方法的作用)
     * @返回类型 AjaxResult
     * @日期 2016年9月12日 下午3:47:10
     * @创建人 罗荣
     * @更新人 罗荣
     *
     */

    @RequestMapping("/newInfoIndex/personassetsSave")
    @ResponseBody
    public AjaxResult personassetsSave(@RequestBody ApplyPersonassetsDto record) {
        MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                + "】 ApplyInfoInputController.personassetsSave 输入参数[" + record + "]");

        //+++++++加入回源状态判断
        ServiceResult<Boolean> isContinue = applyInfoService.isContinueByApplyState(record.getApplyLoanNo());
        if(null != isContinue && !isContinue.getData()) {
            return AjaxResult.failed(record.getApplyLoanNo() + "已经不在申请单阶段，不能修改数据");
        }

        applyPersonassetsService.insert(record);
        MyphLogger.info(
                "操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName() + "】 个人资产数据更新成功");

        // 更新主表子状态
        applyInfoService.updateSubState(record.getApplyLoanNo(), record.getState());

        MyphLogger.info(
                "操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName() + "】 主表数据状态更新成功");

        // 拿 到 个人身份证ID去查询员工信息
        ServiceResult<ApplyInfoDto> applyInfoRes = applyInfoService.queryInfoByAppNo(record.getApplyLoanNo());
        ApplyInfoDto applyInfo = applyInfoRes.getData();
        if (null == applyInfo) {
            MyphLogger.error("申请件信息查询失败:" + record.getApplyLoanNo());
            return AjaxResult.failed("申请件信息查询失败");
        }
        ServiceResult<MemberInfoDto> memberRs = memberService.queryInfoByIdCard(applyInfo.getIdCard());
        if (null == memberRs.getData()) {
            MyphLogger.error("没有查询到该客户的信息:" + applyInfo.getPhone());
            return AjaxResult.failed("没有查询到该客户的信息");
        }

        MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                + "】 数据添加或者更新到系统表中 的个人资产");
        // 更新或者添加系统表中的个人资产表
        MemberAssetsDto dto = new MemberAssetsDto();
        dto.setMemberId(memberRs.getData().getId());
        dto.setCarMortgageMoney(record.getLoanCarAmount());
        // 数量
        dto.setCarAmount(record.getCarNum());
        // 按揭数量
        dto.setMcarAmount(record.getLoanCarNum());

        dto.setMHouseAmount(record.getLoanHouseNum());
        dto.setHouseAmount(record.getHouseNum());
        dto.setHouseMortgageMoney(record.getLoanHouseAmount());
        dto.setCreateTime(new Date());
        dto.setCreateUser(ShiroUtils.getCurrentUserName());
        dto.setModifyTime(new Date());
        dto.setModifyUser(ShiroUtils.getCurrentUserName());

        assetsService.insertOrUpdate(dto);

        MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                + "】 数据添加或者更新到系统表中 的个人资产成功");

        return AjaxResult.success();

    }

    /**
     * 
     * @名称 personassetsSave
     * @描述 综合意见提交(这里用一句话描述这个方法的作用)
     * @返回类型 AjaxResult
     * @日期 2016年9月13日 上午8:42:02
     * @创建人 罗荣
     * @更新人 罗荣
     *
     */
    @RequestMapping("/newInfoIndex/opinionSubimit")
    @ResponseBody
    public AjaxResult opinionSubimit(@RequestBody ApplyOpinionParam record) {
        MyphLogger.info("操作人[" + ShiroUtils.getCurrentUserName() + "]ApplyInfoInputController.opinionSubimit 输入参数["
                + record + "]");

        ServiceResult<ApplyInfoDto> applyInfoRes = applyInfoService.queryInfoByAppNo(record.getApplyNo());
        ApplyInfoDto applyInfo = applyInfoRes.getData();
        if (null == applyInfo) {
            MyphLogger.error("操作人[" + ShiroUtils.getCurrentUserName() + "]申请件信息查询失败:" + record.getApplyNo());
            return AjaxResult.failed("申请件信息查询失败，未查询到相关申请件！");
        }
        if (!FlowStateEnum.APPLY.getCode().equals(applyInfo.getState())) {
            return AjaxResult.success("当前阶段已经不允许提交了");
        }
        if (ApplyBisStateEnum.FINISH.getCode().equals(applyInfo.getSubState())) {
            return AjaxResult.success("申请件已提交过了！");
        }
        if (ApplyBisStateEnum.BACK_INIT.getCode().equals(applyInfo.getSubState())) {
            MyphLogger.info("操作人[" + ShiroUtils.getCurrentUserName() + "]重新提交订单[" + applyInfo + "]");
        }
        String desc = record.getDesc();
        if (StringUtils.isBlank(desc)) {
            desc = applyInfo.getApplyRemark();
        }
        
        // 验证直系联系人是否为我司黑名单的人，如果在，直接拒绝不提示。
//        ServiceResult<JkApplyLinkmanDto> linkManResult = contactsService
//                .getSingleApplyLinkman(applyInfo.getApplyLoanNo(), LinkmanTypeEnum.FAMILY_CONTACT.getType());
//        JkApplyLinkmanDto linkMan = linkManResult.getData();
//        if (null == linkMan) {
//            MyphLogger.error("操作人[" + ShiroUtils.getCurrentUserName() + "]直属亲人联系方式找不到:" + record.getApplyNo());
//            return AjaxResult.failed("直属亲人联系方式找不到！");
//        }
//        String linkManMobile = linkMan.getLinkManPhone();
//        BlackQueryDto querydto = new BlackQueryDto();
//        querydto.setPhone(linkManMobile);
//        ServiceResult<List<InnerBlackDto>> innerBlackRs = innerBlackService.listInfos(querydto);
//        if (!innerBlackRs.success()) {
//            return AjaxResult.failed("调用内部黑名单出错！");
//        }
//        if (!CollectionUtils.isEmpty(innerBlackRs.getData())) {
//            record.setState(ApplyBisStateEnum.REFUSE.getCode());
//        }
        
        applyInfo.setApplyRemark(desc);
        if (ApplyBisStateEnum.COMPOSITE_OPINION.getCode().equals(record.getSaveOrSubmit())) {
            applyInfo.setSubState(ApplyBisStateEnum.COMPOSITE_OPINION.getCode());
        } else {
            applyInfo.setSubState(record.getState());
        }
        // 请选择设置为空，不保存
        if (Constants.UNSELECT.equals(record.getState())) {
            applyInfo.setSubState(null);
        }
        applyInfo.setOpinionTime(new Date());
        applyInfoService.updateInfo(applyInfo);

        MyphLogger.info("操作人[" + ShiroUtils.getCurrentUserName() + "]更新数据主表信息[" + applyInfo + "]");

        if (Constants.UNSELECT.equals(record.getState())) {
            MyphLogger.info("操作人[" + ShiroUtils.getCurrentUserName() + "]这是保存数据，不提交！[" + applyInfo + "]");
            return AjaxResult.success();
        }
        // 如果COMPOSITE_OPINION（保存） 不是【FINISH（提交）】
        if (!ApplyBisStateEnum.COMPOSITE_OPINION.getCode().equals(record.getSaveOrSubmit())) {
            if (!ApplyBisStateEnum.REFUSE.getCode().equals(record.getState())) {
                if (!ClientType.APP.getCode().equals(applyInfo.getClientType())) {
                    applyInfo.setSubState(ApplyBisStateEnum.FINISH.getCode());
                    MyphLogger.info("操作人[" + ShiroUtils.getCurrentUserName() + "]发起MQ消息     任务字段信息[" + record + "]");
                    // 调用征信黑名单确认是否需要--发送MQ去获取报文
                    ThirdBlackMqParam param = new ThirdBlackMqParam();
                    param.setIdno(applyInfo.getIdCard());
                    param.setName(applyInfo.getMemberName());
                    param.setMobile(applyInfo.getPhone());

                    JkMqLogDto dto = new JkMqLogDto();
                    dto.setCreateTime(new Date());
                    dto.setApplyLoanNo(applyInfo.getApplyLoanNo());
                    dto.setIsReSend(Constants.YES_INT);
                    dto.setLastSendTime(new Date());
                    dto.setParam(JSONObject.toJSONString(param));
                    dto.setTime(0);
                    jkMqService.insert(dto);
                    mqUtil.sendQueue(ConstantKey.MAIYAPH_BLACKLIST_REQUEST.toString(), JSONObject.toJSONString(param));
                    MyphLogger.info("操作人[" + ShiroUtils.getCurrentUserName() + "]MQ发消息结束");
                } else {
                    // 是APP直接走通过
                    try {

                        ServiceResult<ApplyInfoDto> rs = applyInfoService.goAudit(applyInfo);
                        if (!rs.success()) {
                            MyphLogger.error("调用更新主流程失败！【{}】", rs.getMessage());
                            return AjaxResult.formatFromServiceResult(rs);
                        }
                    } catch (Exception e) {
                        MyphLogger.error("调用更新主流程失败！", e);
                        return AjaxResult.failed(e.getMessage());
                    }
                }
            } else {
                MyphLogger.info("操作人[" + ShiroUtils.getCurrentUserName() + "]走系统拒绝操作");
                // 系统拒绝
                RejectActionDto applyNotifyDto = new RejectActionDto();
                applyNotifyDto.setApplyLoanNo(applyInfo.getApplyLoanNo());
                applyNotifyDto.setOperateUser(ShiroUtils.getCurrentUserName());
                applyNotifyDto.setRejectDays(Constant.CONFINE_TIME.getCode());
                applyNotifyDto.setFlowStateEnum(FlowStateEnum.APPLY);
                // 走状态机更新主流程
                ServiceResult serviceResult = facadeFlowStateExchangeService.doAction(applyNotifyDto);
                if (!serviceResult.success()) {
                    MyphLogger.error("操作人[" + ShiroUtils.getCurrentUserName() + "]调用更新主流程失败！param【" + applyNotifyDto
                            + "】,MESSAGE:{}", serviceResult.getMessage());
                    return AjaxResult.formatFromServiceResult(serviceResult);
                }

            }
        } else {
            MyphLogger.info("操作人[" + ShiroUtils.getCurrentUserName() + "]这是保存数据，不提交！[" + applyInfo + "]");
        }

        return AjaxResult.success();

    }

}
