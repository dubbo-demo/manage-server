//定义一些常量，用于维护负债标准的基础数据nodeCode
var Class = (function() {
	var constants = {// 定义了两个常量
		earningsType1 : '.earningsType1',
		earningsType2 : '.earningsType2',
		earningsType3 : '.earningsType3',
		earningsType4 : '.earningsType4',
		earningsType5 : '.earningsType5',
		earningsType6 : '.earningsType6',
		earningsType7 : '.earningsType7',
		earningsType8 : '.earningsType8',
		earningsType9 : '.earningsType9',
		earningsType10 : '.earningsType10',
		earningsType11 : '.earningsType11',
		earningsType12 : '.earningsType12',
		earningsType13 : '.earningsType13',
		earningsType14 : '.earningsType14'
	}
	var Test = {};
	Test.getConstant = function(name) {// 获取常量的方法
		return constants[name];
	}
	return Test;
})();

// 将表单序列化成json格式的数据(但不适用于含有控件的表单，例如复选框、多选的select)
(function($) {
	$.fn.serializeJson = function() {
		var jsonData1 = {};
		var serializeArray = this.serializeArray();
		// 先转换成{"id": ["12","14"], "name": ["aaa","bbb"],
		// "pwd":["pwd1","pwd2"]}这种形式
		$(serializeArray).each(
				function() {
					if (jsonData1[this.name]) {
						if ($.isArray(jsonData1[this.name])) {
							jsonData1[this.name].push(this.value);
						} else {
							jsonData1[this.name] = [ jsonData1[this.name],
									this.value ];
						}
					} else {
						jsonData1[this.name] = this.value;
					}
				});
		// 再转成[{"id": "12", "name": "aaa", "pwd":"pwd1"},{"id": "14", "name":
		// "bb", "pwd":"pwd2"}]的形式
		var vCount = 0;
		// 计算json内部的数组最大长度
		for ( var item in jsonData1) {
			var tmp = $.isArray(jsonData1[item]) ? jsonData1[item].length : 1;
			vCount = (tmp > vCount) ? tmp : vCount;
		}

		if (vCount > 1) {
			var jsonData2 = new Array();
			for (var i = 0; i < vCount; i++) {
				var jsonObj = {};
				for ( var item in jsonData1) {
					jsonObj[item] = jsonData1[item][i];
				}
				jsonData2.push(jsonObj);
			}
			return JSON.stringify(jsonData2);
		} else {
			return "[" + JSON.stringify(jsonData1) + "]";
		}
	};
})(jQuery);

