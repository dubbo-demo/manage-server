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
import com.myph.member.blacklist.dto.ThirdBlackDto;
import com.myph.member.blacklist.service.ThirdBlackService;

/**
 * 
 * @Title:
 * @Description:Excel转为JavaBean
 * @Author:wanghaib
 * @Since:2016年8月27日
 * @Version:1.1.0
 */
public class ExcelRowToThirdBlack implements ExcelRowToObj<ThirdBlackDto> {
    private ThirdBlackService thirdBlackService;
    private List<String> excelErrorMsgs = new ArrayList<String>();
    private String memberName;
    private String idCard;
    private String channel;
    private String srcOrg;
    private String isReject;
    private String rejectReason;
    private String respMessage;
    private String createTimeStr;
    private Date createTime;
    private Integer rowNumber;

    @Override
    public ThirdBlackDto excelRowToObj(Row row) {
        rowNumber = row.getRowNum() + 1;
        memberName = ExcelUtil.getCellValue(row.getCell(0));
        idCard = ExcelUtil.getCellValue(row.getCell(1));
        channel = ExcelUtil.getCellValue(row.getCell(2));
        srcOrg = ExcelUtil.getCellValue(row.getCell(3));
        isReject = ExcelUtil.getCellValue(row.getCell(4));
        rejectReason = ExcelUtil.getCellValue(row.getCell(5));
        respMessage = ExcelUtil.getCellValue(row.getCell(6));
        createTimeStr = ExcelUtil.getCellValue(row.getCell(7));
        if (null == memberName) {
            excelErrorMsgs.add("行[" + rowNumber + "] 姓名为空");
            return null;
        }
        if (null == idCard) {
            excelErrorMsgs.add("行[" + rowNumber + "] 身份证号为空");
            return null;
        }
        if (null == channel) {
            excelErrorMsgs.add("行[" + rowNumber + "] 数据来源为空");
            return null;
        }
        if (null == srcOrg) {
            excelErrorMsgs.add("行[" + rowNumber + "] 来源名称为空");
            return null;
        }
        if (CommonUtil.CHINA_ID_MAX_LENGTH != idCard.length() && CommonUtil.CHINA_ID_MIN_LENGTH != idCard.length()) {
            excelErrorMsgs.add("行[" + rowNumber + "] 身份证号[" + idCard + "]" + "不正确");
            return null;
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
        if (thirdBlackService.isIdCardExist(idCard, channel, srcOrg)) {
            excelErrorMsgs.add("行[" + rowNumber + "] 身份证号[" + idCard + "]" + "已存在");
            return null;
        }
        // 校验结束
        ThirdBlackDto black = new ThirdBlackDto();
        black.setMemberName(memberName);
        black.setIdCard(idCard);
        black.setChannel(channel);
        black.setSrcOrg(srcOrg);
        if ("是".equals(isReject)) {
            black.setIsReject(Constants.YES_INT);
        } else if ("否".equals(isReject)) {
            black.setIsReject(Constants.NO_INT);
        }
        black.setRejectReason(rejectReason);
        black.setRespMessage(respMessage);
        black.setCreateTime(createTime);
        black.setCreateUser(ShiroUtils.getCurrentUserName());
        return black;
    }

    public ThirdBlackService getThirdBlackService() {
        return thirdBlackService;
    }

    public void setThirdBlackService(ThirdBlackService thirdBlackService) {
        this.thirdBlackService = thirdBlackService;
    }

    public List<String> getExcelErrorMsgs() {
        return excelErrorMsgs;
    }

    public void setExcelErrorMsgs(List<String> excelErrorMsgs) {
        this.excelErrorMsgs = excelErrorMsgs;
    }
}
