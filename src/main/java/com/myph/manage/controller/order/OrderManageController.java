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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.dto.ApplyManageInfoDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.SensitiveInfoUtils;
import com.myph.constant.ApplyUtils;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.OrderTypeEnum;
import com.myph.constant.StateListUtils;
import com.myph.constant.bis.ApplyBisStateEnum;
import com.myph.employee.dto.EmployeeDetailDto;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.flow.dto.AbandonActionDto;
import com.myph.flow.dto.RejectActionDto;
import com.myph.manage.common.constant.Constant;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.controller.BaseController;
import com.myph.manage.facadeService.FacadeFlowStateExchangeService;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.performance.dto.FrbTargetDto;
import com.myph.performance.dto.OrderManageDto;
import com.myph.performance.param.FrbTargetQueryParam;
import com.myph.performance.param.OrderQueryParam;

@Controller
@RequestMapping("/order")
public class OrderManageController extends BaseController {

    @Autowired
    private ApplyInfoService applyInfoService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EmployeeInfoService employeeInfoService;

    @Autowired
    private FacadeFlowStateExchangeService facadeFlowStateExchangeService;

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
        ServiceResult<Pagination<OrderManageDto>> rs = null;// frbTargetService.queryPageList(param, page);
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

        // 初始化查询外访进件日期
        if (null == queryDto.getAgreeRepayDates()) {
            queryDto.setAgreeRepayDates(date);
        }
        if (null == queryDto.getAgreeRepayDatee()) {
            queryDto.setAgreeRepayDatee(today);
        }
    }
}
