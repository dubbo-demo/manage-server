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

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.constant.OrderTypeEnum;
import com.myph.manage.common.util.BeanUtils;
import com.myph.manage.controller.BaseController;
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
        MyphLogger.info("付融宝标的信息-列表页参数【{}】", param);
        initQueryDate(param);
        ServiceResult<Pagination<OrderManageDto>> rs =  orderManageService.queryPageList(param, page);
        if (rs.success()) {
            for (OrderManageDto dto : rs.getData().getResult()) {
                // 设置页面显示的状态
                if(dto.getRepayState() == 0 && dto.getOverdueState() == 0 && dto.getIsEffective() ==1){
                    dto.setState(OrderTypeEnum.WAIT_REPLAY.getType());
                    dto.setStateDesc(OrderTypeEnum.WAIT_REPLAY.getName());
                }
                if(dto.getRepayState() == 0 && dto.getOverdueState() == 1 && dto.getIsEffective() ==1){
                    dto.setState(OrderTypeEnum.OVERDUE_NO_REPLAY.getType());
                    dto.setStateDesc(OrderTypeEnum.OVERDUE_NO_REPLAY.getName());
                }
                if(dto.getRepayState() == 2 && dto.getOverdueState() == 1 && dto.getIsEffective() ==1){
                    dto.setState(OrderTypeEnum.OVERDUE_PART_REPLAY.getType());
                    dto.setStateDesc(OrderTypeEnum.OVERDUE_PART_REPLAY.getName());
                }
                if(dto.getRepayState() == 1 && dto.getOverdueState() == 0 && dto.getIsEffective() ==1){
                    dto.setState(OrderTypeEnum.SQUARE.getType());
                    dto.setStateDesc(OrderTypeEnum.SQUARE.getName());
                }
                if(dto.getRepayState() == 0 && dto.getOverdueState() == 0 && dto.getIsEffective() ==1 && dto.getLastPayTime().getTime() < dto.getAgreeRepayDate().getTime()){
                    dto.setState(OrderTypeEnum.AHEAD_SQUARE.getType());
                    dto.setStateDesc(OrderTypeEnum.AHEAD_SQUARE.getName());
                }
                // 计算已还金额
                BigDecimal alsoRepay = dto.getAlsoPenalty().add(dto.getAlsoLateFee()).add(dto.getAlsoPrincipal())
                        .add(dto.getAlsoInterest());
                dto.setAlsoRepay(alsoRepay);
                // 计算罚息
                BigDecimal lastPenalty = dto.getPenalty().subtract(dto.getAlsoPenalty())
                        .subtract(dto.getReductionPenalty());
                dto.setLastPenalty(lastPenalty);
                // 计算违约金
                BigDecimal lastLateFee = dto.getLateFee().subtract(dto.getAlsoLateFee())
                        .subtract(dto.getReductionLateFee());
                dto.setLastLateFee(lastLateFee);
                // 计算剩余应还
                BigDecimal surplusRepay = dto.getReapyAmount().add(dto.getPenalty()).add(dto.getLateFee())
                        .subtract(dto.getAlsoInterest()).subtract(dto.getAlsoLateFee()).subtract(dto.getAlsoPenalty())
                        .subtract(dto.getAlsoPrincipal()).subtract(dto.getReductionInterest())
                        .subtract(dto.getReductionLateFee()).subtract(dto.getReductionPenalty())
                        .subtract(dto.getReductionPrincipal());
                dto.setSurplusRepay(surplusRepay);
            }
        }
        model.addAttribute("empDetail", param);
        model.addAttribute("params", param);
        model.addAttribute("page", rs.getData());
        return "/order/orderManage";
    }

    /**
     * 初始化时间
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
    
    
    @RequestMapping("/export")
    public void exportInfo( HttpServletResponse response,OrderQueryParam param) {
        MyphLogger.debug("付融宝标的信息导出：/order/export.htm|param=" + param);
        initQueryDate(param);
        try {
            // 设置参数查询满足条件的所有数据不分页
            List<OrderManageDto> list = orderManageService.queryOrderManageInfo(param).getData();
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
    private List<Map<String, Object>> getExcelMapList(List<OrderManageDto> list) {
        List<Map<String, Object>> destList = new ArrayList<Map<String, Object>>();
        if (null == list) {
            return destList;
        }
        Map<String, Object> destMap = null;
        for (OrderManageDto dto : list) {
            destMap = BeanUtils.transBeanToMap(dto);
            destList.add(destMap);
        }
        return destList;
    }
}
