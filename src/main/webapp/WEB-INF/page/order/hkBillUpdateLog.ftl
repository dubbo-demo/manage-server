 <#include "/sys/top.ftl"> <#include "/sys/left.ftl">
<script src="${cdnPath}/js/loan/frb/frbTarget.js?v=${VERSION_NO}"></script>
<div class="page-content">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<!--页面标题-->
				<h3 class="page-title"></h3>
				<!--面包屑导航-->
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a
						href="${serverPath}/index.htm">首页</a> <i class="icon-angle-right"></i>
					</li>
					<li><a href="#">财务管理</a> <i class="icon-angle-right"></i></li>
					<li><a href="#">账单变更明细</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
			<form id="searchForm" class="form-horizontal" method="post">
				<p>
					<a href="javascript:back()" class="btn blue">返回</a>
                    <a href="javascript:showFile()" class="btn blue">附件</a>
				</p>
			</form>
			</div>
			<!-- table -->
			<div class="tabbable tabbable-custom tabbable-custom-profile">
				<table class="table table-bordered table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th>时间</th>
							<th>操作人</th>
							<th>变更总额</th>
							<th>变更类型</th>
							<th>本金</th>
							<th>利息</th>
							<th>违约金</th>
							<th>罚息</th>
						</tr>
					</thead>
					<tbody>
						<#list page as record>
						<tr>
							<td>${record_index+1}</td>
							<td>${record.createTime!}</td>
							<td>${record.createUser!}</td>
							<td>${record.changeMoney!}</td>
							<td>${record.changeTypeDesc!}</td>
                            <td>${record.alsoPrincipal!}</td>
                            <td>${record.alsoInterest!}</td>
                            <td>${record.alsoLateFee!}</td>
                            <td>${record.alsoPenalty!}</td>
						<tr></#list>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<#include "/sys/bottom.ftl">
<script>
    function back() {
        ChkUtil.form_trim($("#searchForm"));
        $("#searchForm").attr("action", "${ctx}/order/list.htm");
        $("#searchForm").submit();
    }

    
    function showFile() {
        ChkUtil.form_trim($("#searchForm"));
        $("#searchForm").attr("action", "${ctx}/order/export.htm");
        $("#searchForm").submit();
    }
</script>
