package com.myph.manage.controller.order;

import com.myph.common.constant.Constants;
import com.myph.common.exception.BaseException;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heyx
 * @version V1.0
 * @Package: com.myph.manage.controller.order
 * @company: 麦芽金服
 * @Description: 人工代扣，提前结清代扣，代偿
 * @date 2017/8/29
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
    public AjaxResult manMadeRepayCard(String billNo, Integer payType, Integer isAdvanceSettleEnum) {
        MyphLogger.info("人工代扣操作界面-billNo【{}】,payType:{},isAdvanceSettleEnum:{}", billNo, payType);

        // 代扣
        if (payType.equals(BillChangeTypeEnum.PERSON_WITHHOLD.getCode())) {
            BankCardInfoDto card = jkRepaymentPlanService.queryBankCardInfoByBillNo(billNo);
            if (null != card) {
                return AjaxResult.success(card);
            } else {
                return AjaxResult.failed("没有找到该账单客户卡信息");
            }
        }

        // 代偿
        if (payType.equals(BillChangeTypeEnum.PERSON_COMPENSATE.getCode())) {
            EmployeeInfoDto user = ShiroUtils.getCurrentUser();
            // 获取银行卡信息
            ServiceResult<List<UserCardInfoDto>> result = cardService.queryUserCardInfo(user.getMobilePhone());
            if (!result.success()) {
                return AjaxResult.failed(result.getMessage());
            }
            if (null == result && result.getData().size() > 0) {
                for (UserCardInfoDto dto : result.getData()) {
                    if (dto.getIDKFlag().equals(Constants.YES_INT)) {
                        ServiceResult<SysPayBankDto> payBank = sysPayBankService.selectBySbankNo(dto.getBankNo());
                        if (payBank.success()) {
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
        // 四要素检查
        AjaxResult fourInfoCheck = isFourInfoCheck(param);
        if(!fourInfoCheck.isSuccess()) {
            return fourInfoCheck;
        }
        EmployeeInfoDto user = ShiroUtils.getCurrentUser();
        param.setCreateUser(user.getEmployeeName());
        param.setIsAdvanceSettle(IsAdvanceSettleEnum.NO.getCode());
        param.setPayType(PayTypeEnum.PAY_PERSON_KOU.getCode());
        // 发起人工代扣
        try {
            ServiceResult<String> result = repayManMadeService.repayManMade(param);
            if (!result.success()) {
                MyphLogger.info("发起人工代扣-失败【{}】", result.getMessage());
                return AjaxResult.failed(result.getMessage());
            }
        } catch (Exception e) {
            MyphLogger.error("发起人工代扣-异常【{}】", e);
            return AjaxResult.failed("发起人工代扣-异常");
        }
        return AjaxResult.success();
    }

    /**
     * @Description: 对公
     * @author heyx
     * @date 2017/8/29
     * @version V1.0
     */
    @RequestMapping("/businessRepay")
    @ResponseBody
    public AjaxResult businessRepay(HkBillRepayRecordDto param) {
        MyphLogger.info("发起对公-参数【{}】", param);
        EmployeeInfoDto user = ShiroUtils.getCurrentUser();
        param.setCreateUser(user.getEmployeeName());
        param.setIsAdvanceSettle(IsAdvanceSettleEnum.NO.getCode());
        param.setPayType(PayTypeEnum.PAY_MONEY.getCode());
        try {
            ServiceResult<String> result = repayManMadeService
                    .businessRepay(param.getBillNo(), param.getPayAmount(), param.getCreateUser());
            if (!result.success()) {
                MyphLogger.info("发起对公-失败【{}】", result.getMessage());
                return AjaxResult.failed(result.getMessage());
            }
        } catch (Exception e) {
            MyphLogger.error("发起对公-异常【{}】", e);
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
        // 四要素检查
        AjaxResult fourInfoCheck = isFourInfoCheck(param);
        if(!fourInfoCheck.isSuccess()) {
            return fourInfoCheck;
        }
        EmployeeInfoDto user = ShiroUtils.getCurrentUser();
        param.setCreateUser(user.getEmployeeName());
        param.setIsAdvanceSettle(IsAdvanceSettleEnum.NO.getCode());
        param.setPayType(PayTypeEnum.PAI_PERSON_CHANG.getCode());
        // 发起人工代偿
        try {
            ServiceResult<String> result = repayManMadeService.repayManMade(param);
            if (!result.success()) {
                return AjaxResult.failed(result.getMessage());
            }
        } catch (Exception e) {
            MyphLogger.error("发起人工代偿-异常【{}】", e);
            return AjaxResult.failed("发起人工代偿异常");
        }
        return AjaxResult.success();
    }

    private AjaxResult isFourInfoCheck(HkBillRepayRecordDto param) {
        if(null == param) {
            return AjaxResult.failed("发起异常，参数为空");
        }
        if(StringUtils.isEmpty(param.getUsername())) {
            return AjaxResult.failed("发起异常，银行卡持有人姓名为空");
        }
        if(StringUtils.isEmpty(param.getIdBankNo())) {
            return AjaxResult.failed("发起异常，银行卡号为空");
        }
        if(StringUtils.isEmpty(param.getIdCardNo())) {
            return AjaxResult.failed("发起异常，身份证为空");
        }
        if(StringUtils.isEmpty(param.getReservedPhone())) {
            return AjaxResult.failed("发起异常，银行预留手机号为空");
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
        // 四要素检查
        AjaxResult fourInfoCheck = isFourInfoCheck(param);
        if(!fourInfoCheck.isSuccess()) {
            return fourInfoCheck;
        }
        // 发起人工代扣
        try {
            EmployeeInfoDto user = ShiroUtils.getCurrentUser();
            param.setCreateUser(user.getEmployeeName());
            // 设置为1，提前结清
            param.setIsAdvanceSettle(IsAdvanceSettleEnum.YES.getCode());
            ServiceResult<String> result = repayManMadeService.repayManMade(param);
            if (!result.success()) {
                return AjaxResult.failed(result.getMessage());
            }
        } catch (Exception e) {
            MyphLogger.error("发起提前结清扣款-异常【{}】", param, e);
            return AjaxResult.failed("发起提前结清扣款异常");
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
        try {
            EmployeeInfoDto user = ShiroUtils.getCurrentUser();
            param.setCreateUser(user.getEmployeeName());
            ServiceResult<String> result = null;
            // 判断是否提前结清
            if (param.getIsAdvanceSettle().equals(IsAdvanceSettleEnum.YES.getCode())) {
                result = repayManMadeService.reductionRepayAdvanceSettle(param);
            } else {
                result = repayManMadeService.reductionRepay(param);
            }

            if (!result.success()) {
                return AjaxResult.failed(result.getMessage());
            }
        } catch (Exception e) {
            MyphLogger.error("发起提前结清减免-异常【{}】", param, e);
            return AjaxResult.failed("发起提前结清减免异常");
        }
        return AjaxResult.success();
    }

}
