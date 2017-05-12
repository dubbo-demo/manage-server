/**   
 * @Title: ApproveController.java 
 * @Package: com.myph.manage.controller.approve 
 * @company: 麦芽金服
 * @Description: 审批管理(用一句话描述该文件做什么) 
 * @author 罗荣   
 * @date 2016年9月18日 下午5:20:53 
 * @version V1.0   
 */
package com.myph.manage.controller.firstapprove;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.approvetask.dto.ApproveTaskDto;
import com.myph.approvetask.service.ApproveTaskService;
import com.myph.auditlog.dto.AuditLogDto;
import com.myph.auditlog.service.AuditLogService;
import com.myph.common.activemq.ConstantKey;
import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.redis.CacheService;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.constant.ApplyFirstReportEnum;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.bis.ApplyBisStateEnum;
import com.myph.constant.bis.AuditFirstBisStateEnum;
import com.myph.constant.bis.AuditLastBisStateEnum;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.flow.dto.ContinueActionDto;
import com.myph.flow.dto.ExternalActionDto;
import com.myph.flow.dto.FallbackActionDto;
import com.myph.flow.dto.RejectActionDto;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.facadeService.FacadeFlowStateExchangeService;
import com.myph.node.dto.SysNodeDto;
import com.myph.node.service.NodeService;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.product.dto.ProductDto;
import com.myph.product.service.ProductService;
import com.myph.refusereason.dto.SysRefuseReasonDto;
import com.myph.refusereason.service.SysRefuseReasonService;
import com.myph.team.dto.SysTeamDto;
import com.myph.team.service.SysTeamService;

/**
 * @ClassName: ApproveController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 罗荣
 * @date 2016年9月18日 下午5:20:53
 * 
 */
@Controller
@RequestMapping("/firstApprove")
public class FirstApproveController {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ProductService proService;
    @Autowired
    private ApproveTaskService approveService;
    @Autowired
    private ApplyInfoService applyInfoService;
    @Autowired
    private NodeService nodeService;

    @Autowired
    FacadeFlowStateExchangeService facadeFlowStateExchangeService;

    @Autowired
    private EmployeeInfoService employeeInfoService;
    @Autowired
    private SysRefuseReasonService refuseReasonService;
    @Autowired
    private AuditLogService auditLogService;
    
    @Autowired
    private SysTeamService teamService;

    public final static String PATH = "/apply/firstApprove";
    
    public final static String HJFA = "APP|HJFA";
    
    public final static String error = "error/500";

