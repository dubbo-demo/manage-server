<#include "/sys/top.ftl"> <#include "/sys/left.ftl">
<style>
.table-hover tbody tr.pink>td{
background-color: #f2dede
}
.table-hover tbody tr:hover.pink>td{
background: #f2dede
}
</style>
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
					<li><span>信审业务管理</span><i class="icon-angle-right"></i></li>
					<li><span>审批列表</span></li>
				</ul>
			</div>
		</div>
		<div>
			
				<#if progress == 'manageTodo' && roleName != 'manager'>
					<div style="margin-botton:20px;" style="background: #fff; border: 0;">
						<span>尚有<span id = "auditSize">${auditSize!'0'}</span>件待审批</span>
						<button type="button" class="btn blue" onclick="pickup(event)">取件</button>	
					</div>
				<#else>
					<div style="margin-top:34px;" style="background: #fff; border: 0;">
					</div>
				</#if>
			
			
			<div class="portlet-body form">
				<form id="searchForm" action="#" class="form-horizontal" method="post">
					<input type="hidden" id="progress" name="progress" value="${progress!''}" />
					<@p.pageForm value=page!  type="sort"/>
				</form>
			</div>

			<div class="tabbable tabbable-custom tabbable-custom-profile">
			
				<ul class="nav nav-tabs">
							<#if progress=='manageTodo'>
								<li class="active">
									<a href="javascript:void(0)">待办</a>
								</li>
								<li>
									<a href="javascript:go_done()">已办</a>
								</li>
							</#if>
							
							<#if progress=='manageDone'>
								<li >
									<a href="javascript:go_todo()">待办</a>
								</li>
								<li class="active">
									<a href="javascript:void(0)">已办</a>
								</li>		
							</#if>
				</ul>
			
				<!-- table -->
				<table class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<@p.sort field="a.applyLoanNo" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="申请单号" ></@p.sort>
							<th>客户</th>
							<th>门店</th>
							<#if progress == 'manageTodo'>
								<@p.sort field="a.fetchTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="取件日期" ></@p.sort>
								<th>状态</th>	
							</#if>
							<#if progress == 'manageDone'>
								<@p.sort field="a.applyTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="进件日期" ></@p.sort>
								<@p.sort field="a.auditFirstTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="初审日期" ></@p.sort>
								<@p.sort field="a.auditReviewTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="复审日期" ></@p.sort>
								<@p.sort field="a.lastDate" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="终审日期" ></@p.sort>
								<@p.sort field="a.superLastDate" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="高级终审日期" ></@p.sort>	
							</#if>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if page??&&page.result??>
							<#list page.result as item>
							<#if item.stateDesc??&&item.stateDesc?contains("退回")>
							<tr class="odd gradeX pink" id="${item.id}">
							<#elseif item.stateDesc??&&item.stateDesc?contains("回退")>
							<tr class="odd gradeX pink" id="${item.id}">
							<#else>
							<tr class="odd gradeX" id="${item.id}">
							</#if>
								<td>${item_index+1 }</td>
								<td>
									${item.applyLoanNo!""}
								</td>
								<td>${item.memberName!""}</td>
								<td>${item.storeName!""}</td>
								<#if progress == 'manageTodo'>
									<td><#if item.fetchTime??>${item.fetchTime?datetime}</#if></td>
									<td>
										${item.stateDesc}
									</td>
									<td><#if item.subState??>
											<#if item.subState == 2370>
												<a target="_blank" href="${ctx}/apply/applyDetail.htm?applyLoanNo=${item.applyLoanNo!''}">信审</a>
											<#elseif item.subState == 2260>
												<a target="_blank" href="${ctx}/apply/applyDetail.htm?applyLoanNo=${item.applyLoanNo!''}">信审</a>
											<#elseif item.subState == 2264>
												<a target="_blank" href="${ctx}/audit/base/baseInfoAdd.htm?applyLoanNo=${item.applyLoanNo!''}">回退处理</a>
										    <#else>
										    </#if>
										</#if>
									</td>
								</#if>
								<#if progress == 'manageDone'>
									<td><#if item.applyTime??>${item.applyTime?datetime}</#if></td>
									<td><#if item.auditFirstTime??>${item.auditFirstTime?datetime}</#if></td>
									<td><#if item.auditReviewTime??>${item.auditReviewTime?datetime}</#if></td>
									<td><#if item.lastDate??>${item.lastDate?datetime}</#if></td>
									<td><#if item.superLastDate??>${item.superLastDate?datetime}</#if></td>
									<td><a target="_blank" href="${serverPath}/apply/applyDetail.htm?applyLoanNo=${item.applyLoanNo}&cType=1">查看详情</a></td>
								</#if>
							</tr>
							</#list>
						</#if>
					</tbody>
				</table>
				<@p.pagination value=page! />
			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
	function go_done(){
		$("#searchForm").attr("action", "${ctx}/audit/list/manageDone.htm");
		$("#searchForm").submit();
	}
	function go_todo(){
		$("#searchForm").attr("action", "${ctx}/audit/list/manageTodo.htm");
		$("#searchForm").submit();
	}
	function page_jump(url){
		ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
		window.location.href=url;
	}
	function pickup(event) {
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
		var progress = $('#progress').val();
		var forwardPath = serverPath + "/audit/list/"+progress+".htm";
		$.ajax({
			url : serverPath + "/audit/pickup.htm",
			type : "post",
			data : {
			},
			dataType : "json",
			cache : false,
			success : function(res) {
				BootstrapDialog.alert(res.message,function(){
					if(res.success) {
						window.location.href = forwardPath;
					}
				});
			},
			error : function(res) {
				BootstrapDialog.alert(res.message);
			}
		});
	}
</script>