package com.myph.manage.controller.sign;

import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.dto.ApplyUserDto;
import com.myph.apply.dto.JkApplyJobDto;
import com.myph.apply.dto.JkAuditTaskDto;
import com.myph.apply.jobinfo.service.JobInfoService;
import com.myph.apply.service.ApplyInfoService;
import com.myph.apply.service.ApplyUserService;
import com.myph.apply.service.ComplianceService;
import com.myph.audit.service.JkApplyAuditService;
import com.myph.cityCode.dto.CityCodeDto;
import com.myph.cityCode.service.CityCodeService;
import com.myph.common.constant.Constants;
import com.myph.common.constant.SysConfigEnum;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.DateTimeUtil;
import com.myph.common.util.DateUtils;
import com.myph.common.util.NumberToCN;
import com.myph.compliance.dto.JkComplianceDto;
import com.myph.constant.ApplyUtils;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.IsAdvanceSettleEnum;
import com.myph.constant.RepayStateEnum;
import com.myph.constant.bis.SignBisStateEnum;
import com.myph.contract.dto.JkContractDto;
import com.myph.contract.service.JkContractService;
import com.myph.employee.dto.EmpDetailDto;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.flow.dto.AbandonActionDto;
import com.myph.flow.dto.ContinueActionDto;
import com.myph.flow.dto.RejectActionDto;
import com.myph.idgenerator.service.IdGeneratorService;
import com.myph.manage.common.constant.Constant;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.controller.BaseController;
import com.myph.manage.facadeService.FacadeFlowStateExchangeService;
import com.myph.member.card.dto.UserCardInfoDto;
import com.myph.member.card.service.CardService;
import com.myph.node.dto.SysNodeDto;
import com.myph.node.service.NodeService;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.payBank.dto.SysPayBankDto;
import com.myph.payBank.service.SysPayBankService;
import com.myph.product.dto.ProductDto;
import com.myph.product.service.ProductService;
import com.myph.repaymentPlan.dto.JkRepaymentPlanDto;
import com.myph.repaymentPlan.service.JkRepaymentPlanService;
import com.myph.sign.dto.ContractModelView;
import com.myph.sign.dto.JkSignDto;
import com.myph.sign.dto.SignQueryDto;
import com.myph.sign.service.SignService;
import com.myph.sysParamConfig.service.SysParamConfigService;
import com.myph.visit.dto.VisitDetailDto;
import com.myph.visit.service.VisitService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 签约
 * 
 * @author peterhe
 *
 */
@RequestMapping("/sign")
@Controller
public class SignController extends BaseController {

	private static final int numCount = 8;

	private static final Integer twelvePeriods = 12;

	private static final Integer twentyFourPeriods = 24;

	private static final Integer thirtySixPeriods = 36;

	private static final String securityRate = "0.08";// 风险金费率

	private static final BigDecimal prepaymentRateFirst = new BigDecimal("0.05");// 提前还款减免

	private static final BigDecimal prepaymentRateSecond = new BigDecimal("0.03");// 提前还款减免

	private static final String PASS = "通过";// 签约结果

	private static final String ENCODE = "UTF-8";

	@Autowired
	private SignService signService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private ApplyInfoService applyInfoService;

	@Autowired
	private ApplyUserService applyUserService;

	@Autowired
	private JkApplyAuditService jkApplyAuditService;

	@Autowired
	private ProductService productService;

	@Autowired
	private JkContractService contractService;

	@Autowired
	private IdGeneratorService generatorService;

	@Autowired
	private EmployeeInfoService employeeInfoService;

	@Autowired
	private CityCodeService cityCodeService;

	@Autowired
	private JobInfoService jobInfoService;

	@Autowired
	private NodeService nodeService;

	@Autowired
	private VisitService visitService;

	@Autowired
	private JkRepaymentPlanService repaymentPlanService;

	@Autowired
	private ComplianceService complianceService;

	@Autowired
	private SysParamConfigService sysParamConfigService;

	@Autowired
	private FacadeFlowStateExchangeService facadeFlowStateExchangeService;
	@Autowired
	private CardService cardService;
	@Autowired
	SysPayBankService sysPayBankService;
	/**
	 * 
	 * @名称 list
	 * @描述 获取签约列表
	 * @返回类型 String
	 * @日期
	 * @创建人 peterhe
	 * @更新人 peterhe
	 *
	 */
	@RequestMapping("/list")
	public String list(Model model, SignQueryDto queryDto, BasePage basePage) {
		MyphLogger.info("开始申请单待签约查询：/sign/list.htm|querDto=" + queryDto + "|basePage=" + basePage);
		initSignListParams(model, queryDto, basePage);// 加载默认查询条件
		initQueryDate(queryDto);// 加载日期前后限制
		ServiceResult<Pagination<SignQueryDto>> pageResult = signService.listPageInfos(queryDto, basePage);
		List<SignQueryDto> list = pageResult.getData().getResult();
		for (SignQueryDto signDto : list) {
			// 获取大区名称
			Long areaId = signDto.getAreaId();
			ServiceResult<OrganizationDto> tempOrgResult = organizationService.selectOrganizationById(areaId);
			OrganizationDto tempOrg = tempOrgResult.getData();
			if (null != tempOrg) {
				signDto.setAreaName(tempOrg.getOrgName());
			}
			signDto.setStateDesc(ApplyUtils.getFullStateDesc(signDto.getState(), signDto.getSubState()));
		}
		model.addAttribute("page", pageResult.getData());
		model.addAttribute("queryDto", queryDto);
		MyphLogger.debug("结束开始申请单待签约查询：/sign/list.htm|page=" + pageResult);
		return "/apply/sign/sign_list";
	}

