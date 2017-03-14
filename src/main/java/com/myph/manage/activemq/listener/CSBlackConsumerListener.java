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
import com.myph.common.activemq.BaseActivemqListener;
import com.myph.common.constant.Constants;
import com.myph.common.constant.ThirdBlackChannel;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.manage.common.constant.BillPushConstant;
import com.myph.manage.param.CsBlackMqParam;
import com.myph.member.base.dto.MemberInfoDto;
import com.myph.member.base.service.MemberInfoService;
import com.myph.member.blacklist.dto.ThirdBlackDto;
import com.myph.member.blacklist.service.ThirdBlackService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 罗荣
 * @ClassName: ThirdBlackConsumerListener
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016年9月19日 下午6:59:29
 */
public class CSBlackConsumerListener extends BaseActivemqListener {

    @Autowired
    ThirdBlackService thirdBlackService;

    @Autowired
    MemberInfoService memberInfoService;

    /**
     * @Title: onBaseMessage
     * @Description: 接收催收系统发送黑名单mq
     * @author heyx
     * @date 2017/3/10
     * @version V1.0
     */
    @Override
    public void onBaseMessage(Serializable map, String name, String type) {

        MyphLogger.info("MQ 催收系统黑名单信息[" + map + "] name[{}],type[{}]", name, type);
        JSONObject result = JSONObject.parseObject((String) map);
        try {
            if(null == result) {
                return;
            }
            CsBlackMqParam obj = (CsBlackMqParam) JSON.parseObject(result.toString(),
                    CsBlackMqParam.class);
            if(null == obj || null == obj.getChannel()) {
                return;
            } else if(!BillPushConstant.CHANNEL_MYPH.equals(obj.getChannel())) {
                MyphLogger.info("MQ-CS-CALLBACK ！渠道不匹配！parm:{}",result.toJSONString());
                return;
            }
            // 组装第三方黑名单
            ThirdBlackDto thirdBlack = getThirdBlackDto(obj);
            thirdBlack.setRespMessage(result.toJSONString());
            // 插入第三方黑名单
            insertThirdBlackDto(thirdBlack);
        } catch (Exception e) {
            MyphLogger.error("MQ-CS-CALLBACK ！添加到黑名单中异常！",e);
        }

    }

    /**
     * @Description: 插入第三方黑名单
     * @author heyx
     * @date 2017/3/10
     * @version V1.0
     */
    private void insertThirdBlackDto(ThirdBlackDto thirdBlack) {
        if(null == thirdBlack) {
            return;
        }
        // 存在则不插入
        if (!thirdBlackService.isIdCardExist(thirdBlack.getIdCard(), "第三方", ThirdBlackChannel.CS.getDesc())) {
            thirdBlackService.edit(thirdBlack);
            MyphLogger.info("MQ-CS-CALLBACK ！添加到黑名单中结束！");
        }else{
            MyphLogger.info("MQ-CS-CALLBACK ！黑名单已经存在此消息！");
        }
    }
    
    /**
     * @Description: 组装第三方黑名单
     * @author heyx
     * @date 2017/3/10
     * @version V1.0
     */
    private ThirdBlackDto getThirdBlackDto(CsBlackMqParam obj) {
        ThirdBlackDto thirdBlack = new ThirdBlackDto();
        thirdBlack.setChannel(obj.getChannel());
        thirdBlack.setCreateTime(new Date());
        thirdBlack.setCreateUser("MQ_CS_CALLBACK");
        thirdBlack.setIsReject(Constants.YES_INT);
        thirdBlack.setModifyUser("MQ_CS_CALLBACK");
        ServiceResult<MemberInfoDto> memberDto = memberInfoService
                .queryInfoByIdCard(obj.getIdCard());
        if(null != memberDto && null != memberDto.getData()) {
            thirdBlack.setMemberName(memberDto.getData().getMemberName());
        }
        thirdBlack.setIdCard(obj.getIdCard());
        thirdBlack.setUpdateTime(new Date());
        thirdBlack.setChannel("第三方");
        thirdBlack.setRejectReason(obj.getRejectReason());
        thirdBlack.setSrcOrg(ThirdBlackChannel.CS.getDesc());
        return thirdBlack;
    }
}
