/**   
 * @Title: JkApplyAuditController.java 
 * @Package: com.myph.manage.audit.controller
 * @company: 麦芽金服
 * @Description: TODO
 * @date 2016年9月20日 下午9:17:42 
 * @version V1.0   
 */
package com.myph.manage.controller.audit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.dto.JkAuditDto;
import com.myph.apply.dto.JkAuditTaskDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.approvetask.dto.ApproveTaskDto;
import com.myph.approvetask.service.ApproveTaskService;
import com.myph.audit.service.JkApplyAuditService;
import com.myph.auditlog.service.AuditLogService;
import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.DateUtils;
import com.myph.constant.ApplyUtils;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.bis.AbandonBisStateEnum;
import com.myph.constant.bis.AuditDirectorBisStateEnum;
import com.myph.constant.bis.AuditFirstBisStateEnum;
import com.myph.constant.bis.AuditLastBisStateEnum;
import com.myph.constant.bis.AuditManagerBisStateEnum;
import com.myph.constant.bis.ExternalFirstBisStateEnum;
import com.myph.constant.bis.ExternalLastBisStateEnum;
import com.myph.flow.dto.AbandonActionDto;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.controller.BaseController;
import com.myph.manage.facadeService.FacadeFlowStateExchangeService;
import com.myph.node.service.NodeService;
import com.myph.organization.dto.OrganizationDto;
import com.myph.organization.service.OrganizationService;
import com.myph.product.service.ProductService;
import com.myph.teamProduct.dto.TeamProductDto;
import com.myph.teamProduct.service.TeamProductService;

/**
 * 信审业务管理
 * 
 * @ClassName: JkApplyAuditController
 * @Description: TODO
 * @author hf
 * @date 2016年9月20日 下午9:17:42
 * 
 */
@Controller
@RequestMapping("/audit")
public class JkApplyAuditController extends BaseController {

	/** 当前登录人角色 */
	private static final String ROLE_NAME = "roleName";

	private static final String TODO_SORT = "SUBSTATE DESC,a.fetchTime";

	private static final String DONE_SORT = "a.auditFirstTime DESC,a.auditReviewTime DESC,a.lastDate DESC,a.superLastDate DESC";

	@Autowired
	private JkApplyAuditService jkApplyAuditService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private ApplyInfoService applyInfoService;

	@Autowired
	private ApproveTaskService approveTaskService;

	@Autowired
	private ProductService proService;

	@Autowired
	private AuditLogService auditLogService;

	@Autowired
	private NodeService nodeService;

	@Autowired
	private FacadeFlowStateExchangeService facadeFlowStateExchangeService;
	
	@Autowired
    private TeamProductService teamProductService;
	
	@Autowired
    private ProductService productService;

