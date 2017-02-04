package com.myph.manage.controller.member.blacklist;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.DateUtils;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.common.util.BeanUtils;
import com.myph.manage.common.util.ExcelUtil;
import com.myph.manage.controller.BaseController;
import com.myph.manage.service.member.blacklist.ExcelRowToThirdBlack;
import com.myph.member.blacklist.dto.BlackQueryDto;
import com.myph.member.blacklist.dto.ThirdBlackDto;
import com.myph.member.blacklist.service.ThirdBlackService;

@Controller
@RequestMapping("/thirdBlack")
public class ThirdBlackController extends BaseController {
    @Autowired
    private ThirdBlackService thirdBlackService;

    /**
     * 
     * @param model
     * @return
     * @Description:进入黑名单查询
     */
    @RequestMapping("/list")
    public String list(Model model, BlackQueryDto queryDto, BasePage basePage) {
        MyphLogger.debug("开始黑名单查询：/thirdBlack/list.htm|querDto=" + queryDto + "|basePage=" + basePage);
        ServiceResult<Pagination<ThirdBlackDto>> pageResult = thirdBlackService.listPageInfos(queryDto, basePage);
        model.addAttribute("page", pageResult.getData());
        model.addAttribute("queryDto", queryDto);
        MyphLogger.debug("结束黑名单查询：/thirdBlack/list.htm|page=" + pageResult);
        return "/thirdBlack/thirdBlack";
    }

    /**
     * 方法名： addUI 描述： 添加黑名单页面
     * 
     * @author zhuzheng 创建时间：2015年10月6日 上午10:45:56
     * @return
     *
     */
    @RequestMapping("/addUI")
    public String add(Model model) {
        return "/thirdBlack/thirdBlackAdd";
    }

    /**
     * 
     * @param request
     * @param thirdBlack
     * @param parentThirdBlackId
     * @return
     * @Description:添加黑名单
     */
    @RequestMapping("/add")
    public String save(HttpServletRequest request, ThirdBlackDto thirdBlack) {
        MyphLogger.info("开始黑名单保存：/thirdBlack/add.htm|thirdBlack=" + thirdBlack);
        thirdBlack.setUpdateTime(new Date());
        thirdBlack.setCreateTime(new Date());
        thirdBlack.setCreateUser(ShiroUtils.getCurrentUserName());
        thirdBlackService.edit(thirdBlack);
        MyphLogger.info("结束黑名单保存：/thirdBlack/add.htm|thirdBlack=" + thirdBlack);
        return redirectUrl("list.htm");
    }

    /**
     * 
     * @param model
     * @param id
     * @return
     * @Description:黑名单详情页面
     */
    @RequestMapping("/detailUI")
    public String detail(Model model, @RequestParam(value = "id", required = false) Long id) {
        MyphLogger.info("开始黑名单详情：/thirdBlack/detailUI.htm|id=" + id);
        ServiceResult<ThirdBlackDto> dto = thirdBlackService.get(id);
        model.addAttribute("dto", JSON.toJSONString(dto.getData()));
        MyphLogger.info("结束黑名单详情：/thirdBlack/detailUI.htm|id=" + id);
        return "/thirdBlack/thirdBlackAdd";
    }

