/**   
 * @Title: ContactsInputController.java 
 * @Package: com.myph.manage.controller.apply.contacts
 * @company: 麦芽金服
 * @Description: TODO
 * @date 2016年9月14日 下午3:02:34 
 * @version V1.0   
 */
package com.myph.manage.controller.apply.contacts;

import com.myph.apply.dto.ApplyInfoDto;
import com.myph.manage.controller.apply.ApplyBaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.apply.constant.LinkmanTypeEnum;
import com.myph.apply.dto.ApplyUserDto;
import com.myph.apply.dto.JkApplyLinkmanDto;
import com.myph.apply.dto.MemberLinkmanDto;
import com.myph.apply.linkman.service.ContactsService;
import com.myph.apply.linkman.service.MemberLinkmanService;
import com.myph.apply.service.ApplyInfoService;
import com.myph.apply.service.ApplyUserService;
import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.member.base.dto.MemberInfoDto;
import com.myph.member.base.service.MemberInfoService;

/**
 * 联系人录入
 * 
 * @ClassName: ContactsInputController
 * @Description: TODO
 * @author hf
 * @date 2016年9月14日 下午3:02:34
 * 
 */
@RequestMapping("/apply")
@Controller
public class ContactsInputController extends ApplyBaseController{

	@Autowired
	private ContactsService contactsService;

	@Autowired
	private MemberLinkmanService memberLinkmanService;

	@Autowired
	private ApplyUserService applyUserService;

	@Autowired
	private MemberInfoService memberInfoService;

	@Autowired
	private ApplyInfoService applyInfoService;

	/**
	 * 联系人信息加载
	 * 
	 * @名称 contactsInput
	 * @描述 TODO
	 * @返回类型 String
	 * @日期 2016年9月18日 上午11:21:46
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/newInfoIndex/contactsinput")
	public String contactsInput(Model model, String applyLoanNo) {
		MyphLogger.debug("联系人录入加载,申请单单号:{}", applyLoanNo);
		ApplyUserDto applyUser = applyUserService.queryInfoByLoanNo(applyLoanNo).getData();
		if (applyUser != null) {
			MemberInfoDto memberInfo = memberInfoService.queryInfoByIdCard(applyUser.getIdCarNo()).getData();
			MemberLinkmanDto memberLinkman = memberLinkmanService.selectSingleMemberLinkman(memberInfo.getId(),
					LinkmanTypeEnum.FAMILY_CONTACT.getType()).getData();
			if(null != memberLinkman) {
				model.addAttribute("familyMap", memberLinkman);
			}
			MemberLinkmanDto workMemberLinkman = memberLinkmanService.selectSingleMemberLinkman(memberInfo.getId(),
					LinkmanTypeEnum.WORK_CONTACT.getType()).getData();
			if(null != workMemberLinkman) {
				model.addAttribute("workMap", workMemberLinkman);
			}
			MemberLinkmanDto friendMemberLinkman = memberLinkmanService.selectSingleMemberLinkman(memberInfo.getId(),
					LinkmanTypeEnum.FRIEND_CONTACT.getType()).getData();
			if(null != friendMemberLinkman) {
				model.addAttribute("friendMap", friendMemberLinkman);
			}
			MemberLinkmanDto otherMemberLinkman = memberLinkmanService.selectSingleMemberLinkman(memberInfo.getId(),
					LinkmanTypeEnum.OTHER_CONTACT.getType()).getData();
			if(null != otherMemberLinkman) {
				model.addAttribute("otherMap", otherMemberLinkman);
			}
		}
		model.addAttribute("applyLoanNo", applyLoanNo);
		return "/apply/contacts_input";
	}

	/**
	 * 申请件联系人信息录入
	 * 
	 * @名称 contactsSave
	 * @描述 TODO
	 * @返回类型 AjaxResult
	 * @日期 2016年9月18日 下午3:32:06
	 * @创建人 hf
	 * @更新人 hf
	 *
	 */
	@RequestMapping("/newInfoIndex/contactsSave")
	@ResponseBody
	public AjaxResult contactsSave(JkApplyLinkmanDto jkApplyLinkmanDto) {
		String operatorName = ShiroUtils.getCurrentUserName();
		Long operatorId = ShiroUtils.getCurrentUserId();
		MyphLogger.info("申请单录入联系人信息保存,请求参数:{},当前操作人:{},操作人编号:{}", jkApplyLinkmanDto, operatorName, operatorId);
		try {
			if (null == jkApplyLinkmanDto) {
				return AjaxResult.failed("申请件联系人信息不能为空");
			}
			String applyLoanNo = jkApplyLinkmanDto.getApplyLoanNo();
			if (StringUtils.isEmpty(applyLoanNo)) {
				return AjaxResult.failed("申请单号不能为空");
			}
			//+++++++加入回源状态判断
			ServiceResult<ApplyInfoDto> applyInfoRes = applyInfoService.queryInfoByAppNo(applyLoanNo);
			AjaxResult continueResult = getReusltIsContinue(applyInfoRes.getData());
			if(!continueResult.isSuccess()) {
				return continueResult;
			}
            //更新或插入MemberLinkman表
			JkApplyLinkmanDto oldJkApplyLinkmanDto = insertOrUpdateMemberLinkman(jkApplyLinkmanDto);
            //更新或插入jkApplyLinkman表
            insertOrUpdateJkApplyLinkman(jkApplyLinkmanDto,oldJkApplyLinkmanDto);   
            return AjaxResult.success();
		} catch (Exception e) {
			MyphLogger.error("申请件联系人信息保存异常", e);
			return AjaxResult.failed("服务异常，请稍后重试");
		}
	}
	
