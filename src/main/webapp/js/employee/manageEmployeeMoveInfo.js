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