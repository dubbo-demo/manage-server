
<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/productAttribute/list.js"></script>

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
							<a href="#">产品类型管理</a>
						</li>
					</ul>
				</div>
			</div>
		<div>
		<div>
					<form id="searchForm" class="form-horizontal" action="${serverPath}/productAttribute/queryProductAttribute.htm"
					method="post">
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">产品名称：</label> 
								<select name="prodCode" data-value="${(prodCode)!""}">
									<option value="0">请选择</option>
								</select>
						</div>
					</div>
					<p>
					    <button type="submit" class="btn blue">查询</button>
					    <a href="${serverPath}/productAttribute/new_edit.htm" class="btn blue">新增</a>
					</p>
				</form>
		</div>
			<!-- table -->
			<div class="tabbable tabbable-custom tabbable-custom-profile">
			<table class="table table-bordered table-hover table-condensed">
				<thead>
					<tr>
						<th>序号</th>
						<th>产品名称</th>
						<th>贷款额度上限</th>
						<th>贷款额度下限</th>
						<th>产品序号</th>
						<th>进件渠道</th>
						<th>产品可申请门店</th>
						<th>创建日期</th>
						<th>修改日期</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<#list result as record>
						<tr>
							<td>${record_index+1}</td>
							<td>${record.prodName!}</td>
							<td>${record.loanUpLimit!}</td>
							<td>${record.loanDownLimit!}</td>
							<td>${record.prodIndex!}</td>
							<td><#if record.chanel==1>APP<#elseif record.chanel==2>线下<#else>APP&线下</#if></td>
							<td style=" max-width: 200px;text-overflow:ellipsis;white-space:nowrap;overflow: hidden;" title='${record.storeNames!}'>${record.storeNames!}</td>
							<td>${record.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
							<td>${record.updateTime?string('yyyy-MM-dd HH:mm:ss')}</td>
							<td>
							<a href="javascript:page_jump('${serverPath}/productAttribute/new_edit.htm?id=${record.id!}');">修改</a>
							&nbsp;
							&nbsp;
							<a href="javascript:deleteInfo(${record.id!})">删除</a>
							</td>
						<tr>
					</#list>
				</tbody>
			</table>
			</div>
	</div>
	
	
<#include "/sys/bottom.ftl">