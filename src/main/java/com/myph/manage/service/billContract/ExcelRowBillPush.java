package com.myph.manage.service.billContract;

import com.myph.manage.common.util.ExcelRowToObj;
import com.myph.manage.common.util.ExcelUtil;
import com.myph.manage.service.billContract.dto.RepayPlanRequestVo;
import com.myph.member.blacklist.service.InnerBlackService;
import org.apache.poi.ss.usermodel.Row;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author heyx
 * @version V1.0
 * @Title: ExcelRowBillPush
 * @Package: com.myph.manage.service.billContract
 * @company: 麦芽金服
 * @Description: TODO (用一句话描述该文件做什么)
 * @date 2017/3/6
 */
public class ExcelRowBillPush implements ExcelRowToObj<RepayPlanRequestVo> {
    private InnerBlackService innerBlackService;
    private List<String> excelErrorMsgs = new ArrayList<String>();

    private String channelCode;// 渠道:麦芽贷：myd；麦芽普惠：myph
    private String contractNo;// 合同号
    private String term;//	期数
    private String billingNo;//	账单编号
    private String billId;//	账单编号
    private String repayDate;//	还款日期:格式yyyy-MM-dd HH:mm:ss
    private String startPrinBalance;// 期初本金余额
    private String dueFromDate;// 应还日期:格式yyyy-MM-dd HH:mm:ss
    private String dueFromPrin;// 应还本金
    private String dueFromItr;// 应还利息
    private String dueFromAmt;// 当期应还
    private String endPrinBalance;// 期末本金余额
    private String clrRetServiceAmt;// 结清返还服务费
    private String advanceClrAmt;// 提前结清金额
    private String interst;// 罚息
    private String lateFee;// 滞纳金
    private String fee; // 手续费
    private String repayMode;// 还款方式
    private String RepayAmt;// 还款金额
    private String RepayType;// 还款类型
    private String paidAmt;// 已还金额
    private String restDueFrom;// 剩余应还
    private String pushType;//	推送类型:0逾期，1罚息变更，2代扣，3减免，4合并账单，5还款
    private String subtractApplyId;// 减免申请流水	催收系统在申请减免时传的值
    private String executeStatus;// 减免执行状态	0成功，1失败
    private String executeNote;// 减免执行备注
    private String overDueDays;// 逾期天数
    private String status;// 状态:1逾期，2还清

    private Integer rowNumber;

