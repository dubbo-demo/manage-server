package com.myph.manage.controller.organization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.employee.constants.EmployeeMsg;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.team.service.SysTeamService;

@Controller
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EmployeeInfoService employeeInfoService;
    
    @Autowired
    private SysTeamService sysTeamService;

    /**
     * 
     * @名称 organization
     * @描述 展示初始界面
     * @返回类型 String
     * @日期 2016年9月5日 下午3:34:47
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/viewOrganization")
    public String organization(Model model) {
        try {
            return "organization/organization";
        } catch (Exception e) {
            MyphLogger.error(e, "展示组织信息异常");
            return "error/500";
        }
    }
    
    @RequestMapping("/selectOrgByOrgType")
    @ResponseBody
    public AjaxResult selectOrgByOrgType(Integer orgType) {
        try {
            ServiceResult<List<OrganizationDto>> result = organizationService.selectOrgByOrgType(orgType);
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "显示组织信息异常,入参:{}", orgType);
            return AjaxResult.failed("显示组织信息异常");
        }
    }
    
    @RequestMapping("/selectOrgByParentId")
    @ResponseBody
    public AjaxResult selectOrgByParentId(Long parentId) {
        try {
            ServiceResult<List<OrganizationDto>> result = organizationService.selectOrgByParentId(parentId);
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "显示组织信息异常,入参:{}", parentId);
            return AjaxResult.failed("显示组织信息异常");
        }
    }
    
    @RequestMapping("/selectOrgByCityId")
    @ResponseBody
    public AjaxResult selectOrgByCityId(Long cityId) {
        try {
            ServiceResult<List<OrganizationDto>> result = organizationService.selectOrgByCityId(cityId);
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "显示组织信息异常,入参:{}", cityId);
            return AjaxResult.failed("显示组织信息异常");
        }
    }

    @RequestMapping("/selectOrganizationById")
    @ResponseBody
    public AjaxResult selectOrganizationById(Long id) {
        try {
            ServiceResult<OrganizationDto> result = organizationService.selectOrganizationById(id);
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "显示组织信息异常,入参:{}", id);
            return AjaxResult.failed("显示组织信息异常");
        }
    }
    
    @RequestMapping("/selectAllOrgInfo")
    @ResponseBody
    public AjaxResult selectAllOrgInfo() {
        try {
            ServiceResult<List<OrganizationDto>> result = organizationService.selectAllOrgInfo();
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "显示组织信息异常");
            return AjaxResult.failed("显示组织信息异常");
        }
    }
    
    /**
     * 
     * @名称 changeOrganization
     * @描述 新增或修改组织信息
     * @返回类型 AjaxResult
     * @日期 2016年9月5日 下午3:35:48
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/changeOrganization")
    @ResponseBody
    public AjaxResult changeOrganization(OrganizationDto organizationDto) {
        try {

            Map<String, Object> model = new HashMap<String, Object>();
            // 门店校验地市编码 创建门店时，填写地市编码，地市编码必须在地市编码表中 0不存在，1存在
            if (StringUtils.isNotBlank(organizationDto.getCityCode())) {
                ServiceResult<Integer> checkCityCode = organizationService.checkCityCode(organizationDto.getCityCode());
                model.put("checkCityCode", checkCityCode.getData());
                if (checkCityCode.getData() == 0) {
                    return AjaxResult.success(model);
                }
            }
            if (EmployeeMsg.INSERT_TYPE.equals(organizationDto.getInsertOrUpdate())) {
                // 新增
                // 校验组织名称及编码 组织的名称与编号不能重复0、不重复， 1、名称重复，2、编号重复，3、名称编号都重复
                ServiceResult<Integer> checkNameAndCode = organizationService.checkNameAndCode(organizationDto);
                model.put("checkNameAndCode", checkNameAndCode.getData());
                if (checkNameAndCode.getData() > 0) {
                    return AjaxResult.success(model);
                }
                organizationDto.setCreateUser(ShiroUtils.getCurrentUserName());

                organizationService.addOrganization(organizationDto);
            } else {
                // 更新
                Long parentId = organizationDto.getParentId();
                ServiceResult<OrganizationDto> organizationSelect = organizationService
                        .selectOrganizationById(organizationDto.getId());
                if (!organizationSelect.getData().getParentId().equals(parentId) && parentId != 0) {
                    // 1、更新后上级组织变化：校验组织名称及编码 组织的名称与编号不能重复0、不重复， 1、名称重复，2、编号重复，3、名称编号都重复
                    ServiceResult<Integer> checkNameAndCode = organizationService.checkNameAndCode(organizationDto);
                    model.put("checkNameAndCode", checkNameAndCode.getData());
                    if (checkNameAndCode.getData() > 0) {
                        return AjaxResult.success(model);
                    }
                } else {
                    // 2、更新后上级组织未变：校验组织名称及编码 组织的名称与编号不能重复0、不重复， 1、名称重复，2、编号重复，3、名称编号都重复
                    organizationDto.setParentId(organizationSelect.getData().getParentId());
                    ServiceResult<List<Integer>> checkNameAndCode1 = organizationService
                            .checkNameAndCodeParentNoChange(organizationDto);
                    int checkCode = 0;
                    if (checkNameAndCode1.getData().get(0) > 0) {
                        checkCode += 1;
                    }
                    if (checkNameAndCode1.getData().get(1) > 0) {
                        checkCode += 2;
                    }
                    model.put("checkNameAndCode", checkCode);
                    if (checkCode > 0) {
                        return AjaxResult.success(model);
                    }
                }
                organizationService.updateOrganization(organizationDto);
            }
            model.put("checkCityCode", 1);
            return AjaxResult.success(model);
        } catch (Exception e) {
            MyphLogger.error(e, "新增或修改组织信息异常,入参:{}", organizationDto.toString());
            return AjaxResult.failed("新增或修改组织信息异常");
        }
    }

    /**
     * 
     * @名称 selectOrganizationTree
     * @描述 根据Id查到最底层的部门组织
     * @返回类型 AjaxResult
     * @日期 2016年11月8日 上午11:02:19
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/selectOrganizationTree")
    @ResponseBody
    public AjaxResult selectOrganizationTree(Long id) {
        try {
            ServiceResult<List<OrganizationDto>> selectOrganizationTreeResult = organizationService
                    .selectOrganizationTree(id);
            return AjaxResult.success(selectOrganizationTreeResult.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "根据Id查到最底层的部门组织异常,入参:{}", id);
            return AjaxResult.failed("根据Id查到最底层的部门组织异常");
        }
    }

    /**
     * 
     * @名称 selectAllOrganizationTree
     * @描述 根据ID查当前层以及所有子节点组织(父查子)
     * @返回类型 AjaxResult
     * @日期 2016年11月8日 上午11:02:19
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/selectAllOrganizationTree")
    @ResponseBody
    public AjaxResult selectAllOrganizationTree(Long id) {
        try {
            ServiceResult<List<OrganizationDto>> selectOrganizationTreeResult = organizationService
                    .selectAllOrganizationTree(id);
            return AjaxResult.success(selectOrganizationTreeResult.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "根据Id查到最底层的部门组织异常,入参:{}", id);
            return AjaxResult.failed("根据Id查到最底层的部门组织异常");
        }
    }
    
    /**
     * 
     * @名称 delOrganization
     * @描述 删除组织下所有节点
     * @返回类型 AjaxResult
     * @日期 2016年9月5日 下午3:38:44
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/delOrganization")
    @ResponseBody
    public AjaxResult delOrganization(Long id) {
        try {
            Map<String, Object> model = new HashMap<String, Object>();
            // 查询节点及所有子节点
            ServiceResult<List<OrganizationDto>> OrganizationDtoList = organizationService
                    .selectAllOrganizationTree(id);
            List<Long> listId = new ArrayList<Long>();
            for (int i = 0; i < OrganizationDtoList.getData().size(); i++) {
                listId.add(OrganizationDtoList.getData().get(i).getId());
            }
            if (listId.size() > 0) {
                // 删除前进行校验，组织下有人员信息的，不允许删除
                ServiceResult<Integer> checkResult = employeeInfoService.queryEmployeeInfoCountByOrgId(listId);
                model.put("checkResult", checkResult.getData());
                if (checkResult.getData() > 0) {
                    return AjaxResult.success(model, "组织下有人员，不能进行删除");
                }
                // 删除前进行校验，组织下有人员信息的，不允许删除
                checkResult = sysTeamService.queryTeamsCountByStoreId(listId);
                model.put("checkResult", checkResult.getData());
                if (checkResult.getData() > 0) {
                    return AjaxResult.success(model, "组织下有团队，不能进行删除");
                }
                // 删除所有子节点
                for(Long orgId:listId){
                    organizationService.delOrganization(orgId);
                }                
                return AjaxResult.success(model);
            } else {
                return AjaxResult.failed(EmployeeMsg.ORGANIZATION_DEL_FAILUE);
            }
        } catch (Exception e) {
            MyphLogger.error(e, "删除组织下所有节点异常,入参:{}", id);
            return AjaxResult.failed("删除组织下所有节点异常");
        }
    }

    /**
     * 
     * @名称 selectOrgByParentOrgType
     * @描述 获取总部下所有部门信息
     * @返回类型 AjaxResult
     * @日期 2016年9月5日 下午3:36:36
     * @创建人 徐辛沛
     * @更新人 徐辛沛
     *
     */
    @RequestMapping("/selectOrgByParentOrgType")
    @ResponseBody
    public AjaxResult selectOrgByParentOrgType(Integer orgType) {
        try {
            List<OrganizationDto> list = new ArrayList<OrganizationDto>();
            ServiceResult<List<OrganizationDto>> result = organizationService
                    .selectOrgByParentOrgType(orgType);
            list = result.getData();
            return AjaxResult.success(list);
        } catch (Exception e) {
            MyphLogger.error(e, "获取总部下所有部门信息异常,入参:{}", orgType);
            return AjaxResult.failed("获取总部下所有部门信息异常");
        }
    }
    
    
    @RequestMapping("/getRegionInfo")
    @ResponseBody
    public AjaxResult getRegionInfo() {
        try {
            List<OrganizationDto> result = ShiroUtils.getRegionInfo();
            return AjaxResult.success(result);
        } catch (Exception e) {
            MyphLogger.error("获取当前登录用户数据权限中大区信息异常",e);
            return AjaxResult.failed("获取当前登录用户数据权限中大区信息异常");
        }
    }
    
    @RequestMapping("/getStoreInfo")
    @ResponseBody
    public AjaxResult getStoreInfo(Long id) {
        try {
            List<OrganizationDto> result = ShiroUtils.getStoreInfo(id);
            return AjaxResult.success(result);
        } catch (Exception e) {
            MyphLogger.error("根据大区ID获取当前登录用户数据权限中门店信息",e);
            return AjaxResult.failed("根据大区ID获取当前登录用户数据权限中门店信息");
        }
    }
}
