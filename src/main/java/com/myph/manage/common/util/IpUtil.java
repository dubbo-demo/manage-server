/**   
 * @Title: IpUtil.java 
 * @Package: com.myph.manage.common.util
 * @company: 麦芽金服
 * @Description: TODO
 * @date 2016年9月22日 上午10:34:52 
 * @version V1.0   
 */
package com.myph.manage.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取用户IP地址
 * 
 * @ClassName: IpUtil
 * @Description: TODO
 * @author hf
 * @date 2016年9月22日 上午10:34:52
 * 
 */
public class IpUtil {

	public static String getIpAddr(HttpServletRequest request) {
		String ipAddress = null;
		// ipAddress = request.getRemoteAddr();
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			int index = ipAddress.indexOf(",");
			if (index > 0) {
				ipAddress = ipAddress.substring(0, index);
			}
		}
		return ipAddress;
	}

}
