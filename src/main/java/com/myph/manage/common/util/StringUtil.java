package com.myph.manage.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目名称：  myLoan-manage   
 * 类名称：  StringUtil   
 * 描述：  字符串帮助类
 * @author  zhuzheng   
 * 创建时间：  2015年11月16日 下午3:24:25 
 * 修改人：zhuzheng    修改日期： 2015年11月16日
 * 修改备注：
 *
 */
public class StringUtil {
	public static final String PHONE_PATTERN = "^0?1[34578]\\d{9}$";
	public static final String MONEY_PATTERN = "^[0-9]*(\\.[0-9]{1,2})?$";
	
	/**
	 * %
	 */
	public static final String HARDRED_STRING = "%";
	/** 
	 * 方法名：  isMobileNO 
	 * 描述：  判断是否正确的手机号
	 * @author  zhuzheng   
	 * 创建时间：2015年11月16日 下午3:25:31
	 * @param phoneNo
	 * @return
	 *
	 */
	public static boolean isMobileNO(String phoneNo){
        Pattern p = Pattern.compile(PHONE_PATTERN);
        Matcher m = p.matcher(phoneNo);
        return m.matches();
    }
	/** 
	 * 方法名：  isMoney 
	 * 描述：  判断是否正确数字金额字符串
	 * @author  zhuzheng   
	 * 创建时间：2015年12月11日 下午3:13:45
	 * @param moneyStr
	 * @return
	 *
	 */
	public static boolean isMoney(String moneyStr){
		Pattern p = Pattern.compile(MONEY_PATTERN);
		Matcher m = p.matcher(moneyStr);
		return m.matches();
	}
	
	/**
	 * 隐藏电话号码	
	 * 显示前三，后四位，如：186****6097
	 * dealMobile  
	 * @param    
	 * @return  
	 * @throws 
	 * @author zhuzheng
	 * @date   2014年7月8日 下午2:03:07
	 */
	public static String getDealMobile(String mobile){
		String mobileDeal="";
		if(mobile!=null && !"null".equals(mobile) && !"".equals(mobile)){
			mobileDeal=mobile.substring(0, 3)+"****"+mobile.substring(7);
		}
		return mobileDeal;
	}
	
	public static void main(String[] args) {
		System.out.println(isMoney("1000.0"));
	}
	/** 
	 * 方法名：  formatScore 
	 * 描述：  格式化分数
	 * @author  zhuzheng   
	 * 创建时间：2015年12月17日 上午11:26:23
	 * @param string
	 * @return
	 *
	 */
	public static Double formatScore(String str) {
		
		if(str==null || !str.matches("^[0-9]+(\\.[0-9]+)?$"))
			return null;
		return Double.parseDouble(str);
	}
}
