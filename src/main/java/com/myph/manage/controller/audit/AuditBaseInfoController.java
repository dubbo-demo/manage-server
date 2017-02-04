/**   
 * @Title: JkApplyAuditController.java 
 * @Package: com.myph.manage.audit.controller
 * @company: 麦芽金服
 * @Description: TODO
 * @date 2016年9月20日 下午9:17:42 
 * @version V1.0   
 */
package com.myph.manage.controller.audit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.dto.JkAuditTaskDto;
import com.myph.apply.dto.RowKeyServiceDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.audit.dto.AuditThridInfoDto;
import com.myph.audit.service.AuditThridInfoService;
import com.myph.audit.service.JkApplyAuditService;
import com.myph.common.cache.RedisRootNameSpace;
import com.myph.common.hbase.HbaseUtils;
import com.myph.common.http.ApiConnector;
import com.myph.common.log.MyphLogger;
import com.myph.common.redis.CacheService;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.util.MaiyaMD5;
import com.myph.constant.ApplyFirstReportEnum;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.ProductNodeEnum;
import com.myph.manage.common.constant.ClientType;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.controller.BaseController;
import com.myph.member.base.dto.MemberInfoDto;
import com.myph.member.base.service.MemberInfoService;
import com.myph.node.service.NodeService;
import com.myph.product.dto.ProductDto;
import com.myph.product.dto.ProductFiletypeDto;
import com.myph.product.service.ProductFileTypeService;
import com.myph.product.service.ProductService;

/**
 * 
 * @ClassName: AuditWorkCashController
 * @Description: 信审详情-初审报告-现金与财务
 * @author 吴阳春
 * @date 2016年9月22日 上午11:04:32
 *
 */
@Controller
@RequestMapping("/audit/base")
public class AuditBaseInfoController extends BaseController {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private ApplyInfoService applyInfoService;

    @Autowired
    private ProductFileTypeService productFileTypeService;

    @Autowired
    AuditThridInfoService auditThridInfoService;

    @Autowired
    JkApplyAuditService jkApplyAuditService;

    @Autowired
    ProductService productService;

    @Autowired
    MemberInfoService memberInfoService;

    /**
     * 
     * @名称 baseInfoAdd
     * @描述 初审报告基本信息操作界面
     * @返回类型 String
     * @日期 2016年9月26日 下午4:01:16
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    @RequestMapping("/baseInfoAdd")
    public String baseInfoAdd(Model model, String applyLoanNo, String cType) {
        MyphLogger.info("初审报告基本信息操作界面 AuditBaseInfoController.baseInfoAdd 输入参数{}", applyLoanNo);
        ServiceResult<ApplyInfoDto> apply = applyInfoService.queryInfoByLoanNo(applyLoanNo);
        ServiceResult<ProductDto> productDto = productService.selectByPrimaryKey(apply.getData().getProductType());
        model.addAttribute("productName", productDto.getData().getProdName());
        model.addAttribute("apply", apply.getData());
        if (StringUtils.isEmpty(cType) && null != apply.getData()) {
            if (FlowStateEnum.AUDIT_LASTED.getCode().equals(apply.getData().getState())) {
                cType = FlowStateEnum.AUDIT_LASTED.getCode().toString();
            } else if (FlowStateEnum.AUDIT_MANAGER.getCode().equals(apply.getData().getState())) {
                cType = FlowStateEnum.AUDIT_MANAGER.getCode().toString();
            } else if (FlowStateEnum.AUDIT_DIRECTOR.getCode().equals(apply.getData().getState())) {
                cType = FlowStateEnum.AUDIT_DIRECTOR.getCode().toString();
            }
        }
        // 信审任务信息
        ServiceResult<JkAuditTaskDto> applyAudit = jkApplyAuditService.queryInfoByApplyLoanNo(applyLoanNo);
        model.addAttribute("applyAudit", applyAudit.getData());
        ServiceResult<List<AuditThridInfoDto>> auditThridResult = auditThridInfoService.queryInfoByAppNo(applyLoanNo);
        if (auditThridResult.success() && auditThridResult.getData() != null) {
            model.addAttribute("auditThridData", auditThridResult.getData());
        } else {
            // fileUpState 15对应基础数据信审阶段
            ServiceResult<List<ProductFiletypeDto>> fileTypeData = productFileTypeService.showFileByProductIdAndState(
                    apply.getData().getProductType(), ProductNodeEnum.AUDIT.getCode());
            model.addAttribute("fileTypeData", fileTypeData.getData());
        }
        ServiceResult<MemberInfoDto> member = memberInfoService.queryInfoByIdCard(apply.getData().getIdCard());
        if (null != member) {
            model.addAttribute("member", member.getData().getId());
        }
        model.addAttribute("applyLoanNo", applyLoanNo);
        model.addAttribute("cType", cType);
        // web端新增入口
        if (null != apply.getData() && null != apply.getData().getClientType()
                && apply.getData().getClientType().equals(ClientType.WEB.getCode())) {
            return "/apply/audit/baseinfo/web/baseInfo_add";
        }
        return "/apply/audit/baseinfo/baseInfo_add";

    }

    /**
     * 获取征信数据
     * 
     * @param storeId
     * @return
     */
    @RequestMapping("/queryThridData")
    @ResponseBody
    public AjaxResult queryThridData(RowKeyServiceDto rowKeyServiceDto) {
        MyphLogger.info("获取征信报告");
        try {
            String result = ApiConnector.postJson(configUtils.getAuditReport_url(),
                    (JSON) JSONObject.toJSON(rowKeyServiceDto), "UTF-8");
            if (null == result) {
                return AjaxResult.failed("获取征信数据失败");
            }
            MyphLogger.info("获取征信报告 rowKey:{}", result);
            Map<String, Object> smsResult = new HashMap<>();
            smsResult = JSON.parseObject(result);
            if (smsResult.get("result") != null) {
                String data = HbaseUtils.getByString("my_ph_userinfo_authent", "infomessage", smsResult.get("result")
                        .toString());
                return AjaxResult.success(data);
            }
        } catch (Exception e) {
            MyphLogger.error(e, "获取征信数据失败,ID:{}", rowKeyServiceDto.toString());
        }
        return AjaxResult.failed("获取征信数据失败");
    }

