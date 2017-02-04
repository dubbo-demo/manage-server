package com.myph.manage.service.member.blacklist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;

import com.myph.common.constant.Constants;
import com.myph.common.util.DateUtils;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.common.util.CommonUtil;
import com.myph.manage.common.util.ExcelRowToObj;
import com.myph.manage.common.util.ExcelUtil;
import com.myph.manage.common.util.StringUtil;
import com.myph.member.blacklist.dto.InnerBlackDto;
import com.myph.member.blacklist.service.InnerBlackService;

/**
 * 
 * @Title:
 * @Description:Excel转为JavaBean
 * @Author:wanghaib
 * @Since:2016年8月27日
 * @Version:1.1.0
 */
public class ExcelRowToInnerBlack implements ExcelRowToObj<InnerBlackDto> {
    private InnerBlackService innerBlackService;
    private List<String> excelErrorMsgs = new ArrayList<String>();
    private String memberName;
    private String phone;
    private String idCard;
    private String registdAddr;
    private String currentAddr;
    private String hasChildren;
    private String hasOverdue;
    private String overdueDays;
    private String createTimeStr;
    private Date createTime;
    private Integer rowNumber;

    @Override
    public InnerBlackDto excelRowToObj(Row row) {
        rowNumber = row.getRowNum() + 1;
        memberName = ExcelUtil.getCellValue(row.getCell(0));
        idCard = ExcelUtil.getCellValue(row.getCell(1));
        phone = ExcelUtil.getCellValue(row.getCell(2));
        registdAddr = ExcelUtil.getCellValue(row.getCell(3));
        currentAddr = ExcelUtil.getCellValue(row.getCell(4));
        hasChildren = ExcelUtil.getCellValue(row.getCell(5));
        hasOverdue = ExcelUtil.getCellValue(row.getCell(6));
        overdueDays = ExcelUtil.getCellValue(row.getCell(7));
        createTimeStr = ExcelUtil.getCellValue(row.getCell(8));
        if (null == memberName) {
            excelErrorMsgs.add("行[" + rowNumber + "] 姓名为空");
            return null;
        }
        if (null == idCard) {
            excelErrorMsgs.add("行[" + rowNumber + "] 身份证号为空");
            return null;
        }
        if (CommonUtil.CHINA_ID_MAX_LENGTH != idCard.length() && CommonUtil.CHINA_ID_MIN_LENGTH != idCard.length()) {
            excelErrorMsgs.add("行[" + rowNumber + "] 身份证号[" + idCard + "]" + "不正确");
            return null;
        }

        if (null != phone) {
            if (!StringUtil.isMobileNO(phone)) {
                excelErrorMsgs.add("行[" + rowNumber + "] 手机号[" + phone + "]" + "不正确");
                return null;
            }
        }
        if (null != overdueDays) {
            if (!overdueDays.matches("^[0-9]*$")) {
                excelErrorMsgs.add("行[" + rowNumber + "] 逾期天数[" + overdueDays + "]" + "不正确");
                return null;
            }
        }
        if (null != createTimeStr) {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_PATTERN);
            try {
                createTime = sdf.parse(createTimeStr);
            } catch (ParseException e) {
                excelErrorMsgs.add("行[" + rowNumber + "] 日期格式[" + createTimeStr + "]不正确" + sdf.toPattern());
                return null;
            }
        } else {
            createTime = new Date();
        }
        if (innerBlackService.isIdCardExist(idCard)) {
            excelErrorMsgs.add("行[" + rowNumber + "] 身份证号[" + idCard + "]" + "已存在");
            return null;
        }
        InnerBlackDto innerBlack = new InnerBlackDto();
        innerBlack.setMemberName(memberName);
        innerBlack.setIdCard(idCard);
        innerBlack.setPhone(phone);
        innerBlack.setRegistdAddr(registdAddr);
        innerBlack.setCurrentAddr(currentAddr);
        if ("有".equals(hasChildren)) {
            innerBlack.setHasChildren(Constants.YES_INT);
        } else if ("无".equals(hasChildren)) {
            innerBlack.setHasChildren(Constants.NO_INT);
        }
        if ("有".equals(hasOverdue) || "是".equals(hasOverdue)) {
            innerBlack.setHasOverdue(Constants.YES_INT);
        } else if ("无".equals(hasOverdue) || "否".equals(hasOverdue)) {
            innerBlack.setHasOverdue(Constants.NO_INT);
        }
        innerBlack.setOverdueDays(null == overdueDays ? null : Integer.parseInt(overdueDays));
        innerBlack.setCreateTime(createTime);
        innerBlack.setCreateUser(ShiroUtils.getCurrentUserName());
        return innerBlack;
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
