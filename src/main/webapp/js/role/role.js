function deleteById(id, event) {
	//阻止冒泡
	ChkUtil.stopBubbleEvent(event);
	BootstrapDialog.confirm('确定删除吗？',function(param){
			if (param) {
				$.ajax({
					url : serverPath + '/role/delete.htm',
					type : 'post',
					data : {
						id : id
					},
					dataType : 'json',
					success : function(res) {
						if (res.code == 0) {
							$("#role_" + id).remove();
						} else {
							BootstrapDialog.alert(res.message);
						}
					}
				})
			}
		}
	);
}

function role_save(){
	if(!jQSaveValidate()){
		return;
	}
	var roleName = $("#role_save input[name='roleName']").val();
	var roleCode = $("#role_save input[name='roleCode']").val();
	var roleType = $("#role_save select[name='roleType']").val();
	var record ={
		roleName:roleName,
		roleCode:roleCode,
		roleType:roleType
	}
	
	$.ajax({
		url : serverPath + '/role/save.htm',
		type : 'post',
		data : JSON.stringify(record),
		dataType : 'json',
		contentType : "application/json",
		success : function(res) {
			if (res.code == 0) {
				BootstrapDialog.alert("操作成功",function(){
					window.location.reload();
				});
			} else {
				BootstrapDialog.alert(res.message);
			}
		}
	});
}

function role_edit(){
	if(!jQEditValidate()){
		return;
	}
	var roleName = $("#role_edit input[name='roleName']").val();
	var roleCode = $("#role_edit input[name='roleCode']").val();
	var roleType = $("#role_edit select[name='roleType']").val();
	var id = $("#role_edit input[name='id']").val();
	var record ={
		roleName:roleName,
		roleCode:roleCode,
		roleType:roleType,
		id:id
	}
	$.ajax({
		url : serverPath + '/role/update.htm',
		type : 'post',
		data : JSON.stringify(record),
		dataType : 'json',
		contentType : "application/json",
		success : function(res) {
			if (res.code == 0) {
				BootstrapDialog.alert("操作成功",function(){
					window.location.reload();
				});
			} else {
				BootstrapDialog.alert(res.message);
			}
		}
	});
}

function role_update_status(status,id){
	var record ={
		status:status,
		id:id
	}
	$.ajax({
		url : serverPath + '/role/updateStatus.htm',
		type : 'post',
		data : JSON.stringify(record),
		dataType : 'json',
		contentType : "application/json",
		success : function(res) {
			if (res.code == 0) {
				BootstrapDialog.alert("操作成功",function(){
					window.location.reload();
				});
			} else {
				BootstrapDialog.alert(res.message);
			}
		}
	});
}

function role_permission_edit(){
	var treeObj = $.fn.zTree.getZTreeObj("treePullDown");
	var changeCheckedNodes = treeObj.getChangeCheckedNodes();
	var saves = [];
	var removes = [];
	for(var i in changeCheckedNodes){
		if(changeCheckedNodes[i].level == 2){//只保存最权限
			var entity = {
				roleId:treeSetting.roleId,
				permissionId:changeCheckedNodes[i].id
			}
			if(changeCheckedNodes[i].checked){
				saves.push(entity);
			}else{
				removes.push(entity);
			}
		}
	}
	$.ajax({
		url : serverPath + '/role/saveRolePermission.htm',
		type : 'post',
		data :JSON.stringify({
			saves:saves,
			removes:removes,
		}),
		dataType : 'json',
		contentType : "application/json",
		success : function(res) {
			if (res.code == 0) {
				BootstrapDialog.alert("保存成功",function(){
					window.location.reload();
				});
			} else {
				BootstrapDialog.alert(res.message);
			}
		}
	});
}

var treeSetting = {  
        check: {
			enable: true
		},
		data: {
			simpleData: {
				enable: true
			}
		}
    };

