package com.myph.manage.common.handler;

import com.myph.apply.dto.ApplyInfoDto;
import com.myph.common.bean.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Package: com.myph.manage.common.handler
 * @company: 麦芽金服
 * @Description: 捷安征信规则入参
 * @author heyx
 * @date 2017/4/24
 * @version V1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HandlerParmDto extends BaseInfo {

    private static final long serialVersionUID = -3119601470259499861L;

    private ApplyInfoDto applyInfoDto;
}
