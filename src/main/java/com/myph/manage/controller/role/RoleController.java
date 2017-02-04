package com.myph.manage.controller.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.base.dto.MenuDto;
import com.myph.base.service.MenuService;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.param.NewRoleParam;
import com.myph.position.dto.PositionDto;
import com.myph.position.service.OrgPositionService;
import com.myph.position.service.PositionService;
import com.myph.role.dto.RolePermissionSimpleTreeDto;
import com.myph.role.dto.RolePermissionTreeDto;
import com.myph.role.dto.RolePositionDto;
import com.myph.role.dto.SysRoleDto;
import com.myph.role.service.SysRoleService;

@Controller
@RequestMapping("/role")
public class RoleController {
    public final static String PATH = "/role";
    public final static String ERROR = "error";
    
    @Autowired
    SysRoleService roleService;
    
    @Autowired
    MenuService menuService;

    @Autowired
    OrgPositionService orgPositionService;
    
    @Autowired
    PositionService positionService;

    @RequestMapping("/queryPageList")
    public String queryPageList(BasePage page, Model model) {
        ServiceResult<Pagination<SysRoleDto>> list = roleService.queryPageList(page.getPageIndex(), page.getPageSize());
        model.addAttribute("result", list.getData());
        ServiceResult<List<PositionDto>> positions = positionService.selectPosition();

        model.addAttribute("positions", positions.getData());
        model.addAttribute("page", list.getData());
        return PATH + "/list";
    }

    @RequestMapping("/getTemplate")
    public String getTemplate(Long id, Model model) {
        ServiceResult<List<PositionDto>> positions = positionService.selectPosition();
        model.addAttribute("positions", positions.getData());
        if (null != id) {
            ServiceResult<SysRoleDto> result = roleService.getRole(id);
            model.addAttribute("record", result.getData());
            ServiceResult<List<Map<String, Object>>> positionIds = roleService.getRoleSelectedPosition(id);
            model.addAttribute("positions", positionIds.getData());
        }
        return PATH + "/new_edit";
    }

    @RequestMapping("/save")
    @ResponseBody
    public ServiceResult<Long> save(@RequestBody NewRoleParam param, Model model) {
        SysRoleDto dto = new SysRoleDto();
        dto.setRoleCode(param.getRoleCode());
        dto.setRoleName(param.getRoleName());
        dto.setCreateUser(ShiroUtils.getCurrentUserName());
        ServiceResult<Long> result = roleService.save(dto);
        Long id = result.getData();
        MyphLogger.access("保存角色成功！【" + id + "】");
        RolePositionDto rolePositionDto = new RolePositionDto();
        rolePositionDto.setPositionIds(param.getPositionIds());
        rolePositionDto.setRoleId(id);
        rolePositionDto.setCreateUser(ShiroUtils.getCurrentUserName());
        roleService.save(rolePositionDto);
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
        dto.setRoleCode(param.getRoleCode());
        dto.setRoleName(param.getRoleName());
        dto.setId(param.getId());
        ServiceResult<Integer> result = roleService.update(dto);

        MyphLogger.access("更新角色成功！【" + dto.getId() + "】");
        RolePositionDto rolePositionDto = new RolePositionDto();
        rolePositionDto.setPositionIds(param.getPositionIds());
        rolePositionDto.setRoleId(dto.getId());
        rolePositionDto.setCreateUser(ShiroUtils.getCurrentUserName());
        roleService.save(rolePositionDto);
        MyphLogger.access("更新角色与岗位成功！【" + dto.getId() + "】");
        return result;
    }

    @RequestMapping("/updateStatus")
    @ResponseBody
    public ServiceResult<String> updateStatus(@RequestBody NewRoleParam param, Model model) {
        MyphLogger.info("updateStatus-param:" + param.toString());

        ServiceResult<String> result = roleService.updateStatus(param.getId(), param.getStatus());

        MyphLogger.access("更新角色与岗位成功！【" + result.toString() + "】");
        return result;
    }

    @RequestMapping("/saveRolePermission")
    @ResponseBody
    public ServiceResult<Object> saveRolePermission(@RequestBody NewRoleParam param, Model model) {
        MyphLogger.info("saveRolePermission-param:" + param.toString());
        //设置当时登录用户
        for (RolePermissionSimpleTreeDto e : param.getSaves()) {
            e.setCreateUser(ShiroUtils.getCurrentUserName());
        }
        //设置当时登录用户
        for (RolePermissionSimpleTreeDto e : param.getRemoves()) {
            e.setCreateUser(ShiroUtils.getCurrentUserName());
        }
        ServiceResult<Object> result = roleService.insertListSelective(param.getSaves(), param.getRemoves());

        MyphLogger.access("更新角色与权限成功！【" + result.toString() + "】");
        return result;
    }

    @RequestMapping("/isExistRoleName")
    @ResponseBody
    public Boolean isExistRoleName(String roleName,String roleOldName, Model model) {
        MyphLogger.info("isExistRoleName-param:" + roleName);

        ServiceResult<Boolean> result = roleService.isExistRoleName(roleName,roleOldName);

        MyphLogger.access("角色是否存在RoleName【" + result.toString() + "】");
        return result.getData();
    }

    @RequestMapping("/isExistRoleCode")
    @ResponseBody
    public Boolean saveRolePermission(String roleCode,String roleOldCode, Model model) {
        MyphLogger.info("isExistRoleCode-param:" + roleCode);

        ServiceResult<Boolean> result = roleService.isExistRoleCode(roleCode,roleOldCode);

        MyphLogger.access("角色是否存在RoleName【" + result.toString() + "】");
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

}
