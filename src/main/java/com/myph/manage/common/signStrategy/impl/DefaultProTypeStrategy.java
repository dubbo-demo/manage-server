package com.myph.manage.common.signStrategy.impl;

import com.myph.common.constant.Constants;
import com.myph.common.constant.NumberConstants;
import com.myph.common.util.DateTimeUtil;
import com.myph.common.util.DateUtils;
import com.myph.common.util.NumberToCN;
import com.myph.constant.IsAdvanceSettleEnum;
import com.myph.constant.RepayStateEnum;
import com.myph.contract.dto.JkContractDto;
import com.myph.manage.common.constant.ContractEnum;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.common.signStrategy.BaseProTypeStrategy;
import com.myph.manage.common.signStrategy.ProTypeStrategy;
import com.myph.manage.po.PrintPo;
import com.myph.manage.po.PrintPlanPo;
import com.myph.repaymentPlan.dto.JkRepaymentPlanDto;
import com.myph.sign.dto.ContractModelView;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author heyx
 * @version V1.0
 * @Package: com.myph.manage.common.signStrategy.impl
 * @company: 麦芽金服
 * @Description: 默认签约
 * @date 2017/12/15
 */
@Service("defaultProTypeStrategy")
public class DefaultProTypeStrategy extends BaseProTypeStrategy implements ProTypeStrategy {

    @Override
    public PrintPlanPo getPlan(PrintPo printPo) {
        List<JkRepaymentPlanDto> repayPlans = new ArrayList<JkRepaymentPlanDto>();
        BigDecimal initialCapital = BigDecimal.ZERO;// temp当前期初本金
        BigDecimal endPrincipal = BigDecimal.ZERO;// 当前期期末本金余额
        BigDecimal repayAmount = BigDecimal.ZERO;// 月还款额
        BigDecimal returnAmount = BigDecimal.ZERO;// 结算返还服务费(提前结清减免)
        BigDecimal aheadAmount = BigDecimal.ZERO;// 提前结清金额
        JkRepaymentPlanDto repay = null;
        int accumulation = 0;
        for (int i = printPo.getNum(); i >= 1; i--) {
            accumulation++;
            repay = new JkRepaymentPlanDto();
            repay.setApplyLoanNo(printPo.getApplyLoanNo());
            repay.setContractNo(printPo.getContractNo());
            repay.setRepayState(RepayStateEnum.NO_REPAY.getCode()); // 还款状态
            repay.setCreateTime(DateUtils.getCurrentDateTime());
            repay.setUpdateTime(DateUtils.getCurrentDateTime());
            repay.setCreateUser(ShiroUtils.getCurrentUserName());
            repay.setDelflag(Constants.YES_INT);
            String agreeRepayDate = DateTimeUtil.getAddMonth(printPo.getLoanTime(), accumulation);// 用户账单还款开始日期
            repay.setAgreeRepayDate(DateTimeUtil.convertStringToDate(agreeRepayDate));// 协议还款日期
            repay.setRepayPeriod(new Integer(accumulation));// 期数
            // 月还本金 = 合同金额/期数
            BigDecimal principal = printPo.getContractAmount()
                    .divide(printPo.getPeriods(), 2, BigDecimal.ROUND_HALF_UP);
            // 月还利息
            BigDecimal interest = printPo.getInterestAmount()
                    .divide(printPo.getPeriods(), 2, BigDecimal.ROUND_HALF_UP);
            if (printPo.getNum() == i) {
                BigDecimal p1 = principal.setScale(NumberConstants.NUM_ZERO, RoundingMode.DOWN);
                BigDecimal p2 = principal.subtract(p1);// 小数位金额
                BigDecimal firstPrinciple = p2.multiply(printPo.getPeriods());
                principal = p1.add(firstPrinciple).setScale(NumberConstants.NUM_ZERO, BigDecimal.ROUND_HALF_UP);

                BigDecimal i1 = interest.setScale(NumberConstants.NUM_ZERO, RoundingMode.DOWN);
                BigDecimal i2 = interest.subtract(i1);// 小数位金额
                BigDecimal firstInterest = i2.multiply(printPo.getPeriods());
                interest = i1.add(firstInterest).setScale(NumberConstants.NUM_ZERO, BigDecimal.ROUND_HALF_UP);

                repayAmount = principal.add(interest);
                // 当前期初本金余额
                initialCapital = printPo.getContractAmount();
                // 期末本金余额 = 期初本金余额 - 月还本金
                endPrincipal = initialCapital.subtract(principal);

                repay.setPrincipal(principal);
                repay.setInterest(interest);
                repay.setReapyAmount(repayAmount);
                repay.setInitialPrincipal(initialCapital);
                repay.setEndPrincipal(endPrincipal);
            } else {
                initialCapital = endPrincipal;// 当前期初本金余额
                BigDecimal p1 = principal.setScale(NumberConstants.NUM_ZERO, RoundingMode.DOWN);
                BigDecimal i1 = interest.setScale(NumberConstants.NUM_ZERO, RoundingMode.DOWN);
                endPrincipal = initialCapital.subtract(p1);// 当前期末本金余额
                repay.setPrincipal(principal.setScale(NumberConstants.NUM_ZERO, RoundingMode.DOWN));
                repay.setInterest(interest.setScale(NumberConstants.NUM_ZERO, RoundingMode.DOWN));
                // 月还款额 = 月还本金 + 月还利息
                repayAmount = p1.add(i1);
                repay.setReapyAmount(repayAmount);
                repay.setInitialPrincipal(initialCapital);
                repay.setEndPrincipal(endPrincipal);
            }
            // 新增提前还款计划
            if (twelvePeriods.equals(printPo.getNum())) {
                if (accumulation <= 2) {
                    // 提前还款减免
                    returnAmount = printPo.getRepayMon().multiply(prepaymentRateFirst)
                            .setScale(NumberConstants.NUM_ZERO, BigDecimal.ROUND_HALF_UP);
                    repay.setReturnAmount(returnAmount);
                    // 提前还款金额=月还款额+期末本金余额-提前结清减免
                    aheadAmount = repayAmount.add(endPrincipal).subtract(returnAmount);
                }
                if (accumulation <= 4 && accumulation > 2) {
                    returnAmount = printPo.getRepayMon().multiply(prepaymentRateSecond)
                            .setScale(NumberConstants.NUM_ZERO, BigDecimal.ROUND_HALF_UP);
                    repay.setReturnAmount(returnAmount);
                    aheadAmount = repayAmount.add(endPrincipal).subtract(returnAmount);
                }
                if (accumulation > 4) {
                    // 提前还款金额=月还款额+期末本金余额-提前结清减免
                    aheadAmount = repayAmount.add(endPrincipal);
                }
            }
            if (twentyFourPeriods.equals(printPo.getNum()) || thirtySixPeriods.equals(printPo.getNum())) {
                if (accumulation <= 3) {
                    returnAmount = printPo.getRepayMon().multiply(prepaymentRateFirst)
                            .setScale(NumberConstants.NUM_ZERO, BigDecimal.ROUND_HALF_UP);
                    repay.setReturnAmount(returnAmount);
                    aheadAmount = repayAmount.add(endPrincipal).subtract(returnAmount);
                }
                if (accumulation <= 6 && accumulation > 3) {
                    returnAmount = printPo.getRepayMon().multiply(prepaymentRateSecond)
                            .setScale(NumberConstants.NUM_ZERO, BigDecimal.ROUND_HALF_UP);
                    repay.setReturnAmount(returnAmount);
                    aheadAmount = repayAmount.add(endPrincipal).subtract(returnAmount);
                }
                if (accumulation > 6) {
                    // 提前还款金额=月还款额+期末本金余额-提前结清减免
                    aheadAmount = repayAmount.add(endPrincipal);
                }
            }
            repay.setAheadAmount(aheadAmount);
            repay.setIsEffective(IsAdvanceSettleEnum.NO.getCode());
            String str = String.format("%02d", accumulation);
            repay.setBillNo(printPo.getContractNo() + str);
            repayPlans.add(repay);
        }
        PrintPlanPo resultPo = new PrintPlanPo();
        resultPo.setRepayAmount(repayAmount);
        resultPo.setRepayPlans(repayPlans);
        resultPo.setModelName(ContractEnum.REPAYMENT_REMINDER.getContarctName()); // 模板名称
        return resultPo;
    }

