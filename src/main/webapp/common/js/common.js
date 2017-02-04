function ChkUtil() {
	
} 
ChkUtil.select2Name = '请选择';
//分页的条数
ChkUtil.pageSize=20;
// 校验是否为空(先删除二边空格再验证)
ChkUtil.isNull = function (str) {
    str = str.replace(/(^\s*)|(\s*$)/g, "");// 删除二边空格
    if (null == str || "" == str) {
        return true;
    } else {
        return false;
    }
};
//判断是否只有英文
ChkUtil.isEnglish = function (str){
	var patrn = /^[a-z]+$/i;
	return patrn.test(str);
};
/*获取字符串长度，中文算两个字符，英文算一个字符*/
ChkUtil.getLength=function(str) {
   str = str.replace(/(^\s*)|(\s*$)/g, "");//删除二边空格
   var totallength=0;
   for(var i=0;i<str.length;i++){
	  var intCode=str.charCodeAt(i); 
      if(intCode>=0&&intCode<=128) {
    	  totallength=totallength+1; //非中文单个字符长度加 1
      }else{
    	  totallength=totallength+2; //中文字符长度则加 2
      }
   }
   return totallength;
};
//校验是否仅中文
ChkUtil.isChinese=function(str){
//	var patrn=/[\u4E00-\u9FA5\uF900-\uFA2D]/;
	var patrn=/[\u4E00-\u9FA5\uF900-\uFA2D]+$/;
//	var patrn=/^[\u0391-\uFFE5]+$/;   
//	var patrn = /^[u4E00-u9FA5]+$/;
//	var patrn = /[^\u4e00-\u9fa5]/g;
	return patrn.test(str);
};

//密码必须是字母数字下划线组合
ChkUtil.isPassword=function(str){
	var patrn=/^((?=.*[0-9].*)(?=.*[A-Za-z].*)(?=.*_.*))[_0-9A-Za-z]{6,20}$/;
	return patrn.test(str);
};

//校验是否有全角标点
ChkUtil.isFullwidthChar = function(str){
	var patrn = /[\uFF00-\uFFEF]/;
	return patrn.test(str);
}; 

//判断是否只含有中文、字母最多26个
ChkUtil.isChanEng1_26 = function (str) {
	str = str.replace(/(^\s*)|(\s*$)/g, "");// 删除二边空格
	var patrn = /^([\u4e00-\u9fa5]{1,13}|[a-zA-Z]{2,26})$/;
	return patrn.test(str);
};

//判断是否只含有中文、字母、数字
ChkUtil.isChanEng = function (str) {
	str = str.replace(/(^\s*)|(\s*$)/g, "");// 删除二边空格
	var patrn = /^[a-z\u4E00-\u9FA5]+$/i;
	return patrn.test(str);
};
//判断是否只含有中文、字母、数字、下划线、(增加)中划线（判断用户名）
ChkUtil.isNickName = function (str) {
    //str = str.replace(/(^\s*)|(\s*$)/g, "");// 删除二边空格
    var patrn = /^[(\w)\d\u4E00-\u9FA5-]+$/i;
    return patrn.test(str);
};
//校验手机号码
ChkUtil.isMobile = function (str) {
    var patrn = /^0?1[3,4,5,7,8][0-9]{9}$/;
    return patrn.test(str);
};
// 校验电邮地址
ChkUtil.isEmail = function (str) {
    var patrn = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
    return patrn.test(str);
};

