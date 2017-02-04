/**   
 * @Title: NodeController.java 
 * @Package: com.myph.manage.controller.node 
 * @company: 麦芽金服
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 罗荣   
 * @date 2016年9月24日 下午2:44:44 
 * @version V1.0   
 */
package com.myph.manage.controller.refusereason;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.node.dto.SysNodeDto;
import com.myph.node.service.NodeService;
import com.myph.refusereason.dto.SysRefuseReasonDto;
import com.myph.refusereason.service.SysRefuseReasonService;

/**
 * @ClassName: NodeController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 罗荣
 * @date 2016年9月24日 下午2:44:44
 * 
 */
@Controller
@RequestMapping("/refuseReason")
public class RefuseReasonController {
    @Autowired
    SysRefuseReasonService refuseReason;

    @RequestMapping("/selectAll")
    @ResponseBody
    public AjaxResult selectAll(String parentCode) {
        ServiceResult<List<SysRefuseReasonDto>> result = refuseReason.selectAll();
        return AjaxResult.success(result.getData());
    }
}
