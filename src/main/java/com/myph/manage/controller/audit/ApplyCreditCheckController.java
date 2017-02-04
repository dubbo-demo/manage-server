/**   
 * @Title: ApplyCreditCheck.java 
 * @Package: com.myph.manage.controller.audit
 * @company: 麦芽金服
 * @Description: TODO
 * @date 2016年9月26日 下午6:25:21 
 * @version V1.0   
 */
package com.myph.manage.controller.audit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.dto.JkAuditCheckDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.audit.service.JkAuditCheckService;
import com.myph.common.cache.RedisRootNameSpace;
import com.myph.common.log.MyphLogger;
import com.myph.common.redis.CacheService;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.constant.ApplyFirstReportEnum;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.controller.BaseController;

/**
 * 征信核查
 * 
 * @ClassName: ApplyCreditCheck
 * @Description: TODO
 * @author hf
 * @date 2016年9月26日 下午6:25:21
 * 
 */
@RequestMapping("/audit/credit")
@Controller
public class ApplyCreditCheckController extends BaseController {

	@Autowired
	private JkAuditCheckService jkAuditCheckService;

	@Autowired
	private ApplyInfoService applyInfoService;

	/**
	 * 信审业务管理/审批/初审报告/征信核查
	 * 
	 * @名称 list
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年9月26日 下午6:28:12
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/list")
	public String list(Model model, String applyLoanNo, String cType) {
		ServiceResult<JkAuditCheckDto> result = jkAuditCheckService.select(applyLoanNo);
		if (result.success() && result.getData() != null) {
			model.addAttribute("jkAuditCheckDto", result.getData());
		}
		ServiceResult<ApplyInfoDto> apply = applyInfoService.queryInfoByLoanNo(applyLoanNo);
		model.addAttribute("apply", apply.getData());
		model.addAttribute("applyLoanNo", applyLoanNo);
		model.addAttribute("cType", cType);
		return "/apply/audit/credit_check";
	}

	/**
	 * 信审业务管理/审批/初审报告/征信核查保存
	 * 
	 * @名称 list
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年9月26日 下午6:28:12
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/save")
	@ResponseBody
	public AjaxResult saveCreditCheck(JkAuditCheckDto jkAuditCheckDto) {
		String operatorName = ShiroUtils.getCurrentUserName();
		Long operatorId = ShiroUtils.getCurrentUserId();
		MyphLogger.info("征信核查保存,当前操作人:{},操作人编号:{},请求参数:{}", operatorName, operatorId, jkAuditCheckDto.toString());
		try {
			String applyLoanNo = jkAuditCheckDto.getApplyLoanNo();
			if (StringUtils.isEmpty(applyLoanNo)) {
				return AjaxResult.failed("申请单号不能为空");
			}
			ServiceResult<JkAuditCheckDto> result = jkAuditCheckService.select(applyLoanNo);
			ServiceResult<String> updateResult = null;
			if (result.success() && result.getData() != null) {
				updateResult = jkAuditCheckService.update(jkAuditCheckDto);
				CacheService.StringKey.set(ApplyFirstReportEnum.credit_investigation.getKey(applyLoanNo), applyLoanNo,
						RedisRootNameSpace.UnitEnum.ONE_MONTH);
			} else {
				updateResult = jkAuditCheckService.add(jkAuditCheckDto);
				CacheService.StringKey.set(ApplyFirstReportEnum.credit_investigation.getKey(applyLoanNo), applyLoanNo,
						RedisRootNameSpace.UnitEnum.ONE_MONTH);
			}
			return AjaxResult.formatFromServiceResult(updateResult);
		} catch (Exception e) {
			MyphLogger.error(e, "初审报告-征信核查保存异常");
			return AjaxResult.failed("系统异常,请稍后重试");
		}
	}

}
