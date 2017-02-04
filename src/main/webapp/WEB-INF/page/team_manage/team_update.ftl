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
									<a href="${serverPath}/team/list.htm">团队管理</a><i class="icon-angle-right"></i>
								</li>
								<li>
									<a href="#">修改团队信息</a>
								</li>
							</ul>
						</div>
					</div>
				<div>
	<div class="row-fluid">
	<div class="portlet box blue">
			<div class="portlet-title">
									<div class="caption">修改团队信息<i class="icon-reorder"></i></div>
			</div>
			<div class="portlet-body form">

		<!-- edit form -->
		<form id="tab" action="${serverPath}/team/updateTeam.htm" method="POST">
			<input type="hidden" name="id" id="id" value="${id}" />
			<div class="row-fluid">
				<div class="control-group">
					<label class="control-label"><span id="orgTypeName">组织</span><span class="required">*</span></label>
					<div class="controls">
						<input type="text" class="span3 m-wrap" name="storeName" disabled />
						<input type="hidden" name="storeId" id="storeId" value="" />
						<span class="help-inline">&nbsp;</span> </div>
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
	var auditorurl = '';
	var FormSamples = function () {
			    return {   
			        init: function () {
			            $("#leaderId").select2({
			                placeholder: ChkUtil.select2Name,
			                allowClear: true,
			                minimumInputLength: 1,
			                initSelection: function (element, callback) {
					            var data = [{id: element.val(), text: element.val()}];
					            callback({id: element.val(), text: element.val()});//这里初始化
					        },
			                query: function (query) {
			                	var storeId = $("#storeId").val();
			                	if(storeId == null) {
									BootstrapDialog.alert("请先选择门店",function() {
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
		initStoreInfo();
		$("form").validate({
			rules: {
			            teamName: {
		                    required: true,
		                    maxlength:20
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
									if($("#id").val() != res.data.id) {
										BootstrapDialog.alert("已经存在相同的团队名称");
										return;
									} 
								}
								updateTeam();  //提交表单
							}
				});
	           
	        }
		});
	});
	function save(event) {
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
	    $("#tab").submit();
	}
	
	function updateTeam() {
			if($("#s2id_leaderId .select2-choice span").html() != ChkUtil.select2Name) {
				$("#leaderName").val($("#s2id_leaderId .select2-choice span").html());
			} else {
				$("#leaderName").val("");
			}
			$.ajax({
				url : '${serverPath}/team/updateTeam.htm',
				type : 'post',
				data : {
					id : ${id},
					teamName : $("#tab input[name='teamName']").val(),
					teamDesc : $("#tab textarea[name='teamDesc']").val(),
					leaderId : $("#leaderId").val(),
					leaderName : $("#leaderName").val()
				},
				dataType : 'json',
				success : function(res) {
				
					if (res.code == '0') {
						BootstrapDialog.alert("操作成功！", function(){
									window.location.href = serverPath + "/team/list.htm"+"?"+ChkUtil.getCookie("queryParams");
					    });
					}else{
						$("#error").html(res.message);
					}
				}
			})
		return false;
	}
	
	function initStoreInfo() {
			if($("#s2id_leaderId .select2-choice span").html() != ChkUtil.select2Name) {
						$("#leaderName").val($("#s2id_leaderId .select2-choice span").html());
			}
			$.ajax({
				url : '${serverPath}/team/teamInfoQueryById/${id}.htm',
				type : 'post',
				data : {
				},
				dataType : 'json',
				success : function(res) {
					if (res.code == '0') {
						$("#tab input[name='teamName']").val(res.data.teamName);
						$("#tab textarea[name='teamDesc']").val(res.data.teamDesc);
						$("#tab input[name='storeName']").val(res.data.storeName);
						$("#tab input[name='storeId']").val(res.data.storeId);
						if(res.data.storeId == ${orgNodeTeam.nodeCode!80}) {
							//$("#orgTypeName").html("部门");
							auditorurl = serverPath+"/employee/showLastAuditorEmpoyee.htm";
						} else {
							auditorurl = serverPath+"/employee/showBMEmpoyee.htm";
						}
						$("#leaderId").select2("val", res.data.leaderName);
						if(!ChkUtil.isNull(res.data.leaderName)) {
							$("#s2id_leaderId .select2-choice span").html(res.data.leaderName);
							$("#leaderName").val(res.data.leaderName);
						}
						if(res.data.leaderId != 0) {
							$("#leaderId").val(res.data.leaderId);
						}
					}else{
						$("#error").html(res.message);
						return false;
					}
				}
			})
		return false;
	}
</script>
<#include "/sys/bottom.ftl">