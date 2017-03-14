package com.myph.manage.service.billContract;

import com.myph.PushContractBill.dto.FristBillPushEntityDto;
import com.myph.PushContractBill.dto.PushContarctAndBillTaskDto;
import com.myph.PushContractBill.service.PushContarctAndBillTaskService;
import com.myph.apply.dto.MemberJobDto;
import com.myph.apply.dto.MemberLinkmanDto;
import com.myph.apply.jobinfo.service.MemberJobService;
import com.myph.apply.linkman.service.MemberLinkmanService;
import com.myph.apply.service.ApplyInfoService;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.common.util.DateUtils;
import com.myph.manage.common.constant.BillPushConstant;
import com.myph.manage.common.constant.BillPushEnum;
import com.myph.manage.common.util.MYPHConfigUtils;
import com.myph.manage.po.ResultParams;
import com.myph.manage.service.billContract.dto.AddrDto;
import com.myph.manage.service.billContract.dto.ContractRequestVo;
import com.myph.manage.service.billContract.dto.LinkmanDto;
import com.myph.manage.service.billContract.dto.RepayPlanRequestVo;
import com.myph.manage.service.billContract.service.BillRestService;
import com.myph.member.base.dto.MemberInfoDto;
import com.myph.member.base.service.MemberInfoService;
import com.myph.node.dto.SysNodeDto;
import com.myph.node.service.NodeService;
import com.myph.product.service.ProductService;
import com.myph.sign.service.ContractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.ServiceMode;
import java.util.*;

/**
 * Created by dell on 2017/3/9.
 */
@Service
public class BillRestServiceImpl implements BillRestService {

    /**
     * 会员service
     */
    @Autowired
    private MemberInfoService memberInfoService;

    /**
     * 工作service
     */
    @Autowired
    private MemberJobService memberJobService;

    /**
     * 联系人service
     */
    @Autowired
    private MemberLinkmanService memberLinkmanService;

    @Autowired
    private NodeService nodeService;

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

    List<String> excelErrorMsgs = null;

    /**
     * @Description: 合同账单用户基本信息推送
     * @author heyx
     * @return ContractRequestVo
     * @date 2017/3/10
     * @version V1.0
     */
    private ContractRequestVo getContractAndBill(RepayPlanRequestVo successData) {
        ContractRequestVo fristDto = new ContractRequestVo();
        BeanUtils.copyProperties(successData, fristDto);

        // TODO 抓取合同信息,申请单对象
        FristBillPushEntityDto eFristDto = pushContarctAndBillTaskService
                .selectApplyContractLoan(fristDto.getContractNo());
        BeanUtils.copyProperties(eFristDto, fristDto);

        // TODO 备用电话，memeberInfo表里，逗号分隔
        ServiceResult<MemberInfoDto> memberDto = memberInfoService
                .queryInfoByIdCard(eFristDto.getIdCard());
        toInfoMember(fristDto, memberDto.getData());
        // TODO 抓取工作信息
        ServiceResult<MemberJobDto> memberJobR = memberJobService
                .selectByMemberId(memberDto.getData().getId());
        toInfoJob(fristDto, memberJobR.getData());

        //1,现住址;2,公司地址;3,户籍地址
        if ("2".equals(memberDto.getData().getMailAddress())) {
            fristDto.setMailAddr(memberDto.getData().getLiveAddress()); //邮寄地址
        }

        // TODO 抓取联系人信息
        ServiceResult<List<MemberLinkmanDto>> linkMans =
                memberLinkmanService.getLinkmansByMemId(memberDto.getData().getId());
        toInfoLinkMans(fristDto, linkMans.getData());

        return fristDto;
    }

