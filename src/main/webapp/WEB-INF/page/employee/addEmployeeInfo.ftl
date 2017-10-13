<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/employee/addEmployeeInfo.js?v=${VERSION_NO}"></script>
<script type="text/javascript">
    var serverPath = "${serverPath}";
    var queryDto = {orgId:"${(orgId)!''}"};
    var queryTreeDto = {orgId:"${(orgId)!''}"};
    var treeObject={serverPath:serverPath, treePullDown:"#treePullDown", orgId:"#orgId", orgName:"#orgName", orgType:"#orgType", level:"#level", parentId:"#parentId"};
    
$(function() {
	$("form").validate({
		rules : {
			employeeName : {
				required : true,
				maxlength : 6
			},
			identityNumber : {
				required : true,
				idCard : true
			},
			mobilePhone : {
				required : true,
				phone : true
			},
			orgName : {
				required : true
			},
			positionId : {
				required : true
			},
			entryTime : {
				required : true
			}
		},
		submitHandler : function(form) { // 表单提交句柄,为一回调函数，带一个参数form
			form.submit(); // 提交表单
		}
	});

	initTreePullDown(treeObject.treePullDown);

});
</script>
<div class="page-content" >
<div class="container-fluid">
	<!-- header -->
	<div class="row-fluid">
			<!--页面标题-->
				<h3 class="page-title"></h3>
		<ul class="breadcrumb">
			<li><i class="icon-home"></i>
			<li><a href="${serverPath}/index.htm">首页</a> <i class="icon-angle-right"></i> </li>
			</li>
			<li><span>员工绩效管理</span> <i class="icon-angle-right"></i>
			</li>
			<li>
				<a href="${serverPath}/employee/queryEmployeeInfo.htm">员工信息管理</a>
				<i class="icon-angle-right"></i>
			</li>
			<li><span>新增员工信息</span></li>
		</ul>
	</div>
	<div class="portlet-body form">
		<form id="tab" action="${serverPath}/employee/saveEmployeeInfo.htm" method="POST" class="form-horizontal">
		<input type="hidden" id="orgId" name="orgId"/>
		<input type="hidden" id="parentId" name="id"/>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">员工姓名<span class="required">*</span></label>
				<div class="controls">
				<input type="text" class="m-wrap span12" id="employeeName" name="employeeName">
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">身份证号<span class="required">*</span></label>
				<div class="controls">
				<input type="text" class="m-wrap span12" id="identityNumber" name="identityNumber">
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">手机号码<span class="required">*</span></label>
				<div class="controls">
				<input type="text" class="m-wrap span12" id="mobilePhone" name="mobilePhone">
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">性别<span class="required">*</span></label>
				<div class="controls">
				<select class="m-wrap span12" name="sex">
					<option value ="1">男</option>
					<option value ="2">女</option>
				</select>
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
            <div class="control-group span4 ">
                <label class="control-label">开户行省份</label>
                <div class="controls">
                    <select onchange="getCity()" class="m-wrap span12" id="province">
                        <option value="0">请选择省份</option>
                    </select>
                </div>
            </div>
            <div class="control-group span4 ">
                <label class="control-label">开户行城市</label>
                <div class="controls">
                    <select class="m-wrap span12" id="city">
                        <option value="0">请选择市</option>
                    </select>
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
				<label class="control-label">组织架构<span class="required">*</span></label>
				<div class="controls">
				<input type="text" class="m-wrap span12" id="orgName" name="orgName" readonly="readonly" onclick="showMenu('#orgName')">
				</div>
			</div>
			<div id="menuContent" class="menuContent">
				<ul id="treePullDown" class="ztree" style="margin-top:0; width:132px;"></ul>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">岗位<span class="required">*</span></label>
				<div class="controls">
				<select class="m-wrap span12" id="positionId" name="positionId">
					<option value ="">请选择</option>
				</select>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">团队</label>
				<div class="controls">
				<select class="m-wrap span12" id="teamId" name="teamId">
					<option value ="">请选择</option>
				</select>
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">星级</label>
				<div class="controls">
				<select class="m-wrap span12" id="jobLevel" name="jobLevel">
					<option value ="">请选择</option>
				</select>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label control-label">入职日期<span class="required">*</span></label>
				<div class="controls">
				<input type="text" class="m-wrap span12" readonly="readonly" id="entryTime" name="entryTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="control-group span4 ">
				<label class="control-label">创建日期</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" readonly="readonly" value="${.now?string('yyyy-MM-dd')}">
				</div>
			</div>
			<div class="control-group span4 ">
				<label class="control-label">创建人</label>
				<div class="controls">
				<input type="text" class="m-wrap span12" id="createUser" name="createUser" readonly="readonly" value="${(Session.currentUser.employeeName)!''}">
				</div>
			</div>
		</div>
		<div class="form-actions">
			<a class="btn" href="${serverPath}/employee/queryEmployeeInfo.htm?employeeName=${(searchDto.searchemployeeName)!''}&employeeNo=${(searchDto.searchemployeeNo)!''}&identityNumber=${(searchDto.searchidentityNumber)!''}&orgType=${(searchDto.searchorgType)!''}&orgId=${(searchDto.searchorgId)!''}&orgName=${(searchDto.searchorgName)!''}&positionId=${(searchDto.searchpositionId)!''}&pageIndex=${(searchDto.pageIndex)!1}&pageSize=${(searchDto.pageSize)!10}">返回</a>
			<button class="btn blue" onclick="save(event);return false;">提交</button>
		</div>
		</form>
	</div>
</div>
</div>
</div>
<#include "/sys/bottom.ftl">
</body>
</html>