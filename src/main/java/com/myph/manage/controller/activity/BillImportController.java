package com.myph.manage.controller.activity;

import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.manage.common.util.ExcelUtil;
import com.myph.manage.controller.BaseController;
import com.myph.manage.service.billContract.dto.RepayPlanRequestVo;
import com.myph.manage.service.billContract.ExcelRowBillPush;
import com.myph.manage.service.billContract.service.BillRestService;
import com.myph.member.blacklist.dto.BlackQueryDto;
import com.myph.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author heyx
 * @version V1.0
 * @Title: BillImportController
 * @Package: com.myph.manage.controller.activity
 * @company: 麦芽金服
 * @Description: 逾期账单导入
 * @date 2017/3/16
 */
@Controller
@RequestMapping("/billImport")
public class BillImportController extends BaseController {

    @Autowired
    private BillRestService billRestService;

    /**
     * @param model
     * @return
     * @Description:
     */
    @RequestMapping("/list")
    public String list(Model model, BlackQueryDto queryDto, BasePage basePage) {
        MyphLogger.debug("====开始黑名单查询：/billImport/list.htm|querDto=" + queryDto + "|basePage=" + basePage);
        model.addAttribute("queryDto", queryDto);
        MyphLogger.debug("====结束黑名单查询：/billImport/list.htm|page=" + null);
        return "/activity/billImport/billImportForm";
    }

    /**
     * @Description: 导出模板
     * @author heyx
     * @date 2017/3/23
     * @version V1.0
     */
    @RequestMapping("/exportTemplate")
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response) {
        MyphLogger.debug("开始黑名单导出模板：/thirdBlack/exportTemplate.htm");
        try {
            String columnNames[] = { "渠道","合同号","期数","账单编号","账单id"
                    ,"还款日期","期初本金余额","应还日期","应还本金","应还利息","当期应还"
                    ,"期末本金余额","结清返还服务费","提前结清金额","罚息"
                    ,"滞纳金","*手续费","还款方式","还款金额","还款类型","已还金额","剩余应还","推送类型"
                    ,"减免申请流水","减免执行状态","减免执行备注","逾期天数","状态" };// 列名
            String keys[] = {};
            String fileName = "导入催收账单Template";
            // 获取Excel数据
            List<Map<String, Object>> excelList = new ArrayList<Map<String, Object>>();
            // 导出Excel数据
            exportExcel(response, fileName, columnNames, keys, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[催收账单导出模板：/thirdBlack/exportTemplate.htm]");
        }
        MyphLogger.debug("结束催收账单导出模板：/thirdBlack/exportTemplate.htm");
    }

    /**
     * 导入账单
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/impInfo")
    public String impInfo(Model model, HttpServletRequest request,
            @RequestParam(value = "upFile", required = false) MultipartFile upFile) {
        MyphLogger.info("====开始账单导入：/billImport/impInfo.htm");
        if (null == upFile) {
            return "/activity/billImport/billImport";
        }
        int result = AjaxResult.SUCCESS_CODE;
        List<RepayPlanRequestVo> successDatas = new ArrayList<RepayPlanRequestVo>();
        // 错误记录
        List<String> excelErrorMsgs = null;
        // 检验数据成功
        model.addAttribute("validateType",AjaxResult.SUCCESS_CODE);
        try {
            // excel转为bean
            ExcelRowBillPush excelBlack = new ExcelRowBillPush();
            successDatas = ExcelUtil.readExcel(upFile, excelBlack);
            excelErrorMsgs = excelBlack.getExcelErrorMsgs();// 获取校验不通过数据
            if (null == excelErrorMsgs || excelErrorMsgs.isEmpty()) {
                // 组装报文，推送催收接口
                excelErrorMsgs = billRestService.restCS(successDatas);
            } else {
                // 检验数据失败
                model.addAttribute("validateType", AjaxResult.ERROR_CODE);
            }
        } catch (Exception e) {
            result = AjaxResult.ERROR_CODE;
            MyphLogger.error(e, "异常[账单导入：/billImport/impInfo.htm]");
        }
        if (null != excelErrorMsgs && !excelErrorMsgs.isEmpty()) {
            // 导入数据有错误
            result = AjaxResult.ERROR_CODE;
            model.addAttribute("excelErrorMsgs", excelErrorMsgs);
            MyphLogger.info("====结束账单导入：/billImport/impInfo.htm|导入失败");
        } else if (AjaxResult.SUCCESS_CODE == result) {
            model.addAttribute("successDataSize", null == successDatas ? 0 : successDatas.size());
            MyphLogger.debug("successData=============" + successDatas);
            MyphLogger.info("====结束账单导入：/billImport/impInfo.htm|导入成功");
        }
        model.addAttribute("result", result);
        return "/activity/billImport/billImport";
    }

}
