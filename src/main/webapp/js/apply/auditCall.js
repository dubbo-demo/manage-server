function contactsInputNext() {
	var f = false;
	$(".js_input").each(function(i, v) {
		var _val = $(this).val();
		_val = _val.replace(/(^\s*)|(\s*$)/g, "");// 删除二边空格
		if (_val == "") {
			$(this).addClass("error");
			f = true;
		} else {
			$(this).removeClass("error");
		}
	});

	if (f) {
		return false;
	} else {
		return true;
	}
}

//将表单序列化成json格式的数据(但不适用于含有控件的表单，例如复选框、多选的select)
(function($) {
	$.fn.serializeJson = function() {
		var jsonData1 = {};
		var serializeArray = this.serializeArray();
		// 先转换成{"id": ["12","14"], "name": ["aaa","bbb"],
		// "pwd":["pwd1","pwd2"]}这种形式
		$(serializeArray).each(
				function() {
					if (jsonData1[this.name]) {
						if ($.isArray(jsonData1[this.name])) {
							jsonData1[this.name].push(this.value);
						} else {
							jsonData1[this.name] = [ jsonData1[this.name],
									this.value ];
						}
					} else {
						jsonData1[this.name] = this.value;
					}
				});
		// 再转成[{"id": "12", "name": "aaa", "pwd":"pwd1"},{"id": "14", "name":
		// "bb", "pwd":"pwd2"}]的形式
		var vCount = 0;
		// 计算json内部的数组最大长度
		for ( var item in jsonData1) {
			var tmp = $.isArray(jsonData1[item]) ? jsonData1[item].length : 1;
			vCount = (tmp > vCount) ? tmp : vCount;
		}

		if (vCount > 1) {
			var jsonData2 = new Array();
			for (var i = 0; i < vCount; i++) {
				var jsonObj = {};
				for ( var item in jsonData1) {
					jsonObj[item] = jsonData1[item][i];
				}
				jsonData2.push(jsonObj);
			}
			return JSON.stringify(jsonData2);
		} else {
			return "[" + JSON.stringify(jsonData1) + "]";
		}
	};
})(jQuery);

function saveInfo() {
	var checkResult = contactsInputNext();
	if (!checkResult) {
		return false;
	}
	var aArray =  new Array();
	var addAuditCallForm = $("#addAuditCallForm").serializeJson();
	if(addAuditCallForm != "[{}]"){
		var obj = $("#addAuditCallForm");
		var stringInput = eval(addAuditCallForm);
		var flag = true;
		
		$.each(stringInput, function (n, value) {
			var type = value.type;
			if(type == 0){
				BootstrapDialog.alert("请先选择联系人类型");
				flag = false;
				return false;
			}
			var phone = value.phone;
			if($.inArray(phone, aArray) != -1){
				BootstrapDialog.alert("新增号码已存在，请勿重复添加");
				flag = false;
				return false;
			}else{
				aArray.push(value.phone);
			}
		});
		if(!flag){
			return false;
		}
		$.each(stringInput, function (n, value) {
			var phone = value.phone;
			var options = {
					url : serverPath + '/audit/checkAddAuditCall.htm',
					type : 'post',
					async: false,
					dataType : 'json',
					data : {
						"phone":phone,
						"applyLoanNo":$("#applyLoanNo").val()
					},
					success : function(data) {
						var code = data.code;
						if(code == 0){
							if(data.data != 0){
								BootstrapDialog.alert("新增号码已存在，请勿重复添加");
								flag = false;
								return false;
							}
						}else{
							BootstrapDialog.alert("电调新增号码失败");
						}
					}
				};
				$.ajax(options);
		});
		if(!flag){
			return false;
		}
	}
	var changeAuditCall = $("#changeAuditCall").serializeJson();
	var stringChangeAuditCall = eval(changeAuditCall);
	var flag = true;
	$.each(stringChangeAuditCall, function (n, value) {
		var phone = value.phone;
		if($.inArray(phone, aArray) != -1){
			BootstrapDialog.alert("新增号码已存在，请勿重复添加");
			flag = false;
			return false;
		}else{
			aArray.push(value.phone);
		}
	});
	if(!flag){
		return false;
	}
	
	var options = {
		url : serverPath + '/audit/saveAuditCall.htm',
		type : 'post',
		dataType : 'json',
		data : {
			"auditCallJson":changeAuditCall,
			"applyLoanNo":$("#applyLoanNo").val(),
			"memberId":$("#memberId").val()
		},
		success : function(data) {
			var code = data.code;
			if(code == 1){
				BootstrapDialog.alert("保存信息失败");
			}else{
				var msg = data.data;
				if(msg != null && msg != ""){
					BootstrapDialog.alert(msg);
					return false;
				}
				if(addAuditCallForm == "[{}]"){
					BootstrapDialog.alert('保存信息成功', function() {
						window.location.href = window.location;
					});
				}else{
					addAuditCallInfo();
				}
			}
		}
	};
	$.ajax(options);
}

