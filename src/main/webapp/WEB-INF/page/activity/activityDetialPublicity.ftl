<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<style>
	.close {
	    background-image : none !important
	}
</style>
<script src="${cdnPath}/js/activity/addActivity.js"></script>
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
			<li>查看活动资讯</li>
		</ul>
	</div>
	<div class="portlet-body form">
		<form>
		<div class="row-fluid">
			<div class="control-group span4">
				<label class="help-inline text-left span4" style="width:120px;">标题：</label>
				<input type="text" class="m-wrap span8" name="title" value="${item.title}" disabled>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4">
				<label class="help-inline text-left span4" style="width:120px;">活动/资讯类型：</label>
				<select class="m-wrap span8" id="activityType" name="activityType">
					<option value="1">宣传类</option>
				</select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4">
				<label class="help-inline text-left span4" style="width:120px;">发布位置：</label>
				<select class="m-wrap span8" id="location" name="location">
					<option value="1">首页banner</option>
				</select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4">
				<label class="help-inline text-left span4" style="width:120px;">宣传图片：</label>
				<div id="preview-1478067332179-0" class="file-preview-frame" data-fileindex="0">
					<img class="file-preview-image" src="${serverPath}/productFile/downOneFile.htm?fileIdListString=${item.imageId}" title="${item.imageName}" alt="${item.imageName}" style="width:auto;height:160px;">
					<div class="file-thumbnail-footer">
						<div class="file-caption-name" style="width: 167px;" title="${item.imageName}">${item.imageName}</div>
					</div>
				</div>
			</div>
		</div>


		<div class="row-fluid">
			<div class="control-group span4">
				<label class="help-inline text-left span4" style="width:120px;">超链接：</label>
				<input type="text" class="m-wrap span8" value="${(item.detailUrl)!''}" disabled/>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4">
				<label class="help-inline text-left span4"></label>
				<button type="button" class="btn blue" onclick="cancel()">取消</button>
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
</html>