package com.myph.manage.controller.reception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.dto.ApplyUserDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.cityCode.dto.CityCodeDto;
import com.myph.cityCode.service.CityCodeService;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.DateUtils;
import com.myph.common.util.PingYinUtil;
import com.myph.common.util.SensitiveInfoUtils;
import com.myph.constant.StateOperateEnum;
import com.myph.employee.constants.EmployeeMsg.ORGANIZATION_TYPE;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.dto.EmployeeInputDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.idgenerator.service.IdGeneratorService;
import com.myph.manage.common.constant.ApplyOperateEnum;
import com.myph.manage.common.constant.ClientType;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.employee.dto.EmpDetailDto;
import com.myph.member.base.constant.MemberInfoServiceResultCode;
import com.myph.member.base.dto.MemberInfoDto;
import com.myph.member.base.service.MemberInfoService;
import com.myph.member.blacklist.dto.BlackQueryDto;
import com.myph.member.blacklist.service.InnerBlackService;
import com.myph.member.blacklist.service.ThirdBlackService;
import com.myph.node.dto.SysNodeDto;
import com.myph.node.service.NodeService;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.product.dto.ProductDto;
import com.myph.product.service.ProductService;
import com.myph.reception.constant.ReceptionServiceResultCode;
import com.myph.reception.dto.ApplyReceptionDto;
import com.myph.reception.service.ApplyReceptionService;

/**
 * @author heyx
 * @ClassName: ReceptionController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016年9月1日 下午2:06:05
 */
@Controller
@RequestMapping("/reception")
public class ReceptionController {

    @Autowired
    ApplyReceptionService applyReceptionService;

    @Autowired
    NodeService nodeService;

    @Autowired
    EmployeeInfoService employeeInfoService;

    @Autowired
    ApplyInfoService applyInfoService;

    @Autowired
    InnerBlackService innerBlackService;

    @Autowired
    ThirdBlackService thirdBlackService;

    @Autowired
    MemberInfoService memberInfoService;

    @Autowired
    ProductService productService;

    @Autowired
    private IdGeneratorService generatorService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    CityCodeService cityCodeService;

    private final static String remarkConfine = "该用户已经被管理员手动设置了禁闭期";

    /**
     * 列表
     *
     * @param model
     * @param
     * @param basePage
     * @return
     */
    @RequestMapping("/list")
    public String list(Model model, ApplyReceptionDto dto, BasePage basePage) {
        MyphLogger.info("接待管理列表分页查询条件", dto.toString());
        EmployeeInfoDto user = ShiroUtils.getCurrentUser();
        EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
        // 门店级别
        if (null != empDetail && ORGANIZATION_TYPE.STORE_TYPE.toNumber() == user.getOrgType()) {
            model.addAttribute("empStoreId", empDetail.getStoreId());
//            dto.setStoreId(empDetail.getStoreId());
        }
        model.addAttribute("orgType", user.getOrgType());
        model.addAttribute("regionId", empDetail.getRegionId());
        // 大区级别
//        if (null != empDetail.getRegionId() && ORGANIZATION_TYPE.REGION_TYPE.toNumber() == user.getOrgType()) {
//            dto.setAreaId(empDetail.getRegionId());
//        }
        if (null != user) {
            dto.setCustomerServiceId(user.getId());
        }
        if (null == basePage.getSortField()) {
            basePage.setSortField("updateTime");
            basePage.setSortOrder("desc");
        }
        ServiceResult<Pagination<ApplyReceptionDto>> resultInfo = applyReceptionService.listInfo(dto, basePage);
        if (resultInfo.success() && resultInfo.getData() != null) {
            for (ApplyReceptionDto dtoTemp : resultInfo.getData().getResult()) {
                ServiceResult<OrganizationDto> orgDtoResult = organizationService.selectOrganizationById(dtoTemp
                        .getStoreId());
                if (orgDtoResult.success() && orgDtoResult.getData() != null) {
                    dtoTemp.setStoreName(orgDtoResult.getData().getOrgName());
                }
            }
        }
        model.addAttribute("page", resultInfo.getData());
        model.addAttribute("queryDto", dto);
        // MyphLogger.info("接待管理列表分页查询", resultInfo.getData());
        return "/reception/applyReception_list";
    }

