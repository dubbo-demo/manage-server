package com.myph.manage.controller.reception;

import com.myph.manage.common.shiro.dto.RoleConditionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.apply.service.ApplyInfoService;
import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.SensitiveInfoUtils;
import com.myph.employee.constants.EmployeeMsg.ORGANIZATION_TYPE;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.employee.dto.EmpDetailDto;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.reception.dto.ApplyReceptionDto;
import com.myph.reception.dto.ApplyReceptionManageDto;
import com.myph.reception.service.ApplyReceptionManageService;
import com.myph.reception.service.ApplyReceptionService;

/**
 * 
 * @ClassName: ReceptionManageController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author heyx
 * @date 2016年11月21日 上午10:02:06
 *
 */
@Controller
@RequestMapping("/receptionManage")
public class ReceptionManageController {

    @Autowired
    ApplyReceptionService applyReceptionService;

    @Autowired
    ApplyReceptionManageService applyReceptionManageService;

    @Autowired
    ApplyInfoService applyInfoService;

    @Autowired
    OrganizationService organizationService;

    /**
     * 列表
     * 
     * @param model
     * @param dto
     * @param basePage
     * @return
     */
    @RequestMapping("/list")
    public String listManage(Model model, ApplyReceptionDto dto, BasePage basePage) {
        MyphLogger.info("接待管理列表分页查询条件", dto.toString());
        EmployeeInfoDto user = ShiroUtils.getCurrentUser();
        EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
        RoleConditionDto rdto = ShiroUtils.getRoleCondition();

        // 配有渠道权限数据
        if(null != rdto && null != rdto.getClients() && rdto.getClients().size() == 1) {
            dto.setClientType(rdto.getClients().get(0));
        }
        // 门店级别
        if (null != empDetail && ORGANIZATION_TYPE.STORE_TYPE.toNumber() == user.getOrgType()) {
            model.addAttribute("empStoreId", empDetail.getStoreId());
            dto.setStoreId(empDetail.getStoreId());
        }
        model.addAttribute("orgType", user.getOrgType());
        model.addAttribute("regionId", empDetail.getRegionId());
        // 大区级别
        if (null != empDetail.getRegionId() && ORGANIZATION_TYPE.REGION_TYPE.toNumber() == user.getOrgType()) {
            dto.setAreaId(empDetail.getRegionId());
        }
        if (null == basePage.getSortField()) {
            basePage.setSortField("createTime");
            basePage.setSortOrder("desc");
        }
        ServiceResult<Pagination<ApplyReceptionDto>> resultInfo = applyReceptionService.listInfo(dto, basePage);
        if (resultInfo.success() && resultInfo.getData() != null) {
            for (ApplyReceptionDto dtoTemp : resultInfo.getData().getResult()) {
                ServiceResult<OrganizationDto> orgDtoResult = organizationService.selectOrganizationById(dtoTemp
                        .getStoreId());
                if (orgDtoResult.success() && orgDtoResult.getData() != null) {
                    dtoTemp.setStoreName(orgDtoResult.getData().getOrgName());
                }
                dtoTemp.setPhone(SensitiveInfoUtils.maskMobilePhone(dtoTemp.getPhone()));// 隐藏手机号
                dtoTemp.setMemberName(SensitiveInfoUtils.maskUserName(dtoTemp.getMemberName()));// 隐藏姓名
            }
        }
        model.addAttribute("page", resultInfo.getData());
        model.addAttribute("queryDto", dto);
        // MyphLogger.info("接待管理列表分页查询", resultInfo.getData());
        return "/reception/applyReceptionManage_list";
    }

    /**
     * 分配接待信息界面
     * 
     * @return
     */
    @RequestMapping("/allotForm")
    public String allotForm(Model model, String id) {
        MyphLogger.info("分配接待信息界面 id:{}", id);
        ServiceResult<ApplyReceptionDto> data = applyReceptionService.queryInfoById(id);
        model.addAttribute("dto", data.getData());
        return "/reception/applyReception_allot";
    }

    /**
     * 分配接待信息
     * 
     * @return
     */
    @RequestMapping("/allotInfo")
    @ResponseBody
    public synchronized AjaxResult allotInfo(ApplyReceptionManageDto applyReceptionManageDto) {
        try {
//            EmployeeInfoDto user = ShiroUtils.getCurrentUser();
            EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
            if (null == empDetail || empDetail.getIsManage().equals(Constants.NOT_MANAGE)) {
                MyphLogger.info("分配接待信息失败，请用管理账户录入 username:{}", ShiroUtils.getCurrentUserName());
                return AjaxResult.failed("分配接待信息失败，请用管理账户录入!");
            }
//            applyReceptionManageDto.setStoreId(user.getOrgId());
            applyReceptionManageDto.setCreateUser(ShiroUtils.getCurrentUserName());
            ServiceResult<Integer> result = applyReceptionManageService.updateCustomer(applyReceptionManageDto);
            MyphLogger.info("分配接待信息 applyReceptionDto:{}", applyReceptionManageDto.toString());
            return AjaxResult.formatFromServiceResult(result);
        } catch (Exception e) {
            MyphLogger.error(e, "分配接待信息异常,applyReceptionDto:{}", applyReceptionManageDto.toString());
            return AjaxResult.failed("分配接待信息异常");
        }
    }

}
