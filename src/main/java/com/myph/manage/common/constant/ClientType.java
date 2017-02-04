/**   
 * @Title: ClientType.java 
 * @Package: com.myph.manage.common.constant 
 * @company: 麦芽金服
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 罗荣   
 * @date 2016年11月10日 下午2:11:12 
 * @version V1.0   
 */
package com.myph.manage.common.constant;

import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;

/** 
 * @ClassName: ClientType 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 罗荣 
 * @date 2016年11月10日 下午2:11:12 
 *  
 */
public enum ClientType {
    /**
     * WEB
     */
    WEB(0, "web"),

    /**
     * APP
     */
    APP(1, "app");

    ClientType(int code, String message) {
        this.message = message;
        this.code = code;
    }

    private String message;

    private int code;

    private static final Map<Integer, String> clientTypeMap = Maps.newHashMap();

    public Integer getCode() {
        return this.code;
    }
    
    private static void init() {
        for (ClientType c : ClientType.values()){
            clientTypeMap.put(c.code, c.message);
        }
    }
    
    public static String getMessage(Integer code){
        if (CollectionUtils.isEmpty(clientTypeMap)) {
            init();
        }
        return clientTypeMap.get(code);
    }
}
