<form action="#" id="jobinfoinput_form" class="form-horizontal">
  <div class="row-fluid">
  		<div class="control-group span4">
			<input type="hidden" value="${applyLoanNo!''}" name="applyLoanNo" />
			
				<#if memberType == 1>
					
					<label class="control-label">工作单位全称<span class="required">*</span></label>
			
					<div class="controls">
						<input type="input"  maxlength = "100" class="span9 m-wrap js_input" value="${(jobInfo.unitName)!}" name="unitName" />						
					</div>
					
			    <#else>
			    
			    	<label class="control-label">工作单位全称</label>
			
					<div class="controls">
						<input type="input"  maxlength = "100" class="span9 m-wrap" value="${(jobInfo.unitName)!}" name="unitName" />						
					</div>
			    		
			    </#if>
				
		</div>
		
		<div class="control-group span4">
	
				<#if memberType == 1>
					
					<label class="control-label">公司成立时间<span class="required">*</span></label>
			
					<div class="controls">
	
						<div class="input-append date date-picker" data-date="${(queryDto.applyTimeStart?string("yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
							<input name="unitCreateDate" class="m-wrap span9 date-picker  js_input" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(jobInfo.unitCreateDate?string("yyyy-MM-dd"))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
						</div>
	
					</div>
					
			    <#else>
			    
			    	<label class="control-label">公司成立时间</label>
			
					<div class="controls">
	
						<div class="input-append date date-picker" data-date="${(queryDto.applyTimeStart?string("yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
							<input name="unitCreateDate" class="m-wrap span9 date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(jobInfo.unitCreateDate?string("yyyy-MM-dd"))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
						</div>
	
					</div>
			    		
			    </#if>
	
		</div>
		
		<div class="control-group span4">
	
				<#if memberType == 1>
					
					<label class="control-label">单位人数<span class="required">*</span></label>
			
					<div class="controls">
						<input type="text" maxlength = "8" class="span9 m-wrap js_input" value="${(jobInfo.unitNums)!}" name="unitNums" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" />					
					</div>
					
			    <#else>
			    
			    	<label class="control-label">单位人数</label>
			
					<div class="controls">
						<input type="text" maxlength = "8" class="span9 m-wrap" value="${(jobInfo.unitNums)!}" name="unitNums" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" />						
					</div>
			    		
			    </#if>
	
		</div>
	</div>
	<div class="row-fluid">
	
		<div class="control-group span4">

				<#if memberType == 1>
					
					<label class="control-label">单位地址<span class="required">*</span></label>
			
					<div class="controls">
						<input type="text" class = "span9 m-wrap js_input" id = "companyAddress" name="companyAddress" placeholder = "请选择" value="${(jobInfo.companyAddress)!''}" readonly />					
					</div>
					
			    <#else>
			    
			    	<label class="control-label">单位地址</label>
			
					<div class="controls">																			      				
						<input type="text" class = "span9 m-wrap" id = "companyAddress" name="companyAddress" placeholder = "请选择" value="${(jobInfo.companyAddress)!''}" readonly />						
					</div>
			    		
			    </#if>

		</div>
	
		<div class="control-group span4">
	
				<label class="control-label">详细地址</label>
	
				<div class="controls">
	
					<input type="input" maxlength = "200" class="span9 m-wrap" value="${(jobInfo.detailAddr)!}" name="detailAddr" />
	
				</div>
	
		</div>
		
		<div class="control-group span4">
	
			<#if memberType == 1>
				
				<label class="control-label">单位电话<span class="required">*</span></label>
		
				<div class="controls">
					<input placeholder = "单位电话" type="input"  maxlength = "15" class="span6 m-wrap js_input" value="${(jobInfo.unitTelephone)!}" name="unitTelephone" />
				
					<input placeholder = "分机号"  type="input"  maxlength = "15" class="span3 m-wrap" value="${(jobInfo.extensionNum)!}" name="extensionNum" />					
				</div>
				
		    <#else>
		    
		    	<label class="control-label">单位电话</label>
		
				<div class="controls">
					<input placeholder = "单位电话"  type="input"  maxlength = "15" class="span6 m-wrap" value="${(jobInfo.unitTelephone)!}" name="unitTelephone" />
				
					<input placeholder = "分机号"  type="input"  maxlength = "15" class="span3 m-wrap" value="${(jobInfo.extensionNum)!}" name="extensionNum" />					
				</div>
		    		
		    </#if>
		</div>
	</div>
	
	<div class="row-fluid">
	  		<div class="control-group span4">

		    	<label class="control-label">行业类别</label>
		    	<div class="controls">
					<select name="businessType" class="m-wrap span9" data-id="${(jobInfo.businessType)!"0"}">
						<option value="">请选择</option>
					</select>
				</div>

		</div>
	  		
	  		<div class="control-group span4">
	
				<label class="control-label">单位性质</label>
				<div class="controls">
					<select name="companyNature" class="m-wrap span9" data-id="${(jobInfo.companyNature)!"0"}" onchange="initPositionsCode()">
						<option value="0">请选择</option>
					</select>
				</div>
			</div>
			
			<div class="control-group span4">
	
				<label class="control-label">担任职务</label>
	
				<div class="controls">
	
					<select name="positionsCode" class="m-wrap span9" data-id="${(jobInfo.positionsCode)!"0"}">
						<option value="0">请选择</option>
					</select>
	
				</div>
	
			</div>
	</div>
	<div class="row-fluid">
			<div class="control-group span4">

				<label class="control-label">进入公司时间</label>
	
				<div class="controls">
	
					<div class="input-append date date-picker" data-date="${(queryDto.accessCompanyDate?string("yyyy-MM-dd"))!}" data-date-format="yyyy-mm-dd" data-date-viewmode="years">
					<input name="accessCompanyDate" class="span9 m-wrap date-picker" size="16" type="text" data-date-format="yyyy-mm-dd"  value="${(jobInfo.accessCompanyDate?string("yyyy-MM-dd"))!}"/><span class="add-on"><i class="icon-calendar"></i></span>
					</div>		
	
				</div>

			</div>
	
			<div class="control-group span4">
	
				<label class="control-label">每月发薪日<span
					class="required">*</span></label>
	
				<div class="controls">
	
					<input type="input"  maxlength = "2"  class="span9 m-wrap js_input" value="${(jobInfo.salaryDay)!}" name="salaryDay" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" />
	
				</div>
	
			</div>
			
			<div class="control-group span4">
	
				<label class="control-label">发薪方式<span
					class="required">*</span></label>
	
				<div class="controls">
	
					<select name="salaryWay" class = "span9 m-wrap js_input">
						<option value="">请选择</option>
						<option value="0">银行卡</option>
						<option value="1">现金</option>
						<option value="2">银行卡+现金</option>
					</select>
					
				</div>
	
			</div>
	</div>
		
	<div class="row-fluid">
	  					
		<div class="control-group span4">

			<label class="control-label">月基本工资<span
				class="required">*</span></label>

			<div class="controls">

				<input  type="input"  maxlength = "10"  class="span9 m-wrap js_input" value="${(jobInfo.monthlySalary)!}" name="monthlySalary" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"  />

			</div>

		</div>
	  	<div class="control-group span4">
	
			<label class="control-label">其他收入(月)</label>

			<div class="controls">

				<input type="input"  maxlength = "10"  class="span9 m-wrap" value="${(jobInfo.otherIncomes)!}" name="otherIncomes" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" />

			</div>

		</div>
		<div class="control-group  span4">
	
				<label class="control-label"><span
					class="required"></span></label>
	
				<div class="controls">
	
				</div>
	
			</div>	
	</div>
		<div class="row-fluid">
			<div class="control-group span4">

		    	<label class="control-label">副业行业类别</label>
		    	<div class="controls">
					<select name="otherBusinessType" class="m-wrap span9" data-id="${(jobInfo.otherBusinessType)!"0"}">
						<option value="">请选择</option>
					</select>
				</div>
		    		
			</div>
			
			<div class="control-group span4">
	
				<label class="control-label">副业单位性质</label>
	
				<div class="controls">
	
					<select name="otherCompanyNature" class="m-wrap span9" data-id="${(jobInfo.otherCompanyNature)!"0"}" onchange="initOtherPositionsCode()">
						<option value="0">请选择</option>
					</select>
	
				</div>
	
			</div>
			
			<div class="control-group span4">
	
				<label class="control-label">副业职务</label>
	
				<div class="controls">
                    <select name="otherPositionsCode" class="m-wrap span9" data-id="${(jobInfo.otherPositionsCode)!"0"}">
                        <option value="0">请选择</option>
                    </select>	
				</div>
	
			</div>
			
		</div>