$(function() {
	// 页面加载时清空input
	$("input[type='text']").val("");
	// 平均值默认填0
	$(".avg").each(function() {
		var avg = (0 * 1).toFixed(2);
		$(this).text(avg);
	});
	// 查询各种数据进行页面展示
	// 查询信审统计信息
	selectAuditCashStatistics();
	// 查询信审现金统计计算关联表信息
	selectAuditCashCount();
	// 获取资产信息 查询申请件个人资产表
	getPersonassets();
	// 征信获取 房产、汽车抵押按揭类月均还款额 && 贷款负债月均还款 预留
	creditInfoGet();

	// 总负债月供涉及贷款负债月均还款，因此需要初始化
	// 总负债月供自动计算 总负债月供=贷款负债月均还款+信用卡已用额度+其他负债+其他负债（终审判定）
	grossLiability();
	// 月均净可支配收入自动计算 月均净可支配收入=月均总收入-月均总支出-总负债月供
	disposableIncome();
	// 文本框变化时，重新计算平均值/自动计算某些信息
	$(".input-tex").change(
			function() {
				var one = $(this).parent().parent().find(".one").val();
				if (one == "") {
					one = 0;
				} else {
					one = (one * 1).toFixed(2);
					$(this).parent().parent().find(".one").val(one);
				}

				var two = $(this).parent().parent().find(".two").val();
				if (two == "") {
					two = 0;
				} else {
					two = (two * 1).toFixed(2);
					$(this).parent().parent().find(".two").val(two);
				}

				var three = $(this).parent().parent().find(".three").val();
				if (three == "") {
					three = 0;
				} else {
					three = (three * 1).toFixed(2);
					$(this).parent().parent().find(".three").val(three);
				}

				var four = $(this).parent().parent().find(".four").val();
				if (four == "") {
					four = 0;
				} else {
					four = (four * 1).toFixed(2);
					$(this).parent().parent().find(".four").val(four);
				}

				var five = $(this).parent().parent().find(".five").val();
				if (five == "") {
					five = 0;
				} else {
					five = (five * 1).toFixed(2);
					$(this).parent().parent().find(".five").val(five);
				}

				var six = $(this).parent().parent().find(".six").val();
				if (six == "") {
					six = 0;
				} else {
					six = (six * 1).toFixed(2);
					$(this).parent().parent().find(".six").val(six);
				}
				var sum = one * 1 + two * 1 + three * 1 + four * 1 + five * 1
						+ six * 1;
				var avg = (sum * 1 / 6).toFixed(2);
				$(this).parent().siblings(".avg").text(avg);

				// 月总收入自动计算 月总收入=1*利率*占股率+每月薪资收入+其他收入+其他收入（终审考量）
				generalIncome();
				// 月均总支出自动计算 月均总支出=正常家庭开支+非债务造成的月支出
				totalExpenditure();
				// 总负债月供自动计算 总负债月供=贷款负债月均还款+信用卡已用额度+其他负债+其他负债（终审判定）
				grossLiability();
				// 月均净可支配收入自动计算 月均净可支配收入=月均总收入-月均总支出-总负债月供
				disposableIncome();
				// 计算收入负债比率
				countDebtRratio();
				// 将收入负债比率写入input
				$('#debtRratio').val(
						($('#debtRratioRel').val() / 100).toFixed(2));
			});

});

function checkInput(patrn, obj) {
	obj.value = obj.value.replace(/(^\s*)|(\s*$)/g, "");// 删除二边空格
	var objExp = new RegExp(patrn);
	if (!objExp.test(obj.value)) {
		BootstrapDialog.alert("输入格式错误,请重新输入", function() {
			setTimeout(function() {
				obj.focus();
			}, 0);
		});
	}
	return true;
}

function save() {
	// 保存表单信息
	// 保存信审现金统计表信息
	var options = {
		url : serverPath + '/audit/saveAuditCashStatistics.htm',
		type : 'post',
		dataType : 'json',
		data : $("#businessTypeInput_form").serialize(),
	};
	$.ajax(options);

	// 保存信审现金统计计算信息
	var jsonStr = $("#auditCashCountInput_form").serializeJson();
	$.ajax({
		url : serverPath + '/audit/saveAuditCashCount.htm',
		type : 'post',
		data : jsonStr,
		dataType : 'json',
		contentType : "application/json",
		success : function(res) {
			BootstrapDialog.alert("保存信息成功");
		}
	});

}

