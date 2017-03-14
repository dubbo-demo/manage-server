package com.myph.manage.common.constant;

/**
 * Created by dell on 2017/3/9.
 */
public class BillPushConstant {

    public static final String CHANNEL_MYPH = "myph";

    public static final String CHANNEL_MYD= "myd";

    public static String getEdu(int code) {
        switch (code) {
            case 1:
                return "初中及以下";
            case 2:
                return "高中";
            case 3:
                return "中技";
            case 4:
                return "中专";
            case 5:
                return "大专";
            case 6:
                return "本科";
            case 7:
                return "硕士";
            case 8:
                return "博士";
            default:
                return "本科";
        }
    }

    /**
     * 单位属性
     * @param code
     * @return
     */
    public static String getOtherCompanyNature(int code) {
        switch (code) {
            case 1:
                return "个体户";
            case 2:
                return "私营企业";
            case 3:
                return "国有企业";
            case 4:
                return "事业单位";
            case 5:
                return "国家机关";
            case 6:
                return "外资/合资企业";
            case 7:
                return "其他";
            default:
                return "其他";
        }
    }

}
