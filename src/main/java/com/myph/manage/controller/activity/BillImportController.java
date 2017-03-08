package com.myph.manage.controller.activity;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.myph.PushContractBill.dto.PushContarctAndBillTaskDto;
import com.myph.PushContractBill.service.PushContarctAndBillTaskService;
import com.myph.apply.dto.FileManageApplyInfoDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.util.DateUtils;
import com.myph.manage.common.constant.BillPushEnum;
import com.myph.manage.common.util.ExcelUtil;
import com.myph.manage.controller.BaseController;
import com.myph.manage.po.ResultParams;
import com.myph.manage.service.billContract.BillPushDto;
import com.myph.manage.service.billContract.ExcelRowBillPush;
import com.myph.manage.service.billContract.FristBillPushDto;
import com.myph.member.blacklist.dto.BlackQueryDto;
import com.myph.member.blacklist.service.ThirdBlackService;
import com.myph.product.dto.ProductDto;
import com.myph.product.service.ProductService;
import com.myph.sign.service.ContractService;
import org.apache.hadoop.hdfs.web.JsonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

@Controller
@RequestMapping("/billImport")
public class BillImportController extends BaseController {
    @Autowired
    private ThirdBlackService thirdBlackService;

    @Autowired
    private PushContarctAndBillTaskService pushContarctAndBillTaskService;

    @Autowired
    private ApplyInfoService applyInfoService;

    /**
     * 产品service
     */
    @Autowired
    private ProductService productService;

    /**
     * 合同service
     */
    @Autowired
    private ContractService contractService;



    /**
     * @param model
     * @return
     * @Description:进入黑名单查询
     */
    @RequestMapping("/list")
    public String list(Model model, BlackQueryDto queryDto, BasePage basePage) {
        MyphLogger.debug("开始黑名单查询：/billImport/list.htm|querDto=" + queryDto + "|basePage=" + basePage);
        model.addAttribute("queryDto", queryDto);
        MyphLogger.debug("结束黑名单查询：/billImport/list.htm|page=" + null);
        return "/activity/billImport/billImportForm";
    }

    /**
     * 导入账单
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/impInfo")
    public String impInfo(Model model, HttpServletRequest request,
            @RequestParam(value = "upFile", required = false) MultipartFile upFile) {
        MyphLogger.info("开始账单导入：/billImport/impInfo.htm");
        if (null == upFile) {
            return "/activity/billImport/billImport";
        }
        int result = AjaxResult.SUCCESS_CODE;
        List<BillPushDto> successDatas = new ArrayList<BillPushDto>();
        try {
            // excel转为bean
            ExcelRowBillPush excelBlack = new ExcelRowBillPush();
            successDatas = ExcelUtil.readExcel(upFile, excelBlack);
            List<String> excelErrorMsgs = excelBlack.getExcelErrorMsgs();// 获取校验不通过数据
            if (null != excelErrorMsgs && excelErrorMsgs.size() > 0) {
                model.addAttribute("result", AjaxResult.ERROR_CODE);
                model.addAttribute("excelErrorMsgs", excelErrorMsgs);
                MyphLogger.info("结束账单导入：/billImport/impInfo.htm|导入失败");
                return "/activity/billImport/billImport";
            } else {
                BillPushDto successData = null;
                for (int i = 0; i < successDatas.size(); i++) {
                    successData = successDatas.get(i);
                    // TODO 通过合同号查询推送合同账单推送执行结果表，是否有成功发送的数据
                    PushContarctAndBillTaskDto parmPush = new PushContarctAndBillTaskDto();
                    parmPush.setBillPushedStatu(BillPushEnum.SUCCESS.getCode());
                    parmPush.setBillNo(successData.getBillingNo());
                    parmPush.setContractNo(successData.getContractNo());
                    PushContarctAndBillTaskDto resultPush = pushContarctAndBillTaskService.selectSuccessInfo(parmPush);
                    // 没有获取记录数据,第一次发送
                    if(null == resultPush) {
                        FristBillPushDto fristDto = new FristBillPushDto();
                        BeanUtils.copyProperties(successData,fristDto);

                        // TODO 抓取会员信息
                        ServiceResult<FileManageApplyInfoDto> applyInfoR = applyInfoService.selectByApplyLoanNo(successData.getContractNo());
                        if (null != applyInfoR && applyInfoR.success()) {
                            BeanUtils.copyProperties(applyInfoR.getData(),fristDto);
                            if(applyInfoR.getData().getProductType() != null) {
                                fristDto.setProductId(applyInfoR.getData().getProductType().toString());
                                // TODO 产品name,总期数
                                ServiceResult<ProductDto> productR = productService.selectByPrimaryKey(applyInfoR.getData().getProductType());
                                if(productR.success()) {
                                    fristDto.setProductName(productR.getData().getProdName());
                                    fristDto.setTotalTerm(productR.getData().getPeriods() == null ? null : productR.getData().getPeriods().toString());
                                }
                            }

                        }

                        // TODO 抓取合同信息
                        contractService.selectByApplyLoanNo(fristDto.getContractNo());
                        // TODO 备用电话，memeberInfo表里，逗号分隔
                        // TODO 抓取工作信息
                        // TODO 抓取联系人信息
                        // TODO 抓取设备信息 (没有)
                        // TODO http发送正确数据给催收系统 req 合同基础数据接口
                    }

                    // TODO http发送正确数据给催收系统

                    // TODO 无论成功，插入记录表
                    PushContarctAndBillTaskDto record = new PushContarctAndBillTaskDto();
                    pushContarctAndBillTaskService.insert(record);
                }

            }
        } catch (Exception e) {
            result = AjaxResult.ERROR_CODE;
            MyphLogger.error(e, "异常[账单导入：/billImport/impInfo.htm]");
        }
        model.addAttribute("result", result);
        model.addAttribute("successDataSize", null == successData ? 0 : successData.size());
        MyphLogger.debug("successData=============" + successData);
        MyphLogger.info("结束账单导入：/billImport/impInfo.htm|导入成功");
        return "/activity/billImport/billImport";
    }

    @Autowired
    private RestTemplate restTemplate;

    private String restPushBillAndContract(Map<String, String> _params_) {
            Map<String, String> paramstr = new HashMap<String, String>();
            paramstr.put("deviceNo", _params_.get("deviceNo"));
            paramstr.put("applyDate", DateUtils.dateToString(new Date()));
            paramstr.put("deviceType", String.valueOf(_params_.get("client")));
//            JsonUtil.objectToJson(paramstr);
            String url =  "/rule/chain/exec";
            MyphLogger.debug("XXXX -" ,paramstr.toString());
            ResultParams response = null;
            try {
                response = restTemplate.postForObject(url, paramstr, ResultParams.class);
            } catch (Exception e) {
                MyphLogger.error("XXX-异常", paramstr.toString(), e);
            }
        return null;
    }

    /**
     * Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
     *
     * @名称 transBeanMap
     * @描述 Bean转Map
     * @返回类型 Map<String,Object>
     * @日期 2017年3月7日 下午5:15:33
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    public static Map<String, Object> transBeanMap(Object obj) {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }

        return map;

    }

}
