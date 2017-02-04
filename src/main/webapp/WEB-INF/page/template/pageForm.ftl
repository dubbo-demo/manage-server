<#--
生成form组件内 分页一些属性
-->
<#macro pageForm value type="">
<input type="hidden" id="pageIndex" name="pageIndex" value="1"/>
<input type="hidden" id="pageSize" name="pageSize" value="${(value.pageSize)!10}"/>
<#if type != '' && type == "sort">
<input type="hidden" id="sortField" name="sortField" value="${(value.sortField)!}"/>
<input type="hidden" id="sortOrder" name="sortOrder" value="${(value.sortOrder)!}"/>
</#if>
</#macro>