<#include "/sys/top.ftl">
<#include "/sys/left.ftl">

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
                        <a href="#">减免记录</a>
                    </li>
                </ul>
            </div>
        </div>
        <div>
            <div class="portlet-body form">
                <form id="searchForm" class="form-horizontal" action="${serverPath}/reductionRecord/queryPageList.htm"
                      method="post">
                    <input value="${page.total}" type="hidden" name="total">
                <@p.pageForm value=page type="sort"/>
                    <div class="row-fluid">
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">合同编号：</label>
                            <input class="m-wrap span8" name="contractNo" value="${query.contractNo!}">
                        </div>
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">客户身份证：</label>
                            <input class="m-wrap span8" name="idCard" value="${query.idCard!}">
                        </div>
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">类型：</label>
                            <select name="isAdvanceSettle"  class="m-wrap span8">
                                <option value="0" <#if (query.isAdvanceSettle??&&query.isAdvanceSettle== 0)??> selected = selected</#if>>当期减免</option>
                                <option value="1" <#if (query.isAdvanceSettle??&&query.isAdvanceSettle== 1)> selected = selected</#if>>提前结清减免</option>
                            </select>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">大区：</label>
                            <div class="">
                                <select name="areaId" class="m-wrap span8" data-id="${(query.areaId)!0}" onchange="initStoreData()">
                                    <option value="0">请选择</option>
                                </select>
                            </div>
                        </div>
                        <div class="control-group span4 " class="m-wrap span8">
                            <label class="help-inline text-right span4">门店：</label>
                            <div class="">
                                <select name="orgId" class="m-wrap span8" data-id="${(query.orgId)!0}">
                                    <option value="0">请选择</option>
                                </select>
                            </div>
                        </div>
                        <div class="control-group span4 ">
                            <label class="help-inline text-right span4">减免日期：</label>
                            <div class="input-append date date-picker" data-date="${query.reductionDateStart!}"
                                 data-date-format="yyyy-mm-dd" data-date-viewmode="years">
                                <input name="reductionDateStart" id="reductionDateStart" class="m-wrap span10 date-picker"
                                       size="16" type="text" data-date-format="yyyy-mm-dd"
                                       value="${query.reductionDateStart!}"/><span class="add-on"><i
                                    class="icon-calendar"></i></span>
                            </div>
                            <span style="margin-left:3px">--&nbsp;</span>
                            <div class="input-append date date-picker" data-date="${query.reductionDateEnd!}"
                                 data-date-format="yyyy-mm-dd" data-date-viewmode="years">
                                <input name="reductionDateEnd" id="reductionDateEnd" class="m-wrap span10 date-picker"
                                       size="16" type="text" data-date-format="yyyy-mm-dd"
                                       value="${query.reductionDateEnd!}"/><span class="add-on"><i
                                    class="icon-calendar"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="control-group span4 ">
                            <button class="btn blue" type="submit">查询</button>
                            <button class="btn blue" type="button" onclick="download()">下载</button>
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
                        <th >合同编号</th>
                        <th >期数</th>
                        <th >账单编号</th>
                        <th >期初本金</th>
                        <th >月还本金</th>
                        <th >月还利息</th>
                        <th >月还款额</th>
                        <th >期末本金余额</th>
                        <th >提前结清减免</th>
                        <th >提前结清金额</th>
                        <th >类型</th>
                        <th >减免金额</th>
                        <th >操作人</th>
                        <th >操作时间</th>
                        <th >附件</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list page.result as record>
                    <tr>
                        <td>${record_index+1}</td>
                        <td>${(record.contractNo)!}</td>
                        <td>${(record.repayPeriod)!}/${(record.periods)!}</td>
                        <td>${(record.billNo)!}</td>
                        <td>${(record.initialPrincipal)!}</td>
                        <td>${(record.principal)!}</td>
                        <td>${(record.interest)!}</td>
                        <td>${(record.reapyAmount)!}</td>
                        <td>${(record.endPrincipal)!}</td>
                        <td>${(record.returnAmount)!}</td>
                        <td>${(record.aheadAmount)!}</td>
                        <td><#if record.isAdvanceSettle == 0>
                            当期减免
                            <#else>
                            提前结清减免
                        </#if></td>
                        <td>${(record.initialAmount)!}</td>
                        <td>${(record.createUser)!}</td>
                        <td>${record.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                        <td>
                            <a href="${ctx}/productFile/fileUploadForRepay.htm?billNo=${record.billNo}" target="_blank">查看</a>
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

<script>
    $(function () {
        initAreaData();
    });
    function download(){
        var param = $("#searchForm").serialize();
        window.location.href = serverPath + "/reductionRecord/download.htm?"+param;
    }
    function initAreaData() {
        $.ajax({
            url : serverPath + "/organization/getRegionInfo.htm",
            type : "post",
            data : {
                "Time" : new Date().getMilliseconds()
            },
            dataType : "json",
            success : function(result) {
                if (result.code == 0) {
                    initStoreData();
                    // 清空除第一条内容的外的其它数据
                    var select_ = $("select[name='areaId']");
                    select_.find("option:gt(0)").remove();
                    for (var i = 0; i < result.data.length; i++) {
                        var isSelected = result.data[i].id == select_
                                .attr('data-id') ? "selected='selected'" : "";
                        select_.append("<option " + isSelected + " value='"
                                + result.data[i].id + "'>" + result.data[i].orgName
                                + "</option>");
                    }
                } else {
                    BootstrapDialog.alert(result.message);
                }
            },
            error : function() {
                BootstrapDialog.alert("操作失败");
            }
        });
    }
    function initStoreData() {
        var parentId = $("select[name='areaId']").val();
        if (parentId == "0") {
            parentId = $("select[name='areaId']").attr('data-id');
        }
        if (parentId == "0") {
            return;
        }
        $.ajax({
            url : serverPath + "/organization/getStoreInfo.htm",
            type : "post",
            data : {
                "id" : parentId,
                "Time" : new Date().getMilliseconds()
            },
            dataType : "json",
            success : function(result) {
                if (result.code == 0) {
                    // 清空除第一条内容的外的其它数据
                    var select_ = $("select[name='orgId']");
                    select_.find("option:gt(0)").remove();
                    for (var i = 0; i < result.data.length; i++) {
                        var isSelected = result.data[i].id == select_
                                .attr('data-id') ? "selected='selected'" : "";
                        select_.append("<option " + isSelected + " value='"
                                + result.data[i].id + "'>" + result.data[i].orgName
                                + "</option>");
                    }
                } else {
                    BootstrapDialog.alert(result.message);
                }
            },
            error : function() {
                BootstrapDialog.alert("操作失败");
            }
        });
    }
</script>
