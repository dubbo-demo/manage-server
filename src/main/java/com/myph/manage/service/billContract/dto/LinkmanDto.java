package com.myph.manage.service.billContract.dto;

import com.myph.common.bean.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
/**
 * @Title: LinkmanDto
 * @Package: com.myph.manage.service.billContract
 * @company: 麦芽金服
 * @Description: 推送催收联系人dto
 * @author heyx
 * @date 2017/3/9
 * @version V1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LinkmanDto extends BaseInfo {

	private static final long serialVersionUID = -6952538554149101802L;

    private String relation; //联系人关系

    private String name; //name

    private String telNum; //手机号码

    private String addr; //现住址

    private String detailAddr; //详细地址

}