	/**
	 * 签约列表默认查询条件加载
	 * 
	 * @名称 initSignListParams
	 * @描述 TODO
	 * @返回类型 void
	 * @日期 2016年11月16日 上午9:32:24
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	private void initSignListParams(Model model, SignQueryDto queryDto, BasePage basePage) {
		EmployeeInfoDto user = ShiroUtils.getCurrentUser();
		EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
		model.addAttribute("user", user);
		List<OrganizationDto> orgs = ShiroUtils.getStoreInfo();
		// 查询组织条件为空获取当前组织数据权限
		if(null == queryDto.getStoreId()) {
			List<Long> storeIds = new ArrayList<Long>();
			for(OrganizationDto org : orgs){
				storeIds.add(org.getId());
			}
			queryDto.setStoreIds(storeIds);
		}
		model.addAttribute("orgs",orgs);
		model.addAttribute("storeName", empDetail.getStoreName());
		if (null == basePage.getSortField()) {// 进件日期倒序
			basePage.setSortField("a.subState DESC,t.passTime");
			queryDto.setDefaultSort("default");
		}
		List<Integer> subStateList = new ArrayList<Integer>();
		subStateList.add(SignBisStateEnum.INIT.getCode());
		subStateList.add(SignBisStateEnum.BACK_INIT.getCode());
		queryDto.setState(FlowStateEnum.SIGN.getCode());
		queryDto.setSubStateList(StringUtils.join(subStateList, ","));
	}

	/**
	 * 签约按钮
	 * 
	 * @名称 goSign
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年10月18日 下午3:43:01
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/goSign")
	public String goSign(Model model, String applyLoanNo, Integer subState) {
		MyphLogger.info("点击签约按钮跳转开始：/sign/goSign.htm|applyLoanNo=" + applyLoanNo + "|subState=" + subState);
		boolean visitVisible = false;// 外访详情可见
		List<VisitDetailDto> vistiList = visitService.getResultByApplyNO(applyLoanNo).getData();// 查询是否发起外访
		if (CollectionUtils.isNotEmpty(vistiList)) {
			visitVisible = true;
		}
		model.addAttribute("visitTab", visitVisible);
		model.addAttribute("applyLoanNo", applyLoanNo);
		model.addAttribute("subState", subState);
		MyphLogger.info("点击签约按钮跳转结束：/sign/goSign.htm|applyLoanNo=" + applyLoanNo + "|subState=" + subState);
		return "/apply/sign/go_sign";
	}

	/**
	 * 签约详情
	 * 
	 * @名称 signDetail
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年10月18日 下午3:43:01
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/signDetail")
	public String signDetail(Model model, String applyLoanNo, Integer subState) {
		MyphLogger.info("进入签约详情页面开始：/sign/signDetail.htm|applyLoanNo=" + applyLoanNo);
		try {
			String productName = null;
			model.addAttribute("applyLoanNo", applyLoanNo);

			ApplyInfoDto applyInfo = applyInfoService.queryInfoByAppNo(applyLoanNo).getData();// 查询申请单信息

			ApplyUserDto applyUserDto = applyUserService.queryInfoByLoanNo(applyLoanNo).getData();// 查询用户基本信息

			if (SignBisStateEnum.INIT.getCode().equals(subState)) {
				JkAuditTaskDto jkAuditTaskDto = jkApplyAuditService.queryInfoByApplyLoanNo(applyLoanNo).getData();// 查询信审任务表
				model.addAttribute("auditTask", jkAuditTaskDto);
			} else if (SignBisStateEnum.BACK_INIT.getCode().equals(subState)) {
				JkComplianceDto complianceDto = complianceService.queryComplianceByAppNo(applyLoanNo).getData();
				model.addAttribute("compliance", complianceDto);
			}

			JkContractDto jkContractDto = contractService.selectByApplyLoanNo(applyLoanNo).getData();// 查询合同表

			JkSignDto jkSignDto = signService.selectByApplyLoanNo(applyLoanNo).getData();// 查询签约表

			ProductDto productDto = productService.selectByPrimaryKey(applyInfo.getProductType()).getData();// 查询产品表

			if (null != jkContractDto) {
				productName = jkContractDto.getProductName();
			}
			if (null == productName || "".equals(productName)) {
				productName = productService.getProductNameById(applyInfo.getProductType()).getData();// 从基础数据获取产品名称
			}

			int loanUpMax = productDto.getLoanUpLimit().setScale(0, RoundingMode.DOWN).toString().length();// 贷款额度上限

			// 查询银行卡信息
			ServiceResult<List<UserCardInfoDto>> result = cardService.queryUserCardInfo(applyUserDto.getPhone());
			SysPayBankDto bankDto = null;
			UserCardInfoDto userCardInfoDto = null;
			if(result.success()){
				if(null!=result.getData()&&result.getData().size() > 0){
					for(UserCardInfoDto dto:result.getData()){
						if(dto.getIDKFlag().equals(Constants.YES_INT)){
							model.addAttribute("userCardInfo", dto);
							userCardInfoDto = dto;
							//通过银行卡信息去查询银行名称
							ServiceResult<SysPayBankDto> bankResult = sysPayBankService.selectBySbankNo(dto.getBankNo());
							if(bankResult.success()){
								model.addAttribute("bankInfo",bankResult.getData());
								bankDto = bankResult.getData();

								// 获取产品期数，月息，综合服务费；计算合同金额、总利息、服务费和还款总额
								if (productDto != null && jkContractDto != null && jkContractDto.getRepayMoney() != null) {
									JkContractDto jkcontarct = calculateServiceRate(productDto, jkContractDto.getRepayMoney());
									jkContractDto.setServiceRate(jkcontarct.getServiceRate());
								}

								ServiceResult<JkContractDto> contractResult = contractService.selectByApplyLoanNo(applyLoanNo);
								if (contractResult.success() && contractResult.getData() != null) {
									JkContractDto contractDto = contractResult.getData();
									model.addAttribute("contractNo", contractDto.getContractNo());
								} else {
									EmpDetailDto empDetail = ShiroUtils.getEmpDetail();
									Long cityId = empDetail.getCityId();
									CityCodeDto cityCodeDto = null;
									if (cityId != null) {
										// 获取城市编码
										cityCodeDto = cityCodeService.selectByPrimaryKey(cityId).getData();
									}
									if (cityCodeDto != null) {
										String cityCode = cityCodeDto.getCityCode();
										StringBuffer contractNo = new StringBuffer(cityCode);
										contractNo.append(DateUtils.getCurrentTimeNum());
										StringBuffer myphContractNo = new StringBuffer("MYPH");
										String nextVal = generatorService.getNextVal(contractNo.toString(), 4).getData();
										myphContractNo.append(nextVal);
										model.addAttribute("contractNo", myphContractNo);
										final JkContractDto record = new JkContractDto();
										record.setApplyLoanNo(applyLoanNo);
										record.setContractNo(myphContractNo.toString());
										record.setBankCardNo(userCardInfoDto.getBankCardNo());
										record.setBankCity(userCardInfoDto.getBankAccountCity());
										record.setBankName(bankDto.getSname());
										record.setBankType(bankDto.getSsimplecode());
										record.setBankTypeName(bankDto.getSname());
										record.setMemberName(userCardInfoDto.getAccountName());
										record.setIdCard(userCardInfoDto.getIdCardNo());
										record.setReservedPhone(userCardInfoDto.getMobile());
										record.setOrgId(applyInfo.getStoreId());
										final String operatorName = ShiroUtils.getCurrentUserName();
										contractService.insertSelective(record, operatorName);
									}
								}
							}
							break;
						}
					}
				}
			}else{
				MyphLogger.error(result.getMessage());
			}

			if (applyUserDto != null) {
				String mailAddress = applyUserDto.getMailAddress();
				// 邮寄地址:1,现住址;2,公司地址;3,户籍地址
				if ("1".equals(mailAddress)) {
					applyUserDto.setMailAddress(applyUserDto.getLiveAddr() + applyUserDto.getLiveAddress());
				} else if ("2".equals(mailAddress)) {
					JkApplyJobDto applyJobDto = jobInfoService.selectJobInfoByAppNO(applyLoanNo).getData();
					if (applyJobDto != null) {
						applyUserDto
								.setMailAddress(applyJobDto.getCompanyAddress() + "--" + applyJobDto.getDetailAddr());
					}
				} else if ("3".equals(mailAddress)) {
					applyUserDto.setMailAddress(applyUserDto.getCensusAddr() + applyUserDto.getCensusAddress());
				}
			}

			SysNodeDto nodeDto = nodeService.selectByPrimaryKey(applyInfo.getLoanPurpose()).getData();
			if (nodeDto != null) {
				applyInfo.setLoanPurposes(nodeDto.getNodeName());
			}

			model.addAttribute("loanUpMax", loanUpMax);
			model.addAttribute("jkSignDto", jkSignDto);
			model.addAttribute("productDto", productDto);
			model.addAttribute("jkContractDto", jkContractDto);
			model.addAttribute("productName", productName);
			model.addAttribute("appInfo", applyInfo);
			model.addAttribute("applyUserDto", applyUserDto);
			model.addAttribute("subState", subState);

		} catch (Exception e) {
			MyphLogger.error(e, "进入合同详情页服务异常");
		}
		MyphLogger.info("进入签约详情页面结束：/sign/signDetail.htm|applyLoanNo=" + applyLoanNo);
		return "/apply/sign/sign_detail";
	}

	/**
	 * 签约合同详情页保存
	 * 
	 * @param jkContractDto
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public AjaxResult save(JkContractDto jkContractDto) {
		if (null == jkContractDto) {
			return AjaxResult.failed("请求参数不能为空");
		}
		String operatorName = ShiroUtils.getCurrentUserName();
		Long operatorId = ShiroUtils.getCurrentUserId();
		MyphLogger.info("签约合同详情页保存开始：/sign/save.htm|合同参数:{},操作人:{},操作人编号:{}", jkContractDto.toString(), operatorName,
				operatorId);
		ServiceResult<Integer> serviceResult = ServiceResult.newFailure();
		try {
			String applyLoanNo = jkContractDto.getApplyLoanNo();
			if (StringUtils.isEmpty(applyLoanNo)) {
				return AjaxResult.failed("申请单号不能为空");
			}
			JkSignDto jkSignDto = new JkSignDto();
			BeanUtils.copyProperties(jkContractDto, jkSignDto);
			jkSignDto.setDelFlag(Constants.YES_INT);
			jkSignDto.setSignResult(jkContractDto.getSignResult());
			jkContractDto.setRemark(jkContractDto.getRemark());
			/** 临时方案 暂定不放在一个事务 估计后期代码优化 */

