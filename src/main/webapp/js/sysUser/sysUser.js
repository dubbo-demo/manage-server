function search() {
	$("#searchForm").attr("action", serverPath + "/sysUser/querySysUser.htm");
	$("#searchForm").submit();
}

function goPage(pageNumber) {
	$("#pageIndex").val(pageNumber);
	$("#searchForm").submit();
}

// 选择大区时，查询门店信息
function selectStore() {
	var orgType = $("#orgType").val();
	if (orgType != 1) {
		return;
	}
	$("#storeId").empty();
	$("#storeId").append("<option value=0>请选择</option>");
	var url = serverPath + "/organization/selectOrgByOrgType.htm";
	var data = {
		"Time" : new Date().getMilliseconds(),
		"orgType" : 3
	};
	$.getJSON(url, data, function(result) {
		var data = result.data;
		for (var i = 0; i < data.length; i++) {
			var op = "<option value=" + data[i].id + ">" + data[i].orgName
					+ "</option>";
			$("#storeId").append(op);
		}
	});
}

// 禁用、启用，显示文字相应变化
function update(id) {
	var trId = "#" + id;
	var message = "";
	if ("启用" == $(trId).text()) {
		message = '确定禁用账户？';
	} else {
		message = '确定启用账户？';
	}
	BootstrapDialog.confirm({
		message : message,
		type : BootstrapDialog.TYPE_INFO,
		callback : function(result) {
			if (result) {
				var url = serverPath + "/sysUser/updateSysUser.htm";
				var data = {
					"Time" : new Date().getMilliseconds(),
					"employeeId" : id
				};
				$.getJSON(url, data, function(result) {
					var btnId = "#btnNew" + id;
					if ("启用" == $(trId).text()) {
						$(trId).text("禁用");
						$(btnId).text("启用");
					} else {
						$(trId).text("启用");
						$(btnId).text("禁用");
					}
				});
			} else {
				// alert('Nope.');
			}
		}
	});

}

$(function() {
	$("#orgType").val(queryDto.orgType);
	$.ajaxSettings.async = false;
	selectStore();
	$("#storeId").val(queryDto.storeId);
	$.ajaxSettings.async = true;
});