package com.myph.manage.controller.productFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.myph.apply.dto.FileDto;
import com.myph.apply.dto.FileManageApplyInfoDto;
import com.myph.apply.dto.FileUploadDto;
import com.myph.apply.service.ApplyInfoService;
import com.myph.apply.service.FileInfoService;
import com.myph.common.hbase.HbaseUtils;
import com.myph.common.log.MyphLogger;
import com.myph.common.result.AjaxResult;
import com.myph.common.result.ServiceResult;
import com.myph.common.zip.ZipCompress;
import com.myph.constant.ApplyUtils;
import com.myph.constant.FileUpSysNodeEnum;
import com.myph.constant.FlowStateEnum;
import com.myph.constant.NodeConstant;
import com.myph.constant.ProductNodeEnum;
import com.myph.employee.dto.EmployeePositionInfoDto;
import com.myph.fileInfo.dto.JkAppFileInfoDto;
import com.myph.fileInfo.service.JkAppFileInfoService;
import com.myph.fileRelation.dto.JkAppFileDto;
import com.myph.fileRelation.dto.JkAppFileRelationDto;
import com.myph.fileRelation.service.JkAppFileRelationService;
import com.myph.manage.common.constant.ClientType;
import com.myph.manage.common.constant.RepayUpLoadFileTypeEnum;
import com.myph.manage.common.shiro.ShiroUtils;
import com.myph.member.base.dto.MemberInfoDto;
import com.myph.member.base.service.MemberInfoService;
import com.myph.node.dto.SysNodeDto;
import com.myph.node.service.NodeService;
import com.myph.position.dto.PositionDto;
import com.myph.position.service.PositionService;
import com.myph.product.dto.ProductFiletypeDto;
import com.myph.product.service.ProductFileTypeService;
import com.myph.product.service.ProductService;
import com.myph.reception.dto.ApplyReceptionDto;
import com.myph.reception.service.ApplyReceptionService;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author 吴阳春
 * @ClassName: FileUploadController
 * @Description: 文件上传
 * @date 2016年9月12日 下午7:34:30
 */
@Controller
@RequestMapping("/productFile")
public class FileUploadController {

    @Autowired
    private ApplyInfoService applyInfoService;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private FileInfoService fileInfoService;

    @Autowired
    private ProductFileTypeService productFileTypeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PositionService positionService;
    
    @Autowired
    private ApplyReceptionService applyReceptionService;
    
    @Autowired
    private MemberInfoService memberInfoService;
    
    @Autowired
    private JkAppFileRelationService jkAppFileRelationService;
    
    @Autowired
    private JkAppFileInfoService jkAppFileInfoService;