// 月总收入自动计算 月总收入=1*利率*占股率+每月薪资收入+其他收入+其他收入（终审考量）
function generalIncome() {
	var profits = $("#profits").val();
	var shareRatio = $("#shareRatio").val();
	var earningsType4one = profits
			* (shareRatio / 100)
			* 1
			+ ($(Class.getConstant('earningsType1')).children()
					.children(".one").val())
			* 1
			+ ($(Class.getConstant('earningsType2')).children()
					.children(".one").val())
			* 1
			+ ($(Class.getConstant('earningsType3')).children()
					.children(".one").val()) * 1;
	$(Class.getConstant('earningsType4')).children().children(".one").val(
			earningsType4one.toFixed(2));
	var earningsType4two = profits
			* (shareRatio / 100)
			* 1
			+ ($(Class.getConstant('earningsType1')).children()
					.children(".two").val())
			* 1
			+ ($(Class.getConstant('earningsType2')).children()
					.children(".two").val())
			* 1
			+ ($(Class.getConstant('earningsType3')).children()
					.children(".two").val()) * 1;
	$(Class.getConstant('earningsType4')).children().children(".two").val(
			earningsType4two.toFixed(2));
	var earningsType4three = profits
			* (shareRatio / 100)
			* 1
			+ ($(Class.getConstant('earningsType1')).children().children(
					".three").val())
			* 1
			+ ($(Class.getConstant('earningsType2')).children().children(
					".three").val())
			* 1
			+ ($(Class.getConstant('earningsType3')).children().children(
					".three").val()) * 1;
	$(Class.getConstant('earningsType4')).children().children(".three").val(
			earningsType4three.toFixed(2));
	var earningsType4four = profits
			* (shareRatio / 100)
			* 1
			+ ($(Class.getConstant('earningsType1')).children().children(
					".four").val())
			* 1
			+ ($(Class.getConstant('earningsType2')).children().children(
					".four").val())
			* 1
			+ ($(Class.getConstant('earningsType3')).children().children(
					".four").val()) * 1;
	$(Class.getConstant('earningsType4')).children().children(".four").val(
			earningsType4four.toFixed(2));
	var earningsType4five = profits
			* (shareRatio / 100)
			* 1
			+ ($(Class.getConstant('earningsType1')).children().children(
					".five").val())
			* 1
			+ ($(Class.getConstant('earningsType2')).children().children(
					".five").val())
			* 1
			+ ($(Class.getConstant('earningsType3')).children().children(
					".five").val()) * 1;
	$(Class.getConstant('earningsType4')).children().children(".five").val(
			earningsType4five.toFixed(2));
	var earningsType4six = profits
			* (shareRatio / 100)
			* 1
			+ ($(Class.getConstant('earningsType1')).children()
					.children(".six").val())
			* 1
			+ ($(Class.getConstant('earningsType2')).children()
					.children(".six").val())
			* 1
			+ ($(Class.getConstant('earningsType3')).children()
					.children(".six").val()) * 1;
	$(Class.getConstant('earningsType4')).children().children(".six").val(
			earningsType4six.toFixed(2));
	var sum = earningsType4one * 1 + earningsType4two * 1 + earningsType4three
			* 1 + earningsType4four * 1 + earningsType4five * 1
			+ earningsType4six * 1;
	var avg = (sum * 1 / 6).toFixed(2);
	$(Class.getConstant('earningsType4')).children(".avg").text(avg);
}

// 月均总支出自动计算 月均总支出=正常家庭开支+非债务造成的月支出
function totalExpenditure() {
	var earningsType7one = ($(Class.getConstant('earningsType5')).children()
			.children(".one").val())
			* 1
			+ ($(Class.getConstant('earningsType6')).children()
					.children(".one").val()) * 1;
	$(Class.getConstant('earningsType7')).children().children(".one").val(
			earningsType7one.toFixed(2));
	var earningsType7two = ($(Class.getConstant('earningsType5')).children()
			.children(".two").val())
			* 1
			+ ($(Class.getConstant('earningsType6')).children()
					.children(".two").val()) * 1;
	$(Class.getConstant('earningsType7')).children().children(".two").val(
			earningsType7two.toFixed(2));
	var earningsType7three = ($(Class.getConstant('earningsType5')).children()
			.children(".three").val())
			* 1
			+ ($(Class.getConstant('earningsType6')).children().children(
					".three").val()) * 1;
	$(Class.getConstant('earningsType7')).children().children(".three").val(
			earningsType7three.toFixed(2));
	var earningsType7four = ($(Class.getConstant('earningsType5')).children()
			.children(".four").val())
			* 1
			+ ($(Class.getConstant('earningsType6')).children().children(
					".four").val()) * 1;
	$(Class.getConstant('earningsType7')).children().children(".four").val(
			earningsType7four.toFixed(2));
	var earningsType7five = ($(Class.getConstant('earningsType5')).children()
			.children(".five").val())
			* 1
			+ ($(Class.getConstant('earningsType6')).children().children(
					".five").val()) * 1;
	$(Class.getConstant('earningsType7')).children().children(".five").val(
			earningsType7five.toFixed(2));
	var earningsType7six = ($(Class.getConstant('earningsType5')).children()
			.children(".six").val())
			* 1
			+ ($(Class.getConstant('earningsType6')).children()
					.children(".six").val()) * 1;
	$(Class.getConstant('earningsType7')).children().children(".six").val(
			earningsType7six.toFixed(2));
	var sum = earningsType7one * 1 + earningsType7two * 1 + earningsType7three
			* 1 + earningsType7four * 1 + earningsType7five * 1
			+ earningsType7six * 1;
	var avg = (sum * 1 / 6).toFixed(2);
	$(Class.getConstant('earningsType7')).children(".avg").text(avg);
}

