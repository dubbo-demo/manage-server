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
								name="roleCode" value="${record.roleCode}" data-value="${record.roleCode}">
						<#else>
							<input type="text" class="span12 m-wrap"
										name="roleCode">
						</#if>
					</p>
					<p>
					</P>
					<p>
						<div class="btn-group">

							<a class="btn green" href="#" data-toggle="dropdown">

							岗位选择

							<i class="icon-angle-down"></i>

							</a>

							<div class="dropdown-menu bottom-up hold-on-click dropdown-checkboxes" style="height:150px;overflow-y:auto;overflow-x:hidden">
								<#if record??>
									<#list positions as map>
											<label><input type="checkbox" position-data-id = "${map['id']}" value="${map['id']}" name="positionIds"
											<#if map['isSelected'] == 0>
												checked = 'checked'
											</#if>
											>${map['positionName']}</label>
									</#list>
								<#else>
									<#list positions as position>
										<label><input type="checkbox" position-data-id = "${position.id}" value="${position.id}" name="positionIds">${position.positionName}</label>
									</#list>
								</#if>
							</div>

						</div>
						<div id="form_2_positionIds_error"></div>
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