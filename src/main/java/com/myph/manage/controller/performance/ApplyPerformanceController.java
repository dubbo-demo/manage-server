/**   
 * @Title: JkApplyAuditController.java 
 * @Package: com.myph.manage.audit.controller
 * @company: 麦芽金服
 * @Description: TODO
 * @date 2016年9月20日 下午9:17:42 
 * @version V1.0   
 */
package com.myph.manage.controller.performance;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.common.util.DateUtils;
import com.myph.constant.FlowStateEnum;
import com.myph.employee.constants.EmployeeMsg;
import com.myph.employee.constants.EmployeeMsg.ORGANIZATION_TYPE;
import com.myph.employee.dto.EmployeeDetailDto;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.employee.dto.EmpDetailDto;
import com.myph.manage.common.util.BeanUtils;
import com.myph.manage.controller.BaseController;
import com.myph.performance.constant.DecimalFormartConstant;
import com.myph.performance.constant.ExportConstant;
import com.myph.performance.dto.NationwideDto;
import com.myph.performance.service.NationwideStoreService;
import com.myph.performance.service.StoreCustomerService;
import com.myph.position.dto.PositionDto;
import com.myph.position.service.PositionService;

/**
 * 
 * @ClassName: ApplyPerformanceController
 * @Description: 全国门店绩效，客服绩效
 * @author heyx
 * @date 2016年10月19日 上午10:30:50
 *
 */
@Controller
@RequestMapping("/performance")
public class ApplyPerformanceController extends BaseController {

    @Autowired
    private StoreCustomerService storeCustomerService;

    @Autowired
    private NationwideStoreService nationwideStoreService;

    @Autowired
    private PositionService positionService;
    @Autowired
    private EmployeeInfoService employeeInfoService;