// 总负债月供自动计算 总负债月供=贷款负债月均还款+信用卡已用额度+其他负债+其他负债（终审判定）
function grossLiability() {
	var earningsType13one = ($(Class.getConstant('earningsType9')).children()
			.children(".one").val())
			* 1
			+ ($(Class.getConstant('earningsType10')).children().children(
					".one").val())
			* 1
			+ ($(Class.getConstant('earningsType11')).children().children(
					".one").val())
			* 1
			+ ($(Class.getConstant('earningsType12')).children().children(
					".one").val()) * 1;
	$(Class.getConstant('earningsType13')).children().children(".one").val(
			earningsType13one.toFixed(2));
	var earningsType13two = ($(Class.getConstant('earningsType9')).children()
			.children(".two").val())
			* 1
			+ ($(Class.getConstant('earningsType10')).children().children(
					".two").val())
			* 1
			+ ($(Class.getConstant('earningsType11')).children().children(
					".two").val())
			* 1
			+ ($(Class.getConstant('earningsType12')).children().children(
					".two").val()) * 1;
	$(Class.getConstant('earningsType13')).children().children(".two").val(
			earningsType13two.toFixed(2));
	var earningsType13three = ($(Class.getConstant('earningsType9')).children()
			.children(".three").val())
			* 1
			+ ($(Class.getConstant('earningsType10')).children().children(
					".three").val())
			* 1
			+ ($(Class.getConstant('earningsType11')).children().children(
					".three").val())
			* 1
			+ ($(Class.getConstant('earningsType12')).children().children(
					".three").val()) * 1;
	$(Class.getConstant('earningsType13')).children().children(".three").val(
			earningsType13three.toFixed(2));
	var earningsType13four = ($(Class.getConstant('earningsType9')).children()
			.children(".four").val())
			* 1
			+ ($(Class.getConstant('earningsType10')).children().children(
					".four").val())
			* 1
			+ ($(Class.getConstant('earningsType11')).children().children(
					".four").val())
			* 1
			+ ($(Class.getConstant('earningsType12')).children().children(
					".four").val()) * 1;
	$(Class.getConstant('earningsType13')).children().children(".four").val(
			earningsType13four.toFixed(2));
	var earningsType13five = ($(Class.getConstant('earningsType9')).children()
			.children(".five").val())
			* 1
			+ ($(Class.getConstant('earningsType10')).children().children(
					".five").val())
			* 1
			+ ($(Class.getConstant('earningsType11')).children().children(
					".five").val())
			* 1
			+ ($(Class.getConstant('earningsType12')).children().children(
					".five").val()) * 1;
	$(Class.getConstant('earningsType13')).children().children(".five").val(
			earningsType13five.toFixed(2));
	var earningsType13six = ($(Class.getConstant('earningsType9')).children()
			.children(".six").val())
			* 1
			+ ($(Class.getConstant('earningsType10')).children().children(
					".six").val())
			* 1
			+ ($(Class.getConstant('earningsType11')).children().children(
					".six").val())
			* 1
			+ ($(Class.getConstant('earningsType12')).children().children(
					".six").val()) * 1;
	$(Class.getConstant('earningsType13')).children().children(".six").val(
			earningsType13six.toFixed(2));
	var sum = earningsType13one * 1 + earningsType13two * 1
			+ earningsType13three * 1 + earningsType13four * 1
			+ earningsType13five * 1 + earningsType13six * 1;
	var avg = (sum * 1 / 6).toFixed(2);
	$(Class.getConstant('earningsType13')).children(".avg").text(avg);
}