//是否是身份证(不考虑15位)
ChkUtil.isIdCardNew = function (obj)
{
	 var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};
	 var iSum = 0;
	 var strIDno = obj;
	 var idCardLength = strIDno.length;
	 var GetVerifyBit = function (id){ 
	    var result = ''; 
	    var nNum=eval(id.charAt(0)*7+id.charAt(1)*9+id.charAt(2)*10+id.charAt(3)*5+id.charAt(4)*8+id.charAt(5)*4+id.charAt(6)*2+id.charAt(7)*1+id.charAt(8)*6+id.charAt(9)*3+id.charAt(10)*7+id.charAt(11)*9+id.charAt(12)*10+id.charAt(13)*5+id.charAt(14)*8+id.charAt(15)*4+id.charAt(16)*2);
	    nNum=nNum%11; 
	    switch (nNum) { 
	       case 0 : 
	          result="1"; 
	          break; 
	       case 1 : 
	          result="0"; 
	          break; 
	       case 2 : 
	          result="X"; 
	          break; 
	       case 3 : 
	          result="9"; 
	          break; 
	       case 4 : 
	          result="8"; 
	          break; 
	       case 5 : 
	          result="7"; 
	          break; 
	       case 6 : 
	          result="6"; 
	          break; 
	       case 7 : 
	          result="5"; 
	          break; 
	       case 8 : 
	          result="4"; 
	          break; 
	       case 9 : 
	          result="3"; 
	          break; 
	       case 10 : 
	          result="2"; 
	          break; 
	    } 
	    return result; 
	};
	 if(!/^\d{17}(\d|x)$/i.test(strIDno)&&!/^\d{15}$/i.test(strIDno))
	 {
       return false; // 非法身份证号
	 }

	 if(aCity[parseInt(strIDno.substr(0,2))]==null)
	 {
		 return false;// 非法地区
	 }

	 // 15位身份证转换为18位
	 if (idCardLength==15)
	 {
//		 sBirthday = "19" + strIDno.substr(6,2) + "-" + Number(strIDno.substr(8,2)) + "-" + Number(strIDno.substr(10,2));
//		 var d = new Date(sBirthday.replace(/-/g,"/"));
//		 var dd = d.getFullYear().toString() + "-" + (d.getMonth()+1) + "-" + d.getDate();
//		 if(sBirthday != dd)
//		 {
//           return false; // 非法生日
//		 }
//        strIDno=strIDno.substring(0,6)+"19"+strIDno.substring(6,15);
//        strIDno=strIDno+GetVerifyBit(strIDno);
		 return false; 
	  }

      // 判断是否大于2078年，小于1900年
      var year =strIDno.substring(6,10);
      if (year<1900 || year>2078 )
      {
          return false;// 非法生日
      }

      // 18位身份证处理

      // 在后面的运算中x相当于数字10,所以转换成a
      strIDno = strIDno.replace(/x$/i,"a");

	   sBirthday=strIDno.substr(6,4)+"-"+Number(strIDno.substr(10,2))+"-"+Number(strIDno.substr(12,2));
	   var d = new Date(sBirthday.replace(/-/g,"/"));
	   if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate()))
	   {
           return false; // 非法生日
	   }
      // 身份证编码规范验证
	   for(var i = 17;i>=0;i --)
	   {
		    iSum += (Math.pow(2,i) % 11) * parseInt(strIDno.charAt(17 - i),11);
	   }
	   if(iSum%11!=1)
	   {
           return false;// 非法身份证号
	   }

	   // 判断是否屏蔽身份证
	   var words = new Array();
	   words = new Array("11111119111111111","12121219121212121");

	   for(var k=0;k<words.length;k++)
	   {
		   if (strIDno.indexOf(words[k])!=-1)
		   {
			   return false;
		   }
	   }

	   return true;
};

