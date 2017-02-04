$(function() {
	$("#savebutton").attr("disabled", false);
	$("#cancelbutton").attr("disabled", false);
});

function disabledActivityDetail(obj) {
	var tempStyle = $(obj).val();
	if (tempStyle == '0') {
		$("#detailUrl").attr("disabled", false);
		$("#detailStyle").val(1);
		$("#detailStylespan").append("<span id='requiredspan' class='required' style='color:#e02222;'>*&nbsp;</span>");
	} else if (tempStyle == '1') {
		$("#detailUrl").attr("disabled", "disabled");
		$("#detailUrl").val("");
		$(obj).attr("checked", false);
		$("#requiredspan").remove();
		$("#detailStyle").val(0);
	}
}
function checkInput(value) {
	var objExp = new RegExp('^[\a-\z\A-\Z0-9\u4E00-\u9FA5]*$');
	if (!objExp.test(value)) {
		return false;
	}
	return true;
}

function checkParams() {
	var title = $("#title").val();
	var activityType = $("#activityType").val();// 活动类型
	var location = $("#location").val();
	var activityPhoto = $("#activityPhoto").val();
	var detailUrl = $("#detailUrl").val();
	var detailStyle = $("input[name='detailStyle']:checked").val();// 详情样式
	$("#error").html("");
	if (ChkUtil.isNull(title)) {
		$("#error").html('<font color="red">标题不能为空</font>');
		$("#error").css("display", "block");
		return false;
	}
	if (!checkInput(title)) {
		$("#error").html('<font color="red">标题格式错误,请重新输入</font>');
		$("#error").css("display", "block");
		return false;
	}

	if (ChkUtil.isNull(activityPhoto)) {
		$("#error").html('<font color="red">请选择宣传图片</font>');
		$("#error").css("display", "block");
		return false;
	}
	var imagSize = document.getElementById("activityPhoto").files[0].size;
	if (imagSize > 5 * 1024 * 1024) {
		$("#error").html('<font color="red">宣传图片不能超过5M,请重新选择</font>');
		$("#error").css("display", "block");
		return false;
	}
	if (detailStyle == '1') {
		if (detailUrl.length != 0) {
			if (ChkUtil.isNull(detailUrl)) {
				$("#error").html('<font color="red">请输入活动详情超链接!</font>');
				$("#error").css("display", "block");
				return false;
			}
			if (!checkURL(detailUrl)) {
				$("#error").html('<font color="red">活动详情超链接格式不正确!</font>');
				$("#error").css("display", "block");
				return false;
			}
		} else {
			$("#error").html('<font color="red">请输入活动详情超链接!</font>');
			$("#error").css("display", "block");
			return false;
		}
	}
	return true;
}

function save() {
	if (checkParams()) {
		var title = $("#title").val();
		var imagePath = $("#activityPhoto").val();
		var arr = imagePath.split('\\');
		var imageName = arr[arr.length - 1];
		var detailUrl = $("#detailUrl").val();
		// 校验活动信息是否存在
		var url = serverPath + "/activity/checkActivityInfo.htm";
		var data = {
			"title" : title,
			"imageName" : imageName,
			"detailUrl" : detailUrl,
			"Time" : new Date().getMilliseconds()
		};
		$.ajaxSettings.async = false;
		$.getJSON(url, data, function(result) {
			if ("0" == result.data) {
				$("#tab").submit();
				$("#savebutton").attr("disabled", "disabled");
				$("#cancelbutton").attr("disabled", "disabled");
			} else {
				$("#error").html('<font color="red">该活动已存在!</font>');
				$("#error").css("display", "block");
				return;
			}
		});
	}
}

function cancel() {
	$("#canceltab").submit();
}

function checkURL(URL) {
	var str = URL;
	var Expression = /http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
	var objExp = new RegExp(Expression);
	if (objExp.test(str) == true) {
		return true;
	} else {
		return false;
	}
}

function deleteInfo(id) {
	BootstrapDialog.confirm("确定要删除吗？", function(isOk) {
		if (isOk) {
			$.ajax({
				url : serverPath + "/activity/deleteActivity.htm",
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
									+ "/activity/activityQuery.htm?createDate="
									+ createDate + "&pageIndex=" + pageIndex
									+ "&pageSize=" + pageSize + "&id=" + id
						});
					} else {
						BootstrapDialog.alert(result.message, function() {
							window.location.href = serverPath
									+ "/activity/activityQuery.htm"
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
