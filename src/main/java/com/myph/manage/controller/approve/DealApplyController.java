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

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.myph.apply.dto.AuditServiceDto;
import com.myph.common.constant.NumberConstants;
import com.myph.common.hbase.HbaseUtils;
import com.myph.constant.ReqAuditEnum;
import com.myph.member.base.dto.MemberInfoDto;
import com.myph.member.base.service.MemberInfoService;
import com.myph.member.blacklist.dto.ThirdBlackDto;
import com.myph.member.blacklist.service.ThirdBlackService;
import com.myph.reqAuditTask.dto.ReqAuditTaskDto;
import com.myph.reqAuditTask.service.ReqAuditTaskService;
import org.apache.commons.lang3.StringUtils;
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
import com.myph.common.constant.SysConfigEnum;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.util.DateUtils;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.bis.ApplyBisStateEnum;
import com.myph.constant.bis.AuditDirectorBisStateEnum;
import com.myph.constant.bis.AuditFirstBisStateEnum;
import com.myph.constant.bis.AuditLastBisStateEnum;
import com.myph.constant.bis.AuditManagerBisStateEnum;
import com.myph.flow.dto.BaseActionDto;
import com.myph.flow.dto.ContinueActionDto;
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
import com.myph.sysParamConfig.service.SysParamConfigService;

/**
 *
 * @ClassName: DealApplyController
 * @Description: 处理信审单子(这里用一句话描述这个类的作用)
 * @author 罗荣
 * @date 2016年12月15日 上午9:08:28
 *
 */
@Controller
@RequestMapping("/dealApply")
public class DealApplyController {
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
    FacadeFlowStateExchangeService<BaseActionDto> facadeFlowStateExchangeService;
    @Autowired
    ReqAuditTaskService reqAuditTaskService;

    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    ThirdBlackService thirdBlackService;
    @Autowired
    MemberInfoService memberInfoService;

    @Autowired
    private SysParamConfigService sysParamConfigService;

    public final static String PATH = "/apply/approve";

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
        if (null != storeResult.getData()) {
            applyInfo.setStoreName(storeResult.getData().getOrgName());
        }
        // 2、补充大区名称
        ServiceResult<OrganizationDto> areaResult = organizationService.selectOrganizationById(applyInfo.getAreaId());
        if (null != areaResult.getData()) {
            applyInfo.setAreaName(areaResult.getData().getOrgName());
        }
        // 3、补充产品名称
        ServiceResult<ProductDto> result = proService.selectByPrimaryKey(applyInfo.getProductType());
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
        if (!rs.success() || rs.getData() == null) {
            MyphLogger.error("查询申请件的借款用途名称【" + applyInfo.getLoanPurpose() + "】异常");
            return error;
        }
        applyInfo.setLoanPurposeName(rs.getData().getNodeName());

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
        states.put("REFUSE", getRefuseToSubState(applyInfo.getState()));
        states.put("FINISH", getFinishToSubState(applyInfo.getState()));

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
        model.addAttribute("dealName", FlowStateEnum.getDescByCode(applyInfo.getState()));
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
     * @描述 提交信审(这里用一句话描述这个方法的作用)
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

        ServiceResult<ApplyInfoDto> applyrs = applyInfoService.queryInfoByAppNo(taskInfo.getApplyLoanNo());
        if (!applyrs.success() && null == applyrs.getData()) {
            return AjaxResult.failed("无法查询到此单号信息");
        }

