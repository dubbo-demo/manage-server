	<!-- header -->
	<div class="container-fluid">
		<div>
			<div class="row-fluid">
				<div class="portlet box blue">
					<div class="portlet-title">
						<div class="caption">
							<i class="icon-reorder"></i>
							<#if subState == 1510>
								合规意见		
						    <#else>
						    	信审意见
						    </#if>
						</div>
					</div>
					<div class="portlet-body form">
						<!-- edit form -->
						<form id="tab" action="#" method="POST"
							class="form-horizontal">
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">审核结果</label>
									<div class="controls">
										<span class="text">
											<#if subState == 1510>
						                                                                        回退
										    <#else>
										    	通过
										    </#if>
										</span>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<#if subState == 1510>
										<label class="control-label">合规意见</label>
										<div class="controls">
											<textarea rows="2" readonly="readonly" class="m-wrap span20">${(compliance.result)!''}</textarea>
										</div>			
								    <#else>
								    	<label class="control-label">审核意见</label>
										<div class="controls">
											<textarea rows="2" readonly="readonly" class="m-wrap span20">${(auditTask.externalRemark)!''}</textarea>
										</div>	
								    </#if>
								</div>
							</div>
  							<hr>
							<!--<input type="hidden"  name="id" value='${(visit.id)!''}' />-->
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
										<span class="text">${(contractNo)!}</span>
										<input type="hidden"  name="contractNo" value='${(contractNo)!}' />
										<input type="hidden"  name="productType" value='${(productDto.id)!}' />
										<input type="hidden"  name="applyLoanNo" value='${(applyLoanNo)!}' />
										<input type="hidden"  name="periods" value='${(appInfo.loanPeriods)!}' />
										<input type="hidden"  name="productId" value='${(productDto.id)!}' />
									</div>
								</div>
							</div>

							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">借款用途</label>
									<div class="controls">
										<span class="text">${(appInfo.loanPurposes)!}</span>
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
										<span class="text">${(applyUserDto.liveAddr)!}${(applyUserDto.liveAddress)!}</span>
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
										<input id="repayMoneyID" type="text" name="repayMoney" class="span9 m-wrap js_input" value="${(jkContractDto.repayMoney)!}" onblur = "getServiceRate()" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"/>								
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">综合服务费</label>
									<div class="controls">
										<span class="text" id = "serviceRate">${(jkContractDto.serviceRate)!}</span>
										<input type="hidden"  name="serviceRate" value='${(jkContractDto.serviceRate)!}' />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">产品名称</label>
									<div class="controls">
										<span class="text">${productName!''}</span>
										<input type="hidden"  name="productName" value='${productName!''}' />
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">贷款期数</label>
									<div class="controls">
										<span class="text">${(appInfo.loanPeriods)!}</span>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">银行类别</label>
									<div class="controls">
										<select name="bankType" class="m-wrap span9  js_input" data-id="${(jkContractDto.bankType)!"0"}">
											<option value="">请选择</option>
										</select>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">放款日期</label>
									<div id="loantime_div_id" class="controls">
										<div class="input-append date date-picker" data-date="${(appInfo.loanTime?string("yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
											<input id="loanTime" name="loanTime" class="m-wrap span9 date-picker loantime_js_input" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(appInfo.loanTime?string("yyyy-MM-dd"))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
										</div>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">银行卡号</label>
									<div class="controls">
										<input type="text" class="span9 m-wrap js_input" value="${(jkContractDto.bankCardNo)!}" name="bankCardNo" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"/>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">开户城市</label>
									<div class="controls">
										<input type="text" class="span9 m-wrap js_input" id = "bankCityID" name="bankCity" value="${(jkContractDto.bankCity)!'请选择'}" readonly />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">开户行</label>
									<div class="controls">
										<input type="text" class="span9 m-wrap  js_input" value="${(jkContractDto.bankName)!}" name="bankName" />
									</div>
								</div>   
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">面签结果</label>
									<div class="controls">
										<select name="signResult" class = "span9 m-wrap  js_input">
											<option value="">请选择</option>
											<option value="通过">通过</option>
											<option value="拒绝">拒绝</option>
										</select>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">备注</label>
									<div class="controls">
										<textarea name="remark" rows="4" class="m-wrap span9  js_input" value = "">${(jkSignDto.remark)!}</textarea>
									</div>
								</div>
							</div>
						</form>
						<div class="form-actions">
							<input type="button" class="btn blue" onclick="exportMain(event)"
								value="导出" />						
							<input type="button" class="btn blue" onclick="upload(event)"
								value="上传附件" />
							<input type="button" class="btn blue" onclick="save(event)"
								value="保存" />
							<input type="button" class="btn blue" onclick="submit(event)"
								value="提交" /> 
						    <a href="javascript:page_back('${serverPath}/sign/list.htm')" class="btn">返回</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div id="printList" class="modal hide fade" tabindex="-1" data-width="560">
			<div class="modal-header">&nbsp;
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
			</div>
			<div class="modal-body" style="padding-left:25px">
				<div class="row-fluid">
					<div>
						<label class="help-inline text-left span8"><a href="javascript:void(0)" onclick="printMain(event,1)">1.信用贷借款协议</a></label>
					</div>
					<div>
						<label class="help-inline text-left span8"><a href="javascript:void(0)" onclick="printMain(event,2)">2.信用贷信用咨询及管理服务协议</a></label>
					</div>
					<div>
						<label class="help-inline text-left span8"><a href="javascript:void(0)" onclick="printMain(event,3)">3.按期还款温馨提示</a></label>
					</div>
					<div>
						<label class="help-inline text-left span8"><a href="javascript:void(0)" onclick="printMain(event,4)">4.富友账户专用协议</a></label>
					</div>
					<div>
						<label class="help-inline text-left span8"><a href="javascript:void(0)" onclick="printMain(event,5)">5.客户签约核查表</a></label>
					</div>
					<div>
						<label class="help-inline text-left span8"><a href="javascript:void(0)" onclick="printMain(event,6)">6.委托扣款授权书</a></label>
					</div>
				</div>
				<input id = "applyLoanNoId" type = "hidden" value = ""/>
			</div>
		</div>
		
	</div>
