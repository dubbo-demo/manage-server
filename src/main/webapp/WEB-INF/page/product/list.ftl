
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
							<a href="#">产品管理</a>
						</li>
					</ul>
				</div>
			</div>
		<div>
		<div>
					<form id="searchForm" class="form-horizontal" action="${serverPath}/product/queryPageList.htm"
					method="post">
					<@p.pageForm value=page  type="sort"/>
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">产品名称：</label> 
								<select name="productType" data-value="${(productType)!""}">
									<option value="0">请选择</option>
								</select>
						</div>
					</div>
					<p>
						<a href="${serverPath}/product/new_edit.htm" class="btn blue">新增</a>
						<a href="${serverPath}/productFile/productFileManage.htm" target="_blank" class="btn blue">产品附件管理</a>
						<button type="submit" class="btn blue">查询</button>
					</p>
				</form>
		</div>
			<!-- table -->
			<div class="tabbable tabbable-custom tabbable-custom-profile">
			<table class="table table-bordered table-hover table-condensed">
				<thead>
					<tr>
						<th>序号</th>
						<th>产品编号</th>
						<th>产品名称</th>
						<th>贷款期数</th>
						<th>期数单位</th>
						<th>结合费率</th>
						<th>贷款额度上限</th>
						<th>贷款额度下限</th>
						<th>逾期天数</th>
						<th>罚息</th>
						<th>利率/月</th>
						<th>状态</th>
						<th>创建日期</th>
						<th>修改日期</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<#list page.result as record>
						<tr>
							<td>${record_index+1}</td>
							<td>${record.prodCode!}</td>
							<td>${record.prodName!}</td>
							<td>${record.periods!}</td>
							<td>${record.periodsUnitName!}</td>
							<td>${(record.serviceRate*100)!""}%</td>
							<td>${record.loanUpLimit!}</td>
							<td>${record.loanDownLimit!}</td>
							<td>${record.overdueDays!}</td>
							<td>${record.penaltyRate*100!}%</td>
							<td>${record.interestRate*100!}%</td>
							<td>${record.stateName!}</td>
							<td>${record.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
							<td>${record.updateTime?string('yyyy-MM-dd HH:mm:ss')}</td>
							<td>
							<a href="javascript:page_jump('${serverPath}/product/new_edit.htm?id=${record.id!}');">修改</a>
							&nbsp;
							&nbsp;
							<a href="javascript:deleteInfo(${record.id!})">删除</a>
							</td>
						<tr>
					</#list>
				</tbody>
			</table>
			<@p.pagination value=page />
			</div>
	</div>
	
	
<#include "/sys/bottom.ftl">