package com.myph.manage.common.handler.impl;

import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.dto.AuditServiceDto;
import com.myph.auditlog.service.AuditLogService;
import com.myph.common.constant.NumberConstants;
import com.myph.common.constant.SysConfigEnum;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.ReqAuditEnum;
import com.myph.flow.dto.RejectActionDto;
import com.myph.manage.common.handler.AuditHandler;
import com.myph.manage.common.handler.AuditHandlerEnum;
import com.myph.manage.common.handler.HandlerParmDto;
import com.myph.manage.common.handler.HandlerResultDto;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.member.base.dto.MemberInfoDto;
import com.myph.member.base.service.MemberInfoService;
import com.myph.member.blacklist.dto.ThirdBlackDto;
import com.myph.member.blacklist.service.ThirdBlackService;
import com.myph.reqAuditTask.dto.ReqAuditTaskDto;
import com.myph.reqAuditTask.service.ReqAuditTaskService;
import com.myph.sysParamConfig.service.SysParamConfigService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private SysParamConfigService sysParamConfigService;
    @Autowired
    ThirdBlackService thirdBlackService;
    @Autowired
    MemberInfoService memberInfoService;

    @Override
    public HandlerResultDto audit(HandlerParmDto t) {
        MyphLogger.info("捷安征信handler");
        ApplyInfoDto applyrs = t.getApplyInfoDto();
        // 是否进入continue流程机制进入签约
        HandlerResultDto result = new HandlerResultDto();
        result.setIsAuditSuccess(true);

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
            result.setIsAuditSuccess(true);
        }else if (ReqAuditEnum.FAIL_CODE.getCode().equals(retinfo)) {
            MyphLogger.info("操作人ID【" + ShiroUtils.getCurrentUserId() + "】操作人【" + ShiroUtils.getCurrentUserName()
                    + "】捷安征信拒绝实体组装，"+reqJieAnResult.getData());
            //TODO 失败，申请单系统拒绝实体组装
            RejectActionDto rejectActionDto = new RejectActionDto();
            rejectActionDto.setApplyLoanNo(applyrs.getApplyLoanNo());
            rejectActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
            rejectActionDto.setFlowStateEnum(FlowStateEnum.getEnum(applyrs.getState()));
            result.setBaseActionDto(rejectActionDto);

            String content = reqJieAnResult.getData().get(ReqAuditEnum.NAME_CONTENT) == null
                    ? null:reqJieAnResult.getData().get(ReqAuditEnum.NAME_CONTENT).toString();
            //TODO 记录第三方黑名单
            ThirdBlackDto ThirdBlackDto = new ThirdBlackDto();
            ThirdBlackDto.setMemberName(applyrs.getMemberName());
            ThirdBlackDto.setIdCard(applyrs.getApplyLoanNo());
            ThirdBlackDto.setSrcOrg(ReqAuditEnum.NAME_ORGSTR);
            ThirdBlackDto.setRejectReason(content);
            ThirdBlackDto.setChannel(ReqAuditEnum.NAME_CHANNL);
            ThirdBlackDto.setIsReject(NumberConstants.NUM_ONE);
            boolean b = thirdBlackService.isIdCardExist(applyrs.getApplyLoanNo(),ReqAuditEnum.NAME_CHANNL,ReqAuditEnum.NAME_ORGSTR);
            if(!b){
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
            //TODO 生成定时任务，异常走continue流程机制，进入签约
            try {
                ServiceResult<Integer> taskResult = reqAuditTaskService.saveInfo(taskDto);
                if(taskResult.success()) {
                    result.setIsAuditSuccess(false);
                }
            } catch (Exception e) {
                // 出现异常，进入签约阶段
                MyphLogger.error(e,"生成捷安定时任务异常，parm:{}",taskDto.toString());
            }

        }
        return result;
    }
}