    @RequestMapping("/fileUpload")
    public String fileUpload(Model model, FileUploadDto fileUploadDto) {
        try {
            // 查询进件表基本信息
            ServiceResult<FileManageApplyInfoDto> result = applyInfoService
                    .selectByApplyLoanNo(fileUploadDto.getApplyLoanNo());
            // 1、补充主状态名称
            Integer state = result.getData().getState();
            Integer subState = result.getData().getSubState();
            if (state != null) {
                fileUploadDto.setState(state);
                fileUploadDto.setStateName(ApplyUtils.getFullStateDesc(state, subState));
            }
            // 2、补充产品名称
            // 查询申请表信息,由于产品类型变更会对附件产生影响，这里取最初的产品类型。
            ServiceResult<ApplyReceptionDto> applyReceptionResult = applyReceptionService
                    .queryInfoByApplyLoanNo(fileUploadDto.getApplyLoanNo());
            ServiceResult<String> productNameResult = productService
                    .getProductNameById(applyReceptionResult.getData().getProductId());
            fileUploadDto.setProductTypeId(applyReceptionResult.getData().getProdType());
            fileUploadDto.setProductName(productNameResult.getData());
            // 3、补充客户姓名
            fileUploadDto.setMemberName(result.getData().getMemberName());
            // 4、查询文件基本信息
            ServiceResult<List<FileDto>> fileDtoListResult = fileInfoService
                    .selectByApplyLoanNo(fileUploadDto.getApplyLoanNo());
            List<FileDto> fileDtoList = fileDtoListResult.getData();
            // 5、补充文件状态名称
            Set<Long> uploadStateSet = new HashSet<Long>();
            for (int i = 0; i < fileDtoList.size(); i++) {
                Long uploadState = fileDtoList.get(i).getUploadState();
                uploadStateSet.add(uploadState);
            }
            List<Long> listId = new ArrayList<Long>();
            listId.addAll(uploadStateSet);
            ServiceResult<List<SysNodeDto>> sysNodeDtoListResult = nodeService.getListByListid(listId);
            List<SysNodeDto> sysNodeDtoListParam = sysNodeDtoListResult.getData();
            Map<Long, String> sysNodeDtoMap = new HashMap<Long, String>();
            for (int i = 0; i < sysNodeDtoListParam.size(); i++) {
                sysNodeDtoMap.put(sysNodeDtoListParam.get(i).getId(), sysNodeDtoListParam.get(i).getNodeName());
            }
            for (int i = 0; i < fileDtoList.size(); i++) {
                Long uploadState = fileDtoList.get(i).getUploadState();
                fileDtoList.get(i).setUploadStateName(sysNodeDtoMap.get(uploadState));
            }
            fileUploadDto.setFileDtoList(fileDtoList);
            // 6、查询产品应上传文件目录
            ServiceResult<List<ProductFiletypeDto>> productFiletypeDtoListResult = productFileTypeService
                    .showProductFile(fileUploadDto.getProductName());
            // 7、查询文件上传阶段
            ServiceResult<List<SysNodeDto>> fileUpSysNodeListResult = nodeService
                    .getListByParent(NodeConstant.FILE_UP_STATE_PARENT_CODE);
            // 8、如果未传文件上传阶段，指定默认阶段。
            setDefaultFileUpState(fileUploadDto, fileUpSysNodeListResult.getData());
            // 9、查看模式传递信审岗位传递标识
            if (fileUploadDto.getIsView() != null && fileUploadDto.getIsView() == 0) {
                Boolean taskStateFlag = setTaskStateFlag(fileUploadDto);
                if (taskStateFlag) {
                    model.addAttribute("taskStateFlag", taskStateFlag);
                }
            }
            List<ProductFiletypeDto> productFiletypeDtoList = new ArrayList<ProductFiletypeDto>();
            productFiletypeDtoList.addAll(productFiletypeDtoListResult.getData());
            if(ClientType.APP.getCode().equals(result.getData().getClientType())){
                // 10、针对APP查询文件目录
                List<ProductFiletypeDto> appFileDir = this.queryAppFileDir();
                productFiletypeDtoList.addAll(appFileDir);
                // 11、针对APP查询文件信息
                String idCard = result.getData().getIdCard();
                ServiceResult<MemberInfoDto> memberInfoResult = memberInfoService.queryInfoByIdCard(idCard);
                List<FileDto> appfileDtoList = this.queryAppFileInfo(memberInfoResult.getData().getId());
                List<FileDto> fileDtoResult = fileUploadDto.getFileDtoList();
                fileDtoResult.addAll(appfileDtoList);
                fileUploadDto.setFileDtoList(fileDtoResult);
            }            
            model.addAttribute("fileUpSysNodeList", fileUpSysNodeListResult.getData());
            model.addAttribute("productFiletypeDtoList", productFiletypeDtoList);
            model.addAttribute("fileUploadDto", fileUploadDto);
            return "productFile/fileUpload";
        } catch (Exception e) {
            MyphLogger.error(e, "文件上传异常");
            return "error/500";
        }
    }

    /**
     * 
     * @名称 queryAppFileInfo 
     * @描述 针对APP查询文件信息
     * 由于APP文件目录通过sys_node管理，为避免与zd_product_filetype表中id相同造成解析混乱，查出sys_node中id做  * 1000处理
     * @返回类型 List<FileDto>     
     * @日期 2017年4月19日 下午2:32:00
     * @创建人  吴阳春
     * @更新人  吴阳春
     *
     */
    private List<FileDto> queryAppFileInfo(Long memberId) {
        List<FileDto> result = new ArrayList<FileDto>();
        ServiceResult<List<JkAppFileRelationDto>> jkAppFileRelationResult = jkAppFileRelationService.selectByMemberId(memberId);
        if(jkAppFileRelationResult.getData() == null){
            return result;
        }
        
        for(JkAppFileRelationDto jkAppFileRelationDto : jkAppFileRelationResult.getData()) {
            String fileStrs = jkAppFileRelationDto.getFileStrs();
            if(StringUtils.isBlank(fileStrs)){
                continue;
            }
            String[] fileStrsArray = fileStrs.split("\\|");
            List<String> fileStrsList = Arrays.asList(fileStrsArray);
            //根据大数据ID查文件信息
            ServiceResult<List<JkAppFileDto>> jkAppFileInfoResult = jkAppFileInfoService.selectByFileStrs(fileStrsList);
            for(JkAppFileDto jkAppFileDto : jkAppFileInfoResult.getData()){
                if(jkAppFileRelationDto.getUploadId() == null){
                    continue;
                }
                FileDto fileDto = new FileDto();
                BeanUtils.copyProperties(jkAppFileDto, fileDto);
                fileDto.setUploadState(ProductNodeEnum.APP.getCode());
                fileDto.setUploadId(jkAppFileRelationDto.getUploadId() * 1000);
                result.add(fileDto);
            }
        }
        return result;
    }

