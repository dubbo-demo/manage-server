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

    // 组织
    private List<OrganizationDto> orgs;

    // 产品与逾期
    private List<SysRoleConditionDto> prdOvers;

    // 渠道
    private List<Integer> clients;

    // 数据源
    private List<String> sources;

}