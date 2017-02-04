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
					<li><a href="#">系统管理</a><i class="icon-angle-right"></i></li>
					<li><span><#if menu??>修改菜单<#else>新增菜单</#if></span></li>
				</ul>
			</div>
		</div>
		<div class="portlet box">
					
					<div class="portlet-body form">
				<!-- edit form -->
				<form id="tab" action="${ctx}/menu/edit.htm" method="POST" class="form-horizontal">
					<input type="hidden"	name="id" id="id" value="${(menu.id)!'' }"> 
					<input type="hidden" name="parentMenuId" value="${parentMenuId!''}">
					<div class="row-fluid">
						<div class="control-group span6 ">
						<label class="control-label">上级菜单<span class="required">*</span></label> 
						<div class="controls">
						<select name="parentId" class="m-wrap span6"
							id="parentId">
							<option value="-1">请选择上级菜单</option>
							<#if menuList??&&menuList?size gt 0>
							  <#list menuList as menuMap>
								<option value="${menuMap.id}">${menuMap.menuName }</option>
							 </#list>
							 </#if>
						</select>
						</div>
						</div>
					</div>
					<div class="row-fluid">
						<div class="control-group span6 ">
						 <label class="control-label">名称<span class="required">*</span></label> 
						 <div class="controls">
						 <input type="text" name="menuName"
							id="menuName" class="m-wrap span6" maxlength="15"
							value="${(menu.menuName)!'' }">
						 </div>
						</div>
					</div>
					<div class="row-fluid">
						<div class="control-group span6 ">
					    <label class="control-label">访问路径<span class="required">*</span></label> 
					    <div class="controls">
					 	<input	type="text" name="menuUrl" id="menuUrl" maxlength="50"
						class="m-wrap span6" value="${(menu.menuUrl)!'' }">
						</div>
						</div>
					</div>
					<div class="row-fluid">
						<div class="control-group span6 ">
						<label class="control-label">菜单图标<span class="required">*</span></label> 
						<div class="controls">
						<input type="text" name="menuIcon"		id="menuIcon" class="m-wrap span6" maxlength="30"
							value="${(menu.menuIcon)!'' }"> <#if (menu.menuIcon)??><#else><span class="help-inline">例如:icon-glass</span></#if> 
						</div>
						</div>
					</div>
					<div class="row-fluid">
						<div class="control-group span6 ">
						<label class="control-label">菜单顺序<span class="required">*</span></label> 
						<div class="controls">
						<select name="orderColumn" class="m-wrap span6">
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
							<option value="8">8</option>
							<option value="9">9</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
							<option value="13">13</option>
							<option value="14">14</option>
							<option value="15">15</option>
							<option value="16">16</option>
							<option value="17">17</option>
							<option value="18">18</option>
							<option value="19">19</option>
							<option value="20">20</option>						
						</select>
						</div>
						</div>
					</div>
					
				</form>
				<div class="form-actions">
					 <#if parentMenuId?? && parentMenuId!=-1> <a
						href="${ctx}/menu/childrenMenu.htm?id=${parentMenuId}" class="btn">返回</a>
					<#else> <a href="${ctx}/menu/menu.htm" class="btn">返回</a> </#if>
					<input type="button" class="btn blue" onclick="save(event)"	value="提交" />
				</div>
			</div>
			<div class="">
				<div class="page-header">
					<h1>
						<small>可选图标</small>
					</h1>
				</div>

				<table>
					<tr>
						<td><i class="icon-glass"></i> icon-glass</td>
						<td><i class="icon-music"></i> icon-music</td>
						<td><i class="icon-search"></i> icon-search</td>
						<td><i class="icon-envelope"></i> icon-envelope</td>
						<td><i class="icon-heart"></i> icon-heart</td>
						<td><i class="icon-star"></i> icon-star</td>
						<td><i class="icon-star-empty"></i> icon-star-empty</td>
						<td><i class="icon-user"></i> icon-user</td>
						<td><i class="icon-film"></i> icon-film</td>
						<td><i class="icon-th-large"></i> icon-th-large</td>
					</tr>
					<tr>
						<td><i class="icon-th"></i> icon-th</td>
						<td><i class="icon-th-tdst"></i> icon-th-tdst</td>
						<td><i class="icon-ok"></i> icon-ok</td>
						<td><i class="icon-remove"></i> icon-remove</td>
						<td><i class="icon-zoom-in"></i> icon-zoom-in</td>
						<td><i class="icon-zoom-out"></i> icon-zoom-out</td>
						<td><i class="icon-off"></i> icon-off</td>
						<td><i class="icon-signal"></i> icon-signal</td>
						<td><i class="icon-cog"></i> icon-cog</td>
						<td><i class="icon-trash"></i> icon-trash</td>
					</tr>
					<tr>
						<td><i class="icon-home"></i> icon-home</td>
						<td><i class="icon-file"></i> icon-file</td>
						<td><i class="icon-time"></i> icon-time</td>
						<td><i class="icon-road"></i> icon-road</td>
						<td><i class="icon-download-alt"></i> icon-download-alt</td>
						<td><i class="icon-download"></i> icon-download</td>
						<td><i class="icon-upload"></i> icon-upload</td>
						<td><i class="icon-inbox"></i> icon-inbox</td>
						<td><i class="icon-play-circle"></i> icon-play-circle</td>
						<td><i class="icon-repeat"></i> icon-repeat</td>
					</tr>
					<tr>
						<td><i class="icon-refresh"></i> icon-refresh</td>
						<td><i class="icon-tdst-alt"></i> icon-tdst-alt</td>
						<td><i class="icon-lock"></i> icon-lock</td>
						<td><i class="icon-flag"></i> icon-flag</td>
						<td><i class="icon-headphones"></i> icon-headphones</td>
						<td><i class="icon-volume-off"></i> icon-volume-off</td>
						<td><i class="icon-volume-down"></i> icon-volume-down</td>
						<td><i class="icon-volume-up"></i> icon-volume-up</td>
						<td><i class="icon-qrcode"></i> icon-qrcode</td>
						<td><i class="icon-barcode"></i> icon-barcode</td>
					</tr>
					<tr>
						<td><i class="icon-tag"></i> icon-tag</td>
						<td><i class="icon-tags"></i> icon-tags</td>
						<td><i class="icon-book"></i> icon-book</td>
						<td><i class="icon-bookmark"></i> icon-bookmark</td>
						<td><i class="icon-print"></i> icon-print</td>
						<td><i class="icon-camera"></i> icon-camera</td>
						<td><i class="icon-font"></i> icon-font</td>
						<td><i class="icon-bold"></i> icon-bold</td>
						<td><i class="icon-itatdc"></i> icon-itatdc</td>
						<td><i class="icon-text-height"></i> icon-text-height</td>
					</tr>
					<tr>
						<td><i class="icon-text-width"></i> icon-text-width</td>
						<td><i class="icon-atdgn-left"></i> icon-atdgn-left</td>
						<td><i class="icon-atdgn-center"></i> icon-atdgn-center</td>
						<td><i class="icon-atdgn-right"></i> icon-atdgn-right</td>
						<td><i class="icon-atdgn-justify"></i> icon-atdgn-justify</td>
						<td><i class="icon-tdst"></i> icon-tdst</td>
						<td><i class="icon-indent-left"></i> icon-indent-left</td>
						<td><i class="icon-indent-right"></i> icon-indent-right</td>
						<td><i class="icon-facetime-video"></i> icon-facetime-video</td>
						<td><i class="icon-picture"></i> icon-picture</td>
					</tr>
					<tr>
						<td><i class="icon-pencil"></i> icon-pencil</td>
						<td><i class="icon-map-marker"></i> icon-map-marker</td>
						<td><i class="icon-adjust"></i> icon-adjust</td>
						<td><i class="icon-tint"></i> icon-tint</td>
						<td><i class="icon-edit"></i> icon-edit</td>
						<td><i class="icon-share"></i> icon-share</td>
						<td><i class="icon-check"></i> icon-check</td>
						<td><i class="icon-move"></i> icon-move</td>
						<td><i class="icon-step-backward"></i> icon-step-backward</td>
						<td><i class="icon-fast-backward"></i> icon-fast-backward</td>
					</tr>
					<tr>
						<td><i class="icon-backward"></i> icon-backward</td>
						<td><i class="icon-play"></i> icon-play</td>
						<td><i class="icon-pause"></i> icon-pause</td>
						<td><i class="icon-stop"></i> icon-stop</td>
						<td><i class="icon-forward"></i> icon-forward</td>
						<td><i class="icon-fast-forward"></i> icon-fast-forward</td>
						<td><i class="icon-step-forward"></i> icon-step-forward</td>
						<td><i class="icon-eject"></i> icon-eject</td>
						<td><i class="icon-chevron-left"></i> icon-chevron-left</td>
						<td><i class="icon-chevron-right"></i> icon-chevron-right</td>
					</tr>
					<tr>
						<td><i class="icon-plus-sign"></i> icon-plus-sign</td>
						<td><i class="icon-minus-sign"></i> icon-minus-sign</td>
						<td><i class="icon-remove-sign"></i> icon-remove-sign</td>
						<td><i class="icon-ok-sign"></i> icon-ok-sign</td>
						<td><i class="icon-question-sign"></i> icon-question-sign</td>
						<td><i class="icon-info-sign"></i> icon-info-sign</td>
						<td><i class="icon-screenshot"></i> icon-screenshot</td>
						<td><i class="icon-remove-circle"></i> icon-remove-circle</td>
						<td><i class="icon-ok-circle"></i> icon-ok-circle</td>
						<td><i class="icon-ban-circle"></i> icon-ban-circle</td>
					</tr>
					<tr>
						<td><i class="icon-arrow-left"></i> icon-arrow-left</td>
						<td><i class="icon-arrow-right"></i> icon-arrow-right</td>
						<td><i class="icon-arrow-up"></i> icon-arrow-up</td>
						<td><i class="icon-arrow-down"></i> icon-arrow-down</td>
						<td><i class="icon-share-alt"></i> icon-share-alt</td>
						<td><i class="icon-resize-full"></i> icon-resize-full</td>
						<td><i class="icon-resize-small"></i> icon-resize-small</td>
						<td><i class="icon-plus"></i> icon-plus</td>
						<td><i class="icon-minus"></i> icon-minus</td>
						<td><i class="icon-asterisk"></i> icon-asterisk</td>
					</tr>
					<tr>
						<td><i class="icon-exclamation-sign"></i>
							icon-exclamation-sign</td>
						<td><i class="icon-gift"></i> icon-gift</td>
						<td><i class="icon-leaf"></i> icon-leaf</td>
						<td><i class="icon-fire"></i> icon-fire</td>
						<td><i class="icon-eye-open"></i> icon-eye-open</td>
						<td><i class="icon-eye-close"></i> icon-eye-close</td>
						<td><i class="icon-warning-sign"></i> icon-warning-sign</td>
						<td><i class="icon-plane"></i> icon-plane</td>
						<td><i class="icon-calendar"></i> icon-calendar</td>
						<td><i class="icon-random"></i> icon-random</td>
					</tr>
					<tr>
						<td><i class="icon-comment"></i> icon-comment</td>
						<td><i class="icon-magnet"></i> icon-magnet</td>
						<td><i class="icon-chevron-up"></i> icon-chevron-up</td>
						<td><i class="icon-chevron-down"></i> icon-chevron-down</td>
						<td><i class="icon-retweet"></i> icon-retweet</td>
						<td><i class="icon-shopping-cart"></i> icon-shopping-cart</td>
						<td><i class="icon-folder-close"></i> icon-folder-close</td>
						<td><i class="icon-folder-open"></i> icon-folder-open</td>
						<td><i class="icon-resize-vertical"></i> icon-resize-vertical</td>
						<td><i class="icon-resize-horizontal"></i>
							icon-resize-horizontal</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
