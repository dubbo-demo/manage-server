var dataStr = {};

//将表单序列化成json格式的数据(但不适用于含有控件的表单，例如复选框、多选的select)
(function($){
    $.fn.serializeJson = function(){
        var jsonData1 = {};
        var serializeArray = this.serializeArray();
        // 先转换成{"id": ["12","14"], "name": ["aaa","bbb"], "pwd":["pwd1","pwd2"]}这种形式
        $(serializeArray).each(function () {
            if (jsonData1[this.name]) {
                if ($.isArray(jsonData1[this.name])) {
                    jsonData1[this.name].push(this.value);
                } else {
                    jsonData1[this.name] = [jsonData1[this.name], this.value];
                }
            } else {
                jsonData1[this.name] = this.value;
            }
        });
        // 再转成[{"id": "12", "name": "aaa", "pwd":"pwd1"},{"id": "14", "name": "bb", "pwd":"pwd2"}]的形式
        var vCount = 0;
        // 计算json内部的数组最大长度
        for(var item in jsonData1){
            var tmp = $.isArray(jsonData1[item]) ? jsonData1[item].length : 1;
            vCount = (tmp > vCount) ? tmp : vCount;
        }

        if(vCount > 1) {
            var jsonData2 = new Array();
            for(var i = 0; i < vCount; i++){
                var jsonObj = {};
                for(var item in jsonData1) {
                		jsonObj[item] = jsonData1[item][i];
                }
                jsonData2.push(jsonObj);
            }
            return JSON.stringify(jsonData2);
        }else{
            return "[" + JSON.stringify(jsonData1) + "]";
        }
    };
})(jQuery);

$(function() {
	getNode("#prodType","proType");
	getRowKey();
	if(clientType == "1") {
		getJXLRowKey();
	}
});

/**
 * 
 * @param xmlId 目标节点
 * @param parentCode 父id
 */
function getNode(xmlId,parentCode) {
	var url = serverPath+"/reception/showProduct.htm";
	var data = {
		"parentCode" : parentCode,
	};
	$.getJSON(url, data, function(result) {
		$(xmlId).empty();
		var resultData = result.data;
		$(xmlId).append("<option value=''>请选择</option>");
		for (var i = 0; i < resultData.length; i++) {
			if($(xmlId).data("value") == resultData[i].id) {
				$(xmlId).append(
						"<option selected value='" + resultData[i].id + "'>"
								+ resultData[i].nodeName + "</option>");
			} else {
			$(xmlId).append(
					"<option value='" + resultData[i].id + "'>"
							+ resultData[i].nodeName + "</option>");
			}
		}
	});
}

function queryFileForm() {
	var url = serverPath + "/productFile/fileUpload.htm";
	window.open(url + '?isView=0&applyLoanNo=' + $("#applyLoanNo").val());
}

function addFileForm() {
	var url = serverPath + "/productFile/fileUpload.htm";
	window.open(url + '?isView=1&applyLoanNo=' + $("#applyLoanNo").val());
}

function toStatus(e) {
	$("#"+$(e).data("name")).val($(e).val());
}

function contactsSave(){
	var remarks = $("textarea[name='remark']");
	if(remarks.length == 0) {
		BootstrapDialog.alert('第三方信息需要配置产品附件类型!');
		return false;
	}
	var i = 0;
	var isgo = $.each(remarks, function(index, data) {
		if(ChkUtil.isNull(data.value)) {
			BootstrapDialog.alert('第三方信息备注不能为空!');
			return false;
		}
		if(index == (remarks.length-1)) {
			
		    $.ajax({
				url : serverPath+'/productFile/checkFile.htm',
				type : 'get',
				data :{
					"applyLoanNo":$("#applyLoanNo").val()
				},
				dataType : 'json',
				contentType : "application/json",
				success : function(res) {
					if(res.data) {
						goSave();
					} else {
						BootstrapDialog.alert('检查附件是否上传全!');
					}
				},
				error : function() {
					BootstrapDialog.alert('检查附件接口失败!');
				}
			});
		}
	});
}

