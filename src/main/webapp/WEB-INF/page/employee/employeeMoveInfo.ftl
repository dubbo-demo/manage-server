<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script>
    $(function() {
        $(".aShow").on('click',function(){
            var moveRemark = $(this).data("moveremark");
            $(".remark").val(moveRemark);
        })
    });
</script>
<div class="page-content" >
<div class="container-fluid">
	<!-- header -->
	<div class="row-fluid">
			<!--页面标题-->
				<h3 class="page-title">
				</h3>
		<ul class="breadcrumb">
			<li>
				<i class="icon-home"></i>
				<a href="${serverPath}/index.htm">首页</a> 
				<i class="icon-angle-right"></i>
			</li>
			<li><span>员工绩效管理</span><i class="icon-angle-right"></i>
			</li>
			<li>
				<a href="${serverPath}/employee/queryEmployeeInfo.htm">员工信息管理</a>
				<i class="icon-angle-right"></i>
			</li>
			<li><span>员工调动记录</span></li>
		</ul>
	</div>

	<div class="portlet-body form">
		<div class="row-fluid">
			<div class="control-group span4 ">
				<a class="btn blue" href="${serverPath}/employee/queryEmployeeInfo.htm?employeeName=${(searchDto.searchemployeeName)!''}&employeeNo=${(searchDto.searchemployeeNo)!''}&identityNumber=${(searchDto.searchidentityNumber)!''}&orgType=${(searchDto.searchorgType)!''}&orgId=${(searchDto.searchorgId)!''}&orgName=${(searchDto.searchorgName)!''}&positionId=${(searchDto.searchpositionId)!''}&pageIndex=${(searchDto.pageIndex)!1}&pageSize=${(searchDto.pageSize)!10}">返回</a>
			</div>
		</div>
	</div>
	<div class="tabbable tabbable-custom tabbable-custom-profile">
		<!-- table -->
		<table class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>员工姓名</th>
					<th>员工编号</th>
					<th>身份证号</th>
					<th>手机号码</th>
					<th>原大区</th>
					<th>新大区</th>
					<th>原门店</th>
					<th>新门店</th>
					<th>原部门</th>
					<th>新部门</th>
					<th>原岗位</th>
					<th>新岗位</th>
					<th>原团队</th>
					<th>新团队</th>
					<th>原星级</th>
					<th>新星级</th>
					<th>类型</th>
					<th>操作人</th>
					<th>日期</th>
					<th>备注</th>
				</tr>
			</thead>
			<tbody>
				<#list page as item>
				<tr>
					<td>${item_index+1}
						<input type="hidden" value="${item.id?c}"/>
					</td>
					<td>${item.employeeName!}</td>
					<td>${item.employeeNo!}</td>
					<td>${item.identityNumber!}</td>
					<td>${item.mobilePhone!}</td>
					<td>${item.oldRegionName!}</td>
					<td>${item.newRegionName!}</td>
					<td>${item.oldStoreName!}</td>
					<td>${item.newStoreName!}</td>
					<td>${item.oldDepartmentName!}</td>
					<td>${item.newDepartmentName!}</td>
					<td>${item.oldPosition!}</td>
					<td>${item.newPosition!}</td>
					<td>${item.oldTeam!}</td>
					<td>${item.newTeam!}</td>
					<td>${item.oldJobLevel!}</td>
					<td>${item.newJobLevel!}</td>
					<td><#if item.type == 1>修改<#elseif item.type == 2>调动<#elseif item.type == 3>离职<#elseif item.type == 4>复职<#elseif item.type == 5>启用<#elseif item.type == 6>禁用</#if></td>
					<td>${item.createUser!}</td>
					<td>${item.moveTime!}</td>
					<td><a data-target="#show" data-toggle="modal" class="aShow" data-moveRemark='${item.moveRemark!}'>详情</a></td>
				</tr>
				</#list>
			</tbody>
		</table>
	</div>
	<div id="show" class="modal hide fade show" tabindex="-1" data-width="760">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true"></button>
                <h3>备注</h3>
        </div>
        <div class="modal-body">   
            <div class="row-fluid ">
                <label class="help-inline span2 text-left control-label">说明：</label>
                <textarea rows="4" class="span9 m-wrap remark" id="remark" name="remark" readonly></textarea>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>  
        </div>
    </div>
        
</div>
</div>
<#include "/sys/bottom.ftl">
</body>
</html>