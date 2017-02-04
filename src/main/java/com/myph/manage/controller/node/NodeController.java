/**   
 * @Title: NodeController.java 
 * @Package: com.myph.manage.controller.node 
 * @company: 麦芽金服
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 罗荣   
 * @date 2016年9月24日 下午2:44:44 
 * @version V1.0   
 */
package com.myph.manage.controller.node;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myph.common.constant.Constants;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.node.dto.SysNodeDto;
import com.myph.node.service.NodeService;

/**
 * @ClassName: NodeController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 罗荣
 * @date 2016年9月24日 下午2:44:44
 * 
 */
@Controller
@RequestMapping("/node")
public class NodeController {
    @Autowired
    private NodeService nodeService;

    @RequestMapping("/treeList")
    @ResponseBody
    public List<SysNodeDto> selectNodeList(String parentCode) {
        ServiceResult<List<SysNodeDto>> result = null;
        if (null != parentCode) {
            MyphLogger.debug("NodeController.treeList 输入参数[" + parentCode + "]");
            result = nodeService.getListByParent(parentCode);
        } else {
            result = nodeService.getTop();
        }
        return result.getData();
    }

    @RequestMapping("/treeListALL")
    @ResponseBody
    public List<SysNodeDto> treeListALL() {
        ServiceResult<List<SysNodeDto>> result = nodeService.getListAll();
        return result.getData();
    }

    @RequestMapping("/selectNodeList")
    @ResponseBody
    public AjaxResult treeList(String parentCode) {
        ServiceResult<List<SysNodeDto>> result = null;
        if (null != parentCode) {
            MyphLogger.debug("NodeController.selectNodeList 输入参数[" + parentCode + "]");
            result = nodeService.getListByParent(parentCode);
        } else {
            result = nodeService.getTop();
        }
        return AjaxResult.success(result.getData());
    }

    @RequestMapping("/saveNode")
    @ResponseBody
    public AjaxResult saveNode(String nodeName, String nodeCode, String parentCode) {
        ServiceResult result = null;
        if (null != parentCode) {
            result = nodeService.updateIsParent(parentCode, Constants.NO_INT);
            if (!result.success()) {
                return AjaxResult.formatFromServiceResult(result);
            }
        }
        SysNodeDto record = new SysNodeDto();
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDelFlag(Constants.YES_INT);
        record.setNodeCode(nodeCode);
        record.setParentCode(parentCode);
        record.setIsParent(Constants.YES_INT);
        record.setNodeName(nodeName);
        record.setCreateUser(ShiroUtils.getCurrentUserName());
        result = nodeService.insert(record);
        if (!result.success()) {
            return AjaxResult.formatFromServiceResult(result);
        }
        return AjaxResult.success(result.getData());
    }

    @RequestMapping("/deleteInfo")
    @ResponseBody
    public AjaxResult deleteInfo(Long id, String parentCode) {
        ServiceResult result = nodeService.delete(id);
        if (!result.success()) {
            return AjaxResult.formatFromServiceResult(result);
        }
        // 判断父级是否没有子级了，并报父级更新为不是父级状态
        if (StringUtils.isNotBlank(parentCode)) {
            result = nodeService.getChrildrenSize(parentCode);
            if (!result.success()) {
                return AjaxResult.formatFromServiceResult(result);
            }
            Long count = (Long) result.getData();
            // 如果父级的子级已经没有值了，更新为非父级数据
            if (count.equals(0l)) {
                result = nodeService.updateIsParent(parentCode, Constants.YES_INT);
                if (!result.success()) {
                    return AjaxResult.formatFromServiceResult(result);
                }
            }
        }
        return AjaxResult.success(result.getData());
    }

    @RequestMapping("/manage")
    public String manage() {
        return "/node/manage";
    }
}
