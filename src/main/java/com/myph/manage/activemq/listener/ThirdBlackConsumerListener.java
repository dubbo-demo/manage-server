/**
 * @Title: ThirdBlackConsumerListener.java
 * @Package: com.myph.manage.activemq.listener
 * @company: 麦芽金服
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 罗荣
 * @date 2016年9月19日 下午6:59:29
 * @version V1.0
 */
package com.myph.manage.activemq.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.common.activemq.BaseActivemqListener;
import com.myph.common.constant.Constants;
import com.myph.common.constant.ThirdBlackChannel;
import com.myph.common.constant.ThirdBlackReturnState;
import com.myph.common.exception.DataValidateException;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.common.util.DateUtils;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.bis.ApplyBisStateEnum;
import com.myph.flow.dto.BaseActionDto;
import com.myph.flow.dto.ContinueActionDto;
import com.myph.flow.dto.FallbackActionDto;
import com.myph.flow.dto.RejectActionDto;
import com.myph.manage.common.constant.Constant;
import com.myph.manage.facadeService.FacadeFlowStateExchangeService;
import com.myph.manage.param.ThirdBlackMqParam;
import com.myph.member.base.dto.MemberInfoDto;
import com.myph.member.base.service.MemberInfoService;
import com.myph.member.blacklist.dto.ThirdBlackDto;
import com.myph.member.blacklist.service.ThirdBlackService;
import com.myph.mqSendLog.dto.JkMqLogDto;
import com.myph.mqSendLog.service.JkMqService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.data.redis.core.index.RedisIndexDefinition.LowercaseIndexValueTransformer;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author 罗荣
 * @ClassName: ThirdBlackConsumerListener
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016年9月19日 下午6:59:29
 */
public class ThirdBlackConsumerListener extends BaseActivemqListener {
    @Autowired
    FacadeFlowStateExchangeService facadeFlowStateExchangeService;

    @Autowired
    ApplyInfoService applyInfoService;

    @Autowired
    ThirdBlackService thirdBlackService;

    @Autowired
    MemberInfoService memberService;

    @Autowired
    JkMqService jkMqService;

