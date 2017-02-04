$(function() {
	init();
	
	$('#new_info').on('hide.bs.modal', function() {
		// 执行一些动作...
		$(".error").removeClass("error").html("");
	})
	$('#main').contextmenu(
			{
				target : '#context-menu2',
				before : function(e) {
					e.preventDefault();
					if (e.target.tagName == "SPAN") {
						$("#context-menu2 li:gt(0)").show();
					} else {
						$("#context-menu2 li:gt(0)").hide();
					}
					return true;
				},
				onItem : function($menu_e, e) {
					var item = $(e.target);
					var node = {}
					if ($menu_e.target.tagName == "SPAN") {
						node = treeObj.getNodeByTId($($menu_e.target).parents(
								"li").attr("id"));
					}
					window[item.attr("name")](item, node, treeObj);
					return true;
				}
			});
	var form = $("#submit_form");
	form.validate({
		rules : {
			// account
			name : {
				required : true
			},
			code : {
				required : true
			}
		},
		messages : { // custom messages for radio buttons and checkboxes
		}
	});
});
function init(){
	var setting = {
			data : {
				key : {
					name : "nodeName"
				},
				simpleData : {
					enable : true,
					idKey : "nodeCode",
					pIdKey : "parentCode",
					rootPId : null
				}
			}
		};
		$.ajax({
			url : serverPath + "/node/treeListALL.htm",
			type : "post",
			dataType : "json",
			data : newNode,
			success : function(res) {
				console.log(res);
				treeObj = $.fn.zTree.init($("#treePullDown"), setting, res);
			},
			error : function(e) {
				BootstrapDialog.alert("服务异常");
				}
		})
}
function filter(treeId, parentNode, childNodes) {
	if (null != childNodes) {
		for ( var i in childNodes) {
			if (childNodes[i].parentCode == "") {
				childNodes[i].parentCode = null;
			}
		}
	}
	return childNodes;
}
/**
 * 添加节点
 * 
 * @param item
 * @param node
 * @param tree
 */
function newNode(item, node, tree) {
	$("#new_info").modal("show");
	$("#new_info input").val("");
	var data = {
		item : item,
		node : node,
		tree : tree
	}
	$("#new_info").data("info", data);
}
function saveNode() {
	var form = $("#submit_form");
	if (!form.valid()) {
		return;
	}
	$("#new_info").modal("hide");
	var data = $("#new_info").data("info");
	var newNode = {
		parentCode : data.node.nodeCode,
		nodeName : $("#new_info input[name='name']").val(),
		nodeCode : $("#new_info input[name='code']").val()
	}
	$.ajax({
		url : serverPath + "/node/saveNode.htm",
		type : "post",
		dataType : "json",
		data : newNode,
		success : function(res) {
			if (res.code == 0) {
				BootstrapDialog.alert("操作成功");
				init();
			} else {
				BootstrapDialog.alert(res.message, function() {
					$("#new_info").modal("show");
				});
			}
		},
		error : function(e) {
			BootstrapDialog.alert("服务异常");
		}
	})
}
function deleteNode(item, node, tree) {
	if (node.isParent) {
		BootstrapDialog.confirm('请先删除子级！');
		return;
	}
	var param = {
		parentCode : node.parentCode,
		id : node.id
	}
	BootstrapDialog.confirm('确定删除吗？', function(isOk) {
		if (isOk) {
			$.ajax({
				url : serverPath + "/node/deleteInfo.htm",
				type : "post",
				dataType : "json",
				data : param,
				success : function(res) {
					if (res.code == 0) {
						BootstrapDialog.alert("操作成功");
						init();
					} else {
						BootstrapDialog.alert(res.message);
					}
				},
				error : function(e) {
					BootstrapDialog.alert("服务异常");
				}
			})
		}
	});
}