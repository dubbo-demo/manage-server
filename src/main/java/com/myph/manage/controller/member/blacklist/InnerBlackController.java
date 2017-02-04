package com.myph.manage.controller.member.blacklist;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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
import com.myph.manage.service.member.blacklist.ExcelRowToInnerBlack;
import com.myph.member.blacklist.dto.BlackQueryDto;
import com.myph.member.blacklist.dto.InnerBlackDto;
import com.myph.member.blacklist.service.InnerBlackService;

@Controller
@RequestMapping("/innerBlack")
public class InnerBlackController extends BaseController {
    @Autowired
    private InnerBlackService innerBlackService;

    /**
     * 
     * @param model
     * @return
     * @Description:进入黑名单查询
     */
    @RequestMapping("/list")
    public String list(Model model, BlackQueryDto queryDto, BasePage basePage) {
        MyphLogger.debug("开始黑名单查询：/innerBlack/list.htm|querDto=" + queryDto + "|basePage=" + basePage);
        ServiceResult<Pagination<InnerBlackDto>> pageResult = innerBlackService.listPageInfos(queryDto, basePage);
        model.addAttribute("page", pageResult.getData());
        model.addAttribute("queryDto", queryDto);
        MyphLogger.debug("结束黑名单查询：/innerBlack/list.htm|page=" + pageResult);
        return "/innerBlack/innerBlack";
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
        return "/innerBlack/innerBlackAdd";
    }