    /*
     * (非 Javadoc) <p>Title: onBaseMessage</p> <p>Description: </p>
     * @param map
     * @param name
     * @param type
     * @see com.myph.common.activemq.BaseActivemqListener#onBaseMessage(java.io.Serializable, java.lang.String,
     * java.lang.String)
     */
    @Override
    public void onBaseMessage(Serializable map, String name, String type) {

        MyphLogger.info("MQ 征信客户信息[" + map + "] name[{}],type[{}]", name, type);
        JSONObject result = JSONObject.parseObject((String) map);
        ThirdBlackMqParam obj = (ThirdBlackMqParam) JSON.parseObject(result.get("doc").toString(),
                ThirdBlackMqParam.class);

        ServiceResult<ApplyInfoDto> resultInfo = applyInfoService.selectByMemberInfo(obj.getIdno(), obj.getMobile(),
                obj.getName());
        ApplyInfoDto applyInfo = resultInfo.getData();
        if (null == applyInfo) {
            MyphLogger.error("MQ 征信客户信息[" + obj + "]  ==>> 未查询到相关客户申请件！");
            return;
        }
        
        MyphLogger.info("MQ 申请单号[" + applyInfo.getApplyLoanNo() + "]  通道【" + name + "】征信MQ返回结果[" + map + "]");
        Object returnCode = result.get("retcode");
        if (null == returnCode) {
            MyphLogger.error("MQ 返回的数据有误");
            return;
        }
        
        ThirdBlackReturnState code = ThirdBlackReturnState.getEnum(Integer.parseInt(returnCode.toString()));
        if (null == code) {
            MyphLogger.error("MQ 返回的数据有误 returnCode[{}]", returnCode);
            return;
        }
        
        if (ThirdBlackReturnState.PASS.equals(code)) {
            MyphLogger.info("MQ-CALLBACK ！进入通过，走下个流程！");
            try {
                ServiceResult<ApplyInfoDto> rs = applyInfoService.goAudit(applyInfo);
                if (!rs.success()) {
                    MyphLogger.error("MQ 走信审流程失败 ", rs.getMessage());
                }
            } catch (Exception e) {
                MyphLogger.error("MQ 走信审流程失败", e);
                return ;
            }

        } else if (ThirdBlackReturnState.QIANHAI_EXCEPTION.equals(code)
                || ThirdBlackReturnState.QIANHAI_FAIL.equals(code)
                || ThirdBlackReturnState.BAIRONG_EXCEPTION.equals(code)
                || ThirdBlackReturnState.BAIRONG_FAIL.equals(code)
                || ThirdBlackReturnState.YIXING_EXCEPTION.equals(code) || ThirdBlackReturnState.YIXING_NULL.equals(code)
                || ThirdBlackReturnState.TONGDUN_FAIL.equals(code)
                || ThirdBlackReturnState.TONGDUN_EXCEPTION.equals(code)) {
            MyphLogger.error("MQ-CALLBACK 异常，等待重发   。。。。param【" + code.getCode() + "】,MESSAGE:{}", code.getDesc());
        } else {
            // 更新任务表状态为不需要发送
            MyphLogger.info("MQ-CALLBACK ！更新为不需要发送状态开始");
            jkMqService.notResend(applyInfo.getApplyLoanNo());
            MyphLogger.info("MQ-CALLBACK ！更新为不需要发送状态结束");
            // 已经拒绝的是不操作其它的了
            if (FlowStateEnum.REJECT.getCode().equals(applyInfo.getState())) {
                // 已经拒绝过了
                MyphLogger.info("MQ-CALLBACK ！已经拒绝过了！");
                return;
            }
            MyphLogger.info("MQ-CALLBACK ！添加到黑名单中开始");
            String reason = code.getDesc();
            ThirdBlackDto thirdBlack = new ThirdBlackDto();
            thirdBlack.setChannel("系统准入结果存档");
            thirdBlack.setCreateTime(new Date());
            thirdBlack.setCreateUser("MQ_CALLBACK");
            thirdBlack.setDelFlag(Constants.NO_INT);
            thirdBlack.setIsReject(Constants.YES_INT);
            thirdBlack.setModifyUser("MQ_CALLBACK");
            thirdBlack.setMemberName(obj.getName());
            thirdBlack.setIdCard(obj.getIdno());
            thirdBlack.setUpdateTime(new Date());
            thirdBlack.setRespMessage(result.toJSONString());
            // 如果返回结果查询不到对应的描述则把结果保存下来。
            String org = "";
            org = code.name().split("_")[0];
            org = ThirdBlackChannel.getDescByCode(org);
            thirdBlack.setChannel("第三方");
            thirdBlack.setRejectReason(reason);
            thirdBlack.setSrcOrg(org);
            // 存在则不插入
            if (!thirdBlackService.isIdCardExist(obj.getIdno(), "第三方", org)) {
                thirdBlackService.edit(thirdBlack);
                MyphLogger.info("MQ-CALLBACK ！添加到黑名单中结束！");
            }else{
                MyphLogger.info("MQ-CALLBACK ！黑名单已经存在此消息！");
            }
            
            // 设置禁闭期
            // 更新客户信息表，设置禁闭期，在当前时间的基础上加90天
            Date today = new Date();
            DateUtils.addDays(today, Constant.CONFINE_TIME.getCode());

            ServiceResult<MemberInfoDto> memberInfo = memberService.queryInfoByIdCard(applyInfo.getIdCard());
            if (null == memberInfo.getData()) {
                MyphLogger.error(" MQ 客户信息查询失败:" + applyInfo.getIdCard());
                return;
            }
            MyphLogger.info("MQ-CALLBACK ！更新主流程为拒绝开始!");
            // 更新主表子状态为拒绝状态
            applyInfoService.updateSubState(applyInfo.getApplyLoanNo(), ApplyBisStateEnum.REFUSE.getCode());
            // 系统拒绝
            RejectActionDto applyNotifyDto = new RejectActionDto();
            applyNotifyDto.setApplyLoanNo(applyInfo.getApplyLoanNo());
            applyNotifyDto.setOperateUser(applyInfo.getCreateuser());
            applyNotifyDto.setRejectDays(Constant.CONFINE_TIME.getCode());
            applyNotifyDto.setFlowStateEnum(FlowStateEnum.APPLY);
            // 走状态机更新主流程
            ServiceResult serviceResult = facadeFlowStateExchangeService.doAction(applyNotifyDto);
            if (!serviceResult.success()) {
                MyphLogger.error("MQ-CALLBACK调用更新主流程失败！param【" + applyNotifyDto + "】,MESSAGE:{}",
                        serviceResult.getMessage());
            }else{
                MyphLogger.info("MQ-CALLBACK ！更新主流程为拒绝成功！");
            }

        }

    }
}
