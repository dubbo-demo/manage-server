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
							<a href="${ctx}/index.htm">首页</a> <i class="icon-angle-right"></i> </li>
						<li><a href="#">系统管理</a><i class="icon-angle-right"></i></li>
						<li>
							<a href="#">权限中心</a>
						</li>
					</ul>
				</div>
			</div>
		<div>
		<div class="portlet-body form">
			<p>
			<a class="btn blue" data-toggle="modal" href="#permission_save">新增</a>
			<a class="btn blue" href="${ctx}/menu/childrenMenu.htm?id=${menuDto.parentId}">返回上一级</a>
			</p>
		</div>
		<div class="tabbable tabbable-custom tabbable-custom-profile">
			<!-- table -->
			<table class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th>编号</th>
						<th>权限名称</th>
						<th>访问路径</th>
						<th>权限代码</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<#list contents as permission>
					<tr id="permission_${permission.id}">
						<td>${permission.id}</td>
						<td>${permission.permissionName }</td>
						<td>${permission.permissionUrl }</td>
						<td>${permission.permissionCode }</td>
						<td><a data-toggle="modal" href="#permission_edit"
							data-id="${permission.id}">修改</a> <a
							href="javascript:void(0)"
							onclick="deleteById('${permission.id}',event)">删除</a></td>
					</tr>
					</#list>
				</tbody>
			</table>
		</div>
	</div>
	<div id="permission_edit" class="modal hide fade" tabindex="-1"
		data-width="760">
		<form action="#" id="edit_form">
		<div class="modal-header">

			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true"></button>

			<h3>修改</h3>

		</div>

		<div class="modal-body">

			<div class="row-fluid">

				<div class="span6">

					<p>
						<label class="control-label">权限名称<span class="required">*</span></label><input type="text" class="span12 m-wrap"
							name="permissionName" maxlength="15">
					</p>

					<p>
						<label class="control-label">权限代码<span class="required">*</span></label><input type="text" class="span12 m-wrap"
							name="permissionCode" maxlength="30">
					</p>

					<p>
						<label class="control-label">权限URL<span class="required">*</span></label><input type="text" class="span12 m-wrap"
							name="permissionUrl" maxlength="50">
					</p>

					<input type="hidden" value="${menuDto.id}" name="menuId">

				</div>


			</div>

		</div>

		<div class="modal-footer">

			<button type="button" data-dismiss="modal" class="btn">返回</button>

			<button type="button" class="btn blue"
				onclick="permission_edit();">提交</button>

		</div>
		</form>
	</div>
	<div id="permission_save" class="modal hide fade" tabindex="-1"
		data-width="760">
		<form action="#" id="save_form">
		<div class="modal-header">

			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true"></button>

			<h3>新增</h3>

		</div>

		<div class="modal-body">

			<div class="row-fluid">

				<div class="span6">

					<p>
						<label class="control-label">权限名称<span class="required">*</span></label><input type="text" class="span12 m-wrap"
							name="permissionName" maxlength="15">
					</p>

					<p>
						<label class="control-label">权限代码<span class="required">*</span></label><input type="text" class="span12 m-wrap"
							name="permissionCode" maxlength="30">
					</p>

					<p>
						<label class="control-label">权限URL<span class="required">*</span></label><input type="text" class="span12 m-wrap"
							name="permissionUrl" maxlength="50">
					</p>

					<input type="hidden" value="${menuDto.id}" name="menuId">

				</div>


			</div>

		</div>

		<div class="modal-footer">

			<button type="button" data-dismiss="modal" class="btn">返回</button>

			<button type="button" class="btn blue"  onclick="permission_save()">提交</button>

		</div>
		<form>
		</div>
		</div>
	</div>
<#include "/sys/bottom.ftl">
	<script>
		var currentId = null;
		function deleteById(id, event) {
			//阻止冒泡
			ChkUtil.stopBubbleEvent(event);
     	    BootstrapDialog.confirm({
            message: '确定删除此权限吗？',
            type: BootstrapDialog.TYPE_WARNING,
            callback: function(result) {
                if(result) {
                      $.ajax({
						url : '${ctx}/permission/delete.htm',
						type : 'post',
						data : {
							permissionId : id
						},
						dataType : 'json',
						success : function(res) {
							if (res.code == 0) {
								$("#permission_" + id).remove();
							} else {
								BootstrapDialog.alert(res.message);
							}
						}
					})
                }else {
                    //alert('Nope.');
                }
            }
        });
	       
		}
		function getRecordById(id, event) {
			currentId = id;
			//阻止冒泡
			ChkUtil.stopBubbleEvent(event);
			$.ajax({
				url : '${ctx}/permission/getRecordById.htm',
				type : 'post',
				data : {
					permissionId : id
				},
				dataType : 'json',
				success : function(res) {
					if (res.code == 0) {
						var record = res.data;
						for ( var attr in record) {
							$("#permission_edit input[name='" + attr + "']")
									.val(record[attr]);
						}
						$("*[data-id='" + id + "']").data(record);
					} else {
						BootstrapDialog.alert(res.message);
					}
				}
			});
		}
		function permission_edit() {
			if(!permission_validate('#edit_form')){
				return;
			}
			$("#permission_edit").modal('hide');
			var record = $("*[data-id='" + currentId + "']").data();
			for ( var attr in record) {
				var dom = $("#permission_edit input[name='" + attr + "']");
				if (dom.length == 1) {
					record[attr] = dom.val();
				}
			}
			$.ajax({
				url : '${ctx}/permission/update.htm',
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
				},
				error:function(){
					BootstrapDialog.alert("操作失败");
				}
			});

		}
		function permission_save() {
			if(!permission_validate('#save_form')){
				return;
			}
			$("#permission_save").modal('hide');
			var record = {};
				var dom = $("#permission_save input")
				dom.each(function(index,input){
					record[$(input).attr('name')] = $(input).val();	
				});
			$.ajax({
				url : '${ctx}/permission/save.htm',
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
				},
				error:function(){
					BootstrapDialog.alert("操作失败");
				}
			});

		}
		function permission_validate(id){
			var result = $(id).validate({
				rules:{
				permissionName:{
				 	required: true
				},
				permissionCode:{
				 	required: true
				},
				permissionUrl:{
				 	required: true
				}}
			}).form();
			return result;
		}
		$(function() {
			$("*[data-id]").click(function(e) {
				var id = $(this).attr("data-id");
				getRecordById(id, e);
			});
		});
	</script>