function addAuditCallInfo() {
	var addAuditCallForm = $("#addAuditCallForm").serializeJson();
	var options = {
		url : serverPath + '/audit/addAuditCall.htm',
		type : 'post',
		dataType : 'json',
		data : {
			"auditCallJson":addAuditCallForm,
			"applyLoanNo":$("#applyLoanNo").val(),
			"memberId":$("#memberId").val()
		},
		success : function(data) {
			var code = data.code;
			if(code == 1){
				BootstrapDialog.alert("保存信息失败");
			}else{
				BootstrapDialog.alert('保存信息成功', function() {
					window.location.href = window.location;
				});
			}
		}
	};
	$.ajax(options);
}

function checkAddAuditCall(obj){
	checkInput('^[\(\)\-\.0-9\uff08\uff09]*$',obj);
	var options = {
			url : serverPath + '/audit/checkAddAuditCall.htm',
			type : 'post',
			dataType : 'json',
			data : {
				"phone":$(obj).val(),
				"applyLoanNo":$("#applyLoanNo").val()
			},
			success : function(data) {
				var code = data.code;
				if(code == 0){
					if(data.data != 0){
						BootstrapDialog.alert("新增号码已存在，请勿重复添加");
					}
				}else{
					BootstrapDialog.alert("电调新增号码失败");
				}
			}
		};
		$.ajax(options);
}

function checkInput(patrn, obj) {
	contactsInputNext();
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

function checktype(obj){
	var type = $(obj).parent().parent().find('.type').val();
	if(type == 0){
		BootstrapDialog.alert("请先选择联系人类型");
		return false;
	}
}

function addInfo(){
	var styleInfo = "text-decoration:none;background-image:url("+ serverPath + "/images/remove.png)";
	var info = "<div class='control-group' style='padding-top:1px;display:table;width:100%;'> <div style='display:table-cell; vertical-align:middle;width:11%;'><input type='hidden' name='oldRemark' value='!!' /> <a href=javascript:void(0) onclick='removeAuditCall(this)' style="+styleInfo+">&nbsp&nbsp&nbsp&nbsp</a> <input type='text' name='name' maxlength='50' class='span8 js_input'/> </div> <div style='display:table-cell; vertical-align:middle;width:11%;'> <select class='m-wrap span2 type' style='width:50%' id='type' name='type'><option value='0'>请选择</option> <option value='3'>配偶</option> <option value='4'>父母</option> <option value='5'>子女</option> <option value='6'>同事</option> <option value='7'>朋友</option> <option value='8'>备用号码</option> <option value='9'>其他亲属</option></select> </div> <div style='display:table-cell; vertical-align:middle;width:16%;'> <input type='text' style='margin-right:30px;' name='phone' maxlength='20' onclick='checktype(this)' onblur='contactsInputNext()' class='m-wrap medium js_input'/> </div> <div style='display:table-cell; vertical-align:middle;width:73%;'> <textarea rows='2' maxlength='300' name='remark' placeholder='请输入调查结果' onclick='checktype(this)' onblur='contactsInputNext()' class='m-wrap span12'></textarea> </div> </div>";
	$("#addAuditCall").append(info);
}

function removeAuditCall(obj){
	$(obj).parent().parent().remove();
}
