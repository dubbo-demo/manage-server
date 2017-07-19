//身份证号   
jQuery.validator.addMethod("idCard", function(value, element) {
	return this.optional(element) || (typeof ChkUtil == "undefined") ? false
			: ChkUtil.isIdCard(value);
}, "请正确填写身份证号");

// 手机号
jQuery.validator.addMethod("phone", function(value, element) {
	if (value == "") {
		return true;
	}
	return this.optional(element) || (typeof ChkUtil == "undefined") ? false
			: ChkUtil.isMobile(value);
}, "请正确填写手机号");
// 英文匹配
jQuery.validator.addMethod("english", function(value, element) {
	if (value == "") {
		return true;
	}
	return this.optional(element) || (typeof ChkUtil == "undefined") ? false
			: ChkUtil.isEnglish(value);
}, "请输入英文字符");

$.validator.addMethod("positiveinteger", function(value, element) {
	if (value == "") {
		return true;
	}
	var aint = parseInt(value);
	return aint > 0 && (aint + "") == value;
}, "请输入正整数");

$.validator.addMethod("bmId", function(value, element) {
	return this.optional(element) || value != '';
}, "请选择业务经理");
$.validator.addMethod("changeCity", function(value, element) {
	return this.optional(element) || value != '请选择';
}, "请选择");
$.validator.addMethod("cusomerServiceId", function(value, element) {
	return this.optional(element) || value != '';
}, "请选择客服");
$.validator.addMethod("memberType", function(value, element) {
	return this.optional(element) || value != '';
}, "请选择客户类型");
$.validator.addMethod("infoChannel", function(value, element) {
	return this.optional(element) || value != '';
}, "请选择信息获知渠道");

$.validator.addMethod("loanUse", function(value, element) {
	return this.optional(element) || value != '';
}, "请选择借款用途");
$.validator.addMethod("loanPeriods", function(value, element) {
	return this.optional(element) || value != '0';
}, "请选择期数");
$.validator.addMethod("mainCauseNo", function(value, element) {
	return this.optional(element) || value != '0';
}, "请选择拒绝原因");
$.validator.addMethod("secondCauseNo", function(value, element) {
	return this.optional(element) || value != '0';
}, "请选择次要原因");
$.validator.addMethod("approveResult", function(value, element) {
	return this.optional(element) || value != '0';
}, "请选择审核结果");
$.validator.addMethod("productType", function(value, element) {
	return this.optional(element) || value != '0';
}, "请选择产品");
$.validator.addMethod("periodsUnit", function(value, element) {
	return this.optional(element) || value != '0';
}, "请选择期数单位");
// 判断浮点型 ,不做必要验证
jQuery.validator.addMethod("isFloat", function(value, element) {
	if ("" == value) {
		return true;
	}
	return (this.optional(element) || /^[-\+]?\d+(\.\d+)?$/.test(value))
			&& value >= 0;
}, "只能是正数");
jQuery.validator.addMethod("isInt", function(value, element) {
	if ("" == value) {
		return true;
	}
	return (this.optional(element) || /^[0-9]*[0-9][0-9]*$/.test(value));
}, "只能是正整数");
jQuery.validator.addMethod("Floatlen", function(value, element, param) {
	if ("" == value) {
		return true;
	}
	var reg = eval("/^\\d{1," + param[0] + "}(\\.\\d{1," + param[1] + "})?$/");

	return (this.optional(element) || reg.test(value));
}, "请输入正确的数据");
jQuery.validator.addMethod("compare", function(value, element, param) {
	var targetValue1 = $("input[name='"+param[0]+"']").val();
	var targetValue2 = $("input[name='"+param[1]+"']").val();
	return (this.optional(element) || parseFloat(targetValue1) > parseFloat(targetValue2));
}, "必须大于下限");


$.validator.addMethod("teamManager", function(value, element) {
	if($('#bmId').val() == null || $('#bmId').val() == '') {
		return false;
	}
	var b = true;
	$.ajax({
		type : "post",//请求方式
		url : serverPath+'/reception/base/isTeamEmploy.htm',//请求url
		data : { bmId :$('#bmId').val()},//请求参数
		dataType : 'json',//返回数据类型
		async : false, //设为false就是同步请求
		cache : false,//是否缓存，默认true
		success : function (res) {//成功事件
			if (res.code == '0') {
			} else {
				b =  false;
			}
		},
		error : function (XMLHttpRequest, textStatus, errorThrown) {//失败事件
			// 通常情况下textStatus和errorThown只有其中一个有值
			this; // the options for this ajax request
		}
	});
	return b;
}, "请将业务员与团队经理关系绑定！");