    /**
     * @Description: 组装报文，推送催收接口
     * @author heyx
     * @date 2017/3/14
     * @version V1.0
     */
    public List<String> restCS(List<RepayPlanRequestVo> successDatas) throws Exception {
        excelErrorMsgs = new ArrayList<String>();
        if (null == successDatas) {
            return excelErrorMsgs;
        }
        RepayPlanRequestVo successData = null;
        for (int i = 0; i < successDatas.size(); i++) {
            successData = successDatas.get(i);
            // TODO 通过合同号查询推送合同账单推送执行结果表，是否有成功发送的数据
            PushContarctAndBillTaskDto record = new PushContarctAndBillTaskDto();
            record.setBillPushedStatu(BillPushEnum.SUCCESS.getCode());
            record.setContractId(successData.getContractNo());
            record.setBillId(successData.getBillId());
            PushContarctAndBillTaskDto resultPush = pushContarctAndBillTaskService.selectSuccessInfo(record);
            // 没有获取记录数据,该账单已经推送
            if (null != resultPush) {
                MyphLogger.info("ContractNo:{},BillId:{},已经推送", successData.getContractNo(), successData.getBillId());
                return excelErrorMsgs;
            }
            record.setBillPushedStatu(BillPushEnum.SUCCESS.getCode());
            record.setContractId(successData.getContractNo());
            record.setBillId(null);
            resultPush = pushContarctAndBillTaskService.selectSuccessInfo(record);
            // 合同账单基础数据
            ContractRequestVo fristVo= null;
            // 没有获取记录数据,第一次发送
            if (null == resultPush) {
                // 推送送合同账单用户基础信息bean
                fristVo = getContractAndBill(successData);
            }
            try {
                // TODO 组装插入记录表
                record = new PushContarctAndBillTaskDto();
                record.setBillId(successData.getBillId());
                record.setContractId(successData.getContractNo());
                ResultParams response = null;
                if (null != fristVo) {
                    // TODO http发送正确数据给催收系统 req 合同基础数据接口
//                    response = restPushBillAndContract(fristVo);
                } else {
                    // 第二次发送该合同逾期账单
                    // TODO http发送正确数据给催收系统
//                    response = restPushBill(successData);
                }
                if(response.SUCCESS_CODE.equals(response.getRetcode())){
                    record.setBillPushedStatu(BillPushEnum.SUCCESS.getCode());
                    MyphLogger.info("ContractNo:{},BillId:{},接口调用成功，推送成功，插入记录表", successData.getContractNo(), successData.getBillId());
                } else {
                    record.setBillPushedStatu(BillPushEnum.ERROR.getCode());
                    excelErrorMsgs.add("合同号:"+successData.getContractNo()+"-账单号:"+successData.getBillId()+",接口调用成功，推送失败");
                    MyphLogger.info("ContractNo:{},BillId:{},接口调用成功，推送失败", successData.getContractNo(), successData.getBillId());
                }
                // TODO 插入记录表
                pushContarctAndBillTaskService.insert(record);
            } catch (Exception e) {
                excelErrorMsgs.add("合同号:"+successData.getContractNo()+"-账单号:"+successData.getBillId()+",接口调用异常");
                MyphLogger.error("ContractNo:{},BillId:{},接口调用异常", successData.getContractNo(), successData.getBillId());
                throw new Exception(e);
            }

        }
        return excelErrorMsgs;
    }

    /**
     * 组装联系人
     *
     * @param fristDto
     * @param linkMans
     */
    private void toInfoLinkMans(ContractRequestVo fristDto, List<MemberLinkmanDto> linkMans) {
        if (null == linkMans) {
            return;
        }
        try {
            List<LinkmanDto> list = new ArrayList<LinkmanDto>();
            LinkmanDto dto = null;
            MemberLinkmanDto mdto = null;
            for (int i = 0; i < linkMans.size(); i++) {
                mdto = linkMans.get(i);
                dto = new LinkmanDto();
                dto.setAddr(mdto.getLinkManHomeName());
                dto.setRelation(mdto.getLinkManRelation());
                dto.setName(mdto.getLinkManName());
                dto.setTelNum(mdto.getLinkManPhone());
                dto.setDetailAddr(mdto.getLinkManHomeAddress());
                list.add(dto);
            }
            fristDto.setLinkmanList(list);
        } catch (Exception e) {
            excelErrorMsgs.add("合同号:"+fristDto.getContractNo()+"-账单号:"+fristDto.getBillId()+",组装联系人异常");
            MyphLogger.error("组装联系人异常", e);
        }
    }

    /**
     * 组装工作信息
     *
     * @param fristDto
     * @param memberJobDto
     */
    private void toInfoJob(ContractRequestVo fristDto, MemberJobDto memberJobDto) {
        if (null == memberJobDto) {
            return;
        }

        try {
            fristDto.setUnitName(memberJobDto.getUnitName()); //工作单位
            fristDto.setUnitAddr(memberJobDto.getCompanyAddress()); //单位地址
            fristDto.setUnitDetailAddr(memberJobDto.getDetailAddr()); //详细单位地址
            fristDto.setUnitTel(memberJobDto.getUnitTelephone()); //单位电话
            fristDto.setUnitExtendTel(memberJobDto.getExtensionNum()); //单位分机号
            ServiceResult<SysNodeDto> bnode = null;
            fristDto.setIndustryType(""); //行业类别
            if (null != memberJobDto.getBusinessType()) {
                bnode = nodeService.selectByPrimaryKey((long) memberJobDto.getBusinessType());
                if (null != bnode) {
                    fristDto.setIndustryType(bnode.getData() == null ? "" : bnode.getData().getNodeName()); //行业类别
                }
            }
            fristDto.setUnitProperty(BillPushConstant.getOtherCompanyNature(memberJobDto.getOtherCompanyNature())); //单位性质
            fristDto.setPost(""); //担任职务
            if (null != memberJobDto.getPositionsCode()) {
                bnode = nodeService.selectByPrimaryKey((long) memberJobDto.getPositionsCode());
                if (null != bnode) {
                    fristDto.setPost(bnode.getData() == null ? "" : bnode.getData().getNodeName()); //担任职务
                }
            }
            fristDto.setPayDate(memberJobDto.getSalaryDay() == null ? "" : memberJobDto.getSalaryDay().toString()); //每月发薪日
            fristDto.setSalary(
                    memberJobDto.getMonthlySalary() == null ? "" : memberJobDto.getMonthlySalary().toString()); //月基本工资
        } catch (Exception e) {
            excelErrorMsgs.add("合同号:"+fristDto.getContractNo()+"-账单号:"+fristDto.getBillId()+",组装工作信息异常");
            MyphLogger.error("组装工作信息异常", e);
        }
    }

