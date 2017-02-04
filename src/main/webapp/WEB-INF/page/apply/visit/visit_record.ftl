<#include "/sys/top.ftl"> <#include "/sys/left.ftl">
<div class="page-content">
	<!-- header -->
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<!--页面标题-->
				<h3 class="page-title"></h3>
				<!--面包屑导航-->
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="${ctx}/index.htm">首页</a> <i
						class="icon-angle-right"></i></li>
					<li><a href="#">分公司业务管理</a><i class="icon-angle-right"></i></li>
					<li><a href="#">外访管理</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="row-fluid">
				<div class="portlet box blue">
					<div class="portlet-title">
					
						<div class="caption">
							<i class="icon-reorder"></i>外访结果录入
						</div>
					</div>
					<div class="portlet-body form">

						<!-- edit form -->
						<form id="tab" action="#" method="POST"
							class="form-horizontal">
							<input type="hidden"  name="id" value="${(visit.id)!''}" />
							<input type="hidden"  name="applyLoanNo" value="${(visit.applyLoanNo)!''}" />
							<input type="hidden"  name="auditStage" value="${(visit.auditStage)!''}" />
							<div class="row-fluid">
								<div class="control-group span3 ">
									<label class="control-label">外访结论<span class="required">*</span></label>
									<div class="controls">
										<select name="state">
											<option value="3">通过</option>
											<option value="4">拒绝</option>
										</select>
									</div>
								</div>
							</div>

							<div class="row-fluid">
								<div class="control-group span9 ">
									<label class="control-label">备注<span class="required">*</span></label>
									<div class="controls">
										<textarea rows="3" name="remark" class="m-wrap span9" maxlength="200">${(visit.remark)!''}</textarea>
									</div>
								</div>
							</div>
						</form>
						<div class="form-actions">
						<#if (visit.state)?? && visit.state==2>	
							<input type="button" class="btn blue" onclick="upload(event)"
								value="附件上传" />						
							<input type="button" class="btn blue" onclick="save(event)"
								value="提交" /> 
						    <a href="javascript:page_back('${ctx}/visit/list.htm')" class="btn">返回</a>
						 <#else>
							<input type="button" class="btn blue" onclick="viewfile(event)"
								value="附件查看" />						
						    <a href="javascript:page_back('${ctx}/visit/list.htm')" class="btn">返回</a>					 
						 </#if>  
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
var state=${(visit.state)!0};
	$(function(){
		if(state!=2){
		$("select[name='state']").val(state);
		$("#tab input,select,textarea").attr("disabled","disabled");
		}
		
		$("#tab").validate({
			rules: {
		                remark: {
		                    required: true
		                }
		            },
			submitHandler: function(form){   //表单提交句柄,为一回调函数，带一个参数：form   
	         }
		});
	})
	function save(event) {
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
		if (!$("#tab").valid()) {
			return;
		}
		
		$.ajax({
				url : '${ctx}/productFile/checkFile.htm',
				type : 'post',
				data : {"applyLoanNo":$("input[name='applyLoanNo']").val(),"fileUpState":14},
				dataType : 'json',
				success : function(res) {
					if (!res.data) {
						BootstrapDialog.alert("请先上传外访附件");
						return false;
					}
					doSave();
				},
				error:function(){
					BootstrapDialog.alert("系统异常");
				}
			});
	
		
	}
	function doSave(){
		$.ajax({
				url : '${ctx}/visit/record.htm',
				type : 'post',
				data : $('#tab').serialize(),
				dataType : 'json',
				success : function(res) {
					if (res.code == 1) {
						BootstrapDialog.alert(res.message);
	
					} else {
						BootstrapDialog.alert("外访单提交成功",function(){
						page_back("${ctx}/visit/list.htm");
						});
					}
				},
				error:function(){
					BootstrapDialog.alert("系统异常");
				}
			});
	}
	function page_back(url) {
		window.location.href = url+"?"+ChkUtil.getCookie("queryParams");
	}
	function upload(event){
		ChkUtil.stopBubbleEvent(event);
		window.open("${ctx}/productFile/fileUpload.htm?applyLoanNo="+$("input[name='applyLoanNo']").val());
	}
	function viewfile(event){
		ChkUtil.stopBubbleEvent(event);
		window.open("${ctx}/productFile/fileUpload.htm?fileUpState=14&isView=0&applyLoanNo="+$("input[name='applyLoanNo']").val());
	}	
	function checkFile(){
		
	}
</script>