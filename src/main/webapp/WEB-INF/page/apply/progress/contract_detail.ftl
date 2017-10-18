	<!-- header -->
	<div class="container-fluid">
		<div>
			<div class="row-fluid">
				<div class="portlet box">
					
					<div class="portlet-body form">

						<!-- edit form -->
						<form id="tab" action="#" method="POST"
							class="form-horizontal">
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">申请单号</label>
									<div class="controls">
										<span class="text">${(applyLoanNo)!}</span>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">贷款合同编号</label>
									<div class="controls">
										<span class="text">${(appInfo.contractNo)!}</span>
									</div>
								</div>
								<div class="control-group span3 ">
									<input type="button" class="btn blue" onclick="viewfile(event)" value="查看附件" />	
								</div>
							</div>

							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">借款用途</label>
									<div class="controls">
										<span class="text" id="loanPurpose" data-value="${(appInfo.loanPurpose)!}"></span>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">客户</label>
									<div class="controls">
										<span class="text">${(appInfo.memberName)!}</span>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">身份证号</label>
									<div class="controls">
										<span class="text">${(appInfo.idCard)!}</span>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">现住址</label>
									<div class="controls">
										<span class="text">${(applyUserDto.liveAddress)!}</span>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">手机号</label>
									<div class="controls">
										<span class="text">${(appInfo.phone)!}</span>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">邮寄地址</label>
									<div class="controls">
										<span class="text">${(applyUserDto.mailAddress)!}</span>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">业务经理</label>
									<div class="controls">
										<span class="text">${(appInfo.bmName)!}</span>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">客服</label>
									<div class="controls">
										<span class="text">${(appInfo.customerSName)!}</span>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">贷款申请金额</label>
									<div class="controls">
										<span class="text">${(appInfo.applyMoney)!}</span>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">信审审批金额</label>
									<div class="controls">
										<span class="text">${(appInfo.auditRatifyMoney)!}</span>
									</div>
								</div>
							</div>								
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">放款金额</label>
									<div class="controls">
										<span class="text">${(jkContractDto.repayMoney)!}</span>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">综合服务费</label>
									<div class="controls">
										<span class="text">${(jkContractDto.serviceRate)!}</span>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">产品名称</label>
									<div class="controls">
										<span class="text">${productName!''}</span>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">贷款期数</label>
									<div class="controls">
										<span class="text">${(appInfo.loanPeriods)!}</span>
									</div>
								</div>
							</div>
							<div class="row-f luid">
								<div class="control-group span4 ">
									<label class="control-label">银行类别</label>
									<div class="controls">
										<span class="text" id="bankType"  data-value="${(jkContractDto.bankType)!}">${(jkContractDto.bankTypeName)!}</span>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">放款日期</label>
									<div class="controls">
										<span class="text">${(appInfo.loanTime?string("yyyy-MM-dd"))!}</span>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">银行卡号</label>
									<div class="controls">
										<span class="text">${(jkContractDto.bankCardNo)!}</span>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">开户城市</label>
									<div class="controls">
										<span class="text">${(jkContractDto.bankCity)!}</span>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">开户行</label>
									<div class="controls">
										<span class="text">${(jkContractDto.bankName)!}</span>
									</div>
								</div>
							</div>
							<#if signList?? && signList?size gt 0>
							<#list signList as signLog>	
							<div class="row-fluid">
								<div class="control-group span3 ">
									<label class="control-label">面签结果</label>
									<div class="controls">
											<span class="text">
											${signLog.signResult}
											</span>
									</div>
								</div>
								<div class="control-group span3 ">
								</div>
								<div class="control-group span3 ">
					        	    <label class="control-label">审核时间</label>
					        	    <div class="controls">
					           		   <span class="text"><#if (signLog.createTime)??>${signLog.createTime?datetime}</#if></span>
					               </div>
					            </div>
							</div>
							<div class="row-fluid">
								<div class="control-group ">
									<label class="control-label">备注</label>
									<div class="controls">
										<textarea name="remark" rows="2" class="m-wrap span9" readonly>${signLog.remark!}</textarea>
									</div>
								</div>
							</div>
							</#list>
							</#if>
						</form>
						
					</div>
				</div>
				<div class="">
							<input type="button" class="btn blue" onclick="exportMain(event)"
								value="合同预览" />						
				</div>
			</div>
		</div>
		
		<div id="printList" class="modal hide fade" tabindex="-1" data-width="560">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
			</div>
			<div class="modal-body">
					<p>
						<label class="help-inline text-left "><a href="javascript:void(0)" onclick="print(event,1)">1.信用贷借款协议</a></label>
					</p>
					<p>
						<label class="help-inline text-left "><a href="javascript:void(0)" onclick="print(event,2)">2.信用贷信用咨询及管理服务协议</a></label>
					</p>
					<p>
						<label class="help-inline text-left "><a href="javascript:void(0)" onclick="print(event,3)">3.按期还款温馨提示</a></label>
					</p>
					<p>
						<label class="help-inline text-left "><a href="javascript:void(0)" onclick="print(event,4)">4.富友账户专用协议</a></label>
					</p>
					<p>
						<label class="help-inline text-left "><a href="javascript:void(0)" onclick="print(event,5)">5.客户签约核查表</a></label>
					</p>
					<p>
						<label class="help-inline text-left "><a href="javascript:void(0)" onclick="print(event,6)">6.委托扣款授权书</a></label>
					</p>
				<input id = "applyLoanNoId" type = "hidden" value = ""/>
			</div>
		</div>
		
	</div>
<script>
	function getNode(xmlId,parentCode) {
		var url = serverPath+"/reception/showProduct.htm";
		var data = {
			"parentCode" : parentCode,
		};
		$.getJSON(url, data, function(result) {
			$(xmlId).empty();
			var resultData = result.data;
			for (var i = 0; i < resultData.length; i++) {
				if($(xmlId).data("value") == resultData[i].id) {
					$(xmlId).html(resultData[i].nodeName);
				} 
			}
		});
	}	
	getNode("#loanPurpose","loanUse");
	getNode("#bankType","bankType");
	function exportMain(event){
		//阻止冒泡
		//ChkUtil.stopBubbleEvent(event);
		$("#printList").modal('show');
	}
	
	function print(event,type) {
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
		var bankNo = '${(jkContractDto.bankCardNo)!}';
		var bankName = '${(jkContractDto.bankName)!}';
		var repayMoney = '${(jkContractDto.repayMoney)!}';
		var loanTime = '${(appInfo.loanTime?string("yyyy-MM-dd"))!}';
		var productName = '${productName!}';
		var contractNo = '${(appInfo.contractNo)!}';
		
		bankNo = encodeURI(encodeURI(bankNo));
		bankName = encodeURI(encodeURI(bankName));
		productName = encodeURI(encodeURI(productName));
		window.open("${serverPath}/sign/print.htm?applyLoanNo=${applyLoanNo!''}&type="+type+"&contractNo="+contractNo+"&repayMoney="+repayMoney+"&loanTime="+loanTime+"&bankNo="+bankNo+"&bankName="+bankName+"&productName="+productName,"_blank");
	
	}
	
	function viewfile(event){
		ChkUtil.stopBubbleEvent(event);
		window.open("${serverPath}/productFile/fileUpload.htm?fileUpState=16&isView=0&applyLoanNo=${(applyLoanNo)!}");
	}
	
</script>