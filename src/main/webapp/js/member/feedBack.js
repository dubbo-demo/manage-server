function search() {
	$("#searchForm").submit();
}

function downLoad() {
	$("#downLoadstartDate").val($("#searchstartDate").val());
	$("#downLoadendDate").val($("#searchendDate").val());
	$("#downLoadForm").submit();
}