<#include "/apply/approve/top.ftl">
<script>
	$(function(){
		//选中一级目录
		$("li[name='tab']:eq(0)").addClass("active");
		//选中二级目录
		$("li[name='two_li']:eq(0)").addClass("active");

		$("#hostoy_btn").css("display","");

        $("#table_detail").dataTable(
			{
				"aoColumnDefs":[ { "bSortable": false, "aTargets": [0,1,3,4] }]//设置不需要排序的列
				,"aaSorting": [[2, "desc"]]
                ,"bPaginate": false ///显示使用分液器
                ,"bLengthChange": false  ///是否可以修改页面显示行数
				,"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
					$('td:eq(0)', nRow).html(iDisplayIndexFull+1);
				 }
				,"fnHeaderCallback": function( nHead, aData, iStart, iEnd, aiDisplay ) {
					var sortTh=$("#table_detail th[aria-sort]");
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
		window.location.href=e+$("#applyLoanNo").val()+"&cType="+$("#cType").val();
	} 
</script>
<input type="hidden" id="cType" name="cType" value="${cType!}"/>
	<div id="applyDetailList" class="modal hide fade" tabindex="-1" data-width="760" style="width:50%;height:300px;overflow-y: scroll;" >
        <input type="hidden" name="sortField" id="sortField" value="createTime">
        <input type="hidden" name="sortOrder" id="sortOrder" value="desc">
		<div class="modal-body">
            <div class="tabbable tabbable-custom tabbable-custom-profile">
                <table id="table_detail" class="table table-striped table-bordered table-hover">
					<thead>
					<tr>
						<th>序号</th>
						<th>申请单号</th>
						<th data-field="createTime">进件日期</th>
						<th>状态</th>
						<th>操作</th>
					</tr>
					</thead>
					<tbody>
<#if historyData??>
					<#list historyData as record>
                    <tr id="${record_index+1!}">
                        <td>${record_index+1!}</td>
                        <td>${record.applyLoanNo!}</td>
                        <td>${record.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                        <td>${record.stateDesc!}</td>
                        <td>
                            <a href="${serverPath}/apply/applyDetail.htm?applyLoanNo=${record.applyLoanNo!}&cType=1" target="_blank">查看详情</a>
						</td>
                    </tr>
					</#list>
</#if>
					</tbody>
				</table>
            </div>
		</div>
	</div>
<#if updateCType?? && !cType??>
	<#include "/apply/audit/common/detail_content2.ftl">
<#else>
	<#include "/apply/audit/common/detail_content.ftl">
</#if>


		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">