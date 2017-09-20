<#include "/sys/top.ftl">
<#include "/sys/left.ftl">
<script src="${cdnPath}/js/smsTemplate/list.js"></script>

<div class="page-content">
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span12">
                <!--页面标题-->
                <h3 class="page-title"></h3>
                <!--面包屑导航-->
                <ul class="breadcrumb">
                    <li><i class="icon-home"></i>
                        <a href="${serverPath}/index.htm">首页</a>
                        <i class="icon-angle-right"></i>
                    </li>
                    <li>
                        <a href="#">系统管理</a>
                        <i class="icon-angle-right"></i>
                    </li>
                    <li>
                        <a href="#">短信模板管理</a>
                    </li>
                </ul>
            </div>
        </div>
        <div>
            <div class="portlet-body form">
                <form id="searchForm" class="form-horizontal" action="${serverPath}/sms-template/list.htm"
                      method="post">
                <@p.pageForm value=page type="sort"/>
                    <div class="row-fluid">
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">模板编号：</label>
                            <input class="m-wrap span8" name="tplCode" value="${query.tplCode!}">
                        </div>
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">模板名称：</label>
                            <input class="m-wrap span8" name="tplName" value="${query.tplName!}">
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="control-group span4 ">
                            <button class="btn blue" type="submit">查询</button>
                        </div>
                    </div>
                </form>
            </div>
            <!-- table -->
            <div class="tabbable tabbable-custom tabbable-custom-profile">
                <table class="table table-bordered table-hover table-condensed">
                    <thead>
                    <tr>
                        <th style="width: 30px;text-align: center">序号</th>
                        <th style="width: 100px;text-align: center">模板编码</th>
                        <th style="width: 100px;text-align: center">模板名称</th>
                        <th style="text-align: center">模板内容</th>
                        <th style="width: 50px;text-align: center">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list page.result as record>
                    <tr>
                        <td>${record_index+1}</td>
                        <td>${record.tplCode!}</td>
                        <td>${record.tplName!}</td>
                        <td>${record.tplContent!}</td>
                        <td>
                            <a href="javascript:showEdit('${record.tplId!}')">修改</a>
                        </td>
                    <tr>
                    </#list>
                    </tbody>
                </table>
            <@p.pagination value=page />
            </div>
        </div>
    </div>
</div>

<#include "/sys/bottom.ftl" />
<div id="dialogEdit" class="modal fade" style="width:600px;display: none" tabindex="-1" role="dialog"
     aria-labelledby="dialogEdit" aria-hidden="true">
    <div class="modal-content">
        <div class="modal-header">
            <h4 class="modal-title">修改</h4>
        </div>
        <form id="editForm">
            <input type="hidden" name="tplId">
            <div class="modal-body">
                <div class="row-fluid">
                    <div class="control-group span8 ">
                        <label class="help-inline text-right span4">模板编号：</label>
                        <input class="m-wrap span8" name="tplCode" disabled="disabled">
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="control-group span8 ">
                        <label class="help-inline text-right span4">模板名称：</label>
                        <input class="m-wrap span8" name="tplName">
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="control-group span8 ">
                        <label class="help-inline text-right span4">模板内容：</label>
                        <textarea name="tplContent" class="span8" maxlength="200" style="height: 100px;">

                    </textarea>
                    </div>
                </div>
            </div>
        </form>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" onclick="$('#dialogEdit').modal('hide');">关闭</button>
            <button type="button" class="btn btn-primary" onclick="save();">提交</button>
        </div>
    </div><!-- /.modal-content -->
</div>
