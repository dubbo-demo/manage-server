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
					<li><i class="icon-home"></i> <a href="${ctx}/index.htm">首页</a> <i
						class="icon-angle-right"></i></li>
					<li><a href="#">分公司业务管理</a><i class="icon-angle-right"></i></li>
					<li><a href="#">申请件管理</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
				<form id="searchForm" action="${ctx}/apply/manage/list.htm" class="form-horizontal" method="post">
					<@p.pageForm value=page!  type="sort"/>
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">申请单编号：</label> <input
								type="text" class="m-wrap span8" name="applyLoanNo"
								value="${(queryDto.applyLoanNo)!''}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">客户：</label> <input
								type="text" class="m-wrap span8" name="memberName"
								value="${(queryDto.memberName)!''}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">门店：</label> <input
								type="text" class="m-wrap span8" readonly="true" value="${(storeName)!''}">
						</div>
					</div>
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">业务经理：</label> 
							<!--<input	type="text" class="m-wrap span8" name="bmId" value="${(queryDto.bmId)!''}">-->
							<input type="text" class="m-wrap span8" id='bmName' name="bmName" value="${(queryDto.bmName)!}"/>
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">客服：</label> 
							<!--<input	type="text" class="m-wrap span8" name="customerServiceId"	value="${(queryDto.customerServiceId)!''}">-->
							<input type="text" id='customerSName' readonly="true" name="customerSName" class="m-wrap span8" value="${(queryDto.customerSName)!}"/>
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">进件日期：</label> 
									<div class="input-append date date-picker" data-date="${(queryDto.applyTimeStart?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="applyTimeStart" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.applyTimeStart?string('yyyy-MM-dd'))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(queryDto.applyTimeEnd?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="applyTimeEnd" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.applyTimeEnd?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
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
							<@p.sort field="applyLoanNo" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="申请单号" ></@p.sort>
							<th>客户</th>
							<th>手机号</th>
							<th>业务经理</th>
							<th>门店</th>
							<@p.sort field="createTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="进件日期" ></@p.sort>
							<th>客服</th>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if page??&&page.result??>
							<#list page.result as item>
							<#if item.subState??&&item.subState==1041>
							<tr class="odd gradeX pink" id="${item.id}">
							<#else>
							<tr class="odd gradeX" id="${item.id}">
							</#if>
								<td>${item_index+1 }</td>
								<td>${item.applyLoanNo!}</td>
								<td>${item.memberName!}</td>
								<td>${item.phone!}</td>
								<td>${item.bmName!}</td>
								<td>${item.storeName!}</td>
								<td><#if item.createTime??>${item.createTime?datetime}</#if></td>
								<td>${item.customerSName!""}</td>
								<td>${item.stateDesc}</td>
								<td style="width:200px">
									<#if item.state??>
										<#if item.state != 21 && item.state != 20>
											<#if item.subState??>
											&nbsp;&nbsp;&nbsp;
												<#if item.subState == 1001>
												<a class="blue" href="${ctx}/apply/newInfoIndex.htm?applyLoanNo=${item.applyLoanNo!''}">申请单录入</a>
												<#elseif item.subState == 1002>
												<a class="blue"  href="${ctx}/apply/newInfoIndex.htm?applyLoanNo=${item.applyLoanNo!''}">申请单录入</a>
												<#elseif item.subState == 1003>
												<a class="blue"  href="${ctx}/apply/newInfoIndex.htm?applyLoanNo=${item.applyLoanNo!''}">申请单录入</a>
												<#elseif item.subState == 1004>
												<a class="blue"  href="${ctx}/apply/newInfoIndex.htm?applyLoanNo=${item.applyLoanNo!''}">申请单录入</a>
												<#elseif item.subState == 1006>
												<a class="blue"  href="${ctx}/apply/newInfoIndex.htm?applyLoanNo=${item.applyLoanNo!''}">申请单录入</a>
												<#elseif item.subState == 1007>
												<a class="blue"  href="${ctx}/apply/newInfoIndex.htm?applyLoanNo=${item.applyLoanNo!''}">申请单录入</a>
												<#elseif item.subState == 1041>
												<a class="blue" href="${ctx}/apply/applyDetailBack.htm?applyLoanNo=${item.applyLoanNo!''}"> 回退处理</a>
												<#else>
												</#if>
												&nbsp;&nbsp;&nbsp;<a class="blue" href="#" onclick="abandonMain('${(item.applyLoanNo)!}')" data-toggle="modal"> 客户放弃</a>
												&nbsp;&nbsp;&nbsp;<a class="blue" href="#" onclick="refuseMain('${(item.applyLoanNo)!}')" data-toggle="modal"> 拒绝</a>
											</#if>		
										</#if>
									<#else>
									</#if>
								</td>
							</tr>
							</#list>
						</#if>
					</tbody>
				</table>
				<@p.pagination value=page! />
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
						<textarea  id="interiorRemark" rows="4" class="span10 m-wrap  js_input"></textarea>
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
					<div class="control-group ">
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
	$(function(){
		$("select[name='state']").val(${(queryDto.state)!-1});
	})
	
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
		var callBackURL = serverPath + "/apply/manage/list.htm";
		$.ajax({
			url : serverPath + "/apply/manage/storeRefuse.htm",
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
		var callBackURL = serverPath + "/apply/manage/list.htm";
		$.ajax({
			url : serverPath + "/apply/manage/abandon.htm",
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
	
	function search() {
		ChkUtil.form_trim($("#searchForm"));
		ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
		$("#searchForm").submit();
	}
	
	function page_jump(url){
		ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
		window.location.href=url;
	}
	
	$(function(){
		var bmurl="${ctx}/employee/showBMEmpoyee.htm";
		$("#bmId").select2({
		                placeholder: ChkUtil.select2Name,
		                allowClear: true,
		                minimumInputLength: 1,
		                query: function (query) {
		                	$.getJSON(bmurl, {"nameSpell" : query.term}, function(result) {
		                		var data = {
		                            results: result.data
		                        };
		                		query.callback(data);
		        			});
		                }
		 }).on("change",function(e){
			 var select_text="";
			 if(e.val){
			 	select_text=$("#bmId").select2("data").text;
			 }	
			 $("#bmName").val(select_text);
		 });
		 
		 var bmId="${(queryDto.bmId)!}";
		 if(bmId!=""){
			 $("#bmId").select2("data",{id:"${(queryDto.bmId)!}",text:"${(queryDto.bmName)!}"});
			 $("#bmName").val("${(queryDto.bmName)!}");
		 }
		 
		var custmerurl = serverPath+"/employee/showCustomerEmpoyee.htm";
		$("#customerServiceId").select2({
            placeholder: ChkUtil.select2Name,
            allowClear: true,
            minimumInputLength: 1,
            query: function (query) {
            	$.getJSON(custmerurl, {"nameSpell" : query.term}, function(result) {
            		var data = {
                        results: result.data
                    };
            		query.callback(data);
    			});
            }
		 }).on("change",function(e){
			 var select_text="";
			 if(e.val){
			 	select_text=$("#customerServiceId").select2("data").text;
			 }	
			 $("#customerSName").val(select_text);
		 });
		  
		 var customerServiceId="${(queryDto.customerServiceId)!}";
		 if(customerServiceId!=""){
			 $("#customerServiceId").select2("data",{id:"${(queryDto.customerServiceId)!}",text:"${(queryDto.customerSName)!}"});
			 $("#customerSName").val("${(queryDto.customerSName)!}");
		 } 		 
		  
	 })	
</script>
