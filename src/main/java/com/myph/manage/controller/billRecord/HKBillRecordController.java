package com.myph.manage.controller.billRecord;

import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.constant.HkBIllRecordStateEnum;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.hkrecord.service.HkBillRepayRecordService;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.common.util.BeanUtils;
import com.myph.manage.controller.BaseController;
import com.myph.organization.dto.OrganizationDto;
import com.myph.performance.dto.billRecord.RepayRecordDto;
import com.myph.performance.dto.billRecord.RepayRecordQueryDto;
import com.myph.performance.service.RepayRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @ClassName: ReceptionController
 * @Description: 申请件
 * @author heyx
 * @date 2016年9月6日 下午3:56:07
 *
 */
@Controller
@RequestMapping("/hKBillRecord")
public class HKBillRecordController extends BaseController{

    @Autowired
    HkBillRepayRecordService hkBillRepayRecordService;

    @Autowired
    RepayRecordService repayRecordService;

    /**
     * 团队列表
     *
     * @param model
     * @param
     * @param basePage
     * @return
     */
    @RequestMapping("/list")
    public String list(Model model, RepayRecordQueryDto queryDto, BasePage basePage) {
        basePage.setSortField("createTime");
        basePage.setSortOrder("desc");
        ServiceResult<Pagination<RepayRecordDto>>  resultInfo = repayRecordService.queryPagination(queryDto,basePage);
        List<OrganizationDto> orgs = ShiroUtils.getStoreInfo();
        initQueryDate(queryDto);
        // 查询组织条件为空获取当前组织数据权限
        if(null == queryDto.getStoreId()) {
            List<Long> storeIds = new ArrayList<Long>();
            for(OrganizationDto org : orgs){
                storeIds.add(org.getId());
            }
            queryDto.setStoreIds(storeIds);
        }
        model.addAttribute("orgs",orgs);
        model.addAttribute("queryDto", queryDto);
        model.addAttribute("states", HkBIllRecordStateEnum.getEnumMap());
        model.addAttribute("page", resultInfo.getData());
        MyphLogger.info("团队管理列表分页查询", resultInfo.getData());
        return "/billRecord/billRecord_list";
    }

    /**
     * 初始化查询时间，开始时间默认为当月1号，结束时间默认为当天
     *
     * @param queryDto
     */
    private void initQueryDate(RepayRecordQueryDto queryDto) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();
        cal.set(Calendar.DATE, 1);// 设为当前月的1号
        Date date = cal.getTime();

        if (null == queryDto.getBeginPayTime()) {
            queryDto.setBeginPayTime(date);
        }
        if (null == queryDto.getEndPayTime()) {
            queryDto.setEndPayTime(today);
        }

    }

    @RequestMapping("/exportPayRecordInfo")
    public void exportFinanceInfo(HttpServletResponse response, RepayRecordQueryDto param) {
        MyphLogger.debug("还款记录导出：/hKBillRecord/exportPayRecordInfo.htm|param=" + param);
        try {
            // 设置参数查询满足条件的所有数据不分页
            List<RepayRecordDto> list = repayRecordService.queryList(param).getData();
            for(RepayRecordDto dto:list){
//                String addr = dto.getLiveAddr();
//                if(StringUtils.isNotBlank(addr)){
//                    String[] addrArray = addr.split("-");
//                    if(addrArray.length > 1){
//                        dto.setLiveProv(addrArray[0]);
//                        dto.setLiveCity(addrArray[1]);
//                    }
//                }
            }
            String columnNames[] = {
                   "合同编号", "期数", "账单编号"
                    , "扣款类型", "扣款金额", "账户名", "开户行", "卡号", "手机号", "身份证号"
                    , "发起人", "提前结清", "扣款日期", "状态", "备注" };// 列名
            String keys[] = { "contractNo", "repayPeriodName", "billNo", "payTypeName",
                    "payAmount", "username", "backOpen", "idBackNo", "reservedPhone", "idCardNo", "createUser",
                    "isAdvanceSettleName", "createTime", "stateName", "payDesc"};
            String fileName = "还款记录信息" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
            // 获取Excel数据
            List<Map<String, Object>> excelList = getExcelMapList(list);
            // 导出Excel数据
            exportExcel(response, fileName, columnNames, keys, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[还款记录导出：/hKBillRecord/exportFinanceInfo.htm]");
        }
        MyphLogger.debug("还款记录导出：/hKBillRecord/exportFinanceInfo.htm");
    }

    /**
     * 获取Excel数据
     *
     * @param list
     * @return
     */
    private List<Map<String, Object>> getExcelMapList(List<RepayRecordDto> list) {
        List<Map<String, Object>> destList = new ArrayList<Map<String, Object>>();
        if (null == list) {
            return destList;
        }
        Map<String, Object> destMap = null;
        for (RepayRecordDto dto : list) {
            destMap = BeanUtils.transBeanToMap(dto);
//            Date loanTime = dto.getLoanTime();
//            if (null != loanTime) {
//                SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_PATTERN);
//                destMap.put("loanTime", sdf.format(loanTime));
//            }
//            destMap.put("monthlySalary", getMonthMoney(dto.getMonthlySalary()));
//            destMap.put("businessTypeName",getBusinessTypeName(dto.getBusinessType()));
            destList.add(destMap);
        }
        return destList;
    }
    
    
    /**
     * 
     * @名称 queryCountByIdCardNo 
     * @描述 根据身份证查询处理中的代偿还款记录
     * @返回类型 AjaxResult     
     * @日期 2017年9月11日 下午1:51:26
     * @创建人  吴阳春
     * @更新人  吴阳春
     *
     */
    @RequestMapping("/queryCountByIdCardNo")
    @ResponseBody
    public AjaxResult queryCountByIdCardNo(String idCardNo) {
        ServiceResult<Integer> count = hkBillRepayRecordService.selectCountByIdCardNo(idCardNo);
        return AjaxResult.success(count.getData());
    }
}
