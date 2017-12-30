package com.myph.manage.controller.log;

import com.way.common.rom.annotation.BasePage;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * 
 * @ClassName: LogController
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
@Controller
@RequestMapping("/log")
public class LogController {
//    @Autowired
//    LogService logService;

    @RequestMapping("/queryPageList")
    public String queryPageList(String queryUserName, BasePage page, Model model,
                                @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
//        if (null != endDate) {
//            // 包含结束时间，因为不算时分秒 所以加1天
//            endDate = (DateUtils.addDays(endDate, 1));
//        }
//        ServiceResult<Pagination<OperatorLogDto>> result = logService.queryPageList(queryUserName, startDate, endDate,
//                page.getPageSize(), page.getPageIndex());
//        model.addAttribute("page", result.getData());
//        model.addAttribute("startDate", startDate);
//        if (null != endDate) {
//            model.addAttribute("endDate", (DateUtils.addDays(endDate, -1)));
//        }
//        model.addAttribute("queryUserName", queryUserName);
        return "/log/list";
    }
}
