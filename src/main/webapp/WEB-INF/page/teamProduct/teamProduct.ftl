<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/teamProduct/teamProduct.js?v=${VERSION_NO}"></script>
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
                        <a href="#">系统管理</a><i class="icon-angle-right"></i>
                    </li>
                    <li>
                        <a href="#"><#if dto??>系统管理<#else>信审取件配置列表</#if></a>
                    </li>
                </ul>
            </div>
        </div>
    <div>
    
    <div id="selectShow>
    	<div class="portlet-body">
    		<div class="row-fluid">
        		<p>
        		  <a class="btn blue" href="#addTeamProduct" data-toggle="modal">新增</a>
        		</p>
    		</div>
    	</div>
	
    	<!--显示查询结果-->
    	<div class="tabbable tabbable-custom tabbable-custom-profile">
    		<!-- table -->
    		<table class="table table-bordered table-hover table-condensed" id="selectProductFile">
    			<thead>
    				<tr>
    				    <th>序号</th>
    					<th>团队</th>
    					<th>取件产品类型</th>
    					<th>创建时间</th>
    					<th>操作</th>
    					<th>操作</th>
    				</tr>
    			</thead>
    			<tbody>
    			    <#list page.result as record>
    			        <tr id=${(record.id)!}>
    			            <td>${record_index+1}</td>
    			            <td>${(record.teamName)!}</td>
    			            <td>${(record.productTypeNames)!}</td>
    			            <td>${(record.createTime)!}</td>
    			            <td><a href="#updateTeamProduct" data-toggle="modal" onclick="update(${(record.id)!})">修改</a></td>
    			            <td><a href="#" onclick="del(${(record.id)!})">删除</a></td>
    			        </tr>
    			    </#list>
    			</tbody>
    		</table>
    		<@p.pagination value=page />
    		
    		<div id="addTeamProduct" class="modal hide fade" tabindex="-1" data-width="760">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h3>新建岗位</h3>
                </div>
                <div class="modal-body">
                    <div class="row-fluid">
                        <span class="control-label span2">岗位编号<span class="required">*</span></span>
                        <input type="text" name="addPositionCode" id="addPositionCode" readonly />
                    </div>
                    <div class="row-fluid">
                        <span class="control-label span2">岗位名称<span class="required">*</span></span>
                        <input type="text" name="addPositionName" id="addPositionName"/>
                    </div>
                    <div class="row-fluid">
                        <span class="control-label span2">是否管理岗<span class="required">*</span></span>
                        <select name="addIsManage" id="addIsManage">
                            <option value="0">否</option>
                            <option value="1">是</option>
                        </select>
                    </div>     
                </div>
                <div class="modal-footer">
                    <a class="btn blue" data-dismiss="modal" onclick="returnBack();" class="btn">返回</a>
                    <button type="button" class="btn blue" onclick="addPosition();">提交</button>      
                </div>
            </div>
            
            <div id="updateTeamProduct" class="modal hide fade" tabindex="-1" data-width="760">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h3>修改岗位</h3>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="updatePositionId" name="updatePositionId"></input>
                    <div class="row-fluid">
                        <span class="control-label span2">岗位编号<span class="required">*</span></span>
                        <input type="text" name="updatePositionCode" id="updatePositionCode" readonly >
                    </div>
                    <div class="row-fluid">
                        <span class="control-label span2">岗位名称<span class="required">*</span></span>
                        <input type="text" name="updatePositionName" id="updatePositionName" >
                    </div>
                    <div class="row-fluid">
                        <span class="control-label span2">是否管理岗<span class="required">*</span></span>
                        <select name="areaId" name="updateIsManage" id="updateIsManage">
                            <option value="0">否</option>
                            <option value="1">是</option>
                        </select>
                    </div>     
                </div>
                <div class="modal-footer">
                    <a class="btn blue" data-dismiss="modal" onclick="returnBack();" class="btn">返回</a>
                    <button type="button" class="btn blue" onclick="updatePosition();">提交</button>
                </div>
            </div>
            
    	</div>
	</div>
</div>
<#include "/sys/bottom.ftl">