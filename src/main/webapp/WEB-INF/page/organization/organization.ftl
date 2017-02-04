<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<link rel="stylesheet" href="${cdnPath}/common/zTree/zTreeStyle.css?v=${VERSION_NO}" type="text/css">
<link rel="stylesheet" type="text/css" href="${cdnPath}/css/organization/organization.css?v=${VERSION_NO}"></link>
<script type="text/javascript">
    var serverPath = "${serverPath}";
</script>
<div class='page-container row-fluid'>
<div class="page-content">

<style>
    .required{color:#e02222;}
</style>

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
                        <a href="#"><#if dto??>组织管理<#else>组织管理</#if></a>
                    </li>
                </ul>
            </div>
        </div>
    <div>

    <table style="width: 100%">
        <tr style="vertical-align: top;">
            <!-- 显示左侧树 -->
            <td style="width: 130px">
                <div id="architect">
                    <div id="treeName">
                        <p style="font-size: 15px; padding: 5px 5px 10px 0px;">组织架构</p>
                    </div>
                    <div>
                        <ul id="tree" class="ztree" style="margin-top: 0; width: 132px;"></ul>
                    </div>
                </div>
            </td>
            <td style="vertical-align: top;">
                <div id="topDiv" class="breadcrumb">
                    <table>
                        <tr>
                            <td style="width: 23%;"><span id="topName"
                                style="vertical-align: 15%;"></span></td>
                            <td style="width: 23%;"><input type="button" id="btnNew"
                                class="btn blue btnTop" value="新建"
                                onclick="return build();"></input></td>
                            <td style="width: 23%;"><input type="button" id="btnDel"
                                class="btn blue btnTop" value="删除"
                                onclick="return delOrg();"></input></td>
                            <td style="width: 23%;"><input type="button" id="btnUpdate"
                                class="btn blue btnTop" value="修改"
                                onclick="return update();"></input></td>
                            <td style="width: 23%;"><input type="button"
                                id="btnpostManage" class="btn blue btnTop"
                                value="岗位绑定" href="#" onclick="return addPost();"></input></td>
                        </tr>
                    </table>
                </div> 
                 
                <!--岗位管理  -->
                <div id="postManage" >
                    <table class="table table-striped table-bordered table-condensed" id="selectPost"
                        style="margin-top: 5px;">
                        <thead>
                            <tr>
                                <th style="width: 20%;">岗位名称</th>
                                <th style="width: 20%;">岗位编号</th>
                                <th style="width: 20%;">组织</th>
                                <th style="width: 20%;">操作</th>
                                <th style="width: 20%;">操作</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div> 
                <!-- 新增岗位/修改岗位 -->
                <iframe name="frm_dealer" 
                style="display: none"></iframe>
                <form id="changePost" name="changePost" method="POST" 
                    enctype="multipart/form-data" target="frm_dealer">
                <input type="hidden" id="orgId" name="orgId"></input>
                <input type="hidden" id="id" name="id"></input>
                <input type="hidden" id="initialPositionCode" name="initialPositionCode"></input>
                <input type="hidden" id="position" name="position"></input>
                <!-- 新增或修改岗位，0新增，1修改 -->
                <input type="hidden" id="insertOrUpdatePost" name="insertOrUpdatePost"></input>
                <div id="addPost" class="modal hide fade" tabindex="-1" data-width="760">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                        <h3>岗位绑定</h3>
                    </div>
                    <div class="modal-body">
                        <div class="row-fluid">
                            <span class="control-label span2">岗位名称<span class="required">*</span></span>
                            <select name="positionName" id="positionName" onchange="changePostInfo(); return false;">
                            </select>
                        </div> 
                        <div class="row-fluid">
                            <span class="control-label span2">岗位编号<span class="required">*</span></span>
                            <input type="text" name="positionCode" id="positionCode" maxlength="30" readonly/>
                        </div>   
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn blue" onclick="backPost();">返回</button>
                        <button type="button" class="btn blue" onclick="savePost();">提交</button>
                    </div>
                </div> 
                </form> 

                <!-- 新建/修改组织 -->
                <iframe name="frm_dealer" 
                style="display: none"></iframe>
                <form id="changeOrganization" name="changeOrganization" method="POST" class="form-horizontal"
                     target="frm_dealer">
                    <!-- 用于辨别新增还是修改：0新增  1新增 --> 
                    <input type="hidden" id="insertOrUpdate" name="insertOrUpdate"></input>
                    <input type="hidden" id="parentId" name="parentId"></input>
                    <input type="hidden" id="cityId" name="cityId"></input> 
                    <input type="hidden" id="cityCode" name="cityCode"></input>    
                    <input type="hidden" id="OrganizationId" name="id"></input>    
                    <div id="newType" style="display: none;">
                        <div class="row-fluid">
                            <div class="control-group span6 ">
                                <label class="control-label">新建类型<span class="required">*</span></label>
                                <div class="controls">
                                    <select class="m-wrap span6" id="orgType" name="orgType">
                                        <option value="1">大区</option>
                                        <option value="2" selected="selected" id="orgTypeSelected"  >总部</option>                                                                               
                                        <option value="3">门店</option>   
                                        <option value="4">部门</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="row-fluid cityCodeTr" style="display:none;">
                            <div class="control-group span6 ">
                                <label class="control-label">地市名称<span class="required">*</span></label>
                                <div class="controls">
                                    <select class="m-wrap span6" name="city" id="city" onchange="showStoreCode(); return false;">
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="row-fluid cityCodeTr" style="display:none;">
                            <div class="control-group span6 ">
                                <label class="control-label">门店地址<span class="required">*</span></label>
                                <div class="controls">
                                    <input type="text" name="address" id="address"
                                    class="m-wrap span6" maxlength="100" />
                                </div>
                            </div>
                        </div>
                        <div class="row-fluid cityCodeTr" style="display:none;">
                            <div class="control-group span6 ">
                                <label class="control-label">联系电话<span class="required">*</span></label>
                                <div class="controls">
                                    <input type="text" name="telephone" id="telephone"
                                    class="m-wrap span6" maxlength="20" onblur="checkInput('^[\-0-9]*$',this)" />
                                </div>
                            </div>
                        </div>
                        <div class="row-fluid" >
                            <div class="control-group span6 ">
                                <label class="control-label">编号<span class="required">*</span></label>
                                <div class="controls">
                                    <input type="text" name="orgCode" id="orgCode"
                                    class="m-wrap span6" maxlength="20"
                                    onblur="checkInput('^[\a-\z0-9\A-\Z]*$',this)" />
                                </div>
                            </div>
                        </div>
                        <div class="row-fluid" >
                            <div class="control-group span6 ">
                                <label class="control-label">名称<span class="required">*</span></label>
                                <div class="controls">
                                    <input type="text" name="orgName" id="orgName"
                                    class="m-wrap span6" maxlength="50"
                                    onblur="checkInput('^[\a-\z\A-\Z0-9\u4E00-\u9FA5]*$',this)" />
                                </div>
                            </div>
                        </div>
                        <div class="row-fluid" >
                            <div class="control-group span6 ">
                                <label class="control-label">上级部门<span class="required">*</span></label>
                                <div class="controls">
                                    <input id="citySel" class="m-wrap span6" type="text" readonly value="" onclick="showMenu(); return false;"/>
                                </div>
                            </div>
                        </div>   
                        <div><span id="error" style="display: block;"></span></div>
                        <div>
                        <input type="button" id="btnBack"
                                class="btn blue" style="margin-left:315px" value="返回" onclick="return back();"></input> 
                        <input type="button" id="btnSave"
                                class="btn blue" value="提交" onclick="return save();"></input>
                        </div>                  
                        <div id="menuContent" style="margin-left:5px;" class="menuContent span4">
                            <ul id="treePullDown" class="ztree" style="margin-top:0; width:130px;"></ul>
                        </div>                      
                    </div>                          
                </form>
            </td>
        </tr>
    </table>
</div>
</div>
<script src="${cdnPath}/common/js/common.js"></script>
<script src="${cdnPath}/common/bootstrap/media/js/index.js"></script>
<script type="text/javascript" src="${cdnPath}/common/zTree/jquery.ztree.core.js"></script>
<script src="${cdnPath}/js/organization/organization.js?v=${VERSION_NO}"></script>
<#include "/sys/bottom.ftl">