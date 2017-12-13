 <#include "/sys/top.ftl"> <#include "/sys/left.ftl">
<script>
$(function(){
	$.uniform.update($(".checkboxitem").prop("checked", false));
});
</script>

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
					<li><a href="#">放款管理</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
			<form id="searchForm" class="form-horizontal" method="post">
				<@p.pageForm value=page type="sort"/>
				<div class="row-fluid">
					<div class="control-group span4 ">
						<label class="help-inline text-right span4">申请单编号：</label> <input
							type="text" class="m-wrap span8" name="applyLoanNo"
							value="${(params.applyLoanNo)!""}">
					</div>
					<div class="control-group span4 ">
						<label class="help-inline text-right span4">合同编号：</label> <input
							type="text" class="m-wrap span8" name="contractNo"
							value="${(params.contractNo)!""}">
					</div>
					<div class="control-group span4 ">
						<label class="help-inline text-right span4">客户：</label> <input
							type="text" class="m-wrap span8" name="memberName"
							value="${(params.memberName)!""}">
					</div>
				</div>
				<div class="row-fluid">
					<div class="control-group span4 ">
						<label class="help-inline text-right span4">大区：</label> <select
							name="areaId" class="m-wrap span8" data-id="${(params.areaId)!"
							0"}" onchange="initStoreData()">
							<option value="0">请选择</option>
						</select>
					</div>
					<div class="control-group span4 " class="m-wrap span8">
						<label class="help-inline text-right span4">门店：</label> <select
							name="storeId" class="m-wrap span8" data-id="${(params.storeId)!"0"}">
							<option value="0">请选择</option>
						</select>
					</div>
					<div class="control-group span4 " class="m-wrap span8">
						<label class="help-inline text-right span4">产品类型： </label> <select
							name="productType" class="m-wrap span8"
							data-id="${(params.productType)!"0"}">
							<option value="0">请选择</option>
						</select>
					</div>
				</div>
				<div class="row-fluid">
					<div class="control-group span4 ">
						<label class="help-inline text-right span4">进件日期：</label>

						<div class="input-append date date-picker"
							data-date="${(params.submitDates?string("
							yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd"
							data-date-viewmode="years">
							<input name="submitDates" class="m-wrap span8 date-picker"
								size="16" type="text" data-date-format="yyyy-mm-dd"
								value="${(params.submitDates?string(" yyyy-MM-dd"))!}"/><span
								class="add-on"><i class="icon-calendar"></i></span>
						</div>
						<span style="margin-left: -28px">--</span>
						<div class="input-append date date-picker"
							data-date="${(params.submitDatee?string("
							yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd"
							data-date-viewmode="years">
							<input name="submitDatee" class="m-wrap span8 date-picker"
								size="16" type="text" data-date-format="yyyy-mm-dd"
								value="${(params.submitDatee?string(" yyyy-MM-dd"))!}" /><span
								class="add-on"><i class="icon-calendar"></i></span>
						</div>
					</div>

					<div class="control-group span4 ">
						<label class="help-inline text-right span4">签约日期：</label>

						<div class="input-append date date-picker"
							data-date="${(params.signDates?string("
							yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd"
							data-date-viewmode="years">
							<input name="signDates" class="m-wrap span8 date-picker"
								size="16" type="text" data-date-format="yyyy-mm-dd"
								value="${(params.signDates?string(" yyyy-MM-dd"))!}"/><span
								class="add-on"><i class="icon-calendar"></i></span>
						</div>
						<span style="margin-left: -28px">--</span>
						<div class="input-append date date-picker"
							data-date="${(params.signDatee?string("
							yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd"
							data-date-viewmode="years">
							<input name="signDatee" class="m-wrap span8 date-picker"
								size="16" type="text" data-date-format="yyyy-mm-dd"
								value="${(params.signDatee?string(" yyyy-MM-dd"))!}" /><span
								class="add-on"><i class="icon-calendar"></i></span>
						</div>
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
					<a href="javascript:loaned()" class="btn blue">放款</a>
					<a href="javascript:down(0)" class="btn blue">下载</a>
				</p>
			</form>
			</div>
			<!-- table -->
			<div class="tabbable tabbable-custom tabbable-custom-profile">
				<ul class="nav nav-tabs">
					<li name="tab" class="active"><a
						href="${serverPath}/loan/queryPageList.htm?status=0">待办</a></li>
					<li name="tab" class=""><a
						href="${serverPath}/loan/queryPageList.htm?status=1">已办</a></li>
				</ul>
				<table class="table table-bordered table-hover">
					<thead>
						<tr>
							<th><input type="checkbox" name="applyLoanNo" id="cekAll" />
								全选</th>
							<th>序号</th>
							<th>申请单号</th>
							<th>合同编号</th>
							<th>客户</th>
							<th>手机号</th>
							<th>放款日期</th>
							<th>合同金额</th>
							<th>服务费</th>
							<th>放款金额</th>
							<th>应收利息</th>
							<th>还款总额</th>
							<th>总还款期数</th>
							<th>产品名称</th>
							<th>门店</th>
							<th>业务经理</th>
							<th>团队经理</th>
							<th>客服</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#list page.result as record>
						<tr>
							<td><input type="checkbox" class="checkboxitem"
								data="${record.applyLoanNo}" value="${record.id}" /></td>
							<td>${record_index+1}</td>
							<td>${record.applyLoanNo!}</td>
							<td>${record.contractNo!}</td>
							<td>${record.memberName!}</td>
							<td>${record.phone!}</td>
							<td>${(record.loanTime?string('yyyy-MM-dd'))!}</td>

							<td>${record.contractAmount!}</td>
							<td>${record.serviceRate!}</td>
							<td>${record.repayMoney!}</td>
							<td>${record.interestAmount!}</td>
							<td>${record.repaymentAmount!}</td>
							<td>${record.loanPeriods!}</td>

							<td>${record.productName!}</td>
							<td>${record.storeName!}</td>
							<td>${record.bmName!}</td>
							<td>${record.bmName!}</td>
							<td>${record.customerSName!}</td>
							<td><#if record.status == 0> <a
								href="javascript:loanedId(${record.id},'${record.applyLoanNo}')">放款</a>
								</#if>
                                <a href="javascript:giveUp('${record.applyLoanNo}')">放弃</a>
							</td>
						<tr></#list>
					</tbody>
				</table>
				<@p.pagination value=page /> 
			</div>
		</div>
	</div>
</div>
<script src="${cdnPath}/js/apply/approve/approve.js?v=${VERSION_NO}"></script>
<#include "/sys/bottom.ftl">
