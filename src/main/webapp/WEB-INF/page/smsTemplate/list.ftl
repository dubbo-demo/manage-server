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
    </div>
    <div>
        <form id="searchForm" class="form-horizontal" action="${serverPath}/sms-template/list.htm" method="post">
        <@p.pageForm value="page" type="sort"/>
        <div class="row-fluid">
            <div class="control-group span4 ">
                <label class="help-inline text-right span4">产品名称：</label>
            </div>
        </div>
        <p>
            <a href="###" class="btn blue">新增</a>
            <a href="###" target="_blank" class="btn blue">产品附件管理</a>
            <button class="btn blue">查询</button>
        </p>
        </form>
    </div>
    <!-- table -->
    <div class="tabbable tabbable-custom tabbable-custom-profile">
        <table class="table table-bordered table-hover table-condensed">
            <thead>
            <tr>
                <th>序号</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <#list page.result as record>
            <tr>
                <td>${record_index+1}</td>
                <td>
                <a href="javascript:void(0)">修改</a>
                &nbsp;
                &nbsp;
                <a href="javascript:void(0)">删除</a>
                </td>
            <tr>
            </#list>
            </tbody>
        </table>
        <@p.pagination value=page />
    </div>

</div>

<#include "/sys/bottom.ftl" />
