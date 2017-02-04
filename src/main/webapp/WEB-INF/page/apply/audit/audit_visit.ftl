<#include "/apply/approve/top.ftl">
<script>
    $(function(){  
        //选中一级目录
        $("li[name='tab']:eq(2)").addClass("active");
        //选中二级目录
        $("li[name='two_li']:eq(0)").addClass("active");
        <#if cType??&&cType!="">
            //input,text,select
            $("input[type='input']").attr("disabled","disabled");
            $("input[type='text']").attr("disabled","disabled");
            $("input[type='radio']").attr("disabled","disabled");
            $("select,textarea").attr("disabled","disabled");
        </#if>    
    });
    function goHref(e) {
        window.location.href=e+'${applyLoanNo}'+"&cType="+$("#cType").val();        
    } 
</script>

<script type="text/javascript">
    var serverPath = "${serverPath}";
</script>

<input type="hidden" name="cType" id="cType" value="${cType!}" />

<div>
    <div class="portlet-body">
        <#list visitList as visit>
            <div class="portlet-body form">   
                <div style="padding:8px 35px 8px 14px;">
                    <span class="control-label">外访结论：</span>
                    <span class="text">
                        <#if visit.state?? && visit.state==3>
                                                                            通过
                        <#elseif visit.state?? && visit.state==4>
                                                                            拒绝
                        </#if>
                    </span>
                    <span class="control-label" style="margin-right:100px;"></span>
                    <span class="control-label">提交时间：</span>
                    <span class="text">${visit.visitTime?datetime}</span>
                    <span class="control-label" style="margin-right:100px;"></span>
                    <#if visit_index==0><input type="button" class="btn blue" onclick="viewfile(event)" value="查看附件" /></#if> 
                </div>
                <div style="padding:8px 35px 8px 14px;">
                    <div class="control-group ">
                        <span class="control-label">备注：</span>
                        <textarea rows="3" name="remark" class="m-wrap span12" readonly>${(visit.remark)!}</textarea>
                    </div>
                </div>
            </div>
        </#list>
    </div>
</div>
<#include "/apply/approve/bottom.ftl">
<script>
function viewfile(event){
        ChkUtil.stopBubbleEvent(event);
        window.open("${serverPath}/productFile/fileUpload.htm?fileUpState=14&isView=0&applyLoanNo=${(applyLoanNo)!}");
}
</script>