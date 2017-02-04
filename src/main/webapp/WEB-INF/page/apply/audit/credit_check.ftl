<!-- tab层 start -->
<#include "/apply/approve/top.ftl">
<script>
	$(function(){
		//选中一级目录
		$("li[name='tab']:eq(1)").addClass("active");
		//选中二级目录
		$("li[name='two_li']:eq(1)").addClass("active");
		<#if cType??&&cType!="">
			//input,text,select
			$("input[type='input']").attr("disabled","disabled");
			$("input[type='number']").attr("disabled","disabled");
			$("input[type='text']").attr("disabled","disabled");
			$("select,textarea").attr("disabled","disabled");
			// 按钮
			$(".form-horizontal .blue").css('display','none');
		</#if>
	});
	function goHref(e) {
		window.location.href=e+$("#applyLoanNo").val()+"&cType="+$("#cType").val();
	}
	function checkInput(patrn, obj) {
		obj.value = obj.value.replace(/(^\s*)|(\s*$)/g, "");// 删除二边空格
		var objExp = new RegExp(patrn);
		if (!objExp.test(obj.value)) {
			BootstrapDialog.alert("输入格式错误,请重新输入",
					function(){
				setTimeout(function() {
					obj.focus();
				}, 0);
				
			});
		}
		return true;
	}
