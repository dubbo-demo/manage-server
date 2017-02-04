<#include "/sys/top.ftl">
<#include "/sys/left.ftl">

<script type="text/javascript">
    var serverPath = "${serverPath}";
</script>
<style>
     .required{color:#e02222;}
</style>
<div class="page-content" >
<!-- header -->
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span12">
                <!--页面标题-->
                <h3 class="page-title"></h3>
                <!--面包屑导航-->
                <ul class="breadcrumb">
                    <li> <i class="icon-home"></i>
                        <a href="${serverPath}/index.htm">首页</a> <i class="icon-angle-right"></i>
                    </li>
                    <li>
                        <a href="#">系统管理</a><i class="icon-angle-right"></i>
                    </li>
                    <li>
                        <a href="#"><#if dto??>系统管理<#else>产品上传附件管理</#if></a>
                    </li>
                </ul>
            </div>
        </div>
    <div>
    <div id="selectShow">
	<div class="portlet-body form">
	<input type="hidden" id="product" name="product"></input>
	<input type="hidden" id="productFileUpState" name="productFileUpState"></input>
		<form id="searchForm" class="form-horizontal" method="post">
			<div class="row-fluid">
				<div class="control-group span4 ">
					<label class="help-inline text-left span2">产品名称：</label>
					<select class="m-wrap span5" name="selectProductName" id="selectProductName"></select>
				</div>
			</div>
			<div>
				<p>
    				<span>
    					<input type="button" class="btn blue"
                            style="margin-top: 5px;" value="查询" onclick="return search()"></input>
                        <input type="button" class="btn blue"
                            style="margin-top: 5px;" value="新增" onclick="return add()"></input>
                    </span>        
				</p>
			</div>
		</form>
	</div>
	
	<!--显示查询结果-->
	<div class="tabbable tabbable-custom tabbable-custom-profile">
		<!-- table -->
		<table class="table table-striped table-bordered table-hover" id="selectProductFile">
			<thead>
				<tr>
					<th>产品名称</th>
					<th>附件名称</th>
					<th>上传阶段</th>
					<th>是否必传</th>
					<th>操作</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	</div>
	<!--新增/修改-->
	<form id="changeProductFile" name="changePost" method="POST" class="form-horizontal"
        enctype="multipart/form-data" target="frm_dealer">
	<!-- 新增或修改，0新增，1修改 --> 
    <input type="hidden" id="insertOrUpdate" name="insertOrUpdate"></input>
	<div id="addProductFile" style="display: none;">
    	<div class="row-fluid" >
            <div class="control-group span6 ">
                <label class="control-label">产品名称<span class="required">*</span></label>
                <div class="controls">
                    <select class="span6" name="productName" id="productName"></select>
                </div>
            </div>
        </div>
        <div class="row-fluid" >
            <div class="control-group span6 ">
                <label class="control-label">附件名称<span class="required">*</span></label>
                <div class="controls">
                    <input type="text" name="directoryName" id="directoryName"
                    class="m-wrap text-left span6" onblur="checkInput('^[\a-\z\A-\Z0-9\u4E00-\u9FA5]*$',this)" maxlength="50" />
                </div>
            </div>
        </div>
        <div class="row-fluid" >
            <div class="control-group span6 ">
                <label class="control-label">上传阶段<span class="required">*</span></label>
                <div class="controls">
                    <select class="span6" name="fileUpState" id="fileUpState"></select>
                </div>
            </div>
        </div>
        <div class="row-fluid" >
            <div class="control-group span6 ">
                <label class="control-label">是否必填<span class="required">*</span></label>
                <div class="controls">
                    <select class="span6" name="isMust" id="isMust">
                        <option value ="2">请选择</option>
                        <option value ="0">否</option>
                        <option value ="1">是</option>
                    </select>
                </div>
            </div>
        </div>

        <div><span id="error" style="display: block;"></span></div>
        <div class="row-fluid">
            <div class="control-group">
                <input type="button" class="btn blue"
                    style="margin-left: 365px;" value="返回" onclick="return backProductFile()"></input>
                <input type="button" class="btn blue" value="提交" onclick="return save()"></input>
            </div>
        </div>
	</div>
	</form>

	<input type="hidden" id="id" name="id"></input>

</div>
</div>
<script src="${cdnPath}/js/productFile/productFile.js?v=${VERSION_NO}"></script>
<#include "/sys/bottom.ftl">