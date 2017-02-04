<#--
分页
-->
<#macro pagination value>
    <#if !value?is_sequence>
        <#if value.pageCount gt 0>
        <script src="${cdnPath}/js/pagination.js?${VERSION_NO}"></script>
        <#--<div class="col-sm-6">
            <div class="dataTables_info">总共${value.total}条数据，当前显示${value.pageIndex}/${value.pageCount}页</div>
        </div>-->

        <script language="JavaScript">
            var pg = new showPages('pg',${value.pageIndex} ,${value.pageCount},${value.total},${value.pageSize});
            pg.printHtml();
        </script>
        <#else>
        <h3 class="text-center  ">
            没有数据</h3>
        </#if>

    </#if>
</#macro>