<script>
	var applyLoanNo = "${applyLoanNo!''}";
	var isUpLoanLimit = false;
	var isFlag = false;
	$(function(){
		$("select[name='signResult']").val("${(jkSignDto.signResult)!''}");
		var date = new Date();
		date.setDate(date.getDate()-1);//设置天数 -1 天 
	    $('.date-picker').datepicker({
	        dateFormat: 'yy-mm-dd',
	        showMeridian: true,
	        autoclose: true,
	        startDate: date
	    }).on('hide',function(e) {
			var _val = e.date;
			if(_val != "") {
				$('#loanTime').removeClass("error");
				$('.loantime_class').remove();
			}
	    });
	    $("#bankCityID").click(function(){
			$('#bankCityID').removeClass("error");
			$('.bankcity_class').remove();
		});
		showCitys("#bankCityID","${serverPath}");
		initProductData();
		
		$("form").validate({
			rules : {
				repayMoney : {
					required : true
				},
				bankType : {
					required : true
				},
				bankName : {
					required : true
				},
				bankCardNo : {
					required : true
				},
				signResult : {
					required : true
				},
				remark : {
					required : true
				}
			}
		});
	});
	
	function getServiceRate() {
		$('.repayMoneyUpMsg').remove();
		var reapyMoney = $('#tab input[name=repayMoney]').val();
		if (ChkUtil.isNull(reapyMoney)) {
			return false;
		}
		var productType = $('#tab  input[name=productType]').val();
		$.ajax({
			url : '${serverPath}/sign/getServiceRate.htm',
			type : 'post',
			data : {
						reapyMoney : reapyMoney,
						applyLoanNo : applyLoanNo,
						productType: productType
					},
			dataType : 'json',
			success : function(res) {
				if (res.success) {
					isUpLoanLimit = true;
					$('#serviceRate').text(res.data);
				} else {
					var msg = res.message;
					$('#repayMoneyID').addClass('error');
					$('#repayMoneyID').parent().append("<label class='error repayMoneyUpMsg'>"+msg+"</label>");
					return false;
				}
			},
			error:function(){
				BootstrapDialog.alert("系统异常");
			}
		});
	}
	
	function exportMain(event){
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
		var f1 = false;
		var form = $("#tab");
		if (!form.valid()) {
			$("#printList").modal('hide');
			f1 = true;
		}
		var f2 = false;
		$(".loantime_js_input").each(function(i,v){
			var _val = $(this).val();
			if(_val == "" || _val == -1 || _val == '请选择') {
				$(this).addClass("error");
				$('#loantime_div_id').append("<label class='error loantime_class'>这是必填字段</label>");
				f2 = true;
			}else{
				$(this).removeClass("error");
			}
		});
		var f3 = false;
		var bankCity = $('#bankCityID').val();
		if(bankCity == "" || bankCity == '请选择') {
			$('#bankCityID').addClass("error");
			$('#bankCityID').parent().append("<label class='error bankcity_class'>这是必填字段</label>");
			f3 = true;
		} else {
			$('#bankCityID').removeClass("error");
			$('#bankcity_class').remove();
		}
		
		if(f1 == true || f2 == true || f3 == true) {
			return false;
		}
		var isGo = false;
		$('.repayMoneyUpMsg').remove();
		var reapyMoney = $('#tab input[name=repayMoney]').val();
		$.ajax({
			url : '${serverPath}/sign/checkLoanUpLimit.htm',
			type : 'post',
			data : {
						reapyMoney : reapyMoney,
						applyLoanNo : applyLoanNo
					},
			dataType : 'json',
			async: false,
			success : function(res) {
				if (res.success) {
					isGo = true;
				} else {
					var msg = res.message;
					$('#repayMoneyID').addClass('error');
					$('#repayMoneyID').parent().append("<label class='error repayMoneyUpMsg'>"+msg+"</label>");
					return false;
				}
			},
			error:function(){
				BootstrapDialog.alert("系统异常");
			}
		});
		if(!isGo) {
			return false;
		}
		$("#printList").modal('show');
	}
	
	function printMain(event,type) {
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
		print(event,type);
	}
	function print(event,type) {
		var bankNo = $('#tab  input[name=bankCardNo]').val();
		bankNo = encodeURI(encodeURI(bankNo));
		var bankName = $('#tab  input[name=bankName]').val();
		bankName = encodeURI(encodeURI(bankName));
		var repayMoney = $('#tab  input[name=repayMoney]').val();
		repayMoney = encodeURI(encodeURI(repayMoney));
		var loanTime = $('#tab  input[name=loanTime]').val();
		loanTime = encodeURI(encodeURI(loanTime));
		var productName = $('#tab  input[name=productName]').val();
		productName = encodeURI(encodeURI(productName));
		window.open("${serverPath}/sign/print.htm?applyLoanNo=${applyLoanNo!''}&type="+type+"&contractNo=${contractNo!''}&repayMoney="+repayMoney+"&loanTime="+loanTime+"&bankNo="+bankNo+"&bankName="+bankName+"&productName="+productName,"_blank");
	}
	
	function checkUploadFile(applyLoanNo) {
		$.ajax({
			url : '${serverPath}/productFile/checkFile.htm',
			type : 'post',
			data : {
					applyLoanNo : applyLoanNo
				   },
			dataType : 'json',
			async: false,
			success : function(res) {
				if (res.success) {
					if(res.data) {
						isFlag = res.data;
					} else {
						BootstrapDialog.alert("合同附件没有提交，请提交合同附件!");
					}
				} else {
					BootstrapDialog.alert("合同附件没有提交，请提交合同附件!");
				}
			},
			error:function() {
				BootstrapDialog.alert("系统异常");
			}
		});
	}
	
	function save(event) {
		var isGo = false;
		$('.repayMoneyUpMsg').remove();
		var reapyMoney = $('#tab input[name=repayMoney]').val();
		$.ajax({
			url : '${serverPath}/sign/checkLoanUpLimit.htm',
			type : 'post',
			data : {
						reapyMoney : reapyMoney,
						applyLoanNo : applyLoanNo
					},
			dataType : 'json',
			async: false,
			success : function(res) {
				if (res.success) {
					isGo = true;
				} else {
					var msg = res.message;
					$('#repayMoneyID').addClass('error');
					$('#repayMoneyID').parent().append("<label class='error repayMoneyUpMsg'>"+msg+"</label>");
					return false;
				}
			},
			error:function(){
				BootstrapDialog.alert("系统异常");
			}
		});
		if(!isGo) {
			return false;
		}
		ChkUtil.stopBubbleEvent(event);
		$.ajax({
			url : '${serverPath}/sign/save.htm',
			type : 'post',
			data : $('#tab').serialize(),
			dataType : 'json',
			success : function(res) {
				if (res.success) {
					BootstrapDialog.alert("保存成功！");
				} else {
					BootstrapDialog.alert(res.message);
				}
			},
			error:function(){
				BootstrapDialog.alert("系统异常");
			}
		});
	}
	
	function submit(event){
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
		var f1 = false;
		var form = $("#tab");
		if (!form.valid()) {
			f1 = true;
		}
		var f2 = false;
		$(".loantime_js_input").each(function(i,v){
			var _val = $(this).val();
			if(_val == "" || _val == -1 || _val == '请选择') {
				$(this).addClass("error");
				$('#loantime_div_id').append("<label class='error loantime_class'>这是必填字段</label>");
				f2 = true;
			}else{
				$(this).removeClass("error");
			}
		});
		var f3 = false;
		var bankCity = $('#bankCityID').val();
		if(bankCity == "" || bankCity == '请选择') {
			$('#bankCityID').addClass("error");
			$('#bankCityID').parent().append("<label class='error bankcity_class'>这是必填字段</label>");
			f3 = true;
		} else {
			$('#bankCityID').removeClass("error");
			$('#bankcity_class').remove();
		}
		
		if(f1 == true || f2 == true || f3 == true) {
			return false;
		}
		var isGo = false;
		$('.repayMoneyUpMsg').remove();
		var reapyMoney = $('#tab input[name=repayMoney]').val();
		$.ajax({
			url : '${serverPath}/sign/checkLoanUpLimit.htm',
			type : 'post',
			data : {
						reapyMoney : reapyMoney,
						applyLoanNo : applyLoanNo
					},
			dataType : 'json',
			async: false,
			success : function(res) {
				if (res.success) {
					isGo = true;
				} else {
					var msg = res.message;
					$('#repayMoneyID').addClass('error');
					$('#repayMoneyID').parent().append("<label class='error repayMoneyUpMsg'>"+msg+"</label>");
					return false;
				}
			},
			error:function(){
				BootstrapDialog.alert("系统异常");
			}
		});
		if(!isGo) {
			return false;
		}
		checkUploadFile(applyLoanNo);
		if(isFlag) {
			$.ajax({
				url : '${serverPath}/sign/submitSign.htm',
				type : 'post',
				data : $('#tab').serialize(),
				dataType : 'json',
				success : function(res) {
					if (res.success) {
						var signResult = $('#tab select[name=signResult]').val();
						if("拒绝" == signResult) {
							BootstrapDialog.alert("签约拒绝",function() {
								window.location.href = '${serverPath}' + '/sign/list.htm';							
							});
						} else {
							BootstrapDialog.alert("提交合规审核成功",function() {
								window.location.href = '${serverPath}' + '/sign/list.htm';							
							});
						}
						
					} else {
						BootstrapDialog.alert(res.message);
					}
				},
				error:function(){
					BootstrapDialog.alert("系统异常");
				}
			});
		}
	}
	
	function upload(event){
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
		window.open("${serverPath}/productFile/fileUpload.htm?applyLoanNo=${applyLoanNo!''}","_blank");
	}	
	
	
	function page_back(url) {
		window.location.href = url+"?"+ChkUtil.getCookie("queryParams");
	}
	
	function initProductData() {
		$.ajax({
			url : serverPath + "/node/selectNodeList.htm",
			type : "post",
			data : {
				"parentCode" : "bankType",
				"Time" : new Date().getMilliseconds()
			},
			dataType : "json",
			success : function(result) {
				if (result.code == 0) {
					// 清空除第一条内容的外的其它数据
					var select_ = $("select[name='bankType']");
					select_.find("option:gt(0)").remove();
					for (var i = 0; i < result.data.length; i++) {
						
						var isSelected = result.data[i].id == select_
								.attr('data-id') ? "selected='selected'" : "";
						select_.append("<option " + isSelected + " value='"
								+ result.data[i].id + "'>"
								+ result.data[i].nodeName + "</option>");
					}
				} else {
					BootstrapDialog.alert(result.message);
				}
			},
			error : function() {
				BootstrapDialog.alert("操作失败");
			}
		});
	}
</script>