    /**
     * 获取征信数据
     * 
     * @param storeId
     * @return
     */
    @RequestMapping("/queryThridDataApp")
    @ResponseBody
    public AjaxResult queryThridDataApp(String userid) {
        MyphLogger.info("获取征信报告userid:{}", userid);
        try {
            String data = HbaseUtils.getByString("my_ph_userinfo_authent", "infomessage", generRowkey("phzx", userid));
            return AjaxResult.success(data);
        } catch (Exception e) {
            MyphLogger.error(e, "获取征信数据失败,ID:{}", userid);
        }
        return AjaxResult.failed("获取征信数据失败");
    }

    /**
     * 获取聚信立报告
     * 
     * @param storeId
     * @return
     */
    @RequestMapping("/queryJXLData")
    @ResponseBody
    public AjaxResult queryJXLData(String userid) {
        MyphLogger.info("获取聚信立报告", userid);
        try {
            String data = HbaseUtils.getByString("my_ph_userinfo_authent", "infomessage", generRowkey("jxl", userid));
            return AjaxResult.success(data);
        } catch (Exception e) {
            MyphLogger.error(e, "获取聚信立报告失败,ID:{}", userid);
        }
        return AjaxResult.failed("获取聚信立报告失败");
    }

    public static String generRowkey(String queue, String userId) {
        String param_str = queue + userId;
        String rowkey = MaiyaMD5.encode(param_str);
        return rowkey.toLowerCase();
    }

    /**
     * 
     * @名称 auditThridInfoSave
     * @描述 征信信息保存
     * @返回类型 AjaxResult
     * @日期 2016年11月14日 上午9:54:10
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    @RequestMapping("/auditThridInfoSave")
    @ResponseBody
    public AjaxResult auditThridInfoSave(@RequestBody String jsonStr) {
        String operatorName = ShiroUtils.getCurrentUserName();
        Long operatorId = ShiroUtils.getCurrentUserId();
        MyphLogger.info("征信信息保存,当前操作人:{},操作人编号:{},征信数据:{}", operatorName, operatorId, jsonStr);
        try {
            if (null == jsonStr) {
                return AjaxResult.formatFromServiceResult(null);
            }
            JSONArray arr = JSON.parseArray(jsonStr);
            // 信审主表信息
            JSONObject auditTaskJson = JSONObject.parseObject(arr.get(0).toString());
            // 第三方网站信息列表
            JSONObject auditThridInfoJsons = JSONObject.parseObject(arr.get(1).toString());
            String auditThridInfos = auditThridInfoJsons.getString("auditThridInfos");
            String auditTask = auditTaskJson.getString("auditTask");
            List<AuditThridInfoDto> auditThridList = JSONObject.parseArray(auditThridInfos, AuditThridInfoDto.class);
            List<JkAuditTaskDto> jkAuditTaskDto = JSONObject.parseArray(auditTask, JkAuditTaskDto.class);
            if (null == jkAuditTaskDto || jkAuditTaskDto.isEmpty()) {
                return AjaxResult.formatFromServiceResult(null);
            }
            String applyLoanNo = jkAuditTaskDto.get(0).getApplyLoanNo();
            if (null != jkAuditTaskDto) {
                for (AuditThridInfoDto dto : auditThridList) {
                    dto.setApplyLoanNo(applyLoanNo);
                }
            }
            ServiceResult<List<AuditThridInfoDto>> result = auditThridInfoService.queryInfoByAppNo(applyLoanNo);
            if (result.success() && result.getData() != null) {
                // 更新
                ServiceResult<Integer> updateResult = auditThridInfoService.batchUpdate(auditThridList,
                        jkAuditTaskDto.get(0));
                // 信审提交时，验证基本信息是否提交
                CacheService.StringKey.set(ApplyFirstReportEnum.base_info.getKey(applyLoanNo), applyLoanNo,
                        RedisRootNameSpace.UnitEnum.ONE_MONTH);
                return AjaxResult.formatFromServiceResult(updateResult);
            } else {
                // 新增
                ServiceResult<Integer> addResult = auditThridInfoService
                        .batchAdd(auditThridList, jkAuditTaskDto.get(0));
                // 信审提交时，验证基本信息是否提交
                CacheService.StringKey.set(ApplyFirstReportEnum.base_info.getKey(applyLoanNo), applyLoanNo,
                        RedisRootNameSpace.UnitEnum.ONE_MONTH);
                return AjaxResult.formatFromServiceResult(addResult);
            }
        } catch (Exception e) {
            MyphLogger.error("征信信息保存异常", e);
            return AjaxResult.failed("服务异常，请稍后重试");
        }
    }
}