</script>
<input type="hidden" name="cType" id="cType" value="${cType!}" />
<#include "/apply/audit/common/two_tab.ftl">
<!-- tab层 end -->
<style>
	.tabbable .error{border-color: #b94a48; }
	.tabbable .required{color:#e02222;}
</style>
<div class="">
	<div class="">
		<div>
			<div class="tabbable tabbable-custom tabbable-custom-profile">
				<form id="creditForm" action="#" class="form-horizontal" method="post">
				<input type="hidden" value="${applyLoanNo!''}" id="applyLoanNo" name="applyLoanNo" />
				<!-- table -->
				<table class="table table-hover"  border="1" bordercolor="#ddd" >
					<tr>
						<td colspan="3" >
							<div class='text-center'>征信核查</div>
						</td>
					</tr>
					<tr>
						<td  style="border-right:1px solid #ddd;width:150px">
							<span class="">征信核查文件</span>
						</td>
						<td   colspan="2">
							<span><a target="_Blank" href="${ctx}/productFile/fileUpload.htm?applyLoanNo=${applyLoanNo!''}&isView=0">查看附件</a></span>
						</td>
					</tr>
					
					<tr>
						<td colspan="" style="border-right:1px solid #ddd;vertical-align: middle;">
							<span class="">白户类型</span>
						</td>
						<td  colspan="2">
						<div class="controls" style="margin-left:5px">
							<label class="radio">
								<input type="radio"
									checked = "checked"
									name="userType"	value="1" onclick=""/>
								本地白户
							</label>
							<label class="radio">
								<input type="radio"
									name="userType"	value="2"	onclick=""/>
								外地白户
							</label>
							<label class="radio">
								<input type="radio"
									name="userType" value="3" onclick=""/>
								非白户
							</label>
							</div>
						</td>
					</tr>
					
					<tr rowspan=""> 
						<td colspan="" style="border-right:1px solid #ddd;vertical-align: middle;">
							<span class="">开户数</span>
						</td>
						<td style=" width:10px;"></td>
						<td >
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">住房贷款户数</span>
								<input type="text" maxlength = "10" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value="${(jkAuditCheckDto.houseLoanSum)!0}" name="houseLoanSum" />
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">其他贷款户数</span>
								<input type="text" maxlength = "10" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value="${(jkAuditCheckDto.otherLoanSum)!0}" name="otherLoanSum" />
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">信用卡数</span>
								<input type="text" maxlength = "10" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value="${(jkAuditCheckDto.creditCardSum)!0}" name="creditCardSum" />
								<span class="required">*</span>
							</div>
						</td>							
					</tr>
					
					<tr>
						<td colspan="" style="border-right:1px solid #ddd; vertical-align: middle;">
							<span class="">状态</span>
						</td>
						<td></td>
						<td >
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">帐号状态（呆账、止付、核销、冻结）</span>
								<input type="text" maxlength = "100"  class="span3 m-wrap js_input" value="${(jkAuditCheckDto.accountState)!''}" name="accountState" />
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">近24期还款记录出现D、G、Z</span>
								<input type="text"  maxlength = "100" class="span3 m-wrap js_input" value="${(jkAuditCheckDto.dgzCount)!''}" name="dgzCount" />
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">担保贷款五级分类为次级、可疑或损失</span>
								<input type="text"  maxlength = "100" class="span3 m-wrap js_input" value="${(jkAuditCheckDto.securityLevel)!''}" name="securityLevel" />
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">个人消费性贷款及个人经营性贷款五级分类为次级、可疑或损失</span>
								<input type="text" maxlength = "100"  class="span3 m-wrap js_input" value="${(jkAuditCheckDto.selfSecurityLevel)!''}" name="selfSecurityLevel" />
								<span class="required">*</span>
							</div>
						</td>
					</tr>
					<tr >
						<td rowspan="2" style="border-right:1px solid #ddd;vertical-align: middle;">
							<span class="">逾期</span>
						</td>
						
						<td colspan="" style="border-right:1px solid #ddd;vertical-align: middle;">
								<span class="text-left span1">信用卡</span>
								</td>
						<td>
								<div style="padding-bottom:15px">
									<span class="span5 text-right" style="line-height:34px;padding-right:10px ">当前逾期金额</span>
									<input type="text" maxlength = "10" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value="${(jkAuditCheckDto.creditDemurrageMoney)!0}" name="creditDemurrageMoney" />
									<span class="required">*</span>								
								</div>
								<div style="padding-bottom:15px">
									<span class="span5 text-right" style="line-height:34px;padding-right:10px ">任一单笔信用卡最近6个月内最高逾期次数累计在3（含）以上</span>
									<input type="text" class="span3 m-wrap js_input" value="${(jkAuditCheckDto.creditSixMOverdueThree)!0}" name="creditSixMOverdueThree" maxlength = "10" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"/>
									<span class="required">*</span>								
								</div>
								<div style="padding-bottom:15px">
									<span class="span5 text-right" style="line-height:34px;padding-right:10px ">最近6个月内最高逾期期数2（含）以上</span>
									<input type="text" class="span3 m-wrap js_input" value="${(jkAuditCheckDto.creditSixMOverdueTwo)!0}" name="creditSixMOverdueTwo" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"/>
									<span class="required">*</span>								
								</div>
								<div style="padding-bottom:15px">
									<span class="span5 text-right" style="line-height:34px;padding-right:10px ">最近12个月内最高逾期期数3（含）以上</span>
									<input type="text" class="span3 m-wrap js_input" value="${(jkAuditCheckDto.creditOneMOverdueThree)!0}" name="creditOneMOverdueThree" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"/>
									<span class="required">*</span>								
								</div>
								<div style="padding-bottom:15px">
									<span class="span5 text-right" style="line-height:34px;padding-right:10px ">最近1-24月内最高逾期期数4、5、6、7（含）以上</span>
									<input type="text" class="span3 m-wrap js_input" value="${(jkAuditCheckDto.creditTwoMOverdueSeven)!0}" name="creditTwoMOverdueSeven" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"/>
									<span class="required">*</span>								
								</div>									
							
							</td>
							</tr>
							<tr>
							<td style="border-right:1px solid #ddd; vertical-align: middle;"><span class="text-center span1">贷款</span></td>
							
							<td>
								<div style="padding-bottom:15px">
									<span class="span5 text-right" style="line-height:34px;padding-right:10px ">当前逾期金额</span>
									<input type="text" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.loanDemurrageMoney)!0}" name="loanDemurrageMoney" />
									<span class="required">*</span>								
								</div>
								<div style="padding-bottom:15px">
									<span class=" span5 text-right" style="line-height:34px;padding-right:10px ">最近3个月内出现4笔以上非我司单笔5万元以上的信用或保证贷款</span>
									<input type="text" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value="${(jkAuditCheckDto.loanThreeMEnsureLoan)!0}" name="loanThreeMEnsureLoan" />
									<span class="required">*</span>								
								</div>
								<div style="padding-bottom:15px">
									<span class="span5 text-right" style="line-height:34px;padding-right:10px ">任一单笔贷款最近6个月内最高逾期次数累计在3（含）以上</span>
									<input type="text" class="span3 m-wrap js_input" value="${(jkAuditCheckDto.loanSixMOverueThree)!0}" name="loanSixMOverueThree" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"/>
									<span class="required">*</span>								
								</div>
								<div style="padding-bottom:15px"> 
									<span class="span5 text-right" style="line-height:34px;padding-right:10px ">最近6个月内最高逾期期数2（含）以上</span>
									<input type="text" class="span3 m-wrap js_input" value="${(jkAuditCheckDto.loanSixMOverdueTwo)!0}" name="loanSixMOverdueTwo" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"/>
									<span class="required">*</span>								
								</div>
								<div style="padding-bottom:15px">
									<span class="span5 text-right" style="line-height:34px;padding-right:10px ">近12个月内最高逾期期数3（含）以上</span>
									<input type="text" class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.loanOneMOverdueThree)!0}" name="loanOneMOverdueThree" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"/>
									<span class="required">*</span>								
								</div>
								<div style="padding-bottom:15px">
									<span class="span5 text-right" style="line-height:34px;padding-right:10px ">最近1-24月内最高逾期期数4、5、6、7（含）以上</span>
									<input type="text" class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.loanTwoMOverdueSeven)!0}" name="loanTwoMOverdueSeven" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"/>
									<span class="required">*</span>								
								</div>									
						</td>
					</tr>
					
					<tr>
						<td colspan="" style="border-right:1px solid #ddd; vertical-align: middle;">
							<span class="">未销户贷记卡信息</span>
						</td>
						<td></td>
						<td >
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">有无未销户信用卡(如果没有，下面三项不用填写)</span>
								<select id="unCnacleCreCradAmount" class="m-wrap span3 js_input" name="unCnacleCreCradAmount" onchange="checkIsCancleCreditCard(this)">
										<option value = "">请选择</option>
										<option value="1">有</option>
										<option value="0">无</option>
								</select>
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px " >信用额度之和是否为零(如果是，下面两项不用填写)</span>
								<select id="ifZeroCreditSum" class="m-wrap span3 js_input" name="ifZeroCreditSum" onchange="checkZeroCreditSum(this)">
										<option value = "">请选择</option>
										<option value="1">是</option>
										<option value="0">否</option>
								</select>
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">未销户贷记卡授信总额</span>
								<input id="test1" type="text" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap" value = "${(jkAuditCheckDto.creditAmountSum)!0}" name="creditAmountSum" />
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">未销户贷记卡已用额度</span>
								<input id="test2" type="text" maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap" value = "${(jkAuditCheckDto.creditAmountUseSum)!0}" name="creditAmountUseSum" />
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">最早信用卡发放日期</span>
								<div class="input-append date date-picker" data-date="${(jkAuditCheckDto.pionCardDate?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input id="test3" name="pionCardDate" class="span10 m-wrap date-picker"style="padding:1px !important; " readonly  type="text" data-date-format="yyyy-mm-dd"  value="${(jkAuditCheckDto.pionCardDate?string('yyyy-MM-dd'))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
									</div>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">最近6个月平均使用额度</span>
								<input type="text" class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.sixMavglimit)!0}" name="sixMavglimit" />
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">信用卡额度使用率超过80%</span>
								<input type="text" class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.cardlimitupeighty)!0}" name="cardlimitupeighty" />
								<span class="required">*</span>
							</div>
						</td>
					</tr>
					
					<tr>
						<td colspan="" style="border-right:1px solid #ddd; vertical-align: middle;">
							<span class="">未结清贷款信息</span>
						</td>
						<td></td>
						<td >
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">未结清贷款的笔数</span>
								<input type="text"  maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.noPayLoanNum)!0}" name="noPayLoanNum" />
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">未结清贷款余额</span>
								<input type="text"  maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.noPayRemain)!0}" name="noPayRemain" />
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">最近6个月平均应还款额</span>
								<input type="text" class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.sixAvgRepay)!0}" name="sixAvgRepay" />
								<span class="required">*</span>
							</div>
						</td>
					</tr>
					
					<tr>
						<td colspan="" style="border-right:1px solid #ddd; vertical-align: middle;">
							<span class="">对外担保信息</span>
						</td>
						<td></td>
						<td >
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">担保笔数</span>
								<input type="text"  maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.assureNum)!0}" name="assureNum" />
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">担保金额</span>
								<input type="text"  maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.assureMoney)!0}" name="assureMoney" />
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">剩余金额</span>
								<input type="text"  maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.assureRemain)!0}" name="assureRemain" />
								<span class="required">*</span>
							</div>
						</td>
					</tr>
					
					<tr>
						<td colspan="" style="border-right:1px solid #ddd; vertical-align: middle;">
							<span class="">按揭抵押类</span>
						</td>
						<td></td>
						<td >
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">房产抵押按揭贷款总余额</span>
								<input type="text"  maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.houseMortRemain)!0}" name="houseMortRemain" />
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">房产抵押按揭贷款月供金额</span>
								<input type="text"  maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.houseMPay)!0}" name="houseMPay" />
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">汽车按揭贷款总余额</span>
								<input type="text"  maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.carLoanRemain)!0}" name="carLoanRemain" />
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">汽车按揭贷款月供金额</span>
								<input type="text"  maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.carMPay)!0}" name="carMPay" />
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">其他抵押按揭贷款总余额</span>
								<input type="text"  maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.elseLoanRemain)!0}" name="elseLoanRemain" />
								<span class="required">*</span>
							</div>
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px ">其他抵押按揭贷款月供金额</span>
								<input type="text"  maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.elseLoanMPay)!0}" name="elseLoanMPay" />
								<span class="required">*</span>
							</div>
						</td>
					</tr>
					
					<tr>
						<td colspan="" style="border-right:1px solid #ddd; vertical-align: middle;">
							<span class="">查询次数</span>
						</td>
						<td></td>
						<td >
							<div style="padding-bottom:15px">
								<span class="span5 text-right" style="line-height:34px;padding-right:10px "></span>
								<input type="text"  maxlength = "10"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="span3 m-wrap js_input" value = "${(jkAuditCheckDto.searchNum)!0}" name="searchNum" />
								<span class="required">*</span>
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="3" style=" vertical-align: middle;">
							<div class="text-center" style="background: #fff; border: 0;padding:20px;">
								<button type="button" class="btn blue" onclick="save(event)">保存</button>
							</div>
						</td>
					</tr>
			  	</table>
			  	</form>
			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
	$("#accountState").val("${(jkAuditCheckDto.accountState)!}");
	$("#dgzCount").val("${(jkAuditCheckDto.dgzCount)!}");
	$("#securityLevel").val("${(jkAuditCheckDto.securityLevel)!}");
	$("#selfSecurityLevel").val("${(jkAuditCheckDto.selfSecurityLevel)!}");
	$("#unCnacleCreCradAmount").val("${(jkAuditCheckDto.unCnacleCreCradAmount)!}");
	$("#ifZeroCreditSum").val("${(jkAuditCheckDto.ifZeroCreditSum)!}");
	
    $(":radio[name='userType'][value='" + ${(jkAuditCheckDto.userType)!'1'} + "']").prop("checked", "checked");
    
    //有无未销户信用卡
    function checkIsCancleCreditCard(t) {
    	var isCancleCreditCard = t.value;
    	if(isCancleCreditCard == 1) {
    		$('#test1').removeClass();
    		$('#test1').addClass("span3 m-wrap js_input");
    		$('#test1').parent().append("<span class='required'>*</span>");
    		
    		$('#test2').removeClass();
    		$('#test2').addClass("span3 m-wrap js_input");
    		$('#test2').parent().append("<span class='required'>*</span>");
    		
    		$('#test3').removeClass();
    		$('#test3').addClass("span10 m-wrap date-picker js_input");
    		$('#test3').parent().parent().append("<span class='required'>*</span>");
    	} else if(isCancleCreditCard == 0) {
    		$('#test1').removeClass();
    		$('#test1').addClass("span3 m-wrap");
    		$('#test1').next().remove();
    		
    		$('#test2').removeClass();
    		$('#test2').addClass("span3 m-wrap");
    		$('#test2').next().remove();
    		
    		$('#test3').removeClass();
    		$('#test3').addClass("span10 m-wrap date-picker");
    		$('#test3').parent().next().remove();
    	}
    }
    //有无未销户信用卡
    function checkZeroCreditSum(t) {
    	var zeroCreditSum = t.value;
    	if(zeroCreditSum == 1) {
    		$('#test1').removeClass();
    		$('#test1').addClass("span3 m-wrap js_input");
    		$('#test1').parent().append("<span class='required'>*</span>");
    		
    		$('#test2').removeClass();
    		$('#test2').addClass("span3 m-wrap js_input");
    		$('#test2').parent().append("<span class='required'>*</span>");
    		
    	} else if(zeroCreditSum == 0) {
    		$('#test1').removeClass();
    		$('#test1').addClass("span3 m-wrap");
    		$('#test1').next().remove();
    		
    		$('#test2').removeClass();
    		$('#test2').addClass("span3 m-wrap");
    		$('#test2').next().remove();
    	}
    }
    
	function save(event) {
		var f = false;
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
		$(".form-horizontal .js_input").each(function(i,v) {
			var _val = $(this).val();
			if(_val == ""){
				$(this).addClass("error");
				f = true;
			}else{
				$(this).removeClass("error");
			}
		});
		if(f){
			return false;
		} else {
			$.ajax({
				url : serverPath + "/audit/credit/save.htm",
				type : "post",
				data : $('#creditForm').serialize(),
				dataType : "json",
				success : function(res) {
					BootstrapDialog.alert(res.message);
				},
				error : function(res) {
					BootstrapDialog.alert(res.message);
				}
			});
		}
	}
	function go_todo(){
		$("#searchForm").attr("action", "${ctx}/audit/list/todo.htm");
		$("#searchForm").submit();
	}
	function page_jump(url){
		ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
		window.location.href=url;
	}
	function pickup(event) {
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
		var progress = $('#progress').val();
		var forwardPath = serverPath + "/audit/list/"+progress+".htm";
		$.ajax({
			url : serverPath + "/audit/pickup.htm",
			type : "post",
			data : {
			},
			dataType : "json",
			cache : false,
			success : function(res) {
				BootstrapDialog.alert(res.message,function(){
					if(res.success) {
						window.location.href = forwardPath;
					}
				});
			},
			error : function(res) {
				BootstrapDialog.alert(res.message);
			}
		});
	}
</script>