</form>
<script>
$(function () {
	showCitys("#companyAddress","${serverPath}");
	initCompanyNature();
	initProductData();
});

function initPositionsCode(){
    var parentCode = $("select[name='companyNature']").find("option:selected").attr('data-code');
    if(parentCode == '' || parentCode == '0' || typeof(parentCode) == "undefined"){
        var select_ = $("select[name='positionsCode']");
        select_.find("option:gt(0)").remove();    
        return true;
    }
    $.ajax({
        url : serverPath + "/node/selectNodeList.htm",
        type : "post",
        data : {
            "parentCode" : parentCode,
            "Time" : new Date().getMilliseconds()
        },
        dataType : "json",
        success : function(result) {
            if (result.code == 0) {
                // 清空除第一条内容的外的其它数据
                var select_ = $("select[name='positionsCode']");
                select_.find("option:gt(0)").remove();
                for (var i = 0; i < result.data.length; i++) {
                    var isSelected = result.data[i].nodeCode == select_
                            .attr('data-id') ? "selected='selected'" : "";
                    select_.append("<option " + isSelected + " value='"
                            + result.data[i].nodeCode + "'>"
                            + result.data[i].nodeName + "</option>");
                }
            } else {
                BootstrapDialog.alert(result.message);
            }
        },
        error : function() {
            BootstrapDialog.alert("操作失败1");
        }
    });
}

