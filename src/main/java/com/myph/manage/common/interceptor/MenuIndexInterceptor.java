/**   
 * @Title: MenuIndexInterceptor.java 
 * @Package: com.myph.manage.controller.sys
 * @company: 麦芽金服
 * @Description: TODO
 * @date 2016年9月5日 下午6:45:33 
 * @version V1.0   
 */
package com.myph.manage.common.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * 首页左边菜单选中效果拦截器
 * @ClassName: MenuIndexInterceptor 
 * @Description: TODO
 * @author hf 
 * @date 2016年9月5日 下午6:45:33 
 *  
 */
public class MenuIndexInterceptor implements HandlerInterceptor {
	
	

	/* (非 Javadoc) 
	 * <p>Title: preHandle</p> 
	 * <p>Description: </p> 
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception 
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object) 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String menu_index = request.getParameter("menu_index");
		if(StringUtils.isNotBlank(menu_index)) {
			request.getSession().setAttribute("menu_index", menu_index);
		}
		String child_index = request.getParameter("child_index");
		if(StringUtils.isNotBlank(child_index)) {
			request.getSession().setAttribute("child_index", child_index);
		}
		return true;
	}

	/* (非 Javadoc) 
	 * <p>Title: postHandle</p> 
	 * <p>Description: </p> 
	 * @param request
	 * @param response
	 * @param handler
	 * @param modelAndView
	 * @throws Exception 
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView) 
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (非 Javadoc) 
	 * <p>Title: afterCompletion</p> 
	 * <p>Description: </p> 
	 * @param request
	 * @param response
	 * @param handler
	 * @param ex
	 * @throws Exception 
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception) 
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
