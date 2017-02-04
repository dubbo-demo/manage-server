/**   
 * @Title: Constant.java 
 * @Package: com.myph.manage.common.constant 
 * @company: 麦芽金服
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 罗荣   
 * @date 2016年9月14日 上午11:29:29 
 * @version V1.0   
 */
package com.myph.manage.common.constant;

/** 
 * @ClassName: Constant 
 * @Description: 普通常量定义(这里用一句话描述这个类的作用) 
 * @author 罗荣 
 * @date 2016年9月14日 上午11:29:29 
 *  
 */
public enum Constant {
    CONFINE_TIME(90, "90天的禁闭期");

    Constant(int code, String message) {
        this.message = message;
        this.code = code;
    }

    private String message;

    private int code;

    public Integer getCode() {
        return this.code;
    }
    public String getMessage() {
        return this.message;
    }
}
