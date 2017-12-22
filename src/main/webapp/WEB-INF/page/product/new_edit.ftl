
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
							<a href="#">产品管理</a>
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
			<input type = "hidden" name="id" value="${(record.id)!}">
			<div class="row-fluid">
						<div class="control-group span6 ">
							<label class="help-inline  span2 text-right  control-label"> 产品名称<span class="required">*</span></label>
							<select name="prodType" id="prodType" class="m-wrap span8" data-id="${(record.prodType)!"0"}">
								<option value="0">请选择</option>
							</select>
						</div>
						<div class="control-group span6 ">
							<label class="help-inline   span2 text-right  control-label"> 贷款期数<span class="required">*</span></label>
							<select name="periods" id="periods" class="m-wrap span8" data-id="${(record.periods)!"0"}">
								<option value="0">请选择</option>
							</select>
						</div>
			</div>
			<div class="row-fluid">
						<div class="control-group span6 ">
							<label class="help-inline   span2 text-right  control-label"> 期数单位<span class="required">*</span></label> 
							<select name="periodsUnit" id='periodsUnit' class="m-wrap span8" data-id="${(record.periodsUnit)!"0"}">
								<option value="0">请选择</option>
							</select>
						</div>
						<div class="control-group span6 ">
							<label class="help-inline   span2 text-right  control-label"> 利率/月(%)<span class="required">*</span></label>
							<input type="text" class="m-wrap span8" name="interestRate"
								value="${(record.interestRate)!}">
						</div>
			</div>
			<div class="row-fluid">
						<div class="control-group span6 ">
							<label class="help-inline   span2 text-right  control-label"> 贷款额度上限<span class="required">*</span></label> <input 
								type="text" class="m-wrap span8" name="loanUpLimit"
								value="${(record.loanUpLimit)!}">
						</div>
						<div class="control-group span6 ">
							<label class="help-inline   span2 text-right  control-label"> 贷款额度下限<span class="required">*</span></label> <input 
								type="text" class="m-wrap span8" name="loanDownLimit"
								value="${(record.loanDownLimit)!}">
						</div>
			</div>
			<div class="row-fluid">
						<div class="control-group span6 ">
							<label class="help-inline   span2 text-right  control-label"> 综合费率(%)<span class="required">*</span></label> <input 
								type="text" class="m-wrap span8" name="serviceRate"
								value="${(record.serviceRate)!}">
						</div>
						<div class="control-group span6 ">
							<label class="help-inline   span2 text-right  control-label"> 逾期天数<span class="required">*</span></label> <input 
								type="text" class="m-wrap span8" name="overdueDays"
								value="${(record.overdueDays)!}">
						</div>
			</div>
			<div class="row-fluid">
						<div class="control-group span6 ">
							<label class="help-inline   span2 text-right  control-label"> 罚息比例<span class="required">*</span></label> <input 
								type="text" class="m-wrap span8" name="penaltyRate" id='penaltyRate'
								value="${(record.penaltyRate)!}">
						</div>
						<div class="control-group span6 ">
							<label class="help-inline   span2 text-right ">是否启用：</label>
							<label class="radio span" style="width:100px;">
							<input 
							type="radio"
							<#if ((record.status)!0) == 1>
								checked =  "checked"
							</#if>
								value = 1
								name="status"
								/>
							启用
						</label>
						<label class="radio span" style="width:100px;">
							<input 
							type="radio"
							<#if ((record.status)!0) == 0>
								checked =  "checked"
							</#if>
								value = 0
								name="status"
								/>
							禁用
						</label>
						</div>
			</div>
			<div class="well">
				<center>
				<a href="javascript:page_back('${serverPath}/product/queryPageList.htm');" class="btn blue" >返回</a>
				<input type="button" style="margin-left:40px" class="btn blue" value="提交" onclick="save();">
				</center>
			</div>
			</form>
		</div>
		
	</div>
	
	<script src="${cdnPath}/js/product/new_edit.js?v=${VERSION_NO}"></script>
	
<#include "/sys/bottom.ftl">