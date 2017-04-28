<!-- edit form -->
    <hr>
		<div>
			<h4 style="text-align:center">
				<strong>审核结果</strong>
			</h4>
		</div>
	<hr>

	<div class="row-fluid">
		<div class="control-group span3 ">
			<label class="control-label">综合意见</label>
			<div class="controls">
				<span class="text">
				<#if applyInfo.state == 20 && applyInfo.subState == 10>
				拒绝
				<#else>
				通过
				</#if>
				</span>
			</div>
		</div>
		<div class="control-group span3 ">
		</div>
		<div class="control-group span3 ">
			<label class="control-label">审核时间</label>
			<div class="controls">
				<span class="text"><#if (applyInfo.opinionTime)??>${applyInfo.opinionTime?datetime}</#if></span>
			</div>
		</div>
	</div>
	
	<div class="row-fluid">
		<div class="control-group ">
			<label class="control-label">综合意见描述</label>
			<div class="controls">
				<textarea rows="3" name="remark" class="m-wrap span9" readonly>${(applyInfo.applyRemark)!}</textarea>
			</div>
		</div>
	</div>

<#if auditInfo??>
	<div class="row-fluid">
		<div class="control-group span3 ">
			<label class="control-label">${(auditInfo.auditStageName)!}审核</label>
			<div class="controls">
				<span class="text">${(auditInfo.auditResultDesc)!}
				</span>
			</div>
		</div>
		<#if states['BACK_INIT'] == auditInfo.auditResult>
		<div class="control-group span3 ">
			<label class="control-label">退回原因</label>
			<div class="controls">
				<span class="text" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="${(auditInfo.backReasonDesc)!}">${(auditInfo.backReasonDesc)!}
				</span>
			</div>
		</div>
		
		<#elseif states['Director_REFUSE'] == auditInfo.auditResult ||states['Manger_REFUSE'] == auditInfo.auditResult || states['Last_REFUSE'] == auditInfo.auditResult>
		<div class="control-group span3 ">
			<label class="control-label">原因编号</label>
			<div class="controls">
				<span class="text" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="${(auditInfo.lesserCause)!}">${(auditInfo.lesserCause)!}
				</span>
			</div>
		</div>			
		<#else>
		<div class="control-group span3 ">
		</div>
		</#if>	
				
		<div class="control-group span3 ">
			<label class="control-label">审核时间</label>
			<div class="controls">
				<span class="text"><#if (auditInfo.createTime)??>${auditInfo.createTime?datetime}</#if></span>
			</div>
		</div>
	</div>
	<div class="row-fluid">
		<div class="control-group ">
			<label class="control-label">${(auditInfo.auditStageName)!}审核意见</label>
			<div class="controls">
				<textarea rows="3" name="remark" class="m-wrap span9" readonly>${(auditInfo.publicRemark)!}</textarea>
			</div>
		</div>
	</div>
</#if>
<div id="jieAnDiv" style="display: none;">
	<div class="row-fluid">
		<div class="control-group span3 ">
			<label class="control-label">捷安征信拒绝</label>
			<div class="controls">
					<span class="text" id="titleType" >
					</span>
			</div>
		</div>
	</div>
	<div class="row-fluid">
		<div class="control-group ">
			<label class="control-label">捷安征信审核意见</label>
			<div class="controls">
				<textarea rows="3" name="jieAnRemark" id="jieAnRemark" class="m-wrap span9" readonly></textarea>
			</div>
		</div>
	</div>
	</div>
<script>
	var memberId = ${(member.id)!}
    $(function() {
        getJieAn();
    });

    var dataJieStr={};
    function getJieAn() {
        var url = serverPath+"/dealApply/queryJieAnData.htm";
        var data = {
            "userid" : memberId
        };
        var options = {
            url : url,
            type : 'post',
            dataType : 'json',
            data : data,
            success : function(data) {
                dataJieStr = data;
                initJieAnDialog();
            }
        };
        $.ajax(options);
    }

    function initJieAnDialog() {
        if($.isEmptyObject(dataJieStr)) {
            return ;
        }

        if(dataJieStr.code == 1) {
            return;
        }
        var jsonData = $.parseJSON(dataJieStr.data);
        if($.isEmptyObject(jsonData)) {
            return true;
        }
        if(!$.isEmptyObject(jsonData.crime)) {
            // 拼接犯罪记录
            var crime = $.parseJSON(jsonData.crime);
            if(!$.isEmptyObject(crime)) {
                $("#titleType").html("犯罪记录检查");
                $("#jieAnRemark").html(getJieAnStr(crime));
            }
        }
        if(!$.isEmptyObject(jsonData.unhealthy)) {
            // 拼接不良记录
            var unhealthy = $.parseJSON(jsonData.unhealthy);
            if(!$.isEmptyObject(unhealthy)) {
                $("#titleType").html("不良记录检查");
                $("#jieAnRemark").html(unhealthy.RISK_SORT);
            }
        }
        $("#jieAnDiv").css("display","block");
    }

    function getJieAnStr(crime) {
        var jieanStr = "";
        if(!$.isEmptyObject(crime.checkDesc)) {
            jieanStr = "刑事描述:"+crime.checkDesc;
        }
        if(!$.isEmptyObject(crime.caseTime) && "" != jieanStr) {
            jieanStr = jieanStr + "/案发时间:" + crime.caseTime;
        } else if(!$.isEmptyObject(crime.caseTime)) {
            jieanStr = crime.caseTime;
        }
        if(!$.isEmptyObject(crime.caseType) && "" != jieanStr) {
            jieanStr = jieanStr + "/案件类别:" + crime.caseType;
        } else if(!$.isEmptyObject(crime.caseType)) {
            jieanStr = crime.caseType;
        }
        if(!$.isEmptyObject(crime.caseSource) && "" != jieanStr) {
            jieanStr = jieanStr + "/涉案类型:" + crime.caseSource;
        } else if(!$.isEmptyObject(crime.caseSource)) {
            jieanStr = crime.caseSource;
        }
        return jieanStr;
    }
</script>	