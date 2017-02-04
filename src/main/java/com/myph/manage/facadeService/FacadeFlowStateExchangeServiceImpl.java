package com.myph.manage.facadeService;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myph.common.constant.Constants;
import com.myph.common.exception.DataValidateException;
import com.myph.common.result.ServiceResult;
import com.myph.common.util.DateUtils;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.FlowStateExchangeEnum;
import com.myph.flow.dto.BaseActionDto;
import com.myph.flow.dto.ProcessResultDto;
import com.myph.flow.dto.RejectActionDto;
import com.myph.flow.service.FlowStateExchangeService;
import com.myph.member.base.service.MemberInfoService;
import com.myph.member.common.BaseConstants;
import com.myph.member.confine.dto.MemberConfineLogDto;
import com.myph.member.confine.service.MemberConfineLogService;

/**
 * INFO: info
 * User: zhaokai
 * Date: 2016/9/6 - 10:11
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */

@Service
public class FacadeFlowStateExchangeServiceImpl<T extends BaseActionDto> implements FacadeFlowStateExchangeService<T> {

    @Autowired
    private FlowStateExchangeService flowStateExchangeService;

    @Autowired
    private MemberInfoService memberInfoService;
    
    @Autowired
    private MemberConfineLogService memberConfineLogService;


    @Override
    public ServiceResult<Integer> doAction(T obj) {
        //获取当前请求的地址类型
        FlowStateExchangeEnum flowStateExchangeEnum = obj.getExchangeEnum();

        //调用 flowStateExchangeService组件服务
        ServiceResult<ProcessResultDto> serviceResult = null;
        try {
            serviceResult = flowStateExchangeService.doAction(obj);
        } catch (DataValidateException e) {
            return ServiceResult.newFailure(e.getMessage());
        }

        ProcessResultDto processResultDto = serviceResult.getData();
        //跨服务组件调用
        switch (flowStateExchangeEnum) {
            // //需要把 禁闭期的时候  同步到 客户信息里面
            case REJECT:
                if (obj instanceof RejectActionDto) {
                    String rejectDesc=formatRejectDesc(processResultDto, ((RejectActionDto) obj).getRejectDays());
                    memberInfoService.updateConfineInfo(processResultDto.getApplyLoanUserCard(), ((RejectActionDto) obj).getRejectDays(), rejectDesc);
                   
                    //保存禁闭日志
                    savaConfineLog(processResultDto.getApplyLoanUserCard(),(RejectActionDto) obj,rejectDesc);
                }
                break;
            default:
                break;
        }
        return ServiceResult.newSuccess();
    }

    /**
     * 
     * @名称 savaConfineLog
     * @描述 保存禁闭日志
     * @返回类型 void
     * @日期 2016年11月10日 下午5:17:53
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    private void savaConfineLog(String idCard, RejectActionDto reject,String rejectDesc) {
        
        Date now = new Date();
        Date rejectEndDate = null;
        int rejectDays=reject.getRejectDays();
        if (rejectDays >= 0) {
            rejectEndDate = DateUtils.addDays(now, rejectDays);
        } else {
            rejectEndDate = DateUtils.stringToDate(Constants.neverDate);
        }
        MemberConfineLogDto log = new MemberConfineLogDto();
        log.setApplyLoanNo(reject.getApplyLoanNo());
        log.setRemark(rejectDesc);
        log.setBeginTime(now);
        log.setEndTime(rejectEndDate);
        log.setConfineDays(rejectDays);
        log.setCreateTime(now);
        log.setCreateUser(reject.getOperateUser());
        memberConfineLogService.savaConfineLog(idCard, log);
    }

    //生成 禁闭原因
    private String formatRejectDesc(ProcessResultDto processResultDto, Integer rejectDays) {

        FlowStateEnum afterRejecEnum = FlowStateEnum.getEnum(processResultDto.getDestBisState());

        if (FlowStateEnum.AUDIT_FIRST.equals(afterRejecEnum) || FlowStateEnum.AUDIT_LASTED.equals(afterRejecEnum)) {
            if(-1 == rejectDays){
                return String.format(BaseConstants.REJECT_AUDIT_MESSAGE_NEVER, processResultDto.getApplyLoanNo());
            }
            return String.format(BaseConstants.REJECT_AUDIT_MESSAGE, rejectDays, processResultDto.getApplyLoanNo());
        }

        return String.format(BaseConstants.REJECT_BASE_MESSAGE, rejectDays, processResultDto.getApplyLoanNo());
    }

}
