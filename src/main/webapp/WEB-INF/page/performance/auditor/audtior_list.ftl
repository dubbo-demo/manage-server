<#include "/sys/top.ftl"> 
<#include "/sys/left.ftl">

<div class="page-content">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				
				<!--页面标题-->
				<h3 class="page-title">
				</h3>
				<!--面包屑导航-->
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="${ctx}/index.htm">首页</a> <i
						class="icon-angle-right"></i></li>
					<li><a href="#">员工绩效管理</a><i class="icon-angle-right"></i></li>
					<li><a href="#">${state.desc}绩效考核</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
			
				<form id="searchForm" action="#" class="form-horizontal"
					method="post">
					<input type="hidden" name="auditStage" value="${state.code}">
					<input type="hidden" name="sortField" id="sortField" value="passedApplyCount">
					<input type="hidden" name="sortOrder" id="sortOrder" value="desc">
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">员工姓名：</label> <input
								type="text" class="m-wrap span8" name="employeeName"
								value="${(queryDto.employeeName)!}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">员工编号：</label> <input
								type="text" class="m-wrap span8" name="employeeNo"
								value="${(queryDto.employeeNo)!}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">查询日期：</label> 
							<div class="">
									<div class="input-append date date-picker" data-date="${(queryDto.applyTimeStart?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="applyTimeStart" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.applyTimeStart?string('yyyy-MM-dd'))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(queryDto.applyTimeEnd?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="applyTimeEnd" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.applyTimeEnd?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>	
							</div>														
						</div>
					</div>
					<p>
						<button type="button" class="btn blue" onclick="search()">查询</button>
						<button type="button" class="btn blue" onclick="export_excel()">下载</button>
					</p>
				</form>
			</div>

			<div class="tabbable tabbable-custom tabbable-custom-profile">
				<table id="table" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>TOP</th>
							<th>员工姓名</th>
							<th>员工编号</th>
							<th>进件数</th>
							<th data-field="passedApplyCount">${state.desc}通过数</th>
							<th data-field="backApplyCount">${state.desc}回退数</th>
							<th>${state.desc}总时长(小时)</th>
							<th data-field="passedApplyCount">${state.desc}通过率</th>
							<th data-field="backApplyCount">${state.desc}回退率</th>
							<th data-field="avgAuditTime">${state.desc}平均时效</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
					<#if dataList??&&dataList?size gt 0>					
					<#list dataList as item>
						<tr>
							<td></td>
							<td>${item.employeeName!}</td>
							<td>${item.employeeNo!}</td>
							<td>${item.totalApplyCount}</td>
							<td>${item.passedApplyCount}</td>
							<td>${item.backApplyCount}</td>
							<td>${item.totalAuditTime}</td>
							<td><#if item.totalApplyCount==0>0<#else>${(item.passedApplyCount*100/item.totalApplyCount)?string('#.##')}</#if>%</td>
							<td><#if item.totalApplyCount==0>0<#else>${(item.backApplyCount*100/item.totalApplyCount)?string('#.##')}</#if>%</td>
							<td>${item.avgAuditTime}</td>
							<td>
							<#if state=='AUDIT_FIRST'>
							<a href="javascript:page_jump('${ctx}/performance/employee/applylist.htm?employeeId=${item.auditor!}&employeePossiton=firstAuditor')">查看详情</a>
							<#else>
							<a href="javascript:page_jump('${ctx}/performance/employee/applylist.htm?employeeId=${item.auditor!}&employeePossiton=reviewAuditor')">查看详情</a>
							</#if>
							</td>
						</tr>
					</#list>
					</#if>	
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
$(function(){
	var t=$("#table").dataTable(
	{
		  "aoColumnDefs":[ { "bSortable": false, "aTargets": [0,1,2,3,6,10] },{"sType": "number-fate","aTargets": [7,8]}],
		  "aaSorting": [[7,'desc']],
		  "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
	             $('td:eq(0)', nRow).html(iDisplayIndexFull+1);
	      },
	      "fnHeaderCallback": function( nHead, aData, iStart, iEnd, aiDisplay ) {
	    	    var sortTh=$("#table th[aria-sort]");
	    		$("#sortField").val(sortTh.attr("data-field"));
	    		if(sortTh.attr("aria-sort")=='descending'){
	    			$("#sortOrder").val('desc');
	    		}else{
	    			$("#sortOrder").val('asc');
	    		}
	 	  }
	});
})
function search() {
		var auditStage=$("input[name='auditStage']").val();
		if(auditStage==11){
			//	初审绩效
			$("#searchForm").attr("action", "${ctx}/performance/auditor/firstList.htm");
		}else{
			// 终审绩效
			$("#searchForm").attr("action", "${ctx}/performance/auditor/reviewlist.htm");

		}
		ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").submit();
}

function page_jump(url){
	//ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
	//window.location.href=url+"&"+$("#searchForm").serialize();
	window.open(url+"&"+$("#searchForm").serialize());
}


function export_excel() {
	$("#searchForm").attr("action", "${ctx}/performance/auditor/export.htm");
    $("#searchForm").submit();
}
</script>