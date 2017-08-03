/**
 * @Title: ApproveController.java
 * @Package: com.myph.manage.controller.approve
 * @company: 麦芽金服
 * @Description: 审批管理(用一句话描述该文件做什么)
 * @author 罗荣
 * @date 2016年9月18日 下午5:20:53
 * @version V1.0
 */
package com.myph.manage.controller.loan.frb;

import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.DateUtils;
import com.myph.common.util.SensitiveInfoUtils;
import com.myph.manage.common.util.BeanUtils;
import com.myph.manage.controller.BaseController;
import com.myph.performance.dto.FrbaoLoanMemberDto;
import com.myph.performance.service.FrbaoLoanMemberService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author heyx
 * @version V1.0
 * @Package: com.myph.manage.controller.loan.frb
 * @company: 麦芽金服
 * @Description: 付融宝标的借款人信息
 * @date 2017/7/27
 */
@Controller
@RequestMapping("/frb/loanMember")
public class FrbaoMemberController extends BaseController {
    @Autowired
    private FrbaoLoanMemberService frbaoLoanMemberService;

    public final static String PATH = "/loan";

    public final static String error = "error/500";

    @RequestMapping("/queryPageList")
    public String queryPageList(FrbaoLoanMemberDto param, BasePage page, Model model) {
        MyphLogger.info("列表页参数【{}】", param);
        initQueryDate(param);
        ServiceResult<Pagination<FrbaoLoanMemberDto>> rs = frbaoLoanMemberService.queryListFrbaoPagination(param, page);
        if (rs.success()) {
            for (FrbaoLoanMemberDto dto : rs.getData().getResult()) {
                String addr = dto.getLiveAddr();
                if(StringUtils.isNotBlank(addr)){
                    String[] addrArray = addr.split("-");
                    if(addrArray.length > 0){
                        dto.setLiveProv(addrArray[0]);
                        dto.setLiveCity(addrArray[1]);
                    }
                }
                dto.setIdCard(SensitiveInfoUtils.maskIdCard(dto.getIdCard()));// 隐藏身份证
                dto.setMonthlySalaryArea(getMonthMoney(dto.getMonthlySalary()));
                dto.setBusinessTypeName(getBusinessTypeName(dto.getBusinessType()));
            }
        }
        model.addAttribute("params", param);
        model.addAttribute("page", rs.getData());
        return PATH + "/frb/frbLoanMember";
    }