	private JkApplyLinkmanDto insertOrUpdateMemberLinkman(JkApplyLinkmanDto jkApplyLinkmanDto){
	    String oldPhone = "";
        String operatorName = ShiroUtils.getCurrentUserName();
        Long operatorId = ShiroUtils.getCurrentUserId();
        String applyLoanNo = jkApplyLinkmanDto.getApplyLoanNo();
        MemberLinkmanDto memberLinkmanDto = new MemberLinkmanDto();
        BeanUtils.copyProperties(jkApplyLinkmanDto, memberLinkmanDto);
        if(jkApplyLinkmanDto.getId() != null){
            ServiceResult<MemberLinkmanDto> result = memberLinkmanService.selectSelectiveById(jkApplyLinkmanDto.getId());
            oldPhone = result.getData().getLinkManPhone();
            if(!oldPhone.equals(jkApplyLinkmanDto.getLinkManPhone())){
                String alternatePhone = result.getData().getAlternatePhone() + "|" + jkApplyLinkmanDto.getLinkManPhone();
                memberLinkmanDto.setAlternatePhone(alternatePhone);
            }
            memberLinkmanService.updateLinkman(memberLinkmanDto, operatorName);
        } else {
            ApplyUserDto applyUser = applyUserService.queryInfoByLoanNo(applyLoanNo).getData();
            MemberInfoDto memberInfo = memberInfoService.queryInfoByIdCard(applyUser.getIdCarNo()).getData();
            Long memberId = memberInfo.getId();
            memberLinkmanDto.setMemberId(memberId);
            memberLinkmanDto.setClientType(Constants.NO_INT);
            memberLinkmanDto.setAlternatePhone(jkApplyLinkmanDto.getLinkManPhone());
            memberLinkmanService.saveLinkman(memberLinkmanDto, operatorName);
        }
        if (jkApplyLinkmanDto.getState() != null) {
            MyphLogger.info("申请单录入联系人信息录入下一步，更新申请单表状态,单号:{},当前操作人:{},操作人编号:{}", applyLoanNo, operatorName,
                    operatorId);
            applyInfoService.updateSubState(applyLoanNo, jkApplyLinkmanDto.getState());
        }
        JkApplyLinkmanDto result = new JkApplyLinkmanDto();
        result.setLinkManType(jkApplyLinkmanDto.getLinkManType());
        result.setLinkManPhone(oldPhone);
        return result;
    }
    
    private void insertOrUpdateJkApplyLinkman(JkApplyLinkmanDto jkApplyLinkmanDto,JkApplyLinkmanDto oldJkApplyLinkmanDto){
        String operatorName = ShiroUtils.getCurrentUserName();
        ServiceResult<JkApplyLinkmanDto> contactResult = contactsService
                .getSingleApplyLinkman(jkApplyLinkmanDto.getApplyLoanNo(), oldJkApplyLinkmanDto);
        if (contactResult.success() && contactResult.getData() != null) {
            // 更新
            jkApplyLinkmanDto.setId(contactResult.getData().getId());
            contactsService.updateLinkman(jkApplyLinkmanDto, operatorName);
        } else {
            // 新增
            contactsService.saveLinkman(jkApplyLinkmanDto, operatorName);
        }
    }
}
