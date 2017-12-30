package com.myph.manage.common.shiro.dto;

import com.way.common.bean.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LoginCheckDto extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5907142094148164089L;
	
	private String phone;
	private String smsCode;
}
