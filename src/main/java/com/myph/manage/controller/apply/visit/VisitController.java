package com.myph.manage.controller.apply.visit;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.apply.dto.ApplyInfoDto;
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
import com.myph.constant.bis.ExternalFirstBisStateEnum;
import com.myph.constant.bis.ExternalLastBisStateEnum;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.flow.dto.ContinueActionDto;
import com.myph.flow.dto.RejectActionDto;
import com.myph.manage.common.constant.Constant;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.common.shiro.dto.EmpDetailDto;
import com.myph.manage.controller.BaseController;
import com.myph.manage.facadeService.FacadeFlowStateExchangeService;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.visit.dto.VisitDetailDto;
import com.myph.visit.dto.VisitDto;
import com.myph.visit.dto.VisitQueryDto;
import com.myph.visit.service.VisitService;

/**
 * 
 * @ClassName: VisitController
 * @Description: 外访管理
 * @author 王海波
 * @date 2016年9月8日 上午9:24:48
 * 
 */
@Controller
@RequestMapping("/visit")
public class VisitController extends BaseController {

    @Autowired
    private VisitService visitService;
    @Autowired
    private ApplyInfoService applyInfoService;
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EmployeeInfoService employeeInfoService;

    @Autowired
    private FacadeFlowStateExchangeService facadeFlowStateExchangeService;

    /**
     * 
     * @名称 list
     * @描述 外访管理-门店副理
     * @返回类型 String
     * @日期 2016年9月9日 上午10:46:51
     * @创建人 王海波
     * @更新人 王海波
     * 
     */
    @RequestMapping("/list")
    public String list(Model model, VisitQueryDto queryDto, BasePage basePage) {
        MyphLogger.debug("开始外访管理查询：/visit/list.htm|querDto=" + queryDto + "|basePage=" + basePage);
        // 默认待办
        if (null == queryDto.getProgress()) {
            queryDto.setProgress(Constants.TASK_TODO);
        }
        EmployeeInfoDto user = ShiroUtils.getCurrentUser();
        EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
        Long userStoreId = empDetail.getStoreId();
        String userStoreName = "";
        if (null != user) {
            OrganizationDto tempOrg = organizationService.selectOrganizationById(userStoreId).getData();
            if (null != tempOrg) {
                queryDto.setStoreId(userStoreId);
                userStoreName = tempOrg.getOrgName();
                model.addAttribute("storeName", userStoreName);
            }
        }

        queryDto.setIsManage(empDetail.getIsManage());
        // 作业岗只能操作自己的
        if (Constants.NOT_MANAGE == empDetail.getIsManage()) {
            queryDto.setCustomerServiceId(user.getId());
            queryDto.setCustomerServiceName(user.getEmployeeName());
        }
        initQueryDate(queryDto);
        ServiceResult<Pagination<VisitDto>> pageResult = visitService.listPageInfos(queryDto, basePage);

        List<VisitDto> lists = pageResult.getData().getResult();
        for (VisitDto visit : lists) {
            Long storeId = visit.getStoreId();
            Long visitUser = visit.getVisitUser();
            // 根据组织id查询名称
            if (null != storeId) {
                if (storeId.equals(userStoreId)) {
                    visit.setStoreName(userStoreName);
                } else {
                    ServiceResult<OrganizationDto> tempOrgResult = organizationService.selectOrganizationById(storeId);
                    OrganizationDto tempOrg = tempOrgResult.getData();
                    if (null != tempOrg) {
                        visit.setStoreName(tempOrg.getOrgName());
                    }
                }

            }
            // 根据员工id查询名称
            if (null != visitUser) {
                ServiceResult<EmployeeInfoDto> tempEmployeeResult = employeeInfoService.getEntityById(visitUser);
                EmployeeInfoDto tempEmployee = tempEmployeeResult.getData();
                if (null != tempEmployee) {
                    visit.setVisitUserName(tempEmployee.getEmployeeName());
                }
            }
            visit.setPhone(SensitiveInfoUtils.maskMobilePhone(visit.getPhone()));// 隐藏手机号
            visit.setMemberName(SensitiveInfoUtils.maskUserName(visit.getMemberName()));// 隐藏姓名
        }

        model.addAttribute("page", pageResult.getData());
        model.addAttribute("queryDto", queryDto);
        model.addAttribute("progress", queryDto.getProgress());
        model.addAttribute("isManage", queryDto.getIsManage());
        MyphLogger.debug("结束外访管理查询：/visit/list.htm|page=" + pageResult);
        return "/apply/visit/visit_list";
    }

