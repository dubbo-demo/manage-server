package com.myph.manage.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * 员工信息DTO
 *
 * @author dell
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class BasePrintPo implements Serializable {

    private static final long serialVersionUID = 1449897942532531588L;
    private String modelName;
}
