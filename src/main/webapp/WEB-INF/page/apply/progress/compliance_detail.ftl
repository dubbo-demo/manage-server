<!-- edit form -->
<#if complianceList?? && complianceList?size gt 0>
<#list complianceList as compliance>
    <div class="row-fluid">
    		<div class="control-group span3 ">
       	   		<label class="control-label">合规审核</label> 
	       	    <div class="controls">
	            <span class="text">
	                <#if (compliance.result)?? && compliance.result==1>
	                                                                    通过
	                <#elseif (compliance.result)?? && compliance.result==2>
	                                                                    回退
	                </#if>
	            </span>
	            </div>
            </div>
            <div class="control-group span3 ">
			</div>
			<div class="control-group span3 ">
        	    <label class="control-label">审核时间</label>
        	    <div class="controls">
           		   <span class="text"><#if (compliance.createTime)??>${compliance.createTime?datetime}</#if></span>
               </div>
            </div>
        
    </div>
    <div class="row-fluid">
   		 <div class="control-group ">
			<label class="control-label">备注</label>
			<div class="controls">
				<textarea rows="3" name="remark" class="m-wrap span9" readonly>${(compliance.remark)!}</textarea>
			</div>
		</div>
    </div>
</#list>
</#if>
<script>
</script>	