    /**
     * 
     * @名称 allotUI
     * @描述 外访分配页面
     * @返回类型 String
     * @日期 2016年9月12日 上午11:00:17
     * @创建人 王海波
     * @更新人 王海波
     * 
     */
    @RequestMapping("/allotUI")
    public String allotUI(Model model, Long id) {
        MyphLogger.info("开始分配外访页面：/visit/allotUI.htm|id=" + id);
        ServiceResult<VisitDetailDto> visit = visitService.get(id);
        model.addAttribute("visit", visit.getData());
        MyphLogger.info("结束分配外访页面：/visit/allotUI.htm|visit=" + visit);
        return "/apply/visit/visit_allot";
    }

    /**
     * 
     * @名称 recordUI
     * @描述 记录外访页面
     * @返回类型 String
     * @日期 2016-9-16 下午5:37:37
     * @创建人 王海波
     * @更新人 王海波
     * 
     */
    @RequestMapping("/recordUI")
    public String recordUI(Model model, Long id) {
        MyphLogger.info("开始记录外访页面：/visit/recordUI.htm|id=" + id);
        ServiceResult<VisitDetailDto> visit = visitService.get(id);
        model.addAttribute("visit", visit.getData());
        MyphLogger.info("结束记录外访页面：/visit/recordUI.htm|visit=" + visit);
        return "/apply/visit/visit_record";
    }

    /**
     * 
     * @名称 record
     * @描述 记录外访结果
     * @返回类型 AjaxResult
     * @日期 2016-9-16 下午6:06:52
     * @创建人 王海波
     * @更新人 王海波
     * 
     */
    @RequestMapping("/record")
    @ResponseBody
    public AjaxResult record(Model model, VisitDetailDto detail) {
        MyphLogger.info("开始记录外访结果：/visit/record.htm|detail=" + detail);
        FlowStateEnum flowEnum = null;
        if (detail.getAuditStage().equals(FlowStateEnum.AUDIT_FIRST.getCode())) {
            flowEnum = FlowStateEnum.EXTERNAL_FIRST;
        } else if (detail.getAuditStage().equals(FlowStateEnum.AUDIT_LASTED.getCode())) {
            flowEnum = FlowStateEnum.EXTERNAL_LAST;
        }
        try {
            ServiceResult<Integer> visit = visitService.recordVisit(detail);
            if (!visit.success()) {
                MyphLogger.error("记录外访结果失败！detail：{},MESSAGE:{}", detail, visit.getMessage());
                return AjaxResult.formatFromServiceResult(visit);
            }
            /*
             * 通知流程中心
             */
            if (ExternalFirstBisStateEnum.PASSED.getSubCode().equals(detail.getState())) {
                ContinueActionDto applyNotifyDto = new ContinueActionDto();
                applyNotifyDto.setApplyLoanNo(detail.getApplyLoanNo());
                applyNotifyDto.setOperateUser(ShiroUtils.getCurrentUserName());
                applyNotifyDto.setFlowStateEnum(flowEnum);
                ServiceResult<Integer> serviceResult = facadeFlowStateExchangeService.doAction(applyNotifyDto);
                if (!serviceResult.success()) {
                    MyphLogger.error("调用更新主流程失败！param【" + applyNotifyDto + "】,MESSAGE:{}", serviceResult.getMessage());
                    return AjaxResult.formatFromServiceResult(serviceResult);
                }
            } else if (ExternalFirstBisStateEnum.REJECT.getSubCode().equals(detail.getState())) {
                RejectActionDto rejectActionDto = new RejectActionDto();
                rejectActionDto.setApplyLoanNo(detail.getApplyLoanNo());
                rejectActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
                rejectActionDto.setRejectDays(Constant.CONFINE_TIME.getCode());
                rejectActionDto.setFlowStateEnum(flowEnum);
                ServiceResult<Integer> serviceResult = facadeFlowStateExchangeService.doAction(rejectActionDto);
                if (!serviceResult.success()) {
                    MyphLogger.error("调用更新主流程失败！param【" + rejectActionDto + "】,MESSAGE:{}", serviceResult.getMessage());
                    return AjaxResult.formatFromServiceResult(serviceResult);
                }
            }
            MyphLogger.info("结束记录外访结果：/visit/record.htm|result=" + visit);
            return AjaxResult.success(visit.getCode());
        } catch (Exception e) {
            MyphLogger.error(e, "异常[记录外访结果] /visit/record.htm|detail=" + detail);
            return AjaxResult.failed("系统异常");
        }
    }

