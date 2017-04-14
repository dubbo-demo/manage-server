$(function() {
	$('#addProductType').select2();

	$('#addTeamProduct').on('hide.bs.modal', function () {
		  $('.select2-drop').hide();
		})
		
	$('#updateProductType').select2();
	
	$('#updateTeamProduct').on('hide.bs.modal', function () {
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

function addTeamProduct(){
	var url = serverPath + "/teamProduct/addTeamProduct.htm";
	var productTypes = "";
	var productTypeslist=$("#addProductType").select2("data");
	console.log(productTypeslist);
	for (var i = 0; i < productTypeslist.length; i++) {
		
		if(productTypes == ""){
			productTypes = productTypeslist[i].element[0].id;
		}else{
			productTypes = productTypes + "|" + productTypeslist[i].element[0].id;
		}
	}
	var data = {
		"Time" : new Date().getMilliseconds(),
		"teamId" : $("#addTeam option:selected").attr('id'),
		"productTypes" : productTypes
	};
	$.getJSON(url, data, function(result) {
		BootstrapDialog.alert('操作成功', function() {
			window.location.href = window.location;
		});
	});
}

function update(id){
	var idTd = "#" + id;
	$("#updateTeam").val($(idTd).html());
	$("#id").val(id);
	//获取已选中的产品
	var productTypeArray= new Array();
	$.ajax({
		url : serverPath + "/teamProduct/queryteamProductById.htm",
		async:false, 
		type : "post",
		data : {
			"id" : id,
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			var data = result.data;
			var productTypes = data.productTypes;
			productTypeArray = productTypes.split("|");
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
	
	$.ajax({
		url : serverPath + "/node/treeList.htm",
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
				for(var j=0;j<productTypeArray.length;j++){
					if(result[i].id == productTypeArray[j] ){	
						data.push(result[i].id);
					}
				}	
			}
			$("#updateProductType").val(data).trigger('change');
			$("#updateProductType").change();//告诉select2代码已经更新，需要重载
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});	
}

function updateTeamProduct(){
	var url = serverPath + "/teamProduct/updateTeamProduct.htm";
	var productTypes = "";
	var productTypeslist=$("#updateProductType").select2("data");
	console.log(productTypeslist);
	for (var i = 0; i < productTypeslist.length; i++) {
		
		if(productTypes == ""){
			productTypes = productTypeslist[i].id;
		}else{
			productTypes = productTypes + "|" + productTypeslist[i].id;
		}
	}
	var data = {
		"Time" : new Date().getMilliseconds(),
		"id" : $("#id").val(),
		"productTypes" : productTypes
	};
	$.getJSON(url, data, function(result) {
		BootstrapDialog.alert('操作成功', function() {
			window.location.href = window.location;
		});
	});
}