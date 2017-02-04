package com.myph.manage.controller.productFile;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.constant.NodeConstant;
import com.myph.constant.ProductFileConstant;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.node.dto.SysNodeDto;
import com.myph.node.service.NodeService;
import com.myph.product.dto.ProductFiletypeDto;
import com.myph.product.service.ProductFileTypeService;

@Controller
@RequestMapping("/productFile")
public class ProductFileController {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private ProductFileTypeService productFileTypeService;

    /**
     * 
     * @名称 productFile
     * @描述 产品上传附件管理
     * @返回类型 String
     * @日期 2016年9月6日 下午4:42:46
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/productFileManage")
    public String productFile(Model model) {
        try {
            return "productFile/productFile";
        } catch (Exception e) {
            MyphLogger.error(e, "产品上传附件管理");
            return "error/500";
        }
    }

    /**
     * 
     * @名称 showProduct
     * @描述 根据父级编码查询产品信息
     * @返回类型 AjaxResult
     * @日期 2016年9月7日 上午10:37:15
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/showProduct")
    @ResponseBody
    public AjaxResult showProduct() {
        try {
            ServiceResult<List<SysNodeDto>> result = nodeService.getListByParent(NodeConstant.PRODUCT_PARENT_CODE);
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "根据父级编码查询产品信息异常");
            return AjaxResult.failed("根据父级编码查询产品信息异常");
        }
    }

    /**
     * 
     * @名称 showFileUpState
     * @描述 根据父级编码查询文件上传阶段
     * @返回类型 AjaxResult
     * @日期 2016年9月7日 下午7:15:33
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/showFileUpState")
    @ResponseBody
    public AjaxResult showFileUpState() {
        try {
            ServiceResult<List<SysNodeDto>> result = nodeService
                    .getListByParent(NodeConstant.FILE_UP_STATE_PARENT_CODE);
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "根据父级编码查询文件上传阶段异常");
            return AjaxResult.failed("根据父级编码查询文件上传阶段异常");
        }
    }

    /**
     * 
     * @名称 showProductFile
     * @描述 查询产品上传附件信息
     * @返回类型 AjaxResult
     * @日期 2016年9月7日 下午7:56:49
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/showProductFile")
    @ResponseBody
    public AjaxResult showProductFile(String productName) {
        try {
            ServiceResult<List<ProductFiletypeDto>> result = productFileTypeService.showProductFile(productName);
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "查询产品上传附件信息异常,入参:{}", productName);
            return AjaxResult.failed("查询产品上传附件信息异常");
        }
    }

    /**
     * 
     * @名称 changeProductFile
     * @描述 新增或修改产品上传附件信息
     * @返回类型 AjaxResult
     * @日期 2016年9月7日 下午7:57:40
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/changeProductFile")
    @ResponseBody
    public AjaxResult changeProductFile(ProductFiletypeDto productFiletypeDto) {
        try {
            ServiceResult<Boolean> checkName = productFileTypeService.checkName(productFiletypeDto);
            if (checkName.getData()) {
                if (ProductFileConstant.INSERT_TYPE.equals(productFiletypeDto.getInsertOrUpdate())) {
                    // 新增
                    productFiletypeDto.setCreateUser(ShiroUtils.getCurrentUserName());
                    productFileTypeService.insert(productFiletypeDto);
                } else {
                    // 更新
                    productFileTypeService.updateByPrimaryKey(productFiletypeDto);
                }
            }
            return AjaxResult.success();
        } catch (Exception e) {
            MyphLogger.error(e, " 新增或修改产品上传附件信息异常,入参:{}", productFiletypeDto.toString());
            return AjaxResult.failed(" 新增或修改产品上传附件信息异常");
        }
    }

    @RequestMapping("/selectByPrimaryKey")
    @ResponseBody
    public AjaxResult selectByPrimaryKey(Long id) {
        try {
            ServiceResult<ProductFiletypeDto> result = productFileTypeService.selectByPrimaryKey(id);
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "查询产品上传附件异常,入参:{}", id);
            return AjaxResult.failed("查询产品上传附件异常");
        }
    }

    @RequestMapping("/deleteByPrimaryKey")
    @ResponseBody
    public AjaxResult deleteByPrimaryKey(Long id) {
        try {
            int result = productFileTypeService.deleteByPrimaryKey(id);
            return AjaxResult.success(result);
        } catch (Exception e) {
            MyphLogger.error(e, "删除产品上传附件异常,入参:{}", id);
            return AjaxResult.failed("删除产品上传附件异常");
        }
    }
}
