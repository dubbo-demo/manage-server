package com.myph.manage.common.shiro;

import com.alibaba.fastjson.JSONObject;
import com.myph.manage.common.shiro.dto.Authorization;
import com.way.common.result.AjaxResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Administrator
 */
public class FormAuthentication extends FormAuthenticationFilter {
    private static final Logger log = LoggerFactory
            .getLogger(FormAuthentication.class);
    public static final String FAIL_URL = "/sys/login";

    @Override
    protected String getUsername(ServletRequest request) {
        String username = super.getUsername(request);
        // 将初始带符号的用户名保存起来
        ShiroUtils.putToSession(ShiroUtils.ORIGIN_NAME_KEY, username);
        if (username != null) {
            username = ShiroUtils.getRealUserName(username);
        }
        return username;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        if (request.getAttribute(getFailureKeyAttribute()) != null) {
            return true;
        }
        /*return super.onAccessDenied(request, response, mappedValue);*/
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                log.debug("Login submission detected.  Attempting to execute login.");
                return executeLogin(request, response);
            } else {
                log.debug("Login page view.");
                //allow them to see the login page ;)
                return true;
            }
        } else {
            log.debug("Attempting to access a path which requires authentication.  Forwarding to the "
                    + "Authentication url [" + getLoginUrl() + "]");
            if (!"XMLHttpRequest"
                    .equalsIgnoreCase(((HttpServletRequest) request)
                            .getHeader("X-Requested-With"))) {// 不是ajax请求
                saveRequestAndRedirectToLogin(request, response);
            } else {
                log.debug("ajax json.");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                AjaxResult ajaxResult = AjaxResult.newSessionOut();
                out.println(JSONObject.toJSONString(ajaxResult));
                out.flush();
                out.close();
            }
            return false;
        }
    }

    @Override
    protected String getPassword(ServletRequest request) {
        String password = super.getPassword(request);
        if (password != null) {
            // 解密
        }
        return password;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
                                     ServletResponse response) {
        if ((String) ShiroUtils.getSessionAttribute(ShiroUtils.AUTHORIZATION_EXCEPTION_KEY) != null)
            ShiroUtils.putToSession(ShiroUtils.AUTHORIZATION_EXCEPTION_KEY, Authorization.AUTHORIZATION_USERNAME_FAIL);
        try {
            ShiroUtils.removeSessionAttribute(ShiroUtils.AUTHORIZATION_KEY);
            WebUtils.redirectToSavedRequest(request, response, FAIL_URL);
        } catch (IOException e1) {

        }

        return false;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String url = this.getSuccessUrl();
        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + url);
        return false;
    }

}
