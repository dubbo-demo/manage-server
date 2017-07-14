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
					<li><a href="#" id="menu_level_1"></a><i class="icon-angle-right"></i></li>
					<li><a href="#">申请进度查看</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
				<form id="searchForm" action="${ctx}/apply/progress/list.htm" class="form-horizontal" method="post">
					<@p.pageForm value=page  type="sort"/>
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">申请单编号：</label>
							<div class="">
							 <input	type="text" class="m-wrap span8" name="applyLoanNo"
								value="${(queryDto.applyLoanNo)!''}">
							</div>
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">客户：</label>
							<div class=""> <input
								type="text" class="m-wrap span8" name="memberName"
								value="${(queryDto.memberName)!''}">
							</div>
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">状态：</label>
							<div class="">
										<select class="m-wrap span8" name="subState">
											<option value="-1">请选择</option>
											<#if stateEnum?? && stateEnum?size gt 0>
											<#list stateEnum?keys as key>
											<option value="${stateEnum[key].code}">${stateEnum[key].desc}</option>
											</#list>
											</#if>
										</select>	
							</div>																	
						</div>
					</div>
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">业务经理：</label> 
							<div class="">
							<!--<input type="hidden" id='bmId' name="bmId" class="m-wrap span8" />-->
							<input type="text" class="m-wrap span8" id='bmName' name="bmName" value="${(queryDto.bmName)!}"/>
							</div>
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">客服：</label> 
							<div class="">
								<!--<input type="hidden" id='customerServiceId' name="customerServiceId" class="m-wrap span8" />-->
							<input type="text" class="m-wrap span8" id='customerServiceName' name="customerServiceName" value="${(queryDto.customerServiceName)!}"/>
							</div>								
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">进件日期：</label> 
							<div class="">
									<div class="input-append date date-picker" data-date="${(queryDto.applyTimeStart?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="applyTimeStart" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.applyTimeStart?string('yyyy-MM-dd'))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(queryDto.applyTimeEnd?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="applyTimeEnd" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.applyTimeEnd?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>	
							</div>														
						</div>
					</div>	
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">大区：</label>
							<div class="">
							<select name="areaId" class="m-wrap span8" data-id="${(queryDto.areaId)!-1}" onchange="initStoreData()">
								<option value="-1">请选择</option>
							</select>
							</div>
						</div>
						<div class="control-group span4 " class="m-wrap span8">
							<label class="help-inline text-right span4">门店：</label> 
							<div class="">
							<select name="storeId" class="m-wrap span8" data-id="${(queryDto.storeId)!-1}">
								<option value="-1">请选择</option>
							</select>
							</div>
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">签约日期：</label> 
							<div class="">
									<div class="input-append date date-picker" data-date="${(queryDto.signTimeStart?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="signTimeStart" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.signTimeStart?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(queryDto.signTimeEnd?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="signTimeEnd" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.signTimeEnd?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>	
							</div>		
						</div>
					</div>	
					<p>
						<button id="searchBtn" type="button" class="btn blue" onclick="search()">查询</button>
						<button type="button" class="btn blue" onclick="export_excel()">下载</button>
					</p>
				</form>
			</div>

			<div class="tabbable tabbable-custom tabbable-custom-profile">
			
				<!-- table -->
				<table class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<@p.sort field="applyLoanNo" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="申请单号" ></@p.sort>
							<@p.sort field="contractNo" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="合同号" ></@p.sort>
							<th>客户</th>
							<th>手机号</th>
							<th>业务经理</th>
							<th>大区</th>
							<th>门店</th>
							<@p.sort field="createTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="进件日期" ></@p.sort>
							<th>申请金额</th>
							<th>批复产品</th>
							<th>批复金额</th>
							<th>放款金额</th>
							<@p.sort field="signTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="签约日期" ></@p.sort>
							<th>客服</th>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if page??&&page.result??>
							<#list page.result as item>
							<#if item.stateDesc??&&item.stateDesc?contains("退回")>
							<tr class="odd gradeX pink" id="${item.id}">
							<#else>
							<tr class="odd gradeX" id="${item.id}">
							</#if>
								<td>${item_index+1 }</td>
								<td>${item.applyLoanNo!}</td>
								<td>${item.contractNo!}</td>
								<td>${item.memberName!}</td>
								<td>${item.phone!}</td>
								<td>${item.bmName!}</td>
								<td>${item.areaName!}</td>
								<td>${item.storeName!}</td>
								<td><#if item.createTime??>${item.createTime?datetime}</#if></td>
								<td>${item.applyMoney!}</td>
								<td>${item.productName!}</td>
								<td>${item.auditRatifyMoney!}</td>
								<td><#if item.signTime??>${item.repayMoney!}</#if></td>
								<td><#if item.signTime??>${item.signTime?datetime}</#if></td>
								<td>${item.customerSName!""}</td>
								<td>${item.stateDesc!}</td>
								<td>
								<#if item.state != 10  || (item.subState == 1041)>
									&nbsp;&nbsp;&nbsp;<a href="javascript:page_jump('${ctx}/apply/progress/detailUI.htm?applyLoanNo=${(item.applyLoanNo)!}&&state=${(item.state)!}&&subState=${(item.subState)!}')">查看详情</a>
									&nbsp;&nbsp;&nbsp;<a href="javascript:viewfile('${item.applyLoanNo!}')">附件管理</a>
								</#if>	
								</td>
							</tr>
							</#list>
						</#if>
					</tbody>
				</table>
				<@p.pagination value=page />
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
	
		//获取一级菜单名
		var menu_level_1=$(".page-sidebar-menu li.active span.title").html().trim();
		$("#menu_level_1").html(menu_level_1);
		
		$("select[name='subState']").val(${(queryDto.subState)!-1});
	})
	function search() {
		ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").attr("action", "${ctx}/apply/progress/list.htm");
		$("#searchForm").submit();
	}
	function goPage(pageNumber) {
	    ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").attr("action", "${ctx}/apply/progress/list.htm");
	    $("#pageIndex").val(pageNumber);
	    $("#searchForm").submit();
	}
	function page_jump(url){
		ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
		//window.location.href=url;
		window.open(url);
	}
	
	function export_excel() {
		ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").attr("action", "${ctx}/apply/progress/export.htm");
		$("#searchForm").submit();
	}
	
	function viewfile(applyLoanNo){
		window.open("${ctx}/productFile/fileUpload.htm?isView=0&applyLoanNo="+applyLoanNo);
	}
	/**
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
			 $("#customerServiceName").val(select_text);
		 }); 
		 		
		 
		 var customerServiceId="${(queryDto.customerServiceId)!}";
		 if(customerServiceId!=""){
			 $("#customerServiceId").select2("data",{id:"${(queryDto.customerServiceId)!}",text:"${(queryDto.customerServiceName)!}"});
			 $("#customerServiceName").val("${(queryDto.customerServiceName)!}");
		 }
		 
		 var bmId="${(queryDto.bmId)!}";
		 if(bmId!=""){
			 $("#bmId").select2("data",{id:"${(queryDto.bmId)!}",text:"${(queryDto.bmName)!}"});
			 $("#bmName").val("${(queryDto.bmName)!}");
		 }
	 })	
	 **/
	 
	 $(function(){
			init();
	});
	function init(){
		$("#searchBtn").attr("disabled","disabled");
		//加载大区数据
		initAreaData();
	}
	function initAreaData(){
	var queryData ={"Time" : new Date().getMilliseconds()};
	var url=serverPath + "/organization/getRegionInfo.htm";
		$.ajax({
			url: url,
			type:"post",
			data: queryData,
			dataType:"json",
			success:function(result){
				//清空除第一条内容的外的其它数据
				var select_ = $("select[name='areaId']");
				select_.find("option:gt(0)").remove();
				if(orgType==2){
    				for (var i = 0; i < result.data.length; i++) {
    					var isSelected = result.data[i].id == select_.attr('data-id')?"selected='selected'":"";
    					select_.append(
    							"<option "+isSelected+" value='"
    									+ result.data[i].id + "'>"
    									+ result.data[i].orgName
    									+ "</option>");
    				}
				}else{
                    var isSelected = result.data.id == select_.attr('data-id')?"selected='selected'":"";
                    select_.append(
                            "<option "+isSelected+" value='"
                                    + result.data.id + "'>"
                                    + result.data.orgName
                                    + "</option>");
				}
				if(orgType!=2){
				 select_.find("option:eq(0)").remove();
				  select_.val(regionId);
				}
				initStoreData();
			},
			error:function(){
				alert("加载失败");
			}
		});
	}
	function initStoreData(){
		var parentId  =  $("select[name='areaId']").val();
		if(parentId == "0"){
			parentId = $("select[name='areaId']").attr('data-id');
		}
		if(parentId == "0"){
			return;
		}
		var queryData ={"id":parentId, "Time" : new Date().getMilliseconds()}
		var url=serverPath + "/organization/getStoreInfo.htm";
		$.ajax({
			url: url,
			type:"post",
			data:queryData,
			dataType:"json",
			success:function(result){
				//清空除第一条内容的外的其它数据
				var select_ = $("select[name='storeId']");
				select_.find("option:gt(0)").remove();
				if(orgType==3){
				    var isSelected = result.data.id == select_.attr('data-id')?"selected='selected'":"";
                        select_.append(
                                "<option "+isSelected+" value='"
                                        + result.data.id + "'>"
                                        + result.data.orgName
                                        + "</option>");
				}else{
    				for (var i = 0; i < result.data.length; i++) {
                        var isSelected = result.data[i].id == select_.attr('data-id')?"selected='selected'":"";
                        select_.append(
                                "<option "+isSelected+" value='"
                                        + result.data[i].id + "'>"
                                        + result.data[i].orgName
                                        + "</option>");
                    }
				}		
				if(orgType==3){
				  select_.find("option:eq(0)").remove();
				  select_.val(storeId);
				}
				$('#searchBtn').attr("disabled",false);
			},
			error:function(){
				alert("加载失败");
			}
		});
	}
</script>
