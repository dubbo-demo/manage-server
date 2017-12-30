package com.myph.manage.controller;

/**
 * INFO: info
 * User: xinpei.xu
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */

import com.myph.manage.common.util.ExcelUtil;
import com.myph.manage.common.util.MYPHConfigUtils;
import com.way.common.log.WayLogger;
import com.way.common.rom.annotation.BasePage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public abstract class BaseController {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    protected static final int DEFAULT_PAGE_SIZE = 10;

    @Autowired
    protected MYPHConfigUtils configUtils;

    public BaseController() {
    }

    public String getPath(HttpServletRequest request) {
        return request.getContextPath();
    }

    public String getPath(HttpServletRequest request, String url) {
        return request.getContextPath() + url;
    }

    public String redirectUrl(String url) {
        return "redirect:" + url;
    }

    public static int cpn(Integer pageNo) {
        return pageNo != null && pageNo.intValue() >= 1 ? pageNo.intValue() : 1;
    }

    /**
     * @param response
     * @param fileName 文件名
     * @param columnNames 列名
     * @param keys key集合
     * @param excelList 数据集
     * @Description: 导出Excel
     */
    public void exportExcel(HttpServletResponse response, String fileName, String[] columnNames, String[] keys,
            List<Map<String, Object>> excelList) {
        ByteArrayOutputStream os = null;
        ServletOutputStream out = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        InputStream is = null;
        os = new ByteArrayOutputStream();
        byte[] buff = new byte[2048];
        try {
            ExcelUtil.createWorkBook(excelList, keys, columnNames).write(os);
            is = new ByteArrayInputStream(os.toByteArray());
            // 设置response参数
            response.reset();
            String disposition = "attachment;filename="
                    + URLEncoder.encode(new StringBuilder().append(fileName).append(".xlsx").toString(), "UTF-8");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-disposition", disposition);
            out = response.getOutputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length)))
                bos.write(buff, 0, bytesRead);
            out.flush();
        } catch (Exception e) {
            WayLogger.error(e, "exportExcel异常");
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
                if (null != bis) {
                    bis.close();
                }
                if (null != bos) {
                    bos.close();
                }
                buff = null;
            } catch (IOException e) {
                WayLogger.error(e, "exportExcel异常");
            }
        }
    }

    /**
     * 获取分页 page 对象
     *
     * @param request
     * @return
     */
    public BasePage newPage(HttpServletRequest request) {
        // ==获取分页信息
        return newPage(request, "id", "desc");
    }

    /**
     * 指定默认的排序字段和方法 返回page分页对象
     *
     * @param request
     * @param defaultField
     * @param defaultOrder
     * @return
     */
    public BasePage newPage(HttpServletRequest request, String defaultField, String defaultOrder) {
        // ==获取分页信息

        String str_pageIndex = request.getParameter("pageIndex");
        String str_pageSize = request.getParameter("pageSize");
        int pageIndex = NumberUtils.toInt(str_pageIndex, 1);
        int pageSize = NumberUtils.toInt(str_pageSize, 10);
        String sortOrder = request.getParameter("sortOrder");
        String sortField = request.getParameter("sortField");

        if (StringUtils.isBlank(sortOrder)) {
            sortOrder = defaultOrder;
        }
        if (StringUtils.isBlank(sortField)) {
            sortField = defaultField;
        }

        if (StringUtils.isBlank(sortField)) {
            sortField = "id";
        }
        if (StringUtils.isBlank(sortOrder)) {
            sortOrder = "desc";
        }

        BasePage basePage = new BasePage(pageIndex, pageSize);
        basePage.setSortField(sortField);
        basePage.setSortOrder(sortOrder);
        return basePage;
    }
}