			ServiceResult<JkSignDto> signResult = signService.selectByApplyLoanNo(applyLoanNo);
			if (signResult.success() && signResult.getData() != null) {
				signService.updateSelective(jkSignDto);
			} else {
				jkSignDto.setCreateUserId(ShiroUtils.getCurrentUserId());
				jkSignDto.setCreateUser(ShiroUtils.getCurrentUserName());
				signService.insertSelective(jkSignDto);
			}

			ServiceResult<JkContractDto> contractResult = contractService.selectByApplyLoanNo(applyLoanNo);
			if (contractResult.success() && contractResult.getData() != null) {
				serviceResult = contractService.updateSelective(jkContractDto);
				MyphLogger.info("进入签约详情页面开始：/sign/signDetail.htm|result=" + serviceResult);
				return AjaxResult.formatFromServiceResult(serviceResult);
			} else {
				serviceResult = contractService.insertSelective(jkContractDto, ShiroUtils.getCurrentUserName());
				MyphLogger.info("签约合同详情页保存结束：/sign/signDetail.htm|result=" + serviceResult);
				return AjaxResult.formatFromServiceResult(serviceResult);
			}
		} catch (Exception e) {
			MyphLogger.error(e, "合同详情页签约保存后台服务异常");
		}
		return AjaxResult.formatFromServiceResult(serviceResult);
	}

	/**
	 * 提交签约 向合同表，签约表和还款计划表提交相应数据
	 * 
	 * @param jkContractDto
	 * @return
	 */
	@RequestMapping("/submitSign")
	@ResponseBody
	public AjaxResult sign(JkContractDto jkContractDto) {
		MyphLogger.debug("提交签约时 合同信息为：【{}】", jkContractDto.getContractNo());
		if (null == jkContractDto) {
			return AjaxResult.failed("请求参数不能为空");
		}
		//新加检验++++罗荣+++++2017-09-08   通过合同号查询放款计划表中，首期 期初本金 与放款时间
		ServiceResult<JkRepaymentPlanDto> repaymentResult = repaymentPlanService.queryFirstBillByContractNo(jkContractDto.getContractNo());
		//判断放款金额+服务是否恒等于首期金额 期初本金
		if(repaymentResult.success()){
			JkRepaymentPlanDto dto= repaymentResult.getData();
			if(null == dto){
				MyphLogger.error("提交异常,查询还款计划表数据为空【{}】", jkContractDto.getContractNo());
				return AjaxResult.failed("请先导出账单数据！");
			}
			BigDecimal initialPrincipal = dto.getInitialPrincipal();
			if(null!=initialPrincipal){
				if(initialPrincipal.compareTo(jkContractDto.getRepayMoney().add(jkContractDto.getServiceRate())) != 0){
					return AjaxResult.failed("放款金额加服务费不等于期初本金["+initialPrincipal.toString()+"]");
				}
			}else{
				MyphLogger.error("提交异常,合同号[{}]第一期期初本金为空", jkContractDto.getContractNo());
				return AjaxResult.failed("期初本金为空");
			}
			//判断放款日期的后一个月，是否恒等于 放款计划第一期的协议还款日期
			Date loanTime = jkContractDto.getLoanTime();
			//加一个月
			Date newLoanTime= DateUtils.add(loanTime,Calendar.MONTH,1);
			//如果不等于则返回一个提示
			if(!newLoanTime.equals(dto.getAgreeRepayDate())){
				return AjaxResult.failed("放款日期后一个月不等于协议还款日期");
			}
		}else{
			MyphLogger.error("提交异常,合同号[{}]第一期还款信息不存在", jkContractDto.getContractNo());
			return AjaxResult.failed("请先导出生成还款信息！");
		}
		String operatorName = ShiroUtils.getCurrentUserName();
		Long operatorId = ShiroUtils.getCurrentUserId();
		MyphLogger.info("签约提交开始：/sign/submitSign.htm|合同参数:{},操作人:{},操作人编号:{}", jkContractDto.toString(), operatorName,
				operatorId);
		ServiceResult<Integer> serviceResult = ServiceResult.newFailure();
		try {
			String applyLoanNo = jkContractDto.getApplyLoanNo();
			if (StringUtils.isEmpty(applyLoanNo)) {
				return AjaxResult.failed("申请单号不能为空");
			}
			ServiceResult<JkContractDto> result = contractService.selectByApplyLoanNo(applyLoanNo);

			ProductDto productDto = productService.selectByPrimaryKey(jkContractDto.getProductType()).getData();

			String productName = productService.getProductNameById(jkContractDto.getProductType()).getData();

			constructParams(jkContractDto, productDto, productName);

			// 获取产品期数，月息，综合服务费；计算合同金额、总利息、服务费和还款总额
			if (productDto != null && jkContractDto != null) {
				JkContractDto jkcontarct = calculateServiceRate(productDto, jkContractDto.getRepayMoney());
				jkContractDto.setRepayMoney(jkcontarct.getRepayMoney());
				jkContractDto.setRepaymentAmount(jkcontarct.getRepaymentAmount());
				jkContractDto.setContractAmount(jkcontarct.getContractAmount());
				jkContractDto.setServiceRate(jkcontarct.getServiceRate());
				jkContractDto.setInterestAmount(jkcontarct.getInterestAmount());
			}

			JkSignDto jkSignDto = new JkSignDto();
			jkSignDto.setCreateUserId(ShiroUtils.getCurrentUserId());
			jkSignDto.setCreateUser(ShiroUtils.getCurrentUserName());
			BeanUtils.copyProperties(jkContractDto, jkSignDto);

			// 更新申请单主表放款时间
			ApplyInfoDto applyInfo = new ApplyInfoDto();
			applyInfo.setLoanTime(jkContractDto.getLoanTime());
			applyInfo.setUpdateTime(DateUtils.getCurrentDateTime());
			applyInfo.setModifyUser(ShiroUtils.getCurrentUserName());
			applyInfo.setApplyLoanNo(applyLoanNo);
			applyInfo.setContractNo(jkContractDto.getContractNo());

			boolean isUpdate = false;
			if (result.success() && result.getData() != null) {
				isUpdate = true;
			}

			synchronized (this) {
				// 第一步校验当前申请单状态是否在签约阶段
				ApplyInfoDto applyInfoDto = applyInfoService.queryInfoByAppNo(applyLoanNo).getData();// 查询申请单信息
				if (!(FlowStateEnum.SIGN.getCode().equals(applyInfoDto.getState())
						&& (SignBisStateEnum.INIT.getCode().equals(applyInfoDto.getSubState())
								|| SignBisStateEnum.BACK_INIT.getCode().equals(applyInfoDto.getSubState())))) {
					return AjaxResult.failed("该申请单已签约！");
				}
				if (isUpdate) {
					contractService.updateSelective(jkContractDto);
				} else {
					contractService.insertSelective(jkContractDto, operatorName);
				}
				serviceResult = signService.insertAllData(isUpdate, jkSignDto, applyInfo,
						ShiroUtils.getCurrentUserName());

				if (!serviceResult.success()) {
					return AjaxResult.failed("提交签约失败，请稍后重试");
				}
				if (PASS.equals(jkContractDto.getSignResult())) {// 通过
					// 调用状态机进入主流程DTO
					ContinueActionDto applyNotifyDto = new ContinueActionDto();
					applyNotifyDto.setApplyLoanNo(jkContractDto.getApplyLoanNo());
					applyNotifyDto.setOperateUser(ShiroUtils.getCurrentUserName());
					applyNotifyDto.setFlowStateEnum(FlowStateEnum.SIGN);

					// 走状态机更新主流程
					serviceResult = facadeFlowStateExchangeService.doAction(applyNotifyDto);
					if (!serviceResult.success()) {
						MyphLogger.error("调用更新主流程失败！param【{}】,MESSAGE:{}", applyNotifyDto, serviceResult.getMessage());
						return AjaxResult.formatFromServiceResult(serviceResult);
					}
				} else {
					// 调用状态机进入主流程DTO
					RejectActionDto rejectActionDto = new RejectActionDto();
					rejectActionDto.setApplyLoanNo(applyLoanNo);
					rejectActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
					rejectActionDto.setRejectDays(Constant.CONFINE_TIME.getCode());
					rejectActionDto.setFlowStateEnum(FlowStateEnum.SIGN);

					// 走状态机更新主流程
					serviceResult = facadeFlowStateExchangeService.doAction(rejectActionDto);
					if (!serviceResult.success()) {
						MyphLogger.error("调用更新主流程失败！param【{}】,MESSAGE:{}", rejectActionDto, serviceResult.getMessage());
						return AjaxResult.formatFromServiceResult(serviceResult);
					}
				}
			}
		} catch (Exception e) {
			MyphLogger.error(e, "合同详情页签约提交后台服务异常");
		}
		MyphLogger.info("签约提交结束：/sign/submitSign.htm|jkContractDto=" + jkContractDto);
		return AjaxResult.formatFromServiceResult(serviceResult);
	}

	private void constructParams(JkContractDto jkContractDto, ProductDto productDto, String productName) {
		jkContractDto.setTotalRate(productDto.getServiceRate());// 综合服务费率
		jkContractDto.setPenaltyRate(productDto.getPenaltyRate());// 罚息比例
		jkContractDto.setRepayRate(productDto.getPreRepayRate());// 提前还款费率
		jkContractDto.setProductId(productDto.getId());
		jkContractDto.setProductName(productName);
		jkContractDto.setPeriods(productDto.getPeriods());
		jkContractDto.setOverdueDay(productDto.getOverdueDays());// 逾期天数
		jkContractDto.setLoanLimitUp(productDto.getLoanUpLimit());
		jkContractDto.setSignTime(DateUtils.getCurrentDateTime());
		jkContractDto.setUpdateTime(DateUtils.getCurrentDateTime());
		jkContractDto.setCreateUser(ShiroUtils.getCurrentUserName());
		jkContractDto.setCreateTime(DateUtils.getCurrentDateTime());
		jkContractDto.setDelFlag(Constants.YES_INT);
	}

	/**
	 * 根据放款金额计算服务费
	 * 
	 * @名称 getServiceRate
	 * @描述 TODO
	 * @返回类型 AjaxResult
	 * @日期 2016年10月26日 下午3:36:26
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/getServiceRate")
	@ResponseBody
	public AjaxResult getServiceRate(String applyLoanNo, String reapyMoney, Long productType) {
		MyphLogger.info("根据放款金额计算服务费开始：/sign/getServiceRate.htm|applyLoanNo=" + applyLoanNo + "|reapyMoney="
				+ reapyMoney + "|productType=" + productType);
		MentionLoanAmount mentionLoanAmount = checkRepayMoney(applyLoanNo, reapyMoney);
		if (!mentionLoanAmount.isCheck()) {
			return AjaxResult.failed(mentionLoanAmount.getMessage());
		}
		JkContractDto jkcontarct = new JkContractDto();
		jkcontarct = calculateServiceRate(mentionLoanAmount.getProductDto(), new BigDecimal(reapyMoney));
		MyphLogger.info("根据放款金额计算服务费结束：/sign/getServiceRate.htm|serviceRate=" + jkcontarct.getServiceRate());
		return AjaxResult.success(jkcontarct.getServiceRate());
	}

	/**
	 * 放款金额校验公共逻辑
	 * 
	 * @param applyLoanNo
	 * @param reapyMoney
	 * @return mentionLoanAmount
	 */
	public MentionLoanAmount checkRepayMoney(String applyLoanNo, String reapyMoney) {
		MentionLoanAmount mentionLoanAmount = new MentionLoanAmount();
		ApplyInfoDto applyInfo = applyInfoService.queryInfoByAppNo(applyLoanNo).getData();// 查询申请单信息
		Long productId = applyInfo.getProductType();
		if (null == productId) {
			MyphLogger.error("获取产品编号ID为空");
			mentionLoanAmount.setMessage("无法获取产品编号Id，请联系管理员");
			return mentionLoanAmount;
		}
		ProductDto productDto = productService.selectByPrimaryKey(productId).getData();
		// 获取产品期数，月息，综合服务费；计算合同金额、总利息、服务费和还款总额
		if (productDto == null) {
			MyphLogger.error("获取产品配置数据异常，根据产品Id{}获取不到对应的产品数据", productId);
			mentionLoanAmount.setMessage("根据产品Id" + productId + "获取不到对应的产品数据,请联系管理员");
			return mentionLoanAmount;
		}
		// 比较放款金额与信审审批金额
		BigDecimal auditRatifyMoney = applyInfo.getAuditRatifyMoney();
		// 信审金额+提额
		BigDecimal mentionAmount = new BigDecimal("0");
		// 业务经理员工ID
		Long bmId = applyInfo.getBmId();
		EmployeeInfoDto employeeInfoDto = employeeInfoService.getEntityById(bmId).getData();
		if (null != employeeInfoDto && StringUtils.isNotEmpty(employeeInfoDto.getJobLevel())) {
			SysConfigEnum sysConfigEnum = SysConfigEnum.getSysConfig(employeeInfoDto.getJobLevel());
			String varValue = sysParamConfigService.getConfigValueByName(sysConfigEnum);
			if (StringUtils.isNotEmpty(varValue)) {
				mentionAmount = auditRatifyMoney.add(new BigDecimal(varValue));
				if (new BigDecimal(reapyMoney).compareTo(mentionAmount) == Constants.YES_INT) {
					mentionLoanAmount.setMessage("放款金额超出授权金额上限，如有疑问，请联系管理员");
					return mentionLoanAmount;
				}
			}
		} else {
			if (new BigDecimal(reapyMoney).compareTo(auditRatifyMoney) == Constants.YES_INT) {
				mentionLoanAmount.setMessage("放款金额超出授权金额上限，如有疑问，请联系管理员");
				return mentionLoanAmount;
			}
		}
		BigDecimal loanUpLimit = productDto.getLoanUpLimit();
		// 计算后的放款金额大于0 与产品上限金额比较
		if (new BigDecimal(reapyMoney).compareTo(loanUpLimit) == Constants.YES_INT) {
			mentionLoanAmount.setMessage("放款金额超出产品贷款额度上限，如有疑问，请联系管理员");
			return mentionLoanAmount;
		}
		mentionLoanAmount.setProductDto(productDto);
		mentionLoanAmount.setCheck(true);
		return mentionLoanAmount;
	}

	/**
	 * 计算产品服务费明细
	 * 
	 * @param productDto
	 *            产品实体
	 * @param repayMoney
	 *            放款金额
	 * @名称 calculateServiceRate
	 * @描述 TODO
	 * @返回类型 BigDecimal
	 * @日期 2016年10月26日 下午3:39:38
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	private JkContractDto calculateServiceRate(ProductDto productDto, BigDecimal repayMoney) {
		if (repayMoney == null || productDto == null) {
			throw new IllegalArgumentException("参数非法");
		}
		JkContractDto jkContractDto = new JkContractDto();
		// 综合服务费率
		BigDecimal serviceRate = productDto.getServiceRate();
		// 利率/月
		BigDecimal interestRate = productDto.getInterestRate();
		// 期数
		int periods = productDto.getPeriods();
		// 总还款额
		BigDecimal repaymentAmount = repayMoney.multiply(serviceRate).multiply(new BigDecimal(periods)).add(repayMoney);
		BigDecimal intermediateVariable = interestRate.multiply(new BigDecimal(periods)).add(new BigDecimal(1));
		// 合同金额
		BigDecimal contractAmount = repaymentAmount.divide(intermediateVariable, 0, BigDecimal.ROUND_HALF_UP);
		// 总利息
		BigDecimal interestAmount = repaymentAmount.subtract(contractAmount);
		// 综合服务费
		BigDecimal serviceRateAmount = contractAmount.subtract(repayMoney);
		jkContractDto.setRepayMoney(repayMoney);
		jkContractDto.setRepaymentAmount(repaymentAmount);
		jkContractDto.setContractAmount(contractAmount);
		jkContractDto.setServiceRate(serviceRateAmount);
		jkContractDto.setInterestAmount(interestAmount);
		return jkContractDto;
	}

	/**
	 * 校验放款金额上限
	 * 
	 * @名称 checkLoanUpLimit
	 * @描述 TODO
	 * @返回类型 AjaxResult
	 * @日期 2016年10月8日 下午2:30:44
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/checkLoanUpLimit")
	@ResponseBody
	public AjaxResult checkLoanUpLimit(String applyLoanNo, String reapyMoney) {
		MyphLogger.debug("校验放款金额上限开始,单号:{}" + applyLoanNo);
		MentionLoanAmount mentionLoanAmount = checkRepayMoney(applyLoanNo, reapyMoney);
		if (!mentionLoanAmount.isCheck()) {
			return AjaxResult.failed(mentionLoanAmount.getMessage());
		}
		MyphLogger.debug("校验放款金额上限开始,单号:{}" + applyLoanNo);
		return AjaxResult.success();
	}

	/**
	 * 打印
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 * @名称 signDetail
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年10月18日 下午3:43:01
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/print")
	public String print(Model model, String applyLoanNo, String contractNo, Integer type, String loanTime,
			String repayMoney, String bankNo, String bankName, String productName) throws UnsupportedEncodingException {

		bankNo = java.net.URLDecoder.decode(bankNo, ENCODE);
		bankName = java.net.URLDecoder.decode(bankName, ENCODE);
		productName = java.net.URLDecoder.decode(productName, ENCODE);

		MyphLogger.info("合同详情页打印开始：/sign/print.htm|applyLoanNo=" + applyLoanNo + "|contractNo=" + contractNo + "|type="
				+ type + "|bankNo=" + bankNo + "|bankName=" + bankName + "|productName=" + productName);

		String contractFileName = ContractEnum.getViewName(type);// 合同文件名
		ApplyInfoDto applyInfo = applyInfoService.queryInfoByAppNo(applyLoanNo).getData();// 查询申请单信息
		ProductDto productDto = productService.selectByPrimaryKey(applyInfo.getProductType()).getData();
		BigDecimal repayMon = new BigDecimal(repayMoney);// 放款金额
		JkContractDto contractData = calculateServiceRate(productDto, repayMon);// 获取产品服务费明细
		BigDecimal contractAmount = contractData.getContractAmount();// 合同金额
		BigDecimal interestAmount = contractData.getInterestAmount();// 总利息
		Integer num = productDto.getPeriods();// 还款期数
		BigDecimal periods = new BigDecimal(Integer.toString(num));

		Long cityId = applyInfo.getCityId();
		CityCodeDto cityCodeDto = null;
		if (cityId != null) {
			// 获取城市编码
			cityCodeDto = cityCodeService.selectByPrimaryKey(cityId).getData();
		}
		ContractModelView contractModelView = constructContractData(applyLoanNo, bankNo, bankName, applyInfo,
				cityCodeDto);
		Integer subState = applyInfo.getSubState();
		if (ContractEnum.CREDIT_LOAN_PROTOCOL.type == type) {
			initCreditLoanProtocol(subState, applyLoanNo, bankNo, bankName, contractAmount, interestAmount, num,
					periods, contractModelView);
			model.addAttribute("title", ContractEnum.CREDIT_LOAN_PROTOCOL.desc);
		} else if (ContractEnum.CREDIT_COUNSELING_MANAGE_SERVICE.type == type) {
			initCreditCounselingManageService(subState, applyLoanNo, repayMon, contractNo, contractAmount, cityCodeDto,
					contractModelView);
			model.addAttribute("title", ContractEnum.CREDIT_COUNSELING_MANAGE_SERVICE.desc);
		} else if (ContractEnum.REPAYMENT_REMINDER.type == type) {
			initRepaymentReminder(subState, model, applyLoanNo, contractNo, loanTime, contractAmount, interestAmount,
					num, periods, repayMon);
			model.addAttribute("title", ContractEnum.REPAYMENT_REMINDER.desc);
		} else if (ContractEnum.FY_ACCOUNT_SPECIAL_PROTOCOL.type == type) {
			// 已走公共逻辑
			model.addAttribute("title", ContractEnum.FY_ACCOUNT_SPECIAL_PROTOCOL.desc);
		} else if (ContractEnum.CUSTOMER_CONTRACT_VERIFICATION_FORM.type == type) {
			initCustomerContractVerificationForm(applyLoanNo, contractNo, productName, cityCodeDto, contractModelView);
			model.addAttribute("title", ContractEnum.CUSTOMER_CONTRACT_VERIFICATION_FORM.desc);
		} else if (ContractEnum.ENTRUST_DEBIT_AUTHORIZATION.type == type) {
			// 已走公共逻辑
			model.addAttribute("title", ContractEnum.ENTRUST_DEBIT_AUTHORIZATION.desc);
		}
		model.addAttribute("applyLoanNo", applyLoanNo);
		model.addAttribute("contractNo", contractNo);
		model.addAttribute("contractModelView", contractModelView);
		MyphLogger.info("合同详情页打印结束：/sign/print.htm");
		return "/apply/sign/print/" + contractFileName;
	}

	private ContractModelView constructContractData(String applyLoanNo, String bankNo, String bankName,
			ApplyInfoDto applyInfo, CityCodeDto cityCodeDto) {
		ContractModelView contractModelView = new ContractModelView();
		if (cityCodeDto != null) {
			contractModelView.setSignAddress(cityCodeDto.getCityName());
		}
		contractModelView.setPhone(applyInfo.getPhone());
		contractModelView.setSignYear(DateTimeUtil.getYear());
		contractModelView.setSignMonth(DateTimeUtil.getMonth());
		contractModelView.setSignDay(DateTimeUtil.getDay(new Date()));
		contractModelView.setBorrower(applyInfo.getMemberName());
		ApplyUserDto applyUser = applyUserService.queryInfoByLoanNo(applyLoanNo).getData();
		if (applyUser != null) {
			contractModelView.setIdCardNo(applyUser.getIdCarNo());
		}
		contractModelView.setBankNo(bankNo);
		contractModelView.setBankName(bankName);
		contractModelView.setIdCardNo(applyUser.getIdCarNo());
		String liveAddr = applyUser.getLiveAddr();
		if (StringUtils.isNotEmpty(liveAddr)) {
			liveAddr = StringUtils.deleteWhitespace(liveAddr);
		}
		contractModelView.setPresentAddress(liveAddr + applyUser.getLiveAddress());
		SysNodeDto nodeDto = nodeService.selectByPrimaryKey(applyInfo.getLoanPurpose()).getData();
		if (nodeDto != null) {
			contractModelView.setLoanPopurse(nodeDto.getNodeName());
		}
		return contractModelView;
	}

	private void initCustomerContractVerificationForm(String applyLoanNo, String contractNo, String productName,
			CityCodeDto cityCodeDto, ContractModelView contractModelView) {
		contractModelView.setCaseNumber(contractNo);
		contractModelView.setProductName(productName);
		JkApplyJobDto applyJobDto = jobInfoService.selectJobInfoByAppNO(applyLoanNo).getData();
		if (applyJobDto != null) {
			contractModelView.setWorkTelephone(applyJobDto.getUnitTelephone() + "--" + applyJobDto.getExtensionNum());
		}
	}

	private void initRepaymentReminder(Integer subState, Model model, String applyLoanNo, String contractNo,
			String loanTime, BigDecimal contractAmount, BigDecimal interestAmount, Integer num, BigDecimal periods,
			BigDecimal repayMon) {
		List<JkRepaymentPlanDto> repayments = new ArrayList<>();
		String repayDate = loanTime.substring(loanTime.lastIndexOf("-") + 1);
		model.addAttribute("repayDay", repayDate);
		if (!SignBisStateEnum.BACK_INIT.getCode().equals(subState)
				&& !SignBisStateEnum.INIT.getCode().equals(subState)) {// 合规退回
			// 重新生成最新产品费率的数据
			repayments = repaymentPlanService.selectByApplyLoanNo(applyLoanNo).getData();
		}
		if (CollectionUtils.isNotEmpty(repayments)) {
			int i = 0;
			for (JkRepaymentPlanDto repaymentPlan : repayments) {
				if (i == 1) {
					model.addAttribute("repayAmount", repaymentPlan.getReapyAmount());
					break;
				}
				i++;
			}
			model.addAttribute("repayPlans", repayments);
		} else {
			List<JkRepaymentPlanDto> repayPlans = new ArrayList<>();
			BigDecimal initialCapital = new BigDecimal("0");// temp当前期初本金
			BigDecimal endPrincipal = new BigDecimal("0");// 当前期期末本金余额
			BigDecimal repayAmount = new BigDecimal("0");// 月还款额
			BigDecimal returnAmount = new BigDecimal("0");// 结算返还服务费(提前结清减免)
			BigDecimal aheadAmount = new BigDecimal("0");// 提前结清金额
			JkRepaymentPlanDto repay = null;
			int accumulation = 0;
			for (int i = num; i >= 1; i--) {
				accumulation++;
				repay = new JkRepaymentPlanDto();
				repay.setApplyLoanNo(applyLoanNo);
				repay.setContractNo(contractNo);
				repay.setRepayState(RepayStateEnum.NO_REPAY.getCode()); // 还款状态
				repay.setCreateTime(DateUtils.getCurrentDateTime());
				repay.setUpdateTime(DateUtils.getCurrentDateTime());
				repay.setCreateUser(ShiroUtils.getCurrentUserName());
				repay.setDelflag(Constants.YES_INT);
				String agreeRepayDate = DateTimeUtil.getAddMonth(loanTime, accumulation);// 用户账单还款开始日期
				repay.setAgreeRepayDate(DateTimeUtil.convertStringToDate(agreeRepayDate));// 协议还款日期
				repay.setRepayPeriod(new Integer(accumulation));// 期数
				// 月还本金 = 合同金额/期数
				BigDecimal principal = contractAmount.divide(periods, 2, BigDecimal.ROUND_HALF_UP);
				// 月还利息
				BigDecimal interest = interestAmount.divide(periods, 2, BigDecimal.ROUND_HALF_UP);
				if (num == i) {
					BigDecimal p1 = principal.setScale(0, RoundingMode.DOWN);
					BigDecimal p2 = principal.subtract(p1);// 小数位金额
					BigDecimal firstPrinciple = p2.multiply(periods);
					principal = p1.add(firstPrinciple).setScale(0, BigDecimal.ROUND_HALF_UP);

					BigDecimal i1 = interest.setScale(0, RoundingMode.DOWN);
					BigDecimal i2 = interest.subtract(i1);// 小数位金额
					BigDecimal firstInterest = i2.multiply(periods);
					interest = i1.add(firstInterest).setScale(0, BigDecimal.ROUND_HALF_UP);

					repayAmount = principal.add(interest);
					// 当前期初本金余额
					initialCapital = contractAmount;
					// 期末本金余额 = 期初本金余额 - 月还本金
					endPrincipal = initialCapital.subtract(principal);

					repay.setPrincipal(principal);
					repay.setInterest(interest);
					repay.setReapyAmount(repayAmount);
					repay.setInitialPrincipal(initialCapital);
					repay.setEndPrincipal(endPrincipal);
				} else {
					initialCapital = endPrincipal;// 当前期初本金余额
					BigDecimal p1 = principal.setScale(0, RoundingMode.DOWN);
					BigDecimal i1 = interest.setScale(0, RoundingMode.DOWN);
					endPrincipal = initialCapital.subtract(p1);// 当前期末本金余额
					repay.setPrincipal(principal.setScale(0, RoundingMode.DOWN));
					repay.setInterest(interest.setScale(0, RoundingMode.DOWN));
					// 月还款额 = 月还本金 + 月还利息
					repayAmount = p1.add(i1);
					repay.setReapyAmount(repayAmount);
					repay.setInitialPrincipal(initialCapital);
					repay.setEndPrincipal(endPrincipal);
				}
				// 新增提前还款计划
				if (twelvePeriods.equals(num)) {
					if (accumulation <= 2) {
						// 提前还款减免
						returnAmount = repayMon.multiply(prepaymentRateFirst).setScale(0, BigDecimal.ROUND_HALF_UP);
						repay.setReturnAmount(returnAmount);
						// 提前还款金额=月还款额+期末本金余额-提前结清减免
						aheadAmount = repayAmount.add(endPrincipal).subtract(returnAmount);
					}
					if (accumulation <= 4 && accumulation > 2) {
						returnAmount = repayMon.multiply(prepaymentRateSecond).setScale(0, BigDecimal.ROUND_HALF_UP);
						repay.setReturnAmount(returnAmount);
						aheadAmount = repayAmount.add(endPrincipal).subtract(returnAmount);
					}
					if (accumulation > 4) {
						// 提前还款金额=月还款额+期末本金余额-提前结清减免
						aheadAmount = repayAmount.add(endPrincipal);
					}
				}
				if (twentyFourPeriods.equals(num) || thirtySixPeriods.equals(num)) {
					if (accumulation <= 3) {
						returnAmount = repayMon.multiply(prepaymentRateFirst).setScale(0, BigDecimal.ROUND_HALF_UP);
						repay.setReturnAmount(returnAmount);
						aheadAmount = repayAmount.add(endPrincipal).subtract(returnAmount);
					}
					if (accumulation <= 6 && accumulation > 3) {
						returnAmount = repayMon.multiply(prepaymentRateSecond).setScale(0, BigDecimal.ROUND_HALF_UP);
						repay.setReturnAmount(returnAmount);
						aheadAmount = repayAmount.add(endPrincipal).subtract(returnAmount);
					}
					if (accumulation > 6) {
						// 提前还款金额=月还款额+期末本金余额-提前结清减免
						aheadAmount = repayAmount.add(endPrincipal);
					}
				}
				repay.setAheadAmount(aheadAmount);
				repay.setIsEffective(IsAdvanceSettleEnum.NO.getCode());
				String str = String.format("%02d", accumulation);
				repay.setBillNo(contractNo+str);
				repayPlans.add(repay);
			}
			List<JkRepaymentPlanDto> jkRepayments = repaymentPlanService.selectByApplyLoanNo(applyLoanNo).getData();
			if (CollectionUtils.isNotEmpty(jkRepayments)) {
				repaymentPlanService.delete(jkRepayments);
			}
			repaymentPlanService.insert(repayPlans);
			model.addAttribute("repayAmount", repayAmount);
			model.addAttribute("repayPlans", repayPlans);
		}
	}

	private void initCreditCounselingManageService(Integer subState, String applyLoanNo, BigDecimal repayMon,
			String contractNo, BigDecimal contractAmount, CityCodeDto cityCodeDto,
			ContractModelView contractModelView) {
		JkContractDto contractDto = new JkContractDto();
		if (!SignBisStateEnum.BACK_INIT.getCode().equals(subState)
				&& !SignBisStateEnum.INIT.getCode().equals(subState)) {// 合规退回
			// 重新生成最新产品费率的数据
			contractDto = contractService.selectByApplyLoanNo(applyLoanNo).getData();
		}
		contractModelView.setLoanNo(contractNo);
		if (contractDto != null && contractDto.getContractAmount() != null && contractDto.getRepayMoney() != null) {
			contractAmount = contractDto.getContractAmount();
			repayMon = contractDto.getRepayMoney();
			// 费用合计= 合同金额-到手金额
			BigDecimal totalCost = contractAmount.subtract(repayMon).setScale(0, BigDecimal.ROUND_HALF_UP);
			contractModelView.setTotalCost(totalCost);
			contractModelView.setTotalCostCN(NumberToCN.number2CNMontrayUnit(totalCost));
			// 签约金额、合同金额、借款金额
			contractModelView.setContractAmount(contractAmount);
			contractModelView.setContractAmountCN(NumberToCN.number2CNMontrayUnit(contractAmount));
			// 风险金
			BigDecimal securityFund = repayMon.multiply(new BigDecimal(securityRate)).setScale(0,
					BigDecimal.ROUND_HALF_UP);
			contractModelView.setSecurityFund(securityFund);
			contractModelView.setSecurityFundCN(NumberToCN.number2CNMontrayUnit(securityFund));
			// 咨询费、审核费、服务费 = 费用合计 - 风险金额
			BigDecimal serviceMoney = totalCost.subtract(securityFund);
			contractModelView.setServiceMoney(serviceMoney);
			contractModelView.setServiceMoneyCN(NumberToCN.number2CNMontrayUnit(serviceMoney));
		} else {
			// 费用合计= 合同金额-到手金额
			BigDecimal totalCost = contractAmount.subtract(repayMon).setScale(0, BigDecimal.ROUND_HALF_UP);
			contractModelView.setTotalCost(totalCost);
			contractModelView.setTotalCostCN(NumberToCN.number2CNMontrayUnit(totalCost));
			// 签约金额、合同金额、借款金额
			contractModelView.setContractAmount(contractAmount);
			contractModelView.setContractAmountCN(NumberToCN.number2CNMontrayUnit(contractAmount));
			// 风险金
			BigDecimal securityFund = repayMon.multiply(new BigDecimal(securityRate)).setScale(0,
					BigDecimal.ROUND_HALF_UP);
			contractModelView.setSecurityFund(securityFund);
			contractModelView.setSecurityFundCN(NumberToCN.number2CNMontrayUnit(securityFund));
			// 咨询费、审核费、服务费 = 费用合计 - 风险金额
			BigDecimal serviceMoney = totalCost.subtract(securityFund);
			contractModelView.setServiceMoney(serviceMoney);
			contractModelView.setServiceMoneyCN(NumberToCN.number2CNMontrayUnit(serviceMoney));
		}
	}

	private void initCreditLoanProtocol(Integer subState, String applyLoanNo, String bankNo, String bankName,
			BigDecimal contractAmount, BigDecimal interestAmount, Integer num, BigDecimal periods,
			ContractModelView contractModelView) {
		if (!SignBisStateEnum.BACK_INIT.getCode().equals(subState)
				&& !SignBisStateEnum.INIT.getCode().equals(subState)) {// 合规退回
			// 重新生成最新产品费率的数据
			JkContractDto contractDto = contractService.selectByApplyLoanNo(applyLoanNo).getData();
			if (contractDto != null && contractDto.getContractAmount() != null) {
				contractAmount = contractDto.getContractAmount();
				interestAmount = contractDto.getInterestAmount();
			}
		}
		contractModelView.setContractAmount(contractAmount);
		// 月还本金 = 合同金额/期数
		BigDecimal principal = contractAmount.divide(periods, 2, BigDecimal.ROUND_HALF_UP).setScale(0,
				RoundingMode.DOWN);
		// 月还利息
		BigDecimal interest = interestAmount.divide(periods, 2, BigDecimal.ROUND_HALF_UP).setScale(0,
				RoundingMode.DOWN);
		// 月还款额 = 月还本金 + 月还利息
		BigDecimal reapyAmount = principal.add(interest);

		String principalCN = NumberToCN.number2CNMontrayUnit(reapyAmount);
		contractModelView.setPrincipalCN(principalCN);// 月还款本息数额
		String contractCN = NumberToCN.number2CNMontrayUnit(contractAmount.setScale(0, BigDecimal.ROUND_HALF_UP));
		contractModelView.setContractCN(contractCN);// 借款本金数额

		String[] repayMoneyArray = NumberToCN.numberStrToArray(contractAmount.setScale(0, BigDecimal.ROUND_HALF_UP));
		String[] principalArray = NumberToCN.numberStrToArray(reapyAmount.setScale(0, BigDecimal.ROUND_HALF_UP));

		contractModelView.setRepayMoneyArray(repayMoneyArray);// 借款本金数额
		contractModelView.setPrincipalArray(principalArray);// 月还款本息数额

		contractModelView.setRepayMoneyLength(numCount - repayMoneyArray.length);
		contractModelView.setPrincipalLength(numCount - principalArray.length);
		contractModelView.setProductPeriods(num);
		contractModelView.setBankNo(bankNo);
		contractModelView.setBankName(bankName);
	}

	/**
	 * 签约拒绝
	 * 
	 * @名称 rollBack
	 * @描述 TODO
	 * @返回类型 AjaxResult
	 * @日期 2016年10月8日 下午2:30:44
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/signRefuse")
	@ResponseBody
	public AjaxResult signRefuse(String applyLoanNo, String interiorRemark, Model model) {
		String operatorName = ShiroUtils.getCurrentUserName();
		Long operatorId = ShiroUtils.getCurrentUserId();
		MyphLogger.info("签约管理发起拒绝流程,输入参数,申请单号：{},原因:{},当前操作人:{},操作人编号:{}", applyLoanNo, interiorRemark, operatorName,
				operatorId);
		RejectActionDto rejectActionDto = new RejectActionDto();
		rejectActionDto.setApplyLoanNo(applyLoanNo);
		rejectActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
		rejectActionDto.setRejectDays(Constant.CONFINE_TIME.getCode());
		rejectActionDto.setFlowStateEnum(FlowStateEnum.SIGN);
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
			MyphLogger.error("签约管理发起门店拒绝异常", e);
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
		MyphLogger.info("签约管理发起放弃流程,输入参数,申请单号：{},原因:{},当前操作人:{},操作人编号:{}", applyLoanNo, interiorRemark, operatorName,
				operatorId);
		AbandonActionDto abandonActionDto = new AbandonActionDto();
		abandonActionDto.setApplyLoanNo(applyLoanNo);
		abandonActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
		abandonActionDto.setFlowStateEnum(FlowStateEnum.SIGN);
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
	private void initQueryDate(SignQueryDto queryDto) {
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
		if (null == queryDto.getPassTimeStart()) {
			queryDto.setPassTimeStart(twoWeekBefore);
		}
		if (null == queryDto.getPassTimeEnd()) {
			queryDto.setPassTimeEnd(today);
		}
	}

	/**
	 * 合同类型
	 * 
	 * @ClassName: ContarctTypeEnum
	 * @Description: TODO
	 * @author hf
	 * @date 2016年10月27日 下午7:25:19
	 *
	 */
	public enum ContractEnum {
		// 利用构造函数传参
		CREDIT_LOAN_PROTOCOL(1, "借款协议", "credit_loan_protocol"), CREDIT_COUNSELING_MANAGE_SERVICE(2, "信用咨询及管理服务协议",
				"credit_counseling_manage_service"), REPAYMENT_REMINDER(3, "按期还款温馨提示",
						"repayment_reminder"), FY_ACCOUNT_SPECIAL_PROTOCOL(4, "富友-麦芽金服数据科技有限公司专用账户协议",
								"fy_account_special_protocol"), CUSTOMER_CONTRACT_VERIFICATION_FORM(5, "签约核查表",
										"customer_contract_verification_form"), ENTRUST_DEBIT_AUTHORIZATION(6,
												"委托扣款授权书", "entrust_debit_authorization");

		private int type;

		private String desc;

		private String contarctName;

		ContractEnum(int type, String name, String contarctName) {
			this.desc = name;
			this.type = type;
			this.contarctName = contarctName;
		}

		public int getType() {
			return type;
		}

		public static String getViewName(int type) {
			for (ContractEnum e : ContractEnum.values()) {
				if (e.getType() == type) {
					return e.getContarctName();
				}
			}
			return null;
		}

		public String getDesc() {
			return desc;
		}

		public String getContarctName() {
			return contarctName;
		}
	}

	/**
	 * 
	 * @名称 contractDetail
	 * @描述 合同详情
	 * @返回类型 String
	 * @日期 2016年10月25日 下午7:15:49
	 * @创建人 王海波
	 * @更新人 王海波
	 *
	 */
	@RequestMapping("/contractDetail")
	public String contractDetail(Model model, String applyLoanNo) {
		model.addAttribute("applyLoanNo", applyLoanNo);
		ApplyInfoDto applyInfo = applyInfoService.queryInfoByAppNo(applyLoanNo).getData();// 查询申请单信息
		ApplyUserDto applyUserDto = applyUserService.queryInfoByLoanNo(applyLoanNo).getData();// 查询用户基本信息
		String productName = productService.getProductNameById(applyInfo.getProductType()).getData();
		JkContractDto jkContractDto = contractService.selectByApplyLoanNo(applyLoanNo).getData();
		ProductDto productDto = productService.selectByPrimaryKey(applyInfo.getProductType()).getData();
		List<JkSignDto> signList = signService.selectListByApplyLoanNo(applyLoanNo).getData();
		if (applyUserDto != null) {
			String mailAddress = applyUserDto.getMailAddress();
			// 邮寄地址:1,现住址;2,公司地址;3,户籍地址
			if ("1".equals(mailAddress)) {
				applyUserDto.setMailAddress(applyUserDto.getLiveAddr() + applyUserDto.getLiveAddress());
			} else if ("2".equals(mailAddress)) {
				JkApplyJobDto applyJobDto = jobInfoService.selectJobInfoByAppNO(applyLoanNo).getData();
				if (applyJobDto != null) {
					applyUserDto.setMailAddress(applyJobDto.getCompanyAddress() + "--" + applyJobDto.getDetailAddr());
				}
			} else if ("3".equals(mailAddress)) {
				applyUserDto.setMailAddress(applyUserDto.getCensusAddr() + applyUserDto.getCensusAddress());
			}
		}
		model.addAttribute("productDto", productDto);
		model.addAttribute("jkContractDto", jkContractDto);
		model.addAttribute("productName", productName);
		model.addAttribute("appInfo", applyInfo);
		model.addAttribute("applyUserDto", applyUserDto);
		model.addAttribute("signList", signList);
		return "/apply/progress/contract_detail";
	}

	class MentionLoanAmount {
		private boolean isCheck;// 金额校验是否通过
		private String message;// 提示信息
		private BigDecimal repayMoneyAfter;// 放款金额
		private ProductDto productDto;// 产品配置信息

		public boolean isCheck() {
			return isCheck;
		}

		public void setCheck(boolean isCheck) {
			this.isCheck = isCheck;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public BigDecimal getRepayMoneyAfter() {
			return repayMoneyAfter;
		}

		public void setRepayMoneyAfter(BigDecimal repayMoneyAfter) {
			this.repayMoneyAfter = repayMoneyAfter;
		}

		public ProductDto getProductDto() {
			return productDto;
		}

		public void setProductDto(ProductDto productDto) {
			this.productDto = productDto;
		}
	}
}