    /**
     * 组装会员基本信息
     *
     * @param fristDto
     * @param memberInfo
     */
    private void toInfoMember(ContractRequestVo fristDto, MemberInfoDto memberInfo) {
        if(null == memberInfo) {
            return;
        }
        try {
            fristDto.setUserId("");
            fristDto.setUserName(memberInfo.getMemberName()); //姓名
            fristDto.setSex(memberInfo.getSex().toString()); //性别:0,男；1，女

            fristDto.setNational(memberInfo.getNation()); //民族
            fristDto.setIdNum(memberInfo.getIdCarNo()); //身份证
            fristDto.setEducation(BillPushConstant.getEdu(Integer.valueOf(memberInfo.getEduCode()))); //学历
            List<AddrDto> list = new ArrayList<AddrDto>();
            AddrDto map = new AddrDto();
            map.setAddr("");// 现地址
            map.setDetailAddr(memberInfo.getLiveAddress());//详细地址
            //1,现住址;2,公司地址;3,户籍地址
            if ("1".equals(memberInfo.getMailAddress())) {
                fristDto.setMailAddr(memberInfo.getLiveAddress()); //邮寄地址
            } else if ("3".equals(memberInfo.getMailAddress())) {
                fristDto.setMailAddr(memberInfo.getCensusAddress()); //邮寄地址
            }
            // 备用电话
            if (!StringUtils.isEmpty(memberInfo.getAlternatePhone())) {
                String[] phones = memberInfo.getAlternatePhone().split(",");
                fristDto.setStandbyNumList(Arrays.asList(phones));
            }
            fristDto.setAddrList(list);
            fristDto.setEmail(memberInfo.getEmail()); //电子邮箱
            fristDto.setMobile(memberInfo.getPhone()); //手机号
            fristDto.setWeixin(memberInfo.getSNSAccount()); //微信
            fristDto.setRegisterAddr("");
            fristDto.setRegisterDetailAddr(memberInfo.getCensusAddress()); //详细户籍地址
        } catch (Exception e) {
            excelErrorMsgs.add("合同号:"+fristDto.getContractNo()+"-账单号:"+fristDto.getBillId()+",组装会员基本信息异常");
            MyphLogger.error("组装会员基本信息异常", e);
        }
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MYPHConfigUtils mYPHConfigUtils;

    /**
     * @Description: 推送合同账单用户基础信息
     * @author heyx
     * @date 2017/3/10
     * @version V1.0
     */
    private ResultParams restPushBillAndContract(ContractRequestVo vo) {
        String url = mYPHConfigUtils.getRestContractBillUrl();
        MyphLogger.debug(url, vo.toString());
        ResultParams response = null;
        try {
            response = restTemplate.postForObject(url, vo, ResultParams.class);
        } catch (Exception e) {
            excelErrorMsgs.add("合同号:"+vo.getContractNo()+"-账单号:"+vo.getBillId()+",推送合同账单用户信息接口异常");
            MyphLogger.error(url+"===rest异常===", e);
        }
        return response;
    }

    /**
     * @Description: 推送账单信息
     * @author heyx
     * @date 2017/3/10
     * @version V1.0
     */
    private ResultParams restPushBill(RepayPlanRequestVo vo) {
        String url = mYPHConfigUtils.getRestRepayPlanUrl();
        MyphLogger.debug(url, vo.toString());
        ResultParams response = null;
        try {
            response = restTemplate.postForObject(url, vo, ResultParams.class);
        } catch (Exception e) {
            excelErrorMsgs.add("合同号:"+vo.getContractNo()+"-账单号:"+vo.getBillId()+",推送账单接口调用异常");
            MyphLogger.error(url+"===rest异常===", e);
        }
        return response;
    }
}
