
<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script type="text/javascript">
    $(function() {
        $("select[name='roleType']").val(${(queryDto.roleType)!});
    });
</script>
<script src="${cdnPath}/js/role/role.js?v=${VERSION_NO}"></script>
	<div class="page-content">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<!--页面标题-->
					<h3 class="page-title"></h3>
					<!--面包屑导航-->
					<ul class="breadcrumb">
						<li> <i class="icon-home"></i>
							<a href="${serverPath}/index.htm">首页</a>
							<i class="icon-angle-right"></i> 
						</li>
						<li>
							<a href="#">系统管理</a>
							<i class="icon-angle-right"></i> 
						</li>
						<li>
							<a href="#">角色权限管理</a>
						</li>
					</ul>
				</div>
			</div>
		<div>
		<form id="searchForm" class="form-horizontal" method="post" action="${serverPath}/role/queryPageList.htm">
        <div class="row-fluid">
            <div class="control-group span4 ">
                <label class="help-inline text-right span4">角色：</label>
                <input type="text" class="m-wrap span8" id="roleName" name="roleName" value="${queryDto.roleName!""}">
            </div>
            <div class="control-group span4 ">
                <label class="help-inline text-right span4">类型：</label>
                <select class="m-wrap span8" id="roleType" name="roleType">
                    <option value ="-1">请选择</option>
                    <option value ="0">菜单权限角色</option>
                    <option value ="1">数据权限角色</option>
                </select>
            </div>
        </div>
        <div class="row-fluid">
            <div class="control-group span4 ">
                <button class="btn blue" type="submit">查询</button>
                <a class="btn blue" data-toggle="modal" href="#role_save" id="role_new">新增</a>
            </div>
            
        </div>		
		<@p.pageForm value=page  type="sort"/>
        </form>
		<!-- table -->
		<div class="tabbable tabbable-custom tabbable-custom-profile">
		<table class="table table-bordered table-hover table-condensed">
			<thead>
				<tr>
					<th>角色编号</th>
					<th>角色名称</th>
					<th>角色类型</th>
					<th>权限设置</th>
					<th>角色状态</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<#list result.result as role>
				<tr id="role_${role.id}">
					<td>${role.roleCode}</td>
					<td>${role.roleName }</td>
					<td><#if role.roleType == 0>菜单权限角色<#else>数据权限角色</#if></td>
					<td>
						<#if role.roleType == 0>
                            <a class="btn blue" data-toggle="modal" data-permission-id="${role.id}" href="#role_permission">权限设置</a>
						<#else>
                            <a class="btn blue" data-toggle="modal" data-permission2-id="${role.id}" href="#role_condition">数据权限</a>
						</#if>
					</td>
					<td>
					<label class="radio span">
						<input 
						type="radio"
						<#if (role.state!0) == 1>
							checked =  "checked"
						</#if>
							name="${role.id}_state"
							onclick="role_update_status(1,${role.id})"
							/>
						启用
					</label>
					<label class="radio span">
						<input 
						type="radio"
						<#if (role.state!0) == 0>
							checked =  "checked"
						</#if>
							name="${role.id}_state"
							onclick="role_update_status(0,${role.id})"
							/>
						禁用
					</label>
					</td>
					<td><a data-toggle="modal" href="#role_edit"
						data-id="${role.id}">修改</a> <a
						href="javascript:void(0)"
						onclick="deleteById('${role.id}',event)">删除</a></td>
				</tr>
				</#list>
			</tbody>
		</table>
		<@p.pagination value=page />
	</div>
	</div>
	
	<div id="role_edit" class="modal hide fade" tabindex="-1"
		data-width="760">

	</div>
	<div id="role_save" class="modal hide fade" tabindex="-1"
		data-width="760">
		
	</div>
	<div id="role_permission" class="modal hide fade" tabindex="-1"
		data-width="760" >
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h3>权限选择</h3>
			</div>
			<ul id="treePullDown" class="ztree" style="margin-top:0;height:400px;overflow-y:auto;overflow-x:hidden "></ul>
			<div class="modal-footer">
				<button type="button" data-dismiss="modal" class="btn">返回</button>
				<button type="button" class="btn blue" data-dismiss="modal" onclick="role_permission_edit();">提交</button>
			</div>
	</div>
	
<#include "/sys/bottom.ftl">