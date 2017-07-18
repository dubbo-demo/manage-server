<#include "/sys/top.ftl"> <#include "/sys/left.ftl">
<style>
.table-hover tbody tr.pink>td{
background-color: #f2dede
}
.table-hover tbody tr:hover.pink>td{
background: #f2dede
}
</style>
<div class="page-content">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<!--页面标题-->
				<h3 class="page-title"></h3>
				<!--面包屑导航-->
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="${serverPath}/index.htm">首页</a> <i
						class="icon-angle-right"></i></li>
					<li><a href="#">分公司业务管理</a><i class="icon-angle-right"></i></li>
					<li><a href="#">签约管理</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
				<form id="searchForm" action="${serverPath}/sign/list.htm" class="form-horizontal" method="post">
					<@p.pageForm value=page  type="sort"/>
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">申请单编号：</label>
							<div>
							 <input	type="text" class="m-wrap span8" name="applyLoanNo"
								value="${(queryDto.applyLoanNo)!''}">
							</div>
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">客户：</label>
							<div> <input
								type="text" class="m-wrap span8" name="memberName"
								value="${(queryDto.memberName)!''}">
							</div>
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">手机号：</label>
							<div>
								<input
								type="text" class="m-wrap span8" name="phone"
								value="${(queryDto.phone)!''}">
							</div>																	
						</div>
					</div>
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">门店：</label> 
							<div>
                                <select data-placeholder="Your Favorite Football Team" name="storeId" class="chosen span6 chzn-done" tabindex="-1">
                                    <option value="">请选择</option>
									<#if orgs??>
									    <#list orgs as o>
                                            <option value="${o.id}" <#if queryDto.storeId?? && o.id == queryDto.storeId>selected</#if>>${o.orgName}</option>
									    </#list>
									</#if>

								</select>
								<#--<input type="text" class="m-wrap span8" readonly="true" value="${(storeName)!''}">-->
							</div>
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">客服：</label> 
							<div>
								<input type="text" class="m-wrap span8" id='customerServiceName' name="customerServiceName" value=""/>
							</div>								
						</div>
					</div>
					
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">进件日期：</label> 
							<div>
									<div class="input-append date date-picker" data-date="${(queryDto.applyTimeStart?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="applyTimeStart" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.applyTimeStart?string('yyyy-MM-dd'))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(queryDto.applyTimeEnd?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="applyTimeEnd" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.applyTimeEnd?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>	
							</div>														
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">审核通过日期：</label>
							<div>
									<div class="input-append date date-picker" data-date="${(queryDto.passTimeStart?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="passTimeStart" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.passTimeStart?string('yyyy-MM-dd'))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(queryDto.passTimeEnd?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="passTimeEnd" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.passTimeEnd?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>	
							</div>														
						</div>
					</div>
					<div class="row-fluid">
						<div class="control-group span4 ">
							<button class="btn blue" onclick="search()">查询</button>
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
							<@p.sort field="a.applyLoanNo" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="申请单号" ></@p.sort>
							<th>客户</th>
							<th>手机号</th>
							<th>业务经理</th>
							<th>大区</th>
							<@p.sort field="a.createTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="进件日期" ></@p.sort>
							<@p.sort field="t.auditReviewTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="审核通过日期" ></@p.sort>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if page??&&page.result??>
							<#list page.result as item>
							<#if item.stateDesc??&&item.stateDesc?contains("退回")>
							<tr class="odd gradeX pink" style="background-color:yellow;" id="${item.id}">
							<#else>
							<tr class="odd gradeX" id="${item.id}">
							</#if>
								<td>${item_index+1 }</td>
								<td>${item.applyLoanNo!}</td>
								<td>${item.memberName!}</td>
								<td>${item.phone!}</td>
								<td>${item.bmName!}</td>
								<td>${item.areaName!}</td>
								<td><#if item.createTime??>${item.createTime?datetime}</#if></td>
								<td><#if item.passTime??>${item.passTime?datetime}</#if></td>
								<td>${item.stateDesc!}</td>
								<td>
								<#if item.state == 15 && item.subState == 1501 >
									<a href="javascript:page_jump('${serverPath}/sign/goSign.htm?applyLoanNo=${(item.applyLoanNo)!}&&subState=${(item.subState)!}')">签约</a>
								</#if>
								<#if item.state == 15 && item.subState == 1510 >
									<a href="javascript:page_jump('${serverPath}/sign/goSign.htm?applyLoanNo=${(item.applyLoanNo)!}&&subState=${(item.subState)!}')">回退处理</a>
								</#if>
								<a class="blue" href="#" onclick="abandonMain('${(item.applyLoanNo)!}')" data-toggle="modal"> 客户放弃</a>
									<a class="blue" href="#" onclick="refuseMain('${(item.applyLoanNo)!}')" data-toggle="modal"> 拒绝</a>	
								</td>
							</tr>
							</#list>
						</#if>
					</tbody>
				</table>
				<@p.pagination value=page />
			</div>
		</div>
		<div id="abandon" class="modal hide fade" tabindex="-1" data-width="760">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
					<h4 class="refuseDivTitle">客户放弃</h4>
					<style>
				  	.refuseDivTitle {text-align:center;font-weight:bold}
				  	</style>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="control-group">
						<label class="help-inline text-right span2">说明：</label>
						<div class="controls">
						<textarea  id="interiorRemark" rows="4" class="span10 m-wrap js_input"></textarea>
						</div>
					</div>
				</div>
				<input id = "applyLoanNoId" type = "hidden" value = ""/>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn blue" onclick="abandon();">确定</button>
				<button type="button" data-dismiss="modal" class="btn">取消</button>
			</div>
		</div>
		
		
		<div id="refuse" class="modal hide fade" tabindex="-1" data-width="760">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
					<h4 class="refuseDivTitle">门店拒绝</h4>
					<style>
				  	.refuseDivTitle {text-align:center;font-weight:bold}
				  	</style>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="control-group">
						<label class="help-inline text-right span2">说明：</label>
						<div class="controls">
						<textarea  id="interiorRemark" rows="4" class="span10 m-wrap js_input"></textarea>
						</div>
					</div>
				</div>
				<div class="row-fluid">
						<div class="control-group">
						<label class="help-inline text-right span2"></label>
							<div class="controls">
								<label class="help-inline text-right" style="color:red;">注：门店拒绝后，90天内不得再次申请借款！</label>
							</div>
						</div>
				</div>
				<input id = "applyLoanNoId" type = "hidden" value = ""/>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn blue" onclick="storeRefused();">确定</button>
				<button type="button" data-dismiss="modal" class="btn">取消</button>
			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
	var orgType="${(user.orgType)!}"; //组织类型1:大区 2：总部 3：门店
	var regionId="${(empDetail.regionId)!}";//大区id
	var storeId="${(empDetail.storeId)!}";//门店id
	$(function(){
		$("select[name='subState']").val(${(queryDto.subState)!-1});
	})
	function search() {
		ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").attr("action", "${serverPath}/sign/list.htm");
		$("#searchForm").submit();
	}
	function goPage(pageNumber) {
	    ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").attr("action", "${serverPath}/sign/list.htm");
	    $("#pageIndex").val(pageNumber);
	    $("#searchForm").submit();
	}
	function page_jump(url){
		ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
		window.location.href=url;
	}
	
	function refuseMain(applyLoanNo) {
		$("#applyLoanNoId").val('');
		$("#applyLoanNoId").val(applyLoanNo);
		$("#refuse").modal('show');
	}
	
	function storeRefused() {
		var f = false;
		$("#refuse .js_input").each(function(i,v){
			var _val = $(this).val();
			if(_val == "" || _val == -1){
				$(this).addClass("error");
				f = true;
			}else{
				$(this).removeClass("error");
			}
		});
		if(f) {
			return false;
		}
		
		var applyLoanNo = $("#applyLoanNoId").val();
		var callBackURL = serverPath + "/sign/list.htm";
		$.ajax({
			url : serverPath + "/sign/signRefuse.htm",
			type : "post",
			data : {
				"applyLoanNo" : applyLoanNo,
				"interiorRemark" :$("#interiorRemark").val(),
				"Time" : new Date().getMilliseconds()
			},
			dataType : "json",
			success : function(result) {
				if (result.success) {
					window.location.href = callBackURL;
				} else {
					BootstrapDialog.alert(result.message);
				}
			},
			error : function() {
				BootstrapDialog.alert("操作失败");
			}
		});
	}
	
	function abandonMain(applyLoanNo) {
		$("#applyLoanNoId").val('');
		$("#applyLoanNoId").val(applyLoanNo);
		$("#abandon").modal('show');
	}
	
	function abandon() {
		var f = false;
		$("#abandon .js_input").each(function(i,v){
			var _val = $(this).val();
			if(_val == "" || _val == -1){
				$(this).addClass("error");
				f = true;
			}else{
				$(this).removeClass("error");
			}
		});
		if(f) {
			return false;
		}
	
		var applyLoanNo = $("#applyLoanNoId").val();
		var callBackURL = serverPath + "/sign/list.htm";
		$.ajax({
			url : serverPath + "/sign/abandon.htm",
			type : "post",
			data : {
				"applyLoanNo" : applyLoanNo,
				"interiorRemark" :$("#interiorRemark").val(),
				"Time" : new Date().getMilliseconds()
			},
			dataType : "json",
			success : function(result) {
				if (result.success) {
					window.location.href = callBackURL;
				} else {
					BootstrapDialog.alert(result.message);
				}
			},
			error : function() {
				BootstrapDialog.alert("操作失败");
			}
		});
	}
	 
	 $(function(){
	 		selectStoreId();
	});
	function selectStoreId() {
		var url = "${serverPath}/organization/selectOrgByOrgType.htm";
		var data = {
			"orgType" : 3,
			"Time" : new Date().getMilliseconds()
		};
		$.ajaxSettings.async = true;
		$.getJSON(url, data, function(result) {
			var resultData = result.data;
			$("#storeId").append("<option value='0'>请选择</option>");
			for (var i = 0; i < resultData.length; i++) {
				$("#storeId").append("<option value='"+resultData[i].id+"'>"+resultData[i].orgName+"</option>");
			}
		});
    }
</script>