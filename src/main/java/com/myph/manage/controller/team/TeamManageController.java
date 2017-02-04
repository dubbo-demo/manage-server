package com.myph.manage.controller.team;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.employee.dto.EmployeeDetailDto;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.node.dto.SysNodeDto;
import com.myph.node.service.NodeService;
import com.myph.team.dto.SysTeamDto;
import com.myph.team.service.SysTeamService;

/**
 * 团队管理
 * 
 * @author dell
 *
 */
@Controller
@RequestMapping("/team")
public class TeamManageController {

    @Autowired
    private SysTeamService sysTeamService;

    @Autowired
    private EmployeeInfoService employeeInfoService;

    @Autowired
    private NodeService nodeService;

    /**
     * 团队列表
     * 
     * @param model
     * @param sysTeamDto
     * @param basePage
     * @return
     */
    @RequestMapping("/list")
    public String list(Model model, SysTeamDto queryDto, BasePage basePage) {
        int accountType = ShiroUtils.getAccountType();// 1：管理员 0：普通
        if (Constants.YES_INT != accountType) {// 管理员
            EmployeeInfoDto user = ShiroUtils.getCurrentUser();
            if (null != user) {
                EmployeeDetailDto userInfo = employeeInfoService.queryEmployeeInfo(user.getId()).getData();
                if (null != userInfo) {
                    Long userStoreId = userInfo.getStoreId();
                    if (userStoreId != null) {
                        queryDto.setStoreId(userStoreId);
                    } else {
                        queryDto.setStoreId(userInfo.getOrgId());
                    }
                }
            }
        }

        ServiceResult<Pagination<SysTeamDto>> teamResult = sysTeamService.listTeams(queryDto, basePage);
        ServiceResult<List<SysNodeDto>> records = nodeService.getListByParent("TEAMPOSITION");
        if (!CollectionUtils.isEmpty(records.getData())) {
            model.addAttribute("orgNodeTeam", records.getData().get(0));
        } else {
            model.addAttribute("orgNodeTeam", new SysNodeDto());
        }
        model.addAttribute("isManage", accountType == Constants.YES_INT ? "true" : "false");
        model.addAttribute("page", teamResult.getData());
        model.addAttribute("queryDto", queryDto);
        MyphLogger.info("团队管理列表分页查询", teamResult.getData());
        return "/team_manage/team_list";
    }

    /**
     * 新增团队跳转
     * 
     * @return
     */
    @RequestMapping("/addTeamForm")
    public String addTeamForm(Model model) {
        String operatorName = ShiroUtils.getCurrentUserName();
        Long operatorId = ShiroUtils.getCurrentUserId();
        MyphLogger.info("团队管理新增团队,当前操作人:{},操作人编号:{}", operatorName, operatorId);
        int accountType = ShiroUtils.getAccountType();// 1：管理员 0：普通
        if (Constants.YES_INT != accountType) {// 管理员
            EmployeeInfoDto user = ShiroUtils.getCurrentUser();
            if (null != user) {
                EmployeeDetailDto userInfo = employeeInfoService.queryEmployeeInfo(user.getId()).getData();
                if (null != userInfo) {
                    Long userStoreId = userInfo.getStoreId();
                    if (userStoreId != null) {
                        model.addAttribute("storeId", userStoreId);
                    }
                }
            }
        }
        model.addAttribute("isManage", accountType == Constants.YES_INT ? "true" : "false");
        return "/team_manage/team_add";
    }

    /**
     * 
     * @名称 addGroupForm
     * @描述 新增征信团队
     * @返回类型 String
     * @日期 2016年12月14日 下午3:03:56
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    @RequestMapping("/addGroupForm")
    public String addGroupForm(Model model) {
        String operatorName = ShiroUtils.getCurrentUserName();
        Long operatorId = ShiroUtils.getCurrentUserId();
        MyphLogger.info("新增征信团队,当前操作人:{},操作人编号:{}", operatorName, operatorId);
        return "/team_manage/group";
    }

    /**
     * 新增团队信息
     * 
     * @return
     */
    @RequestMapping("/addTeam")
    @ResponseBody
    public AjaxResult addTeam(HttpServletRequest request, SysTeamDto sysTeamDto) {
        try {
            sysTeamDto.setUpdateTime(new Date());
            sysTeamDto.setCreateTime(new Date());
            sysTeamDto.setCreateUser(ShiroUtils.getCurrentUserName());
            ServiceResult<Integer> data = sysTeamService.addTeam(sysTeamDto);
            return AjaxResult.formatFromServiceResult(data);
        } catch (Exception e) {
            MyphLogger.error(e, "新增团队信息异常,sysTeamDto:{}", sysTeamDto.toString());
            return AjaxResult.failed("新增团队信息异常");
        }
    }

