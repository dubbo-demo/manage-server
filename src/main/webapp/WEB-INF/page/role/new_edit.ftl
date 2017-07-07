<script type="text/javascript">
    $(function() {
        $("select[name='roleType']").val(${(record.roleType)!});
    });
</script>
		<div class="modal-header">

			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true"></button>
			<#if record??>
				<h3>修改</h3>
			<#else>
				<h3>新增</h3>
			</#if>

		</div>
		<form id="form_commit" action="#">
		<#if record??>
			<input type="hidden" value="${record.id}" name="id">
		</#if>
		<div class="modal-body">
			
			<div class="row-fluid">

				<div class="span6">
					<p>
						<label class="control-label">角色名称<span class="required">*</span></label>
						<#if record??>
							<input type="text" class="span12 m-wrap"
								name="roleName" value="${record.roleName}" data-value="${record.roleName}">
						<#else>
							<input type="text" class="span12 m-wrap"
										name="roleName">
						</#if>
					</p>

					<p>
						<label class="control-label">角色编号<span class="required">*</span></label>
						<#if record??>
							<input type="text" class="span12 m-wrap"
								name="roleCode" readonly value="${record.roleCode}" data-value="${record.roleCode}">
						<#else>
							<input type="text" class="span12 m-wrap"
										name="roleCode">
						</#if>
					</p>
					<p>
                        <label class="control-label">角色类型<span class="required">*</span></label>
                            <select class="m-wrap span12" id="roleType" name="roleType" <#if record??> disabled="true"</#if>>
                                <option value ="0">菜单权限角色</option>
                                <option value ="1">数据权限角色</option>
                            </select>					
					</P>
					<p>
					</p>
				</div>


			</div>

		</div>
		</form>

		<div class="modal-footer">
			<button type="button" data-dismiss="modal" class="btn">返回</button>
			<#if record??>
			<button type="button" class="btn blue" onclick="role_edit();">提交</button>
			<#else>
			<button type="button" class="btn blue"  onclick="role_save();">提交</button>
			</#if>
		</div>