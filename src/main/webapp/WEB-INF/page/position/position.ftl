<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/position/position.js?v=${VERSION_NO}"></script>
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
                        <a href="#"><#if dto??>系统管理<#else>岗位管理</#if></a>
                    </li>
                </ul>
            </div>
        </div>
    <div>
    <form id="searchForm" class="form-horizontal" action="${serverPath}/position/showPosition.htm"
        method="post">
        <@p.pageForm value=page  type="sort"/>
        <div class="row-fluid">
            <div class="control-group span4 ">
                <label class="help-inline text-right span4">岗位名称：</label> 
                <input type="text" class="m-wrap span8" id="positionName" name="positionName" value="${(positionDto.positionName)!""}">
            </div>
        </div>
        <p>
            <button type="submit" class="btn blue">查询</button>
            <a class="btn blue" href="#addPosition" onclick='selectMaxPositionId()' data-toggle="modal">新增岗位</a>
        </p>
    </form>
    <div id="selectShow>	
    	<!--显示查询结果-->
    	<div class="tabbable tabbable-custom tabbable-custom-profile">
    		<!-- table -->
    		<table class="table table-bordered table-hover table-condensed" id="selectProductFile">
    			<thead>
    				<tr>
    					<th>岗位名称</th>
    					<th>岗位编号</th>
    					<th>是否管理岗</th>
    					<th>角色</th>
    					<th>操作</th>
    				</tr>
    			</thead>
    			<tbody>
    			    <#list page.result as position>
    			        <tr id=${(position.id)!}>
    			            <td id="positionName">${(position.positionName)!}</td>
    			            <td id="positionCode">${(position.positionCode)!}</td>
    			            <td><#if position?? && position.isManage?? && position.isManage==1>是<#else>否</#if></td>
    			            <td id="positionCode">${(position.roleNames)!}</td>
    			            <td><a href="#updatePosition" data-toggle="modal" onclick="update(${(position.id)!})">修改</a>
    			                <a href="#" onclick="del(${(position.id)!})">删除</a></td>
    			        </tr>
    			    </#list>
    			</tbody>
    		</table>
    		<@p.pagination value=page />
    		<div id="addPosition" class="modal hide fade" tabindex="-1" data-width="760" >
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h3>新建岗位</h3>
                </div>
                <div class="modal-body" style="height:150px;overflow-y:visible;">
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
                    <div class="row-fluid">
                    <lable class="dropdown bottom-up">
                        <a class="btn green" href="#" data-toggle="dropdown">菜单角色选择<i class="icon-angle-down"></i></a>
                        <div class="dropdown-menu menuRole bottom-up dropdown-checkboxes hold-on-click" style="height:150px;margin-left:0px;margin-bottom:-190px;width:100px;overflow-y:auto;overflow-x:auto">
                        </div>
                    </lable>
                    <lable class="dropdown bottom-up">
                        <a class="btn green" href="#" data-toggle="dropdown" style="margin-left:50px;">数据角色选择<i class="icon-angle-down"></i></a>
                        <div class="dropdown-menu dataRole bottom-up dropdown-checkboxes hold-on-click" style="height:150px;margin-left:50px;margin-bottom:-190px;width:100px;overflow-y:auto;overflow-x:auto">
                        </div>
                    </lable>                       
                    </div>  
                </div>
                <div class="modal-footer">
                    <a class="btn blue" data-dismiss="modal" onclick="returnBack();" class="btn">返回</a>
                    <button type="button" class="btn blue" onclick="addPosition();">提交</button>      
                </div>
            </div>
            
            <div id="updatePosition" class="modal hide fade" tabindex="-1" data-width="760">
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