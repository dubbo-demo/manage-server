<#include "/sys/top.ftl"> <#include "/sys/left.ftl">
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
					<li><a href="#">风险管理</a><i class="icon-angle-right"></i></li>
					<li><a href="#">内部黑名单</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="row-fluid">
				<div class=" details">
					<#if result==0>
					<h3>导入成功:记录数${successDataSize}</h3>
					</#if> 
					<#if result==1>
						<#if excelErrorMsgs?? && excelErrorMsgs?size gt 0> 
						<h3>导入失败：数据错误记录数${excelErrorMsgs?size}:</h3>
						<#list	excelErrorMsgs as msg> <font color="#ff0000">${msg}</font> <br />
						</#list> 
						<#else> 
						<h3>导入失败：请检查数据</h3>
						</#if>
					</#if>
				</div>
			</div>
			<div class="form-actions">
				<a href="${ctx}/innerBlack/list.htm" class="btn">返回</a>
			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
