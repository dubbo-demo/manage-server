function checkInput(patrn, obj) {
	obj.value = obj.value.replace(/(^\s*)|(\s*$)/g, "");// 删除二边空格
	var objExp = new RegExp(patrn);
	if (!objExp.test(obj.value)) {
		BootstrapDialog.alert("输入格式错误,请重新输入",
				function(){
			setTimeout(function() {
				obj.focus();
			}, 0);
			
		});
	}
	return true;
}

// 根据父级编码查询产品信息
function showProduct() {
	$("#selectProductName").empty();
	$("#productName").empty();
	$("#selectProductName").append("<option value=0 selected=''>请选择</option>");
	$("#productName").append("<option value=0 selected=''>请选择</option>");
	var url = serverPath + "/productFile/showProduct.htm";
	var data = {
		"Time" : new Date().getMilliseconds()
	};
	$.getJSON(url, data, function(result) {
		var product = result.data;
		$("#product").val(JSON.stringify(product));
		for (var i = 0; i < product.length; i++) {
			var op = "<option value=" + product[i].id + ">"
					+ product[i].nodeName + "</option>";
			$("#selectProductName").append(op);
			$("#productName").append(op);
		}
	});
}

// 根据父级编码查询文件上传阶段
function filrUpState() {
	$("#fileUpState").empty();
	$("#fileUpState").append("<option value=0 selected=''>请选择</option>");
	var url = serverPath + "/productFile/showFileUpState.htm";
	var data = {
		"Time" : new Date().getMilliseconds()
	};
	$.getJSON(url, data, function(result) {
		var productFileUpState = result.data;
		$("#productFileUpState").val(JSON.stringify(productFileUpState));
		for (var i = 0; i < productFileUpState.length; i++) {
			var op = "<option value=" + productFileUpState[i].id + ">"
					+ productFileUpState[i].nodeName + "</option>";
			$("#fileUpState").append(op);
		}
	});
}

$(function() {
	showProduct();
	filrUpState();
	search();
});

// 查询产品上传附件信息
function search() {
	var productName = $("#selectProductName").find("option:selected").text();
	if ("请选择" == productName) {
		productName = "";
	}
	$("#selectProductFile tbody").html("");
	var options = {
		url : serverPath + "/productFile/showProductFile.htm",
		type : 'post',
		dataType : 'json',
		data : {
			"Time" : new Date().getMilliseconds(),
			"productName" : productName
		},
		success : function(data) {
			var data = data.data;
			for (var i = 0; i < data.length; i++) {
				var trHTML = "<tr id=" + data[i].id + ">" + "<td>"
						+ data[i].productName + "</td>" + "<td>"
						+ data[i].directoryName + "</td>" + "<td>"
						+ data[i].fileUpStateName + "</td>" + "<td>"
						+ (data[i].isMust == 0 ? "否" : "是") + "</td>"
						+ "<td><a onclick='update(" + data[i].id
						+ ");return false;'>修改</a></td>"
						+ "<td><a onclick='del(" + data[i].id
						+ ");return false;'>删除</a></td></tr>";
				$("#selectProductFile").append(trHTML);
			}
		}
	};
	$.ajax(options);
}

function add() {
	$("#selectShow").hide();
	$("#addProductFile").show();
	$("#changeProductFile input[type='text']").val("");
	$("#error").html("");
	$("#insertOrUpdate").val(0);
}

function backProductFile() {
	$("#selectShow").show();
	$("#addProductFile").hide();
	$("#changeProductFile input[type='text']").val("");
	$("#changeProductFile select").val(0);
	$("#error").html("");
}

function save() {
	$("#error").html("");
	// 入参进行校验
	var productName = $("#productName").find("option:selected").text();
	var productTypeId = $("#productName").find("option:selected").val();
	if ("请选择" == productName) {
		$("#error").html('<font color="red">' + "请选择产品名称" + '</font>');
		document.getElementById("error").style.display = "block";
		return;
	}

	var fileUpState = $("#fileUpState").find("option:selected").text();
	var fileUpStateId = $("#fileUpState").find("option:selected").val();
	if ("请选择" == fileUpState) {
		$("#error").html('<font color="red">' + "请选择上传阶段" + '</font>');
		document.getElementById("error").style.display = "block";
		return;
	}

	if ($("#directoryName").val() == "") {
		$("#error").html('<font color="red">' + "附件名称不能为空" + '</font>');
		document.getElementById("error").style.display = "block";
		return;
	}
	
	var isMust = $("#isMust").find("option:selected").text();
	if ("请选择" == isMust) {
		$("#error").html('<font color="red">' + "请选择是否必填" + '</font>');
		document.getElementById("error").style.display = "block";
		return;
	}

	// 提交
	var options = {
		url : serverPath + '/productFile/changeProductFile.htm',
		type : 'post',
		dataType : 'json',
		data : {
			"productTypeId" : productTypeId,
			"directoryName" : $("#directoryName").val(),
			"fileUpState" : fileUpStateId,
			"isMust" : $("#isMust").val(),
			"insertOrUpdate" : $("#insertOrUpdate").val(),
			"id" : $("#id").val()
		},
		success : function(data) {
			backProductFile();
			search();
		}
	};
	$.ajax(options);
}

function update(id) {
	$("#selectShow").hide();
	$("#addProductFile").show();
	$("#changeProductFile input[type='text']").val("");
	$("#changeProductFile select").val(0);
	$("#error").html("");
	$("#insertOrUpdate").val(1);
	$("#id").val(id);
	// 根据ID查现有数据进行默认展示
	var url = serverPath + "/productFile/selectByPrimaryKey.htm";
	var data = {
		"Time" : new Date().getMilliseconds(),
		"id" : id
	};
	$.getJSON(url, data, function(result) {
		var productFile = result.data;
		$("#directoryName").val(productFile.directoryName);
		$("#isMust").val(productFile.isMust);
		var productNameId = "";
		var product = JSON.parse($("#product").val());
		for (var i = 0; i < product.length; i++) {
			if (product[i].nodeName == productFile.productName) {
				productNameId = product[i].id;
				$("#productName").val(productNameId);
				break;
			}
		}
		var fileUpStateId = "";
		var productFileUpState = JSON.parse($("#productFileUpState").val());
		for (var i = 0; i < productFileUpState.length; i++) {
			if (productFileUpState[i].nodeName == productFile.fileUpStateName) {
				fileUpStateId = productFileUpState[i].id;
				$("#fileUpState").val(fileUpStateId);
				break;
			}
		}

	});
}

function del(id) {
	$("#id").val(id);
	BootstrapDialog.confirm('确定删除选择的产品？', function(result){
		if(result){
			dodel(id);
		}
      });
}

function dodel(id) {
	var url = serverPath + "/productFile/deleteByPrimaryKey.htm";
	var data = {
		"Time" : new Date().getMilliseconds(),
		"id" : $("#id").val()
	};
	$.getJSON(url, data, function(result) {
		search();
	});
}