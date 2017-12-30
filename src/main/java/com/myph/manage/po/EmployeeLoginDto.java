package com.myph.manage.po;

import com.way.base.menu.dto.MenuDto;
import com.way.common.bean.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * 员工信息DTO
 * 
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class EmployeeLoginDto extends BaseEntity {
     
    private static final long serialVersionUID = 3984953473366018219L;
    private String employeeName; // '员工姓名'
    private String employeeNo; // '员工编号'
    private String mobilePhone; // '手机号'
    private long orgId; // '组织id'
    private long positionId; // '岗位id'
    private String positionName; // '岗位名称'
    private List<MenuDto> menuDtos;
    private Map<String, List<String>> menuUrlPermissionCode;//菜单URL及其对应的权限CODE
    
}
