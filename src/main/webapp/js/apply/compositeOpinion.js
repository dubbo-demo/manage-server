/**
 * 综合意见
 */
function compositeOpinionInit(data) {
	$('#form_wizard_1 .button-submit').click(function () {
		compositeOpinionNext();
    })
}

function compositeOpinionNext() {
	var form = $("#compositeOpinion_form");
	if (!form.valid()) {
		return false;
	}
	var fileValidate = false;
	$.ajax({
		url : serverPath+'/productFile/checkFile.htm?applyLoanNo='+applyLoanNo,
		type : 'get',
		dataType : 'json',
		async: false,
		success : function(res) {
			if (res.data) {
				fileValidate = true;
			}
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
	var state = form.find("select").val();
	if(state == "0"){
		BootstrapDialog.alert("请选择结论");
		return;
	}
	if(!fileValidate && state == FINISH){
		BootstrapDialog.alert("请上传相关产品附件");
		return false;
	}
	var record = {
			saveOrSubmit:FINISH
	}
	compositeOpinionSave(record);
}
function compositeOpinionSave(record){
	var form = $("#compositeOpinion_form");
	if(!record){
		record = {
				saveOrSubmit:COMPOSITE_OPINION
		};
	}
	record.applyNo = applyLoanNo;
	var dom = form.find("input");
	dom.each(function(index, input) {
		record[$(input).attr('name')] = $(input).val();
	});
	var desc = form.find("textarea").val();
	var state = form.find("select").val();
	record['desc'] = desc;
	record['state'] = state;

	$.ajax({
		url : serverPath+'/apply/newInfoIndex/opinionSubimit.htm',
		type : 'post',
		data : JSON.stringify(record),
		dataType : 'json',
		contentType : "application/json",
		success : function(res) {
			if (res.code == 0) {
				BootstrapDialog.alert(res.message,function(){
					window.location.href=serverPath+"/apply/manage/list.htm";
				});
			} else {
				BootstrapDialog.alert(res.message);
			}
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
}