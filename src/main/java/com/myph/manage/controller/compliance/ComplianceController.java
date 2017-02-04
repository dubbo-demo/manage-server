
package com.myph.manage.controller.compliance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.dto.ComplianceDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.apply.service.ComplianceService;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.DateUtils;
import com.myph.compliance.constant.ComplianceResultEnum;
import com.myph.compliance.dto.JkComplianceDto;
import com.myph.compliance.dto.JkComplianceLogDto;
import com.myph.compliance.service.JkComplianceLogService;
import com.myph.compliance.service.JkComplianceService;
import com.myph.constant.ApplyUtils;
import com.myph.constant.ErrMessageEnum;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.bis.SignBisStateEnum;
import com.myph.flow.dto.ContinueActionDto;
import com.myph.flow.dto.FallbackActionDto;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.controller.BaseController;
import com.myph.manage.facadeService.FacadeFlowStateExchangeService;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.product.service.ProductService;
import com.myph.sign.dto.JkSignDto;
import com.myph.sign.service.SignService;

@Controller
@RequestMapping("/compliance")
public class ComplianceController extends BaseController {

    @Autowired
    private ComplianceService complianceService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ProductService productService;

    @Autowired
    private JkComplianceService jkComplianceService;

    @Autowired
    private JkComplianceLogService jkComplianceLogService;

    @Autowired
    private FacadeFlowStateExchangeService facadeFlowStateExchangeService;

    @Autowired
    private SignService signService;

    @Autowired
    private ApplyInfoService applyInfoService;

    @RequestMapping("/list")
    public String list(Model model, ComplianceDto complianceDto, BasePage basePage) {
        try {
            if (StringUtils.isBlank(complianceDto.getProgress())) {
                complianceDto.setProgress("todo");
            }
            // 根据产品类型查询相关产品ID
            if (complianceDto.getProductType() != null) {
                ServiceResult<List<Long>> listProductIdResult = productService
                        .selectIdByType(complianceDto.getProductType());
                List<Long> listProductId = listProductIdResult.getData();
                if (listProductId.size() > 0) {
                    String stringProductId = StringUtils.join(listProductId, ",");
                    complianceDto.setStringProductId(stringProductId);
                }
            }
            // 补充默认签约日期
            if (null == complianceDto.getSignTimeDates()) {
                Date today = DateUtils.getToday();
                complianceDto.setSignTimeDates(DateUtils.addWeeks(today, -3));
            }
            if (null == complianceDto.getSignTimeDatee()) {
                complianceDto.setSignTimeDatee(new Date());
            }
            // 默认只查当前用户的，管理员用户可看全部
            complianceDto.setIsManager(ShiroUtils.getAccountType());
            complianceDto.setAuditor(ShiroUtils.getCurrentUserName());
            ServiceResult<Pagination<ComplianceDto>> result = complianceService.getJkApplyCompliance(complianceDto,
                    basePage);
            Pagination<ComplianceDto> page = result.getData();
            List<ComplianceDto> compliances = result.getData().getResult();
            for (ComplianceDto ComplianceDto : compliances) {
                // 1、补充大区名称
                ServiceResult<OrganizationDto> areaResult = organizationService
                        .selectOrganizationById(ComplianceDto.getAreaId());
                if (areaResult.success() && areaResult.getData() != null) {
                    ComplianceDto.setAreaName(areaResult.getData().getOrgName());
                }
                // 2、补充门店名称
                ServiceResult<OrganizationDto> orgDtoResult = organizationService
                        .selectOrganizationById(ComplianceDto.getStoreId());
                if (orgDtoResult.success() && orgDtoResult.getData() != null) {
                    ComplianceDto.setStoreName(orgDtoResult.getData().getOrgName());
                }
                // 3、补充产品名称
                ServiceResult<String> productName = productService.getProductNameById(ComplianceDto.getProductId());
                ComplianceDto.setProductName(productName.getData());
                // 4、补充状态名称
                String stateName = ApplyUtils.getFullStateDesc(ComplianceDto.getState(), ComplianceDto.getSubState());
                ComplianceDto.setStateName(stateName);
            }
            page.setResult(compliances);
            model.addAttribute("compliance", complianceDto);
            model.addAttribute("page", page);
            model.addAttribute("progress", complianceDto.getProgress());
            return "/compliance/compliance_list";
        } catch (Exception e) {
            MyphLogger.error(e, "合规异常，入参{}", complianceDto.toString());
            return "error/500";
        }
    }

