<script src="${cdnPath}/js/apply/userinfo/userInfo.js?v=${VERSION_NO}"></script>
<script src="${cdnPath}/js/apply/userinfo/userInfo_add.js?v=${VERSION_NO}"></script>
<script>
 function clickModel(e) {
 	$(e).modal();
 }
</script>

					<div class="portlet-body form">

						<!-- edit form -->
						<form id="tab_userinfo" action="#" method="POST"
							class="form-horizontal">
							<input type="hidden" name="applyLoanNo" value="${apply.applyLoanNo!''}" />
							<input type="hidden" id='userinfo_ftl' name="userinfo_ftl" value="0" />
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">业务经理<span class="required">*</span></label>
									<div class="controls">
										<input type="hidden" id='bmId'  value="${apply.bmId!''}"  name="bmId" class="m-wrap large" />
										<input type="hidden" id='bmName' name="bmName" value="${apply.bmName!'请选择'}" class="m-wrap large" />
                                        <input type="input" id='teamManager' name="teamManager" value="" style="width:0px;height:0px;" />
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">客服<span class="required">*</span></label>
									<div class="controls">
										<input type="hidden" id='customerServiceId' readonly  name="customerServiceId" value="${apply.customerServiceId!''}" class="m-wrap large" />
										<input type="text" id='customerSName' readonly value="${apply.customerSName!'请选择'}" name="customerSName" class="m-wrap large" />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">客户姓名<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="memberName" readonly value="${apply.memberName!''}" class="m-wrap large" maxlength="20"/>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">姓名拼音<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="nameSpell" readonly value="${apply.nameSpell!''}" class="m-wrap large" maxlength="20"/>
									</div>
								</div>
							</div>

							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">性别<span class="required">*</span></label>
									<div class="controls">
										<select name="sex" class="m-wrap large">
											<option value="">请选择</option>
											<option value="0">男</option>
											<option value="1">女</option>
										</select>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">学历<span class="required">*</span></label>
									<div class="controls">
										<select name="eduCode" class="m-wrap large">
											<option value="">请选择</option>
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
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">身份证号<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="idCarNo" readonly value="${apply.idCard!''}" id="idCard" class="m-wrap large" maxlength="18"/>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">民族<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="nation" class="m-wrap large" maxlength="20"/>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">手机号码<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="phone" readonly value="${apply.phone!''}" class="m-wrap large" maxlength="20"/>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">QQ/微信</label>
									<div class="controls">
										<input type="text" name="SNSAccount" class="m-wrap large" maxlength="50"/>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">户籍地址<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="censusAddr" id="censusAddr" placeholder = "请选择" readonly class="m-wrap large" maxlength="200"/>
										<input type="hidden" name="censusProvcode" id="censusProvcode" class="m-wrap large" value="0" />
										<input type="hidden" name="censusCitycode" id="censusCitycode" class="m-wrap large" value="0" />
										<input type="hidden" name="censusTowncode" id="censusTowncode" class="m-wrap large" value="0" />
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">现住址<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="liveAddr" id="liveAddr" placeholder = "请选择" readonly class="m-wrap large" maxlength="200"/>
										<input type="hidden" name="liveProvcode" id="liveProvcode" class="m-wrap large" value="0" />
										<input type="hidden" name="liveCitycode" id="liveCitycode" class="m-wrap large" value="0" />
										<input type="hidden" name="liveTowncode" id="liveTowncode" class="m-wrap large" value="0" />
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">详细户籍地址<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="censusAddress" class="m-wrap large" maxlength="200"/>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">详细现住址<span class="required">*</span></label>
									<div class="controls">
										<input type="text" name="liveAddress" class="m-wrap large" maxlength="200"/>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">邮寄地址<span class="required">*</span></label>
									<div class="controls">
										<select name="mailAddress" id="mailAddress" class="m-wrap large">
											<option value="">请选择</option>
											<option value="1">现住址</option>
											<option value="2">公司地址</option>
											<option value="3">户籍地址</option>
										</select>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">电子邮箱</label>
									<div class="controls">
										<input type="text" name="email" class="m-wrap large" maxlength="200"/>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">居住属性<span class="required">*</span></label>
									<div class="controls">
										<select name="liveProperty" class="m-wrap large">
											<option value="">请选择</option>
											<option value="1">无按揭自置</option>
											<option value="2">有按揭自置</option>
											<option value="3">亲属产权</option>
											<option value="4">单位宿舍</option>
											<option value="5">租房居住</option>
											<option value="6">自建住房</option>
										</select>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">资质分类<span class="required">*</span></label>
									<div class="controls">
										<select name="qualifiType" class="m-wrap large">
											<option value="">请选择</option>
											<option value="1">A类客户</option>
											<option value="2">B类客户</option>
											<option value="3">C类客户</option>
											<option value="4">D类客户</option>
										</select>
										<span onclick="javascript:clickModel('#warn_dialog');" class="glyphicons no-js circle_info" 
										data-toggle="modal" ><i></i></span>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">婚姻状况<span class="required">*</span></label>
									<div class="controls">
										<select name="maritalStatus" class="m-wrap large">
											<option value="">请选择</option>
											<option value="1">未婚</option>
											<option value="2">已婚</option>
											<option value="3">离异</option>
											<option value="4">丧偶</option>
											<option value="5">再婚</option>
											<option value="6">复婚</option>
										</select>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">有无子女<span class="required">*</span></label>
									<div class="controls">
										<select name="haveChild" class="m-wrap large">
											<option value="">请选择</option>
											<option value="1">有</option>
											<option value="0">无</option>
										</select>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="control-group span6 ">
									<label class="control-label">其他借款<span class="required">*</span></label>
									<div class="controls">
										<input type="number" name="haveLoan" class="m-wrap large" maxlength="8"/>
									</div>
								</div>
								<div class="control-group span6 ">
									<label class="control-label">现住址居住时长(年)<span class="required">*</span></label>
									<div class="controls">
										<input type="number" name="liveYears" class="m-wrap large" max="100"/>
									</div>
								</div>
							</div>
							<input type="hidden" name="type" id="submitType" value="1" />
						</form>
						<#-- <div class="form-actions">
							<input type="button" class="btn blue" onclick="addInfo()"
									value="保存" /> 
							<input type="button" class="btn blue" onclick="save(event,2)"
								value="提交" /> 
						 	<a href="${serverPath}/innerBlack/list.htm" class="btn">返回</a>
						</div> -->
					</div>
<#include "/apply/userinfo/userinfo_dialog.ftl">