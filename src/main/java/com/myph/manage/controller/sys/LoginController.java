package com.myph.manage.controller.sys;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.rpc.RpcException;
import com.myph.base.common.SmsTemplateEnum;
import com.myph.base.dto.MenuDto;
import com.myph.base.service.MenuService;
import com.myph.common.constant.Constants;
import com.myph.common.exception.SmsException;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.util.DateUtils;
import com.myph.common.util.IpUtil;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.dto.EmployeePositionInfoDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.log.dto.OperatorLogDto;
import com.myph.log.service.LogService;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.permission.AuthPermission;
import com.myph.manage.permission.AuthorityType;
import com.myph.permission.service.PermissionService;
import com.myph.role.service.SysRoleService;
import com.myph.sms.service.SmsService;
import com.myph.user.dto.SysUserDto;
import com.myph.user.service.SysUserService;

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

	/**
	 * 麦芽普惠信贷系统登录
	 *
	 * @param model
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
				MyphLogger.info("--------登录后台成功--------更新最后登录时间，记录登录日志");
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
				SysUserDto sysUserDto = new SysUserDto();
				sysUserDto.setEmployeeId(employeeId);
				sysUserDto.setLastLoginTime(DateUtils.getCurrentDateTime());
				sysUserService.updateSysUserLastLoginTime(sysUserDto);
			}
		} catch (UnknownAccountException e) {
			result = "用户名或密码不正确";
		} catch (IncorrectCredentialsException e) {
			MyphLogger.error("登录麦芽普惠信贷管理系统异常", e);
			result = "用户名或密码不正确";
		} catch (LockedAccountException e) {
			result = "账户已锁定";
			MyphLogger.error("登录麦芽普惠信贷管理系统异常", e);
		} catch (ExcessiveAttemptsException e) {
			result = "用户名或密码错误次数过多";
			MyphLogger.error("登录麦芽普惠信贷管理系统异常", e);
		} catch (SessionException e) {
			result = "会话已过期,请重新登录";
			MyphLogger.error("登录麦芽普惠信贷管理系统异常", e);
		} catch (AuthenticationException e) {
			result = e.getMessage();
			MyphLogger.error("登录麦芽普惠信贷管理系统异常", e);
		} catch (RpcException e) {
			result = "系统异常,请稍后重试";
			MyphLogger.error("登录麦芽普惠信贷管理系统异常", e);
		} catch (Exception e) {
			result = "系统异常,请稍后重试";
			MyphLogger.error("登录麦芽普惠信贷管理系统异常", e);
		}
		MyphLogger.info("处理登录的post请求dologin.htm，手机号:{},短信验证码:{}", phone, smsCode);
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
			MyphLogger.error(e, "进入首页异常");
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
				MyphLogger.info("用户{}退出登录", userName);
				subject.logout();
			}
		} catch (Exception e) {
			MyphLogger.error(e, "用户退出登录异常");
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
			ServiceResult<SysUserDto> sysUserResult = sysUserService.selectSysUserById(employeeInfoDto.getId());
			SysUserDto sysUserDto = sysUserResult.getData();
			if (!sysUserResult.success() || sysUserDto == null) {// 账户状态0：禁用
				result = ServiceResult.newFailure("用户账户不存在，请联系系统管理员");
				return AjaxResult.formatFromServiceResult(result);
			} else if (Constants.NO_INT == sysUserDto.getAmountState()) {
				result = ServiceResult.newFailure("用户账户未被启用，请联系系统管理员");
				return AjaxResult.formatFromServiceResult(result);
			}
			result = sendSmsService.sendLoginMessage(phone, SmsTemplateEnum.LOGIN_TEMPLATE);
			return AjaxResult.formatFromServiceResult(result);
		} catch (SmsException e) {
			MyphLogger.error(e, "发送短信异常,请稍后重试");
			return AjaxResult.failed(e.getMessage());
		} catch (Exception e) {
			MyphLogger.error(e, "发送登录短信异常");
			return AjaxResult.failed("发送短信异常,请稍后重试");
		} finally {
			MyphLogger.access("{}发送登录短信", phone);
		}
	}
}