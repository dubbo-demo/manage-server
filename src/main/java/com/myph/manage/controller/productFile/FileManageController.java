package com.myph.manage.controller.productFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.apply.dto.FileManageApplyInfoDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.constant.ApplyUtils;
import com.myph.constant.FlowStateEnum;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.product.service.ProductService;

@Controller
@RequestMapping("/productFile")
public class FileManageController {

    @Autowired
    private ApplyInfoService applyInfoService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EmployeeInfoService employeeInfoService;

    @Autowired
    private ProductService productService;

    /**
     * 
     * @名称 productFile
     * @描述 附件管理--管理员
     * @返回类型 String
     * @日期 2016年9月6日 下午4:42:46
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/fileManage")
    public String fileManage(Model model) {
        try {
            return "productFile/fileManage";
        } catch (Exception e) {
            MyphLogger.error(e, "附件管理--管理员异常");
            return "error/500";
        }
    }

    @RequestMapping("/showFileManage")
    @ResponseBody
    public AjaxResult showFileManage(String applyLoanNo) {
        try {
            // 查询进件表基本信息
            ServiceResult<FileManageApplyInfoDto> result = applyInfoService.selectByApplyLoanNo(applyLoanNo);
            // 补充门店名称、大区名称、业务经理姓名、客服姓名、主状态名称、产品名称
            // 1、补充门店名称
            ServiceResult<OrganizationDto> storeResult = organizationService
                    .selectOrganizationById(result.getData().getStoreId());
            result.getData().setStoreName(storeResult.getData().getOrgName());
            // 2、补充大区名称
            ServiceResult<OrganizationDto> areaResult = organizationService
                    .selectOrganizationById(result.getData().getAreaId());
            result.getData().setAreaName(areaResult.getData().getOrgName());
            // 3、补充业务经理姓名
            ServiceResult<EmployeeInfoDto> bmResult = employeeInfoService.getEntityById(result.getData().getBmId());
            result.getData().setBmName(bmResult.getData().getEmployeeName());
            // 4、补充客户姓名
            ServiceResult<EmployeeInfoDto> customerServiceResult = employeeInfoService
                    .getEntityById(result.getData().getCustomerServiceId());
            result.getData().setCustomerServiceName(customerServiceResult.getData().getEmployeeName());
            // 5、补充主状态名称
            Integer state = result.getData().getState();
            Integer subState = result.getData().getSubState();
            if (state != null) {
                String stateName = ApplyUtils.getFullStateDesc(state, subState);
                result.getData().setStateName(stateName);
            }
            // 6、补充产品名称
            ServiceResult<String> productNameResult = productService
                    .getProductNameById(result.getData().getProductType());
            result.getData().setProductName(productNameResult.getData());
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "附件管理--管理员异常,入参:{}", applyLoanNo);
            return AjaxResult.failed("附件管理--管理员异常");
        }
    }
}