    /**
     * 
     * @throws Exception
     * @名称 allStore
     * @描述 全国门店绩效
     * @返回类型 String
     * @日期 2016年9月26日 下午4:01:16
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    @RequestMapping("/allStore")
    public String allStore(Model model, NationwideDto query) throws Exception {
        MyphLogger.info("ApplyPerformanceController.allStore 输入参数{}", query.toString());
        EmployeeInfoDto user = ShiroUtils.getCurrentUser();
        EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
        if (null == query.getSignTimeStart() && null == query.getSignTimeEnd()) {
            query.setSignTimeStart(DateUtils.getMonthStart());
            query.setSignTimeEnd(DateUtils.getCurrentDateTime());
        }
        model.addAttribute("user", user);
        model.addAttribute("empDetail", empDetail);
        // 1:大区
        if (ORGANIZATION_TYPE.REGION_TYPE.toNumber() == user.getOrgType()) {
            query.setAreaId(empDetail.getRegionId());
        }
        // 2:门店
        if (ORGANIZATION_TYPE.STORE_TYPE.toNumber() == user.getOrgType()) {
            query.setStoreId(empDetail.getStoreId());
        }
        model.addAttribute("query", query);
        ServiceResult<List<NationwideDto>> list = nationwideStoreService.listStoreInfo(query);
        model.addAttribute("data", list.getData());
        return "/performance/allstore_list";
    }

    /**
     * 
     * @名称 storeCustomer
     * @描述 客服绩效
     * @返回类型 String
     * @日期 2016年9月26日 下午4:01:16
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    @RequestMapping("/storeCustomer")
    public String storeCustomer(Model model, NationwideDto query) {
        MyphLogger.info("客服绩效 ApplyPerformanceController.storeCustomer 输入参数{}", query.toString());
        EmployeeInfoDto user = ShiroUtils.getCurrentUser();
        EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
        if (null == query.getSignTimeStart() && null == query.getSignTimeEnd()) {
            query.setSignTimeStart(DateUtils.getMonthStart());
            query.setSignTimeEnd(DateUtils.getCurrentDateTime());
        }
        // 1:大区
        if (ORGANIZATION_TYPE.REGION_TYPE.toNumber() == user.getOrgType()) {
            query.setAreaId(empDetail.getRegionId());
        }
        // 2:门店
        if (ORGANIZATION_TYPE.STORE_TYPE.toNumber() == user.getOrgType()) {
            query.setStoreId(empDetail.getStoreId());
        }
        ServiceResult<List<NationwideDto>> list = storeCustomerService.listStoreInfo(query);
        List<NationwideDto> result = getAllCustomer(user, empDetail, list.getData(), query);
        model.addAttribute("query", query);
        model.addAttribute("user", user);
        model.addAttribute("empDetail", empDetail);
        model.addAttribute("data", result);
        return "/performance/storecustomer_list";
    }

    /**
     * 
     * @名称 getAllCustomer
     * @描述 查询所有客服
     * @返回类型 List<NationwideDto>
     * @日期 2016年11月11日 下午2:25:43
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    private List<NationwideDto> getAllCustomer(EmployeeInfoDto user, EmpDetailDto empDetail, List<NationwideDto> list,
            NationwideDto query) {
        Map<Long, NationwideDto> map = listToMap(list);
        Long orgId = null;
        int orgType = user.getOrgType();
        if (null != user && null != empDetail) {
            // 总部 查询所有
            if (EmployeeMsg.ORGANIZATION_TYPE.HQ_TYPE.toNumber() == orgType) {
                orgId = null;
            }
            // 大区
            else if (EmployeeMsg.ORGANIZATION_TYPE.REGION_TYPE.toNumber() == orgType) {
                orgId = empDetail.getRegionId();
            }
            // 门店
            else {
                orgId = empDetail.getStoreId();
            }
        }
        Set<Long> positionIds = positionService.getPositionIdsByPosition(EmployeeMsg.POSITION.CUSTOMERSERVICE).getData();
        if (CollectionUtils.isEmpty(positionIds)) {
            return list;
        }
        List<EmployeeDetailDto> auditorList =new ArrayList<EmployeeDetailDto>();
        for(Long positionId :positionIds){
            EmployeeDetailDto equery = new EmployeeDetailDto();
            equery.setPositionId(positionId);
            equery.setOrgId(orgId);
            if (null != query.getCustomerServiceId()) {
                equery.setId(query.getCustomerServiceId());
            }
            equery.setEmployeeName(query.getEmployeeName());
            List<EmployeeDetailDto> tempList = employeeInfoService.queryEmployeeInfoList(equery).getData();
            auditorList.addAll(tempList);
        }

        if (CollectionUtils.isEmpty(auditorList)) {
            return list;
        }
        for (EmployeeDetailDto employee : auditorList) {
            if (null == map.get(employee.getId())) {
                NationwideDto dto = new NationwideDto();
                dto.setCustomerServiceId(employee.getId());
                dto.setEmployeeName(employee.getEmployeeName());
                list.add(dto);
            }
        }
        return list;
    }

    /**
     * 
     * @名称 listToMap
     * @描述 list转为Map
     * @返回类型 ImmutableMap<Long,AuditorPerformanceDto>
     * @日期 2016年11月8日 下午6:11:07
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    private ImmutableMap<Long, NationwideDto> listToMap(List<NationwideDto> auditList) {
        return Maps.uniqueIndex(auditList, new Function<NationwideDto, Long>() {
            @Override
            public Long apply(NationwideDto input) {
                return null != input.getCustomerServiceId() ? input.getCustomerServiceId() : -1;
            }

        });
    }

    /**
     * 
     * @名称 bmStore
     * @描述 门店业务员绩效
     * @返回类型 String
     * @日期 2016年9月26日 下午4:01:16
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    @RequestMapping("/bmStore")
    public String bmStore(Model model, NationwideDto query) {
        MyphLogger.info("门店业务员绩效ApplyPerformanceController.allStore 输入参数{}", query.toString());
        query.setState(FlowStateEnum.FINISH.getCode());
        model.addAttribute("query", query);
        ServiceResult<List<NationwideDto>> list = nationwideStoreService.listBmInfo(query);
        model.addAttribute("data", list.getData());
        return "/performance/store_list";
    }

    /**
     * 
     * @名称 allStoreExport
     * @描述 全国门店绩效导出
     * @返回类型 void
     * @日期 2016年10月26日 下午2:57:54
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    @RequestMapping("/allStoreExport")
    public void allStoreExport(HttpServletRequest request, HttpServletResponse response, NationwideDto query) {
        MyphLogger.info("开始绩效导出：/performance/allStoreExport.htm");
        try {
            EmployeeInfoDto user = ShiroUtils.getCurrentUser();
            EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
            // 1:大区
            if (ORGANIZATION_TYPE.REGION_TYPE.toNumber() == user.getOrgType()) {
                query.setAreaId(empDetail.getRegionId());
                query.setStoreId(empDetail.getStoreId());
            }
            // 2:门店
            if (ORGANIZATION_TYPE.STORE_TYPE.toNumber() == user.getOrgType()) {
                query.setStoreId(empDetail.getStoreId());
            }
            String fileName = ExportConstant.ALLSTORE_FILENAME
                    + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
            List<NationwideDto> list = nationwideStoreService.listStoreInfo(query).getData();
            if (null != list) {
                // 默认排序
                Collections.sort(list, new performanceComparator(query.getSortField(), query.getSortOrder()));
            }
            // 获取Excel数据
            List<Map<String, Object>> excelList = getExcelMapList(list);
            // 导出Excel数据
            exportExcel(response, fileName, ExportConstant.ALLSTORE_COLUMNNAMES, ExportConstant.ALLSTORE_KEYS,
                    excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[结束门店总绩效导出：/performance/allStoreExport.htm]");
        }
        MyphLogger.info("结束门店总绩效导出：/performance/storeCustomerExport.htm");
    }

    /**
     * list排序
     * 
     * @ClassName: performanceComparator
     * @Description: TODO(这里用一句话描述这个类的作用)
     * @author heyx
     * @date 2016年11月11日 下午2:27:44
     *
     */
    private class performanceComparator implements Comparator<NationwideDto> {
        // 排序字段
        private Method getMethod;

