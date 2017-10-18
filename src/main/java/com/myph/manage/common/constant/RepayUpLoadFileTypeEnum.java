package com.myph.manage.common.constant;

import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import java.util.Map;

public enum RepayUpLoadFileTypeEnum {
    WITHHOLD(1, "代扣"),
    COMPENSATE(2, "代偿"),
    ADVANCESETTLE(3, "提前结清扣款"),
    REMISSION(4, "减免"),
    CORPORATE(5, "对公还款");

    RepayUpLoadFileTypeEnum(Integer code, String desc) {
        this.desc = desc;
        this.code = code;
    }

    private int code;

    private String desc;

    private static final Map<Integer, String> enumNameMap = Maps.newHashMap();

    private static final Map<Integer, RepayUpLoadFileTypeEnum> enumMap = Maps.newHashMap();

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private static void init() {
        for (RepayUpLoadFileTypeEnum c : RepayUpLoadFileTypeEnum.values()) {
            enumMap.put(c.code, c);
            enumNameMap.put(c.code, c.desc);
        }
    }

    public static String getDescByCode(int code) {
        if (CollectionUtils.isEmpty(enumNameMap)) {
            init();
        }
        return enumNameMap.get(code);
    }

    public static RepayUpLoadFileTypeEnum getEnum(Integer code) {
        if (CollectionUtils.isEmpty(enumMap)) {
            init();
        }
        return enumMap.get(code);
    }
}
