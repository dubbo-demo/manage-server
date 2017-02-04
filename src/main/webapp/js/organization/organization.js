//用于控制岗位信息是否展示
var organizationPostManageFlag = false;

// 加载组织结构
function showOrganization(setting, obj) {
	var zNodes = new Array();
	var url = serverPath + "/organization/selectAllOrgInfo.htm";
	var data = {
		"Time" : new Date().getMilliseconds()
	};
	$.getJSON(url, data, function(result) {
		var resultData = result.data;
		for (var i = 0; i < resultData.length; i++) {
			var treeNote = {};
			treeNote.id = resultData[i].id;
			treeNote.pId = resultData[i].parentId;
			treeNote.name = resultData[i].orgName;
			treeNote.level = resultData[i].orgLevel;
			treeNote.type = resultData[i].orgType;
			treeNote.open = true;
			zNodes.push(treeNote);
		}
		$.fn.zTree.init(obj, setting, zNodes);
	});

}

function initTree(obj) {
	var setting = {
		view : {
			dblClickExpand : false
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			onClick : onClickTree
		}
	};
	showOrganization(setting, obj);
}

function initTreePullDown(obj) {
	var settingLeft = {
		view : {
			dblClickExpand : false
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			onClick : onClick
		}
	};
	showOrganization(settingLeft, obj);
}

$(function() {
	// $("#selectPostThead").hide();
	// 默认选中新建总部
	$("#orgTypeSelected").siblings().removeAttr('selected');
	$("#orgTypeSelected").attr("selected", "selected");
	// 初始化组织树
	initTree($("#tree"));
	initTreePullDown($("#treePullDown"));
	// 选择框变化时，显示不同信息
	orgTypeChange();
	// 加载地区编码菜单
	showSelectCityCode();
	// 初始化数据
	$("#insertOrUpdate").val(0);
	$("#parentId").val(0);
	// initOrgPositionInfo();
	showPostInfo();
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

function showMenu() {
	var type = $("#orgType").val();
	if (type != 1 && type != 2) {
		var cityObj = $("#citySel");
		var cityOffset = $("#citySel").offset();
		$("#menuContent").css({
			left : cityOffset.left + "px",
			top : cityOffset.top + cityObj.outerHeight() + "px"
		}).slideDown("fast");
		$("body").bind("mousedown", onBodyDown);
	}
}

function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(
			event.target).parents("#menuContent").length > 0)) {
		hideMenu();
	}
}

function onClick(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("treePullDown"), nodes = zTree
			.getSelectedNodes(), v = "";
	nodes.sort(function compare(a, b) {
		return a.id - b.id;
	});
	for (var i = 0, l = nodes.length; i < l; i++) {
		v += nodes[i].name + ",";
	}
	if (v.length > 0)
		v = v.substring(0, v.length - 1);
	$("#citySel").val(v);
	$("#parentId").val(nodes[0].id);
	hideMenu();
}

function onClickTree() {
	if (organizationPostManageFlag) {
		return;
	}
	// 获取选中的ID
	var zTree = $.fn.zTree.getZTreeObj("tree");
	var nodes = zTree.getSelectedNodes();
	if (nodes.length < 1) {
		BootstrapDialog.alert("请先选中要修改的节点");
		return;
	}
	var id = nodes[0].id;
	var type = nodes[0].type;
	if (type == 1) {
		return false;
	}
	$("#postManage").show();
	$("#selectPost tbody").html("");
	// 查找选中节点及其子节点下所有组织岗位信息
	var url = serverPath + "/orgPosition/selectOrgPositionInfo.htm";
	var data = {
		"Time" : new Date().getMilliseconds(),
		"id" : id
	};
	$.getJSON(url, data, function(result) {
		var data = result.data;
		$("#selectPost tbody").empty();
		for (var i = 0; i < data.length; i++) {
			var trHTML = "<tr id=" + data[i].id + ">" + "<td id="
					+ data[i].orgId + ">" + data[i].positionName + "</td>"
					+ "<td>" + data[i].positionCode + "</td>" + "<td>"
					+ data[i].orgName + "</td>" + "<td><a onclick='updatePost("
					+ data[i].id + ");return false;'>修改</a></td>"
					+ "<td><a onclick='delPostShow(" + data[i].id
					+ ");return false;'>删除</a></td></tr>";
			$("#selectPost").append(trHTML);
		}

	});
}

