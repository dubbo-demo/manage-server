package com.myph.manage.controller.cityCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.cityCode.dto.CityCodeDto;
import com.myph.cityCode.service.CityCodeService;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.employee.constants.EmployeeMsg.DISTRICT_TREE_LEVEL;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.sysDistrict.dto.SysDistrictDto;
import com.myph.sysDistrict.service.SysDistrictService;

@Controller
@RequestMapping("/cityCode")
public class CityCodeController {
    @Autowired
    private CityCodeService cityCodeService;

    @Autowired
    private SysDistrictService sysDistrictService;

    /**
     * 
     * @名称 selectCityCode
     * @描述 查询地市编码信息
     * @返回类型 AjaxResult
     * @日期 2016年9月5日 下午3:33:50
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/selectCityCode")
    @ResponseBody
    public AjaxResult selectCityCode() {
        try {
            MyphLogger.debug("CityCodeController.selectCityCode 输入参数[]");
            Map<String, Object> model = new HashMap<String, Object>();
            ServiceResult<List<CityCodeDto>> result = cityCodeService.selectCityCode();
            model.put("selectCityCode", result.getData());
            return AjaxResult.success(model);
        } catch (Exception e) {
            MyphLogger.error(e, "查询地市编码信息异常");
            return AjaxResult.failed("查询地市编码信息异常");
        }
    }

    @RequestMapping("/showCityCode")
    public String queryEmployeeInfo(Model model) {
        try {
            ServiceResult<List<CityCodeDto>> cityCodeResult = cityCodeService.selectCityCode();
            model.addAttribute("cityCodeResult", cityCodeResult.getData());
            // 查询省
            ServiceResult<List<SysDistrictDto>> sysDistrictResult = sysDistrictService
                    .selectByTreelevel(DISTRICT_TREE_LEVEL.LEVEL_1.toNumber());
            model.addAttribute("sysDistrictResult", sysDistrictResult.getData());
            return "cityCode/cityCode";
        } catch (Exception e) {
            MyphLogger.error(e, "查询地市编码信息异常");
            return "error/500";
        }
    }

    /**
     * 
     * @名称 getCityByProvinceId
     * @描述 根据省份ID找市
     * @返回类型 AjaxResult
     * @日期 2016年10月13日 下午3:24:46
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/getCity")
    @ResponseBody
    public AjaxResult getCityByProvinceId(Long id) {
        try {
            ServiceResult<List<SysDistrictDto>> sysDistrictResult = sysDistrictService.getCityByProvinceId(id);
            return AjaxResult.success(sysDistrictResult.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "根据省份ID找市异常,入参:{}", id);
            return AjaxResult.failed("根据省份ID找市异常");
        }
    }

    @RequestMapping("/delCityCode")
    @ResponseBody
    public AjaxResult delCityCode(Long changeCityId) {
        try {
            ServiceResult<Integer> result = cityCodeService.deleteByPrimaryKey(changeCityId);
            return AjaxResult.success(result);
        } catch (Exception e) {
            MyphLogger.error(e, "删除地市信息异常,入参:{}", changeCityId);
            return AjaxResult.failed("删除地市信息异常");
        }
    }

    @RequestMapping("/insertOrUpdate")
    @ResponseBody
    public AjaxResult insertOrUpdate(CityCodeDto cityCodeDto,
            @RequestParam("insertOrUpdate") Integer insertOrUpdate) {
        try {
            // 校验地市名称与编码防重 1城市名已存在 2城市编码已存在
            ServiceResult<Integer> checkResult = cityCodeService.checkCity(cityCodeDto.getCityName(), cityCodeDto.getCityCode(), insertOrUpdate);
            if (!checkResult.getData().equals(0)) {
                return AjaxResult.success(checkResult.getData());
            }
            // insertOrUpdate 0新增 1更新
            if (insertOrUpdate.equals(0)) {
                CityCodeDto record = new CityCodeDto();
                record.setCityCode(cityCodeDto.getCityCode());
                record.setCityName(cityCodeDto.getCityName());
                record.setCreateUser(ShiroUtils.getCurrentUserName());
                record.setProvinceCode(cityCodeDto.getProvinceCode());
                cityCodeService.insertSelective(record);
            } else {
                CityCodeDto record = new CityCodeDto();
                record.setCityCode(cityCodeDto.getCityCode());
                record.setId(cityCodeDto.getId());
                cityCodeService.updateByPrimaryKeySelective(record);
            }
            return AjaxResult.success(0);
        } catch (Exception e) {
            MyphLogger.error(e, "修改地市信息异常,入参:{},{}", cityCodeDto.toString(),insertOrUpdate);
            return AjaxResult.failed("修改地市信息异常");
        }
    }
    
}
