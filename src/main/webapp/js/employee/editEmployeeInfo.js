$(function() {
	$("form").validate({
		rules : {
			employeeName : {
				required : true,
				maxlength : 6
			},
			identityNumber : {
				required : true,
				idCard : true
			},
			mobilePhone : {
				required : true,
				phone : true
			}
		},
		submitHandler : function(form) { // 表单提交句柄,为一回调函数，带一个参数：form
			form.submit(); // 提交表单
		}
	});

	var storeId;
	var orgType = dto.orgType;
	$("#positionId").empty();
	$("#positionId").append("<option value='0'>请选择</option>");
	if ("2" == orgType) {
		storeId = dto.orgId;
	} else {
		storeId = dto.storeId;
	}
	if (storeId.length > 0) {
		var url = serverPath + "/team/initTeamName/" + storeId + ".htm";
		var teamId = dto.teamId;
		var jobLevel = dto.jobLevel;
		var data = {};
		$.ajaxSettings.async = false;
		$.getJSON(url, data, function(result) {
			var resultData = result.data;
			if (resultData.length > 0) {
				$("#teamId").empty();
				$("#teamId").attr("disabled", false);
				$("#teamId").append("<option value=''>请选择</option>");
				for (var i = 0; i < resultData.length; i++) {
					$("#teamId").append(
							"<option value='" + resultData[i].id + "'>"
									+ resultData[i].teamName + "</option>");
				}
				$("#teamId").val(teamId);
				$("#jobLevel").attr("disabled", false);
				$("#jobLevel").empty();
				$("#jobLevel").append("<option value=''>请选择</option>");
				if (resultData.length > 0) {
					$("#jobLevel").append("<option value='一级'>一级</option>");
					$("#jobLevel").append("<option value='二级'>二级</option>");
					$("#jobLevel").append("<option value='三级'>三级</option>");
					$("#jobLevel").append("<option value='四级'>四级</option>");
					$("#jobLevel").append("<option value='五级'>五级</option>");
					$("#jobLevel").val(jobLevel);
				}
			}
		});
	}

	$("#teamId").change(function() {
		$("#jobLevel").empty();
		$("#jobLevel").append("<option value=''>请选择</option>");
		$("#jobLevel").append("<option value='一级'>一级</option>");
		$("#jobLevel").append("<option value='二级'>二级</option>");
		$("#jobLevel").append("<option value='三级'>三级</option>");
		$("#jobLevel").append("<option value='四级'>四级</option>");
		$("#jobLevel").append("<option value='五级'>五级</option>");
	});

});

function edit(event) {
	// 阻止冒泡
	ChkUtil.stopBubbleEvent(event);
	var employeeName = $("#employeeName").val();
	var identityNumber = $("#identityNumber").val();
	var mobilePhone = $("#mobilePhone").val();
	var teamId = $("#teamId").val();
	var jobLevel = $("#jobLevel").val();
	if (teamId == "" && jobLevel != "") {
		alert("请选择新团队!");
		return;
	}
	var url = serverPath + "/employee/checkEmployeeInfo.htm";
	var data = {
		"id" : id,
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