        // 保存任务更新备份表
        record.setAuditStage(applyrs.getData().getState());
        // taskInfo 这个时候，通过的时候，是修改成终审完成
        if (taskInfo.getAuditState().equals(getRefuseToSubState(applyrs.getData().getState()))) {
            record.setAuditResult(getRefuseToSubState(applyrs.getData().getState()));
            taskInfo.setAuditResult(applyrs.getData().getState());
        } else {
            // 验证 推荐金额 是否在产品上下限区间
            if (!Constants.UNSELECT_LONG.equals(taskInfo.getProId())) {
                ServiceResult<ProductDto> productRs = proService.selectByPrimaryKey(taskInfo.getProId());
                if (productRs.success() && null != productRs.getData()) {
                    ProductDto pro = productRs.getData();
                    // -1 小于 0等于 1大于
                    if (taskInfo.getCreditMoney().compareTo(pro.getLoanUpLimit()) == 1
                            || taskInfo.getCreditMoney().compareTo(pro.getLoanDownLimit()) == -1) {
                        return AjaxResult.failed(
                                "申请金额应在产品上限和下限范围中【" + pro.getLoanUpLimit() + "-" + pro.getLoanDownLimit() + "】");
                    }
                } else {
                    MyphLogger.error("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【"
                            + ShiroUtils.getCurrentUserName() + "】 调用保存终审任务状态服务失败！" + productRs.getMessage());
                    return AjaxResult.failed("未查询到产品");
                }
            }
            record.setAuditResult(getFinishToSubState(applyrs.getData().getState()));
            //TODO 信审任务子状态设置为征信中
            taskInfo.setAuditState(getAuditToSubState(applyrs.getData().getState()));
            taskInfo.setAuditResult(FlowStateEnum.AUDIT_DIRECTOR.getCode());
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
        Integer nextState = null;
        if(applyrs.getData().getState().equals(FlowStateEnum.AUDIT_MANAGER.getCode())){
            taskInfo.setLastAuditor(ShiroUtils.getCurrentUserId());
            taskInfo.setAuditLastDate(new Date());
            nextState = FlowStateEnum.AUDIT_DIRECTOR.getCode();
        }else if(applyrs.getData().getState().equals(FlowStateEnum.AUDIT_DIRECTOR.getCode())){
            taskInfo.setSuperLastAuditor(ShiroUtils.getCurrentUserId());
            taskInfo.setAuditSuperLastDate(new Date());
            nextState = FlowStateEnum.SIGN.getCode();
        }
        //拒绝就不到下个主状态，还是当前状态
        if(taskInfo.getAuditState().equals(getRefuseToSubState(applyrs.getData().getState()))){
            nextState = applyrs.getData().getState();
        }
        // 因为这里终审和初审都要更新相同的数据【这里的方法【updateFisrtData】名就不做更改操作了】
        ServiceResult<?> sResult = approveService.updateFisrtData(taskInfo);
        if (!sResult.success()) {
            MyphLogger.error("调用保存终审数据服务失败！" + sResult.getMessage());
            return AjaxResult.failed(sResult.getMessage());
        }
        MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                + "】 更新信审任务表成功！：" + taskInfo);

        // 更新任务表状态
        sResult = approveService.updateTaskState(taskInfo.getApplyLoanNo(),nextState, taskInfo.getAuditState());
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

        if (getRefuseToSubState(applyrs.getData().getState()).equals(taskInfo.getAuditState())) {
            MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                    + "】 进入主流程状态设置为【拒绝】！");
            RejectActionDto applyNotifyDto = new RejectActionDto();
            applyNotifyDto.setApplyLoanNo(taskInfo.getApplyLoanNo());
            applyNotifyDto.setOperateUser(ShiroUtils.getCurrentUserName());
            applyNotifyDto.setRejectDays(taskInfo.getConfinementTime());
            applyNotifyDto.setFlowStateEnum(FlowStateEnum.getEnum(applyrs.getData().getState()));
            ServiceResult<?> serviceResult = facadeFlowStateExchangeService.doAction(applyNotifyDto);
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

            //金额小于配置值 时，直接进入签约
            String value = sysParamConfigService.getConfigValueByName(SysConfigEnum.AUDIT_DIRECTORAUDITMONEY);
            if (taskInfo.getCreditMoney().compareTo(new BigDecimal(value)) == -1
                    && applyrs.getData().getState().equals(FlowStateEnum.AUDIT_MANAGER.getCode())) {
                applyNotifyDto.setToSign(true);
            }

