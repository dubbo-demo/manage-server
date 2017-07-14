/**
 * 
 */
package com.myph.manage.common.shiro;

import com.myph.manage.common.shiro.dto.RoleConditionDto;
import com.myph.organization.dto.OrganizationDto;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.myph.common.log.MyphLogger;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.constants.EmployeeMsg.ORGANIZATION_TYPE;
import com.myph.employee.dto.EmpDetailDto;

/**
 * @author Administrator
 * 
 */
public class ShiroUtils {
    public static final String AUTHORIZATION_KEY = "_authorization_key";
    public static final String AUTHORIZATION_OPERATORID_KEY = "_authorization_operatorID_key";
    public static final String AUTHORIZATION_EXCEPTION_KEY = "_authorization_error";
    public static final String ORIGIN_NAME_KEY = "origin_name_key";
    public static final String ORIGIN_IP_KEY = "origin_ip_key";
    public static final String CURRENT_USER_KEY = "currentUser";
    public static final String CURRENT_ACCOUNT_TYPE = "accountType";
    public static final String CURRENT_EMP_DETAIL = "empDetail";
    public static final String ROLE_CONDITION= "role_condition";

    public static EmployeeInfoDto getCurrentUser() {
        try {
            return (EmployeeInfoDto) ShiroUtils.getSessionAttribute(CURRENT_USER_KEY);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 获取当前用户shiro Session
     * 
     * @return
     */
    public static Session getSession() {
        try {
            return SecurityUtils.getSubject().getSession();
        } catch (Exception e) {

            return null;
        }
    }

    /**
     * 获取会话中的值
     * 
     * @return
     */
    public static Object getSessionAttribute(String key) {
        try {
            return SecurityUtils.getSubject().getSession().getAttribute(key);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 是否认证
     * 
     * @return
     */
    public static boolean isAuthenticated() {
        try {
            return SecurityUtils.getSubject().isAuthenticated();
        } catch (Exception e) {

            return false;
        }
    }

    /**
     * 删除会话中的值
     * 
     * @param key
     */
    public static void removeSessionAttribute(String key) {
        try {
            SecurityUtils.getSubject().getSession().removeAttribute(key);
        } catch (Exception e) {

        }
    }

    /**
     * 取得当前用户的登录名, 如果当前用户未登录则返回空字符串.
     */
    public static String getCurrentUserName() {
        String username = "";
        try {
            username = ShiroUtils.getCurrentUser() == null ? null : ShiroUtils.getCurrentUser().getEmployeeName();
        } catch (Exception e) {

        }
        if (username == null) {
            username = "anonymousUser";
        }
        return username;

    }

    /**
     * 取得当前用户的ID, 如果当前用户未登录则返回空字符串.
     */
    public static Long getCurrentUserId() {
        Long userId = null;
        try {
            userId = ShiroUtils.getCurrentUser() == null ? null : ShiroUtils.getCurrentUser().getId();
        } catch (Exception e) {

        }
        return userId;

    }

    /**
     * 取得当前用户登录IP, 如果当前用户未登录则返回空字符串.
     */
    public static String getCurrentUserIp() {
        String ip = "";
        try {
            ip = (String) ShiroUtils.getSessionAttribute(ShiroUtils.ORIGIN_IP_KEY);
        } catch (Exception e) {

        }
        return ip;
    }

    /**
     * 判断用户是否拥有角色, 如果用户拥有参数中的任意一个角色则返回true.
     */
    public static boolean hasAnyRole(String... roles) {
        return false;
    }

    /**
     * 获取市场ID
     * 
     * @return
     */
    public static String getMarketID() {
        return null;
    }

    /**
     * 获取用户名
     * 
     * @param username
     * @return
     */
    public static String getRealUserName(String username) {
        if (username == null) {
            return "";
        }
        if (username.indexOf("$") != -1) {
            String[] strs = username.split("\\$");
            if (strs.length > 1) {
                username = strs[0];
            }
        }
        if (username.indexOf("^") != -1) {
            String[] strs = username.split("\\^");
            if (strs.length > 1) {
                username = strs[0];
            }
        }
        // 如果是第三方登录，设置登录类型和匹配编码
        if (username.indexOf("~") != -1) {
            String[] strs = username.split("\\~");
            if (strs.length > 1) {
                username = strs[0];
            }
        }
        return username;
    }

    /**
     * 将用户相关信息放到SESSION中
     * 
     * @param key
     * @param value
     */
    public static void putToSession(Object key, Object value) {
        Subject currentUser = SecurityUtils.getSubject();
        if (null != currentUser) {
            Session session = currentUser.getSession();
            if (null != session) {
                session.setAttribute(key, value);
            } else {

            }
        }
    }

    /**
     * 获取当前登录账户类型 1：管理员 0：普通
     * 
     * @名称 getAccountType
     * @描述 TODO
     * @返回类型 String
     * @日期 2016年9月27日 下午6:24:25
     * @创建人 hf
     * @更新人 hf
     *
     */
    public static int getAccountType() {
        int accountType = 0;
        try {
            Object accountTypeObj = ShiroUtils.getSessionAttribute(ShiroUtils.CURRENT_ACCOUNT_TYPE);
            accountType = Integer.parseInt(String.valueOf(accountTypeObj));
        } catch (Exception e) {
            MyphLogger.error("获取当前登录账户类型异常", e);
        }
        return accountType;
    }

    /**
     * 获取当前登录用户组织相关信息
     * 
     * @名称 getAccountType
     * @描述 TODO
     * @返回类型 String
     * @日期 2016年9月27日 下午6:24:25
     * @创建人 hf
     * @更新人 hf
     *
     */
    public static EmpDetailDto getEmpDetail() {
        EmpDetailDto empDetailDto = new EmpDetailDto();
        try {
            empDetailDto = (EmpDetailDto) ShiroUtils.getSessionAttribute(ShiroUtils.CURRENT_EMP_DETAIL);
        } catch (Exception e) {
            MyphLogger.error("获取当前登录用户组织相关信息异常", e);
        }
        return empDetailDto;
    }

    /**
     * 
     * @名称 getRoleCondition 
     * @描述 获取当前登录用户数据权限相关信息
     * @返回类型 RoleConditionDto     
     * @日期 2017年7月12日 下午1:55:49
     * @创建人  HYX
     * @更新人  HYX
     *
     */
    public static RoleConditionDto getRoleCondition() {
        RoleConditionDto roleConditionDto = new RoleConditionDto();
        try {
            roleConditionDto = (RoleConditionDto) ShiroUtils.getSessionAttribute(ShiroUtils.ROLE_CONDITION);
        } catch (Exception e) {
            MyphLogger.error("获取当前登录用户数据权限相关信息异常", e);
        }
        return roleConditionDto;
    }
    
    /**
     * 
     * @名称 getRegionInfo 
     * @描述 获取当前登录用户数据权限中大区信息
     * @返回类型 List<OrganizationDto>     
     * @日期 2017年7月13日 下午2:20:24
     * @创建人  吴阳春
     * @更新人  吴阳春
     *
     */
    public static List<OrganizationDto> getRegionInfo() {
        List<OrganizationDto> result = new ArrayList<OrganizationDto>();
        try {
            RoleConditionDto roleConditionDto = (RoleConditionDto) ShiroUtils.getSessionAttribute(ShiroUtils.ROLE_CONDITION);
            for(OrganizationDto dto:roleConditionDto.getOrgs()){
                if(dto.getOrgType() == ORGANIZATION_TYPE.REGION_TYPE.toNumber()){
                    result.add(dto);
                }
            }
        } catch (Exception e) {
            MyphLogger.error("获取当前登录用户数据权限中大区信息异常", e);
        }
        return result; 
    }

    /**
     * 
     * @名称 getStoreInfo 
     * @描述 根据大区ID获取当前登录用户数据权限中门店信息
     * @返回类型 List<OrganizationDto>     
     * @日期 2017年7月13日 下午2:22:16
     * @创建人  吴阳春
     * @更新人  吴阳春
     *
     */
    public static List<OrganizationDto> getStoreInfo(Long id) {
        List<OrganizationDto> result = new ArrayList<OrganizationDto>();
        try {
            RoleConditionDto roleConditionDto = (RoleConditionDto) ShiroUtils.getSessionAttribute(ShiroUtils.ROLE_CONDITION);
            for(OrganizationDto dto:roleConditionDto.getOrgs()){
                if(dto.getParentId().equals(id)){
                    result.add(dto);
                }
            }
        } catch (Exception e) {
            MyphLogger.error("根据大区ID获取当前登录用户数据权限中门店信息异常", e);
        }
        return result; 
    }

    /**
     *
     * @名称 getStoreInfo
     * @描述 获取当前登录用户数据权限中门店信息
     * @返回类型 List<OrganizationDto>
     * @日期 2017年7月13日 下午2:22:16
     * @创建人  吴阳春
     * @更新人  吴阳春
     *
     */
    public static List<OrganizationDto> getStoreInfo() {
        List<OrganizationDto> result = new ArrayList<OrganizationDto>();
        try {
            RoleConditionDto roleConditionDto = (RoleConditionDto) ShiroUtils.getSessionAttribute(ShiroUtils.ROLE_CONDITION);
            for(OrganizationDto dto:roleConditionDto.getOrgs()){
                if(dto.getOrgType() == ORGANIZATION_TYPE.STORE_TYPE.toNumber()){
                    result.add(dto);
                }
            }
        } catch (Exception e) {
            MyphLogger.error("获取当前登录用户数据权限中门店信息异常", e);
        }
        return result;
    }
    
}
