<#include "/sys/top.ftl"> 
<#include "/sys/left.ftl">
<div class="page-content">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				
				<!--页面标题-->
				<h3 class="page-title">
				</h3>
				<!--面包屑导航-->
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="${ctx}/index.htm">首页</a> <i
						class="icon-angle-right"></i></li>
					<li><a href="#">风险管理</a><i class="icon-angle-right"></i></li>
					<li><a href="#">内部黑名单</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
				<form id="searchForm" action="#" class="form-horizontal"
					method="post">
					<@p.pageForm value=page />
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">姓名：</label> <input
								type="text" class="m-wrap span8" name="memberName"
								value="${(queryDto.memberName)!""}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">手机号：</label> <input
								type="text" class="m-wrap span8" name="phone"
								value="${(queryDto.phone)!""}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">身份证：</label> <input
								type="text" class="m-wrap span8" name="idCard"
								value="${(queryDto.idCard)!""}">
						</div>
					</div>
					<p>
						<button type="button" class="btn blue" onclick="search()">查询</button>
						<shiro:hasPermission name="innerBlack:add">
						<a class="btn blue" href="${ctx}/innerBlack/addUI.htm">新增黑名单</a> 
						</shiro:hasPermission>
						<shiro:hasPermission name="innerBlack:import">
						<a	href="#import" data-toggle="modal" class="btn blue">导入</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="innerBlack:export">
						<button type="button" class="btn blue" onclick="export_excel()">导出</button>
						</shiro:hasPermission>
					</p>
				</form>
			</div>

			<div class="tabbable tabbable-custom tabbable-custom-profile">
				<!-- table -->
				<table class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th>姓名</th>
							<th>身份证号</th>
							<th>出生日期</th>
							<th>手机号</th>
							<th>户籍地址</th>
							<th>现住地址</th>
							<th>有无子女</th>
							<th>是否有逾期</th>
							<th>逾期天数</th>
							<th>创建日期</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#list page.result as item>
						<tr class="odd gradeX" id="${item.id}">
							<td>${item_index+1 }</td>
							<td>${item.memberName }</td>
							<td>${item.idCard }</td>
							<td>${(item.birthday?string('yyyy-MM-dd'))!}</td>
							<td>${item.phone!""}</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="${item.registdAddr!""}">${item.registdAddr!""}</span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="${item.currentAddr!""}">${item.currentAddr!""}</span></td>
							<td><#if item.hasChildren??>
								${(item.hasChildren==0)?string('无','有') } </#if></td>
							<td><#if item.hasOverdue??>
								${(item.hasOverdue==0)?string('无','有') } </#if></td>
							<td>${item.overdueDays!""}</td>
							<td><#if item.createTime??>${item.createTime?date}</#if></td>
							<td><a href="${ctx}/innerBlack/detailUI.htm?id=${item.id}">查看详情</a></td>
						</tr>
						</#list>
					</tbody>
				</table>
				<@p.pagination value=page />
			</div>
		</div>
		<div id="import" class="modal hide fade" tabindex="-1"
			data-width="760">

			<div class="modal-header">

				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>

				<h3>导入内部黑名单</h3>

			</div>
			<form id="frm" method="post" action="" enctype="multipart/form-data">
				<div class="modal-body">

					<div class="row-fluid">

						<div class="span6">

							<p>
								上传文件<input type="file" id="fileName" name="upFile" /><span>
									&nbsp;&nbsp;<a href="${ctx}/innerBlack/exportTemplate.htm" />模板下载</a>
								</span>
							</p>
						</div>


					</div>

				</div>
				</form>
				<div class="modal-footer">
					<button type="button" data-dismiss="modal" class="btn">关闭</button>
					<button type="button" class="btn blue" onclick="impInfo();">导入</button>
				</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>

	function batch_import() {
		var url = "${ctx}/innerBlack/toImprot.htm";
		window.open(url);
	}
	function search() {
		form_trim();
		saveQueryParams();
		$("#searchForm").attr("action", "${ctx}/innerBlack/list.htm");
		$("#searchForm").submit();
	}
	function export_excel() {
		form_trim();
		saveQueryParams();
		$("#searchForm").attr("action", "${ctx}/innerBlack/export.htm");
		$("#searchForm").submit();
	}
	
	function form_trim(){
		var $input=$("#searchForm input[type='text']");
		$input.each(function(i,n){
			 var value = $(n).val(); 
			 $(n).val($.trim(value));
		})
	}

	//导入
	function impInfo() {
		if (!checkForm()) {
			return;
		}
		var url = "${ctx}/innerBlack/impInfo.htm";
		$("#frm").attr("action", url);
		$("#frm").submit();
	}
	//验证信息
	function checkForm() {
		var fileName = $("#fileName").val();
		if (fileName == "") {
			BootstrapDialog.alert("上传文件不能为空!");
			return false;
		}
		if (fileName.indexOf(".xls") == "-1"
				&& fileName.indexOf(".xlsx") == "-1") {
			BootstrapDialog.alert("导入文件格式错误，请重新选择!");
			return false;
		}
		return true;
	}
	
	function goPage(pageNumber) {
		
		$("#searchForm").attr("action", "${ctx}/innerBlack/list.htm");
	    $("#pageIndex").val(pageNumber);
	    form_trim();
	    saveQueryParams();
	    $("#searchForm").submit();
	}
	
	function saveQueryParams(){
		ChkUtil.saveCookie("queryParams", $("#searchForm").serialize());
	}

</script>