    /**
     * 
     * @名称 doCompliance
     * @描述 合规管理展示页面 cType为0只读 为1可编辑
     * @返回类型 String
     * @日期 2016年10月24日 下午4:58:39
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/doCompliance")
    public String doCompliance(Model model, @RequestParam("applyLoanNo") String applyLoanNo, String cType) {
        try {
            List<JkComplianceLogDto> jkComplianceLogDtoList = new ArrayList<JkComplianceLogDto>();
            if (StringUtils.isNotBlank(applyLoanNo)) {
                ServiceResult<List<JkComplianceLogDto>> jkComplianceDtoLogResult = jkComplianceLogService
                        .selectByApplyNo(applyLoanNo);
                jkComplianceLogDtoList = jkComplianceDtoLogResult.getData();
            }
            model.addAttribute("complianceList", jkComplianceLogDtoList);
            model.addAttribute("applyLoanNo", applyLoanNo);
            model.addAttribute("cType", cType);
            return "/compliance/compliance_do";
        } catch (Exception e) {
            MyphLogger.error(e, "合规管理展示异常,入参{}", applyLoanNo);
            return "error/500";
        }
    }

    @RequestMapping("/saveCompliance")
    @ResponseBody
    public AjaxResult saveCompliance(Model model, JkComplianceDto dto) {
        try {
            String operatorName = ShiroUtils.getCurrentUserName();
            Long operatorId = ShiroUtils.getCurrentUserId();
            MyphLogger.info("保存合规信息,请求参数:{},当前操作人:{},操作人编号:{}", dto, operatorName, operatorId);
            // 调用状态机更新主状态
            if (dto.getResult().equals(ComplianceResultEnum.PASS.getType())) {
                ContinueActionDto applyNotifyDto = new ContinueActionDto();
                applyNotifyDto.setApplyLoanNo(dto.getApplyLoanNo());
                applyNotifyDto.setOperateUser(ShiroUtils.getCurrentUserName());
                applyNotifyDto.setFlowStateEnum(FlowStateEnum.CONTRACT);
                // 获取合同号，供后续流程使用
                ServiceResult<ApplyInfoDto> applyInfoDtoResult = applyInfoService
                        .queryInfoByAppNo(dto.getApplyLoanNo());
                JSONObject doc = new JSONObject();
                doc.put("contractNo", applyInfoDtoResult.getData().getContractNo());
                applyNotifyDto.setDoc(doc);
                ServiceResult serviceResult = facadeFlowStateExchangeService.doAction(applyNotifyDto);
                if (!serviceResult.success()) {
                    MyphLogger.error("调用更新主流程失败！param【" + applyNotifyDto + "】,MESSAGE:{}", serviceResult.getMessage());
                    return AjaxResult.failed(ErrMessageEnum.COMPLIANCE_ERR.getCode());
                }
            } else {
                FallbackActionDto fallbackActionDto = new FallbackActionDto();
                fallbackActionDto.setApplyLoanNo(dto.getApplyLoanNo());
                fallbackActionDto.setFlowStateEnum(FlowStateEnum.CONTRACT);
                fallbackActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
                fallbackActionDto.setPublicRemark(dto.getRemark());
                ServiceResult<JkSignDto> jkSignDtoResult = signService.selectByApplyLoanNo(dto.getApplyLoanNo());
                JkSignDto jkSignDto = jkSignDtoResult.getData();
                if (jkSignDto != null) {
                    fallbackActionDto.setRecptUserId(jkSignDto.getCreateUserId());
                }
                // 更新子状态为合规回退
                applyInfoService.updateSubState(dto.getApplyLoanNo(), SignBisStateEnum.BACK_INIT.getCode());
                ServiceResult serviceResult = facadeFlowStateExchangeService.doAction(fallbackActionDto);
                if (!serviceResult.success()) {
                    MyphLogger.error("调用更新主流程失败！param【" + fallbackActionDto + "】,MESSAGE:{}",
                            serviceResult.getMessage());
                    return AjaxResult.failed(ErrMessageEnum.COMPLIANCE_ERR.getCode());
                }
            }

            // 查询是否有记录，有记录则更新，无记录插入
            ServiceResult<JkComplianceDto> selectResult = jkComplianceService.selectByApplyNo(dto.getApplyLoanNo());
            if (selectResult.getData() != null && StringUtils.isNotBlank(selectResult.getData().getApplyLoanNo())) {
                JkComplianceDto record = new JkComplianceDto();
                record.setId(selectResult.getData().getId());
                record.setRemark(dto.getRemark());
                record.setResult(dto.getResult());
                record.setCreateUser(ShiroUtils.getCurrentUserName());
                record.setApplyLoanNo(dto.getApplyLoanNo());
                jkComplianceService.updateByPrimaryKeySelective(record);
            } else {
                dto.setCreateUser(ShiroUtils.getCurrentUserName());
                jkComplianceService.insertSelective(dto);
            }

            return AjaxResult.success();
        } catch (Exception e) {
            MyphLogger.error(e, "保存合规信息异常,入参:{}", dto.toString());
            return AjaxResult.failed("保存合规信息异常");
        }
    }

}
