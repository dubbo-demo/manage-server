package com.myph.manage.param;

import java.util.List;

import com.myph.common.bean.BaseInfo;
import com.myph.role.dto.RolePermissionSimpleTreeDto;
import com.myph.role.dto.RolePermissionTreeDto;
import com.myph.role.dto.RolePositionDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NewRoleParam extends BaseInfo{
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