// 月均净可支配收入自动计算 月均净可支配收入=月均总收入-月均总支出-总负债月供
function disposableIncome() {
	var earningsType14one = ($(Class.getConstant('earningsType4')).children()
			.children(".one").val())
			* 1
			- ($(Class.getConstant('earningsType7')).children()
					.children(".one").val())
			* 1
			- ($(Class.getConstant('earningsType13')).children().children(
					".one").val()) * 1;
	$(Class.getConstant('earningsType14')).children().children(".one").val(
			earningsType14one.toFixed(2));
	var earningsType14two = ($(Class.getConstant('earningsType4')).children()
			.children(".two").val())
			* 1
			- ($(Class.getConstant('earningsType7')).children()
					.children(".two").val())
			* 1
			- ($(Class.getConstant('earningsType13')).children().children(
					".two").val()) * 1;
	$(Class.getConstant('earningsType14')).children().children(".two").val(
			earningsType14two.toFixed(2));
	var earningsType14three = ($(Class.getConstant('earningsType4')).children()
			.children(".three").val())
			* 1
			- ($(Class.getConstant('earningsType7')).children().children(
					".three").val())
			* 1
			- ($(Class.getConstant('earningsType13')).children().children(
					".three").val()) * 1;
	$(Class.getConstant('earningsType14')).children().children(".three").val(
			earningsType14three.toFixed(2));
	var earningsType14four = ($(Class.getConstant('earningsType4')).children()
			.children(".four").val())
			* 1
			- ($(Class.getConstant('earningsType7')).children().children(
					".four").val())
			* 1
			- ($(Class.getConstant('earningsType13')).children().children(
					".four").val()) * 1;
	$(Class.getConstant('earningsType14')).children().children(".four").val(
			earningsType14four.toFixed(2));
	var earningsType14five = ($(Class.getConstant('earningsType4')).children()
			.children(".five").val())
			* 1
			- ($(Class.getConstant('earningsType7')).children().children(
					".five").val())
			* 1
			- ($(Class.getConstant('earningsType13')).children().children(
					".five").val()) * 1;
	$(Class.getConstant('earningsType14')).children().children(".five").val(
			earningsType14five.toFixed(2));
	var earningsType14six = ($(Class.getConstant('earningsType4')).children()
			.children(".six").val())
			* 1
			- ($(Class.getConstant('earningsType7')).children()
					.children(".six").val())
			* 1
			- ($(Class.getConstant('earningsType13')).children().children(
					".six").val()) * 1;
	$(Class.getConstant('earningsType14')).children().children(".six").val(
			earningsType14six.toFixed(2));
	var sum = earningsType14one * 1 + earningsType14two * 1
			+ earningsType14three * 1 + earningsType14four * 1
			+ earningsType14five * 1 + earningsType14six * 1;
	var avg = (sum * 1 / 6).toFixed(2);
	$(Class.getConstant('earningsType14')).children(".avg").text(avg);
}

function creditInfoGet() {
	var options = {
		url : serverPath + '/audit/selectAuditCheck.htm',
		type : 'post',
		dataType : 'json',
		data : {
			'applyLoanNo' : $("#applyLoanNo").val()
		},
		success : function(result) {
			var data = result.data;
			if (data != null) {
				var houseMPay = data.houseMPay!=null?data.houseMPay:0;
				var carMPay = data.carMPay!=null?data.carMPay:0;
				var vsixAvgRepay = data.vsixAvgRepay!=null?data.vsixAvgRepay:0;
				var houseCarPay = houseMPay * 1 + carMPay * 1;

				$(Class.getConstant('earningsType8')).children().children(
						".one").val(houseCarPay.toFixed(2));
				$(Class.getConstant('earningsType8')).children().children(
						".two").val(houseCarPay.toFixed(2));
				$(Class.getConstant('earningsType8')).children().children(
						".three").val(houseCarPay.toFixed(2));
				$(Class.getConstant('earningsType8')).children().children(
						".four").val(houseCarPay.toFixed(2));
				$(Class.getConstant('earningsType8')).children().children(
						".five").val(houseCarPay.toFixed(2));
				$(Class.getConstant('earningsType8')).children().children(
						".six").val(houseCarPay.toFixed(2));
				$(Class.getConstant('earningsType8')).children(".avg").text(
						houseCarPay.toFixed(2));

				$(Class.getConstant('earningsType9')).children().children(
						".one").val(vsixAvgRepay.toFixed(2));
				$(Class.getConstant('earningsType9')).children().children(
						".two").val(vsixAvgRepay.toFixed(2));
				$(Class.getConstant('earningsType9')).children().children(
						".three").val(vsixAvgRepay.toFixed(2));
				$(Class.getConstant('earningsType9')).children().children(
						".four").val(vsixAvgRepay.toFixed(2));
				$(Class.getConstant('earningsType9')).children().children(
						".five").val(vsixAvgRepay.toFixed(2));
				$(Class.getConstant('earningsType9')).children().children(
						".six").val(vsixAvgRepay.toFixed(2));
				$(Class.getConstant('earningsType9')).children(".avg").text(
						vsixAvgRepay.toFixed(2));
			}

		}
	};
	$.ajax(options);
}

