var FormSamples = function () {
		var custmerurl = serverPath+"/employee/showCustomerEmpoyee.htm";
	    return {   
	        init: function () {
	            $("#newCustomerServiceId").select2({
	                placeholder: "请选择",
	                allowClear: true,
	                minimumInputLength: 1,
	                query: function (query) {
	                	$.getJSON(custmerurl, {"nameSpell" : query.term}, function(result) {
		            		var data = {
		                        results: result.data
		                    };
		            		query.callback(data);
						});
	                }
	            });  
	            
	        }
	    };

	}();

$(function() {
	FormSamples.init();
});

function addInfo() {
	
	return false;
}
function save(event, e) {
	// 阻止冒泡
	ChkUtil.stopBubbleEvent(event);
	if($("#s2id_newCustomerServiceId .select2-choice span").html() == "请选择") {
		alert("请根据员工姓名首字母选择客服!");
		return false;
	}
	if($("#s2id_newCustomerServiceId .select2-choice span").html() != ChkUtil.select2Name) {
		$("#newCustomerSName").val($("#s2id_newCustomerServiceId .select2-choice span").html());
	}
	$.post(serverPath+'/receptionManage/allotInfo.htm', $('form').serialize(), function(res) {
		if (res.code == '0') {
			BootstrapDialog.alert("分配成功！", function(){
				window.location.href = serverPath + "/receptionManage/list.htm"+"?"+ChkUtil.getCookie("queryParams");
            });
		} else {
			BootstrapDialog.alert(res.message);
		}
	}, "json");
}

