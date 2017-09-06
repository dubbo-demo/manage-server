/**
 * @Title: ProductController.java
 * @Package: com.myph.manage.controller.product
 * @company: 麦芽金服
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 罗荣
 * @date 2016年9月21日 下午2:07:47 
 * @version V1.0
 */
package com.myph.manage.controller.reductionRecord;

import com.myph.common.constant.Constants;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.manage.common.util.BeanUtils;
import com.myph.manage.controller.BaseController;
import com.myph.reduction.dto.HkReductionRecordDto;
import com.myph.reduction.param.HkReductionRecordQuery;
import com.myph.reduction.service.HkReductionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 罗荣
 */
@Controller
@RequestMapping("/reductionRecord")
public class ReductionRecordController extends BaseController {
    @Autowired
    HkReductionRecordService hkReductionRecordService;

    @RequestMapping("/queryPageList")
    public String queryPageList(HkReductionRecordQuery query, BasePage page, Model model) {
        ServiceResult<Pagination<HkReductionRecordDto>> result = hkReductionRecordService.queryPageList(query, page);
        model.addAttribute("page", result.getData());
        model.addAttribute("query", query);
        return "/reductionRecord/list";
    }
    @RequestMapping("/download")
    public void download(HkReductionRecordQuery query, Integer total, Model model, HttpServletResponse response) {
        BasePage page = new BasePage();
        page.setPageSize(total);
        ServiceResult<Pagination<HkReductionRecordDto>> result = hkReductionRecordService.queryPageList(query, page);
        String columnNames[] = {
                "合同编号", "期数", "账单编号"
                , "期初本金", "月还本金", "月还利息", "月还款额", "期末本金余额", "提前结清减免", "提前结清金额"
                , "类型", "减免金额", "操作人", "操作时间" };// 列名
        String keys[] = { "contractNo", "periods", "billNo",
                "initialPrincipal","principal", "interest", "reapyAmount", "endPrincipal","returnAmount","aheadAmount",
                "isAdvanceSettle", "initialAmount", "createUser","createTime"};
        String fileName = "还款记录信息" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
        // 获取Excel数据
        List<Map<String, Object>> excelList = getExcelMapList(result.getData().getResult());
        // 导出Excel数据
        exportExcel(response, fileName, columnNames, keys, excelList);
    }
    private List<Map<String,Object>> getExcelMapList(List<HkReductionRecordDto> list){
        List<Map<String, Object>> destList = new ArrayList<Map<String, Object>>();
        if (null == list) {
            return destList;
        }
        Map<String, Object> destMap = null;
        for (HkReductionRecordDto dto:list){
            destMap = BeanUtils.transBeanToMap(dto);
            if(null!=dto.getIsAdvanceSettle()){
                if(dto.getIsAdvanceSettle().equals(Constants.YES_INT)){
                    destMap.put("isAdvanceSettle","当期结清");
                }else{
                    destMap.put("isAdvanceSettle","提前结清减免");
                }
            }
            destMap.put("periods",dto.getRepayPeriod()+"/"+dto.getPeriods());
            destList.add(destMap);
        }
        return destList;
    }
}
