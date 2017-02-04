/**
 * 保存
 */
function saveUserInfo() {
	var userinfo_ftl = $("#userinfo_ftl").val();
	if(userinfo_ftl == 0) {
		userinfo_addInfo();
	} else {
		userinfo_updateInfo();
	}
}

function initBaseSelect(e) {
	$("#tab_userinfo select[name='"+e+"']").val($("#tab_userinfo select[name='"+e+"']").data("value"));
}

function addErrorCss(e) {
	if(e.val() == "" || e.val() == "请选择"){
		e.addClass("error");
		return true;
	}else{
		e.removeClass("error");
		return false;
	}
}

/**
 * 提交
 */
function submitUserInfo() {
	if(addErrorCss($("#liveAddr"))) {
		return false;
	}
	if(addErrorCss($("#censusAddr"))) {
		return false;
	}
	var form = $("#tab_userinfo");
	if (!form.valid()) {
		$('#form_wizard_1').find(
		'.button-previous').hide();
		return false;
	}
//	if($("#s2id_customerServiceId .select2-choice span").html() == "请选择") {
	if($("#customerSName").val() == "请选择" || $("#customerSName").val() == "") {
//		BootstrapDialog.alert("请根据员工姓名首字母选择客服!", function() {
		BootstrapDialog.alert("2016-10-18需求调整，客服在接待表阶段插入录入者!", function() {
			setTimeout(function() {
				$('#form_wizard_1').find(
				'.button-previous').hide();
			}, 0);
		});
		return false;
	}
	var userinfo_ftl = $("#userinfo_ftl").val();
	if(userinfo_ftl == 0) {
		userinfo_save();
	} else {
		userinfo_update();
	}
}

function userinfo_addInfo() {
	if(ChkUtil.isNull($("#idCard").val())) {
		alert("请输入身份证!");
		return false;
	}
	if($("#s2id_bmId .select2-choice span").html() != ChkUtil.select2Name) {
		$("#bmName").val($("#s2id_bmId .select2-choice span").html());
	}
	$.post(serverPath+'/apply/member/addInfo.htm', $('#tab_userinfo').serialize(), function(res) {
		if (res.code == '0') {
			// 转换数据操作为update
			$("#userinfo_ftl").val(1);
			if($("#submitType").val() == 1 ) {
				BootstrapDialog.alert("操作成功");
			}
		} else {
			BootstrapDialog.alert(res.message);
			return false;
		}
	}, "json");
}

function userinfo_save() {
	// 阻止冒泡
	$("#submitType").val(2);
	$("#tab_userinfo").submit();
}

function userinfo_updateInfo() {
	if(ChkUtil.isNull($("#idCard").val())) {
		alert("请输入身份证!");
		return false;
	}
	if($("#s2id_bmId .select2-choice span").html() != ChkUtil.select2Name) {
		$("#bmName").val($("#s2id_bmId .select2-choice span").html());
	}
	$.post(serverPath+'/apply/member/updateInfo.htm', $('#tab_userinfo').serialize(), function(res) {
		if (res.code == '0') {
			if($("#submitType").val() == 1 ) {
				BootstrapDialog.alert("操作成功");
			}
		} else {
			BootstrapDialog.alert(res.message);
			return false;
		}
	}, "json");
}

$(function () {
	showCitys2("#census",serverPath);
	showCitys2("#live",serverPath);
});
function showCitys2(inputId,contextPath){
	 $(inputId+"Addr").jcity({
       urlOrData: contextPath + "/js/citydata.json",
       animate: { showClass: 'animated flipInX', hideClass: 'animated flipOutX' },  // 需要第一步引用的animate.min.css文件，也可以自己定义动画 
       onChoice: function (data) {
    	   $(inputId+"Provcode").val(data.provinceCode);
    	   $(inputId+"Citycode").val(data.cityCode);
    	   $(inputId+"Towncode").val(data.areaCode);
       }
   });
}

function userinfo_update(e) {
	// 阻止冒泡
//	ChkUtil.stopBubbleEvent(e);
	$("#submitType").val(2);
	$("#tab_userinfo").submit();
}