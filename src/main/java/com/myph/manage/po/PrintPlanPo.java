package com.myph.manage.po;

import com.myph.common.bean.BaseInfo;
import com.myph.repaymentPlan.dto.JkRepaymentPlanDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 员工信息DTO
 *
 * @author dell
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class PrintPlanPo extends BasePrintPo {

    private static final long serialVersionUID = -7753921772167770900L;

    private BigDecimal repayAmount;

    private List<JkRepaymentPlanDto> repayPlans;

}
