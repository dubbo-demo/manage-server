package com.myph.manage.controller.activity;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.myph.activity.dto.ActivityDto;
import com.myph.activity.service.ActivityService;
import com.myph.common.hbase.HbaseUtils;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.manage.common.shiro.ShiroUtils;

/**
 * 活动信息管理Controller
 * 
 * @author admin
 *
 */
@Controller
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/addActivity.htm")
    public String addActivity(Model model, ActivityDto queryDto, String pageIndex, Integer pageSize) {
        model.addAttribute("queryDto", queryDto);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", pageSize);
        return "/activity/addActivity";
    }

    /**
     * 活动信息唯一性校验
     * 
     * @param dto
     * @return
     */
    @RequestMapping("/checkActivityInfo")
    @ResponseBody
    public AjaxResult checkEmployeeInfo(ActivityDto dto) {
        ServiceResult<Integer> result = activityService.checkEmployeeInfo(dto);
        return AjaxResult.success((Object) result.getData());
    }

    /**
     * 新建活动信息
     * 
     * @param activityInfo
     * @return
     * @throws UnsupportedEncodingException
     * @Description:新增活动 修改by wanghb20160525
     */
    @RequestMapping(value = "/saveActivity.htm")
    public String saveActivity(ActivityDto dto, @RequestParam(value = "file", required = false) MultipartFile file) {

        dto.setCreatePerson(ShiroUtils.getCurrentUser().getEmployeeName());
        // UUID作为imageId
        String imageId = UUID.randomUUID().toString().replaceAll("-", "");
        dto.setImageId(imageId);
        dto.setImageName(file.getOriginalFilename());
        try {
            // 新建活动信息
            activityService.saveActivity(dto);
            // 将文件存入大数据
            HbaseUtils.put(imageId, file.getBytes());
        } catch (Exception e) {
            MyphLogger.error("新建活动信息失败");
        }
        return "redirect:activityQuery.htm";
    }

    /**
     * 活动信息
     * 
     * @param params
     * @param pageNumber
     * @return
     */
    @RequestMapping(value = "/activityQuery.htm")
    public String activityQuery(ActivityDto queryDto, String pageIndex, Model model, Integer pageSize) {
        int startNum = 1;
        if (StringUtils.isNoneBlank(pageIndex)) {
            startNum = Integer.parseInt(pageIndex);
        }
        if (null == pageSize) {
            pageSize = 10;
        }
        BasePage basePage = new BasePage(startNum, pageSize);

        // 查询活动信息
        ServiceResult<Pagination<ActivityDto>> result = activityService.activityQuery(queryDto, basePage);

        Pagination<ActivityDto> page = result.getData();

        model.addAttribute("page", page);
        model.addAttribute("queryDto", queryDto);
        return "/activity/activityManage";
    }

    /**
     * 删除活动信息
     * 
     * @return
     */
    @RequestMapping("/deleteActivity")
    @ResponseBody
    public AjaxResult deleteActivity(String id) {
        try {
            // 删除活动信息
            activityService.deleteActivity(id);
            return AjaxResult.success();
        } catch (Exception e) {
            MyphLogger.error("删除活动信息失败");
            return AjaxResult.failed("删除活动信息失败");
        }
    }

    /**
     * 展示活动详情
     * 
     * @return
     */
    @RequestMapping("/showActivityDetial")
    public String showActivityDetial(Model model, ActivityDto queryDto, String pageIndex, Integer pageSize) {
        BasePage basePage = new BasePage(1, 1);
        // 展示活动详情
        ServiceResult<Pagination<ActivityDto>> result = activityService.showActivityDetial(queryDto, basePage);
        Pagination<ActivityDto> page = result.getData();
        model.addAttribute("item", page.getResult().get(0));
        model.addAttribute("queryDto", queryDto);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", pageSize);
        if (1 == queryDto.getActivityType()) {
            return "/activity/activityDetialPublicity";
        }
        return null;
    }

}
