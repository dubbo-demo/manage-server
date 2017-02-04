package com.myph.manage.controller.sysUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.constants.SysUserMsg;
import com.myph.employee.dto.EmployeeSysUserDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.user.dto.SysUserDto;
import com.myph.user.service.SysUserService;

@Controller
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private EmployeeInfoService employeeInfoService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 
     * @名称 queryEmployeeInfo
     * @描述 查询账户信息
     * @返回类型 String
     * @日期 2016年9月5日 下午3:44:37
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/querySysUser")
    public String queryEmployeeInfo(EmployeeSysUserDto queryDto,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, Model model,
            Integer pageSize) {
        try {
            if (null == pageSize) {
                pageSize = 10;
            }
            BasePage basePage = new BasePage(pageIndex, pageSize);

            // 查询员工基本信息
            ServiceResult<Pagination<EmployeeSysUserDto>> result = employeeInfoService.queryEmployeeSysUser(queryDto,
                    basePage);
            // 批量查询账号状态
            List<Long> employeeIdList = new ArrayList<Long>();
            for (int i = 0; i < result.getData().getResult().size(); i++) {
                employeeIdList.add(result.getData().getResult().get(i).getId());
            }
            ServiceResult<List<SysUserDto>> selectSysUserResult = sysUserService.selectSysUserByListId(employeeIdList);
            Map<Long, Integer> selectSysUserMap = new HashMap<Long, Integer>();
            if (selectSysUserResult.getData() != null && selectSysUserResult.getData().size() > 0) {
                for (SysUserDto selectSysUser : selectSysUserResult.getData()) {
                    selectSysUserMap.put(selectSysUser.getEmployeeId(), selectSysUser.getAmountState());
                }
            }
            if (result.getData() != null && result.getData().getResult() != null
                    && result.getData().getResult().size() > 0) {
                for (EmployeeSysUserDto employeeSysUserDto : result.getData().getResult()) {
                    employeeSysUserDto.setAmountState(selectSysUserMap.containsKey(employeeSysUserDto.getId())?selectSysUserMap.get(employeeSysUserDto.getId()):SysUserMsg.AMOUNT_STATE_DISABLE);
                }
            }
            Pagination<EmployeeSysUserDto> page = result.getData();

            model.addAttribute("page", page);
            model.addAttribute("queryDto", queryDto);
            return "sysUser/sysUser";
        } catch (Exception e) {
            MyphLogger.error(e, "查询账户信息异常,入参:{}", queryDto.toString());
            return "error/500";
        }
    }

    /**
     * 
     * @名称 updateSysUser
     * @描述 更新账户状态
     * @返回类型 AjaxResult
     * @日期 2016年9月5日 下午3:45:11
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/updateSysUser")
    @ResponseBody
    public AjaxResult updateSysUser(SysUserDto sysUserDto) {
        try {
            ServiceResult<Integer> result = sysUserService.updateSysUserAmountState(sysUserDto);
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "更新账户状态异常,入参:{}", sysUserDto.toString());
            return AjaxResult.failed("更新账户状态异常");
        }
    }
}
