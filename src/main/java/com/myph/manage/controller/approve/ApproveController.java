/**   
 * @Title: ApproveController.java 
 * @Package: com.myph.manage.controller.approve 
 * @company: 麦芽金服
 * @Description: 审批管理(用一句话描述该文件做什么) 
 * @author 罗荣   
 * @date 2016年9月18日 下午5:20:53 
 * @version V1.0   
 */
package com.myph.manage.controller.approve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myph.allot.dto.AllotLogDto;
import com.myph.allot.service.AllotService;
import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.dto.ApproveDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.applyprogress.dto.ApplyProgressQueryDto;
import com.myph.approvetask.dto.ApproveParamDto;
import com.myph.approvetask.dto.ApproveTaskDto;
import com.myph.approvetask.service.ApproveTaskService;
import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.DateUtils;
import com.myph.common.util.SensitiveInfoUtils;
import com.myph.constant.ApplyUtils;
import com.myph.constant.BusinessState;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.PositionEnum;
import com.myph.constant.StateListUtils;
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
import com.myph.employee.dto.EmployeeDetailDto;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.dto.EmployeeInputDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.product.service.ProductService;

/**
 * @ClassName: ApproveController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 罗荣
 * @date 2016年9月18日 下午5:20:53
 * 
 */
@Controller
@RequestMapping("/approve")
public class ApproveController {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ProductService proService;
    @Autowired
    private ApproveTaskService approveService;
    @Autowired
    private ApplyInfoService applyInfoService;

    @Autowired
    private EmployeeInfoService employeeInfoService;

    @Autowired
    private AllotService allotService;

    public final static String PATH = "/apply/approve";

    public final static String error = "error/500";
        
    // 未选择
    public static final Integer UNSELECT = -1;
    
    @RequestMapping("/queryPageList")
    public String queryPageList(ApproveParamDto page, Model model) {
        if (Constants.UNSELECT_LONG.equals(page.getAreaId())) {
            page.setAreaId(null);
        }
        if (Constants.UNSELECT_LONG.equals(page.getProId())) {
            page.setProId(null);
        }
        if (Constants.UNSELECT_LONG.equals(page.getProductType())) {
            page.setProductType(null);
        }
        if (Constants.UNSELECT_LONG.equals(page.getStoreId())) {
            page.setStoreId(null);
        }
        if (Constants.UNSELECT.equals(page.getIsAssigned())) {
            page.setIsAssigned(null);
        }
        if (null == page.getSubmitDates() && null == page.getSubmitDatee()) {
            page.setSubmitDatee(DateUtils.getToday());
            page.setSubmitDates(DateUtils.addDays(DateUtils.getToday(), -14));
        }
        generateSubStateList(page);
        MyphLogger.info("查询申请件列表开始");
        //根据teamId获取人员
        ServiceResult<List<EmployeeDetailDto>> employeeResult = employeeInfoService.queryEmployeeInfoByTeamId(page.getTeamId());
        List<Long> employeeIds = new ArrayList<Long>();
        for(EmployeeDetailDto dto:employeeResult.getData()){
            employeeIds.add(dto.getId());
        }
        page.setEmployeeIdList(StringUtils.join(employeeIds, ","));
        ServiceResult<Pagination<ApproveDto>> list = approveService.queryPageList(page);
        MyphLogger.info("查询申请件列表结束");

        MyphLogger.info("申请件列表：" + list);

        MyphLogger.info("数据替换");
        for (ApproveDto e : list.getData().getResult()) {
        	MyphLogger.info("前数据："+e);
            // 1、补充门店名称
            ServiceResult<OrganizationDto> storeResult = organizationService.selectOrganizationById(e.getStoreId());
            if(null != storeResult.getData()){
                e.setStoreName(storeResult.getData().getOrgName());
            }
            // 2、补充大区名称
            ServiceResult<OrganizationDto> areaResult = organizationService.selectOrganizationById(e.getAreaId());
            if(null != areaResult.getData()){
                e.setAreaName(areaResult.getData().getOrgName());
            }
            // 3、补充产品名称
            ServiceResult<String> result = proService.getProductNameById(e.getProId());
            e.setProName(result.getData());

            // 隐藏客户姓名与身份证号
            String idCardNum = e.getIdCard();
            idCardNum = SensitiveInfoUtils.maskIdCard(idCardNum);
            e.setIdCard(idCardNum);
            String userName = e.getMemberName();
            userName = SensitiveInfoUtils.maskUserName(userName);
            e.setMemberName(userName);

            // 设置状态名称
            String stateName = ApplyUtils.getFullStateDesc(e.getState(), e.getSubState());
            e.setStateName(stateName);
            
            ServiceResult<EmployeeInfoDto> emplyeeResult = employeeInfoService
                    .getEntityById(e.getFirstApproveUserId());
            if(emplyeeResult.success()&&null !=  emplyeeResult.getData()){
                e.setFirstAuditorName(emplyeeResult.getData().getEmployeeName());
            }
            emplyeeResult = employeeInfoService
                    .getEntityById(e.getReviewAuditorUserId());
            if(emplyeeResult.success()&&null !=  emplyeeResult.getData()){
                e.setReviewAuditorName((emplyeeResult.getData().getEmployeeName()));
            }
            emplyeeResult = employeeInfoService
                    .getEntityById(e.getLastAuditorUserId());
            if(emplyeeResult.success()&&null !=  emplyeeResult.getData()){
                e.setLastAuditorName((emplyeeResult.getData().getEmployeeName()));
            }
            emplyeeResult = employeeInfoService
                    .getEntityById(e.getSuperLastAuditorUserId());
            if(emplyeeResult.success()&&null !=  emplyeeResult.getData()){
                e.setSuperLastAuditorName((emplyeeResult.getData().getEmployeeName()));
            }
            MyphLogger.info("后数据："+e);
        }
        MyphLogger.info("数据替换结束");

        model.addAttribute("params", page);
        model.addAttribute("page", list.getData());
        model.addAttribute("stateEnum", StateListUtils.getStateList());
        return PATH + "/list";
    }

