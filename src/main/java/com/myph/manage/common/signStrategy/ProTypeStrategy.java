package com.myph.manage.common.signStrategy;

import com.myph.contract.dto.JkContractDto;
import com.myph.manage.po.PrintPo;
import com.myph.manage.po.PrintPlanPo;
import com.myph.sign.dto.ContractModelView;

/**
 * @author heyx
 * @version V1.0
 * @Package: com.myph.manage.common.signStrategy
 * @company: 麦芽金服
 * @Description: 签约策略
 * @date 2017/12/15
 */
public interface ProTypeStrategy {

    /**
     * @Description: 获得还款计划实例
     * @author heyx
     * @date 2017/12/15
     * @version V1.0
     */
    PrintPlanPo getPlan(PrintPo printPo);

    /**
     * 借款协议
     *
     * @param printPo
     * @return
     */
    ContractModelView loanProtocolModel(PrintPo printPo, ContractModelView contractModelView);

    /**
     * @Description: 信用咨询及管理服务协议
     * @author heyx
     * @date 2017/12/18
     * @version V1.0
     */
    ContractModelView counselingManageService(PrintPo printPo, JkContractDto jkContractDto,
            ContractModelView contractModelView);

    /**
     * @Description: 签约核查表
     * @author heyx
     * @date 2017/12/18
     * @version V1.0
     */
    ContractModelView contractVerificationForm(ContractModelView contractModelView);

    /**
     * @Description: 富友-麦芽金服数据科技有限公司专用账户协议
     * @author heyx
     * @date 2017/12/18
     * @version V1.0
     */
    ContractModelView accountSpecialProtocol(ContractModelView contractModelView);

    /**
     * @Description: 委托扣款授权书
     * @author heyx
     * @date 2017/12/18
     * @version V1.0
     */
    ContractModelView entrustDebitAuthorization(ContractModelView contractModelView);

}
