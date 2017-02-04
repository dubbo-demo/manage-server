/**   
 * @Title: ProductController.java 
 * @Package: com.myph.manage.controller.product 
 * @company: 麦芽金服
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 罗荣   
 * @date 2016年9月21日 下午2:07:47 
 * @version V1.0   
 */
package com.myph.manage.controller.product;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.constant.Constants;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.node.dto.SysNodeDto;
import com.myph.node.service.NodeService;
import com.myph.product.dto.ProductDto;
import com.myph.product.service.ProductService;

/**
 * @ClassName: ProductController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 罗荣
 * @date 2016年9月21日 下午2:07:47
 * 
 */
@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    NodeService nodeService;

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

    @RequestMapping("/queryPageList")
    public String queryPageList(Long productType, BasePage page, Model model) {
        if(Constants.UNSELECT_LONG.equals(productType)){
            productType = null;
        }
        ServiceResult<Pagination<ProductDto>> result = productService.queryPageList(productType,
                page.getPageSize(), page.getPageIndex());
        for (ProductDto e : result.getData().getResult()) {
            ServiceResult<SysNodeDto> nodeRs = nodeService.selectByCode(e.getPeriodsUnit());
            if(nodeRs.success()){
                if(null != nodeRs.getData()){
                   e.setPeriodsUnitName(nodeRs.getData().getNodeName());
                }
            }
        }
        model.addAttribute("page", result.getData());
        model.addAttribute("productType", productType);
        return "/product/list";
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
            //得到百分比
            if(null!=dto.getInterestRate()){
                BigDecimal newNum = dto.getInterestRate().multiply(new BigDecimal(100));
                dto.setInterestRate(newNum);
            }
            if(null!=dto.getPenaltyRate()){
                BigDecimal newNum = dto.getPenaltyRate().multiply(new BigDecimal(100));
                dto.setPenaltyRate(newNum);
            }
            if(null!=dto.getPreRepayRate()){
                BigDecimal newNum = dto.getPreRepayRate().multiply(new BigDecimal(100));
                dto.setPreRepayRate(newNum);
            }
            if(null!=dto.getServiceRate()){
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
        //把百分比除100
        if(null!=dto.getInterestRate()){
            BigDecimal newNum = dto.getInterestRate().divide(new BigDecimal(100));
            dto.setInterestRate(newNum);
        }
        if(null!=dto.getPenaltyRate()){
            BigDecimal newNum = dto.getPenaltyRate().divide(new BigDecimal(100));
            dto.setPenaltyRate(newNum);
        }
        if(null!=dto.getPreRepayRate()){
            BigDecimal newNum = dto.getPreRepayRate().divide(new BigDecimal(100));
            dto.setPreRepayRate(newNum);
        }
        if(null!=dto.getServiceRate()){
            BigDecimal newNum = dto.getServiceRate().divide(new BigDecimal(100));
            dto.setServiceRate(newNum);
        }
        
        // 更新
        if (null != dto.getId()) {
            dto.setProdCode(dto.getProdType()+"_"+dto.getPeriods());
            productService.updateByPrimaryKey(dto);
        } else {
        //保存 需要验证此期数和产品类型得到的产品是否已经存在了
            ServiceResult<ProductDto> rs = productService.selectByCode(dto.getProdType()+"_"+dto.getPeriods());
            if(rs.success()){
                return AjaxResult.failed("相同产品名称与期数不允许重复");
            }
            dto.setProdCode(dto.getProdType()+"_"+dto.getPeriods());
            dto.setDelFlag(Constants.YES_INT);
            dto.setCreateTime(new Date());
            dto.setUpdateTime(new Date());
            dto.setCreateUser(ShiroUtils.getCurrentUserName());
            productService.insert(dto);
        }
       return AjaxResult.success();
    }
}
