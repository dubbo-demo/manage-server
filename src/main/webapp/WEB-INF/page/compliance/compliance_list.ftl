<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
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
					<li><span>合规管理</span><i class="icon-angle-right"></i></li>
					<li>
                        <a href="#"><#if dto??>合规审批<#else>合规审批</#if></a>
                    </li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
				<form id="searchForm" class="form-horizontal" method="post">
					<@p.pageForm value=page />
					<input type="hidden" id="progress" name="progress" value="${progress!''}" />
					<div class="row-fluid">
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">申请单编号：</label> <input
                                type="text" class="m-wrap span8" name="applyLoanNo"
                                value="${(compliance.applyLoanNo)!""}">
                        </div>
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">合同号：</label> <input
                                type="text" class="m-wrap span8" name="contractNo"
                                value="${(compliance.contractNo)!""}">
                        </div>
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">客户：</label> <input
                                type="text" class="m-wrap span8" name="memberName"
                                value="${(compliance.memberName)!""}">
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">大区：</label>
                            <select name="areaId" class="m-wrap span8" data-id="${(compliance.areaId)!"0"}" onchange="initStoreData()">
                                <option value="0">请选择</option>
                            </select>
                        </div>
                        <div class="control-group span4 " class="m-wrap span8">
                            <label class="help-inline text-right span4">门店：</label> 
                            <select name="storeId" class="m-wrap span8" data-id="${(compliance.storeId)!"0"}">
                                <option value="0">请选择</option>
                            </select>
                        </div>
                        <div class="control-group span4 " class="m-wrap span8">
                            <label class="help-inline text-right span4">产品类型：
                            </label>
                            <select name="productType" class="m-wrap span8" data-id="${(compliance.productType)!"0"}">
                                <option value="0">请选择</option>
                            </select>
                        </div>
                    </div>  
                    <div class="row-fluid">
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">进件日期：</label> 
                            
                                    <div class="input-append date date-picker" data-date="${(compliance.submitDates?string("yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
                                        <input name="submitDates" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(compliance.submitDates?string("yyyy-MM-dd"))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
                                    </div>      
                                    <span style="margin-left:-28px">--</span>
                                    <div class="input-append date date-picker" data-date="${(compliance.submitDatee?string("yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
                                        <input name="submitDatee" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(compliance.submitDatee?string("yyyy-MM-dd"))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
                                    </div>                                                          
                        </div>                
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">签约日期：</label> 
                            
                                    <div class="input-append date date-picker" data-date="${(compliance.signTimeDates?string("yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
                                        <input name="signTimeDates" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(compliance.signTimeDates?string("yyyy-MM-dd"))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
                                    </div>      
                                    <span style="margin-left:-28px">--</span>
                                    <div class="input-append date date-picker" data-date="${(compliance.signTimeDatee?string("yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
                                        <input name="signTimeDatee" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(compliance.signTimeDatee?string("yyyy-MM-dd"))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
                                    </div>                                                          
                        </div>
                    </div>                                          
                    <p>
                        <button type="submit" class="btn blue">查询</button>
                    </p>
				</form>
			</div>

			<div class="tabbable tabbable-custom boxless">	
				<ul class="nav nav-tabs">
    				<li <#if progress=='todo'>class="active"</#if>>
    					<a onclick="go_todo()">待办</a>
    				</li>
    				<li <#if progress=='done'>class="active"</#if>>
    					<a onclick="go_done()">已办</a>
    				</li>	
				</ul>
			
				<!-- table -->
				<table class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<@p.sort field="t.applyLoanNo" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="申请单号" ></@p.sort>
                            <@p.sort field="t.contractNo" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="合同号" ></@p.sort>
							<th>客户</th>
							<th>产品类型</th>
							<th>大区</th>
							<th>门店</th>					
							<@p.sort field="t.createTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="进件日期" ></@p.sort>
                            <@p.sort field="t.signTime" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="签约日期" ></@p.sort>
							<#if progress=='done'>
							    <th>审核人</th>
							    <th>审批结果</th>
							    <th>审批日期</th>
							</#if>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if page??&&page.result??>
							<#list page.result as item>
							<tr class="odd gradeX" >
								<td>${item_index+1 }</td>
								<td>
									${item.applyLoanNo!""}
								</td>
								<td>
                                    ${item.contractNo!""}
                                </td>
								<td>${item.memberName!""}</td>
								<td>${item.productName!""}</td>
								<td>${item.areaName!""}</td>
								<td>${item.storeName!""}</td>
                                <td><#if item.createTime??>${item.createTime?datetime}</#if></td>
                                <td><#if item.signTime??>${item.signTime?datetime}</#if></td>
								<#if progress == 'done'>
								    <td>${item.auditor!""}</td>
								    <td>
                                        <#if (item.result)?? && item.result==1>
                                                                                                                        通过
                                        <#elseif (item.result)?? && item.result==2>
                                                                                                                         回退
                                        </#if>
                                    </td>
								    <td><#if item.complianceTime??>${item.complianceTime?datetime}</#if></td>
								</#if>
                                <td>${item.stateName!""}</td>
                                <#if progress == 'done'>
                                    <td><a target="_blank" href="${serverPath}/compliance/doCompliance.htm?applyLoanNo=${item.applyLoanNo}&cType=0">查看详情</a></td>
                                <#else>
                                    <td><a href="${ctx}/compliance/doCompliance.htm?applyLoanNo=${item.applyLoanNo!''}&cType=1">审核</a></td>
                                </#if>
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
<script src="${cdnPath}/js/compliance/compliance.js?v=${VERSION_NO}"></script>