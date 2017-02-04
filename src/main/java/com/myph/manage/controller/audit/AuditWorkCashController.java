/**   
 * @Title: JkApplyAuditController.java 
 * @Package: com.myph.manage.audit.controller
 * @company: 麦芽金服
 * @Description: TODO
 * @date 2016年9月20日 下午9:17:42 
 * @version V1.0   
 */
package com.myph.manage.controller.audit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.myph.apply.dto.FileManageApplyInfoDto;
import com.myph.apply.dto.JkAuditCheckDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.audit.service.JkAuditCheckService;
import com.myph.auditCashCount.dto.AuditCashCountDto;
import com.myph.auditCashCount.service.AuditCashCountService;
import com.myph.auditCashStatistics.dto.AuditCashStatisticsDto;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.NodeConstant;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.controller.BaseController;
import com.myph.manage.controller.audit.JkApplyAuditController.Auditor;
import com.myph.node.dto.SysNodeDto;
import com.myph.node.service.NodeService;
import com.myph.personassets.dto.ApplyPersonassetsDto;
import com.myph.personassets.service.ApplyPersonassetsService;
import com.myph.auditCashStatistics.service.AuditCashStatisticsService;

/**
 * 
 * @ClassName: AuditWorkCashController
 * @Description: 信审详情-初审报告-现金与财务
 * @author 吴阳春
 * @date 2016年9月22日 上午11:04:32
 *
 */
@Controller
@RequestMapping("/audit")
public class AuditWorkCashController extends BaseController {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private ApplyInfoService applyInfoService;

    @Autowired
    private AuditCashStatisticsService auditCashStatisticsService;

    @Autowired
    private ApplyPersonassetsService applyPersonassetsService;

    @Autowired
    private AuditCashCountService auditCashCountService;

    @Autowired
    private JkAuditCheckService jkAuditCheckService;

