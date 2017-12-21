$(function() {
	$("*[name='disEdit'] input").attr("disabled", true);
	$("*[name='disEdit'] textarea").attr("disabled", true);
	initProductData();
	initloanPeriodsData();
	initRefuseReasonData();
	initloanPeriodsData();
	initRefuseReason();
	var form = $("#form_submit");
	form.validate({
		onfocusout:false,
		rules : {
			// account
			loanPeriods : {
				required : true,
				loanPeriods : true
			},
			auditState : {
				required : true
			},
			applyMoney : {
				required : true
			},
			creditMoney : {
				required : true,
				isFloat:true,
				maxlength:10
			},
			externalRemark : {
				required : true,
				maxlength:300
			},
			mainCauseNo : {
				required : true,
				mainCauseNo : true
			},
			secondCauseNo : {
				required : true,
				secondCauseNo : true
			},
			auditState:{	
				required : true,
				approveResult:true
			},
			proType:{	
				required : true,
				productType:true
			},
			proId:{
				loanPeriods : true
			}
		},

		messages : { // custom messages for radio buttons and checkboxes
		}
	});
});
var returnURL = serverPath + "/audit/list/todo.htm";
function secondaryInit(this_, id) {
	var select_ = $("#" + id);
	if ($(this_).val() == 0) {
		select_.find("option:gt(0)").remove();
		return;
	}
	$.ajax({
		url : serverPath + "/node/selectNodeList.htm",
		type : "post",
		data : {
			"parentCode" : $(this_).val()
		},
		dataType : "json",
		success : function(result) {
			if (result.code == 0) {
				select_.find("option:gt(0)").remove();
				for (var i = 0; i < result.data.length; i++) {
					select_.append("<option value='" + result.data[i].nodeCode
							+ "'>" + result.data[i].nodeName + "</option>");
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
function initRefuseReason() {
	var select_ = $("select[name='mainCauseNo']");
	$.ajax({
		url : serverPath + "/refuseReason/selectAll.htm",
		type : "post",
		data : {
		},
		dataType : "json",
		success : function(result) {
			if (result.code == 0) {
				select_.find("option:gt(0)").remove();
				for (var i = 0; i < result.data.length; i++) {
					select_.append("<option data='"+result.data[i].confinementTime+"' value='" + result.data[i].refuseCode
							+ "'>" + result.data[i].refuseDesc + "</option>");
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
function changeRefuseReason(this_){
	var value = $(this_).val();
	$("#reasonCode").html(value);
}
function reasonInit(this_) {
	if ($(this_).val() == Fisrt_FIRST_APPROVE_REFUSE) {
		$("*[name='reason']").show();
		$("*[name='applyInfo']").hide();
	} else {
		$("*[name='reason']").hide();
		$("*[name='applyInfo']").show();
	}
	if ($(this_).val() == "0") {
		$("*[name='reason']").hide();
		$("*[name='applyInfo']").hide();
	}
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
				var select_ = $("select[name='proType']");
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

function abandonMain(applyLoanNo) {
	$("#applyLoanNoId").val('');
	$("#applyLoanNoId").val(applyLoanNo);
	$("#abandon").modal('show');
}
function abandon() {
	var f = false;
	$("#abandon .js_input").each(function(i,v){
		var _val = $(this).val();
		if(_val == "" || _val == -1){
			$(this).addClass("error");
			f = true;
		}else{
			$(this).removeClass("error");
		}
	});
	if(f) {
		return false;
	}

	var applyLoanNo = $("#applyLoanNoId").val();
	$.ajax({
		url : serverPath + "/audit/abandon.htm",
		type : "post",
		data : {
			"applyLoanNo" : applyLoanNo,
			"interiorRemark" :$("#interiorRemark").val(),
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			if (result.success) {
				window.location.href = returnURL;
			} else {
				BootstrapDialog.alert(result.message);
			}
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
}
function initloanPeriodsData(change) {
	var parent = $("select[name='proType']");
	var id = parent.attr("data-id");
	var parentValue = parent.val();
	if (parentValue == 0 && !change) {
		parentValue = id;
	}
	if (parentValue == ""||null == parentValue || parentValue == 0) {
		var select_ = $("select[name='proId']");
		select_.find("option:gt(0)").remove();
		return;
	}
	$.ajax({
		url : serverPath + "/product/showProductForAudit.htm",
		type : "post",
		data : {
			"applyLoanNo" : $('#applyLoanNo').val(),
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			if (result.code == 0) {
				// 清空除第一条内容的外的其它数据
				var select_ = $("select[name='proId']");
				select_.find("option:gt(0)").remove();
				for (var i = 0; i < result.data.length; i++) {
					var isSelected = result.data[i].id == select_
							.attr('data-id') ? "selected='selected'" : "";
					select_.append("<option " + isSelected + " data='"
							+ result.data[i].periods + "' value='"
							+ result.data[i].id + "'>" + result.data[i].periods
							+ "期</option>");
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

function initRefuseReasonData() {
	$.ajax({
		url : serverPath + "/node/selectNodeList.htm",
		type : "post",
		data : {
			"parentCode" : "RollBackReason",
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			if (result.code == 0) {
				// 清空除第一条内容的外的其它数据
				var select_ = $("#backReason");
				select_.find("option:gt(0)").remove();
				for (var i = 0; i < result.data.length; i++) {
					var isSelected = result.data[i].nodeCode == select_
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
function visitExternal(applyLoanNo) {
	var value = $("#visitExternal textarea").val();
	if("" == value){
		BootstrapDialog.alert("请填写外访注意点");
		return;
	}
	$.ajax({
		url : serverPath + "/firstApprove/visitExternal.htm",
		type : "post",
		data : {
			"requirement" : value,
			"applyLoanNo" : applyLoanNo,
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			if (result.code == 0) {
				window.location.href = returnURL;
			} else {
				BootstrapDialog.alert(result.message);
			}
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
}
function rollBack(applyLoanNo) {
	var backReason = $("#backReason").val();
	if (backReason == 0) {
		BootstrapDialog.alert("请选择原因");
		return;
	}
	var remark = $("#interiorRemark").val();
	if("" == remark){
		BootstrapDialog.alert("请备注详细原因");
		return;
	}
	var value = $("#rollBack textarea").val();
	$.ajax({
		url : serverPath + "/firstApprove/rollBack.htm",
		type : "post",
		data : {
			"backReasonId" : backReason,
			"applyLoanNo" : applyLoanNo,
			"backReasonDesc" : $("#backReason option:selected").text(),
			"interiorRemark" :$("#interiorRemark").val(),
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			if (result.code == 0) {
				window.location.href = returnURL;
			} else {
				BootstrapDialog.alert(result.message);
			}
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
}
function submit(applyLoanNo) {
	if (!$("#form_submit").valid()) {
		return;
	}
	var paramStr = $("#form_submit").serialize();
	var loanPeriods = $("*[name='proId'] option:selected").attr("data");
	var productName = $("*[name='proType'] option:selected").text().trim();
	var confinementTime = $("*[name='mainCauseNo'] option:selected").attr("data");
	if(!(typeof(loanPeriods) =="undefined")){
		paramStr += "&loanPeriods=" + loanPeriods;
	} 
	if(!(typeof(productName) =="undefined")){
		paramStr += "&productName=" + productName;
	}
	if(!(typeof(confinementTime) =="undefined")){
		paramStr += "&confinementTime="+confinementTime;
	}
	$.ajax({
		url : serverPath + "/firstApprove/submitTaskInfo.htm",
		type : "post",
		data : paramStr,
		dataType : "json",
		success : function(result) {
			if (result.code == 0) {
				BootstrapDialog.alert("提交成功",function(){
					window.location.href = returnURL;
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