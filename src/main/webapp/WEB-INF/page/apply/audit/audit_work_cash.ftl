<!-- tab层 start -->
<#include "/apply/approve/top.ftl">
<script>
    $(function(){
        //选中一级目录
        $("li[name='tab']:eq(1)").addClass("active");
        //选中二级目录
        $("li[name='two_li']:eq(3)").addClass("active");
        <#if cType?? && cType=='1' >
            $("input[type='input']").attr("disabled","disabled");
            $("input[type='text']").attr("disabled","disabled");
            $("input[type='radio']").attr("disabled","disabled");
            $("select,textarea").attr("disabled","disabled");
        </#if>
    });
    function goHref(e) {
        window.location.href=e+$("#applyLoanNo").val()+"&cType="+$("#cType").val();
    } 
</script>
<#include "/apply/audit/common/two_tab.ftl">
<!-- tab层 end -->
<script src="${cdnPath}/js/apply/auditWorkCash.js?v=${VERSION_NO}"></script>

<script type="text/javascript">
    var serverPath = "${serverPath}";
    var fileUploadDto = {memberType:"${(apply.memberType)!''}",state:"${(apply.state)!''}"};
</script>
<style>
     .required{color:#e02222;}
</style>

<input type="hidden" name="cType" id="cType" value="${cType!}" />

<div class="portlet box blue">
    <div class="portlet-body">
        <!-- table -->     
        <table class="table table-hover">
            <tr><td style="text-align: center;">
                <form action="#" id="businessTypeInput_form">
                    <span class="control-label">行业</span>
                    <select class="m-wrap" style="margin-right:60px;" id="businessType" <#if apply.state == 13 > disabled="disabled"</#if> name="businessType">
                        <option value =0>请选择</option>
                        <#list industryList as industry>
                            <option value =${industry.id}>${industry.nodeName}</option>
                        </#list>
                    </select>
                    <span class="control-label">利润</span>
                    <input type="text" name="profits" id="profits" style="margin-right:60px;" <#if apply.state == 13 > readonly</#if> class="m-wrap medium input-tex requiredMemberType"/>
                    <span class="control-label">占股率</span>
                    <input type="text" name="shareRatio"  id="shareRatio" <#if apply.state == 13 > readonly</#if> class="m-wrap medium input-tex requiredMemberType" />
                    <span class="control-label">%</span>
                    <input type="hidden" name="debtRratio" id="debtRratio" />
                    <input type="hidden" name="applyLoanNo" id="applyLoanNo" value='${apply.applyLoanNo}'/>
                </form>
            </td></tr>
            <tr><td>
                <table class="table table-striped table-bordered table-hover" id="contentTable">
                    <h4 style="text-align: center;">现金与流量计算表</h4>
                    <thead>
                        <tr>
                            <th style="width:25%; text-align: center;">客户收入与负债信息</th>
                            <th style="width:26%; text-align: center;">纳入标准</th>
                            <th style="width:7%; text-align: center;">近一个月</th>
                            <th style="width:7%; text-align: center;">近二个月</th>
                            <th style="width:7%; text-align: center;">近三个月</th>
                            <th style="width:7%; text-align: center;">近四个月</th>
                            <th style="width:7%; text-align: center;">近五个月</th>
                            <th style="width:7%; text-align: center;">近六个月</th>
                            <th style="width:7%; text-align: center;">月平均</th>
                        </tr>
                    </thead>
                    <style>
                    .input-tex{width:100%;box-sizing: border-box;height:30px !important;text-align: center;}
                    #contentTable td{text-align: center;vertical-align:middle}
                    </style>
                    <tbody>
                    <form action="#" id="auditCashCountInput_form">
                    <!--终审13 初审11,终审时不可更改初审信息，初审时不显示终审应填项-->
                        <#list earningsSysNodeResult as earnings>
                        <#assign  keys = earnings?keys/>
                        <input type="hidden" name="applyLoanNo" id="applyLoanNo" value='${apply.applyLoanNo}'/>
                        <input type="hidden" name="liabilitiesTypeNo" id="liabilitiesTypeNo" value='${earnings.id}'/>
                        <input type="hidden" name="nodeCode" id="nodeCode" value='${earnings.nodeCode}'/>
                        <input type="hidden" name="state" id="state" value='${apply.state}'/>
                            <tr id=${(earnings.id)!} class="${(earnings.nodeCode)!}" style="<#if !lastAuditor?? && (earnings.nodeCode == 'earningsType3' || earnings.nodeCode == 'earningsType12')>display: none;</#if>">
                                <td>${(earnings.nodeName)!}</td>
                                <td>${(earnings.nodeDesc)!}</td>
                                <td><input type="text" name="oneMonth" id="oneMonth" class="input-tex one <#if earnings.mustFill == 1>requiredInput</#if>" onblur="checkInput('^[\-\.0-9]*$',this)" 
                                        <#if earnings.nodeCode == 'earningsType4' || earnings.nodeCode == 'earningsType7' || earnings.nodeCode == 'earningsType8' || earnings.nodeCode == 'earningsType9' || earnings.nodeCode == 'earningsType13' || earnings.nodeCode == 'earningsType14'> readonly</#if>
                                        <#if lastAuditor?? && earnings.nodeCode != 'earningsType3' && earnings.nodeCode != 'earningsType12'> readonly</#if>
                                        /></td>
                                <td><input type="text" name="twoMonth" id="twoMonth" class="input-tex two <#if earnings.mustFill == 1>requiredInput</#if>" onblur="checkInput('^[\-\.0-9]*$',this)" 
                                        <#if earnings.nodeCode == 'earningsType4' || earnings.nodeCode == 'earningsType7' || earnings.nodeCode == 'earningsType8' || earnings.nodeCode == 'earningsType9' || earnings.nodeCode == 'earningsType13' || earnings.nodeCode == 'earningsType14'> readonly</#if>
                                        <#if lastAuditor?? && earnings.nodeCode != 'earningsType3' && earnings.nodeCode != 'earningsType12'> readonly</#if>
                                        /></td>
                                <td><input type="text" name="threeMonth" id="threeMonth" class="input-tex three <#if earnings.mustFill == 1>requiredInput</#if>" onblur="checkInput('^[\-\.0-9]*$',this)" 
                                        <#if earnings.nodeCode == 'earningsType4' || earnings.nodeCode == 'earningsType7' || earnings.nodeCode == 'earningsType8' || earnings.nodeCode == 'earningsType9' || earnings.nodeCode == 'earningsType13' || earnings.nodeCode == 'earningsType14'> readonly</#if>
                                        <#if lastAuditor?? && earnings.nodeCode != 'earningsType3' && earnings.nodeCode != 'earningsType12'> readonly</#if>
                                        /></td>
                                <td><input type="text" name="fourMonth" id="fourMonth" class="input-tex four <#if earnings.mustFill == 1>requiredInput</#if>" onblur="checkInput('^[\-\.0-9]*$',this)" 
                                        <#if earnings.nodeCode == 'earningsType4' || earnings.nodeCode == 'earningsType7' || earnings.nodeCode == 'earningsType8' || earnings.nodeCode == 'earningsType9' || earnings.nodeCode == 'earningsType13' || earnings.nodeCode == 'earningsType14'> readonly</#if>
                                        <#if lastAuditor?? && earnings.nodeCode != 'earningsType3' && earnings.nodeCode != 'earningsType12'> readonly</#if>
                                        /></td> 
                                <td><input type="text" name="fiveMonth" id="fiveMonth" class="input-tex five <#if earnings.mustFill == 1>requiredInput</#if>" onblur="checkInput('^[\-\.0-9]*$',this)" 
                                        <#if earnings.nodeCode == 'earningsType4' || earnings.nodeCode == 'earningsType7' || earnings.nodeCode == 'earningsType8' || earnings.nodeCode == 'earningsType9' || earnings.nodeCode == 'earningsType13' || earnings.nodeCode == 'earningsType14'> readonly</#if>
                                        <#if lastAuditor?? && earnings.nodeCode != 'earningsType3' && earnings.nodeCode != 'earningsType12'> readonly</#if>
                                        /></td>
                                <td><input type="text" name="sixMonth" id="sixMonth" class="input-tex six <#if earnings.mustFill == 1>requiredInput</#if>" onblur="checkInput('^[\-\.0-9]*$',this)" 
                                        <#if earnings.nodeCode == 'earningsType4' || earnings.nodeCode == 'earningsType7' || earnings.nodeCode == 'earningsType8' || earnings.nodeCode == 'earningsType9' || earnings.nodeCode == 'earningsType13' || earnings.nodeCode == 'earningsType14'> readonly</#if>
                                        <#if lastAuditor?? && earnings.nodeCode != 'earningsType3' && earnings.nodeCode != 'earningsType12'> readonly</#if>
                                        /></td>                               
                                <td class="avg"></td>
                            </tr>
                        </#list>
                    </form>
                    </tbody>
                </table>
                <span class="control-label">收入负债比率</span>
                <input type="text" name="debtRratioRel" id="debtRratioRel"
                    class="m-wrap medium" readonly/>
                <span class="control-label" style="margin-right:220px;" >%</span>
            </td></tr>
            <tr><td style="text-align: center;">
                <h4 style="text-align: center;">资产信息</h4>
                <div>
                    <span class="control-label">房产总数量</span>
                    <input type="text" name="houseNum" id="houseNum" style="margin-right:220px;" 
                        class="m-wrap medium" readonly/>
                    <span class="control-label">按揭数量</span>
                    <input type="text" name="loanHouseNum" id="loanHouseNum" style="margin-right:220px;" 
                        class="m-wrap medium" readonly/>
                    <span class="control-label">按揭金额</span>
                    <input type="text" name="loanHouseAmount" id="loanHouseAmount" style="margin-right:220px;" 
                        class="m-wrap medium" readonly/>
                </div>
                <div>
                    <span class="control-label">车辆总数量</span>
                    <input type="text" name="carNum" id="carNum" style="margin-right:220px;" 
                        class="m-wrap medium" readonly/>
                    <span class="control-label">按揭数量</span>
                    <input type="text" name="loanCarNum" id="loanCarNum" style="margin-right:220px;" 
                        class="m-wrap medium" readonly/>
                    <span class="control-label">按揭金额</span>
                    <input type="text" name="loanCarAmount" id="loanCarAmount" style="margin-right:220px;" 
                        class="m-wrap medium" readonly/>
                </div>   
            </td></tr>
            <#if !cType?? || cType!='1' >
            <tr><td style="text-align: center;">
                <input type="button" id='save' name='save' class="btn blue" value="保存" onclick="return save()"></input>
            </td></tr>
            </#if>
        </table>
    </div>
</div>
<#include "/apply/approve/bottom.ftl">