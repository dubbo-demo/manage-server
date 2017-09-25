$(function() {
	$("form").validate({
		rules : {
			employeeName : {
				required : true,
				maxlength : 6
			},
			identityNumber : {
				required : true,
				idCard : true
			},
			mobilePhone : {
				required : true,
				phone : true
			}
		},
		submitHandler : function(form) { // 表单提交句柄,为一回调函数，带一个参数：form
			form.submit(); // 提交表单
		}
	});

	var storeId;
	var orgType = dto.orgType;
	$("#positionId").empty();
	$("#positionId").append("<option value='0'>请选择</option>");
	if ("2" == orgType) {
		storeId = dto.orgId;
	} else {
		storeId = dto.storeId;
	}
	if (storeId.length > 0) {
		var url = serverPath + "/team/initTeamName/" + storeId + ".htm";
		var teamId = dto.teamId;
		var jobLevel = dto.jobLevel;
		var data = {};
		$.ajaxSettings.async = false;
		$.getJSON(url, data, function(result) {
			var resultData = result.data;
			if (resultData.length > 0) {
				$("#teamId").empty();
				$("#teamId").attr("disabled", false);
				$("#teamId").append("<option value=''>请选择</option>");
				for (var i = 0; i < resultData.length; i++) {
					$("#teamId").append(
							"<option value='" + resultData[i].id + "'>"
									+ resultData[i].teamName + "</option>");
				}
				$("#teamId").val(teamId);
				$("#jobLevel").attr("disabled", false);
				$("#jobLevel").empty();
				$("#jobLevel").append("<option value=''>请选择</option>");
				if (resultData.length > 0) {
					$("#jobLevel").append("<option value='一级'>一级</option>");
					$("#jobLevel").append("<option value='二级'>二级</option>");
					$("#jobLevel").append("<option value='三级'>三级</option>");
					$("#jobLevel").append("<option value='四级'>四级</option>");
					$("#jobLevel").append("<option value='五级'>五级</option>");
					$("#jobLevel").val(jobLevel);
				}
			}
		});
	}

	$("#teamId").change(function() {
		$("#jobLevel").empty();
		$("#jobLevel").append("<option value=''>请选择</option>");
		$("#jobLevel").append("<option value='一级'>一级</option>");
		$("#jobLevel").append("<option value='二级'>二级</option>");
		$("#jobLevel").append("<option value='三级'>三级</option>");
		$("#jobLevel").append("<option value='四级'>四级</option>");
		$("#jobLevel").append("<option value='五级'>五级</option>");
	});

});

function edit(event) {
	var flag = true;
	var options = {
			url : serverPath + "/card/queryUserCardInfo.htm",
			type : 'post',
			async:false,
			dataType : 'json',
			data : {
				'phone':$('#mobilePhone').val(),
				"Time" : new Date().getMilliseconds()
			},
			success : function(result) {
				if(result.code == 0){
					var data = result.data;
					if(data != null){
						BootstrapDialog.alert("当前员工有卡信息，请先解绑");
						flag = false;
						return false;
					}
				}
			}
		};
		$.ajax(options);
		if(!flag){
			return false;
		}
	// 阻止冒泡
	ChkUtil.stopBubbleEvent(event);
	var employeeName = $("#employeeName").val();
	var identityNumber = $("#identityNumber").val();
	var mobilePhone = $("#mobilePhone").val();
	var teamId = $("#teamId").val();
	var jobLevel = $("#jobLevel").val();
	if (teamId == "" && jobLevel != "") {
		BootstrapDialog.alert("请选择新团队!");
		return;
	}
	var url = serverPath + "/employee/checkEmployeeInfo.htm";
	var data = {
		"id" : id,
		"identityNumber" : identityNumber,
		"mobilePhone" : mobilePhone,
		"Time" : new Date().getMilliseconds()
	};
	$.ajaxSettings.async = false;
	$.getJSON(url, data, function(result) {
		if ("0" == result.data) {
			$("#tab").submit();
		} else if ("1" == result.data) {
			BootstrapDialog.alert("该身份证号已存在!");
			return;
		} else if ("2" == result.data) {
			BootstrapDialog.alert("该手机号已存在!");
			return;
		}
	});
}

