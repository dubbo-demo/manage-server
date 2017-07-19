<style>



</style>
		<form id="addForm" action="addTips.htm" class="form-horizontal" method="post">
            <div class="control-group" style="margin-left: -100px;">
				<#if areaList??>
				<#list areaList as a>
                <div class="controls">
                    <label class="checkbox" style="width:15%;">
                        <span>
							<input type="checkbox" name="areaIds" class="checkboxitem" id="area${a.id}" value="${a.orgCode}" data-areaid="${a.id}"  onchange="changArea(this,'${a.id}')"/>
						</span> <b>${a.orgName}</b>
                    </label>
						<#assign x=0 />
						<#if shopList??>
						<#list shopList as s>
						<#if s.parentId == a.id>
							<#assign x=x+1 />
							<#if x%5=0>
                            </div>
                            <div class="controls">
							<label class="checkbox" style="width:15%;">
								<span>
								</span>
							</label>
							</#if>
                            <label class="checkbox" style="width:17%;">
                                <span>
									<input type="checkbox" name="shops" class="checkboxitem" id="shop${s.id}"
										   onchange="changeShop(this,'${s.parentId}')" parentId="${s.parentId}" value="${s.id}"/>
								</span>${s.orgName}
                            </label>
						</#if>
						</#list>
						</#if>

				</div>
				</#list>
				</#if>

			</div>
            <div class="control-group" style="margin-left: -100px;">
                <div class="controls">
                    <label class="checkbox" style="width:15%;">
							<span>
								<input type="checkbox" name="infoRole" class="checkboxitem" id="infoRoleClient" value="infoRoleClient" onchange="changArea(this,'infoRoleClient')">
							</span> <b>进件渠道</b>
                    </label>
                    <label class="checkbox" style="width:17%;">
						<span>
							<input type="checkbox" name="clientRole" class="checkboxitem"
                                   onchange="changePrent(this,'infoRoleClient')" id="clientApp" parentid="infoRoleClient" value="0">
						</span>web
                    </label>
                    <label class="checkbox" style="width:17%;">
						<span>
							<input type="checkbox" name="clientRole" class="checkboxitem"
                                   onchange="changePrent(this,'infoRoleClient')" id="clientWeb" parentid="infoRoleClient" value="1">
						</span>app
                    </label>
                </div>
            </div>
            <div class="control-group" style="margin-left: -100px;">
                <div class="controls">
                    <label class="checkbox" style="width:15%;">
							<span>
								<input type="checkbox" name="infoRole" class="checkboxitem" id="infoRoleSource" value="infoRoleSource" onchange="changArea(this,'infoRoleSource')">
							</span><b>数据源</b>
                    </label>
                    <label class="checkbox" style="width:17%;">
						<span>
							<input type="checkbox" name="sourceRole" class="checkboxitem"
                                   onchange="changePrent(this,'infoRoleSource')" id="sourceMYD" parentid="infoRoleSource" value="myd">
						</span>麦芽贷
                    </label>
                    <label class="checkbox" style="width:17%;">
						<span>
							<input type="checkbox" name="sourceRole" class="checkboxitem"
                                   onchange="changePrent(this,'infoRoleSource')" id="sourceMYFQ" parentid="infoRoleSource" value="maiyafq">
						</span>麦芽分期
                    </label>
                    <label class="checkbox" style="width:17%;">
						<span>
							<input type="checkbox" name="sourceRole" class="checkboxitem"
                                   onchange="changePrent(this,'infoRoleSource')" id="sourceMYPH" parentid="infoRoleSource" value="myph">
						</span>麦芽普惠
                    </label>
                    <label class="checkbox" style="width:17%;">
						<span>
							<input type="checkbox" name="sourceRole" class="checkboxitem"
                                   onchange="changePrent(this,'infoRoleSource')" id="sourceFRB" parentid="infoRoleSource" value="frbao">
						</span>付融宝
                    </label>
                </div>
            </div>
            <div class="control-group" style="margin-left: -100px;">
                <div class="controls">
                    <label class="checkbox" style="width:15%;">
							<span>
								<input type="checkbox" name="infoRole" class="checkboxitem" id="infoRoleProduct" value="infoRoleProduct" onchange="changArea(this,'infoRoleProduct')">
							</span> <b>产品与逾期</b>
                    </label>
                    <label class="checkbox" style="width:17%;">
						<span>
						</span>
                    </label>
                    <label class="checkbox" style="width:17%;">
						<span>
						</span>
                    </label>
                </div>
            <#if productList??>
                <#list productList as p>
                <div class="controls">
                    <label class="checkbox" style="width:15%;">
						<span>
						</span>
                    </label>
                    <label class="checkbox" style="width:17%;">
						<span>
							<input type="checkbox" name="parentRole" class="checkboxitem"
                                   onchange="changeParentRole(this,'infoRoleProduct','${p.id}')" id="prd${p.id}" parentid="infoRoleProduct" value="${p.id}" data-proid="${p.id}">
						</span>${p.nodeName}
                    </label>
                    <#if overs??>
                        <#assign x=0 />
                        <#list overs as o>
                            <#assign x=o_index+1 />
                            <#if x%4=0>
                            </div>
                            <div class="controls">
                                <label class="checkbox" style="width:15%;">
						<span>
						</span>
                                </label>
                                <label class="checkbox" style="width:17%;">
						<span>
						</span>
                                </label>
                                <label class="checkbox" style="width:17%;">
						<span>
							<input type="checkbox" name="conditionRole" class="checkboxitem"
                                   onchange="changeConditionRole(this,'infoRoleProduct','${p.id}')" productid="${p.id}" id="prd_${p.id}${o.nodeCode}" parentid="infoRoleProduct" value="${o.nodeCode}">
						</span>${o.nodeName}
                                </label>
                            <#else>
                                <label class="checkbox" style="width:17%;">
						<span>
							<input type="checkbox" name="conditionRole" class="checkboxitem"
                                   onchange="changeConditionRole(this,'infoRoleProduct','${p.id}')" productid="${p.id}" id="prd_${p.id}${o.nodeCode}" parentid="infoRoleProduct" value="${o.nodeCode}">
						</span>${o.nodeName}
                                </label>
                            </#if>
                            <#if (overs?size)==(o_index+1)>
                            </div>
                            </#if>
                        </#list>
                    </#if>
                </#list>
            </#if>

            <div class="controls">
                <label class="checkbox" style="width:15%;">
						<span>
						</span>
                </label>
                <label class="checkbox" style="width:17%;">
						<span>
							<input type="checkbox" name="parentRole" class="checkboxitem"
                                   onchange="changeParentRole(this,'infoRoleProduct','1111111')" id="prd1111111" parentid="infoRoleProduct" value="1111111" data-proid="1111111">
						</span>麦芽小贷
                </label>
            <#if overs??>
                <#assign x=0 />
                <#list overs as o>
                    <#assign x=o_index+1 />
                    <#if x%4=0>
                    </div>
                    <div class="controls">
                        <label class="checkbox" style="width:15%;">
						<span>
						</span>
                        </label>
                        <label class="checkbox" style="width:17%;">
						<span>
						</span>
                        </label>
                        <label class="checkbox" style="width:17%;">
						<span>
							<input type="checkbox" name="conditionRole" class="checkboxitem"
                                   onchange="changeConditionRole(this,'infoRoleProduct','1111111')" productid="1111111" id="prd_1111111${o.nodeCode}" parentid="infoRoleProduct" value="${o.nodeCode}">
						</span>${o.nodeName}
                        </label>
                    <#else>

                        <label class="checkbox" style="width:17%;">
						<span>
							<input type="checkbox" name="conditionRole" class="checkboxitem"
                                   onchange="changeConditionRole(this,'infoRoleProduct','1111111')" productid="1111111" id="prd_1111111${o.nodeCode}" parentid="infoRoleProduct" value="${o.nodeCode}">
						</span>${o.nodeName}
                        </label>
                    </#if>
                    <#if (overs?size)==(o_index+1)>
                    </div>
                    </#if>
                </#list>
            </#if>

            <div class="controls">
                <label class="checkbox" style="width:15%;">
						<span>
						</span>
                </label>
                <label class="checkbox" style="width:17%;">
						<span>
							<input type="checkbox" name="parentRole" class="checkboxitem"
                                   onchange="changeParentRole(this,'infoRoleProduct','2222222')" id="prd2222222" parentid="infoRoleProduct" value="2222222" data-proid="2222222">
						</span>麦芽分期
                </label>
            <#if overs??>
                <#assign x=0 />
                <#list overs as o>
                    <#assign x=o_index+1 />
                    <#if x%4=0>
                    </div>
                    <div class="controls">
                        <label class="checkbox" style="width:15%;">
						<span>
						</span>
                        </label>
                        <label class="checkbox" style="width:17%;">
						<span>
						</span>
                        </label>
                        <label class="checkbox" style="width:17%;">
						<span>
							<input type="checkbox" name="conditionRole" class="checkboxitem"
                                   onchange="changeConditionRole(this,'infoRoleProduct','2222222')" productid="2222222" id="prd_2222222${o.nodeCode}" parentid="infoRoleProduct" value="${o.nodeCode}">
						</span>${o.nodeName}
                        </label>
                    <#else>
                        <label class="checkbox" style="width:17%;">
						<span>
							<input type="checkbox" name="conditionRole" class="checkboxitem"
                                   onchange="changeConditionRole(this,'infoRoleProduct','2222222')" productid="2222222" id="prd_2222222${o.nodeCode}" parentid="infoRoleProduct" value="${o.nodeCode}">
						</span>${o.nodeName}
                        </label>
                    </#if>
                    <#if (overs?size)==(o_index+1)>
                    </div>
                    </#if>
                </#list>
            </#if>

            </div>
		</form>