//是否是身份证
ChkUtil.isIdCard = function (obj)
{
	 var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};
	 var iSum = 0;
	 var strIDno = obj;
	 var idCardLength = strIDno.length;
	 var GetVerifyBit = function (id){ 
	    var result = ''; 
	    var nNum=eval(id.charAt(0)*7+id.charAt(1)*9+id.charAt(2)*10+id.charAt(3)*5+id.charAt(4)*8+id.charAt(5)*4+id.charAt(6)*2+id.charAt(7)*1+id.charAt(8)*6+id.charAt(9)*3+id.charAt(10)*7+id.charAt(11)*9+id.charAt(12)*10+id.charAt(13)*5+id.charAt(14)*8+id.charAt(15)*4+id.charAt(16)*2);
	    nNum=nNum%11; 
	    switch (nNum) { 
	       case 0 : 
	          result="1"; 
	          break; 
	       case 1 : 
	          result="0"; 
	          break; 
	       case 2 : 
	          result="X"; 
	          break; 
	       case 3 : 
	          result="9"; 
	          break; 
	       case 4 : 
	          result="8"; 
	          break; 
	       case 5 : 
	          result="7"; 
	          break; 
	       case 6 : 
	          result="6"; 
	          break; 
	       case 7 : 
	          result="5"; 
	          break; 
	       case 8 : 
	          result="4"; 
	          break; 
	       case 9 : 
	          result="3"; 
	          break; 
	       case 10 : 
	          result="2"; 
	          break; 
	    } 
	    return result; 
	};
	 if(!/^\d{17}(\d|x)$/i.test(strIDno)&&!/^\d{15}$/i.test(strIDno))
	 {
       return false; // 非法身份证号
	 }

	 if(aCity[parseInt(strIDno.substr(0,2))]==null)
	 {
		 return false;// 非法地区
	 }

	 // 15位身份证转换为18位
	 if (idCardLength==15)
	 {
		 sBirthday = "19" + strIDno.substr(6,2) + "-" + Number(strIDno.substr(8,2)) + "-" + Number(strIDno.substr(10,2));
		 var d = new Date(sBirthday.replace(/-/g,"/"));
		 var dd = d.getFullYear().toString() + "-" + (d.getMonth()+1) + "-" + d.getDate();
		 if(sBirthday != dd)
		 {
           return false; // 非法生日
		 }
        strIDno=strIDno.substring(0,6)+"19"+strIDno.substring(6,15);
        strIDno=strIDno+GetVerifyBit(strIDno);
	  }

      // 判断是否大于2078年，小于1900年
      var year =strIDno.substring(6,10);
      if (year<1900 || year>2078 )
      {
          return false;// 非法生日
      }

      // 18位身份证处理

      // 在后面的运算中x相当于数字10,所以转换成a
      strIDno = strIDno.replace(/x$/i,"a");

	   sBirthday=strIDno.substr(6,4)+"-"+Number(strIDno.substr(10,2))+"-"+Number(strIDno.substr(12,2));
	   var d = new Date(sBirthday.replace(/-/g,"/"));
	   if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate()))
	   {
           return false; // 非法生日
	   }
      // 身份证编码规范验证
	   for(var i = 17;i>=0;i --)
	   {
		    iSum += (Math.pow(2,i) % 11) * parseInt(strIDno.charAt(17 - i),11);
	   }
	   if(iSum%11!=1)
	   {
           return false;// 非法身份证号
	   }

	   // 判断是否屏蔽身份证
	   var words = new Array();
	   words = new Array("11111119111111111","12121219121212121");

	   for(var k=0;k<words.length;k++)
	   {
		   if (strIDno.indexOf(words[k])!=-1)
		   {
			   return false;
		   }
	   }

	   return true;
};

ChkUtil.isCaptcha = function (str) {
    var patrn = /^[\da-zA-Z]{4}$/;
    return patrn.test(str);
};

/**
 *校验码简单验证 
 * @param {Object} str
 */
ChkUtil.isSmsCaptcha = function (str) {
    var patrn = /^\d{6}$/;
    return patrn.test(str);
};

//校验是否存在任何空白字符
ChkUtil.isLowerS= function (str) {
    var patrn = /\s/;
    return patrn.test(str);
};

//3.0密码规则
ChkUtil.pwdRegx = function(pwd){
    var flag=false;
    if(pwd.length < 6 || pwd.length>20){
        return flag;
    }else{
        var singleFlag=ChkUtil.isW(pwd);
        var azFlag=ChkUtil.isa_z(pwd);
        var upperAZFlag=ChkUtil.isUpperA_Z(pwd);
        var digitFlag=ChkUtil.isDigit(pwd);
        if(!singleFlag && !azFlag && !upperAZFlag && !digitFlag){
            flag=true;
        }
        return flag;
    }   
};
//校验是否全是非字母字符
ChkUtil.isW= function (str) {
    var patrn = /^[\W_]+$/;
    return patrn.test(str);
};
//校验是否全是小写字母
ChkUtil.isa_z= function (str) {
    var patrn = /^[a-z]+$/;
    return patrn.test(str);
};
//校验是否全是大写字母
ChkUtil.isUpperA_Z= function (str) {
    var patrn = /^[A-Z]+$/;
    return patrn.test(str);
};
//检验是否全是大写字母或小写字母
ChkUtil.isAllLetters= function (str) {
    var patrn = /^[A-Za-z]+$/;
    return patrn.test(str);
};
// 校验是否是整数
ChkUtil.isInteger = function (str) {
    var patrn = /^([+-]?)(\d+)$/;
    return patrn.test(str);
};
// 校验是否为正整数
ChkUtil.isPlusInteger = function (str) {
    var patrn = /^([+]?)(\d+)$/;
    return patrn.test(str);
};
// 校验是否全是数字
ChkUtil.isDigit = function (str) {
    var patrn = /^\d+$/;
    return patrn.test(str);
};
//3.0密码强度规则
ChkUtil.pwdLevel = function(value){
    var pattern_1 =/^[\da-z]{6,7}$/;
    var pattern_2 =/^[(\W|_)A-Z]{10,20}$/;
    var pattern_3 = /\d+/;
    var pattern_4 = /[a-z]+/;
    var pattern_5 = /[A-Z]+/;
    var pattern_6 = /(\W|_)+/;
    var pattern_7 = /\S{10,20}/;
    var level = 2;
    if (pattern_1.test(value)) {
        level=1;
    }else{
        var level31=pattern_7.test(value) && ((pattern_3.test(value) && pattern_5.test(value) && pattern_6.test(value)) || (pattern_4.test(value) && pattern_5.test(value) && pattern_6.test(value)));
        var level32=pattern_2.test(value);
        var level33=pattern_3.test(value) && pattern_4.test(value) && pattern_5.test(value) && pattern_6.test(value);
        if (level31 || level32 || level33) {
            level=3;
        }
    }
    return level;
};

