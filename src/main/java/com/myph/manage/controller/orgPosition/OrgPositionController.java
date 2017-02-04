package com.myph.manage.controller.orgPosition;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.employee.constants.EmployeeMsg;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.position.dto.OrgPositionDto;
import com.myph.position.dto.PositionDto;
import com.myph.position.service.OrgPositionService;
import com.myph.position.service.PositionService;

@Controller
@RequestMapping("/orgPosition")
public class OrgPositionController {

    @Autowired
    private OrgPositionService orgPositionService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PositionService positionService;

    /**
     * 
     * @名称 selectOrgPositionList
     * @描述 根据组织ID查岗位信息
     * @返回类型 AjaxResult
     * @日期 2016年9月5日 下午3:37:04
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/selectOrgPositionList")
    @ResponseBody
    public AjaxResult selectOrgPositionList(Long id) {
        try {
            ServiceResult<List<OrgPositionDto>> result = orgPositionService.selectOrgPositionList(id);
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "根据组织ID查岗位信息异常,入参:{}", id);
            return AjaxResult.failed("根据组织ID查岗位信息异常");
        }
    }

    /**
     * 
     * @名称 selectOrgPositionInfo
     * @描述 根据组织ID查询组织及其子节点下所有岗位信息
     * @返回类型 AjaxResult
     * @日期 2016年9月5日 下午3:37:19
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/selectOrgPositionInfo")
    @ResponseBody
    public AjaxResult selectOrgPositionInfo(Long id) {
        try {
            // 查询组织最底层的部门信息
            ServiceResult<List<OrganizationDto>> organizationDtoResult = organizationService.selectOrganizationTree(id);
            List<Long> listId = new ArrayList<Long>();
            for (int i = 0; i < organizationDtoResult.getData().size(); i++) {
                listId.add(organizationDtoResult.getData().get(i).getId());
            }
            if (listId.size() > 0) {
                // 根据部门ID查询岗位信息
                ServiceResult<List<OrgPositionDto>> result = orgPositionService.selectOrgPositionInfoByListId(listId);
                return AjaxResult.success(result.getData());
            }
            return AjaxResult.success();
        } catch (Exception e) {
            MyphLogger.error(e, "根据组织ID查询组织及其子节点下所有岗位信息异常,入参:{}", id);
            return AjaxResult.failed("根据组织ID查询组织及其子节点下所有岗位信息异常");
        }
    }

    /**
     * 
     * @名称 selectDistinctPositionInfo
     * @描述 根据组织ID查询组织及其子节点下所有岗位信息
     * @返回类型 AjaxResult
     * @日期 2016年11月17日 下午3:37:19
     * @创建人 徐辛沛
     * @更新人 徐辛沛
     *
     */
    @RequestMapping("/selectDistinctPositionInfo")
    @ResponseBody
    public AjaxResult selectDistinctPositionInfo(Long id) {
        try {
            // 查询组织最底层的部门信息
            ServiceResult<List<OrganizationDto>> organizationDtoResult = organizationService.selectOrganizationTree(id);
            List<Long> listId = new ArrayList<Long>();
            for (int i = 0; i < organizationDtoResult.getData().size(); i++) {
                listId.add(organizationDtoResult.getData().get(i).getId());
            }
            Set<OrgPositionDto> data = new TreeSet<OrgPositionDto>(new Comparator<OrgPositionDto>() {
                @Override
                public int compare(OrgPositionDto dto1, OrgPositionDto dto2) {
                    return dto1.getPositionId().compareTo(dto2.getPositionId());
                }
            });
            if (listId.size() > 0) {
                // 根据部门ID查询岗位信息
                ServiceResult<List<OrgPositionDto>> result = orgPositionService.selectOrgPositionInfoByListId(listId);
                for (OrgPositionDto orgPositionDto : result.getData()) {
                    OrgPositionDto dto = new OrgPositionDto();
                    dto.setPositionId(orgPositionDto.getPositionId());
                    dto.setPositionName(orgPositionDto.getPositionName());
                    data.add(dto);
                }
                return AjaxResult.success(data);
            }
            return AjaxResult.success();
        } catch (Exception e) {
            MyphLogger.error(e, "根据组织ID查询组织及其子节点下所有岗位信息异常,入参:{}", id);
            return AjaxResult.failed("根据组织ID查询组织及其子节点下所有岗位信息异常");
        }
    }

    /**
     * 
     * @名称 selectPosition
     * @描述 查询所有岗位信息
     * @返回类型 AjaxResult
     * @日期 2016年9月5日 下午3:42:53
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/selectPosition")
    @ResponseBody
    public AjaxResult selectPosition() {
        try {
            ServiceResult<List<PositionDto>> result = positionService.selectPosition();
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "查询所有岗位信息异常");
            return AjaxResult.failed("查询所有岗位信息异常");
        }
    }

    /**
     * 
     * @名称 changePost
     * @描述 新增或更新组织岗位关系
     * @返回类型 AjaxResult
     * @日期 2016年9月5日 下午3:43:11
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/changePost")
    @ResponseBody
    public AjaxResult changePost(OrgPositionDto orgPositionDto) {
        try {
            // 插入前进行校验，根据组织+岗位进行校验，如果有数据则不进行插入操作。
            OrgPositionDto record = new OrgPositionDto();
            record.setOrgId(orgPositionDto.getOrgId());
            record.setPositionId(orgPositionDto.getPositionId());
            ServiceResult<Long> selectCountResult = orgPositionService.selectCountOrgPosition(record);
            if (0 == selectCountResult.getData()) {
                if (0 == orgPositionDto.getInsertOrUpdatePost()) {
                    // 插入
                    orgPositionDto.setCreateUser(ShiroUtils.getCurrentUserName());
                    ServiceResult<Integer> result = orgPositionService.insertSelective(orgPositionDto);
                    return AjaxResult.success(result.getData());
                } else {
                    // 更新
                    ServiceResult<Integer> result = orgPositionService.updateByPrimaryKeySelective(orgPositionDto);
                    return AjaxResult.success(result.getData());
                }

            } else {
                return AjaxResult.failed(EmployeeMsg.POSITION_INSERT_FAILUE);
            }
        } catch (Exception e) {
            MyphLogger.error(e, "新增或更新组织岗位关系异常,入参:{}", orgPositionDto.toString());
            return AjaxResult.failed("新增或更新组织岗位关系异常");
        }
    }

    /**
     * 
     * @名称 delPost
     * @描述 删除组织岗位关系
     * @返回类型 AjaxResult
     * @日期 2016年9月5日 下午3:43:45
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/delPost")
    @ResponseBody
    public AjaxResult delPost(Long id) {
        try {
            Map<String, Object> model = new HashMap<String, Object>();
            // 删除前进行校验，岗位下有人员信息的，不允许删除
            ServiceResult<Integer> checkResult = orgPositionService.queryEmployeeInfoCountByOrgPoId(id);
            model.put("checkResult", checkResult.getData());
            if (checkResult.getData() > 0) {
                return AjaxResult.success(model);
            }
            orgPositionService.deleteByPrimaryKey(id);
            return AjaxResult.success(model);
        } catch (Exception e) {
            MyphLogger.error(e, " 删除组织岗位关系异常,入参:{}", id);
            return AjaxResult.failed(" 删除组织岗位关系异常");
        }
    }
}