    @RequestMapping("/approveDeal")
    public String approveDeal(String applyLoanNo, String cType, Model model) {

        ServiceResult<ApplyInfoDto> resultApplyInfo = applyInfoService.queryInfoByLoanNo(applyLoanNo);
        ApplyInfoDto applyInfo = resultApplyInfo.getData();
        if (null == applyInfo) {
            MyphLogger.error("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 查询申请件失败【" + applyLoanNo + "】");
            return error;
        }
        ServiceResult<ApproveTaskDto> resultApproveDto = approveService.selectByApplyLoanNo(applyLoanNo);
        ApproveTaskDto approveDto = resultApproveDto.getData();
        if (null == approveDto) {
            MyphLogger.error("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 查询申请件未提审【" + applyLoanNo + "】");
            return error;
        }
        // 1、补充门店名称
        ServiceResult<OrganizationDto> storeResult = organizationService.selectOrganizationById(applyInfo.getStoreId());
        if(null != storeResult.getData()){
            applyInfo.setStoreName(storeResult.getData().getOrgName());
        }
        // 2、补充大区名称
        ServiceResult<OrganizationDto> areaResult = organizationService.selectOrganizationById(applyInfo.getAreaId());
        if(null != areaResult.getData()){
            applyInfo.setAreaName(areaResult.getData().getOrgName());
        }
        // 3、补充产品名称
        ServiceResult<ProductDto> result = proService.selectByPrimaryKey(approveDto.getProductTypeNo());
        if (null == result.getData()) {
            MyphLogger.error("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 查询申请件的产品信息未能成功查询到【" + applyInfo.getProductType() + "】异常");
            return error;
        }
        
        ServiceResult<SysNodeDto> rs = nodeService.selectByPrimaryKey(result.getData().getProdType());
        if (!rs.success() || null  == rs.getData()) {
            MyphLogger.error("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 查询申请件的产品信息类型名称【" + result.getData().getProdType() + "】异常");
            return error;
        }
        applyInfo.setProductName(rs.getData().getNodeName());
        
        // 申请件的借款用途
        ServiceResult<SysNodeDto> loanPurposeResult = nodeService.selectByPrimaryKey(applyInfo.getLoanPurpose());
        if (loanPurposeResult.success() && loanPurposeResult.getData() != null) {
            applyInfo.setLoanPurposeName(loanPurposeResult.getData().getNodeName());
        }
        // 设置状态名称

        // 状态名称先获取初审，然后再获取终审，有初审，才能有终审
        String stateName = AuditFirstBisStateEnum.getName(approveDto.getAuditState());

        // 初审不存在，就没有终审
        if (null == stateName) {
            stateName = AuditLastBisStateEnum.getName(approveDto.getAuditState());
        }
        // 把状态放到前面页面上去
        Map<String, Object> states = new HashMap<>();
        for (AuditFirstBisStateEnum e : AuditFirstBisStateEnum.values()) {
            states.put("Fisrt_" + e.name(), e.getCode());
        }
        for (AuditLastBisStateEnum e : AuditLastBisStateEnum.values()) {
            states.put("Last_" + e.name(), e.getCode());
        }
        for (FlowStateEnum e : FlowStateEnum.values()) {
            states.put("Flow_" + e.name(), e.getCode());
        }
        for (ApplyBisStateEnum e : ApplyBisStateEnum.values()) {
            states.put("Apply_" + e.name(), e.getCode());
        }
        // 查询日志记录
        ServiceResult<List<AuditLogDto>> sr = auditLogService.selectByApplyLoanNo(applyLoanNo);
        if (!sr.success()) {
            MyphLogger.error("LastApproveController.approveDeal  查询日志记录失败【" + applyLoanNo + "】");
            return error;
        }
        //产品类型为户籍方案时，初审页面无回退按钮
        if(rs.getData().getNodeCode().equals(HJFA)){
            model.addAttribute("allowBack","noAllowBack");
        }else{
            model.addAttribute("allowBack", "allowBack");
        }
        approveDto.setAuditStateName(stateName);
        model.addAttribute("applyLoanInfo", applyInfo);
        model.addAttribute("states", states);
        model.addAttribute("applyApproveTask", approveDto);
        model.addAttribute("product", result.getData());
        model.addAttribute("cType", cType);
        model.addAttribute("auditLogs", sr.getData());
        MyphLogger.info("查询结果：" + model.toString());
        return PATH + "/approve_deal";
    }

