package com.myph.manage.controller.member;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.DateUtils;
import com.myph.common.util.SensitiveInfoUtils;
import com.myph.manage.common.constant.ClientType;
import com.myph.manage.common.util.BeanUtils;
import com.myph.manage.common.util.CommonUtil;
import com.myph.manage.controller.BaseController;
import com.myph.member.base.dto.MemberVerifyDto;
import com.myph.member.base.dto.MemberVerifyQueryDto;
import com.myph.member.base.service.MemberVerifyService;

@Controller
@RequestMapping("/member/verify")
public class MemberVerifyController extends BaseController {

    @Autowired
    private MemberVerifyService memberVerifyService;

    @RequestMapping("/list")
    public String verifylist(Model model, MemberVerifyQueryDto queryDto, BasePage basePage) {
        MyphLogger.debug("开始客户准入信息查询：/member/verify/list.htm|querDto=" + queryDto.toString() + "|basePage="
                + basePage.toString());
        ServiceResult<Pagination<MemberVerifyDto>> pageResult = memberVerifyService.listPageInfos(queryDto, basePage);
        List<MemberVerifyDto> lists = pageResult.getData().getResult();
        for (MemberVerifyDto member : lists) {
            member.setBirthday(CommonUtil.getBirthdayByIdCard(member.getIdCarNo()));
          //  member.setPhone(SensitiveInfoUtils.maskMobilePhone(member.getPhone()));// 隐藏手机号
          //  member.setMemberName(SensitiveInfoUtils.maskUserName(member.getMemberName()));// 隐藏姓名
            member.setIdCarNo(SensitiveInfoUtils.maskIdCard(member.getIdCarNo()));// 隐藏身份证
        }
        model.addAttribute("page", pageResult.getData());
        model.addAttribute("queryDto", queryDto);
        MyphLogger.debug("结束客户准入信息查询：/member/verify/list.htm|page=" + pageResult.toString());
        return "/member/member_verify_list";
    }

    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, MemberVerifyQueryDto queryDto) {
        MyphLogger.debug("开始客户准入信息导出：/member/verify/export.htm|queryDto=" + queryDto);
        try {
            // 设置参数查询满足条件的所有数据不分页
            List<MemberVerifyDto> list = memberVerifyService.listInfos(queryDto).getData();
            String columnNames[] = { "序号", "渠道来源", "姓名", "身份证号", "出生日期", "手机号码", "门店城市", "现居城市(个人资料)", "申请金额",
                    "系统准入是否通过", "系统准入未通过原因", "现居城市是否有门店", "创建日期" };// 列名
            String keys[] = { "index", "memberSource", "memberName", "idCarNo", "birthday", "phone", "liveCityOnApply",
                    "liveCity", "applyMoney", "creditResult", "creditResultMsg", "hasStore", "createTime" };
            String fileName = "APP客户信息" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
            // 获取Excel数据
            List<Map<String, Object>> excelList = getExcelMapList(list);
            // 导出Excel数据
            exportExcel(response, fileName, columnNames, keys, excelList);
        } catch (Exception e) {
            MyphLogger.error(e, "异常[结束客户准入信息导出：/member/verify/export.htm]");
        }
        MyphLogger.debug("结束客户准入信息导出：/member/verify/export.htm");
    }

    /**
     * 
     * @param srcList
     * @param destList
     * @param keys需转化的属性
     * @Description:dto转化为ExcelMap
     */
    private List<Map<String, Object>> getExcelMapList(List<MemberVerifyDto> srcList) {
        List<Map<String, Object>> destList = new ArrayList<Map<String, Object>>();
        if (null == srcList) {
            return destList;
        }
        int index = 1;
        for (MemberVerifyDto dto : srcList) {
            Map<String, Object> destMap = BeanUtils.transBeanToMap(dto);
            destMap.put("index", index++);
            if (ClientType.APP.getCode().equals(dto.getMemberSource())) {
                destMap.put("memberSource", "APP");
            } else {
                destMap.put("memberSource", "线下");
            }
            Date birthday = CommonUtil.getBirthdayByIdCard(dto.getIdCarNo());
            if (null != birthday) {
                SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_PATTERN);
                destMap.put("birthday", sdf.format(birthday));
            }
            int creditResult = dto.getCreditResult();
            if (creditResult == Constants.YES_INT) {
                destMap.put("creditResult", "通过");
            } else if (creditResult == Constants.NO_INT) {
                destMap.put("creditResult", "不通过");
            } else {
                destMap.put("creditResult", "未知");
            }
            if (Constants.YES_INT == dto.getHasStore()) {
                destMap.put("hasStore", "有");
            } else {
                destMap.put("hasStore", "没有");
            }
            
            destMap.put("idCarNo", SensitiveInfoUtils.maskIdCard(dto.getIdCarNo()));
            destList.add(destMap);
        }
        return destList;
    }

}
