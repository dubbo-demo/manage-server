function save(event) {
	// 阻止冒泡
	ChkUtil.stopBubbleEvent(event);
	var employeeName = $("#employeeName").val();
	var identityNumber = $("#identityNumber").val();
	var mobilePhone = $("#mobilePhone").val();
	var orgId = $("#orgId").val();
	var orgName = $("#orgName").val();
	var level = $("#level").val();
	var positionId = $("#positionId").val();
	var teamId = $("#teamId").val();
	var jobLevel = $("#jobLevel").val();
	var entryTime = $("#entryTime").val();

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

	var url = serverPath + "/employee/checkEmployeeInfo.htm";
	var data = {
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
	if ("4" != nodes[0].orgType) {
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
	if (nodes[0].orgType == "4") {
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
				if(result.code == 0){
					BootstrapDialog.alert("绑卡成功！");
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
				if(result.code == 0){
					BootstrapDialog.alert("鉴权成功！");
				}else{
					BootstrapDialog.alert(result.message);
				}
			}
		};
		$.ajax(options);	
}

function removeBindCard(event){
	ChkUtil.stopBubbleEvent(event);
	//拼装入参调用绑卡接口
	var options = {
			url : serverPath + "/card/removeBindCard.htm",
			type : 'post',
			dataType : 'json',
			data : {
				'memberId':$('#mobilePhone').val(),
				'bankCardNo':$('#bankCardNo').val(),	
				"Time" : new Date().getMilliseconds()
			},
			success : function(data) {
				if(result.code == 0){
					BootstrapDialog.alert("解绑成功！");
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
			dataType : 'json',
			data : {
				"Time" : new Date().getMilliseconds()
			},
			success : function(data) {
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

$(function() {
	getBankList();
});