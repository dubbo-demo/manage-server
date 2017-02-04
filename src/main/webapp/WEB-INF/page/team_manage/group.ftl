<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
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
									<a href="#">首页</a> <i class="icon-angle-right"></i> </li>
								<li>
									<a href="${serverPath}/team/list.htm">信审小组管理</a><i class="icon-angle-right"></i>
								</li>
								<li>
									<a href="#">信审小组管理</a>
								</li>
							</ul>
						</div>
					</div>
				<div>
	<div class="row-fluid">
	<div class="portlet box blue">
			<div class="portlet-title">
									<div class="caption">信审小组管理<i class="icon-reorder"></i></div>
			</div>
			<div class="portlet-body form">

			<!-- edit form -->
			<form id="tab" class="form-horizontal" action="${serverPath}/team/updateTeam.htm" method="POST">
				<input type="hidden" name="id" value="" />
				<div class="row-fluid">
					<div class="control-group">
						<label class="control-label">组织<span class="required">*</span></label>
						<div class="controls">
							<select class="m-wrap span3" id="storeId" name="storeId">
							</select>
							<span class="help-inline">&nbsp;</span> 
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">团队名称<span class="required">*</span></label>
						<div class="controls">
							<input type="text" class="span3 m-wrap" name="teamName" maxlength="20" />
							<span class="help-inline">&nbsp;</span> </div>
					</div>
					<div class="control-group ">
						<label class="control-label">负责人</label>
						<div class="controls">
							<input type="hidden" id='leaderId' name="leaderId" class="m-wrap span3" />
							<input type="hidden" id='leaderName' name="leaderName" />
						</div>
					</div>
				</div>
	
				<div class="control-group">
					<label class="control-label">描述</label>
					<div class="controls">
						<textarea class="span3 m-wrap" rows="3" name="teamDesc" maxlength="100"></textarea>
					</div>
				</div>
			</form>
			<div class="form-actions">
				<a href="javascript:page_back('${serverPath}/team/list.htm')" class="btn">返回</a>
				<input type="button" class="btn blue" onclick="save(event)" value="提交"/> 
			</div>
		</div>
		</div>
	</div>
</div>
		<script>
			function page_back(url) {
				window.location.href = url+"?"+ChkUtil.getCookie("queryParams");
			}
			
			var FormSamples = function () {
				var auditorurl = serverPath+"/employee/showLastAuditorEmpoyee.htm";
			    return {   
			        init: function () {
			            $("#leaderId").select2({
			                placeholder: ChkUtil.select2Name,
			                allowClear: true,
			                minimumInputLength: 1,
			                query: function (query) {
			                	var storeId = $("#storeId").val();
			                	if(storeId == 0) {
									BootstrapDialog.alert("请先选择组织",function() {
										var data = {
				                            results: []
				                        };
				                		query.callback(data);
									});
								} else {
				                	$.getJSON(auditorurl, {"nameSpell" : query.term,"orgId" : storeId}, function(result) {
				                		var data = {
				                            results: result.data
				                        };
				                		query.callback(data);
				        			});
			        			}
			                }
			            });  
			            
			        }
			    };
			
			}();
			
			$(function(){
				FormSamples.init();
				$.validator.addMethod("changeStore", function (value, element) {
				    return this.optional(element) || value != 0; 
				}, "请选择组织");
				selectStoreId();
				$("form").validate({
					rules: {
					            teamName: {
				                    required: true,
				                    maxlength:20
				                },
				                storeId : {
				                	required: true,
				                	changeStore: true
				                }
				            },
					submitHandler: function(form){   //表单提交句柄,为一回调函数，带一个参数：form   
						$.ajax({
							url : '${serverPath}/team/queryCountByTeamName.htm',
							type : 'post',
							data : {
								teamName : $("#tab input[name='teamName']").val()
							},
							dataType : 'json',
							success : function(res) {
								if (res.code == 0) {
									BootstrapDialog.alert("已经存在相同的团队名称");
								}else{
									addTeam();  //提交表单   
								}
							}
						});
			           
			        }
				});
			});
			
			function addTeam() {
					if($("#s2id_leaderId .select2-choice span").html() != ChkUtil.select2Name) {
						$("#leaderName").val($("#s2id_leaderId .select2-choice span").html());
					}
					$.ajax({
						url : '${serverPath}/team/addTeam.htm',
						type : 'post',
						data : {
							storeId : $("#tab select[name='storeId']").val(),
							teamName : $("#tab input[name='teamName']").val(),
							teamDesc : $("#tab textarea[name='teamDesc']").val(),
							leaderId : $("#leaderId").val(),
							leaderName : $("#leaderName").val()
						},
						dataType : 'json',
						success : function(res) {
						
							if (res.code == '0') {
								BootstrapDialog.alert("操作成功",function() {
									window.location.href = serverPath + "/team/list.htm";							
								});
							}else{
								BootstrapDialog.alert(res.message);
							}
						}
					})
				return false;
			}
			
			function save(event) {
				//阻止冒泡
				ChkUtil.stopBubbleEvent(event);
			    $("#tab").submit();
			}
			
			function selectStoreId() {
				var url = "${serverPath}/node/selectNodeList.htm";
				var data = {
					"parentCode" : "TEAMPOSITION",
					"Time" : new Date().getMilliseconds()
				};
				$.ajaxSettings.async = true;
				$.getJSON(url, data, function(result) {
					var resultData = result.data;
					for (var i = 0; i < resultData.length; i++) {
							$("#storeId").append("<option value='"+resultData[i].nodeCode+"'>" + resultData[i].nodeName + "</option>");
					}
					
				});
		    }
		</script>
<#include "/sys/bottom.ftl" >