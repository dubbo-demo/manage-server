package com.myph.manage.controller.permissiion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.base.dto.MenuDto;
import com.myph.base.service.MenuService;
import com.myph.common.result.ServiceResult;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.permission.dto.PermissionDto;
import com.myph.permission.service.PermissionService;

@Controller
@RequestMapping("/permission")
public class PermissionController {
    public final static String PATH = "/permission";
    public final static String ERROR = "error";
    @Autowired
    PermissionService permissionService;
    @Autowired
    MenuService menuService;

    @RequestMapping("/list")
    public String queryPageList(@RequestParam("menuId") Long menuId, Model model) {
        ServiceResult<List<PermissionDto>> list = permissionService.getPermissionByMenuId(menuId);
        ServiceResult<MenuDto> menuResult = menuService.getMenuById(menuId);
        if (ServiceResult.SUCCESS_CODE != menuResult.getCode()) {
            model.addAttribute("serviceResult", menuResult);
            return ERROR;
        }
        if (ServiceResult.SUCCESS_CODE != list.getCode()) {
            model.addAttribute("serviceResult", list);
            return ERROR;
        } else {
            model.addAttribute("contents", list.getData());
            model.addAttribute("menuDto", menuResult.getData());
        }
        return PATH + "/list";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ServiceResult<String> delete(@RequestParam("permissionId") Long permissionId, Model model) {
        ServiceResult<String> result = permissionService.delete(permissionId);
        return result;
    }

    @RequestMapping("/getRecordById")
    @ResponseBody
    public ServiceResult<PermissionDto> getRecordById(@RequestParam("permissionId") Long permissionId, Model model) {
        ServiceResult<PermissionDto> result = permissionService.get(permissionId);
        return result;
    }

    @RequestMapping("/update")
    @ResponseBody
    public ServiceResult<String> update(@RequestBody PermissionDto record, Model model) {
        record.setCreateUser(ShiroUtils.getCurrentUserName());
        ServiceResult<String> result = permissionService.edit(record);
        return result;
    }

    @RequestMapping("/save")
    @ResponseBody
    public ServiceResult<Long> save(@RequestBody PermissionDto record, Model model) {
        record.setCreateUser(ShiroUtils.getCurrentUserName());
        ServiceResult<Long> result = permissionService.insert(record);
        return result;
    }
}
