package com.myph.manage.common.util;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * INFO: info User: zhaokai Date: 2016/8/24 - 14:30 Version: 1.0 History:
 * <p>
 * 如果有修改过程，请记录
 * </P>
 */
@Data
@Component
public class MYPHConfigUtils {

    @Value("${auditReport_url}")
    private String auditReport_url;

    @Value("${rest.contract.bill.url}")
    private String restContractBillUrl;

    @Value("${rest.repay.plan.url}")
    private String restRepayPlanUrl;

}
