package com.myph.manage.controller.apply;

import java.util.Date;

import com.myph.team.dto.SysTeamDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.apply.dto.ApplyInfoDto;
import com.myph.apply.dto.ApplyUserDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.apply.service.ApplyUserService;
import com.myph.common.constant.RedisConstants;
import com.myph.common.log.MyphLogger;
import com.myph.common.redis.CacheService.KeyBase;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.constant.bis.ApplyBisStateEnum;
import com.myph.manage.common.constant.ApplyOperateEnum;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.member.base.constant.MemberInfoServiceResultCode;
import com.myph.member.base.dto.MemberInfoDto;
import com.myph.member.base.service.MemberInfoService;

/**
 * 
 * @ClassName: ReceptionController
 * @Description: 申请件
 * @author heyx
 * @date 2016年9月6日 下午3:56:07
 *
 */
@Controller
@RequestMapping("/apply/member")
public class ApplyMemberController extends ApplyBaseController{

    @Autowired
    ApplyInfoService applyInfoService;

    @Autowired
    ApplyUserService applyUserService;

    @Autowired
    MemberInfoService memberInfoService;

    /**
     * 根据applyLoanNo加载个人信息服务
     * 
     * @param applyLoanNo
     * @return
     */
    @RequestMapping("/userInfoQueryByNo/{applyLoanNo}")
    @ResponseBody
    public AjaxResult userInfoQueryByNo(@PathVariable String applyLoanNo) {
        try {
            ServiceResult<ApplyUserDto> data = applyUserService.queryInfoByLoanNo(applyLoanNo);
            return AjaxResult.success(data.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "加载对应个人信息异常,ID:{}", applyLoanNo);
            return AjaxResult.failed("加载对应个人信息异常");
        }
    }

    /**
     * 根据applyLoanNo加载申请主表信息服务
     * 
     * @param applyLoanNo
     * @return
     */
    @RequestMapping("/applyInfoQueryByNo/{applyLoanNo}")
    @ResponseBody
    public AjaxResult applyInfoQueryByNo(@PathVariable String applyLoanNo) {
        try {
            ServiceResult<ApplyInfoDto> data = applyInfoService.queryInfoByLoanNo(applyLoanNo);
            return AjaxResult.success(data.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "加载对应申请主表信息异常,ID:{}", applyLoanNo);
            return AjaxResult.failed("加载对应申请主表信息异常");
        }
    }

    /**
     * 录入个人信息
     * 
     * @return
     */
    @RequestMapping("/addInfo")
    @ResponseBody
    public AjaxResult addInfo(ApplyUserDto applyUserDto, Integer type) {
        MyphLogger.info("录入个人信息 updateInfoBack 输入参数{}", applyUserDto.toString());
        try {
            ServiceResult<ApplyUserDto> userDto = applyUserService.queryInfoByLoanNo(applyUserDto.getApplyLoanNo());
            // web端修改入口
            if (null != userDto.getData()) {
                return AjaxResult.failed(applyUserDto.getApplyLoanNo()+"已经新增成功，修改请关闭当前界面，从申请单管理界面重新进入！");
            }
            // update团队经理
            SysTeamDto teamDto = getTeamManage(applyUserDto.getBmId());
            if(null == teamDto) {
                return AjaxResult.failed(NO_TEAM_STR);
            } else {
                applyUserDto.setTeamManageId(teamDto.getLeaderId());
                applyUserDto.setTeamManageName(teamDto.getLeaderName());
            }
            ServiceResult<Integer> data = null;
            if (null != applyUserDto && null != applyUserDto.getEmail()) {
                applyUserDto.setEmail(applyUserDto.getEmail().toLowerCase());
            }
            if (ApplyOperateEnum.APPLYUSER_ADD.getCode().equals(type)) {
                /*
                 * 新增保存 <br>
                 */
                data = applyUserService.addInfo(applyUserDto, null);
            } else if (ApplyOperateEnum.APPLYUSER_UPDATE.getCode().equals(type)) {
                /*
                 * 新增提交 <br>
                 */
                data = applyUserService.addInfo(applyUserDto, ApplyBisStateEnum.WORKINFO.getCode());
                updateMemberInfo(applyUserDto);
            } else {
                MyphLogger.info("新增个人信息异常,type:{}", type);
                return AjaxResult.failed("新增个人信息异常");
            }
            return AjaxResult.formatFromServiceResult(data);
        } catch (Exception e) {
            MyphLogger.error(e, "新增个人信息异常,dto:{}", applyUserDto.toString());
            return AjaxResult.failed("新增个人信息异常");
        }
    }

