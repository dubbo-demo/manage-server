package com.myph.manage.controller.sms.template;

import com.myph.common.result.AjaxResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.manage.controller.BaseController;
import com.myph.sms.template.dto.SmsTemplate;
import com.myph.sms.template.dto.SmsTemplateQuery;
import com.myph.sms.template.service.SmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 短信模板相关内容
 *
 * @auther Gong.Xiaozhi
 * @since 2017/8/17
 */
@RequestMapping("/sms-templcate")
@Controller
public class SmsTemplateController extends BaseController {

    @Autowired
    private SmsTemplateService smsTemplateService;

    /**
     * 短信模板内容列表
     *
     * @param query
     * @param basePage
     * @return
     */
    @RequestMapping("/list")
    public String listSmsTemplates(SmsTemplateQuery query, BasePage basePage) {
        return "";
    }

    /**
     * 更新某个短信模板
     *
     * @param smsTemplate
     * @return
     */
    @RequestMapping("/update")
    public AjaxResult updateSmsTemplate(SmsTemplate smsTemplate) {
        if (smsTemplate == null) {
            return AjaxResult.failed("入参为空");
        }
        return null;
    }

    /**
     * 删除某个短信模板
     *
     * @param tplId
     * @return
     */
    @RequestMapping("/delete/{tplId}")
    public AjaxResult deleteByTplId(@PathVariable("tplId") Long tplId) {
        if (tplId == null) {
            return AjaxResult.failed("删除短信模板失败，模板id为null");
        }
        return null;
    }
}
