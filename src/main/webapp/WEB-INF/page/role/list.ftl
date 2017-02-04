
<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
	<div class="page-content">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<!--页面标题-->
					<h3 class="page-title"></h3>
					<!--面包屑导航-->
					<ul class="breadcrumb">
						<li> <i class="icon-home"></i>
							<a href="${serverPath}/index.htm">首页</a>
							<i class="icon-angle-right"></i> 
						</li>
						<li>
							<a href="#">系统管理</a>
							<i class="icon-angle-right"></i> 
						</li>
						<li>
							<a href="#">角色权限管理</a>
						</li>
					</ul>
				</div>
			</div>
		<div>
		<form id="searchForm" class="form-horizontal"
					method="post">
		<p>
		<a class="btn blue" data-toggle="modal" href="#role_save" id="role_new">新增</a>
		</p>
		<@p.pageForm value=page  type="sort"/>
			<!-- table -->
			<div class="tabbable tabbable-custom tabbable-custom-profile">
			<table class="table table-bordered table-hover table-condensed">
				<thead>
					<tr>
						<th>角色编号</th>
						<th>角色名称</th>
						<th>权限设置</th>
						<th>角色状态</th>
						<th>岗位</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<#list result.result as role>
					<tr id="role_${role.id}">
						<td>${role.roleCode}</td>
						<td>${role.roleName }</td>
						<td><a class="btn blue" data-toggle="modal" data-permission-id="${role.id}" href="#role_permission">权限设置</a></td>
						<td>
						<label class="radio span">
							<input 
							type="radio"
							<#if (role.state!0) == 1>
								checked =  "checked"
							</#if>
								name="${role.id}_state"
								onclick="role_update_status(1,${role.id})"
								/>
							启用
						</label>
						<label class="radio span">
							<input 
							type="radio"
							<#if (role.state!0) == 0>
								checked =  "checked"
							</#if>
								name="${role.id}_state"
								onclick="role_update_status(0,${role.id})"
								/>
							禁用
						</label>
						</td>
						<td>${role.position}</td>
						<td><a data-toggle="modal" href="#role_edit"
							data-id="${role.id}">修改</a> <a
							href="javascript:void(0)"
							onclick="deleteById('${role.id}',event)">删除</a></td>
					</tr>
					</#list>
				</tbody>
			</table>
			<@p.pagination value=page />
		</form>
	</div>
	</div>
	
	<div id="role_edit" class="modal hide fade" tabindex="-1"
		data-width="760">

	</div>
	<div id="role_save" class="modal hide fade" tabindex="-1"
		data-width="760">
		
	</div>
	<div id="role_permission" class="modal hide fade" tabindex="-1"
		data-width="760" >
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h3>权限选择</h3>
			</div>
			<ul id="treePullDown" class="ztree" style="margin-top:0;height:400px;overflow-y:auto;overflow-x:hidden "></ul>
			<div class="modal-footer">
				<button type="button" data-dismiss="modal" class="btn">返回</button>
				<button type="button" class="btn blue" data-dismiss="modal" onclick="role_permission_edit();">提交</button>
			</div>
	</div>
	
<#include "/sys/bottom.ftl">
	<script>
		
		var currentId = null;
		function deleteById(id, event) {
			//阻止冒泡
			ChkUtil.stopBubbleEvent(event);
			BootstrapDialog.confirm('确定删除吗？',function(param){
					if (param) {
						$.ajax({
							url : '${ctx}/role/delete.htm',
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
			var positionIds = [];
			$("#role_save input[name='positionIds']:checked").each(function(index,dom){
				var value = $(dom).val();
				positionIds.push(value);
			});
			var record ={
				roleName:roleName,
				roleCode:roleCode,
				positionIds:positionIds
			}
			
			$.ajax({
				url : '${ctx}/role/save.htm',
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
			var id = $("#role_edit input[name='id']").val();
			var positionIds = [];
			$("#role_edit input[name='positionIds']:checked").each(function(index,dom){
				var value = $(dom).val();
				positionIds.push(value);
			});
			var record ={
				roleName:roleName,
				roleCode:roleCode,
				positionIds:positionIds,
				id:id
			}
			$.ajax({
				url : '${ctx}/role/update.htm',
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
				url : '${ctx}/role/updateStatus.htm',
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
				url : '${ctx}/role/saveRolePermission.htm',
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
				$("#role_edit").load("${ctx}/role/getTemplate.htm?id="+id,function(){
						$("#role_edit form").validate({
						rules: {
						            roleName: {
					                    required: true,
					                    maxlength:20,
					                    remote:{
					                    	url:"${ctx}/role/isExistRoleName.htm",
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
					                    	url:"${ctx}/role/isExistRoleCode.htm",
					                    	type:"post",
					                    	dataType:"json",
					                    	data:{
					                    		roleCode:function(){return $("input[name='roleCode']").val()},
					                    		roleOldCode:function(){return $("input[name='roleCode']").attr('data-value')}
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
				$("#role_save").load("${ctx}/role/getTemplate.htm",function(){
						 $("#role_save form").validate({
						rules: {
						            roleName: {
					                    required: true,
					                    maxlength:20,
					                    remote:{
					                    	url:"${ctx}/role/isExistRoleName.htm",
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
					                    	url:"${ctx}/role/isExistRoleCode.htm",
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
			$("*[data-permission-id]").click(function(e){
				var id = $(this).attr("data-permission-id");
				treeSetting.roleId = id;
				$.ajax({
					url : '${ctx}/role/getPermissionTree.htm',
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
				var len = $("#role_save input[name='positionIds']:checked").length
				console.log(len);
				if(len>0){
					$("#form_2_positionIds_error").html("");
					return true;
				}else{
					$("#form_2_positionIds_error").html("请选择岗位").css("color","red");
					return false;
				}
			}else{
				return false;
			}
			
			
		}
		function jQEditValidate(){
			var formResult = $("#role_edit form").validate().form();
			if(formResult){
				var len = $("#role_edit input[name='positionIds']:checked").length
				console.log(len);
				if(len>0){
					$("#form_2_positionIds_error").html("");
					return true;
				}else{
					$("#form_2_positionIds_error").html("请选择岗位").css("color","red");
					return false;
				}
			}else{
				return false;
			}
			
			
		}
	</script>