var oldName="${(menu.menuName)!'' }";
	$(function(){
		var oldorderColumn='${(menu.orderColumn)!}';
		var parentMenuId='${parentMenuId!}';
		$("form").validate({
			rules: {
			            menuName: {
		                    required: true,
		                    maxlength:20,
		                    remote:{
			                    url:'${ctx}/menu/checkMenuExist.htm',
			                    type : 'post',
			                    dataType : 'json',
			                    data : {
									"menuName" :function(){return $("input[name='menuName']").val()}
								},
								dataFilter: function(data, type) {  
										if(oldName==$("input[name='menuName']").val()){
											return true;
										}else{
											return data;
										}
			                     }  
		                    }
		                },
		                menuUrl: {
		                    required: true
		                },
		                menuIcon:{
		                    required: true
		                }
		            },
		     messages:{
					    menuName:{
					    	remote:"菜单名称已存在"
					    }
		    },
			submitHandler: function(form){   //表单提交句柄,为一回调函数，带一个参数：form   
	                    var menuIcon = $("#menuIcon").val();
	                    menuIcon = menuIcon.replace(/(^\s*)|(\s*$)/g, "");// 删除二边空格
						$("#menuIcon").val(menuIcon);
	                    form.submit();   //提交表单   
	         }
		});
		
		$("select[name='parentId']").change(function(){
					$("select[name='orderColumn'").empty(); 
					var menu_id=$(this).val();
					$.ajax({
						url : '${ctx}/menu/getUsedOrderColumn.htm',
						type : 'post',
						data : {
							id : menu_id
						},
						dataType : 'json',
						success : function(res) {
							var orders=res.data;
							for(i=1;i<=20;i++){
							if(orders.indexOf(i)==-1||(oldorderColumn==i&&menu_id==parentMenuId)){
								$("select[name='orderColumn'").append("<option value='"+i+"'>"+i+"</option>");
							}	
							}	
							if(menu_id==parentMenuId&&oldorderColumn!=''){
								$("select[name='orderColumn']").val(oldorderColumn);
							}
					 }
					 })
		
		});
		$("select[name='parentId']").val('${parentMenuId!-1}');
		$("select[name='parentId']").trigger("change");
		
	});
	function save(event) {
		//阻止冒泡
		ChkUtil.stopBubbleEvent(event);
	    $("#tab").submit();
	}
</script>