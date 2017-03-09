package com.myph.manage.service.billContract;

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
public class BillPushDto extends BaseInfo {

    private static final long serialVersionUID = -3879576886370386474L;

    private String channelCode;// 渠道:麦芽贷：myd；麦芽普惠：myph
    private String contractNo;// 合同号
    private int term;//	期数
    private String billingNo;//	账单编号
    private String repayDate;//	还款日期:格式yyyy-MM-dd HH:mm:ss
    private BigDecimal startPrinBalance;// 期初本金余额
    private String dueFromDate;// 应还日期:格式yyyy-MM-dd HH:mm:ss
    private BigDecimal dueFromPrin;// 应还本金
    private BigDecimal dueFromItr;// 应还利息
    private BigDecimal dueFromAmt;// 当期应还
    private BigDecimal endPrinBalance;// 期末本金余额
    private BigDecimal clrRetServiceAmt;// 结清返还服务费
    private BigDecimal advanceClrAmt;// 提前结清金额
    private BigDecimal interst;// 罚息
    private BigDecimal lateFee;// 滞纳金
    private BigDecimal fee; // 手续费
    private String repayMode;// 还款方式
    private BigDecimal RepayAmt;// 还款金额
    private BigDecimal paidAmt;// 已还金额
    private BigDecimal restDueFrom;// 剩余应还
    private String pushType;//	推送类型:0逾期，1罚息变更，2代扣，3减免，4合并账单，5还款
    private int overDueDays;// 逾期天数
    private String status;// 状态:1逾期，2还清

}
