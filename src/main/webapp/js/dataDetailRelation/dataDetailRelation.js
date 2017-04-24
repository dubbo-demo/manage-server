$(function() {
	$('#addInfoNames').select2();

	$('#addDataDetailRelation').on('hide.bs.modal', function () {
		  $('.select2-drop').hide();
		})
		
	$('#updateInfoNames').select2();
	
	$('#updateDataDetailRelation').on('hide.bs.modal', function () {
		  $('.select2-drop').hide();
		})		
});

function checkInput(patrn, obj) {
	obj.value = obj.value.replace(/(^\s*)|(\s*$)/g, "");// 删除二边空格
	var objExp = new RegExp(patrn);
	if (!objExp.test(obj.value)) {
		BootstrapDialog.alert("输入格式错误,请重新输入", function() {
			setTimeout(function() {
				obj.focus();
			}, 0);
		});
	}
	return true;
}

function add(){
	$.ajax({
		url : serverPath + "/dataDetailRelation/queryDataDetails.htm",
		type : "post",
		data : {
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			for(var i=0;i<result.length;i++){
				$("#addInfoNames").append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
			}
			$("#addInfoNames").val(data).trigger('change');
			$("#addInfoNames").change();//告诉select2代码已经更新，需要重载
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});	
}


function addDataDetailRelation(){
	var infoNames = "";
	var infoNamelist=$("#addInfoNames").select2("data");
	console.log(infoNamelist);
	for (var i = 0; i < infoNamelist.length; i++) {
		
		if(infoNames == ""){
			infoNames = infoNamelist[i].element[0].id;
		}else{
			infoNames = infoNames + "|" + infoNamelist[i].element[0].id;
		}
	}
	
	$.ajax({
		url : serverPath + "/dataDetailRelation/addDataDetailRelation.htm",
		type : "post",
		data : {
			addPageName:$("#addPageName").val(),
			addPageCode:$("#addCode").val(),
			infoCode:infoNames,
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			addDataDetailResult = result.data;
			if(addDataDetailResult == 0){
				BootstrapDialog.alert('操作成功', function() {
					window.location.href = window.location;
					return true;
				});
			}
			if(addDataDetailResult == 1){
				BootstrapDialog.alert("大资料项名称重复");
			}
			if(addDataDetailResult == 2){
				BootstrapDialog.alert("大资料项编码重复");
			}
			if(addDataDetailResult == 3){
				BootstrapDialog.alert("大资料项名称和编码重复");
			}
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
}

function update(id){
	var pageNameId = "#" + id + 'pageName';
	var pageCodeId = "#" + id + 'pageCode';
	$("#updatePageName").val($(pageNameId).html());
	$("#updatePageCode").val($(pageCodeId).html());
	$("#id").val(id);
	
	//获取已选中的产品
	var pageCodeArray= new Array();
	$.ajax({
		url : serverPath + "/dataDetailRelation/queryDataDetailRelationById.htm",
		async:false, 
		type : "post",
		data : {
			"id" : id,
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			var data = result.data;
			var pageCodes = data.pageCode;
			pageCodeArray = pageCodes.split("|");
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
	
	$.ajax({
		url : serverPath + "/dataDetailRelation/queryDataDetails.htm",
		async:false, 
		type : "post",
		data : {
			"parentCode" : "proType",
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			var data = new Array();
			$("#updateProductType").empty();
			for(var i=0;i<result.length;i++){
				$("#updateProductType").append("<option value='"+result[i].id+"'>"+result[i].nodeName+"</option>");
				for(var j=0;j<pageCodeArray.length;j++){
					if(result[i].id == pageCodeArray[j] ){	
						data.push(result[i].id);
					}
				}	
			}
			$("#updateInfoNames").val(data).trigger('change');
			$("#updateInfoNames").change();//告诉select2代码已经更新，需要重载
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});	
	
}

function updateDataDetailRelation(){
	var updateDataDetailReceptionResult = '';
	var infoCodes = "";
	var infoCodeslist=$("#updateInfoNames").select2("data");
	console.log(infoCodeslist);
	for (var i = 0; i < infoCodeslist.length; i++) {
		
		if(infoCodes == ""){
			infoCodes = infoCodeslist[i].id;
		}else{
			infoCodes = infoCodes + "|" + infoCodeslist[i].id;
		}
	}
	$.ajax({
		url : serverPath + "/dataDetailRelation/updateDataDetailRelation.htm",
		type : "post",
		data : {
			"Time" : new Date().getMilliseconds(),
			"id" : $("#id").val(),
			"pageName" : $("#updatePageName").val(),
			"pageCode" : $("#updatePageCode").val(),
			"infoCode" : infoCodes
		},
		dataType : "json",
		success : function(result) {
			updateDataDetailReceptionResult = result.data;
			if(addDataDetailResult == 1){
				BootstrapDialog.alert("大资料项名称重复");
			}
			if(updateDataDetailReceptionResult == 2){
				BootstrapDialog.alert("大资料项编码重复");
			}
			if(updateDataDetailReceptionResult == 0){
				BootstrapDialog.alert('操作成功', function() {
					window.location.href = window.location;
				});
			}
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
}