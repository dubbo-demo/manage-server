package com.myph.manage.rocketmq.listener;

import com.alibaba.fastjson.JSON;
import com.maiya.rocketmq.listener.MessageListener;
import com.myph.common.constant.ChannelEnum;
import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.constant.HkBIllRecordStateEnum;
import com.myph.constant.IsAdvanceSettleEnum;
import com.myph.hkrecord.dto.HkBillRepayRecordDto;
import com.myph.hkrecord.service.HkBillRepayRecordService;
import com.myph.manage.common.constant.Constant;
import com.myph.repayManMade.service.RepayManMadeService;
import com.myph.repaymentPlan.dto.JkRepaymentPlanDto;
import com.myph.repaymentPlan.service.JkRepaymentPlanService;
import com.repayment.collectionTask.dto.PayResultDkMQ;
import com.repayment.lock.service.RepaymentLockService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class RepaymentResultNotifyTpicMsgListener implements MessageListener {

    @Autowired
    RepayManMadeService repayManMadeService;

    @Autowired
    JkRepaymentPlanService jkRepaymentPlanService;

    @Lazy
    @Autowired
    RepaymentLockService repaymentLockService;

    @Autowired
    HkBillRepayRecordService hkBillRepayRecordService;

    @Override
    public boolean onMessage(List<MessageExt> messages, ConsumeConcurrentlyContext Context) {
        for (MessageExt messageExt : messages) {
            String message = null;
            PayResultDkMQ payResultDto = null;
            try {
                //接收消息
                message = new String(messageExt.getBody(), "UTF-8");
                // 转换字符串成dto
                payResultDto = JSON.parseObject(message, PayResultDkMQ.class);
                // 不在业务方处理锁中
                if (repaymentLockService
                        .checkLockDebitBill(ChannelEnum.MYPH.getCode(),payResultDto.getBillId())) {
                    // 加业务方接受还款中心，处理锁
                    repaymentLockService.lockDebitBill(ChannelEnum.MYPH.getCode(),payResultDto.getBillId());
                    MyphLogger.info("普惠接收还款中心代扣结果RepaymentResultNotifyTpicMsgListener,开始消费消息: " + message);
                    HkBillRepayRecordDto dto = new HkBillRepayRecordDto();
                    dto.setBillNo(payResultDto.getBillNo());
                    // 获取最新还款记录
                    ServiceResult<HkBillRepayRecordDto> repayDto = repayManMadeService.validateRepayInfo(dto);
                    if (!repayDto.success()) {
                        MyphLogger.error("普惠接收还款中心代扣结果RepaymentResultNotifyTpicMsgListener异常,找不到账单，parm{}", messages);
                        return true;
                    }
                    BeanUtils.copyProperties(repayDto, dto);
                    //更新还款记录状态
                    if(payResultDto.getTradeStatus().equals(Constants.YES)) {
                        dto.setState(HkBIllRecordStateEnum.SUCESS.getCode());
                    } else {
                        dto.setState(HkBIllRecordStateEnum.FALSE.getCode());
                        hkBillRepayRecordService.updateStateByBillNo(dto);
                        MyphLogger.info("普惠接收还款中心代扣，parm{},修改还款记录状态为失败 " + message);
                        return true;
                    }
                    dto.setBillId(payResultDto.getBillId());
                    JkRepaymentPlanDto jpDto = jkRepaymentPlanService.queryById(payResultDto.getBillId());

                    // 金额大于账单金额，提前结清代扣
                    if (dto.getIsAdvanceSettle().equals(IsAdvanceSettleEnum.YES.getCode())) {
                        repayManMadeService.splitAdvanceSettle(dto);
                    } else {
                        // 人工代扣
                        repayManMadeService.splitRepay(dto);
                    }
                    MyphLogger.info("普惠接收还款中心代扣结果RepaymentResultNotifyTpicMsgListener,消费成功: " + message);
                }

            } catch (UnsupportedEncodingException e) {
                MyphLogger.error("普惠接收还款中心代扣结果RepaymentResultNotifyTpicMsgListener", e);
                return true;
            } catch (Exception e) {
                MyphLogger.error("普惠接收还款中心代扣结果RepaymentResultNotifyTpicMsgListener,messageExt = " + JSON
                        .toJSONString(messageExt) + " message=" + message, e);
                return true;
            } finally {
                // 加业务方接受还款中心，释放锁
                repaymentLockService.lockDebitBill(ChannelEnum.MYPH.getCode(),payResultDto.getBillId());
            }
        }
        return true;
    }

}
