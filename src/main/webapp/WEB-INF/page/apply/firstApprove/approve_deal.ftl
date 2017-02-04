<#include "/apply/approve/top.ftl">

<script>
	$(function(){
		$("select").val('0');
	})
	$("li[name='tab']:eq(3)").addClass("active");
	<#list states?keys as key>
		var ${key} = ${states[key]};
	</#list>
	function goHref(e) {
			window.location.href=e+"${(applyLoanInfo.applyLoanNo)!}&cType=${cType!}";
	} 
</script>
<script src="${cdnPath}/js/apply/firstApprove/firstApprove.js"></script>
<div class="tab-content">

	<!-- BEGIN SAMPLE TABLE PORTLET-->

	<div class="portlet box blue">

		<div class="portlet-title">

			<div class="caption">
				<center>申请单信息</center>
			</div>

		</div>

		<div class="portlet-body" name="disEdit">

			<div class="row-fluid">
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">贷款类型：</label> <input
						type="text" class="m-wrap span8" value="信用贷款">
				</div>
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">门店：</label> <input
						type="text" class="m-wrap span8"
						value="${(applyLoanInfo.storeName)!}">
				</div>
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">区域：</label> <input
						type="text" class="m-wrap span8" 
						value="${(applyLoanInfo.areaName)!}">
				</div>
			</div>
			<div class="row-fluid">
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">产品名称：</label> <input
						type="text" class="m-wrap span8" 
						value="${(applyLoanInfo.productName)!}">
				</div>
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">贷款期数：</label> <input
						type="text" class="m-wrap span8" 
						value="${(applyLoanInfo.loanPeriods)!}">
				</div>
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">申请金额：</label> <input
						type="text" class="m-wrap span8" name="idCard"
						value="${(applyLoanInfo.applyMoney)!}">
				</div>
			</div>
			<div class="row-fluid">
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">申请单编号：</label> <input
						id="applyLoanNo" type="text" class="m-wrap span8"
						 value="${(applyLoanInfo.applyLoanNo)!}">
				</div>
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">借款用途：</label> <input
						id="applyLoanNo" type="text" class="m-wrap span8"
						 value="${(applyLoanInfo.loanPurposeName)!}">
				</div>
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">申请时间：</label> <input
						type="text" class="m-wrap span8" 
						value="${(applyLoanInfo.createTime?string("yyyy-MM-dd"))!}">
				</div>

			</div>
			<div class="row-fluid">
				<div class="control-group span4">
					<label class="help-inline text-right span4">接待建议：</label>
					<textarea rows="3" class="span8 m-wrap">${(applyLoanInfo.applyRemark)!}</textarea>
				</div>
			
			</div>

		</div>

	</div>
	<!-- END SAMPLE TABLE PORTLET-->
	<#list auditLogs as auditLog>
	<div class="portlet box blue">

		<div class="portlet-title">

			<div class="caption">
				<center>${auditLog.auditStageName}审核</center>
			</div>

		</div>

		<div class="portlet-body">

			<div class="row-fluid">
				<#if states['Fisrt_BACK_INIT'] == auditLog.auditResult || states['Apply_BACK_INIT'] == auditLog.auditResult || auditLog.auditResult == 2264 || auditLog.auditResult == 1355>
				<div class="control-group span4 " >
					<label class="help-inline text-right span4">审核结果：</label> 
					<div class="controls">
							<input
						type="text" class="m-wrap span8"
						value="${(auditLog.auditResultDesc)!}" readonly="true">
					</div>
				</div>
				<div class="control-group span4 " >
					<label class="help-inline text-right span4">原因：</label> 
					<div class="controls">
							<input
						type="text" class="m-wrap span8"
						value="${(auditLog.backReasonDesc)!}" readonly="true">
					</div>
				</div>
				<#else>
					<#if !(auditLog.auditResult == 1142 || auditLog.auditResult == 1352 || auditLog.auditResult == 2262 || auditLog.auditResult == 2372)>
					<div class="control-group span4 " >
						<label class="help-inline text-right span4">审核人：</label> 
						
						<div class="controls">
									<input
							type="text" class="m-wrap span8"
							value="${(auditLog.createUser)!}" readonly="true">
							</div>
					</div>
					</#if>
				<div class="control-group span4 " >
					<label class="help-inline text-right span4">审核结果：</label> 
					<div class="controls">
							<input
						type="text" class="m-wrap span8"
						value="${(auditLog.auditResultDesc)!}" readonly="true">
					</div>
				</div>
				</#if>
				<div class="control-group span4 " >
					<label class="help-inline text-right span4">审核日期：</label> <input
						type="text" class="m-wrap span8" name="applyMoney"
						value="${(auditLog.createTime?string("yyyy-MM-dd HH:mm:ss"))!}" readonly="true">
				</div>
			</div>
			<#if auditLog.auditResult == 1142 || auditLog.auditResult == 1352 || auditLog.auditResult == 2262 || auditLog.auditResult == 2372>
				<div class="row-fluid">
					<div class="control-group span4 " >
						<label class="help-inline text-right span4">拒绝原因：</label> 
						<div class="controls">
								<input
							type="text" class="m-wrap span8"
							value="${(auditLog.mainCause)!}" readonly="true">
						</div>
					</div>
					<div class="control-group span4 " >
						<label class="help-inline text-right span4">原因编号：</label> 
						<div class="controls">
								<input
							type="text" class="m-wrap span8"
							value="${(auditLog.lesserCause)!}" readonly="true">
						</div>
					</div>
				</div>
			<#elseif !(states['Fisrt_BACK_INIT'] == auditLog.auditResult || states['Apply_BACK_INIT'] == auditLog.auditResult || auditLog.auditResult == 2264 || auditLog.auditResult == 1355)>
			<div class="row-fluid">
				<div class="control-group span4 ">
					<label class="help-inline text-right span4">产品名称：</label> 
					
					<div class="controls">
								<input
						type="text" class="m-wrap span8" name="applyMoney"
						value="${(auditLog.productName)!}" readonly="true">
						</div>
				</div>
				<div class="control-group span4 " >
					<label class="help-inline text-right span4">贷款期数：</label> 
					<div class="controls">
							<input
						type="text" class="m-wrap span8" name="applyMoney"
						value="${(auditLog.loanPeriods)!}" readonly="true">
					</div>
				</div>
				<div class="control-group span4 " >
					<label class="help-inline text-right span4">申请金额：</label> <input
						type="text" class="m-wrap span8" name="applyMoney"
						value="${(auditLog.suggestMoney)!}" readonly="true">
				</div>
			</div>
			</#if>
			
			<div class="row-fluid">
				<div class="control-group span4">
				<label class="help-inline text-right span4">审核意见：</label>
				<textarea rows="3" class="span8 m-wrap" readonly="true">${(auditLog.interiorRemark)!}</textarea>
				</div>
			</div>
		</div>
	</div>
	<!-- END SAMPLE TABLE PORTLET-->
	</#list>
	<div class="portlet box blue">
		<div class="portlet-title">
			<div class="caption">
				<center>初审审核</center>
			</div>
		</div>
		<div class="portlet-body">
			<form id="form_submit">
			<input type="hidden" class="m-wrap span8" value="${(applyLoanInfo.applyLoanNo)!}" name="applyLoanNo">
			<div class="row-fluid">
			
				<input type="hidden" value="${states['Last_INIT']}" name="auditState">
				<div class="control-group span4" name="reason" style="display: none;">
					<label class="help-inline text-right span4 control-label">拒绝原因<span class="required">*</span></label> 
					<div class="controls">
						<select class="m-wrap span8"
							onchange="changeRefuseReason(this);" name="mainCauseNo">
							<option value="0">请选择</option>
						</select>
					</div>
				</div>
				<div class="control-group span4" name="reason" style="display: none;">
					<label class="help-inline text-right span4 control-label">编号:&nbsp;&nbsp;<span id="reasonCode"></span></label> 
				</div>
			</div>
			<div class="row-fluid">
				<div class="control-group span4 " name="applyInfo" >
					<label class="help-inline text-right span4 control-label">产品名称<span class="required">*</span></label> 
					
					<div class="controls">
								<select onchange="initloanPeriodsData(true);" name='proType' class="m-wrap span8" >
								<option value="0">请选择</option>
								<select>
						</div>
				</div>
				<div class="control-group span4 " name="applyInfo">
					<label class="help-inline text-right span4 control-label">贷款期数<span class="required">*</span></label> 
					<div class="controls">
							<select name='proId' class="m-wrap span8">
							<option value="0">请选择</option>
							<select>
					</div>
				</div>
				<div class="control-group span4 " name="applyInfo">
					<label class="help-inline text-right span4 control-label">批复金额<span class="required">*</span></label> <input
						type="text" class="m-wrap span8" name="creditMoney"
						value="">
				</div>
			</div>
			<div class="row-fluid">
				<div class="control-group span4 ">
					<label class="help-inline text-right span4 control-label">审核意见<span class="required">*</span></label>
					<textarea rows="3" class="span8 m-wrap" name="externalRemark"></textarea>
				</div>

			</div>

			</form>

			<!-- END SAMPLE TABLE PORTLET-->

			<div class="well">
				<center>
					<a class="btn blue" href="#visitExternal" data-toggle="modal"> 发起外访</a>
					<a class="btn blue" href="#" onclick="abandonMain('${(applyLoanInfo.applyLoanNo)!}')" data-toggle="modal"> 客户放弃</a>
					<a class="btn blue" href="#rollBack" data-toggle="modal"> 回退</a>
					<input type="button" class="btn blue"
					<#if applyApproveTask.auditState == 1141>
					 value="重新提交"
					 <#else>
					 value="提交"
					 </#if>
						onclick="submit();">
				</center>
			</div>
		</div>
	</div>
	
	<div id="visitExternal" class="modal hide fade" tabindex="-1"
		data-width="760">
		<div class="modal-header">

			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true"></button>
				<h3>外访</h3>
		</div>
		<div class="modal-body">
			<div class="control-group span8 ">
				<label class="help-inline text-right span4 control-label">外访注意点<span class="required">*</span></label>
				<textarea rows="4" class="span8 m-wrap"></textarea>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" data-dismiss="modal" class="btn">关闭</button>
			<button type="button" class="btn blue" onclick="visitExternal('${(applyLoanInfo.applyLoanNo)!}');">保存</button>
		</div>
	</div>
	<div id="rollBack" class="modal hide fade" tabindex="-1"
		data-width="760">
			<div class="modal-header">

				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
					<h3>回退</h3>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="control-group span8 ">
						<label class="help-inline text-right span4 control-label">回退原因<span class="required">*</span></label>
						<div class="controls">
							<select id="backReason" class="m-wrap">
							<option value="0">请选择</option>
							<select>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<div class="control-group span8 ">
						<label class="help-inline text-right span4 control-label">备注<span class="required">*</span></label>
						<div class="controls">
						<textarea  id="interiorRemark" rows="4" class="span8 m-wrap"></textarea>
						</div>
					</div>
				</div>
				
			</div>
			<div class="modal-footer">
				<button type="button" data-dismiss="modal" class="btn">关闭</button>
				<button type="button" class="btn blue" onclick="rollBack('${(applyLoanInfo.applyLoanNo)!}');">保存</button>
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
	
	<#include "/apply/approve/bottom.ftl">