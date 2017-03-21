/**   
 * @Title: ThirdBlackMqParam.java 
 * @Package: com.myph.manage.param 
 * @company: 麦芽金服
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 罗荣   
 * @date 2016年9月19日 下午7:43:36 
 * @version V1.0   
 */
package com.myph.manage.param;

import com.myph.common.bean.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @ClassName: ThirdBlackMqParam
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 罗荣
 * @date 2016年9月19日 下午7:43:36
 * 
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CsBlackMqParam extends BaseInfo {

    private static final long serialVersionUID = 5064101134536748895L;
    private String 	memberName; //用户名
    private String channel ; //渠道
    private String 	idCard; //身份证号
    private String 	mobile; //手机号
    private String 	userId; //用户ID
    private String 	rejectReason; //黑名单原因
    private String 	createUser; //创建人
    private String createDate; //创建时间

}