	/**
	 * 初审入口
	 * 
	 * @名称 firstAuditMain
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年9月20日 下午9:25:05
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/firstAuditMain")
	public String firstAuditMain() {
		ShiroUtils.getSession().setAttribute(Auditor.AUDIT.name, Auditor.FIRSTAUDITOR.name);
		return redirectUrl("/audit/list/todo.htm");
	}

	/**
	 * 复审入口
	 * 
	 * @名称 reviewAuditMain
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年9月20日 下午9:25:05
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/reviewAuditMain")
	public String reviewAuditMain() {
		ShiroUtils.getSession().setAttribute(Auditor.AUDIT.name, Auditor.LASTAUDITOR.name);
		return redirectUrl("/audit/list/todo.htm");
	}

	/**
	 * 经理入口
	 * 
	 * @名称 managerAuditMain
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年9月20日 下午9:25:05
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/managerAuditMain")
	public String managerAuditMain() {
		ShiroUtils.getSession().setAttribute(Auditor.AUDIT.name, Auditor.MANAGERAUDITOR.name);
		return redirectUrl("/audit/list/manageTodo.htm");
	}

	/**
	 * 总监入口
	 * 
	 * @名称 directorAuditorMain
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年9月20日 下午9:25:05
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/directorAuditorMain")
	public String directorAuditorMain() {
		ShiroUtils.getSession().setAttribute(Auditor.AUDIT.name, Auditor.DIRECTORAUDITOR.name);
		return redirectUrl("/audit/list/manageTodo.htm");
	}

	/**
	 * 初审/复审信审列表(待办)
	 * 
	 * @名称 list
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年9月20日 下午9:25:05
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/list/todo")
	public String todoList(Model model, JkAuditDto queryDto, BasePage basePage) {
		try {
			queryDto.setEmployeeId(ShiroUtils.getCurrentUser().getId());// 必传
			int accountType = ShiroUtils.getAccountType();
			String auditor = (String) ShiroUtils.getSessionAttribute(Auditor.AUDIT.name);

			model.addAttribute("progress", ApplyAuditType.TODO.name);
			MyphLogger.debug("当前用户的账户类型是:{},角色是:{}", accountType, auditor);
			Integer auditResult = 0;
			if (Auditor.FIRSTAUDITOR.name.equals(auditor)) {
				initFirstTodoAuditParams(model, queryDto, accountType, auditor);
				AjaxResult getproductIdResult = getproductIdByteamId();
	            if(!getproductIdResult.isSuccess()){
	                auditResult = 0;
	            }else{
	                auditResult = jkApplyAuditService
	                        .getAudits(Auditor.FIRSTAUDITOR.name, AuditFirstBisStateEnum.INIT.getCode(),(List<Integer>)getproductIdResult.getData()).getData();// 获取待初审的未取件数据
	            }
			} else if (Auditor.LASTAUDITOR.name.equals(auditor)) {// 复审无需取件
				initLastTodoAuditParams(model, queryDto, accountType, auditor);
				// auditResult =
				// jkApplyAuditService.getAudits(Auditor.LASTAUDITOR.name,
				// AuditLastBisStateEnum.INIT.getCode());// 获取待复审的未取件数据
			}

			ServiceResult<Pagination<JkAuditDto>> result = null;
			Pagination<JkAuditDto> page = null;
			List<JkAuditDto> audits = null;
			if (DONE_SORT.equals(basePage.getSortField())) {// 特殊处理
				basePage.setSortField(null);
			}
			if (TODO_SORT.equals(basePage.getSortField())) {
				queryDto.setDefaultSort("default");
			}
			if (StringUtils.isEmpty(basePage.getSortField())) {
				basePage.setSortField(TODO_SORT);
				queryDto.setDefaultSort("default");
				basePage.setSortOrder(null);
			}
			result = jkApplyAuditService.getJkApplyAuditByTODO(queryDto, basePage);
			page = result.getData();
			audits = result.getData().getResult();
			for (JkAuditDto jkAuditDto : audits) {
				ServiceResult<OrganizationDto> orgDtoResult = organizationService
						.selectOrganizationById(jkAuditDto.getStoreId());
				if (orgDtoResult.success() && orgDtoResult.getData() != null) {
					jkAuditDto.setStoreName(orgDtoResult.getData().getOrgName());
				}
				jkAuditDto.setStateDesc(ApplyUtils.getFullStateDesc(jkAuditDto.getState(), jkAuditDto.getSubState()));
			}
			model.addAttribute("page", page);
			page.setResult(audits);
			model.addAttribute("auditSize", auditResult);// 未取件数
		} catch (Exception e) {
			MyphLogger.error("初审/复审信审列表（待办）加载异常", e);
		}
		return "/apply/audit/audit_list";
	}

	/**
	 * 初审/复审信审列表(已办)
	 * 
	 * @名称 list
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年9月20日 下午9:25:05
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/list/done")
	public String doneList(Model model, JkAuditDto queryDto, BasePage basePage) {
		try {
			queryDto.setEmployeeId(ShiroUtils.getCurrentUser().getId());// 必传
			int accountType = ShiroUtils.getAccountType();// 1：管理员 0：普通
			String auditor = (String) ShiroUtils.getSessionAttribute(Auditor.AUDIT.name);

			model.addAttribute("progress", ApplyAuditType.DONE.name);
			MyphLogger.debug("当前用户的账户类型是:{},角色是:{}", accountType, auditor);
			if (DONE_SORT.equals(basePage.getSortField())) {
				queryDto.setDefaultSort("default");
			}
			if (TODO_SORT.equals(basePage.getSortField())) {// 特殊处理
				basePage.setSortField(null);
			}
			if (StringUtils.isEmpty(basePage.getSortField())) {
				basePage.setSortField(DONE_SORT);
				queryDto.setDefaultSort("default");
				basePage.setSortOrder(null);
			}
			if (Auditor.FIRSTAUDITOR.name.equals(auditor)) {// 如果是初审 需要
				/** 如果当前用户是系统管理员，会重置前面的查询条件 */
				initDoneFirstAuditParams(model, queryDto, accountType, auditor);// 初始化初审已办列表查询条件
			} else if (Auditor.LASTAUDITOR.name.equals(auditor)) {
				initDoneLastAuditParams(model, queryDto, accountType, auditor);// 初始化复审已办列表查询条件
			}

			Pagination<JkAuditDto> page = jkApplyAuditService.getJkApplyAuditByDONE(queryDto, basePage).getData();

			List<JkAuditDto> historyAudits = page.getResult();
			if (CollectionUtils.isNotEmpty(historyAudits)) {
				for (JkAuditDto jkAuditDto : historyAudits) {
					ServiceResult<OrganizationDto> orgDtoResult = organizationService
							.selectOrganizationById(jkAuditDto.getStoreId());
					if (orgDtoResult.success() && orgDtoResult.getData() != null) {
						jkAuditDto.setStoreName(orgDtoResult.getData().getOrgName());
					}
				}
			}
			page.setResult(historyAudits);
			model.addAttribute("page", page);
		} catch (Exception e) {
			MyphLogger.error("初审/复审信审列表（已办）加载异常", e);
		}
		return "/apply/audit/audit_list";
	}

	/**
	 * 初始化复审人员/管理员的查询条件
	 * 
	 * @名称 initLastAuditParams
	 * @描述 TODO
	 * @返回类型 void
	 * @日期 2016年10月20日 上午10:45:45
	 * @创建人 hf
	 * @更新人 hf
	 * 
	 */
	private void initLastTodoAuditParams(Model model, JkAuditDto queryDto, int accountType, String auditor) {
		List<Integer> stateList = new ArrayList<Integer>();
		List<Integer> subStateList = new ArrayList<Integer>();
		queryDto.setAuditor(auditor);
		model.addAttribute(ROLE_NAME, Auditor.LASTAUDITOR.name);
		if (Constants.YES_INT == accountType) {// 管理员
			queryDto.setAuditor(Auditor.SYSTEM_MANAGER.name);
			model.addAttribute(ROLE_NAME, Auditor.SYSTEM_MANAGER.name);
		}
		stateList.add(FlowStateEnum.AUDIT_LASTED.getCode());// 待复审主流程
		stateList.add(FlowStateEnum.EXTERNAL_LAST.getCode());// 复审待外访主流程
		queryDto.setStateList(StringUtils.join(stateList, ","));

		subStateList.add(AuditLastBisStateEnum.INIT.getCode());
		subStateList.add(AuditLastBisStateEnum.BACK_INIT.getCode());
		subStateList.add(ExternalLastBisStateEnum.PASSED.getCode());
		queryDto.setSubStateList(StringUtils.join(subStateList, ","));
	}

	/**
	 * 初始化初审人员/管理员的查询条件
	 * 
	 * @名称 initAuditParams
	 * @描述 TODO
	 * @返回类型 void
	 * @日期 2016年10月20日 上午10:44:37
	 * @创建人 hf
	 * @更新人 hf
	 * 
	 */
	private void initFirstTodoAuditParams(Model model, JkAuditDto queryDto, int accountType, String auditor) {
		List<Integer> stateList = new ArrayList<Integer>();
		List<Integer> subStateList = new ArrayList<Integer>();
		queryDto.setAuditor(auditor);
		model.addAttribute(ROLE_NAME, Auditor.FIRSTAUDITOR.name);
		if (Constants.YES_INT == accountType) {// 管理员
			queryDto.setAuditor(Auditor.SYSTEM_MANAGER.name);
			model.addAttribute(ROLE_NAME, Auditor.SYSTEM_MANAGER.name);
		}
		stateList.add(FlowStateEnum.AUDIT_FIRST.getCode());// 待初审主流程
		stateList.add(FlowStateEnum.EXTERNAL_FIRST.getCode());// 初审待外访主流程
		queryDto.setStateList(StringUtils.join(stateList, ","));

		subStateList.add(AuditFirstBisStateEnum.INIT.getCode());
		subStateList.add(AuditFirstBisStateEnum.BACK_INIT.getCode());
		subStateList.add(ExternalFirstBisStateEnum.PASSED.getCode());
		queryDto.setSubStateList(StringUtils.join(subStateList, ","));
	}

	/**
	 * 信审列表已办查询条件初始化
	 * 
	 * @param model
	 * @param queryDto
	 * @param accountType
	 */
	private void initDoneFirstAuditParams(Model model, JkAuditDto queryDto, int accountType, String auditor) {
		queryDto.setAuditor(auditor);
		model.addAttribute(ROLE_NAME, Auditor.FIRSTAUDITOR.name);
		if (Constants.YES_INT == accountType) {// 管理员
			queryDto.setAuditor(Auditor.SYSTEM_MANAGER.name);
			model.addAttribute(ROLE_NAME, Auditor.SYSTEM_MANAGER.name);
		}
	}

	/**
	 * 信审列表已办查询条件初始化
	 * 
	 * @param model
	 * @param queryDto
	 * @param accountType
	 */
	private void initDoneLastAuditParams(Model model, JkAuditDto queryDto, int accountType, String auditor) {
		queryDto.setAuditor(auditor);
		model.addAttribute(ROLE_NAME, Auditor.LASTAUDITOR.name);
		if (Constants.YES_INT == accountType) {// 管理员
			queryDto.setAuditor(Auditor.SYSTEM_MANAGER.name);
			model.addAttribute(ROLE_NAME, Auditor.SYSTEM_MANAGER.name);
		}
	}

	/**
	 * 取件
	 * 
	 * @名称 list
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年9月21日 下午9:53:22
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping(value = "/pickup", method = RequestMethod.POST)
	@ResponseBody
	public synchronized AjaxResult pickup() {
		Long employeeId = 0l;
		String applyLoanNo = "";
		try {
			employeeId = ShiroUtils.getCurrentUser().getId();
			String operatorName = ShiroUtils.getCurrentUserName();
			MyphLogger.info("审批列表{}执行取件功能,操作人编号:{}", operatorName, employeeId);
			ServiceResult<String> result = null;
			List<JkAuditTaskDto> audits = null;
			String auditor = (String) ShiroUtils.getSessionAttribute(Auditor.AUDIT.name);
			if (Auditor.FIRSTAUDITOR.name.equals(auditor)) {
			    //获取当前用户可以取件的产品类型
			    AjaxResult getproductIdResult = getproductIdByteamId();
			    if(!getproductIdResult.isSuccess()){
			        return getproductIdResult;
			    }
				audits = jkApplyAuditService
						.getAuditResult(Auditor.FIRSTAUDITOR.name, AuditFirstBisStateEnum.INIT.getCode(),(List<Integer>)getproductIdResult.getData()).getData();
				if (CollectionUtils.isEmpty(audits)) {
					return AjaxResult.failed("无待审批申请单");
				}
				JkAuditTaskDto jkAuditTaskDto = audits.get(0);
				applyLoanNo = jkAuditTaskDto.getApplyLoanNo();
				JkAuditTaskDto param = new JkAuditTaskDto();
				param.setId(jkAuditTaskDto.getId());
				param.setFirstAuditor(employeeId);
				result = jkApplyAuditService.pickup(param);
			} else if (Auditor.LASTAUDITOR.name.equals(auditor)) {
				audits = jkApplyAuditService
						.getAuditResult(Auditor.LASTAUDITOR.name, AuditLastBisStateEnum.INIT.getCode(),null).getData();
				if (CollectionUtils.isEmpty(audits)) {
					return AjaxResult.failed("无待审批申请单");
				}
				JkAuditTaskDto jkAuditTaskDto = audits.get(0);
				applyLoanNo = jkAuditTaskDto.getApplyLoanNo();
				JkAuditTaskDto param = new JkAuditTaskDto();
				param.setId(jkAuditTaskDto.getId());
				param.setReviewAuditor(employeeId);
				result = jkApplyAuditService.pickup(param);
			} else if (Auditor.MANAGERAUDITOR.name.equals(auditor)) {
			    AjaxResult getproductIdResult = getproductIdByteamId();
                if(!getproductIdResult.isSuccess()){
                    return getproductIdResult;
                }
				audits = jkApplyAuditService
						.getAuditResult(Auditor.MANAGERAUDITOR.name, AuditManagerBisStateEnum.INIT.getCode(),(List<Integer>)getproductIdResult.getData()).getData();
				if (CollectionUtils.isEmpty(audits)) {
					return AjaxResult.failed("无待审批申请单");
				}
				JkAuditTaskDto jkAuditTaskDto = audits.get(0);
				applyLoanNo = jkAuditTaskDto.getApplyLoanNo();
				JkAuditTaskDto param = new JkAuditTaskDto();
				param.setId(jkAuditTaskDto.getId());
				param.setLastAuditor(employeeId);
				result = jkApplyAuditService.pickup(param);
			} else if (Auditor.DIRECTORAUDITOR.name.equals(auditor)) {
			    AjaxResult getproductIdResult = getproductIdByteamId();
                if(!getproductIdResult.isSuccess()){
                    return getproductIdResult;
                }
				audits = jkApplyAuditService
						.getAuditResult(Auditor.DIRECTORAUDITOR.name, AuditDirectorBisStateEnum.INIT.getCode(),(List<Integer>)getproductIdResult.getData())
						.getData();
				if (CollectionUtils.isEmpty(audits)) {
					return AjaxResult.failed("无待审批申请单");
				}
				JkAuditTaskDto jkAuditTaskDto = audits.get(0);
				applyLoanNo = jkAuditTaskDto.getApplyLoanNo();
				JkAuditTaskDto param = new JkAuditTaskDto();
				param.setId(jkAuditTaskDto.getId());
				param.setSuperLastAuditor(employeeId);
				result = jkApplyAuditService.pickup(param);
			}
			MyphLogger.info("员工{},编号:{}执行取件成功,获取的申请单号:{}", operatorName, employeeId, applyLoanNo);
			return AjaxResult.formatFromServiceResult(result);
		} catch (Exception e) {
			MyphLogger.error(e, "取件异常");
			return AjaxResult.failed("无待审批申请单");
		}
	}

	/**
	 * 
	 * @名称 getproductIdByteamId 
	 * @描述 获取当前用户可以取件的产品id
	 * @返回类型 AjaxResult     
	 * @日期 2017年4月17日 下午4:00:33
	 * @创建人  吴阳春
	 * @更新人  吴阳春
	 *
	 */
	private AjaxResult getproductIdByteamId(){
	    Long teamId = ShiroUtils.getCurrentUser().getTeamId();
        if(teamId == null){
            return AjaxResult.failed("当前员工不在团队中，不能取件");
        }
        ServiceResult<TeamProductDto> teamProductResult = teamProductService.selectByTeamId(teamId);
        String productTypes = teamProductResult.getData().getProductTypes();
        String[] productTypeArray = productTypes.split("\\|");
        List<String> productTypeList = Arrays.asList(productTypeArray);
        if(productTypeList.size() <= 0){
            return AjaxResult.failed("当前员工不在团队中，不能取件");
        }
        List<Long> result = new ArrayList<Long>();
        for(String str : productTypeList) {
            result.add(Long.valueOf(str));
          }
        ServiceResult<List<Long>> productIds = productService.selectIdByTypes(result);
        return AjaxResult.success(productIds.getData());
	}
	/**
	 * 经理/总监信审列表(待办)
	 * 
	 * @名称 list
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年9月20日 下午9:25:05
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/list/manageTodo")
	public String manageTodoList(Model model, JkAuditDto queryDto, BasePage basePage) {
		try {
			queryDto.setEmployeeId(ShiroUtils.getCurrentUser().getId());// 必传
			int accountType = ShiroUtils.getAccountType();
			String auditor = (String) ShiroUtils.getSessionAttribute(Auditor.AUDIT.name);
			model.addAttribute("progress", ApplyAuditType.MANAGETODO.name);
			MyphLogger.debug("当前用户的账户类型是:{},角色是:{}", accountType, auditor);
			Integer auditResult = 0;
			AjaxResult getproductIdResult = getproductIdByteamId();
            if(!getproductIdResult.isSuccess()){
                auditResult = 0;
            }else{
    			if (Auditor.MANAGERAUDITOR.name.equals(auditor)) {
    				initManagerTodoAuditParams(model, queryDto, accountType, auditor);
    				auditResult = jkApplyAuditService
    						.getAudits(Auditor.MANAGERAUDITOR.name, AuditManagerBisStateEnum.INIT.getCode(),(List<Integer>)getproductIdResult.getData()).getData();
    			} else if (Auditor.DIRECTORAUDITOR.name.equals(auditor)) {
    				initDirectorTodoAuditParams(model, queryDto, accountType, auditor);
    				auditResult = jkApplyAuditService
    						.getAudits(Auditor.DIRECTORAUDITOR.name, AuditDirectorBisStateEnum.INIT.getCode(),(List<Integer>)getproductIdResult.getData()).getData();
    			}
            }
			ServiceResult<Pagination<JkAuditDto>> result = null;
			Pagination<JkAuditDto> page = null;
			List<JkAuditDto> audits = null;
			if (DONE_SORT.equals(basePage.getSortField())) {// 特殊处理
				basePage.setSortField(null);
			}
			if (TODO_SORT.equals(basePage.getSortField())) {
				queryDto.setDefaultSort("default");
			}
			if (StringUtils.isEmpty(basePage.getSortField())) {
				basePage.setSortField(TODO_SORT);
				queryDto.setDefaultSort("default");
				basePage.setSortOrder(null);
			}
			result = jkApplyAuditService.getJkApplyAuditByTODO(queryDto, basePage);
			page = result.getData();
			audits = result.getData().getResult();
			for (JkAuditDto jkAuditDto : audits) {
				ServiceResult<OrganizationDto> orgDtoResult = organizationService
						.selectOrganizationById(jkAuditDto.getStoreId());
				if (orgDtoResult.success() && orgDtoResult.getData() != null) {
					jkAuditDto.setStoreName(orgDtoResult.getData().getOrgName());
				}
				jkAuditDto.setStateDesc(ApplyUtils.getFullStateDesc(jkAuditDto.getState(), jkAuditDto.getSubState()));
			}
			model.addAttribute("page", page);
			page.setResult(audits);
			model.addAttribute("auditSize", auditResult);// 未取件数
		} catch (Exception e) {
			MyphLogger.error("（终审）经理/总监信审列表（待办）加载异常", e);
		}
		return "/apply/audit/manage_audit_list";
	}

	/**
	 * 经理/总监信审列表(终审已办)
	 * 
	 * @名称 list
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年9月20日 下午9:25:05
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/list/manageDone")
	public String manageDoneList(Model model, JkAuditDto queryDto, BasePage basePage) {
		try {
			queryDto.setEmployeeId(ShiroUtils.getCurrentUser().getId());// 必传
			int accountType = ShiroUtils.getAccountType();// 1：管理员 0：普通
			String auditor = (String) ShiroUtils.getSessionAttribute(Auditor.AUDIT.name);

			model.addAttribute("progress", ApplyAuditType.MANAGEDONE.name);
			MyphLogger.debug("当前用户的账户类型是:{},角色是:{}", accountType, auditor);
			if (DONE_SORT.equals(basePage.getSortField())) {
				queryDto.setDefaultSort("default");
			}
			if (TODO_SORT.equals(basePage.getSortField())) {// 特殊处理
				basePage.setSortField(null);
			}
			if (StringUtils.isEmpty(basePage.getSortField())) {
				basePage.setSortField(DONE_SORT);
				queryDto.setDefaultSort("default");
				basePage.setSortOrder(null);
			}
			if (Auditor.MANAGERAUDITOR.name.equals(auditor)) {
				/** 如果当前用户是系统管理员，会重置前面的查询条件 */
				initDoneManageAuditParams(model, queryDto, accountType, auditor);
			} else if (Auditor.DIRECTORAUDITOR.name.equals(auditor)) {
				initDoneDirectorAuditParams(model, queryDto, accountType, auditor);
			}
			Pagination<JkAuditDto> page = jkApplyAuditService.getJkApplyAuditByDONE(queryDto, basePage).getData();
			List<JkAuditDto> historyAudits = page.getResult();
			if (CollectionUtils.isNotEmpty(historyAudits)) {
				for (JkAuditDto jkAuditDto : historyAudits) {
					ServiceResult<OrganizationDto> orgDtoResult = organizationService
							.selectOrganizationById(jkAuditDto.getStoreId());
					if (orgDtoResult.success() && orgDtoResult.getData() != null) {
						jkAuditDto.setStoreName(orgDtoResult.getData().getOrgName());
					}
				}
			}
			page.setResult(historyAudits);
			model.addAttribute("page", page);
		} catch (Exception e) {
			MyphLogger.error("初审/复审信审列表（已办）加载异常", e);
		}
		return "/apply/audit/manage_audit_list";
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
		MyphLogger.info("初审人员发起初审放弃,申请单号：{},当前操作人:{},操作人编号:{}", applyLoanNo, operatorName, operatorId);
		AbandonActionDto abandonActionDto = new AbandonActionDto();
		abandonActionDto.setApplyLoanNo(applyLoanNo);
		abandonActionDto.setOperateUser(ShiroUtils.getCurrentUserName());
		abandonActionDto.setFlowStateEnum(FlowStateEnum.AUDIT_FIRST);
		ServiceResult<Integer> serviceResult = null;
		// 拒绝说明是否要加
		try {
			ApplyInfoDto applyInfo = new ApplyInfoDto();
			applyInfo.setApplyRemark(interiorRemark);
			applyInfo.setApplyLoanNo(applyLoanNo);
			applyInfoService.updateInfo(applyInfo);

			ApproveTaskDto taskInfo = new ApproveTaskDto();
			taskInfo.setApplyLoanNo(applyLoanNo);
			taskInfo.setAuditFirstTime(DateUtils.getCurrentDateTime());
			taskInfo.setAuditState(AbandonBisStateEnum.FIRSTAUDITOR_GIVEUP.getCode());
			taskInfo.setFirstAuditor(operatorId);
			approveTaskService.updateFisrtData(taskInfo);

			// 调用 主流程发起放弃
			serviceResult = facadeFlowStateExchangeService.doAction(abandonActionDto);
		} catch (Exception e) {
			MyphLogger.error("信审列表-初审发起放弃流程异常", e);
		}
		return AjaxResult.formatFromServiceResult(serviceResult);
	}

	/**
	 * 初始化经理/管理员的查询条件
	 * 
	 * @名称 initManagerTodoAuditParams
	 * @描述 TODO
	 * @返回类型 void
	 * @日期 2016年10月20日 上午10:44:37
	 * @创建人 hf
	 * @更新人 hf
	 * 
	 */
	private void initManagerTodoAuditParams(Model model, JkAuditDto queryDto, int accountType, String auditor) {
		List<Integer> stateList = new ArrayList<Integer>();
		List<Integer> subStateList = new ArrayList<Integer>();
		queryDto.setAuditor(auditor);
		model.addAttribute(ROLE_NAME, Auditor.MANAGERAUDITOR.name);
		if (Constants.YES_INT == accountType) {// 管理员
			queryDto.setAuditor(Auditor.SYSTEM_MANAGER.name);
			model.addAttribute(ROLE_NAME, Auditor.SYSTEM_MANAGER.name);
		}
		stateList.add(FlowStateEnum.AUDIT_MANAGER.getCode());// 待终审主流程
		queryDto.setStateList(StringUtils.join(stateList, ","));

		subStateList.add(AuditManagerBisStateEnum.INIT.getCode());
		subStateList.add(AuditManagerBisStateEnum.BACK_INIT.getCode());
		queryDto.setSubStateList(StringUtils.join(subStateList, ","));
	}

	/**
	 * 初始化总监/管理员的查询条件
	 * 
	 * @名称 initDirectorTodoAuditParams
	 * @描述 TODO
	 * @返回类型 void
	 * @日期 2016年10月20日 上午10:45:45
	 * @创建人 hf
	 * @更新人 hf
	 * 
	 */
	private void initDirectorTodoAuditParams(Model model, JkAuditDto queryDto, int accountType, String auditor) {
		List<Integer> stateList = new ArrayList<Integer>();
		List<Integer> subStateList = new ArrayList<Integer>();
		queryDto.setAuditor(auditor);
		model.addAttribute(ROLE_NAME, Auditor.DIRECTORAUDITOR.name);
		if (Constants.YES_INT == accountType) {// 管理员
			queryDto.setAuditor(Auditor.SYSTEM_MANAGER.name);
			model.addAttribute(ROLE_NAME, Auditor.SYSTEM_MANAGER.name);
		}
		stateList.add(FlowStateEnum.AUDIT_DIRECTOR.getCode());// 高级终审主流程
		queryDto.setStateList(StringUtils.join(stateList, ","));

		subStateList.add(AuditDirectorBisStateEnum.INIT.getCode());
		queryDto.setSubStateList(StringUtils.join(subStateList, ","));
	}

	/**
	 * 信审列表已办查询条件初始化
	 * 
	 * @param model
	 * @param queryDto
	 * @param accountType
	 */
	private void initDoneManageAuditParams(Model model, JkAuditDto queryDto, int accountType, String auditor) {
		queryDto.setAuditor(auditor);
		model.addAttribute(ROLE_NAME, Auditor.MANAGERAUDITOR.name);
		if (Constants.YES_INT == accountType) {// 管理员
			queryDto.setAuditor(Auditor.SYSTEM_MANAGER.name);
			model.addAttribute(ROLE_NAME, Auditor.SYSTEM_MANAGER.name);
		}
	}

	/**
	 * 信审列表已办查询条件初始化
	 * 
	 * @param model
	 * @param queryDto
	 * @param accountType
	 */
	private void initDoneDirectorAuditParams(Model model, JkAuditDto queryDto, int accountType, String auditor) {
		queryDto.setAuditor(auditor);
		model.addAttribute(ROLE_NAME, Auditor.DIRECTORAUDITOR.name);
		if (Constants.YES_INT == accountType) {// 管理员
			queryDto.setAuditor(Auditor.SYSTEM_MANAGER.name);
			model.addAttribute(ROLE_NAME, Auditor.SYSTEM_MANAGER.name);
		}
	}

	/**
	 * 审核人类型
	 * 
	 * @ClassName: Auditor
	 * @Description: TODO
	 * @author hf
	 * @date 2016年9月22日 下午4:19:34
	 *
	 */
	public enum Auditor {
		// 利用构造函数传参
		FIRSTAUDITOR("firstAuditor", "初审"), LASTAUDITOR("lastAuditor", "复审"), MANAGERAUDITOR("auditorManager",
				"经理"), DIRECTORAUDITOR("directorAuditor",
						"总监"), SYSTEM_MANAGER("manager", "管理员"), AUDIT("auditor", "信审Key");

		// 定义私有变量
		private String name;
		private String desc;

		// 构造函数，枚举类型只能为私有
		Auditor(String name, String desc) {
			this.name = name;
			this.desc = desc;
		}
	}

	/**
	 * 待办/已办
	 * 
	 * @ClassName: Auditor
	 * @Description: TODO
	 * @author hf
	 * @date 2016年9月22日 下午4:19:34
	 *
	 */
	public enum ApplyAuditType {
		// 利用构造函数传参
		TODO("todo", "待办"), DONE("done", "已办"), MANAGETODO("manageTodo", "待办"), MANAGEDONE("manageDone", "已办");

		// 定义私有变量
		private String name;
		private String desc;

		ApplyAuditType(String name, String desc) {
			this.name = name;
			this.desc = desc;
		}
	}
}