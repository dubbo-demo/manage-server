
<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/product/list.js"></script>

	<div class="page-content">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<!--页面标题-->
					<h3 class="page-title"></h3>
					<!--面包屑导航-->
					<ul class="breadcrumb">
						<li> <i class="icon-home"></i>
							<a href="${serverPath}/index.htm">首页</a>
							<i class="icon-angle-right"></i> 
						</li>
						<li>
							<a href="#">系统管理</a>
							<i class="icon-angle-right"></i> 
						</li>
						<li>
							<a href="#">日志管理</a>
						</li>
					</ul>
				</div>
			</div>
		<div>
					<form id="searchForm" class="form-horizontal"
					method="post">
					<@p.pageForm value=page  type="sort"/>
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">帐号：</label> <input
								type="text" class="m-wrap span8" name="queryUserName"
								value="${(queryUserName)!""}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">查询日期：</label> 
							
									<div class="input-append date date-picker" data-date="${(startDate?string("yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="startDate" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(startDate?string("yyyy-MM-dd"))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(endDate?string("yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="endDate" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(endDate?string("yyyy-MM-dd"))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>															
						</div>
					</div>
						<p>
							<button type="submit" class="btn blue">查询</button>
						</p>
				</form>
			<!-- table -->
			<table class="table table-bordered table-hover table-condensed">
				<thead>
					<tr>
						<th>帐户名</th>
						<th>登陆IP</th>
						<th>登陆时间</th>
						<th>操作内容</th>
					</tr>
				</thead>
				<tbody>
					<#list page.result as record>
						<tr>
							<td>${record.userName!}</td>
							<td>${record.operatorIp!}</td>
							<td>${record.operatorTime?string('yyyy-MM-dd HH:mm:ss')}</td>
							<td>${record.operatorDesc!}</td>
						<tr>
					</#list>
				</tbody>
			</table>
			<@p.pagination value=page />
	</div>
	
	
<#include "/sys/bottom.ftl">