package com.myph.manage.controller.sys;

import com.alibaba.dubbo.rpc.RpcException;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.common.shiro.session.ShiroRedisSessionDao;
import com.myph.manage.permission.AuthPermission;
import com.myph.manage.permission.AuthorityType;
import com.myph.manage.po.EmployeeLoginDto;
import com.way.base.log.dto.OperatorLogDto;
import com.way.base.log.service.LogService;
import com.way.base.menu.dto.MenuDto;
import com.way.base.menu.service.MenuService;
import com.way.base.permission.dto.PermissionDto;
import com.way.base.permission.service.PermissionService;
import com.way.base.role.service.SysRoleService;
import com.way.base.sms.service.SmsService;
import com.way.common.constant.Constants;
import com.way.common.exception.SmsException;
import com.way.common.log.WayLogger;
import com.way.common.redis.CacheService;
import com.way.common.result.AjaxResult;
import com.way.common.result.ServiceResult;
import com.way.common.util.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 麦芽普惠登录
 *
 * @author dell
 */
@AuthPermission(authType = AuthorityType.NO)
@Controller
public class LoginController {

	@Autowired
	private MenuService menuService;

	@Autowired
	private SysRoleService sysRoleService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private SmsService sendSmsService;

	@Autowired
	private EmployeeInfoService employeeInfoService;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private LogService logService;
	
	@Autowired
    private RestTemplate restTemplate;
	
	@Autowired
    private ShiroRedisSessionDao shiroRedisSessionDao;
	
	@Value("#{configProperties['mycs_url']}")
	private String mycsUrl;

	public static final String SHIRO_REDIS_SESSION = "myph:shiro_redis:session";
	
	/**
	 * 麦芽普惠信贷系统登录
	 *
	 * @param
	 * @param phone
	 * @param smsCode
	 * @return
	 */
	@RequestMapping(value = "/dologin", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult login(HttpServletRequest request, String phone, String smsCode) {
		String result = "";
		try {
			if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(smsCode)) {
				return AjaxResult.failed("用户名或验证码不能为空");
			}
			Subject subject = SecurityUtils.getSubject();
			subject.login(new UsernamePasswordToken(phone, smsCode));
			if (subject.isAuthenticated()) {
				WayLogger.info("--------登录后台成功--------更新最后登录时间，记录登录日志");
				String userIpAddress = IpUtil.getIpAddr(request);
				Long employeeId = ShiroUtils.getCurrentUser().getId();
				// 记录登录日志,调用罗荣 的日志服务组件
				OperatorLogDto record = new OperatorLogDto();
				record.setOperatorTime(new Date());
				record.setOperatorDesc("登录操作");
				ShiroUtils.getSession().setAttribute(ShiroUtils.ORIGIN_IP_KEY, userIpAddress);
				record.setOperatorIp(ShiroUtils.getCurrentUserIp());
				record.setUserId(employeeId);
				logService.insert(record);
			}
		} catch (UnknownAccountException e) {
			result = "用户名或密码不正确";
		} catch (IncorrectCredentialsException e) {
			WayLogger.error("登录麦芽普惠信贷管理系统异常", e);
			result = "用户名或密码不正确";
		} catch (LockedAccountException e) {
			result = "账户已锁定";
			WayLogger.error("登录麦芽普惠信贷管理系统异常", e);
		} catch (ExcessiveAttemptsException e) {
			result = "用户名或密码错误次数过多";
			WayLogger.error("登录麦芽普惠信贷管理系统异常", e);
		} catch (SessionException e) {
			result = "会话已过期,请重新登录";
			WayLogger.error("登录麦芽普惠信贷管理系统异常", e);
		} catch (AuthenticationException e) {
			result = e.getMessage();
			WayLogger.error("登录麦芽普惠信贷管理系统异常", e);
		} catch (RpcException e) {
			result = "系统异常,请稍后重试";
			WayLogger.error("登录麦芽普惠信贷管理系统异常", e);
		} catch (Exception e) {
			result = "系统异常,请稍后重试";
			WayLogger.error("登录麦芽普惠信贷管理系统异常", e);
		}
		WayLogger.info("处理登录的post请求dologin.htm，手机号:{},短信验证码:{}", phone, smsCode);
		if (!"".equals(result)) {
			return AjaxResult.failed(result);
		} else {
			return AjaxResult.success();
		}
	}

