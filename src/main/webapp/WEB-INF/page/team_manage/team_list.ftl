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
									<a href="${serverPath}/index.htm">首页</a> <i class="icon-angle-right"></i> </li>
								<li><a href="#">员工绩效管理</a><i class="icon-angle-right"></i></li>
								<li>
									<a href="#">团队管理</a>
								</li>
							</ul>
						</div>
					</div>
				
				<div class="portlet-body form">
							<form action="${serverPath}/team/list.htm" id="searchForm" class="form-horizontal" method="post">
								<@p.pageForm value=page />
								<div class="row-fluid">
									<div class="control-group span4 ">
										<label class="help-inline text-right span4">组织：</label>
										<select class="m-wrap span8" id="storeId" name="storeId">
											<option value ="0">请选择</option>
										</select>
									</div>
									<div class="control-group span4 ">
										<label class="help-inline text-right span4">团队：</label>
										<input type="text" class="m-wrap span8" name="teamName" value="${(queryDto.teamName)!''}">
									</div>
								</div>
								<div class="row-fluid">
									<div class="control-group span4 ">
										<@shiro.hasPermission name="employee:team:search">
											<button type="submit" class="btn blue">查询</button>
										</@shiro.hasPermission>
										<@shiro.hasPermission name="employee:team:add">
											<a class="btn blue" href="javascript:page_jump('${serverPath}/team/addTeamForm.htm')">新增门店团队</a>
										</@shiro.hasPermission>
										<@shiro.hasPermission name="employee:team:addGroup">
											<a class="btn blue" href="javascript:page_jump('${serverPath}/team/addGroupForm.htm')">新增信审团队</a>
										</@shiro.hasPermission>
									</div>
								</div>
							</form>
				</div>	

	<div class="tabbable tabbable-custom tabbable-custom-profile">
		<!-- table -->
		<table class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th>组织</th>
					<th>团队</th>
					<th>团队负责人</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
					
					<#list page.result as item>
						<tr class="odd gradeX" id="${item.id}">
							<td>${(item.storeName)!""}</td>
							<td>${(item.teamName)!""}</td>
							<td>${(item.leaderName)!""}</td>
							<td>
								<shiro:hasPermission name="menu:editUI">
								<!--<a href="${ctx}/innerBlack/detailUI.html?id=${item.id}">查看详情</a>-->
								<@shiro.hasPermission name="employee:team:update">
									<a href="javascript:page_jump('${serverPath}/team/updateTeamForm.htm?id=${item.id}')">修改</a>
								</@shiro.hasPermission>
								<@shiro.hasPermission name="employee:team:delete">
									<a href="javascript:void(0)" onclick="deleteTeam(${item.id},event)">禁用</a>
								</@shiro.hasPermission>
							</td>
						</tr>
					</#list>
			</tbody>
		</table>
		<@p.pagination value=page />
	</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script src="${cdnPath}/common/js/common.js"></script>
<script>
function page_jump(url){
		var urldata = $("#searchForm").serialize();
		if(urldata.indexOf("storeId") == -1) {
			urldata = urldata +"&storeId="+$("#storeId").val();
		}
		ChkUtil.saveCookie("queryParams", urldata,"","/");
		window.location.href=url;
}
	//记载门店
	function loadStores() {
		$("#storeId").empty();
		$("#storeId").append("<option value=0>请选择</option>");
		var url = "${serverPath}/organization/selectOrgByOrgType.htm";
		var data = {
			"Time" : new Date().getMilliseconds(),
			"orgType" : 3
		};
		$.getJSON(url, data, function(result) {
			var data = result.data;
			for (var i = 0; i < data.length; i++) {
					var op = "<option value=" + data[i].id + ">" + data[i].orgName
						+ "</option>";
				$("#storeId").append(op);
			}
			// 征信部
			data2 = {
					"parentCode" : "TEAMPOSITION",
					"Time" : new Date().getMilliseconds()
			};
			url2 = "${serverPath}/node/selectNodeList.htm";
			$.getJSON(url2, data2, function(result2) {
				var resultData = result2.data;
				for (var i = 0; i < resultData.length; i++) {
						$("#storeId").append("<option value='"+resultData[i].nodeCode+"'>" + resultData[i].nodeName + "</option>");
				}
				<#if queryDto.storeId??>
					$("#storeId").val("${queryDto.storeId!0}");
				</#if>
			});
		});
	}
	
	$(function(){ 
	　　    loadStores();
		$("#storeId").val("${queryDto.storeId!0}");
		if("${(isManage)!}" != "true") {
			if("${queryDto.storeId!0}" > 0) {
				$("#storeId").attr("disabled","disabled");
			} else {
				$("#storeId").removeAttr("disabled");  
			}
		}
	});
	
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
	
	function deleteTeam(id,event) {
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
		BootstrapDialog.confirm({
            message: '确定禁用该团队吗？',
            type: BootstrapDialog.TYPE_WARNING,
            callback: function(result) {
                if(result) {
                     $.ajax({
						url : '${serverPath}/team/deleteTeam.htm',
						type : 'post',
						data : {
							id : id
						},
						dataType : 'json',
						success : function(res) {
							if (res.code == '0') {
								$("#" + id).remove();
							}else{
								alert(res.message);
							}
						}
					})
                }else {
                    //alert('Nope.');
                }
            }
        });
	}
	
	function closeWin(){
	}
</script>