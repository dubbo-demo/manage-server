package com.myph.manage.po;

import com.way.common.bean.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 员工信息DTO
 *
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class PrintPo extends BaseEntity {

    private static final long serialVersionUID = -8459792496440019138L;

    private Integer subState;
    private String applyLoanNo;
    private String contractNo;
    private String loanTime;
    private BigDecimal contractAmount;
    private BigDecimal interestAmount;
    private Integer num;
    private BigDecimal periods;
    private BigDecimal repayMon;
    private String bankNo;
    private String bankName;
    private String periodsUnit;
}
