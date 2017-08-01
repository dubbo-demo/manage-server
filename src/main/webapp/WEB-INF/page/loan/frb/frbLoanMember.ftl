 <#include "/sys/top.ftl"> <#include "/sys/left.ftl">
<script>
$(function(){
});

function down(status){
    ChkUtil.form_trim($("#searchForm"));
    ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
    var url = serverPath + "/frb/loanMember/exportFinanceInfo.htm?status=" + status;
    $("#searchForm").attr("action", url);
    $("#searchForm").submit();
    url = serverPath + "/frb/loanMember/queryPageList.htm";
    $("#searchForm").attr("action", url);
}
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
					<li><a href="#">付融宝借款人信息</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
			<form id="searchForm" class="form-horizontal" method="post">
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
                             data-date="${(params.startLoanTime?string("
						yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd"
                             data-date-viewmode="years">
                            <input name="startLoanTime" class="m-wrap span8 "
                                   size="16" type="text"
                                   value="${(params.startLoanTime?string(" yyyy-MM-dd"))!}"/><span
                                class="add-on"><i class="icon-calendar"></i></span>
                        </div>
                        <span style="margin-left: -28px">--</span>
                        <div class="input-append date date-picker"
                             data-date="${(params.endLoanTime?string("
						yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd"
                             data-date-viewmode="years">
                            <input name="endLoanTime" class="m-wrap span8 "
                                   size="16" type="text"
                                   value="${(params.endLoanTime?string(" yyyy-MM-dd"))!}" /><span
                                class="add-on"><i class="icon-calendar"></i></span>
                        </div>
                    </div>
                </div>

			<@p.pageForm value=page  type="sort"/>
				<p>
					<button type="submit" class="btn blue">查询</button>
					<a href="javascript:down(0)" class="btn blue">下载</a>
				</p>
			</form>
			</div>
			<!-- table -->
			<div class="tabbable tabbable-custom tabbable-custom-profile">
				<table class="table table-bordered table-hover">
					<thead>
						<tr>
							<th style="width:4%">序号</th>
                            <th style="width:10%">合同号</th>
                            <th style="width:10%">身份证号码</th>
                            <#--<th style="width:5%">用户状态</th>-->
                            <#--<th>是否已认证</th>-->
                            <th style="width:5%">姓名</th>
                            <th style="width:4%">性别</th>
                            <th style="width:4%">年龄</th>
                            <#--<th>国籍</th>-->
                            <th style="width:5%">省份</th>
                            <th style="width:5%">城市</th>
                            <th style="width:5%">公司性质</th>
                            <th style="width:5%">职业身份</th>
                            <th style="width:10%">工作地点</th>
                            <th style="width:10%">经营主业</th>
                            <#--<th style="width:5%">房产</th>-->
                            <#--<th style="width:5%">车产</th>-->
                            <th style="width:5%">月入范围/元</th>
                            <th style="width:5%">工作年限</th>
                            <th style="width:5%">帐户名</th>
                            <th style="width:5%">开户行</th>
                            <th style="width:5%">帐号</th>
                            <#--<th>借款金额</th>-->
                            <#--<th>还款方式</th>-->
                            <#--<th>借款时长</th>-->
                            <#--<th>借款时长单位</th>-->
                            <#--<th>借款描述</th>-->
                            <#--<th>借款用途</th>-->
                            <#--<th>保障方式</th>-->
                            <#--<th>服务费(元)</th>-->
                            <#--<th>放款金额(元)</th>-->
                            <#--<th>放款时间</th>-->

						</tr>
					</thead>
					<tbody>
						<#list page.result as record>
						<tr>
							<td>${record_index+1}</td>
                            <td>${record.contractNo!}</td>
                            <td>${record.idCard!}</td>
                            <#--<td>${record.userState!}</td>-->
                            <#--<td>${record.isAuthen!}</td>-->
                            <td>${record.memberName!}</td>
                            <td>${record.sex!}</td>
                            <td>${record.age!}</td>
                            <#--<td>${record.nationality!}</td>-->
                            <td>${record.liveProv!}</td>
                            <td>${record.liveCity!}</td>
                            <td>${record.companyNatureName!}</td>
                            <td>${record.positionsName!}</td>
                            <td>${record.jobDetailAddr!}</td>
                            <td>${record.businessTypeName!}</td>
                            <#--<td>${record.huoseCondition!}</td>-->
                            <#--<td>${record.carCondition!}</td>-->
                            <td>${record.monthlySalaryArea!}</td>
                            <td>${record.jobYear!}<#if record.jobYear??>年</#if></td>
                            <td>${record.openBankMember!}</td>
                            <td>${record.bankTypeName!}</td>
                            <td>${record.bankCardNo!}</td>
                            <#--<td>${record.contractAmount!}</td>-->
                            <#--<td>${record.payType!}</td>-->
                            <#--<td>${record.periods!}</td>-->
                            <#--<td>${record.timeUnit!}</td>-->
                            <#--<td>${record.loanPurposes!}</td>-->
                            <#--<td>${record.loanPurposeName!}</td>-->
                            <#--<td>${record.guaranteeMode!}</td>-->
                            <#--<td>${record.serviceRate!}</td>-->
                            <#--<td>${record.repayMoney!}</td>-->
                            <#--<td>${record.loanTime?string('yyyy-MM-dd HH:mm:ss')}</td>-->
						<tr></#list>
					</tbody>
				</table>
				<@p.pagination value=page /> 
			</div>
		</div>
	</div>
</div>

<#include "/sys/bottom.ftl">
