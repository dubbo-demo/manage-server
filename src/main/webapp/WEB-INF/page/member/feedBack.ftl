<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<style>
	.close {
	    background-image : none !important
	}
</style>
<script src="${cdnPath}/js/member/feedBack.js"></script>
<script type="text/javascript">
    var serverPath = "${serverPath}";
</script>

<div class="page-content">
<div class="container-fluid">

	<!-- header -->
	<div class="row-fluid">
			<!--页面标题-->
				<h3 class="page-title"></h3>
		<ul class="breadcrumb">
			<li><i class="icon-home"></i>
			<li><a href="${serverPath}/index.htm">首页</a> <i class="icon-angle-right"></i> </li>
			</li>
			<li>
				<li>客户管理</li>
				<i class="icon-angle-right"></i>
			</li>
			<li>意见反馈</li>
		</ul>
	</div>
	
	<div class="portlet-body form">
		<form id="searchForm" action="${serverPath}/member/queryFeedBack.htm" class="form-horizontal" method="post">
		<@p.pageForm value=page />
			<div class="row-fluid">
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">反馈时间：</label>
					<input type="text" class="m-wrap span8" style="width:150px;" readonly="readonly" id="searchstartDate" name="startDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${(queryDto.startDate?string("yyyy-MM-dd"))!''}">--
					<input type="text" class="m-wrap span8" style="width:150px;" readonly="readonly" id="searchendDate" name="endDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${(queryDto.endDate?string("yyyy-MM-dd"))!''}">
				</div>
				<div class="control-group span4 ">
					<label class="help-inline text-right span4"></label>
					<button type="button" class="btn blue" onclick="search()">查询</button>
					<button type="button" class="btn blue" onclick="downLoad()">导出</button>
				</div>
			</div>
		</form>
	</div>

	<div class="tabbable tabbable-custom tabbable-custom-profile">
		<!-- table -->
		<table class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th>反馈内容</th>
					<th>联系方式</th>
					<th>反馈时间</th>
					<th>手机型号</th>
				</tr>
			</thead>
			<tbody>
				<#list page.result as item>
				<tr>
					<td>${(item.content)!''}</td>
					<td>${(item.mobilePhone)!''}</td>
					<td>${item.sendDate?string("yyyy-MM-dd hh:mm:ss")}</td>
					<td>${(item.phoneModel)!''}</td>
				</tr>
				</#list>
			</tbody>
		</table>
        <@p.pagination value=page />

	<div id="audit" style='display: none'>
		<form id="downLoadForm" action="${serverPath}/member/export.htm" method="POST">
			<input type="hidden" name="startDate" id="downLoadstartDate">
			<input type="hidden" name="endDate" id="downLoadendDate">
		</form>
	</div>
	</div>
<#include "/sys/bottom.ftl">
</body>
</html>