function bindCard(event){
	ChkUtil.stopBubbleEvent(event);
	//校验必填项
	var bankNo = $("#bankNo").find("option:selected").val();
	if(bankNo == '' || $("#bankCardNo").val() == '' || $('#accountBankName').val() == '' || $('#mobile').val() == '' || $('#employeeName').val() == '' || $('#identityNumber').val() == '' ){
		BootstrapDialog.alert("请填写必填项!");
		return;
	}
	//拼装入参调用绑卡接口	
	var options = {
			url : serverPath + "/card/bindCard.htm",
			type : 'post',
			dataType : 'json',
			data : {
				'memberId':$('#mobilePhone').val(),
				'bankCardNo':$('#bankCardNo').val(),
				'accountBankName':$('#accountBankName').val(),
				'bankNo':bankNo,
				'mobile':$('#mobile').val(),
				'accountName':$('#employeeName').val(),
				'idCardNo':$('#identityNumber').val(),		
				"Time" : new Date().getMilliseconds()
			},
			success : function(data) {
				if(data.code == 0){
					BootstrapDialog.alert("绑卡成功！");
					$('.bindCard').hide();
					$('.authentication').show();
					$('.removeBindCard').show();
					//绑卡成功的，相关参数不允许修改，解绑后可修改
					$("#bankNo").attr("disabled",true);
					$('#bankCardNo').attr("readonly","readonly");
					$('#accountBankName').attr("readonly","readonly");
					$('#mobile').attr("readonly","readonly");
				}else{
					BootstrapDialog.alert(data.message);
					$('.bindCard').show();
					$('.authentication').hide();
					$('.removeBindCard').hide();
				}
			},
			error : function() {
				$('.bindCard').show();
				$('.authentication').hide();
				$('.removeBindCard').hide();
			}
		};
		$.ajax(options);	
}

function authentication(event){
	ChkUtil.stopBubbleEvent(event);
	//拼装入参调用绑卡接口
	var options = {
			url : serverPath + "/card/authentication.htm",
			type : 'post',
			dataType : 'json',
			data : {
				'memberId':$('#mobilePhone').val(),
				'bankCardNo':$('#bankCardNo').val(),	
				"Time" : new Date().getMilliseconds()
			},
			success : function(data) {
				if(data.code == 0){
					BootstrapDialog.alert("鉴权成功！");
					$('#authStatus').val('已认证');
					$('.bindCard').hide();
					$('.authentication').hide();
					$('.removeBindCard').show();
				}else{
					BootstrapDialog.alert(data.message);
					$('.bindCard').hide();
					$('.authentication').show();
					$('.removeBindCard').show();
				}
			},
			error : function() {
				$('.bindCard').hide();
				$('.authentication').show();
				$('.removeBindCard').show();
			}
		};
		$.ajax(options);	
}

