/**   
 * @Title: ProductController.java 
 * @Package: com.myph.manage.controller.product 
 * @company: 麦芽金服
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 罗荣   
 * @date 2016年9月21日 下午2:07:47 
 * @version V1.0   
 */
package com.myph.manage.controller.log;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.constant.Constants;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.DateUtils;
import com.myph.log.dto.OperatorLogDto;
import com.myph.log.service.LogService;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.product.dto.ProductDto;
import com.myph.product.service.ProductService;

/**
 * 
 * @ClassName: LogController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 罗荣
 * @date 2016年10月19日 下午4:48:50
 *
 */
@Controller
@RequestMapping("/log")
public class LogController {
    @Autowired
    LogService logService;

    @RequestMapping("/queryPageList")
    public String queryPageList(String queryUserName, BasePage page, Model model,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        if (null != endDate) {
            // 包含结束时间，因为不算时分秒 所以加1天
            endDate = (DateUtils.addDays(endDate, 1));
        }
        ServiceResult<Pagination<OperatorLogDto>> result = logService.queryPageList(queryUserName, startDate, endDate,
                page.getPageSize(), page.getPageIndex());
        model.addAttribute("page", result.getData());
        model.addAttribute("startDate", startDate);
        if (null != endDate) {
            model.addAttribute("endDate", (DateUtils.addDays(endDate, -1)));
        }
        model.addAttribute("queryUserName", queryUserName);
        return "/log/list";
    }
}
