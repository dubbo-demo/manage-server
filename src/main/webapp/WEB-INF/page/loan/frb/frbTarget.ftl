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
					<li><a href="#">付融宝标的信息</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
			<form id="searchForm" class="form-horizontal" method="post">
				<@p.pageForm value=page type="sort"/>
				<div class="row-fluid">
					<div class="control-group span4 ">
						<label class="help-inline text-right span4">合同号：</label> <input
							type="text" class="m-wrap span8" name="contractNo"
							value="${(params.contractNo)!""}">
					</div>
					<div class="control-group span4 ">
						<label class="help-inline text-right span4">身份证：</label> <input
							type="text" class="m-wrap span8" name="idCard"
							value="${(params.idCard)!""}">
					</div>
					<div class="control-group span4 ">
						<label class="help-inline text-right span4">放款日期：</label>

						<div class="input-append date date-picker"
							data-date="${(params.loanDates?string("
							yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd"
							data-date-viewmode="years">
							<input name="loanDates" class="m-wrap span8 date-picker"
								size="16" type="text" data-date-format="yyyy-mm-dd"
								value="${(params.loanDates?string(" yyyy-MM-dd"))!}"/><span
								class="add-on"><i class="icon-calendar"></i></span>
						</div>
						<span style="margin-left: -28px">--</span>
						<div class="input-append date date-picker"
							data-date="${(params.loanDatee?string("
							yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd"
							data-date-viewmode="years">
							<input name="loanDatee" class="m-wrap span8 date-picker"
								size="16" type="text" data-date-format="yyyy-mm-dd"
								value="${(params.loanDatee?string(" yyyy-MM-dd"))!}" /><span
								class="add-on"><i class="icon-calendar"></i></span>
						</div>
					</div>

				</div>
				<p>
					<button type="submit" class="btn blue">查询</button>
					<a href="javascript:down()" class="btn blue">下载</a>
				</p>
			</form>
			</div>
			<!-- table -->
			<div class="tabbable tabbable-custom tabbable-custom-profile">
				<table class="table table-bordered table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th>合同编号</th>
							<th>身份证号码</th>
							<th>借款金额</th>
							<th>还款方式</th>
							<th>借款时长</th>
							<th>借款时长单位</th>
							<th>借款描述</th>
							<th>借款用途</th>
							<th>保障方式</th>
							<th>服务费（代扣金额）</th>
							<th>放款金额</th>
						</tr>
					</thead>
					<tbody>
						<#list page.result as record>
						<tr>
							<td>${record_index+1}</td>
							<td>${record.contractNo!}</td>
							<td>${record.idCard!}</td>
							<td>${record.contractAmount!}</td>
							<td>等额本息</td>
							<td>${record.periods!}</td>
							<td>${record.periodsUnit!}</td>
							<td>${record.loanPurposes!}</td>
							<td>现金贷</td>
							<td>征信信用</td>
							<td>${record.serviceRate!}</td>
							<td>${record.repayMoney!}</td>
						<tr></#list>
					</tbody>
				</table>
				<@p.pagination value=page /> 
			</div>
		</div>
	</div>
</div>

<#include "/sys/bottom.ftl">
