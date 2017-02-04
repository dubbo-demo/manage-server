<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<#assign ctx = request.contextPath>
    <meta charset="utf-8"/>
    <title>麦芽普惠信贷信息管理平台|中地集团</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <meta content="" name="description"/>
    <meta content="" name="author"/>
    <script>
        var serverPath = "${serverPath}";
        var cdnPath = "${cdnPath}";
    </script>
    <!-- BEGIN GLOBAL MANDATORY STYLES -->
    <link href="${cdnPath}/common/bootstrap/media/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="${cdnPath}/common/bootstrap/media/css/bootstrap-responsive.min.css" rel="stylesheet"/>
    <link href="${cdnPath}/common/bootstrap/media/css/font-awesome.min.css" rel="stylesheet"/>
    <link href="${cdnPath}/common/bootstrap/media/css/style-metro.css" rel="stylesheet"/>
    <link href="${cdnPath}/common/bootstrap/media/css/style.css" rel="stylesheet"/>
    <link href="${cdnPath}/common/bootstrap/media/css/style-responsive.css" rel="stylesheet"/>


    <link href="${cdnPath}/common/bootstrap/media/css/default.css" rel="stylesheet" id="style_color"/>
    <link href="${cdnPath}/common/bootstrap/media/css/uniform.default.css" rel="stylesheet"/>
    <link href="${cdnPath}/common/bootstrap/media/css/datepicker.css" rel="stylesheet"/>
    <link href="${cdnPath}/common/bootstrap/media/css/bootstrap-dialog.css" rel="stylesheet"/>

    <!-- END GLOBAL MANDATORY STYLES -->
    <!-- BEGIN PAGE LEVEL STYLES -->
    <link href="${cdnPath}/common/bootstrap/media/css/jquery.gritter.css" rel="stylesheet"/>
    <link href="${cdnPath}/common/bootstrap/media/css/daterangepicker.css" rel="stylesheet"/>
    <link href="${cdnPath}/common/bootstrap/media/css/fullcalendar.css" rel="stylesheet"/>
    <link href="${cdnPath}/common/bootstrap/media/css/jqvmap.css" rel="stylesheet" media="screen"/>
    <link href="${cdnPath}/common/bootstrap/media/css/jquery.easy-pie-chart.css" rel="stylesheet"
          media="screen"/>
    <link rel="stylesheet" href="${cdnPath}/common/bootstrap/media/css/select2_metro.css"/>
    <link rel="stylesheet" href="${cdnPath}/common/bootstrap/media/css/chosen.css"/>
    <link rel="stylesheet" type="text/css" href="${cdnPath}/common/bootstrap/media/css/glyphicons.css" />
    
    <!-- END PAGE LEVEL STYLES -->

    <link href="${cdnPath }/common/bootstrap/media/css/DT_bootstrap.css" rel="stylesheet"
          media="screen"/>
    <link rel="shortcut icon" href="${cdnPath}/common/bootstrap/media/image/favicon.ico"/>

    <link href="${cdnPath }/common/css/validate/validate.css" rel="stylesheet"/>
    <link href="${cdnPath }/common/zTree/zTreeStyle.css" rel="stylesheet"/>

    <link rel="stylesheet" type="text/css" href="${cdnPath}/common/bootstrap/media/css/glyphicons.css" />

    <!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
    
    
    <!-- 新增省市区控件 begin -->
    <link href="${cdnPath}/css/district/jquery.city.css" rel="stylesheet" />
	<link href="${cdnPath}/css/district/animate.min.css" rel="stylesheet" /> <!--可选-->
    <!-- 新增省市区控件 end -->
    
    <link href="${cdnPath}/common/bootstrap/media/css/fileinput.css" rel="stylesheet" />
    
    <!-- BEGIN CORE PLUGINS -->
    <script src="${cdnPath}/common/bootstrap/media/js/jquery-1.10.1.min.js"></script>
    <script src="${cdnPath}/common/zTree/jquery.ztree.core.js"></script>
    <script src="${cdnPath}/js/utils/organizationzTree.js"></script>
    <script src="${cdnPath}/common/calendar/WdatePicker.js"></script>
    <script src="${cdnPath}/common/bootstrap/media/js/jquery-migrate-1.2.1.min.js"></script>
    <!-- IMPORTANT! Load jquery-ui-1.10.1.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
    <script src="${cdnPath}/common/bootstrap/media/js/jquery-ui-1.10.1.custom.min.js"></script>
    <script src="${cdnPath}/common/bootstrap/media/js/bootstrap.min.js"></script>
    <!--[if lt IE 9]>
    <script src="${cdnPath}/common/bootstrap/media/js/excanvas.min.js"></script>
    <script src="${cdnPath}/common/bootstrap/media/js/respond.min.js"></script>
    <![endif]-->
   
    <script src="${cdnPath}/common/bootstrap/media/js/jquery.slimscroll.min.js"></script>
    <script src="${cdnPath}/common/bootstrap/media/js/jquery.blockui.min.js"></script>
    <script src="${cdnPath}/common/bootstrap/media/js/jquery.cookie.min.js"></script>
     <script src="${cdnPath}/common/bootstrap/media/js/jquery.dataTables.js"></script>
    <script src="${cdnPath}/common/bootstrap/media/js/jquery.uniform.min.js"></script>
    <script src="${cdnPath}/common/bootstrap/media/js/bootstrap-datepicker.js"></script>
    <script src="${cdnPath}/common/bootstrap/media/js/bootstrap-dialog.js"></script>
    <script src="${cdnPath}/common/bootstrap/media/js/DT_bootstrap.js"></script>


    <script src="${cdnPath}/common/bootstrap/media/js/app.js"></script>
    <script src="${cdnPath}/common/bootstrap/media/js/search.js"></script>

    <!-- begin form validate-->
    <script src="${cdnPath}/common/js/common.js"></script>
    <script src="${cdnPath}/common/bootstrap/media/js/jquery.validate.min.js"></script>
    <script src="${cdnPath}/common/js/validate/additional-methods.js"></script>
    <script src="${cdnPath}/common/js/validate/messages_cn.js"></script>
    <!-- end form validate-->


    <script src="${cdnPath}/common/bootstrap/media/js/jquery.bootstrap.wizard.min.js"></script>

    <script src="${cdnPath}/common/bootstrap/media/js/chosen.jquery.min.js"></script>

    <script src="${cdnPath}/common/bootstrap/media/js/select2.min.js"></script>

    <script src="${cdnPath }/common/zTree/jquery.ztree.core.min.js"></script>
    <script src="${cdnPath }/common/zTree/jquery.ztree.excheck.min.js"></script>
    
    <!-- 新增省市区控件 begin -->
    <script src="${cdnPath }/js/jquery.city.js?v=${VERSION_NO}"></script>
    
    <script src="${cdnPath }/js/chinaCitys/city-picker.data.js"></script>
	<script src="${cdnPath }/js/chinaCitys/city-picker.js"></script>
	<script src="${cdnPath }/js/chinaCitys/main.js"></script>
	<link href="${cdnPath }/css/chinaCitys/city-picker.css" rel="stylesheet">
    <!-- 新增省市区控件 end -->
    
    <script src="${cdnPath}/common/bootstrap/media/js/fileinput.js"></script>
    <script src="${cdnPath}/common/bootstrap/media/js/fileinput_locale_zh.js"></script>
    
    <script>
        jQuery(document).ready(function () {
            App.init(); // initlayout and core plugins
            Search.init();
            var stypeColor = $.cookie("style_color");
            if (stypeColor) {
                $('#style_color').attr("href", cdnPath + "/common/bootstrap/media/css/" + stypeColor + ".css");
            }
            
        });
        (function ($) {
            //备份jquery的ajax方法
            var _ajax = $.ajax;

            //重写jquery的ajax方法
            $.ajax = function (opt) {
                //备份opt中error和success方法
                var fn = {
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    },
                    success: function (data, textStatus) {
                    }
                }
                if (opt.error) {
                    fn.error = opt.error;
                }
                if (opt.success) {
                    fn.success = opt.success;
                }

                //扩展增强处理
                var _opt = $.extend(opt, {
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        //错误方法增强处理
                        if (String(XMLHttpRequest.responseText).indexOf("login") != -1) {
                            window.location.href = "${serverPath}/login.htm";
                            return false;
                        }
                        fn.error(XMLHttpRequest, textStatus, errorThrown);
                    },
                    success: function (data, textStatus) {
                        if (-1000 == data.code) {
                            window.location.href = "${serverPath}/login.htm";
                            return false;
                        }
                        fn.success(data, textStatus);
                    }
                });
                return _ajax(_opt);
            };
        })(jQuery);
    </script>
    <style>
        ul.breadcrumb a:hover {
            text-decoration: none !important;
            cursor: text;
        }

        ul li:first-child a:hover {
            cursor: pointer !important
        }
    </style>
    <!-- END CORE PLUGINS -->
	