    /**
     * 
     * @名称 queryAppFileDir 
     * @描述   针对APP查询文件目录
     * 由于APP文件目录通过sys_node管理，为避免与zd_product_filetype表中id相同造成解析混乱，查出sys_node中id做  * 1000处理
     * @返回类型 List<ProductFiletypeDto>     
     * @日期 2017年4月19日 下午2:17:58
     * @创建人  吴阳春
     * @更新人  吴阳春
     *
     */
    private List<ProductFiletypeDto> queryAppFileDir() {
        List<ProductFiletypeDto> result = new ArrayList<ProductFiletypeDto>();
        ServiceResult<List<SysNodeDto>> sysNodeDtoResult = nodeService.getListByParent(NodeConstant.APP_UPLOAD_FILE_DIR);
        for(int i=0;i<sysNodeDtoResult.getData().size();i++){
            ProductFiletypeDto productFiletypeDto = new ProductFiletypeDto();
            productFiletypeDto.setId(sysNodeDtoResult.getData().get(i).getId() * 1000);
            productFiletypeDto.setFileUpState(ProductNodeEnum.APP.getCode());
            productFiletypeDto.setDirectoryName(sysNodeDtoResult.getData().get(i).getNodeName());
            result.add(productFiletypeDto);
        }
        return result;
    }

    private Boolean setTaskStateFlag(FileUploadDto fileUploadDto) {
        Boolean taskStateFlag = false;
        EmployeePositionInfoDto employeePositionInfoDto = (EmployeePositionInfoDto) ShiroUtils.getSession()
                .getAttribute("position");
        if (employeePositionInfoDto == null) {
            return taskStateFlag;
        }
        Long positionId = employeePositionInfoDto.getPositionId();
        ServiceResult<PositionDto> positionDtoResult = positionService.getEntityByPositionId(positionId);
        if (positionDtoResult.getData() != null) {
            String positionCode = positionDtoResult.getData().getPositionCode();
            ServiceResult<List<SysNodeDto>> positionAuditListResult = nodeService
                    .getListByParent(NodeConstant.POSITION_AUDIT_PARENT_CODE);
            if (positionAuditListResult.getData() == null
                    || CollectionUtils.isEmpty(positionAuditListResult.getData())) {
                return taskStateFlag;
            }
            for (SysNodeDto sysNode : positionAuditListResult.getData()) {
                String[] nodeCodeArray = sysNode.getNodeCode().split("\\|");
                String positionAuditCode = "";
                if (nodeCodeArray.length > 1) {
                    positionAuditCode = nodeCodeArray[1];
                } else {
                    positionAuditCode = nodeCodeArray[0];
                }
                if (positionCode.equals(positionAuditCode)) {
                    taskStateFlag = true;
                    break;
                }
            }
        }
        return taskStateFlag;
    }

    // 系统管理员可以选择所有阶段，默认选中申请单附件
    // 申请单阶段只能选择申请单附件
    // 初审外访、终审外访可选申请单附件，外访附件，默认选中外访附件
    // 初审、终审阶段只能选择信审附件
    // 签约阶段可选申请单附件、外访附件、合同附件，默认选中合同附件
    private void setDefaultFileUpState(FileUploadDto fileUploadDto, List<SysNodeDto> fileUpSysNodeList) {
        if (fileUploadDto.getFileUpState() != null) {
            return;
        }
        Map<String, Long> fileUpSysNodeMap = new HashMap<String, Long>();
        for (int i = 0; i < fileUpSysNodeList.size(); i++) {
            fileUpSysNodeMap.put(fileUpSysNodeList.get(i).getNodeCode(), fileUpSysNodeList.get(i).getId());
        }
        if (fileUploadDto.getIsManage() != null && fileUploadDto.getIsManage() == 1) {
            fileUploadDto.setFileUpState(fileUpSysNodeMap.get(FileUpSysNodeEnum.APPLYSTATE.getCode()));
        } else {
            if (fileUploadDto.getIsView() != null && fileUploadDto.getIsView().equals(0)) {
                fileUploadDto.setFileUpState(fileUpSysNodeMap.get(FileUpSysNodeEnum.APPLYSTATE.getCode()));
                ;
            }
            if (FlowStateEnum.APPLY.getCode().equals(fileUploadDto.getState())) {
                fileUploadDto.setFileUpState(fileUpSysNodeMap.get(FileUpSysNodeEnum.APPLYSTATE.getCode()));
            } else if (FlowStateEnum.AUDIT_FIRST.getCode().equals(fileUploadDto.getState())
                    || FlowStateEnum.AUDIT_LASTED.getCode().equals(fileUploadDto.getState())) {
                fileUploadDto.setFileUpState(fileUpSysNodeMap.get(FileUpSysNodeEnum.TASKSTATE.getCode()));
                if (fileUploadDto.getIsView() != null && fileUploadDto.getIsView() == 0) {
                    Boolean taskStateFlag = setTaskStateFlag(fileUploadDto);
                    if (!taskStateFlag) {
                        fileUploadDto.setFileUpState(fileUpSysNodeMap.get(FileUpSysNodeEnum.APPLYSTATE.getCode()));
                    }
                }
            } else if (FlowStateEnum.EXTERNAL_FIRST.getCode().equals(fileUploadDto.getState())
                    || FlowStateEnum.EXTERNAL_LAST.getCode().equals(fileUploadDto.getState())) {
                fileUploadDto.setFileUpState(fileUpSysNodeMap.get(FileUpSysNodeEnum.VISITSTATE.getCode()));
            } else if (FlowStateEnum.SIGN.getCode().equals(fileUploadDto.getState())) {
                fileUploadDto.setFileUpState(fileUpSysNodeMap.get(FileUpSysNodeEnum.SIGNSTATE.getCode()));
            }
        }

    }