    @RequestMapping("/auditWorkCash")
    public String allotUI(Model model, @RequestParam("applyLoanNo") String applyLoanNo, String cType) {
        try {
            // 查询行业类型
            ServiceResult<List<SysNodeDto>> industrySysNodeListResult = nodeService
                    .getListByParent(NodeConstant.INDUSTRY_TYPE_PARENT_CODE);
            // 查询进件表基本信息
            ServiceResult<FileManageApplyInfoDto> applyInfo = applyInfoService.selectByApplyLoanNo(applyLoanNo);
            // 查询客户收入与负债信息类型
            ServiceResult<List<SysNodeDto>> earningsSysNodeListResult = nodeService
                    .getListByParent(NodeConstant.EARNINGS_TYPE_PARENT_CODE);
            List<SysNodeDto> earningsSysNodeList = earningsSysNodeListResult.getData();
            List<Map<String, Object>> earningsSysNodeResult = new ArrayList<>();
            for (int i = 0; i < earningsSysNodeList.size(); i++) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("index", i);
                param.put("id", earningsSysNodeList.get(i).getId());
                String sourceStr = earningsSysNodeList.get(i).getNodeName();
                String[] sourceStrArray = sourceStr.split("\\|");
                param.put("nodeName", sourceStrArray[0]);
                if (sourceStrArray.length > 1) {
                    param.put("nodeDesc", sourceStrArray[1]);
                } else {
                    param.put("nodeDesc", "");
                }
                param.put("nodeCode", earningsSysNodeList.get(i).getNodeCode());
                if (NodeConstant.EARNINGS_TYPE_MUST_FILL_LIST.contains(param.get("nodeCode"))) {
                    param.put("mustFill", 1);
                } else {
                    param.put("mustFill", 0);
                }
                earningsSysNodeResult.add(param);
            }
            // 获取入口：初审 or 终审
            if (!Auditor.FIRSTAUDITOR.toString().equals(ShiroUtils.getSession().getAttribute("auditor"))) {
                model.addAttribute("lastAuditor", 1);
            }
            model.addAttribute("apply", applyInfo.getData());
            model.addAttribute("industryList", industrySysNodeListResult.getData());
            model.addAttribute("earningsSysNodeResult", earningsSysNodeResult);
            model.addAttribute("cType", cType);
            return "/apply/audit/audit_work_cash";
        } catch (Exception e) {
            MyphLogger.error(e, "信审财务异常,入参:{}", applyLoanNo);
            return "error/500";
        }
    }

    @RequestMapping("/saveAuditCashStatistics")
    @ResponseBody
    public AjaxResult saveAuditCashStatistics(AuditCashStatisticsDto auditCashStatisticsDto) {
        try {
            String operatorName = ShiroUtils.getCurrentUserName();
            Long operatorId = ShiroUtils.getCurrentUserId();
            MyphLogger.info("保存信审财务,请求参数:{},当前操作人:{},操作人编号:{}", auditCashStatisticsDto, operatorName, operatorId);
            BigDecimal shareRatio = auditCashStatisticsDto.getShareRatio();
            if (shareRatio == null) {
                shareRatio = new BigDecimal(0);
            }
            auditCashStatisticsDto.setShareRatio(shareRatio.divide(new BigDecimal(100)));
            // 根据申请单号查询是否有记录，有记录更新，没有记录插入
            ServiceResult<Integer> checkDataResult = auditCashStatisticsService
                    .checkData(auditCashStatisticsDto.getApplyLoanNo());
            if (checkDataResult.getData() > 0) {
                auditCashStatisticsService.updateByApplyLoanNoSelective(auditCashStatisticsDto);
            } else {
                auditCashStatisticsService.insertSelective(auditCashStatisticsDto);
            }
            return AjaxResult.success();
        } catch (Exception e) {
            MyphLogger.error(e, "保存信审财务异常,入参:{}", auditCashStatisticsDto.toString());
            return AjaxResult.failed("保存信审财务异常");
        }
    }

    /**
     * 
     * @名称 getPersonassets
     * @描述 获取资产信息
     * @返回类型 AjaxResult
     * @日期 2016年11月10日 上午10:54:35
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/getPersonassets")
    @ResponseBody
    public AjaxResult getPersonassets(@RequestParam("applyLoanNo") String applyLoanNo) {
        try {
            ServiceResult<ApplyPersonassetsDto> result = applyPersonassetsService.getByAppNo(applyLoanNo);
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, " 获取资产信息异常,入参:{}", applyLoanNo);
            return AjaxResult.failed(" 获取资产信息异常");
        }
    }

    /**
     * 
     * @名称 saveAuditCashCount
     * @描述 保存信审现金统计计算关联表信息
     * @返回类型 AjaxResult
     * @日期 2016年9月28日 上午12:53:12
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/saveAuditCashCount")
    @ResponseBody
    public AjaxResult saveAuditCashCount(@RequestBody String auditCashCountJson) {
        try {
            String operatorName = ShiroUtils.getCurrentUserName();
            Long operatorId = ShiroUtils.getCurrentUserId();
            MyphLogger.info("保存信审现金统计计算关联表信息,请求参数:{},当前操作人:{},操作人编号:{}", auditCashCountJson, operatorName, operatorId);
            List<AuditCashCountDto> auditCashCountDtolist = JSONObject.parseArray(auditCashCountJson,
                    AuditCashCountDto.class);
            for (int i = 0; i < auditCashCountDtolist.size(); i++) {
                if (auditCashCountDtolist.get(i).getState().equals(FlowStateEnum.AUDIT_FIRST.getCode())) {
                    if (!auditCashCountDtolist.get(i).getNodeCode().equals(NodeConstant.OTHER_REVENUE)
                            && !auditCashCountDtolist.get(i).getNodeCode().equals(NodeConstant.OTHER_LIABILITIES)) {
                        // 根据申请单号、负债标准查询是否有记录id，有记录更新，没有记录插入
                        ServiceResult<Long> idResult = auditCashCountService.checkData(
                                auditCashCountDtolist.get(i).getApplyLoanNo(),
                                auditCashCountDtolist.get(i).getLiabilitiesTypeNo());
                        if (idResult.getData() != null) {
                            auditCashCountDtolist.get(i).setId(idResult.getData());
                            auditCashCountService.updateByPrimaryKeySelective(auditCashCountDtolist.get(i));
                        } else {
                            auditCashCountService.insertSelective(auditCashCountDtolist.get(i));
                        }
                    }
                } else {
                    // 终审时对数据进行更新或插入
                    ServiceResult<Long> idResult = auditCashCountService.checkData(
                            auditCashCountDtolist.get(i).getApplyLoanNo(),
                            auditCashCountDtolist.get(i).getLiabilitiesTypeNo());
                    if (idResult.getData() != null) {
                        auditCashCountDtolist.get(i).setId(idResult.getData());
                        auditCashCountService.updateByPrimaryKeySelective(auditCashCountDtolist.get(i));
                    } else {
                        auditCashCountService.insertSelective(auditCashCountDtolist.get(i));
                    }
                }
            }
            return AjaxResult.success();
        } catch (Exception e) {
            MyphLogger.error(e, "保存信审现金统计计算关联表信息异常,入参:{}", auditCashCountJson);
            return AjaxResult.failed("保存信审现金统计计算关联表信息异常");
        }
    }

    /**
     * 
     * @名称 selectAuditCashStatistics
     * @描述 查询信审统计信息
     * @返回类型 AjaxResult
     * @日期 2016年9月28日 上午1:20:37
     * @创建人 吴阳春
     * @更新人 吴阳春
     *
     */
    @RequestMapping("/selectAuditCashStatistics")
    @ResponseBody
    public AjaxResult selectAuditCashStatistics(@RequestParam("applyLoanNo") String applyLoanNo) {
        try {
            ServiceResult<AuditCashStatisticsDto> result = auditCashStatisticsService.selectByApplyLoanNo(applyLoanNo);
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "查询信审统计信息异常,入参:{}", applyLoanNo);
            return AjaxResult.failed("查询信审统计信息异常");
        }
    }

    @RequestMapping("/selectAuditCashCount")
    @ResponseBody
    public AjaxResult selectAuditCashCount(@RequestParam("applyLoanNo") String applyLoanNo) {
        try {
            ServiceResult<List<AuditCashCountDto>> result = auditCashCountService.selectByApplyLoanNo(applyLoanNo);
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "查询信审现金统计计算关联表异常,入参:{}", applyLoanNo);
            return AjaxResult.failed("查询信审现金统计计算关联表异常");
        }
    }

    @RequestMapping("/selectAuditCheck")
    @ResponseBody
    public AjaxResult selectAuditCheck(@RequestParam("applyLoanNo") String applyLoanNo) {
        try {
            ServiceResult<JkAuditCheckDto> result = jkAuditCheckService.select(applyLoanNo);
            return AjaxResult.success(result.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "信审征信核查异常,入参:{}", applyLoanNo);
            return AjaxResult.failed("信审征信核查异常");
        }
    }
}
