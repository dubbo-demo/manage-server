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