    @RequestMapping("/upLoadFile")
    @ResponseBody
    public void upLoadFile(HttpServletRequest req, HttpServletResponse resp,
            @RequestParam("files[]") CommonsMultipartFile[] files) {
        try {
            for (int i = 0; i < files.length; i++) {
                // 校验文件后缀，不能为zip或rar
                String fileName = files[i].getOriginalFilename();
                String extName = "";
                if (fileName.lastIndexOf(".") >= 0) {
                    extName = fileName.substring(fileName.lastIndexOf(".") + 1);
                }
                if ("zip".equals(extName) || "rar".equals(extName)) {
                    MyphLogger.error("不可上传压缩包文件");
                    Long fileSize = 0l;
                    JSONArray ja = new JSONArray();
                    JSONObject json = new JSONObject();
                    json.put("name", fileName);
                    json.put("size", fileSize);
                    json.put("error", "不可上传压缩包文件");
                    ja.add(json);
                    JSONObject js = new JSONObject();
                    js.put("files", ja);
                    resp.getWriter().print(js.toString());
                    return;
                }
                // UUID作为key
                String rowKey = UUID.randomUUID().toString().replaceAll("-", "");
                byte[] bufferResult = IOUtils.toByteArray(files[i].getInputStream());
                Long fileSize = (long) bufferResult.length;
                // 将文件存入大数据
                HbaseUtils.put(rowKey, bufferResult);
                // 将文件与大数据关系存入申请附件关联表
                FileDto record = new FileDto();
                record.setApplyLoanNo(req.getParameter("applyLoanNo"));
                record.setFileName(fileName);
                record.setFileFormart(extName);
                record.setFileSize(fileSize);
                record.setFileStr(rowKey);
                record.setUploadState(Long.valueOf(req.getParameter("uploadState")));
                Long uploadType = Long.valueOf(req.getParameter("uploadType"));
                ServiceResult<ProductFiletypeDto> productFiletypeDtoResult = productFileTypeService
                        .selectByPrimaryKey(uploadType);
                record.setUploadType(productFiletypeDtoResult.getData().getDirectoryName());
                record.setUploadId(productFiletypeDtoResult.getData().getId());
                fileInfoService.insertSelective(record);
                String operatorName = ShiroUtils.getCurrentUserName();
                Long operatorId = ShiroUtils.getCurrentUserId();
                MyphLogger.info("文件上传成功,入参:{},{},{},{},当前操作人:{},操作人编号:{}", req.getParameter("applyLoanNo"), fileName,
                        rowKey, uploadType, operatorName, operatorId);
                /*
                 * 注：插件需要服务器端返回JSON格式的字符串，且必须以下面的格式来返回，一个字段都不能少 如果上传失败，那么则须用特定格式返回信息： "name": "picture1.jpg", "size":
                 * 902604, "error": "Filetype not allowed" 另外，files必须为一个JSON数组，哪怕上传的是一个文件
                 */
                JSONArray ja = new JSONArray();
                JSONObject json = new JSONObject();
                json.put("name", fileName);
                json.put("size", fileSize);
                json.put("url", "");
                json.put("thumbnailUrl", "");
                json.put("deleteUrl", "");
                json.put("deleteType", "DELETE");
                ja.add(json);
                JSONObject js = new JSONObject();
                js.put("files", ja);
                resp.getWriter().print(js.toString());
            }
        } catch (Exception e) {
            MyphLogger.error(e, "文件上传异常,入参:{}", req.getParameter("applyLoanNo"));
        }
    }

