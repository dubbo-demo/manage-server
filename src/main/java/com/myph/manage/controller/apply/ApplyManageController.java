/**   
 * @Title: ApplyManageController.java 
 * @Package: com.myph.manage.controller.apply
 * @company: 麦芽金服
 * @Description: TODO
 * @date 2016年9月24日 下午1:35:06 
 * @version V1.0   
 */
package com.myph.manage.controller.apply;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.dto.ApplyManageInfoDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.constant.ApplyUtils;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.bis.ApplyBisStateEnum;
import com.myph.employee.dto.EmployeeDetailDto;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.flow.dto.AbandonActionDto;
import com.myph.flow.dto.RejectActionDto;
import com.myph.manage.common.constant.Constant;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.controller.BaseController;
import com.myph.manage.facadeService.FacadeFlowStateExchangeService;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;

/**
 * 申请件管理
 * 
 * @ClassName: ApplyManageController
 * @Description: TODO
 * @author hf
 * @date 2016年9月24日 下午1:35:06
 * 
 */
@Controller
@RequestMapping("/apply/manage")
public class ApplyManageController extends BaseController {

	@Autowired
	private ApplyInfoService applyInfoService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private EmployeeInfoService employeeInfoService;

	@Autowired
	private FacadeFlowStateExchangeService facadeFlowStateExchangeService;

