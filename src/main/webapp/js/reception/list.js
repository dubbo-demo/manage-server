$(function() {
	loadStores();
});

// 加载门店
function loadStores() {
	$("#storeId").empty();
	if(ChkUtil.isNull($("#empStoreId").val())) {
		$("#storeId").append("<option value=''>请选择</option>");
	}
	var url;
	var parentId  =  regionId;
	if(ChkUtil.isNull(parentId)) {
		return;
	}
	var queryData;
	if(orgType == 3)
	{
		url = serverPath + "/organization/selectOrganizationById.htm";
		queryData ={"id":$("#empStoreId").val(),"Time" : new Date().getMilliseconds()}
		$.getJSON(url, queryData, function(result) {
			var data = result.data;
			var op = "<option value=" + data.id + ">" + data.orgName
			+ "</option>";
			$("#storeId").append(op);
			if(ChkUtil.isNull($("#empStoreId").val())) {
				$("#storeId").val($("#storeId2").val());
			} else {
				$("#storeId").val($("#empStoreId").val());
			}
			
		});
	}else{
		url = serverPath + "/organization/selectOrgByParentId.htm";
		queryData ={"parentId" : parentId,	"Time" : new Date().getMilliseconds()}
		$.getJSON(url, queryData, function(result) {
			var data = result.data;
			for (var i = 0; i < data.length; i++) {
				var op = "<option value=" + data[i].id + ">" + data[i].orgName
						+ "</option>";
				$("#storeId").append(op);
			}
			if(ChkUtil.isNull($("#empStoreId").val())) {
				$("#storeId").val($("#storeId2").val());
			} else {
				$("#storeId").val($("#empStoreId").val());
			}
			
		});
	};
}

function search() {
	form_trim();
//	$("#searchForm").attr("action", "${ctx}/innerBlack/list.htm");
	$("#searchForm").submit();
}

function deleteTeam(id,event,state) {
	//阻止冒泡
	var message = "";
	if(state == 3) {
		message = '客户放弃';
	} else {
		message = '门店拒绝';
	}
	$(".refuseDivTitle").html(message);
	$("#rid").val(id);
	$("#rstate").val(state);
	return false;
}

function goDelte(event) {
	ChkUtil.stopBubbleEvent(event);
	if(ChkUtil.isNull($("#description").val().trim())) {
		BootstrapDialog.alert("请填写内容！");
		return;
	}
	$.ajax({
		url : serverPath + '/reception/updateState.htm',
		type : 'post',
		data : {
			id : $("#rid").val(),
			state : $("#rstate").val(),
			description : $("#description").val().trim()
		},
		dataType : 'json',
		success : function(res) {
			$("#refuse").modal('hide');
			if (res.code == '0') {
				BootstrapDialog.alert("操作信息成功",function(){
					$("#searchForm").submit();
				});
			}else{
				BootstrapDialog.alert(res.message);
			}
		}
	})
}

function form_trim(){
	var $input=$("#searchForm input[type='text']");
	$input.each(function(i,n){
		 var value = $(n).val(); 
		 $(n).val($.trim(value));
	})
}

function closeWin() {
}