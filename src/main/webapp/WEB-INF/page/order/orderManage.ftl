 <#include "/sys/top.ftl"> <#include "/sys/left.ftl">
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
					<li><a href="#">财务管理</a> <i class="icon-angle-right"></i></li>
					<li><a href="#">还款清单</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
			<form id="searchForm" class="form-horizontal" method="post">
				<@p.pageForm value=page type="sort"/>
				<div class="row-fluid">
					<div class="control-group span4 ">
						<label class="help-inline text-right span4">合同号：</label> <input
							type="text" class="m-wrap span8" name="contractNo"
							value="${(params.contractNo)!""}">
					</div>
					<div class="control-group span4 ">
						<label class="help-inline text-right span4">身份证：</label> <input
							type="text" class="m-wrap span8" name="idCard"
							value="${(params.idCard)!""}">
					</div>
                    <div class="control-group span4 ">
                        <label class="help-inline text-right span4">状态：</label>
                        <div class="">
                            <select class="m-wrap span8" name="state">
                                <option value="-1">请选择</option>
                                <option value="10">未还款</option>
                                <option value="20">部分还款</option>
                                <option value="30">结清</option>
                            </select>   
                        </div>                                                                  
                    </div>					
					<div class="row-fluid">
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">大区：</label>
                            <div class="">
                            <select name="areaId" class="m-wrap span8" data-id="${(params.areaId)!0}" onchange="initStoreData()">
                                <option value="0">请选择</option>
                            </select>
                            </div>
                        </div>
                        <div class="control-group span4 " class="m-wrap span8">
                            <label class="help-inline text-right span4">门店：</label> 
                            <div class="">
                            <select name="storeId" class="m-wrap span8" data-id="${(params.storeId)!0}">
                                <option value="0">请选择</option>
                            </select>
                            </div>
                        </div>
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">应还日期：</label> 
                            <div class="">
                                    <div class="input-append date date-picker" data-date="${(params.agreeRepayDates?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
                                        <input name="agreeRepayDates" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(params.agreeRepayDates?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
                                    </div>      
                                    <span style="margin-left:-28px">--</span>
                                    <div class="input-append date date-picker" data-date="${(params.agreeRepayDatee?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
                                        <input name="agreeRepayDatee" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(params.agreeRepayDatee?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
                                    </div>  
                            </div>      
                        </div>
					</div>

				</div>
				<p>
					<a href="javascript:search()" class="btn blue">查询</a>
                    <@shiro.hasPermission name="order:down">
                    <a href="javascript:down()" class="btn blue">下载</a>
                    </@shiro.hasPermission>					
				</p>
			</form>
			</div>
			<!-- table -->
			<div class="tabbable tabbable-custom tabbable-custom-profile">
				<table class="table table-bordered table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th>合同编号</th>
							<th>期数</th>
							<th>账单编号</th>
							<th>期初本金</th>
							<th>月还本金</th>
							<th>月还利息</th>
							<th>月还款额</th>
							<th>期末本金余额</th>
							<th>提前结清减免</th>
							<th>提前结清金额</th>
							<th>已还金额</th>
							<th>罚息</th>
							<th>违约金</th>
							<th>剩余应还</th>
							<th>应还日期</th>
							<th>逾期天数</th>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#list page.result as record>
						<tr>
							<td>${record_index+1}</td>
							<td>${record.contractNo!}</td>
							<td>${record.strPeriod!}</td>
							<td>${record.billNo!}</td>
							<td>${record.initialPrincipal!}</td>
                            <td>${record.principal!}</td>
                            <td>${record.interest!}</td>
                            <td>${record.reapyAmount!}</td>
                            <td>${record.endPrincipal!}</td>
                            <td>${record.returnAmount!}</td>
                            <td>${record.aheadAmount!}</td>
                            <td>${record.alsoRepay!}</td>
                            <td>${record.lastPenalty!}</td>
                            <td>${record.lastLateFee!}</td>
                            <td>${record.surplusRepay!}</td>
                            <td><#if record.agreeRepayDate??>${(record.agreeRepayDate?string("yyyy-MM-dd"))!}</#if></td>
                            <td>${record.overdueDay!}</td>
                            <td>${record.stateDesc!}</td>
                            <td class='a' data-ifshow='${record.ifShow!}' data-state='${record.state!}'>
                                <@shiro.hasPermission name="order:withhold">
                                <a data-target="#withholdShow" data-toggle="modal" href="" class="withholdShow ifshow state" data-paytype="4" data-paytype2="2" data-billno='${record.billNo!}'>代扣</a>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="order:compensate">
                                <a data-target="#compensateShow" data-toggle="modal" href="" class="compensateShow ifshow state" data-paytype="5" data-paytype2="3" data-billno='${record.billNo!}'>代偿</a>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="order:reduction">
                                <a data-target="#reductionShow" data-toggle="modal" href="" class="reductionShow ifshow state" data-paytype="1" data-billno='${record.billNo!}'>减免</a>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="order:earlySettlement">
                                <a data-target="#earlySettlementShow" data-toggle="modal" href="" class="earlySettlementShow ifshow state" data-paytype="14" data-paytype2="2" data-billno='${record.billNo!}'>提前结清</a>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="order:toPublic">
                                <a data-target="#toPublicShow" data-toggle="modal" href="" class="toPublicShow ifshow state" data-paytype="1" data-billno='${record.billNo!}'>对公</a>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="order:detail">
                                <a href="${serverPath}/order/hkBillUpdateLog.htm?billNo=${record.billNo!}">详情</a>
                                </@shiro.hasPermission>
                            </td>
						<tr></#list>
					</tbody>
				</table>
				<@p.pagination value=page /> 
			</div>
		</div>
	</div>
</div>
 <#include "/order/order_model.ftl">
<#include "/sys/bottom.ftl">
<script>
    $(function(){
        init();
    });

    function search() {
        ChkUtil.form_trim($("#searchForm"));
        $("#searchForm").attr("action", "${ctx}/order/list.htm");
        ChkUtil.saveCookie("orderManage", $("#searchForm").serialize(),"","/");
        $("#searchForm").submit();
    }

    
    function down() {
        ChkUtil.form_trim($("#searchForm"));
        $("#searchForm").attr("action", "${ctx}/order/export.htm");
        $("#searchForm").submit();
    }
     

    function init(){
        $("select[name='state']").val(${(params.state)!-1});
        $("#searchBtn").attr("disabled","disabled");
        //加载大区数据
        initAreaData();
        $(".a .ifshow").each(function () {
            var ifShow = $(this).parent().data('ifshow');
            if(ifShow == 0){
                $(this).hide();
            }                
        });
        $(".a .state").each(function () {  
            var state = $(this).parent().data('state');
            if(state == 30){
                $(this).hide();
            }              
        });        
    }
function initAreaData() {
    $.ajax({
        url : serverPath + "/organization/getRegionInfo.htm",
        type : "post",
        data : {
            "Time" : new Date().getMilliseconds()
        },
        dataType : "json",
        success : function(result) {
            if (result.code == 0) {
                initStoreData();
                // 清空除第一条内容的外的其它数据
                var select_ = $("select[name='areaId']");
                select_.find("option:gt(0)").remove();
                for (var i = 0; i < result.data.length; i++) {
                    var isSelected = result.data[i].id == select_
                            .attr('data-id') ? "selected='selected'" : "";
                    select_.append("<option " + isSelected + " value='"
                            + result.data[i].id + "'>" + result.data[i].orgName
                            + "</option>");
                }
            } else {
                BootstrapDialog.alert(result.message);
            }
        },
        error : function() {
            BootstrapDialog.alert("操作失败");
        }
    });
}
function initStoreData() {
    var parentId = $("select[name='areaId']").val();
    if (parentId == "0") {
        parentId = $("select[name='areaId']").attr('data-id');
    }
    if (parentId == "0") {
        return;
    }
    $.ajax({
        url : serverPath + "/organization/getStoreInfo.htm",
        type : "post",
        data : {
            "id" : parentId,
            "Time" : new Date().getMilliseconds()
        },
        dataType : "json",
        success : function(result) {
            if (result.code == 0) {
                // 清空除第一条内容的外的其它数据
                var select_ = $("select[name='storeId']");
                select_.find("option:gt(0)").remove();
                for (var i = 0; i < result.data.length; i++) {
                    var isSelected = result.data[i].id == select_
                            .attr('data-id') ? "selected='selected'" : "";
                    select_.append("<option " + isSelected + " value='"
                            + result.data[i].id + "'>" + result.data[i].orgName
                            + "</option>");
                }
            } else {
                BootstrapDialog.alert(result.message);
            }
        },
        error : function() {
            BootstrapDialog.alert("操作失败");
        }
    });
}
</script>
