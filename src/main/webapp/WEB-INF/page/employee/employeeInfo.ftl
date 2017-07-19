<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/employee/employeeInfo.js?v=${VERSION_NO}"></script>
<script type="text/javascript">
    var serverPath = "${serverPath}";
    
    var queryTreeDto = {orgId:"${(orgId)!''}"};
    var queryDto = {orgId:"${(queryDto.orgId)!''}", positionId:"${(queryDto.positionId)!''}"};
	var treeObject={serverPath:serverPath, treePullDown:"#treePullDown", orgId:"#orgId", orgName:"#orgName", 
		orgType:"#orgType", level:"#level", storeId:"#storeId"};

	$(function() {
		initTreePullDown(treeObject.treePullDown);
		$("select[name='icmbFlag']").val(${(queryDto.icmbFlag)!});
	});
</script>
<div class="page-content" >
<div class="container-fluid">
	<!-- header -->
	<div class="row-fluid">
			<!--页面标题-->
				<h3 class="page-title">
				</h3>
		<ul class="breadcrumb">
			<li><i class="icon-home"></i>
			<li><a href="${serverPath}/index.htm">首页</a> <i class="icon-angle-right"></i> </li>
			<li><span>员工绩效管理</span><i class="icon-angle-right"></i></li>
			<li><span>员工信息管理</span></li>
		</ul>
	</div>
	
	<div class="portlet-body form">
		<form id="searchForm" action="${serverPath}/employee/queryEmployeeInfo.htm" class="form-horizontal" method="post">
		<@p.pageForm value=page />
		<input type="hidden" id="orgId" name="orgId" value="${(queryDto.orgId)!''}"></input>
			<div class="row-fluid">
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">员工姓名：</label>
					<input type="text" class="m-wrap span8" name="employeeName" value="${(queryDto.employeeName)!''}">
				</div>
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">员工编号：</label>
					<input type="text" class="m-wrap span8" name="employeeNo" value="${(queryDto.employeeNo)!''}">
				</div>
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">身份证号：</label>
					<input type="text" class="m-wrap span8" name="identityNumber" value="${(queryDto.identityNumber)!''}">
				</div>
			</div>
			
			<div class="row-fluid">
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">组织架构：</label>
					<input type="text" class="m-wrap span8" id="orgName" value="${(queryDto.orgName)!''}" name="orgName" readonly="readonly" onclick="showMenu('#orgName')">
				</div>
				<div id="menuContent" class="menuContent">
					<ul id="treePullDown" class="ztree" style="margin-top:0; width:132px;"></ul>
				</div>
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">岗位：</label>
					<select class="m-wrap span8" id="positionId" name="positionId">
						<option value ="0">请选择</option>
					</select>
				</div>
				<div class="control-group span4 ">
                    <label class="help-inline text-right span4">状态：</label>
                    <select class="m-wrap span8" id="icmbFlag" name="icmbFlag">
                        <option value ="-1">全部</option>
                        <option value ="0" selected = "selected">在职</option>
                        <option value ="1">离职</option>
                    </select>
                </div>
			</div>
			<p>
					<@shiro.hasPermission name="employee:search">
					<button class="btn blue" onclick="search(event)">查询</button>
					</@shiro.hasPermission>
					<@shiro.hasPermission name="employee:add">
					<a class="btn blue" href="${serverPath}/employee/addEmployeeInfo.htm?searchemployeeName=${(queryDto.employeeName)!''}&searchemployeeNo=${(queryDto.employeeNo)!''}&searchidentityNumber=${(queryDto.identityNumber)!''}&searchorgType=${(queryDto.orgType)!''}&searchorgId=${(queryDto.orgId)!''}&searchorgName=${(queryDto.orgName)!''}&searchpositionId=${(queryDto.positionId)!''}&pageIndex=${(page.pageIndex)!1}&pageSize=${(page.pageSize)!10}">新增员工信息</a>
					</@shiro.hasPermission>
			</p>
		</form>
	</div>
	
	<div class="tabbable tabbable-custom tabbable-custom-profile">
		<!-- table -->
		<table class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>员工姓名</th>
					<th>员工编号</th>
					<th>身份证号</th>
					<th>性别</th>
					<th>手机号码</th>
					<th>所属区域</th>
					<th>大区</th>
					<th>门店</th>
					<th>部门</th>
					<th>岗位</th>
					<th>星级</th>
					<th>团队负责人</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<#list page.result as item>
				<tr>
					<td>${item_index+1}
						<input type="hidden" value="${item.id?c}"/>
					</td>
					<td>${item.employeeName}</td>
					<td>${item.employeeNo}</td>
					<td>${item.identityNumber}</td>
					<td><#if item.sex == 1>男<#else>女</#if></td>
					<td>${item.mobilePhone}</td>
					<td><#if item.orgType == 2>总部<#else>大区</#if></td>
					<td>${(item.regionName)!''}</td>
					<td>${(item.storeName)!''}</td>
					<td>${item.departmentName}</td>
					<td>${item.positionName}</td>
					<td>${(item.jobLevel)!''}</td>
					<td>${(item.leaderName)!''}</td>
                    <td>					
                    <@shiro.hasPermission name="employee:edit">
					<a href="${serverPath}/employee/editEmployeeInfo.htm?id=${item.id?c}&searchemployeeName=${(queryDto.employeeName)!''}&searchemployeeNo=${(queryDto.employeeNo)!''}&searchidentityNumber=${(queryDto.identityNumber)!''}&searchorgType=${(queryDto.orgType)!''}&searchorgId=${(queryDto.orgId)!''}&searchorgName=${(queryDto.orgName)!''}&searchpositionId=${(queryDto.positionId)!''}&pageIndex=${(page.pageIndex)!1}&pageSize=${(page.pageSize)!10}">修改</a>
					</@shiro.hasPermission>
					<@shiro.hasPermission name="employee:move">
					<a href="${serverPath}/employee/manageEmployeeMoveInfo.htm?id=${item.id?c}&searchemployeeName=${(queryDto.employeeName)!''}&searchemployeeNo=${(queryDto.employeeNo)!''}&searchidentityNumber=${(queryDto.identityNumber)!''}&searchorgType=${(queryDto.orgType)!''}&searchorgId=${(queryDto.orgId)!''}&searchorgName=${(queryDto.orgName)!''}&searchpositionId=${(queryDto.positionId)!''}&pageIndex=${(page.pageIndex)!1}&pageSize=${(page.pageSize)!10}">员工调动</a>
					</@shiro.hasPermission>
					<a href="${serverPath}/employee/queryEmployeeMoveInfo.htm?id=${item.id?c}&searchemployeeName=${(queryDto.employeeName)!''}&searchemployeeNo=${(queryDto.employeeNo)!''}&searchidentityNumber=${(queryDto.identityNumber)!''}&searchorgType=${(queryDto.orgType)!''}&searchorgId=${(queryDto.orgId)!''}&searchorgName=${(queryDto.orgName)!''}&searchpositionId=${(queryDto.positionId)!''}&pageIndex=${(page.pageIndex)!1}&pageSize=${(page.pageSize)!10}">变更记录</a>
					<a href="${serverPath}/employee/queryEmployeeInfoDetail.htm?id=${item.id?c}&searchemployeeName=${(queryDto.employeeName)!''}&searchemployeeNo=${(queryDto.employeeNo)!''}&searchidentityNumber=${(queryDto.identityNumber)!''}&searchorgType=${(queryDto.orgType)!''}&searchorgId=${(queryDto.orgId)!''}&searchorgName=${(queryDto.orgName)!''}&searchpositionId=${(queryDto.positionId)!''}&pageIndex=${(page.pageIndex)!1}&pageSize=${(page.pageSize)!10}">详细信息</a>
					<@shiro.hasPermission name="employee:forbidden">
					<a  <#if item.icmbFlag?? && item.icmbFlag == '0'>href="javascript:updateUserflag(${item.id!},${item.userFlag!})"<#else>href="javascript:alertInfo()"</#if>><#if item.userFlag?? &&  item.userFlag == '0'>启用<#else>禁用</#if></a>
					</@shiro.hasPermission>
					<a data-target="#icmbShow" data-toggle="modal" class="aIcmbShow" data-id='${item.id!}' data-icmbflag='${item.icmbFlag!}'><#if item.icmbFlag?? && item.icmbFlag == '0'>离职<#else>复职</#if></a></td>
				</tr>
				</#list>
			</tbody>
		</table>
        <div id="icmbShow" class="modal hide fade icmbShow" tabindex="-1" data-width="760">
            <input type="hidden" id="icmbShowId" name="icmbShowId"></input>
            <input type="hidden" id="icmbShowFlag" name="icmbShowFlag"></input>
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                    aria-hidden="true"></button>
                    <h3>备注</h3>
            </div>
            <div class="modal-body">
                <div class="row-fluid ">
                    <label class="help-inline span3 text-left control-label"><span id="icmbTimeLabel"></span><span class="required">*</span></label>
                    <input id="icmbTime" name="icmbTime" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"/><span class="add-on"><i class="icon-calendar"></i></span>
                </div>      
                <div class="row-fluid ">
                    <label class="help-inline span3 text-left control-label">说明：<span class="required">*</span></label>
                    <textarea rows="4" class="span8 m-wrap" id="remark" name="remark" maxlength="50"></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default icmbButton" data-dismiss="modal">关闭</button>  
                <button type="button" class="btn blue icmbButton" onclick="updateIcmbFlag();">确定</button>
            </div>
        </div>
        <@p.pagination value=page />
	</div>
</div>
</div>
</div>
<#include "/sys/bottom.ftl">
</body>
</html>