// 分页
function showPages(name, pageIndex, pageCount, total, pageSize) { //初始化属性
    this.name = name;      //对象名称
    this.page = pageIndex;         //当前页数
    this.pageCount = pageCount;    //总页数
    this.total = total;
    this.pageSize = pageSize;
}

showPages.prototype.checkPages = function () { //进行当前页数和总页数的验证
    if (isNaN(parseInt(this.page))) this.page = 1;
    if (isNaN(parseInt(this.pageSize))) this.pageSize = 10;
    if (isNaN(parseInt(this.pageCount))) this.pageCount = 1;
    if (this.page < 1) this.page = 1;
    if (this.pageCount < 1) this.pageCount = 1;
    if (this.page > this.pageCount) this.page = this.pageCount;
    this.page = parseInt(this.page);
    this.pageCount = parseInt(this.pageCount);
}

showPages.prototype.createHtml = function () { //生成html代码
    var strHtml = '', prevPage = this.page - 1, nextPage = this.page + 1;
    var isFirstPage = false;
    var isLastPage = false;
    var hasPreviousPage = false;
    var hasNextPage = false;
    var navigatePages = 6; // 导航页码数
    var navigatePageNumbers = new Array(); // 所有导航页号

    isFirstPage = (this.page == 1);
    isLastPage = (this.page == this.pageCount && this.page != 1);
    hasPreviousPage = (this.page > 1);
    hasNextPage = (this.page < this.pageCount);

    // 当总页数小于或等于导航页码数时
    if (this.pageCount <= navigatePages) {
        navigatePageNumbers = new Array(this.pageCount);
        for (var i = 0; i < this.pageCount; i++) {
            navigatePageNumbers[i] = i + 1;
        }
    } else { // 当总页数大于导航页码数时
        navigatePageNumbers = new Array(navigatePages);
        var startNum = (this.page - navigatePages / 2);
        var endNum = (this.page + navigatePages / 2);

        if (startNum < 1) {
            startNum = 1;
            // (最前navigatePages页
            for (var i = 0; i < navigatePages; i++) {
                navigatePageNumbers[i] = startNum++;
            }
        } else if (endNum > this.pageCount) {
            endNum = this.pageCount;
            // 最后navigatePages页
            for (var i = navigatePages - 1; i >= 0; i--) {
                navigatePageNumbers[i] = endNum--;
            }
        } else {
            // 所有中间页
            for (var i = 0; i < navigatePages; i++) {
                navigatePageNumbers[i] = startNum++;
            }
        }
    }


    strHtml += '<ul>';

    if (hasPreviousPage) {
        strHtml += '<li ><a href="javascript:' + this.name + '.toPage(1);">首页</a></li>';
        strHtml += '<li ><a href="javascript:' + this.name + '.toPage(' + prevPage + ');">上一页</a></li>';
    } else {
        strHtml += '<li class="disabled" ><a href="javascript:void(0);">首页</a></li>';
        strHtml += '<li class="disabled" ><a href="javascript:void(0);">上一页</a></li>';
    }

    for (var i = 0; i < navigatePageNumbers.length; i++) {
        if (navigatePageNumbers[i] == this.page) {
            strHtml += '<li class="active"><a href="javascript:void(0);">' + navigatePageNumbers[i] + '</a></li>';
        } else {
            strHtml += '<li ><a href="javascript:' + this.name + '.toPage(' + navigatePageNumbers[i] + ');">' + navigatePageNumbers[i] + '</a></li>';
        }
    }
    if (hasNextPage) {
        strHtml += '<li><a href="javascript:' + this.name + '.toPage(' + nextPage + ');">下一页</a></li>';
        strHtml += '<li ><a href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">末页</a></li>';
    } else {
        strHtml += '<li class="disabled" ><a href="javascript:void(0);">下一页</a></li>';
        strHtml += '<li class="disabled" ><a href="javascript:void(0);">末页</a></li>';

    }

    /*if (prevPage < 1) {
     strHtml += '<li class="disabled" ><a href="javascript:void(0);">首页</a></li>';
     strHtml += '<li class="disabled" ><a href="javascript:void(0);">上一页</a></li>';
     } else {
     strHtml += '<li ><a href="javascript:' + this.name + '.toPage(1);">首页</a></li>';
     strHtml += '<li ><a href="javascript:' + this.name + '.toPage(' + prevPage + ');">上一页</a></li>';
     }
     strHtml += '<li class="active"><a href="javascript:void(0);">' + this.page + '</a></li>';
     if (nextPage > this.pageCount) {
     strHtml += '<li class="disabled" ><a href="javascript:void(0);">下一页</a></li>';
     strHtml += '<li class="disabled" ><a href="javascript:void(0);">末页</a></li>';
     } else {
     strHtml += '<li><a href="javascript:' + this.name + '.toPage(' + nextPage + ');">下一页</a></li>';
     strHtml += '<li ><a href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">末页</a></li>';
     }*/
    strHtml += '<li class="disabled" ><a href="javascript:void(0);">共' + this.pageCount + '页</a></li>';
    strHtml += '</ul>';
    return strHtml;
}

showPages.prototype.createPage = function () { //生成html代码
    var strHtml = '';
    strHtml += '<label>每页条数&nbsp;&nbsp;'
    strHtml += '<select size="1"  id="pg_size" onchange="changePageSize()" style="margin-bottom:0px">';
    strHtml += '<option value="10"';
    if (this.pageSize == 10) {
        strHtml += ' selected="selected"';
    }
    strHtml += '>10</option>';
    strHtml += '<option value="20"';
    if (this.pageSize == 20) {
        strHtml += ' selected="selected"';
    }
    strHtml += '>20</option>';
    strHtml += '<option value="50"';
    if (this.pageSize == 50) {
        strHtml += ' selected="selected"';
    }
    strHtml += '>50</option>';
    strHtml += '<option value="100"';
    if (this.pageSize == 100) {
        strHtml += ' selected="selected"';
    }
    strHtml += '>100</option>';

    strHtml += '</select></label>';
    return strHtml;
}
showPages.prototype.toPage = function (page) { //页面跳转
    goPage(page);
}
showPages.prototype.printHtml = function () { //显示html代码
    this.checkPages();
    var domStr = '<div class="row-fluid">'
        + '<div class="span3">'
        + '<div id="table_length" class="dataTables_length">'
        + this.createPage()
        + '</div>'
        + '</div>'
        + '<div class="span9">'
        + '<div class="dataTables_paginate paging_bootstrap pagination pagination-right">'
        + this.createHtml()
        + '</div>'
        + '</div>'
        + '</div>';
    document.write(domStr);
}



/*添加排序部分*/
var sortArray = {"thSort sorting_asc": "asc", "thSort sorting_desc": "desc", "thSort sorting": "asc"}

function chanageSort(oldSort) {
    if (oldSort == "thSort sorting_asc") {
        return "thSort sorting_desc";
    } else if (oldSort == "thSort sorting_desc") {
        return "thSort sorting_asc";
    } else {
        return "thSort sorting_asc";
    }

}
$(".thSort").on("click", function () {
    var $this = $(this);
    var newClass = chanageSort($this.attr('class'));
    $(this).attr('class', newClass);
    $("#sortOrder").val(sortArray[$this.attr('class')]);
    $("#sortField").val($this.data("field"));
    goPage(1);
});


function changePageSize() {
    var level = $("#pg_size").val();
    $("#pageSize").val(level);
    goPage(1);
}

function goPage(pageNumber) {
    $("#pageIndex").val(pageNumber);
    $("#searchForm").submit();
}