function countDebtRratio() {
	// 收入负债比率=（月均净可支配收入/总负债月供）*100%
	var earningsType14 = $(Class.getConstant('earningsType14'))
			.children(".avg").text();
	var earningsType13 = $(Class.getConstant('earningsType13'))
			.children(".avg").text();
	if (earningsType13 == '' || earningsType13 == '0.00') {
		earningsType13 = 1;
	}
	var debtRratio = ((earningsType14 / earningsType13).toFixed(2)) * 100;
	$('#debtRratioRel').val(debtRratio);
}

function getPersonassets() {
	var options = {
		url : serverPath + '/audit/getPersonassets.htm',
		type : 'post',
		dataType : 'json',
		data : {
			'applyLoanNo' : $("#applyLoanNo").val()
		},
		success : function(result) {
			var data = result.data;
			$('#houseNum').val(data.houseNum);
			$('#loanHouseNum').val(data.loanHouseNum);
			$('#loanHouseAmount').val(data.loanHouseAmount);

			$('#carNum').val(data.carNum);
			$('#loanCarNum').val(data.loanCarNum);
			$('#loanCarAmount').val(data.loanCarAmount);

		}
	};
	$.ajax(options);
}

// 查询信审统计信息
function selectAuditCashStatistics() {
	var options = {
		url : serverPath + '/audit/selectAuditCashStatistics.htm',
		type : 'post',
		data : {
			'applyLoanNo' : $("#applyLoanNo").val()
		},
		dataType : 'json',
		success : function(res) {
			var data = res.data;
			if(data != null){
				$('#businessType').val(data.businessType!=null?data.businessType:0);
				$('#profits').val(data.profits!=null?data.profits:"");
				var shareRatio = data.shareRatio!=null?data.shareRatio:"";
				if(shareRatio != ""){
					$('#shareRatio').val(data.shareRatio * 100);
				}
				var debtRratio = data.debtRratio!=null?data.debtRratio:"";
				if(debtRratio != ""){
					$('#debtRratioRel').val(data.debtRratio * 100);
				}
			}
		}
	};
	$.ajax(options);
}

// 查询信审现金统计计算关联表信息
function selectAuditCashCount() {
	var options = {
		url : serverPath + '/audit/selectAuditCashCount.htm',
		type : 'post',
		data : {
			'applyLoanNo' : $("#applyLoanNo").val()
		},
		dataType : 'json',
		success : function(res) {
			var data = res.data;
			$.each(data, function(n, value) {
				var id = "#" + value.liabilitiesTypeNo;
				$(id).children().children(".one").val(value.oneMonth);
				$(id).children().children(".two").val(value.twoMonth);
				$(id).children().children(".three").val(value.threeMonth);
				$(id).children().children(".four").val(value.fourMonth);
				$(id).children().children(".five").val(value.fiveMonth);
				$(id).children().children(".six").val(value.sixMonth);
				var sum = value.oneMonth * 1 + value.twoMonth * 1
						+ value.threeMonth * 1 + value.fourMonth * 1
						+ value.fiveMonth * 1 + value.sixMonth * 1;
				var avg = (sum * 1 / 6).toFixed(2);
				$(id).children(".avg").text(avg);
			});
		}
	};
	$.ajax(options);

}
