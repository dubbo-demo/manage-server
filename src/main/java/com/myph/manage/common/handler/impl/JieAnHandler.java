package com.myph.manage.common.handler.impl;

import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.dto.AuditServiceDto;
import com.myph.approvetask.dto.ApproveTaskDto;
import com.myph.approvetask.service.ApproveTaskService;
import com.myph.common.constant.NumberConstants;
import com.myph.common.constant.SysConfigEnum;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.common.util.DateUtils;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.ReqAuditEnum;
import com.myph.flow.dto.RejectActionDto;
import com.myph.manage.common.handler.AuditHandler;
import com.myph.manage.common.handler.AuditHandlerEnum;
import com.myph.manage.common.handler.HandlerParmDto;
import com.myph.manage.common.handler.HandlerResultDto;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.facadeService.FacadeFlowStateExchangeService;
import com.myph.member.base.dto.MemberInfoDto;
import com.myph.member.base.service.MemberInfoService;
import com.myph.member.blacklist.dto.ThirdBlackDto;
import com.myph.member.blacklist.service.ThirdBlackService;
import com.myph.reqAuditTask.dto.ReqAuditTaskDto;
import com.myph.reqAuditTask.service.ReqAuditTaskService;
import com.myph.sysParamConfig.service.SysParamConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import java.util.Map;

/**
 * Created by dell on 2017/4/21.
 */
@Service
public class JieAnHandler extends AuditHandler{

    @Autowired
    ReqAuditTaskService reqAuditTaskService;

    @Autowired
    FacadeFlowStateExchangeService facadeFlowStateExchangeService;

    @Autowired
    private SysParamConfigService sysParamConfigService;
    @Autowired
    ThirdBlackService thirdBlackService;
    @Autowired
    MemberInfoService memberInfoService;

    @Autowired
    ApproveTaskService approveService;

    @Async
    @Override
    public HandlerResultDto audit(HandlerParmDto t) {
        MyphLogger.info("捷安征信handler");
        boolean continueState = false;
        ApplyInfoDto applyrs = t.getApplyInfoDto();
        // 是否进入continue流程机制进入签约
        HandlerResultDto result = new HandlerResultDto();
        result.setIsAuditSuccess(true);
        result.setBaseActionDto(t.getContinueActionDto());

        ServiceResult<MemberInfoDto> member = memberInfoService.queryInfoByIdCard(applyrs.getIdCard());
        AuditServiceDto serviceDto = new AuditServiceDto();
        if (member.success()) {
            serviceDto.setUserid(member.getData().getId().toString());
        }
        serviceDto.setIdcard(applyrs.getIdCard());
        serviceDto.setName(applyrs.getMemberName());
        serviceDto.setPhone(applyrs.getPhone());
        ServiceResult<Map<String, Object>> reqJieAnResult = reqAuditTaskService.
                getAuditInfo(serviceDto,sysParamConfigService.getConfigValueByName(SysConfigEnum.JIA_AN_URL));
        String retinfo = (!reqJieAnResult.success()
                || reqJieAnResult.getData().get(ReqAuditEnum.NAME_RETINFO) == null) ?
                "" :
                reqJieAnResult.getData().get(ReqAuditEnum.NAME_RETINFO).toString();
        if(ReqAuditEnum.SUCCESS_CODE.getCode().equals(retinfo)){
            MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                    + "】捷安征信流程扭转实体组装，"+t.getContinueActionDto());
            result.setIsAuditSuccess(true);
            continueState = true;
        }else if (ReqAuditEnum.FAIL_CODE.getCode().equals(retinfo)) {
            // 失败，申请单系统拒绝实体组装
            RejectActionDto rejectActionDto = new RejectActionDto();
            rejectActionDto.setApplyLoanNo(applyrs.getApplyLoanNo());
            rejectActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
            rejectActionDto.setFlowStateEnum(FlowStateEnum.getEnum(applyrs.getState()));
            result.setBaseActionDto(rejectActionDto);
            MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                    + "】捷安征信拒绝实体组装，"+rejectActionDto);
            String content = reqJieAnResult.getData().get(ReqAuditEnum.NAME_CONTENT) == null
                    ? null:reqJieAnResult.getData().get(ReqAuditEnum.NAME_CONTENT).toString();
            // 记录第三方黑名单
            ThirdBlackDto ThirdBlackDto = new ThirdBlackDto();
            ThirdBlackDto.setMemberName(applyrs.getMemberName());
            ThirdBlackDto.setIdCard(applyrs.getApplyLoanNo());
            ThirdBlackDto.setSrcOrg(ReqAuditEnum.NAME_ORGSTR);
            ThirdBlackDto.setRejectReason(content);
            ThirdBlackDto.setChannel(ReqAuditEnum.NAME_CHANNL);
            ThirdBlackDto.setIsReject(NumberConstants.NUM_ONE);
            boolean b = thirdBlackService.isIdCardExist(applyrs.getApplyLoanNo(),ReqAuditEnum.NAME_CHANNL,ReqAuditEnum.NAME_ORGSTR);
            if(!b){
                MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                        + "】导入第三方黑名单，"+ThirdBlackDto);
                thirdBlackService.insert(ThirdBlackDto);
            }
            result.setMessage(content);
        } else {
            ReqAuditTaskDto taskDto = new ReqAuditTaskDto();
            taskDto.setYwState(applyrs.getState() == null?null:applyrs.getState());
            taskDto.setIdNo(applyrs.getApplyLoanNo());
            taskDto.setReqTime(NumberConstants.NUM_ZERO);
            taskDto.setReqType(AuditHandlerEnum.JIEAN.getCode());
            taskDto.setIdCard(applyrs.getIdCard());
            // 生成定时任务，异常走continue流程机制，进入签约
            try {
                MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                        + "】生成捷安定时任务实体组装，"+t.getContinueActionDto());
                ServiceResult<Integer> taskResult = reqAuditTaskService.saveInfo(taskDto);
                if(taskResult.success()) {
                    result.setIsAuditSuccess(false);
                    MyphLogger.info("生成捷安定时任务成功");
                } else {
                    MyphLogger.info("生成捷安定时任务失败");
                }
            } catch (Exception e) {
                // 出现异常，进入签约阶段
                MyphLogger.error(e,"生成捷安定时任务异常，parm:{}",taskDto.toString());
            }

        }
        // 判断是否进入下一步
        // true:1.终审不满足金额配置，默认进入高级终审
        // true:2.捷安征信通过进入签约
        // true:3.拒绝
        if(result.getIsAuditSuccess()) {
            ServiceResult<?> serviceResult = facadeFlowStateExchangeService.doAction(result.getBaseActionDto());
            if (!serviceResult.success()) {
                MyphLogger.error("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                        + "】 调用更新主流程失败！param【{}】,MESSAGE:{}", result.getBaseActionDto(), serviceResult.getMessage());
            } else {
                if (continueState) {
                    ApproveTaskDto taskInfoDto = new ApproveTaskDto();
                    taskInfoDto.setApplyLoanNo(applyrs.getApplyLoanNo());
                    taskInfoDto.setPassTime(DateUtils.getCurrentDateTime());
                    approveService.updateFisrtData(taskInfoDto);
                    MyphLogger.info("更新信审通过时间,入参" + taskInfoDto.toString());
                }
                MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                        + "】 进入主流程状态设置为【" + result.getBaseActionDto().getFlowStateEnum().getDesc() + "】成功");
            }
        }
        return result;
    }
}