    /**
     * 修改个人信息
     * 
     * @return
     */
    @RequestMapping("/updateInfo")
    @ResponseBody
    public AjaxResult updateInfo(ApplyUserDto applyUserDto, Integer type) {
        if (null == applyUserDto) {
            return AjaxResult.failed("申请件个人信息不能为空");
        }
        String applyLoanNo = applyUserDto.getApplyLoanNo();
        if (StringUtils.isEmpty(applyLoanNo)) {
            return AjaxResult.failed("申请单号不能为空");
        }
        MyphLogger.info("修改个人信息 updateInfo 输入参数{}", applyUserDto.toString());
        try {
            // update团队经理
            SysTeamDto teamDto = getTeamManage(applyUserDto.getBmId());
            if(null == teamDto) {
                return AjaxResult.failed(NO_TEAM_STR);
            } else {
                applyUserDto.setTeamManageId(teamDto.getLeaderId());
                applyUserDto.setTeamManageName(teamDto.getLeaderName());
            }
            ServiceResult<Integer> data = null;
            if (null != applyUserDto && null != applyUserDto.getEmail()) {
                applyUserDto.setEmail(applyUserDto.getEmail().toLowerCase());
            }
            if (ApplyOperateEnum.APPLYUSER_ADD.getCode().equals(type)) {
                // 修改保存
                data = applyUserService.updateInfo(applyUserDto, null);
            } else if (ApplyOperateEnum.APPLYUSER_UPDATE.getCode().equals(type)) {
                // 修改提交
                data = applyUserService.updateInfo(applyUserDto, ApplyBisStateEnum.WORKINFO.getCode());
                updateMemberInfo(applyUserDto);
            } else {
                MyphLogger.info("修改个人信息异常,type:{}", type);
                return AjaxResult.failed("修改个人信息异常");
            }
            if (null != data && data.success()) {
                // APP:修改工人信息，去掉redis缓存
                KeyBase.delete(RedisConstants.KEY_MYPH_USER_INFO);
            }
            return AjaxResult.formatFromServiceResult(data);
        } catch (Exception e) {
            MyphLogger.error(e, "修改个人信息异常,dto:{}", applyUserDto.toString());
            return AjaxResult.failed("修改个人信息异常");
        }
    }

    /**
     * 退回修改个人信息
     * 
     * @return
     */
    @RequestMapping("/updateInfoBack")
    @ResponseBody
    public AjaxResult updateInfoBack(ApplyUserDto applyUserDto, Integer type) {
        MyphLogger.info("退回修改个人信息 updateInfoBack 输入参数{}", applyUserDto.toString());
        try {
            ServiceResult<ApplyInfoDto> applyInfoRes = applyInfoService.queryInfoByAppNo(applyUserDto.getApplyLoanNo());
            AjaxResult continueResult = getReusltIsContinue(applyInfoRes.getData());
            if(!continueResult.isSuccess()) {
                return continueResult;
            }
            ServiceResult<Integer> data = null;
            // 修改保存
            data = applyUserService.updateInfoBack(applyUserDto);
            updateMemberInfo(applyUserDto);
            return AjaxResult.formatFromServiceResult(data);
        } catch (Exception e) {
            MyphLogger.error(e, "修改个人信息异常,dto:{}", applyUserDto.toString());
            return AjaxResult.failed("修改个人信息异常");
        }
    }

