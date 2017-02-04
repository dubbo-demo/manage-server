<#include "/sys/top.ftl"> <#include "/sys/left.ftl">
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
					<li><a href="#">外访管理</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
				<form id="searchForm" action="#" class="form-horizontal" method="post">
					<input type="hidden" id="progress" name="progress" value="${progress!'todo'}" />
					<@p.pageForm value=page  type="sort"/>
					<div class="row-fluid">
						<div class="control-group span4 ">
						
							<label class="help-inline text-right span4">申请单编号：</label>
							<div class="">
							 <input
								type="text" class="m-wrap span8" name="applyLoanNo"
								value="${(queryDto.applyLoanNo)!''}">
							</div>				
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">客户：</label>
							<div class="">
							 <input	type="text" class="m-wrap span8" name="memberName"
								value="${(queryDto.memberName)!''}">
							</div>	
						</div>
						<input name="storeId" type="hidden" value="${(queryDto.storeId)!''}">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">门店：</label> 
							<div class="">
							<input	type="text" class="m-wrap span8" readonly="true" value="${(storeName)!''}">
							</div>
						</div>
					</div>
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">手机号：</label>
							<div class="">
							 <input	type="text" class="m-wrap span8" name="phone" value="${(queryDto.phone)!''}">
							</div>
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">业务经理：</label> 
							<div class="">
							<!--<input type="hidden" id='bmId' name="bmId" class="m-wrap span8" value="${(queryDto.bmId)!}" />-->
							<input type="text" class="m-wrap span8" id='bmName' name="bmName" value="${(queryDto.bmName)!}"/>
							</div>
								
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">客服：</label> 
							<div class="">
								<!--<input type="hidden" id='customerServiceId' name="customerServiceId"   class="m-wrap span8" value="${(queryDto.customerServiceId)!}" />-->
							<input type="text" class="m-wrap span8" id='customerServiceName' name="customerServiceName" value="${(queryDto.customerServiceName)!}"/>
							</div>								
						</div>
					</div>	
					<#if progress=='todo'>
					<div class="row-fluid">
					<div class="span4">
						<div class="control-group  ">
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
						<#if isManage==1>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">外访需求日期：</label> 
							<div class="">
									<div class="input-append date date-picker" data-date="${(queryDto.createTimeStart?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="createTimeStart" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.createTimeStart?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(queryDto.createTimeEnd?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="createTimeEnd" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.createTimeEnd?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>	
							</div>		
						</div>
						<#else>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">外访分配日期：</label> 
							<div class="">
									<div class="input-append date date-picker" data-date="${(queryDto.allotTimeStart?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="allotTimeStart" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.allotTimeStart?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(queryDto.allotTimeEnd?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="allotTimeEnd" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.allotTimeEnd?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>	
							</div>		
						</div>
						</#if>
					</div>	
					</#if>
					<#if progress=='done'>
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">状态：</label> 
							<div class="">
										<select class="m-wrap span8" name="state">
											<option value="-1">请选择</option>
											<#if isManage==1>
											<option value="2">待外访</option>
											</#if>
											<option value="3">外访通过</option>
											<option value="4">外访拒绝</option>
										</select>	
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
						<#if isManage==1>							
						<div id="visitTimeDiv"class="control-group span4 " >
							<label class="help-inline text-right span4">外访完成日期：</label> 
							<div class="">
									<div class="input-append date date-picker" data-date="${(queryDto.visitTimeStart?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="visitTimeStart" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.visitTimeStart?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(queryDto.visitTimeEnd?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="visitTimeEnd" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.visitTimeEnd?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>
							</div>			
						</div>
						<#else>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">外访分配日期：</label> 
							<div class="">
									<div class="input-append date date-picker" data-date="${(queryDto.allotTimeStart?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="allotTimeStart" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.allotTimeStart?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(queryDto.allotTimeEnd?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="allotTimeEnd" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.allotTimeEnd?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>	
							</div>		
						</div>
						</#if>
					
					</div>	
					</#if>		
					<p>
						<button type="button" class="btn blue" onclick="search()">查询</button>
					</p>
				</form>
			</div>

			<div class="tabbable tabbable-custom tabbable-custom-profile">
			
				<ul class="nav nav-tabs">
							<#if progress=='todo'>
								<li class="active">
									<a href="javascript:void(0)">待办</a>
								</li>
								<li>
									<a href="javascript:go_done()">已办</a>
								</li>
							</#if>
							<#if progress=='done'>
								<li >
									<a href="javascript:go_todo()">待办</a>
								</li>
								<li class="active">
									<a href="javascript:void(0)">已办</a>
								</li>
							</#if>
				</ul>
			
				<!-- table -->
				<table class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<!--<th class="sorting">申请单号</th>-->
							<@p.sort field="applyLoanNo" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="申请单号" ></@p.sort>
							<th>客户</th>
							<th>手机号</th>
							<th>业务经理</th>
							<th>门店</th>
							<!--<th class="sorting_desc">进件日期</th>-->
							<@p.sort field="applyTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="进件日期" ></@p.sort>
							<@p.sort field="createTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="外访需求日期" ></@p.sort>
							<#if progress=='done'>
							<@p.sort field="allotTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="外访分配日期" ></@p.sort>
							<@p.sort field="visitTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="外访完成日期" ></@p.sort>
							<th>客服</th>
							<th>外访人</th>
							<#elseif isManage==1>
							<th>客服</th>
							<#else>
							<@p.sort field="allotTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="外访分配日期" ></@p.sort>
							<th>客服</th>
							<th>外访人</th>
							</#if>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if page??&&page.result??>
							<#list page.result as item>
							<tr class="odd gradeX" id="${item.id}">
								<td>${item_index+1 }</td>
								<td>${item.applyLoanNo!"" }</td>
								<td>${item.memberName!"" }</td>
								<td>${item.phone!""}</td>
								<td>${item.bmName!""}</td>
								<td>${item.storeName!""}</td>
								<td><#if item.applyTime??>${item.applyTime?datetime}</#if></td>
								<td><#if item.createTime??>${item.createTime?datetime}</#if></td>
								<#if progress=='done'>
								<td><#if item.allotTime??>${item.allotTime?datetime}</#if></td>
								<td><#if item.visitTime??>${item.visitTime?datetime}</#if></td>
								<td>${item.customerServiceName!""}</td>
								<td>${item.visitUserName!""}</td>
								<#elseif isManage==1>
								<td>${item.customerServiceName!""}</td>
								<#else>
								<td><#if item.allotTime??>${item.allotTime?datetime}</#if></td>
								<td>${item.customerServiceName!""}</td>
								<td>${item.visitUserName!""}</td>
								</#if>
								<td><#if item.state??>
										<#if item.state == 1>
											待分配
										<#elseif item.state==2>
											待外访
									    <#elseif item.state==3>
									    	外访通过
									    <#elseif item.state==4>
									    	外访拒绝
									    <#else>
									    --
									    </#if>									    
									</#if>
								</td>
								<td>
								<#if isManage == 1>
									<#if item.state == 1>
									<a href="javascript:page_jump('${ctx}/visit/allotUI.htm?id=${item.id}')">分配</a>
									<#elseif item.state == 2>
									<a href="javascript:print('${item.id}')">重新打印</a>
									</#if>
								<#elseif isManage == 0>
									<#if item.state == 2>
									<a href="javascript:page_jump('${ctx}/visit/recordUI.htm?id=${item.id}')">结果录入</a>
									<#else>
									<a href="javascript:page_jump('${ctx}/visit/recordUI.htm?id=${item.id}')">查看</a>
									</#if>
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
<#if  progress=='done'>
$(function(){
	$("select[name='state']").val(${(queryDto.state)!-1});
	/**
	$("select[name='state']").change(
		function(){
			if($(this).val()==3||$(this).val()==4){
				$("#visitTimeDiv").show();
				$("#visitTimeDiv").find(":input").attr("disabled", false);  
			}else{
				$("#visitTimeDiv").hide();
				$("#visitTimeDiv").find(":input").attr("disabled", true);  
			}

		}
	);
	$("select[name='state']").trigger("change");**/
})
</#if>
	function search() {
		$("#searchForm").attr("action", "${ctx}/visit/list.htm");
		ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").submit();
	}
	function goPage(pageNumber) {
		$("#searchForm").attr("action", "${ctx}/visit/list.htm");
	    $("#pageIndex").val(pageNumber);
	    ChkUtil.form_trim($("#searchForm"));
	    $("#searchForm").submit();
	}
	function go_done(){
	/**
		$("input[name='createTimeStart']").val("");
		$("input[name='createTimeEnd']").val("");
		$("select[name='state']").val("");
		$("#progress").val("done");
		$("#searchForm").attr("action", "${ctx}/visit/list.htm");
		ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").submit();**/
		window.location.href='${ctx}/visit/list.htm?progress=done'
	}
	function go_todo(){
	/**
		$("input[name='visitTimeStart']").val("");
		$("input[name='visitTimeEnd']").val("");
		$("select[name='state']").val("");
		$("#progress").val("todo");
		$("#searchForm").attr("action", "${ctx}/visit/list.htm");
		ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").submit();**/
		window.location.href='${ctx}/visit/list.htm?progress=todo'
	}
	function print(id){
		window.open("${ctx}/visit/print.htm?id="+id);
	}
	
	function page_jump(url){
		ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
		window.location.href=url;
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
</script>
