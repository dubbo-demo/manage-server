<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<link rel="stylesheet" type="text/css" href="${cdnPath}/common/bootstrap/media/css/select2_metro.css" />
<script type="text/javascript" src="${cdnPath}/common/bootstrap/media/js/select2.min.js"></script>
<div class="page-content">
			<!-- header -->
			<div class="container-fluid">
				<div class="row-fluid">
					<div class="span12">
						<!--页面标题-->
						<h3 class="page-title"></h3>
						<!--面包屑导航-->
						<ul class="breadcrumb">
							<li> <i class="icon-home"></i>
								<a href="list.htm">首页</a> <i class="icon-angle-right"></i> </li>
							<li>
								<a href="#">申请件管理</a><i class="icon-angle-right"></i>
							</li>
							<li>
									<a href="javascript:void(0)">分公司业务管理</a><i class="icon-angle-right"></i>
								</li>
							<li>
								<a href="#">接待信息</a>
							</li>
						</ul>
					</div>
				</div>
			<div>
			<!-- header_end -->
			<div class="row-fluid">
				<div class="portlet box blue">
					<div class="portlet-title">
						<div class="caption">
							<i class="icon-reorder"></i>接待信息
						</div>
					</div>
					<div class="portlet-body form">

						<!-- edit form -->
						<form id="tab" action="${serverPath}/reception/list.htm" method="POST"
							class="form-horizontal">
							<input type="hidden" id="id" name="id" value="${dto.id!}">
							<input type="hidden" id="applyLoanNo" name="applyLoanNo" value="${dto.applyLoanNo}">
							<input type="hidden" id="cityId" name="cityId" value="${dto.cityId!}">
							<input type="hidden" id="storeId" name="storeId" value="${dto.storeId!}">
							<input type="hidden" id="areaId" name="areaId" value="${dto.areaId!}">
							<input type="hidden" id="clientType" name="clientType" value="${dto.clientType!}">
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">客户姓名<span class="required">*</span></label>
									<div class="controls">
										<input type="text" maxlength="6" name="memberName" 
										<#if dto.clientType?? && dto.clientType == 1>readonly</#if> value="${dto.memberName!}" class="m-wrap span6" />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">手机号码<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="phone"  value="${dto.phone!}"  
										<#if dto.clientType?? && dto.clientType == 1>readonly</#if> maxlength="11" class="m-wrap span6" />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">身份证号<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="idCard" id="idCard" 
										<#if dto.clientType?? && dto.clientType == 1>readonly</#if> value="${dto.idCard!}"  class="m-wrap span6" />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">业务经理<span class="required">*</span></label>
									<div class="controls">
										<input type="hidden" id='bmId'  value="${dto.bmId!}"  name="bmId" class="m-wrap span6" />
										<input type="hidden" id='bmName' name="bmName" value="${dto.bmName!}" />
										<input type="hidden" id='customerServiceId'  value="${dto.customerServiceId!}"  name="customerServiceId"  />
										<input type="hidden" id='customerSName' name="customerSName" value="${dto.customerSName!}" />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">客户类型<span class="required">*</span></label>
									<div class="controls">
										<select name="memberType" class="m-wrap span6">
											<option value="">请选择</option>
											<#if dto.memberType?? && dto.memberType!="">
												<#if dto.memberType == "0">
													<option value="0" selected>工薪族</option>
													<option value="1">企业主</option>
												<#else>
													<option value="0">工薪族</option>
													<option value="1" selected>企业主</option>
												</#if>
											<#else>
												<option value="0">工薪族</option>
												<option value="1">企业主</option>
											</#if>
											
										</select>
									</div>
								</div>
							</div>
							<div class="row-fluid">
							</div>
							<hr style="margin:5px 0px 15px 0px;">
							<div>
								<span class="span1"></span>
								<h4>
									<strong>贷款意向</strong>
								</h4>
							</div>
							<hr style="margin:10px 0px 15px 0px;">
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">信息获知渠道<span class="required">*</span></label>
									<div class="controls">
										<select name="infoChannel" class="m-wrap span6" data-value="${dto.infoChannel!}">
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
										<select name="loanUse" class="m-wrap span6" data-value="${dto.loanUse!}">
										</select>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">可承受期还款额<span class="required">*</span></label>
									<div class="controls">
										<input type="hidden" name="affordMonthRepay" 
										 id="affordMonthRepay" value="${dto.affordMonthRepay!}" class="m-wrap span6" />
										<input type="text" name="affordMonthRepay1"
										onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" 
										id="affordMonthRepay1" class="m-wrap " style="width:100px;"/>
										<span style="line-height:20px;" >__</span>
										<input type="text" name="affordMonthRepay2" id="affordMonthRepay2"
										onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" class="m-wrap " style="width:100px;"/>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">产品类型<span class="required">*</span></label>
									<div class="controls">
									<input type="hidden" name="prodType2" id="prodType2" value="${dto.prodType!}">
										<select name="prodType" id="prodType"
										data-value="${dto.prodType!}" onchange="getProduct(this)" class="m-wrap span6">
										</select>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">贷款期数<span class="required">*</span></label>
									<div class="controls">
									<input type="hidden" name="productId2" id="productId2" value="${dto.productId!}">
										<input type="hidden" name="loanPeriods" id="loanPeriods" value="${dto.loanPeriods!}"/>
										<select name="productId" id="productId" class="m-wrap span6">
											<option value="">请选择</option>
										</select>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">申请金额<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="applyMoney"  maxlength="8" value="${dto.applyMoney!}" 
										<#if dto.clientType?? && dto.clientType == 1>readonly</#if>
										onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="m-wrap large" />
									</div>
								</div>
							</div>
							<#-- <div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">期望获得贷款额<span class="required">*</span></label>
									<div class="controls">
										<input type="hidden" name="expectLoanAmount"
										 id="expectLoanAmount" value="${dto.expectLoanAmount!}" class="m-wrap large" />
										<input type="text" name="expectLoanAmount1" id="expectLoanAmount1" 
										onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" class="m-wrap " style="width:100px;"/>
										——<input type="text" name="expectLoanAmount2" id="expectLoanAmount2" 
										onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" class="m-wrap " style="width:100px;"/>
									</div>
								</div>
							</div> -->
							<hr>
							<div style="margin:5px 0px 15px 0px;">
								<span class="span1"></span>
								<h4>
									<strong>受理</strong>
								</h4>
							</div>
							<hr style="margin:10px 0px 15px 0px;">
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">受理结论<span class="required">*</span></label>
									<div class="controls">
										<select name="state" id="state" class="m-wrap span6">
											<option value="">请选择</option>
											<option value="1">通过</option>
											<option value="2">拒绝</option>
										</select>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group">
									<label class="control-label">贷款详细用途</label>
									<div class="controls">
										<textarea class="span9 m-wrap" rows="3" name="loanPurpose" maxlength="300">${dto.loanPurpose!}</textarea>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group">
									<label class="control-label">意见描述</label>
									<div class="controls">
										<textarea class="span9 m-wrap" rows="3" name="description" maxlength="300">${dto.description!}</textarea>
									</div>
								</div>
							</div>
							<input type="hidden" id="submitType" name="submitType" value="1" class="m-wrap large" />
						</form>
						<div class="form-actions">
							
							<input type="button" class="btn blue" onclick="addInfo(4)" data-type="1" value="保存" /> &nbsp;&nbsp;
							<input type="button" class="btn blue" onclick="save(event,this)" data-type="2" 	value="提交" /> &nbsp;&nbsp;
							<a href="javascript:page_back('${serverPath}/reception/list.htm')" class="btn">返回</a>
						</div>
					</div>
				</div>
			</div>
		</div>
</div>
<script src="${cdnPath}/js/reception/updateInfo.js?v=${VERSION_NO}"></script>
<script>
	function page_back(url) {
		window.location.href = url+"?"+ChkUtil.getCookie("queryParams");
	}
</script>
<#include "/sys/bottom.ftl" >