    /**
     * @名称 checkFile
     * @描述 用于校验当前阶段文件是否上传完全，false不完全，true完全
     * @返回类型 AjaxResult
     * @日期 2016年9月21日 下午3:53:56
     * @创建人 吴阳春
     * @更新人 吴阳春
     */
    @RequestMapping("/checkFile")
    @ResponseBody
    public AjaxResult checkFile(FileUploadDto fileUploadDto) {
        try {
            String applyLoanNo = fileUploadDto.getApplyLoanNo();
            if (StringUtils.isBlank(applyLoanNo)) {
                return AjaxResult.success(false);
            }
            // 查询进件表基本信息
            ServiceResult<FileManageApplyInfoDto> fileManageApplyInfoDtoResult = applyInfoService
                    .selectByApplyLoanNo(fileUploadDto.getApplyLoanNo());
            ServiceResult<ApplyReceptionDto> applyReceptionResult = applyReceptionService
                    .queryInfoByApplyLoanNo(fileUploadDto.getApplyLoanNo());
            Long productId = applyReceptionResult.getData().getProductId();
            // 补充主状态名称
            Integer state = fileManageApplyInfoDtoResult.getData().getState();
            if (state != null) {
                fileUploadDto.setState(state);
                fileUploadDto.setStateName(FlowStateEnum.getDescByCode(state));
            }
            // 查询文件上传阶段
            ServiceResult<List<SysNodeDto>> fileUpSysNodeListResult = nodeService
                    .getListByParent(NodeConstant.FILE_UP_STATE_PARENT_CODE);
            // 如果未传文件上传阶段，指定默认阶段。
            setDefaultFileUpState(fileUploadDto, fileUpSysNodeListResult.getData());
            // 根据产品id查询指定上传阶段必传文件目录
            Long fileUpState = fileUploadDto.getFileUpState();
            ServiceResult<List<ProductFiletypeDto>> productFiletypeDtoListResult = productFileTypeService
                    .showMustFile(productId, fileUpState);
            List<ProductFiletypeDto> productFiletypeDtoList = productFiletypeDtoListResult.getData();
            // 没有必传目录直接返回校验通过
            if (productFiletypeDtoList.isEmpty()) {
                return AjaxResult.success(true);
            }
            // 查询指定上传阶段的文件目录
            ServiceResult<List<Long>> uploadTypeListResult = fileInfoService
                    .selectFileInfo(fileUploadDto.getApplyLoanNo(), fileUpState);
            List<Long> uploadTypeList = uploadTypeListResult.getData();
            // 有必传目录且未查到文件直接返回校验不通过
            if (uploadTypeList.isEmpty()) {
                return AjaxResult.success(false);
            }
            for (int i = 0; i < productFiletypeDtoList.size(); i++) {
                if (!uploadTypeList.contains(productFiletypeDtoList.get(i).getId())) {
                    return AjaxResult.success(false);
                }
            }
            return AjaxResult.success(true);
        } catch (Exception e) {
            MyphLogger.error(e, "文件校验异常,入参:{}", fileUploadDto.toString());
            return AjaxResult.failed("文件校验异常");
        }
    }

    /**
     * @名称 showFileByProductIdAndState
     * @描述 根据产品id查询指定上传阶段文件目录
     * @返回类型 AjaxResult
     * @日期 2016年9月27日 下午3:45:04
     * @创建人 吴阳春
     * @更新人 吴阳春
     */
    @RequestMapping("/showFileByProductIdAndState")
    @ResponseBody
    public AjaxResult showFileByProductIdAndState(@RequestParam("productId") Long productId,
            @RequestParam("fileUpState") Long fileUpState) {
        try {
            ServiceResult<List<ProductFiletypeDto>> directoryNameListResult = productFileTypeService
                    .showFileByProductIdAndState(productId, fileUpState);
            return AjaxResult.success(directoryNameListResult.getData());
        } catch (Exception e) {
            MyphLogger.error(e, "根据产品id查询指定上传阶段文件目录信息异常,入参:{},{}", productId, fileUpState);
            return AjaxResult.failed("根据产品id查询指定上传阶段文件目录异常");
        }
    }

    /**
     * @名称 downLoadFile
     * @描述 文件下载
     * @返回类型 void
     * @日期 2016年9月25日 下午9:19:44
     * @创建人 吴阳春
     * @更新人 吴阳春
     * @see 只传单个ID的，设置打开或保存，传多个ID的，打包进行保存
     * @see 1、根据id从数据库中获取key值
     * @see 2、根据key值从大数据批量获取文件,单个文件直接返回页面进行保存，多个文件进行3步骤
     * @see 3、将文件保存为临时文件
     * @see 4、将临时文件压缩为zip
     * @see 5、将zip返回页面保存 6、删除临时目录及文件
     */
    @RequestMapping("/downLoadFile")
    @ResponseBody
    public void downLoadFile(HttpServletRequest req, HttpServletResponse resp,
            @RequestParam("fileIdListString") String fileIdListString) {
        try {
            List<Long> fileIdList = new ArrayList<Long>();
            String[] stringList = fileIdListString.split(",");
            for (int i = 0; i < stringList.length; i++) {
                fileIdList.add(Long.valueOf(stringList[i]));
            }
            if (fileIdList.size() == 1) {
                downOneFile(req, resp, fileIdList,ClientType.WEB.getCode());
            } else {
                downListFile(req, resp, fileIdList,ClientType.WEB.getCode());
            }
        } catch (Exception e) {
            MyphLogger.error(e, "下载文件异常,入参:{}", fileIdListString);
        }
    }

