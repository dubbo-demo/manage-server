<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<style>
.form-horizontal  .control-group{
  border-left: 1px solid #eee !important;
}
.form-horizontal .row-fluid:first-child .control-group{
  border-top: 1px solid #eee !important;
}
.form-horizontal .row-fluid:last-child .control-group{
  border-bottom: 0px solid #eee !important;
}
.form-horizontal .row-fluid .control-group:last-child {
  border-right: 1px solid #eee !important;
}
</style>
<div class="page-content">
	<div class="container-fluid">
		<div class="row-fluid">
				<!--页面标题-->
				<h3 class="page-title">
				</h3>
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
			<li><span>员工详情</span></li>
		</ul>
	</div>
	<div class="portlet-body form">
		<form class="form-horizontal form-bordered">
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">员工姓名</label>
				<div class="controls">
					<span class="text">${item.employeeName}</span>
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">姓名拼音</label>
				<div class="controls">
					<span class="text">${item.nameSpell}</span>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">身份证号</label>
				<div class="controls">
					<span class="text">${item.identityNumber}</span>
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">员工编号</label>
				<div class="controls">
					<span class="text">${item.employeeNo}</span>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">手机号码</label>
				<div class="controls">
					<span class="text">${item.mobilePhone}</span>
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">性别</label>
				<div class="controls">
					<span class="text"><#if item.sex==1>男<#else>女</#if></span>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">所属区域</label>
				<div class="controls">
					<span class="text"><#if item.orgType == 2>总部<#else>大区</#if></span>
				</div>
			</div>
		<#if item.orgType == 1>
			<div class="control-group span4 ">
				<label class="control-label">大区</label>
				<div class="controls">
					<span class="text">${item.regionName}</span>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">门店</label>
				<div class="controls">
					<span class="text">${(item.storeName)!''}</span>
				</div>
			</div>
		</#if>
			<div class="control-group span4 ">
				<label class="control-label">部门</label>
				<div class="controls">
					<span class="text">${item.departmentName}</span>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">岗位</label>
				<div class="controls">
					<span class="text">${item.positionName}</span>
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">团队</label>
				<div class="controls">
					<span class="text">${(item.teamName)!''}&nbsp;</span>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">星级</label>
				<div class="controls">
					<span class="text">${(item.jobLevel)!''}&nbsp;</span>
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">入职日期</label>
				<div class="controls">
					<span class="text">${item.entryTime}</span>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">创建人</label>
				<div class="controls">
					<span class="text">${item.createUser}</span>
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">创建日期</label>
				<div class="controls">
					<span class="text">${item.createTime?string("yyyy-MM-dd")}</span>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">修改人</label>
				<div class="controls">
					<span class="text">${(item.updateUser)!''}&nbsp;</span>
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">修改日期</label>
				<div class="controls">
					<span class="text"><#if item.updateUser??>${item.updateTime?string("yyyy-MM-dd")}</#if>&nbsp;</span>
				</div>
			</div>
		</div>
		</form>
		<div class="form-actions">
			<a class="btn" href="${serverPath}/employee/queryEmployeeInfo.htm?employeeName=${(searchDto.searchemployeeName)!''}&employeeNo=${(searchDto.searchemployeeNo)!''}&identityNumber=${(searchDto.searchidentityNumber)!''}&orgType=${(searchDto.searchorgType)!''}&orgId=${(searchDto.searchorgId)!''}&orgName=${(searchDto.searchorgName)!''}&positionId=${(searchDto.searchpositionId)!''}&pageIndex=${(searchDto.pageIndex)!1}&pageSize=${(searchDto.pageSize)!10}">返回</a>
		</div>
	</div>
</div>
</div>
<#include "/sys/bottom.ftl">
</body>
</html>