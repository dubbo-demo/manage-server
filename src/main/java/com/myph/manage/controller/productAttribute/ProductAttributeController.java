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

    @RequestMapping("/allList")
    @ResponseBody
    public AjaxResult list() {
        ServiceResult<List<ProductDto>> result = productService.selectAll();
        return AjaxResult.success(result.getData());
    }

    @RequestMapping("/selectByProductType")
    @ResponseBody
    public AjaxResult selectByProductType(Long proType) {
        ServiceResult<List<ProductDto>> result = productService.queryListByProdType(proType);
        return AjaxResult.success(result.getData());
    }

    @RequestMapping("/deleteInfo")
    @ResponseBody
    public AjaxResult deleteInfo(Long id) {
        ServiceResult<Integer> result = productService.deleteByPrimaryKey(id);
        return AjaxResult.success(result.getData());
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
            // 得到百分比
            if (null != dto.getInterestRate()) {
                BigDecimal newNum = dto.getInterestRate().multiply(new BigDecimal(100));
                dto.setInterestRate(newNum);
            }
            if (null != dto.getPenaltyRate()) {
                BigDecimal newNum = dto.getPenaltyRate().multiply(new BigDecimal(100));
                dto.setPenaltyRate(newNum);
            }
            if (null != dto.getPreRepayRate()) {
                BigDecimal newNum = dto.getPreRepayRate().multiply(new BigDecimal(100));
                dto.setPreRepayRate(newNum);
            }
            if (null != dto.getServiceRate()) {
                BigDecimal newNum = dto.getServiceRate().multiply(new BigDecimal(100));
                dto.setServiceRate(newNum);
            }
            model.addAttribute("record", dto);
        }
        return "/product/new_edit";
    }

    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public AjaxResult saveOrUpdate(ProductDto dto, Model model) {
        // 把百分比除100
        if (null != dto.getInterestRate()) {
            BigDecimal newNum = dto.getInterestRate().divide(new BigDecimal(100));
            dto.setInterestRate(newNum);
        }
        if (null != dto.getPenaltyRate()) {
            BigDecimal newNum = dto.getPenaltyRate().divide(new BigDecimal(100));
            dto.setPenaltyRate(newNum);
        }
        if (null != dto.getPreRepayRate()) {
            BigDecimal newNum = dto.getPreRepayRate().divide(new BigDecimal(100));
            dto.setPreRepayRate(newNum);
        }
        if (null != dto.getServiceRate()) {
            BigDecimal newNum = dto.getServiceRate().divide(new BigDecimal(100));
            dto.setServiceRate(newNum);
        }

        // 更新
        if (null != dto.getId()) {
            dto.setProdCode(dto.getProdType() + "_" + dto.getPeriods());
            productService.updateByPrimaryKey(dto);
        } else {
            // 保存 需要验证此期数和产品类型得到的产品是否已经存在了
            ServiceResult<ProductDto> rs = productService.selectByCode(dto.getProdType() + "_" + dto.getPeriods());
            if (rs.success()) {
                return AjaxResult.failed("相同产品名称与期数不允许重复");
            }
            dto.setProdCode(dto.getProdType() + "_" + dto.getPeriods());
            dto.setDelFlag(Constants.YES_INT);
            dto.setCreateTime(new Date());
            dto.setUpdateTime(new Date());
            dto.setCreateUser(ShiroUtils.getCurrentUserName());
            productService.insert(dto);
        }
        return AjaxResult.success();
    }
}
