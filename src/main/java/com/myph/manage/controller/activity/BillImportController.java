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
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

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
        try {
            // excel转为bean
            ExcelRowBillPush excelBlack = new ExcelRowBillPush();
            successDatas = ExcelUtil.readExcel(upFile, excelBlack);
            excelErrorMsgs = excelBlack.getExcelErrorMsgs();// 获取校验不通过数据
            if (null == excelErrorMsgs || excelErrorMsgs.isEmpty()) {
                // 组装报文，推送催收接口
                excelErrorMsgs = billRestService.restCS(successDatas);
            }
        } catch (Exception e) {
            result = AjaxResult.ERROR_CODE;
            MyphLogger.error(e, "异常[账单导入：/billImport/impInfo.htm]");
        }
        if (null != excelErrorMsgs && !excelErrorMsgs.isEmpty()) {
            result = AjaxResult.ERROR_CODE;
            model.addAttribute("excelErrorMsgs", excelErrorMsgs);
            MyphLogger.info("====结束账单导入：/billImport/impInfo.htm|导入失败");
        } else {
            model.addAttribute("successDataSize", null == successDatas ? 0 : successDatas.size());
            MyphLogger.debug("successData=============" + successDatas);
            MyphLogger.info("====结束账单导入：/billImport/impInfo.htm|导入成功");
        }
        model.addAttribute("result", result);
        return "/activity/billImport/billImport";
    }

    /**
     * Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
     *
     * @名称 transBeanMap
     * @描述 Bean转Map
     * @返回类型 Map<String,Object>
     * @日期 2017年3月7日 下午5:15:33
     * @创建人 heyx
     * @更新人 heyx
     */
    public static Map<String, Object> transBeanMap(Object obj) {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }

        return map;

    }

}
