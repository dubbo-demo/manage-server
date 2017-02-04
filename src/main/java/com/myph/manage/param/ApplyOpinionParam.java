/**   
 * @Title: ApplyOpinionParam.java 
 * @Package: com.myph.manage.param 
 * @company: 麦芽金服
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 罗荣   
 * @date 2016年9月13日 上午8:43:22 
 * @version V1.0   
 */
package com.myph.manage.param;

import java.util.List;

import com.myph.common.bean.BaseInfo;
import com.myph.role.dto.RolePermissionSimpleTreeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @ClassName: ApplyOpinionParam
 * @Description: 申请件综合意见Controller传参(这里用一句话描述这个类的作用)
 * @author 罗荣
 * @date 2016年9月13日 上午8:43:22
 * 
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ApplyOpinionParam extends BaseInfo {
    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = -546559562069924142L;
    private Integer state;
    private String desc;
    private String applyNo;
    private Integer saveOrSubmit;
}
