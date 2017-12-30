package com.myph.manage.common.shiro.dto;

import com.way.common.bean.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @ClassName: EmpDetailDto
 *
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EmpDetailDto extends BaseEntity {

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