    /**
     * @Title: excelRowToObj
     * @Description: (用一句话描述该方法做什么)
     * @author heyx
     * @date 2017/3/6
     * @version V1.0
     */
    @Override
    public RepayPlanRequestVo excelRowToObj(Row row) {
        rowNumber = row.getRowNum() + 1;

        channelCode = ExcelUtil.getCellValue(row.getCell(0));
        contractNo = ExcelUtil.getCellValue(row.getCell(1));
        term = ExcelUtil.getCellValue(row.getCell(2));
        billingNo = ExcelUtil.getCellValue(row.getCell(3));
        billId = ExcelUtil.getCellValue(row.getCell(4));
        repayDate = ExcelUtil.getCellValue(row.getCell(5));
        startPrinBalance = ExcelUtil.getCellValue(row.getCell(6));
        dueFromDate = ExcelUtil.getCellValue(row.getCell(7));
        dueFromPrin = ExcelUtil.getCellValue(row.getCell(8));
        dueFromItr = ExcelUtil.getCellValue(row.getCell(9));
        dueFromAmt = ExcelUtil.getCellValue(row.getCell(10));
        endPrinBalance = ExcelUtil.getCellValue(row.getCell(11));
        clrRetServiceAmt = ExcelUtil.getCellValue(row.getCell(12));
        advanceClrAmt = ExcelUtil.getCellValue(row.getCell(13));
        interst = ExcelUtil.getCellValue(row.getCell(14));
        lateFee = ExcelUtil.getCellValue(row.getCell(15));
        fee = ExcelUtil.getCellValue(row.getCell(16));
        repayMode = ExcelUtil.getCellValue(row.getCell(17));
        RepayAmt = ExcelUtil.getCellValue(row.getCell(18));
        RepayType = ExcelUtil.getCellValue(row.getCell(19));
        paidAmt = ExcelUtil.getCellValue(row.getCell(20));
        restDueFrom = ExcelUtil.getCellValue(row.getCell(21));
        pushType = ExcelUtil.getCellValue(row.getCell(22));
        subtractApplyId = ExcelUtil.getCellValue(row.getCell(23));
        executeStatus = ExcelUtil.getCellValue(row.getCell(24));
        executeNote = ExcelUtil.getCellValue(row.getCell(25));
        overDueDays = ExcelUtil.getCellValue(row.getCell(26));
        status = ExcelUtil.getCellValue(row.getCell(27));

        if (null == channelCode) {
            excelErrorMsgs.add("行[" + rowNumber + "] 渠道为空");
            return null;
        }
        if (null == contractNo) {
            excelErrorMsgs.add("行[" + rowNumber + "] 合同号为空");
            return null;
        }
        if (null == term) {
            excelErrorMsgs.add("行[" + rowNumber + "] 期数为空");
            return null;
        }
        if (null == billingNo) {
            excelErrorMsgs.add("行[" + rowNumber + "] 账单编号为空");
            return null;
        }
        if (null == billId) {
            excelErrorMsgs.add("行[" + rowNumber + "] 账单id为空");
            return null;
        }
        if (null == dueFromDate) {
            excelErrorMsgs.add("行[" + rowNumber + "] 应还日期为空");
            return null;
        }

        if (null == pushType) {
            excelErrorMsgs.add("行[" + rowNumber + "] 推送类型为空");
            return null;
        }
        if (null == status) {
            excelErrorMsgs.add("行[" + rowNumber + "] 状态为空");
            return null;
        }
        if (null != overDueDays) {
            if (!overDueDays.matches("^[0-9]*$")) {
                excelErrorMsgs.add("行[" + rowNumber + "] 逾期天数[" + overDueDays + "]" + "不正确");
                return null;
            }
        }
        RepayPlanRequestVo dto = new RepayPlanRequestVo();
        dto.setChannelCode(channelCode);
        dto.setContractNo(contractNo);
        if (null != term) {
            dto.setTerm(Integer.parseInt(term));
        }
        dto.setBillingNo(billingNo);
        dto.setBillId(billId);
        dto.setRepayDate(repayDate);
        dto.setStartPrinBalance(new BigDecimal(startPrinBalance));
        dto.setDueFromDate(dueFromDate);
        dto.setDueFromPrin(new BigDecimal(dueFromPrin));
        dto.setDueFromItr(new BigDecimal(dueFromItr));
        dto.setDueFromAmt(new BigDecimal(dueFromAmt));
        dto.setEndPrinBalance(new BigDecimal(endPrinBalance));
        dto.setClrRetServiceAmt(new BigDecimal(clrRetServiceAmt));
        dto.setAdvanceClrAmt(new BigDecimal(advanceClrAmt));
        dto.setInterst(new BigDecimal(interst));
        dto.setLateFees(new BigDecimal(lateFee));
        dto.setFee(new BigDecimal(fee));
        dto.setRepayMode(repayMode);
        dto.setRepayAmt(new BigDecimal(RepayAmt));
        dto.setPaidAmt(new BigDecimal(paidAmt));
        dto.setRestDueFrom(new BigDecimal(restDueFrom));
        dto.setPushType(pushType);
        if (null != overDueDays) {
            dto.setOverDueDays(Integer.parseInt(overDueDays));
        }
        dto.setStatus(status);

        return dto;
    }

    public List<String> getExcelErrorMsgs() {
        return excelErrorMsgs;
    }

    public void setExcelErrorMsgs(List<String> excelErrorMsgs) {
        this.excelErrorMsgs = excelErrorMsgs;
    }

    public InnerBlackService getInnerBlackService() {
        return innerBlackService;
    }

    public void setInnerBlackService(InnerBlackService innerBlackService) {
        this.innerBlackService = innerBlackService;
    }
}
