<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/dataDetail/dataDetail.js?v=${VERSION_NO}"></script>
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
                        <a href="#"><#if dto??>APP后台管理<#else>资料管理配置</#if></a>
                    </li>
                </ul>
            </div>
        </div>
    <div>
    
    <div id="selectShow>
    	<div class="portlet-body">
    		<div class="row-fluid">
        		<p>
        		  <a class="btn blue" href="#addDataDetail" onclick='cleanAdd()' data-toggle="modal">新增</a>
        		</p>
    		</div>
    	</div>
	
    	<!--显示查询结果-->
    	<div class="tabbable tabbable-custom tabbable-custom-profile">
    		<!-- table -->
    		<table class="table table-bordered table-hover table-condensed" id="selectProductFile">
    			<thead>
    				<tr>
    				    <th>位号</th>
    					<th>编号</th>
    					<th>名称</th>
    					<th>状态</th>
    					<th>操作</th>
    				</tr>
    			</thead>
    			<tbody>
    			    <#list result as record>
    			        <tr>
    			            <td id='${(record.id)!}num'>${(record.num)!}</td>
    			            <td id='${(record.id)!}code'>${(record.code)!}</td>
    			            <td id='${(record.id)!}name'>${(record.name)!}</td>
    			            <td id='${(record.id)!}isMust'><#if record.isMust == 0>非必填<#else>必填</#if></td>
    			            <td><a href="#updateDataDetail" data-toggle="modal" onclick="update(${(record.id)!})">修改</a></td>
    			        </tr>
    			    </#list>
    			</tbody>
    		</table>
    		
    		<div id="addDataDetail" class="modal hide fade" tabindex="-1" data-width="760">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h3>新建资料管理配置</h3>
                </div>
                <div class="modal-body">
                    <div class="row-fluid">
                        <span class="control-label span3">位号<span class="required">*</span></span>
                        <input type="text" name="addNum" id="addNum" class="form-control span6" />
                    </div>
                    <div class="row-fluid">
                        <span class="control-label span3">编号<span class="required">*</span></span>
                        <input type="text" name="addCode" id="addCode" class="form-control span6" />
                    </div>
                    <div class="row-fluid">
                        <span class="control-label span3">名称<span class="required">*</span></span>
                        <input type="text" name="addName" id="addName" class="form-control span6" />
                    </div>
                    <div class="row-fluid">
                        <div class="control-group span11 ">
                            <span class="control-label span3">状态<span class="required">*</span></span>
                            <label class="radio span" style="width:100px;">
                                <input type="radio" checked="checked" value = 1 name="addIsMust" />必填
                            </label>
                            <label class="radio span" style="width:100px;">
                                <input type="radio" value = 0 name="addIsMust" />非必填
                            </label>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <span class="control-label span3" id="error" ></span>
                    </div>
                </div>
                <div class="modal-footer">
                    <a class="btn blue" data-dismiss="modal" onclick="returnBack();" class="btn">返回</a>
                    <button type="button" class="btn blue" onclick="addDataDetail();">提交</button>      
                </div>
            </div>
            
            <input type="hidden" id="id" name="id"></input>
            <div id="updateDataDetail" class="modal hide fade" tabindex="-1" data-width="760">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h3>更新资料管理配置</h3>
                </div>
                <div class="modal-body">
                    <div class="row-fluid">
                        <span class="control-label span3">位号<span class="required">*</span></span>
                        <input type="text" name="updateNum" id="updateNum" class="form-control span6" readonly />
                    </div>
                    <div class="row-fluid">
                        <span class="control-label span3">编号<span class="required">*</span></span>
                        <input type="text" name="updateCode" id="updateCode" class="form-control span6" readonly />
                    </div>
                    <div class="row-fluid">
                        <span class="control-label span3">名称<span class="required">*</span></span>
                        <input type="text" name="updateName" id="updateName" class="form-control span6" />
                    </div>
                    <div class="row-fluid">
                        <div class="control-group span11 ">
                            <span class="control-label span3">状态<span class="required">*</span></span>
                            <label class="radio span" style="width:100px;">
                                <input type="radio" value = 1 name="updateIsMust" />必填
                            </label>
                            <label class="radio span" style="width:100px;">
                                <input type="radio" value = 0 name="updateIsMust" />非必填
                            </label>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <span class="control-label span3" id="errorUpdate" ></span>
                    </div>
                </div>
                <div class="modal-footer">
                    <a class="btn blue" data-dismiss="modal" onclick="returnBack();" class="btn">返回</a>
                    <button type="button" class="btn blue" onclick="updateDataDetail();">提交</button>      
                </div>
            </div>
            
    	</div>
	</div>
</div>
<#include "/sys/bottom.ftl">