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
			
				<#if progress == 'todo' && roleName == 'firstAuditor'>
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
							<#if progress=='todo'>
								<li class="active">
									<a href="javascript:void(0)">待办</a>
								</li>
								<li>
									<a href="javascript:go_done()">已办</a>
								</li>
							</#if>
							
							<#if progress=='done'>
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
							<#if progress == 'todo'>
								<@p.sort field="a.fetchTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="取件日期" ></@p.sort>
								<th>状态</th>	
							</#if>
							<#if progress == 'done'>
								<@p.sort field="a.applyTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="进件日期" ></@p.sort>
								<@p.sort field="a.auditFirstTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="初审日期" ></@p.sort>
								<@p.sort field="a.auditReviewTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="复审日期" ></@p.sort>	
								<@p.sort field="a.lastDate" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="终审日期" ></@p.sort>
								<@p.sort field="a.superLastDate" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="总监终审日期" ></@p.sort>
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
								<#if progress == 'todo'>
									<td><#if item.fetchTime??>${item.fetchTime?datetime}</#if></td>
									<td>
										${item.stateDesc}
									</td>
									<td><#if item.subState??>
											<#if item.subState == 1140>
												<a target="_blank" href="${ctx}/apply/applyDetail.htm?applyLoanNo=${item.applyLoanNo!''}">信审</a>
											<#elseif item.subState == 1350>
												<a target="_blank" href="${ctx}/apply/applyDetail.htm?applyLoanNo=${item.applyLoanNo!''}">信审</a>
											<#elseif item.subState == 1355>
												<a target="_blank" href="${ctx}/apply/applyDetail.htm?applyLoanNo=${item.applyLoanNo!''}">信审</a>
											<#elseif item.subState == 1141>
												<a target="_blank" href="${ctx}/audit/base/baseInfoAdd.htm?applyLoanNo=${item.applyLoanNo!''}">回退处理</a>
										    <#else>
										    </#if>
										</#if>
										<#if progress == 'todo' && roleName == 'firstAuditor'>
											<a class="blue" href="#" onclick="abandonMain('${(item.applyLoanNo)!}')" data-toggle="modal"> 放弃</a>
										</#if>
									</td>
								</#if>
								<#if progress == 'done'>
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
			
			<div id="abandon" class="modal hide fade" tabindex="-1" data-width="760">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true"></button>
						<h4 class="refuseDivTitle">初审放弃</h4>
						<style>
					  	.refuseDivTitle {text-align:center;font-weight:bold}
					  	</style>
				</div>
				<div class="modal-body">
					<div class="row-fluid">
						<div class="control-group">
							<label class="help-inline text-right span2">说明：</label>
							<div class="controls">
							<textarea  id="interiorRemark" rows="4" class="span10 m-wrap  js_input"></textarea>
							</div>
						</div>
					</div>
					<input id = "applyLoanNoId" type = "hidden" value = ""/>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn blue" onclick="abandon();">确定</button>
					<button type="button" data-dismiss="modal" class="btn">取消</button>
				</div>
			</div>
		
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
	function go_done(){
		$("#searchForm").attr("action", "${ctx}/audit/list/done.htm");
		$("#searchForm").submit();
	}
	function go_todo(){
		$("#searchForm").attr("action", "${ctx}/audit/list/todo.htm");
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
	
	function abandonMain(applyLoanNo) {
		$("#applyLoanNoId").val('');
		$("#applyLoanNoId").val(applyLoanNo);
		$("#abandon").modal('show');
	}
	
	function abandon() {
		var f = false;
		$("#abandon .js_input").each(function(i,v){
			var _val = $(this).val();
			if(_val == "" || _val == -1){
				$(this).addClass("error");
				f = true;
			}else{
				$(this).removeClass("error");
			}
		});
		if(f) {
			return false;
		}
	
		var applyLoanNo = $("#applyLoanNoId").val();
		var callBackURL = serverPath + "/audit/list/todo.htm";
		$.ajax({
			url : serverPath + "/audit/abandon.htm",
			type : "post",
			data : {
				"applyLoanNo" : applyLoanNo,
				"interiorRemark" :$("#interiorRemark").val(),
				"Time" : new Date().getMilliseconds()
			},
			dataType : "json",
			success : function(result) {
				if (result.success) {
					window.location.href = callBackURL;
				} else {
					BootstrapDialog.alert(result.message);
				}
			},
			error : function() {
				BootstrapDialog.alert("操作失败");
			}
		});
	}
</script>