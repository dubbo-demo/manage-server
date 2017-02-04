package com.myph.manage.controller.performance.auditor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.util.DateUtils;
import com.myph.constant.FlowStateEnum;
import com.myph.manage.common.util.BeanUtils;
import com.myph.manage.controller.BaseController;
import com.myph.performance.dto.AuditorPerformanceDto;
import com.myph.performance.dto.AuditorQueryDto;
import com.myph.performance.service.AuditorPerformanceService;

/**
 * 
 * @ClassName: AuditorPerformanceController
 * @Description: 信审绩效
 * @author 王海波
 * @date 2016年10月24日 上午9:29:54
 *
 */
@Controller
@RequestMapping("/performance/auditor")
public class AuditorPerformanceController extends BaseController {

    @Autowired
    private AuditorPerformanceService auditorPerformanceService;

    @RequestMapping("/firstList")
    public String firstList(Model model, AuditorQueryDto queryDto) {
        MyphLogger.info("开始初审绩效查询：/performance/auditor/firstList.htm|queryDto=" + queryDto);
        initQueryDate(queryDto);// 初始化日期
        List<AuditorPerformanceDto> list = auditorPerformanceService.listFirstAuditor(queryDto).getData();
        model.addAttribute("dataList", list);
        model.addAttribute("state", FlowStateEnum.AUDIT_FIRST);
        model.addAttribute("queryDto", queryDto);
        MyphLogger.info("结束初审绩效查询：/performance/auditor/firstList.htm");
        return "/performance/auditor/audtior_list";
    }

    @RequestMapping("/reviewlist")
    public String reviewlist(Model model, AuditorQueryDto queryDto) {
        MyphLogger.info("开始终审绩效查询：/performance/auditor/reviewlist.htm|queryDto=" + queryDto);
        initQueryDate(queryDto);// 初始化日期
        List<AuditorPerformanceDto> list = auditorPerformanceService.listReviewAuditor(queryDto).getData();
        model.addAttribute("dataList", list);
        model.addAttribute("state", FlowStateEnum.AUDIT_LASTED);
        model.addAttribute("queryDto", queryDto);
        MyphLogger.info("结束终审绩效查询：/performance/auditor/reviewlist.htm");
        return "/performance/auditor/audtior_list";
    }

    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, AuditorQueryDto queryDto) {
        MyphLogger.info("开始绩效导出：/performance/auditor/export.htm|queryDto=" + queryDto);
        try {
            // 设置参数查询满足条件的所有数据不分页
            List<AuditorPerformanceDto> list = null;
            String stateDesc = "";
            if (FlowStateEnum.AUDIT_FIRST.getCode().equals(queryDto.getAuditStage())) {
                list = auditorPerformanceService.listFirstAuditor(queryDto).getData();
                stateDesc = FlowStateEnum.AUDIT_FIRST.getDesc();
            } else {
                list = auditorPerformanceService.listReviewAuditor(queryDto).getData();
                stateDesc = FlowStateEnum.AUDIT_LASTED.getDesc();
            }
            // 默认排序
            Collections.sort(list, new PerformanceComparator(queryDto.getSortField(), queryDto.getSortOrder()));
            String columnNames[] = { "TOP", "员工姓名", "员工编号", "进件数", stateDesc + "通过数", stateDesc + "回退数",
                    stateDesc + "总时长(小时)", stateDesc + "通过率", stateDesc + "回退率", stateDesc + "平均时效" };// 列名
            String keys[] = { "index", "employeeName", "employeeNo", "totalApplyCount", "passedApplyCount",
                    "backApplyCount", "totalAuditTime", "passedApplyRate", "backApplyRate", "avgAuditTime" };
            String fileName = stateDesc + "绩效考核" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
            // 获取Excel数据
            List<Map<String, Object>> excelList = getExcelMapList(list);
            // 导出Excel数据
            exportExcel(response, fileName, columnNames, keys, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[结束信审绩效导出：/performance/auditor/export.htm]");
        }
        MyphLogger.info("结束信审绩效导出：/performance/auditor/export.htm");
    }

    /**
     * 
     * @param srcList
     * @param destList
     * @param keys需转化的属性
     * @Description:dto转化为ExcelMap
     */
    private List<Map<String, Object>> getExcelMapList(List<AuditorPerformanceDto> srcList) {
        List<Map<String, Object>> destList = new ArrayList<Map<String, Object>>();
        if (null == srcList) {
            return destList;
        }
        int index = 1;
        for (AuditorPerformanceDto dto : srcList) {
            Map<String, Object> destMap = BeanUtils.transBeanToMap(dto);
            destMap.put("index", index++);
            int totalApplyCount = dto.getTotalApplyCount();
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
            df.setMaximumFractionDigits(2);
            if (0 == totalApplyCount) {
                destMap.put("passedApplyRate", "0%");
                destMap.put("backApplyRate", "0%");
            } else {
                int passedApplyCount = dto.getPassedApplyCount();
                int backApplyCount = dto.getBackApplyCount();
                destMap.put("passedApplyRate", df.format(passedApplyCount * 100f / totalApplyCount) + '%');
                destMap.put("backApplyRate", df.format(backApplyCount * 100f / totalApplyCount) + '%');
            }
            destList.add(destMap);
        }
        return destList;
    }

    private class PerformanceComparator implements Comparator<AuditorPerformanceDto> {
        // 排序字段
        private Method getMethod;

        private String order;

        PerformanceComparator(String field, String order) {
            this.order = order;
            try {
                PropertyDescriptor pd = new PropertyDescriptor(field, AuditorPerformanceDto.class);
                getMethod = pd.getReadMethod();
            } catch (IntrospectionException e) {
                MyphLogger.error(e, "信审绩效排序属性错误：field=" + field);
                try {
                    getMethod = new PropertyDescriptor("passedApplyCount", AuditorPerformanceDto.class).getReadMethod();
                } catch (IntrospectionException e1) {
                    MyphLogger.error(e, "信审绩效排序属性错误：field=passedApplyCount");
                }
            }
        }

        @Override
        public int compare(AuditorPerformanceDto arg0, AuditorPerformanceDto arg1) {
            long result = 0;
            try {
                result = Long.parseLong(String.valueOf(getMethod.invoke(arg0)))
                        - Long.parseLong(String.valueOf(getMethod.invoke(arg1)));
                // 降序取反
                if (Constants.ORDER_DESC.equals(order)) {
                    result = -result;
                }
            } catch (Exception e) {
                MyphLogger.error(e, "信审绩效排序异常");
            }
            return result > 0 ? 1 : (result == 0 ? 0 : -1);
        }
    }

    /**
     * 
     * @名称 initQueryDate
     * @描述 初始化日期
     * @返回类型 void
     * @日期 2016年10月28日 上午10:30:22
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    private void initQueryDate(AuditorQueryDto queryDto) {
        // 初始化查询进件日期 默认当月
        if (null == queryDto.getApplyTimeStart()) {
            queryDto.setApplyTimeStart(DateUtils.getMonthStart());
        }
        if (null == queryDto.getApplyTimeEnd()) {
            queryDto.setApplyTimeEnd(DateUtils.getToday());
        }
    }
}