// 校验电邮地址
ChkUtil.isEmail = function (str) {
    var patrn = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
    return patrn.test(str);
};
//处理冒泡
ChkUtil.stopBubbleEvent = function(evt){
	var e = evt||window.event;
	if(window.event){
		e.cancelBubble = true;
	}else{
		e.stopPropagation();
	}
};

ChkUtil.mosaicZero = function (s){
	return s<10?'0'+s:s;
};

ChkUtil.getAgent = function(){
	var u = navigator.userAgent;
	return u;
};
// 校验是否全是数字
ChkUtil.isMoney = function (str) {
	var patrn = /^[0-9]*(\.[0-9]{1,2})?$/;
	return patrn.test(str);
};
/**
 * 随便写了个更加精准的倒计时方法,不会因为切到后台或前进后退而停顿
 * ChkUtil.countDown 
 * @param    (number)initialTime  计时的初始时间
 *           (boolean)recycleFlag 是否重置初始时间
 * @return      var time = {
                    (number)nowTime : 当前时间毫秒数,
                    (number)interval : 距离第一次调用本方法已经过了多久毫秒数
                    (number)countDown60s : 倒计时60s所剩余的描述
                    (number)countDown10Mins_m : 倒计时10分钟剩余分钟数
                    (number)countDown10Mins_s : 倒计时10分钟剩余的秒数部分
                };
 * @throws 
 * @author zhuzheng                      ()-().----.          .       
 * @date   2015年10月10日     
 * @tips  调用时假如需要传参数，请使用匿名函数来传 setTimeout(function(){ChkUtil.countDown(time,true);},1000); 
 * @param  type 用来记录，是那个地方要使用的，存cookie以保存,         \"/` ___  ;________.'
 *	使用前，需要存一下cookie，ChkUtil.saveCookie3(type,now,1);//一分钟有效期的cookie
 */
ChkUtil.initialTime = 0;
ChkUtil.countDown = function(type,checkNewFlag){
	var now = (new Date()).getTime();
	/*
	if(ChkUtil.getCookie(type)!=null || ChkUtil.initialTime != 0){//上次倒计时没有结束
		ChkUtil.initialTime = ChkUtil.getCookie(type);
	}else if(checkNewFlag == 'Y'){//上次倒计时结束了，开始新得倒计时
    	ChkUtil.saveCookie3(type,now,1);//一分钟有效期的cookie
        ChkUtil.initialTime = now;
    }
    */
    var countDownTime = {};
    if(ChkUtil.getCookie(type)!=null){//标记还有，肯定没结束
		if(ChkUtil.initialTime == 0){//用户刷新了页面
			ChkUtil.initialTime = ChkUtil.getCookie(type);
		}
    }else{//新的倒计时，或者时间到了，结束了
    	if(checkNewFlag == 'Y'){//新的倒计时
	        ChkUtil.initialTime = now;	
            ChkUtil.saveCookie3(type,now,1);
    	}else{//时间到了，但是cookie卡在这个时间里已经没有了，也就是说倒计时该结束了
    		countDownTime.timeOver = true;
    		return countDownTime;
    	}

    }
    var jsNowTIme = now;
    var interval = jsNowTIme - ChkUtil.initialTime;
    countDownTime.countDown60s = ((60 - interval/1000)>0?(60 - interval/1000):0).toFixed(0);
    countDownTime.interval = interval;
    countDownTime.jsNowTIme = jsNowTIme;
    countDownTime.initialTime = ChkUtil.initialTime;
        //countDown10Mins_m : 9 - ChkUtil.countDownTime.interval/60000>0?9 - ChkUtil.countDownTime.interval/60000:0,
        //countDown10Mins_s : 60 - ChkUtil.countDownTime.interval%60000/1000
    //console.log(countDownTime);
    return countDownTime;
};

/**
 * 判断是手机还是电脑访问 1,手机 0.电脑
 */
