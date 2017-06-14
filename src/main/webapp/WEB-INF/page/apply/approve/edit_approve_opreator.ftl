
<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/apply/approve/edit_approve_opreator.js?v=${VERSION_NO}"></script>

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
							<a href="#">修改审批人</a>
						</li>
					</ul>
				</div>
			</div>
		<div>
		<div class="well">
			<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline  text-right span3">贷款类型：</label> <input disabled="true"
								type="text" class="m-wrap span8" name="applyLoanNo"
								value="信用贷款">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline  text-right span3">门店：</label> <input disabled="true"
								type="text" class="m-wrap span8" name="memberName"
								value="${(applyLoanInfo.storeName)!}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline  text-right span3">区域：</label> <input disabled="true"
								type="text" class="m-wrap span8" name="idCard"
								value="${(applyLoanInfo.areaName)!}">
						</div>
			</div>
			<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline  text-right span3">产品名称：</label> <input disabled="true"
								type="text" class="m-wrap span8" name="applyLoanNo"
								value="${(applyLoanInfo.productName)!}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline  text-right span3">贷款期数：</label> <input disabled="true"
								type="text" class="m-wrap span8" name="memberName"
								value="${(applyLoanInfo.loanPeriods)!}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline  text-right span3">申请金额：</label> <input disabled="true"
								type="text" class="m-wrap span8" name="idCard"
								value="${(applyLoanInfo.applyMoney)!}">
						</div>
			</div>
			<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline  text-right span3">申请单编号：</label> <input id="applyLoanNo" disabled="true"
								type="text" class="m-wrap span8" name="applyLoanNo"
								value="${(applyLoanInfo.applyLoanNo)!}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline  text-right span3">提单时间：</label> <input disabled="true"
								type="text" class="m-wrap span8" name="memberName"
								value="${(applyApproveTask.createTime?string("yyyy-MM-dd HH:mm:ss"))!}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline  text-right span3">申请单状态：</label> <input disabled="true"
								type="text" class="m-wrap span8" name="idCard"
								value="${(applyApproveTask.auditStateName)!}">
						</div>
						<input type="hidden" name="state" id="state" value='${(applyLoanInfo.state)!}'/>
			</div>
			<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline  text-right span3">原审批人：</label>
							<div class="controls">
							 <input disabled="true"
								type="text" class="m-wrap span8" name="applyLoanNo"
								value="${(approveUser)!}">
								<input
								type="hidden" id="oldApproveUser" class="m-wrap span8" value="${(emplyee.id)!}">
							</div>
						</div>
						<div class="control-group span4 ">
							<label class="help-inline  text-right span3">取件时间：</label> 
							<div class="controls">
							<input
								type="text" class="m-wrap span8" name="memberName" disabled="true"
								value="${(applyApproveTask.fetchTime?string("yyyy-MM-dd HH:mm:ss"))!}">
							</div>
						</div>
						
			</div>
			<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline  text-right span3 control-label">新审批人<span class="required">*</span></label> 
							<div class="controls">
							<input type="hidden" id='newApproveUser' name="newApproveUser" class="m-wrap span8" />
							<input type="hidden" id='newApproveUserName' name="newApproveUserName" />
							</div>
						</div>
					
			</div>
			<div class="well">
				<center>
				<a href="javascript:page_back('${serverPath}/approve/queryPageList.htm');" class="btn blue" >取消</a>
				<a class="btn blue" style="margin-left:40px" href="javascript:;" onclick="saveApproveUser();">提交</a>
				</center>
			</div>
		</div>
		
	</div>
	
	
<#include "/sys/bottom.ftl">