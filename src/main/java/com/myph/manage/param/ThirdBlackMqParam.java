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
public class ThirdBlackMqParam extends BaseInfo {

    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = -8510875256705579879L;
    private String name;
    private String idno;
    private String mobile;

}
