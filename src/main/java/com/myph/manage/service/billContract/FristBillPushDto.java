package com.myph.manage.service.billContract;

import com.myph.common.bean.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
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
public class FristBillPushDto extends BaseInfo {

    private static final long serialVersionUID = -3879576886370386474L;

    private String channelCode;// 渠道:麦芽贷：myd；麦芽普惠：myph
    private String contractNo;// 合同号
    private int term;//	期数
    private String billingNo;//	账单编号
    private String repayDate;//	还款日期:格式yyyy-MM-dd HH:mm:ss
    private BigDecimal startPrinBalance;// 期初本金余额
    private String dueFromDate;// 应还日期:格式yyyy-MM-dd HH:mm:ss
    private BigDecimal dueFromPrin;// 应还本金
    private BigDecimal dueFromItr;// 应还利息
    private BigDecimal dueFromAmt;// 当期应还
    private BigDecimal endPrinBalance;// 期末本金余额
    private BigDecimal clrRetServiceAmt;// 结清返还服务费
    private BigDecimal advanceClrAmt;// 提前结清金额
    private BigDecimal interst;// 罚息
    private BigDecimal lateFee;// 滞纳金
    private BigDecimal fee; // 手续费
    private String repayMode;// 还款方式
    private BigDecimal RepayAmt;// 还款金额
    private BigDecimal paidAmt;// 已还金额
    private BigDecimal restDueFrom;// 剩余应还
    private String pushType;//	推送类型:0逾期，1罚息变更，2代扣，3减免，4合并账单，5还款
    private int overDueDays;// 逾期天数
    private String status;// 状态:1逾期，2还清

    /*
    申请单信息，合同信息
     */
    private String areaId; //大区
    private String storeId; //门店
    private String bmId; //业务经理
    private String bmName; //业务经理名称
    private String productId; //产品Id
    private String productName; //产品名称
    private int totalTerm; //产品总期数

    private BigDecimal contractAmt; //合同金额
    private BigDecimal lendAmt; //借款金额
    private BigDecimal purePrincipal; //纯本金
    private BigDecimal serviceAmt; //服务费
    private String loanDate; //放款时间
    private String repayBeginDate; //还款起始日
    private String repayEndDate; //还款截止日

    /*
   会员信息
     */
    private String userName; //姓名
    private String sex; //性别
    private String national; //民族
    private String idNum; //身份证
    private String age; //年龄
    private String education; //学历
    private String mailAddr; //邮寄地址
    private String email; //电子邮箱
    private String mobile; //手机号
    private String weixin; //微信
    private String qq; //QQ号码
    private String registerAddr; //户籍地址
    private String registerDetailAddr; //详细户籍地址

    /*
     * 现住址
     * 现地址	addr
     * 详细地址	detailAddr
     */
    private List<Map<String, String>> addrList;

    /*
     工作信息
      */
    private String unitName; //工作单位
    private String unitAddr; //单位地址
    private String unitDetailAddr; //详细单位地址
    private String unitTel; //单位电话
    private String unitExtendTel; //单位分机号
    private String industryType; //行业类别
    private String unitProperty; //单位性质
    private String post; //担任职务
    private String payDate; //每月发薪日
    private String salary; //月基本工资

    /*
     * @title 联系人
     * 联系人关系	relation
     * 姓名	name
     * 手机号码	telNum
     * 现住址	addr
     * 详细地址	destilAddr
     */
    private List<Map<String, String>> linkmanList;

}
