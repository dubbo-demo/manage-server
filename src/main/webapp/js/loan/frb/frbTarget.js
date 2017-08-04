function down(){
	ChkUtil.form_trim($("#searchForm"));
	ChkUtil.saveCookie("queryParams", $("#searchForm").serialize(),"","/");
	var url = serverPath + "/loan/frb/exportInfo.htm";
	$("#searchForm").attr("action", url);
	$("#searchForm").submit();
	url = serverPath + "/loan/frb/queryPageList.htm";
	$("#searchForm").attr("action", url);
}
