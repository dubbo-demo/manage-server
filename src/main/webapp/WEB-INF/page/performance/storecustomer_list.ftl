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
					<li><a href="javascript:void(0);">门店风控绩效考核</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
				<form id="searchForm" action="#" class="form-horizontal"
					method="post">
					<input type="hidden" name="sortField" id="sortField" value="visitRate">
					<input type="hidden" name="sortOrder" id="sortOrder" value="desc">
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">员工姓名：</label>
							<input type="text" class="m-wrap span8" name="employeeName" value="${(query.employeeName)!""}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">员工编号：</label>
							<input type="text" class="m-wrap span8" name="customerServiceId" value="${(query.customerServiceId)!""}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">查询日期：</label> 
							<div class="">
									<div class="input-append date date-picker" data-date="${(query.signTimeStart?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="signTimeStart" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(query.signTimeStart?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(query.signTimeEnd?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="signTimeEnd" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(query.signTimeEnd?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
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
							<th>员工姓名</th>
							<th>员工编号</th>
							<th data-field="sumCount">进件总数</th>
							<th data-field="oneAuditPassCount">初审通过数</th>
							<th data-field="oneAuditRefuseCount">初审退回数</th>
							<th data-field="twoAuditPassCount">终审通过数</th>
							<th data-field="twoAuditRefuseCount">终审退回数</th>
							<#--<th data-field="visitCount">外访数</th> -->
							<#--<th data-field="visitSumTime">外访总时长(小时)</th> -->
							<th data-field="signCount">签约件数</th>
							
							<th data-field="failRate">差错率</th>
							<th data-field="signRate">签约率</th>
							<#-- <th data-field="visitRate">外访平均时效</th> -->
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
					<#if data??>
						<#list data as item>
						<tr id="4">
							<td>${item_index+1!}</td>
							<td>${item.employeeName!}</td>
							<td>${item.customerServiceId!}</td>
							<td>${item.sumCount!}</td>
							<td>${item.oneAuditPassCount!'0'}</td>
							<td>${item.oneAuditRefuseCount!'0'}</td>
							<td>${item.twoAuditPassCount!'0'}</td>
							<td>${item.twoAuditRefuseCount!'0'}</td>
							<#--<td>${item.visitCount!'0'}</td> -->
							<#--<td>${item.visitSumTime!'0'}</td> -->
							<td>${item.signCount!'0'}</td>
							
							<td>${item.failRate!'0'}%</td>
							<td>${item.signRate!'0'}%</td>
							<#--<td>${item.visitRate!'0'}%</td> -->
							<td>
							<a 
							href="javascript:page_jump('${serverPath}/performance/employee/applylist.htm?employeeId=${item.customerServiceId!}&employeePossiton=customerService')">查看详情</a>
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
	$("#table").dataTable(
	{
		   "aoColumnDefs":[ { "bSortable": false, "aTargets": [0,1,2,11] },{"sType": "number-fate","aTargets": [9,10]}]//设置不需要排序的列
		   ,"aaSorting": [[10, "desc"]]
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
	function page_jump(url){
		window.open(url+"&"+$("#searchForm").serialize());
	}
	function search() {
		ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").attr("action", "${serverPath}/performance/storeCustomer.htm");
		$("#searchForm").submit();
	}
	function goPage(pageNumber) {
	    ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").attr("action", "${serverPath}/apply/progress/list.htm");
	    $("#pageIndex").val(pageNumber);
	    $("#searchForm").submit();
	}
	/**function page_jump(url){
		ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
		window.location.href=url;
	}*/
	
	function export_excel() {
		ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").attr("action", "${serverPath}/performance/storeCustomerExport.htm");
		$("#searchForm").submit();
	}
	$(function(){
	});
</script>