$(function(){
	$("input[type='input']").attr("disabled","disabled");
	$("input[type='text']").attr("disabled","disabled");
	$("input[type='number']").attr("disabled","disabled");
	$("select,textarea").attr("disabled","disabled");
	
	$(".unshare").hide();
	$(".inbox").hide();
	$(".inbox").click(function(){
		// 客户信息
		if($(this).data("savetype") == 'KHXX') {
			if(userinfo_updateInfo(this)) {
				return false;
			}
		}else if($(this).data("savetype") == 'LXR') { // 联系人
			contactsAddInfo();
		}else if($(this).data("savetype") == 'GRZC') {//个人资产
			personassetsAddInfo();
		}else if ($(this).data("savetype") == 'GZXX') {//工作信息
			jobInfoAddInfo();
		}
//		editClose(this);
	});
	$(".unshare").click(function(){
		 window.location.reload();//刷新当前页面
//		editClose2(this);
	});
	$(".pencil").click(function(){
		editOpen(this);
	});
	getNode("#prodType","proType");
	getNode("#loanUse","loanUse");
	
	// 联系人信息
	$("#tab_contacts_input").load(serverPath+"/apply/newInfoIndex/contactsinput.htm?&applyLoanNo="+applyLoanNo,window["detail_contactsInit"]);
	
	// 个人资产
	$("#tab_personassets").load(serverPath+"/apply/newInfoIndex/personassets.htm?&applyLoanNo="+applyLoanNo,window["detail_personassetsInit"]);
	
	// 工作信息
	$("#tab_job_info_input").load(serverPath+"/apply/newInfoIndex/jobinfoinput.htm?&applyLoanNo="+applyLoanNo,window["detail_jobInfoInit"]);
	
});

function queryBackFileForm() {
	var url = serverPath + "/productFile/fileUpload.htm";
	window.open(url + '?applyLoanNo=' + applyLoanNo);
}

function jobInfoAddInfo() {
	var form = $("#jobinfoinput_form");
	var f = false;
	$("#jobinfoinput_form .js_input").each(function(i,v){
		var _val = $(this).val();
		if(_val == "" || _val == -1){
			$(this).addClass("error");
			f = true;
		}else{
			$(this).removeClass("error");
		}
	});
	if(f) {
		return false;
	}
	jobInfoSave();
}

