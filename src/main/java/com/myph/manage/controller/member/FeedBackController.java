package com.myph.manage.controller.member;

import com.myph.manage.common.util.BeanUtils;
import com.myph.manage.controller.BaseController;
import com.myph.member.feedback.dto.FeedBackDto;
import com.myph.member.feedback.service.FeedBackService;
import com.way.common.rom.annotation.BasePage;
import com.way.common.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 客户信息反馈Controller
 * 
 * @author admin
 *
 */
@Controller
@RequestMapping("/member")
public class FeedBackController extends BaseController {

    @Autowired
    private FeedBackService feedBackService;

    /**
     * 客户反馈信息查询
     * 
     * @param queryDto
     * @param pageIndex
     * @param model
     * @param pageSize
     * @return
     */
    @RequestMapping("/queryFeedBack")
    public String queryFeedBack(FeedBackDto queryDto, String pageIndex, Model model, Integer pageSize) {
        int startNum = 1;
        if (StringUtils.isNoneBlank(pageIndex)) {
            startNum = Integer.parseInt(pageIndex);
        }
        if (null == pageSize) {
            pageSize = 10;
        }
        BasePage basePage = new BasePage(startNum, pageSize);

        if ((null == queryDto.getStartDate() && null != queryDto.getEndDate())
                || (null == queryDto.getEndDate() && null != queryDto.getStartDate())) {
            queryDto.setStartDate(new Date());
            queryDto.setEndDate(queryDto.getStartDate());
        }
        // 客户反馈信息查询
        ServiceResult<Pagination<FeedBackDto>> result = feedBackService.queryFeedBack(queryDto, basePage);

        Pagination<FeedBackDto> page = result.getData();

        model.addAttribute("page", page);
        model.addAttribute("queryDto", queryDto);
        return "member/feedBack";
    }

    /**
     * 客户反馈信息导出
     * 
     * @param request
     * @param response
     * @param queryDto
     */
    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, FeedBackDto queryDto) {
        MyphLogger.info("开始客户反馈信息导出：/member/export.htm");
        try {
            if ((null == queryDto.getStartDate() && null != queryDto.getEndDate())
                    || (null == queryDto.getEndDate() && null != queryDto.getStartDate())) {
                queryDto.setStartDate(new Date());
                queryDto.setEndDate(queryDto.getStartDate());
            }
            // 设置参数查询满足条件的所有数据不分页
            ServiceResult<List<FeedBackDto>> pageResult = feedBackService.queryFeedBack(queryDto);
            List<FeedBackDto> list = pageResult.getData();
            String columnNames[] = { "反馈内容", "联系方式", "反馈时间", "手机型号" };// 列名
            String keys[] = { "content", "mobilePhone", "sendDate", "phoneModel" };
            String fileName = "客户反馈信息" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
            // 获取Excel数据
            List<Map<String, Object>> excelList = getExcelMapList(list);
            // 导出Excel数据
            exportExcel(response, fileName, columnNames, keys, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[客户反馈信息导出：/member/export.htm]");
        }
        MyphLogger.info("结束客户反馈信息导出：/member/export.htm");
    }

    /**
     * 获取Excel数据
     * 
     * @param list
     * @return
     */
    public List<Map<String, Object>> getExcelMapList(List<FeedBackDto> list) {
        List<Map<String, Object>> destList = new ArrayList<Map<String, Object>>();
        if (null == list) {
            return destList;
        }
        Map<String, Object> destMap = null;
        for (FeedBackDto dto : list) {
            destMap = BeanUtils.transBeanToMap(dto);
            Date sendDate = dto.getSendDate();
            if (null != sendDate) {
                SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_PATTERN);
                destMap.put("sendDate", sdf.format(sendDate));
            }
            destList.add(destMap);
        }
        return destList;
    }
}
