<#include "/sys/top.ftl"> <#include "/sys/left.ftl">
<div class="page-content">
	<!-- header -->
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<!--页面标题-->
				<h3 class="page-title">
				</h3>
				<!--面包屑导航-->
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="${ctx}/index.htm">首页</a> <i
						class="icon-angle-right"></i></li>
					<li><a href="#">风险管理</a><i class="icon-angle-right"></i></li>
					<li><a href="#"><#if dto??>内部黑名单详情<#else>新增内部黑名单</#if></a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="row-fluid">
				<div class="portlet box blue">
					<div class="portlet-title">
						<div class="caption">
							<i class="icon-reorder"></i>客户黑名单资料
						</div>
					</div>
					<div class="portlet-body form">

						<!-- edit form -->
						<form id="tab" action="${ctx}/innerBlack/add.htm" method="POST"
							class="form-horizontal">
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">姓名<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="memberName" class="m-wrap large" maxlength="20"/>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">有无子女</label>
									<div class="controls">
										<select name="hasChildren" class="m-wrap large">
											<option value="0">无</option>
											<option value="1">有</option>
										</select>
									</div>
								</div>
							</div>

							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">性别</label>
									<div class="controls">
										<select name="sex" class="m-wrap large">
											<option value="0">男</option>
											<option value="1">女</option>
										</select>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">学历</label>
									<div class="controls">
										<select name="education" class="m-wrap large">
											<option value="初中及以下">初中及以下</option>
											<option value="高中">高中</option>
											<option value="中技">中技</option>
											<option value="中专">中专</option>
											<option value="大专">大专</option>
											<option value="本科">本科</option>
											<option value="硕士">硕士</option>
											<option value="博士">博士</option>
										</select>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">身份证号<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="idCard" class="m-wrap large" maxlength="18"/>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">民族</label>
									<div class="controls">
										<input type="text" name="nation" class="m-wrap large" maxlength="20"/>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">手机号码</label>
									<div class="controls">
										<input type="text" name="phone" class="m-wrap large" maxlength="20"/>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">QQ/微信</label>
									<div class="controls">
										<input type="text" name="snsAccount" class="m-wrap large" maxlength="50"/>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">户籍地址</label>
									<div class="controls">
										<input type="text" name="registdAddr" class="m-wrap large" maxlength="200"/>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">现住址</label>
									<div class="controls">
										<input type="text" name="currentAddr" class="m-wrap large" maxlength="200"/>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">居住属性</label>
									<div class="controls">
										<select name="liveProperty" class="m-wrap large">
											<option value="自有房产">自有房产</option>
											<option value="租房居住">租房居住</option>
										</select>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">是否有逾期</label>
									<div class="controls">
										<select name="hasOverdue" class="m-wrap large">
											<option value="0">无</option>
											<option value="1">有</option>
										</select>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">婚姻状况</label>
									<div class="controls">
										<select name="maritalStatus" class="m-wrap large">
											<option value="0">未婚</option>
											<option value="1">已婚</option>
											<option value="2">离异</option>
											<option value="3">丧偶</option>
										</select>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">逾期天数</label>
									<div class="controls">
										<input type="text" name="overdueDays" class="m-wrap large" maxlength="4"/>
									</div>
								</div>
							</div>
							<hr>
							<div>
								<span class="span1"></span>
								<h4>
									<strong>工作信息</strong>
								</h4>
							</div>
							<hr>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">工作单位全称</label>
									<div class="controls">
										<input type="text" name="workUnitName" class="m-wrap large" maxlength="200"/>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">单位地址</label>
									<div class="controls">
										<input type="text" name="workUnitAddr" class="m-wrap large" maxlength="200"/>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">单位电话</label>
									<div class="controls">
										<input type="text" name="workUnitTel" class="m-wrap large" maxlength="20"/>
									</div>
								</div>
							</div>
						</form>
						<div class="form-actions">
							<input type="button" class="btn" onclick="back(event)"
								value="返回" /> 	
							<input type="button" id="subBtn" class="btn blue" onclick="save(event)"
								value="提交" />								
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
	
	function save(event) {
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
	    $("#tab").submit();
	}
	function back(event) {
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
		var backurl="${ctx}/innerBlack/list.htm?"+ChkUtil.getCookie("queryParams");
		window.location.href = backurl;
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
	<#if dto??>
	$(function(){
	$("#tab select").attr("disabled","disabled");
	$("#tab input").attr("readonly","readonly");
	$("#subBtn").hide();
	fillObj();
	})
	<#else>
	$(function(){
		$("form").validate({
			rules: {
			            memberName: {
		                    required: true,
		                    maxlength:20,
		                },
		                idCard: {
		                    required: true,
		                    idCard:true,
		                    remote:{
			                    url:'${ctx}/innerBlack/checkIdCardExist.htm',
			                    type : 'post',
			                    dataType : 'json',
			                    data : {
									"idCard" :function(){return $("input[name='idCard']").val()}
								}
		                    }
		                },
		                phone:{
		                	phone:true
		                },
		                overdueDays:{
		                	number:true
		                }
		            },
		    messages:{
					    idCard:{
					    	remote:"身份证已存在"
					    }
		    },
			submitHandler: function(form){   //表单提交句柄,为一回调函数，带一个参数：form   
	                    form.submit();   //提交表单   
	         }
		});
	});
	</#if>
</script>