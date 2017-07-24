<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/employee/editEmployeeInfo.js?v=${VERSION_NO}"></script>
<script type="text/javascript">
    var serverPath = "${serverPath}";
    var id = "${item.id}";
    var dto = {orgType:"${(item.orgType)!''}", orgId:"${(item.orgId)!''}", storeId:"${(item.storeId)!''}", 
    		teamId:"${(item.teamId)!''}", jobLevel:"${(item.jobLevel)!''}"};
</script>
<div class="page-content" >
<div class="container-fluid">
	<!-- header -->
	<div class="row-fluid">
			<!--页面标题-->
				<h3 class="page-title"></h3>
		<ul class="breadcrumb">
			<li>
				<i class="icon-home"></i>
				<a href="${serverPath}/index.htm">首页</a> 
				<i class="icon-angle-right"></i>
			</li>
			<li><span>员工绩效管理</span><i class="icon-angle-right"></i>
			</li>
			<li>
				<a href="${serverPath}/employee/queryEmployeeInfo.htm">员工信息管理</a>
				<i class="icon-angle-right"></i>
			</li>
			<li><span>修改员工信息</span></li>
		</ul>
	</div>
	<div class="portlet-body form">
		<form id="tab" action="${serverPath}/employee/updateEmployeeInfo.htm" method="POST" class="form-horizontal">
		<input type="hidden" name="id" value="${item.id}"/>
		<input type="hidden" name="oldTeam" value="${item.teamName!}"/>
		<input type="hidden" name="oldJobLevel" value="${item.jobLevel!}"/>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">员工姓名<span class="required">*</span></label>
				<div class="controls">
				<input type="text" class="m-wrap span12" id="employeeName" name="employeeName" value="${item.employeeName}">
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">身份证号<span class="required">*</span></label>
				<div class="controls">
				<input type="text" class="m-wrap span12" id="identityNumber" name="identityNumber" value="${item.identityNumber}">
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">手机号码<span class="required">*</span></label>
				<div class="controls">
				<input type="text" class="m-wrap span12" id="mobilePhone" name="mobilePhone" value="${item.mobilePhone}">
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">员工编号</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" id="employeeNo" name="employeeNo" readonly="readonly" value="${item.employeeNo}">
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">团队</label>
				<div class="controls">
				<select class="m-wrap span12" id="teamId" name="teamId" disabled>
					<option value ="">请选择</option>
				</select>
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">星级</label>
				<div class="controls">
				<select class="m-wrap span12" id="jobLevel" name="jobLevel" disabled>
					<option value ="">请选择</option>
				</select>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">入职日期</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" readonly="readonly" id="entryTime" name="entryTime" value="${item.entryTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">性别<span class="required">*</span></label>
				<div class="controls">
				<select class="m-wrap span12" id="sex" name="sex">
					<option value ="1" <#if item.sex==1>selected="selected"</#if>>男</option>
					<option value ="2" <#if item.sex==2>selected="selected"</#if>>女</option>
				</select>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">修改人</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" id="updateUser" name="updateUser" readonly="readonly" value="${(Session.currentUser.employeeName)!''}">
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">修改日期</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" readonly="readonly" value="${.now?string('yyyy-MM-dd')}">
				</div>
			</div>
		</div>
		<div class="form-actions">
			<a class="btn" href="${serverPath}/employee/queryEmployeeInfo.htm?employeeName=${(searchDto.searchemployeeName)!''}&employeeNo=${(searchDto.searchemployeeNo)!''}&identityNumber=${(searchDto.searchidentityNumber)!''}&orgType=${(searchDto.searchorgType)!''}&orgId=${(searchDto.searchorgId)!''}&orgName=${(searchDto.searchorgName)!''}&positionId=${(searchDto.searchpositionId)!''}&pageIndex=${(searchDto.pageIndex)!1}&pageSize=${(searchDto.pageSize)!10}">返回</a>
			<button class="btn blue" onclick="edit(event);return false;">提交</button>
		</div>
		</form>
	</div>
</div>
</div>
</div>
<#include "/sys/bottom.ftl">
</body>
</html>