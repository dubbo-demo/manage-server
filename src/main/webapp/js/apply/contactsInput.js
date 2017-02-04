//将表单序列化成json格式的数据(但不适用于含有控件的表单，例如复选框、多选的select)
        (function($){
            $.fn.serializeJson = function(){
                var jsonData1 = {};
                var serializeArray = this.serializeArray();
                // 先转换成{"id": ["12","14"], "name": ["aaa","bbb"], "pwd":["pwd1","pwd2"]}这种形式
                $(serializeArray).each(function () {
                    if (jsonData1[this.name]) {
                        if ($.isArray(jsonData1[this.name])) {
                            jsonData1[this.name].push(this.value);
                        } else {
                            jsonData1[this.name] = [jsonData1[this.name], this.value];
                        }
                    } else {
                        jsonData1[this.name] = this.value;
                    }
                });
                // 再转成[{"id": "12", "name": "aaa", "pwd":"pwd1"},{"id": "14", "name": "bb", "pwd":"pwd2"}]的形式
                var vCount = 0;
                // 计算json内部的数组最大长度
                for(var item in jsonData1){
                    var tmp = $.isArray(jsonData1[item]) ? jsonData1[item].length : 1;
                    vCount = (tmp > vCount) ? tmp : vCount;
                }

                if(vCount > 1) {
                    var jsonData2 = new Array();
                    for(var i = 0; i < vCount; i++){
                        var jsonObj = {};
                        for(var item in jsonData1) {
                        	if(item == "applyLoanNo") {
                        		jsonObj[item] = jsonData1[item];
                        	} else if(item == "linkManPhone") {
                        		jsonObj[item] = jsonData1[item][i];
                        	} else {
                        		jsonObj[item] = jsonData1[item][i];
                        	}
                        }
                        jsonData2.push(jsonObj);
                    }
                    return JSON.stringify(jsonData2);
                }else{
                    return "[" + JSON.stringify(jsonData1) + "]";
                }
            };
        })(jQuery);
//业务处理
function contactsInputInit(data) {
	var form = $("#contactsinput_form");
	form.validate({
		doNotHideMessage : true, // this option enables to show the
									// error/success messages on tab switch.
		errorElement : 'span', // default input error message container
		errorClass : 'validate-inline', // default input error message class
		focusInvalid : false, // do not focus the last invalid input
		rules : {
		},

		messages : { // custom messages for radio buttons and checkboxes
		},

		errorPlacement : function(error, element) { // render error placement
													// for each input type
		},

		invalidHandler : function(event, validator) { // display error alert
														// on form submit
		},

		highlight : function(element) { // hightlight error inputs
			$(element).closest('.help-inline').removeClass('ok'); // display
																	// OK icon
			$(element).closest('.control-group').removeClass('success')
					.addClass('error'); // set error class to the control group
		},

		unhighlight : function(element) { // revert the change dony by
											// hightlight
			$(element).closest('.control-group').removeClass('error'); // set
																		// error
																		// class
																		// to
																		// the
																		// control
																		// group
		},

		success : function(label) {
			if (label.attr("for") == "gender"
					|| label.attr("for") == "payment[]") { // for checkboxes
															// and radip
															// buttons, no need
															// to show OK icon
				label.closest('.control-group').removeClass('error').addClass(
						'success');
				label.remove(); // remove error label here
			} else { // display success icon for other inputs
				label.addClass('valid ok') // mark the current input as valid
											// and display OK icon
				.closest('.control-group').removeClass('error').addClass(
						'success'); // set success class to the control group
			}
		},
		submitHandler : function(form) {

		}

	});
}

