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

$(function() {
	// 全选框变化时，下面的选择框同步变化
	$("#checkAll").change(function() {
		if ($("#checkAll").prop("checked")) {
			$.uniform.update($(".checkboxFile").prop("checked", true));
		} else {
			$.uniform.update($(".checkboxFile").prop("checked", false));
		}
	});
	// 右键效果
	smartMenu();
	// 页面初始化时，统计选中个数
	$("#checkedNum").html($(".checkboxFile:checked").length);
	// 选择框变化时，统计选中个数
	$(".checkbox").change(function() {
		$("#checkedNum").html($(".checkboxFile:checked").length);
	});
	// 图片放大效果
	$('.div').viewer();

	$('.img').click(
			function() {
				$(".checkboxParent").each(
						function() {
							var me = $(this).find(".img");
							var title = $(this).find(".titleDiv").children()
									.attr('title');
							var src = me.attr("src");
							var arr = new Array();
							arr = title.split('.');
							var postfix = "";
							if(arr.length > 1){
								postfix = arr[1].toLowerCase();
							}
							if ((postfix == 'jpg' || postfix == 'jpeg' || postfix == 'tif'|| postfix == 'tiff'
									|| postfix == 'bmp' || postfix == 'png')
									&& arr[0] != 'fileImg'
									&& src.indexOf("fileImg") >= 0) {
								// 文件类型为图片且不为默认图片时，重新获取图片
								var id = $(this).attr('id');
								var url = serverPath + "/productFile/getFile.htm";
								// 文件src属性填二进制字符串
								var options = {
									url : url,
									type : 'post',
									dataType : 'json',
									data : {
										"Time" : new Date().getMilliseconds(),
										"fileIdListString" : id
									},
									success : function(result) {
										me.attr("src", result.data);
									}
								};
								$.ajax(options);
							}
						});
			});
});

function smartMenu() {
	var isView = fileUploadDto.isView;
	var imageMenuData = '';
	if (isView == 1) {
		imageMenuData = [
				[ {
					text : "下载",
					func : function() {
						var url = serverPath + "/productFile/downLoadFile.htm";
						window.open(url + '?fileIdListString='
								+ $("#smartMenuId").val());
					}
				} ],
				[ {
					text : "删除",
					func : function() {
						BootstrapDialog.confirm('确认删除文件？', function(result) {
							if (result) {
								var options = {
									url : serverPath
											+ "/productFile/delFile.htm",
									type : 'post',
									dataType : 'json',
									data : {
										"Time" : new Date().getMilliseconds(),
										"fileIdListString" : $("#smartMenuId")
												.val()
									},
									success : function(data) {
										window.location = window.location;
									}
								};
								$.ajax(options);
							}
						});
					}
				} ] ];
	} else {
		imageMenuData = [ [ {
			text : "下载",
			func : function() {
				var url = serverPath + "/productFile/downLoadFile.htm";
				window.open(url + '?fileIdListString='
						+ $("#smartMenuId").val());
			}
		} ] ];
	}

	$(".img").smartMenu(
			imageMenuData,
			{
				name : "image",
				beforeShow : function() {
					var smartMenuId = $(this).parent().attr('id');
					$("#smartMenuId").val(smartMenuId);
					var name = $(this).next().children().children().find(
							"input").attr("checked", "checked")
					$.uniform.update($(this).next().children().children().find(
							"input").attr("checked", "checked"));
				}
			});
}

/*
 * url 目标url arg 需要替换的参数名称 arg_val 替换后的参数的值 return url 参数替换后的url
 */
function changeURLArg(url, arg, arg_val) {
	var pattern = arg + '=([^&]*)';
	var replaceText = arg + '=' + arg_val;
	if (url.match(pattern)) {
		var tmp = '/(' + arg + '=)([^&]*)/gi';
		tmp = url.replace(eval(tmp), replaceText);
		return tmp;
	} else {
		if (url.match('[\?]')) {
			return url + '&' + replaceText;
		} else {
			return url + '?' + replaceText;
		}
	}
	return url + '\n' + arg + '\n' + arg_val;
}

$(function() {
	var uploader = $('#fileupload');
	var url = serverPath + "/productFile/upLoadFileForRepay.htm";

	// Initialize the jQuery File Upload widget:
	uploader.fileupload({

		// Uncomment the following to send cross-domain cookies:
		// xhrFields: {withCredentials: true},
		'url' : url,
		'method' : 'POST',
		'autoUpload' : false,
		'maxFileSize' : 10000000,// 单个文件限制10M
		'maxNumberOfFiles' : 4000,
		'previewMaxWidth' : 50,
		'previewMaxHeight' : 50
	});

	uploader.bind('fileuploadfailed', function(e, data) {
		// eva.p(data);
	});
	uploader.bind('fileuploadadded', function(e, data) {

		// alert('my-add');
		// if(!data.files.valid) {
		// uploader.find('.files .cancel').click();
		// }
	});
	uploader.bind('fileuploadchange', function(e, data) {
		// alert('my-change');
		// uploader.find('.files').empty();
	});

	uploader.bind('fileuploaddone', function(e, data) {

		// alert('上传成功');
	});
});

$("#fileuploadShow").on('hidden', function() {
	window.location = window.location;
})

function down() {
	var fileIdListString = "";
	$(".checkboxFile").each(function() {
		if ($(this).prop("checked")) {
			var id = $(this).parents('.checkboxParent').attr('id');
			if (fileIdListString == "") {
				fileIdListString = id;
			} else {
				fileIdListString = fileIdListString + ',' + id;
			}
		}
	});
	console.log('fileIdListString' + fileIdListString);
	if (fileIdListString == "") {
		BootstrapDialog.alert("请选择要下载的文件");
		return;
	}
	var url = serverPath + "/productFile/downLoadFile.htm";
	window.open(url + '?fileIdListString=' + fileIdListString);
}

function del() {
	var fileIdListString = "";
	$(".checkboxFile").each(function() {
		if ($(this).prop("checked")) {
			var id = $(this).parents('.checkboxParent').attr('id');
			if (fileIdListString == "") {
				fileIdListString = id;
			} else {
				fileIdListString = fileIdListString + ',' + id;
			}
		}
	});
	if (fileIdListString == "") {
		BootstrapDialog.alert("请选择要删除的文件");
		return;
	}
	BootstrapDialog.confirm('确认删除文件？', function(result) {
		if (result) {
			var options = {
				url : serverPath + "/productFile/delFile.htm",
				type : 'post',
				dataType : 'json',
				data : {
					"Time" : new Date().getMilliseconds(),
					"fileIdListString" : fileIdListString,
					"applyLoanNo" : $("#applyLoanNo").val()
				},
				success : function(data) {
					window.location = window.location;
				}
			};
			$.ajax(options);
		}
	});

}