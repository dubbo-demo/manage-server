package com.myph.manage.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.myph.common.log.MyphLogger;
import com.myph.employee.dto.EmployeeInfoDto;

public class CommonUtil {
    /** 中国公民身份证号码最小长度。 */
    public static final int CHINA_ID_MIN_LENGTH = 15;

    /** 中国公民身份证号码最大长度。 */
    public static final int CHINA_ID_MAX_LENGTH = 18;

    /** 每位加权因子 */
    public static final int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

    /**
     * 方法名： getUserMobile 描述： 获取登录的mobile
     * 
     * @author zhuzheng 创建时间：2015年11月23日 下午3:22:36
     * @return
     * 
     */
    public static String getUserMobile() {
        String userMobile = " ";
        try {
            Subject currentUser = SecurityUtils.getSubject();
            EmployeeInfoDto employee = (EmployeeInfoDto) currentUser.getSession().getAttribute("currentUser");
            userMobile = employee.getMobilePhone();
        } catch (Exception e) {
            MyphLogger.debug("不存在的session/session过期");
        }
        return userMobile;
    }

    /**
     * 方法名： getUserId 描述： 获取登录的user
     * 
     * @author zhuzheng 创建时间：2015年9月29日 下午5:47:13
     * @return
     * 
     */
    public static EmployeeInfoDto getUser() {
        Subject currentUser = SecurityUtils.getSubject();
        EmployeeInfoDto employee = (EmployeeInfoDto) currentUser.getSession().getAttribute("currentUser");
        return employee;
    }

    /**
     * 方法名： getAgeByIdCard 描述： 计算年龄
     * 
     * @author zhuzheng 创建时间：2015年10月13日 上午9:34:25
     * @param date2
     * @return
     * @throws ParseException
     * 
     */
    public static String getAgeByIdCard(String idCard) {

        if (StringUtils.isBlank(idCard)) {
            return "用户出生日期未知";
        }
        int iAge = 0;
        if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        String year = idCard.substring(6, 10);
        Calendar cal = Calendar.getInstance();
        int iCurrYear = cal.get(Calendar.YEAR);
        iAge = iCurrYear - Integer.valueOf(year);
        return iAge + "";
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     * 
     * @param iArr
     * @return 身份证编码。
     */
    public static int getPowerSum(int[] iArr) {
        int iSum = 0;
        if (power.length == iArr.length) {
            for (int i = 0; i < iArr.length; i++) {
                for (int j = 0; j < power.length; j++) {
                    if (i == j) {
                        iSum = iSum + iArr[i] * power[j];
                    }
                }
            }
        }
        return iSum;
    }

    /**
     * 数字验证
     * 
     * @param val
     * @return 提取的数字。
     */
    public static boolean isNum(String val) {
        return val == null || "".equals(val) ? false : val.matches("^[0-9]*$");
    }

    /**
     * 将15位身份证号码转换为18位
     * 
     * @param idCard 15位身份编码
     * @return 18位身份编码
     */
    public static String conver15CardTo18(String idCard) {
        String idCard18 = "";
        if (idCard.length() != CHINA_ID_MIN_LENGTH) {
            return null;
        }
        if (isNum(idCard)) {
            // 获取出生年月日
            String birthday = idCard.substring(6, 12);
            Date birthDate = null;
            try {
                birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
            } catch (ParseException e) {
                MyphLogger.error(e, "身份证转换异常idCard:" + idCard);
            }
            Calendar cal = Calendar.getInstance();
            if (birthDate != null)
                cal.setTime(birthDate);
            // 获取出生年(完全表现形式,如：2010)
            String sYear = String.valueOf(cal.get(Calendar.YEAR));
            idCard18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
            // 转换字符数组
            char[] cArr = idCard18.toCharArray();
            if (cArr != null) {
                int[] iCard = converCharToInt(cArr);
                int iSum17 = getPowerSum(iCard);
                // 获取校验位
                String sVal = getCheckCode18(iSum17);
                if (sVal.length() > 0) {
                    idCard18 += sVal;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
        return idCard18;
    }

    /**
     * 将power和值与11取模获得余数进行校验码判断
     * 
     * @param iSum
     * @return 校验位
     */
    public static String getCheckCode18(int iSum) {
        String sCode = "";
        switch (iSum % 11) {
            case 10:
                sCode = "2";
                break;
            case 9:
                sCode = "3";
                break;
            case 8:
                sCode = "4";
                break;
            case 7:
                sCode = "5";
                break;
            case 6:
                sCode = "6";
                break;
            case 5:
                sCode = "7";
                break;
            case 4:
                sCode = "8";
                break;
            case 3:
                sCode = "9";
                break;
            case 2:
                sCode = "x";
                break;
            case 1:
                sCode = "0";
                break;
            case 0:
                sCode = "1";
                break;
            default:
                break;
        }
        return sCode;
    }

    /**
     * 将字符数组转换成数字数组
     * 
     * @param ca 字符数组
     * @return 数字数组
     */
    public static int[] converCharToInt(char[] ca) {
        int len = ca.length;
        int[] iArr = new int[len];
        try {
            for (int i = 0; i < len; i++) {
                iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
            }
        } catch (NumberFormatException e) {
            MyphLogger.error(e, "数字转换异常");
        }
        return iArr;
    }

    /**
     * 方法名： getClientIP 描述： 获取客户端真实IP
     * 
     * @author zhuzheng 创建时间：2015年11月7日 上午10:24:46
     * @param request
     * @return
     * 
     */
    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");

        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // ip=getFormatIpString(ip);
        return ip;
    }

    /**
     * 
     * @名称 getBirthdayByIdCard
     * @描述 根据身份证获取出生日期
     * @返回类型 Date
     * @日期 2016年10月13日 下午3:26:46
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    public static Date getBirthdayByIdCard(String idCard) {
        if (null == idCard) {
            return null;
        }
        try {
            // 18位身份证
            if (CHINA_ID_MAX_LENGTH == idCard.length()) {
                return new SimpleDateFormat("yyyyMMdd").parse(idCard.substring(6, 14));
            }
            // 15位身份证
            else if (CHINA_ID_MIN_LENGTH == idCard.length()) {
                return new SimpleDateFormat("yyMMdd").parse(idCard.substring(6, 12));
            }
        } catch (ParseException e) {
            MyphLogger.error(e, "异常[身份证出生日期转化]");
        }
        return null;
    }

    /**
     * 方法名： getFormatIpString 描述： * 获取ip标准格式 例如172.18.0.68，转化为172.018.000.068
     * 
     * @author zhuzheng 创建时间：2015年11月7日 上午10:24:17
     * @param ip
     * @return
     * 
     */
    private static String getFormatIpString(String ip) {
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.000.000.001";
        }
        String ipFormatString = "";
        String[] ipArray = ip.split("\\.");
        int ipArrayLen = ipArray.length;
        for (int i = 0; i < ipArrayLen; i++) {
            for (int j = ipArray[i].length(); j < 3; j++) {
                ipArray[i] = "0" + ipArray[i];
            }
        }
        ipFormatString = ipArray[0] + "." + ipArray[1] + "." + ipArray[2] + "." + ipArray[3];
        return ipFormatString;
    }

}