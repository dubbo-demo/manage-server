package com.myph.manage.common.shiro.dto;

import com.myph.common.bean.BaseInfo;
import com.myph.organization.dto.OrganizationDto;
import com.myph.roleCondition.dto.SysRoleConditionDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RoleConditionDto extends BaseInfo {

    private static final long serialVersionUID = 3425947513154526967L;

    private List<OrganizationDto> orgs;

    private List<SysRoleConditionDto> prdOvers;

    private List<SysRoleConditionDto> clients;

    private List<SysRoleConditionDto> sources;

}