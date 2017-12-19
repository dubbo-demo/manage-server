package com.myph.manage.po;

import com.myph.common.bean.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 员工信息DTO
 *
 * @author dell
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class PrintPo extends BaseInfo {

    private static final long serialVersionUID = -8459792496440019138L;

    public PrintPo(){}

    public PrintPo(Integer subState, String applyLoanNo, String loanTime, String contractNo,
            BigDecimal contractAmount, BigDecimal interestAmount, Integer num, BigDecimal periods,
            BigDecimal repayMon,String bankNo,String bankName) {
        this.subState = subState;
        this.applyLoanNo = applyLoanNo;
        this.loanTime = loanTime;
        this.contractNo = contractNo;
        this.contractAmount = contractAmount;
        this.interestAmount = interestAmount;
        this.num = num;
        this.periods = periods;
        this.repayMon = repayMon;
        this.bankNo = bankNo;
        this.bankName = bankName;
    }

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


}