        private String order;

        performanceComparator(String field, String order) {
            this.order = order;
            try {
                PropertyDescriptor pd = new PropertyDescriptor(field, NationwideDto.class);
                getMethod = pd.getReadMethod();
            } catch (IntrospectionException e) {
                MyphLogger.error(e, "绩效排序属性错误：field=" + field);
                try {
                    getMethod = new PropertyDescriptor("sumRepayMoney", NationwideDto.class).getReadMethod();
                } catch (IntrospectionException e1) {
                    MyphLogger.error(e, "绩效排序属性错误：field=sumRepayMoney");
                }
            }
        }

        @Override
        public int compare(NationwideDto arg0, NationwideDto arg1) {
            double result = 0;
            try {
                result = Double.parseDouble(String.valueOf(getMethod.invoke(arg0)))
                        - Double.parseDouble(String.valueOf(getMethod.invoke(arg1)));
                // 降序取反
                if (Constants.ORDER_DESC.equals(order)) {
                    result = 0 - result;
                }
            } catch (Exception e) {
                MyphLogger.error(e, "绩效排序异常");
            }
            return result > 0 ? 1 : (result == 0 ? 0 : -1);
        }
    }

    /**
     * 
     * @名称 storeCustomerExport
     * @描述 客服绩效导出
     * @返回类型 void
     * @日期 2016年11月10日 下午2:07:27
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    @RequestMapping("/storeCustomerExport")
    public void storeCustomerExport(HttpServletRequest request, HttpServletResponse response, NationwideDto query) {
        MyphLogger.info("开始绩效导出：/performance/storeCustomerExport.htm");
        EmployeeInfoDto user = ShiroUtils.getCurrentUser();
        EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
        query.setStoreId(empDetail.getStoreId());
        try {
            String fileName = ExportConstant.STORECUSTOMER_FILENAME
                    + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
            ServiceResult<List<NationwideDto>> list = storeCustomerService.listStoreInfo(query);
            List<NationwideDto> result = getAllCustomer(user, empDetail, list.getData(), query);
            if (null != result) {
                // 默认排序
                Collections.sort(result, new performanceComparator(query.getSortField(), query.getSortOrder()));
            }
            // 获取Excel数据
            List<Map<String, Object>> excelList = getExcelMapList(result);
            // 导出Excel数据
            exportExcel(response, fileName, ExportConstant.STORECUSTOMER_COLUMNNAMES,
                    ExportConstant.STORECUSTOMER_KEYS, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[结束客服绩效导出：/performance/storeCustomerExport.htm]");
        }
        MyphLogger.info("结束客服绩效导出：/performance/storeCustomerExport.htm");
    }

    /**
     * 
     * @名称 bmStoreExport
     * @描述 门店业务员绩效导出
     * @返回类型 String
     * @日期 2016年9月26日 下午4:01:16
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    @RequestMapping("/bmStoreExport")
    public void bmStoreExport(HttpServletRequest request, HttpServletResponse response, NationwideDto query) {
        MyphLogger.info("开始绩效导出：/performance/bmStoreExport.htm");
        try {
            query.setState(FlowStateEnum.FINISH.getCode());
            String fileName = ExportConstant.BMSTORE_FILENAME
                    + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
            List<NationwideDto> list = nationwideStoreService.listBmInfo(query).getData();
            if (null != list) {
                // 默认排序
                Collections.sort(list, new performanceComparator(query.getSortField(), query.getSortOrder()));
            }
            // 获取Excel数据
            List<Map<String, Object>> excelList = getExcelMapList(list);
            // 导出Excel数据
            exportExcel(response, fileName, ExportConstant.BMSTORE_COLUMNNAMES, ExportConstant.BMSTORE_KEYS, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[结束业务员绩效导出：/performance/bmStoreExport.htm]");
        }
        MyphLogger.info("结束业务员绩效导出：/performance/bmStoreExport.htm");
    }

    /**
     * 
     * @param srcList
     * @param destList
     * @param keys需转化的属性
     * @Description:dto转化为ExcelMap
     */
    private List<Map<String, Object>> getExcelMapList(List<NationwideDto> srcList) {
        List<Map<String, Object>> destList = new ArrayList<Map<String, Object>>();
        if (null == srcList) {
            return destList;
        }
        int index = 1;
        for (NationwideDto dto : srcList) {
            if (null == dto.getSumRepayMoney()) {
                dto.setSumRepayMoney(DecimalFormartConstant.ZERO_BIGDECIMAL);
            }
            if (null == dto.getSumApplyMoney()) {
                dto.setSumApplyMoney(DecimalFormartConstant.ZERO_BIGDECIMAL);
            }
            Map<String, Object> destMap = BeanUtils.transBeanToMap(dto);
            // 百分比数据加入%
            for (String str : ExportConstant.MATE_FORMART) {
                destMap.put(str, destMap.get(str) + "%");
            }
            destMap.put("index", index++);
            destList.add(destMap);
        }
        return destList;
    }
}
