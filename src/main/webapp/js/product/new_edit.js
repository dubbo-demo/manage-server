$(function(){
	initProductType();
	initPeriodsType();
	initPeriodsUnitType();
	var form = $("#submit_form");
	form.validate({
		onfocusout:false,
		rules : {
			// account
			periods : {
				required : true,
				loanPeriods : true
			},
			periodsUnit : {
				required : true,
				periodsUnit:true
			},
			interestRate : {
				required : true,
				Floatlen:[2,6]
			},
			loanUpLimit : {
				required : true,
				isFloat:true,
				compare:["loanUpLimit","loanDownLimit"]
			},
			loanDownLimit : {
				required : true,
				isFloat:true,
				compare:["loanUpLimit","loanDownLimit"]
			},
			serviceRate : {
				required : true,
				Floatlen:[2,2]
			},
			overdueDays : {
				required : true,
				isInt:true
			},
			penaltyRate:{	
				required : true,
				Floatlen:[2,2]
			},
			prodType:{	
				required : true,
				productType:true
			}
		},

		messages : { // custom messages for radio buttons and checkboxes
			serviceRate:{
				Floatlen:"请输入[00.00]数据"
			},
			penaltyRate:{
				Floatlen:"请输入[00.00]数据"
			},
			interestRate:{
				Floatlen:"请输入[00.000000]数据"
			},
			loanDownLimit:{
				compare:"上限必须大于下限"
			},
			loanUpLimit:{
				compare:"上限必须大于下限"
			}
		}
	});
});
function initPeriodsType(){
	$.ajax({
		url : serverPath + "/node/selectNodeList.htm",
		type : "post",
		data : {
			"parentCode" : "periods",
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			if (result.code == 0) {
				// 清空除第一条内容的外的其它数据
				var select_ = $("select[name='periods']");
				select_.find("option:gt(0)").remove();
				for (var i = 0; i < result.data.length; i++) {
					var isSelected = result.data[i].nodeCode == select_
							.attr('data-id') ? "selected='selected'" : "";
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
function initProductType(){
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
				var select_ = $("select[name='prodType']");
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
function initPeriodsUnitType(){
	$.ajax({
		url : serverPath + "/node/selectNodeList.htm",
		type : "post",
		data : {
			"parentCode" : "periodsUnit",
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			if (result.code == 0) {
				// 清空除第一条内容的外的其它数据
				var select_ = $("select[name='periodsUnit']");
				select_.find("option:gt(0)").remove();
				for (var i = 0; i < result.data.length; i++) {
					var isSelected = result.data[i].nodeCode == select_
					.attr('data-id') ? "selected='selected'" : "";
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
function save(){
	var form = $("#submit_form");
	if(!form.valid()){
		return;
	}
	$.ajax({
		url : serverPath + "/product/saveOrUpdate.htm",
		type : "post",
		data : $("#submit_form").serialize(),
		dataType : "json",
		success : function(result) {
				if(result.code == 0){
					BootstrapDialog.alert(result.message,function(){
						window.location.href=serverPath+"/product/queryPageList.htm"
					});
				}else{
					BootstrapDialog.alert(result.message);
				}
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
}