function build() {
	$("#insertOrUpdate").val("0");
	$("#newType").show();
	$(".btnTop").hide();
	$("#changeOrganization input[type='text']").val("");
	$("#topName").html("新增总部");
	// 默认选中新建总部
	$("#orgTypeSelected").siblings().removeAttr('selected');
	$("#orgTypeSelected").attr("selected", "selected");
	$(".cityCodeTr").hide();
	$("#postManage").hide();
	$("#orgCode").attr('readonly', false);
	$("#architect").attr('readonly', true);
	organizationPostManageFlag = true;
}

function update() {
	organizationPostManageFlag = true;
	$("#insertOrUpdate").val("1");
	$("#postManage").hide();
	$("#changeOrganization input[type='text']").val("");
	// 获取选中的ID
	var zTree = $.fn.zTree.getZTreeObj("tree");
	var nodes = zTree.getSelectedNodes();
	if (nodes.length < 1) {
		BootstrapDialog.alert("请先选中要修改的节点");
		return;
	}
	var id = nodes[0].id;
	// 获取节点信息
	var url = serverPath + "/organization/selectOrganizationById.htm";
	var data = {
		"Time" : new Date().getMilliseconds(),
		"id" : id
	};
	$.getJSON(url, data, function(result) {
		var data = result.data;
		// 根据ID找到对应的信息进行展示
		$(".cityCodeTr").hide();
		$("#newType").show();
		$(".btnTop").hide();
		var type = data.orgType;
		if (type == 1) {
			$("#topName").text("修改大区");
		} else if (type == 2) {
			$("#topName").text("修改总部");
		} else if (type == 3) {
			$(".cityCodeTr").show();
			var cityId = data.cityId;
			var t = $("#city").options;
			$("#city").val(cityId);
			$("#topName").text("修改门店");
		} else if (type == 4) {
			$("#topName").text("修改部门");
		}
		$("#orgType").val(type);
		var parentId = data.parentId;
		if (parentId != "") {
			var urlParent = serverPath + "/organization/selectOrganizationById.htm";
			var dataParent = {
				"Time" : new Date().getMilliseconds(),
				"id" : parentId
			};
			$.getJSON(urlParent, dataParent, function(result) {
				$('#citySel').val(result.data.orgName);
			});

		}
		$("#orgCode").val(data.orgCode);
		$("#orgName").val(data.orgName);
		$("#address").val(data.address);
		$("#telephone").val(data.telephone);
		$("#OrganizationId").val(id);
	});

}

function save() {
	// 提交前进行合法校验
	// 1、名称、编号不可为空
	if ($("#orgCode").val() == "") {
		$("#error").html('<font color="red">' + "组织编号不可为空" + '</font>');
		document.getElementById("error").style.display = "block";
		return;
	}
	if ($("#orgName").val() == "") {
		$("#error").html('<font color="red">' + "组织名称不可为空" + '</font>');
		document.getElementById("error").style.display = "block";
		return;
	}
	if (type == 3) {
		if ($("#address").val() == "") {
			$("#error").html('<font color="red">' + "门店地址不可为空" + '</font>');
			document.getElementById("error").style.display = "block";
			return;
		}
		if ($("#telephone").val() == "") {
			$("#error").html('<font color="red">' + "门店电话不可为空" + '</font>');
			document.getElementById("error").style.display = "block";
			return;
		}
	}
	// 2、新建类型为部门或门店时，上级部门不可为空
	var type = $("#orgType").val();
	if (type == 3 || type == 4) {
		var zTree = $.fn.zTree.getZTreeObj("treePullDown");
		var nodes = zTree.getSelectedNodes();
		if ($('#citySel').val() == '') {
			$("#error").html('<font color="red">' + "上级组织不可为空" + '</font>');
			document.getElementById("error").style.display = "block";
			return;
		}
		if (nodes.length > 0) {
			var selectType = nodes[0].type;
			if (type == 3) {
				if (selectType != 1) {
					$("#error").html(
							'<font color="red">' + "上级组织必须为大区" + '</font>');
					document.getElementById("error").style.display = "block";
					return;
				}
			}
			if (type == 4) {
				if (selectType == 4) {
					$("#error").html(
							'<font color="red">' + "请选择正确的上级组织【不可为部门】"
									+ '</font>');
					document.getElementById("error").style.display = "block";
					return;
				}

			}
		}
	} else if (type == 1 || type == 2) {
		if (!$('#citySel').val() == '') {
			$("#error").html('<font color="red">' + "上级组织必须为空" + '</font>');
			document.getElementById("error").style.display = "block";
			return;
		}
	}
	// 3、新建类型为门店时，地市代码不可为“请选择”
	if (type == 3) {
		var city = $("#city").val();
		if (city == "请选择") {
			$("#error").html('<font color="red">' + "请选择地市" + '</font>');
			document.getElementById("error").style.display = "block";
			return;
		}
	}

	// 编码统一小写改大写
	$("#orgCode").val($("#orgCode").val().toUpperCase());

	// 提交
	var options = {
		url : serverPath + '/organization/changeOrganization.htm',
		type : 'post',
		dataType : 'json',
		data : $("#changeOrganization").serialize(),
		success : function(data) {
			var model = eval(data.data);
			// 0、不重复， 1、名称重复，2、编号重复，3、名称编号都重复 地市编码表中 0不存在，1存在
			if (model.checkNameAndCode == 0 && model.checkCityCode == 1) {
				BootstrapDialog.alert('操作成功', function() {
					organizationPostManageFlag = false;
					window.location.href = window.location;
				});
			} else {
				var errMsg = "";
				if (model.checkNameAndCode == 1) {
					errMsg = "组织名称重复";
				} else if (model.checkNameAndCode == 2) {
					errMsg = "组织编号重复";
				} else if (model.checkNameAndCode == 3) {
					errMsg = "组织编号及名称重复";
				}
				if (model.checkCityCode == 0) {
					errMsg += "地市编码不存在";
				}
				$("#error").html('<font color="red">' + errMsg + '</font>');
				document.getElementById("error").style.display = "block";
				$("#citySel").val("");
			}
		}
	};
	$.ajax(options);
}