    /**
     * 新增跳转
     *
     * @return
     */
    @RequestMapping("/addForm")
    public String addForm() {
        return "/reception/applyReception_add";
    }

    /**
     * 修改跳转
     *
     * @return
     */
    @RequestMapping("/updateForm")
    public String updateForm(Model model, String id) {
        ServiceResult<ApplyReceptionDto> data = applyReceptionService.queryInfoById(id);
        model.addAttribute("dto", data.getData());
        return "/reception/applyReception_update";
    }

    /**
     * 根据id加载团队信息服务
     *
     * @return
     */
    @RequestMapping("/queryInfoQueryById/{id}")
    @ResponseBody
    public AjaxResult queryInfoQueryById(@PathVariable String id) {
        try {
            ServiceResult<ApplyReceptionDto> data = applyReceptionService.queryInfoById(id);
            return AjaxResult.success(data.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "加载对应id的接待信息异常,ID:{}", id);
            return AjaxResult.failed("加载id的接待信息异常");
        }
    }

    /**
     * 根据applyLoanNo加载团队信息服务
     *
     * @param applyLoanNo
     * @return
     */
    @RequestMapping("/queryInfoQueryByApplyLoanNo/{applyLoanNo}")
    @ResponseBody
    public AjaxResult queryInfoQueryByApplyLoanNo(@PathVariable String applyLoanNo) {
        try {
            ServiceResult<ApplyReceptionDto> data = applyReceptionService.queryInfoByApplyLoanNo(applyLoanNo);
            return AjaxResult.success(data.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "加载对应id的接待信息异常,ID:{}", applyLoanNo);
            return AjaxResult.failed("加载id的接待信息异常");
        }
    }

    /**
     * @名称 showProduct
     * @描述
     * @返回类型 AjaxResult
     * @日期 2016年9月7日 下午6:55:05
     * @创建人 heyx
     * @更新人 heyx
     */
    @RequestMapping("/showProduct")
    @ResponseBody
    public AjaxResult showProduct(String parentCode) {
        ServiceResult<List<SysNodeDto>> result = nodeService.getListByParent(parentCode);
        return AjaxResult.success(result.getData());
    }

    /**
     * @名称 showProduct
     * @描述
     * @返回类型 AjaxResult
     * @日期 2016年9月7日 下午6:55:05
     * @创建人 heyx
     * @更新人 heyx
     */
    @RequestMapping("/showProductName")
    @ResponseBody
    public AjaxResult showProductName(String id) {
        ServiceResult<SysNodeDto> result = new ServiceResult<SysNodeDto>();
        if (StringUtils.isNotBlank(id)) {
            result = nodeService.selectByPrimaryKey(Long.valueOf(id));
        }
        return AjaxResult.success(result.getData());
    }

    /**
     * @名称 showProduct
     * @描述
     * @返回类型 AjaxResult
     * @日期 2016年9月7日 下午6:55:05
     * @创建人 heyx
     * @更新人 heyx
     */
    @RequestMapping("/showProductNum")
    @ResponseBody
    public AjaxResult showProductNum(String productType) {
        ServiceResult<List<ProductDto>> result = productService.queryListByProdType(Long.valueOf(productType));
        return AjaxResult.success(result.getData());
    }

    /**
     * @名称 showProduct
     * @描述 获取业务经理
     * @返回类型 AjaxResult
     * @日期 2016年9月7日 下午6:55:05
     * @创建人 heyx
     * @更新人 heyx
     */
    @RequestMapping("/showBMEmpoyee")
    @ResponseBody
    public AjaxResult showBMEmpoyee(String nameSpell) {
        EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
        List<Long> ids = new ArrayList<Long>();
        ids.add(empDetail.getStoreId());
        ServiceResult<List<EmployeeInputDto>> result = employeeInfoService.queryUserInputInfoByOrgId(ids, nameSpell);
        return AjaxResult.success(result.getData());
    }

