<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<link href="${cdnPath}/css/smartMenu/smartMenu.css" rel="stylesheet" type="text/css"/>
<link href="${cdnPath}/css/viewer/viewer.css" rel="stylesheet" type="text/css"/>
<link href="${cdnPath}/common/bootstrap/media/css/jquery.fileupload-ui.css" rel="stylesheet" type="text/css"/>
<link href="${cdnPath}/common/bootstrap/media/css/jquery.fancybox.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
    var serverPath = "${serverPath}";
    var fileUploadDto = {fileUpState:"${(fileUploadDto.fileUpState)!''}",isView:"${(fileUploadDto.isView)!'1'}"};
</script>

<div class="page-content" >
    <!-- header -->
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span12">
                <!--页面标题-->
                <h3 class="page-title"></h3>
                <!--面包屑导航-->
                <ul class="breadcrumb">
                    <li> <i class="icon-home"></i>
                        <a href="${serverPath}/index.htm">首页</a> <i class="icon-angle-right"></i>
                    </li>
                    <li>
                        <a href="#">系统管理</a><i class="icon-angle-right"></i>
                    </li>
                    <li>
                        <a href="#"><#if dto??>系统管理<#else>附件管理</#if></a>
                    </li>
                </ul>
            </div>
        </div>
    <div>
    
	<!--FirstTable-->
	<div class="portlet-body">
		<!-- table -->    
		<style>
		.viewer-canvas{ no-repeat center center;}
		</style> 
		<table class="table table-hover">
			  <tr><td>
		         <span class="control-label">账单编号：${fileUploadDto.billNo}</span>
		         <input type="hidden" name="applyLoanNo" id="applyLoanNo" value='${fileUploadDto.billNo}' />
                 <span class="control-label">&nbsp;&nbsp;附件阶段：还款阶段</span>
			  </td></tr>
			  <tr><td>
			     <div class="control-group controls span3 " style="margin-right:45px;">
			         <label>
                        <div class="control-label">
                            <span><input type="checkbox" id="checkAll" name="checkAll" class="checkbox" value=""></span>
                            <span class="control-label">全选&nbsp;&nbsp;&nbsp;已选择</span><span class="control-label" id="checkedNum">0</span><span class="control-label">个文件</span>
                        </div>                                                                  
                     </label>
                     
			     </div>
			     
			     <a class="btn blue <#if fileUploadDto.isView?? && (fileUploadDto.isView==0)>hide</#if>" href="#fileuploadShow" style="margin-right:30px;" data-toggle="modal">上传</a>
			     <input type="button" class="btn blue" value="下载" style="margin-right:30px;" onclick="return down()"></input>
			     <input type="button" class="btn blue <#if fileUploadDto.isView?? && (fileUploadDto.isView==0)>hide</#if>" value="删除" style="margin-right:30px;" onclick="return del()"></input>
              </td></tr>

                <tr><td>
                    <#assign haveFile = 0>
                    <p>代扣：</p>
                    <div class="div">
                    <#if (fileUploadDto.fileDtoList??) && (fileUploadDto.fileDtoList?size>0)>  
                        <#list fileUploadDto.fileDtoList as fileDto>
                        <#if fileDto.uploadType?? && fileDto.uploadType == '代扣'>
                            <#assign haveFile = 1>
                            <div style="float:left; width:178px; height:180px" class="checkboxParent" id=${fileDto.id}>
                                <img  src="${cdnPath}/images/productFile/fileImg.png" alt="默认图片" class="img" style=" width:103px; height:103px"/>
                                <div class='titleDiv'>
                                    <span class="span2 " style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="${fileDto.fileName}">
                                        <input class="checkbox checkboxFile" type="checkbox" name="checkboxFile" value="">
                                    &nbsp;${fileDto.fileName}</span>
                                </div>
                                <div>
                                    <p class="control-label">${fileDto.createTime?string('yyyy-MM-dd HH:mm:ss')}</p>
                                </div>
                            </div>
                        </#if>
                        </#list>
                    </#if> 
                    </div> 
                    <#if haveFile == 0>
                        <div>
                            <span>尚未上传相关附件！</span>
                            <img src="${cdnPath}/images/productFile/UpFilefail.png" height="103px" weight="103px" alt="尚未上传相关附件！" />
                        </div>
                    </#if>                             
                </td></tr>
                <tr><td>
                    <#assign haveFile = 0>
                    <p>代偿：</p>
                    <div class="div">
                    <#if (fileUploadDto.fileDtoList??) && (fileUploadDto.fileDtoList?size>0)>  
                        <#list fileUploadDto.fileDtoList as fileDto>
                        <#if fileDto.uploadType?? && fileDto.uploadType == '代偿'>
                            <#assign haveFile = 1>
                            <div style="float:left; width:178px; height:180px" class="checkboxParent" id=${fileDto.id}>
                                <img  src="${cdnPath}/images/productFile/fileImg.png" alt="默认图片" class="img" style=" width:103px; height:103px"/>
                                <div class='titleDiv'>
                                    <span class="span2 " style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="${fileDto.fileName}">
                                        <input class="checkbox checkboxFile" type="checkbox" name="checkboxFile" value="">
                                    &nbsp;${fileDto.fileName}</span>
                                </div>
                                <div>
                                    <p class="control-label">${fileDto.createTime?string('yyyy-MM-dd HH:mm:ss')}</p>
                                </div>
                            </div>
                        </#if>
                        </#list>
                    </#if> 
                    </div> 
                    <#if haveFile == 0>
                        <div>
                            <span>尚未上传相关附件！</span>
                            <img src="${cdnPath}/images/productFile/UpFilefail.png" height="103px" weight="103px" alt="尚未上传相关附件！" />
                        </div>
                    </#if>                             
                </td></tr>
                <tr><td>
                    <#assign haveFile = 0>
                    <p>提前结清扣款：</p>
                    <div class="div">
                    <#if (fileUploadDto.fileDtoList??) && (fileUploadDto.fileDtoList?size>0)>  
                        <#list fileUploadDto.fileDtoList as fileDto>
                        <#if fileDto.uploadType?? && fileDto.uploadType == '提前结清扣款'>
                            <#assign haveFile = 1>
                            <div style="float:left; width:178px; height:180px" class="checkboxParent" id=${fileDto.id}>
                                <img  src="${cdnPath}/images/productFile/fileImg.png" alt="默认图片" class="img" style=" width:103px; height:103px"/>
                                <div class='titleDiv'>
                                    <span class="span2 " style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="${fileDto.fileName}">
                                        <input class="checkbox checkboxFile" type="checkbox" name="checkboxFile" value="">
                                    &nbsp;${fileDto.fileName}</span>
                                </div>
                                <div>
                                    <p class="control-label">${fileDto.createTime?string('yyyy-MM-dd HH:mm:ss')}</p>
                                </div>
                            </div>
                        </#if>
                        </#list>
                    </#if> 
                    </div> 
                    <#if haveFile == 0>
                        <div>
                            <span>尚未上传相关附件！</span>
                            <img src="${cdnPath}/images/productFile/UpFilefail.png" height="103px" weight="103px" alt="尚未上传相关附件！" />
                        </div>
                    </#if>                             
                </td></tr>   
                <tr><td>
                    <#assign haveFile = 0>
                    <p>减免：</p>
                    <div class="div">
                    <#if (fileUploadDto.fileDtoList??) && (fileUploadDto.fileDtoList?size>0)>  
                        <#list fileUploadDto.fileDtoList as fileDto>
                        <#if fileDto.uploadType?? && fileDto.uploadType == '减免'>
                            <#assign haveFile = 1>
                            <div style="float:left; width:178px; height:180px" class="checkboxParent" id=${fileDto.id}>
                                <img  src="${cdnPath}/images/productFile/fileImg.png" alt="默认图片" class="img" style=" width:103px; height:103px"/>
                                <div class='titleDiv'>
                                    <span class="span2 " style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="${fileDto.fileName}">
                                        <input class="checkbox checkboxFile" type="checkbox" name="checkboxFile" value="">
                                    &nbsp;${fileDto.fileName}</span>
                                </div>
                                <div>
                                    <p class="control-label">${fileDto.createTime?string('yyyy-MM-dd HH:mm:ss')}</p>
                                </div>
                            </div>
                        </#if>
                        </#list>
                    </#if> 
                    </div> 
                    <#if haveFile == 0>
                        <div>
                            <span>尚未上传相关附件！</span>
                            <img src="${cdnPath}/images/productFile/UpFilefail.png" height="103px" weight="103px" alt="尚未上传相关附件！" />
                        </div>
                    </#if>                             
                </td></tr>  
                <tr><td>
                    <#assign haveFile = 0>
                    <p>对公还款：</p>
                    <div class="div">
                    <#if (fileUploadDto.fileDtoList??) && (fileUploadDto.fileDtoList?size>0)>  
                        <#list fileUploadDto.fileDtoList as fileDto>
                        <#if fileDto.uploadType?? && fileDto.uploadType == '对公还款'>
                            <#assign haveFile = 1>
                            <div style="float:left; width:178px; height:180px" class="checkboxParent" id=${fileDto.id}>
                                <img  src="${cdnPath}/images/productFile/fileImg.png" alt="默认图片" class="img" style=" width:103px; height:103px"/>
                                <div class='titleDiv'>
                                    <span class="span2 " style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="${fileDto.fileName}">
                                        <input class="checkbox checkboxFile" type="checkbox" name="checkboxFile" value="">
                                    &nbsp;${fileDto.fileName}</span>
                                </div>
                                <div>
                                    <p class="control-label">${fileDto.createTime?string('yyyy-MM-dd HH:mm:ss')}</p>
                                </div>
                            </div>
                        </#if>
                        </#list>
                    </#if> 
                    </div> 
                    <#if haveFile == 0>
                        <div>
                            <span>尚未上传相关附件！</span>
                            <img src="${cdnPath}/images/productFile/UpFilefail.png" height="103px" weight="103px" alt="尚未上传相关附件！" />
                        </div>
                    </#if>                             
                </td></tr>                                            
              <tr><td>
                <span style="color:#F00">注:不可上传压缩包文件，单个附件大小不得超过10M。</span>
              </td></tr>
		</table>   
	</div>
