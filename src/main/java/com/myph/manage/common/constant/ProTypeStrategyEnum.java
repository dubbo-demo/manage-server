package com.myph.manage.common.constant;

import com.google.common.collect.Maps;
import com.myph.manage.common.signStrategy.ProTypeStrategy;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author heyx
 * @version V1.0
 * @Package: com.myph.manage.common.constant
 * @company: 麦芽金服
 * @Description: 产品大类
 * @date 2017/12/15
 */
public enum ProTypeStrategyEnum {
    // 利用构造函数传参
    DEFAULT(1, "默认","defaultProTypeStrategy"),
    ZERO(2, "零用贷","");

    private int type;

    private String desc;

    private String proTypeStrategy;

    private static final Map<Integer, ProTypeStrategyEnum> flowStateEnumMap = Maps.newHashMap();

    ProTypeStrategyEnum(int type, String name,String proTypeStrategy) {
        this.desc = name;
        this.type = type;
        this.proTypeStrategy = proTypeStrategy;
    }

    private static void init() {
        for (ProTypeStrategyEnum c : ProTypeStrategyEnum.values()){
            flowStateEnumMap.put(c.type, c);
        }
    }

    public static ProTypeStrategyEnum getEnum(Integer code) {
        if (CollectionUtils.isEmpty(flowStateEnumMap)) {
            init();
        }
        return flowStateEnumMap.get(code);
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getProTypeStrategy() {
        return proTypeStrategy;
    }

}
