<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/reception/list.js?v=${VERSION_NO}"></script>
<script>
	var orgType="${orgType!}"; //组织类型1:大区 2：总部 3：门店
	var regionId="${regionId!}";//大区id
	function page_jump(url){
		ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
		window.location.href=url;
	}
	$(function(){
		$("select[name='clientType']").val("${queryDto.clientType!}");
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
									<a href="javascript:void(0)">分公司业务管理</a><i class="icon-angle-right"></i>
								</li>
								<li>
									<a href="#">接待分配管理</a>
								</li>
							</ul>
						</div>
					</div>
				<div>
				<div class="portlet-body form">
							<form action="${serverPath}/receptionManage/list.htm" id="searchForm" class="form-horizontal" method="post">
							<@p.pageForm value=page  type="sort"/>
								<input type="hidden" id="pageIndex" name="pageIndex" value='1'/>
								<div class="row-fluid">
									<div class="control-group span3 ">
										<label class="help-inline text-right span4">申请件单号：</label>
										<input type="text" class="m-wrap span5" name="applyLoanNo" value="${(queryDto.applyLoanNo)!''}">
									</div>
									<div class="control-group span3 ">
										<label class="help-inline text-right span4">客户：</label>
										<input type="text" class="m-wrap span5" name="memberName" value="${(queryDto.memberName)!''}">
									</div>
									<div class="control-group span3 ">
										<label class="help-inline text-right span4">客服：</label>
										<input type="text" class="m-wrap span5" name="customerSName" value="${(queryDto.customerSName)!''}">
									</div>
									<div class="control-group span3 ">
										<label class="help-inline text-right span4">门店：</label>
										<input type="hidden" id="empStoreId" value="${empStoreId!}">
										<input type="hidden" id="storeId2" value="${queryDto.storeId!}">
										<select class="m-wrap span5" id="storeId" name="storeId" value="">
										</select>
									</div>
								</div>
								<div class="row-fluid">
									<div class="control-group span3 ">
										<label class="help-inline text-right span4">渠道：</label>
										<select class="m-wrap span5" id="clientType" name="clientType" value="${queryDto.clientType!}">
											<#if clients??>
												<#if (clients?size) gt 1>
                                                    <option value="">请选择</option>
												</#if>
												<#list clients as c>
													<#if c == 0>
                                                        <option value="0" <#if queryDto.clientType?? && queryDto.clientType == 0>selected</#if> >web</option>
													</#if>
													<#if c == 1>
                                                        <option value="1" <#if queryDto.clientType?? && queryDto.clientType == 1>selected</#if> >app</option>
													</#if>
												</#list>
											<#else >
                                                <option value="">请选择</option>
                                                <option value="0" <#if queryDto.clientType?? && queryDto.clientType == 0>selected</#if> >web</option>
                                                <option value="1" <#if queryDto.clientType?? && queryDto.clientType == 1>selected</#if> >app</option>
											</#if>

										</select>
									</div>
									<div class="control-group span3 ">
										<label class="help-inline text-right span4">查询日期</label> 
										<div class="input-append date date-picker" data-date="${(queryDto.queryTime?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
											<input name="queryTime" class="m-wrap span10 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.queryTime?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
										</div>	
									</div>
								</div>
								<div class="row-fluid">
									<div class="control-group span4 ">
										<button type="button" onclick="search()" class="btn blue">查询</button>
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
					<th>渠道来源</th>
					<@p.sort field="applyLoanNo" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="申请单号" ></@p.sort>
					<th>客户</th>
					<th>手机号</th>
					<th>客服</th>
					<th>门店</th>
					<@p.sort field="createTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="创建日期" ></@p.sort>
					<@p.sort field="allotDate" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="分配日期" ></@p.sort>
					<th>状态描述</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
			<#if page?? && page.result?? >
					<#list page.result as item>
						<tr class="odd gradeX" id="${item.id!}">
							<td>${item_index+1 }</td>
							<#if item.clientType??>
								<#if item.clientType == 0>
									<td>线下</td>
								<#else>
									<td>app</td>
								</#if>
							<#else>
								<td>线下</td>
							</#if>
							<td>${item.applyLoanNo! }</td>
							<td>${item.memberName! }</td>
							<td>${item.phone! }</td>
							<td>${(item.customerSName)!""}</td>
							<td>${(item.storeName)!""}</td>
							<td>${item.createTime?datetime}</td>
							<td>${(item.allotDate?datetime)!""}</td>
							<#if item.customerServiceId??>
								<td>已分配</td>
							<#else>
								<td>未分配</td>
							</#if>
							<td>
								<!-- <shiro:hasPermission name="menu:editUI">
								<a href="${ctx}/innerBlack/detailUI.html?id=${item.id}">查看详情</a> -->
									<a href="javascript:page_jump('${serverPath}/receptionManage/allotForm.htm?id=${item.id!}')">分配</a>
							</td>
						</tr>
					</#list>
			
					<tr>
					<td colspan="14" align="center">
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
					<h4 class="refuseDivTitle"></h3>
					<style>
				  	.refuseDivTitle {text-align:center;font-weight:bold}
				  	</style>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="control-group span10">
						<label class="help-inline text-right span3">说明:</label>
						<div class="controls">
						<textarea id="description" rows="4" class="m-wrap span9"></textarea>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn blue" onclick="goDelte()">确定</button>
				<button type="button" data-dismiss="modal" class="btn">取消</button>
			</div>
</div>
<#include "/sys/bottom.ftl">