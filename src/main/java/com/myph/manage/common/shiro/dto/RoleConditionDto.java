package com.myph.manage.common.shiro.dto;

import com.way.common.bean.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RoleConditionDto extends BaseEntity {

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