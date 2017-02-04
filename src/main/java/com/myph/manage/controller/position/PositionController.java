package com.myph.manage.controller.position;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.position.dto.PositionDto;
import com.myph.position.service.OrgPositionService;
import com.myph.position.service.PositionService;
import com.myph.role.service.SysRoleService;

@Controller
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @Autowired
    private OrgPositionService orgPositionService;

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping("/showPosition")
    public String querPosition(Model model) {
        try {
            ServiceResult<List<PositionDto>> positionList = positionService.selectPosition();
            model.addAttribute("positionList", positionList.getData());
            return "position/position";
        } catch (Exception e) {
            MyphLogger.error(e, "显示岗位异常");
            return "error/500";
        }
    }

    @RequestMapping("/selectMaxPositionId")
    @ResponseBody
    public AjaxResult selectMaxPositionId() {
        try {
            ServiceResult<Integer> maxPositionId = positionService.selectMaxPositionId();
            return AjaxResult.success(maxPositionId.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "获取岗位最大数异常");
            return AjaxResult.failed("获取岗位最大数异常");
        }
    }

    @RequestMapping("/addPosition")
    @ResponseBody
    public AjaxResult addPosition(Model model, PositionDto positionDto) {
        try {
            // 检查岗位名称是否重复
            ServiceResult<Integer> checkPositionName = positionService.checkPositionName(positionDto);
            if (checkPositionName.getData() > 0) {
                return AjaxResult.success(checkPositionName.getData());
            }
            // 新增岗位
            positionDto.setCreateUser(ShiroUtils.getCurrentUserName());
            positionService.insertSelective(positionDto);
            return AjaxResult.success(0);
        } catch (Exception e) {
            MyphLogger.error(e, "新增岗位异常,入参:{}", positionDto.toString());
            return AjaxResult.failed("新增岗位异常");
        }
    }

    @RequestMapping("/updatePosition")
    @ResponseBody
    public AjaxResult updatePosition(Model model, PositionDto positionDto) {
        try {
            // 检查岗位名称是否重复
            ServiceResult<Integer> checkPositionName = positionService.checkPositionName(positionDto);
            if (checkPositionName.getData() > 0) {
                return AjaxResult.success(checkPositionName.getData());
            }
            // 更新岗位
            positionService.updateByPrimaryKeySelective(positionDto);
            return AjaxResult.success(0);
        } catch (Exception e) {
            MyphLogger.error(e, "更新岗位异常,入参:{}", positionDto.toString());
            return AjaxResult.failed("更新岗位异常");
        }
    }

    @RequestMapping("/delPosition")
    @ResponseBody
    public AjaxResult delPosition(Model model, Long id) {
        try {
            positionService.deleteByPrimaryKey(id);
            return AjaxResult.success(0);
        } catch (Exception e) {
            MyphLogger.error(e, "删除岗位异常,入参:{}", id);
            return AjaxResult.failed("删除岗位异常");
        }
    }

    @RequestMapping("/check")
    @ResponseBody
    public AjaxResult check(Model model, Long id) {
        try {
            // 校验组织-岗位关系是否存在
            ServiceResult<Integer> checkOrgPositionByPositionId = orgPositionService.checkOrgPositionByPositionId(id);
            if (checkOrgPositionByPositionId.getData() > 0) {
                return AjaxResult.success(1);
            }
            // 校验角色-岗位关系是否存在
            ServiceResult<Integer> checkRoleByPositionId = sysRoleService.checkRoleByPositionId(id);
            if (checkRoleByPositionId.getData() > 0) {
                return AjaxResult.success(2);
            }
            return AjaxResult.success(0);
        } catch (Exception e) {
            MyphLogger.error(e, "校验岗位异常,入参:{}", id);
            return AjaxResult.failed("校验岗位异常");
        }
    }

    @RequestMapping("/getEntityByPositionId")
    @ResponseBody
    public AjaxResult selectPositionById(Model model, Long id) {
        try {
            ServiceResult<PositionDto> positionDto = positionService.getEntityByPositionId(id);
            return AjaxResult.success(positionDto.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "查询岗位异常,入参:{}", id);
            return AjaxResult.failed("查询岗位异常");
        }
    }

}
