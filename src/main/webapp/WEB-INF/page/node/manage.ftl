 <#include "/sys/top.ftl"> <#include "/sys/left.ftl">
<script src="${cdnPath}/js/node/manage.js"></script>
<script src="${cdnPath}/common/js/rightMenu.js"></script>
<script>
	
</script>

<div class="page-content">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<!--页面标题-->
				<h3 class="page-title"></h3>
				<!--面包屑导航-->
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a
						href="${serverPath}/index.htm">首页</a> <i class="icon-angle-right"></i>
					</li>
					<li><a href="#">系统管理</a> <i class="icon-angle-right"></i></li>
					<li><a href="#">基础数据维护</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div style="color: red; margin-left: 20px">提醒：在红色区域内右击增加顶级节点</div>
			<div class="container-fluid">
				<div class="row-fluid" data-toggle="context" id="main"
					style="border: 1px solid red; overflow-y: auto; height: 700px;">
					<div class="span8">
						<ul id="treePullDown" class="ztree"></ul>
					</div>
				</div>
			</div>
			<div id="context-menu2">
				<ul class="dropdown-menu" role="menu">
					<li><a tabindex="-1" name="newNode">新增</a></li>
					<li class="divider"></li>
					<li><a tabindex="-1" name="deleteNode">删除</a></li>
				</ul>
			</div>
			<div id="new_info" class="modal hide fade" tabindex="-1"
				data-width="760">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true"></button>
					<h3>新增</h3>
				</div>
				<div class="modal-body">
					<div class="row-fluid">
						<form id="submit_form">
							<div class="span6">
								<p>
									<label class="control-label">字段名称<span class="required">*</span></label>
									<input type="text" class="span12 m-wrap" name="name" value="">
								</p>
								<p>
									<label class="control-label">下拉编号<span class="required">*</span></label>
									<input type="text" class="span12 m-wrap" name="code" value="">
								</p>
								<p></P>
							</div>
						</form>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" data-dismiss="modal" class="btn">返回</button>
					<button type="button" class="btn blue" onclick="saveNode();">提交</button>
				</div>
			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
