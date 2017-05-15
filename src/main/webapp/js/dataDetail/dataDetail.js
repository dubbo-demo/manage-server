$(function() {
	$('#addDataDetail').on('hide.bs.modal', function () {
		  $("#error").html('');
		})
		
	$('#updateDataDetail').on('hide.bs.modal', function () {
		  $("#errorUpdate").html('');
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

function cleanAdd(){
	("input").empty();
}

function addDataDetail(){
	var addDataDetailResult = '';
	if($("#addNum").val() == '' || $("#addCode").val() == '' || $("#addName").val() == ''){
		$("#error").html('<font color="red">请输入必选项</font>');
		$("#error").css("display", "block");
		return false;
	}
	//校验重复
	$.ajax({
		url : serverPath + "/dataDetail/addDataDetail.htm",
		type : "post",
		data : {
			num:$("#addNum").val(),
			code:$("#addCode").val(),
			name:$("#addName").val(),
			isMust:$("input[name='addIsMust']:checked").val(),
			"Time" : new Date().getMilliseconds()
		},
		dataType : "json",
		success : function(result) {
			addDataDetailResult = result.data;
			if(addDataDetailResult == 0){
				window.location.href = window.location;
				return true;
			}
			if(addDataDetailResult == 1){
				$("#error").html('<font color="red">名称重复</font>');
				$("#error").css("display", "block");
				return false;
			}
			if(addDataDetailResult == 2){
				$("#error").html('<font color="red">编号重复</font>');
				$("#error").css("display", "block");
				return false;
			}
			if(addDataDetailResult == 3){
				$("#error").html('<font color="red">名称和编号重复</font>');
				$("#error").css("display", "block");
				return false;
			}
			if(addDataDetailResult == 4){
				$("#error").html('<font color="red">位号重复</font>');
				$("#error").css("display", "block");
				return false;
			}
			if(addDataDetailResult == 5){
				$("#error").html('<font color="red">名称和位号重复</font>');
				$("#error").css("display", "block");
				return false;
			}
			if(addDataDetailResult == 6){
				$("#error").html('<font color="red">编号和位号重复</font>');
				$("#error").css("display", "block");
				return false;
			}
			if(addDataDetailResult == 7){
				$("#error").html('<font color="red">名称和编号和位号重复</font>');
				$("#error").css("display", "block");
				return false;
			}
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
}

function update(id){
	var numId = "#" + id + 'num';
	var codeId = "#" + id + 'code';
	var nameId = "#" + id + 'name';
	var isMustId = "#" + id + 'isMust';
	$("#updateNum").val($(numId).html());
	$("#updateCode").val($(codeId).html());
	$("#updateName").val($(nameId).html());
	if($(isMustId).html() == '必填'){
		$("input[name='updateIsMust']").eq(0).attr("checked", "checked");
		$("input[name='updateIsMust']").eq(1).removeAttr("checked");
		$("input[name='updateIsMust']").eq(0).click();
	}else{
		$("input[name='updateIsMust']").eq(0).removeAttr("checked");
		$("input[name='updateIsMust']").eq(1).attr("checked", "checked");
		$("input[name='updateIsMust']").eq(1).click();
	}
	$("#id").val(id);
}

function updateDataDetail(){
	var updateDataDetailResult = '';
	if($("#updateName").val() == ''){
		$("#errorUpdate").html('<font color="red">请输入必选项</font>');
		$("#errorUpdate").css("display", "block");
		return false;
	}
	$.ajax({
		url : serverPath + "/dataDetail/updateDataDetail.htm",
		type : "post",
		data : {
			"Time" : new Date().getMilliseconds(),
			"id" : $("#id").val(),
			"name" : $("#updateName").val(),
			"isMust" : $("input[name='updateIsMust']:checked").val()
		},
		dataType : "json",
		success : function(result) {
			updateDataDetailResult = result.data;
			if(updateDataDetailResult != 0){
				$("#errorUpdate").html('<font color="red">名称重复</font>');
				$("#errorUpdate").css("display", "block");
				return false;
			}else{
				window.location.href = window.location;
			}
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
}