function initOtherPositionsCode(){
    var parentCode = $("select[name='otherCompanyNature']").find("option:selected").attr('data-code');
    if(parentCode == '' || parentCode == '0' || typeof(parentCode) == "undefined"){
        var select_ = $("select[name='otherPositionsCode']");
        select_.find("option:gt(0)").remove();    
        return true;
    }    
    $.ajax({
        url : serverPath + "/node/selectNodeList.htm",
        type : "post",
        data : {
            "parentCode" : parentCode,
            "Time" : new Date().getMilliseconds()
        },
        dataType : "json",
        success : function(result) {
            if (result.code == 0) {
                // 清空除第一条内容的外的其它数据
                var select_ = $("select[name='otherPositionsCode']");
                select_.find("option:gt(0)").remove();
                for (var i = 0; i < result.data.length; i++) {
                    var isSelected = result.data[i].nodeCode == select_
                            .attr('data-id') ? "selected='selected'" : "";
                    select_.append("<option " + isSelected + " value='"
                            + result.data[i].nodeCode + "'>"
                            + result.data[i].nodeName + "</option>");
                }
            } else {
                BootstrapDialog.alert(result.message);
            }
        },
        error : function() {
            BootstrapDialog.alert("操作失败2");
        }
    });
}


function initCompanyNature() {  
    var url = serverPath + "/node/selectNodeList.htm";
    var data = {
        "parentCode" : "unitPro",
        "Time" : new Date().getMilliseconds()
    };
    $.getJSON(url, data, function(result) {
        if (result.code == 0) {
            // 清空除第一条内容的外的其它数据
            var select_ = $("select[name='companyNature']");
            select_.find("option:gt(0)").remove();
            for (var i = 0; i < result.data.length; i++) {
                var isSelected = result.data[i].nodeCode == select_
                        .attr('data-id') ? "selected='selected'" : "";
                select_.append("<option " + isSelected + " value='"
                        + result.data[i].nodeCode + "' data-code='" + result.data[i].nodeCode + "'>"
                        + result.data[i].nodeName + "</option>");
            }
            var test = $("select[name='companyNature']").find("option:selected").attr('data-code');
            debugger;
            if(test != '' && test != '0' && typeof(test)!="undefined"){
                initPositionsCode();
            }
           
            var select_ = $("select[name='otherCompanyNature']");
            select_.find("option:gt(0)").remove();
            for (var i = 0; i < result.data.length; i++) {     
                var isSelected = result.data[i].nodeCode == select_
                        .attr('data-id') ? "selected='selected'" : "";
                select_.append("<option " + isSelected + " value='"
                        + result.data[i].nodeCode + "' data-code='" + result.data[i].nodeCode + "'>"
                        + result.data[i].nodeName + "</option>");
            }
            var test1 = $("select[name='otherCompanyNature']").find("option:selected").attr('data-code');
            if(test1 != '' && test1 != '0' && typeof(test1)!="undefined"){
            initOtherPositionsCode(); 
            }
        } else {
            BootstrapDialog.alert(result.message);
        }
    });       
}

function initProductData() {
    var url = serverPath + "/node/selectNodeList.htm";
    var data = {
        "parentCode" : "industryType",
        "Time" : new Date().getMilliseconds()
    };
    $.getJSON(url, data, function(result) {
        if (result.code == 0) {
            // 清空除第一条内容的外的其它数据
            
            var select_ = $("select[name='businessType']");
            select_.find("option:gt(0)").remove();
            for (var i = 0; i < result.data.length; i++) {
                
                var isSelected = result.data[i].id == select_
                        .attr('data-id') ? "selected='selected'" : "";
                select_.append("<option " + isSelected + " value='"
                        + result.data[i].id + "'>"
                        + result.data[i].nodeName + "</option>");
            }
            
            var select_ = $("select[name='otherBusinessType']");
            select_.find("option:gt(0)").remove();
            for (var i = 0; i < result.data.length; i++) {
                
                var isSelected = result.data[i].id == select_
                        .attr('data-id') ? "selected='selected'" : "";
                select_.append("<option " + isSelected + " value='"
                        + result.data[i].id + "'>"
                        + result.data[i].nodeName + "</option>");
            }
        } else {
            BootstrapDialog.alert(result.message);
        }
    });  
}


$("select[name='salaryWay']").val("${(jobInfo.salaryWay)!}");


 Search.init();
 </script>