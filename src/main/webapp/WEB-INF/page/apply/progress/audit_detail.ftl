<!-- edit form -->
    <hr>
		<div>
			<h4 style="text-align:center">
				<strong>审核结果</strong>
			</h4>
		</div>
	<hr>

	<div class="row-fluid">
		<div class="control-group span3 ">
			<label class="control-label">综合意见</label>
			<div class="controls">
				<span class="text">
				<#if applyInfo.state == 20 && applyInfo.subState == 10>
				拒绝
				<#else>
				通过
				</#if>
				</span>
			</div>
		</div>
		<div class="control-group span3 ">
		</div>
		<div class="control-group span3 ">
			<label class="control-label">审核时间</label>
			<div class="controls">
				<span class="text"><#if (applyInfo.opinionTime)??>${applyInfo.opinionTime?datetime}</#if></span>
			</div>
		</div>
	</div>
	
	<div class="row-fluid">
		<div class="control-group ">
			<label class="control-label">综合意见描述</label>
			<div class="controls">
				<textarea rows="3" name="remark" class="m-wrap span9" readonly>${(applyInfo.applyRemark)!}</textarea>
			</div>
		</div>
	</div>

<#if auditInfo??>
	<div class="row-fluid">
		<div class="control-group span3 ">
			<label class="control-label">${(auditInfo.auditStageName)!}审核</label>
			<div class="controls">
				<span class="text">${(auditInfo.auditResultDesc)!}
				</span>
			</div>
		</div>
		<#if states['BACK_INIT'] == auditInfo.auditResult>
		<div class="control-group span3 ">
			<label class="control-label">退回原因</label>
			<div class="controls">
				<span class="text" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="${(auditInfo.backReasonDesc)!}">${(auditInfo.backReasonDesc)!}
				</span>
			</div>
		</div>
		
		<#elseif states['Director_REFUSE'] == auditInfo.auditResult ||states['Manger_REFUSE'] == auditInfo.auditResult || states['Last_REFUSE'] == auditInfo.auditResult>
		<div class="control-group span3 ">
			<label class="control-label">原因编号</label>
			<div class="controls">
				<span class="text" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="${(auditInfo.lesserCause)!}">${(auditInfo.lesserCause)!}
				</span>
			</div>
		</div>			
		<#else>
		<div class="control-group span3 ">
		</div>
		</#if>	
				
		<div class="control-group span3 ">
			<label class="control-label">审核时间</label>
			<div class="controls">
				<span class="text"><#if (auditInfo.createTime)??>${auditInfo.createTime?datetime}</#if></span>
			</div>
		</div>
	</div>
	<div class="row-fluid">
		<div class="control-group ">
			<label class="control-label">${(auditInfo.auditStageName)!}审核意见</label>
			<div class="controls">
				<textarea rows="3" name="remark" class="m-wrap span9" readonly>${(auditInfo.publicRemark)!}</textarea>
			</div>
		</div>
	</div>
</#if>
<script>
</script>	