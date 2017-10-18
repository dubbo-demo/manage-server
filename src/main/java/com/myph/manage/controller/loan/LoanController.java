/**   
 * @Title: ApproveController.java 
 * @Package: com.myph.manage.controller.approve 
 * @company: 麦芽金服
 * @Description: 审批管理(用一句话描述该文件做什么) 
 * @author 罗荣   
 * @date 2016年9月18日 下午5:20:53 
 * @version V1.0   
 */
package com.myph.manage.controller.loan;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.myph.apply.service.ApplyInfoService;
import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.DateUtils;
import com.myph.common.util.SensitiveInfoUtils;
import com.myph.constant.FlowStateEnum;
import com.myph.flow.dto.ContinueActionDto;
import com.myph.loan.dto.LoanedInfoDto;
import com.myph.loan.param.QueryListParam;
import com.myph.loan.service.LoanedService;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.common.util.BeanUtils;
import com.myph.manage.controller.BaseController;
import com.myph.manage.facadeService.FacadeFlowStateExchangeService;
import com.myph.member.base.dto.MemberVerifyDto;
import com.myph.member.feedback.dto.FeedBackDto;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.performance.service.FinanceManageService;
import com.myph.product.service.ProductService;
import com.myph.repaymentPlan.service.JkRepaymentPlanService;

/**
 * 
 * @ClassName: LoanController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 罗荣 
 * @date 2016年11月14日 下午5:30:23 
 *
 */
@Controller
@RequestMapping("/loan")
public class LoanController extends BaseController{
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ProductService proService;

    @Autowired
    private LoanedService loanService;

    @Autowired
    FacadeFlowStateExchangeService facadeFlowStateExchangeService;
    
    @Autowired
    private FinanceManageService financeManageService;

    @Autowired
    ApplyInfoService applyInfoService;
    
    @Autowired
    JkRepaymentPlanService jkRepaymentPlanService;

    public final static String PATH = "/loan";

    public final static String error = "error/500";

    @RequestMapping("/queryPageList")
    public String queryPageList(QueryListParam param, BasePage page, Model model) {
        MyphLogger.info("列表页参数【{}】",param);
        if (null == param.getStatus()) {
            param.setStatus(Constants.NO_INT);
        }
        if (Constants.UNSELECT_LONG.equals(param.getProductType())) {
            param.setProductType(null);
        }
        if (Constants.UNSELECT_LONG.equals(param.getStoreId())) {
            param.setStoreId(null);
        }
        if (Constants.UNSELECT_LONG.equals(param.getProId())) {
            param.setProId(null);
        }
        if (Constants.UNSELECT_LONG.equals(param.getAreaId())) {
            param.setAreaId(null);
        }
        ServiceResult<Pagination<LoanedInfoDto>> rs = loanService.queryPageList(param, page);
        for (LoanedInfoDto e : rs.getData().getResult()) {
            // 1、补充门店名称
            ServiceResult<OrganizationDto> storeResult = organizationService.selectOrganizationById(e.getStoreId());
            if(null != storeResult.getData()){
                e.setStoreName(storeResult.getData().getOrgName());
            }
            // 2、补充大区名称
            ServiceResult<OrganizationDto> areaResult = organizationService.selectOrganizationById(e.getAreaId());
            if(null != areaResult.getData()){
                e.setAreaName(areaResult.getData().getOrgName());
            }
            // 3、补充产品名称
            ServiceResult<String> result = proService.getProductNameById(e.getProductType());
            e.setProductName(result.getData());
            e.setPhone(SensitiveInfoUtils.maskMobilePhone(e.getPhone()));// 隐藏手机号
            e.setIdCard(SensitiveInfoUtils.maskIdCard(e.getIdCard()));// 隐藏身份证
        }
        model.addAttribute("params", param);
        model.addAttribute("page", rs.getData());

        return PATH + "/list_" + param.getStatus();
    }

    @RequestMapping("/loaned")
    @ResponseBody
    public AjaxResult loaned(Model model, HttpServletRequest request) {
        String[] idsStr = request.getParameterValues("ids[]");
        String[] applyLoanNos = request.getParameterValues("applyLoanNos[]");
        for (String applyLoanNo : applyLoanNos) {
            LoanedInfoDto param = new LoanedInfoDto();
            param.setCreateUser(ShiroUtils.getCurrentUserName());
            param.setCreateUserId(ShiroUtils.getCurrentUserId());
            param.setApplyLoanNo(applyLoanNo);
            param.setStatus(Constants.YES_INT);
            //更新放款记录表
            loanService.update(param);
            //更新账单表为有效
            jkRepaymentPlanService.updateIsEffectiveByApplyLoanNo(applyLoanNo);
            // 调用状态机进入主流程
            ContinueActionDto applyNotifyDto = new ContinueActionDto();
            applyNotifyDto.setApplyLoanNo(applyLoanNo);
            applyNotifyDto.setOperateUser(ShiroUtils.getCurrentUserName());
            applyNotifyDto.setOperateUserId(ShiroUtils.getCurrentUserId());
            applyNotifyDto.setFlowStateEnum(FlowStateEnum.FINANCE);
            // 走状态机更新主流程
            ServiceResult serviceResult = facadeFlowStateExchangeService.doAction(applyNotifyDto);
            if (!serviceResult.success()) {
                MyphLogger.error("MQ-CALLBACK调用更新主流程失败！param【" + applyNotifyDto + "】,MESSAGE:{}",
                        serviceResult.getMessage());
            }else{
                MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 放款成功！【"+applyNotifyDto+"】");
            }
        }
        
        MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 放款数据修改成功！param【"+idsStr+"】");
        return AjaxResult.success();
    }

