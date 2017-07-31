package com.myph.manage.controller.loan.frb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.myph.apply.service.ApplyInfoService;
import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.DateUtils;
import com.myph.manage.common.util.BeanUtils;
import com.myph.manage.controller.BaseController;
import com.myph.performance.dto.FrbTargetDto;
import com.myph.performance.param.FrbTargetQueryParam;
import com.myph.performance.service.FrbTargetService;

@Controller
@RequestMapping("/loan/frb")
public class FrbTargetController extends BaseController{

    @Autowired
    ApplyInfoService applyInfoService;
    
    @Autowired
    private FrbTargetService frbTargetService;


    @RequestMapping("/queryPageList")
    public String queryPageList(FrbTargetQueryParam param, BasePage page, Model model) {
        MyphLogger.info("付融宝标的信息-列表页参数【{}】",param);
        ServiceResult<Pagination<FrbTargetDto>> rs = frbTargetService.queryPageList(param, page);
        model.addAttribute("params", param);
        model.addAttribute("page", rs.getData());
        return "/loan/frb/frbTarget";
    }
    
    @RequestMapping("/exportInfo")
    public void exportInfo( HttpServletResponse response,FrbTargetQueryParam param) {
        MyphLogger.debug("付融宝标的信息导出：/loan/frb/exportInfo.htm|param=" + param);
        try {
            // 设置参数查询满足条件的所有数据不分页
            List<FrbTargetDto> list = frbTargetService.queryFrbTargetInfo(param).getData();
            String columnNames[] = {"合同编号", "身份证号码", "借款金额","还款方式", "借款时长", "借款时长单位", "借款描述", 
                    "借款用途", "保障方式", "服务费（代扣金额）", "放款金额" };// 列名
            String keys[] = { "contractNo","idCard","contractAmount","payMethod","periods","periodsUnit","loanPurposes",
                    "purpose","supportMethod","serviceRate","repayMoney" };
            String fileName = "付融宝标的信息" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
            // 获取Excel数据
            List<Map<String, Object>> excelList = getExcelMapList(list);
            // 导出Excel数据
            exportExcel(response, fileName, columnNames, keys, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[结束付融宝标的信息导出：/loan/frb/exportInfo.htm]");
        }
        MyphLogger.debug("结束付融宝标的信息导出：/loan/frb/exportInfo.htm");
    }
    
    /**
     * 获取Excel数据
     * 
     * @param list
     * @return
     */
    private List<Map<String, Object>> getExcelMapList(List<FrbTargetDto> list) {
        List<Map<String, Object>> destList = new ArrayList<Map<String, Object>>();
        if (null == list) {
            return destList;
        }
        Map<String, Object> destMap = null;
        for (FrbTargetDto dto : list) {
            destMap = BeanUtils.transBeanToMap(dto);
            destMap.put("payMethod", "等额本息");
            destMap.put("purpose", "现金贷");
            destMap.put("supportMethod", "征信信用");
            destList.add(destMap);
        }
        return destList;
    }
}
