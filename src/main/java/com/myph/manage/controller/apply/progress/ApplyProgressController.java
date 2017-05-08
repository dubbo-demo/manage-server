package com.myph.manage.controller.apply.progress;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.apply.service.ApplyUserService;
import com.myph.applyprogress.dto.ApplyProgressDto;
import com.myph.applyprogress.dto.ApplyProgressQueryDto;
import com.myph.applyprogress.service.ApplyProgressService;
import com.myph.auditlog.dto.AuditLogDto;
import com.myph.auditlog.service.AuditLogService;
import com.myph.common.constant.SysConfigEnum;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.DateUtils;
import com.myph.common.util.SensitiveInfoUtils;
import com.myph.compliance.dto.JkComplianceLogDto;
import com.myph.compliance.service.JkComplianceLogService;
import com.myph.constant.BusinessState;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.bis.AbandonBisStateEnum;
import com.myph.constant.bis.ApplyBisStateEnum;
import com.myph.constant.bis.AuditDirectorBisStateEnum;
import com.myph.constant.bis.AuditFirstBisStateEnum;
import com.myph.constant.bis.AuditLastBisStateEnum;
import com.myph.constant.bis.AuditManagerBisStateEnum;
import com.myph.constant.bis.ContractBisStateEnum;
import com.myph.constant.bis.ExternalFirstBisStateEnum;
import com.myph.constant.bis.ExternalLastBisStateEnum;
import com.myph.constant.bis.FinanceBisStateEnum;
import com.myph.constant.bis.FinishBisStateEnum;
import com.myph.constant.bis.SignBisStateEnum;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.common.shiro.dto.EmpDetailDto;
import com.myph.manage.common.util.BeanUtils;
import com.myph.manage.controller.BaseController;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.product.service.ProductService;
import com.myph.reception.dto.ApplyReceptionDto;
import com.myph.reception.service.ApplyReceptionService;
import com.myph.sysParamConfig.service.SysParamConfigService;
import com.myph.visit.dto.VisitDetailDto;
import com.myph.visit.service.VisitService;

/**
 * 
 * @ClassName: ApplyProgressController
 * @Description: 申请进度查看
 * @author 王海波
 * @date 2016年9月21日 上午9:45:01
 *
 */
@Controller
@RequestMapping("/apply/progress")
public class ApplyProgressController extends BaseController {
    // 未选择
    public static final Integer UNSELECT = -1;

    private static final HashMap<String, BusinessState> STATE_LIST = new LinkedHashMap<String, BusinessState>();
    @Autowired
    private ApplyProgressService applyProgressService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private VisitService visitService;
    @Autowired
    private ApplyReceptionService applyReceptionService;
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private ApplyInfoService applyInfoService;
    @Autowired
    private ApplyUserService applyUserService;
    @Autowired
    private ProductService productService;
    @Autowired
    private JkComplianceLogService jkComplianceLogService;
    @Autowired
    private SysParamConfigService sysParamConfigService;

    /**
     * 初始化状态条件
     */
    static {
        List<BusinessState> states = new ArrayList<BusinessState>();
        // 进件
        states.add(ApplyBisStateEnum.INIT);
        states.add(ApplyBisStateEnum.REFUSE);
        states.add(ApplyBisStateEnum.BACK_INIT);
        // 初审
        states.add(AuditFirstBisStateEnum.INIT);
        states.add(AuditFirstBisStateEnum.BACK_INIT);
        // 复审
        states.add(AuditLastBisStateEnum.INIT);
        states.add(AuditLastBisStateEnum.REFUSE);
        states.add(AuditLastBisStateEnum.BACK_INIT);
        // 终审
        states.add(AuditManagerBisStateEnum.INIT);
        states.add(AuditManagerBisStateEnum.REFUSE);
        states.add(AuditManagerBisStateEnum.BACK_INIT);
        // 高级终审
        states.add(AuditDirectorBisStateEnum.INIT);
        states.add(AuditDirectorBisStateEnum.REFUSE);
        // 外访
        states.add(ExternalFirstBisStateEnum.INIT);
        states.add(ExternalFirstBisStateEnum.ALLOT);
        states.add(ExternalFirstBisStateEnum.REJECT);
        // 签约
        states.add(SignBisStateEnum.INIT);
        states.add(SignBisStateEnum.REJECT);
        states.add(SignBisStateEnum.BACK_INIT);
        // 合规
        states.add(ContractBisStateEnum.INIT);
        // 放款
        states.add(FinanceBisStateEnum.INIT);
        states.add(FinishBisStateEnum.INIT);
        states.add(AbandonBisStateEnum.INIT);
        for (BusinessState bs : states) {
            STATE_LIST.put(bs.getClass().getSimpleName() + "." + bs, bs);
        }
    }