    @RequestMapping("/downLoadAppFile")
    @ResponseBody
    public void downLoadAppFile(HttpServletRequest req, HttpServletResponse resp,
            @RequestParam("fileIdListString") String fileIdListString) {
        try {
            List<Long> fileIdList = new ArrayList<Long>();
            String[] stringList = fileIdListString.split(",");
            for (int i = 0; i < stringList.length; i++) {
                fileIdList.add(Long.valueOf(stringList[i]));
            }
            if (fileIdList.size() == 1) {
                downOneFile(req, resp, fileIdList,ClientType.APP.getCode());
            } else {
                downListFile(req, resp, fileIdList,ClientType.APP.getCode());
            }
        } catch (Exception e) {
            MyphLogger.error(e, "下载文件异常,入参:{}", fileIdListString);
        }
    }
    
    @RequestMapping("/downOneFile")
    @ResponseBody
    public void downOneFile(HttpServletRequest req, HttpServletResponse resp,
            @RequestParam("fileIdListString") String fileIdListString) {
        OutputStream stream = null;
        try {
            byte[] data = HbaseUtils.getByBytes(fileIdListString);
            resp.reset();
            resp.setContentType("application/octet-stream; charset=utf-8");
            // 设置Content-Disposition
            resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("abc", "UTF-8"));
            stream = resp.getOutputStream();
            stream.write(data);
            stream.flush();
        } catch (Exception e) {
            MyphLogger.error(e, "下载文件异常,入参:{}", fileIdListString);
        } finally {
            if (null != stream) {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
    }

    /**
     * 
     * @名称 downOneFile 
     * @描述 下载单个文件 
     * clientType：图片来源0web,1app
     * @返回类型 void     
     * @日期 2017年4月20日 下午2:56:03
     * @创建人  吴阳春
     * @更新人  吴阳春
     *
     */
    private void downOneFile(HttpServletRequest req, HttpServletResponse resp, List<Long> fileIdList,Integer clientType) throws Exception {
        OutputStream stream = null;
        try {
            String fileStr = "";
            String fileName = "";
            if(ClientType.WEB.getCode().equals(clientType)){
                ServiceResult<FileDto> fileDtoResult = fileInfoService.selectByPrimaryKey(fileIdList.get(0));
                fileStr = fileDtoResult.getData().getFileStr();
                fileName = fileDtoResult.getData().getFileName();
            }else{
                ServiceResult<JkAppFileInfoDto> jkAppFileInfoDtoResult = jkAppFileInfoService.selectByPrimaryKey(fileIdList.get(0));
                fileStr = jkAppFileInfoDtoResult.getData().getFileStr();
                fileName = jkAppFileInfoDtoResult.getData().getFileName();
            }
            byte[] data = HbaseUtils.getByBytes(fileStr);
            resp.reset();
            resp.setContentType("application/octet-stream; charset=utf-8");
            // 设置Content-Disposition
            resp.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            stream = resp.getOutputStream();
            stream.write(data);
            stream.flush();
        } catch (Exception e) {
            MyphLogger.error(e, "下载文件异常,入参:{}", fileIdList);
        } finally {
            if (null != stream) {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 
     * @名称 downListFile 
     * @描述 下载多个文件
     * clientType：图片来源0web,1app
     * @返回类型 void     
     * @日期 2017年4月20日 下午2:58:24
     * @创建人  吴阳春
     * @更新人  吴阳春
     *
     */
    private void downListFile(HttpServletRequest req, HttpServletResponse resp, List<Long> fileIdList,Integer clientType) {
        OutputStream stream = null;
        FileInputStream fis = null;
        try {
            Long timeInMillis = Calendar.getInstance().getTimeInMillis();
            // 压缩文件默认文件名
            String downZIPFileName = timeInMillis + ".zip";
            // 下载文件临时目录
            String tempDirName = req.getSession().getServletContext().getRealPath("/") + "temp" + "/" + timeInMillis;
            // 压缩文件路径+文件名
            String zipFile = tempDirName + "/" + downZIPFileName;

            File downDir = new File(tempDirName);
            if (!downDir.exists() && !downDir.isDirectory()) {
                downDir.mkdirs();
            }
            Map<String, Integer> fileNameMap = new HashMap<String, Integer>();

            for (int i = 0; i < fileIdList.size(); i++) {
                String fileStr = "";
                String fileName = "";
                if(ClientType.WEB.getCode().equals(clientType)){
                    ServiceResult<FileDto> fileDtoResult = fileInfoService.selectByPrimaryKey(fileIdList.get(i));
                    fileStr = fileDtoResult.getData().getFileStr();
                    fileName = fileDtoResult.getData().getFileName();
                }else{
                    ServiceResult<JkAppFileInfoDto> jkAppFileInfoDtoResult = jkAppFileInfoService.selectByPrimaryKey(fileIdList.get(i));
                    fileStr = jkAppFileInfoDtoResult.getData().getFileStr();
                    fileName = jkAppFileInfoDtoResult.getData().getFileName();
                }
                byte[] data = HbaseUtils.getByBytes(fileStr);
                // 防重名处理
                String prefix = fileName.substring(0, fileName.lastIndexOf("."));
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                fileNameMap.put(prefix, fileNameMap.containsKey(prefix) ? fileNameMap.get(prefix) + 1 : 0);
                if (!fileNameMap.get(prefix).equals(0)) {
                    fileName = prefix + fileNameMap.get(prefix) + "." + suffix;
                }
                FileCopyUtils.copy(data, new File(tempDirName + "/" + fileName));
            }

            ZipCompress.zip(new File(tempDirName), zipFile);
            resp.reset();
            resp.setContentType("application/zip");
            resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downZIPFileName, "UTF-8"));
            stream = resp.getOutputStream();
            fis = new FileInputStream(zipFile);
            IOUtils.copy(fis, stream);
            stream.flush();
            FileUtils.deleteQuietly(new File(tempDirName));
        } catch (Exception e) {
            MyphLogger.error(e, "下载文件异常,入参:{}", fileIdList);
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != stream) {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @RequestMapping("/getFile")
    @ResponseBody
    public AjaxResult getFile(@RequestParam("fileIdListString") Long fileIdListString, HttpServletRequest req,
            HttpServletResponse response) {
        try {
            StringBuffer requestURL = req.getRequestURL();
            requestURL.delete(requestURL.length() - 12, requestURL.length());
            requestURL.insert(requestURL.length(), "/loadFile.htm?id=" + fileIdListString);
            return AjaxResult.success(requestURL);
        } catch (Exception e) {
            MyphLogger.error(e, "下载文件异常,入参:{}", fileIdListString);
            return AjaxResult.failed("下载文件异常");
        }
    }

    @RequestMapping("/loadFile")
    public void loadFile(@RequestParam("id") Long fileIdListString, HttpServletRequest req,
            HttpServletResponse response) {
        byte[] data = null;
        OutputStream stream = null;
        try {
            ServiceResult<FileDto> fileDtoResult = fileInfoService.selectByPrimaryKey(fileIdListString);
            data = HbaseUtils.getByBytes(fileDtoResult.getData().getFileStr());
            stream = response.getOutputStream();
            stream.write(data);
            stream.flush();
        } catch (Exception e) {
            MyphLogger.error(e, "下载文件异常,入参:{}", fileIdListString);
        } finally {
            if (null != stream) {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @名称 delFile
     * @描述 删除文件关联信息
     * @返回类型 AjaxResult
     * @日期 2016年9月26日 上午1:22:41
     * @创建人 吴阳春
     * @更新人 吴阳春
     */
    @RequestMapping("/delFile")
    @ResponseBody
    public AjaxResult delFile(@RequestParam("applyLoanNo") String applyLoanNo,
            @RequestParam("fileIdListString") String fileIdListString) {
        try {
            List<Long> fileIdList = new ArrayList<Long>();
            String[] stringList = fileIdListString.split(",");
            for (int i = 0; i < stringList.length; i++) {
                fileIdList.add(Long.valueOf(stringList[i]));
            }
            fileInfoService.deleteByFileIdList(fileIdList);
            String operatorName = ShiroUtils.getCurrentUserName();
            Long operatorId = ShiroUtils.getCurrentUserId();
            MyphLogger.info("文件删除成功,入参:{},{},当前操作人:{},操作人编号:{}", applyLoanNo, fileIdListString, operatorName,
                    operatorId);
            return AjaxResult.success();
        } catch (Exception e) {
            MyphLogger.error(e, "删除文件关联信息异常,入参:{}", fileIdListString);
            return AjaxResult.failed("删除文件关联信息异常");
        }
    }
    
    @RequestMapping("/getAppFile")
    @ResponseBody
    public AjaxResult getAppFile(@RequestParam("fileIdListString") Long id, HttpServletRequest req,
            HttpServletResponse response) {
        try {
            StringBuffer requestURL = req.getRequestURL();
            requestURL.delete(requestURL.length() - 15, requestURL.length());
            requestURL.insert(requestURL.length(), "/loadAppFile.htm?id=" + id);
            return AjaxResult.success(requestURL);
        } catch (Exception e) {
            MyphLogger.error(e, "下载文件异常,入参:{}", id);
            return AjaxResult.failed("下载文件异常");
        }
    }

    @RequestMapping("/loadAppFile")
    public void loadAppFile(@RequestParam("id") Long id, HttpServletRequest req,
            HttpServletResponse response) {
        byte[] data = null;
        OutputStream stream = null;
        try {
            ServiceResult<JkAppFileInfoDto> fileDtoResult = jkAppFileInfoService.selectByPrimaryKey(id);
            data = HbaseUtils.getByBytes(fileDtoResult.getData().getFileStr());
            stream = response.getOutputStream();
            stream.write(data);
            stream.flush();
        } catch (Exception e) {
            MyphLogger.error(e, "下载文件异常,入参:{}", id);
        } finally {
            if (null != stream) {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 
     * @名称 fileUploadForRepay 
     * @描述 代扣文件查看、上传
     * @返回类型 String     
     * @日期 2017年8月23日 下午7:46:25
     * @创建人  吴阳春
     * @更新人  吴阳春
     * 代扣等相关附件与账单编号关联
     */
    @RequestMapping("/fileUploadForRepay")
    public String fileUploadForRepay(Model model, FileUploadDto fileUploadDto) {
        try {
            // 查询文件基本信息
            ServiceResult<List<FileDto>> fileDtoListResult = fileInfoService
                    .selectByApplyLoanNo(fileUploadDto.getBillNo());
            List<FileDto> fileDtoList = fileDtoListResult.getData();
            fileUploadDto.setFileDtoList(fileDtoList);
            model.addAttribute("fileUploadDto", fileUploadDto);
            return "productFile/fileUploadForRepay";
        } catch (Exception e) {
            MyphLogger.error(e, "文件上传异常");
            return "error/500";
        }
    }
    
    
    @RequestMapping("/upLoadFileForRepay")
    @ResponseBody
    public void upLoadFileForRepay(HttpServletRequest req, HttpServletResponse resp,
            @RequestParam("files[]") CommonsMultipartFile[] files) {
        try {
            for (int i = 0; i < files.length; i++) {
                // 校验文件后缀，不能为zip或rar
                String fileName = files[i].getOriginalFilename();
                String extName = "";
                if (fileName.lastIndexOf(".") >= 0) {
                    extName = fileName.substring(fileName.lastIndexOf(".") + 1);
                }
                if ("zip".equals(extName) || "rar".equals(extName)) {
                    MyphLogger.error("不可上传压缩包文件");
                    Long fileSize = 0l;
                    JSONArray ja = new JSONArray();
                    JSONObject json = new JSONObject();
                    json.put("name", fileName);
                    json.put("size", fileSize);
                    json.put("error", "不可上传压缩包文件");
                    ja.add(json);
                    JSONObject js = new JSONObject();
                    js.put("files", ja);
                    resp.getWriter().print(js.toString());
                    return;
                }
                // UUID作为key
                String rowKey = UUID.randomUUID().toString().replaceAll("-", "");
                byte[] bufferResult = IOUtils.toByteArray(files[i].getInputStream());
                Long fileSize = (long) bufferResult.length;
                // 将文件存入大数据
                HbaseUtils.put(rowKey, bufferResult);
                // 将文件与大数据关系存入申请附件关联表
                FileDto record = new FileDto();
                record.setApplyLoanNo(req.getParameter("billNo"));
                record.setFileName(fileName);
                record.setFileFormart(extName);
                record.setFileSize(fileSize);
                record.setFileStr(rowKey);
                record.setUploadState(0l);
                Integer uploadType = Integer.valueOf(req.getParameter("uploadType"));
                record.setUploadType(RepayUpLoadFileTypeEnum.getDescByCode(uploadType));
                record.setUploadId(0l);
                fileInfoService.insertSelective(record);
                String operatorName = ShiroUtils.getCurrentUserName();
                Long operatorId = ShiroUtils.getCurrentUserId();
                MyphLogger.info("文件上传成功,入参:{},{},{},{},当前操作人:{},操作人编号:{}", req.getParameter("applyLoanNo"), fileName,
                        rowKey, uploadType, operatorName, operatorId);
                /*
                 * 注：插件需要服务器端返回JSON格式的字符串，且必须以下面的格式来返回，一个字段都不能少 如果上传失败，那么则须用特定格式返回信息： "name": "picture1.jpg", "size":
                 * 902604, "error": "Filetype not allowed" 另外，files必须为一个JSON数组，哪怕上传的是一个文件
                 */
                JSONArray ja = new JSONArray();
                JSONObject json = new JSONObject();
                json.put("name", fileName);
                json.put("size", fileSize);
                json.put("url", "");
                json.put("thumbnailUrl", "");
                json.put("deleteUrl", "");
                json.put("deleteType", "DELETE");
                ja.add(json);
                JSONObject js = new JSONObject();
                js.put("files", ja);
                resp.getWriter().print(js.toString());
            }
        } catch (Exception e) {
            MyphLogger.error(e, "文件上传异常,入参:{}", req.getParameter("applyLoanNo"));
        }
    }
    

    @RequestMapping("/checkFileForRepay")
    @ResponseBody
    public AjaxResult checkFileForRepay(String billNo,String uploadType) {
        try {
            ServiceResult<Integer> result = fileInfoService.selectCountByBillNoAndUploadType(billNo,uploadType);
            if(result.getData() > 0){
                return AjaxResult.success(true);
            }
            return AjaxResult.success(false);
        } catch (Exception e) {
            MyphLogger.error(e, "文件校验异常,入参:{},{}", billNo,uploadType);
            return AjaxResult.failed("文件校验异常");
        }
    }

}
