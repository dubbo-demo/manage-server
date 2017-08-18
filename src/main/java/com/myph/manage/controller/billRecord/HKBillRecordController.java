package com.myph.manage.controller.billRecord;

import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.ServiceResult;
import com.myph.common.rom.annotation.BasePage;
import com.myph.common.rom.annotation.Pagination;
import com.myph.employee.dto.EmployeeDetailDto;
import com.myph.employee.dto.EmployeeInfoDto;
import com.myph.hkrecord.dto.HkBillRepayRecordDto;
import com.myph.hkrecord.dto.HkBillRepayRecordQueryDto;
import com.myph.hkrecord.service.HkBillRepayRecordService;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.manage.controller.apply.ApplyBaseController;
import com.myph.node.dto.SysNodeDto;
import com.myph.team.dto.SysTeamDto;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 *
 * @ClassName: ReceptionController
 * @Description: 申请件
 * @author heyx
 * @date 2016年9月6日 下午3:56:07
 *
 */
@Controller
@RequestMapping("/hKBillRecord")
public class HKBillRecordController extends ApplyBaseController{

    @Autowired
    HkBillRepayRecordService hkBillRepayRecordService;

    /**
     * 团队列表
     *
     * @param model
     * @param
     * @param basePage
     * @return
     */
    @RequestMapping("/list")
    public String list(Model model, HkBillRepayRecordQueryDto queryDto, BasePage basePage) {
        ServiceResult<Pagination<HkBillRepayRecordDto>>  resultInfo = hkBillRepayRecordService.queryPagination(queryDto,basePage);
        model.addAttribute("queryDto", queryDto);
        model.addAttribute("page", resultInfo.getData());
        MyphLogger.info("团队管理列表分页查询", resultInfo.getData());
        return "/billRecord/billRecord_list";
    }
}