<script>
    $(function(){
      $("#text").keyup(function(){
       var len = $(this).val().length;
       if(len > 399){
        $(this).val($(this).val().substring(0,400));
       }
       var num =400 - $(this).val().length
       $("#word").text(num);
      });
        <#if orgs??>
            <#list orgs as org>
                $.uniform.update($("input[id='shop${org}']:checkbox").prop("checked", true));
            </#list>
        </#if>



    <#if prds??>
        <#list prds as prd>
            $.uniform.update($("input[id='prd_${prd}']:checkbox").prop("checked", true));
        </#list>
    </#if>


    <#if clients??>
        <#list clients as client>
            $.uniform.update($("input[id='prd_${client}']:checkbox").prop("checked", true));
        </#list>
    </#if>

    <#if sources??>
        <#list sources as s>
            $.uniform.update($("input[id='prd_${s}']:checkbox").prop("checked", true));
        </#list>
    </#if>     });

    function getSource() {
        var sysRoleCs = [];
        var SysRoleConditionDto={};
        var shops = '';
        // 数据源
        $("input[name=sourceRole]:checkbox").each(function(i, v) {
            shops = shops +","+$(this).val();
        });
        shops = shops.substr(1);
        sysRoleCs[0] = {};
        SysRoleConditionDto.dimension = 'infoRoleSource';
        SysRoleConditionDto.parentCode = 'source';
        SysRoleConditionDto.roleId = $("#condition_roleid").val();
        SysRoleConditionDto.conditionCode = shops;
        SysRoleConditionDto.delFlag = 1;
        sysRoleCs[0] = SysRoleConditionDto;

        shops = '';
        SysRoleConditionDto = {};

        // 数据源
        $("input[name=clientRole]:checkbox").each(function(i, v) {
            shops = shops +","+$(this).val();
        });
        shops = shops.substr(1);
        sysRoleCs[1] = {};
        SysRoleConditionDto.dimension = 'infoRoleClient';
        SysRoleConditionDto.parentCode = 'client';
        SysRoleConditionDto.roleId = $("#condition_roleid").val();
        SysRoleConditionDto.conditionCode = shops;
        SysRoleConditionDto.delFlag = 1;
        sysRoleCs[1] = SysRoleConditionDto;

        return sysRoleCs;
    }

 function getProductInfo() {
     var sysRoleCs = [];
     sysRoleCs = getSource();
     var SysRoleConditionDto={};
     var numIndex =sysRoleCs.length;
     $("input[name=parentRole]:checkbox").each(function(i, v) {
         var shops = '';
         $("input[productid="+$(this).data("proid")+"]:checkbox").each(function(ii, vv) {
             var shop = $(this);
             if($(this).attr("checked"))
             {
                 shops = shops + ',' +shop.val();
             }
         });
         if (shops == "") {
         } else {
             shops = shops.substr(1);
             sysRoleCs[numIndex] = {};
             SysRoleConditionDto.dimension = 'infoRoleProduct';
             SysRoleConditionDto.parentCode = $(this).val();
             SysRoleConditionDto.roleId = $("#condition_roleid").val();
             SysRoleConditionDto.conditionCode = shops;
             SysRoleConditionDto.delFlag = 1;
             sysRoleCs[numIndex] = SysRoleConditionDto;
             SysRoleConditionDto = {};
             numIndex++;
             shops = '';
         }
     });
     return sysRoleCs;
 }

 function getRoleConditionData() {
	 var sysRoleCs = [];
     sysRoleCs = getProductInfo();
	 var SysRoleConditionDto={};
	 var numIndex =sysRoleCs.length;
     $("input[name=areaIds]:checkbox").each(function(i, v) {
		 var shops = '';
         $("input[parentId="+$(this).data("areaid")+"]:checkbox").each(function(ii, vv) {
             var shop = $(this);
             if($(this).attr("checked"))
             {
                 shops = shops + ',' +shop.val();
             }
         });
		 if (shops == "") {
         } else {
             shops = shops.substr(1);
             sysRoleCs[numIndex] = {};
             SysRoleConditionDto.dimension = 'org';
             SysRoleConditionDto.roleId = $("#condition_roleid").val();
             SysRoleConditionDto.parentCode = $(this).val();
             SysRoleConditionDto.conditionCode = shops;
             SysRoleConditionDto.delFlag = 1;
             sysRoleCs[numIndex] = SysRoleConditionDto;
             SysRoleConditionDto = {};
             numIndex++;
             shops = '';
         }
     });
     return sysRoleCs;
 }

 function submitRoleCondition() {
     var roleData = getRoleConditionData();
	 if(null == roleData || roleData.length == 0) {
         BootstrapDialog.alert('没有选择任何数据权限!');
		 return;
	 }
     $.ajax({
         url : serverPath+'/roleCondition/saveInfo.htm',
         type : 'post',
         data :JSON.stringify(roleData),
         dataType : 'json',
         contentType : "text/html",
         success : function(res) {
             if (res.code == 0) {
                 BootstrapDialog.alert('操作成功!');
             } else {
                 BootstrapDialog.alert(res.message);
             }
         },
         error : function() {
             BootstrapDialog.alert('操作失败!');
         }
     });
 }

function goAdd(){
	if(check()){
		$("#addForm").submit();
	}
}
function check(){
   var title = $("#title").val();
   var text = $("#text").val();
   if(title=="" || title==null){
    	alert("请填写标题！");
    	return false;
   }
   if(text=="" || text==null){
    	alert("请填写正文！");
    	return false;
   }
   var ischecked = false;
   $("input[name='shops']").each(function(i){  
    	var isCheck = $(this).attr("checked");
    	if('checked' == isCheck || isCheck){
        	ischecked = true; 
	    }
	});
    if(!ischecked){
	    alert("请至少选择一个门店");
		return false;
	}
   return true;
}

function changArea(obj,id){
	if($(obj).is(':checked')){
		$.uniform.update($("input[parentId="+id+"]:checkbox").prop("checked", true));
	}else{
		$.uniform.update($("input[parentId="+id+"]:checkbox").prop("checked", false));
	}
}

 function changeParentRole(obj,id,pid){
     if($(obj).is(':checked')){
         $.uniform.update($("input[productId="+pid+"]:checkbox").prop("checked", true));
     }else{
         $.uniform.update($("input[id="+id+"]:checkbox").prop("checked", false));
         $.uniform.update($("input[productId="+pid+"]:checkbox").prop("checked", false));
     }
 }

 function changeConditionRole(obj,id,pid){
     if(!$(obj).is(':checked')){
         $.uniform.update($("input[id="+id+"]:checkbox").prop("checked", false));
         $.uniform.update($("input[id=prd"+pid+"]:checkbox").prop("checked", false));
     }
 }

function changePrent(obj,id){
    if(!$(obj).is(':checked')){
        $.uniform.update($("input[id="+id+"]:checkbox").prop("checked", false));
    }
}

function changeShop(obj,id){
	if(!$(obj).is(':checked')){
		$.uniform.update($("input[id=area"+id+"]:checkbox").prop("checked", false));
	}
}
</script>
