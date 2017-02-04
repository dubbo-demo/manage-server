<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/sysUser/sysUser.js?v=${VERSION_NO}"></script>
<script type="text/javascript">
    var serverPath = "${serverPath}";
    var queryDto = {"orgType":"${queryDto.orgType!''}",
                    "storeId":"${queryDto.storeId!''}"};
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
                        <a href="#"><#if dto??>账户管理<#else>账户管理</#if></a>
                    </li>
                </ul>
            </div>
        </div>
    <div>
	<div class="portlet-body form">
		<form id="searchForm" class="form-horizontal" method="post">
		<@p.pageForm value=page />
			<div class="row-fluid">
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">账户名：</label>
					<input type="text" class="m-wrap span8" id="mobilePhone" name="mobilePhone" value="${queryDto.mobilePhone!""}">
				</div>
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">姓名：</label>
					<input type="text" class="m-wrap span8" id="employeeName" name="employeeName" value="${queryDto.employeeName!""}">
				</div>
			</div>
			<div class="row-fluid">
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">所属区域：</label>
					<select class="m-wrap span8" name="orgType" id="orgType" value="${queryDto.orgType!""}" onchange="selectStore()" ">
						<option value ="0">请选择</option>
						<option value ="1">大区</option>
						<option value ="2">总部</option>
					</select>
				</div>
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">门店：</label>
					<select class="m-wrap span8" id="storeId" name="storeId">
						<option value ="0">请选择</option>
					</select>
				</div>
			</div>
			<div class="row-fluid">
				<div class="control-group span4 ">
					<button class="btn blue" onclick="search()">查询</button>
				</div>
			</div>
		</form>
	</div>
	
	<div class="tabbable tabbable-custom tabbable-custom-profile">
		<!-- table -->
		<table class="table table-bordered table-hover table-condensed">
			<thead>
				<tr>
					<th>账户名</th>
					<th>姓名</th>
					<th>所属区域</th>
					<th>门店</th>
					<th>账号状态</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<#list page.result as item>
				<tr>
					<td>${item.mobilePhone!""}</td>
					<td>${item.employeeName!""}</td>
					<td><#if item.orgType == 2>总部<#else>大区</#if></td>
					<td>${item.parentOrgName!""}</td>
                    <td id=${item.id}><#if item.amountState == 0>禁用<#else>启用</#if></td>
                    <td><a id="btnNew${item.id}" class="btn blue" onclick="return update(${item.id});"><#if item.amountState == 1>禁用<#else>启用</#if></a>
                    </td>
				</tr>
				</#list>
			</tbody>
		</table>
		<@p.pagination value=page />
	</div>
</div>
</div>
<#include "/sys/bottom.ftl">