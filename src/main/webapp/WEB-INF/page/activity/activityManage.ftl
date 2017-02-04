<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<style>
	.close {
	    background-image : none !important
	}
</style>
<script src="${cdnPath}/js/activity/addActivity.js"></script>
<script type="text/javascript">
    var serverPath = "${serverPath}";
    var createDate = "${(queryDto.createDate?string("yyyy-MM-dd"))!''}";
    var pageIndex = "${(page.pageIndex)!1}";
    var pageSize = "${(page.pageSize)!10}";
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
				<li>运营管理</li>
				<i class="icon-angle-right"></i>
			</li>
			<li>资讯活动列表</li>
		</ul>
	</div>
	
	<div class="portlet-body form">
		<form id="searchForm" action="${serverPath}/activity/activityQuery.htm" class="form-horizontal" method="post">
		<@p.pageForm value=page />
			<div class="row-fluid">
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">发布时间：</label>
					<input type="text" style="width:200px;" class="m-wrap span8" readonly="readonly" name="createDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${(queryDto.createDate?string("yyyy-MM-dd"))!''}">
					&nbsp;&nbsp;&nbsp;<button class="btn blue" onclick="search(event)">查询</button>
				</div>
			</div>
			<div class="row-fluid">
				<a class="btn blue" href="${serverPath}/activity/addActivity.htm?&createDate=${(queryDto.createDate?string("yyyy-MM-dd"))!''}&pageIndex=${(page.pageIndex)!1}&pageSize=${(page.pageSize)!10}">新增活动信息</a>
			</div>
		</form>
	</div>

	<div class="tabbable tabbable-custom tabbable-custom-profile">
		<!-- table -->
		<table class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th>活动编号</th>
					<th>活动标题</th>
					<th>活动详情样式</th>
					<th>活动/资讯类型</th>
					<th>发布位置</th>
					<th>发布时间</th>
					<th>发布人</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<#list page.result as item>
				<tr>
					<td>${item.activityNo}</td>
					<td>${item.title}</td>
					<td><#if item.detailStyle==0>无活动详情
						<#elseif item.detailStyle==1>URL外链跳转
						<#else>---</#if>
					</td>
					<td>宣传类</td>
					<td>首页 banner</td>
					<td>${item.createDate?string("yyyy-MM-dd hh:mm:ss")}</td>
					<td>${item.createPerson}</td>
					<td><a href="javascript:deleteInfo(${item.id?c})">删除</a>
					<a href="${serverPath}/activity/showActivityDetial.htm?id=${item.id?c}&activityType=${item.activityType}&createDate=${(queryDto.createDate?string("yyyy-MM-dd"))!''}&pageIndex=${(page.pageIndex)!1}&pageSize=${(page.pageSize)!10}">查看</a></td>
				</tr>
				</#list>
			</tbody>
		</table>
        <@p.pagination value=page />

	<div id="edit" style='display: none'>
		<div class="well">
			<form:form id="editForm" modelAttribute="editactivity"
				action="${ctx}/sys/activity/editActivity.htm" method="post">
				<input id="activityId" name="activityId" type="hidden" value="" />
				<input id="operate" name="operate" type="hidden" value="" />
			</form:form>
			<span id="editerror"></span>
		</div>
	</div>

	<div id="audit" style='display: none'>
		<form:form id="auditForm" modelAttribute="activity"
			class="form-horizontal" style="margin-top:15px">
			<input id="activityIdAudit" name="activityIdAudit" type="hidden"
				value="" />
			<div class="control-group">
				<label for="auditResult" class="control-label" style="width: 90px">审核结果：</label>
				<div class="controls" style="margin-left: 100px">
					<select class="span3" name="auditResult" id="auditResult">
						<option value="sel">请选择审核结果</option>
						<option value="2">通过</option>
						<option value="3">拒绝</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" style="width: 90px">&nbsp;</label>
				<div class="controls" style="margin-left: 100px">
					<div id="btnSubmit" class="btn btn-primary" onclick="auditSubmit()">
						<i class="icon-ok icon-white"></i>提交
					</div>
					&nbsp;
					<div id="btnCancel" class="btn" onclick="closeDialog('audit')">取消</div>
				</div>
			</div>
			<!--  <tr>
					<td align="center">审核结果： <select class="span3"  name="auditResult" id="auditResult">
							<option value="sel">请选择审核结果</option>
							<option value="2">通过</option>
							<option value="3">拒绝</option>
					</select>
					</td>
				</tr>
				<tr>
					<td><input id="btnSubmit" type="button" value="提交" onclick="auditSubmit()" />&nbsp; 
					<input id="btnCancel"  type="button" value="取消" onclick="closeDialog('audit')" /></td>
				</tr>-->
		</form:form>
	</div>

	<div id="activityDel" style='display: none'>
		<!-- <table> -->
		<form:form id="delForm" modelAttribute="activity"
			action="${ctx}/sys/activity/deleteActivity.htm" method="post"
			class="form-horizontal" style="margin-top:15px">
			<input id="activityIdDel" name="activityIdDel" type="hidden" value="" />
			<!-- <tr>
					<td align="center">确认删除该活动?</td>
				</tr>
				<tr>
					<td><input id="btnSubmit" type="button" value="提交" onclick="delSubmit()" />&nbsp; 
						<input id="btnCancel" type="button" value="取消" onclick="closeDialog('activityDel')" /></td>
				</tr> -->
			<div class="control-group">
				<p style="text-align: center; font-size: 16px; padding-top: 20px">
					确认删除该活动?</p>
			</div>
			<div class="control-group">
				<label class="control-label" style="width: 112px">&nbsp;</label>
				<div class="controls" style="margin-left: 122px">
					<div id="btnSubmit" class="btn btn-primary" onclick="delSubmit()">
						<i class="icon-ok icon-white"></i>提交
					</div>
					&nbsp;
					<div id="btnCancel" class="btn"
						onclick="closeDialog('activityDel')">取消</div>
				</div>
			</div>
		</form:form>
		<!--</table>-->
	</div>

	<div id="down" style='display: none'>
		<!--	<table>-->
		<form:form id="downForm" modelAttribute="activity"
			class="form-horizontal" style="margin-top:15px"
			action="${ctx}/sys/activity/downActivity.htm" method="post">
			<input id="activityIdDown" name="activityIdDown" type="hidden"
				value="" />
			<!--  <tr>
					<td align="center">确认下架该活动?</td>
				</tr>
				<tr>
					<td><input id="btnSubmit" type="button" value="提交"
						onclick="return downSubmit();" />&nbsp; <input id="btnCancel"
						type="button" value="取消" onclick="closeDialog('down')" /></td>
				</tr> -->
			<div class="control-group">
				<p style="text-align: center; font-size: 16px; padding-top: 20px">
					确认下架该活动?</p>
			</div>
			<div class="control-group">
				<label class="control-label" style="width: 112px">&nbsp;</label>
				<div class="controls" style="margin-left: 122px">
					<div id="btnSubmit" class="btn btn-primary"
						onclick="return downSubmit();">
						<i class="icon-ok icon-white"></i>提交
					</div>
					&nbsp;
					<div id="btnCancel" class="btn" onclick="closeDialog('down')">取消</div>
				</div>
			</div>

		</form:form>
		<!-- </table>-->
	</div>

	<div id="up" style='display: none'>
		<!-- <table> -->
		<form:form id="upForm" modelAttribute="activity"
			class="form-horizontal" style="margin-top:15px">
			<input id="activityIdUp" name="activityIdUp" type="hidden" value="" />
			<!--	<tr>
					<td align="center">确认上架 该活动?</td>
				</tr>
				<tr>
					<td><input id="btnSubmit" type="button" value="提交"
						onclick="return upSubmit();" />&nbsp; <input id="btnCancel"
						type="button" value="取消" onclick="closeDialog('up')" /></td>
				</tr>  -->
			<div class="control-group">
				<p style="text-align: center; font-size: 16px; padding-top: 20px">
					确认上架 该活动?</p>
			</div>
			<div class="control-group">
				<label class="control-label" style="width: 112px">&nbsp;</label>
				<div class="controls" style="margin-left: 122px">
					<div id="btnSubmit" class="btn btn-primary"
						onclick="return upSubmit();">
						<i class="icon-ok icon-white"></i>提交
					</div>
					&nbsp;
					<div id="btnCancel" class="btn" onclick="closeDialog('up')">取消</div>
				</div>
			</div>
		</form:form>
		<!-- </table> -->
	</div>
	</div>
<#include "/sys/bottom.ftl">
</body>
</html>