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
					<li><a href="#">运营管理</a><i class="icon-angle-right"></i></li>
					<li><a href="#">账单导入</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
				<form action="#" id="searchForm" class="form-horizontal"
					method="post">
					<div class="row-fluid">
						<#--<div class="control-group span4 ">-->
							<#--<label class="help-inline text-right span4">姓名：</label> <input-->
								<#--type="text" class="m-wrap span8" name="memberName"-->
								<#--value="">-->
						<#--</div>-->
						<#--<div class="control-group span4 ">-->
							<#--<label class="help-inline text-right span4">身份证：</label> <input-->
								<#--type="text" class="m-wrap span8" name="idCard"-->
								<#--value="">-->
						<#--</div>-->
					</div>
					<p>
						<#--<button type="button" class="btn blue" onclick="search()">查询</button>-->
						<shiro:hasPermission name="thirdBlack:import">
						<a href="#import" data-toggle="modal" class="btn blue">导入</a>
						</shiro:hasPermission>
						<#--<shiro:hasPermission name="thirdBlack:export">-->
						<#--<button type="button" class="btn blue" onclick="export_excel()">导出</button>-->
						<#--</shiro:hasPermission>-->
					</p>
				</form>
			</div>

			<div class="tabbable tabbable-custom tabbable-custom-profile">
				<!-- table -->
				<table class="table table-striped table-bordered table-hover">
					<#--<thead>-->
						<#--<tr>-->
							<#--<th>序号</th>-->
							<#--<th>姓名</th>-->
							<#--<th>身份证号</th>-->
							<#--<th>数据来源</th>-->
							<#--<th>来源名称</th>-->
							<#--<th>是否拒绝</th>-->
							<#--<th>拒绝原因</th>-->
							<#--<th style="width:350px">返回信息</th>-->
							<#--<th>创建日期</th>-->
							<#--<th>操作</th>-->
						<#--</tr>-->
					<#--</thead>-->
					<tbody>
					</tbody>
				</table>
			</div>
			<div id="import" class="modal hide fade" tabindex="-1"
				data-width="760">

				<div class="modal-header">

					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true"></button>

					<h3>导入第三方黑名单</h3>

				</div>
				<form id="frm" method="post" action="" enctype="multipart/form-data">
					<div class="modal-body">

						<div class="row-fluid">

							<div class="span6">

								<p>
									上传文件<input type="file" id="fileName" name="upFile" /><span>
										&nbsp;&nbsp;<a href="${ctx}/billImport/exportTemplate.htm" />模板下载</a>
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
</div>
<#include "/sys/bottom.ftl">
<script>
	function deleteById(id, event) {
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
		BootstrapDialog.confirm({
            message: '确定删除？',
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
							$.ajax({
								url : '${ctx}/thirdBlack/delete.htm',
								type : 'post',
								data : {
									id : id
								},
								dataType : 'json',
								success : function(res) {
									if (res.code == 0) {
										$("#" + id).remove();
									} else {
										BootstrapDialog.alert("系统异常，删除失败");
									}
								}
							})
						}
					})
                }else {
                    //alert('Nope.');
                }
            }
        });
	}

	function batch_import() {
		var url = "${ctx}/thirdBlack/toImprot.htm";
		window.open(url);
	}
	function search() {
	    form_trim();
		$("#searchForm").attr("action", "${ctx}/thirdBlack/list.htm");
		$("#searchForm").submit();
	}
	function export_excel() {
		form_trim();
		$("#searchForm").attr("action", "${ctx}/thirdBlack/export.htm");
		$("#searchForm").submit();
	}

	//导入
	function impInfo() {
		if (!checkForm()) {
			return;
		}
		var url = "${ctx}/billImport/impInfo.htm";
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
	    form_trim();
		$("#searchForm").attr("action", "${ctx}/thirdBlack/list.htm");
	    $("#pageIndex").val(pageNumber);
	    $("#searchForm").submit();
	}
	function form_trim(){
		var $input=$("#searchForm input[type='text']");
		$input.each(function(i,n){
			 var value = $(n).val(); 
			 $(n).val($.trim(value));
		})
	}
</script>