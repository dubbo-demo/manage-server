package com.myph.manage.po;

import com.myph.base.dto.MenuDto;
import com.myph.common.bean.BaseInfo;
import com.myph.product.dto.ProductDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 员工信息DTO
 * 
 * @author dell
 *
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class MentionLoanAmount extends BaseInfo {
    private static final long serialVersionUID = -8143789051149163072L;

    private boolean isCheck;// 金额校验是否通过
    private String message;// 提示信息
    private BigDecimal repayMoneyAfter;// 放款金额
    private ProductDto productDto;// 产品配置信息
    
}
