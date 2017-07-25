package com.myph.manage.common.shiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.tools.ant.taskdefs.condition.And;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.myph.base.dto.MenuDto;
import com.myph.base.service.MenuService;
import com.myph.common.constant.Constants;
import com.myph.common.exception.DataValidateException;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.constant.NodeConstant;
import com.myph.constant.RoleConditionEnum;
import com.myph.employee.dto.EmployeeDetailDto;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.manage.common.shiro.dto.RoleConditionDto;
import com.myph.employee.constants.EmployeeMsg.ORGANIZATION_TYPE;
import com.myph.employee.dto.EmpDetailDto;
import com.myph.node.dto.SysNodeDto;
import com.myph.node.service.NodeService;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.permission.dto.PermissionDto;
import com.myph.permission.service.PermissionService;
import com.myph.position.dto.PositionDto;
import com.myph.position.service.PositionService;
import com.myph.role.service.SysRoleService;
import com.myph.roleCondition.dto.SysRoleConditionDto;
import com.myph.roleCondition.service.SysRoleConditionService;
import com.myph.sms.service.SmsService;
import com.myph.user.dto.SysUserDto;
import com.myph.user.service.SysUserService;

/**
 * 自定义Realm AuthorizingRealm将获取的Subject相关信息分成两步 主要实现认证和授权的管理操作
 *
 * @author dell
 */
public class MaiyaphRealm extends AuthorizingRealm {

