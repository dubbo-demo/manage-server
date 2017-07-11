package com.myph.manage.controller.sysRoleCondition;

import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.constant.NodeConstant;
import com.myph.constant.RoleConditionEnum;
import com.myph.employee.constants.EmployeeMsg;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.node.dto.SysNodeDto;
import com.myph.node.service.NodeService;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.product.dto.ProductDto;
import com.myph.product.service.ProductService;
import com.myph.roleCondition.dto.SysRoleConditionDto;
import com.myph.roleCondition.service.SysRoleConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heyx
 * @ClassName: ReceptionController
 * @Description: 申请件
 * @date 2016年9月6日 下午3:56:07
 */
@Controller
@RequestMapping("/roleCondition")
public class SysRoleConditionController {

    @Autowired
    private OrganizationService OrganizationService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SysRoleConditionService sysRoleConditionService;

    @Autowired
    private NodeService nodeService;

    // 信息录入公有页面
    @RequestMapping("/init")
    public String newInfoIndex(Model model, Long roleId) {
        ServiceResult<List<OrganizationDto>> regionOrg
                = OrganizationService.selectOrgByOrgType(EmployeeMsg.ORGANIZATION_TYPE.REGION_TYPE.toNumber());
        ServiceResult<List<OrganizationDto>> storeOrg
                = OrganizationService.selectOrgByOrgType(EmployeeMsg.ORGANIZATION_TYPE.STORE_TYPE.toNumber());
        ServiceResult<List<SysNodeDto>> productList =  nodeService.getListByParent(NodeConstant.PRODUCT_PARENT_CODE);
        ServiceResult<List<SysRoleConditionDto>> roleList = sysRoleConditionService.selectByRoleId(roleId);

        ServiceResult<List<SysNodeDto>> overList = nodeService.getListByParent(NodeConstant.OVERDUE_STAGE);

        String orgStr = null;
        String[] pStr = null;
        List<String> pList = new ArrayList<String>();
        List<String> cList = new ArrayList<String>();
        List<String> sList = new ArrayList<String>();
        for(SysRoleConditionDto roleC : roleList.getData()) {
            if(RoleConditionEnum.ORG.getDesc().equals(roleC.getDimension())) {
                if(StringUtils.isEmpty(orgStr)) {
                    orgStr = roleC.getConditionCode();
                } else {
                    orgStr = orgStr + "," + roleC.getConditionCode();
                }
            }
            if(RoleConditionEnum.PRODUCT.getDesc().equals(roleC.getDimension())) {
                pStr = roleC.getConditionCode() == null ? null : roleC.getConditionCode().split(",");
                for(String yq : pStr) {
                    pList.add(roleC.getParentCode()+yq);
                }
            }
            if(RoleConditionEnum.CLIENT.getDesc().equals(roleC.getDimension())) {
                pStr = roleC.getConditionCode() == null ? null : roleC.getConditionCode().split(",");
                for(String yq : pStr) {
                    cList.add(roleC.getParentCode()+yq);
                }
            }
            if(RoleConditionEnum.SOURCE.getDesc().equals(roleC.getDimension())) {
                pStr = roleC.getConditionCode() == null ? null : roleC.getConditionCode().split(",");
                for(String yq : pStr) {
                    sList.add(roleC.getParentCode()+yq);
                }
            }
        }

        model.addAttribute("areaList", regionOrg.getData());
        model.addAttribute("shopList", storeOrg.getData());
        model.addAttribute("productList", productList.getData());
        model.addAttribute("orgs", orgStr == null ? null : orgStr.split(","));
        model.addAttribute("prds", pList);
        model.addAttribute("clients", cList);
        model.addAttribute("sources", sList);
        model.addAttribute("overs", overList.getData());
        return "/role/role_condition";

    }

    /**
     *
     * @名称 saveInfo
     * @描述 权限数据保存
     * @返回类型 AjaxResult
     * @日期 2017年7月7日 上午9:54:10
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    @RequestMapping("/saveInfo")
    @ResponseBody
    public AjaxResult saveInfo(@RequestBody String jsonStr) {
        String operatorName = ShiroUtils.getCurrentUserName();
        Long operatorId = ShiroUtils.getCurrentUserId();
        MyphLogger.info("数据权限保存,当前操作人:{},操作人编号:{},权限数据:{}", operatorName, operatorId, jsonStr);
        try {
            if (null == jsonStr) {
                return AjaxResult.formatFromServiceResult(null);
            }
            List<SysRoleConditionDto> conditionList = JSONObject.parseArray(jsonStr, SysRoleConditionDto.class);
            for(SysRoleConditionDto condition : conditionList) {
                condition.setUpdateUser(operatorName);
            }
            ServiceResult<Integer> delResult = sysRoleConditionService.deleteRoleId(conditionList.get(0).getRoleId());
            ServiceResult<Integer> addreuslt = sysRoleConditionService.batchAdd(conditionList);
            if(addreuslt.success()) {
                return AjaxResult.success();
            }
        } catch (Exception e) {
            MyphLogger.error("数据权限保存异常", e);
            return AjaxResult.failed("服务异常，请稍后重试");
        }
        return AjaxResult.failed("数据权限保存失败");
    }

}
