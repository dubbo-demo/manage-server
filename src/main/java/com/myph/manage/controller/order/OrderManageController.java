/**   
 * @Title: ApplyManageController.java 
 * @Package: com.myph.manage.controller.apply
 * @company: 麦芽金服
 * @Description: TODO
 * @date 2016年9月24日 下午1:35:06 
 * @version V1.0   
 */
package com.myph.manage.controller.order;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jws.soap.InitParam;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.constant.OrderTypeEnum;
import com.myph.constant.OverdueStateEnum;
import com.myph.constant.RepayStateEnum;
import com.myph.employee.dto.EmpDetailDto;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.common.util.BeanUtils;
import com.myph.manage.controller.BaseController;
import com.myph.organization.dto.OrganizationDto;
import com.myph.performance.dto.FrbTargetDto;
import com.myph.performance.dto.OrderManageDto;
import com.myph.performance.param.FrbTargetQueryParam;
import com.myph.performance.param.OrderQueryParam;
import com.myph.performance.service.OrderManageService;

@Controller
@RequestMapping("/order")
public class OrderManageController extends BaseController {

    @Autowired
    private OrderManageService orderManageService;

    /**
     * 
     * @名称 list
     * @描述 还款账单明细
     * @返回类型 String
     * @日期 2017年8月18日 下午1:50:58
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/list")
    public String list(OrderQueryParam param, BasePage page, Model model) {
        MyphLogger.info("还款账单明细-列表页参数【{}】", param);
        initQueryDate(param);
        initStoreIdList(param);
        ServiceResult<Pagination<OrderManageDto>> rs = orderManageService.queryPageList(param, page);
        if (rs.success()) {
            for (OrderManageDto dto : rs.getData().getResult()) {
                /*
                 * 设置页面显示的状态 
                 * 待还款 =未还款+未逾期 
                 * 逾期未还款=未还款+逾期 
                 * 逾期部分还款=部分还款+逾期
                 * 结清=已还款 
                 * 提前结清=已还款+实际还款时间<协议还款时间
                 */
                if (dto.getRepayState() == RepayStateEnum.NO_REPAY.getCode()
                        && dto.getOverdueState() == OverdueStateEnum.NO_OVERDUE.getCode()) {
                    dto.setState(OrderTypeEnum.WAIT_REPLAY.getType());
                    dto.setStateDesc(OrderTypeEnum.WAIT_REPLAY.getName());
                }
                if (dto.getRepayState() == RepayStateEnum.NO_REPAY.getCode()
                        && dto.getOverdueState() == OverdueStateEnum.YES_OVERDUE.getCode()) {
                    dto.setState(OrderTypeEnum.OVERDUE_NO_REPLAY.getType());
                    dto.setStateDesc(OrderTypeEnum.OVERDUE_NO_REPLAY.getName());
                }
                if (dto.getRepayState() == RepayStateEnum.PART_REPAY.getCode()
                        && dto.getOverdueState() == OverdueStateEnum.YES_OVERDUE.getCode()) {
                    dto.setState(OrderTypeEnum.OVERDUE_PART_REPLAY.getType());
                    dto.setStateDesc(OrderTypeEnum.OVERDUE_PART_REPLAY.getName());
                }
                if (dto.getRepayState() == RepayStateEnum.ALREADY_REPAY.getCode()) {
                    dto.setState(OrderTypeEnum.SQUARE.getType());
                    dto.setStateDesc(OrderTypeEnum.SQUARE.getName());
                }
                if (dto.getRepayState() == RepayStateEnum.ALREADY_REPAY.getCode() && dto.getLastPayTime() != null
                        && dto.getLastPayTime().getTime() < dto.getAgreeRepayDate().getTime()) {
                    dto.setState(OrderTypeEnum.AHEAD_SQUARE.getType());
                    dto.setStateDesc(OrderTypeEnum.AHEAD_SQUARE.getName());
                }
                // 设置页面显示的期数：当前期数/总期数
                String strPeriod = String.valueOf(dto.getRepayPeriod()) + "/" + String.valueOf(dto.getPeriods());
                dto.setStrPeriod(strPeriod);
                // 计算已还金额=已还罚息+已还滞纳金+已还本金+已还利息
                BigDecimal alsoRepay = dto.getAlsoPenalty().add(dto.getAlsoLateFee()).add(dto.getAlsoPrincipal())
                        .add(dto.getAlsoInterest());
                dto.setAlsoRepay(alsoRepay);
                // 计算罚息=应还罚息-减免罚息-已还罚息
                BigDecimal lastPenalty = dto.getPenalty().subtract(dto.getAlsoPenalty())
                        .subtract(dto.getReductionPenalty());
                dto.setLastPenalty(lastPenalty);
                // 计算违约金=应还滞纳金-减免滞纳金-已还滞纳金
                BigDecimal lastLateFee = dto.getLateFee().subtract(dto.getAlsoLateFee())
                        .subtract(dto.getReductionLateFee());
                dto.setLastLateFee(lastLateFee);
                // 计算剩余应还=月还款额+应还罚息+应还滞纳金-已还罚息-已还滞纳金-已还本金-已还利息-减免罚息-减免滞纳金-减免本金-减免利息
                BigDecimal surplusRepay = dto.getReapyAmount().add(dto.getPenalty()).add(dto.getLateFee())
                        .subtract(dto.getAlsoInterest()).subtract(dto.getAlsoLateFee()).subtract(dto.getAlsoPenalty())
                        .subtract(dto.getAlsoPrincipal()).subtract(dto.getReductionInterest())
                        .subtract(dto.getReductionLateFee()).subtract(dto.getReductionPenalty())
                        .subtract(dto.getReductionPrincipal());
                dto.setSurplusRepay(surplusRepay);
            }
        }
        model.addAttribute("params", param);
        model.addAttribute("page", rs.getData());
        return "/order/orderManage";
    }
    @RequestMapping("/hkBillUpdateLog")
    public String hkBillUpdateLog(String billNo, BasePage page, Model model) {
        return "/order/hkBillUpdateLog";
    }
    
    
    /**
     * 初始化查询时间，开始时间默认为当月1号，结束时间默认为当天
     * 
     * @param queryDto
     */
    private void initQueryDate(OrderQueryParam queryDto) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();
        cal.set(Calendar.DATE, 1);// 设为当前月的1号
        Date date = cal.getTime();

        if (null == queryDto.getAgreeRepayDates()) {
            queryDto.setAgreeRepayDates(date);
        }
        if (null == queryDto.getAgreeRepayDatee()) {
            queryDto.setAgreeRepayDatee(today);
        }

    }
    
    //入参处理：门店为请选择时，设置门店ID为数据权限中设置的门店
    private void initStoreIdList(OrderQueryParam param) {
        if(param.getStoreId() != null && param.getStoreId() != 0){
            return;
        }
        List<OrganizationDto> orgResult = ShiroUtils.getStoreInfo();
        List<Long> storeIdList = new ArrayList<Long>();
        for(int i=0;i<orgResult.size();i++){
            storeIdList.add(orgResult.get(i).getId());
        }
        param.setStoreIdList(StringUtils.join(storeIdList, ","));
    }
    
    @RequestMapping("/export")
    public void exportInfo(HttpServletResponse response, OrderQueryParam param) {
        MyphLogger.debug("还款账单明细导出：/order/export.htm|param=" + param);
        initQueryDate(param);
        try {
            // 设置参数查询满足条件的所有数据
            List<OrderManageDto> list = orderManageService.queryOrderManageInfo(param).getData();
            String columnNames[] = { "合同编号", "期数", "账单编号", "期初本金", "月还本金", "月还利息", "月还款额", "期末本金余额", "提前结清减免", "提前结清金额",
                    "已还金额", "罚息", "违约金", "剩余应还", "应还日期", "逾期天数", "状态" };// 列名
            String keys[] = { "contractNo", "strPeriod", "billNo", "initialPrincipal", "principal", "interest",
                    "reapyAmount", "endPrincipal", "returnAmount", "aheadAmount", "alsoRepay", "lastPenalty",
                    "lastLateFee", "surplusRepay", "agreeRepayDate", "overdueDay", "stateDesc" };
            String fileName = "还款账单明细" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
            // 获取Excel数据
            List<Map<String, Object>> excelList = getExcelMapList(list);
            // 导出Excel数据
            exportExcel(response, fileName, columnNames, keys, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[还款账单明细导出：/order/export.htm]");
        }
        MyphLogger.debug("结束还款账单明细导出：/order/export.htm");
    }

    /**
     * 获取Excel数据
     * 
     * @param list
     * @return
     */
    private List<Map<String, Object>> getExcelMapList(List<OrderManageDto> list) {
        List<Map<String, Object>> destList = new ArrayList<Map<String, Object>>();
        if (null == list) {
            return destList;
        }
        Map<String, Object> destMap = null;
        for (OrderManageDto dto : list) {
            /*
             * 设置页面显示的状态 
             * 待还款 =未还款+未逾期 
             * 逾期未还款=未还款+逾期 
             * 逾期部分还款=部分还款+逾期
             * 结清=已还款 
             * 提前结清=已还款+实际还款时间<协议还款时间
             */
            if (dto.getRepayState() == RepayStateEnum.NO_REPAY.getCode()
                    && dto.getOverdueState() == OverdueStateEnum.NO_OVERDUE.getCode()) {
                dto.setState(OrderTypeEnum.WAIT_REPLAY.getType());
                dto.setStateDesc(OrderTypeEnum.WAIT_REPLAY.getName());
            }
            if (dto.getRepayState() == RepayStateEnum.NO_REPAY.getCode()
                    && dto.getOverdueState() == OverdueStateEnum.YES_OVERDUE.getCode()) {
                dto.setState(OrderTypeEnum.OVERDUE_NO_REPLAY.getType());
                dto.setStateDesc(OrderTypeEnum.OVERDUE_NO_REPLAY.getName());
            }
            if (dto.getRepayState() == RepayStateEnum.PART_REPAY.getCode()
                    && dto.getOverdueState() == OverdueStateEnum.YES_OVERDUE.getCode()) {
                dto.setState(OrderTypeEnum.OVERDUE_PART_REPLAY.getType());
                dto.setStateDesc(OrderTypeEnum.OVERDUE_PART_REPLAY.getName());
            }
            if (dto.getRepayState() == RepayStateEnum.ALREADY_REPAY.getCode()) {
                dto.setState(OrderTypeEnum.SQUARE.getType());
                dto.setStateDesc(OrderTypeEnum.SQUARE.getName());
            }
            if (dto.getRepayState() == RepayStateEnum.ALREADY_REPAY.getCode() && dto.getLastPayTime() != null
                    && dto.getLastPayTime().getTime() < dto.getAgreeRepayDate().getTime()) {
                dto.setState(OrderTypeEnum.AHEAD_SQUARE.getType());
                dto.setStateDesc(OrderTypeEnum.AHEAD_SQUARE.getName());
            }
            // 设置页面显示的期数：当前期数/总期数
            String strPeriod = String.valueOf(dto.getRepayPeriod()) + "/" + String.valueOf(dto.getPeriods());
            dto.setStrPeriod(strPeriod);
            // 计算已还金额=已还罚息+已还滞纳金+已还本金+已还利息
            BigDecimal alsoRepay = dto.getAlsoPenalty().add(dto.getAlsoLateFee()).add(dto.getAlsoPrincipal())
                    .add(dto.getAlsoInterest());
            dto.setAlsoRepay(alsoRepay);
            // 计算罚息=应还罚息-减免罚息-已还罚息
            BigDecimal lastPenalty = dto.getPenalty().subtract(dto.getAlsoPenalty())
                    .subtract(dto.getReductionPenalty());
            dto.setLastPenalty(lastPenalty);
            // 计算违约金=应还滞纳金-减免滞纳金-已还滞纳金
            BigDecimal lastLateFee = dto.getLateFee().subtract(dto.getAlsoLateFee())
                    .subtract(dto.getReductionLateFee());
            dto.setLastLateFee(lastLateFee);
            // 计算剩余应还=月还款额+应还罚息+应还滞纳金-已还罚息-已还滞纳金-已还本金-已还利息-减免罚息-减免滞纳金-减免本金-减免利息
            BigDecimal surplusRepay = dto.getReapyAmount().add(dto.getPenalty()).add(dto.getLateFee())
                    .subtract(dto.getAlsoInterest()).subtract(dto.getAlsoLateFee()).subtract(dto.getAlsoPenalty())
                    .subtract(dto.getAlsoPrincipal()).subtract(dto.getReductionInterest())
                    .subtract(dto.getReductionLateFee()).subtract(dto.getReductionPenalty())
                    .subtract(dto.getReductionPrincipal());
            dto.setSurplusRepay(surplusRepay);
            destMap = BeanUtils.transBeanToMap(dto);
            destList.add(destMap);
        }
        return destList;
    }
}
