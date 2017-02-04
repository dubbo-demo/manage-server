<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<link rel="stylesheet" type="text/css" href="${cdnPath}/common/bootstrap/media/css/select2_metro.css" />
<script type="text/javascript" src="${cdnPath}/common/bootstrap/media/js/select2.min.js"></script>
<script src="${cdnPath}/js/reception/addInfo.js?v=${VERSION_NO}"></script>
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
								<a href="${serverPath}/team/list.html">申请件管理</a><i class="icon-angle-right"></i>
							</li>
							<li>
									<a href="javascript:void(0)">分公司业务管理</a><i class="icon-angle-right"></i>
								</li>
							<li>
								<a href="#">新增接待信息</a>
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
						<form id="tab" action="${serverPath}/innerBlack/add.htm" method="POST"
							class="form-horizontal">
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">客户姓名<span class="required">*</span></label>
									<div class="controls">
										<input type="text" maxlength="6" name="memberName" class="m-wrap span6" />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">手机号码<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="phone"  maxlength="11" class="m-wrap span6" />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">身份证号<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="idCard" id="idCard" class="m-wrap span6" />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">业务经理<span class="required">*</span></label>
									<div class="controls">
										<input type="hidden" id='bmId' name="bmId" class="m-wrap span6" />
										<input type="hidden" id='bmName' name="bmName" />
									</div>
								</div>
							</div>
							<#-- <div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">客服<span class="required">*</span></label>
									<div class="controls">
										<input type="hidden" id='cusomerServiceId' name="cusomerServiceId" class="m-wrap large" />
									</div>
								</div>
							</div> -->
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">客户类型<span class="required">*</span></label>
									<div class="controls">
										<select name="memberType" class="m-wrap span6">
											<option value="">请选择</option>
											<option value="0">工薪族</option>
											<option value="1">企业主</option>
										</select>
									</div>
								</div>
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
										<select name="infoChannel" class="m-wrap span6">
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
										<select name="loanUse" class="m-wrap span6">
										</select>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">可承受期还款额<span class="required">*</span></label>
									<div class="controls">
										<input type="hidden" name="affordMonthRepay" 
										 id="affordMonthRepay" value="" class="m-wrap large" />
										<input type="text" name="affordMonthRepay1"
										onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" 
										id="affordMonthRepay1" class="m-wrap " style="width:100px;"/>
										<span style="line-height:20px;display:lin" >__</span>
										<input type="text" name="affordMonthRepay2" id="affordMonthRepay2"
										onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" class="m-wrap " style="width:100px;"/>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">产品类型<span class="required">*</span></label>
									<div class="controls">
										<select name="prodType" id="prodType" class="m-wrap span6" onchange="getProduct(this)">
										</select>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">贷款期数<span class="required">*</span></label>
									<div class="controls">
										<input type="hidden" name="loanPeriods" id="loanPeriods"/>
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
										<input type="text" name="applyMoney"  maxlength="8"
										onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  class="m-wrap span6" />
									</div>
								</div>
							</div>
							<#--<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">期望获得贷款额<span class="required">*</span></label>
									<div class="controls">
										<input type="hidden" name="expectLoanAmount" id="expectLoanAmount" value="" class="m-wrap large" />
										<input type="text" name="expectLoanAmount1" id="expectLoanAmount1" 
										onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" class="m-wrap " style="width:100px;"/>
										——<input type="text" name="expectLoanAmount2" id="expectLoanAmount2" 
										onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" class="m-wrap " style="width:100px;"/>
									</div>
								</div>
							</div>-->
							<hr style="margin:5px 0px 10px 0px;">
							<div>
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
										<textarea class="span9 m-wrap" rows="3" name="loanPurpose" maxlength="300"></textarea>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group">
									<label class="control-label">意见描述</label>
									<div class="controls">
										<textarea class="span9 m-wrap" rows="3" name="description" maxlength="300"></textarea>
									</div>
								</div>
							</div>
							<input type="hidden" id="submitType" name="submitType" value="2" class="m-wrap large" />
						</form>
						<div class="form-actions">
							
							<input type="button" class="btn blue" onclick="addInfo(3)" data-type="2" value="保存" /> &nbsp;&nbsp;
							<input type="button" class="btn blue" onclick="save(event,this)" data-type="1" 	value="提交" /> &nbsp;&nbsp;
							<a href="javascript:page_back('${serverPath}/reception/list.htm')" class="btn">返回</a>
						</div>
					</div>
				</div>
			</div>
		</div>
</div>
<script>
	function page_back(url) {
		window.location.href = url+"?"+ChkUtil.getCookie("queryParams");
	}
</script>
<#include "/sys/bottom.ftl" >