<!-- tab层 start -->
<#include "/apply/approve/top.ftl">
<script>
    $(function(){
        //选中一级目录
        $("li[name='tab']:eq(1)").addClass("active");
        //选中二级目录
        $("li[name='two_li']:eq(2)").addClass("active");
        <#if cType??&&cType!="">
            //input,text,select
            $("input[type='input']").attr("readonly","readonly");
            $("input[type='text']").attr("readonly","readonly");
            $("input[type='radio']").attr("readonly","readonly");
            $("select").attr("disabled","disabled");
            $("textarea").attr("readonly","readonly");
        </#if>
    });
    function goHref(e) {
        window.location.href=e+$("#applyLoanNo").val()+"&cType="+$("#cType").val();
    } 
</script>
<#include "/apply/audit/common/two_tab.ftl">
<!-- tab层 end -->

<script type="text/javascript">
    var serverPath = "${serverPath}";
</script>
<style>
     .required{color:#e02222;}
</style>

<input type="hidden" name="cType" id="cType" value="${cType!}" />
<input type="hidden" name="applyLoanNo" id="applyLoanNo" value='${applyLoanNo}'/>
<input type="hidden" name="memberId" id="memberId" value='${memberId!""}'/>
<div class="portlet box blue">
    <div class="portlet-body">
        <!-- table -->     
        <table class="table">
            <tr><td style="text-align: right;" >
                <h4 style="text-align: center;">电调联系人</h4>
                <form id="changeAuditCall" action="#">
                    <#list auditCallDtoList as auditCallDto>
                        <input type="hidden" name="type" value='${auditCallDto.type!""}'/>
                        <input type="hidden" name="oldPhone" value='${auditCallDto.phone!""}' />
                        <input type="hidden" name="oldRemark" value='${auditCallDto.remark!"!!"}' />
                        <input type="hidden" name="ifChangePhone" value='0' />
                        <input type="hidden" name="ifChangeRemark" value='0' />
                        <input type="hidden" name="remarkId" value='${auditCallDto.remarkId!"0"}' />
                        <input type="hidden" name="phoneId" value='${auditCallDto.phoneId!"0"}' />
                        <div class="control-group" style="padding-top:1px;display:table;width:100%;"> 
                            <div style="display:table-cell; vertical-align:middle;width:11%;">
                                <span class="control-label" name='name' style="margin-left:10px;">${auditCallDto.name!""}</span>
                            </div> 
                            <#if auditCallDto.type != 2>      
                                <div style="display:table-cell; vertical-align:middle;width:11%;">
                                    <span class="control-label" name='typeDescription' style="margin-right:10px;">${auditCallDto.typeDescription!""}</span>
                                </div>
                                <div style="display:table-cell; vertical-align:middle;width:16%;">
                                    <input type="text" style="margin-right:30px;" maxlength="20" name='phone' onblur="checkInput('^[\(\)\-\.0-9\uff08\uff09]*$',this)" value='${auditCallDto.phone!""}' class="m-wrap medium js_input" <#if auditCallDto.type == 1>readonly</#if>/>
                                </div>
                                <input type="hidden" name="phoneExtension" value='0' />
                                <input type="hidden" name="oldPhoneExtension" value='0' />
                            <#else>
                                <div style="display:table-cell; vertical-align:middle;width:27%;">
                                    <div>
                                        <span class="control-label" style="margin-right:10px;">单位电话</span>
                                        <input type="text" style="margin-right:30px;" name='phone' maxlength="20" class="m-wrap medium " value='${auditCallDto.phone!""}' onblur="checkInput('^[\(\)\-\.0-9\uff08\uff09]*$',this)" />                           
                                    </div>
                                    <div>
                                        <span class="control-label" style="margin-right:10px;">分机号</span>
                                        <input type="text" style="margin-right:30px;" name='phoneExtension' maxlength="20" class="m-wrap medium " value='${auditCallDto.phoneExtension!""}' onblur="checkInput('^[\(\)\-\.0-9\uff08\uff09]*$',this)" />                           
                                        <input type="hidden" name="oldPhoneExtension" value='${auditCallDto.phoneExtension!""}' />
                                    </div>
                                </div>
                            </#if>
                            <div style="display:table-cell; vertical-align:middle;width:73%;">
                                <textarea rows="2" maxlength="300" name='remark' placeholder='请输入调查结果' class="m-wrap span12 <#if auditCallDto.type == 1>js_input</#if>" onblur='contactsInputNext()'  >${auditCallDto.remark!""}</textarea>  
                            </div>    
                        </div>
                    </#list>
                </form>
                <form id="addAuditCallForm" action="#">
                    <div id="addAuditCall"></div>
                </form>
                <div style="text-align: left;margin-left:115px;">
                    <input type="button" class="btn blue" value="增加" onclick="return addInfo()"></input>
                </div>
            </td></tr>
            <#if !cType?? || cType==''>
            <tr><td style="text-align: center;">
                <input type="button" id='save' name='save' class="btn blue" value="保存" onclick="return saveInfo()"></input>
            </td></tr>
            </#if>
        </table>
    </div>
</div>
<#include "/apply/approve/bottom.ftl">
<script src="${cdnPath}/js/apply/auditCall.js?v=${VERSION_NO}"></script>
