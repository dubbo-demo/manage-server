<#include "/sys/top.ftl"> <#include "/sys/left.ftl">
<script>
	var applyLoanNo =  "${applyLoanNo}";
	<#list constant as constantItem>
		var ${constantItem} = ${constantItem.getCode()};
	</#list>
    var state = "${applyInfo.subState!}"; 
</script>
<script src="${cdnPath}/js/apply/newInfoIndex.js"></script>
<script src="${cdnPath}/js/apply/personassets.js"></script>
<script src="${cdnPath}/js/apply/compositeOpinion.js"></script>
<script src="${cdnPath}/js/apply/jobinfoInput.js"></script>
<script src="${cdnPath}/js/apply/contactsInput.js"></script>
<div class="page-content">
	<!-- BEGIN PAGE CONTAINER-->

	<div class="container-fluid">

		<!-- BEGIN PAGE HEADER-->

		<div class="row-fluid">

			<div class="span12">


				<h3 class="page-title">

					 申请件管理 

				</h3>

				<ul class="breadcrumb">

					<li><i class="icon-home"></i> <a href="${serverPath}/index.htm">首页</a> <span
						class="icon-angle-right"></span></li>

					<li><a href="#">分公司业务管理</a> <span class="icon-angle-right"></span>

					</li>

					<li><a href="#">申请件管理</a></li>

				</ul>

			</div>

		</div>

		<!-- END PAGE HEADER-->

		<!-- BEGIN PAGE CONTENT-->

		<div class="row-fluid">

			<div class="span12">

				<div class="portlet box blue" id="form_wizard_1">

					<div class="portlet-title">

						<div class="caption">

							<i class="icon-reorder"></i>  <span id="box_title">申请件管理 </span>
						</div>
						
					</div>

					<div class="portlet-body form">

							<div class="form-wizard">

								<div class="navbar steps">

									<div class="navbar-inner">

										<ul class="row-fluid">

											<li class="span" style="width:17%" state="1001"><a href="${serverPath}/apply/member/userInfoForm.htm" ajax-recall="" next-onclick="submitUserInfo" ajax="true" save='saveUserInfo' data-target="#tab2" data-toggle="tab"
												class="step"> <span class="number">1</span> <span
													class="desc"><i class="icon-ok"></i> 个人信息录入</span>

											</a></li>

											<li class="span" style="width:17%" state="1002"><a href="${serverPath}/apply/newInfoIndex/jobinfoinput.htm" ajax-recall="jobInfoInputInit" next-onclick="jobInfoInputNext" ajax="true" save='jobInfoSave' data-target="#tab3" data-toggle="tab"
												class="step"> <span class="number">2</span> <span
													class="desc"><i class="icon-ok"></i>工作信息录入</span>

											</a></li>

											<li class="span" style="width:17%" state="1007"><a href="${serverPath}/apply/newInfoIndex/contactsinput.htm" ajax-recall="contactsInputInit" next-onclick="contactsInputNext" ajax="true" save='contactsSave' data-target="#tab4" data-toggle="tab"
												class="step"> <span class="number">3</span> <span
													class="desc"><i class="icon-ok"></i>联系人信息录入</span>

											</a></li>
											
											<li class="span" style="width:17%" state="1003"><a href="${serverPath}/apply/newInfoIndex/personassets.htm" ajax-recall="personassetsInit" next-onclick="personassetsNext" ajax="true" save='personassetsSave' data-target="#tab5" data-toggle="tab"
												class="step"> <span class="number">4</span> <span
													class="desc"><i class="icon-ok"></i>个人资产信息录入</span>

											</a></li>
											<li class="span" style="width:17%" state="1004"><a href="${serverPath}/apply/newInfoIndex/compositeOpinion.htm" ajax-recall="compositeOpinionInit" next-onclick="compositeOpinionNext" ajax="true" save='compositeOpinionSave' data-target="#tab6" data-toggle="tab"
												class="step"> <span class="number">5</span> <span
													class="desc"><i class="icon-ok"></i>综合意见</span>

											</a></li>

										</ul>

									</div>

								</div>
								<div class="tab-content">

									<div class="alert alert-error hide">

										<button class="close" data-dismiss="alert"></button>

										You have some form errors. Please check below.

									</div>

									<div class="alert alert-success hide">

										<button class="close" data-dismiss="alert"></button>

										Your form validation is successful!

									</div>

									<!--<div class="tab-pane active" id="tab1">

										
									</div>-->

									<div class="tab-pane" id="tab2">


									</div>

									<div class="tab-pane" id="tab3">

										
									</div>

									<div class="tab-pane" id="tab4">
									

									</div>

									<div class="tab-pane" id="tab5">
								

									</div>

									<div class="tab-pane" id="tab6">
									
									</div>

								</div>

								<div class="form-actions clearfix">

									<a href="javascript:save();" class="btn save"> <i
										class="m-icon-swapleft"></i> 保存

									</a> 
									<a href="${serverPath}/productFile/fileUpload.htm?applyLoanNo=${applyLoanNo}" class="btn attachment" style="display: none;" target="_blank"> <i
										class="m-icon-swapleft" ></i> 管理附件

									</a> 
									<a href="javascript:;" class="btn button-previous"> <i
										class="m-icon-swapleft"></i> 上一步

									</a> 
									
									<a href="javascript:;" class="btn blue button-next">

										下一步 <i class="m-icon-swapright m-icon-white"></i>

									</a> 
									<a href="javascript:;" class="btn green button-submit">

										提交 <i class="m-icon-swapright m-icon-white"></i>

									</a>
									
									<a href="javascript:;" class="btn button-back"> <i
										class="m-icon-swapleft"></i> 返回

									</a>

								</div>

							</div>

					</div>

				</div>

			</div>

		</div>

		<!-- END PAGE CONTENT-->

	</div>
</div>
<style>

.tab-content {
   
    overflow: visible;
}
</style>

<!-- END PAGE CONTAINER-->
<#include "/sys/bottom.ftl">
<script>
 FormWizard.init();
 </script>