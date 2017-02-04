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
								<a href="#">接待分配</a>
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
							<i class="icon-reorder"></i>分配单
						</div>
					</div>
					<div class="portlet-body form">

						<!-- edit form -->
						<form id="tab" action="${serverPath}/reception/listManage.htm" method="POST"
							class="form-horizontal">
							<input type="hidden" id="id" name="id" value="${dto.id!}">
							<input type="hidden" id="cityId" name="cityId" value="${dto.cityId!}">
							<input type="hidden" id="storeId" name="storeId" value="${dto.storeId!}">
							<input type="hidden" id="areaId" name="areaId" value="${dto.areaId!}">
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">申请单编号</label>
									<div class="controls">
										<input type="text" name="applyLoanNo" readonly value="${dto.applyLoanNo!}" class="m-wrap span6" />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">手机号码</label>
									<div class="controls">
										<input type="text" readonly name="phone" value="${dto.phone!}"  
										 maxlength="11" class="m-wrap span6" />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">身份证号</label>
									<div class="controls">
										<input type="text" name="idCard" id="idCard" 
										readonly value="${dto.idCard!}"  class="m-wrap span6" />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">客户姓名</label>
									<div class="controls">
										<input type="text" maxlength="6" name="memberName" 
										readonly value="${dto.memberName!}" class="m-wrap span6" />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">渠道</label>
									<div class="controls">
										<#if dto.clientType??>
											<#if dto.clientType == 0>
												<input type="text" maxlength="6" 
														readonly value="线下" class="m-wrap span6" />
											<#else>
												<input type="text" maxlength="6" 
														readonly value="app" class="m-wrap span6" />
											</#if>
										<#else>
											<input type="text" maxlength="6" 
														readonly value="线下" class="m-wrap span6" />
										</#if>
										
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">创建时间</label>
									<div class="controls">
										<input type="text" maxlength="6"  
										readonly value="${dto.createTime?datetime}" class="m-wrap span6" />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">所属客服</label>
									<div class="controls">
										<input type="text" maxlength="6" name="memberName" 
										readonly value="${dto.customerSName!}" class="m-wrap span6" />
										<input type="hidden" id="oldCustomerServiceId" name="oldCustomerServiceId" value="${dto.customerServiceId!}">
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">分配客服<span class="required">*</span></label>
									<div class="controls">
										<input type="hidden" id='newCustomerServiceId' name="newCustomerServiceId" class="m-wrap span6" />
										<input type="hidden" id="newCustomerSName" name="newCustomerSName">
									</div>
								</div>
							</div>
							<input type="hidden" id="submitType" name="submitType" value="1" class="m-wrap large" />
						</form>
						<div class="form-actions">
							<a href="javascript:page_back('${serverPath}/receptionManage/list.htm')" class="btn">返回</a>
							<input type="button" class="btn blue" onclick="save(event,this)" value="提交" /> &nbsp;&nbsp;
						</div>
					</div>
				</div>
			</div>
		</div>
</div>
<script src="${cdnPath}/js/reception/allot.js?v=${VERSION_NO}"></script>
<script>
	function page_back(url) {
		window.location.href = url+"?"+ChkUtil.getCookie("queryParams");
	}
</script>
<#include "/sys/bottom.ftl" >