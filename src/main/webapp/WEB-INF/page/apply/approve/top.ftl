 <#include "/sys/top.ftl"> <#include "/sys/left.ftl">
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
                        $("li[name='tab']:eq(2)").hide();
                    } else {
                    	$("li[name='tab']:eq(2)").show();
                    }
                }
            };
            $.ajax(options);
        })
</script>

<div class="page-content">
	<div class="container-fluid">
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
						<li><a href="#">信审业务管理</a> <i class="icon-angle-right"></i></li>
						<li><a href="#">信审审批</a></li>
					</ul>
				</div>
			</div>
			<div class="row-fluid">

				<div class="span12">

					<div class="tabbable tabbable-custom boxless">

						<ul class="nav nav-tabs">

							<li name="tab" class=""><a href="javascript:goHref('${serverPath}/apply/applyDetail.htm?applyLoanNo=')">申请人基本信息</a></li>

							<li name="tab" class=""><a href="javascript:goHref('${serverPath}/audit/base/baseInfoAdd.htm?applyLoanNo=')">初审报告</a></li>

							<li name="tab" class="" style="display:none;"><a href="javascript:goHref('${serverPath}/audit/auditVisit.htm?applyLoanNo=')">外访信息</a></li>

							<li name="tab" class=""><a href="javascript:goHref('${serverPath}/approve/toApprove.htm?applyLoanNo=')">审批处理</a></li>

						</ul>
				<#if apply?? && apply.subState == 1141>
				 <div style="height:1px; width:1px;position: relative; ">
					<img style="left:46px;top:57px;position: absolute;width:119px;max-width:119px;" src="${cdnPath}/images/return.png">
				</div>
				</#if>