    /**
     * 
     * @名称 print
     * @描述 打印外访单
     * @返回类型 String
     * @日期 2016年9月13日 下午7:13:44
     * @创建人 王海波
     * @更新人 王海波
     * 
     */
    @RequestMapping("/print")
    public String print(Model model, VisitDetailDto detail) {
        MyphLogger.info("开始打印外访单：/visit/print.htm|detail=" + detail);
        VisitDetailDto visit = visitService.get(detail.getId()).getData();
        if (null != detail.getVisitUser()) {
            visit.setVisitUser(detail.getVisitUser());
        }
        if (null != detail.getFinishTime()) {
            visit.setFinishTime(detail.getFinishTime());
        }
        Long visitUser = visit.getVisitUser();
        // 根据员工id查询名称
        if (null != visitUser) {
            EmployeeInfoDto tempEmployee = employeeInfoService.getEntityById(visitUser).getData();
            if (null != tempEmployee) {
                visit.setVisitUserName(tempEmployee.getEmployeeName());
            }
        }
        model.addAttribute("visit", visit);
        MyphLogger.info("结束打印外访单：/visit/print.htm|visit=" + visit);
        return "/apply/visit/visit_print";
    }

    /**
     * 
     * @名称 allot
     * @描述 分配外访
     * @返回类型 String
     * @日期 2016年9月12日 下午4:47:04
     * @创建人 王海波
     * @更新人 王海波
     * 
     */
    @RequestMapping("/allot")
    @ResponseBody
    public AjaxResult allot(Model model, VisitDetailDto detail) {
        MyphLogger.info("开始分配外访单：/visit/allot.htm|detail=" + detail);
        try {
            detail.setAllotUser(ShiroUtils.getCurrentUserId());// 设置分配人
            ServiceResult<Integer> visit = visitService.allotVisit(detail);
            // 更新主表子状态
            ApplyInfoDto applyInfo = applyInfoService.queryInfoByAppNo(detail.getApplyLoanNo()).getData();
            if (FlowStateEnum.EXTERNAL_FIRST.getCode().equals(applyInfo.getState())) {
                // 初审-待外访
                applyInfoService.updateSubState(detail.getApplyLoanNo(), ExternalFirstBisStateEnum.ALLOT.getCode());
            } else if (FlowStateEnum.EXTERNAL_LAST.getCode().equals(applyInfo.getState())) {
                // 终审-带外访
                applyInfoService.updateSubState(detail.getApplyLoanNo(), ExternalLastBisStateEnum.ALLOT.getCode());
            }
            MyphLogger.info("结束分配外放单：/visit/record.htm|result=" + visit);
            return AjaxResult.success(visit.getCode());
        } catch (Exception e) {
            MyphLogger.error(e, "异常[分配外访]/visit/allot.htm|detail=" + detail);
            return AjaxResult.failed("系统异常");
        }
    }

    /**
     * 
     * @名称 initQueryDate
     * @描述 初始化时间查询条件
     * @返回类型 void
     * @日期 2016年9月29日 下午4:24:40
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    private void initQueryDate(VisitQueryDto queryDto) {
        Date today = DateUtils.getToday();
        Date twoWeekBefore = DateUtils.addWeeks(today, -2);
        // 初始化查询外访进件日期
        if (null == queryDto.getApplyTimeStart()) {
            queryDto.setApplyTimeStart(twoWeekBefore);
        }
        if (null == queryDto.getApplyTimeEnd()) {
            queryDto.setApplyTimeEnd(today);
        }
        if (Constants.NOT_MANAGE == queryDto.getIsManage()) {
            // 初始化查询外访分配日期
            if (null == queryDto.getAllotTimeStart()) {
                queryDto.setAllotTimeStart(twoWeekBefore);
            }
            if (null == queryDto.getAllotTimeEnd()) {
                queryDto.setAllotTimeEnd(today);
            }
        }
        String progress = queryDto.getProgress();
        if (Constants.IS_MANAGE == queryDto.getIsManage()) {
            if ("todo".equals(progress)) {
                // 初始化查询外访需求日期
                if (null == queryDto.getCreateTimeStart()) {
                    queryDto.setCreateTimeStart(twoWeekBefore);
                }
                if (null == queryDto.getCreateTimeEnd()) {
                    queryDto.setCreateTimeEnd(today);
                }
            }
        }

    }

}
