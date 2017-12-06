<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<#--<script src="${cdnPath}/js/reception/list.js?v=${VERSION_NO}"></script>-->
<script>
	var orgType="${orgType!}"; //组织类型1:大区 2：总部 3：门店
	var regionId="${regionId!}";//大区id
	function page_jump(url){
        ChkUtil.form_trim($("#searchForm"));
        $("#searchForm").attr("action", url);
        $("#searchForm").submit();
        $("#searchForm").attr("action", "${ctx}/hKBillRecord/list.htm");
	}
    function search() {
        ChkUtil.form_trim($("#searchForm"));
        $("#searchForm").attr("action", "${ctx}/hKBillRecord/list.htm");
        $("#searchForm").submit();
    }
    function form_trim(){
        var $input=$("#searchForm input[type='text']");
        $input.each(function(i,n){
            var value = $(n).val();
            $(n).val($.trim(value));
        })
    }
    $(function() {
        $("#state").val(${queryDto.state!});
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
								<li> <i class="icon-home"></i>
									<a href="${serverPath}/index.htm">首页</a> <i class="icon-angle-right"></i> </li>
								<li>
									<a href="javascript:void(0)">财务管理</a><i class="icon-angle-right"></i>
								</li>
								<li>
									<a href="#">扣款记录</a>
								</li>
							</ul>
						</div>
					</div>
				<div>
				<div class="portlet-body form">
							<form action="${serverPath}/hKBillRecord/list.htm" id="searchForm" class="form-horizontal" method="post">
							<@p.pageForm value=page  type="sort"/>
								<input type="hidden" id="pageIndex" name="pageIndex" value='1'/>
								<div class="row-fluid">
									<div class="control-group span4 ">
										<label class="help-inline text-right span4">合同号：</label>
										<input type="text" class="m-wrap span5" name="contractNo" value="${(queryDto.contractNo)!''}">
									</div>
									<div class="control-group span4 ">
										<label class="help-inline text-right span4">客户身份证号：</label>
										<input type="text" class="m-wrap span5" name="idCardNo" value="${(queryDto.idCardNo)!''}">
									</div>
									<div class="control-group span4 ">
										<label class="help-inline text-right span4">状态：</label>
										<select class="m-wrap span5" id="state" name="state">
                                            <option value="">请选择</option>
										<#if states?? >
											<#list states?values as item>
                                                <option value="${item.getCode()!}">${item.getDesc()!}</option>
											</#list>
										</#if>
										</select>
									</div>
								</div>
                                <div class="row-fluid">
                                    <div class="control-group span4 ">
                                        <label class="help-inline text-right span4">组织：</label>
                                        <select class="m-wrap span5" id="storeId" name="storeId" value="">
                                            <option value="">请选择</option>
										<#if orgs??>
											<#list orgs as o>
                                                <option value="${o.id}" <#if queryDto.storeId?? && o.id == queryDto.storeId>selected</#if>>${o.orgName}</option>
											</#list>
										</#if>
                                        </select>
                                    </div>
                                    <div class="control-group span4 ">
                                        <label class="help-inline text-right span4">代扣日期：</label>
                                        <div class="input-append date date-picker" data-date="${(queryDto.beginPayTime?string('yyyy-MM-dd'))!}"
                                             data-date-format="yyyy-mm-dd" data-date-viewmode="years">
                                            <input name="beginPayTime" class="m-wrap span8 date-picker" size="16" type="text"
                                                   data-date-format="yyyy-mm-dd"  value="${(queryDto.beginPayTime?string('yyyy-MM-dd'))!}"/>
                                            <span class="add-on"><i class="icon-calendar"></i></span>
                                        </div>
                                        <span style="margin-left:-28px">--</span>
                                        <div class="input-append date date-picker" data-date="${(queryDto.endPayTime?string('yyyy-MM-dd'))!}"
                                             data-date-format="yyyy-mm-dd" data-date-viewmode="years">
                                            <input name="endPayTime" class="m-wrap span8 date-picker" size="16" type="text"
                                                   data-date-format="yyyy-mm-dd"  value="${(queryDto.endPayTime?string('yyyy-MM-dd'))!}" />
                                            <span class="add-on"><i class="icon-calendar"></i></span>
                                        </div>
                                    </div>
                                    <div class="control-group span4 ">
                                    </div>
                                </div>
								<div class="row-fluid">
									<div class="control-group span4 ">
										<button type="button" onclick="search()" class="btn blue">查询</button>
										<a class="btn blue" href="javascript:page_jump('${serverPath}/hKBillRecord/exportPayRecordInfo.htm')">下载</a>
									</div>
								</div>
							</form>
				</div>	

	<div class="tabbable tabbable-custom tabbable-custom-profile">
		<!-- table -->
		<table class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th>序号</th>
                    <th>合同编号</th>
                    <th>期数</th>
                    <th>账单编号</th>
					<th>扣款类型</th>
					<th>扣款金额</th>
					<th>账户名</th>
					<th>开户行</th>
                    <th>卡号</th>
                    <th>手机号</th>
					<th>身份证号</th>
                    <th>发起人</th>
                    <th>提前结清</th>
                    <th>扣款日期</th>
                    <th>状态</th>
                    <th>备注</th>
				</tr>
			</thead>
			<tbody>
			<#if page?? && page.result?? >
					<#list page.result as item>
						<tr class="odd gradeX" id="${item.id!}">
							<td>${item_index+1 }</td>
							<td>${item.contractNo! }</td>
							<td>${item.repayPeriodName!"" }</td>
							<td>${item.billNo! }</td>
                            <td>${item.payTypeName! }</td>
                            <td>${item.payAmount! }</td>
							<td>${(item.username)!""}</td>
							<td>${(item.bankName)!""}</td>
                            <td>${(item.idBankNo)!""}</td>
                            <td>${(item.reservedPhone)!""}</td>
                            <td>${(item.idCardNo)!""}</td>
                            <td>${(item.createUser)!""}</td>
							<td>${(item.isAdvanceSettleName)!""}</td>
							<td>${item.createTime?datetime}</td>
                            <td>${(item.stateName)!""}</td>
                            <td><a data-toggle="modal" href="#refuse"
                                                       data-billdesc="${item.payDesc!""}">备注</a></td>
						</tr>
					</#list>
			
					<tr>
					<td colspan="16" align="center">
						<@p.pagination value=page />
		            </td>
					</tr>
			</#if>
			</tbody>
		</table>
	</div>
</div>
<div id="refuse" class="modal hide fade" tabindex="-1" data-width="760">
			<input type="hidden" id="rid" value=""/>
			<input type="hidden" id="rstate" value=""/>
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
					<h4 class="refuseDivTitle">备注</h3>
					<style>
				  	.refuseDivTitle {text-align:center;font-weight:bold}
				  	</style>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="control-group span10">
						<label class="help-inline text-right span3"></label>
						<div class="controls">
						<textarea id="description" rows="4" class="m-wrap span9"></textarea>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" data-dismiss="modal" class="btn">取消</button>
			</div>
</div>
<script type="text/javascript">
	$(function() {
        $("*[data-billdesc]").click(function (e) {
            var billdesc = $(this).data("billdesc");
            $("#description").html(billdesc);
        });
	});
</script>
<#include "/sys/bottom.ftl">