    private void updateMemberInfo(ApplyUserDto applyUserDto) {
        MemberInfoDto mDto = new MemberInfoDto();
        BeanUtils.copyProperties(applyUserDto, mDto);
        mDto.setModifyTime(new Date());
        mDto.setModifyUser(ShiroUtils.getCurrentUserName());
        ServiceResult<Integer> mResult = memberInfoService.updateInfoByIdCard(mDto);
        if (mResult.getCode().equals(MemberInfoServiceResultCode.UPDATE_ZERO.getCode())) {
            mDto.setCreateTime(new Date());
            mDto.setCreateUser(ShiroUtils.getCurrentUserName());
            memberInfoService.addInfo(mDto);
        }
    }

    /**
     * 
     * @名称 newInfoIndex
     * @描述 TODO(这里用一句话描述这个方法的作用)
     * @返回类型 String
     * @日期 2016年9月9日 上午9:55:18
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    @RequestMapping("/userInfoUpdateForm")
    public String userInfoUpdateForm(Model model, String applyLoanNo) {
        MyphLogger.info("userInfoUpdateForm 输入参数{}", applyLoanNo);
        ServiceResult<ApplyInfoDto> apply = applyInfoService.queryInfoByLoanNo(applyLoanNo);
        model.addAttribute("apply", apply.getData());
        ServiceResult<ApplyUserDto> userDto = applyUserService.queryInfoByLoanNo(applyLoanNo);
        model.addAttribute("userDto", userDto.getData());
        return "/apply/userinfo/userInfo_update";

    }

    /**
     * 
     * @名称 newInfoIndex
     * @描述 TODO(这里用一句话描述这个方法的作用)
     * @返回类型 String
     * @日期 2016年9月9日 上午9:55:18
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    @RequestMapping("/userInfoAddForm")
    public String userInfoAddForm(Model model, String applyLoanNo) {
        MyphLogger.info("userInfoAddForm 输入参数{}", applyLoanNo);
        ServiceResult<ApplyInfoDto> data = applyInfoService.queryInfoByLoanNo(applyLoanNo);
        model.addAttribute("apply", data.getData());
        return "/apply/userinfo/userInfo_add";

    }

    // 个人资产页
    @RequestMapping("/auditDetail")
    public String auditDetail() {
        MyphLogger.info("ApplyInfoInputController.applyPersonassets 输入参数{}");

        return "/apply/auditDetail";

    }

    /**
     * 
     * @名称 newInfoIndex
     * @描述 个人信息操作界面入口
     * @返回类型 String
     * @日期 2016年9月9日 上午9:55:18
     * @创建人 heyx
     * @更新人 heyx
     *
     */
    @RequestMapping("/userInfoForm")
    public String userInfoForm(Model model, String applyLoanNo) {
        MyphLogger.info("userInfoUpdateForm 输入参数{}", applyLoanNo);
        ServiceResult<ApplyInfoDto> apply = applyInfoService.queryInfoByLoanNo(applyLoanNo);
        model.addAttribute("apply", apply.getData());
        ServiceResult<ApplyUserDto> userDto = applyUserService.queryInfoByLoanNo(applyLoanNo);
        // web端修改入口
        if (null != userDto.getData()) {
            model.addAttribute("userDto", userDto.getData());
            return "/apply/userinfo/userInfo_update";
        }
        // app端新增入口
        // if (null != apply.getData() && null != apply.getData().getClientType()
        // && apply.getData().getClientType().equals(ClientType.APP.getCode())) {
//        MyphLogger.info("app个人信息录入 输入参数{}", applyLoanNo);
        ServiceResult<MemberInfoDto> member = memberInfoService.queryInfoByIdCard(apply.getData().getIdCard());
        model.addAttribute("member", member.getData());
        return "/apply/userinfo/userInfoApp_add";
        // }
        // web端新增入口
        // return "/apply/userinfo/userInfo_add";
    }

}
