/**   
 * @Title: ApproveController.java 
 * @Package: com.myph.manage.controller.approve 
 * @company: 麦芽金服
 * @Description: 审批管理(用一句话描述该文件做什么) 
 * @author 罗荣   
 * @date 2016年9月18日 下午5:20:53 
 * @version V1.0   
 */
package com.myph.manage.controller.lastapprove;

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
import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.bis.ApplyBisStateEnum;
import com.myph.constant.bis.AuditFirstBisStateEnum;
import com.myph.constant.bis.AuditLastBisStateEnum;
import com.myph.constant.bis.AuditManagerBisStateEnum;
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

/**
 * @ClassName: ApproveController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 罗荣
 * @date 2016年9月18日 下午5:20:53
 * 
 */
@Controller
@RequestMapping("/lastApprove")
public class LastApproveController {
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
    private SysRefuseReasonService refuseReasonService;
    @Autowired
    FacadeFlowStateExchangeService facadeFlowStateExchangeService;

    @Autowired
    private AuditLogService auditLogService;

    public final static String PATH = "/apply/lastApprove";

    public final static String error = "error/500";

    @RequestMapping("/approveDeal")
    public String approveDeal(String applyLoanNo, Integer cType, Model model) {

        ServiceResult<ApplyInfoDto> resultApplyInfo = applyInfoService.queryInfoByLoanNo(applyLoanNo);
        ApplyInfoDto applyInfo = resultApplyInfo.getData();
        if (null == applyInfo) {
            MyphLogger.error("LastApproveController.approveDeal 查询申请件失败【" + applyLoanNo + "】");
            return error;
        }
        ServiceResult<ApproveTaskDto> resultApproveDto = approveService.selectByApplyLoanNo(applyLoanNo);
        ApproveTaskDto approveDto = resultApproveDto.getData();
        if (null == approveDto) {
            MyphLogger.error("LastApproveController.approveDeal 查询申请件未提审【" + applyLoanNo + "】");
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
            MyphLogger.error("LastApproveController.approveDeal 查询申请件的产品信息未能成功查询到【" + applyInfo.getProductType() + "】");
            return error;
        }
        ServiceResult<SysNodeDto> rs = nodeService.selectByPrimaryKey(result.getData().getProdType());
        if (!rs.success() || null == rs.getData()) {
            MyphLogger.error("查询申请件的产品信息类型名称【" + result.getData().getProdType() + "】异常");
            return error;
        }
        applyInfo.setProductName(rs.getData().getNodeName());

        // 申请件的借款用途
        rs = nodeService.selectByPrimaryKey(applyInfo.getLoanPurpose());
        if (rs.success() && rs.getData() != null) {
            applyInfo.setLoanPurposeName(rs.getData().getNodeName());
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
        approveDto.setAuditStateName(stateName);
        // 查询日志记录
        ServiceResult<List<AuditLogDto>> sr = auditLogService.selectByApplyLoanNo(applyLoanNo);
        if (!sr.success()) {
            MyphLogger.error("LastApproveController.approveDeal  查询日志记录失败【" + applyLoanNo + "】");
            return error;
        }
        model.addAttribute("applyLoanInfo", applyInfo);
        model.addAttribute("auditLogs", sr.getData());
        model.addAttribute("states", states);
        model.addAttribute("applyApproveTask", approveDto);
        model.addAttribute("product", result.getData());
        model.addAttribute("cType", cType);
        MyphLogger.info("LastApproveController.approveDeal  查询结果：" + model.toString());
        return PATH + "/approve_deal";
    }

    /**
     * [提交初审]
     * <p>
     * 保存初审数据到任务表中
     * </p>
     * <p>
     * 如果是拒绝，调用拒绝主流程 如果是通过，调用通过主流程
     * </p>
     * <p>
     * 保存到修改记录表中去，记录保存
     * </p>
     * <p>
     * 调用主流程更新主流程状态
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
        MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                + "】 FirstApproveController.submitTaskInfo 输入参数[{}]", taskInfo);
        
        //现在没有内或者外部意见了
        taskInfo.setInternalRemark(taskInfo.getExternalRemark());
        
        // 保存任务更新备份表
        record.setAuditStage(FlowStateEnum.AUDIT_LASTED.getCode());
        // taskInfo 这个时候，通过的时候，是修改成终审完成
        if (taskInfo.getAuditState().equals(AuditLastBisStateEnum.REFUSE.getCode())) {
            record.setAuditResult(AuditLastBisStateEnum.REFUSE.getCode());
        } else {
        	// 验证 推荐金额 是否在产品上下限区间
            if (!Constants.UNSELECT_LONG.equals(taskInfo.getProId())) {
               ServiceResult<ProductDto> productRs = proService.selectByPrimaryKey(taskInfo.getProId());
               if(productRs.success()&&null!=productRs.getData()){
                   ProductDto pro = productRs.getData();
                   //-1 小于  0等于  1大于
                   if(taskInfo.getCreditMoney().compareTo(pro.getLoanUpLimit()) == 1 || taskInfo.getCreditMoney().compareTo(pro.getLoanDownLimit()) == -1){
                       return AjaxResult.failed("申请金额应在产品上限和下限范围中【"+pro.getLoanUpLimit()+"-"+pro.getLoanDownLimit()+"】");
                   }
               }else{
                   MyphLogger.error("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                   + "】 调用保存终审任务状态服务失败！"+productRs.getMessage() );
                   return AjaxResult.failed("未查询到产品");
               }
            }
            record.setAuditResult(AuditLastBisStateEnum.FINISH.getCode());
            taskInfo.setAuditState(AuditManagerBisStateEnum.INIT.getCode());
            taskInfo.setAuditResult(FlowStateEnum.AUDIT_MANAGER.getCode());
        }
        String mainCause = null;
        String secondCause = null;
        if (!Constants.UNSELECT_STR.equals(taskInfo.getMainCauseNo())) {
            ServiceResult<SysRefuseReasonDto> mainNodeRs = refuseReasonService.selectOne(taskInfo.getMainCauseNo());
            mainCause = mainNodeRs.getData().getRefuseDesc();
            secondCause = mainNodeRs.getData().getRefuseCode();
        }
        taskInfo.setMainCause(mainCause);
        taskInfo.setSecondCause(secondCause);
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
        MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                + "】 添加信审日志成功！：" + record);

        // 保存初审数据到任务表中
        taskInfo.setProductTypeNo(taskInfo.getProId());
        // 因为这里终审和初审都要更新相同的数据【这里的方法【updateFisrtData】名就不做更改操作了】
        ServiceResult sResult = approveService.updateFisrtData(taskInfo);
        if (!sResult.success()) {
            MyphLogger.error("调用保存终审数据服务失败！" + sResult.getMessage());
            return AjaxResult.failed(sResult.getMessage());
        }
        MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                + "】 更新信审任务表成功！：" + taskInfo);

        // 更新任务表状态
        sResult = approveService.updateTaskState(taskInfo.getApplyLoanNo(),taskInfo.getAuditResult(), taskInfo.getAuditState());
        if (!sResult.success()) {
            MyphLogger.error("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                    + "】 调用保存终审任务状态服务失败！" + sResult.getMessage());
            return AjaxResult.failed(sResult.getMessage());
        }
        MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                + "】 更新信审任务表状态成功！：" + taskInfo);

        // 更新主表状态
        sResult = applyInfoService.updateSubState(taskInfo.getApplyLoanNo(), taskInfo.getAuditState());
        if (!sResult.success()) {
            MyphLogger.error("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                    + "】 调用保存主表状态数据服务失败！" + sResult.getMessage());
            return AjaxResult.failed(sResult.getMessage());
        }
        MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                + "】 更新主表子状态成功！：" + taskInfo);

        if (AuditLastBisStateEnum.REFUSE.getCode().equals(taskInfo.getAuditState())) {
            MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                    + "】 进入主流程状态设置为【拒绝】！");
            RejectActionDto applyNotifyDto = new RejectActionDto();
            applyNotifyDto.setApplyLoanNo(taskInfo.getApplyLoanNo());
            applyNotifyDto.setOperateUser(ShiroUtils.getCurrentUserName());
            applyNotifyDto.setRejectDays(taskInfo.getConfinementTime());
            applyNotifyDto.setFlowStateEnum(FlowStateEnum.AUDIT_LASTED);
            ServiceResult serviceResult = facadeFlowStateExchangeService.doAction(applyNotifyDto);
            if (!serviceResult.success()) {
                MyphLogger.error("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                        + "】 调用更新主流程失败！param【{}】,MESSAGE:{}", applyNotifyDto, serviceResult.getMessage());
                return AjaxResult.formatFromServiceResult(serviceResult);
            } else {
                MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                        + "】 进入主流程状态设置为【拒绝】成功");
            }
        } else {
        	 // 更新信审审批金额与产品
            MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                    + "】 更新主表的审批金额开始！");
            ApplyInfoDto updateDto = new ApplyInfoDto();
            updateDto.setApplyLoanNo(taskInfo.getApplyLoanNo());
            updateDto.setAuditRatifyMoney(taskInfo.getCreditMoney());
            updateDto.setProductType(taskInfo.getProId());
            updateDto.setLoanPeriods(taskInfo.getLoanPeriods());
            sResult = applyInfoService.updateInfo(updateDto);
            MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                    + "】 更新主表的审批金额结束：" + sResult.getMessage());
            
            MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                    + "】 进入主流程状态设置为【通过】！");
            // 调用状态机进入主流程DTO
            ContinueActionDto applyNotifyDto = new ContinueActionDto();
            applyNotifyDto.setApplyLoanNo(taskInfo.getApplyLoanNo());
            applyNotifyDto.setOperateUser(ShiroUtils.getCurrentUserName());
            applyNotifyDto.setFlowStateEnum(FlowStateEnum.AUDIT_LASTED);
            // 走状态机更新主流程
            ServiceResult serviceResult = facadeFlowStateExchangeService.doAction(applyNotifyDto);
            if (!serviceResult.success()) {
                MyphLogger.error("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                        + "】 调用更新主流程失败！param【{}】,MESSAGE:{}", applyNotifyDto, serviceResult.getMessage());
                return AjaxResult.formatFromServiceResult(serviceResult);
            } else {
                MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                        + "】 主流程状态设置为【通过】成功！");
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
        MyphLogger.info("FirstApproveController.rollBack 输入参数[{}]", applyLoanNo);
        ServiceResult serviceResult = approveService.selectByApplyLoanNo(applyLoanNo);
        if (!serviceResult.success()) {
            return AjaxResult.formatFromServiceResult(serviceResult);
        }
        ApproveTaskDto taskInfo = (ApproveTaskDto) serviceResult.getData();
        // 保存任务更新备份表
        record.setAuditResult(AuditFirstBisStateEnum.BACK_INIT.getCode());// 终审退回 到 初审阶段
        record.setAuditStage(FlowStateEnum.AUDIT_LASTED.getCode());
        record.setDelFlag(Constants.YES_INT);
        record.setCreateUser(ShiroUtils.getCurrentUserName());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setAuditor(ShiroUtils.getCurrentUser().getId());
        serviceResult = auditLogService.insert(record);

        if (!serviceResult.success()) {
            return AjaxResult.formatFromServiceResult(serviceResult);
        }
        // 更新主表为完成状态【终审回退】
        serviceResult = applyInfoService.updateSubState(applyLoanNo, AuditFirstBisStateEnum.BACK_INIT.getCode());
        if (!serviceResult.success()) {
            return AjaxResult.formatFromServiceResult(serviceResult);
        }
        // 更新任务表为完成状态【终审回退】
        serviceResult = approveService.updateTaskState(applyLoanNo,FlowStateEnum.AUDIT_FIRST.getCode(), AuditFirstBisStateEnum.BACK_INIT.getCode());

        // 调用 主流程回退
        FallbackActionDto fallbackActionDto = new FallbackActionDto();
        fallbackActionDto.setApplyLoanNo(applyLoanNo);
        fallbackActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
        fallbackActionDto.setPublicRemark(record.getBackReasonDesc());
        fallbackActionDto.setRecptUserId(taskInfo.getFirstAuditor());// 终审回退给初审
        fallbackActionDto.setFlowStateEnum(FlowStateEnum.AUDIT_LASTED);
        serviceResult = facadeFlowStateExchangeService.doAction(fallbackActionDto);
        if (!serviceResult.success()) {
            return AjaxResult.formatFromServiceResult(serviceResult);
        }
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
        MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                + "】 FirstApproveController.visitExternal 输入参数[{}]", applyLoanNo);

        // 设置任务表状态为回退状态
        ServiceResult serviceR = approveService.updateTaskState(applyLoanNo,FlowStateEnum.EXTERNAL_FIRST.getCode(), AuditLastBisStateEnum.VISIT.getCode());
        if (!serviceR.success()) {
            MyphLogger.error("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                    + "】 更新任务子表出错");
            return AjaxResult.formatFromServiceResult(serviceR);
        }
        ExternalActionDto externalActionDto = new ExternalActionDto();
        externalActionDto.setApplyLoanNo(applyLoanNo);
        externalActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
        externalActionDto.setRequirement(requirement);
        externalActionDto.setOperateUserId(ShiroUtils.getCurrentUserId());
        externalActionDto.setFlowStateEnum(FlowStateEnum.AUDIT_LASTED);
        serviceR = facadeFlowStateExchangeService.doAction(externalActionDto);
        if (!serviceR.success()) {
            return AjaxResult.formatFromServiceResult(serviceR);
        }
        return AjaxResult.formatFromServiceResult(serviceR);
    }

}