    /**
     * 
     * @param request
     * @param innerBlack
     * @param parentInnerBlackId
     * @return
     * @Description:添加黑名单
     */
    @RequestMapping("/add")
    public String save(HttpServletRequest request, InnerBlackDto innerBlack) {
        MyphLogger.info("开始黑名单保存：/innerBlack/add.htm|innerBlack=" + innerBlack);
        innerBlack.setCreateUser(ShiroUtils.getCurrentUserName());
        innerBlackService.edit(innerBlack);
        MyphLogger.info("结束黑名单保存：/innerBlack/add.htm|innerBlack=" + innerBlack);
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
        MyphLogger.info("开始黑名单详情：/innerBlack/detailUI.htm|id=" + id);
        ServiceResult<InnerBlackDto> dto = innerBlackService.get(id);
        model.addAttribute("dto", JSON.toJSONString(dto.getData()));
        MyphLogger.info("结束黑名单详情：/innerBlack/detailUI.htm|id=" + id);
        return "/innerBlack/innerBlackAdd";
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
        MyphLogger.info("开始黑名单导入：/innerBlack/impInfo.htm");
        if (null == upFile) {
            return "/innerBlack/innerBlackImport";
        }
        int result = AjaxResult.SUCCESS_CODE;
        List<InnerBlackDto> successData = new ArrayList<InnerBlackDto>();
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
            ExcelRowToInnerBlack excelBlack = new ExcelRowToInnerBlack();
            excelBlack.setInnerBlackService(innerBlackService);
            successData = ExcelUtil.readExcel(savefile, excelBlack);
            // 删除临时文件
            if (savefile.exists()) {
                savefile.delete();
            }
            List<String> excelErrorMsgs = excelBlack.getExcelErrorMsgs();// 获取校验不通过数据
            if (CollectionUtils.isNotEmpty(excelErrorMsgs)) {
                model.addAttribute("result", AjaxResult.ERROR_CODE);
                model.addAttribute("excelErrorMsgs", excelErrorMsgs);
                MyphLogger.info("结束黑名单导入：/innerBlack/impInfo.htm|导入失败");
                return "/innerBlack/innerBlackImport";
            } else {
                if (null != successData && !successData.isEmpty()) {
                    innerBlackService.batchInsert(successData);
                }
            }
        } catch (Exception e) {
            result = AjaxResult.ERROR_CODE;
            MyphLogger.error(e, "异常[开始黑名单导入：/innerBlack/impInfo.htm]");
        }
        model.addAttribute("result", result);
        model.addAttribute("successDataSize", null == successData ? 0 : successData.size());
        MyphLogger.debug("successData=============" + successData);
        MyphLogger.info("结束黑名单导入：/innerBlack/impInfo.htm|导入成功");
        return "/innerBlack/innerBlackImport";
    }

    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, BlackQueryDto queryDto) {
        MyphLogger.debug("开始黑名单导出：/innerBlack/export.htm");
        try {
            // 设置参数查询满足条件的所有数据不分页
            ServiceResult<List<InnerBlackDto>> pageResult = innerBlackService.listInfos(queryDto);
            List<InnerBlackDto> list = pageResult.getData();
            String columnNames[] = { "姓名*", "身份证号*", "手机号", "户籍地址", "现住地址", "有无子女", "是否有逾期", "逾期天数", "创建日期" };// 列名
            String keys[] = { "memberName", "idCard", "phone", "registdAddr", "currentAddr", "hasChildren",
                    "hasOverdue", "overdueDays", "createTime" };
            String fileName = "内部黑名单" + DateUtils.getCurrentTimeNumber();

            // 获取Excel数据
            List<Map<String, Object>> excelList = getExcelMapList(list);
            // 导出Excel数据
            exportExcel(response, fileName, columnNames, keys, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[黑名单导出：/innerBlack/export.htm]");
        }
        MyphLogger.debug("结束黑名单导出：/innerBlack/export.htm");
    }

    @RequestMapping("/exportTemplate")
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response, BlackQueryDto queryDto) {
        MyphLogger.debug("开始黑名单导出模板：/innerBlack/exportTemplate.htm");
        try {
            String columnNames[] = { "姓名*", "身份证号*", "手机号", "户籍地址", "现住地址", "有无子女", "是否有逾期", "逾期天数", "创建日期" };// 列名
            String keys[] = { "memberName", "idCard", "phone", "registdAddr", "currentAddr", "hasChildren",
                    "hasOverdue", "overdueDays", "createTime" };
            String fileName = "内部黑名单Template";
            // 获取Excel数据
            List<Map<String, Object>> excelList = new ArrayList<Map<String, Object>>();
            // 导出Excel数据
            exportExcel(response, fileName, columnNames, keys, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[黑名单导出模板：/innerBlack/exportTemplate.htm]");
        }
        MyphLogger.debug("结束黑名单导出模板：/innerBlack/exportTemplate.htm");
    }

    /**
     * 
     * @param srcList
     * @param destList
     * @param keys需转化的属性
     * @Description:dto转化为ExcelMap
     */
    private List<Map<String, Object>> getExcelMapList(List<InnerBlackDto> srcList) {
        List<Map<String, Object>> destList = new ArrayList<Map<String, Object>>();
        if (null == srcList) {
            return destList;
        }
        Map<String, Object> destMap = null;
        int hasChildren = 0;
        int hasOverdue = 0;
        for (InnerBlackDto dto : srcList) {
            destMap = BeanUtils.transBeanToMap(dto);
            hasChildren = (null == destMap.get("hasChildren") ? -1 : (Integer) destMap.get("hasChildren"));
            if (Constants.NO_INT == hasChildren) {
                destMap.put("hasChildren", "无");
            } else if (Constants.YES_INT == hasChildren) {
                destMap.put("hasChildren", "有");
            } else {
                destMap.put("hasChildren", "");
            }
            hasOverdue = (null == destMap.get("hasOverdue") ? -1 : (Integer) destMap.get("hasOverdue"));
            if (Constants.NO_INT == hasOverdue) {
                destMap.put("hasOverdue", "无");
            } else if (Constants.YES_INT == hasOverdue) {
                destMap.put("hasOverdue", "有");
            } else {
                destMap.put("hasOverdue", "");
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

    /**
     * 
     * @param idCard
     * @return
     * @Description:检查身份证是否存在
     */
    @RequestMapping("/checkIdCardExist")
    @ResponseBody
    public boolean checkIdCardExist(String idCard) {
        MyphLogger.debug("开始校验身份证：/innerBlack/checkIdCardExist.htm|idCard=" + idCard);
        // 校验身份证是否存在
        boolean exist = innerBlackService.isIdCardExist(idCard);
        MyphLogger.debug("结束校验身份证：/innerBlack/checkIdCardExist.htm|idCard=" + idCard + "|exist=" + exist);
        if (exist) {
            return false;
        } else {
            return true;
        }

    }
}
