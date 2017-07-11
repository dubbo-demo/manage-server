package com.myph.manage.controller.apply;

import com.myph.apply.dto.ApplyInfoDto;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.bis.*;
import com.myph.team.dto.SysTeamDto;
import com.myph.team.service.SysTeamService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author heyx
 * @version V1.0
 * @Package: com.myph.manage.controller.apply
 * @company: 麦芽金服
 * @Description: 申请单操作基类
 * @date 2017/5/31
 */
public class ApplyBaseController {

    @Autowired
    SysTeamService sysTeamService;

    public static final String NO_TEAM_STR = "请将业务员与团队经理关系绑定！";

    /**
     * 根据业务经理获取团队信息
     * @param bmid
     * @return
     */
    public SysTeamDto getTeamManage(Long bmid) {
        // 获取团队经理
        SysTeamDto teamDto = null;
        if(null != bmid) {
            teamDto =  sysTeamService.queryInfoByBmId(bmid).getData();
        }
        return teamDto;
    }

    /**
     * @Description: 是否能够操作数据
     * @author heyx
     * @date 2017/5/31
     * @version V1.0
     */
    public Boolean isUpdateBySubState(Integer state, Integer subState) {
        if (subState == null) {
            return false;
        }
        if (state.equals(FlowStateEnum.APPLY.getCode()) && !subState.equals(ApplyBisStateEnum.FINISH.getCode())) {
            return true;
        } else if (subState.equals(AuditFirstBisStateEnum.INIT.getCode())
                || subState.equals(AuditLastBisStateEnum.INIT.getCode())
                || subState.equals(AuditDirectorBisStateEnum.INIT.getCode())
                || subState.equals(AuditManagerBisStateEnum.INIT.getCode())) {
            return true;
        } else {
            return false;
        }
    }

    public AjaxResult getReusltIsContinue(ApplyInfoDto applyInfo) {
        if (null == applyInfo) {
            MyphLogger.error("申请件信息查询失败:" + applyInfo.getApplyLoanNo());
            return AjaxResult.failed("申请件信息查询失败");
        }
        boolean isContinue = isUpdateBySubState(applyInfo.getState(),applyInfo.getSubState());
        if(!isContinue) {
            MyphLogger.info(applyInfo.getApplyLoanNo() + "当前阶段，不能修改数据：" + applyInfo.getSubState());
            return AjaxResult.failed(applyInfo.getApplyLoanNo() + "当前阶段，不能修改数据");
        }
        return AjaxResult.success();
    }

    /**
     * @Description: 是否能够修改子状态
     * @author heyx
     * @date 2017/5/31
     * @version V1.0
     */
    public Boolean isUpdateSubState(Integer state, Integer subState) {
        if (subState == null) {
            return false;
        }
        if (state.equals(FlowStateEnum.APPLY.getCode()) && !subState.equals(ApplyBisStateEnum.FINISH.getCode())) {
            return true;
        } else {
            return false;
        }
    }
}