function contactsInputNext() {
	$('.phone_repeat').remove();
	var f1= false; var f2= false;var f3= false;var f4 = false;
	var phone1;
	var phone2;
	var phone3;
	var phone4;
	var num = 0;
	$(".linkManType").each(function(i,v) {
		if($(v).attr('checked')) {
			num ++;
		}
	});
	if(num < 3) {
		BootstrapDialog.alert("最少选中3种联系人类型");
		return false;
	};
	
	if($(".form_class_test1 .linkManType").attr('checked') == "checked") {
		$(".form_class_test1 .js_input").each(function(i,v){
			var _val = $(this).val();
			if(v.name == "linkManPhone") {
				phone1 = _val;
			}
			if(_val == "" || _val == -1 || _val == "请选择"){
				$(this).addClass("error");
				f1 = true;
			}else{
				$(this).removeClass("error");
			}
		});
	};
	
	if($(".form_class_test4 .linkManType").attr('checked') == "checked") {
		$(".form_class_test4 .js_input").each(function(i,v){
			var _val = $(this).val();
			if(v.name == "linkManPhone") {
				phone4 = _val;
				var flag = false;
				if(phone4 == phone1){
					flag = true;
				}
				if(phone4 == phone2){
					flag = true;
				}
				if(phone4 == phone3){
					flag = true;
				}
				if(flag) {
					$(this).parent().append("<span class='phone_repeat' style='color:red'>手机号不能重复!</span>");
					f3 = true;
				}
			}
			if(_val == "" || _val == -1 || _val == "请选择"){
				$(this).addClass("error");
				f4 = true;
			}else{
				$(this).removeClass("error");
			}
		});
	};
	
	if($(".form_class_test2 .linkManType").attr('checked') == "checked") {
		$(".form_class_test2 .js_input").each(function(i,v) {
			var _val = $(this).val();
			if(v.name == "linkManPhone") {
				phone2 = _val;
				if(phone2 == phone1){
					$(this).parent().append("<span class='phone_repeat' style='color:red'>手机号不能重复!</span>");
					f2 = true;
				}
			}
			if(_val == "" || _val == -1 || _val == "请选择"){
				$(this).addClass("error");
				f2 = true;
			}else{
				$(this).removeClass("error");
			}
		});
	};
	if($(".form_class_test3 .linkManType").attr('checked') == "checked") {
		$(".form_class_test3 .js_input").each(function(i,v) {
			var _val = $(this).val();
			if(v.name == "linkManPhone") {
				phone3 = _val;
				var flag = false;
				if(phone3 == phone1){
					flag = true;
				}
				if(phone3 == phone2){
					flag = true;
				}
				if(flag) {
					$(this).parent().append("<span class='phone_repeat' style='color:red'>手机号不能重复!</span>");
					f3 = true;
				}
			}
			if(_val == "" || _val == -1 || _val == "请选择"){
				$(this).addClass("error");
				f3 = true;
			}else{
				$(this).removeClass("error");
			}
		});
	};
	
	
	if(f1 == true || f2 == true || f3 == true || f4 == true) {
		return false;
	}
	var flag = true;
	if($(".form_class_test1 .linkManType").attr('checked') == "checked") {
		flag = checkPhone(".form_class_test1","linkManPhone");
	}
	if(!flag) {
		BootstrapDialog.alert('请输入有效的手机号码！');
		return false;
	}
	
	if($(".form_class_test4 .linkManType").attr('checked') == "checked") {
		flag = checkPhone(".form_class_test4","linkManPhone");
	}
	if(!flag) {
		BootstrapDialog.alert('请输入有效的手机号码！');
		return false;
	}
	
	if($(".form_class_test2 .linkManType").attr('checked') == "checked") {
		flag = checkPhone(".form_class_test2","linkManPhone");
	}
	if(!flag) {
		BootstrapDialog.alert('请输入有效的手机号码！');
		return false;
	}
	
	if($(".form_class_test3 .linkManType").attr('checked') == "checked") {
		flag = checkPhone(".form_class_test3","linkManPhone");
	}
	if(!flag) {
		BootstrapDialog.alert('请输入有效的手机号码！');
		return false;
	}
	
	if($(".form_class_test1 .linkManType").attr('checked') == "checked") {
		contactsSaveInfo("nextFlag","form_class_test1",PERSON_ASSETS);
	}
	if($(".form_class_test4 .linkManType").attr('checked') == "checked") {
		contactsSaveInfo("nextFlag","form_class_test4",PERSON_ASSETS);
	}
	if($(".form_class_test2 .linkManType").attr('checked') == "checked") {
		contactsSaveInfo("nextFlag","form_class_test2",PERSON_ASSETS);
	}
	if($(".form_class_test3 .linkManType").attr('checked') == "checked") {
		contactsSaveInfo("nextFlag","form_class_test3",PERSON_ASSETS);
	}
}