            applyNotifyDto.setApplyLoanNo(taskInfo.getApplyLoanNo());
            applyNotifyDto.setOperateUser(ShiroUtils.getCurrentUserName());
            applyNotifyDto.setFlowStateEnum(FlowStateEnum.getEnum(applyrs.getData().getState()));
            //TODO ++++++++++++++++++++++捷安数据接入调整+++++++++++++++++++++++++++++++
            boolean isNext = true;
            //TODO 是否调捷安征信
            if(isJieAnAuditState(applyNotifyDto)) {
                AjaxResult jieAnResult = jieAnAuditModel(applyrs,taskInfo);
                if(jieAnResult.isSuccess()) {
                    isNext = (boolean)jieAnResult.getData();
                } else {
                    return AjaxResult.success(jieAnResult.getMessage());
                }
            }
            // 判断是否进入下一步
            // true:1.终审不满足金额配置，默认进入高级终审
            // true:2.捷安征信通过进入签约
            if(isNext) {
                // 走状态机更新主流程
                ServiceResult<?> serviceResult = facadeFlowStateExchangeService.doAction(applyNotifyDto);
                if (!serviceResult.success()) {
                    MyphLogger
                            .error("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                                    + "】 调用更新主流程失败！param【{}】,MESSAGE:{}", applyNotifyDto, serviceResult.getMessage());
                    return AjaxResult.formatFromServiceResult(serviceResult);
                } else {
                    ApproveTaskDto taskInfoDto = new ApproveTaskDto();
                    taskInfoDto.setApplyLoanNo(taskInfo.getApplyLoanNo());
                    taskInfoDto.setPassTime(DateUtils.getCurrentDateTime());
                    approveService.updateFisrtData(taskInfoDto);
                    MyphLogger.info("更新信审通过时间,入参" + taskInfoDto.toString());
                    MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                            + "】 主流程状态设置为【通过】成功！");
                }
            }
        }
        return AjaxResult.success();

    }

    /**
     * @Description: 捷安征信处理逻辑
     * @author heyx
     * @date 2017/4/19
     * @version V1.0
     */
    private AjaxResult jieAnAuditModel(ServiceResult<ApplyInfoDto> applyrs,ApproveTaskDto taskInfo) {
        // 是否进入continue流程机制进入签约
        boolean isNext = true;

        ServiceResult<MemberInfoDto> member = memberInfoService.queryInfoByIdCard(applyrs.getData().getIdCard());
        AuditServiceDto serviceDto = new AuditServiceDto();
        if (member.success()) {
            serviceDto.setUserid(member.getData().getId().toString());
        }
        serviceDto.setIdcard(applyrs.getData().getIdCard());
        serviceDto.setName(applyrs.getData().getMemberName());
        serviceDto.setPhone(applyrs.getData().getPhone());
        ServiceResult<Map<String, Object>> reqJieAnResult = reqAuditTaskService.
                getAuditInfo(serviceDto,sysParamConfigService.getConfigValueByName(SysConfigEnum.JIA_AN_URL));
        String retinfo = (!reqJieAnResult.success()
                || reqJieAnResult.getData().get(ReqAuditEnum.NAME_RETINFO) == null) ?
                "" :
                reqJieAnResult.getData().get(ReqAuditEnum.NAME_RETINFO).toString();
        if(ReqAuditEnum.SUCCESS_CODE.getCode().equals(retinfo)){
            isNext = true;
        }else if (ReqAuditEnum.FAIL_CODE.getCode().equals(retinfo)) {
            MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                    + "】捷安征信拒绝，"+reqJieAnResult.getData());
            //TODO 失败，申请单系统拒绝
            RejectActionDto rejectActionDto = new RejectActionDto();
            rejectActionDto.setApplyLoanNo(taskInfo.getApplyLoanNo());
            rejectActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
            rejectActionDto.setFlowStateEnum(FlowStateEnum.getEnum(applyrs.getData().getState()));
            ServiceResult<?> rejectResult = facadeFlowStateExchangeService.doActionG(rejectActionDto);
            if (!rejectResult.success()) {
                MyphLogger.error("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                        + "】 调用更新主流程失败！param【{}】,MESSAGE:{}", rejectActionDto, rejectResult.getMessage());
                return AjaxResult.formatFromServiceResult(rejectResult);
            } else {
                MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                        + "】 进入主流程状态设置为【拒绝】成功");
            }
            isNext = false;
            String content = reqJieAnResult.getData().get(ReqAuditEnum.NAME_CONTENT) == null
                    ? null:reqJieAnResult.getData().get(ReqAuditEnum.NAME_CONTENT).toString();
            //TODO 记录第三方黑名单
            ThirdBlackDto ThirdBlackDto = new ThirdBlackDto();
            ThirdBlackDto.setMemberName(applyrs.getData().getMemberName());
            ThirdBlackDto.setIdCard(applyrs.getData().getApplyLoanNo());
            ThirdBlackDto.setSrcOrg(ReqAuditEnum.NAME_ORGSTR);
            ThirdBlackDto.setRejectReason(content);
            ThirdBlackDto.setChannel(ReqAuditEnum.NAME_CHANNL);
            ThirdBlackDto.setIsReject(NumberConstants.NUM_ONE);
            boolean b = thirdBlackService.isIdCardExist(applyrs.getData().getApplyLoanNo(),ReqAuditEnum.NAME_CHANNL,ReqAuditEnum.NAME_ORGSTR);
            if(!b){
                thirdBlackService.insert(ThirdBlackDto);
            }
            return AjaxResult.failed(content);
        } else {
            ReqAuditTaskDto taskDto = new ReqAuditTaskDto();
            taskDto.setReqStatu(applyrs.getData().getState() == null?null:applyrs.getData().getState().toString());
            taskDto.setIdNo(applyrs.getData().getApplyLoanNo());
            taskDto.setIdCard(applyrs.getData().getIdCard());
            isNext = false;
            //TODO 生成定时任务，异常走continue流程机制，进入签约
            try {
                ServiceResult<Integer> taskResult = reqAuditTaskService.saveInfo(taskDto);
                if(!taskResult.success()) {
                        isNext = true;
                }
            } catch (Exception e) {
                // 出现异常，进入签约阶段
                isNext = true;
                MyphLogger.error(e,"生成捷安定时任务异常，parm:{}",taskDto.toString());
            }

        }
        return AjaxResult.success(isNext);
    }

    /**
     * 获取捷安报告
     *
     * @return
     */
    @RequestMapping("/queryJieAnData")
    @ResponseBody
    public AjaxResult queryJieAnData(String userid) {
        MyphLogger.info("获取捷安报告", userid);
        try {
            ServiceResult<String> data = reqAuditTaskService.queryCrimecredit(userid);
            return AjaxResult.success(data.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "获取捷安报告失败,ID:{}", userid);
        }
        return AjaxResult.failed("获取捷安报告失败");
    }

    /**
     * 是否进入捷安征信<br/>
     * true:1.终审满足金额配置，sign标志位true
     * true:2.高级终审提交
     * @param dto
     * @return
     */
    private Boolean isJieAnAuditState(ContinueActionDto dto) {
        switch (dto.getFlowStateEnum()) {
            case AUDIT_MANAGER :
                if(dto.getToSign()) {
                    return true;
                }
                return false;
            case AUDIT_DIRECTOR:
                return true;
            default:
                return true;
        }

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
        ServiceResult<ApplyInfoDto> applyrs = applyInfoService.queryInfoByAppNo(applyLoanNo);

        if (!applyrs.success() && null == applyrs.getData()) {
            return AjaxResult.failed("无法查询到此单号信息");
        }
        ServiceResult<?> serviceResult = approveService.selectByApplyLoanNo(applyLoanNo);
        if (!serviceResult.success()) {
            return AjaxResult.formatFromServiceResult(serviceResult);
        }
        ApproveTaskDto taskInfo = (ApproveTaskDto) serviceResult.getData();

        // 调用 主流程回退
        FallbackActionDto fallbackActionDto = new FallbackActionDto();
        fallbackActionDto.setApplyLoanNo(applyLoanNo);
        fallbackActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
        fallbackActionDto.setPublicRemark(record.getBackReasonDesc());
        fallbackActionDto.setRecptUserId(getBackToAuditor(taskInfo, applyrs.getData().getState()));// 终审回退给初审
        fallbackActionDto.setFlowStateEnum(FlowStateEnum.getEnum(applyrs.getData().getState()));

        serviceResult = facadeFlowStateExchangeService.doAction(fallbackActionDto);
        if (!serviceResult.success()) {
            return AjaxResult.formatFromServiceResult(serviceResult);
        }
        // 保存任务更新备份表
        record.setAuditResult(getBackToSubState(applyrs.getData().getState()));
        record.setAuditStage(getBackToState(applyrs.getData().getState()));
        record.setDelFlag(Constants.YES_INT);
        record.setCreateUser(ShiroUtils.getCurrentUserName());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setAuditor(ShiroUtils.getCurrentUser().getId());
        serviceResult = auditLogService.insert(record);

        if (!serviceResult.success()) {
            return AjaxResult.formatFromServiceResult(serviceResult);
        }
        // 更新任务表为完成状态【终审回退】
        serviceResult = approveService.updateTaskState(applyLoanNo,getBackToState(applyrs.getData().getState()), getBackToSubState(applyrs.getData().getState()));
        return AjaxResult.formatFromServiceResult(serviceResult);

    }

    private Long getBackToAuditor(ApproveTaskDto task, Integer currentState) {
        switch (FlowStateEnum.getEnum(currentState)) {
            case AUDIT_MANAGER:
                return task.getReviewAuditor();
            case AUDIT_DIRECTOR:
                return task.getLastAuditor();
            default:
                break;
        }
        return null;
    }

    private Integer getBackToSubState(Integer currentState) {
        switch (FlowStateEnum.getEnum(currentState)) {
            case AUDIT_MANAGER:
                return AuditLastBisStateEnum.BACK_INIT.getCode();
            case AUDIT_DIRECTOR:
                return AuditManagerBisStateEnum.BACK_INIT.getCode();
            default:
                break;
        }
        return null;
    }
    private Integer getBackToState(Integer currentState) {
        switch (FlowStateEnum.getEnum(currentState)) {
            case AUDIT_MANAGER:
                return FlowStateEnum.AUDIT_LASTED.getCode();
            case AUDIT_DIRECTOR:
                return FlowStateEnum.AUDIT_MANAGER.getCode();
            default:
                break;
        }
        return null;
    }

    private Integer getRefuseToSubState(Integer currentState) {
        switch (FlowStateEnum.getEnum(currentState)) {
            case AUDIT_MANAGER:
                return AuditManagerBisStateEnum.REFUSE.getCode();
            case AUDIT_DIRECTOR:
                return AuditDirectorBisStateEnum.REFUSE.getCode();
            default:
                break;
        }
        return null;
    }

    private Integer getFinishToSubState(Integer currentState) {
        switch (FlowStateEnum.getEnum(currentState)) {
            case AUDIT_MANAGER:
                return AuditManagerBisStateEnum.FINISH.getCode();
            case AUDIT_DIRECTOR:
                return AuditDirectorBisStateEnum.FINISH.getCode();
            default:
                break;
        }
        return null;
    }
    private Integer getNextInitToSubState(Integer currentState) {
        switch (FlowStateEnum.getEnum(currentState)) {
            case AUDIT_MANAGER:
                return AuditDirectorBisStateEnum.INIT.getCode();
            case AUDIT_DIRECTOR:
                return AuditDirectorBisStateEnum.FINISH.getCode();
            default:
                break;
        }
        return null;
    }

    private Integer getAuditToSubState(Integer currentState) {
        switch (FlowStateEnum.getEnum(currentState)) {
            case AUDIT_MANAGER:
                return AuditManagerBisStateEnum.AUDIT.getCode();
            case AUDIT_DIRECTOR:
                return AuditDirectorBisStateEnum.AUDIT.getCode();
            default:
                break;
        }
        return null;
    }
}
