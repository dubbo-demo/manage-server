package com.myph.manage.po;

import com.myph.common.bean.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Title:
 * @Description: 操作返回信息公共类
 * @author heyx
 * @date 2017/3/7
 * @version V1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResultParams extends BaseEntity {
	
	/**
	 * 操作返回状态
	 */
	String retcode;

	/**
	 * 操作返回信息
	 */
	String retinfo;

}