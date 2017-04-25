<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/dataDetailRelation/dataDetailRelation.js?v=${VERSION_NO}"></script>
<script type="text/javascript">
    var serverPath = "${serverPath}";
</script>

<style>
    .required{color:#e02222;}
</style>

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
                        <a href="#">APP后台管理</a><i class="icon-angle-right"></i>
                    </li>
                    <li>
                        <a href="#"><#if dto??>APP后台管理<#else>资料关系配置</#if></a>
                    </li>
                </ul>
            </div>
        </div>
    <div>
    
    <div id="selectShow>
    	<div class="portlet-body">
    		<div class="row-fluid">
        		<p>
        		  <a class="btn blue" href="#addDataDetailRelation" onclick="add()" data-toggle="modal">新增关系</a>
        		</p>
    		</div>
    	</div>
	
    	<!--显示查询结果-->
    	<div class="tabbable tabbable-custom tabbable-custom-profile">
    		<!-- table -->
    		<table class="table table-bordered table-hover table-condensed" id="selectProductFile">
    			<thead>
    				<tr>
    				    <th>资料项名称</th>
    					<th>资料信息</th>
    					<th>操作</th>
    				</tr>
    			</thead>
    			<tbody>
    			    <#list result as record>
    			        <tr>
    			            <td id='${(record.id)!}pageName'>${(record.pageName)!}</td>
    			            <td id='${(record.id)!}infoName'>${(record.infoNames)!}</td>
    			            <td><a href="#updateDataDetailRelation" data-toggle="modal" onclick="update(${(record.id)!})">修改</a></td>
    			        </tr>
    			    </#list>
    			</tbody>
    		</table>
    		
    		<div id="addDataDetailRelation" class="modal hide fade" tabindex="-1" data-width="760">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h3>新建资料关系配置</h3>
                </div>
                <div class="modal-body">
                    <div class="row-fluid">
                        <span class="control-label span3">资料项名称<span class="required">*</span></span>
                        <input type="text" name="addPageName" id="addPageName" class="form-control span6" />
                    </div>
                    <div class="row-fluid">
                        <span class="control-label span3">资料项编码<span class="required">*</span></span>
                        <input type="text" name="addPageCode" id="addPageCode" class="form-control span6" />
                    </div>
                    <div class="row-fluid">
                        <span class="control-label span3" style="margin-right: -13px">资料信息<span class="required">*</span></span>
                        <select name="addInfoNames" id="addInfoNames" class="form-control span6" multiple="multiple">
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <a class="btn blue" data-dismiss="modal" onclick="returnBack();" class="btn">返回</a>
                    <button type="button" class="btn blue" onclick="addDataDetailRelation();">提交</button>      
                </div>
            </div>
            
            <input type="hidden" id="id" name="id"></input>
            <div id="updateDataDetailRelation" class="modal hide fade" tabindex="-1" data-width="760">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h3>更新资料关系配置</h3>
                </div>
                <div class="modal-body">
                    <div class="row-fluid">
                        <span class="control-label span3">资料项名称<span class="required">*</span></span>
                        <input type="text" name="updatePageName" id="updatePageName" class="form-control span6" />
                    </div>
                    <div class="row-fluid">
                        <span class="control-label span3" style="margin-right: -13px">资料信息<span class="required">*</span></span>
                        <select name="updateInfoNames" id="updateInfoNames" class="form-control span6" multiple="multiple">
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <a class="btn blue" data-dismiss="modal" onclick="returnBack();" class="btn">返回</a>
                    <button type="button" class="btn blue" onclick="updateDataDetailRelation();">提交</button>      
                </div>
            </div>
            
    	</div>
	</div>
</div>
<#include "/sys/bottom.ftl">