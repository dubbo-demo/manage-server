
$(function() {
	$(".close").click(function() {
		$(this).parent().hide();
		return false;
	});
	if($.cookie("phone")){  
	     $('#phone').val($.cookie("phone"));  
	}  
});
function submitLogin(event) {
	//阻止冒泡
	ChkUtil.stopBubbleEvent(event);
	document.forms[0].submit();
}

// 倒计时
function countdown(time) {
	$("#send_sms").html(
			'重新发送(<em id="regSeconds">' + time
					+ '</em>)');
	timer = setInterval('changeSecA()', 1000);
}
function changeSecA() {
	var sec = $('#regSeconds').html();
	var secNum = parseInt(sec);
	if (secNum <= 0) {
		clearInterval(timer);
		$("#send_sms").html('重新发送');
	} else {
		secNum--;
		$('#regSeconds').html(secNum);
	}
}
// 发送短信验证码
function sendSmsCode(evt) {
	//阻止冒泡
	ChkUtil.stopBubbleEvent(evt);
	$('.alert-success-error').hide();
	var phone = $('#phone').val();
	if (ChkUtil.isNull(phone)) {
		$('.alert-phone-error').show();
		return;
	} else {
		$('.alert-phone-error').hide();
		$('.alert-error').hide();
		$('.alert-success-error').hide();
		$('.alert-smsCode-error').hide();
	}
	if (true) {
		var url = serverPath + "/sendLoginSmsCode.htm";
		$.ajax({
					url : url,
					type : "post",
					data : {
						phone : phone
					},
					dataType : "json",
					cache : false,
					async: false,
					success : function(res) {
						if(res.code == 0) {
							countdown(60);
							$.cookie("phone",phone,{expires:30})
						}
						$('.alert-success-error', $('.login-form')).show(0,function(){
							$('#login_sms_msg').html(res.message);
						});
					},
					error : function(res) {
						$('.alert-success-error', $('.login-form')).show(0,function(){
							$('#login_sms_msg').html("后台服务异常,请稍后重试");
						});
					}
				});
	}
}

$('#smsCode').keydown(function (e) {
	var code;    
	if  (!e)    
	{    
	    var e=window.event;    
	}    
	if(e.keyCode)    
	{      
	    code=e.keyCode;    
	}    
	else if(e.which)    
	{    
	    code   =   e.which;    
	}  
	if(code==13)  
	{  
	  ///这里是调用执行的方法  
	   login();
	   return false;  
	   //阻止了回车键事件冒泡  
	}
//    if (e.which == 13) {
//        login();
//        return false;
//    }
});
function login(evt) {
	//阻止冒泡
	var phone = $('#phone').val();
	var smsCode = $('#smsCode').val();
	$('.alert-success-error').hide();
	if (ChkUtil.isNull(phone)) {
		$('.alert-phone-error').show();
		return;
	} else if (ChkUtil.isNull(smsCode)) {
		$('.alert-phone-error').hide();
		$('.alert-smsCode-error').show();
		$('.alert-error').hide();
		return;
	} else {
		$('.alert-smsCode-error').hide();
		$('.alert-phone-error').hide();
		$('.alert-error').hide();
		$('.alert-success-error').hide();
	}
	if (true) {
		var url = serverPath+"/dologin.htm";
		$.ajax({
					url : url,
					type : "post",
					data : {
						phone : phone,
						smsCode :smsCode
					},
					dataType : "json",
					cache : false,
					success : function(res) {
						if(res.success) {
							window.location.href = serverPath + "/index.htm";
						} else {
							$('.alert-success-error').show(0,function(){
								$('#login_sms_msg').html(res.message);
							});
						}
					},
					error : function(res) {
						$('.alert-success-error').show(0,function(){
							$('#login_sms_msg').html(res.message);
						});
					}
				});
	}
}

