//业务处理
function jobInfoInputInit(data) {
	var form = $("#jobinfoinput_form");
	form.validate({
		doNotHideMessage : true, // this option enables to show the
									// error/success messages on tab switch.
		errorElement : 'span', // default input error message container
		errorClass : 'validate-inline', // default input error message class
		focusInvalid : false, // do not focus the last invalid input
		rules : {
			// account
			monthlySalary : {
				required : true
			},
			salaryDay : {
				required : true
			},
			salaryWay : {
				required : true
			}
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

function jobInfoInputNext() {
	var form = $("#jobinfoinput_form");
	var f = false;
	$("#jobinfoinput_form .js_input").each(function(i,v){
		var _val = $(this).val();
		if(_val == "" || _val == -1 || _val == "请选择"){
			$(this).addClass("error");
			f = true;
		}else{
			$(this).removeClass("error");
		}
	});
	if(f) {
		return false;
	}
//	if (!form.valid()) {
//		return false;
//	}
	jobInfoSave("nextFlag");
}
function jobInfoSave(nextFlag){
	var form = $("#jobinfoinput_form");
	var record = {};
	//TODO
	record.companyNature = $("select[name='companyNature']").val();
	record.otherCompanyNature = $("select[name='otherCompanyNature']").val();
	record.positionsCode = $("select[name='positionsCode']").val();
	record.businessType = $("select[name='businessType']").val();
	record.otherBusinessType = $("select[name='otherBusinessType']").val();
	record.salaryWay = $("select[name='salaryWay']").val();
	var dom = form.find("input");
	dom.each(function(index, input) {
		record[$(input).attr('name')] = $(input).val();
	});
	if (nextFlag != undefined) { // 下一步处理，更新申请单子状态到下一步
		record.state = LINKMAN_INPUT;
	}
	$.ajax({
		url : serverPath+'/apply/newInfoIndex/jobInfoSave.htm',
		type : 'post',
		data : JSON.stringify(record),
		dataType : 'json',
		contentType : "application/json",
		success : function(res) {
			if (res.code == 0) {
				if (nextFlag == undefined) {
					BootstrapDialog.alert('操作成功!');
				}
			} else {
				BootstrapDialog.alert(res.message);
			}
		},
		error : function() {
			BootstrapDialog.alert('操作失败!');
		}
	});
}
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