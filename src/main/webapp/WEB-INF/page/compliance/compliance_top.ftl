<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<style>
.portlet.box .portlet-body
{
padding:0px
}
hr{margin:0;margin-bottom:4px;  }
</style>
<script>
    function getQueryString(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return unescape(r[2]); return null;
        } 
        <!--查询是否有外访信息，如果没有，不展示菜单-->
        $(function(){
            var options = {
                url : serverPath + "/audit/checkVisit.htm",
                type : 'post',
                dataType : 'json',
                data : {
                    "applyLoanNo" : getQueryString("applyLoanNo")
                },
                success : function(data) {
                    var result = data.data;
                    if(result == 0){
                        $("li[name='tab']:eq(3)").hide();
                    }
                }
            };
            $.ajax(options);
        })
        
    function load_page(obj,url){
        $(obj).tab('show');
        $("#tab-content").load(url);
        $("#doCompliance").hide();
        $("#showCompliance").hide();
    }
    function load_apply_page(obj,url){
        $(obj).tab('show');
        $("#tab-content").load(url,function(){
            $("#tab-content").find(".portlet-body").append("<form id=\"tab_audit\" action=\"#\" method=\"POST\" class=\"form-horizontal\"></form>");
            $("#tab_audit").load('${ctx}/apply/progress/auditDetail.htm?applyLoanNo=${applyLoanNo!}');
        
        });
        $("#doCompliance").hide();
        $("#showCompliance").hide();
    }
    function load_contract_page(obj,url){
        $(obj).tab('show');
        $("#tab-content").load(url,function(){
            $("#tab-content").find(".portlet-body").append("<form id=\"tab_audit\" action=\"#\" method=\"POST\" class=\"form-horizontal\"></form>");
            $("#tab_audit").load('${ctx}/apply/progress/complianceDetail.htm?applyLoanNo=${applyLoanNo!}');
        
        });
        $("#doCompliance").hide();
        $("#showCompliance").hide();
    }
    function page_back(url) {
        window.location.href = url+"?"+ChkUtil.getCookie("queryParams");
    }
</script>
    
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
					<li><span>合规管理</span><i class="icon-angle-right"></i></li>
					<li>
                        <a href="#"><#if dto??>合规管理<#else>合规审核</#if></a>
                    </li>
				</ul>
			</div>
		</div>
            <div class="row-fluid">

                <div class="span12">

                    <div class="tabbable tabbable-custom boxless">

                        <ul class="nav nav-tabs">

                            <li name="tab" class=""><a href="javascript:goHref('${serverPath}/compliance/doCompliance.htm?applyLoanNo=')">审核结果</a></li>

                            <li name="tab" class=""><a  href="#" onclick="load_page(this,'${ctx}/apply/progress/receptionDetail.htm?applyLoanNo=${applyLoanNo!}')" >接待表</a></li>

                            <li name="tab" class=""><a  href="#" onclick="load_apply_page(this,'${ctx}/apply/applyDetail.htm?applyLoanNo=${applyLoanNo!}&isView=1')" >申请表</a></li>

                            <li name="tab" class=""><a  href="#" onclick="load_page(this,'${ctx}/apply/progress/visitDetail.htm?applyLoanNo=${applyLoanNo!}')">外访详情</a></li>
                            
                            <li name="tab" class=""><a  href="#" onclick="load_contract_page(this,'${ctx}/sign/contractDetail.htm?applyLoanNo=${applyLoanNo!}')">合同详情</a></li>

                        </ul>
                        <div class="tab-content" id="tab-content">
                        </div>