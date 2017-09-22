<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script type="text/javascript">
    var serverPath = "${serverPath}";
    var queryDto = {orgId:"${(orgId)!''}"};
    var queryTreeDto = {orgId:"${(orgId)!''}"};
	var treeObject={serverPath:serverPath, treePullDown:"#treePullDown", orgId:"#orgId", orgName:"#orgName", orgType:"#orgType", level:"#level", parentId:"#parentId"};
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
			<li><span>员工调动</span></li>
		</ul>
	</div>
	<div class="portlet-body form">
		<form id="tab" action="${serverPath}/employee/addEmployeeMoveInfo.htm" class="form-horizontal" method="POST">
		<input type="hidden" name="createUser" value="${(Session.currentUser.employeeName)!''}">
		<input type="hidden" name="id" value="${item.id?c}"/>
		<input type="hidden" name="employeeId" value="${item.id?c}"/>
		<input type="hidden" id="orgId" name="orgId"/>
		<input type="hidden" id="mobilePhone" name="mobilePhone" value="${item.mobilePhone}"/>
		<input type="hidden" id="parentId" name="parentId"/>
		<input type="hidden" id="newOrgType" name="newOrgType"/>
		<input type="hidden" id="newPosition" name="newPosition"/>
		<input type="hidden" id="newTeam" name="newTeam"/>
		<input type="hidden" id="newJobLevel" name="newJobLevel"/>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">员工姓名</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" value="${item.employeeName}" readonly="readonly">
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">身份证号</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" value="${item.identityNumber}" readonly="readonly">
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">姓名拼音</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" value="${item.nameSpell}" readonly="readonly">
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">员工编号</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" value="${item.employeeNo}" readonly="readonly">
				</div>
			</div>
		</div>
        <div class="row-fluid">
            <div class="control-group span4 ">
                <label class="control-label">银行类别</label>
                <div class="controls">
                <select class="m-wrap span12" name="bankNo" id="bankNo">
                </select>
                </div>
            </div>
            <div class="control-group span4 ">
                <label class="control-label">卡号</label>
                <div class="controls">
                <input type="text" class="m-wrap span12" id="bankCardNo" name="bankCardNo">
                </div>
            </div>
        </div>      
        <div class="row-fluid">
            <div class="control-group span4 ">
                <label class="control-label">开户行</label>
                <div class="controls">
                <input type="text" class="m-wrap span12" id="accountBankName" name="accountBankName">
                </div>
            </div>
            <div class="control-group span4 ">
                <label class="control-label">银行预留手机号</label>
                <div class="controls">
                <input type="text" class="m-wrap span12" id="mobile" name="mobile">
                </div>
            </div>
         </div>                
         <div class="row-fluid">
            <div class="control-group span8 ">
                <label class="control-label">认证状态</label>
                <div class="controls">
                <input type="text" class="m-wrap span3" id="authStatus" value = "未认证" name="authStatus" readOnly></input>
                <button class="btn blue bindCard" onclick="bindCard(event);return false;">绑卡</button>
                <button class="btn blue authentication" onclick="authentication(event);return false;">鉴权</button>
                <button class="btn blue removeBindCard" onclick="removeBindCard(event);return false;">解绑</button>
                </div>
            </div>
        </div>      
		
		
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">原区域</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" name="oldOrgType" value="<#if item.orgType==2>总部<#else>大区</#if>" readonly="readonly">
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">新组织<span class="required">*</span></label>
				<div class="controls">
				<input type="text" class="m-wrap span12" id="orgName" name="orgName" readonly="readonly" onclick="showMenu('#orgName')">
				</div>
			</div>
			<div id="menuContent" class="menuContent">
				<ul id="treePullDown" class="ztree" style="margin-top:0; width:132px;"></ul>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">原大区</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" name="oldRegionName" value="${(item.regionName)!''}" readonly="readonly">
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">新岗位<span class="required">*</span></label>
				<div class="controls">
				<select class="m-wrap span12" id="positionId" name="positionId">
					<option value ="">请选择</option>
				</select>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">原门店</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" name="oldStoreName" value="${(item.storeName)!''}" readonly="readonly">
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">新团队</label>
				<div class="controls">
				<select class="m-wrap span12" id="teamId" name="teamId">
					<option value ="">请选择</option>
				</select>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">原部门</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" name="oldDepartmentName" value="${item.departmentName}" readonly="readonly">
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">新星级</label>
				<div class="controls">
				<select class="m-wrap span12" id="jobLevel" name="jobLevel">
					<option value ="">请选择</option>
				</select>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">原岗位</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" name="oldPosition" value="${item.positionName}" readonly="readonly">
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">原团队</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" name="oldTeam" value="${(item.teamName)!''}" readonly="readonly">
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">原星级</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" name="oldJobLevel" value="${(item.jobLevel)!''}" readonly="readonly">
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">调动日期<span class="required">*</span></label>
				<div class="controls">
				<input type="text" class="m-wrap span12" readonly="readonly" id="moveTime" name="moveTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${.now?string('yyyy-MM-dd')}">
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">调动原因<span class="required">*</span></label>
				<div class="controls">
				<textarea rows="3" id="moveRemark" name="moveRemark"></textarea>
				</div>
			</div>
		</div>
		<div class="form-actions">
			<a class="btn" href="${serverPath}/employee/queryEmployeeInfo.htm?employeeName=${(searchDto.searchemployeeName)!''}&employeeNo=${(searchDto.searchemployeeNo)!''}&identityNumber=${(searchDto.searchidentityNumber)!''}&orgType=${(searchDto.searchorgType)!''}&orgId=${(searchDto.searchorgId)!''}&orgName=${(searchDto.searchorgName)!''}&positionId=${(searchDto.searchpositionId)!''}&pageIndex=${(searchDto.pageIndex)!1}&pageSize=${(searchDto.pageSize)!10}">返回</a>
			<button class="btn blue" onclick="saveMoveInfo(event);return false;">提交</button>
		</div>
		</form>
	</div>
</div>
</div>
</div>
<#include "/sys/bottom.ftl">
<script src="${cdnPath}/js/employee/manageEmployeeMoveInfo.js?v=${VERSION_NO}"></script>
</body>
</html>