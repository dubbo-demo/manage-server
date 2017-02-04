/**   
 * @Title: JkApplyAuditController.java 
 * @Package: com.myph.manage.audit.controller
 * @company: 麦芽金服
 * @Description: TODO
 * @date 2016年9月20日 下午9:17:42 
 * @version V1.0   
 */
package com.myph.manage.controller.audit;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.manage.controller.BaseController;
import com.myph.visit.dto.VisitDetailDto;
import com.myph.visit.service.VisitService;

/**
 * 
 * @ClassName: AuditVisitController
 * @Description: 信审审批-外访详情
 * @author 吴阳春
 * @date 2016年9月29日 下午2:37:43
 *
 */
@Controller
@RequestMapping("/audit")
public class AuditVisitController extends BaseController {
    
    @Autowired
    private VisitService visitService;

    @RequestMapping("/auditVisit")
    public String visitDetail(Model model, String applyLoanNo, String cType) {
        ServiceResult<List<VisitDetailDto>> result = visitService.getResultByApplyNO(applyLoanNo);
        List<VisitDetailDto> visitList = result.getData();

        if (visitList.size() > 0) {
            Collections.reverse(visitList);
        }
        model.addAttribute("cType", cType);
        model.addAttribute("visitList", visitList);
        model.addAttribute("applyLoanNo", applyLoanNo);
        return "/apply/audit/audit_visit";
    }

    @RequestMapping(value = "/checkVisit", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult checkVisit(Model model, String applyLoanNo) {
        ServiceResult<List<VisitDetailDto>> result = visitService.getResultByApplyNO(applyLoanNo);
        if (result.getData().size() > 0) {
            return AjaxResult.success(1);
        } else {
            return AjaxResult.success(0);
        }
    }
}
