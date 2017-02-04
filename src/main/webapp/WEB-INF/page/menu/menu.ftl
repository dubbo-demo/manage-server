<#include "/sys/top.ftl"> <#include "/sys/left.ftl">
<div class="page-content">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<!--页面标题-->
				<h3 class="page-title"></h3>
				<!--面包屑导航-->
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="${ctx}/index.htm">首页</a> <i
						class="icon-angle-right"></i></li>
					<li><a href="#">系统管理</a><i class="icon-angle-right"></i></li>
					<li><a href="javascript:void(0)">菜单管理</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
				<p>
					<shiro:hasPermission name="menu:addUI">
						<a class="btn blue"
							href="${ctx}/menu/editUI.htm?parentMenuId=${parentMenuId!-1}">新增</a>
					</shiro:hasPermission>
					<#if level == 1> <a class="btn blue" href="${ctx}/menu/menu.htm">返回上一级</a>
					</#if>
				</p>
			</div>
			<div class="tabbable tabbable-custom tabbable-custom-profile">
				<!-- table -->
				<table class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>编号</th>
							<th>菜单名称</th>
							<th>菜单等级</th>
							<th>菜单顺序</th>
							<th>访问路径</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if menuList??&&menuList?size gt 0> <#list menuList as menu>
						<tr id="${menu.id}">
							<td><#if level == 1> <a
								href="${ctx}/permission/list.htm?menuId=${menu.id}&parentMenuId=${parentMenuId!-1}">${menu.menuCode}</a>
								<#else> <a href="${ctx}/menu/childrenMenu.htm?id=${menu.id}">${menu.menuCode }</a>
								</#if>
							</td>
							<td>${menu.menuName }</td>
							<td>${(menu.menuLevel==0)?string("一级","二级") }</td>
							<td>${menu.orderColumn }</td>
							<td>${menu.menuUrl }</td>
							<td><shiro:hasPermission name="menu:editUI">
									<a
										href="${ctx}/menu/editUI.htm?menuId=${menu.id}&parentMenuId=${parentMenuId!-1}">修改</a>
								</shiro:hasPermission> <shiro:hasPermission name="menu:delete">
									<a href="javascript:void(0)"
										onclick="deleteMenu('${menu.id}',event)">删除</a>
								</shiro:hasPermission></td>
						</tr>
						</#list> </#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
	function deleteMenu(id, event) {
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
        BootstrapDialog.confirm({
            message: '确定删除菜单及其子菜单？',
            type: BootstrapDialog.TYPE_WARNING,
            callback: function(result) {
                if(result) {
                     $.ajax({
						url : '${ctx}/menu/delete.htm',
						type : 'post',
						data : {
							id : id
						},
						dataType : 'json',
						success : function(res) {
							if (res.code == 1) {
								BootstrapDialog.alert(res.message);
							} else {
								$("#" + id).remove();
							}
						}
					})
                }else {
                    //alert('Nope.');
                }
            }
        });
	}
</script>