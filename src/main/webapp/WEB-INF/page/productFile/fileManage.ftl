<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/productFile/fileManage.js?v=${VERSION_NO}"></script>
<script type="text/javascript">
    var serverPath = "${serverPath}";
</script>

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
                        <a href="#">附件管理</a><i class="icon-angle-right"></i>
                    </li>
                    <li>
                        <a href="#"><#if dto??>附件管理<#else>附件管理-管理员</#if></a>
                    </li>
                </ul>
            </div>
        </div>
    <div>
    
    <div id="selectShow">
    	<div class="portlet-body form">
    		<div class="row-fluid">
    			<div class="control-group span4 ">
    				<label class="help-inline text-left">申请单编号：</label>
    				<input type="text" name="applyLoanNo" id="applyLoanNo"
                        class="m-wrap text-left span5" onblur="checkInput('^[\a-\z\A-\Z0-9]*$',this)" maxlength="50" />
    			    <input type="button" class="btn blue" value="查询" onclick="return search()"></input>
    			</div>
    		</div>
    	</div>
	
    	<!--显示查询结果-->
    	<div class="tabbable tabbable-custom tabbable-custom-profile">
    		<!-- table -->
    		<table class="table table-striped table-bordered table-hover" id="selectProductFile">
    			<thead>
    				<tr>
    					<th>申请单号</th>
    					<th>合同号</th>
    					<th>客户</th>
    					<th>手机号</th>
    					<th>业务经理</th>
    					<th>大区</th>
    					<th>门店</th>
    					<th>进件日期</th>
    					<th>客服</th>
    					<th>状态</th>
    					<th>操作</th>
    				</tr>
    			</thead>
    			<tbody>
    			</tbody>
    		</table>
    	</div>
	</div>
</div>
<#include "/sys/bottom.ftl">