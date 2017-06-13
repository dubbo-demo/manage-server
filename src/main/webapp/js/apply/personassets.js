/**
 * 
 * 个人资产
 */
function personassetsInit(data) {
	var form = $("#personassets_form");
	form.validate({
		rules : {
			// account
			houseNum : {
				isInt:true
			},
			loanHouseNum : {
				isInt:true
			},
			loanHouseAmount : {
				isFloat:true
			},
			carNum : {
				isInt:true
			},
			loanCarAmount : {
				isFloat:true
			},
			loanCarNum : {
				isInt:true
			}
		},

		messages : { // custom messages for radio buttons and checkboxes
		}
	});
}
/**
 * TODO
 * @returns
 */
function personassetsNext() {
	var record = {
			state:COMPOSITE_OPINION
	};
	var form = $("#personassets_form");
	if (!form.valid()) {
		return false;
	}
	return personassetsSave(record);
}
/**
 * TODO
 * @param record
 */
function personassetsSave(record){
	var form = $("#personassets_form");
	if(!record){
		record = {
				state:PERSON_ASSETS
		};
	}
	var dom = form.find("input");
	var i = 0;
	dom.each(function(index, input) {
		var value = $(input).val();
		if("" != value){
			record[$(input).attr('name')] = value;
			i++;
		}
	});
	if(i <= 0){
		return ;
	}
	//TODO
	record.applyLoanNo = applyLoanNo;
	
	$.ajax({
		url : serverPath+'/apply/newInfoIndex/personassetsSave.htm',
		type : 'post',
		data : JSON.stringify(record),
		dataType : 'json',
		contentType : "application/json",
		success : function(res) {
			if (res.code == 0) {
				if(record.state == PERSON_ASSETS || record.state == BACK_INIT){
					BootstrapDialog.alert("操作成功！");
				}
			} else {
				BootstrapDialog.alert(res.message);
			}
		},
		error : function() {
			BootstrapDialog.alert("操作失败");
		}
	});
}