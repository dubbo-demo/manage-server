package com.myph.manage.common.util;

import org.apache.poi.ss.usermodel.Row;

/**
 * 
 * @Title:
 * @Description:Excel列转为对象
 * @Author:wanghaib
 * @Since:2016年8月27日
 * @Version:1.1.0
 */
public interface ExcelRowToObj<T> {
    public T excelRowToObj(Row row);
}