</div>

<input type="hidden" id="smartMenuId" name="smartMenuId"></input>

<div id="fileuploadShow" class="modal hide fade" style="width:1200px;height:700px;overflow-y: scroll;margin-left:-600px" tabindex="-1" aria-hidden="true" >    
    <form id="fileupload" action="${serverPath}/productFile/upLoadFileForRepay.htm" method="POST" enctype="multipart/form-data">
        <input type="hidden" id="billNo" name="billNo" value="${fileUploadDto.billNo}"></input>
        <noscript><input type="hidden" name="redirect" value="${serverPath}/productFile/upLoadFileForRepay.htm"></noscript>
        <div class="row-fluid fileupload-buttonbar">
            <table class="table table-hover">
                <tr><td>
                    <div class="modal-header">
                    <button class="close" aria-hidden="true" data-dismiss="modal" type="button"></button>
                    <h3 class="form-section" style="margin-right:50px;display:inline">上传附件</h3>
                    <span class="control-label"style="margin-right:10px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;目录</span>
                    <select class="m-wrap span3" id="uploadType" name="uploadType">
                        <option value =1>代扣</option>
                        <option value =2>代偿</option>
                        <option value =3>提前结清扣款</option>
                        <option value =4>减免</option>
                        <option value =5>对公还款</option>
                    </select>
                    </div>
                </td></tr>
            </table>
            <div class="span7">
                <span class="btn green fileinput-button">
                
                <span>添加附件</span>
                    <input type="file" name="files[]" multiple>
                </span>
    
                <button type="submit" class="btn blue start" style="float:left;margin-right:5px">
                    <span>开始上传</span>
                </button>
    
                <button type="button" class="btn red delete" style="float:left;margin-right:5px">
                    <span>清空列表</span>
                </button>
                
            </div>
    
            <div class="span5 fileupload-progress fade">
                <!-- The global progress bar -->
                <div class="progress progress-success progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
                    <div class="bar" style="width:0%;"></div>
                </div>
                <div class="progress-extended">&nbsp;</div>
            </div>
        </div>
    
        <div class="fileupload-loading"></div><br>
        <table role="presentation" class="table table-striped">
            <tbody class="files" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
        </table>
    </form>
    <br>
    <div class="row-fluid">
        <div class="span12">
            <script id="template-upload" type="text/x-tmpl">
                {% for (var i=0, file; file=o.files[i]; i++) { %}
                    <tr class="template-upload fade">
                        <td class="preview"><span class=""></span></td>
                        <td class="name"><span>{%=file.name%}</span></td>
                        <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
                        {% if (file.error) { %}
                            <td class="error" colspan="2"><span class="label label-important">Error</span> {%=file.error%}</td>
                        {% } else if (o.files.valid && !i) { %}
                            <td>
                                <div class="progress progress-success progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="bar" style="width:0%;"></div></div>
                            </td>
                            <td class="start">{% if (!o.options.autoUpload) { %}
                                <button class="btn">
                                    <span>上传</span>
                                </button>
                            {% } %}</td>
                        {% } else { %}
                            <td colspan="2"></td>
                        {% } %}
                        <td class="cancel">{% if (!i) { %}
                            <button class="btn red">
                                <span>删除</span>
                            </button>
                        {% } %}</td>
                    </tr>
                {% } %}
            </script>
            <script id="template-download" type="text/x-tmpl">
                {% for (var i=0, file; file=o.files[i]; i++) { %}
                    <tr class="template-download fade">
                        {% if (file.error) { %}
                            <td></td>
                            <td class="name"><span>{%=file.name%}</span></td>
                            <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
                            <td class="error" colspan="2"><span class="label label-important">Error</span> {%=file.error%}</td>
                        {% } else { %}
                            <td class="preview">
                            {% if (file.thumbnail_url) { %}
                                <a class="fancybox-button" data-rel="fancybox-button" href="{%=file.url%}" title="{%=file.name%}">
                                <img src="media/image/{%=file.thumbnail_url%}">
                                </a>
                            {% } %}</td>
                            <td class="name">
                                <a href="{%=file.url%}" title="{%=file.name%}" data-gallery="{%=file.thumbnail_url&&'gallery'%}" download="{%=file.name%}">{%=file.name%}</a>
                            </td>
                            <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
                            <td colspan="2"></td>
                        {% } %}
                    </tr>
                {% } %}
            </script>
        </div>
    </div>
