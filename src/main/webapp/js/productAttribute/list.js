$(function(){
	initProductData();
});
function deleteInfo(id) {
	BootstrapDialog.confirm("确定要删除吗？", function(isOk) {
		if (isOk) {
			$.ajax({
				url : serverPath + "/productAttribute/deleteInfo.htm",
				type : "post",
				data : {
					"id" : id,
					"Time" : new Date().getMilliseconds()
				},
				dataType : "json",
				success : function(result) {
					if (result.code == 0) {
						BootstrapDialog.alert("删除成功", function() {
							window.location.href = serverPath
									+ "/productAttribute/queryProductAttribute.htm"
						});
					} else {
						BootstrapDialog.alert(result.message, function() {
							window.location.href = serverPath
									+ "/productAttribute/queryProductAttribute.htm"
						});
					}
				},
				error : function() {
					BootstrapDialog.alert("操作失败");
				}
			});
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
				var select_ = $("select[name='prodCode']");
				select_.find("option:gt(0)").remove();
				for (var i = 0; i < result.data.length; i++) {
					var isSelected = result.data[i].nodeCode == select_
							.attr('data-value') ? "selected='selected'" : "";
					select_.append("<option " + isSelected + " value='"
							+ result.data[i].nodeCode + "'>"
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