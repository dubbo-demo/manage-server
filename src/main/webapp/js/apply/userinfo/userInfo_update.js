var userinfo_FormSamples = function () {
		var bmurl = serverPath+"/employee/showBMEmpoyee.htm";
		var custmerurl = serverPath+"/employee/showCustomerEmpoyee.htm";
	    return {   
	        init: function () {
//	            $("#customerServiceId").select2({
//	                placeholder: ChkUtil.select2Name,
////	                allowClear: true,
//	                minimumInputLength: 1,
//	                query: function (query) {
//	                	$.getJSON(custmerurl, {"nameSpell" : query.term}, function(result) {
//		            		var data = {
//		                        results: result.data
//		                    };
//		            		query.callback(data);
//						});
//	                }
//	            });  
	            $("#bmId").select2({
	                placeholder: ChkUtil.select2Name,
//	                allowClear: true,
	                minimumInputLength: 1,
	                query: function (query) {
	                	$.getJSON(bmurl, {"nameSpell" : query.term}, function(result) {
	                		var data = {
	                            results: result.data
	                        };
	                		query.callback(data);
	        			});
	                }
	            });  
	            
	        }
	    };

	}();
userinfo_FormSamples.initReception = function () {
		$("#s2id_bmId .select2-choice span").html($("#bmName").val());
//		$("#s2id_customerServiceId .select2-choice span").html($("#customerSName").val());
		initBaseSelect('sex');
		initBaseSelect('eduCode');
		initBaseSelect('mailAddress');
		initBaseSelect('liveProperty');
		initBaseSelect('qualifiType');
		initBaseSelect('maritalStatus');
		initBaseSelect('haveChild');
		
}
$(function() {
//	getNode("#prodType","proType");
	userinfo_FormSamples.init();
//	getNode("#tab_userinfo select[name='sex']","sex");
	userinfo_FormSamples.initReception();
	
	$("#tab_userinfo").validate({
		rules : {
			memberName : {
				required : true,
				maxlength : 10
			},
			idCard : {
				required : true
				,idCard : true
			},
			phone : {
				required : true
				,phone : true
			},
			teamManager : {
				teamManager : true
			},
			bmId : {
				required : true
				,bmId : true
			},
			customerServiceId : {
				required : true
				,customerServiceId : true
			},
			nameSpell : {
				required : true
			},
			sex : {
				required : true
			},
			eduCode : {
				required : true
			},
			idCarNo : {
				required : true
			},
			nation : {
				required : true
			},
			phone : {
				required : true
			},
//			SNSAccount : {
//				required : true
//			},
//			liveAddr : {
//				required : true
//				,changeCity : true
//			},
//			censusAddr : {
//				required : true
//				,changeCity : true
//			},
			liveAddress : {
				required : true
			},
			censusAddress : {
				required : true
			},
			mailAddress : {
				required : true
			},
			email : {
				email:true
			},
			liveProperty : {
				required : true
			},
			qualifiType : {
				required : true
			},
			maritalStatus : {
				required : true
			},
			haveChild : {
				required : true
			},
			haveLoan : {
				required : true,
				min:0,
				max:100000000,
			},
			liveYears : {
				required : true,
				min:0.1,
				max:100,
				maxlength:4
			}
		},
		submitHandler : function(form) { // 表单提交句柄,为一回调函数，带一个参数：form
			saveUserInfo(); //提交表单
		}
	});
});


