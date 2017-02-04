<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<style>
	.close {
	    background-image : none !important
	}
	.kv-fileinput-caption{display:none !important}
</style>
<script src="${cdnPath}/js/activity/addActivity.js?20161202"></script>
<script type="text/javascript">
    var serverPath = "${serverPath}";
</script>

<div class="page-content">
<div class="container-fluid">
	<!-- header -->
	<div class="row-fluid">
			<!--页面标题-->
				<h3 class="page-title"></h3>
		<ul class="breadcrumb">
			<li><i class="icon-home"></i>
			<li><a href="${serverPath}/index.htm">首页</a> <i class="icon-angle-right"></i> </li>
			</li>
			<li>
				<li>运营管理</li>
				<i class="icon-angle-right"></i>
			</li>
			<li>
				<a href="${serverPath}/activity/activityQuery.htm">资讯活动列表</a>
				<i class="icon-angle-right"></i>
			</li>
			<li>新增活动资讯</li>
		</ul>
	</div>
	<div class="portlet-body form">
		<form id="tab" action="${serverPath}/activity/saveActivity.htm" method="POST" enctype="multipart/form-data">
		<div class="row-fluid">
			<div class="control-group span4">
				<label class="help-inline text-right span4" style="width:120px;">标题：<span class="required" style="color:#e02222;">*</span>&nbsp;</label>
				<input type="text" class="m-wrap span8" id="title" name="title">
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4">
				<label class="help-inline text-right span4" style="width:120px;">活动/资讯类型：<span class="required" style="color:#e02222;">*</span>&nbsp;</label>
				<select class="m-wrap span8" id="activityType" name="activityType">
					<option value="1">宣传类</option>
				</select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4">
				<label class="help-inline text-right span4" style="width:120px;">发布位置：<span class="required" style="color:#e02222;">*</span>&nbsp;</label>
				<select class="m-wrap span8" id="publishLocation" name="publishLocation">
					<option value="1">首页banner</option>
				</select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4">
				<label class="help-inline text-right span4" style="width:120px;">宣传图片：<span class="required" style="color:#e02222;">*</span>&nbsp;</label>
				<img width="100" height="100" src="" id="viewActivityPhoto" name="viewActivityPhoto" style="display: none"></img>
				<input type="file" name="file" id="activityPhoto" class="file" />
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="control-group span4">
				<label class="help-inline text-right span4" style="width:120px;">
				<input id="detailStyle" name="detailStyle" type="radio" value="0"
						onclick="disabledActivityDetail(this)" /><span id="detailStylespan">超链接：</span></label>
				<input type="text" class="m-wrap span8" id="detailUrl" name="detailUrl" disabled/>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4">
				<label class="help-inline text-left span4" style="width:120px;"></label>
				<button type="button" class="btn blue" onclick="cancel()" id="cancelbutton">取消</button>
				<button type="button" class="btn blue" onclick="save()" id="savebutton">提交</button>
				<span id="error" ></span>
			</div>
		</div>
		</form>
		<form id="canceltab" action="${serverPath}/activity/activityQuery.htm?createDate=${(queryDto.createDate?string("yyyy-MM-dd"))!''}&pageIndex=${(pageIndex)!1}&pageSize=${(pageSize)!10}" method="POST">
		</form>
	</div>
</div>
</div>
</div>
<#include "/sys/bottom.ftl">
</body>
<script>
	$("#activityPhoto").fileinput({
		'allowedFileExtensions' : [ 'jpg', 'png', 'gif' ],
	});
</script>
</html>