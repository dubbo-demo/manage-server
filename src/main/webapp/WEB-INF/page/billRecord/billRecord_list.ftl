<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/reception/list.js?v=${VERSION_NO}"></script>
<script>
	var orgType="${orgType!}"; //组织类型1:大区 2：总部 3：门店
	var regionId="${regionId!}";//大区id
	function page_jump(url){
		ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
		window.location.href=url;
	}
    function search() {
        form_trim();
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
<div class="page-content">
				<div class="container-fluid">
					<div class="row-fluid">
						<div class="span12">
							<!--页面标题-->
							<h3 class="page-title"></h3>
							<!--面包屑导航-->
							<ul class="breadcrumb">
								<li> <i class="icon-home"></i>
									<a href="${serverPath}/index.htm">首页</a> <i class="icon-angle-right"></i> </li>
								<li>
									<a href="javascript:void(0)">分公司业务管理</a><i class="icon-angle-right"></i>
								</li>
								<li>
									<a href="#">接待管理</a>
								</li>
							</ul>
						</div>
					</div>
				<div>
				<div class="portlet-body form">
							<form action="${serverPath}/reception/list.htm" id="searchForm" class="form-horizontal" method="post">
							<@p.pageForm value=page  type="sort"/>
								<input type="hidden" id="pageIndex" name="pageIndex" value='1'/>
								<div class="row-fluid">
									<div class="control-group span3 ">
										<label class="help-inline text-right span4">申请件单号：</label>
										<input type="text" class="m-wrap span5" name="applyLoanNo" value="${(queryDto.applyLoanNo)!''}">
									</div>
									<div class="control-group span3 ">
										<label class="help-inline text-right span4">客户：</label>
										<input type="text" class="m-wrap span5" name="memberName" value="${(queryDto.memberName)!''}">
									</div>
									<div class="control-group span3 ">
										<label class="help-inline text-right span4">业务经理：</label>
										<input type="text" class="m-wrap span5" name="bmName" value="${(queryDto.bmName)!''}">
									</div>
									<div class="control-group span3 ">
										<label class="help-inline text-right span4">门店：</label>
										<input type="hidden" id="empStoreId" value="${empStoreId!}">
										<input type="hidden" id="storeId2" value="${queryDto.storeId!}">
										<select class="m-wrap span5" id="storeId" name="storeId" value="">
										</select>
									</div>
								</div>
								<div class="row-fluid">
									<div class="control-group span4 ">
										<button type="button" onclick="search()" class="btn blue">查询</button>
										<a class="btn blue" href="javascript:page_jump('${serverPath}/reception/addForm.htm')">下载</a>
									</div>
								</div>
							</form>
				</div>	

	<div class="tabbable tabbable-custom tabbable-custom-profile">
		<!-- table -->
		<table class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th>序号</th>
                    <th>合同编号</th>
                    <th>期数</th>
                    <th>账单编号</th>
					<th>扣款类型</th>
					<th>扣款金额</th>
					<th>账户名</th>
					<th>开户行</th>
                    <th>卡号</th>
                    <th>手机号</th>
					<th>身份证号</th>
                    <th>发起人</th>
                    <th>提前结清</th>
                    <th>扣款日期</th>
                    <th>状态</th>
                    <th>备注</th>
				</tr>
			</thead>
			<tbody>
			<#if page?? && page.result?? >
					<#list page.result as item>
						<tr class="odd gradeX" id="${item.id!}">
							<td>${item_index+1 }</td>
							<td>${item.contractNo! }</td>
							<td>${item.repayPeriod! }/${item.periods! }</td>
							<td>${item.billNo! }</td>
                            <td>${item.payTypeName! }</td>
                            <td>${item.billNo! }</td>
                            <td>${item.payTypeName! }</td>
                            <td>${item.payAmount! }</td>
							<td>${(item.username)!""}</td>
							<td>${(item.backOpen)!""}</td>
                            <td>${(item.idBackNo)!""}</td>
                            <td>${(item.reservedPhone)!""}</td>
                            <td>${(item.idCardNo)!""}</td>
                            <td>${(item.createUser)!""}</td>
							<#if item.isAdvanceSettle?? && item.isAdvanceSettle == 1>
                                <td>否</td>
							<#else>
                                <td>是</td>
							</#if>
							<td>${item.createTime?datetime}</td>
                            <td>${(item.stateName)!""}</td>
                            <td>备注</td>
						</tr>
					</#list>
			
					<tr>
					<td colspan="14" align="center">
						<@p.pagination value=page />
		            </td>
					</tr>
			</#if>
			</tbody>
		</table>
	</div>
</div>
<div id="refuse" class="modal hide fade" tabindex="-1" data-width="760">
			<input type="hidden" id="rid" value=""/>
			<input type="hidden" id="rstate" value=""/>
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
					<h4 class="refuseDivTitle"></h3>
					<style>
				  	.refuseDivTitle {text-align:center;font-weight:bold}
				  	</style>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="control-group span10">
						<label class="help-inline text-right span3">说明:</label>
						<div class="controls">
						<textarea id="description" rows="4" class="m-wrap span9"></textarea>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn blue" onclick="goDelte(event)">确定</button>
				<button type="button" data-dismiss="modal" class="btn">取消</button>
			</div>
</div>
<#include "/sys/bottom.ftl">