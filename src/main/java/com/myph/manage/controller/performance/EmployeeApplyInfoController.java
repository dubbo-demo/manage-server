package com.myph.manage.controller.performance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.performance.dto.AuditorQueryDto;
import com.myph.performance.dto.EmployeeApplyContractDto;
import com.myph.performance.service.EmployeeApplyContractService;

/**
 * 
 * @ClassName: EmployeeApplyInfoController
 * @Description: 员工申请件信息
 * @author 王海波
 * @date 2016年10月24日 上午9:29:47
 *
 */
@Controller
@RequestMapping("/performance/employee")
public class EmployeeApplyInfoController {

    @Autowired
    private EmployeeApplyContractService employeeApplyContractService;
    @Autowired
    private EmployeeInfoService employeeInfoService;

    /**
     * 
     * @名称 list
     * @描述 查询角色人员对应合同列表（employeePossiton[客服：customerService,初审专员：firstAuditor,终审专员 reviewAuditor]）
     * @返回类型 String
     * @日期 2016年10月24日 下午3:04:36
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    @RequestMapping("/applylist")
    public String list(Model model, AuditorQueryDto queryDto, BasePage basePage) {
        Long employeeId = queryDto.getEmployeeId();
        EmployeeInfoDto emp = employeeInfoService.getEntityById(employeeId).getData();
        Pagination<EmployeeApplyContractDto> page = employeeApplyContractService.listPageInfos(queryDto, basePage)
                .getData();
        model.addAttribute("emp", emp);
        model.addAttribute("page", page);
        model.addAttribute("queryDto", queryDto);
        return "/performance/employee/employee_applylist";
    }
}
