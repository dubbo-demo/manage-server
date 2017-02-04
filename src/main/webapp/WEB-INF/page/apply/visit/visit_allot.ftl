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
							<i class="icon-reorder"></i>分配外访
						</div>
					</div>
					<div class="portlet-body form">

						<!-- edit form -->
						<form id="tab" action="#" method="POST"
							class="form-horizontal">
							<input type="hidden"  name="id" value='${(visit.id)!''}' />
							<input type="hidden"  name="applyLoanNo" value="${(visit.applyLoanNo)!''}" />
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">外访日期<span class="required">*</span></label>
									<div class="controls">
										<div class="input-append date date-picker" data-date="${.now?string("yyyy-MM-dd")}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
											<input name="finishTime" class="m-wrap span8 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${.now?string("yyyy-MM-dd")}"/><span class="add-on"><i class="icon-calendar"></i></span>
										</div>	
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">外访人<span class="required">*</span></label>
									<div class="controls">
										<input type="hidden" id='visitUser' name="visitUser" class="m-wrap span8"/>
										<input type="hidden" id='visitUserName' name="visitUserName" />
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">申请单编号</label>
									<div class="controls">
										<span class="text">${(visit.applyLoanNo)!''}</span>
									</div>
								</div>								
							</div>

							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">客户姓名</label>
									<div class="controls">
										<span class="text">${(visit.memberName)!''}</span>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">身份证</label>
									<div class="controls">
										<span class="text">${(visit.idCard)!''}</span>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">性别</label>
									<div class="controls">
										<span class="text">
										<#if visit.sex??>
											<#if visit.sex==0>
												男
											<#elseif visit.sex==1>
												女
											</#if>
										</#if>
										</span>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">手机号码</label>
									<div class="controls">
										<span class="text">${(visit.phone)!''}</span>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">住宅详细地址</label>
									<div class="controls">
										<span class="text">${(visit.addr)!''}</span>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">单位详细地址</label>
									<div class="controls">
										<span class="text">${(visit.workAddr)!''}</span>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">单位电话</label>
									<div class="controls">
										<span class="text">${(visit.unitTelephone)!''}&nbsp;分机号&nbsp;${(visit.extensionNumber)!''}</span>
									</div>
								</div>
							</div>							
							<hr>
							<div class="row-fluid">
								<div class="control-group span9 ">
									<label class="control-label">信审外访要求</label>
									<div class="controls">
										<textarea rows="3" readonly="readonly" class="m-wrap span9">${(visit.requirement)!''}</textarea>
									</div>
								</div>
							</div>
						</form>
						<div class="form-actions">
							<input type="button" class="btn blue" onclick="print(event)"
								value="打印" />						
							<input type="button" class="btn blue" onclick="allot(event)"
								value="分配" /> 
						    <a href="javascript:page_back('${ctx}/visit/list.htm')" class="btn">返回</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
	var hasprint=false;
	$(function(){
		var custmerurl = serverPath+"/employee/showVisitEmpoyee.htm";
		$("#visitUser").select2({
		                placeholder: ChkUtil.select2Name,
		                allowClear: true,
		                minimumInputLength: 1,
		                query: function (query) {
		                	$.getJSON(custmerurl, {"nameSpell" : query.term}, function(result) {
		                		var data = {
		                            results: result.data
		                        };
		                		query.callback(data);
		        			});
		                }
		 }).on("change",function(e){
			 var select_text="";
			 if(e.val){
			 	select_text=$("#visitUser").select2("data").text;
			 }	
			 $("#visitUserName").val(select_text);
		 }); 
		 
		 $("#tab").validate({
		 	ignore:"",
			rules: {
		                visitUser: {
		                    required: true
		                }
		            },
			submitHandler: function(form){   //表单提交句柄,为一回调函数，带一个参数：form   
	         }
		});
	});
	
	function print(event){
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
		if (!$("#tab").valid()) {
		return;
		}
		
		window.open("${ctx}/visit/print.htm?"+$('#tab').serialize());
		hasprint=true;
	
	}
	
	function page_back(url) {
		window.location.href = url+"?"+ChkUtil.getCookie("queryParams");
	}
	function allot(event) {
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
		if (!$("#tab").valid()) {
			return;
		}
		if(!hasprint){
			BootstrapDialog.alert("请先打印外访单");
			return false;
		}
		$.ajax({
				url : '${ctx}/visit/allot.htm',
				type : 'post',
				data : $('#tab').serialize(),
				dataType : 'json',
				success : function(res) {
					if (res.code == 1) {
						BootstrapDialog.alert(res.message);
	
					} else {
						BootstrapDialog.alert("分配外访成功",function(){
							page_back("${ctx}/visit/list.htm");
						});
					}
				},
				error:function(){
					BootstrapDialog.alert("系统异常");
				}
			});
	}
	
	function fillObj(){
	    var obj=JSON.parse('${dto!""}');
		for(var attr in obj){
	              var $input = $("#tab input[name='"+attr+"']");
	              var type = $input.attr("type");                
	              if(type=="checkbox" ||type=="radio"){
	                  var avalues = obj[attr].split(",");
	                  for(var v=0; v<avalues.length;v++){
	                    $input.each(function(i,n){
	                        var value = $(n).val();                        
	                        if(value == avalues[v]){                        
	                          $(n).attr("checked","checked");
	                        }
	                    });
	                }
	              }
	              else{
	                $input.val(obj[attr]);
	              }
	              var $select = $("#tab select[name='"+attr+"']");
	              if($select.length>0){
	             	 $select.val(obj[attr]);
	              }
	                
	     }            
	}
</script>