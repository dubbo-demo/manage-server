<div class="tab-pane row-fluid active" id="reciptionDetail">
	<div class="portlet box">
		<div class="portlet-body form">
				<div>
					<h4 style="text-align:center">
						<strong>接待信息</strong>
					</h4>
				</div>
			<hr>
			<!-- edit form -->
			<form id="tab" action="#" method="POST"
				class="form-horizontal">
				<div class="row-fluid">
					<div class="control-group span6 ">
						<label class="control-label">客户姓名<span class="required">*</span></label>
						<div class="controls">
							<input type="text" maxlength="6" name="memberName" value="${(reception.memberName)!}" class="m-wrap large" />
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<div class="control-group span6 ">
						<label class="control-label">手机号码<span class="required">*</span></label>
						<div class="controls">
							<input type="text" name="phone"  value="${(reception.phone)!}"  maxlength="11" class="m-wrap large" />
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<div class="control-group span6 ">
						<label class="control-label">身份证号<span class="required">*</span></label>
						<div class="controls">
							<input type="text" name="idCard" id="idCard" value="${(reception.idCard)!}"  class="m-wrap large" />
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<div class="control-group span6 ">
						<label class="control-label">业务经理<span class="required">*</span></label>
						<div class="controls">
							<input type="text" id='bmName' name="bmName" value="${(reception.bmName)!}" class="m-wrap large"/>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<div class="control-group span6 ">
						<label class="control-label">客户类型<span class="required">*</span></label>
						<div class="controls">
							<select name="memberType" class="m-wrap large">
								<option value="">请选择</option>
								<#if (reception.memberType)?? && reception.memberType == "0">
									<option value="0" selected>工薪族</option>
									<option value="1">企业主</option>
								<#else>
									<option value="0">工薪族</option>
									<option value="1" selected>企业主</option>
								</#if>
							</select>
						</div>
					</div>
				</div>
				<div class="row-fluid">
				</div>
				<hr>
				<div>
					<h4 style="text-align:center">
						<strong>贷款意向</strong>
					</h4>
				</div>
				<hr>
				<div class="row-fluid">
					<div class="control-group span6 ">
						<label class="control-label">信息获知渠道<span class="required">*</span></label>
						<div class="controls">
							<select name="infoChannel" class="m-wrap large" data-value="${(reception.infoChannel)!}">
								<option value="">请选择</option>
								<option value="1">网络</option>
								<option value="2">报纸</option>
								<option value="5">促销活动</option>
								<option value="3">朋友介绍</option>
								<option value="4">其他</option>
							</select>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<div class="control-group span6 ">
						<label class="control-label">借款用途<span class="required">*</span></label>
						<div class="controls">
							<select name="loanUse" class="m-wrap large" data-value="${(reception.loanUse)!}">
							</select>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<div class="control-group span6 ">
						<label class="control-label">可承受月还款额<span class="required">*</span></label>
						<div class="controls">
							<#if (reception.affordMonthRepay)??>
							 <input type="text"  class="m-wrap " style="width:100px;" value="${(reception.affordMonthRepay?split('-'))[0]}"/>
							 <span style="line-height:20px;">__</span>
							 <input type="text" class="m-wrap " style="width:100px;" value="${(reception.affordMonthRepay?split('-'))[1]}"/>
							 <#else>
							 <input type="text"  class="m-wrap " style="width:100px;"/>
							 <span style="line-height:20px;">__</span>
							<input type="text" class="m-wrap " style="width:100px;"/>
							 </#if>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<div class="control-group span6 ">
						<label class="control-label">产品类型<span class="required">*</span></label>
						<div class="controls">
						<input type="hidden" name="prodType2" id="prodType2" value="${(reception.prodType)!}">
							<select name="prodType" id="prodType"
							data-value="${(reception.prodType)!}" onchange="getProduct(this)" class="m-wrap large">
							</select>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<div class="control-group span6 ">
						<label class="control-label">贷款期数<span class="required">*</span></label>
						<div class="controls">
						<input type="hidden" name="productId2" id="productId2" value="${(reception.productId)!}">
						<input type="hidden" name="loanPeriods2" id="loanPeriods2" value="${(reception.loanPeriods)!}">
							<select name="loanPeriods" id="loanPeriods" data-value="${(reception.prodType)!}" class="m-wrap large">
								<option value="${(reception.loanPeriods)!}" selected="selected">${(reception.loanPeriods)!}</option>
							</select>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<div class="control-group span6 ">
						<label class="control-label">申请金额<span class="required">*</span></label>
						<div class="controls">
							<input type="text" name="applyMoney"  maxlength="10" value="${(reception.applyMoney)!}"
							onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="m-wrap large" />
						</div>
					</div>
				</div>
				<hr>
				<div>
					<h4 style="text-align:center">
						<strong>受理</strong>
					</h4>
				</div>
				<hr>
				<div class="row-fluid">
					<div class="control-group span6 ">
						<label class="control-label">受理结论<span class="required">*</span></label>
						<div class="controls">
							<select name="state" id="state" class="m-wrap large">
								<option value="">请选择</option>
								<option value="1">通过</option>
								<option value="0">拒绝</option>
							</select>
						</div>
					</div>
					<div class="control-group span6 ">
						<label class="control-label">接待时间</label>
						<div class="controls">
							<span class="text">${(reception.createTime?datetime)!''}</span>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<div class="control-group">
						<label class="control-label">贷款详细用途</label>
						<div class="controls">
							<textarea class="span9 m-wrap" rows="3" name="loanPurpose" readonly maxlength="300">${(reception.loanPurpose)!}</textarea>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<div class="control-group">
						<label class="control-label">意见描述</label>
						<div class="controls">
							<textarea class="span9 m-wrap" rows="3" name="description" readonly maxlength="300">${(reception.description)!}</textarea>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>	

<script>
function getProductNum(prod) {
	var url = serverPath+"/reception/showProductNum.htm";
	var data = {
		"productType" : prod,
	};
	$.getJSON(url, data, function(result) {
		var resultData = result.data;
		$("#loanPeriods").append("<option value=''>请选择</option>");
		for (var i = 0; i < resultData.length; i++) {
			$("#loanPeriods").append(
					"<option value='" + resultData[i].id + "'>"
							+ resultData[i].periods + "</option>");
		}
		$("#loanPeriods").val($("#productId2").val());
	});
}
function getNode(xmlId,parentCode) {
	var url = serverPath+"/reception/showProduct.htm";
	var data = {
		"parentCode" : parentCode,
	};
	$.getJSON(url, data, function(result) {
		$(xmlId).empty();
		var resultData = result.data;
		$(xmlId).append("<option value=''>请选择</option>");
		for (var i = 0; i < resultData.length; i++) {
			if($(xmlId).data("value") == resultData[i].id) {
				$(xmlId).append(
						"<option selected value='" + resultData[i].id + "'>"
								+ resultData[i].nodeName + "</option>");
			} else {
			$(xmlId).append(
					"<option value='" + resultData[i].id + "'>"
							+ resultData[i].nodeName + "</option>");
			}
		}
	});
}
$("select[name='infoChannel']").val(${(reception.infoChannel)!});
$("select[name='state']").val(${(reception.state)!});
$("#reciptionDetail select").attr("disabled","disabled");
$("#reciptionDetail input").attr("readonly","readonly");

getNode("#prodType","proType");
getNode("#tab select[name='loanUse']","loanUse");
//getProductNum($("#prodType2").val());
</script>