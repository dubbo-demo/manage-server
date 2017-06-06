$(function() {
	init();
	$("#cekAll").change(function() {
		if ($("#cekAll").prop("checked")) {
			$.uniform.update($(".checkboxitem").prop("checked", true));
		} else {
			$.uniform.update($(".checkboxitem").prop("checked", false));
		}
	});
});
function init() {
	// 加载大区数据
	initAreaData();
	// 加载团队数据
	initTeamData();
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

function initTeamData() {
	$.ajax({
		url : serverPath + "/team/initTeamName/80.htm",
		type : "post",
		data : {
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			if (result.code == 0) {
				// 清空除第一条内容的外的其它数据
				var select_ = $("select[name='teamId']");
				select_.find("option:gt(0)").remove();
				for (var i = 0; i < result.data.length; i++) {
					var isSelected = result.data[i].id == select_
							.attr('data-id') ? "selected='selected'" : "";
					select_.append("<option " + isSelected + " value='"
							+ result.data[i].id + "'>" + result.data[i].teamName
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

function loanedId(id,applyLoanNo) {
	var ids = [];
	var applyLoanNos = [];
	ids.push(id);
	applyLoanNos.push(applyLoanNo);
	loaned(ids,applyLoanNos);
}
function loaned(ids,applyLoanNos) {
	var id = null;
	var applyLoanNo = null;
	if (typeof(ids) == "undefined") {
		ids = []
		applyLoanNos = [];
		var checkBoxLen = $(".checkboxitem:checked").length;
		if (checkBoxLen > 0) {
			$(".checkboxitem:checked").each(function(index, dom) {
				id = $(dom).val();
				applyLoanNo = $(dom).attr("data");
				ids.push(id);
				applyLoanNos.push(applyLoanNo);
			});
		}
	}
	if (ids.length > 0) {
		BootstrapDialog.confirm("确认已完成放款？",function(isOk){
			if(isOk){
				$.ajax({
					url : serverPath + "/loan/loaned.htm",
					type : "post",
					data : {
						ids : ids,
						applyLoanNos:applyLoanNos,
						"Time" : new Date().getMilliseconds()
					},
					dataType : "json",
					success : function(result) {
						if (result.code == 0) {
							BootstrapDialog.alert("操作成功",function(){
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
		});
	}else{
		BootstrapDialog.alert("请选择要放款单！");
	}
}