	/**
	 * 申请单列表
	 * 
	 * @名称 list
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年9月20日 下午9:25:05
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/list")
	public String list(Model model, ApplyManageInfoDto queryDto, BasePage basePage) {
		try {
			initApplyListParams(model, queryDto, basePage);
			initQueryDate(queryDto);
			ServiceResult<Pagination<ApplyManageInfoDto>> result = applyInfoService.listInfo(queryDto, basePage);
			if (result.success() && result.getData() != null) {
				for (ApplyManageInfoDto applyManageInfoDto : result.getData().getResult()) {
					ServiceResult<OrganizationDto> orgDtoResult = organizationService
							.selectOrganizationById(applyManageInfoDto.getStoreId());
					if (orgDtoResult.success() && orgDtoResult.getData() != null) {
						applyManageInfoDto.setStoreName(orgDtoResult.getData().getOrgName());
					}
					applyManageInfoDto.setStateDesc(ApplyUtils.getFullStateDesc(applyManageInfoDto.getState(),
							applyManageInfoDto.getSubState()));
				}
			}
			model.addAttribute("page", result.getData());
			model.addAttribute("queryDto", queryDto);
		} catch (Exception e) {
			MyphLogger.error("申请单列表", e);
		}
		return "/apply/apply_manage";
	}

	/**
	 * 加载申请单列表初始化条件
	 * 
	 * @名称 initApplyListParams
	 * @描述 TODO
	 * @返回类型 void
	 * @日期 2016年11月16日 上午9:54:57
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	private void initApplyListParams(Model model, ApplyManageInfoDto queryDto, BasePage basePage) {
		EmployeeInfoDto user = ShiroUtils.getCurrentUser();
		Long userStoreId = null;
		String userStoreName = "";
		if (null != user) {
			EmployeeDetailDto userInfo = employeeInfoService.queryEmployeeInfo(user.getId()).getData();
			if (null != userInfo) {
				userStoreId = userInfo.getStoreId();
				userStoreName = userInfo.getStoreName();
				//电销客服需求调整 禅道1113，取消门店限制，只跟当前业务员挂钩
				//queryDto.setStoreId(userStoreId);
				model.addAttribute("storeName", userStoreName);
			}
		}
		queryDto.setCustomerServiceId(user.getId()); // 当前客服
		queryDto.setCustomerSName(user.getEmployeeName());// 页面展示
		if (null == basePage.getSortField()) {
			basePage.setSortField("subState desc,applyLoanNo");
			queryDto.setDefaultSort("default");
		}
		List<Integer> subStateList = new ArrayList<Integer>();
		subStateList.add(ApplyBisStateEnum.INIT.getCode());
		subStateList.add(ApplyBisStateEnum.WORKINFO.getCode());
		subStateList.add(ApplyBisStateEnum.PERSON_ASSETS.getCode());
		subStateList.add(ApplyBisStateEnum.COMPOSITE_OPINION.getCode());
		subStateList.add(ApplyBisStateEnum.LINKMAN_INPUT.getCode());
		subStateList.add(ApplyBisStateEnum.BACK_INIT.getCode());
		subStateList.add(ApplyBisStateEnum.FINISH.getCode());
		queryDto.setState(FlowStateEnum.APPLY.getCode());
		queryDto.setSubStateList(StringUtils.join(subStateList, ","));
	}

	/**
	 * 门店拒绝
	 * 
	 * @名称 rollBack
	 * @描述 TODO
	 * @返回类型 AjaxResult
	 * @日期 2016年10月8日 下午2:30:44
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/storeRefuse")
	@ResponseBody
	public AjaxResult storeRefuse(String applyLoanNo, String interiorRemark, Model model) {
		String operatorName = ShiroUtils.getCurrentUserName();
		Long operatorId = ShiroUtils.getCurrentUserId();
		MyphLogger.info("申请单列表门店拒绝, 输入参数,申请单号：{},当前操作人:{},操作人编号:{}", applyLoanNo, operatorName,
				operatorId);
		RejectActionDto rejectActionDto = new RejectActionDto();
		rejectActionDto.setApplyLoanNo(applyLoanNo);
		rejectActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
		rejectActionDto.setRejectDays(Constant.CONFINE_TIME.getCode());
		rejectActionDto.setFlowStateEnum(FlowStateEnum.APPLY);
		ServiceResult<Integer> serviceResult = null;
		// 拒绝说明是否要加
		try {
			ApplyInfoDto applyInfo = new ApplyInfoDto();
			applyInfo.setApplyRemark(interiorRemark);
			applyInfo.setApplyLoanNo(applyLoanNo);
			applyInfoService.updateInfo(applyInfo);
			// 调用 主流程回退
			serviceResult = facadeFlowStateExchangeService.doAction(rejectActionDto);
		} catch (Exception e) {
			MyphLogger.error("申请单管理发起门店拒绝异常", e);
		}
		return AjaxResult.formatFromServiceResult(serviceResult);
	}

	/**
	 * 发起放弃流程
	 * 
	 * @名称 rollBack
	 * @描述 TODO
	 * @返回类型 AjaxResult
	 * @日期 2016年10月8日 下午2:30:44
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/abandon")
	@ResponseBody
	public AjaxResult abandon(String applyLoanNo, String interiorRemark, Model model) {
		String operatorName = ShiroUtils.getCurrentUserName();
		Long operatorId = ShiroUtils.getCurrentUserId();
		MyphLogger.info("申请单列表门店放弃,输入参数,申请单号：{},当前操作人:{},操作人编号:{}", applyLoanNo, operatorName,
				operatorId);
		AbandonActionDto abandonActionDto = new AbandonActionDto();
		abandonActionDto.setApplyLoanNo(applyLoanNo);
		abandonActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
		abandonActionDto.setFlowStateEnum(FlowStateEnum.APPLY);
		ServiceResult<Integer> serviceResult = null;
		// 拒绝说明是否要加
		try {
			ApplyInfoDto applyInfo = new ApplyInfoDto();
			applyInfo.setApplyRemark(interiorRemark);
			applyInfo.setApplyLoanNo(applyLoanNo);
			applyInfoService.updateInfo(applyInfo);
			// 调用 主流程回退
			serviceResult = facadeFlowStateExchangeService.doAction(abandonActionDto);
		} catch (Exception e) {
			MyphLogger.error("申请单管理发起放弃流程异常", e);
		}
		return AjaxResult.formatFromServiceResult(serviceResult);
	}

	/**
	 * 获取前2周时间区间
	 * 
	 * @param queryDto
	 */
	private void initQueryDate(ApplyManageInfoDto queryDto) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date today = cal.getTime();
		cal.add(Calendar.WEEK_OF_YEAR, -2);
		Date twoWeekBefore = cal.getTime();
		// 初始化查询外访进件日期
		if (null == queryDto.getApplyTimeStart()) {
			queryDto.setApplyTimeStart(twoWeekBefore);
		}
		if (null == queryDto.getApplyTimeEnd()) {
			queryDto.setApplyTimeEnd(today);
		}
	}
}