function checkPhoneMain() {
	var flag = true;
	if($(".form_class_test1 .linkManType").attr('checked') == "checked") {
		flag = checkPhone(".form_class_test1","linkManPhone");
	}
	if(!flag) {
		BootstrapDialog.alert('请输入有效的手机号码！');
		return false;
	}
	
	if($(".form_class_test4 .linkManType").attr('checked') == "checked") {
		flag = checkPhone(".form_class_test4","linkManPhone");
	}
	if(!flag) {
		BootstrapDialog.alert('请输入有效的手机号码！');
		return false;
	}
	
	if($(".form_class_test2 .linkManType").attr('checked') == "checked") {
		flag = checkPhone(".form_class_test2","linkManPhone");
	}
	if(!flag) {
		BootstrapDialog.alert('请输入有效的手机号码！');
		return false;
	}
	
	if($(".form_class_test3 .linkManType").attr('checked') == "checked") {
		flag = checkPhone(".form_class_test3","linkManPhone");
	}
	if(!flag) {
		BootstrapDialog.alert('请输入有效的手机号码！');
		return false;
	}
	
	return flag;
}

var checkNum = 0;
function contactsSave(nextFlag) {
	checkNum = $('input:checked').length;
	var num = 0;
	if($(".form_class_test1 .linkManType").attr('checked') == "checked") {
		num ++;
		contactsSaveInfo(undefined,"form_class_test1");
	}
	if($(".form_class_test4 .linkManType").attr('checked') == "checked") {
		num ++;
		contactsSaveInfo(undefined,"form_class_test4");
	}
	if($(".form_class_test2 .linkManType").attr('checked') == "checked") {
		num ++;
		contactsSaveInfo(undefined,"form_class_test2");
	}
	if($(".form_class_test3 .linkManType").attr('checked') == "checked") {
		num ++;
		contactsSaveInfo(undefined,"form_class_test3");
	}
	if(num == 0) {
		BootstrapDialog.alert('请先选中多选框再提交!');
	}
}

function checkPhone(formID,elementID) {
	var flag = false;
	var phone = $(formID+ ' input[name='+elementID+']').val();
	if(!(/^1[34578]\d{9}$/.test(phone))) {
		flag = false;
		$(formID+ ' input[name='+elementID+']').addClass("error");
	} else {
		flag = true;
		$(formID+" ."+elementID).removeClass("error");
	}
	return flag;
}

function contactsSaveInfo(nextFlag,formID,next) {
	debugger;
	if(next != undefined) { // 下一步
		$('.' + formID + ' input[name=state]').val(next);
	}
	$(".form_class_test1  select[name='linkManRelation']").removeAttr("disabled"); 
    var jsonStr = $("."+formID).serialize();
	$.ajax({
		url : serverPath+'/apply/newInfoIndex/contactsSave.htm',
		type : 'post',
		data : jsonStr,
		dataType : 'json',
		success : function(res) {
			$(".form_class_test1  select[name='linkManRelation']").attr("disabled","disabled");
			if (res.code == 0) {
				if(checkNum > 0) {
					checkNum--;
					if(checkNum == 0) {
						BootstrapDialog.alert('操作成功!');
					}
				}
			} else {
				if(checkNum > 0) {
					checkNum--;
					if(checkNum == 0) {
						BootstrapDialog.alert(res.message);
					}
				}
			}
		},
		error : function() {
			$(".form_class_test1  select[name='linkManRelation']").attr("disabled","disabled");
			if(checkNum > 0) {
				checkNum--;
				if(checkNum == 0) {
					BootstrapDialog.alert("系统异常");
				}
			}
		}
	});
}