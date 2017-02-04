package com.myph.manage.common.listener;

import org.springframework.web.context.ContextLoaderListener;

/**
 * INFO: info
 * User: zhaokai
 * Date: 2016/8/24 - 18:54
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */

public class CustomContextLoaderListener extends ContextLoaderListener {
    static{
        //设置dubbo使用slf4j来记录日志
        System.setProperty("dubbo.application.logger","slf4j");
    }
}