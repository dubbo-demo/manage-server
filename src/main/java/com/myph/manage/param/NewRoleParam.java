package com.myph.manage.param;

import com.way.base.role.dto.RolePermissionSimpleTreeDto;
import com.way.common.bean.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NewRoleParam extends BaseEntity {
    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -3726153940076334117L;
    private Long id;
    private String roleName;
    private String roleCode;
    private Integer roleType;
    private Long[] positionIds;
    private Integer status;
    private List<RolePermissionSimpleTreeDto> saves;
    private List<RolePermissionSimpleTreeDto> removes;
}
