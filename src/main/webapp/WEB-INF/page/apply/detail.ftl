<#include "/apply/approve/top.ftl">
<script>
	$(function(){
		//选中一级目录
		$("li[name='tab']:eq(0)").addClass("active");
		//选中二级目录
		$("li[name='two_li']:eq(0)").addClass("active");
	});
	function goHref(e) {
		window.location.href=e+$("#applyLoanNo").val()+"&cType="+$("#cType").val();
	} 
</script>
<input type="hidden" id="cType" name="cType" value="${cType!}"/>
<#include "/apply/audit/common/detail_content.ftl">

		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">