    /**
     * [提交初审]
     * <p>
     * <p>
     * 保存初审数据到任务表中
     * </p>
     * <p>
     * 调用主流程更新主流程状态
     * </p>
     * <p>
     * 如果是拒绝，调用拒绝主流程 如果是通过，调用通过主流程
     * </p>
     * <p>
     * 保存到修改记录表中去，记录保存
     * </p>
     * </p>
     * 
     * @名称 submitTaskInfo
     * @描述 TODO(这里用一句话描述这个方法的作用)
     * @返回类型 AjaxResult
     * @日期 2016年9月26日 上午9:02:11
     * @创建人 罗荣
     * @更新人 罗荣
     *
     */
    @RequestMapping("/submitTaskInfo")
    @ResponseBody
    public AjaxResult submitTaskInfo(ApproveTaskDto taskInfo, Model model, AuditLogDto record) {
        MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 FirstApproveController.submitTaskInfo 输入参数[" + taskInfo + "]");
        //现在没有内或者外部意见了
        taskInfo.setInternalRemark(taskInfo.getExternalRemark());
        
        if (!taskInfo.getAuditState().equals(AuditFirstBisStateEnum.FIRST_APPROVE_REFUSE.getCode())) {
        	// 验证 推荐金额 是否在产品上下限区间
        	if (!Constants.UNSELECT_LONG.equals(taskInfo.getProId())) {
        		ServiceResult<ProductDto> productRs = proService.selectByPrimaryKey(taskInfo.getProId());
        		if(productRs.success()&&null!=productRs.getData()){
        			ProductDto pro = productRs.getData();
        			//-1 小于 0等于 1大于
        			if(taskInfo.getCreditMoney().compareTo(pro.getLoanUpLimit()) == 1 || taskInfo.getCreditMoney().compareTo(pro.getLoanDownLimit()) == -1){
        				return AjaxResult.failed("申请金额应在产品上限和下限范围中【"+pro.getLoanUpLimit()+"-"+pro.getLoanDownLimit()+"】");
        			}
        		}else{
        			MyphLogger.error("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
        			+ "】 调用保存终审任务状态服务失败！"+productRs.getMessage() );
        			return AjaxResult.failed("未查询到产品");
        		}
        	}
        	// 验证初审报告的数据完整性
            for (ApplyFirstReportEnum e : ApplyFirstReportEnum.values()) {
                if (!CacheService.KeyBase.isExistsKey(e.getKey(taskInfo.getApplyLoanNo()))) {
                    MyphLogger.error("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 [" + e.getDesc() + "] 数据不完整！" + taskInfo.getApplyLoanNo());
                    return AjaxResult.failed("[" + e.getDesc() + "] 数据不完整！");
                }
            }
            // 都通过了才删除
            for (ApplyFirstReportEnum e : ApplyFirstReportEnum.values()) {
                if (!CacheService.KeyBase.isExistsKey(e.getKey(taskInfo.getApplyLoanNo()))) {
                    CacheService.KeyBase.delete(e.getKey(taskInfo.getApplyLoanNo()));
                }
            }
        }
        MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 验证数据其它页面数据结束！");

        String mainCause = null;
        String secondCause = null;
        if (!Constants.UNSELECT_STR.equals(taskInfo.getMainCauseNo())) {
            ServiceResult<SysRefuseReasonDto> mainNodeRs = refuseReasonService.selectOne(taskInfo.getMainCauseNo());
            mainCause = mainNodeRs.getData().getRefuseDesc();
            secondCause = mainNodeRs.getData().getRefuseCode();
        }
        taskInfo.setMainCause(mainCause);
        taskInfo.setSecondCause(secondCause);
        // 保存任务更新备份表
        record.setAuditStage(FlowStateEnum.AUDIT_FIRST.getCode());
        // taskInfo 这个时候，通过的时候，是修改成待终审
        if (taskInfo.getAuditState().equals(AuditFirstBisStateEnum.FIRST_APPROVE_REFUSE.getCode())) {
            record.setAuditResult(AuditFirstBisStateEnum.FIRST_APPROVE_REFUSE.getCode());
        } else {
            record.setAuditResult(AuditFirstBisStateEnum.FIRST_APPROVE_PASS.getCode());
        }
        record.setDelFlag(Constants.YES_INT);
        record.setCreateUser(ShiroUtils.getCurrentUserName());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setInteriorRemark(taskInfo.getInternalRemark());
        record.setPublicRemark(taskInfo.getExternalRemark());
        record.setSuggestMoney(taskInfo.getCreditMoney());
        record.setMainCause(mainCause);
        record.setLesserCause(secondCause);
        record.setLoanPeriods(taskInfo.getLoanPeriods());
        record.setProductName(taskInfo.getProductName());
        record.setAuditor(ShiroUtils.getCurrentUser().getId());
        auditLogService.insert(record);

        // 保存初审数据到任务表中
        taskInfo.setProductTypeNo(taskInfo.getProId());
        EmployeeInfoDto user = ShiroUtils.getCurrentUser();
        
        if(null == user.getTeamId()){
            MyphLogger.error("复审人ID为null");
            return AjaxResult.failed("复审人ID为null");
        }
        ServiceResult<SysTeamDto> teamRs = teamService.teamInfoQueryById(String.valueOf(user.getTeamId()));
        if((!teamRs.success())&&null == teamRs.getData()){
            MyphLogger.error("调用保存初审数据服务失败！信审teamLeader没有查询到【"+user.getTeamId()+"】");
            return AjaxResult.failed(teamRs.getMessage());
        }
        
        taskInfo.setReviewAuditor(teamRs.getData().getLeaderId());
        if(AuditFirstBisStateEnum.FIRST_APPROVE_REFUSE.getCode().equals(taskInfo.getAuditState())){
            taskInfo.setAuditResult(FlowStateEnum.AUDIT_FIRST.getCode());
        }else{
            taskInfo.setAuditResult(FlowStateEnum.AUDIT_LASTED.getCode());
        }
        ServiceResult sResult = approveService.updateFisrtData(taskInfo);
        if (!sResult.success()) {
            MyphLogger.error("调用保存初审数据服务失败！" + sResult.getMessage());
            return AjaxResult.failed(sResult.getMessage());
        }
        MyphLogger.info("保存初审数据成功:" + taskInfo);

        // 更新任务表状态
        sResult = approveService.updateTaskState(taskInfo.getApplyLoanNo(),taskInfo.getAuditResult(), taskInfo.getAuditState());

        // 调用主流程
        if (AuditFirstBisStateEnum.FIRST_APPROVE_REFUSE.getCode().equals(taskInfo.getAuditState())) {
            MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 进入拒绝流程状态机");

            RejectActionDto applyNotifyDto = new RejectActionDto();
            applyNotifyDto.setApplyLoanNo(taskInfo.getApplyLoanNo());
            applyNotifyDto.setOperateUser(ShiroUtils.getCurrentUserName());
            applyNotifyDto.setRejectDays(taskInfo.getConfinementTime());
            applyNotifyDto.setFlowStateEnum(FlowStateEnum.AUDIT_FIRST);
            ServiceResult serviceResult = facadeFlowStateExchangeService.doAction(applyNotifyDto);
            if (!serviceResult.success()) {
                MyphLogger.error("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 调用更新主流程失败！param【" + applyNotifyDto + "】,MESSAGE:{}", serviceResult.getMessage());
                return AjaxResult.formatFromServiceResult(serviceResult);
            }else{
                MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 拒绝设置【成功】");
            }
        } else {
        	  // 更新信审审批金额
            MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 更新主表的审批金额开始！");
            ApplyInfoDto updateDto = new ApplyInfoDto();
            updateDto.setApplyLoanNo(taskInfo.getApplyLoanNo());
            updateDto.setAuditRatifyMoney(taskInfo.getCreditMoney());
            sResult = applyInfoService.updateInfo(updateDto);
            MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 更新主表的审批金额结束：" + sResult.getMessage());
            
            MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 进入下一步【终审】流程状态机");
            // 调用状态机进入主流程DTO
            ContinueActionDto applyNotifyDto = new ContinueActionDto();
            applyNotifyDto.setApplyLoanNo(taskInfo.getApplyLoanNo());
            applyNotifyDto.setOperateUser(ShiroUtils.getCurrentUserName());
            applyNotifyDto.setFlowStateEnum(FlowStateEnum.AUDIT_FIRST);
            // 走状态机更新主流程
            ServiceResult serviceResult = facadeFlowStateExchangeService.doAction(applyNotifyDto);
            if (!serviceResult.success()) {
                MyphLogger.error("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 调用更新主流程失败！param【" + applyNotifyDto + "】,MESSAGE:{}", serviceResult.getMessage());
                return AjaxResult.formatFromServiceResult(serviceResult);
            }else{
                MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 下一步【终审】设置成功！");
            }
        }
        return AjaxResult.success();

    }