    @RequestMapping("/exportFinanceInfo")
    public void exportFinanceInfo(HttpServletResponse response, FrbaoLoanMemberDto param) {
        MyphLogger.debug("付融宝借款人信息导出：/loan/frb/loanMember/exportFinanceInfo.htm|param=" + param);
        initQueryDate(param);
        try {
            // 设置参数查询满足条件的所有数据不分页
            List<FrbaoLoanMemberDto> list = frbaoLoanMemberService.queryListFrbao(param).getData();
            for(FrbaoLoanMemberDto dto:list){
                String addr = dto.getLiveAddr();
                if(StringUtils.isNotBlank(addr)){
                    String[] addrArray = addr.split("-");
                    if(addrArray.length > 0){
                        dto.setLiveProv(addrArray[0]);
                        dto.setLiveCity(addrArray[1]);
                    }
                }  
            }
            String columnNames[] = {
                    "合同号", "身份证号码", "用户状态", "是否已认证"
                    , "姓名", "性别", "年龄", "国籍", "省份", "城市", "公司性质"
                    , "职业身份", "工作/经营地点", "经营主业", "房产", "车产"
                    , "月入范围(元)", "工作/经营年限(年)", "帐户名", "开户行"
                    , "帐号", "借款金额", "还款方式", "借款时长", "借款时长单位", "借款描述"
                    , "借款用途", "保障方式", "服务费(元)", "放款金额(元)", "放款时间" };// 列名
            String keys[] = { "contractNo", "idCard", "userState", "isAuthen",
                    "memberName", "sex", "age", "nationality", "liveProv", "liveCity", "companyNatureName",
                    "positionsName", "jobDetailAddr", "businessTypeName", "huoseCondition", "carCondition",
                    "monthlySalary", "jobYear", "openBankMember", "bankTypeName"
                    , "bankCardNo", "contractAmount", "payType", "periods", "timeUnit", "loanPurposes",
                    "loanPurposeName", "guaranteeMode", "serviceRate", "repayMoney", "loanTime" };
            String fileName = "付融宝借款人信息" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
            // 获取Excel数据
            List<Map<String, Object>> excelList = getExcelMapList(list);
            // 导出Excel数据
            exportExcel(response, fileName, columnNames, keys, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[付融宝借款人信息导出：/loan/exportFinanceInfo.htm]");
        }
        MyphLogger.debug("付融宝借款人信息导出：/loan/exportFinanceInfo.htm");
    }

    /**
     * 获取Excel数据
     *
     * @param list
     * @return
     */
    private List<Map<String, Object>> getExcelMapList(List<FrbaoLoanMemberDto> list) {
        List<Map<String, Object>> destList = new ArrayList<Map<String, Object>>();
        if (null == list) {
            return destList;
        }
        Map<String, Object> destMap = null;
        for (FrbaoLoanMemberDto dto : list) {
            destMap = BeanUtils.transBeanToMap(dto);
            Date loanTime = dto.getLoanTime();
            if (null != loanTime) {
                SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_PATTERN);
                destMap.put("loanTime", sdf.format(loanTime));
            }
            destMap.put("monthlySalary", getMonthMoney(dto.getMonthlySalary()));
            destMap.put("businessTypeName",getBusinessTypeName(dto.getBusinessType()));
            destList.add(destMap);
        }
        return destList;
    }

    private String getMonthMoney(Double monthM) {
        if (null == monthM) {
            return "";
        }
        if (monthM < 1500) {
            return "";
        }
        if (monthM >= 1500 && monthM <= 1900) {
            return "1500-1999";
        }
        if (monthM >= 2000 && monthM <= 5999) {
            return "2000-5999";
        }
        if (monthM >= 6000 && monthM <= 7999) {
            return "6000-7999";
        }
        if (monthM >= 8000 && monthM <= 10000) {
            return "8000-10000";
        }
        if (monthM >= 10001 && monthM <= 14999) {
            return "10001-14999";
        }
        return ">15000";
    }

    private String getBusinessTypeName(Long businessType) {
        if (null == businessType || 125L == businessType || 127L == businessType || 130L == businessType
                || 148L == businessType || 134L == businessType) {
            return "个人经营/其它";
        } else if (128L == businessType || 129L == businessType) {
            return "计算机/互联网/通信";
        } else if (123L == businessType || 143L == businessType || 145L == businessType) {
            return "生产/工艺/制造";
        } else if (139L == businessType || 146L == businessType || 147L == businessType) {
            return "医疗/护理/制药";
        } else if (124L == businessType || 136L == businessType || 142L == businessType) {
            return "贸易/消费/制造/营运";
        } else if (141L == businessType) {
            return "文化/广告/传媒";
        } else if (140L == businessType) {
            return "娱乐/艺术/表演";
        } else if (19L == businessType || 126L == businessType || 144L == businessType
                || 131L == businessType || 132L == businessType
                || 137L == businessType || 138L == businessType) {
            return "房地产/服务业/餐饮";
        } else if (135L == businessType) {
            return "教育/培训";
        }
        return "个人经营/其它";
    }
    
    /**
     * 获取前2周时间区间
     * 
     * @param queryDto
     */
    private void initQueryDate(FrbaoLoanMemberDto param) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();
        cal.add(Calendar.WEEK_OF_YEAR, -2);
        Date twoWeekBefore = cal.getTime();

        if (null == param.getStartLoanTime()) {
            param.setStartLoanTime(twoWeekBefore);
        }
        if (null == param.getEndLoanTime()) {
            param.setEndLoanTime(today);
        }
    }
}
