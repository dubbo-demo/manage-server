package com.myph.manage.rocketmq.listener;

import com.alibaba.fastjson.JSON;
import com.maiya.rocketmq.listener.MessageListener;
import com.maiya.rocketmq.log.RocketMqLogger;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.UnsupportedEncodingException;
import java.util.List;

public class RepaymentResultNotifyTpicMsgListener implements MessageListener {

    @Override
    public boolean onMessage(List<MessageExt> messages, ConsumeConcurrentlyContext Context) {
        for (MessageExt messageExt : messages) {
            String message = null;
            try {
                //接收消息

            } catch (Exception e) {

            }
        }
        return true;
    }

}
