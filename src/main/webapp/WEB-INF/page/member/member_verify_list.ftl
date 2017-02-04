<#include "/sys/top.ftl"> 
<#include "/sys/left.ftl">
<style  type="text/css">
.nowrapellipsis{
	overflow:hidden;
	text-overflow:ellipsis;
	white-space:nowrap;
	margin-left:0px
}
</style>
<div class="page-content">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				
				<!--页面标题-->
				<h3 class="page-title">
				</h3>
				<!--面包屑导航-->
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="${ctx}/index.htm">首页</a> <i
						class="icon-angle-right"></i></li>
					<li><a href="#">客户管理</a><i class="icon-angle-right"></i></li>
					<li><a href="#">APP客户信息</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
				<form id="searchForm" action="${ctx}/member/verify/list.htm" class="form-horizontal"
					method="post">
					<@p.pageForm value=page />
					<div class="row-fluid">
						<div class="control-group span4 ">
							
							<label class="help-inline text-right span4">姓名：</label>
							 <input	type="text" class="m-wrap span8" name="memberName"
								value="${(queryDto.memberName)!}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">身份证：</label> 
							<input
								type="text" class="m-wrap span8" name="idCarNo"
								value="${(queryDto.idCarNo)!}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">系统准入是否通过：</label>
							<select name="creditResult" class="m-wrap span8">
								<option value="-1">请选择</option>
								<option value="1">是</option>
								<option value="0">否</option>
							</select>
						</div>
					</div>
					<div class="row-fluid">
					
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">现居城市是否有门店：</label>
							<select name="hasStore" class="m-wrap span8">
								<option value="-1">请选择</option>
								<option value="1">有</option>
								<option value="0">没有</option>
							</select>
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">创建日期：</label> 
									<div class="input-append date date-picker" data-date="${(queryDto.createTimeStart?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="createTimeStart" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.createTimeStart?string('yyyy-MM-dd'))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(queryDto.createTimeEnd?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="createTimeEnd" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(queryDto.createTimeEnd?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>	
						</div>						
					</div>
					<p>
						<button type="button" class="btn blue" onclick="search()">查询</button>
						<button type="button" class="btn blue" onclick="export_excel()">下载</button>
					</p>
				</form>
			</div>

			<div class="tabbable tabbable-custom tabbable-custom-profile">
				<!-- table -->
				<table class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th>渠道来源</th>
							<th>姓名</th>
							<th>身份证号</th>
							<th>出生日期</th>
							<th>手机号码</th>
							<th  class="hidden-480">门店城市</th>
							<th  class="hidden-480">现居城市(个人资料)</th>
							<th>申请金额</th>
							<th>系统准入是否通过</th>
							<th>系统准入未通过原因</th>
							<th>现居城市是否有门店</th>
							<th>创建日期</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#list page.result as item>
						<tr class="odd gradeX" id="${item.id}">
							<td>${item_index+1 }</td>
							<td><#if item.memberSource??>
								${(item.memberSource==0)?string('线下','APP')} </#if></td>
							<td>${item.memberName! }</td>
							<td>${item.idCarNo! }</td>
							<td>${(item.birthday?string('yyyy-MM-dd'))!}</td>
							<td>${item.phone!}</td>
							<td  class="hidden-480"><span class="span1 nowrapellipsis" title="${item.liveCityOnApply!}">${item.liveCityOnApply!}</span></td>
							<td  class="hidden-480"><span class="span1 nowrapellipsis" title="${item.liveCity!}">${item.liveCity!}</span></td>
							<td>${item.applyMoney!}</td>
							<td><#if item.creditResult??>
								<#switch item.creditResult>
									<#case 0>不通过<#break>
									<#case 1>通过<#break>
									<#default>未知
								</#switch>
							</#if></td>
							<td><span class="span2 nowrapellipsis" title="${item.creditResultMsg!}">${item.creditResultMsg!}</span></td>
							<td>
							<#if item.hasStore??>
								<#switch item.hasStore>
									<#case 0>没有<#break>
									<#case 1>有<#break>
									<#default>
								</#switch>
							</#if></td>
							<td><#if item.createTime??>${item.createTime?date}</#if></td>
							<td><a href="${ctx}/member/detailUI.htm?id=${item.memberId}"  target='_blank'>查看详情</a></td>
						</tr>
						</#list>
					</tbody>
				</table>
				<@p.pagination value=page />
			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
	$(function(){
		$("select[name='creditResult']").val(${(queryDto.creditResult)!});
		$("select[name='hasStore']").val(${(queryDto.hasStore)!});
	})
	function search() {
		ChkUtil.form_trim($("#searchForm"));
		ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
		$("#searchForm").attr("action", "${ctx}/member/verify/list.htm");
		$("#searchForm").submit();
	}
	
	function export_excel() {
		ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").attr("action", "${ctx}/member/verify/export.htm");
		$("#searchForm").submit();
	}

	function goPage(pageNumber) {
		
		$("#searchForm").attr("action", "${ctx}/member/verify/list.htm");
	    $("#pageIndex").val(pageNumber);
	    ChkUtil.form_trim($("#searchForm"));
	    ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
	    $("#searchForm").submit();
	}
</script>