    @RequestMapping("/loanedDetail")
    public String loanedDetail(String applyLoanNo, Model model, HttpServletRequest request) {
        ServiceResult<LoanedInfoDto> loanInfo = loanService.findDetail(applyLoanNo);
        if (loanInfo.success()) {
            // 1、补充门店名称
            ServiceResult<OrganizationDto> storeResult = organizationService
                    .selectOrganizationById(loanInfo.getData().getStoreId());
            if(null != storeResult.getData()){
                loanInfo.getData().setStoreName(storeResult.getData().getOrgName());
            }
            // 2、补充大区名称
            ServiceResult<OrganizationDto> areaResult = organizationService
                    .selectOrganizationById(loanInfo.getData().getAreaId());
            if(null != areaResult.getData()){
                loanInfo.getData().setAreaName(areaResult.getData().getOrgName());
            }
            // 3、补充产品名称
            ServiceResult<String> result = proService.getProductNameById(loanInfo.getData().getProductType());
            loanInfo.getData().setProductName(result.getData());
            model.addAttribute("loanInfo", loanInfo.getData());
        } else {
            model.addAttribute("message", loanInfo.getMessage());
            return error;
        }
        return "/apply/progress/loaned_detail";
    }

    
    @RequestMapping("/exportFinanceInfo")
    public void exportFinanceInfo( HttpServletResponse response,com.myph.performance.param.QueryListParam param) {
        MyphLogger.debug("放款管理导出：/loan/exportFinanceInfo.htm|param=" + param);
        if (null == param.getStatus()) {
            param.setStatus(Constants.NO_INT);
        }
        if (Constants.UNSELECT_LONG.equals(param.getProductType())) {
            param.setProductType(null);
        }
        if (Constants.UNSELECT_LONG.equals(param.getStoreId())) {
            param.setStoreId(null);
        }
        if (Constants.UNSELECT_LONG.equals(param.getProId())) {
            param.setProId(null);
        }
        if (Constants.UNSELECT_LONG.equals(param.getAreaId())) {
            param.setAreaId(null);
        }
        try {
            // 设置参数查询满足条件的所有数据不分页
            List<com.myph.performance.dto.LoanedInfoDto> list = financeManageService.queryFinanceInfo(param).getData();
            String columnNames[] = {"签约日期", "大区", "门店","客服", "客户", "团队经理", "客户经理", "手机号码", "身份证号", "合同号",
                    "申请单号", "银行卡号", "开户行", "产品名称", "借款金额","借款期限", "借款利率",
                    "还款开始时间", "还款结束时间", "服务费", "实际打款金额", "总利息", "每期应收", "首期应收" };// 列名
            String keys[] = { "signTime","areaName","storeName","customerSName","memberName","leaderName","bmName","phone","idCard","contractNo",
                    "applyLoanNo","bankCardNo","bankName","productName","contractAmount","loanPeriods","totalRate",
                    "beginTime","endTime","serviceRate","repayMoney","interestAmount","everyReapyAmount","firstReapyAmount" };
            String fileName = "放款管理" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
            // 获取Excel数据
            List<Map<String, Object>> excelList = getExcelMapList(list);
            // 导出Excel数据
            exportExcel(response, fileName, columnNames, keys, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[结束放款管理导出：/loan/exportFinanceInfo.htm]");
        }
        MyphLogger.debug("结束放款管理导出：/loan/exportFinanceInfo.htm");
    }
    
    /**
     * 获取Excel数据
     * 
     * @param list
     * @return
     */
    private List<Map<String, Object>> getExcelMapList(List<com.myph.performance.dto.LoanedInfoDto> list) {
        List<Map<String, Object>> destList = new ArrayList<Map<String, Object>>();
        if (null == list) {
            return destList;
        }
        Map<String, Object> destMap = null;
        for (com.myph.performance.dto.LoanedInfoDto dto : list) {
            destMap = BeanUtils.transBeanToMap(dto);
            Date beginTime = dto.getBeginTime();
            if (null != beginTime) {
                SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_PATTERN);
                destMap.put("beginTime", sdf.format(beginTime));
            }
            Date endTime = dto.getEndTime();
            if (null != endTime) {
                SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_PATTERN);
                destMap.put("endTime", sdf.format(endTime));
            }
            destList.add(destMap);
        }
        return destList;
    }
}
