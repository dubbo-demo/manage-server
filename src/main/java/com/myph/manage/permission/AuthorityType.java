package com.myph.manage.permission;

/**
 * INFO: 注解到 controller的方法上  标识当前方式是否需要验证用户是否登录
 * User: zhaokai
 * Date: 2016/9/9 - 13:31
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
public enum AuthorityType {
    //用户必须要验证 YES 不要验证  no：不需要验证
    YES,NO;
}