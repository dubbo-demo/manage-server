package com.myph.manage.controller.sys;

import com.myph.base.common.BaseConstants;
import com.myph.base.dto.MenuDto;
import com.myph.base.service.MenuService;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.controller.BaseController;
import com.myph.permission.service.PermissionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private PermissionService permissionService;

    /**
     * @param model
     * @return
     * @Description:菜单列表
     */
    @RequestMapping("/menu")
    public String menu(Model model) {
        MyphLogger.info("开始菜单列表：/menu/menu.htm");
        ServiceResult<List<MenuDto>> menuResult = menuService.getTopMenu();
        List<MenuDto> menuList = menuResult.getData();
        if (CollectionUtils.isNotEmpty(menuList)) {
            model.addAttribute("menuList", menuList);
        }
        model.addAttribute("level", BaseConstants.SYS_MENU_LEVEL_0);
        MyphLogger.info("结束菜单列表：/menu/menu.htm");
        return "/menu/menu";
    }

    /**
     * @param model
     * @param id
     * @return
     * @Description:获取子菜单
     */
    @RequestMapping("/childrenMenu")
    public String childrenMenu(Model model, Long id) {
        MyphLogger.info("开始获取子菜单列表：/menu/childrenMenu.htm");
        ServiceResult<List<MenuDto>> menuResult = menuService.getChildrenMenu(id);
        List<MenuDto> menuList = menuResult.getData();
        if (CollectionUtils.isNotEmpty(menuList)) {
            model.addAttribute("menuList", menuList);
        }
        model.addAttribute("level", BaseConstants.SYS_MENU_LEVEL_1);
        model.addAttribute("parentMenuId", id);
        MyphLogger.info("结束获取子菜单列表：/menu/childrenMenu.htm");
        return "/menu/menu";
    }

    /**
     * 方法名： editUI 描述： 添加/修改菜单页面
     *
     * @return
     */
    @RequestMapping("/editUI")
    public String edit(Model model, @RequestParam(value = "menuId", required = false) Long menuId, Long parentMenuId) {
        MyphLogger.info("开始菜单编辑页面：/menu/editUI.htm|menuId=" + menuId);
        ServiceResult<List<MenuDto>> menuResult = menuService.getAllMenu();
        List<MenuDto> menuList = menuResult.getData();
        if (CollectionUtils.isNotEmpty(menuList)) {
            model.addAttribute("menuList", menuList);
        }
        if (null != menuId) {
            ServiceResult<MenuDto> menuMap = menuService.getMenuById(menuId);
            model.addAttribute("menu", menuMap.getData());
        }
        model.addAttribute("parentMenuId", parentMenuId);
        MyphLogger.info("结束菜单编辑页面：/menu/editUI.htm");
        return "/menu/edit";
    }

    /**
     * 方法名： add 描述：添加/编辑菜单
     *
     * @param role
     * @return
     */
    @RequestMapping("/edit")
    public String add(HttpServletRequest request, MenuDto menu, Long parentMenuId) {
        MyphLogger.info("开始菜单编辑保存：/menu/edit.htm|menu=" + menu.toString());
        menu.setUpdateTime(new Date());
        menu.setCreateTime(new Date());
        menu.setCreateUser(ShiroUtils.getCurrentUserName());
        menuService.edit(menu);
        MyphLogger.info("开始菜单编辑保存：/menu/edit.htm|menu=" + menu.toString());
        if (!BaseConstants.SYS_MENU_ROOT_ID.equals(parentMenuId)) {
            return redirectUrl("childrenMenu.htm?id=" + parentMenuId);

        } else {
            return redirectUrl("menu.htm");
        }

    }

    /**
     * 方法名： delete 描述： 菜单删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public AjaxResult delete(HttpServletRequest request, Long id) {
        MyphLogger.info("开始菜单删除：/menu/delete.htm|menuid=" + id);
        try {
            MenuDto menu = menuService.getMenuById(id).getData();
            // 如果是一级菜单
            if (null != menu && BaseConstants.SYS_MENU_LEVEL_0.equals(menu.getMenuLevel())) {
                ServiceResult<List<MenuDto>> menuResult = menuService.getChildrenMenu(id);
                List<MenuDto> menuList = menuResult.getData();
                if (CollectionUtils.isNotEmpty(menuList)) {
                    return AjaxResult.failed("删除失败,请先删除子菜单");
                }
            }
            menuService.delete(id);
            permissionService.deleteByMenuId(id);
            return AjaxResult.success();
        } catch (Exception e) {
            MyphLogger.error(e, "异常[菜单删除：/menu/delete.htm]|menuid=" + id);
            return AjaxResult.failed("删除失败,系统异常");
        }
    }

    /**
     * @名称 checkMenuExist
     * @描述 校验菜单存在（true:不存在校验通过;false:存在校验不通过）
     * @返回类型 boolean
     * @日期 2016年9月6日 下午3:30:10
     * @创建人 王海波
     * @更新人 王海波
     */
    @RequestMapping("/checkMenuExist")
    @ResponseBody
    public boolean checkMenuExist(String menuName) {
        MyphLogger.info("开始校验菜单：/menu/checkMenuExist.htm|menuName=" + menuName);
        // 校验菜单是否存在
        boolean exist = menuService.isMenuExist(menuName);
        MyphLogger.info("结束校验菜单：/menu/checkMenuExist.htm|menuName=" + menuName + "|exist=" + exist);
        if (exist) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * @名称 getUsedOrderColumn
     * @描述 获取下级菜单顺序
     * @返回类型 AjaxResult
     * @日期 2016年9月20日 下午7:17:33
     * @创建人 王海波
     * @更新人 王海波
     */
    @RequestMapping("/getUsedOrderColumn")
    @ResponseBody
    public AjaxResult getUsedOrderColumn(Long id) {
        MyphLogger.info("开始获取下级菜单顺序：/menu/getUsedOrderColumn.htm|menuid=" + id);
        try {
            ServiceResult<List<MenuDto>> menuResult = menuService.getChildrenMenu(id);
            List<MenuDto> menuList = menuResult.getData();
            List<Integer> orders = new ArrayList<Integer>();
            if (CollectionUtils.isNotEmpty(menuList)) {
                for (MenuDto menu : menuList) {
                    orders.add(menu.getOrderColumn());
                }
            }
            MyphLogger.info("结束校验菜单：/menu/getUsedOrderColumn.htm|orders=" + orders);
            return AjaxResult.success(orders);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[获取下级菜单顺序：/menu/getUsedOrderColumn.htm]|menuid=" + id);
            return AjaxResult.failed("获取下级菜单顺序失败,系统异常");
        }

    }
}
