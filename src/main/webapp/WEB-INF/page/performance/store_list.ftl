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
					<li><i class="icon-home"></i> <a href="${serverPath}/index.htm">首页</a> <i
						class="icon-angle-right"></i></li>
					<li><a href="javascript:void(0);">员工绩效管理</a><i class="icon-angle-right"></i></li>
					<li><a href="javascript:void(0);">绩效考核统计表</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
				<form id="searchForm" action="#" class="form-horizontal"
					method="post">
					<input type="hidden" name="sortField" id="sortField" value="sumRepayMoney">
					<input type="hidden" name="sortOrder" id="sortOrder" value="desc">
					<input type="hidden" name="storeId" value="${(query.storeId)!""}">
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">员工姓名：</label>
							<input type="text" class="m-wrap span6" name="employeeName" value="${(query.employeeName)!""}">
						</div>
						<div class="control-group span6 ">
							<label class="help-inline text-right span6">查询日期(放款日期区间)：</label> 
							<div class="">
									<div class="input-append date date-picker" data-date="${(query.signTimeStart?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="signTimeStart" class="m-wrap span7 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(query.signTimeStart?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(query.signTimeEnd?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="signTimeEnd" class="m-wrap span7 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(query.signTimeEnd?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>	
							</div>		
						</div>
					</div>	
					<div class="row-fluid">
						<div class="control-group span4 ">
							<button type="button" class="btn blue" onclick="search()">查询</button>
							<button type="button" class="btn blue" onclick="export_excel()">下载</button>
						</div>
					</div>
				</form>
			</div>

			<div class="tabbable tabbable-custom tabbable-custom-profile">
				<table id="table" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>TOP</th>
							<th>团队名称</th>
							<th>团队经理</th>
							<th>员工姓名</th>
							<th>员工编号</th>
							<th data-field="sumApplyMoney">申请总金额</th>
							<th data-field="sumRepayMoney">放款总金额</th>
							<th>合同状态</th>
						</tr>
					</thead>
					<tbody>
					<#if data??>
						<#list data as item>
						<tr id="${item_index+1!}">
							<td>${item_index+1!}</td>
							<td>${item.teamName!}</td>
							<td>${item.teamManager!}</td>
							<td>${item.employeeName!}</td>
							<td>${item.bmId!}</td>
							<td>${item.sumApplyMoney!'0'}</td>
							<td>${item.sumRepayMoney!'0'}</td>
							<td>${item.stateName}</td>
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
	$("#table").dataTable(
	{
		   "aoColumnDefs":[ { "bSortable": false, "aTargets": [0,1,2,3,4,7] }]//设置不需要排序的列
		   ,"aaSorting": [[6, "desc"]]
		   ,"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
	       		$('td:eq(0)', nRow).html(iDisplayIndexFull+1);
	       }
	       ,"fnHeaderCallback": function( nHead, aData, iStart, iEnd, aiDisplay ) {
	    	    var sortTh=$("#table th[aria-sort]");
	    		$("#sortField").val(sortTh.attr("data-field"));
	    		if(sortTh.attr("aria-sort")=='descending'){
	    			$("#sortOrder").val('desc');
	    		}else{
	    			$("#sortOrder").val('asc');
	    		}
	 	  }
	}
	);
})

</script>
<script>
	function page_back(url) {
		window.location.href = url+"?"+ChkUtil.getCookie("queryParams");
	}
	function search() {
		ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").attr("action", "${serverPath}/performance/bmStore.htm");
		$("#searchForm").submit();
	}
	function goPage(pageNumber) {
	    ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").attr("action", "${serverPath}/apply/progress/list.htm");
	    $("#pageIndex").val(pageNumber);
	    $("#searchForm").submit();
	}
	function page_jump(url){
		ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
		window.location.href=url;
	}
	
	function export_excel() {
		ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").attr("action", "${serverPath}/performance/bmStoreExport.htm");
		$("#searchForm").submit();
	}
	$(function(){
	});
</script>