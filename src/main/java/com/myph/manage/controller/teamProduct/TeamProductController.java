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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.constant.NodeConstant;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.node.dto.SysNodeDto;
import com.myph.node.service.NodeService;
import com.myph.team.dto.SysTeamDto;
import com.myph.team.service.SysTeamService;
import com.myph.teamProduct.dto.TeamProductDto;
import com.myph.teamProduct.service.TeamProductService;

@Controller
@RequestMapping("/teamProduct")
public class TeamProductController {

    @Autowired
    private TeamProductService teamProductService;
    
    @Autowired
    private NodeService nodeService;
    
    @Autowired
    private SysTeamService sysTeamService;

    @RequestMapping("/queryPageList")
    public String queryPageList(@RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
            Model model, Integer pageSize) {
        if (null == pageSize) {
            pageSize = 10;
        }
        BasePage basePage = new BasePage(pageIndex, pageSize);
        // 查询团队产品关系信息
        ServiceResult<Pagination<TeamProductDto>> result = teamProductService.queryTeamProductInfo(basePage);
        List<TeamProductDto> teamProductDtoList = result.getData().getResult();
        // 补充产品名称
        for (int i = 0; i < teamProductDtoList.size(); i++) {
            String productTypes = teamProductDtoList.get(i).getProductTypes();
            List<String> productTypeNameList = new ArrayList<String>();
            StringBuffer productTypeNames = new StringBuffer();
            String[] productTypeArray = productTypes.split("\\|");
            for (int j = 0; j < productTypeArray.length; j++) {
                if(StringUtils.isBlank(productTypeArray[j])){
                    continue;
                }
                ServiceResult<SysNodeDto> nameResult = nodeService.selectByPrimaryKey(Long.valueOf(productTypeArray[j]));
                productTypeNameList.add(nameResult.getData().getNodeName());
                if (productTypeNames.length() <= 0) {
                    productTypeNames = new StringBuffer(nameResult.getData().getNodeName());
                } else {
                    productTypeNames.append(",").append(nameResult.getData().getNodeName());
                }
            }
            result.getData().getResult().get(i).setProductTypeNames(productTypeNames);
        }
        model.addAttribute("page", result.getData());
        //查询基础数据产品类型信息
        ServiceResult<List<SysNodeDto>> productTypeResult = nodeService.getListByParent(NodeConstant.PRODUCT_PARENT_CODE);
        model.addAttribute("productTypeResult", productTypeResult.getData());
        //获取未与产品进行关联的团队信息
        ServiceResult<List<SysTeamDto>> teamResult = sysTeamService.queryTeamsNoRelationProductType();
        model.addAttribute("teamResult", teamResult.getData());
        return "/teamProduct/teamProduct";
    }

    @RequestMapping("/addTeamProduct")
    @ResponseBody
    public AjaxResult addTeamProduct(TeamProductDto teamProductDto) {
        teamProductDto.setCreateUser(ShiroUtils.getCurrentUserName());
        teamProductService.insertSelective(teamProductDto);
        return AjaxResult.success();
    }
    
    @RequestMapping("/queryteamProductById")
    @ResponseBody
    public AjaxResult queryteamProductById(Long id) {
        ServiceResult<TeamProductDto> result = teamProductService.selectByPrimaryKey(id);
        return AjaxResult.success(result.getData());
    }
    
    @RequestMapping("/updateTeamProduct")
    @ResponseBody
    public AjaxResult updateTeamProduct(TeamProductDto teamProductDto) {
        teamProductService.updateByPrimaryKeySelective(teamProductDto);
        return AjaxResult.success();
    }
}
