$(function(){
	var  custmerurl =serverPath+ "/approve/searchApproveUser.htm";
	var state = $("#state").val();
	$("#newApproveUser").select2({
        placeholder: ChkUtil.select2Name,
        allowClear: true,
        minimumInputLength: 1,
        query: function (query) {
        	$.getJSON(custmerurl, {
        		"searchWord" : query.term,
        		"state" :$("#state").val()
        		}, function(result) {
        		var data = {
                    results: result.data
                };
        		query.callback(data);
			});
        }
    }); 
	
})

function saveApproveUser(){
	var value = $("#newApproveUser").val();
	if(value == ""){
		BootstrapDialog.alert("新审核人不能为空！");
		return;
	}
	$.ajax({
		url: serverPath + "/approve/saveApproveUser.htm",
		type:"post",
		data:{
			"applyLoanNo":$("#applyLoanNo").val(),
			"newApproveUser":value,
			"oldApproveUser":$("#oldApproveUser").val()
		},
		dataType:"json",
		success:function(result){
			if(result.code == 0){
				BootstrapDialog.alert("操作成功",function(){
					window.location.href="queryPageList.htm";
				});
			}else{
				BootstrapDialog.alert(result.message);
			}
		},
		error:function(){
			BootstrapDialog.alert("加载失败");
		}
	});
}