	/**
	 * 登录页
	 *
	 * @return
	 */
	@RequestMapping(value = "/login")
	public String loginPage() {
		return "login";
	}

	/**
	 * 登录成功首页
	 *
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index() {
		try {
			Subject subject = SecurityUtils.getSubject();
			if (subject.isAuthenticated()) {
				EmployeeInfoDto employeeInfoDto = ShiroUtils.getCurrentUser();
				if (employeeInfoDto != null) {
					ServiceResult<EmployeePositionInfoDto> employeePositionResult = employeeInfoService.queryPositionInfoByEmployeeId(employeeInfoDto.getId());
					if (employeePositionResult.getData() != null) {
						ShiroUtils.getSession().setAttribute("position", employeePositionResult.getData());
					}
					ServiceResult<List<String>> roleResult = sysRoleService
							.getRolesByPositionId(employeeInfoDto.getPositionId());// 获取岗位对应的角色ID
					ServiceResult<List<Long>> permissionResult = permissionService
							.getPermissionsByRoleId(roleResult.getData());// 根据角色获取对应的权限ID
					ServiceResult<List<Long>> menuResult = permissionService
							.getMenuIdsByPerId(permissionResult.getData());// 根据权限ID获取菜单ID
					if (permissionResult.getData() != null) {
						ServiceResult<List<MenuDto>> menuDtoResult = menuService.getMenuListByIds(menuResult.getData());// 根据菜单ID获取所有菜单
						ShiroUtils.getSession().setAttribute("menus", menuDtoResult.getData());
					}
				}
			}
		} catch (Exception e) {
			WayLogger.error(e, "进入首页异常");
		}
		return "index";
	}

	/**
	 * 退出登录
	 *
	 * @return
	 */
	@RequestMapping(value = "/exitLogin")
	public String exitLogin() {
		try {
			Subject subject = SecurityUtils.getSubject();
			if (subject.isAuthenticated()) {
				// 记录退出日志
				// 更新此次登录的退出时间
				String userName = ShiroUtils.getCurrentUserName();
				String phone = ShiroUtils.getCurrentUser().getMobilePhone();
				WayLogger.info("用户{}退出登录", userName);
				subject.logout();
				//调用催收登录退出接口
				String url = mycsUrl + "/loginOut.htm";
				restTemplate.postForObject(url, phone, Object.class);
			}
		} catch (Exception e) {
			WayLogger.error(e, "用户退出登录异常");
		}
		return "redirect:login.htm";
	}

