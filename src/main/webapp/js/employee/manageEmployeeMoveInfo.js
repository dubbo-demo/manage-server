$(function() {

	$("form").validate({
		rules : {
			orgName : {
				required : true
			},
			positionId : {
				required : true
			},
			moveTime : {
				required : true
			},
			moveRemark : {
				required : true,
				maxlength : 100
			}
		},
		submitHandler : function(form) { // 表单提交句柄,为一回调函数，带一个参数：form
			form.submit(); // 提交表单
		}
	});

	initTreePullDown(treeObject.treePullDown);
});

function saveMoveInfo(event) {
	// 阻止冒泡
	ChkUtil.stopBubbleEvent(event);
	var orgId = $("#orgId").val();
	var orgName = $("#orgName").val();
	var positionId = $("#positionId").val();
	var teamId = $("#teamId").val();
	var jobLevel = $("#jobLevel").val();
	var moveTime = $("#moveTime").val();
	var moveRemark = $("#moveRemark").val();
	if (teamId == "" && jobLevel != "") {
		BootstrapDialog.alert("请选择新团队!");
		return;
	}
	
	//V2.0若新增、调动为员工为业务经理/团队经理时，团队必填。
	var positionCode = $("#positionId").find("option:selected").data('code');
	if(teamId == "" && (positionCode == "GW022" || positionCode == "GW023")){
		BootstrapDialog.alert("请选择新团队!");
		return;
	}
//	if (teamId != "" && jobLevel == "") {
//		BootstrapDialog.alert("请选择新星级!");
//		return;
//	}

	$("#newPosition").val($("#positionId").find("option:selected").text());
	if (teamId != "") {
		$("#newTeam").val($("#teamId").find("option:selected").text());
	}
	if (jobLevel != "") {
		$("#newJobLevel").val($("#jobLevel").find("option:selected").text());
	}
//	if ("2" == level) {
//		$("#orgType").val(1);
//		$("#newOrgType").val("大区");
//	}
//	if ("1" == level) {
//		$("#orgType").val(2);
//		$("#newOrgType").val("总部");
//	}
	$("#tab").submit();
}

function goback(event) {
	// 阻止冒泡
	ChkUtil.stopBubbleEvent(event);
	window.location.href = serverPath + "/employee/queryEmployeeInfo.htm";
}

function onClick(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("treePullDown"), nodes = zTree
			.getSelectedNodes(), v = "";
	nodes.sort(function compare(a, b) {
		return a.id - b.id;
	});
	if ("4" != nodes[0].orgType && "5" != nodes[0].orgType) {
		BootstrapDialog.alert("组织架构只能选部门!");
		return;
	}
	for (var i = 0, l = nodes.length; i < l; i++) {
		v += nodes[i].name + ",";
	}
	if (v.length > 0)
		v = v.substring(0, v.length - 1);
	$(treeObject.orgId).val(nodes[0].id);
	$(treeObject.orgName).val(v);
	$(treeObject.level).val(nodes[0].level);
	$(treeObject.parentId).val(nodes[0].pId);
	hideMenu();
	// 如果选择部门,则带出部门下岗位信息
	if (nodes[0].orgType == "4" || nodes[0].orgType == "5") {
		var url = treeObject.serverPath
				+ "/orgPosition/selectOrgPositionList.htm";
		var data = {
			"id" : nodes[0].id,
			"Time" : new Date().getMilliseconds()
		};
		$.ajaxSettings.async = false;
		$.getJSON(url, data, function(result) {
			var resultData = result.data;
			$("#positionId").empty();
			$("#positionId").append("<option value=''>请选择</option>");
			for (var i = 0; i < resultData.length; i++) {
				$("#positionId").append(
						"<option value='" + resultData[i].positionId + "' data-code='" + resultData[i].positionCode +"'>"
						+ resultData[i].positionName + "</option>");
			}
		});
		//默认门店下部门
		var type = 3;
		var url = treeObject.serverPath
		+ "/organization/selectOrganizationById.htm";
		var data = {
			"id" : nodes[0].pId,
			"Time" : new Date().getMilliseconds()
		};
		$.ajaxSettings.async = false;
		$.getJSON(url, data, function(result) {
			var resultData = result.data;
			type = resultData.orgType;
		});
		//
		if("3" == type){
			url = treeObject.serverPath + "/team/initTeamName/" + nodes[0].pId
			+ ".htm";
		}else if("2" == type){
			url = treeObject.serverPath + "/team/initTeamName/" + nodes[0].id
			+ ".htm";
		}
		
		data = {};
		$.ajaxSettings.async = false;
		$.getJSON(url, data, function(result) {
			var resultData = result.data;
			$("#teamId").attr("disabled", false);
			$("#teamId").empty();
			$("#teamId").append("<option value=''>请选择</option>");
			for (var i = 0; i < resultData.length; i++) {
				$("#teamId").append(
						"<option value='" + resultData[i].id + "'>"
								+ resultData[i].teamName + "</option>");
			}
			$("#jobLevel").empty();
			$("#jobLevel").append("<option value=''>请选择</option>");
			if(resultData.length > 0){
				$("#jobLevel").append("<option value='一级'>一级</option>");
				$("#jobLevel").append("<option value='二级'>二级</option>");
				$("#jobLevel").append("<option value='三级'>三级</option>");
				$("#jobLevel").append("<option value='四级'>四级</option>");
				$("#jobLevel").append("<option value='五级'>五级</option>");
			}
		});
	} else {
		$("#positionId").empty();
		$("#positionId").append("<option value=''>请选择</option>");
		$("#teamId").empty();
		$("#teamId").append("<option value=''>请选择</option>");
		$("#jobLevel").empty();
		$("#jobLevel").append("<option value=''>请选择</option>");
	}
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
					BootstrapDialog.alert(result.message);
				}
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
					BootstrapDialog.alert(result.message);
				}
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
					//解绑成功的，相关参数允许修改
					$("#bankNo").attr("disabled",false);
					$('#bankCardNo').attr("readonly",false);
					$('#accountBankName').attr("readonly",false);
					$('#mobile').attr("readonly",false);
				}else{
					BootstrapDialog.alert(result.message);
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
					BootstrapDialog.alert("解绑成功！");
					$('.bindCard').show();
					$('.authentication').hide();
					$('.removeBindCard').hide();
				}else{
					BootstrapDialog.alert(result.message);
				}
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
			}
		};
		$.ajax(options);		
}

$(function() {
	getBankList();
	queryUserCardInfo();
});