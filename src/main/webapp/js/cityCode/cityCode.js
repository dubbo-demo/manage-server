$(function() {

});

function getCity() {
	var options = {
		url : serverPath + '/cityCode/getCity.htm',
		type : 'post',
		dataType : 'json',
		data : {
			"id" : $('#province').val()
		},
		success : function(data) {
			$("#city").empty();
			$("#city").append("<option value=0>请选择市</option>");
			var cityData = data.data;
			for (var i = 0; i < cityData.length; i++) {
				var op = "<option value=" + cityData[i].id + " data-code=" + cityData[i].code + ">"
						+ cityData[i].name + "</option>";
				$("#city").append(op);
			}
		}
	};
	$.ajax(options);
}

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

function checkAndChange() {
	var insertOrUpdate = $('#insertOrUpdate').val();
	if (insertOrUpdate == 0) {
		var cityId = $("#city").val();
		if (cityId == 0) {
			BootstrapDialog.alert("请选择城市");
			return false;
		}
	}

	// 编码统一小写改大写
	$("#cityCode").val($("#cityCode").val().toUpperCase());
	return true;
}

function add() {
	$('#main').show();
	$('#description').show();
	$('#buttonInput').hide();
	$('#cityNameDiv').hide();
	$('#hName').html('新增城市');
	$('#insertOrUpdate').val(0);
}

function change() {
	var changeCityId = $("#citySelect").val();
	if (!changeCityId || changeCityId == 0) {
		BootstrapDialog.alert("请选择城市");
		return false;
	}
	$('#id').val($("#citySelect").find("option:selected").val());
	$('#cityName').val($("#citySelect").find("option:selected").text());
	$('#cityCode').val($("#citySelect").find("option:selected").attr('data-code'));

	$('#main').show();
	$('#description').hide();
	$('#buttonInput').hide();
	$('#cityNameDiv').show();
	$('#hName').html('修改城市');
	$('#insertOrUpdate').val(1);
}

function save() {
	var checkResult = checkAndChange();
	if(!checkResult){
		return false;
	}
	var insertOrUpdate = $('#insertOrUpdate').val();
	var cityName = '';
	var id = "";
	var provinceCode = '';
	if (insertOrUpdate == 0) {
		cityName = $("#city").find("option:selected").text();
		provinceCode = $("#city").find("option:selected").attr("data-code");
	} else {
		cityName = $("#cityName").val();
		id = $("#id").val();
	}
	var cityCode = $("#cityCode").val();
	if (cityCode == "") {
		BootstrapDialog.alert("请填写城市编码");
		return false;
	}
	var options = {
		url : serverPath + '/cityCode/insertOrUpdate.htm',
		type : 'post',
		dataType : 'json',
		data : {
			"cityName" : cityName,
			"provinceCode" : provinceCode,
			"cityCode" : cityCode,
			"insertOrUpdate" : insertOrUpdate,
			"id":id
		},
		success : function(data) {
			var result = data.data;
			if (result == 1) {
				BootstrapDialog.alert("已有此城市，不可重复添加");
				return false;
			}
			if (result == 2) {
				BootstrapDialog.alert("城市编码已存在，不可重复");
				return false;
			}
			if (result == 0) {
				BootstrapDialog.alert('更新成功', function(result) {
					window.location.href = window.location;
				});
			}
		}
	};
	$.ajax(options);
}

function del() {
	var changeCityId = $("#citySelect").val();
	if (!changeCityId || changeCityId == 0) {
		BootstrapDialog.alert("请选择城市");
		return false;
	}
	var changeCityName = $("#citySelect").find("option:selected").text();
	BootstrapDialog.confirm("确定要删除" + changeCityName, function(result) {
		if (result) {
			var options = {
				url : serverPath + '/cityCode/delCityCode.htm',
				type : 'post',
				dataType : 'json',
				data : {
					"changeCityId" : changeCityId
				},
				success : function(data) {
					BootstrapDialog.alert('删除成功', function(result) {
						window.location.href = window.location;
					});
				}
			};
			$.ajax(options);
		}
	});

}

function returnCity() {
	$('#main').hide();
	$('#buttonInput').show();
}
