package com.myph.manage.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @Title:
 * @Description:Excel工具类
 * @Author:wanghaib
 * @Since:2016年8月27日
 * @Version:1.1.0
 */
public class ExcelUtil {
    private static Logger log = LoggerFactory.getLogger(ExcelUtil.class);
    private static final int SHEET_MAXNUMBER = 3000;
    // 格式化日期
    public static final String DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";

    public static List<Map<String, String>> readExcel(File templetFile, int startrow, int startcol, int sheetnum) {
        List<Map<String, String>> varList = new ArrayList<Map<String, String>>();
        if (templetFile != null) {
            String ofn = templetFile.getName();// 文件名
            String extName = ""; // 扩展名格式：
            if (ofn.lastIndexOf(".") >= 0) {
                extName = ofn.substring(ofn.lastIndexOf("."));
            }
            if (".xls".equals(extName.toLowerCase())) {
                varList = readExcelByXls(templetFile, startrow, startcol, sheetnum);
            } else if (".xlsx".equals(extName.toLowerCase())) {
                varList = readExcelByXlsx(templetFile, startrow, startcol, sheetnum);
            }
        }
        return varList;
    }

    /**
     * 操作Excel2003以前（包括2003）的版本,扩展名是.xls
     * 
     * @param templetFile 文件
     * @param startrow 开始行号
     * @param startcol 开始列号
     * @param sheetnum sheet
     * @return list
     */
    public static List<Map<String, String>> readExcelByXls(File file, int startrow, int startcol, int sheetnum) {
        List<Map<String, String>> varList = new ArrayList<Map<String, String>>();

        try {

            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
            HSSFSheet sheet = wb.getSheetAt(sheetnum); // sheet 从0开始
            int rowNum = sheet.getLastRowNum() + 1; // 取得最后一行的行号
            if (rowNum == 1) {
                return varList;
            }

            Row title = sheet.getRow(0);
            // 得到第一行的所有列
            Iterator<Cell> cellTitle = title.cellIterator();
            // 将标题的文字内容放入到一个map中。
            Map<Integer, String> titlemap = new HashMap<Integer, String>();
            // 从标题第一列开始
            int index = 0;
            // 循环标题所有的列
            while (cellTitle.hasNext()) {
                Cell cell = cellTitle.next();
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                String value = cell.getStringCellValue();
                // 还是把表头trim一下
                value = value.trim();
                titlemap.put(index, value);
                index = index + 1;
            }

            for (int i = startrow + 1; i < rowNum; i++) { // 行循环开始

                Map<String, String> varpd = new HashMap<String, String>();
                HSSFRow row = sheet.getRow(i); // 行
                int cellNum = row.getLastCellNum(); // 每行的最后一个单元格位置

                for (int j = startcol; j < cellNum; j++) { // 列循环开始

                    HSSFCell cell = row.getCell(j);
                    String cellValue = null;
                    if (null != cell) {
                        switch (cell.getCellType()) { // 判断excel单元格内容的格式，并对其进行转换，以便插入数据库
                            case 0:
                                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    cellValue = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()))
                                            .toString();
                                } else {
                                    cellValue = String.valueOf(cell.getNumericCellValue());
                                }
                                break;
                            case 1:
                                cellValue = cell.getStringCellValue();
                                break;
                            case 2:
                                cellValue = String.valueOf(cell.getDateCellValue());
                                break;
                            case 3:
                                cellValue = "";
                                break;
                            case 4:
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case 5:
                                cellValue = String.valueOf(cell.getErrorCellValue());
                                break;
                            default:
                                break;
                        }
                    } else {
                        cellValue = "";
                    }

                    varpd.put(titlemap.get(j), cellValue);

                }
                varList.add(varpd);
            }

        } catch (Exception e) {
            log.error("readExcelByXls异常", e);
        }

        return varList;
    }

    /**
     * 是操作Excel2007的版本，扩展名是.xlsx
     * 
     * @param templetFile 文件
     * @param startrow 开始行号
     * @param startcol 开始列号
     * @param sheetnum sheet
     * @return list
     */
    public static List<Map<String, String>> readExcelByXlsx(File file, int startrow, int startcol, int sheetnum) {
        List<Map<String, String>> varList = new ArrayList<Map<String, String>>();

        try {
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = wb.getSheetAt(sheetnum); // sheet 从0开始
            int rowNum = sheet.getLastRowNum() + 1; // 取得最后一行的行号

            if (rowNum == 1) {
                return varList;
            }

            Row title = sheet.getRow(0);
            // 得到第一行的所有列
            Iterator<Cell> cellTitle = title.cellIterator();
            // 将标题的文字内容放入到一个map中。
            Map<Integer, String> titlemap = new HashMap<Integer, String>();
            // 从标题第一列开始
            int index = 0;
            // 循环标题所有的列
            while (cellTitle.hasNext()) {
                Cell cell = cellTitle.next();
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                String value = cell.getStringCellValue();
                // 还是把表头trim一下
                value = value.trim();
                titlemap.put(index, value);
                index = index + 1;
            }

            for (int i = startrow; i < rowNum; i++) { // 行循环开始

                Map<String, String> varpd = new HashMap<String, String>();
                XSSFRow row = sheet.getRow(i); // 行
                int cellNum = row.getLastCellNum(); // 每行的最后一个单元格位置

                for (int j = startcol; j < cellNum; j++) { // 列循环开始

                    XSSFCell cell = row.getCell(j);
                    String cellValue = null;
                    if (null != cell) {
                        switch (cell.getCellType()) { // 判断excel单元格内容的格式，并对其进行转换，以便插入数据库
                            case 0:
                                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    cellValue = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()))
                                            .toString();
                                } else {
                                    cellValue = String.valueOf(cell.getNumericCellValue());
                                }
                                break;
                            case 1:
                                cellValue = cell.getStringCellValue();
                                break;
                            case 2:
                                cellValue = cell.getStringCellValue();
                                break;
                            case 3:
                                cellValue = "";
                                break;
                            case 4:
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case 5:
                                cellValue = String.valueOf(cell.getErrorCellValue());
                                break;
                            default:
                                break;
                        }
                    } else {
                        cellValue = "";
                    }
                    varpd.put(titlemap.get(j), cellValue);

                }
                varList.add(varpd);
            }

        } catch (Exception e) {
            log.error(e.toString());
        }

        return varList;
    }

    public static void main(String[] args) throws Exception {
        /*
         * File file = new File("E:/黑名单temp.xls"); List<Map<String, String>> coll = readExcel(file, 0, 0, 0);
         * System.err.println(coll.toString()); List<InnerBlackDto> list = readExcel(file, new ExcelRowToInnerBlack());
         * System.err.println(list.toString());
         */
        System.err.println(1 % 2000 + ":" + 0 % 2000);
    }

    /**
     * 创建excel文档，
     * 
     * @param list 数据
     * @param keys list中map的key数组集合
     * @param columnNames excel的列名
     * */
    public static Workbook createWorkBook(List<Map<String, Object>> list, String[] keys, String columnNames[]) {
        // 创建excel工作簿
        // Workbook wb = new HSSFWorkbook();
        XSSFWorkbook wb = new XSSFWorkbook();
        int size = list.size();
        // sheet页最大行数大小
        int sheetSize = SHEET_MAXNUMBER;
        // 计算需要多少sheet页
        int c = size % sheetSize == 0 ? size / sheetSize : size / sheetSize + 1;
        if (c == 1 || c == 0) {
            createSheet(list, columnNames, wb, keys, c);
        } else {
            List<Map<String, Object>> list1 = null;
            for (int n = 1; n < c + 1; n++) {
                if (n == 1) {
                    list1 = list.subList(1, n * sheetSize);
                } else if (n * sheetSize < size) {
                    list1 = list.subList(sheetSize * (n - 1), n * sheetSize);
                } else {
                    list1 = list.subList(sheetSize * (n - 1), size);
                }
                createSheet(list1, columnNames, wb, keys, n);
                list1 = null;
            }
        }
        return wb;
    }

    /**
     * 创建sheet页，并写入数据
     * 
     * @param list 数据源list
     * @param columnNames 列名
     * @param wb 创建的excel
     * @param keys 对应list中的key值
     * @param n 需要的sheet页
     */
    public static void createSheet(List<Map<String, Object>> list, String columnNames[], XSSFWorkbook wb,
            String[] keys, int n) {
        // 创建第一个sheet（页），并命名
        XSSFSheet sheet = wb.createSheet("sheet" + n);
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for (int i = 0; i < keys.length; i++) {
            sheet.setColumnWidth((short) i, (short) (35.7 * 150));
        }
        // 创建第一行
        Row row = sheet.createRow((short) 0);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

        // Font f3=wb.createFont();
        // f3.setFontHeightInPoints((short) 10);
        // f3.setColor(IndexedColors.RED.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        // 设置列名
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        for (short i = 1; i < list.size() + 1; i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow((short) i);
            // 在row行上创建一个方格
            for (short j = 0; j < keys.length; j++) {
                Cell cell = row1.createCell(j);
                // 写入对应列的值
                // cell.setCellValue(list.get(i - 1).get(keys[j]) == null ? " " : list.get(i -
                // 1).get(keys[j]).toString());
                cell.setCellValue(getValue(list.get(i - 1).get(keys[j])));
                cell.setCellStyle(cs2);
            }
        }
    }

    public static String getValue(Object value) {
        String textValue = "";
        if (value == null)
            return textValue;

        if (value instanceof Boolean) {
            boolean bValue = (Boolean) value;
            textValue = "是";
            if (!bValue) {
                textValue = "否";
            }
        } else if (value instanceof Date) {
            Date date = (Date) value;
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING);
            textValue = sdf.format(date);
        } else
            textValue = value.toString();

        return textValue;
    }

    public static String getCellValue(Cell cell) {
        Object value = null;
        if (null == cell) {
            return null;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING);
                    value = sdf.format(cell.getDateCellValue());
                } else {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    value = cell.getStringCellValue();
                }
                break;
            default:
                value = cell.getStringCellValue();
                break;
        }
        if (StringUtils.isNotBlank(String.valueOf(value))) {
            return String.valueOf(value);
        } else {
            return null;
        }

    }

    /**
     * 
     * @param file
     * @param rowToObj
     * @return
     * @Description:Excel转实体类
     */
    public static <T> List<T> readExcel(File file, ExcelRowToObj<T> rowToObj) {
        Workbook wb = null;
        List<T> varList = new ArrayList<T>();
        try {
            if (file != null) {
                String ofn = file.getName();// 文件名
                String extName = ""; // 扩展名格式：
                if (ofn.lastIndexOf(".") >= 0) {
                    extName = ofn.substring(ofn.lastIndexOf("."));
                }
                if (".xls".equals(extName.toLowerCase())) {
                    wb = new HSSFWorkbook(new FileInputStream(file));
                } else if (".xlsx".equals(extName.toLowerCase())) {
                    wb = new XSSFWorkbook(new FileInputStream(file));
                }
            }
            if (null == wb) {
                return varList;
            }
            Sheet sheet = wb.getSheetAt(0); // sheet 从0开始
            int rowNum = sheet.getLastRowNum() + 1; // 取得最后一行的行号
            if (rowNum == 1) {
                return varList;
            }
            for (int i = 1; i < rowNum; i++) { // 行循环开始
                if (null == sheet.getRow(i)) {
                    continue;
                }
                T t = (T) rowToObj.excelRowToObj(sheet.getRow(i));
                varList.add(t);
            }

        } catch (Exception e) {
            log.error("异常[readExcel]", e);
        }
        return varList;
    }
}
