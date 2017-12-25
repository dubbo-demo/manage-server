package com.myph.manage.common.signStrategy;

import com.myph.common.constant.PeriodsUnitEnum;
import com.myph.common.util.DateTimeUtil;
import com.myph.common.util.NumberToCN;
import com.myph.contract.dto.JkContractDto;
import com.myph.manage.po.PrintPo;
import com.myph.sign.dto.ContractModelView;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.util.internal.StringUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author heyx
 * @version V1.0
 * @Package: com.myph.manage.common.signStrategy
 * @company: 麦芽金服
 * @Description: 签约策略基础服务
 * @date 2017/12/15
 */
public class BaseProTypeStrategy {
    public static final int numCount = 8;
    public static final Integer twelvePeriods = 12;

    public static final Integer twentyFourPeriods = 24;

    public static final Integer thirtySixPeriods = 36;

    public static final String securityRate = "0.08";// 风险金费率

    public static final BigDecimal prepaymentRateFirst = new BigDecimal("0.05");// 提前还款减免

    public static final BigDecimal prepaymentRateSecond = new BigDecimal("0.03");// 提前还款减免

    public ContractModelView loanProtocolModel(PrintPo printPo, ContractModelView contractModelView) {
        contractModelView.setContractAmount(printPo.getContractAmount());
        // 月还本金 = 合同金额/期数
        BigDecimal principal = printPo.getContractAmount().divide(printPo.getPeriods(), 2, BigDecimal.ROUND_HALF_UP)
                .setScale(0,
                        RoundingMode.DOWN);
        // 月还利息
        BigDecimal interest = printPo.getInterestAmount().divide(printPo.getPeriods(), 2, BigDecimal.ROUND_HALF_UP)
                .setScale(0,
                        RoundingMode.DOWN);
        // 月还款额 = 月还本金 + 月还利息
        BigDecimal reapyAmount = principal.add(interest);

        String principalCN = NumberToCN.number2CNMontrayUnit(reapyAmount);
        contractModelView.setPrincipalCN(principalCN);// 月还款本息数额
        String contractCN = NumberToCN
                .number2CNMontrayUnit(printPo.getContractAmount().setScale(0, BigDecimal.ROUND_HALF_UP));
        contractModelView.setContractCN(contractCN);// 借款本金数额

        String[] repayMoneyArray = NumberToCN
                .numberStrToArray(printPo.getContractAmount().setScale(0, BigDecimal.ROUND_HALF_UP));
        String[] principalArray = NumberToCN.numberStrToArray(reapyAmount.setScale(0, BigDecimal.ROUND_HALF_UP));

        contractModelView.setRepayMoneyArray(repayMoneyArray);// 借款本金数额
        contractModelView.setPrincipalArray(principalArray);// 月还款本息数额

        contractModelView.setRepayMoneyLength(numCount - repayMoneyArray.length);
        contractModelView.setPrincipalLength(numCount - principalArray.length);
        contractModelView.setProductPeriods(printPo.getNum());
        contractModelView.setBankNo(printPo.getBankNo());
        contractModelView.setBankName(printPo.getBankName());
        return contractModelView;
    }

    public ContractModelView counselingManageService(PrintPo printPo, JkContractDto jkContractDto,
            ContractModelView contractModelView) {
        contractModelView.setLoanNo(printPo.getContractNo());
        if (jkContractDto != null && jkContractDto.getContractAmount() != null
                && jkContractDto.getRepayMoney() != null) {
            printPo.setContractAmount(jkContractDto.getContractAmount());
            printPo.setInterestAmount(jkContractDto.getInterestAmount());
            // 费用合计= 合同金额-到手金额
            BigDecimal totalCost = printPo.getContractAmount().subtract(printPo.getRepayMon())
                    .setScale(0, BigDecimal.ROUND_HALF_UP);
            contractModelView.setTotalCost(totalCost);
            contractModelView.setTotalCostCN(NumberToCN.number2CNMontrayUnit(totalCost));
            // 签约金额、合同金额、借款金额
            contractModelView.setContractAmount(printPo.getContractAmount());
            contractModelView.setContractAmountCN(NumberToCN.number2CNMontrayUnit(printPo.getContractAmount()));
            // 风险金
            BigDecimal securityFund = printPo.getRepayMon().multiply(new BigDecimal(securityRate)).setScale(0,
                    BigDecimal.ROUND_HALF_UP);
            contractModelView.setSecurityFund(securityFund);
            contractModelView.setSecurityFundCN(NumberToCN.number2CNMontrayUnit(securityFund));
            // 咨询费、审核费、服务费 = 费用合计 - 风险金额
            BigDecimal serviceMoney = totalCost.subtract(securityFund);
            contractModelView.setServiceMoney(serviceMoney);
            contractModelView.setServiceMoneyCN(NumberToCN.number2CNMontrayUnit(serviceMoney));
        } else {
            // 费用合计= 合同金额-到手金额
            BigDecimal totalCost = printPo.getContractAmount().subtract(printPo.getRepayMon())
                    .setScale(0, BigDecimal.ROUND_HALF_UP);
            contractModelView.setTotalCost(totalCost);
            contractModelView.setTotalCostCN(NumberToCN.number2CNMontrayUnit(totalCost));
            // 签约金额、合同金额、借款金额
            contractModelView.setContractAmount(printPo.getContractAmount());
            contractModelView.setContractAmountCN(NumberToCN.number2CNMontrayUnit(printPo.getContractAmount()));
            // 风险金
            BigDecimal securityFund = printPo.getRepayMon().multiply(new BigDecimal(securityRate)).setScale(0,
                    BigDecimal.ROUND_HALF_UP);
            contractModelView.setSecurityFund(securityFund);
            contractModelView.setSecurityFundCN(NumberToCN.number2CNMontrayUnit(securityFund));
            // 咨询费、审核费、服务费 = 费用合计 - 风险金额
            BigDecimal serviceMoney = totalCost.subtract(securityFund);
            contractModelView.setServiceMoney(serviceMoney);
            contractModelView.setServiceMoneyCN(NumberToCN.number2CNMontrayUnit(serviceMoney));
        }
        return contractModelView;
    }

    public String getAgreeDate(String loanTime,int accumulation,String periodsUnit) {
        if(!StringUtils.isEmpty(periodsUnit)){
            switch (PeriodsUnitEnum.getEnum(periodsUnit)) {
                case MONTH:
                    return DateTimeUtil.getAddMonth(loanTime, accumulation);
                case WEEK:
                    return DateTimeUtil.getAddWeek(loanTime, accumulation);
            }
        }
        return DateTimeUtil.getAddMonth(loanTime, accumulation);// 用户账单还款开始日期
    }
}