    /**
     * 
     * @名称 list
     * @描述 获取申请进度列表
     * @返回类型 String
     * @日期 2016年9月22日 上午9:16:23
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    @RequestMapping("/list")
    public String list(Model model, ApplyProgressQueryDto queryDto, BasePage basePage) {
        MyphLogger.debug("开始申请进度查询：/apply/progress/list.htm|querDto=" + queryDto + "|basePage=" + basePage);
        EmployeeInfoDto user = ShiroUtils.getCurrentUser();
        EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
        model.addAttribute("user", user);
        model.addAttribute("empDetail", empDetail);

        if (null == queryDto.getStoreId()) {
            if(empDetail.getRegionId() != null){
                ServiceResult<List<OrganizationDto>> orgResult = organizationService.selectOrgByParentId(empDetail.getRegionId());
                List<Long> storeIdList = new ArrayList<Long>();
                for(int i=0;i<orgResult.getData().size();i++){
                    storeIdList.add(orgResult.getData().get(i).getId());
                }
                queryDto.setStoreIdList(StringUtils.join(storeIdList, ","));
            }
            if(empDetail.getStoreId() != null){
                queryDto.setStoreId(empDetail.getStoreId());
            }
        }
        if (null == basePage.getSortField()) {
            basePage.setSortField("createTime");
            basePage.setSortOrder("desc");
        }
        initQueryDate(queryDto);
        generateSubStateList(queryDto);// 子状态集合条件
        ServiceResult<Pagination<ApplyProgressDto>> pageResult = applyProgressService.listPageInfos(queryDto, basePage);
        List<ApplyProgressDto> list = pageResult.getData().getResult();
        for (ApplyProgressDto progress : list) {
            // 获取门店名称
            Long storeId = progress.getStoreId();
            ServiceResult<OrganizationDto> tempOrgResult = organizationService.selectOrganizationById(storeId);
            OrganizationDto tempOrg = tempOrgResult.getData();
            if (null != tempOrg) {
                progress.setStoreName(tempOrg.getOrgName());
            }
            // 获取大区名称
            Long areaId = progress.getAreaId();
            tempOrgResult = organizationService.selectOrganizationById(areaId);
            tempOrg = tempOrgResult.getData();
            if (null != tempOrg) {
                progress.setAreaName(tempOrg.getOrgName());
            }
            progress.setPhone(SensitiveInfoUtils.maskMobilePhone(progress.getPhone()));// 隐藏手机号
            progress.setMemberName(SensitiveInfoUtils.maskUserName(progress.getMemberName()));// 隐藏姓名
            
            // 获取产品名称
            if (StringUtils.isNotEmpty(progress.getContractNo())
                    || SignBisStateEnum.INIT.getCode().equals(progress.getSubState())) {
                // 获取产品名称
                String productName = productService.getProductNameById(progress.getProductType()).getData();
                progress.setProductName(productName);

            } else {
                progress.setProductName(null);
                progress.setAuditRatifyMoney(null);
            }
            
        }

        model.addAttribute("page", pageResult.getData());
        model.addAttribute("queryDto", queryDto);
        model.addAttribute("stateEnum", STATE_LIST);
        MyphLogger.debug("结束申请进度查询：/apply/progress/list.htm|page=" + pageResult);
        return "/apply/progress/progress_list";
    }

    /**
     * 
     * @名称 detailUI
     * @描述 申请进度详情
     * @返回类型 String
     * @日期 2016年9月22日 上午9:20:02
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    @RequestMapping("/detailUI")
    public String detailUI(Model model, String applyLoanNo, Integer state,Integer subState) {
        MyphLogger.info("开始申请单进度详情：/apply/progress/detailUI.htm|applyLoanNo=" + applyLoanNo + "|subState=" + subState);
        model.addAttribute("applyLoanNo", applyLoanNo);
        boolean visitVisible = false;// 外访详情可见
        boolean contractVisible = false;// 合同详情可见
        boolean loanVisible = false;// 放款详情可见
        List<VisitDetailDto> vistiList = visitService.getResultByApplyNO(applyLoanNo).getData();
        if (CollectionUtils.isNotEmpty(vistiList)) {
            visitVisible = true;
        }
        // 签约之后展示合同详情
        if (state.equals(FlowStateEnum.CONTRACT.getCode()) || state.equals(FlowStateEnum.FINANCE.getCode())
                || state.equals(FlowStateEnum.FINISH.getCode())) {
            contractVisible = true;
        }
        if (subState.equals(SignBisStateEnum.BACK_INIT.getCode()) || subState.equals(FlowStateEnum.CONTRACT.getCode())
                || subState.equals(FlowStateEnum.SIGN.getCode())) {
            contractVisible = true;
        }
        // 放款后展示放款详情
        if (state.equals(FlowStateEnum.FINISH.getCode())) {
            loanVisible = true;
        }
        model.addAttribute("subState", subState);
        model.addAttribute("visitTab", visitVisible);// 外访详情
        model.addAttribute("contractTab", contractVisible);// 合同详情
        model.addAttribute("loanTab", loanVisible);// 放款详情
        MyphLogger.info("结束申请单进度详情：/apply/progress/detailUI.htm|applyLoanNo=" + applyLoanNo + "|subState=" + subState);
        return "/apply/progress/progress_detail";
    }

    /**
     * 
     * @名称 visitDetail
     * @描述 外访详情
     * @返回类型 String
     * @日期 2016年9月22日 下午3:25:24
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    @RequestMapping("/visitDetail")
    public String visitDetail(Model model, String applyLoanNo) {
        ServiceResult<List<VisitDetailDto>> result = visitService.getResultByApplyNO(applyLoanNo);
        model.addAttribute("visitlist", result.getData());
        model.addAttribute("applyLoanNo", applyLoanNo);
        return "/apply/progress/visit_detail";
    }

    /**
     * 
     * @名称 receptionDetail
     * @描述 接待详情
     * @返回类型 String
     * @日期 2016年10月25日 下午7:15:21
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    @RequestMapping("/receptionDetail")
    public String receptionDetail(Model model, String applyLoanNo) {
        ServiceResult<ApplyReceptionDto> result = applyReceptionService.queryInfoByLoanNo(applyLoanNo);
        model.addAttribute("reception", result.getData());
        return "/apply/progress/reception_detail";
    }

    /**
     * 
     * @名称 auditDetail
     * @描述 审批记录
     * @返回类型 String
     * @日期 2016年10月25日 下午7:15:32
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    @RequestMapping("/auditDetail")
    public String auditDetail(Model model, String applyLoanNo) {
        ServiceResult<ApplyInfoDto> applyInfoRes = applyInfoService.queryInfoByAppNo(applyLoanNo);
        ApplyInfoDto applyInfo = applyInfoRes.getData();
        List<AuditLogDto> resultLogs = auditLogService.selectByApplyLoanNo(applyLoanNo).getData();
        AuditLogDto lastAduit = null;
        if (CollectionUtils.isNotEmpty(resultLogs)) {
            lastAduit = resultLogs.get(resultLogs.size() - 1);
            // 是不是流程最终状态
            Set<Integer> finalState = new HashSet<Integer>();
            finalState.add(ApplyBisStateEnum.BACK_INIT.getCode());
            finalState.add(AuditLastBisStateEnum.REFUSE.getCode());
            finalState.add(AuditManagerBisStateEnum.REFUSE.getCode());
            finalState.add(AuditDirectorBisStateEnum.REFUSE.getCode());
            finalState.add(AuditDirectorBisStateEnum.FINISH.getCode());
            if (finalState.contains(lastAduit.getAuditResult())) {
                model.addAttribute("auditInfo", lastAduit);
            } else if (AuditManagerBisStateEnum.FINISH.getCode().equals(lastAduit.getAuditResult())) {
                // 金额小于配置值 时，终审为最终阶段
                String value = sysParamConfigService.getConfigValueByName(SysConfigEnum.AUDIT_DIRECTORAUDITMONEY);
                if (lastAduit.getSuggestMoney().compareTo(new BigDecimal(value)) == -1) {
                    model.addAttribute("auditInfo", lastAduit);
                }
            }

        }
        // 把状态放到前面页面上去
        Map<String, Object> states = new HashMap<>();
        for (AuditFirstBisStateEnum e : AuditFirstBisStateEnum.values()) {
            states.put("Fisrt_" + e.name(), e.getCode());
        }
        for (AuditLastBisStateEnum e : AuditLastBisStateEnum.values()) {
            states.put("Last_" + e.name(), e.getCode());
        }
        for (AuditManagerBisStateEnum e : AuditManagerBisStateEnum.values()) {
            states.put("Manger_" + e.name(), e.getCode());
        }
        for (AuditDirectorBisStateEnum e : AuditDirectorBisStateEnum.values()) {
            states.put("Director_" + e.name(), e.getCode());
        }
        for (FlowStateEnum e : FlowStateEnum.values()) {
            states.put("Flow_" + e.name(), e.getCode());
        }
        states.put(ApplyBisStateEnum.BACK_INIT.name(), ApplyBisStateEnum.BACK_INIT.getCode());
        model.addAttribute("states", states);
        model.addAttribute("applyInfo", applyInfo);
        return "/apply/progress/audit_detail";
    }

    /**
     * 
     * @名称 complianceDetail
     * @描述 合规审批记录
     * @返回类型 String
     * @日期 2016年10月26日 下午2:41:12
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    @RequestMapping("/complianceDetail")
    public String complianceDetail(Model model, String applyLoanNo) {
        ServiceResult<List<JkComplianceLogDto>> jkComplianceDtoLogResult = jkComplianceLogService
                .selectByApplyNo(applyLoanNo);
        model.addAttribute("complianceList", jkComplianceDtoLogResult.getData());
        return "/apply/progress/compliance_detail";
    }

    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, ApplyProgressQueryDto queryDto,
            BasePage basePage) {
        MyphLogger.debug("开始申请进度导出：/apply/progress/export.htm|querDto=" + queryDto + "|basePage=" + basePage);
        try {
            generateSubStateList(queryDto);// 子状态集合条件
            // 设置参数查询满足条件的所有数据不分页
            ServiceResult<List<ApplyProgressDto>> pageResult = applyProgressService.listInfos(queryDto, basePage);
            List<ApplyProgressDto> list = pageResult.getData();
            for (ApplyProgressDto progress : list) {
                // 获取门店名称
                Long storeId = progress.getStoreId();
                ServiceResult<OrganizationDto> tempOrgResult = organizationService.selectOrganizationById(storeId);
                OrganizationDto tempOrg = tempOrgResult.getData();
                if (null != tempOrg) {
                    progress.setStoreName(tempOrg.getOrgName());
                }
                // 获取大区名称
                Long areaId = progress.getAreaId();
                tempOrgResult = organizationService.selectOrganizationById(areaId);
                tempOrg = tempOrgResult.getData();
                if (null != tempOrg) {
                    progress.setAreaName(tempOrg.getOrgName());
                }
                progress.setPhone(SensitiveInfoUtils.maskMobilePhone(progress.getPhone()));// 隐藏手机号
                progress.setMemberName(SensitiveInfoUtils.maskUserName(progress.getMemberName()));// 隐藏姓名
                
                if (StringUtils.isNotEmpty(progress.getContractNo())
                        || SignBisStateEnum.INIT.getCode().equals(progress.getSubState())) {
                    // 获取产品名称
                    String productName = productService.getProductNameById(progress.getProductType()).getData();
                    progress.setProductName(productName);

                } else {
                    progress.setProductName(null);
                    progress.setAuditRatifyMoney(null);
                }
                
                
            }

            String columnNames[] = { "序号", "申请单号", "合同号", "贷款人", "手机号", "业务经理", "大区", "门店", "进件日期", "申请金额", "批复产品",
                    "批复金额", "放款金额", "签约日期", "客服", "状态" };// 列名
            String keys[] = { "index", "applyLoanNo", "contractNo", "memberName", "phone", "bmName", "areaName",
                    "storeName", "createTime", "applyMoney", "productName", "auditRatifyMoney", "repayMoney",
                    "signTime", "customerSName", "stateDesc" };
            String fileName = "申请进度" + DateUtils.getCurrentTimeNumber();
            // 获取Excel数据
            List<Map<String, Object>> excelList = getExcelMapList(list);
            // 导出Excel数据
            exportExcel(response, fileName, columnNames, keys, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[申请进度导出：/apply/progress/export.htm]");
        }
        MyphLogger.debug("结束申请进度导出：/apply/progress/export.htm");
    }

    /**
     * 
     * @param srcList
     * @param destList
     * @param keys需转化的属性
     * @Description:dto转化为ExcelMap
     */
    private List<Map<String, Object>> getExcelMapList(List<ApplyProgressDto> srcList) {
        List<Map<String, Object>> destList = new ArrayList<Map<String, Object>>();
        if (null == srcList) {
            return destList;
        }
        int index = 1;
        for (ApplyProgressDto dto : srcList) {
            Map<String, Object> destMap = BeanUtils.transBeanToMap(dto);
            destMap.put("index", index++);
            destList.add(destMap);
        }
        return destList;
    }

