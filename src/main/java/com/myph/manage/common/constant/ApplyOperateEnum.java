package com.myph.manage.common.constant;

public enum ApplyOperateEnum {

    /**
     * message:接待信息新增提交
     */
    RECEPTION_SUBMIT_ADD(1, "接待信息新增提交"),

    /**
     * message:接待信息修改提交
     */
    RECEPTION_SUBMIT_UPDATE(2, "接待信息修改提交"),

    /**
     * message:接待信息新增保存
     */
    RECEPTION_SAVE_ADD(3, "接待信息新增保存"),

    /**
     * message:接待信息修改保存
     */
    RECEPTION_SAVE_UPDATE(4, "接待信息修改保存"),

    /**
     * message:申请件个人信息新增
     */
    APPLYUSER_ADD(1, "申请件个人信息新增"),

    /**
     * message:申请件个人信息修改
     */
    APPLYUSER_UPDATE(2, "申请件个人信息修改");

    ApplyOperateEnum(int code, String message) {
        this.message = message;
        this.code = code;
    }

    private String message;

    private int code;

    public Integer getCode() {
        return this.code;
    }
}
