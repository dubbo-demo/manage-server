<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script type="text/javascript">
    var serverPath = "${serverPath}";
</script>
<style>
    #architect {
        border: 1px solid #41C7DB;
        margin: 0px 10px 10px 0px;
        width: 190px;
        float: left;
        height: 700px;
        overflow-y: scroll;
        overflow-x: scroll;
    }
    
    .required{color:#e02222;}

</style>


<div class="page-content" >
<!-- header -->
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span12">
                <!--页面标题-->
                <h3 class="page-title"></h3>
                <!--面包屑导航-->
                <ul class="breadcrumb">
                    <li> <i class="icon-home"></i>
                        <a href="${serverPath}/index.htm">首页</a> <i class="icon-angle-right"></i>
                    </li>
                    <li>
                        <a href="#">系统管理</a><i class="icon-angle-right"></i>
                    </li>
                    <li>
                        <a href="#"><#if dto??>地市编码表<#else>地市编码表</#if></a>
                    </li>
                </ul>
            </div>
        </div>
    <div>
    
	<div class="portlet-body">
	<table style="width: 100%">
        <tr style="vertical-align: top;">
            <td style="width: 110px">
	    <div style="float: left;">
    	    
    		<div class="controls" style='border: 1px solid #41C7DB;margin: 0px 10px 10px 0px;padding:0px;'>
    		    <div style='background: rgba(0, 0, 0, 0)
                    -moz-linear-gradient(center bottom, #e6e6e6 0%, #ffffff 100%) repeat
                    scroll 0 0;'>
    		        <p style="font-size: 15px;padding: 5px 5px 15px 0px;">地市列表</p>
    		    </div>
                <select style="border:none; " size=40 id='citySelect'>
                    <#list cityCodeResult as cityCode>
                        <option data-code=${(cityCode.cityCode)!} value=${(cityCode.id)!}>${(cityCode.cityName)!}</option>
                    </#list>
                </select>
            </div>
        </div>
        </td>
        <td style="vertical-align: top;">
        <div style="margin-bottom:10px;" class="breadcrumb" id='buttonInput'>
            <table>
                <tr>
                    <td style="width: 30%;"><span
                        style="vertical-align: 15%;"></span></td>
                    <td style="width: 30%;"><input type="button" style="margin-right:20px;" id='add' name='add' class="btn blue" value="新增" onclick="return add()"></input></td>
                    <td style="width: 30%;"><input type="button" style="margin-right:20px;" id='change' name='change' class="btn blue" value="修改" onclick="return change()"></input></td>
                    <td style="width: 30%;"><input type="button" id='del' name='del' class="btn blue" value="删除" onclick="return del()"></input></td>
                </tr>
            </table>
        </div>
        <!--insertOrUpdate 0新增 1更新 -->
        <input type="hidden" id="insertOrUpdate" name="insertOrUpdate"></input>
        <input type="hidden" id="id" name="id"></input>
        <div id='main' style="display: none;">
            <div class="breadcrumb"><span id='hName'>新增城市</span>
            </div>
            <div id="description">
                <div>
                    <span class="control-label span1">省份名称<span class="required">*</span></span>
                    <select onchange="getCity()" class='span2' id="province">
                        <option value="0">请选择省份</option>
                        <#list sysDistrictResult as sysDistrict>
                            <option value=${(sysDistrict.id)!}>${(sysDistrict.name)!}</option>
                        </#list>
                    </select>
                </div>
                <div>
                    <span class="control-label span1">城市名称<span class="required">*</span></span>
                    <select class='span2' id="city">
                        <option value="0">请选择市</option>
                    </select>
                </div>
            </div>
            <div id="cityNameDiv">
                <span class="control-label span1">城市名称<span class="required">*</span></span>
                <input type="text" name="cityName" id="cityName" class="span2" maxlength="20" readonly />           
            </div>
            <div>
                <span class="control-label span1">地市代码<span class="required">*</span></span>
                <input type="text" name="cityCode" id="cityCode" class="span2" maxlength="20" onblur="checkInput('^[\a-\z0-9\A-\Z]*$',this)" />           
            </div>
            <div>
                <input type="button" id='returnCity' name='returnCity' class="btn blue" value="返回" style="margin-left:155px" onclick="return returnCity()"></input>
                <input type="button" id='save' name='save' class="btn blue" value="提交" onclick="return save()"></input> 
            </div>
        </div>
        </td>
        </tr>
    </table>
	</div>	
	
</div>
<script src="${cdnPath}/js/cityCode/cityCode.js?v=${VERSION_NO}"></script>

<#include "/sys/bottom.ftl">