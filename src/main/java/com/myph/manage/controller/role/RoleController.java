package com.myph.manage.controller.role;

import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.param.NewRoleParam;
import com.way.base.menu.dto.MenuDto;
import com.way.base.menu.service.MenuService;
import com.way.base.role.dto.AuditPositionRoleDto;
import com.way.base.role.dto.RolePermissionSimpleTreeDto;
import com.way.base.role.dto.RolePermissionTreeDto;
import com.way.base.role.dto.SysRoleDto;
import com.way.base.role.service.SysRoleService;
import com.way.common.constant.Constants;
import com.way.common.log.WayLogger;
import com.way.common.result.AjaxResult;
import com.way.common.result.ServiceResult;
import com.way.common.rom.annotation.BasePage;
import com.way.common.rom.annotation.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {
    public final static String PATH = "/role";
    public final static String ERROR = "error";
    
    @Autowired
    private SysRoleService roleService;
    
    @Autowired
    private MenuService menuService;

    @RequestMapping("/queryPageList")
    public String queryPageList(SysRoleDto queryDto, BasePage page, Model model) {
        ServiceResult<Pagination<SysRoleDto>> list = roleService.queryPageList(queryDto,page.getPageIndex(), page.getPageSize());
        model.addAttribute("result", list.getData());
        model.addAttribute("page", list.getData());
        model.addAttribute("queryDto", queryDto);
        return PATH + "/list";
    }

    @RequestMapping("/getTemplate")
    public String getTemplate(Long id, Model model) {
        if (null != id) {
            ServiceResult<SysRoleDto> result = roleService.getRole(id);
            model.addAttribute("record", result.getData());
        }
        return PATH + "/new_edit";
    }

    @RequestMapping("/save")
    @ResponseBody
    public ServiceResult<Long> save(@RequestBody NewRoleParam param, Model model) {
        SysRoleDto dto = new SysRoleDto();
        dto.setRoleCode(param.getRoleCode());
        dto.setRoleName(param.getRoleName());
        dto.setRoleType(param.getRoleType());
        dto.setCreateUser(ShiroUtils.getCurrentUserName());
        ServiceResult<Long> result = roleService.save(dto);
        Long id = result.getData();
        WayLogger.access("保存角色成功！【" + id + "】");
        return result;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ServiceResult<String> delete(Long id, Model model) {
        ServiceResult<String> result = roleService.delete(id);
        return result;
    }

    @RequestMapping("/update")
    @ResponseBody
    public ServiceResult<Integer> update(@RequestBody NewRoleParam param, Model model) {
        SysRoleDto dto = new SysRoleDto();
        dto.setRoleName(param.getRoleName());
        dto.setId(param.getId());
        ServiceResult<Integer> result = roleService.update(dto);

        WayLogger.access("更新角色成功！【" + dto.getId() + "】");
        return result;
    }

    @RequestMapping("/updateStatus")
    @ResponseBody
    public ServiceResult<String> updateStatus(@RequestBody NewRoleParam param, Model model) {
        WayLogger.info("updateStatus-param:" + param.toString());

        ServiceResult<String> result = roleService.updateStatus(param.getId(), param.getStatus());

        WayLogger.access("更新角色与岗位成功！【" + result.toString() + "】");
        return result;
    }

    @RequestMapping("/saveRolePermission")
    @ResponseBody
    public ServiceResult<Object> saveRolePermission(@RequestBody NewRoleParam param, Model model) {
        WayLogger.info("saveRolePermission-param:" + param.toString());
        //设置当时登录用户
        for (RolePermissionSimpleTreeDto e : param.getSaves()) {
            e.setCreateUser(ShiroUtils.getCurrentUserName());
        }
        //设置当时登录用户
        for (RolePermissionSimpleTreeDto e : param.getRemoves()) {
            e.setCreateUser(ShiroUtils.getCurrentUserName());
        }
        ServiceResult<Object> result = roleService.insertListSelective(param.getSaves(), param.getRemoves());

        WayLogger.access("更新角色与权限成功！【" + result.toString() + "】");
        return result;
    }

    @RequestMapping("/isExistRoleName")
    @ResponseBody
    public Boolean isExistRoleName(String roleName,String roleOldName, Model model) {
        WayLogger.info("isExistRoleName-param:" + roleName);

        ServiceResult<Boolean> result = roleService.isExistRoleName(roleName,roleOldName);

        WayLogger.access("角色是否存在RoleName【" + result.toString() + "】");
        return result.getData();
    }

    @RequestMapping("/isExistRoleCode")
    @ResponseBody
    public Boolean saveRolePermission(String roleCode,String roleOldCode, Model model) {
        WayLogger.info("isExistRoleCode-param:" + roleCode);

        ServiceResult<Boolean> result = roleService.isExistRoleCode(roleCode,roleOldCode);

        WayLogger.access("角色是否存在RoleName【" + result.toString() + "】");
        return result.getData();
    }

    @RequestMapping("/getPermissionTree")
    @ResponseBody
    public ServiceResult<List<RolePermissionTreeDto>> getPermissionTree(Long roleId, Model model) {
        ServiceResult<List<MenuDto>> topMenu = menuService.getTopMenu();
        List<RolePermissionTreeDto> tree = new ArrayList<>();
        ServiceResult<List<RolePermissionTreeDto>> result = null;

        for (MenuDto e : topMenu.getData()) {
            // 一级菜单
            RolePermissionTreeDto parent = new RolePermissionTreeDto();
            parent.setId(e.getId());
            parent.setName(e.getMenuName());
            ServiceResult<List<MenuDto>> childMenu = menuService.getChildrenMenu(e.getId());
            List<RolePermissionTreeDto> childTrees = new ArrayList<>();
            parent.setChildren(childTrees);
            parent.setIsSelected(1);
            tree.add(parent);

            for (MenuDto child : childMenu.getData()) {
                // 二级菜单
                RolePermissionTreeDto childTree = new RolePermissionTreeDto();
                childTree.setId(child.getId());
                childTree.setName(child.getMenuName());
                childTree.setIsSelected(1);
                childTrees.add(childTree);
                result = roleService.getPermissionTree(roleId, child.getId());
                List<RolePermissionTreeDto> permissions = result.getData();
                childTree.setChildren(permissions);

                for (RolePermissionTreeDto rolePermissionTreeDto : permissions) {
                    // 权限
                    if (rolePermissionTreeDto.getIsSelected().equals(0)) {
                        childTree.setIsSelected(0);
                        parent.setIsSelected(0);
                        break;
                    }
                }
            }
        }

        return ServiceResult.newSuccess(tree);
    }
    
    @RequestMapping("/selectRolesByType")
    @ResponseBody
    public AjaxResult selectRolesByType(Integer roleType) {
        ServiceResult<List<SysRoleDto>> result = roleService.selectRolesByType(roleType);
        return AjaxResult.success(result.getData());
    }
    
    @RequestMapping("/selectRolesByTypeAndPositionId")
    @ResponseBody
    public AjaxResult selectRolesByTypeAndPositionId(Integer roleType,Long positionId) {
        ServiceResult<List<SysRoleDto>> sysRoles = roleService.selectRolesByType(roleType);
        ServiceResult<List<Long>> menuRoleIds = roleService.selectRoleIds(positionId, Constants.NO_INT);
        ServiceResult<List<Long>> dataRoleIds = roleService.selectRoleIds(positionId,Constants.YES_INT);
        AuditPositionRoleDto result = new AuditPositionRoleDto();
        result.setRoles(sysRoles.getData());
        result.setMenuRoleIds(menuRoleIds.getData());
        result.setDataRoleIds(dataRoleIds.getData());
        return AjaxResult.success(result);
    }

}