function back() {
	$("#newType").hide();
	$(".btnTop").show();
	$("#changeOrganization input[type='text']").val("");
	$("#topName").html("");
	$("#error").html("");
	$("#postManage").show();
	organizationPostManageFlag = false;
}

// 选择框变化时，显示不同信息
function orgTypeChange() {
	$("#orgType").change(function() {
		//重新加载下拉组织信息，避免上次选中照成的干扰
		var zTree = $.fn.zTree.getZTreeObj("treePullDown");
		zTree.cancelSelectedNode();
		$("#parentId").val(0);
		$("#changeOrganization input[type='text']").val("");
		var type = $(this).val();
		var insertOrUpdate = $("#insertOrUpdate").val();
		$("#newMain input").val("");
		$(".cityCodeTr").hide();
		$("#orgCode").attr('readonly', false);
		if (type == 1) {
			$("#topName").text("新增大区");
		} else if (type == 2) {
			$("#topName").text("新增总部");
		} else if (type == 3) {
			$(".cityCodeTr").show();
			$("#topName").text("新增门店");
			// 设置为只读
			$("#orgCode").attr('readonly', true);
		} else if (type == 4) {
			$("#topName").text("新增部门");
		}
	});
}

// 加载地区编码select选择框
function showSelectCityCode() {
	$("#city").empty();
	$("#city").append("<option value=0>请选择</option>");
	var url = serverPath + "/cityCode/selectCityCode.htm";
	var data = {
		"Time" : new Date().getMilliseconds()
	};
	$.getJSON(url, data, function(result) {
		var selectCityCode = result.data.selectCityCode;
		for (var i = 0; i < selectCityCode.length; i++) {
			var op = "<option data-val=" + selectCityCode[i].cityCode
					+ " value=" + selectCityCode[i].id + ">"
					+ selectCityCode[i].cityName + "</option>";
			$("#city").append(op);
		}
	});
}

// 加载城市代码时，拼装门店编号。规则：地市代码+两位流水号，流水号为当前城市下门店数量+1
function showStoreCode() {

	var options = $("#city option:selected");
	var cityCode = options.data("val");
	var cityId = options.val();
	var url = serverPath + "/organization/selectOrgByCityId.htm";
	var data = {
		"Time" : new Date().getMilliseconds(),
		"cityId" : cityId
	};
	$.getJSON(url, data, function(result) {
		var data = result.data;
		var param = "";
		if (data.length < 9) {
			param = "0" + (data.length + 1);
		} else {
			param = data.length + 1;
		}
		$("#orgCode").val(cityCode + param);
		$("#cityId").val(cityId);
		$("#cityCode").val(cityCode);
	});
}

function delOrg() {
	$("#insertOrUpdate").val("1");
	// 获取选中的ID
	var zTree = $.fn.zTree.getZTreeObj("tree");
	var nodes = zTree.getSelectedNodes();
	if (nodes.length < 1) {
		BootstrapDialog.alert("请先选中要修改的节点");
		return;
	}
	BootstrapDialog.confirm('删除组织？', function(result) {
		if (result) {
			del();
		}
	});
}