    @RequestMapping("/toApprove")
    public String approve(String applyLoanNo, Integer cType, RedirectAttributes attr, Model model) {
        MyphLogger.info("操作人["+ShiroUtils.getCurrentUserName()+"]页面跳转分发！");
        ServiceResult<ApplyInfoDto> resultApplyInfo = applyInfoService.queryInfoByLoanNo(applyLoanNo);
        ApplyInfoDto applyInfo = resultApplyInfo.getData();
        if (null == applyInfo) {
            MyphLogger.error("操作人["+ShiroUtils.getCurrentUserName()+"]查询申请件失败【" + applyLoanNo + "】");
            return error;
        }
        attr.addAttribute("applyLoanNo", applyLoanNo);
        if (null != cType && cType.equals(1)) {
            MyphLogger.info("操作人["+ShiroUtils.getCurrentUserName()+"]跳转到查看页面");
            attr.addAttribute("cType", cType);
            return "redirect:/lastApprove/approveDeal.htm";
        }
        if (FlowStateEnum.AUDIT_FIRST.getCode().equals(applyInfo.getState())) {
            return "redirect:/firstApprove/approveDeal.htm";
        } else if (FlowStateEnum.AUDIT_LASTED.getCode().equals(applyInfo.getState())) {
            attr.addAttribute("cType", FlowStateEnum.AUDIT_LASTED.getCode());
            return "redirect:/lastApprove/approveDeal.htm";
        }else if (FlowStateEnum.AUDIT_MANAGER.getCode().equals(applyInfo.getState())) {
            return "redirect:/dealApply/approveDeal.htm";
        }else if (FlowStateEnum.AUDIT_DIRECTOR.getCode().equals(applyInfo.getState())) {
            return "redirect:/dealApply/approveDeal.htm";
        } else if (ApplyBisStateEnum.BACK_INIT.getCode().equals(applyInfo.getSubState())) {
            return "redirect:/firstApprove/approveDeal.htm";
        } else {
            attr.addAttribute("cType", "1");
            return "redirect:/lastApprove/approveDeal.htm";
        }
    }