$(function() {
	$("*[data-id]").click(function(e) {
		var id = $(this).attr("data-id");
		$("#role_save").html("");
		$("#role_edit").load(serverPath + "/role/getTemplate.htm?id="+id,function(){
				$("#role_edit form").validate({
				rules: {
				            roleName: {
			                    required: true,
			                    maxlength:20,
			                    remote:{
			                    	url:serverPath + "/role/isExistRoleName.htm",
			                    	type:"post",
			                    	dataType:"json",
			                    	data:{
			                    		roleName:function(){return $("input[name='roleName']").val()},
			                    		roleOldName:function(){return $("input[name='roleName']").attr('data-value')}
			                    	}
			                    }
			                },
			                roleCode: {
			                    required: true,
			                    maxlength:20,
		                        english:true,
			                    remote:{
			                    	url:serverPath + "/role/isExistRoleCode.htm",
			                    	type:"post",
			                    	dataType:"json",
			                    	data:{
			                    		roleCode:function(){return $("input[name='roleCode']").val()},
			                    		roleOldCode:function(){return $("input[name='roleCode']").attr('data-value')}
			                    	}
			                    }
			                }
			            },
			    messages: { 
                    roleName:{
                    	remote:"角色名重复"
                    },
                    roleCode:{
                    	remote:"角色编号重复"
                    }
                },
			    errorPlacement: function (error, element) { 
	                    if (element.attr("name") == "positionIds") {
	                      	 error.addClass("no-left-padding").insertAfter("#form_2_positionIds_error");
	                    }else {
                   			 error.insertAfter(element);
                		}
            	}
			})
		});
	});
	$("#role_new").click(function(e){
		$("#role_edit").html("");
		$("#role_save").load(serverPath + "/role/getTemplate.htm",function(){
				 $("#role_save form").validate({
				rules: {
				            roleName: {
			                    required: true,
			                    maxlength:20,
			                    remote:{
			                    	url:serverPath + "/role/isExistRoleName.htm",
			                    	type:"post",
			                    	dataType:"json",
			                    	data:{
			                    		roleName:function(){return $("input[name='roleName']").val()}
			                    	}
			                    }
			                },
			                roleCode: {
			                    required: true,
			                    maxlength:20,
			                    english:true,
			                    remote:{
			                    	url:serverPath + "/role/isExistRoleCode.htm",
			                    	type:"post",
			                    	dataType:"json",
			                    	data:{
			                    		roleCode:function(){return $("input[name='roleCode']").val()}
			                    		
			                    	}
			                    }
			                },
			                positionIds:{
			                	required: true
			                }
			            },
			    messages: { 
                    positionIds: {
                        required: "请选择岗位"
                    },
                    roleName:{
                    	remote:"角色名重复"
                    },
                    roleCode:{
                    	remote:"角色编号重复"
                    }
                },
			    errorPlacement: function (error, element) { 
			    		console.log(element);
	                    if (element.attr("name") == "positionIds") {
	                      	 error.addClass("no-left-padding").insertAfter("#form_2_positionIds_error");
	                    }else {
                   			 error.insertAfter(element);
                		}
            	}
			})
		});
	});
	$("*[data-permission2-id]").click(function(e){
		$("#condition_roleid").val($(this).data("permission2-id"));
		$("#role_condition_content").load("${ctx}/roleCondition/init.htm?roleId="+$(this).data("permission2-id"),function(){

		});
	});
	$("*[data-permission-id]").click(function(e){
		var id = $(this).attr("data-permission-id");
		treeSetting.roleId = id;
		$.ajax({
			url : serverPath + '/role/getPermissionTree.htm',
			type : 'get',
			data : {roleId:id},
			dataType : 'json',
			success : function(res) {
				if (res.code == 0) {
				console.log(res.data);
					$.fn.zTree.init($("#treePullDown"), treeSetting, res.data);
				} else {
					BootstrapDialog.alert(res.message);
				}
			}
		});
	});
});

function jQSaveValidate(){
var formResult = $("#role_save form").validate().form();
	if(formResult){
		return true;
	}else{
		return false;
	}
}

function jQEditValidate(){
	var formResult = $("#role_edit form").validate().form();
	if(formResult){
		return true;
	}else{
		return false;
	}
}