    /**
     * 借款协议
     *
     * @param printPo
     * @return
     */
    @Override
    public ContractModelView loanProtocolModel(PrintPo printPo, ContractModelView contractModelView) {
        contractModelView.setModelName(ContractEnum.CREDIT_LOAN_PROTOCOL.getContarctName()); // 模板name
        return super.loanProtocolModel(printPo,contractModelView);
    }

    /**
     * @Description: 信用咨询及管理服务协议
     * @author heyx
     * @date 2017/12/18
     * @version V1.0
     */
    @Override
    public ContractModelView counselingManageService(PrintPo printPo, JkContractDto jkContractDto,
            ContractModelView contractModelView) {
        contractModelView.setModelName(ContractEnum.CREDIT_COUNSELING_MANAGE_SERVICE.getContarctName()); // 模板name
        return super.counselingManageService(printPo,jkContractDto,contractModelView);
    }

    /**
     * @Description: 签约核查表
     * @author heyx
     * @date 2017/12/18
     * @version V1.0
     */
    @Override
    public ContractModelView contractVerificationForm(ContractModelView contractModelView) {
        contractModelView.setModelName(ContractEnum.CUSTOMER_CONTRACT_VERIFICATION_FORM.getContarctName()); // 模板name
        return contractModelView;
    }

    /**
     * @Description: 富友-麦芽金服数据科技有限公司专用账户协议
     * @author heyx
     * @date 2017/12/18
     * @version V1.0
     */
    @Override
    public ContractModelView accountSpecialProtocol(ContractModelView contractModelView) {
        contractModelView.setModelName(ContractEnum.FY_ACCOUNT_SPECIAL_PROTOCOL.getContarctName()); // 模板name
        return contractModelView;
    }

    /**
     * @Description: 委托扣款授权书
     * @author heyx
     * @date 2017/12/18
     * @version V1.0
     */
    @Override
    public ContractModelView entrustDebitAuthorization(ContractModelView contractModelView) {
        contractModelView.setModelName(ContractEnum.ENTRUST_DEBIT_AUTHORIZATION.getContarctName()); // 模板name
        return contractModelView;
    }
}