    @RequestMapping("/editApproveOpreator")
    public String editApproveOpreator(String applyLoanNo, Model model) {

        ServiceResult<ApplyInfoDto> resultApplyInfo = applyInfoService.queryInfoByLoanNo(applyLoanNo);
        ApplyInfoDto applyInfo = resultApplyInfo.getData();
        if (null == applyInfo) {
            MyphLogger.error("操作人["+ShiroUtils.getCurrentUserName()+"]查询申请件失败【" + applyLoanNo + "】");
            return error;
        }
        ServiceResult<ApproveTaskDto> resultApproveDto = approveService.selectByApplyLoanNo(applyLoanNo);
        ApproveTaskDto approveDto = resultApproveDto.getData();
        if (null == approveDto) {
            MyphLogger.error("操作人["+ShiroUtils.getCurrentUserName()+"]查询申请件未提审【" + applyLoanNo + "】");
            return error;
        }
        // 1、补充门店名称
        ServiceResult<OrganizationDto> storeResult = organizationService.selectOrganizationById(applyInfo.getStoreId());
        applyInfo.setStoreName(storeResult.getData().getOrgName());
        // 2、补充大区名称
        ServiceResult<OrganizationDto> areaResult = organizationService.selectOrganizationById(applyInfo.getAreaId());
        applyInfo.setAreaName(areaResult.getData().getOrgName());
        // 3、补充产品名称
        ServiceResult<String> result = proService.getProductNameById(applyInfo.getProductType());
        applyInfo.setProductName(result.getData());
        // 设置状态名称
        // 状态名称先获取初审，然后再获取终审，有初审，才能有终审
        String stateName = ApplyUtils.getAuaditStateDesc(approveDto.getAuditState());

        String approveUser = null;
        EmployeeInfoDto emplyee = null;
        // 始审获取对应审核人
        if (null != approveDto.getFirstAuditor()&&FlowStateEnum.AUDIT_FIRST.getCode().equals(approveDto.getAuditResult())) {
            ServiceResult<EmployeeInfoDto> emplyeeResult = employeeInfoService
                    .getEntityById(approveDto.getFirstAuditor());
            emplyee = emplyeeResult.getData();
            if (null == emplyee) {
                MyphLogger.error("操作人["+ShiroUtils.getCurrentUserName()+"]查询初审批人失败【" + approveDto.getFirstAuditor() + "】");
                return error;
            }
            approveUser = emplyee.getEmployeeName();
        }
        // 复审获取对应审核人
        if (null != approveDto.getReviewAuditor()
                && FlowStateEnum.AUDIT_LASTED.getCode().equals(approveDto.getAuditResult())) {
            ServiceResult<EmployeeInfoDto> emplyeeLastResult = employeeInfoService
                    .getEntityById(approveDto.getReviewAuditor());
            emplyee = emplyeeLastResult.getData();
            if (null == emplyee) {
                MyphLogger.error("操作人["+ShiroUtils.getCurrentUserName()+"]查询复审批人失败【" + approveDto.getReviewAuditor() + "】");
                return error;
            }
            approveUser = emplyee.getEmployeeName();
        }
        // 终审
        if (null != approveDto.getLastAuditor()
                && FlowStateEnum.AUDIT_MANAGER.getCode().equals(approveDto.getAuditResult())) {
            ServiceResult<EmployeeInfoDto> emplyeeLastResult = employeeInfoService
                    .getEntityById( approveDto.getLastAuditor());
            emplyee = emplyeeLastResult.getData();
            if (null == emplyee) {
                MyphLogger.error("操作人["+ShiroUtils.getCurrentUserName()+"]查询终审批人失败【" + approveDto.getReviewAuditor() + "】");
                return error;
            }
            approveUser = emplyee.getEmployeeName();
        }
        // 超级终审
        if (null != approveDto.getSuperLastAuditor()
                && FlowStateEnum.AUDIT_DIRECTOR.getCode().equals(approveDto.getAuditResult())) {
            ServiceResult<EmployeeInfoDto> emplyeeLastResult = employeeInfoService
                    .getEntityById(approveDto.getSuperLastAuditor());
            emplyee = emplyeeLastResult.getData();
            if (null == emplyee) {
                MyphLogger.error("操作人["+ShiroUtils.getCurrentUserName()+"]查询超强终审批人失败【" + approveDto.getReviewAuditor() + "】");
                return error;
            }
            approveUser = emplyee.getEmployeeName();
        }
//        if (null == emplyee) {
//            MyphLogger.error("操作人["+ShiroUtils.getCurrentUserName()+"]当前初审单未取件【" + applyLoanNo + "】");
//            return error;
//        }
        approveDto.setAuditStateName(stateName);
        model.addAttribute("applyLoanInfo", applyInfo);
        model.addAttribute("applyApproveTask", approveDto);
        model.addAttribute("approveUser", approveUser);
        model.addAttribute("emplyee", emplyee);
        MyphLogger.debug("查询结果：" + model.toString());
        return PATH + "/edit_approve_opreator";
    }

