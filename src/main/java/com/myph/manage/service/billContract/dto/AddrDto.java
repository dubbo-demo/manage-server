package com.myph.manage.service.billContract.dto;

import com.myph.common.bean.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AddrDto extends BaseInfo {
	private static final long serialVersionUID = -4457532504230828351L;
	private String addr;
	private String detailAddr;
}