    /**
     * 导入黑名单
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/impInfo")
    public String impInfo(Model model, HttpServletRequest request,
            @RequestParam(value = "upFile", required = false) MultipartFile upFile) {
        MyphLogger.info("开始黑名单导入：/thirdBlack/thirdBlack.htm");
        if (null == upFile) {
            return "/innerBlack/innerBlackImport";
        }
        int result = AjaxResult.SUCCESS_CODE;
        List<ThirdBlackDto> successData = new ArrayList<ThirdBlackDto>();
        File savefile = null;
        String savePath = request.getSession().getServletContext().getRealPath("/") + File.separator + "upload"
                + File.separator;
        String fileName = upFile.getOriginalFilename();
        try {
            savefile = new File(new File(savePath), new Date().getTime() + fileName);
            if (!savefile.getParentFile().exists())
                savefile.getParentFile().mkdirs();
            upFile.transferTo(savefile);
            // excel转为bean
            ExcelRowToThirdBlack excelBlack = new ExcelRowToThirdBlack();
            excelBlack.setThirdBlackService(thirdBlackService);
            successData = ExcelUtil.readExcel(savefile, excelBlack);
            // 删除临时文件
            if (savefile.exists()) {
                savefile.delete();
            }
            List<String> excelErrorMsgs = excelBlack.getExcelErrorMsgs();// 获取校验不通过数据
            if (null != excelErrorMsgs && excelErrorMsgs.size() > 0) {
                model.addAttribute("result", AjaxResult.ERROR_CODE);
                model.addAttribute("excelErrorMsgs", excelErrorMsgs);
                MyphLogger.info("结束黑名单导入：/thirdBlack/thirdBlack.htm|导入失败");
                return "/thirdBlack/thirdBlackImport";
            } else {
                if (null != successData && !successData.isEmpty()) {
                    thirdBlackService.batchInsert(successData);
                }
            }
        } catch (Exception e) {
            result = AjaxResult.ERROR_CODE;
            MyphLogger.error(e, "异常[黑名单导入：/thirdBlack/thirdBlack.htm]");
        }
        model.addAttribute("result", result);
        model.addAttribute("successDataSize", null == successData ? 0 : successData.size());
        MyphLogger.debug("successData=============" + successData);
        MyphLogger.info("结束黑名单导入：/thirdBlack/thirdBlack.htm|导入成功");
        return "/thirdBlack/thirdBlackImport";
    }

    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, BlackQueryDto queryDto) {
        MyphLogger.debug("开始黑名单导出：/thirdBlack/export.htm");
        try {
            // 设置参数查询满足条件的所有数据不分页
            ServiceResult<List<ThirdBlackDto>> pageResult = thirdBlackService.listInfos(queryDto);
            List<ThirdBlackDto> list = pageResult.getData();
            String columnNames[] = { "姓名*", "身份证号*", "数据来源", "来源名称", "是否拒绝", "拒绝原因", "返回信息", "创建日期" };// 列名
            String keys[] = { "memberName", "idCard", "channel", "srcOrg", "isReject", "rejectReason", "respMessage",
                    "createTime" };
            String fileName = "第三方黑名单" + DateUtils.getCurrentTimeNumber();

            // 获取Excel数据
            List<Map<String, Object>> excelList = getExcelMapList(list);
            // 导出Excel数据
            exportExcel(response, fileName, columnNames, keys, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[黑名单导出：/thirdBlack/export.htm]");
        }
        MyphLogger.debug("结束黑名单导出：/thirdBlack/export.htm");
    }

    @RequestMapping("/exportTemplate")
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response, BlackQueryDto queryDto) {
        MyphLogger.debug("开始黑名单导出模板：/thirdBlack/exportTemplate.htm");
        try {
            String columnNames[] = { "姓名*", "身份证号*", "数据来源", "来源名称", "是否拒绝", "拒绝原因", "返回信息", "创建日期" };// 列名
            String keys[] = { "memberName", "idCard", "channel", "srcOrg", "isReject", "rejectReason", "respMessage",
                    "createTime" };
            String fileName = "第三方黑名单Template";
            // 获取Excel数据
            List<Map<String, Object>> excelList = new ArrayList<Map<String, Object>>();
            // 导出Excel数据
            exportExcel(response, fileName, columnNames, keys, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[黑名单导出模板：/thirdBlack/exportTemplate.htm]");
        }
        MyphLogger.debug("结束黑名单导出模板：/thirdBlack/exportTemplate.htm");
    }

    /**
     * 
     * @param srcList
     * @param destList
     * @param keys需转化的属性
     * @Description:dto转化为ExcelMap
     */
    private List<Map<String, Object>> getExcelMapList(List<ThirdBlackDto> srcList) {
        List<Map<String, Object>> destList = new ArrayList<Map<String, Object>>();
        if (null == srcList) {
            return destList;
        }
        Map<String, Object> destMap = null;
        int isReject = Constants.NO_INT;
        for (ThirdBlackDto dto : srcList) {
            destMap = BeanUtils.transBeanToMap(dto);
            isReject = (null == destMap.get("isReject") ? -1 : (Integer) destMap.get("isReject"));
            if (Constants.NO_INT == isReject) {
                destMap.put("isReject", "否");
            } else if (Constants.YES_INT == isReject) {
                destMap.put("isReject", "是");
            } else {
                destMap.put("isReject", "");
            }
            Date createTime = dto.getCreateTime();
            if (null != createTime) {
                SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_PATTERN);
                destMap.put("createTime", sdf.format(createTime));
            }
            destList.add(destMap);
        }
        return destList;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public AjaxResult delete(HttpServletRequest request, Long id) {
        MyphLogger.info("开始黑名单删除：/thirdBlack/delete.htm|id=" + id);
        AjaxResult ajax = new AjaxResult();
        try {
            ThirdBlackDto thirdBlack = new ThirdBlackDto();
            thirdBlack.setId(id);
            thirdBlack.setUpdateTime(new Date());
            thirdBlack.setModifyUser(ShiroUtils.getCurrentUserName());
            thirdBlack.setDelFlag(Constants.DELETE);
            thirdBlackService.edit(thirdBlack);
            ajax.setCode(AjaxResult.SUCCESS_CODE);
        } catch (Exception e) {
            ajax.setCode(AjaxResult.ERROR_CODE);
        }
        MyphLogger.info("结束黑名单删除：/thirdBlack/delete.htm|ajaxResutl=" + ajax);
        return ajax;
    }
}
