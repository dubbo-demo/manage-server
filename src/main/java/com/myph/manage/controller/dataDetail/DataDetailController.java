package com.myph.manage.controller.dataDetail;

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
import com.myph.manage.common.shiro.ShiroUtils;

@Controller
@RequestMapping("/dataDetail")
public class DataDetailController {
    
    @Autowired
    private DataDetailService dataDetailService;

    @RequestMapping("/queryDataDetail")
    public String queryPageList(Model model) {
        ServiceResult<List<DataDetailDto>> result = dataDetailService.selectAllDataDetail();
        model.addAttribute("result", result.getData());
        return "/dataDetail/dataDetail";
    }

    @RequestMapping("/addDataDetail")
    @ResponseBody
    public AjaxResult checkAddDataDetail(DataDetailDto dataDetailDto) {
        ServiceResult<Integer> result = dataDetailService.checkAddDataDetail(dataDetailDto);
        if(result.getData() != 0){
            return AjaxResult.success(result.getData());
        }
        dataDetailDto.setCreateUser(ShiroUtils.getCurrentUserName());
        dataDetailService.insertSelective(dataDetailDto);
        return AjaxResult.success(result.getData());
    }

    @RequestMapping("/updateDataDetail")
    @ResponseBody
    public AjaxResult updateDataDetail(DataDetailDto dataDetailDto) {
        ServiceResult<Integer> result = dataDetailService.checkUpdateDataDetail(dataDetailDto);
        if(result.getData() != 0){
            return AjaxResult.success(result.getData());
        }
        dataDetailService.updateByPrimaryKeySelective(dataDetailDto);
        return AjaxResult.success(result.getData());
    }
}