    /**
     * @名称 showProduct
     * @描述 获取客服经理
     * @返回类型 AjaxResult
     * @日期 2016年9月7日 下午6:55:05
     * @创建人 heyx
     * @更新人 heyx
     */
    @RequestMapping("/showCustomerEmpoyee")
    @ResponseBody
    public AjaxResult showCustomerEmpoyee(String nameSpell) {
        EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
        List<Long> ids = new ArrayList<Long>();
        ids.add(empDetail.getStoreId());
        ServiceResult<List<EmployeeInputDto>> result = employeeInfoService.queryUserInputInfoByOrgId(ids, nameSpell);
        return AjaxResult.success(result.getData());
    }

    /**
     * 新增
     *
     * @return
     */
    @RequestMapping("/addInfo")
    @ResponseBody
    public AjaxResult addInfo(ApplyReceptionDto applyReceptionDto, String submitType) {
        try {
            EmployeeInfoDto user = ShiroUtils.getCurrentUser();
            EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
            if (null == empDetail || null == empDetail.getCityId()) {
                return AjaxResult.failed("新增接待信息失败，请用门店账户录入!");
            }
            applyReceptionDto.setCreateUser(user.getEmployeeName());
            applyReceptionDto.setCityId(empDetail.getCityId());
            applyReceptionDto.setAreaId(empDetail.getRegionId());
            applyReceptionDto.setStoreId(empDetail.getStoreId());
            applyReceptionDto.setCustomerServiceId(user.getId());
            applyReceptionDto.setCustomerSName(user.getEmployeeName());
            applyReceptionDto.setClientType(0);
            if (StringUtils.isNotEmpty(applyReceptionDto.getMemberName())) {
                applyReceptionDto.setNameSpell(PingYinUtil.getPingYin(applyReceptionDto.getMemberName()).toUpperCase());
            }
            ServiceResult<Integer> data = null;
            CityCodeDto cityCodeDto = null;
            if (empDetail != null) {
                // 获取城市编码
                cityCodeDto = cityCodeService.selectByPrimaryKey(empDetail.getCityId()).getData();
            }
            if (null == cityCodeDto) {
                return AjaxResult.failed("城市编码不存在，请联系管理员!");
            }
            String applyNo = cityCodeDto.getCityCode() + DateUtils.getCurrentTimeNum();
            applyReceptionDto.setApplyLoanNo(generatorService.getNextVal(applyNo, 5).getData());
            // 新增提交
            if (null != submitType
                    && submitType.equals(String.valueOf(ApplyOperateEnum.RECEPTION_SUBMIT_ADD.getCode()))) {
                if (StateOperateEnum.ADOPT.getCode().equals(applyReceptionDto.getState())) {
                    ApplyUserDto applyUserDto = new ApplyUserDto();
                    BeanUtils.copyProperties(applyReceptionDto, applyUserDto);
                    applyUserDto.setIdCarNo(applyReceptionDto.getIdCard());
                    try {
                        updateMemberInfo(applyUserDto);
                    } catch (Exception e) {
                        MyphLogger.error(e, "操作用户信息异常,身份证:{},手机号:{}", SensitiveInfoUtils.maskIdCard(applyUserDto.getIdCarNo()),
                                applyUserDto.getPhone());
                        return AjaxResult.failed("身份证或手机号已经存在会员信息中!");
                    }
                }
                data = applyReceptionService.subMitInfo(applyReceptionDto);
            } else {
                applyReceptionDto.setState(0);
                // 保存提交
                data = applyReceptionService.addInfo(applyReceptionDto);
            }
            MyphLogger.info("新增接待信息 applyReceptionDto:{}", applyReceptionDto.toString());
            return AjaxResult.formatFromServiceResult(data);
        } catch (Exception e) {
            MyphLogger.error(e, "新增接待信息异常,applyReceptionDto:{}", applyReceptionDto.toString());
            return AjaxResult.failed("新增接待信息异常");
        }
    }

