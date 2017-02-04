package com.myph.manage.controller.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myph.apply.constant.LinkmanTypeEnum;
import com.myph.apply.dto.MemberJobDto;
import com.myph.apply.dto.MemberLinkmanDto;
import com.myph.apply.jobinfo.service.MemberJobService;
import com.myph.apply.linkman.service.MemberLinkmanService;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.common.util.SensitiveInfoUtils;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.employee.service.EmployeeInfoService;
import com.myph.manage.common.util.CommonUtil;
import com.myph.manage.controller.BaseController;
import com.myph.member.assets.dto.MemberAssetsDto;
import com.myph.member.assets.service.AssetsService;
import com.myph.member.base.dto.MemberInfoDto;
import com.myph.member.base.dto.MemberQueryDto;
import com.myph.member.base.service.MemberInfoService;
import com.myph.member.confine.dto.MemberConfineLogDto;
import com.myph.member.confine.service.MemberConfineLogService;
import com.myph.member.intenetinfo.dto.IntenetInfoDto;
import com.myph.member.intenetinfo.service.IntenetInfoService;
import com.myph.personassets.dto.ApplyPersonassetsDto;

@Controller
@RequestMapping("/member")
public class MemberInfoController extends BaseController {
    @Autowired
    private MemberInfoService memberInfoService;
    @Autowired
    private MemberJobService memberJobService;
    @Autowired
    private MemberLinkmanService memberLinkmanService;
    @Autowired
    private AssetsService assetsService;
    @Autowired
    private MemberConfineLogService memberConfineLogService;
    @Autowired
    private IntenetInfoService intenetInfoService;
    @Autowired
    private EmployeeInfoService employeeInfoService;

    /**
     * 
     * @名称 list
     * @描述 查询客户列表
     * @返回类型 String
     * @日期 2016年10月13日 上午10:57:45
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    @RequestMapping("/list")
    public String list(Model model, MemberQueryDto queryDto, BasePage basePage) {
        MyphLogger.debug("开始客户查询：/member/list.htm|querDto=" + queryDto.toString() + "|basePage=" + basePage.toString());
        ServiceResult<Pagination<MemberInfoDto>> pageResult = memberInfoService.listPageInfos(queryDto, basePage);
        List<MemberInfoDto> lists = pageResult.getData().getResult();
        for (MemberInfoDto member : lists) {
            member.setBirthday(CommonUtil.getBirthdayByIdCard(member.getIdCarNo()));
            MemberJobDto job = memberJobService.selectByMemberId(member.getId()).getData();
            if (null != job) {
                member.setUnitName(job.getUnitName());
                String companyAddress = job.getCompanyAddress();
                String detailAddr = job.getDetailAddr();
                member.setDetailAddr((null == companyAddress ? "" : companyAddress)
                        + (null == detailAddr ? "" : detailAddr));

            }
            // 查询互联网信息
            IntenetInfoDto intenet = intenetInfoService.queryIntenetInfoByMemberId(member.getId()).getData();
            if (null != intenet) {
                member.setTaoBao(intenet.getTaoBao());
                member.setJingDong(intenet.getJingDong());
            }
            // 查询推荐人信息
            if (null != member.getEmployeeId()) {
                EmployeeInfoDto employee = employeeInfoService.getEntityById(member.getEmployeeId()).getData();
                if (null != employee) {
                    member.setEmployeeNo(employee.getEmployeeNo());
                }
            }
            member.setPhone(SensitiveInfoUtils.maskMobilePhone(member.getPhone()));// 隐藏手机号
            member.setMemberName(SensitiveInfoUtils.maskUserName(member.getMemberName()));// 隐藏姓名
            member.setIdCarNo(SensitiveInfoUtils.maskIdCard(member.getIdCarNo()));// 隐藏身份证
        }
        model.addAttribute("page", pageResult.getData());
        model.addAttribute("queryDto", queryDto);
        MyphLogger.debug("结束客户查询：/member/list.htm|page=" + pageResult.toString());
        return "/member/member_list";
    }

    /**
     * 
     * @名称 detailUI
     * @描述 个人信息详情页
     * @返回类型 String
     * @日期 2016年10月14日 上午9:53:23
     * @创建人 王海波
     * @更新人 王海波
     *
     */
    @RequestMapping("/detailUI")
    public String detailUI(Model model, Long id) {
        // 员工信息
        MemberInfoDto memberInfo = memberInfoService.getMemberInfoById(id).getData();
        model.addAttribute("memberInfo", memberInfo);
        // 工作信息
        MemberJobDto jobInfo = memberJobService.selectByMemberId(id).getData();
        model.addAttribute("memberType", 1);
        model.addAttribute("jobInfo", jobInfo);
        // 联系人信息
        List<MemberLinkmanDto> linkmanInfos = memberLinkmanService.getLinkmansByMemId(id).getData();
        for (MemberLinkmanDto linkmanDto : linkmanInfos) {
            if (LinkmanTypeEnum.FAMILY_CONTACT.getType() == linkmanDto.getLinkManType()) {
                model.addAttribute("familyMap", linkmanDto);
                continue;
            }
            if (LinkmanTypeEnum.WORK_CONTACT.getType() == linkmanDto.getLinkManType()) {
                model.addAttribute("workMap", linkmanDto);
                continue;
            }
            if (LinkmanTypeEnum.FRIEND_CONTACT.getType() == linkmanDto.getLinkManType()) {
                model.addAttribute("friendMap", linkmanDto);
                continue;
            }
            if (LinkmanTypeEnum.OTHER_CONTACT.getType() == linkmanDto.getLinkManType()) {
                model.addAttribute("otherMap", linkmanDto);
                continue;
            }
        }
        model.addAttribute("linkmanInfos", linkmanInfos);
        // 资产信息
        MemberAssetsDto assetsInfo = assetsService.getAssetsByMemId(id).getData();
        if (null != assetsInfo) {
            ApplyPersonassetsDto personassets = new ApplyPersonassetsDto();
            personassets.setLoanCarAmount(assetsInfo.getCarMortgageMoney());
            personassets.setCarNum(assetsInfo.getCarAmount());
            personassets.setLoanCarNum(assetsInfo.getMcarAmount());
            personassets.setLoanHouseAmount(assetsInfo.getHouseMortgageMoney());
            personassets.setHouseNum(assetsInfo.getHouseAmount());
            personassets.setLoanHouseNum(assetsInfo.getMHouseAmount());
            model.addAttribute("record", personassets);
        }

        // 禁闭期信息
        List<MemberConfineLogDto> confineLogs = memberConfineLogService.listInfosByMemberId(id).getData();
        model.addAttribute("confineLogs", confineLogs);
        return "/member/member_detail";
    }

}
