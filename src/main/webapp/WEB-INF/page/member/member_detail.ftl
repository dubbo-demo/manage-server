<#include "/sys/top.ftl"> <#include "/sys/left.ftl">
<div class="page-content">
	<input type="hidden" name="subState" value="${subState!}">
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
					<li><a href="#">客户管理</a><i class="icon-angle-right"></i></li>
					<li><a href="#">客户信息管理</a></li>
				</ul>
			</div>
		</div>
		<div>
		<div class="row-fluid">
			<div class="portlet box">
				<div class="portlet-body form">
						<hr>
						<div>
							<h4 style="text-align:center" data-ftl="tab_userinfo">
								<strong>客户信息</strong>
							</h4>
						</div>
						<hr>
						<!-- 客户信息 form begin -->
						<form id="tab_userinfo" action="#" method="POST"
							class="form-horizontal">
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">客户姓名<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="memberName" value="${(memberInfo.memberName)!}" class="m-wrap span9" maxlength="20"/>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">姓名拼音<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="nameSpell" value="${(memberInfo.nameSpell)!}" class="m-wrap span9" maxlength="20"/>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">身份证号<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="idCarNo" value="${(memberInfo.idCarNo)!}" id="idCarNo" class="m-wrap span9" maxlength="18"/>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">性别</label>
									<div class="controls">
										<select name="sex" class="m-wrap span9">
											<option value="0">男</option>
											<option value="1">女</option>
										</select>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">学历</label>
									<div class="controls">
										<select name="eduCode" class="m-wrap span9">
											<option value="1">初中及以下</option>
											<option value="2">高中</option>
											<option value="3">中技</option>
											<option value="4">中专</option>
											<option value="5">大专</option>
											<option value="6">本科</option>
											<option value="7">硕士</option>
											<option value="8">博士</option>
										</select>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">民族</label>
									<div class="controls">
										<input type="text" name="nation" 
										id="nation" value="${(memberInfo.nation)!}" class="m-wrap span9" maxlength="20"/>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">手机号码</label>
									<div class="controls">
										<input type="text" name="phone" value="${memberInfo.phone!''}" class="m-wrap span9" maxlength="20"/>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">QQ/微信</label>
									<div class="controls">
										<input type="text" name="SNSAccount" 
										id="SNSAccount" value="${memberInfo.SNSAccount!}" class="m-wrap span9" maxlength="50"/>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">电子邮箱</label>
									<div class="controls">
										<input type="text" name="email" 
										id="email" value="${memberInfo.email!}" class="m-wrap span9" maxlength="30"/>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">户籍地址</label>
									<div class="controls">
										<input type="text" name="censusAddr" id="censusAddr" value="${memberInfo.censusAddr!}" class="m-wrap span9" maxlength="200"/>
										<input type="hidden" name="censusProvcode" id="censusProvcode" 
										class="m-wrap large" value="${memberInfo.censusProvcode!}" />
										<input type="hidden" name="censusCitycode" id="censusCitycode" 
										class="m-wrap large" value="${memberInfo.censusCitycode!}" />
										<input type="hidden" name="censusTowncode" id="censusTowncode" 
										class="m-wrap large" value="${memberInfo.censusTowncode!}" />
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">详细户籍地址</label>
									<div class="controls">
										<input type="text" name="censusAddress" 
										value="${memberInfo.censusAddress!}" class="m-wrap span9" maxlength="200"/>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">邮寄地址</label>
									<div class="controls">
										<select name="mailAddress" id="mailAddress" class="m-wrap span9">
											<option value="1">家庭地址</option>
											<option value="2">公司地址</option>
											<option value="3">户籍地址</option>
										</select>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">现住址</label>
									<div class="controls">
										<input type="text" name="liveAddr" id="liveAddr" value="${memberInfo.liveAddr!}"  class="m-wrap span9" maxlength="200"/>
										<input type="hidden" name="liveProvcode" id="censusProvcode" class="m-wrap" 
										value="${memberInfo.liveProvcode!}"/>
										<input type="hidden" name="liveCitycode" id="censusCitycode" class="m-wrap" 
										value="${memberInfo.liveCitycode!}" />
										<input type="hidden" name="liveTowncode" id="censusTowncode" class="m-wrap" 
										value="${memberInfo.liveTowncode!}" />
									</div>
								</div>
								
								<div class="control-group span4 ">
									<label class="control-label">详细现住址</label>
									<div class="controls">
										<input type="text" name="liveAddress" 
										value="${memberInfo.liveAddress!}" class="m-wrap span9" maxlength="200"/>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">现住址居住时间</label>
									<div class="controls">
										<input type="text" name="liveYears" value="${memberInfo.liveYears!}" class="m-wrap span9" maxlength="200"/>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">居住属性</label>
									<div class="controls">
										<select name="liveProperty" class="m-wrap span9">
											<option value="1">无按揭自置</option>
											<option value="2">有按揭自置</option>
											<option value="3">亲属产权</option>
											<option value="4">单位宿舍</option>
											<option value="5">租房居住</option>
											<option value="6">自建住房</option>
										</select>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">资质分类</label>
									<div class="controls">
										<select name="qualifiType" class="m-wrap span9">
											<option value="1">A类客户</option>
											<option value="2">B类客户</option>
											<option value="3">C类客户</option>
											<option value="4">D类客户</option>
										</select>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">婚姻状况</label>
									<div class="controls">
										<select name="maritalStatus" class="m-wrap span9">
											<option value="1">未婚</option>
											<option value="2">已婚</option>
											<option value="3">离异</option>
											<option value="4">丧偶</option>
											<option value="5">再婚</option>
											<option value="6">复婚</option>
										</select>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="control-label">有无子女</label>
									<div class="controls">
										<select name="haveChild" class="m-wrap span9">
											<option value="1">有</option>
											<option value="0">无</option>
										</select>
									</div>
								</div>
								<div class="control-group span4 ">
									<label class="control-label">其他借款</label>
									<div class="controls">
										<input type="text" name="haveLoan"  value="${memberInfo.haveLoan!}" class="m-wrap span9" maxlength="200"/>
									</div>
								</div>
								
							</div>
							<input type="hidden" name="type" id="submitType" value="1" />
						</form>
						<!-- 客户信息 form end -->
						
						<!-- 工作信息 form start-->
						<hr>
						<div>
							
							<h4 style="text-align:center"  data-ftl="tab_job_info_input">
								<strong>工作信息</strong>
							</h4>
						</div>
						<hr>
					    <#include "/apply/job_info_input.ftl" >
						<!-- 工作信息 form end-->
						
						<!-- 联系人信息 form start-->
						<hr>
						<div>
							
							<h4 style="text-align:center"  data-ftl="tab_contacts_input">
								<strong>联系人信息</strong>
							</h4>
						</div>
						<hr>
						<#include "/apply/contacts_input.ftl" >
						<!-- 联系人信息 form end-->
						
						<!-- 个人资产 form start-->
						<hr>
						<div>
							
							<h4 style="text-align:center" data-ftl="tab_personassets">
								<strong>个人资产信息</strong>
							</h4>
						</div>
						<hr>
						<#include "/apply/personassets.ftl" >
						<!-- 个人资产 form end-->
						
						<!-- 禁闭信息 form start-->
						<hr>
						<div>
							
							<h4 style="text-align:center" data-ftl="tab_personassets">
								<strong>禁闭信息</strong>
							</h4>
						</div>
						<hr>
						<form id="confineForm" action="#" method="POST"	class="form-horizontal">
						<#if confineLogs?? && confineLogs?size gt 0>
						<#list confineLogs as confine>
						<div class="row-fluid">
							<div class="control-group span4">
								<label class="control-label">禁闭期起止日期</label>
								<div class="controls">
									<div class="input-append date date-picker">
										<input  class="m-wrap span8 date-picker" size="16" type="text"  value="${(confine.beginTime?string('yyyy-MM-dd'))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
									</div>
									<span style="margin-left:-28px">--</span>
									<div class="input-append date date-picker">
										<input  class="m-wrap span8 date-picker" size="16" type="text"   value="${(confine.endTime?string('yyyy-MM-dd'))!}" /><span class="add-on"><i class="icon-calendar"></i></span>
									</div>
								</div>
							</div>
							<div class="control-group span4">
								<label class="control-label">禁闭期时长</label>
								<div class="controls">
									<input class="m-wrap span2" type="text" readonly value="<#if confine.confineDays==-1>永久<#else>${(confine.confineDays)!}</#if>">
									<label class="help-inline" style="color:#000">天&nbsp;&nbsp状态:</label><label class="help-inline" style="color:red"><#if confine.state==0>生效<#else>过期</#if></label>
								</div>
							</div>
							<div class="control-group span4">
								<label class="control-label">拒绝单号</label>
								<div class="controls">
									<input class="m-wrap" type="text" readonly value="${(confine.applyLoanNo)!}">
								</div>
							</div>
						</div>
						</#list>
						<#else>
							<label style="text-align:center">无</label>
						</#if>
						</form>
						<!-- 禁闭信息 form end-->
						</div>
				</div>
		</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
	$(function(){
		$("input[type='input']").attr("readonly","readonly");
		$("input[type='text']").attr("readonly","readonly");
		$("input[type='number']").attr("readonly","readonly");
		$("textarea").attr("readonly","readonly");
		$("input[type='checkbox']").attr("disabled","disabled");
		$("select").attr("disabled","disabled");
		$("input[type='input']").unbind();
		$("input[type='text']").unbind();
		$(".add-on").unbind();
		$(".required").html("");
		
	});


	$(function(){
		$("select[name='sex']").val(${memberInfo.sex!});
		$("select[name='eduCode']").val(${memberInfo.eduCode!});
		$("select[name='mailAddress']").val(${memberInfo.mailAddress!});
		$("select[name='liveProperty']").val(${memberInfo.liveProperty!});
		$("select[name='qualifiType']").val(${memberInfo.qualifiType!});
		$("select[name='maritalStatus']").val(${memberInfo.maritalStatus!});
		$("select[name='haveChild']").val(${memberInfo.haveChild!});
	});
	function page_back(url) {
		window.location.href = url+"?"+ChkUtil.getCookie("queryParams");
	};
	
</script>