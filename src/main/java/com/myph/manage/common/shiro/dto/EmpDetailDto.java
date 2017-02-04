/**   
 * @Title: EmpDetailDto.java 
 * @Package: com.myph.manage.common.shiro.dto
 * @company: 麦芽金服
 * @Description: TODO
 * @date 2016年9月29日 下午2:51:56 
 * @version V1.0   
 */
package com.myph.manage.common.shiro.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.myph.common.bean.BaseInfo;

/**
 * @ClassName: EmpDetailDto
 * @Description: TODO
 * @author hf
 * @date 2016年9月29日 下午2:51:56
 * 
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EmpDetailDto extends BaseInfo {

    /**
     * @Fields serialVersionUID : TODO
     */
    private static final long serialVersionUID = 4533818731082564723L;
    private Long regionId; // '大区ID',
    private Long storeId; // '门店ID',
    private Long cityId; // 城市id
    private String orgCode; // 部门编号
    private Long departmentId; // '部门ID',
    private Integer isManage; // 是否管理岗
    private String storeName; // '门店名称'
}
