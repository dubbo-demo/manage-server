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