<#include "/sys/top.ftl"> 
<#include "/sys/left.ftl">
<div class="page-content">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				
				<!--页面标题-->
				<h3 class="page-title">
				</h3>
				<!--面包屑导航-->
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="${serverPath}/index.htm">首页</a> <i
						class="icon-angle-right"></i></li>
					<li><a href="javascript:void(0);">员工绩效管理</a><i class="icon-angle-right"></i></li>
					<li><a href="javascript:void(0);">业务员绩效考核</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
				<form id="searchForm" action="#" class="form-horizontal"
					method="post">
					<input type="hidden" name="sortField" id="sortField" value="sumRepayMoney">
					<input type="hidden" name="sortOrder" id="sortOrder" value="desc">
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">大区：</label>
							<div class="">
							<select name="areaId" class="m-wrap span8" data-id="${(query.areaId)!}" onchange="initStoreData()">
								<option value="">请选择</option>
							</select>
							</div>
						</div>
						<div class="control-group span4 " class="m-wrap span8">
							<label class="help-inline text-right span4">门店：</label> 
							<div class="">
							<select name="storeId" class="m-wrap span8" data-id="${(query.storeId)!}">
								<option value="">请选择</option>
							</select>
							</div>
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">查询日期：</label> 
							<div class="">
									<div class="input-append date date-picker" data-date="${(query.signTimeStart?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="signTimeStart" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(query.signTimeStart?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>		
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker" data-date="${(query.signTimeEnd?string('yyyy-MM-dd'))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
										<input name="signTimeEnd" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(query.signTimeEnd?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>	
							</div>		
						</div>
					</div>	
					<div class="row-fluid">
						<div class="control-group span4 ">
							<button type="button" class="btn blue" onclick="search()">查询</button>
							<button type="button" class="btn blue" onclick="export_excel()">下载</button>
						</div>
					</div>
				</form>
			</div>

			<div class="tabbable tabbable-custom tabbable-custom-profile">
				<table id="table" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th>门店</th>
							<th>进件总数</th>
							<th>初审通过数</th>
							<th>初审拒绝数</th>
							<th>终审通过数</th>
							<th>终审拒绝数</th>
							<th>合规通过数</th>
							<th>财务通过数</th>
							<th>申请总金额</th>
							<th data-field="sumRepayMoney">放款总金额</th>
							<th data-field="loanMeanRate">平均放款率</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
					<#if data??>
						<#list data as item>
						<tr id="${item_index+1!}">
							<td>${item_index+1!}</td>
							<td>${item.storeName!}</td>
							<td>${item.sumCount!}</td>
							<td>${item.oneAuditPassCount!'0'}</td>
							<td>${item.oneAuditRefuseCount!'0'}</td>
							<td>${item.twoAuditPassCount!'0'}</td>
							<td>${item.twoAuditRefuseCount!'0'}</td>
							<td>${item.complianceCount!'0'}</td>
							<td>${item.loanCount!'0'}</td>
							<td>${item.sumApplyMoney!'0'}</td>
							<td>${item.sumRepayMoney!'0'}</td>
							<td>${item.loanMeanRate!'0'}%</td>
							<#if item.sumCount gt 0>
								<td><a href="javascript:goHref(${item.storeId!''})">详细信息</a></td>
							<#else>
								<td></td>
							</#if>
						</tr>
						</#list>
					</#if>
						
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
$(function(){
	$("#table").dataTable(
	{
		   "aoColumnDefs":[ { "bSortable": false, "aTargets": [0,1,2,3,4,5,6,7,8,9,12] },{"sType": "number-fate","aTargets": [11]}]//设置不需要排序的列
		   ,"aaSorting": [[10, "desc"]]
		   ,"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
	             $('td:eq(0)', nRow).html(iDisplayIndexFull+1);
	      }
	      ,"fnHeaderCallback": function( nHead, aData, iStart, iEnd, aiDisplay ) {
	    	    var sortTh=$("#table th[aria-sort]");
	    		$("#sortField").val(sortTh.attr("data-field"));
	    		if(sortTh.attr("aria-sort")=='descending'){
	    			$("#sortOrder").val('desc');
	    		}else{
	    			$("#sortOrder").val('asc');
	    		}
	 	  }
	}
	);
});
function goHref(e) {
		if(null != e) {
			window.open("${serverPath}/performance/bmStore.htm?storeId="+e+"&areaId="
			+$("select[name='areaId']").val()
			+"&signTimeStart="+$("input[name='signTimeStart']").val()
			+"&signTimeEnd="+$("input[name='signTimeEnd']").val(), '_blank');
		} else {
			window.open("${serverPath}/performance/bmStore.htm?storeId="
			+"&areaId="+$("select[name='areaId']").val()
			+"&signTimeStart="+$("input[name='signTimeStart']").val()
			+"&signTimeEnd="+$("input[name='signTimeEnd']").val(), '_blank');
		}
}
</script>

<script>
	var orgType="${(user.orgType)!}"; //组织类型1:大区 2：总部 3：门店
	var regionId="${(empDetail.regionId)!}";//大区id
	var storeId="${(empDetail.storeId)!}";//门店id
	function search() {
		ChkUtil.form_trim($("#searchForm"));
		ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
		$("#searchForm").attr("action", "${serverPath}/performance/allStore.htm");
		$("#searchForm").submit();
	}
	function goPage(pageNumber) {
	    ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").attr("action", "${serverPath}/apply/progress/list.htm");
	    $("#pageIndex").val(pageNumber);
	    $("#searchForm").submit();
	}
	function page_jump(url){
		ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
		window.location.href=url;
	}
	
	function export_excel() {
		ChkUtil.form_trim($("#searchForm"));
		$("#searchForm").attr("action", "${serverPath}/performance/allStoreExport.htm");
		$("#searchForm").submit();
	}
	$(function(){
			init();
	});
	function init(){
		$("#searchBtn").attr("disabled","disabled");
		//加载大区数据
		initAreaData();
	}
	function initAreaData(){
	var queryData;
	var url;
	if(orgType==2){
	    url=serverPath + "/organization/selectOrgByOrgType.htm";
		queryData ={"orgType" : 1,	"Time" : new Date().getMilliseconds()}
	}else{
	    url=serverPath + "/organization/selectOrganizationById.htm";
		queryData ={"id":regionId,	"Time" : new Date().getMilliseconds()}
	};
		$.ajax({
			url: url,
			type:"post",
			data: queryData,
			dataType:"json",
			success:function(result){
				//清空除第一条内容的外的其它数据
				var select_ = $("select[name='areaId']");
				select_.find("option:gt(0)").remove();
				if(orgType==2){
				    for (var i = 0; i < result.data.length; i++) {
                        var isSelected = result.data[i].id == select_.attr('data-id')?"selected='selected'":"";
                        select_.append(
                                "<option "+isSelected+" value='"
                                        + result.data[i].id + "'>"
                                        + result.data[i].orgName
                                        + "</option>");
                    }
				}else{
				    var isSelected = result.data.id == select_.attr('data-id')?"selected='selected'":"";
                        select_.append(
                                "<option "+isSelected+" value='"
                                        + result.data.id + "'>"
                                        + result.data.orgName
                                        + "</option>");
				}
				
				if(orgType!=2){
				 select_.find("option:eq(0)").remove();
				  select_.val(regionId);
				}
				initStoreData();
			},
			error:function(){
				alert("加载失败");
			}
		});
	}
	function initStoreData(){
		var parentId  =  $("select[name='areaId']").val();
		if(ChkUtil.isNull(parentId)) {
			$("select[name='storeId']").find("option:gt(0)").remove();
			return;
		}
		if(parentId == "0"){
			parentId = $("select[name='areaId']").attr('data-id');
		}
		if(parentId == "0"){
			return;
		}
		var queryData;
		var url;
		if(orgType==3)
		{
			queryData ={"id":storeId,	"Time" : new Date().getMilliseconds()}
			url=serverPath + "/organization/selectOrganizationById.htm";
		}else{
			queryData ={"parentId" : parentId,	"Time" : new Date().getMilliseconds()}
			url=serverPath + "/organization/selectOrgByParentId.htm";
		};
		$.ajax({
			url: url,
			type:"post",
			data:queryData,
			dataType:"json",
			success:function(result){
				//清空除第一条内容的外的其它数据
				var select_ = $("select[name='storeId']");
				select_.find("option:gt(0)").remove();
				if(orgType==3){
				    var isSelected = result.data.id == select_.attr('data-id')?"selected='selected'":"";
                        select_.append(
                                "<option "+isSelected+" value='"
                                        + result.data.id + "'>"
                                        + result.data.orgName
                                        + "</option>");
				}else{
				    for (var i = 0; i < result.data.length; i++) {
                        var isSelected = result.data[i].id == select_.attr('data-id')?"selected='selected'":"";
                        select_.append(
                                "<option "+isSelected+" value='"
                                        + result.data[i].id + "'>"
                                        + result.data[i].orgName
                                        + "</option>");
                    }
				}
				
				if(orgType==3){
				  select_.find("option:eq(0)").remove();
				  select_.val(storeId);
				}
				$('#searchBtn').attr("disabled",false);
			},
			error:function(){
				alert("加载失败");
			}
		});
	}
</script>