package com.myph.manage.controller.order;

import com.myph.common.constant.Constants;
import com.myph.common.exception.DatabaseException;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.constant.BillChangeTypeEnum;
import com.myph.constant.IsAdvanceSettleEnum;
import com.myph.constant.PayTypeEnum;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.hkrecord.dto.HkBillRepayRecordDto;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.controller.BaseController;
import com.myph.member.card.dto.UserCardInfoDto;
import com.myph.member.card.service.CardService;
import com.myph.payBank.dto.SysPayBankDto;
import com.myph.payBank.service.SysPayBankService;
import com.myph.reduction.dto.HkReductionRecordDto;
import com.myph.repayManMade.service.RepayManMadeService;
import com.myph.repaymentPlan.dto.BankCardInfoDto;
import com.myph.repaymentPlan.service.JkRepaymentPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package: com.myph.manage.controller.order
 * @company: 麦芽金服
 * @Description: 人工代扣，提前结清代扣，代偿
 * @author heyx
 * @date 2017/8/29
 * @version V1.0
 */
@Controller
@RequestMapping("/repayMade")
public class OrderRepayMadeController extends BaseController {

    @Autowired
    private RepayManMadeService repayManMadeService;

    @Autowired
    private CardService cardService;

    @Autowired
    private JkRepaymentPlanService jkRepaymentPlanService;

    @Autowired
    private SysPayBankService sysPayBankService;
    /**
     * @Description: 银行卡信息
     * @author heyx
     * @date 2017/8/29
     * @version V1.0
     */
    @RequestMapping("/manMadeRepayCard")
    @ResponseBody
    public AjaxResult manMadeRepayCard(String billNo,Integer payType,Integer isAdvanceSettleEnum) {
        MyphLogger.info("人工代扣操作界面-billNo【{}】,payType:{},isAdvanceSettleEnum:{}", billNo,payType);

        // 代扣
        if(payType.equals(BillChangeTypeEnum.PERSON_WITHHOLD.getCode())) {
            List<String> parm = new ArrayList<String>();
            parm.add(billNo);
            List<BankCardInfoDto> cards = jkRepaymentPlanService.queryBankCardInfoByBillNo(parm);
            if(null != cards && !cards.isEmpty()) {
                return AjaxResult.success(cards.get(0));
            } else {
                return AjaxResult.failed("没有找到该账单客户卡信息");
            }
        }

        // 代偿
        if(payType.equals(BillChangeTypeEnum.PERSON_COMPENSATE.getCode())) {
            EmployeeInfoDto user = ShiroUtils.getCurrentUser();
            //TODO 获取银行卡信息
            ServiceResult<List<UserCardInfoDto>> result = cardService.queryUserCardInfo(user.getMobilePhone());
            if(!result.success()){
                return AjaxResult.failed(result.getMessage());
            }
            if(result.getData().size() > 0){
                for(UserCardInfoDto dto:result.getData()){
                    if(dto.getIDKFlag().equals(Constants.YES_INT)){
                        ServiceResult<SysPayBankDto> payBank = sysPayBankService.selectBySbankNo(dto.getBankNo());
                        if(payBank.success()) {
                            dto.setBankTypeName(payBank.getData().getSname());
                        }
                        return AjaxResult.success(dto);
                    }
                }
            }
        }

        return AjaxResult.success();
    }

    /**
     * @Description: 人工代扣
     * @author heyx
     * @date 2017/8/29
     * @version V1.0
     */
    @RequestMapping("/manMadeRepay")
    @ResponseBody
    public AjaxResult manMadeRepay(HkBillRepayRecordDto param, Model model) {
        MyphLogger.info("发起人工代扣-参数【{}】", param);
        param.setPayType(PayTypeEnum.PAY_PERSON_KOU.getCode());
        //TODO 发起人工代扣
        try {
            ServiceResult<String> result =  repayManMadeService.repayManMade(param);
            if(!result.success()) {
                AjaxResult.failed(result.getMessage());
                MyphLogger.info("发起人工代扣-失败【{}】", result.getMessage());
            }
        } catch (DatabaseException e) {
            MyphLogger.error("发起人工代扣-异常【{}】", e);
            return AjaxResult.failed(e.getMessage());
        }
        return AjaxResult.success();
    }

    /**
     * @Description: 人工代偿
     * @author heyx
     * @date 2017/8/29
     * @version V1.0
     */
    @RequestMapping("/userMadeRepay")
    @ResponseBody
    public AjaxResult userMadeRepay(HkBillRepayRecordDto param, Model model) {
        MyphLogger.info("发起人工代偿-参数【{}】", param);
        param.setPayType(PayTypeEnum.PAY_PERSON_KOU.getCode());
        //TODO 发起人工代扣
        try {
            ServiceResult<String> result =  repayManMadeService.repayManMade(param);
            if(!result.success()) {
                AjaxResult.failed(result.getMessage());
            }
        } catch (DatabaseException e) {
            MyphLogger.error("发起人工代偿-异常【{}】", e);
            return AjaxResult.failed(e.getMessage());
        }
        return AjaxResult.success();
    }

    /**
     * @Description: 提前结清
     * @author heyx
     * @date 2017/8/29
     * @version V1.0
     */
    @RequestMapping("/advanceSettleMadeRepay")
    @ResponseBody
    public AjaxResult advanceSettleMadeRepay(HkBillRepayRecordDto param, Model model) {
        MyphLogger.info("发起提前结清扣款-参数【{}】", param);
        // 设置为1，提前结清
        param.setIsAdvanceSettle(1);
        //TODO 发起人工代扣
        try {
            ServiceResult<String> result =  repayManMadeService.repayManMade(param);
            if(!result.success()) {
                AjaxResult.failed(result.getMessage());
            }
        } catch (DatabaseException e) {
            MyphLogger.error("发起提前结清扣款-异常【{}】",param, e);
            return AjaxResult.failed(e.getMessage());
        }
        return AjaxResult.success();
    }

    /**
     * @Description: 人工减免
     * @author heyx
     * @date 2017/8/29
     * @version V1.0
     */
    @RequestMapping("/redutionMadeRepay")
    @ResponseBody
    public AjaxResult redutionMadeRepay(HkReductionRecordDto param, Model model) {
        MyphLogger.info("发起提前结清减免-参数【{}】", param);
        //TODO 发起人工代扣
        try {
            ServiceResult<String> result = null;
            // 判断是否提前结清
            if(param.getIsAdvanceSettle().equals(IsAdvanceSettleEnum.YES.getCode())) {
                result = repayManMadeService.reductionRepay(param);
            } else {
                result = repayManMadeService.reductionRepayAdvanceSettle(param);
            }

            if(!result.success()) {
                AjaxResult.failed(result.getMessage());
            }
        } catch (DatabaseException e) {
            MyphLogger.error("发起提前结清减免-异常【{}】",param, e);
            return AjaxResult.failed(e.getMessage());
        }
        return AjaxResult.success();
    }

}
