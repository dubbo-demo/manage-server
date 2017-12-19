/**   
 * @Title: ProductController.java 
 * @Package: com.myph.manage.controller.product 
 * @company: 麦芽金服
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 罗荣   
 * @date 2016年9月21日 下午2:07:47 
 * @version V1.0   
 */
package com.myph.manage.controller.productAttribute;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.constant.Constants;
import com.myph.common.constant.NumberConstants;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.node.service.NodeService;
import com.myph.organization.service.OrganizationService;
import com.myph.prodAttribute.dto.ProdAttributeDto;
import com.myph.prodAttribute.service.ProductAttributeService;
import com.myph.product.dto.ProductDto;
import com.myph.product.service.ProductService;

@Controller
@RequestMapping("/productAttribute")
public class ProductAttributeController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductAttributeService productAttributeService;
    @Autowired
    NodeService nodeService;
    @Autowired
    OrganizationService organizationService;

    @RequestMapping("/deleteInfo")
    @ResponseBody
    public AjaxResult deleteInfo(Long id) {
        productAttributeService.updateDelflagById(id);
        return AjaxResult.success();
    }

    @RequestMapping("/queryProductAttribute")
    public String queryProductAttribute(String prodCode, Model model) {
        List<ProdAttributeDto> result = new ArrayList<ProdAttributeDto>();
        // 未选择产品类型查所有
        if (StringUtils.isBlank(prodCode) || NumberConstants.STR_ZERO.equals(prodCode)) {
            result = productAttributeService.queryAllProductAttribute().getData();
        } else {
            result = productAttributeService.queryProductAttributeByProdCode(prodCode).getData();
        }
        if (result.size() == NumberConstants.NUM_ZERO) {
            model.addAttribute("prodCode", prodCode);
            model.addAttribute("result", result);
            return "/productAttribute/list";
        }
        //补充门店名称
        for (ProdAttributeDto dto : result) {
            String storeCodes = dto.getStoreCodes();
            String storeNames = dto.getStoreNames();
            String[] storeCodeArray = storeCodes.split("\\|");
            List<String> storeCodeList = Arrays.asList(storeCodeArray);
            for (int i = 0; i < storeCodeList.size(); i++) {
                String orgName = organizationService.queryOrgNameByOrgCode(storeCodeList.get(i));
                storeNames = StringUtils.isBlank(storeNames) ? orgName : (storeNames + "|" + orgName);
            }
            dto.setStoreNames(storeNames);
        }
        model.addAttribute("prodCode", prodCode);
        model.addAttribute("result", result);
        return "/productAttribute/list";
    }

    @RequestMapping("/new_edit")
    public String newOrEdit(Long id, Model model) {
        if (null != id) {
            ServiceResult<ProductDto> result = productService.selectByPrimaryKey(id);
            if (!result.success()) {
                model.addAttribute("result", result);
                return "/error/error";
            }
            ProductDto dto = result.getData();
            model.addAttribute("record", dto);
        }
        return "/product/new_edit";
    }
    
    
    
    
    
    
    
    
    
    

}
