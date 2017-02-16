//$(function() {
//	$("form").validate({
//		rules : {
//			employeeName : {
//				required : true,
//				maxlength : 6
//			},
//			identityNumber : {
//				required : true,
//				idCard : true
//			},
//			mobilePhone : {
//				required : true,
//				phone : true
//			},
//			orgName : {
//				required : true
//			},
//			positionId : {
//				required : true
//			},
//			entryTime : {
//				required : true,
//				maxlength : 10
//			}
//		},
//		submitHandler : function(form) { // 表单提交句柄,为一回调函数，带一个参数：form
//			form.submit(); // 提交表单
//		}
//	});
//
//	initTreePullDown(treeObject.treePullDown);
//
//});

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
		alert("请选择新团队!");
		return;
	}
//	if (teamId != "" && jobLevel == "") {
//		alert("请选择新星级!");
//		return;
//	}

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
			alert("该身份证号已存在!");
			return;
		} else if ("2" == result.data) {
			alert("该手机号已存在!");
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
		alert("组织架构只能选部门!");
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
						"<option value='" + resultData[i].positionId + "'>"
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