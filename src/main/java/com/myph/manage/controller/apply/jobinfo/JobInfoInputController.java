/**   
 * @Title: JobInfoInputController.java 
 * @Package: com.myph.manage.controller.apply.jobinfo
 * @company: 麦芽金服
 * @Description: TODO
 * @date 2016年9月12日 下午2:11:39 
 * @version V1.0   
 */
package com.myph.manage.controller.apply.jobinfo;

import com.myph.manage.controller.apply.ApplyBaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.dto.JkApplyJobDto;
import com.myph.apply.dto.MemberJobDto;
import com.myph.apply.jobinfo.service.JobInfoService;
import com.myph.apply.jobinfo.service.MemberJobService;
import com.myph.apply.service.ApplyInfoService;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.util.DateUtils;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.member.base.dto.MemberInfoDto;
import com.myph.member.base.service.MemberInfoService;
import com.myph.reception.dto.ApplyReceptionDto;
import com.myph.reception.service.ApplyReceptionService;

/**
 * 工作信息录入
 * 
 * @ClassName: JobInfoInputController
 * @Description: 申请单管理工作信息录入
 * @author hf
 * @date 2016年9月12日 下午2:11:39
 * 
 */
@RequestMapping("/apply")
@Controller
public class JobInfoInputController extends ApplyBaseController{

	@Autowired
	private JobInfoService jobInfoService;

	@Autowired
	private MemberJobService memberJobService;

	@Autowired
	private MemberInfoService memberInfoService;

	@Autowired
	private ApplyInfoService applyInfoService;

	@Autowired
	private ApplyReceptionService applyReceptionService;

	/**
	 * 工作信息录入(兼容APP申请单修改之后代码)
	 * 
	 * @名称 jobInfoInput
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年9月12日 下午2:14:29
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/newInfoIndex/jobinfoinput")
	public String jobInfoInput(Model model, String applyLoanNo) {
		MyphLogger.debug("JobInfoInputController.jobInfoInput,申请单单号:{}", applyLoanNo);
		// 查询接待
		ApplyReceptionDto applyReceptionDto = applyReceptionService.queryInfoByApplyLoanNo(applyLoanNo).getData();
		if (null != applyReceptionDto && StringUtils.isNotEmpty(applyReceptionDto.getMemberType())) {
			model.addAttribute("memberType", Integer.parseInt(applyReceptionDto.getMemberType()));// 客户类型1是企业主
		} else {
			model.addAttribute("memberType", 0);// 客户类型1是企业主
		}
		String idCardNo = applyReceptionDto.getIdCard();
		MemberInfoDto memberInfo = memberInfoService.queryInfoByIdCard(idCardNo).getData();// 查询客户信息
		if (null == memberInfo) {
			MyphLogger.error("查询客户信息数据异常,身份证：{}", idCardNo);
		}
		Long memberId = memberInfo.getId();// 获取客户会员ID

		MemberJobDto memberJob = memberJobService.selectByMemberId(memberId).getData();// 查询会员工作信息
		model.addAttribute("jobInfo", memberJob);
		model.addAttribute("applyLoanNo", applyLoanNo);
		return "/apply/job_info_input";
	}

	/**
	 * 工作信息录入保存/下一步(兼容APP申请单修改之后代码)
	 * 
	 * @名称 jobInfoSave
	 * @描述 TODO
	 * @返回类型 AjaxResult
	 * @日期 2016年9月12日 下午2:40:27
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/newInfoIndex/jobInfoSave")
	@ResponseBody
	public AjaxResult jobInfoSave(@RequestBody JkApplyJobDto record) {
		String operatorName = ShiroUtils.getCurrentUserName();
		Long operatorId = ShiroUtils.getCurrentUserId();
		MyphLogger.info("申请单录入工作信息录入保存,请求参数:{},当前操作人:{},操作人编号:{}", record, operatorName, operatorId);
		try {
			String applyLoanNo = record.getApplyLoanNo();

			ApplyInfoDto appInfo = applyInfoService.queryInfoByAppNo(applyLoanNo).getData();
			//+++++++加入回源状态判断
			AjaxResult continueResult = getReusltIsContinue(appInfo);
			if(!continueResult.isSuccess()) {
				return continueResult;
			}
			String idCardNo = appInfo.getIdCard();

			MemberInfoDto memberInfo = memberInfoService.queryInfoByIdCard(idCardNo).getData();// 查询客户信息
			Long memberId = memberInfo.getId();// 获取客户会员ID

			MemberJobDto memberJob = memberJobService.selectByMemberId(memberId).getData();// 查询会员工作信息
			MemberJobDto memberJobDto = new MemberJobDto();
			// 同步会员模块工作信息
			BeanUtils.copyProperties(record, memberJobDto);
			memberJobDto.setMemberId(memberId);
			String unitTelephone = record.getUnitTelephone();
			String extensionNum = record.getExtensionNum();
			if (StringUtils.isNotEmpty(unitTelephone) && StringUtils.isNotEmpty(extensionNum)) {
				memberJobDto.setAlternatePhone(unitTelephone + "," + extensionNum);
			}
			if (memberJob != null) {
				memberJobDto.setModifyUser(ShiroUtils.getCurrentUserName());
				memberJobDto.setModifyTime(DateUtils.getCurrentDateTime());
				memberJobService.updateSelective(memberJobDto);
			} else {
				// 同步会员模块工作信息
				memberJobDto.setCreateUser(ShiroUtils.getCurrentUserName());
				memberJobDto.setCreateTime(DateUtils.getCurrentDateTime());
				memberJobService.insertSelective(memberJobDto);
			}

			JkApplyJobDto applyJob = jobInfoService.selectJobInfoByAppNO(applyLoanNo).getData();// 查询申请单工作信息
			if (record.getState() != null && isUpdateSubState(appInfo.getState(),appInfo.getSubState())) {
				MyphLogger.info("申请单录入工作信息录入下一步，更新申请单表状态,单号:{},当前操作人:{},操作人编号:{}", applyLoanNo, operatorName,
						operatorId);
				// 更新主表子状态
				applyInfoService.updateSubState(applyLoanNo, record.getState());
			}
			if (applyJob != null) {
				// 更新
				record.setModifyUser(ShiroUtils.getCurrentUserName());
				record.setModifyTime(DateUtils.getCurrentDateTime());
				ServiceResult<Integer> updateResult = jobInfoService.updateSelective(record);
				return AjaxResult.formatFromServiceResult(updateResult);
			} else {
				// 新增
				record.setCreateTime(DateUtils.getCurrentDateTime());
				record.setCreateUser(ShiroUtils.getCurrentUserName());
				ServiceResult<Integer> addResult = jobInfoService.insertSelective(record);
				return AjaxResult.formatFromServiceResult(addResult);
			}
		} catch (Exception e) {
			MyphLogger.error("申请件工作信息保存异常", e);
			return AjaxResult.failed("服务异常，请稍后重试");
		}
	}

}
