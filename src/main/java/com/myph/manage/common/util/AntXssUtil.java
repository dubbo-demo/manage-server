package com.myph.manage.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class AntXssUtil {
    /**
     * 黑名单 existsXssElement
     * 
     * @param
     * @return
     * @throws
     * @author zhuzheng
     * @date 2015年11月2日 下午1:12:16
     */
    public static boolean existsXssElement(String s) throws UnsupportedEncodingException {
        boolean res = false;
        if (s.indexOf("\\x") != -1 || s.indexOf("&#") != -1) {
            res = true;
        } else {
            s = s.toLowerCase();
            res = getIncludeHtmlSpecialCharsFlag(s);
            if (!res) {
                res = getIncludeSqlSpecialCharsFlag(s);
            }
        }
        return res;
    }

    /**
     * 替换一些特殊字符 replaceSpecialChars
     * 
     * @param
     * @return
     * @throws
     * @author zhuzheng
     * @date 2015年11月2日 下午1:12:16
     */
    public static String replaceSpecialChars(String s) throws UnsupportedEncodingException {
        String specialChar = "";
        s = s.replaceAll("(\r|\n|\t|\f|'|\")", "");
        s = s.replaceAll("(\r|\n|\t|\f|'|\")", "");
        int specialCharsLen = specailCharArray.length;
        for (int i = 0; i < specialCharsLen; i++) {
            specialChar = specailCharArray[i];
            specialChar = URLDecoder.decode(URLDecoder.decode(specialChar, "utf-8"), "utf-8");
            s = s.replaceAll(specialChar, "");
        }
        return s;
    }

    /**
     * 判断是否包含html特殊字符 getIncludeHtmlSpecialCharsFlag
     * 
     * @param
     * @return
     * @throws UnsupportedEncodingException
     * @throws
     * @author zhuzheng
     * @date 2015年11月2日 下午1:12:16
     */
    public static boolean getIncludeHtmlSpecialCharsFlag(String s) throws UnsupportedEncodingException {
        boolean res = false;
        s = replaceSpecialChars(s);
        if ( // XSS黑名单
        s.indexOf("javascript:") != -1 || s.indexOf("document.cookie") != -1 || s.indexOf("<script") != -1
                || s.indexOf("<iframe") != -1 || s.indexOf("\"><script") != -1 || s.indexOf("<style") != -1
                || s.indexOf("<img") != -1 || s.indexOf("onclick=") != -1 || s.indexOf("\"><style") != -1
                || s.indexOf(")//") != -1 || s.indexOf("\">") != -1 || s.indexOf("<body") != -1
                || s.indexOf("/xss/") != -1 || s.indexOf("onfocus") != -1
                || s.indexOf("alert") != -1 // || s.indexOf(";") != -1
                || s.indexOf("fromcharcode") != -1 || s.indexOf("eval") != -1 || s.indexOf("<a") != -1
                || s.indexOf("cookie") != -1 || s.indexOf("document.write") != -1 || s.indexOf(">@import ") != -1) {
            res = true;
        }
        return res;
    }

    /**
     * 判断是否包含sql特殊字符 getIncludeSqlSpecialCharsFlag
     * 
     * @param
     * @return
     * @throws
     * @author zhuzheng
     * @date 2015年11月2日 下午1:12:16
     */
    public static boolean getIncludeSqlSpecialCharsFlag(String str) {
        // 过滤掉的sql关键字，可以手动添加
        String badStr = "'|and |exec |execute |insert |select |delete |update |count(|trim|"
                + "char(|declare|sitename|net user|xp_cmdshell|like |create |drop |"
                + "table |from |grant |use |group_concat|column_name|"
                + "information_schema.columns|table_schema|union |where |order |by |"
                + "chr|mid(|master|truncate |or ";
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }

    public static String[] specailCharArray = { "%00", "%2B", "%25E3%2580%2580" };

}
