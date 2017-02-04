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

import javax.servlet.http.HttpServletRequest;

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
import com.myph.common.util.SensitiveInfoUtils;
import com.myph.constant.FlowStateEnum;
import com.myph.flow.dto.ContinueActionDto;
import com.myph.loan.dto.LoanedInfoDto;
import com.myph.loan.param.QueryListParam;
import com.myph.loan.service.LoanedService;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.facadeService.FacadeFlowStateExchangeService;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.product.service.ProductService;

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
public class LoanController {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ProductService proService;

    @Autowired
    private LoanedService loanService;

    @Autowired
    FacadeFlowStateExchangeService facadeFlowStateExchangeService;

    @Autowired
    ApplyInfoService applyInfoService;

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
        Long[] ids = new Long[idsStr.length];
        for (int i = 0; i < ids.length; i++) {
            Long id = Long.parseLong(idsStr[i]);
            //更新内容信息
            loanService.update(ShiroUtils.getCurrentUserName(),ShiroUtils.getCurrentUserId(),id);
            ids[i] = id;
        }
        //更新状态
        ServiceResult<Integer> result = loanService.loaned(ids);
        MyphLogger.info("操作人ID【"+ShiroUtils.getCurrentUserId()+"】操作人【"+ShiroUtils.getCurrentUserName()+"】 放款数据修改成功！param【"+ids+"】");
        return AjaxResult.success(result.getData());
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

}
