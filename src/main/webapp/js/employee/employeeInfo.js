$(function() {

	if (queryDto.orgId != 0) {
		var orgId = queryDto.orgId;
		$("#positionId").empty();
		$("#positionId").append("<option value='0'>请选择</option>");
		var url = serverPath + "/orgPosition/selectDistinctPositionInfo.htm";
		var data = {
			"id" : orgId,
			"Time" : new Date()
					.getMilliseconds()
		};
		$.ajaxSettings.async = false;
		$.getJSON(url, data, function(result) {
			var resultData = result.data;
			for (var i = 0; i < resultData.length; i++) {
				$("#positionId").append("<option value='" + resultData[i].positionId + "'>" 
						+ resultData[i].positionName + "</option>");
			}
			if (queryDto.positionId != '') {
				$("#positionId").val(queryDto.positionId);
			}
		});
	}
	$(".aIcmbShow").on('click',function(){
		  var icmbFlag = $(this).data("icmbflag");
	        var id = $(this).attr("data-id");
	        $("#icmbShowId").val(id);
	        $("#icmbShowFlag").val(icmbFlag);
	        if(icmbFlag == 1){
	        	$("#icmbTimeLabel").html("复职日期：");
	        }else{
	        	$("#icmbTimeLabel").html("离职日期：");
	        }        
	        $("#icmbTime").removeClass("error");
	        $("#remark").removeClass("error");
	})

});

function search(event) {
	// 阻止冒泡
	ChkUtil.stopBubbleEvent(event);
	ChkUtil.form_trim($("#searchForm"));
	$("#searchForm").submit();
}

function selectOrgTypeInfo(obj) {
	var orgType = obj.value;
	if (orgType == "0") {
		$("#regionId").empty();
		$("#regionId").append("<option value='0'>请选择</option>");
		$("#storeId").empty();
		$("#storeId").append("<option value='0'>请选择</option>");
		$("#departmentId").empty();
		$("#departmentId").append("<option value='0'>请选择</option>");
		$("#positionId").empty();
		$("#positionId").append("<option value='0'>请选择</option>");
	} else {
		var url;
		if (orgType == "1") {
			url = serverPath + "/organization/selectOrganization.htm";
		} else {
			url = serverPath + "/organization/selectDepartmentInfoUnderHeadQuarter.htm";
		}
		var data = {
			"orgType" : orgType,
			"Time" : new Date().getMilliseconds()
		};
		$.ajaxSettings.async = false;
		$.getJSON(url, data, function(result) {
			var resultData = result.data;
			if (orgType == "1") {
				$("#regionId").empty();
				$("#regionId").append("<option value='0'>请选择</option>");
				$("#storeId").empty();
				$("#storeId").append("<option value='0'>请选择</option>");
				$("#departmentId").empty();
				$("#departmentId").append("<option value='0'>请选择</option>");
				$("#positionId").empty();
				$("#positionId").append("<option value='0'>请选择</option>");
				for (var i = 0; i < resultData.length; i++) {
					$("#regionId").append("<option value='" + resultData[i].id + "'>" + resultData[i].orgName + "</option>");
				}
			} else {
				$("#regionId").empty();
				$("#regionId").append("<option value='0'>请选择</option>");
				$("#storeId").empty();
				$("#storeId").append("<option value='0'>请选择</option>");
				$("#departmentId").empty();
				$("#departmentId").append("<option value='0'>请选择</option>");
				$("#positionId").empty();
				$("#positionId").append("<option value='0'>请选择</option>");
				for (var i = 0; i < resultData.length; i++) {
					$("#departmentId").append(
							"<option value='" + resultData[i].id + "'>"
									+ resultData[i].orgName + "</option>");
				}
			}
		});
	}
}

function selectStoreInfo(obj, storeId) {
	var parentId = obj.value;
	$(storeId).empty();
	$(storeId).append("<option value='0'>请选择</option>");
	$("#departmentId").empty();
	$("#departmentId").append("<option value='0'>请选择</option>");
	$("#positionId").empty();
	$("#positionId").append("<option value='0'>请选择</option>");
	if (parentId != "0") {
		var url = serverPath + "/organization/selectOrganization.htm";
		var data = {
			"parentId" : parentId,
			"Time" : new Date().getMilliseconds()
		};
		$.ajaxSettings.async = false;
		$.getJSON(url, data, function(result) {
			var resultData = result.data;
			for (var i = 0; i < resultData.length; i++) {
				$(storeId).append(
						"<option value='" + resultData[i].id + "'>"
								+ resultData[i].orgName + "</option>");
			}
		});
	}
}