    /**
     * @名称 queryCountByTeamName
     * @描述 检查是否相同团队名
     * @返回类型 boolean
     * @日期 2016年9月8日 下午6:40:49
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    @RequestMapping("/queryCountByTeamName")
    @ResponseBody
    public AjaxResult queryCountByTeamName(String teamName) {
        try {
            ServiceResult<SysTeamDto> data = sysTeamService.queryCountByTeamName(teamName);
            return AjaxResult.formatFromServiceResult(data);
        } catch (Exception e) {
            MyphLogger.error(e, "检查是否相同团队名,teamName:{}", teamName);
            return AjaxResult.failed("检查是否相同团队名");
        }
    }

    /**
     * 修改团队跳转
     * 
     * @return
     */
    @RequestMapping("/updateTeamForm")
    public String updateForm(Model model, String id) {
        model.addAttribute("id", id);
        ServiceResult<List<SysNodeDto>> records = nodeService.getListByParent("TEAMPOSITION");
        if (!CollectionUtils.isEmpty(records.getData())) {
            model.addAttribute("orgNodeTeam", records.getData().get(0));
        } else {
            model.addAttribute("orgNodeTeam", new SysNodeDto());
        }
        return "/team_manage/team_update";
    }

    /**
     * 修改团队信息
     * 
     * @return
     */
    @RequestMapping("/updateTeam")
    @ResponseBody
    public AjaxResult updateTeam(SysTeamDto sysTeamDto) {
        try {
            ServiceResult<Integer> data = sysTeamService.updateTeam(sysTeamDto);
            return AjaxResult.formatFromServiceResult(data);
        } catch (Exception e) {
            MyphLogger.error(e, "修改团队信息异常,ID:{}", sysTeamDto.toString());
            return AjaxResult.failed("修改团队信息异常");
        }
    }

    /**
     * 删除团队
     * 
     * @return
     */
    @RequestMapping("/deleteTeam")
    @ResponseBody
    public AjaxResult deleteTeam(String id) {
        try {
            ServiceResult<Integer> data = sysTeamService.deleteTeam(Long.valueOf(id));
            return AjaxResult.formatFromServiceResult(data);
        } catch (Exception e) {
            MyphLogger.error(e, "删除团队信息异常,ID:{}", id);
            return AjaxResult.failed("删除团队信息异常");
        }
    }

    /**
     * 根据团队ID加载团队信息服务
     * 
     * @param storeId
     * @return
     */
    @RequestMapping("/teamInfoQueryById/{id}")
    @ResponseBody
    public AjaxResult teamInfoQueryById(@PathVariable String id) {
        try {
            ServiceResult<SysTeamDto> data = sysTeamService.teamInfoQueryById(id);
            return AjaxResult.success(data.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "加载对应id的团队信息异常,ID:{}", id);
            return AjaxResult.failed("加载id的团队信息异常");
        }
    }

    /**
     * 根据门店ID加载团队信息服务
     * 
     * @param storeId
     * @return
     */
    @RequestMapping("/initTeamName/{storeId}")
    @ResponseBody
    public AjaxResult initTeamName(@PathVariable Long storeId) {
        try {
            ServiceResult<List<SysTeamDto>> data = sysTeamService.listTeamsByStoreId(storeId);
            return AjaxResult.success(data.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "加载对应门店的团队信息异常,门店ID:{storeId}", storeId);
            return AjaxResult.failed("加载对应门店的团队信息异常");
        }
    }
}