ChkUtil.judgeSystem = function(){
	var system ={    
        win : false,    
        mac : false,    
        xll : false    
    }; 
    var p = navigator.platform; 
    system.win = p.indexOf("Win") == 0;    
    system.mac = p.indexOf("Mac") == 0;    
    system.x11 = (p == "X11") || (p.indexOf("Linux") == 0); 
    if(system.win||system.mac||system.xll){
        return 0;   
    }
    return 1;
};
ChkUtil.changeDiv = function() {
        var main = $("#" + arguments[0]).css("display");
		var div = $("#" + arguments[1]).css("display");
		if (main == 'none' && div != 'none') {
			$("#" + arguments[0]).css("display", "block");
			$("#" + arguments[1]).css("display", "none");
		} else {
			$("#" + arguments[0]).css("display", "none");
			$("#" + arguments[1]).css("display", "block");
		};
};

//操作cookie
ChkUtil.getCookie = function(name) {
    var strCookies = document.cookie;
    var cookieName = name + "=";
    var valueBegin, valueEnd, value;
    valueBegin = strCookies.indexOf(cookieName);
    if (valueBegin == -1) {
        return null;
    }
    valueEnd = strCookies.indexOf(";", valueBegin);
    if (valueEnd == -1) {
        valueEnd = strCookies.length;
    }
    value = strCookies.substring(valueBegin + cookieName.length, valueEnd);
    return decodeURIComponent(value);
};
ChkUtil.saveCookie = function(name, value, expires, path, domain, secure) {
    var strCookie = name + "=" + encodeURIComponent(value);
    if (expires) {
        var curTime = new Date();
        curTime.setTime(curTime.getTime() + expires * 24 * 60 * 60 * 1000);
        strCookie += "; expires=" + curTime.toGMTString();
    }
    strCookie += (path) ? "; path=" + path : "";
    strCookie += (domain) ? "; domain=" + domain : "";
    strCookie += (secure) ? "; secure" : "";
    document.cookie = strCookie;
};

//格式化今日日期  返回日期
ChkUtil.formatDate = function() {
	var date = new Date();
	if (arguments[0] != undefined) {
		date = new Date(arguments[0]);
	}
	var datStr = (date.getMonth() + 1);
	return date.getFullYear() + "-" + (datStr >= 10 ? datStr : '0' + datStr)
			+ "-" + date.getDate();
};
//格式化今日日期  返回日期时间
ChkUtil.formatDate2 = function(nowTime) {
	var   now = new Date(nowTime);
    var   year=now.getFullYear();       
    var   month=now.getMonth()+1;       
    var   date=now.getDate();       
    var   hour=now.getHours();       
    var   minute=now.getMinutes();       
    var   second=now.getSeconds();       
    return   year+"-"+month+"-"+date+"   "+hour+":"+minute+":"+second;     
};


//关闭window窗口
ChkUtil.closeWin = function () {
	var browserName = navigator.appName;
	if (browserName == "Netscape") {
		var opened = window.open('about:blank', '_self');
		opened.opener = null;
		opened.close();
	} else if (browserName == "Microsoft Internet Explorer") {
		window.opener = null;
		window.open('', '_self');
		window.close();
	}
};
//根据身份证计算年龄
ChkUtil.getAgeByIdCard  = function (idCard){
	 var usercode = idCard;
	 if(usercode=='' || usercode.length != 18){ 
	    return 0;
	 }
	 var date = new Date();
	 var year = date.getFullYear(); 
	 var birthday_year = parseInt(usercode.substr(6,4));
	  var userage= year - birthday_year;
	 return userage>0?userage:0;
};

//去除表单Input前后空格
ChkUtil.form_trim = function (form){
	var $input=form.find("input[type='text']");
	var $input_2=form.find("input[type='input']");
	$input.each(function(i,n){
		 var value = $(n).val(); 
		 $(n).val($.trim(value));
	});
	$input_2.each(function(i,n){
		var value = $(n).val();
		$(n).val($.trim(value));
	});
}

function showCitys(inputId,contextPath){
	 $(inputId).jcity({
        urlOrData: contextPath + "/js/citydata.json",
        animate: { showClass: 'animated flipInX', hideClass: 'animated flipOutX' },  // 需要第一步引用的animate.min.css文件，也可以自己定义动画 
        onChoice: function (data) {
            console.log(data);
        }
    });
}

function page_jump(url){
	ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
	window.location.href=url;
}
function page_back(url) {
	var param = ChkUtil.getCookie("queryParams");
	if(null != param){
		if(url.indexOf("?") > -1){
			url += "&" + param;
		}else{
			url += "?" + param;
		}
	}
	window.location.href = url;
}