    private void updateMemberInfo(ApplyUserDto applyUserDto) {
        MemberInfoDto mDto = new MemberInfoDto();
        BeanUtils.copyProperties(applyUserDto, mDto);
        mDto.setModifyTime(new Date());
        mDto.setModifyUser(ShiroUtils.getCurrentUserName());
        ServiceResult<Integer> mResult = memberInfoService.updateInfoByIdCard(mDto);
        if (mResult.getCode().equals(MemberInfoServiceResultCode.UPDATE_ZERO.getCode())) {
            mDto.setMemberSource(ClientType.WEB.getCode());
            mDto.setCreateTime(new Date());
            mDto.setCreateUser(ShiroUtils.getCurrentUserName());
            memberInfoService.addInfo(mDto);
            MyphLogger.info("新增用户信息 idcard:{}", SensitiveInfoUtils.maskIdCard(mDto.getIdCarNo()));
        } else {
            MyphLogger.info("修改用户信息 idcard:{}", SensitiveInfoUtils.maskIdCard(mDto.getIdCarNo()));
        }
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping("/updateInfo")
    @ResponseBody
    public AjaxResult updateInfo(ApplyReceptionDto applyReceptionDto, String submitType) {
        MyphLogger.info("修改接待信息");
        try {
            EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
            if (null == empDetail) {
                return AjaxResult.failed("修改提交信息失败，请登录账户录入!");
            }
            if(null == empDetail.getRegionId()) {
                OrganizationDto org = organizationService.selectOrganizationById(applyReceptionDto.getStoreId()).getData();
                applyReceptionDto.setAreaId(org == null ? null : org.getParentId());
            } else {
                applyReceptionDto.setAreaId(empDetail.getRegionId());
                applyReceptionDto.setCityId(empDetail.getCityId());
            }
            applyReceptionDto.setCreateUser(ShiroUtils.getCurrentUserName());
            if (StringUtils.isNotEmpty(applyReceptionDto.getMemberName())) {
                applyReceptionDto.setNameSpell(PingYinUtil.getPingYin(applyReceptionDto.getMemberName()).toUpperCase());
            }
            ServiceResult<Integer> data = null;
            // 修改提交
            if (null != submitType
                    && submitType.equals(String.valueOf(ApplyOperateEnum.RECEPTION_SUBMIT_UPDATE.getCode()))) {
                applyReceptionDto.setUpdateTime(new Date());
                if (StateOperateEnum.ADOPT.getCode().equals(applyReceptionDto.getState())) {
                    ApplyUserDto applyUserDto = new ApplyUserDto();
                    applyUserDto.setIdCarNo(applyReceptionDto.getIdCard());
                    BeanUtils.copyProperties(applyReceptionDto, applyUserDto);
                    try {
                        updateMemberInfo(applyUserDto);
                    } catch (Exception e) {
                        MyphLogger.error(e, "操作用户信息异常,身份证:{},手机号:{}", SensitiveInfoUtils.maskIdCard(applyUserDto.getIdCarNo()),
                                applyUserDto.getPhone());
                        return AjaxResult.failed("身份证或手机号已经存在会员信息中!");
                    }
                }
                data = applyReceptionService.updateSubmitInfo(applyReceptionDto);
            } else {
                // 修改保存
                applyReceptionDto.setState(0);
                data = applyReceptionService.updateInfo(applyReceptionDto);
            }
            MyphLogger.info("修改接待信息 applyReceptionDto:{}", applyReceptionDto.toString());
            return AjaxResult.formatFromServiceResult(data);
        } catch (Exception e) {
            MyphLogger.error(e, "修改接待信息异常,applyReceptionDto:{}", applyReceptionDto.toString());
            return AjaxResult.failed("修改接待信息异常");
        }
    }

    /**
     * 修改接待信息状态
     *
     * @return
     */
    @RequestMapping("/updateState")
    @ResponseBody
    public AjaxResult updateState(ApplyReceptionDto applyReceptionDto) {
        MyphLogger.info("放弃或者拒绝接待信息", applyReceptionDto.toString());
        try {
            ServiceResult<Integer> data = applyReceptionService.updateState(applyReceptionDto);
            return AjaxResult.formatFromServiceResult(data);
        } catch (Exception e) {
            MyphLogger.error(e, "接待信息操作异常,applyReceptionDto:{}", applyReceptionDto.toString());
            return AjaxResult.failed("接待信息操作异常");
        }
    }

    /**
     * @名称 validateCardToResult
     * @描述 是否准入失败
     * @返回类型 boolean
     * @日期 2016年9月8日 下午6:40:49
     * @创建人 heyx
     * @更新人 heyx
     */
    @RequestMapping("/validateCardToResult")
    @ResponseBody
    public AjaxResult validateCardToResult(String idCard) {
        MyphLogger.info("开始准入 idcard:{}", SensitiveInfoUtils.maskIdCard(idCard));
        try {
            ServiceResult<Integer> data = validateReuslt(idCard);
            MyphLogger.info("结束准入 idcard:{}", SensitiveInfoUtils.maskIdCard(idCard));
            return AjaxResult.formatFromServiceResult(data);
        } catch (Exception e) {
            MyphLogger.error(e, "准入信息异常,idCard:{}", SensitiveInfoUtils.maskIdCard(idCard));
            return AjaxResult.failed("准入信息异常");
        }
    }

    private ServiceResult<Integer> validateReuslt(String idCard) {
        ServiceResult<Integer> data = ServiceResult.newSuccess();
        BlackQueryDto querydto = new BlackQueryDto();
        querydto.setIdCard(idCard);
        ApplyInfoDto applyInfo = applyInfoService.queryCountByIdCard(idCard).getData();
        // 内部黑名单，第三方黑名单
        if (null != applyInfo) {
            MyphLogger.info("存在正在申请中的单子 applyLoanNo:{}", applyInfo.getApplyLoanNo());
            data = ServiceResult.newByServiceResultCode(ReceptionServiceResultCode.HAVE_INFO);
            return data;
        } else {
            MyphLogger.info("不存在正在申请中的单子");
        }
        if (!CollectionUtils.isEmpty(innerBlackService.listInfos(querydto).getData())) {
            MyphLogger.info("该身份证在内部黑名单");
            data = ServiceResult.newByServiceResultCode(ReceptionServiceResultCode.INNER_BLACK);
            return data;
        } else {
            MyphLogger.info("该身份证不在内部黑名单");
        }
        if (!CollectionUtils.isEmpty(thirdBlackService.listInfos(querydto).getData())) {
            MyphLogger.info("该身份证在第三方黑名单");
            data = ServiceResult.newByServiceResultCode(ReceptionServiceResultCode.THIRD_BLACK);
            return data;
        } else {
            MyphLogger.info("该身份证不在第三方黑名单");
        }
        String configRemark = isConfineTime(idCard);
        if (null != configRemark) {
            MyphLogger.info(configRemark);
            // 在禁闭期
            data = ServiceResult.newFailure(configRemark);
        } else {
            MyphLogger.info("该身份证不在禁闭期内");
        }
        return data;
    }

    /**
     * @名称 isConfineTime
     * @描述 是否在禁闭期
     * @返回类型 boolean
     * @日期 2016年9月12日 下午5:04:08
     * @创建人 heyx
     * @更新人 heyx
     */
    private String isConfineTime(String idCard) {
        ServiceResult<MemberInfoDto> mResult = memberInfoService.queryInfoByIdCard(idCard);
        if (mResult.getCode() == 0) {
            MemberInfoDto dto = mResult.getData();
            if (null == dto.getConfineTime()) {
                return null;
            }
            if (dto.getConfineTime().getTime() < new Date().getTime()) {
                // 过了禁闭期
                return null;
            } else {
                return StringUtils.isEmpty(mResult.getData().getConfineRemark())? remarkConfine : mResult.getData().getConfineRemark();
            }
        }
        return null;
    }
}
