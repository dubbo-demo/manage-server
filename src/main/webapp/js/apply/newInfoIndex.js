var FormWizard = function() {

	return {
		// main function to initiate the module
		init : function() {
			if (!jQuery().bootstrapWizard) {
				return;
			}

			function format(state) {
				if (!state.id)
					return state.text; // optgroup
				return "<img class='flag' src='assets/img/flags/"
						+ state.id.toLowerCase() + ".png'/>&nbsp;&nbsp;"
						+ state.text;
			}

			$("#country_list").select2({
				placeholder : "Select",
				allowClear : true,
				formatResult : format,
				formatSelection : format,
				escapeMarkup : function(m) {
					return m;
				}
			});

			var form = $('#submit_form');
			var error = $('.alert-error', form);
			var success = $('.alert-success', form);

			form.validate({
				doNotHideMessage : true, // this option enables to show the
											// error/success messages on tab
											// switch.
				errorElement : 'span', // default input error message container
				errorClass : 'validate-inline', // default input error message
												// class
				focusInvalid : false, // do not focus the last invalid input
				rules : {
					// account
					username : {
						minlength : 5,
						required : true
					},
					password : {
						minlength : 5,
						required : true
					},
					rpassword : {
						minlength : 5,
						required : true,
						equalTo : "#submit_form_password"
					},
					// profile
					fullname : {
						required : true
					},
					email : {
						required : true,
						email : true
					},
					phone : {
						required : true
					},
					gender : {
						required : true
					},
					address : {
						required : true
					},
					city : {
						required : true
					},
					country : {
						required : true
					},
					// payment
					card_name : {
						required : true
					},
					card_number : {
						minlength : 16,
						maxlength : 16,
						required : true
					},
					card_cvc : {
						digits : true,
						required : true,
						minlength : 3,
						maxlength : 4
					},
					card_expiry_mm : {
						digits : true,
						required : true
					},
					card_expiry_yyyy : {
						digits : true,
						required : true
					},
					'payment[]' : {
						required : true,
						minlength : 1
					}
				},

				messages : { // custom messages for radio buttons and
								// checkboxes
					'payment[]' : {
						required : "Please select at least one option",
						minlength : jQuery
								.format("Please select at least one option")
					}
				},

				errorPlacement : function(error, element) { // render error
															// placement for
															// each input type
					if (element.attr("name") == "gender") { // for uniform radio
															// buttons, insert
															// the after the
															// given container
						error.addClass("no-left-padding").insertAfter(
								"#form_gender_error");
					} else if (element.attr("name") == "payment[]") { // for
																		// uniform
																		// radio
																		// buttons,
																		// insert
																		// the
																		// after
																		// the
																		// given
																		// container
						error.addClass("no-left-padding").insertAfter(
								"#form_payment_error");
					} else {
						error.insertAfter(element); // for other inputs, just
													// perform default behavoir
					}
				},

				invalidHandler : function(event, validator) { // display error
																// alert on form
																// submit
					success.hide();
					error.show();
					App.scrollTo(error, -200);
				},

				highlight : function(element) { // hightlight error inputs
					$(element).closest('.help-inline').removeClass('ok'); // display
																			// OK
																			// icon
					$(element).closest('.control-group').removeClass('success')
							.addClass('error'); // set error class to the
												// control group
				},

				unhighlight : function(element) { // revert the change dony by
													// hightlight
					$(element).closest('.control-group').removeClass('error'); // set
																				// error
																				// class
																				// to
																				// the
																				// control
																				// group
				},

				success : function(label) {
					if (label.attr("for") == "gender"
							|| label.attr("for") == "payment[]") { // for
																	// checkboxes
																	// and radip
																	// buttons,
																	// no need
																	// to show
																	// OK icon
						label.closest('.control-group').removeClass('error')
								.addClass('success');
						label.remove(); // remove error label here
					} else { // display success icon for other inputs
						label.addClass('valid ok') // mark the current input as
													// valid and display OK icon
						.closest('.control-group').removeClass('error')
								.addClass('success'); // set success class to
														// the control group
					}
				},

				submitHandler : function(form) {
					$(".js_input").each(function(i, v) {
						var _val = $(this).val();
						if (_val == "") {
							$(this).addClass("error");
							return false;
						}
					})
					success.show();
					error.hide();
					// add here some ajax code to submit your form or just call
					// form.submit() if you want to submit the form without ajax
				}

			});

			// default form wizard
			$('#form_wizard_1')
					.bootstrapWizard(
							{
								'nextSelector' : '.button-next',
								'previousSelector' : '.button-previous',
								onTabClick : function(tab, navigation, index) {
									// alert('on tab click disabled');
									return false;
								},
								onNext : function(tab, navigation, index) {
									var returnBoolean = true;
									success.hide();
									error.hide();
									
									var tab_a = tab.find("a");
									var onclick = tab_a.attr("next-onclick");
									if (onclick) {
										var temp = window[onclick]() == undefined ? true:false;
										if(!temp){
											return false;
										}
									}
									var total = navigation.find('li').length;
									var current = index + 1;
									// set done steps
									jQuery('li', $('#form_wizard_1'))
											.removeClass("done");
									var li_list = navigation.find('li');
									for (var i = 0; i < index; i++) {
										jQuery(li_list[i]).addClass("done");
									}

									if (current == 1) {
										$('#form_wizard_1').find(
												'.button-previous').hide();
									} else {
										$('#form_wizard_1').find(
												'.button-previous').show();
									}

									if (current >= total) {
										$('#form_wizard_1')
												.find('.button-next').hide();
										$('#form_wizard_1').find(
												'.button-submit').show();
										$('#form_wizard_1').find(
												'.btn.attachment').show();
									} else {
										$('#form_wizard_1')
												.find('.button-next').show();
										$('#form_wizard_1').find(
												'.button-submit').hide();
										$('#form_wizard_1').find(
												'.btn.attachment').hide();
									}
									App.scrollTo($('.page-title'));
									return returnBoolean;
								},
								onPrevious : function(tab, navigation, index) {
									$("#companyAddress").next('.city-popup').remove();
									success.hide();
									error.hide();

									var total = navigation.find('li').length;
									var current = index + 1;
									// set done steps
									jQuery('li', $('#form_wizard_1'))
											.removeClass("done");
									var li_list = navigation.find('li');
									for (var i = 0; i < index; i++) {
										jQuery(li_list[i]).addClass("done");
									}

									if (current == 1) {
										$('#form_wizard_1').find(
												'.button-previous').hide();
									} else {
										$('#form_wizard_1').find(
												'.button-previous').show();
									}

									if (current >= total) {
										$('#form_wizard_1')
												.find('.button-next').hide();
										$('#form_wizard_1').find(
												'.button-submit').show();
										$('#form_wizard_1').find(
												'.btn.attachment').show();
									} else {
										$('#form_wizard_1')
												.find('.button-next').show();
										$('#form_wizard_1').find(
												'.button-submit').hide();
										$('#form_wizard_1').find(
												'.btn.attachment').hide();
									}

									App.scrollTo($('.page-title'));
								},
								onTabShow : function(tab, navigation, index) {
									var total = navigation.find('li').length;
									var current = index+1;
									var $percent = (current / total) * 100;
									$('#form_wizard_1').find('.bar').css({
										width : $percent + '%'
									});
									
									// 返回按钮
									if (current == 1) {
										$('#form_wizard_1').find(
										'.button-previous').hide();
										$('#form_wizard_1').find(
												'.button-back').show();
										$('#form_wizard_1').find(
										'.button-back').attr("href",serverPath+"/apply/manage/list.htm?menu_index=1");
									} else {
										$('#form_wizard_1').find(
												'.button-back').hide();
									}
									
									var tab_a = tab.find("a");
									var ajax = tab_a.attr("ajax");
									if (ajax == "true") {
										var html = $(tab_a.attr("data-target"))
												.text();
										if ("" == html.trim()) {
											var href_a = tab_a.attr("href");
											if (applyLoanNo) {
												if (href_a.indexOf("?") != -1) {
													href_a += "&applyLoanNo="
															+ applyLoanNo;
												} else {
													href_a += "?applyLoanNo="
															+ applyLoanNo;
												}
											}
											$(tab_a.attr("data-target"))
													.load(
															href_a,
															window[tab_a
																	.attr("ajax-recall")]);
										}
									}
								}
							});

			$('#form_wizard_1').find('.button-previous').hide();
			var current_tab =  $('#form_wizard_1 li[state='+state+'] a');
			var total = $('#form_wizard_1 li').length;
			current_tab.tab('show');
			//这里从1开始
			var current = current_tab.find(".number").text();
			jQuery('li', $('#form_wizard_1'))
			.removeClass("done");
			var li_list =  $('#form_wizard_1 li');
			for (var i = 0; i < (parseInt(current)); i++) {
				jQuery(li_list[i]).addClass("done");
			}
			if (current == 1 || current == '') {
				$('#form_wizard_1').find(
						'.button-previous').hide();
			} else {
				$('#form_wizard_1').find(
						'.button-previous').show();
			}

			if (current >= total) {
				$('#form_wizard_1')
						.find('.button-next').hide();
				$('#form_wizard_1').find(
						'.button-submit').show();
				$('#form_wizard_1').find(
						'.btn.attachment').show();
			} else {
				$('#form_wizard_1')
						.find('.button-next').show();
				$('#form_wizard_1').find(
						'.button-submit').hide();
				$('#form_wizard_1').find(
						'.btn.attachment').hide();
			}
			
		}

	};

}();
function save() {
	var tab_a = $('#form_wizard_1 .active').find("a");
	var attr_save = tab_a.attr('save');
	window[attr_save]();
}