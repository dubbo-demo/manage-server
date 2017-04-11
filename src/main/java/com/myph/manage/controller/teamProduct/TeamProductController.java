/**   
 * @Title: ProductController.java 
 * @Package: com.myph.manage.controller.product 
 * @company: 麦芽金服
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 罗荣   
 * @date 2016年9月21日 下午2:07:47 
 * @version V1.0   
 */
package com.myph.manage.controller.teamProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.constant.Constants;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.employee.dto.EmployeeSysUserDto;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.node.dto.SysNodeDto;
import com.myph.node.service.NodeService;
import com.myph.product.dto.ProductDto;
import com.myph.product.service.ProductService;
import com.myph.teamProduct.dto.TeamProductDto;
import com.myph.teamProduct.service.TeamProductService;

@Controller
@RequestMapping("/teamProduct")
public class TeamProductController {

    @Autowired
    private TeamProductService teamProductService;

    @Autowired
    private ProductService productService;

    @RequestMapping("/queryPageList")
    public String queryPageList(@RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
            Model model, Integer pageSize) {
        if (null == pageSize) {
            pageSize = 10;
        }
        BasePage basePage = new BasePage(pageIndex, pageSize);
        // 查询团队产品关系信息
        ServiceResult<Pagination<TeamProductDto>> result = teamProductService.queryTeamProductInfo(basePage);
        // 补充产品名称
        for (int i = 0; i < result.getData().getResult().size(); i++) {
            String productTypes = result.getData().getResult().get(i).getProductTypes();
            List<String> productTypeNameList = new ArrayList<String>();
            StringBuffer productTypeNames = null;
            String[] productTypeArray = productTypes.split("\\|");
            for (int j = 0; j < productTypeArray.length; j++) {
                ServiceResult<String> nameResult = productService.getProductNameById(Long.valueOf(productTypeArray[i]));
                productTypeNameList.add(nameResult.getData());
                if (productTypeNames.length() <= 0) {
                    productTypeNames = new StringBuffer(nameResult.getData());
                } else {
                    productTypeNames.append(",").append(nameResult.getData());
                }
            }
            result.getData().getResult().get(i).setProductTypeNames(productTypeNames);
        }
        model.addAttribute("page", result.getData());
        return "/teamProduct/teamProduct";
    }

}