function selectDepartmentInfo(obj, departmentId) {
	var parentId = obj.value;
	$(departmentId).empty();
	$(departmentId).append("<option value='0'>请选择</option>");
	$("#positionId").empty();
	$("#positionId").append("<option value='0'>请选择</option>");
	if (parentId != "0") {
		var url = serverPath + "/organization/selectOrganization.htm";
		var data = {
			"parentId" : parentId,
			"Time" : new Date().getMilliseconds()
		};
		$.ajaxSettings.async = false;
		$.getJSON(url, data, function(result) {
			var resultData = result.data;
			for (var i = 0; i < resultData.length; i++) {
				$(departmentId).append(
						"<option value='" + resultData[i].id + "'>"
								+ resultData[i].orgName + "</option>");
			}
		});
	}
}

function selectPositionInfo(obj, positionId) {
	var parentId = obj.value;
	$(positionId).empty();
	$(positionId).append("<option value='0'>请选择</option>");
	if (parentId != "0") {
		var url = serverPath + "/orgPosition/selectOrgPositionList.htm";
		var data = {
			"id" : parentId,
			"Time" : new Date().getMilliseconds()
		};
		$.ajaxSettings.async = false;
		$.getJSON(url, data, function(result) {
			var resultData = result.data;
			for (var i = 0; i < resultData.length; i++) {
				$(positionId).append(
						"<option value='" + resultData[i].positionId + "'>"
								+ resultData[i].positionName + "</option>");
			}
		});
	}
}

function selectChildInfo(obj, storeId, departmentId, positionId) {
	var parentId = obj.value;
	var selectId;
	if (storeId == "" && departmentId == "") {
		selectId = positionId;
	} else if (storeId == "") {
		selectId = departmentId;
		$(departmentId).empty();
		$(departmentId).append("<option value='0'>请选择</option>");
		$(positionId).empty();
		$(positionId).append("<option value='0'>请选择</option>");
	} else {
		selectId = storeId;
		$(positionId).empty();
		$(positionId).append("<option value='0'>请选择</option>");
	}
	if (parentId != "0") {
		var url = serverPath + "/organization/selectOrganization.htm";
		var data = {
			"parentId" : parentId,
			"Time" : new Date().getMilliseconds()
		};
		$.ajaxSettings.async = false;
		$.getJSON(url, data, function(result) {
			var resultData = result.data;
			$(selectId).empty();
			$(selectId).append("<option value='0'>请选择</option>");
			for (var i = 0; i < resultData.length; i++) {
				$(selectId).append(
						"<option value='" + resultData[i].id + "'>"
								+ resultData[i].orgName + "</option>");
			}
		});
	}
}

function onClick(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("treePullDown"), nodes = zTree
			.getSelectedNodes(), v = "";
	nodes.sort(function compare(a, b) {
		return a.id - b.id;
	});
	for (var i = 0, l = nodes.length; i < l; i++) {
		v += nodes[i].name + ",";
	}
	if (v.length > 0) {
		v = v.substring(0, v.length - 1);
	}
	$(treeObject.orgId).val(nodes[0].id);
	$(treeObject.orgName).val(v);
	$(treeObject.level).val(nodes[0].level);
	$(treeObject.storeId).val(nodes[0].pId);
	hideMenu();
	var url = treeObject.serverPath + "/orgPosition/selectDistinctPositionInfo.htm";
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
}

function updateUserflag(id,userFlag){
	$.ajax({
		url : serverPath + "/employee/updateUserflag.htm",
		type : "post",
		data : {
			"id" : id,
			"userFlag" : userFlag,
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			if (result.code == 0) {
				BootstrapDialog.alert('操作成功', function(result) {
					window.location.reload();
				});
			} else {
				BootstrapDialog.alert("操作失败");
			}
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
}

function updateIcmbFlag(){
	var f = false;
	if(ChkUtil.isNull($("#icmbTime").val())) {
		$("#icmbTime").addClass("error");
		f = true;
	}else{
		$("#icmbTime").removeClass("error");
	}
	if(ChkUtil.isNull($("#remark").val())) {
		$("#remark").addClass("error");
		f = true;
	}else{
		$("#remark").removeClass("error");
	}	
	if(f) {
		return false;
	}
	$.ajax({
		url : serverPath + "/employee/updateIcmbFlag.htm",
		type : "post",
		data : {
			"id" : $("#icmbShowId").val(),
			"icmbFlag" : $("#icmbShowFlag").val(),
			"icmbTime" : $("#icmbTime").val(),
			"remark" : $("#remark").val()
		},
		dataType : "json",
		success : function(result) {
			if (result.code == 0) {
				$('.icmbButton').attr("disabled","disabled");
				BootstrapDialog.alert('操作成功', function(result) {
					window.location.reload();
				});
			} else {
				BootstrapDialog.alert(result.message);
			}
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
}

function alertInfo(){
	BootstrapDialog.alert("离职员工禁止启用");
}