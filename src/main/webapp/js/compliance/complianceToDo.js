function save() {
	var remark = $("#remark").val().replace(/(^\s*)|(\s*$)/g, "");
	if(remark == ""){
		BootstrapDialog.alert('请填写审核信息！');
		return false;
	}
	var options = {
		url : serverPath + '/compliance/saveCompliance.htm',
		type : 'post',
		data : {
			'applyLoanNo' : $("#applyLoanNo").val(),
			'result' : $("#resultSelect").val(),
			'remark' : remark
		},
		dataType : 'json',
		success : function(result) {
			var code = result.code;
			if(code == 1601){
				BootstrapDialog.alert('此单已被审核过', function(result) {
					window.location.href = serverPath + '/compliance/list.htm';
				});
			}else{
				BootstrapDialog.alert('提交成功', function(result) {
					window.location.href = serverPath + '/compliance/list.htm';
				});
			}
		}
	};
	$.ajax(options);
}