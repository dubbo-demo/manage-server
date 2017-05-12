package com.myph.manage.controller.dataDetailRelation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.dataDetail.dto.DataDetailDto;
import com.myph.dataDetail.service.DataDetailService;
import com.myph.dataDetailRelation.dto.DataDetailRelationDto;
import com.myph.dataDetailRelation.service.DataDetailRelationService;
import com.myph.manage.common.shiro.ShiroUtils;

@Controller
@RequestMapping("/dataDetailRelation")
public class DataDetailRelationController {
    
    @Autowired
    private DataDetailService dataDetailService;
    
    @Autowired
    private DataDetailRelationService dataDetailRelationService;

    @RequestMapping("/queryDataDetailRelation")
    public String queryPageList(Model model) {
        ServiceResult<List<DataDetailRelationDto>> result = dataDetailRelationService.selectAllDataDetailRelation();
        model.addAttribute("result", result.getData());
        return "/dataDetailRelation/dataDetailRelation";
    }

    /**
     * 
     * @名称 queryDataDetails 
     * @描述 查询可选的资料小项
     * @返回类型 AjaxResult     
     * @日期 2017年4月24日 下午7:13:16
     * @创建人  吴阳春
     * @更新人  吴阳春
     *
     */
    @RequestMapping("/queryDataDetails")
    @ResponseBody
    public AjaxResult queryDataDetails() {
        ServiceResult<List<DataDetailDto>> dataDetailResult = dataDetailService.selectAllDataDetail();
        List<DataDetailDto> dataDetailList = dataDetailResult.getData();
        if(dataDetailList.size() <= 0){
            return AjaxResult.success();
        }
        ServiceResult<List<DataDetailRelationDto>> dataDetailRelationList = dataDetailRelationService.selectAllDataDetailRelation();
        List<String> infoCodes = new ArrayList<String>();
        for(int i=0;i<dataDetailRelationList.getData().size();i++){
            String infoCode = dataDetailRelationList.getData().get(i).getInfoCode();
            String[] infoCodeArray = infoCode.split("\\|");
            List<String> infoCodeList = Arrays.asList(infoCodeArray);
            infoCodes.addAll(infoCodeList);
        }
        Iterator<DataDetailDto> iter = dataDetailList.iterator(); 
        while (iter.hasNext()) { 
            String code = iter.next().getCode();
            if(infoCodes.contains(code)){
                iter.remove();
            }
        }
        return AjaxResult.success(dataDetailList);
    }

    
    /**
     * 
     * @名称 selectDataDetailByPageId 
     * @描述 根据大资料项id查询小资料项详细信息
     * @返回类型 AjaxResult     
     * @日期 2017年4月25日 下午3:03:05
     * @创建人  吴阳春
     * @更新人  吴阳春
     *
     */
    @RequestMapping("/selectDataDetailByPageId")
    @ResponseBody
    public AjaxResult selectDataDetailByPageId(Long id) {
        ServiceResult<List<DataDetailDto>> result = dataDetailRelationService.selectDataDetailByPageId(id);
        return AjaxResult.success(result.getData());
    }
    
    @RequestMapping("/addDataDetailRelation")
    @ResponseBody
    public AjaxResult addDataDetailRelation(DataDetailRelationDto dataDetailRelationDto) {
        ServiceResult<Integer> result = dataDetailRelationService.checkAddDataDetailRelation(dataDetailRelationDto);
        if(result.getData() != 0){
            return AjaxResult.success(result.getData());
        }
        dataDetailRelationDto.setCreateUser(ShiroUtils.getCurrentUserName());
        dataDetailRelationService.insertSelective(dataDetailRelationDto);
        return AjaxResult.success(result.getData());
    }
    
    
    
    @RequestMapping("/updateDataDetailRelation")
    @ResponseBody
    public AjaxResult updateDataDetail(DataDetailRelationDto dataDetailRelationDto) {
        ServiceResult<Integer> result = dataDetailRelationService.checkUpdateDataDetailRelation(dataDetailRelationDto);
        if(result.getData() != 0){
            return AjaxResult.success(result.getData());
        }
        dataDetailRelationService.updateByPrimaryKeySelective(dataDetailRelationDto);
        return AjaxResult.success(result.getData());
    }
    
    @RequestMapping("/queryDataDetailRelationById")
    @ResponseBody
    public AjaxResult queryDataDetailRelationById(Long id) {
        ServiceResult<DataDetailRelationDto> result = dataDetailRelationService.selectByPrimaryKey(id);
        return AjaxResult.success(result.getData());
    }
}
