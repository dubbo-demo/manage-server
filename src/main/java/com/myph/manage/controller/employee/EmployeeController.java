package com.myph.manage.controller.employee;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.PingYinUtil;
import com.myph.common.util.SensitiveInfoUtils;
import com.myph.employee.constants.EmployeeMoveTypeEnum;
import com.myph.employee.constants.EmployeeMsg;
import com.myph.employee.dto.EmployeeDetailDto;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.dto.EmployeeInputDto;
import com.myph.employee.dto.EmployeeMoveInfoDto;
import com.myph.employee.dto.EmployeeSearchInfoDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.employee.dto.EmpDetailDto;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.position.service.PositionService;
import com.myph.team.service.SysTeamService;
import com.myph.user.dto.SysUserDto;
import com.myph.user.service.SysUserService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeInfoService employeeInfoRemoteService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private PositionService positionService;
    
    @Autowired
    private SysTeamService sysTeamService;

    @RequestMapping("/queryEmployeeInfo")
    public String queryEmployeeInfo(EmployeeDetailDto queryDto, String pageIndex, Model model, Integer pageSize) {
        EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
        EmployeeInfoDto user = ShiroUtils.getCurrentUser();
        Long orgId = 0l;
        if (user.getOrgType() == EmployeeMsg.ORGANIZATION_TYPE.STORE_TYPE.toNumber()) {
            orgId = empDetail.getStoreId();
        } else if(user.getOrgType() == EmployeeMsg.ORGANIZATION_TYPE.REGION_TYPE.toNumber()) {
            orgId = empDetail.getRegionId();
        } else{
            orgId = 0l;
        }
        if (null == queryDto.getOrgId()) {
            queryDto.setOrgId(orgId);
        }
        // 默认在职
        if (null == queryDto.getIcmbFlag()){
            queryDto.setIcmbFlag(Constants.NO);
        }
        int startNum = 1;
        if (StringUtils.isNoneBlank(pageIndex)) {
            startNum = Integer.parseInt(pageIndex);
        }
        if (null == pageSize) {
            pageSize = 10;
        }
        BasePage basePage = new BasePage(startNum, pageSize);

        // 查询员工基本信息
        ServiceResult<Pagination<EmployeeDetailDto>> result = employeeInfoRemoteService.queryEmployeeInfo(queryDto,
                basePage);

        Pagination<EmployeeDetailDto> page = result.getData();
        if (null != page && null != page.getResult()) {
            List<EmployeeDetailDto> list = page.getResult();
            for (EmployeeDetailDto dto : list) {
                dto.setRelPhone(dto.getMobilePhone());
                dto.setIdentityNumber(SensitiveInfoUtils.maskIdCard(dto.getIdentityNumber()));// 隐藏身份证
                dto.setMobilePhone(SensitiveInfoUtils.maskMobilePhone(dto.getMobilePhone()));// 隐藏手机号
            }
        }

        model.addAttribute("page", page);
        model.addAttribute("orgId", orgId);
        model.addAttribute("queryDto", queryDto);
        return "employee/employeeInfo";
    }

    /**
     * 员工唯一性校验
     * 
     * @param dto
     * @return
     */
    @RequestMapping("/checkEmployeeInfo")
    @ResponseBody
    public AjaxResult checkEmployeeInfo(EmployeeDetailDto dto) {
        ServiceResult<Integer> result = employeeInfoRemoteService.checkEmployeeInfo(dto);
        return AjaxResult.success((Object) result.getData());
    }

    /**
     * 新增员工信息页面
     * 
     * @return
     */
    @RequestMapping("/addEmployeeInfo")
    public String addEmployeeInfo(EmployeeSearchInfoDto searchDto, Model model) {
        EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
        EmployeeInfoDto user = ShiroUtils.getCurrentUser();
        Long orgId = 0l;
        if (user.getOrgType() == 3) {
            orgId = empDetail.getStoreId();
        } else {
            orgId = empDetail.getRegionId();
        }
        // if (null == queryDto.getOrgId()) {
        // queryDto.setOrgId(orgId);
        // }
        model.addAttribute("orgId", orgId);
        model.addAttribute("searchDto", searchDto);
        return "employee/addEmployeeInfo";
    }

    /**
     * 新增员工信息
     * 
     * @param dto
     * @return
     */
    @RequestMapping("/saveEmployeeInfo")
    public String saveEmployeeInfo(EmployeeDetailDto dto) {
        dto.setNameSpell(PingYinUtil.getPingYin(dto.getEmployeeName()));
        employeeInfoRemoteService.saveEmployeeInfo(dto);
        return "redirect:queryEmployeeInfo.htm";
    }

    /**
     * 修改员工信息
     * 
     * @return
     */
    @RequestMapping("/editEmployeeInfo")
    public String editEmployeeInfo(String id, Model model, EmployeeSearchInfoDto searchDto) {
        EmployeeDetailDto querydto = new EmployeeDetailDto();
        BasePage basePage = new BasePage(1, 1);
        querydto.setId(Long.parseLong(id));
        // 查询员工基本信息
        ServiceResult<Pagination<EmployeeDetailDto>> result = employeeInfoRemoteService.queryEmployeeInfo(querydto,
                basePage);
        Pagination<EmployeeDetailDto> page = result.getData();
        model.addAttribute("item", page.getResult().get(0));
        model.addAttribute("searchDto", searchDto);
        return "employee/editEmployeeInfo";
    }

    /**
     * 修改员工信息
     * 
     * @param dto
     * @param start
     * @param modelMap
     * @return
     */
    @RequestMapping("/updateEmployeeInfo")
    public String updateEmployeeInfo(EmployeeDetailDto dto) {
        dto.setNameSpell(PingYinUtil.getPingYin(dto.getEmployeeName()));
        dto.setUpdateUser(ShiroUtils.getCurrentUser().getEmployeeName());
        employeeInfoRemoteService.updateEmployeeInfo(dto);
        return "redirect:queryEmployeeInfo.htm";
    }

    /**
     * 获取员工信息详细
     * 
     * @param id
     * @return
     */
    @RequestMapping("/queryEmployeeInfoDetail")
    public String queryEmployeeInfoDetail(String id, Model model, EmployeeSearchInfoDto searchDto) {
        EmployeeDetailDto querydto = new EmployeeDetailDto();
        BasePage basePage = new BasePage(1, 1);
        querydto.setId(Long.parseLong(id));
        // 查询员工基本信息
        ServiceResult<Pagination<EmployeeDetailDto>> result = employeeInfoRemoteService.queryEmployeeInfo(querydto,
                basePage);
        Pagination<EmployeeDetailDto> page = result.getData();
        model.addAttribute("item", page.getResult().get(0));
        model.addAttribute("searchDto", searchDto);
        return "employee/employeeInfoDetail";
    }

    /**
     * 员工调动记录查询
     * 
     * @param id
     * @return
     */
    @RequestMapping("/queryEmployeeMoveInfo")
    public String queryEmployeeMoveInfo(String id, Model model, EmployeeSearchInfoDto searchDto) {
        // 员工调动记录详情查询
        ServiceResult<List<EmployeeMoveInfoDto>> result = employeeInfoRemoteService.queryEmployeeMoveInfo(id);
        List<EmployeeMoveInfoDto> page = result.getData();
        model.addAttribute("page", page);
        model.addAttribute("searchDto", searchDto);
        return "employee/employeeMoveInfo";
    }

    /**
     * 员工调动管理
     * 
     * @return
     */
    @RequestMapping("/manageEmployeeMoveInfo")
    public String manageEmployeeMoveInfo(String id, Model model, EmployeeSearchInfoDto searchDto) {
        EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
        EmployeeInfoDto user = ShiroUtils.getCurrentUser();
        Long orgId = 0l;
        if (user.getOrgType() == 3) {
            orgId = empDetail.getStoreId();
        } else {
            orgId = empDetail.getRegionId();
        }
        // if (null == queryDto.getOrgId()) {
        // queryDto.setOrgId(orgId);
        // }
        model.addAttribute("orgId", orgId);
        EmployeeDetailDto querydto = new EmployeeDetailDto();
        BasePage basePage = new BasePage(1, 1);
        querydto.setId(Long.parseLong(id));
        // 查询员工基本信息
        ServiceResult<Pagination<EmployeeDetailDto>> result = employeeInfoRemoteService.queryEmployeeInfo(querydto,
                basePage);
        Pagination<EmployeeDetailDto> page = result.getData();
        model.addAttribute("item", page.getResult().get(0));
        model.addAttribute("searchDto", searchDto);
        return "employee/manageEmployeeMoveInfo";
    }

    /**
     * 新增员工调动记录
     * 
     * @return
     */
    @RequestMapping("/addEmployeeMoveInfo")
    public String addEmployeeMoveInfo(EmployeeMoveInfoDto dto) {
        employeeInfoRemoteService.addEmployeeMoveInfo(dto);
        return "redirect:queryEmployeeInfo.htm";
    }

    @RequestMapping("/addSysUser")
    public String addSysUser(EmployeeDetailDto dto) {
        SysUserDto sysUserDto = new SysUserDto();
        sysUserDto.setEmployeeId(dto.getId());
        sysUserDto.setCreateUser(ShiroUtils.getCurrentUserName());
        ServiceResult<Integer> result = sysUserService.addSysUser(sysUserDto);
        if (result.success()) {
            // 更新员工账户状态
            employeeInfoRemoteService.updateEmployeeUserFlag(dto);
        }
        return "redirect:queryEmployeeInfo.htm";
    }

    /**
     * 
     * @名称 showBMEmpoyee
     * @描述 根据姓名拼音匹配业务经理
     * @返回类型 AjaxResult
     * @日期 2016年9月26日 下午3:11:07
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    @RequestMapping("/showBMEmpoyee")
    @ResponseBody
    public AjaxResult showBMEmpoyee(String nameSpell, Long orgId) {
        Set<Long> ids = getSubOrgIds(orgId);
        List<EmployeeInputDto> employeeList = getEmplyeesByName(ids, EmployeeMsg.POSITION.BM, nameSpell);
        return AjaxResult.success(employeeList);
    }

    /**
     * 
     * @名称 showCustomerEmpoyee
     * @描述 根据姓名拼音匹配客服
     * @返回类型 AjaxResult
     * @日期 2016年9月26日 下午4:21:37
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    @RequestMapping("/showCustomerEmpoyee")
    @ResponseBody
    public AjaxResult showCustomerEmpoyee(String nameSpell, Long orgId) {
        Set<Long> ids = getSubOrgIds(orgId);
        List<EmployeeInputDto> employeeList = getEmplyeesByName(ids, EmployeeMsg.POSITION.CUSTOMERSERVICE, nameSpell);
        return AjaxResult.success(employeeList);
    }

    /**
     * 
     * @名称 showVisitEmpoyee
     * @描述 根据姓名拼音匹配外访人员
     * @返回类型 AjaxResult
     * @日期 2016年9月26日 下午4:22:03
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    @RequestMapping("/showVisitEmpoyee")
    @ResponseBody
    public AjaxResult showVisitEmpoyee(String nameSpell, Long orgId) {
        Set<Long> ids = getSubOrgIds(orgId);
        List<EmployeeInputDto> employeeList = getEmplyeesByName(ids, EmployeeMsg.POSITION.VISIT, nameSpell);
        return AjaxResult.success(employeeList);
    }

    /**
     * 
     * @名称 showVisitEmpoyee
     * @描述 根据姓名拼音匹配终审人员
     * @返回类型 AjaxResult
     * @日期 2016年9月26日 下午4:22:03
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    @RequestMapping("/showLastAuditorEmpoyee")
    @ResponseBody
    public AjaxResult showLastAuditorEmpoyee(String nameSpell, Long orgId) {
        Set<Long> ids = getSubOrgIds(orgId);
        List<EmployeeInputDto> employeeList = getEmplyeesByName(ids, EmployeeMsg.POSITION.AGINAUDITOR, nameSpell);
        return AjaxResult.success(employeeList);
    }

    /**
     * 
     * @名称 getEmplyeesByName
     * @描述 根据组织id,岗位id,模糊匹配拼音
     * @返回类型 List<EmployeeInputDto>
     * @日期 2016年9月28日 下午3:12:28
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    private List<EmployeeInputDto> getEmplyeesByName(Set<Long> ids, EmployeeMsg.POSITION position, String nameSpell) {
        List<EmployeeInputDto> employeeList = new ArrayList<EmployeeInputDto>();
        Set<Long> positionIds = positionService.getPositionIdsByPosition(position).getData();
        if (CollectionUtils.isEmpty(positionIds)) {
            return employeeList;
        }
        employeeList = employeeInfoRemoteService.queryUserInfoByNameMatch(ids, positionIds, nameSpell).getData();
        return employeeList;
    }

    /**
     * 
     * @名称 getSubOrgIds
     * @描述 查询当前组织及子节点组织
     * @返回类型 Set<Long>
     * @日期 2016年9月26日 下午5:11:15
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    private Set<Long> getSubOrgIds(Long orgId) {
        Set<Long> ids = new HashSet<Long>();
        if (null == orgId) {
            EmployeeInfoDto user = ShiroUtils.getCurrentUser();
            EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
            int orgType = user.getOrgType();
            if (null != user && null != empDetail) {
                // 总部 查询所有
                if (EmployeeMsg.ORGANIZATION_TYPE.HQ_TYPE.toNumber() == orgType) {
                    return ids;
                }
                // 大区
                else if (EmployeeMsg.ORGANIZATION_TYPE.REGION_TYPE.toNumber() == orgType) {
                    orgId = empDetail.getRegionId();
                }
                // 门店
                else {
                    orgId = empDetail.getStoreId();
                }
            }
        }

        ids.add(orgId);

        // 根据ID查当前层以及所有子节点组织
        List<OrganizationDto> orgList = organizationService.selectAllOrganizationTree(orgId).getData();
        if (CollectionUtils.isNotEmpty(orgList)) {
            for (OrganizationDto dto : orgList) {
                ids.add(dto.getId());
            }
        }
        return ids;
    }
    
    /**
     * 
     * @名称 updateUserflag 
     * @描述  更新员工启用/禁用状态
     * @返回类型 AjaxResult     
     * @日期 2017年7月3日 下午4:21:25
     * @创建人  吴阳春
     * @更新人  吴阳春
     *
     */
    @RequestMapping("/updateUserflag")
    @ResponseBody
    public AjaxResult updateUserflag(Long id,Integer userFlag) {
        EmployeeDetailDto employeeDetailDto = new EmployeeDetailDto();
        employeeDetailDto.setId(id);
        if(userFlag == Constants.NO_INT){
            employeeDetailDto.setUserFlag(Constants.YES);
        }else{
            employeeDetailDto.setUserFlag(Constants.NO);
        }
        employeeDetailDto.setUpdateUser(ShiroUtils.getCurrentUser().getEmployeeName());
        MyphLogger.info("更新员工启用/禁用状态：{}",employeeDetailDto);
        employeeInfoRemoteService.updateUserflag(employeeDetailDto);
        return AjaxResult.success();
    }

    /**
     * 
     * @名称 updateIcmbFlag 
     * @描述 更新员工离职/在职状态
     * @返回类型 AjaxResult     
     * @日期 2017年7月3日 下午4:21:35
     * @创建人  吴阳春
     * @更新人  吴阳春
     *
     */
    @RequestMapping("/updateIcmbFlag")
    @ResponseBody
    public AjaxResult updateIcmbFlag(Long id,Integer icmbFlag,String icmbTime,String remark) {
        EmployeeDetailDto employeeDetailDto = new EmployeeDetailDto();
        employeeDetailDto.setId(id);
        if(icmbFlag == Constants.NO_INT){
            employeeDetailDto.setIcmbFlag(Constants.YES);
            employeeDetailDto.setUserFlag(Constants.NO);
        }else{
            employeeDetailDto.setIcmbFlag(Constants.NO);
            employeeDetailDto.setUserFlag(Constants.YES);
        }
        employeeDetailDto.setUpdateUser(ShiroUtils.getCurrentUser().getEmployeeName());
        employeeDetailDto.setIcmbTime(icmbTime);
        employeeDetailDto.setRemark(remark);
        MyphLogger.info("更新员工离职/在职状态：{}",employeeDetailDto);
        //更新前校验是否为团队经理，团队经理不允许直接离职。
        ServiceResult<Integer> result = sysTeamService.queryCountByLeaderId(id);
        if(result.getData() > Constants.NO_INT){
            return AjaxResult.failed("先解除此员工对应团队负责人关系");
        }
        employeeInfoRemoteService.updateIcmbFlag(employeeDetailDto);
        return AjaxResult.success();
    }
    
}