    @Autowired
    private EmployeeInfoService employeeInfoService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private SmsService sendSmsService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private SysRoleConditionService sysRoleConditionService;

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用 PrincipalCollection:身份集合 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String phone = (String) principals.getPrimaryPrincipal();// 目前系统只存在一个Realm,直接调用获取身份即可
        if (StringUtils.isNotBlank(phone)) {
            ServiceResult<EmployeeInfoDto> employeeInfoResult = employeeInfoService.getEntityByMobile(phone);// 获取员工信息
            EmployeeInfoDto employeeInfoDto = employeeInfoResult.getData();
            long positionId = employeeInfoDto.getPositionId();// 岗位ID
            List<String> permissionUrls = new ArrayList<String>();// 权限URL
            ServiceResult<List<String>> roleResult = sysRoleService.getRolesByPositionId(positionId);// 获取岗位对应的角色ID

            ServiceResult<List<Long>> permissionIdResult = permissionService
                    .getPermissionsByRoleId(roleResult.getData());// 获取角色对应的权限ID
            List<Long> permissions = permissionIdResult.getData();
            // 获取菜单的URL
            ServiceResult<List<Long>> menuResult = permissionService.getMenuIdsByPerId(permissionIdResult.getData());// 根据权限ID获取菜单ID

            if (menuResult.getData() != null) {
                ServiceResult<List<MenuDto>> menuDtoResult = menuService.getMenuListByIds(menuResult.getData());// 根据菜单ID获取所有菜单
                if (menuDtoResult.success()) {
                    for (MenuDto menuDto : menuDtoResult.getData()) {
                        List<MenuDto> subList = menuDto.getChirdlen();
                        for (MenuDto subMenuDto : subList) {
                            permissionUrls.add(subMenuDto.getMenuUrl());
                        }
                    }
                }
            }

            ServiceResult<List<PermissionDto>> permissionResult = permissionService.getPermissionByPerId(permissions);// 根据权限ID获取权限码
            List<String> permissionCodes = new ArrayList<>();// 权限码
            if (permissionResult.getData() != null) {
                for (PermissionDto permissionDto : permissionResult.getData()) {
                    permissionUrls.add(permissionDto.getPermissionUrl());
                    permissionCodes.add(permissionDto.getPermissionCode());
                }
            }

            // 为当前用户设置角色和权限
            SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
            simpleAuthorInfo.addRoles(permissionCodes);// 权限码
            simpleAuthorInfo.addStringPermissions(permissionUrls);// 权限+菜单URL
            return simpleAuthorInfo;
        }
        return null;
    }

    /**
     * (身份认证信息)登录时调用由Subject.login(...)触发 AuthenticationToken：凭据
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {
        try {
            UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
            ServiceResult smsCheckResult = sendSmsService.checkLoginSms(token.getUsername(),
                    new String(token.getPassword()));
            if (!smsCheckResult.success()) {
                throw new DataValidateException(smsCheckResult.getMessage());
            }
            // 员工信息服务 账号信息服务
            MyphLogger.info("[MaiyaphRealm.doGetAuthenticationInfo]登录麦芽普惠信贷后台，获取缓存里面的短信验证码");
            ServiceResult<EmployeeInfoDto> employeeResult = employeeInfoService.getEntityByMobile(token.getUsername());
            EmployeeInfoDto employeeInfoDto = employeeResult.getData();
            if (null == employeeInfoDto) {
                MyphLogger.debug("用户名不正确,手机号:{}", token.getUsername());
                throw new DataValidateException("用户名不正确");
            }
            if (employeeInfoDto.getUserFlag() == null || Constants.NO.equals(employeeInfoDto.getUserFlag())) {
                MyphLogger.debug("用户账户未启用,手机号:{},用户ID:{}", employeeInfoDto.getMobilePhone(), employeeInfoDto.getId());
                throw new DataValidateException("用户账户未启用");
            }
            if (employeeInfoDto.getIcmbFlag() == null || Constants.YES.equals(employeeInfoDto.getIcmbFlag())) {
                MyphLogger.debug("用户已离职,手机号:{},用户ID:{}", employeeInfoDto.getMobilePhone(), employeeInfoDto.getId());
                throw new DataValidateException("用户已离职");
            }
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(employeeInfoDto.getMobilePhone(),
                    token.getPassword(), employeeInfoDto.getEmployeeName());
            ServiceResult<EmployeeDetailDto> empDetailResult = organizationService
                    .selectEmployeeDetail(employeeInfoDto.getOrgId());

            EmpDetailDto empDetailDto = new EmpDetailDto();
            if (empDetailResult.success() && empDetailResult.getData() != null) {
                EmployeeDetailDto employeeDetailDto = empDetailResult.getData();
                BeanUtils.copyProperties(employeeDetailDto, empDetailDto);
            }
            ServiceResult<PositionDto> postionResult = positionService
                    .getEntityByPositionId(employeeInfoDto.getPositionId());

            if (postionResult.success() && postionResult.getData() != null) {
                PositionDto positionDto = postionResult.getData();
                // 获取管理员岗位code
                Integer currentAccountType = Constants.NO_INT;
                String positionCode = positionDto.getPositionCode();
                ServiceResult<List<SysNodeDto>> positionManageListResult = nodeService
                        .getListByParent(NodeConstant.POSITION_MANAGE_PARENT_CODE);
                if (positionManageListResult.getData() != null || !positionManageListResult.getData().isEmpty()) {
                    for (SysNodeDto sysNodeDto : positionManageListResult.getData()) {
                        if (sysNodeDto.getNodeCode().equals(positionCode)) {
                            currentAccountType = Constants.YES_INT;
                            break;
                        }
                    }
                }
                this.setSession(ShiroUtils.CURRENT_ACCOUNT_TYPE, currentAccountType);// 账户类型
                empDetailDto.setIsManage(positionDto.getIsManage());
            }
            RoleConditionDto roleConditionDto = this.getRoleCondition(employeeInfoDto.getOrgType(),
                    employeeInfoDto.getPositionId(),empDetailDto);
            this.setSession(ShiroUtils.CURRENT_EMP_DETAIL, empDetailDto);// 员工明细资料
            this.setSession(ShiroUtils.CURRENT_USER_KEY, employeeInfoDto);// 员工信息
            this.setSession(ShiroUtils.ROLE_CONDITION, roleConditionDto);// 角色权限
            return authcInfo;
        } catch (DataValidateException e) {
            MyphLogger.error("[MaiyaphRealm.doGetAuthenticationInfo]麦芽普惠登录身份认证信息异常", e);
            throw new AuthenticationException(e.getMessage());
        } catch (Exception e) {
            MyphLogger.error("[MaiyaphRealm.doGetAuthenticationInfo]麦芽普惠登录身份认证信息异常", e);
            throw new AuthenticationException("登录失败,请重试");
        }
    }

    /**
     * 将一些数据放到ShiroSession中,以便于其它地方使用 比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
     *
     * @see
     */
    private void setSession(Object key, Object value) {
        Subject currentUser = SecurityUtils.getSubject();
        if (null != currentUser) {
            Session session = currentUser.getSession();
            if (null != session) {
                MyphLogger.info("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
                session.setAttribute(key, value);
            }
        }
    }

    /**
     * 
     * @名称 getRoleCondition 
     * @描述 获取数据权限，配了组织权限的不再查通用赋权的组织权限。
     * @返回类型 RoleConditionDto     
     * @日期 2017年7月13日 上午11:36:20
     * @创建人  吴阳春
     * @更新人  吴阳春
     *
     */
    private RoleConditionDto getRoleCondition(Integer orgType, Long positionId,EmpDetailDto empDetailDto) {
        RoleConditionDto result = new RoleConditionDto();
        ServiceResult<List<Long>> roleIds = sysRoleService.selectRoleIds(positionId, null);
        ServiceResult<List<SysRoleConditionDto>> roleConditions = sysRoleConditionService
                .selectByRoleIds(roleIds.getData());
        List<OrganizationDto> orgs = new ArrayList<OrganizationDto>();
        List<SysRoleConditionDto> infoRoleProducts = new ArrayList<SysRoleConditionDto>();
        if (roleConditions.getData() != null) {
            for (SysRoleConditionDto dto : roleConditions.getData()) {
                //非空校验，避免异常情况
                if(StringUtils.isBlank(dto.getConditionCode())){
                    continue;
                }
                // 处理组织信息
                if (dto.getDimension().equals(RoleConditionEnum.ORG.getCode())) {
                    ServiceResult<OrganizationDto> regionResult = organizationService
                            .selectOrganizationById(Long.valueOf(dto.getParentCode()));
                    List<String> strIds = Arrays.asList(dto.getConditionCode().split(","));
                    List<Long> ids = new ArrayList<Long>();
                    for (String strId : strIds) {
                        ids.add(Long.valueOf(strId));
                    }
                    ServiceResult<List<OrganizationDto>> storeResult = organizationService.selectOrganizationByIds(ids);
                    orgs.add(regionResult.getData());
                    orgs.addAll(storeResult.getData());
                }
                // 处理渠道信息
                if (dto.getDimension().equals(RoleConditionEnum.CLIENT.getCode())) {
                    List<String> strIds = Arrays.asList(dto.getConditionCode().split(","));
                    List<Integer> ids = new ArrayList<Integer>();
                    for (String strId : strIds) {
                        ids.add(Integer.valueOf(strId));
                    }
                    result.setClients(ids);
                }
                // 处理数据源信息
                if (dto.getDimension().equals(RoleConditionEnum.SOURCE.getCode())) {
                    List<String> codes = Arrays.asList(dto.getConditionCode().split(","));
                    result.setSources(codes);
                }
                // 处理产品与逾期信息
                if (dto.getDimension().equals(RoleConditionEnum.PRODUCT.getCode())) {
                    infoRoleProducts.add(dto);
                }
            }
        }
        if(infoRoleProducts.size() > 0){
            result.setPrdOvers(infoRoleProducts);
        }
        //配了组织权限的不再查通用赋权的组织权限
        if (orgs.size() > 0) {
            result.setOrgs(orgs);
        } else {
            List<OrganizationDto> curOrgs = this.getCurOrgs(orgType,empDetailDto);
            result.setOrgs(curOrgs);
        }
        MyphLogger.debug("数据权限缓存:"+result.toString());
        return result;
    }
    
    /**
     * 
     * @名称 getCurOrgs 
     * @描述 获取通用赋权的组织权限 
     * @返回类型 List<OrganizationDto>     
     * @日期 2017年7月13日 上午11:37:39
     * @创建人  吴阳春
     * @更新人  吴阳春
     *
     */
    private List<OrganizationDto> getCurOrgs(Integer orgType,EmpDetailDto empDetailDto){
        List<OrganizationDto> curOrgs = new ArrayList<OrganizationDto>();
        if (ORGANIZATION_TYPE.REGION_TYPE.toNumber() == orgType) {
            //所在大区及其下门店权限
            ServiceResult<OrganizationDto> regionResult = organizationService.selectOrganizationById(empDetailDto.getRegionId());
            ServiceResult<List<OrganizationDto>> storeResult = organizationService.selectOrgByParentId(empDetailDto.getRegionId());
            curOrgs.add(regionResult.getData());
            curOrgs.addAll(storeResult.getData());
        } else if (ORGANIZATION_TYPE.HQ_TYPE.toNumber() == orgType) {
            //所有大区及门店权限
            ServiceResult<List<OrganizationDto>> regionResult = organizationService.selectOrgByOrgType(ORGANIZATION_TYPE.REGION_TYPE.toNumber());
            ServiceResult<List<OrganizationDto>> storeResult = organizationService.selectOrgByOrgType(ORGANIZATION_TYPE.STORE_TYPE.toNumber());
            curOrgs.addAll(regionResult.getData());
            curOrgs.addAll(storeResult.getData());
        } else {
            //所在门店权限
            ServiceResult<OrganizationDto> storeResult = organizationService.selectOrganizationById(empDetailDto.getStoreId());
            curOrgs.add(storeResult.getData());
            //门店对应大区
            ServiceResult<OrganizationDto> regionResult = organizationService.selectOrganizationById(storeResult.getData().getParentId());
            curOrgs.add(regionResult.getData());
        }
        return curOrgs;
    }
}
