package com.myph.manage.common.constant;

/**
 * @Title:
 * @Description: 账单推送结果
 * @author heyx
 * @date 2017/3/8
 * @version V1.0
 */
public enum BillPushEnum {
    /**
     * message:账单推送状态: 0失败 1：成功
     */
    ERROR(0, "失败"),SUCCESS(1, "成功")
    ;

    BillPushEnum(int code, String message) {
        this.message = message;
        this.code = code;
    }

    private String message;

    private int code;

    public Integer getCode() {
        return this.code;
    }
}
