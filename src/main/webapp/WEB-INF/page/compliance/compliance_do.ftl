<#include "/compliance/compliance_top.ftl">
<script>
    $(function(){  
        //选中一级目录
        $("li[name='tab']:eq(0)").addClass("active");
    });
    function goHref(e) {
        window.location.href=e+'${applyLoanNo}'+"&cType="+$("#cType").val();        
    } 
</script>

<script type="text/javascript">
    var serverPath = "${serverPath}";
</script>

<style>
    .required{color:#e02222;}
</style>

<input type="hidden" name="cType" id="cType" value="${cType!}" />
<input type="hidden" name="applyLoanNo" id="applyLoanNo" value="${applyLoanNo!}" />

<div>
    <div class="portlet-body">
        <div class="portlet-body form"> 
        <div id="showCompliance">        
        <#list complianceList as compliance>
            <div style="padding:8px 35px 8px 14px;">
                <span class="control-label">审核结果：</span> 
                    <span class="text">
                        <#if (compliance.result)?? && compliance.result==1>
                                                                            通过
                        <#elseif (compliance.result)?? && compliance.result==2>
                                                                            回退
                        </#if>
                    </span>
                    <span class="control-label" style="margin-right:100px;"></span>
                    <span class="control-label">提交时间：</span>
                    <span class="text"><#if (compliance.createTime)??>${compliance.createTime?datetime}</#if></span>
                
            </div>
            <div style="padding:8px 35px 8px 14px;">
                <div class="control-group ">
                    <span class="control-label">备注：</span>
                    <textarea rows="3" readonly class="m-wrap span12" >${(compliance.remark)!}</textarea>
                </div>
            </div>
         </#list>
         </div>  
         <#if cType?? && cType=='1'>
         <div id="doCompliance">
            <input type="hidden" name="result" id="result" />
            <div style="padding:8px 35px 8px 14px;">
                <span class="control-label">审核结果：</span> 
                    <select class="m-wrap span2" id="resultSelect" name="resultSelect">   
                        <option value =1>通过</option>
                        <option value =2>回退</option>
                    </select>
            </div>
            <div style="padding:8px 35px 8px 14px;">
                <div class="control-group ">
                    <span class="control-label">备注：<span class="required">*</span></span>
                    <textarea rows="3" name="remark" id="remark" class="m-wrap span12" maxlength="300" > </textarea>
                </div>
            </div>
            <div style="padding:8px 35px 8px 14px;">
                <a href="${serverPath}/compliance/list.htm" class="btn blue">返回</a>
                <input type="button" class="btn blue" value="提交" onclick="return save()"></input> 
            </div>
         <div>  
         </#if>
         </div>
    </div>
</div>
<#include "/apply/approve/bottom.ftl">
<script src="${cdnPath}/js/compliance/complianceToDo.js?v=${VERSION_NO}"></script>