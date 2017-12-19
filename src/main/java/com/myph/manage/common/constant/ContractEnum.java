package com.myph.manage.common.constant;

/**
 * 合同类型
 *
 * @ClassName: ContarctTypeEnum
 * @Description: 默认
 * @author hf
 * @date 2016年10月27日 下午7:25:19
 *
 */
public enum ContractEnum {
    // 利用构造函数传参
    CREDIT_LOAN_PROTOCOL(1, "借款协议", "credit_loan_protocol"), CREDIT_COUNSELING_MANAGE_SERVICE(2, "信用咨询及管理服务协议",
            "credit_counseling_manage_service"), REPAYMENT_REMINDER(3, "按期还款温馨提示",
            "repayment_reminder"), FY_ACCOUNT_SPECIAL_PROTOCOL(4, "富友-麦芽金服数据科技有限公司专用账户协议",
            "fy_account_special_protocol"), CUSTOMER_CONTRACT_VERIFICATION_FORM(5, "签约核查表",
            "customer_contract_verification_form"), ENTRUST_DEBIT_AUTHORIZATION(6,
            "委托扣款授权书", "entrust_debit_authorization");

    private int type;

    private String desc;

    private String contarctName;

    ContractEnum(int type, String name, String contarctName) {
        this.desc = name;
        this.type = type;
        this.contarctName = contarctName;
    }

    public int getType() {
        return type;
    }

    public static String getViewName(int type) {
        for (ContractEnum e : ContractEnum.values()) {
            if (e.getType() == type) {
                return e.getContarctName();
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public String getContarctName() {
        return contarctName;
    }
}