</head>
<body class="page-header-fixed page-footer-fixed">
<!-- 头部开始 -->
<div class="header navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a class="brand" href=""><img src="${cdnPath}/common/bootstrap/media/image/maiyaphlogo.png" alt="logo"/></a>
            <!-- BEGIN RESPONSIVE MENU TOGGLER -->

			<a href="javascript:;" class="btn-navbar collapsed" data-toggle="collapse" data-target=".nav-collapse">

			<img src="${cdnPath}/common/bootstrap/media/image/menu-toggler.png" alt="" />

			</a>          

			<!-- END RESPONSIVE MENU TOGGLER -->  
            <ul class="nav pull-right">
                <li class="dropdown user">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <img alt="" src="${cdnPath}/common/bootstrap/media/image/avatar1_small.jpg"/>
                        <span class="username">
	                        <#if Session.position?exists>
	 							${(Session.position.positionName)!}
							</#if>
						</span>
						<span class="username">
	                        <#if Session.currentUser?exists>
	 							${(Session.currentUser.employeeName)!}   
							</#if>
						</span>
                        <i class="icon-angle-down"></i>
                    </a>
                    <ul class="dropdown-menu">
                        <!--<li><a href="extra_profile.html"><i class="icon-user"></i> My Profile</a></li>
                        <li><a href="page_calendar.html"><i class="icon-calendar"></i> My Calendar</a></li>
                        <li><a href="inbox.html"><i class="icon-envelope"></i> My Inbox(3)</a></li>
                        <li><a href="#"><i class="icon-tasks"></i> My Tasks</a></li>
                        <li class="divider"></li>
                        <li><a href="extra_lock.html"><i class="icon-lock"></i> Lock Screen</a></li>-->
                        <li><a href="${serverPath}/exitLogin.htm"><i class="icon-key"></i>退出登录</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>
<!--头部结束-->
<!-- 主体开始 -->
<div class="page-container">
<#include "/sys/skin.ftl">