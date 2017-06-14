
<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/apply/approve/approve.js?v=${VERSION_NO}"></script>

	<div class="page-content">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<!--页面标题-->
					<h3 class="page-title"></h3>
					<!--面包屑导航-->
					<ul class="breadcrumb">
						<li> <i class="icon-home"></i>
							<a href="${serverPath}/index.htm">首页</a>
							<i class="icon-angle-right"></i> 
						</li>
						<li>
							<a href="#">信审业务管理</a>
							<i class="icon-angle-right"></i> 
						</li>
						<li>
							<a href="#">审批列表</a>
						</li>
					</ul>
				</div>
			</div>
					<form id="searchForm" class="form-horizontal" action="${serverPath}/approve/queryPageList.htm"
					method="post">
					<@p.pageForm value=page  type="sort"/>
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">申请单编号：</label> <input
								type="text" class="m-wrap span8" name="applyLoanNo"
								value="${(params.applyLoanNo)!""}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">客户：</label> <input
								type="text" class="m-wrap span8" name="memberName"
								value="${(params.memberName)!""}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">身份证号：</label> <input
								type="text" class="m-wrap span8" name="idCard"
								value="${(params.idCard)!""}">
						</div>
					</div>
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">大区：</label>
							<select name="areaId" class="m-wrap span8" data-id="${(params.areaId)!"0"}" onchange="initStoreData()">
								<option value="0">请选择</option>
							</select>
						</div>
						<div class="control-group span4 " class="m-wrap span8">
							<label class="help-inline text-right span4">门店：</label> 
							<select name="storeId" class="m-wrap span8" data-id="${(params.storeId)!"0"}">
								<option value="0">请选择</option>
							</select>
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
							<label class="help-inline text-right span4">提交日期：</label> 
							
									<div class="input-append date date-picker" data-date="${(params.submitDates?string("yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="submitDates" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(params.submitDates?string("yyyy-MM-dd"))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(params.submitDatee?string("yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="submitDatee" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(params.submitDatee?string("yyyy-MM-dd"))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>															
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">是否分配：</label> 
							<#if params.isAssigned??>
							<select tabindex="1" name="isAssigned" data-id="${(params.isAssigned)!"0"}" class="span8 m-wrap">
								<option value="0">请选择</option>
								<option value="1" <#if params.isAssigned == 1>selected = 'selected'</#if> >是</option>
								<option value="2" <#if params.isAssigned == 2>selected = 'selected'</#if> >否</option>
							</select>
							<#else>
							<select tabindex="1" name="isAssigned" data-id="${(params.isAssigned)!"0"}" class="span8 m-wrap">
								<option value="0">请选择</option>
								<option value="1">是</option>
								<option value="2">否</option>
							</select>
							</#if>													
						</div>
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">信审小组：</label> 
                            <select name="teamId" class="m-wrap span8" data-id="${(params.teamId)!"0"}">
                                <option value="0">请选择</option>
                            </select>                                              
                        </div>						
					</div>										
					<p>
						<button type="submit" class="btn blue">查询</button>
					</p>
				</form>
			<!-- table -->
			<div class="tabbable tabbable-custom tabbable-custom-profile">
			<table class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th>序号</th>
						<@p.sort field="applyLoanNo" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="申请单号" ></@p.sort>
						<th>客户</th>
						<th>身份证号</th>
						<th>大区</th>
						<th>门店</th>
						<th>产品类型</th>
						<@p.sort field="submitDate" sortField="${(page.sortField)!}" sortOrder="${(page.sortOrder)!}" name="提交日期" ></@p.sort>
						<th>初审</th>
						<th>复审</th>
						<th>终审</th>
						<th>高级终审</th>
						<th>状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<#list page.result as record>
						<tr>
							<td>${record_index+1}</td>
							<td>${record.applyLoanNo!}</td>
							<td>${record.memberName!}</td>
							<td>${record.idCard!}</td>
							<td>${record.areaName!""}</td>
							<td>${record.storeName!}</td>
							<td>${record.proName!}</td>
							<td>${record.submitDate?string('yyyy-MM-dd HH:mm:ss')}</td>
							<td>${record.firstAuditorName!}</td>
							<td>${record.reviewAuditorName!}</td>
							<td>${record.lastAuditorName!}</td>
							<td>${record.superLastAuditorName!}</td>
							<td>${record.stateName!}</td>
							<td>
							<#if record.getEditAuditor()>
							<a href="javascript:page_jump('${serverPath}/approve/editApproveOpreator.htm?applyLoanNo=${record.applyLoanNo!}');">
							修改</a>
							</#if>
							<#if record.firstApproveUserId?? && record.lastApproveUserId?? && (record.subState == 1350 || record.subState == 1141)>
							<a href="javascript:page_jump('${serverPath}/approve/editApproveOpreator.htm?applyLoanNo=${record.applyLoanNo!}');" >修改</a>
							</#if>
							&nbsp;&nbsp;<a href="${serverPath}/apply/applyDetail.htm?applyLoanNo=${record.applyLoanNo!}&cType=1" target="_blank">查看详情</a></td>
						<tr>
					</#list>
				</tbody>
			</table>
			<@p.pagination value=page />
	</div>
	</div>
	<script>
    	$(function(){
            $("select[name='subState']").val(${(params.subState)!-1});
        })
    </script>
	
<#include "/sys/bottom.ftl">