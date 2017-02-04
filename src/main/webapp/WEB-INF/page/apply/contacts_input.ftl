	<form id = "form_class_test1" class = "form_class_test1" action="${serverPath}/apply/newInfoIndex/contactsSave.htm">
	<input type="hidden" value="${applyLoanNo!''}" name="applyLoanNo" />
	<!--<input type="hidden" value="MYPHNJ201610090001" name="applyLoanNo" />-->
	<style>

	input[type="radio"], input[type="checkbox"] {
    margin: -3px 0 0; 
    margin-top: 1px \9;
    line-height: normal;
	}
	.table{
		width="100%";
	}
	.table th,
	.table td{border:none;width:540px; padding:4px 8px}
	.table .required{color:#e02222;    font-family: "宋体";}
	
	select,textarea,input{margin-bottom:0 !important}
	hr{margin:0;margin-bottom:4px;  }
	.control-label{text-align: right;padding-right:30px;font-size:14px;  padding-top: 5px;}
	</style>
	
	<table class="table table-hover"  >
		<tr>
			<td>
				<span class="control-label span4">
					<input id="linkManType1" type="checkbox" name="linkManType" class="linkManType" value="1" />直系亲属&nbsp;<span class="required">*</span>
					<input type="hidden" name="linkManType" value="${(familyMap.linkManType)!}" />
				</span>
				<input type="input" name="linkManName" value="${(familyMap.linkManName)!}" class="span6 m-wrap js_input" />
				<input type="hidden" name="state" value="" />
			</td>
			<td>
				<span class="control-label span4">手机号&nbsp;<span class="required">*</span></span>
				<input type="input" class="span6 m-wrap js_input app_class" value="${(familyMap.linkManPhone)!}" name="linkManPhone" />
			</td>
			<td>
				<span class="control-label span4">和本人关系&nbsp;<span class="required">*</span></span>
				<select id="family_relation" class="m-wrap span6 js_input app_class" name="linkManRelation">
						<option value = "">请选择</option>
						<option value="父母">父母</option>
						<option value="配偶">配偶</option>
						<option value="子女">子女</option>
				</select>
			</td>
		</tr>
		
		<tr>
			<td>
				<span class="control-label span4">家庭住址&nbsp;<span class="required">*</span></span>
				<input type="text" class="span6 m-wrap" id = "contactHomeAddress" name="linkManHomeName" value="${(familyMap.linkManHomeName)!'请选择'}" readonly />
			</td>
			
			<td>
				<span class="control-label span4">详细地址&nbsp;<span class="required">*</span></span>
				<input type="input" class="span6 m-wrap js_input" value="${(familyMap.linkManHomeAddress)!}" name="linkManHomeAddress" />
			</td>
		</tr>
		<tr>
			<td>	
				<span class="control-label span4">工作单位全称</span>
				<input placeholder = "" type="input" class="span6 m-wrap" value="${(familyMap.linkManCompanyName)!}" name="linkManCompanyName" />
			</td>
			<td>
				<span class="control-label span4">单位地址</span>
				<input type="text" class="span6 m-wrap"  id = "linkManCompanyAddress" name="linkManCompanyAddress" value = "${(familyMap.linkManCompanyAddress)!'请选择'}" readonly />
			</td>
			<td>
				<span class="control-label span4">详细地址</span>
				<input type="input" class="span6 m-wrap" value="${(familyMap.linkManCompanyAddr)!}" name="linkManCompanyAddr" />
			</td>
		</tr>
  	</table>
  	</form>
  	<hr>
  	<form class = "form_class_test2" action="${serverPath}/apply/newInfoIndex/contactsSave.htm">
  	<input type="hidden" value="${applyLoanNo!''}" name="applyLoanNo" />
  	<table class="table table-hover"  >
		<tr>
			<td>
				<span class="control-label span4">
					<input id="linkManType2" type="checkbox" name="linkManType" class="linkManType" value="4"/>其他亲属&nbsp;<span class="required">*</span>
					<input type="hidden" name="linkManType" value="${(otherMap.linkManType)!}" />	
				</span>
				<input type="input" name="linkManName" value="${(otherMap.linkManName)!}" class="span6 m-wrap js_input" />
				<input type="hidden" name="state" value="" />
			</td>
			<td>
				<span class="control-label span4">手机号&nbsp;&nbsp;<span class="required">*</span></span>
				<input type="input" class="span6 m-wrap js_input app_class" value="${(otherMap.linkManPhone)!}" name="linkManPhone" />
			</td>
			<td>
				<span class="control-label span4">和本人关系&nbsp;&nbsp;<span class="required">*</span></span>
				<input type="input" class="span6 m-wrap js_input app_class" value="${(otherMap.linkManRelation)!}" name="linkManRelation" />
			</td>
		</tr>
		
		<tr>
			<td>
				<span class="control-label span4">家庭住址</span>
				<input type="text" class="span6 m-wrap"  id = "contactHomeAddress2" name="linkManHomeName" value = "${(otherMap.linkManHomeName)!'请选择'}" readonly />
			</td>
			<td>
				<span class="control-label span4">详细地址</span>
				<input type="input" class="span6 m-wrap" value="${(otherMap.linkManHomeAddress)!}" name="linkManHomeAddress" />
			</td>
		</tr>
		<tr>
			<td>	
				<span class="control-label span4">工作单位全称</span>
				<input placeholder = "" type="input" class="span6 m-wrap" value="${(otherMap.linkManCompanyName)!}" name="linkManCompanyName" />
			</td>
			<td>
				<span class="control-label span4">单位地址</span>
				<input type="text" class="span6 m-wrap"  id = "linkManCompanyAddress2" name="linkManCompanyAddress"  value = "${(otherMap.linkManCompanyAddress)!'请选择'}" readonly />
			</td>
			<td>
				<span class="control-label span4">详细地址</span>
				<input type="input" class="span6 m-wrap" value="${(otherMap.linkManCompanyAddr)!}" name="linkManCompanyAddr" />
			</td>
		</tr>
  	</table>
  	</form>
  	<hr>
  	<form class = "form_class_test3" action="${serverPath}/apply/newInfoIndex/contactsSave.htm">
  	<input type="hidden" value="${applyLoanNo!''}" name="applyLoanNo" />
  	<table class="table table-hover"  >
		<tr>
			<td>
				<span class="control-label span4">
					<input id="linkManType3" type="checkbox" name="linkManType" class="linkManType" value="2" />同事&nbsp;<span class="required">*</span>
					<input type="hidden" name="linkManType" value="${(workMap.linkManType)!}" />	
				</span>
				<input type="input" name="linkManName" value="${(workMap.linkManName)!}" class="span6 m-wrap js_input" />
				<input type="hidden" name="state" value="" />
			</td>
			<td>
				<span class="control-label span4">手机号&nbsp;<span class="required">*</span></span>
				<input type="input" class="span6 m-wrap js_input app_class" value="${(workMap.linkManPhone)!}" name="linkManPhone" />
			</td>
			<td>
				<span class="control-label span4">和本人关系&nbsp;<span class="required">*</span></span>
				<input type="input" class="span6 m-wrap js_input app_class" value="${(workMap.linkManRelation)!}" name="linkManRelation" />
			</td>
		</tr>
		
		<tr>
			<td>
				<span class="control-label span4">家庭住址</span>
				<input type="text" class="span6 m-wrap"  id = "contactHomeAddress3" name="linkManHomeName" value="${(workMap.linkManHomeName)!'请选择'}" readonly />
			</td>
			<td>
				<span class="control-label span4">详细地址</span>
				<input type="input" class="span6 m-wrap" value="${(workMap.linkManHomeAddress)!}" name="linkManHomeAddress" />
			</td>
		</tr>
		<tr>
			<td>	
				<span class="control-label span4">工作单位全称</span>
				<input placeholder = "" type="input" class="span6 m-wrap" value="${(workMap.linkManCompanyName)!}" name="linkManCompanyName" />
			</td>
			<td>
				<span class="control-label span4">单位地址</span>
				<input type="text" class="span6 m-wrap"  id = "linkManCompanyAddress3" name="linkManCompanyAddress" value="${(workMap.linkManCompanyAddress)!'请选择'}" readonly />
			</td>
			<td>
				<span class="control-label span4">详细地址</span>
				<input type="input" class="span6 m-wrap" value="${(workMap.linkManCompanyAddr)!}" name="linkManCompanyAddr" />
			</td>
		</tr>
  	</table>
  	</form>
  	<hr>
  	<form class = "form_class_test4" action="${serverPath}/apply/newInfoIndex/contactsSave.htm">
  	<input type="hidden" value="${applyLoanNo!''}" name="applyLoanNo" />
  	<table class="table table-hover"  >
		<tr>
			<td>
				<span class="control-label span4">
					<input id="linkManType4" type="checkbox" name="linkManType" class="linkManType" value="3" />朋友&nbsp;<span class="required">*</span>
					<input type="hidden" name="linkManType" value="${(friendMap.linkManType)!}" />	
				</span>
				<input type="hidden" name="linkManType" value="${(friendMap.linkManType)!}" />
				<input type="input" name="linkManName" value="${(friendMap.linkManName)!}" class="span6 m-wrap js_input" />
				<input type="hidden" name="state" value="" />
			</td>
			<td>
				<span class="control-label span4">手机号&nbsp;<span class="required">*</span></span>
				<input type="input" class="span6 m-wrap js_input app_class" value="${(friendMap.linkManPhone)!}" name="linkManPhone" />
			</td>
			<td>
				<span class="control-label span4">和本人关系&nbsp;<span class="required">*</span></span>
				<input type="input" class="span6 m-wrap js_input app_class" value="${(friendMap.linkManRelation)!}" name="linkManRelation" />
			</td>
		</tr>
		
		<tr>
			<td>
				<span class="control-label span4">家庭住址</span>
				<input type="text" class="span6 m-wrap"  id = "contactHomeAddress4" name="linkManHomeName" value="${(friendMap.linkManHomeName)!'请选择'}" readonly />
			</td>
			<td>
				<span class="control-label span4">详细地址</span>
				<input type="input" class="span6 m-wrap" value="${(friendMap.linkManHomeAddress)!}" name="linkManHomeAddress" />
			</td>
		</tr>
		<tr>
			<td>	
				<span class="control-label span4">工作单位全称</span>
				<input placeholder = "" type="input" class="span6 m-wrap" value="${(friendMap.linkManCompanyName)!}" name="linkManCompanyName" />
			</td>
			<td>
				<span class="control-label span4">单位地址</span>
				<input type="text" class="span6 m-wrap"  id = "linkManCompanyAddress4" name="linkManCompanyAddress" value="${(friendMap.linkManCompanyAddress)!'请选择'}" readonly />
			</td>
			<td>
				<span class="control-label span4">详细地址</span>
				<input type="input" class="span6 m-wrap" value="${(friendMap.linkManCompanyAddr)!}" name="linkManCompanyAddr" />
			</td>
		</tr>
  	</table>
  	</form>
<script>

$(function () {
	showCitys("#contactHomeAddress","${serverPath}");
	showCitys("#contactHomeAddress2","${serverPath}");
	showCitys("#contactHomeAddress3","${serverPath}");
	showCitys("#contactHomeAddress4","${serverPath}");
	
	showCitys("#linkManCompanyAddress","${serverPath}");
	showCitys("#linkManCompanyAddress2","${serverPath}");
	showCitys("#linkManCompanyAddress3","${serverPath}");
	showCitys("#linkManCompanyAddress4","${serverPath}");
     
});
 $("#family_relation").val("${(familyMap.linkManRelation)!}");
 $("input:checkbox[value='${(familyMap.linkManType)!''}']").attr('checked','true');
 $("input:checkbox[value='${(otherMap.linkManType)!''}']").attr('checked','true');
 $("input:checkbox[value='${(workMap.linkManType)!''}']").attr('checked','true');
 $("input:checkbox[value='${(friendMap.linkManType)!''}']").attr('checked','true');
 
 var clientType1 = '${(familyMap.clientType)!''}';
 if(clientType1 == 1) {
 	$(".form_class_test1 input[name='linkManName']").attr("readonly","readonly");
	$(".form_class_test1  input[name='linkManPhone']").attr("readonly","readonly");
	$(".form_class_test1  select[name='linkManRelation']").attr("disabled","disabled");
	$(".form_class_test1  input[type='checkbox']").attr("disabled","disabled");
 }
 
 var clientType2 = '${(otherMap.clientType)!''}';
 if(clientType2 == 1) {
 	$(".form_class_test2 input[name='linkManName']").attr("readonly","readonly");
	$(".form_class_test2  input[name='linkManPhone']").attr("readonly","readonly");
	$(".form_class_test2  input[name='linkManRelation']").attr("readonly","readonly");
	$(".form_class_test2  input[type='checkbox']").attr("disabled","disabled");
 }
 
 var clientType3 = '${(workMap.clientType)!''}';
 if(clientType3 == 1) {
 	$(".form_class_test3 input[name='linkManName']").attr("readonly","readonly");
	$(".form_class_test3  input[name='linkManPhone']").attr("readonly","readonly");
	$(".form_class_test3  input[name='linkManRelation']").attr("readonly","readonly");
	$(".form_class_test3  input[type='checkbox']").attr("disabled","disabled");
 }
 
 var clientType4 = '${(friendMap.clientType)!''}';
 if(clientType4 == 1) {
 	$(".form_class_test4 input[name='linkManName']").attr("readonly","readonly");
	$(".form_class_test4  input[name='linkManPhone']").attr("readonly","readonly");
	$(".form_class_test4  input[name='linkManRelation']").attr("readonly","readonly");
	$(".form_class_test4  input[type='checkbox']").attr("disabled","disabled");
 }
 Search.init();
 </script>