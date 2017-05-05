package com.myph.manage.common.handler;

import com.myph.common.bean.BaseInfo;
import com.myph.flow.dto.BaseActionDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Package: com.myph.manage.common.handler
 * @company: 麦芽金服
 * @Description: 捷安征信规则结果
 * @author heyx
 * @date 2017/4/24
 * @version V1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HandlerResultDto extends BaseInfo{

    private static final long serialVersionUID = 453795432689927385L;
    private Boolean isAuditSuccess = true;

    private String message;

    private BaseActionDto baseActionDto;

}