    private void initQueryDate(ApplyProgressQueryDto queryDto) {
        Date today = DateUtils.getToday();
        // 初始化查询进件日期
        if (null == queryDto.getApplyTimeStart()) {
            queryDto.setApplyTimeStart(DateUtils.addWeeks(today, -2));
        }
        if (null == queryDto.getApplyTimeEnd()) {
            queryDto.setApplyTimeEnd(today);
        }
    }

    /**
     * 
     * @名称 generateSubStateList
     * @描述 根据子状态获取查询子状态集合
     * @返回类型 void
     * @日期 2016年10月8日 下午4:55:32
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    private void generateSubStateList(ApplyProgressQueryDto queryDto) {
        Integer subState = queryDto.getSubState();
        List<Integer> subStateList = new ArrayList<Integer>();
        if (null == subState || UNSELECT.equals(subState)) {
            return;
        }
        // 申请单录入
        if (ApplyBisStateEnum.INIT.getCode().equals(subState)) {
            subStateList.add(ApplyBisStateEnum.INIT.getCode());
            subStateList.add(ApplyBisStateEnum.WORKINFO.getCode());
            subStateList.add(ApplyBisStateEnum.PERSON_ASSETS.getCode());
            subStateList.add(ApplyBisStateEnum.COMPOSITE_OPINION.getCode());
            subStateList.add(ApplyBisStateEnum.LINKMAN_INPUT.getCode());
        }// 待派件
        else if (ExternalFirstBisStateEnum.INIT.getCode().equals(subState)) {
            subStateList.add(ExternalFirstBisStateEnum.INIT.getCode());
            subStateList.add(ExternalLastBisStateEnum.INIT.getCode());
        }// 待外访
        else if (ExternalFirstBisStateEnum.ALLOT.getCode().equals(subState)) {
            subStateList.add(ExternalFirstBisStateEnum.ALLOT.getCode());
            subStateList.add(ExternalLastBisStateEnum.ALLOT.getCode());
        }// 外访拒绝
        else if (ExternalFirstBisStateEnum.REJECT.getCode().equals(subState)) {
            subStateList.add(FlowStateEnum.EXTERNAL_FIRST.getCode());
            subStateList.add(FlowStateEnum.EXTERNAL_LAST.getCode());
        }// 复审拒绝
        else if (AuditLastBisStateEnum.REFUSE.getCode().equals(subState)) {
            subStateList.add(FlowStateEnum.AUDIT_LASTED.getCode());
        }// 终审拒绝
        else if (AuditManagerBisStateEnum.REFUSE.getCode().equals(subState)) {
            subStateList.add(FlowStateEnum.AUDIT_MANAGER.getCode());
        } else if (AuditDirectorBisStateEnum.REFUSE.getCode().equals(subState)) {
            subStateList.add(FlowStateEnum.AUDIT_DIRECTOR.getCode());
        }// 申请拒绝
        else if (ApplyBisStateEnum.REFUSE.getCode().equals(subState)) {
            subStateList.add(FlowStateEnum.APPLY.getCode());
        } // 签约拒绝
        else if (SignBisStateEnum.REJECT.getCode().equals(subState)) {
            subStateList.add(FlowStateEnum.SIGN.getCode());
        }// 合规拒绝
        else if (ContractBisStateEnum.REJECT.getCode().equals(subState)) {
            subStateList.add(FlowStateEnum.CONTRACT.getCode());
        }// 客户放弃
        else if (AbandonBisStateEnum.INIT.getCode().equals(subState)) {
            // 设置主流程状态为客户放弃
            queryDto.setState(FlowStateEnum.ABANDON.getCode());
        } else {
            subStateList.add(subState);
        }
        queryDto.setSubStateList(StringUtils.join(subStateList, ","));
    }

}
