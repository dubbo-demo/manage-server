<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<div class="page-content" >
<div class="container-fluid">
	<!-- header -->
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
			<li><span>员工调动记录</span></li>
		</ul>
	</div>

	<div class="portlet-body form">
		<div class="row-fluid">
			<div class="control-group span4 ">
				<a class="btn blue" href="${serverPath}/employee/queryEmployeeInfo.htm?employeeName=${(searchDto.searchemployeeName)!''}&employeeNo=${(searchDto.searchemployeeNo)!''}&identityNumber=${(searchDto.searchidentityNumber)!''}&orgType=${(searchDto.searchorgType)!''}&orgId=${(searchDto.searchorgId)!''}&orgName=${(searchDto.searchorgName)!''}&positionId=${(searchDto.searchpositionId)!''}&pageIndex=${(searchDto.pageIndex)!1}&pageSize=${(searchDto.pageSize)!10}">返回</a>
			</div>
		</div>
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
					<th>手机号码</th>
					<th>调动日期</th>
					<th>原所属区域</th>
					<th>新所属区域</th>
					<th>原大区</th>
					<th>新大区</th>
					<th>原门店</th>
					<th>新门店</th>
					<th>原部门</th>
					<th>新部门</th>
					<th>原岗位</th>
					<th>新岗位</th>
					<th>原团队</th>
					<th>新团队</th>
					<th>原星级</th>
					<th>新星级</th>
				</tr>
			</thead>
			<tbody>
				<#list page as item>
				<tr>
					<td>${item_index+1}
						<input type="hidden" value="${item.id?c}"/>
					</td>
					<td>${item.employeeName}</td>
					<td>${item.employeeNo}</td>
					<td>${item.identityNumber}</td>
					<td>${item.mobilePhone}</td>
					<td>${item.moveTime}</td>
					<td>${item.oldOrgType}</td>
					<td>${item.newOrgType}</td>
					<td>${item.oldRegionName}</td>
					<td>${item.newRegionName}</td>
					<td>${item.oldStoreName}</td>
					<td>${item.newStoreName}</td>
					<td>${item.oldDepartmentName}</td>
					<td>${item.newDepartmentName}</td>
					<td>${item.oldPosition}</td>
					<td>${item.newPosition}</td>
					<td>${item.oldTeam}</td>
					<td>${item.newTeam}</td>
					<td>${item.oldJobLevel}</td>
					<td>${item.newJobLevel}</td>
				</tr>
				</#list>
			</tbody>
		</table>
	</div>
</div>
</div>
<#include "/sys/bottom.ftl">
</body>
</html>