function personassetsAddInfo() {
	var record = {
			state:BACK_INIT
	};
	var form = $("#personassets_form");
	if (!form.valid()) {
		return false;
	}
	return personassetsSave(record);
}
var checkNum ;
function contactsAddInfo() {
	checkNum = $('#tab_contacts_input input:checked').length;
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
			if(_val == "" || _val == -1){
				$(this).addClass("error");
				f1 = true;
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
			if(_val == ""){
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
			if(_val == ""){
				$(this).addClass("error");
				f3 = true;
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
			if(_val == ""){
				$(this).addClass("error");
				f4 = true;
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
	
	if($(".form_class_test4 .linkManType").attr('checked') == "checked") {
		flag = checkPhone(".form_class_test4","linkManPhone");
	}
	if(!flag) {
		BootstrapDialog.alert('请输入有效的手机号码！');
		return false;
	}
	
	if($(".form_class_test1 .linkManType").attr('checked') == "checked") {
		contactsSaveInfo(undefined,"form_class_test1");
	}
	if($(".form_class_test2 .linkManType").attr('checked') == "checked") {
		contactsSaveInfo(undefined,"form_class_test2");
	}
	if($(".form_class_test3 .linkManType").attr('checked') == "checked") {
		contactsSaveInfo(undefined,"form_class_test3");
	}
	if($(".form_class_test4 .linkManType").attr('checked') == "checked") {
		contactsSaveInfo(undefined,"form_class_test4");
	}
}

function detail_contactsInit() {
	contactsInputInit();
	$("#tab_contacts_input input[type='input']").attr("disabled","disabled");
	$("#tab_contacts_input input[type='text']").attr("disabled","disabled");
	$("#tab_contacts_input input[type='number']").attr("disabled","disabled");
	$("#tab_contacts_input select,textarea").attr("disabled","disabled");
}

function detail_personassetsInit() {
	personassetsInit();
	$("#tab_personassets input[type='input']").attr("disabled","disabled");
	$("#tab_personassets input[type='text']").attr("disabled","disabled");
	$("#tab_personassets input[type='number']").attr("disabled","disabled");
	$("#tab_personassets select,textarea").attr("disabled","disabled");
}

function detail_jobInfoInit() {
	jobInfoInputInit();
	$("#tab_job_info_input input[type='input']").attr("disabled","disabled");
	$("#tab_job_info_input input[type='text']").attr("disabled","disabled");
	$("#tab_job_info_input input[type='number']").attr("disabled","disabled");
	$("#tab_job_info_input select,textarea").attr("disabled","disabled");
}

function validate() {
//	debugger;
	// 个人信息
	var userinfo_form = $("#tab_userinfo");
	if (!userinfo_form.valid()) {
		return false;
	}
	
	// 工作
	var form = $("#jobinfoinput_form");
	var f = false;
	$("#jobinfoinput_form .js_input").each(function(i,v){
		var _val = $(this).val();
		if(_val == "" || _val == -1){
			$(this).addClass("error");
			f = true;
		}else{
			$(this).removeClass("error");
		}
	});
	if(f) {
		return false;
	}
	
	// 资产
	var form = $("#personassets_form");
	if (!form.valid()) {
		return false;
	}
	
	// 联系人
	checkNum = $('#tab_contacts_input input:checked').length;
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
			if(_val == "" || _val == -1){
				$(this).addClass("error");
				f1 = true;
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
			if(_val == ""){
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
			if(_val == ""){
				$(this).addClass("error");
				f3 = true;
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
			if(_val == ""){
				$(this).addClass("error");
				f4 = true;
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
	
	if($(".form_class_test4 .linkManType").attr('checked') == "checked") {
		flag = checkPhone(".form_class_test4","linkManPhone");
	}
	if(!flag) {
		BootstrapDialog.alert('请输入有效的手机号码！');
		return false;
	}
	
//	if(checkPhoneMain()) {
//		return true;
//	} else {
//		return false;
//	}
	
	return true;
}

function reshareSave(){
	if(!validate()) {
		BootstrapDialog.alert("检查退回信息，红框提示必填");
		return;
	}
	
	var record = {
				saveOrSubmit:FINISH
	};
	record.applyNo = applyLoanNo;
	record['state'] = FINISH;
	$.ajax({
		url : serverPath+'/apply/newInfoIndex/opinionSubimit.htm',
		type : 'post',
		data : JSON.stringify(record),
		dataType : 'json',
		contentType : "application/json",
		success : function(res) {
			if (res.code == 0) {
				BootstrapDialog.alert("操作成功");
				window.location.href=serverPath+"/apply/manage/list.htm";
			} else {
				BootstrapDialog.alert(res.message);
			}
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
}

/**
 * 
 * @param xmlId 目标节点
 * @param parentCode 父id
 */
function getNode(xmlId,parentCode) {
	var url = serverPath+"/reception/showProduct.htm";
	var data = {
		"parentCode" : parentCode,
	};
	$.getJSON(url, data, function(result) {
		var resultData = result.data;
		for (var i = 0; i < resultData.length; i++) {
			if($(xmlId).data("value") == resultData[i].id) {
				$(xmlId).val(resultData[i].nodeName);
			}
		}
	});
}

/**
 * 
 * @param xmlId 目标节点
 * @param parentCode 父id
 */
function getNodeName(xmlId) {
	var url = serverPath+"/reception/showProductName.htm";
	var data = {
		"id" : $(xmlId).data("value"),
	};
	$.getJSON(url, data, function(result) {
		$(xmlId).val(result.data.nodeName);
	});
}

function editOpen(e) {
	$(e).hide()
	$(e).next().show();
	$(e).next().next().show();
	var tab = "#"+$(e).parent().data("ftl");
	$(tab+" input[type='input']").removeAttr("disabled");
	$(tab+" input[type='text']").removeAttr("disabled");
	$(tab+" input[type='number']").removeAttr("disabled");
	$(tab+" select").removeAttr("disabled");
	$(tab+" textarea").removeAttr("disabled");
}

function editClose(e) {
	$(e).prev().show();
	$(e).hide()
	$(e).next().hide();
	openInput(e);
}
function editClose2(e) {
	$(e).prev().prev().show();
	$(e).hide()
	$(e).prev().hide();
	openInput(e);
}

function openInput(e) {
	var tab = "#"+$(e).parent().data("ftl");
	$(tab+" input[type='input']").attr("disabled","disabled");
	$(tab+" input[type='text']").attr("disabled","disabled");
	$(tab+" input[type='number']").attr("disabled","disabled");
	$(tab+" select").attr("disabled","disabled");
	$(tab+" textarea").attr("disabled","disabled");
}