function del() {
	var zTree = $.fn.zTree.getZTreeObj("tree");
	var nodes = zTree.getSelectedNodes();
	var id = nodes[0].id;
	var url = serverPath + "/organization/delOrganization.htm";
	var data = {
		"Time" : new Date().getMilliseconds(),
		"id" : id
	};
	$.getJSON(url, data, function(result) {
		var data = result.data;
		if (data.checkResult == 0) {
			window.location.href = window.location;
		} else {
			BootstrapDialog.alert(result.message);
		}
	});
}

// 新增岗位
function addPost() {
	// 对选中的值进行校验，只有选中部门时，才新增岗位
	// 获取选中的ID
	var zTree = $.fn.zTree.getZTreeObj("tree");
	var nodes = zTree.getSelectedNodes();
	if (nodes.length < 1) {
		BootstrapDialog.alert("请先选中左侧组织节点");
		return false;
	}
	$("#insertOrUpdatePost").val(0);
	var id = nodes[0].id;
	var type = nodes[0].type;
	if (type != 4) {
		BootstrapDialog.alert("请先选中岗位所在的部门");
		return false;
	}
	$("#orgId").val(id);
	//showPostInfo();
	$('#addPost').modal('show');
}

// 加载地区名称select选择框
function showPostInfo() {
	$("#positionName").empty();
	$("#positionCode").val();
	$("#position").val();
	$("#positionName").append("<option value=0>请选择</option>");
	var url = serverPath + "/orgPosition/selectPosition.htm";
	var data = {
		"Time" : new Date().getMilliseconds()
	};
	$.getJSON(url, data, function(result) {
		var data = result.data;
		$("#position").val(JSON.stringify(data));
		for (var i = 0; i < data.length; i++) {
			var op = "<option value=" + data[i].positionCode + ">"
					+ data[i].positionName + "</option>";
			$("#positionName").append(op);
		}
	});
}

// 地区名称变化时，地区编码相应改变
function changePostInfo() {
	var options = $("#positionName option:selected");
	$("#positionCode").val(options.val());
}

// 保存或修改组织岗位关联关系
function savePost() {
	var positionCode = $("#positionCode").val();
	var positionId = "";
	var position = JSON.parse($("#position").val());
	for (var i = 0; i < position.length; i++) {
		if (position[i].positionCode == positionCode) {
			positionId = position[i].id;
			break;
		}
	}
	// 修改时校验是否修改，未修改的不进行提交
	if (1 == $("#insertOrUpdatePost").val()
			&& $("#initialPositionCode").val() == positionCode) {
		backPost();
		return;
	}
	var url = serverPath + "/orgPosition/changePost.htm";
	var data = {
		"Time" : new Date().getMilliseconds(),
		"insertOrUpdatePost" : $("#insertOrUpdatePost").val(),
		"orgId" : $("#orgId").val(),
		"id" : $("#id").val(),
		"positionId" : positionId
	};
	$.getJSON(url, data, function(result) {
		backPost();
		BootstrapDialog.alert('操作成功', function() {
			// 重新加载岗位信息
			onClickTree();
		});
	});
}

function backPost() {
	$("#addPost").modal('hide');
}

// 修改岗位信息
function updatePost(id) {
	$("#addPost").modal('show');
	$("#insertOrUpdatePost").val(1);
	// 加载岗位下拉菜单，默认选中对应的岗位
	//showPostInfo();
	var cssSelect = "#" + id + " td:first-child";
	$("#id").val(id);
	var position = JSON.parse($("#position").val());
	for (var i = 0; i < position.length; i++) {
		if (position[i].positionName == $(cssSelect).text()) {
			$("#positionName").val(position[i].positionCode);
			$("#positionCode").val(position[i].positionCode);
			$("#orgId").val($(cssSelect).attr("id"));
			break;
		}
	}
	// 保存初始信息，用于校验是否修改，未修改的不进行提交
	$("#initialPositionCode").val($("#positionCode").val());
}

function delPostShow(id) {
	BootstrapDialog.confirm('删除岗位？', function(result) {
		if (result) {
			$("#id").val(id);
			delPost(id);
		}
	});
}

// 删除组织岗位对应关系
function delPost(id) {
	// 同步校验岗位下是否有人员信息
	$.ajaxSettings.async = false;
	var url = serverPath + "/orgPosition/delPost.htm";
	var data = {
		"Time" : new Date().getMilliseconds(),
		"id" : $("#id").val()
	};
	$.getJSON(url, data, function(result) {
		var data = result.data;
		if (data.checkResult != 0) {
			BootstrapDialog.alert('岗位下有人员，不能进行删除');
		} else {
			backPost();
			BootstrapDialog.alert('删除组织岗位关系成功', function() {
				// 重新加载岗位信息
				onClickTree();
			});
		}
	});
	$.ajaxSettings.async = true;
}