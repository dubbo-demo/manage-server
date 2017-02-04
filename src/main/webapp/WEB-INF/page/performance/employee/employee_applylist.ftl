<#include "/sys/top.ftl"> <#include "/sys/left.ftl">
<div class="page-content">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<!--页面标题-->
				<h3 class="page-title"></h3>
				<!--面包屑导航-->
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="${ctx}/index.htm">首页</a> <i
						class="icon-angle-right"></i></li>
					<li><a href="#">员工绩效管理</a><i class="icon-angle-right"></i></li>
					<li><a href="#">详情列表</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
				<form id="searchForm" action="${ctx}/performance/employee/applylist.htm" class="form-horizontal" method="post">
					<input type="hidden" name="employeeId" value="${queryDto.employeeId!}">
					<input type="hidden" name="employeePossiton" value="${queryDto.employeePossiton!}">
					<input type="hidden" name="applyTimeStart" value="${(queryDto.applyTimeStart?string('yyyy-MM-dd'))!}">
					<input type="hidden" name="applyTimeEnd" value="${(queryDto.applyTimeEnd?string('yyyy-MM-dd'))!}">
					<@p.pageForm value=page />
				</form>
			</div>

			<div class="tabbable tabbable-custom tabbable-custom-profile">
			
				<!-- table -->
				<table class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th>员工姓名</th>
							<th>申请单号</th>
							<th>合同号</th>
							<th>申请金额</th>
							<th>放款金额</th>
							<th>初审通过日期</th>
							<th>终审通过日期</th>
							<th>合同状态</th>
						</tr>
					</thead>
					<tbody>
						<#if page??&&page.result??>
							<#list page.result as item>
							<tr class="odd gradeX">
								<td>${item_index+1 }</td>
								<td>${(emp.employeeName)!}</td>
								<td>${item.applyLoanNo!}</td>
								<td>${item.contractNo!}</td>
								<td>${item.applyMoney!}</td>
								<td>${item.repayMoney!}</td>
								<td><#if item.auditFirstTime??>${item.auditFirstTime?datetime}</#if></td>
								<td><#if item.auditReviewTime??>${item.auditReviewTime?datetime}</#if></td>
								<td><#if item.loanTime??>已放款<#else>待放款</#if></td>
							</tr>
							</#list>
						</#if>
					</tbody>
				</table>
				<@p.pagination value=page />
			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
	$(function(){
		$("select[name='subState']").val(${(queryDto.subState)!-1});
	})
</script>
