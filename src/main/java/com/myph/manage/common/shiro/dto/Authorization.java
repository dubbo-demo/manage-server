/** 
 * Authorization.java
 * create on 2011-1-20
 * Copyright 2011-2015 Todaysteel All Rights Reserved.
 */
package com.myph.manage.common.shiro.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.shiro.authc.SimpleAuthenticationInfo;

/**
 * 记录登录交易员主要信息到Session的实体
 * 
 * @author <a href="liu.wei@163.com">liuwei</a>
 * @since version1.0
 */
public class Authorization extends SimpleAuthenticationInfo implements
		Serializable {
	private static final long serialVersionUID = 1L;
	public static final String AUTHORIZATION_USERNAME_FAIL= "用户名或密码错误！";
	public static final String AUTHORIZATION_WRONG_STATUS= "当前用户已停用！";
	public static final String AUTHORIZATION_LOCK_STATUS= "帐号已被锁定！";
	// 交易员信息
	private boolean isLogin = false;
	private boolean falgSetTranPassword;
	private String operatorID;// 交易员ID
	private String realName;// 交易员真实名称
	private String operatorNo;// 交易员编号
	private String mobile;// 手机号码
	private String logName;// 日志分页用
	private int bisauthenticate; // 是否认证
	private Date validDate;// 有效期
	private int memberType;// 会员类型
	private int loginType;// 登陆类型
	private int loginWinType;
	private String isex;
	private Date dbirthdate;
	private String sidno;
	private String sprovince;
	private String scity;
	private String saddress;
	private String username;

	// private String stransactionpassword;

	public int getBisauthenticate() {
		return bisauthenticate;
	}

	public int getLoginWinType() {
		return loginWinType;
	}

	public void setLoginWinType(int loginWinType) {
		this.loginWinType = loginWinType;
	}

	public String getIsex() {
		return isex;
	}

	public boolean isFalgSetTranPassword() {
		return falgSetTranPassword;
	}

	public void setFalgSetTranPassword(boolean falgSetTranPassword) {
		this.falgSetTranPassword = falgSetTranPassword;
	}

	public void setIsex(String isex) {
		this.isex = isex;
	}

	public Date getDbirthdate() {
		return dbirthdate;
	}

	public void setDbirthdate(Date dbirthdate) {
		this.dbirthdate = dbirthdate;
	}

	public String getSidno() {
		return sidno;
	}

	public void setSidno(String sidno) {
		this.sidno = sidno;
	}

	public String getSprovince() {
		return sprovince;
	}

	public void setSprovince(String sprovince) {
		this.sprovince = sprovince;
	}

	public String getScity() {
		return scity;
	}

	public void setScity(String scity) {
		this.scity = scity;
	}

	public String getSaddress() {
		return saddress;
	}

	public void setSaddress(String saddress) {
		this.saddress = saddress;
	}

	public void setBisauthenticate(int bisauthenticate) {
		this.bisauthenticate = bisauthenticate;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	// 扩展User类,扩展属性
	public Authorization(boolean falgSetTranPassword, String principal,
			String credentials, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			String operatorID, String realName, String operatorNo,
			int loginType, String mobile, int bisauthenticate, String isex,
			Date dbirthdate, String sidno, String sprovince, String scity,
			String saddress, int loginWinType) {
		// super(username, password, enabled, accountNonExpired,
		// credentialsNonExpired, accountNonLocked, authorities);
		super(principal, credentials, realName==null?"":realName);
		this.username = principal;
		this.falgSetTranPassword = falgSetTranPassword;
		this.operatorID = operatorID;
		this.realName = realName;
		this.operatorNo = operatorNo;
		this.loginType = loginType;
		this.mobile = mobile;
		this.loginWinType = loginWinType;
		this.bisauthenticate = bisauthenticate;
		this.isex = isex;
		this.dbirthdate = dbirthdate;
		this.sidno = sidno;
		this.sprovince = sprovince;
		this.scity = scity;
		this.saddress = saddress;

	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getLoginType() {
		return loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}

	public String getOperatorID() {
		return operatorID;
	}

	public void setOperatorID(String operatorID) {
		this.operatorID = operatorID;
	}

	public String getOperatorNo() {
		return operatorNo;
	}

	public void setOperatorNo(String operatorNo) {
		this.operatorNo = operatorNo;
	}

	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public int getMemberType() {
		return memberType;
	}

	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	
	

}