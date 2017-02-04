<#include "/sys/top.ftl"> <#include "/sys/left.ftl">
<style>
.portlet.box .portlet-body
{
padding:0px
}
hr{margin:0;margin-bottom:4px;  }
</style>

<div class="page-content">
	<input type="hidden" name="subState" value="${subState!}">
	<!-- header -->
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<!--页面标题-->
				<h3 class="page-title"></h3>
				<!--面包屑导航-->
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="${ctx}/index.htm">首页</a> <i
						class="icon-angle-right"></i></li>
					<li><a href="#" id="menu_level_1"></a><i class="icon-angle-right"></i></li>
					<li><a href="#">申请进度查看</a></li>
				</ul>
			</div>
		</div>
		<div class="tabbable tabbable-custom boxless">
			<ul class="nav nav-tabs">
								<li class="active">
									<a  href="#" onclick="load_page(this,'${ctx}/apply/progress/receptionDetail.htm?applyLoanNo=${applyLoanNo!}')" >接待表</a>
								</li>
								<li>
									<a  href="#" onclick="load_apply_page(this,'${ctx}/apply/applyDetail.htm?applyLoanNo=${applyLoanNo!}&isView=1')" >申请表</a>
								</li>
								<#if visitTab == true>
								<li>
									<a  href="#" onclick="load_page(this,'${ctx}/apply/progress/visitDetail.htm?applyLoanNo=${applyLoanNo!}')">外访详情</a>
								</li>
								</#if>
								<#if contractTab == true>
								<li>
									<a  href="#" onclick="load_contract_page(this,'${ctx}/sign/contractDetail.htm?applyLoanNo=${applyLoanNo!}')">合同详情</a>
								</li>
								</#if>
								<#if loanTab == true>
								<li>
									<a  href="#" onclick="load_page(this,'${ctx}/loan/loanedDetail.htm?applyLoanNo=${applyLoanNo!}')">放款情况</a>
								</li>	
								</#if>							
			</ul>
			<div class="tab-content" id="tab-content">

			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
	
	function load_page(obj,url){
		$(obj).tab('show');
		$("#tab-content").load(url);
		}
	function load_apply_page(obj,url){
		$(obj).tab('show');
		$("#tab-content").load(url,function(){
			$("#tab-content").find(".portlet-body").append("<form id=\"tab_audit\" action=\"#\" method=\"POST\" class=\"form-horizontal\"></form>");
			$("#tab_audit").load('${ctx}/apply/progress/auditDetail.htm?applyLoanNo=${applyLoanNo!}');
		
		});
	}
	function load_contract_page(obj,url){
		$(obj).tab('show');
		$("#tab-content").load(url,function(){
			$("#tab-content").find(".portlet-body").append("<form id=\"tab_audit\" action=\"#\" method=\"POST\" class=\"form-horizontal\"></form>");
			$("#tab_audit").load('${ctx}/apply/progress/complianceDetail.htm?applyLoanNo=${applyLoanNo!}');
		
		});
	}
	function page_back(url) {
		window.location.href = url+"?"+ChkUtil.getCookie("queryParams");
	}
	$(function(){
		//获取一级菜单名
		var menu_level_1=$(".page-sidebar-menu li.active span.title").html().trim();
		$("#menu_level_1").html(menu_level_1);
		
		$(".nav-tabs li:first>a").click();
	})
	
</script>