    // searchApproveUser
    @RequestMapping("/searchApproveUser")
    @ResponseBody
    public AjaxResult searchApproveUser(Integer state, String searchWord, Model model) {

        EmployeeInfoDto old = new EmployeeInfoDto();
        List<String> positionList = new ArrayList<String>();
        //初审
        if(state.equals(FlowStateEnum.AUDIT_FIRST.getCode())){
            positionList.add(PositionEnum.POSITION_AUDIT_FIRST.getCode());
        }
        //复审
        if(state.equals(FlowStateEnum.AUDIT_LASTED.getCode())){
            positionList.add(PositionEnum.POSITION_AUDIT_LASTED.getCode());
        }
        //终审
        if(state.equals(FlowStateEnum.AUDIT_MANAGER.getCode())){
            positionList.add(PositionEnum.POSITION_AUDIT_MANAGER.getCode());
        }
        //高级终审
        if(state.equals(FlowStateEnum.AUDIT_DIRECTOR.getCode())){
            positionList.add(PositionEnum.POSITION_AUDIT_DEPUTY_DIRECTOR.getCode());
            positionList.add(PositionEnum.POSITION_AUDIT_DIRECTOR.getCode());
        }
        
        ServiceResult<List<EmployeeInputDto>> result = employeeInfoService.queryUserByoldInfo(positionList, searchWord);

        return AjaxResult.success(result.getData());
    }

    // 审批人修改
    @RequestMapping("/saveApproveUser")
    @ResponseBody
    public AjaxResult saveApproveUser(Long newApproveUser, Long oldApproveUser, String applyLoanNo, Model model) {

        ServiceResult<Integer> rs = approveService.updateApproveUser(newApproveUser, applyLoanNo);
        AllotLogDto dto = new AllotLogDto();
        ServiceResult<ApproveTaskDto> taskInfoRs = approveService.selectByApplyLoanNo(applyLoanNo);
        if (!taskInfoRs.success()) {
            return AjaxResult.formatFromServiceResult(taskInfoRs);
        }
        ServiceResult<EmployeeInfoDto> emplyeeLastResult = employeeInfoService.getEntityById(newApproveUser);
        EmployeeInfoDto emplyee = emplyeeLastResult.getData();
        if (null == emplyee) {
            MyphLogger.error("操作人["+ShiroUtils.getCurrentUserName()+"]查询终审批人失败【" + newApproveUser + "】");
            return AjaxResult.failed("查询终审批人失败【" + newApproveUser + "】");
        }

        // 插入日志
        dto.setApplyLoanNo(applyLoanNo);
        dto.setAuditStage(taskInfoRs.getData().getAuditState());
        dto.setNewAuditUser(emplyee.getEmployeeName());
        emplyeeLastResult = employeeInfoService.getEntityById(oldApproveUser);
        emplyee = emplyeeLastResult.getData();
        if (null == emplyee) {
            MyphLogger.error("操作人["+ShiroUtils.getCurrentUserName()+"]查询老审批人失败【" + oldApproveUser + "】");
            return AjaxResult.failed("查询老审批人失败【" + oldApproveUser + "】");
        }
        dto.setOldAuditUser(emplyee.getEmployeeName());
        allotService.insertLog(dto);
        return AjaxResult.success(rs.getData());
    }
    
    private void generateSubStateList(ApproveParamDto page) {
        Integer subState = page.getSubState();
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
            page.setState(FlowStateEnum.ABANDON.getCode());
        } else {
            subStateList.add(subState);
        }
        page.setSubStateList(StringUtils.join(subStateList, ","));
    }
}