    /**
     * <p>
     * 调用流程状态机， 然后添加任务日志记录表
     * </p>
     * 
     * @名称 rollBack
     * @描述 初审回退(这里用一句话描述这个方法的作用)
     * @返回类型 AjaxResult
     * @日期 2016年9月26日 上午10:54:58
     * @创建人 罗荣
     * @更新人 罗荣
     *
     */
    @RequestMapping("/rollBack")
    @ResponseBody
    public AjaxResult rollBack(String applyLoanNo, AuditLogDto record, Model model) {
        MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 FirstApproveController.rollBack 输入参数[" + applyLoanNo + "]");

        ServiceResult<ApplyInfoDto> rs = applyInfoService.queryInfoByAppNo(applyLoanNo);
        if (!rs.success()) {
            return AjaxResult.formatFromServiceResult(rs);
        }
        if (null == rs.getData()) {
            return AjaxResult.failed("申请单未查询到！");
        }
        // 保存任务更新备份表
        record.setAuditResult(ApplyBisStateEnum.BACK_INIT.getCode());
        record.setAuditStage(FlowStateEnum.AUDIT_FIRST.getCode());
        record.setDelFlag(Constants.YES_INT);
        record.setCreateUser(ShiroUtils.getCurrentUserName());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setSuggestMoney(rs.getData().getApplyMoney());
        record.setLoanPeriods(rs.getData().getLoanPeriods());
        record.setAuditor(ShiroUtils.getCurrentUser().getId());
        
        //回退到进件，把原因写到外部意见中，并内部意见也需要加上
        record.setPublicRemark(record.getInteriorRemark());
        
        //查询产品名称
        ServiceResult<ProductDto> result = proService.selectByPrimaryKey(rs.getData().getProductType());
        if (null == result.getData()) {
            MyphLogger.error("查询申请件的产品信息未能成功查询到【" + rs.getData().getProductType() + "】");
            return AjaxResult.failed("查询申请件的产品信息未能成功查询到【" + rs.getData().getProductType() + "】");
        }
        ServiceResult<SysNodeDto> nodeRs = nodeService.selectByPrimaryKey(result.getData().getProdType());
        if (!rs.success() || nodeRs.getData() == null) {
            MyphLogger.error("查询申请件的产品信息类型名称【" + result.getData().getProdType() + "】");
            return AjaxResult.failed("查询申请件的产品信息类型名称【" + result.getData().getProdType() + "】");
        }
        
        record.setProductName(nodeRs.getData().getNodeName());
        
        //保存日志记录
        ServiceResult serviceResult = auditLogService.insert(record);
        
        MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 保存信审日志记录【" +record + "】成功！");
        
        if (!serviceResult.success()) {
            return AjaxResult.formatFromServiceResult(serviceResult);
        }
        // 更新主表为完成状态【初审回退】
        serviceResult = applyInfoService.updateSubState(applyLoanNo, ApplyBisStateEnum.BACK_INIT.getCode());
        if (!serviceResult.success()) {
            return AjaxResult.formatFromServiceResult(serviceResult);
        }
        MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 更新信审回退主表状态成功！");
        
        // 设置任务表状态为回退状态
        serviceResult = approveService.updateTaskState(applyLoanNo,FlowStateEnum.APPLY.getCode(), ApplyBisStateEnum.BACK_INIT.getCode());
        if (!serviceResult.success()) {
            return AjaxResult.formatFromServiceResult(serviceResult);
        }
        MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 更新信审回退任务状态成功！");
        
        MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 调用回退回流程");
        // 调用 主流程回退
        FallbackActionDto fallbackActionDto = new FallbackActionDto();
        fallbackActionDto.setApplyLoanNo(applyLoanNo);
        fallbackActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
        fallbackActionDto.setPublicRemark(record.getBackReasonDesc());
        fallbackActionDto.setRecptUserId(rs.getData().getCustomerServiceId());// 初审回退给客服
        fallbackActionDto.setFlowStateEnum(FlowStateEnum.AUDIT_FIRST);
        serviceResult = facadeFlowStateExchangeService.doAction(fallbackActionDto);
        if (!serviceResult.success()) {
            return AjaxResult.formatFromServiceResult(serviceResult);
        }
        MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 调用回退回流程成功");
        return AjaxResult.formatFromServiceResult(serviceResult);

    }