	/**
	 * 发送登录短信
	 *
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/sendLoginSmsCode", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult sendLoginSmsCode(String phone) {
		try {
			ServiceResult<String> result = null;
			ServiceResult<EmployeeInfoDto> employeeResult = employeeInfoService.getEntityByMobile(phone);
			EmployeeInfoDto employeeInfoDto = employeeResult.getData();
			if (null == employeeInfoDto) {
				result = ServiceResult.newFailure("用户名不存在");
				return AjaxResult.formatFromServiceResult(result);
			}
			if(employeeInfoDto.getUserFlag() == null || Constants.NO.equals(employeeInfoDto.getUserFlag())){
			    result = ServiceResult.newFailure("用户账户未被启用，请联系系统管理员");
                return AjaxResult.formatFromServiceResult(result);
            }
			if(employeeInfoDto.getIcmbFlag() == null || Constants.YES.equals(employeeInfoDto.getIcmbFlag())){
			    result = ServiceResult.newFailure("用户已离职，请联系系统管理员");
			    return AjaxResult.formatFromServiceResult(result);
            }
			result = sendSmsService.sendLoginMessage(phone, SmsTemplateEnum.LOGIN_TEMPLATE);
			return AjaxResult.formatFromServiceResult(result);
		} catch (SmsException e) {
			WayLogger.error(e, "发送短信异常,请稍后重试");
			return AjaxResult.failed(e.getMessage());
		} catch (Exception e) {
			WayLogger.error(e, "发送登录短信异常");
			return AjaxResult.failed("发送短信异常,请稍后重试");
		} finally {
			WayLogger.access("{}发送登录短信", phone);
		}
	}
	
	@RequestMapping(value = "/checkLogin", method = RequestMethod.GET)
    @ResponseBody
	public ServiceResult<EmployeeLoginDto> checkLogin(String token) {
        WayLogger.info("checkLogin :{}",token);
        final String redisKey = SHIRO_REDIS_SESSION + ":" + token;
        // 验证是否存在redis缓存，存在表示已登录
        if (!CacheService.KeyBase.isExistsKey(redisKey)) {
            return ServiceResult.newFailure();
        }
        // 抽取员工、菜单、按钮权限信息给催收系统
        EmployeeLoginDto employeeLoginDto = new EmployeeLoginDto();
        Session session = (Session) CacheService.StringKey.getCached(redisKey.getBytes());
       // Session session = shiroRedisSessionDao.readSession(redisKey.getBytes());
        EmployeeInfoDto employeeInfoDto = (EmployeeInfoDto) session.getAttribute("currentUser");
        ServiceResult<List<String>> roleResult = sysRoleService
                .getRolesByPositionId(employeeInfoDto.getPositionId());// 获取岗位对应的角色ID
        ServiceResult<List<Long>> permissionResult = permissionService
                .getPermissionsByRoleId(roleResult.getData());// 根据角色获取对应的权限ID
        ServiceResult<List<Long>> menuResult = permissionService
                .getMenuIdsByPerId(permissionResult.getData());// 根据权限ID获取菜单ID
        Map<String, List<String>> menuUrlPermissionCode = new HashMap<String, List<String>>();
        if(menuResult.getData() != null){
            for(int i=0;i<menuResult.getData().size();i++){
                //获取菜单信息
                ServiceResult<MenuDto> menuDtoResult = menuService.getMenuById(menuResult.getData().get(i));
                //获取权限信息
                ServiceResult<List<PermissionDto>> permissionDtoListResult = permissionService.getPermissionByMenuId(menuResult.getData().get(i));
                List<PermissionDto> permissionDtoList = permissionDtoListResult.getData();
                List<String> permissionCodeList = new ArrayList<String>();
                //将角色拥有的权限信息放在菜单下
                for(int j=0;j<permissionDtoList.size();j++){
                    if(permissionResult.getData().contains(permissionDtoList.get(j).getId())){
                        permissionCodeList.add(permissionDtoList.get(j).getPermissionCode()); 
                    }
                }
                menuUrlPermissionCode.put(menuDtoResult.getData().getMenuUrl(), permissionCodeList);
            }
			ServiceResult<List<MenuDto>> menuDtoResult = menuService.getMenuListByIds(menuResult.getData());// 根据菜单ID获取所有菜单
			if(menuDtoResult.getData() != null){
				employeeLoginDto.setMenuDtos(menuDtoResult.getData());
			}
        }
        BeanUtils.copyProperties(employeeInfoDto, employeeLoginDto);
        ServiceResult<EmployeePositionInfoDto> employeePositionResult = employeeInfoService.queryPositionInfoByEmployeeId(employeeInfoDto.getId());
        employeeLoginDto.setPositionName(employeePositionResult.getData().getPositionName());
        employeeLoginDto.setMenuUrlPermissionCode(menuUrlPermissionCode);
        return ServiceResult.newSuccess(employeeLoginDto);
    }
}
