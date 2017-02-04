<!-- tab层 start -->
<#include "/apply/approve/top.ftl">
<script>
	$(function(){
		//选中一级目录
		$("li[name='tab']:eq(1)").addClass("active");
		//选中二级目录
		$("li[name='two_li']:eq(0)").addClass("active");
		<#if cType??&&cType!="">
			//input,text,select
			$("input[type='input']").attr("disabled","disabled");
			$("input[type='text']").attr("disabled","disabled");
			$("input[type='radio']").attr("disabled","disabled");
			$("select,textarea").attr("disabled","disabled");
			// 按钮
			$(".form-actions .blue").css('display','none')
		</#if>
	});
	function goHref(e) {
		window.location.href=e+$("#applyLoanNo").val()+"&cType="+$("#cType").val();
	} 
</script>
<input type="hidden" name="cType" id="cType" value="${cType!}" />
<#include "/apply/audit/common/two_tab.ftl">
<!-- tab层 end -->
	<script src="${cdnPath}/js/apply/audit/web/baseinfo_add.js"></script>
			<div class="row-fluid">
				<div class="portlet box blue">
					<div class="portlet-body form">
					<form action="#" method="POST"
							class="form-horizontal">
						<input type="hidden" name="type" id="memberName" value="${apply.memberName!}" />
						<input type="hidden" name="type" id="idcard" value="${apply.idCard!}" />
						<input type="hidden" name="type" id="phone" value="${apply.phone!}" />
						
						<hr>
						<div>
									<h4 style="text-align:center">
										<strong>贷款申请信息</strong>
									</h4>
						</div>
						
						<hr>
						
							<div class="row-fluid">
								<div class="control-group span3 ">
									<label class="control-label">申请单号<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="applyLoanNo" value="${apply.applyLoanNo!}" readonly class="m-wrap" />
									</div>
								</div>
								<div class="control-group span3 ">
									<label class="control-label">产品类型</label>
									<div class="controls">
										<input type="text" name="prodType" value="${productName!}" readonly class="m-wrap" />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span3 ">
									<label class="control-label">申请金额<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="prodType" id="prodType" readonly value="${apply.applyMoney!}" 
										 class="m-wrap" />
									</div>
								</div>
								<div class="control-group span3 ">
									<label class="control-label">贷款期数</label>
									<div class="controls">
										<input type="text" name="loanPeriods" value="${apply.loanPeriods!}" readonly class="m-wrap" />
									</div>
								</div>
							</div>
							</form>
							<hr>
							<div>
								<h4 style="text-align:center" data-ftl="tab_userinfo">
									<strong>资料核实</strong>
								</h4>
							</div>
							<hr>
							<form id="tab" action="#" method="POST"
							class="form-horizontal">
							<input type="hidden" name="applyLoanNo" id="applyLoanNo" value="${apply.applyLoanNo!}" />
								<div class="row-fluid">
									<div class="control-group span3 ">
										<label class="control-label"></label>
									</div>
										&nbsp;&nbsp;<input type="button" class="btn yellow" onclick="javascript:queryFileForm();" value="查看附件" />
									<div class="control-group span3 ">
										<label class="control-label">资料是否完整一致<span class="required">*</span></label>
										<div class="controls">                                                

											<label class="radio">
											<input type="radio" name="dataIfSame" value="1" <#if applyAudit.dataIfSame == 1> checked</#if> />是</label>
											
											<label class="radio">
											<input type="radio" name="dataIfSame" value="2" <#if applyAudit.dataIfSame == 2> checked</#if> />否</label>  

										</div>
									</div>
								</div>
								<div class="row-fluid">
									<div class="control-group">
										<label class="control-label">备注信息</label>
										<div class="controls">
											<textarea class="span9 m-wrap" rows="3" name="dataCheckRemark" maxlength="300">${applyAudit.dataCheckRemark!}</textarea>
										</div>
									</div>
								</div>
								</form>
							<hr>
							<div>
								
								<h4 style="text-align:center" >
									<strong>系统风控</strong>
								</h4>
							</div>
							<hr>
							<div class="form-horizontal">
								<div class="row-fluid">
									<label class="control-label">系统风控数据：</label>
									<#-- <div class="control-group span2 ">
										<label class="control-label">麦芽</label>
										<div class="controls">
											<p style="padding: 6px 6px !important; line-height: 20px;">
												<a href="javascript:void(0);" class="m-wrap">20</a>
											</p>
										</div>
									</div> -->
									<div class="control-group span2 ">
										<a data-toggle="modal" href="#yx_dialog" class="m-wrap">
										<label class="control-label">宜信</label></a>
										<div class="controls">
											<p style="padding: 6px 6px !important; line-height: 20px;">
												<a data-toggle="modal" href="#yx_dialog" id="yx_a" class="m-wrap"></a>
											</p>
										</div>
									</div>
									<div class="control-group span2 ">
									<a data-toggle="modal" href="#qh_dialog" class="m-wrap">
										<label class="control-label">前海</label></a>
										<div class="controls">
											<p style="padding: 6px 6px !important; line-height: 20px;">
												<a data-toggle="modal" href="#qh_dialog" id="qh_a" class="m-wrap"></a>
											</p>
										</div>
									</div>
									<div class="control-group span2">
										<a data-toggle="modal" href="#br_dialog" class="m-wrap">
											<label class="control-label">百融</label></a>
											<div class="controls">
												<p style="padding: 6px 6px !important; line-height: 20px;">
													<a data-toggle="modal" href="#br_dialog" id="br_a" class="m-wrap"></a>
												</p>
											</div>
										
									</div>
									<div class="control-group span1 ">
										<a data-toggle="modal" href="#td_dialog" class="m-wrap">
											<label class="control-label">同盾</label></a>
											<div class="controls">
												<p style="padding: 6px 6px !important; line-height: 20px;">
													<a data-toggle="modal" href="#td_dialog" id="td_a" class="m-wrap"></a>
												</p>
											</div>
										
									</div>
								</div>
							</div>
							<hr>
							<div>
								
								<h4 style="text-align:center">
									<strong>第三方信息查询</strong>
								</h4>
							</div>
							<hr>
							<div id="thrid_tab">
							<form id="tab2" action="#" method="POST"
							class="form-horizontal">
							<#if fileTypeData??>
								<#list fileTypeData as item>
								<div class="row-fluid">
									<div class="control-group span3 ">
										<label class="control-label">${item.directoryName!}</label>
										<div class="controls">        
											<input type="hidden" value="${item.directoryName!}" id="infoSource" name="infoSource">                                        
											<input type="hidden" value="${item.id!}" id="infoSourceNo" name="infoSourceNo">
											<label class="radio">
											<input type="radio" name="status${item_index!}" data-name="status${item_index!}" onchange="toStatus(this)" value="1" checked />正常</label>
											
											<label class="radio">
											<input type="radio" name="status${item_index!}" data-name="status${item_index!}" onchange="toStatus(this)" value="2"  />异常</label>  
											<input type="hidden" value="1" id="status${item_index!}" name="status">
										</div>
									</div>
									<div class="control-group span6 ">
										<label class="control-label"><span class="required">*</span>备注</label>
										<div class="controls"> 
											<textarea class="span9 m-wrap " rows="1" name="remark"  maxlength="300"></textarea>
										</div>
									</div>
									<!--<div class="control-group span2 ">
										<p style="padding: 6px 6px !important; line-height: 20px;">
												<a href="javascript:void(0);" class="m-wrap">查看图片</a>
										</p>
									</div>-->
									<#if cType??&&cType!="">
										<#if item_index == 0>
											<input type="button" class="btn yellow" onclick="javascript:queryFileForm();" value="查看附件" />
										</#if>
									<#else>
										<#if item_index == 0>
											<input type="button" class="btn blue" onclick="javascript:addFileForm();" value="上传附件" />
										</#if>
									</#if>
								</div>
								</#list>
							</#if>
							
							<#if auditThridData??>
								<#list auditThridData as item>
									<div class="row-fluid">
										<div class="control-group span3 ">
											<label class="control-label">${item.infoSource!}</label>
											<div class="controls">   
												<input type="hidden" value="${item.id!}" id="id" name="id">     
												<input type="hidden" value="${item.infoSource!}" id="infoSource" name="infoSource">                                        
												<input type="hidden" value="${item.infoSourceNo!}" id="infoSourceNo" name="infoSourceNo">
												<label class="radio">
												<input type="radio" name="status${item_index!}" 
												data-name="status${item_index!}" onchange="toStatus(this)" value="1" <#if item.status??&&item.status == 1> checked</#if> />正常</label>
												
												<label class="radio">
												<input type="radio" name="status${item_index!}" 
												data-name="status${item_index!}" onchange="toStatus(this)" value="2" <#if item.status??&&item.status == 2> checked</#if>  />异常</label>  
												<input type="hidden" value="${item.status!}" id="status${item_index!}" name="status">
											</div>
										</div>
										<div class="control-group span6 ">
											<label class="control-label"><span class="required">*</span>备注</label>
											<div class="controls"> 
												<textarea class="span9 m-wrap" rows="1" name="remark" maxlength="300">${item.remark!}</textarea>
											</div>
										</div>
										<#if cType??&&cType!="">
											<#if item_index == 0>
												<input type="button" class="btn yellow" onclick="javascript:queryFileForm();" value="查看附件" />
											</#if>
										<#else>
											<#if item_index == 0>
											<input type="button" class="btn blue" onclick="javascript:addFileForm();" value="上传附件" />
											</#if>
										</#if>
										
										<!--<div class="control-group span2 ">
											<p style="padding: 6px 6px !important; line-height: 20px;">
													<a href="javascript:void(0);" class="m-wrap">查看图片</a>
											</p>
										</div>-->
									</div>
								</#list>
							</#if>
							<div class="form-actions" style="text-align:center;">
								<input type="button" class="btn blue" onclick="contactsSave()"
										value="保存" /> 
								<#-- <input type="button" class="btn blue" onclick="save(event,2)"
									value="提交" /> 
							 	<a href="${serverPath}/innerBlack/list.htm" class="btn">返回</a> -->
							</div>
							</form>
							
<#include "/apply/audit/baseinfo/zcxy_dialog.ftl">
<#include "/apply/audit/baseinfo/td_dialog.ftl">
<#include "/apply/audit/baseinfo/br_dialog.ftl">
<#include "/apply/audit/baseinfo/qh_dialog.ftl">
<#include "/apply/approve/bottom.ftl">