</div> 

<script src="${cdnPath}/js/productFile/fileUploadForRepay.js?v=${VERSION_NO}"></script>
<script src="${cdnPath}/js/smartMenu/jquery-smartMenu-min.js"></script>
<script src="${cdnPath}/common/bootstrap/media/js/jquery.ui.widget.js"></script>
<script src="${cdnPath}/common/bootstrap/media/js/tmpl.min.js"></script>
<script src="${cdnPath}/common/bootstrap/media/js/load-image.min.js"></script>
<script src="${cdnPath}/common/bootstrap/media/js/canvas-to-blob.min.js"></script>
<script src="${cdnPath}/js/productFile/jquery.blueimp-gallery.min.js"></script>
<script src="${cdnPath}/common/bootstrap/media/js/jquery.iframe-transport.js"></script>
<script src="${cdnPath}/common/bootstrap/media/js/jquery.fileupload.js"></script>
<script src="${cdnPath}/common/bootstrap/media/js/jquery.fileupload-fp.js"></script>
<script src="${cdnPath}/js/productFile/fileupload-validate.js"></script>
<script src="${cdnPath}/common/bootstrap/media/js/jquery.fileupload-ui.js"></script>
<script src="${cdnPath}/js/productFile/viewer.js"></script>
<#include "/sys/bottom.ftl">