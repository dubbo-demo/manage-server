package com.myph.manage.permission;

import java.lang.annotation.*;

/**
 * INFO: 服务端完成shiro鉴权功能 ：注解可以加在方法
 * User: zhaokai
 * Date: 2016/9/9 - 13:31
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AuthPermission {
    /**
     * 是否需要验证 ：yes：需要 no：不需要
     * @return
     */
    AuthorityType authType() default AuthorityType.YES;
}