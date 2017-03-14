package com.myph.manage.service.billContract.dto;

import com.myph.common.bean.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author heyx
 * @version V1.0
 * @Title: BillPushDto
 * @Package: com.myph.manage.service.billContract
 * @company: 麦芽金服
 * @Description: TODO (用一句话描述该文件做什么)
 * @date 2017/3/6
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ContractRequestVo extends BaseInfo {

    private static final long serialVersionUID = -3879576886370386474L;

    private String channelCode;

    private String contractNo;

    private String areaId;

    private String shopId;
    /**
     * 业务经理id
     */
    private String bmId;
    /**
     * 业务经理名称
     */
    private String bmName;
    /**
     * 产品Id
     */
    private String productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品总期数
     */
    private Integer totalTerm;
    /**
     * 合同金额
     */
    private BigDecimal contractAmt;
    /**
     * 借款金额
     */
    private BigDecimal lendAmt;
    /**
     * 纯本金
     */
    private BigDecimal purePrincipal;
    /**
     * 服务费
     */
    private BigDecimal serviceAmt;
    /**
     * 放款时间
     */
    private String loanDate;
    /**
     * 还款起始日
     */
    private String repayBeginDate;
    /**
     * 还款截止日
     */
    private String repayEndDate;
    /**
     * 期数
     */
    private Integer term;
    /**
     * 账单编号
     */
    private String billingNo;
    /**
     * 账单id
     */
    private String billId;
    /**
     * 还款日期
     */
    private String repayDate;
    /**
     * 期初本金余额
     */
    private BigDecimal startPrinBalance;
    /**
     * 应还日期
     */
    private String dueFromDate;
    /**
     * 应还本金
     */
    private BigDecimal dueFromPrin;
    /**
     * 应还利息
     */
    private BigDecimal dueFromItr;
    /**
     * 应还金额
     */
    private BigDecimal dueFromAmt;
    /**
     * 期末本金余额
     */
    private BigDecimal endPrinBalance;
    /**
     * 结清返还服务费
     */
    private BigDecimal clrRetServiceAmt;
    /**
     * 提前结清金额
     */
    private BigDecimal advanceClrAmt;
    /**
     * 罚息
     */
    private BigDecimal interst;
    /**
     * 滞纳金
     */
    private BigDecimal lateFees;
    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 已还金额
     */
    private BigDecimal paidAmt;
    /**
     * 剩余应还
     */
    private BigDecimal restDueFrom;
    /**
     * 逾期天数
     */
    private Integer overDueDays;
    /**
     * 推送类型
     */
    private String pushType;
    /**
     * 状态
     */
    private String status;
    private String userId;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 性别
     */
    private String sex;
    /**
     * 民族
     */
    private String national;
    /**
     * 身份证
     */
    private String idNum;
    /**
     * 年龄
     */
    private String age;
    /**
     * 学历
     */
    private String education;
    /**
     * 邮寄地址
     */
    private String mailAddr;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * qq
     */
    private String qq;
    /**
     * 微信
     */
    private String weixin;
    /**
     * 户籍地址
     */
    private String registerAddr;
    /**
     * 详细户籍地址
     */
    private String registerDetailAddr;
    /**
     * 工作单位
     */
    private String unitName;
    /**
     * 单位地址
     */
    private String unitAddr;
    /**
     * 详细单位地址
     */
    private String unitDetailAddr;
    /**
     * 单位电话
     */
    private String unitTel;
    /**
     * 单位分机号
     */
    private String unitExtendTel;
    /**
     * 行业类别
     */
    private String industryType;
    /**
     * 单位性质
     */
    private String unitProperty;
    /**
     * 担任职务
     */
    private String post;
    /**
     * 每月发薪日
     */
    private String payDate;
    /**
     * 月基本工资
     */
    private String salary;

    /*
     * 现住址
     * 现地址	addr
     * 详细地址	detailAddr
     */
    private List<AddrDto> addrList;

    /*
     * @title 联系人
     * 联系人关系	relation
     * 姓名	name
     * 手机号码	telNum
     * 现住址	addr
     * 详细地址	destilAddr
     */
    private List<LinkmanDto> linkmanList;

    /*
     *设备号
     */
    private List<DeviceMatchDto> deviceList = new ArrayList<DeviceMatchDto>();

    /*
     * 备用号码
     */
    private List<String> standbyNumList = new ArrayList<String>();

}
