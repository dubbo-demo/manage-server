$(function(){
	$("input[type='input']").attr("readonly","readonly");
	$("input[type='text']").attr("readonly","readonly");
	$("textarea").attr("readonly","readonly");
	$("select").attr("disabled","disabled");
	getNodeName("#prodType");
	getNodeName("#loanUse");
	
	// 联系人信息
	$("#tab_contacts_input").load(serverPath+"/apply/newInfoIndex/contactsinput.htm?&applyLoanNo="+$("#applyLoanNo").val(),window["detail_contactsInit"]);
	
	// 个人资产
	$("#tab_personassets").load(serverPath+"/apply/newInfoIndex/personassets.htm?&applyLoanNo="+$("#applyLoanNo").val(),window["detail_personassetsInit"]);
	
	// 工作信息
	$("#tab_job_info_input").load(serverPath+"/apply/newInfoIndex/jobinfoinput.htm?&applyLoanNo="+$("#applyLoanNo").val(),window["detail_jobInfoInit"]);
	
});

function detail_contactsInit() {
	contactsInputInit();
	$("#tab_contacts_input input[type='input']").attr("readonly","readonly");
	$("#tab_contacts_input input[type='text']").attr("readonly","readonly");
	$("#tab_contacts_input input[type='number']").attr("readonly","readonly");
	$("#tab_contacts_input input[type='checkbox']").attr("disabled","disabled");
	$("#tab_contacts_input .add-on").unbind();
	$("#tab_contacts_input textarea").attr("readonly","readonly");
	$("#tab_contacts_input select").attr("disabled","disabled");
	$("input[type='input']").unbind();
	$("input[type='text']").unbind();
	$("input[type='number']").unbind();
}

function detail_personassetsInit() {
	personassetsInit();
	$("#tab_personassets input[type='input']").attr("readonly","readonly");
	$("#tab_personassets input[type='text']").attr("readonly","readonly");
	$("#tab_personassets input[type='number']").attr("readonly","readonly");
	$("#tab_personassets .add-on").unbind();
	$("#tab_personassets textarea").attr("readonly","readonly");
	$("#tab_personassets select").attr("disabled","disabled");
	$("input[type='input']").unbind();
	$("input[type='text']").unbind();
	$("input[type='number']").unbind();
}

function detail_jobInfoInit() {
	jobInfoInputInit();
	$("#tab_job_info_input input[type='input']").attr("readonly","readonly");
	$("#tab_job_info_input input[type='text']").attr("readonly","readonly");
	$("#tab_job_info_input input[type='number']").attr("readonly","readonly");
	$("#tab_job_info_input .add-on").unbind();
	$("#tab_job_info_input textarea").attr("readonly","readonly");
	$("#tab_job_info_input select").attr("disabled","disabled");
	$("input[type='input']").unbind();
	$("input[type='text']").unbind();
	$("input[type='number']").unbind();
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

function queryFileForm() {
	var url = serverPath + "/productFile/fileUpload.htm";
	window.open(url + '?isView=0&applyLoanNo=' + $("#applyLoanNo").val());
}

