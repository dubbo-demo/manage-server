package com.myph.manage.common.handler;

/**
 * Created by dell on 2017/4/21.
 */
public abstract class AuditHandler {
    /**
     * 持有下一个处理请求的对象
     */
    protected AuditHandler auditHander = null;

    /**
     * 取值方法
     */
    public AuditHandler getHandler() {
        return auditHander;
    }

    /**
     * 设置下一个处理请求的对象
     */
    public void setHandler(AuditHandler hander) {
        this.auditHander = hander;
    }

    /**
     * @Description: 征信方法
     * @author heyx
     * @date 2017/4/21
     * @version V1.0
     */
    public abstract HandlerResultDto audit(HandlerParmDto t);

}
