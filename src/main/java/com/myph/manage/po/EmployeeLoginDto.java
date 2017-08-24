package com.myph.manage.po;

import com.myph.base.dto.MenuDto;
import com.myph.common.bean.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * 员工信息DTO
 * 
 * @author dell
 *
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class EmployeeLoginDto extends BaseInfo {
     
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