function removeBindCard(event){
	ChkUtil.stopBubbleEvent(event);
	//解绑前先校验是否可解绑，根据身份证查账单还款记录，有代偿未完结不允许解绑
	var count = 0;
	var options = {
			url : serverPath + "/hKBillRecord/queryCountByIdCardNo.htm",
			type : 'post',
			async:false,
			dataType : 'json',
			data : {	
				"Time" : new Date().getMilliseconds()
			},
			success : function(data) {
				if(data.code == 0){
					count = data.data;
				}else{
					BootstrapDialog.alert(data.message);
				}
			}
		};
		$.ajax(options);
	if(count != 0){
		BootstrapDialog.alert("存在处理中的还款记录，请先结清再解绑");
		return;
	}
	//拼装入参调用绑卡接口
	var options = {
			url : serverPath + "/card/removeBindCard.htm",
			type : 'post',
			async:false,
			dataType : 'json',
			data : {
				'memberId':$('#mobilePhone').val(),
				'bankCardNo':$('#bankCardNo').val(),	
				"Time" : new Date().getMilliseconds()
			},
			success : function(data) {
				if(data.code == 0){
					//解绑成功的，相关参数允许修改
					$("#bankNo").attr("disabled",false);
					$('#bankCardNo').attr("readonly",false);
					$('#accountBankName').attr("readonly",false);
					$('#mobile').attr("readonly",false);
					BootstrapDialog.alert("解绑成功！");
					$('.bindCard').show();
					$('.authentication').hide();
					$('.removeBindCard').hide();
				}else{
					BootstrapDialog.alert(data.message);
					$('.bindCard').hide();
					$('.authentication').show();
					$('.removeBindCard').show();
				}
			},
			error : function() {
				$('.bindCard').hide();
				$('.authentication').show();
				$('.removeBindCard').show();
			}
		};
		$.ajax(options);		
}

function getBankList(){
	var url = serverPath + "/card/getListAll.htm";
	var data = {
	"Time" : new Date().getMilliseconds()
	};
	$.getJSON(url, data, function(result) {
	var resultData = result.data;
	$("#bankNo").empty();
	$("#bankNo").append("<option value=''>请选择</option>");
	for (var i = 0; i < resultData.length; i++) {
		$("#bankNo").append(
				"<option value='" + resultData[i].sbankno +"'>"
						+ resultData[i].sname + "</option>");
	}
	});
	var options = {
			url : serverPath + "/card/getListAll.htm",
			type : 'post',
			async:false,
			dataType : 'json',
			data : {
				"Time" : new Date().getMilliseconds()
			},
			success : function(result) {
				var resultData = result.data;
				$("#bankNo").empty();
				$("#bankNo").append("<option value=''>请选择</option>");
				for (var i = 0; i < resultData.length; i++) {
					$("#bankNo").append(
							"<option value='" + resultData[i].sbankno +"'>"
									+ resultData[i].sname + "</option>");
				}
			}
		};
		$.ajax(options);
}

function queryUserCardInfo(){
	//拼装入参调用绑卡接口
	var options = {
			url : serverPath + "/card/queryUserCardInfo.htm",
			type : 'post',
			async:false,
			dataType : 'json',
			data : {
				'phone':$('#mobilePhone').val(),
				"Time" : new Date().getMilliseconds()
			},
			success : function(result) {
				if(result.code == 0){
					var data = result.data;
					if(data != null){
						$('#bankNo').val(data.bankNo);
						$('#bankCardNo').val(data.bankCardNo);
						$('#accountBankName').val(data.accountBankName);
						$('#mobile').val(data.mobile);
						if(data.authStatus == 1){
							$('#authStatus').val("已认证");
							$('.authentication').hide();
						}else{
							$('#authStatus').val("未认证");
							$('.authentication').show();
						}
						$('.bindCard').hide();
						$('.removeBindCard').show();
						//查到绑卡信息，相关参数不支持修改
						$("#bankNo").attr("disabled",true);
						$('#bankCardNo').attr("readonly","readonly");
						$('#accountBankName').attr("readonly","readonly");
						$('#mobile').attr("readonly","readonly");
					}else{
						$('.bindCard').show();
						$('.authentication').hide();
						$('.removeBindCard').hide();
						//未查到绑卡信息，相关参数支持修改
						$("#bankNo").attr("disabled",false);
						$('#bankCardNo').attr("readonly",false);
						$('#accountBankName').attr("readonly",false);
						$('#mobile').attr("readonly",false);
					}
				}else{
					BootstrapDialog.alert(result.message);
				}
			},
		error : function() {
			$('.bindCard').show();
			$('.authentication').hide();
			$('.removeBindCard').hide();
		}
		};
		$.ajax(options);		
}

$(function() {
	getBankList();
	queryUserCardInfo();
});