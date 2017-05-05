package com.myph.manage.common.handler;

/**
 * Created by dell on 2017/4/21.
 */
public enum AuditHandlerEnum {
    JIEAN("JieAn", "捷安");

    AuditHandlerEnum(String code, String desc) {

        this.code = code;
        this.desc = desc;
    }

    private String code;

    private String desc;

    public String getCode() {
        // 转成2为长度的code码，不足两位前面补齐0
        return code;
    }

    public String getDesc() {
        return this.desc;
    }
}
