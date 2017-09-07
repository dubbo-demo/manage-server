package com.myph.manage.controller.card;

import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.member.card.dto.CardParamDto;
import com.myph.member.card.dto.UserCardInfoDto;
import com.myph.member.card.service.CardService;
import com.myph.payBank.dto.SysPayBankDto;
import com.myph.payBank.service.SysPayBankService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/card")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private SysPayBankService sysPayBankService;

    /**
     * @名称 bindCard
     * @描述 绑卡
     * @返回类型 AjaxResult
     * @日期 2017年8月31日 下午6:45:40
     * @创建人 吴阳春
     * @更新人 吴阳春
     */
    @RequestMapping("/bindCard")
    @ResponseBody
    public AjaxResult bindCard(UserCardInfoDto dto) {
        try {
            //参数初始化
            initBindCardParam(dto);
            ServiceResult<Integer> result = cardService.bindCard(dto);
            if (result.success()) {
                return AjaxResult.success();
            } else {
                return AjaxResult.failed(result.getMessage());
            }
        } catch (Exception e) {
            MyphLogger.error(e, "绑卡异常,入参:{}", dto);
            return AjaxResult.failed("绑卡异常");
        }
    }

    /**
     * @名称 authentication
     * @描述 鉴权
     * @返回类型 AjaxResult
     * @日期 2017年8月31日 下午6:46:12
     * @创建人 吴阳春
     * @更新人 吴阳春
     */
    @RequestMapping("/authentication")
    @ResponseBody
    public AjaxResult authentication(CardParamDto dto) {
        try {
            //参数初始化
            dto.setSFromIp(ShiroUtils.getCurrentUserIp());
            ServiceResult<Integer> result = cardService.authentication(dto);
            if (result.success()) {
                return AjaxResult.success();
            } else {
                return AjaxResult.failed(result.getMessage());
            }
        } catch (Exception e) {
            MyphLogger.error(e, "鉴权异常,入参:{}", dto);
            return AjaxResult.failed("鉴权异常");
        }
    }

    /**
     * @名称 queryUserCardInfo
     * @描述 查询用户代扣卡信息
     * @描述 根据iDKFlag判断是否为代扣卡，支付中心限制一个人只能绑定一张代扣卡。
     * @返回类型 AjaxResult
     * @日期 2017年8月31日 下午6:58:49
     * @创建人 吴阳春
     * @更新人 吴阳春
     */
    @RequestMapping("/queryUserCardInfo")
    @ResponseBody
    public AjaxResult queryUserCardInfo(String phone) {
        try {
            ServiceResult<List<UserCardInfoDto>> result = cardService.queryUserCardInfo(phone);
            if (!result.success()) {
                return AjaxResult.failed(result.getMessage());
            }
            if (result.getData().size() > 0) {
                for (UserCardInfoDto dto : result.getData()) {
                    if (dto.getIDKFlag().equals(Constants.YES_INT)) {
                        return AjaxResult.success(dto);
                    }
                }
            }
            return AjaxResult.success();
        } catch (Exception e) {
            MyphLogger.error(e, "查询用户代扣卡信息异常,入参:{}", phone);
            return AjaxResult.failed("查询用户代扣卡信息异常");
        }
    }

    @RequestMapping("/removeBindCard")
    @ResponseBody
    public AjaxResult removeBindCard(CardParamDto dto) {
        try {
            //参数初始化
            dto.setSFromIp(ShiroUtils.getCurrentUserIp());
            ServiceResult<Integer> result = cardService.removeBindCard(dto);
            if (result.success()) {
                return AjaxResult.success();
            } else {
                return AjaxResult.failed(result.getMessage());
            }
        } catch (Exception e) {
            MyphLogger.error(e, "解绑异常,入参:{}", dto);
            return AjaxResult.failed("解绑异常");
        }
    }

    /**
     * @名称 getListAll
     * @描述 获取可用银行列表
     * @返回类型 AjaxResult
     * @日期 2017年8月31日 下午4:01:00
     * @创建人 吴阳春
     * @更新人 吴阳春
     */
    @RequestMapping("/getListAll")
    @ResponseBody
    public AjaxResult getListAll() {
        try {
            ServiceResult<List<SysPayBankDto>> result = sysPayBankService.getListAll();
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "获取可用银行列表异常");
            return AjaxResult.failed("获取可用银行列表异常");
        }
    }

    private void initBindCardParam(UserCardInfoDto dto) {
        dto.setIDFFlag(Constants.YES_INT);//是否支持快捷0表示不支持  1表示支持    默认支持
        dto.setIDKFlag(Constants.YES_INT);//是否支持代扣0表示不支持  1表示支持    默认支持
        dto.setIKJFlag(Constants.NO_INT);//是否支持代付0表示不支持  1表示支持    默认不支持
        if (StringUtils.isBlank(dto.getBankAccountCity())) {
            dto.setBankAccountCity("-");
        }
        dto.setSFromIp(ShiroUtils.getCurrentUserIp());
    }

}
