package com.myph.manage.service.billContract.dto;

import com.myph.common.bean.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author heyx
 * @version V1.0
 * @Title: BillPushDto
 * @Package: com.myph.manage.service.billContract
 * @company: 麦芽金服
 * @Description: TODO (用一句话描述该文件做什么)
 * @date 2017/3/6
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RepayPlanRequestVo extends BaseInfo {

    private static final long serialVersionUID = -3879576886370386474L;

    private String channelCode;

    private String contractNo;

    /**
     * 期数
     */
    private Integer term;
    /**
     * 账单编号
     */
    private String billingNo;
    private String billId;
    /**
     * 还款日期
     */
    private String repayDate;
    /**
     * 期初本金余额
     */
    private BigDecimal startPrinBalance;
    /**
     * 应还日期
     */
    private String dueFromDate;
    /**
     * 应还本金
     */
    private BigDecimal dueFromPrin;
    /**
     * 应还利息
     */
    private BigDecimal dueFromItr;
    /**
     * 应还金额
     */
    private BigDecimal dueFromAmt;
    /**
     * 期末本金余额
     */
    private BigDecimal endPrinBalance;
    /**
     * 结清返还服务费
     */
    private BigDecimal clrRetServiceAmt;
    /**
     * 提前结清金额
     */
    private BigDecimal advanceClrAmt;
    /**
     * 滞纳金
     */
    private BigDecimal lateFees;
    /**
     * 罚息
     */
    private BigDecimal interst;
    /**
     * 手续费
     */
    private BigDecimal fee;
    /**
     * 还款方式
     */
    private String repayMode;
    /**
     * 还款金额
     */
    private BigDecimal RepayAmt;
    /**
     * 已还金额
     */
    private BigDecimal paidAmt;
    /**
     * 剩余应还
     */
    private BigDecimal restDueFrom;
    /**
     * 逾期天数
     */
    private Integer overDueDays;
    /**
     * 推送类型
     */
    private String pushType;
    /**
     * 状态
     */
    private String status;

}
