$(function() {
	init();
});
function init() {
	// 加载大区数据
	initAreaData();
	initProductData();
}
function initAreaData() {
	$.ajax({
		url : serverPath + "/organization/selectOrgByOrgType.htm",
		type : "post",
		data : {
			"orgType" : 1,
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			if (result.code == 0) {
				initStoreData();
				// 清空除第一条内容的外的其它数据
				var select_ = $("select[name='areaId']");
				select_.find("option:gt(0)").remove();
				for (var i = 0; i < result.data.length; i++) {
					var isSelected = result.data[i].id == select_
							.attr('data-id') ? "selected='selected'" : "";
					select_.append("<option " + isSelected + " value='"
							+ result.data[i].id + "'>" + result.data[i].orgName
							+ "</option>");
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
function initStoreData() {
	var parentId = $("select[name='areaId']").val();
	if (parentId == "0") {
		parentId = $("select[name='areaId']").attr('data-id');
	}
	if (parentId == "0") {
		return;
	}
	$.ajax({
		url : serverPath + "/organization/selectOrgByParentId.htm",
		type : "post",
		data : {
			"parentId" : parentId,
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			if (result.code == 0) {
				// 清空除第一条内容的外的其它数据
				var select_ = $("select[name='storeId']");
				select_.find("option:gt(0)").remove();
				for (var i = 0; i < result.data.length; i++) {
					var isSelected = result.data[i].id == select_
							.attr('data-id') ? "selected='selected'" : "";
					select_.append("<option " + isSelected + " value='"
							+ result.data[i].id + "'>" + result.data[i].orgName
							+ "</option>");
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
function initProductData() {
	$.ajax({
		url : serverPath + "/node/selectNodeList.htm",
		type : "post",
		data : {
			"parentCode" : "proType",
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			if (result.code == 0) {
				// 清空除第一条内容的外的其它数据
				var select_ = $("select[name='productType']");
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

function go_todo(){
	$("#progress").val("todo");
	$("#searchForm").submit();
}

function go_done(){
	$("#progress").val("done");
	$("#searchForm").submit();
}