    /**
     * 
     * @名称 visitExternal
     * @描述 发起外访(这里用一句话描述这个方法的作用)
     * @返回类型 AjaxResult
     * @日期 2016年9月26日 上午10:54:58
     * @创建人 罗荣
     * @更新人 罗荣
     *
     */
    @RequestMapping("/visitExternal")
    @ResponseBody
    public AjaxResult visitExternal(String applyLoanNo, String requirement, Model model) {
        MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 FirstApproveController.visitExternal 输入参数[" + applyLoanNo + "]");

        ServiceResult serviceR = applyInfoService.updateSubState(applyLoanNo,
                AuditFirstBisStateEnum.FIRST_VISIT.getCode());
        if (!serviceR.success()) {
            return AjaxResult.formatFromServiceResult(serviceR);
        }
        // 设置任务表状态为回退状态
        serviceR = approveService.updateTaskState(applyLoanNo,FlowStateEnum.EXTERNAL_FIRST.getCode(), AuditFirstBisStateEnum.FIRST_VISIT.getCode());
        if (!serviceR.success()) {
            return AjaxResult.formatFromServiceResult(serviceR);
        }
        MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 FirstApproveController.visitExternal 发起外访主流程调用[" + applyLoanNo + "]");
        ExternalActionDto externalActionDto = new ExternalActionDto();
        externalActionDto.setApplyLoanNo(applyLoanNo);
        externalActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
        externalActionDto.setRequirement(requirement);
        externalActionDto.setOperateUserId(ShiroUtils.getCurrentUserId());
        externalActionDto.setFlowStateEnum(FlowStateEnum.AUDIT_FIRST);
        serviceR = facadeFlowStateExchangeService.doAction(externalActionDto);
        if (!serviceR.success()) {
            return AjaxResult.formatFromServiceResult(serviceR);
        }
        // 更新主表为完成状态【】
        return AjaxResult.formatFromServiceResult(serviceR);
    }

}