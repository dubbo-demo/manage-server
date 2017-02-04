package com.myph.manage.common.shiro.dto;

import com.myph.common.bean.BaseInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LoginCheckDto extends BaseInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5907142094148164089L;
	
	private String phone;
	private String smsCode;
}
