
<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
	<div class="page-content">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<!--页面标题-->
					<h3 class="page-title"></h3>
					<!--面包屑导航-->
					<ul class="breadcrumb">
						<li> <i class="icon-home"></i>
							<a href="${serverPath}/index.htm">首页</a>
							<i class="icon-angle-right"></i> 
						</li>
						<li>
							<a href="#">产品类型管理</a>
							<i class="icon-angle-right"></i> 
						</li>
						<li>
							<#if record??>
							<a href="#">修改</a>
							<#else>
							<a href="#">新增</a>
							</#if>
						</li>
					</ul>
				</div>
			</div>
		<div>
		<div class="well">
		<form id="submit_form">
			<input type = "hidden" name="id" id='id' value="${(record.id)!}">
			<input type = "hidden" name="storeCodes" id='storeCodes' value="${(record.storeCodes)!}">
			<div class="row-fluid">
				<div class="control-group span6 ">
					<label class="help-inline  span2 text-right  control-label"> 产品名称<span class="required">*</span></label>
					<select name="prodCode" id='prodCode' class="m-wrap span8" data-code="${(record.prodCode)!"0"}">
						<option value="0">请选择</option>
					</select>
				</div>
                <div class="control-group span6 ">
                    <label class="help-inline   span2 text-right  control-label"> 产品序号<span class="required">*</span></label> <input 
                        type="text" class="m-wrap span8" name="prodIndex" id='prodIndex'
                        value="${(record.prodIndex)!}">
                </div>				
			</div>

			<div class="row-fluid">
				<div class="control-group span6 ">
					<label class="help-inline   span2 text-right  control-label"> 贷款额度上限<span class="required">*</span></label> <input 
						type="text" class="m-wrap span8" name="loanUpLimit" id='loanUpLimit' 
						value="${(record.loanUpLimit)!}">
				</div>
				<div class="control-group span6 ">
					<label class="help-inline   span2 text-right  control-label"> 贷款额度下限<span class="required">*</span></label> <input 
						type="text" class="m-wrap span8" name="loanDownLimit" id='loanDownLimit'
						value="${(record.loanDownLimit)!}">
				</div>
			</div>
			
            <div class="row-fluid">
                <div class="control-group span6 ">
                    <label class="help-inline   span2 text-right  control-label"> 模板类型<span class="required">*</span></label> <input 
                        type="text" class="m-wrap span8" name="contractTemplate" id='contractTemplate'
                        value="${(record.contractTemplate)!}">
                </div>
                <div class="control-group span6 ">
                    <label class="help-inline   span2 text-right  control-label"> 结算规则<span class="required">*</span></label> <input 
                        type="text" class="m-wrap span8" name="punishRule" id='punishRule'
                        value="${(record.punishRule)!}">
                </div>
            </div>			
			<div class="row-fluid">
                <div class="control-group span6 ">
                    <label class="help-inline   span2 text-right  control-label"> 产品图标<span class="required">*</span></label> <input 
                        type="text" class="m-wrap span8" name="prodIcon" id='prodIcon' maxlength="100"
                        value="${(record.prodIcon)!}">
                </div>			
				<div class="control-group span6 ">
					<label class="help-inline   span2 text-right  control-label"> 进件渠道<span class="required">*</span></label>
    					<select name="channel" id='channel' class="m-wrap span8" data-id="${(record.channel)!}">
                            <option value="1" <#if ((record.channel)!0) == 1 > selected='selected' </#if>>APP</option>
                            <option value="2" <#if ((record.channel)!0) == 2 > selected='selected' </#if>>线下</option>
                            <option value="3" <#if ((record.channel)!0) == 3 > selected='selected' </#if>>APP&线下</option>
                        </select>
				</div>
			</div>
            <div class="row-fluid">
                <div class="control-group span6 ">
                    <label class="help-inline   span2 text-right  control-label"> 产品可申请城市<span class="required">*</span></label>
                    <select name="addStore" id="addStore" class="form-control span6" multiple="multiple">
                    </select>
                </div>              
            </div>
            
			<div class="well">
				<center>
				<a href="javascript:page_back('${serverPath}/productAttribute/queryProductAttribute.htm');" class="btn blue" >返回</a>
				<input type="button" style="margin-left:40px" class="btn blue" value="提交" onclick="save();">
				</center>
			</div>
			</form>
		</div>
		
	</div>
	
	<script src="${cdnPath}/js/productAttribute/new_edit.js?v=${VERSION_NO}"></script>
	
<#include "/sys/bottom.ftl">