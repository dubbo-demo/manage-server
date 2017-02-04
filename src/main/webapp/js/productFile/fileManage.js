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

function search() {
	var applyLoanNo = $("#applyLoanNo").val();
	if (applyLoanNo == "") {
		BootstrapDialog.alert("请输入申请单编号");
		return;
	}
	$("#selectProductFile tbody").html("");
	var options = {
		url : serverPath + "/productFile/showFileManage.htm",
		type : 'post',
		dataType : 'json',
		data : {
			"Time" : new Date().getMilliseconds(),
			"applyLoanNo" : applyLoanNo
		},
		success : function(data) {
			var data = data.data;
			var href = serverPath + '/productFile/fileUpload.htm?applyLoanNo=' + data.applyLoanNo + '&isManage=1';
			var trHTML = "<tr id=" + data.id + ">" + "<td>"
					+ data.applyLoanNo + "</td>"+ "<td>"
					+ data.contractNo + "</td>" + "<td>"
					+ data.memberName + "</td>" + "<td>" + data.phone
					+ "</td>" + "<td>" + data.bmName + "</td>" + "<td>"
					+ data.areaName + "</td>" + "<td>"
					+ data.storeName + "</td>" + "<td>"
					+ (data.createTime != null ? data.createTime : "") + "</td>" + "<td>"
					+ data.customerServiceName + "</td>" + "<td>"
					+ data.stateName + "</td>"
					+ "<td><a target='_blank' href=" + href + ">附件管理</a></td></tr>";
			$("#selectProductFile").append(trHTML);
		}
	};
	$.ajax(options);
}