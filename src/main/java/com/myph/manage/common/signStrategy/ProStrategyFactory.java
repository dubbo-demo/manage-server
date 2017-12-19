package com.myph.manage.common.signStrategy;

import com.myph.manage.common.constant.ProTypeStrategyEnum;
import com.myph.manage.spring.SpringContextHolder;

/**
 * @author heyx
 * @version V1.0
 * @Package: com.myph.manage.common.signStrategy
 * @company: 麦芽金服
 * @Description: 签约策略代理类
 * @date 2017/12/15
 */
public class ProStrategyFactory {

    public static ProTypeStrategy getProTypeStrategy(ProTypeStrategyEnum proTypeStrategyEnum) {
        return SpringContextHolder.getBean(proTypeStrategyEnum.getProTypeStrategy());
    }
}
