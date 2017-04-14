package com.myph.manage.service.billContract.dto;

import com.myph.common.bean.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceMatchDto extends BaseInfo {
    private static final long serialVersionUID = -7817103414888001164L;
    private String deviceMatchNum;
    private String deviceMatch;
}