function goSave() {
	var auditTaskJson = $("#tab").serializeJson();
    var auditThridInfoJson = $("#tab2").serializeJson();
    var jsonStr = "[{auditTask:"+auditTaskJson+"},"+"{auditThridInfos:"+auditThridInfoJson+"}]";
	$.ajax({
		url : serverPath+'/audit/base/auditThridInfoSave.htm',
		type : 'post',
		data :jsonStr,
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

var jxl =  {};

function getRowKey() {
	var url = serverPath + "/audit/base/queryThridDataApp.htm";
	var data = {
			"userid" : userid
	};
	var options = {
		url : url,
		type : 'post',
		dataType : 'json',
		data : data,
		success : function(data) {
			dataStr = data;
			initThridDialog();
		}
	};
	$.ajax(options);
}

/**
 * 聚信立
 */
function getJXLRowKey() {
	var url = serverPath + "/audit/base/queryJXLData.htm";
	var data = {
		 "userid" : userid
	};
	var options = {
		url : url,
		type : 'post',
		dataType : 'json',
		data : data,
		success : function(data) {
			jxl = data;
			showJXLView(data);
		}
	};
	$.ajax(options);
}

function showJXLView(e) {
	if($.isEmptyObject(e) && e.retcode == '000000') {
		return true;
	} else {
		var jxlData = $.parseJSON(e.data);
		if($.isEmptyObject(jxlData)) {
			return true;
		}
		var jxlTable = $("#jxl_dialog_content");
		jxlTable.append(
				"<tr ><td><span class='control-label'>是否实名认证</span></td><td><span class='control-label'>" 
				+ jxlData.reliability +"</span></td></tr>");
		jxlTable.append(
				"<tr ><td><span class='control-label'>使用时长</span></td><td><span class='control-label'>" 
				+ jxlData.usetime +"</span></td></tr>");
		jxlTable.append(
				"<tr ><td><span class='control-label'>黑名单</span></td><td><span class='control-label'>" 
				+ jxlData.blacklist +"</span></td></tr>");
		jxlTable.append(
				"<tr ><td><span class='control-label'>长时间关机</span></td><td><span class='control-label'>" 
				+ jxlData.closephone +"</span></td></tr>");
		jxlTable.append(
				"<tr ><td><span class='control-label'>非正常机构通话</span></td><td><span class='control-label'>" 
				+ jxlData.errcall +"</span></td></tr>");
		jxlTable.append(
				"<tr ><td><span class='control-label'>贷款类号码联系情况</span></td><td><span class='control-label'>" 
				+ jxlData.fincall +"</span></td></tr>");
		jxlTable.append(
				"<tr ><td><span class='control-label'>出行数据</span></td><td><span class='control-label'>" 
				+ jxlData.trip +"</span></td></tr>");
		jxlTable.append(
				"<tr ><td><span class='control-label'>有无直系亲属通话记录</span></td><td><span class='control-label'>" 
				+ jxlData.contact +"</span></td></tr>");
	}
}

function productTypeHtml(data) {
	var shtml = "";
	shtml += '<div class="row-fluid"><div class="control-group span3 ">';
	shtml += '<label class="control-label">' + data.directoryName + '</label>';
	shtml += '<div class="controls"><label class="radio">';
	shtml += '<input type="radio" name="optionsRadios2" value="option1" />正常</label>';
	shtml += '<label class="radio">';
	shtml += '<input type="radio" name="optionsRadios2" value="option2" checked />异常</label>';
	shtml += '</div></div>';
	shtml += '<div class="control-group span6 ">';
	shtml += '<label class="control-label"><span class="required">*</span>备注</label>';
	shtml += '<div class="controls">';
	shtml += '<textarea class="span9 m-wrap" rows="1" name="loanPurpose" maxlength="300">${reception.loanPurpose!}</textarea>';
	shtml += '</div></div><div class="control-group span2 ">';
	shtml += '<p style="padding: 6px 6px !important; line-height: 20px;">';
	shtml += '<a href="javascript:void(0);" class="m-wrap">查看图片</a>';
	shtml += '</p></div></div>';
	return shtml;
}

function initThridDialog() {
	if(dataStr.code == 1) {
		return;
	}
	var jsonData = $.parseJSON(dataStr.data);
	if($.isEmptyObject(jsonData)) {
		return true;
	}
	var dataTemp,dataTemp2;
	// 同盾
	var tdData = jsonData.TD;
	if(!$.isEmptyObject(tdData)) {
		dataTemp = {},dataTemp2={};
		$.each(tdData, function(index, data) {
			$("#td_dialog_content").append(
					"<tr><td><span class='control-label'>" 
					+ data.group + "</span></td><td><span class='control-label'>" + data.item_name + "</span></td></tr>");
			dataTemp = data.item_detail;
			if(!$.isEmptyObject(dataTemp)) {
				$("#td_dialog_content").append(
						"<tr><td><span class='control-label'></span></td><td><span class='control-label'>"
						+ JSON.stringify(dataTemp)+"</span></td></tr>");
			}
//					$.each(dataTemp.frequency_detail_list, function(index2, data2) {
//						$("#td_dialog_content").append(
//								"<tr><td><span class='control-label'></span></td><td><span class='control-label'>"
//								+ data2.detail+"</span></td></tr>");
//						if(data2.data != null) {
//							$("#td_dialog_content").append(
//									"<tr><td><span class='control-label'></span></td><td><span class='control-label'>" 
//									+ data2.data+"</span></td></tr>");
//						}
//					});
		});
	}
	// 百融
	var brData = jsonData.BR;
	if(!$.isEmptyObject(brData)) {
		$("#br_dialog_content").append(
				"<tr><td><span class='control-label'>" 
				+ "评分" + "</span></td><td><span class='control-label'>" + brData.brcreditpoint + "</span></td></tr>");
		//$("#br_a").html(brData.brcreditpoint);
		var brspecial = brData.specialList_c;
		if(null != brData && null != brspecial) {
			for(var key in brspecial)
			{
				$("#br_dialog_content").append(
						"<tr><td><span class='control-label'>"
						+"通过"+getspecialName(key)+"查询"+getBrDesc(brspecial,key)+ "</span></td><td><span class='control-label'></span></td></tr>");
			}
		}
	}
	
	
	//前海
	dataTemp = {},dataTemp2={};
	var qhData = jsonData.QH;
	if(!$.isEmptyObject(qhData)) {
		$.each(qhData, function(key, value) {
			//		 this;      //this指向当前属性的值
			//	     name;      //name表示Object当前属性的名称
			//	     value;     //value表示Object当前属性的值
				$("#qh_dialog_content").append(
						"<tr><td><span class='control-label'>" + getqhName(key) + "</span></td></tr>");
		});
	}
	
	//宜信
	var yxData = jsonData.YX;
	if(!$.isEmptyObject(yxData)) {
		var creditScoreT = yxData.zhichengscore.creditScore;
//		if(isNaN(creditScoreT)){
//			creditScoreT = "";
//		}
//		$("#yx_a").html(creditScoreT);
		$("#yx_dialog_content").append(
				"<tr><td><span class='control-label'>评分:" 
				+ yxData.zhichengscore.creditScore + "</span></td><td><span class='control-label'>" 
				+ yxData.zhichengscore.breakcontractrate 
				+"</span></td><td><span class='control-label'></span></td>"+ "</tr>");
		$("#yx_dialog_content").append(
				"<tr><td><span class='control-label'>逾期条数:"+ yxData.overdue.overdueCount + "</span></td><td><span class='control-label'>" 
				+ "逾期90天的条数:"+ yxData.overdue.overdueNinetyDaysCount+ "</span></td><td><span class='control-label'>" 
				+ "逾期180天的条数:"+ yxData.overdue.overdueOneeightyDaysCount +"</span></td></tr>");
		if(!$.isEmptyObject(yxData.querytimes)) {
			$("#yx_dialog_content").append(
					"<tr class='portlet green'><td><span class='control-label'>日期</span></td><td><span class='control-label'>" 
					+ "查询原因</span></td><td><span class='control-label'>" 
					+ "查询机构</span></td></tr>");
			$.each(yxData.querytimes, function(index, data) {
				$("#yx_dialog_content").append(
						"<tr><td><span class='control-label'>" 
						+ data.time + "</span></td><td><span class='control-label'>" 
						+ data.reason+ "</span></td><td><span class='control-label'>" 
						+ data.organization + "</span></td></tr>");
			});
		}
		if(!$.isEmptyObject(yxData.queryrecords)) {
			$("#yx_dialog_content").append(
					"<tr class='portlet green'><td><span class='control-label'>日期</span></td><td><span class='control-label'>" 
					+ "借款金额范围</span></td><td><span class='control-label'>" 
					+ "审批结果</span></td></tr>");
			$.each(yxData.queryrecords, function(i, value) {
				$("#yx_dialog_content").append(
						"<tr><td><span class='control-label'>" 
						+ value.time + "</span></td><td><span class='control-label'>" 
						+ value.loanAmount+ "</span></td><td><span class='control-label'>" 
						+ value.approveorrefuse + "</span></td></tr>");
			});
		}
	}
		
}

var qhJSON = {"3000010":"被执行人","3000030":"违约","3000050":"失信被执行人","3000060":"逾期31-60天","3000070":"逾期61-90天","3000090":"逾期91-180天"};
function getqhName(e) {
	return qhJSON[e];
}

function getBrDesc(brjson,e) {
	for(var key in brjson[e])
	{
		return getspecialFirstName(key.split("_")[0])+getspecialTwoName(key.split("_")[1]);
	}
}

var specialJSON = {"id":"身份证","cell":"手机号","gid":"进行核查"};
function getspecialName(e) {
	return specialJSON[e];
}

var specialFirstJSON = {"bank":"银行","p2p":"p2p","phone":"通讯运营商","insurance":"保险","court":"法院","credit":"征信"};
function getspecialFirstName(e) {
	return specialFirstJSON[e];
}

var specialTwoJSON = {"bad":"不良","overdue":"短时逾期","fraud":"欺诈","lost":"失联","refuse":"拒绝","Executed":"执行人"};
function getspecialTwoName(e) {
	return specialTwoJSON[e];
}