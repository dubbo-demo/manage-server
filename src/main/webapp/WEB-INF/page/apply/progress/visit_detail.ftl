<div class="tab-pane row-fluid active" id="visitDetail">
	<div class="portlet box">
	<!--
		<div class="portlet-title">
			<div class="caption">
				<i class="icon-reorder"></i>外访单
			</div>
		</div>
		-->
		<div class="portlet-body form">
			
			<!-- edit form -->
			<form id="tab" action="#" method="POST"	class="form-horizontal">
			<#if visitlist?? && visitlist?size gt 0>
				<#list visitlist as visit>
				<#if visit_index gt 0>
				<hr>
				</#if>
				<div class="row-fluid">
					<div class="control-group span6 ">
						<label class="control-label">外访结论</label>
						<div class="controls">
							<span class="text">
								<#if visit.state??>
									<#if visit.state==3>
										通过
									<#elseif visit.state==4>
										拒绝
									</#if>
								</#if>
							</span>
						</div>
					</div>
					<div class="control-group span3 ">
						<label class="control-label">提交时间</label>
						<div class="controls">
							<span class="text"><#if visit.visitTime??>${visit.visitTime?datetime}</#if></span>
						</div>
					</div>
					<div class="control-group span3 ">
							<input type="button" class="btn blue" onclick="viewfile(event)" value="查看附件" />	
					</div>
				</div>
				<div class="row-fluid">
					<div class="control-group ">
						<label class="control-label">备注</label>
						<div class="controls">
							<textarea rows="3" name="remark" class="m-wrap span9" readonly>${(visit.remark)!}</textarea>
						</div>
					</div>
				</div>
				</#list>
			</#if>
			</form>
			
			</div>
		</div>
	</div>
</div>
<script>
function viewfile(event){
		ChkUtil.stopBubbleEvent(event);
		window.open("${serverPath}/productFile/fileUpload.htm?fileUpState=14&isView=0&applyLoanNo=${(applyLoanNo)!}");
}
</script>	