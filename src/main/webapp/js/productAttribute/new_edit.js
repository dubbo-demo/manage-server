$(function(){
	if($('#id').val() != ''){
		initSelectStore();
		initSelectProductData();
		$('#prodCode').attr("disabled","disabled").css("background-color","#EEEEEE;");
	}else{
		initStore();
		initProductData();
	}
	$('#addStore').select2();
		
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
			prodType:{	
				required : true,
				productType:true
			}
		},

		messages : { // custom messages for radio buttons and checkboxes
			loanDownLimit:{
				compare:"上限必须大于下限"
			},
			loanUpLimit:{
				compare:"上限必须大于下限"
			}
		}
	});
});

function save(){
	var form = $("#submit_form");
	if(!form.valid()){
		return;
	}
	$.ajax({
		url : serverPath + "/productAttribute/saveOrUpdate.htm",
		type : "post",
		data : $("#submit_form").serialize(),
		dataType : "json",
		success : function(result) {
				if(result.code == 0){
					BootstrapDialog.alert(result.message,function(){
						window.location.href=serverPath+"/productAttribute/queryProductAttribute.htm"
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


function initProductData() {
	$.ajax({
		url : serverPath + "/productAttribute/queryUnConfigProd.htm",
		type : "post",
		data : {
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

function initStore(){
	var data = new Array();
	$.ajax({
		url : serverPath + "/organization/selectOrgByOrgType.htm",
		type : "post",
		data : {
			"orgType":3,
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			$("#addStore").empty();
			var datas = result.data;
			for(var i=0;i<datas.length;i++){
				$("#addStore").append("<option value='"+datas[i].orgCode+"'>"+datas[i].orgName+"</option>");
				data.push(datas[i].orgCode);
			}
			$("#addStore").val(data).trigger('change');
			$("#addStore").change();//告诉select2代码已经更新，需要重载
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
}

function initSelectStore(){
	var storeArray= new Array();
	var storeCodes = $('#storeCodes').val();
	storeArray=storeCodes.split("|");
	var data = new Array();
	$.ajax({
		url : serverPath + "/organization/selectOrgByOrgType.htm",
		type : "post",
		data : {
			"orgType":3,
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			$("#addStore").empty();
			var datas = result.data;
			for(var i=0;i<datas.length;i++){
				$("#addStore").append("<option value='"+datas[i].orgCode+"'>"+datas[i].orgName+"</option>");
				for (j=0;j<storeArray.length ;j++ ) { 
					if(storeArray[j] == datas[i].orgCode){
						data.push(datas[i].orgCode);
						break;
					}
				} 	
			}
			$("#addStore").val(data).trigger('change');
			$("#addStore").change();//告诉select2代码已经更新，需要重载
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
}

function initSelectProductData() {
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