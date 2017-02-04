/* Set the defaults for DataTables initialisation */
$.extend( true, $.fn.dataTable.defaults, {
	   "bFilter": false,
	   "sDom": "<'row-fluid'>t<'row-fluid'<'span6'l><'span6'p>>",
	   "sPaginationType": "bootstrap",//用于指定分页器风格
	   "aLengthMenu":[10, 20, 50, 100], // change per page values here
	   "oLanguage": {//语言设置  
        "sLengthMenu": "每页条数 &nbsp;&nbsp;_MENU_",     
        "sZeroRecords": "没有检索到数据",   
        "sEmptyTable": "没有数据",
        "sInfo": "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录",     
        "sInfoEmtpy": "没有数据",     
        "sProcessing": '<i class="fa fa-coffee"></i> 正在加载数据...',    
        "oPaginate": {     
            "sFirst": "首页",     
            "sPrevious": "上一页",     
            "sNext": "下一页",     
            "sLast": "末页",
      	}
	   },
		"fnInitComplete": function(oSettings, json){
			if(oSettings.fnRecordsTotal()==0){
				oSettings.nTBody.remove();
				$(oSettings.nTableWrapper).find("table").nextAll().remove();
				$(oSettings.nTableWrapper).append("<h3 class=\"text-center\">没有数据</h3>");
			}
		},
		"fnDrawCallback": function( oSettings ) {
			App.fixContentHeight();
		 }
} );


/* Default class modification */
$.extend( $.fn.dataTableExt.oStdClasses, {
	"sWrapper": "dataTables_wrapper form-inline"
} );


/* API method to get paging information */
$.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings )
{
	return {
		"iStart":         oSettings._iDisplayStart,
		"iEnd":           oSettings.fnDisplayEnd(),
		"iLength":        oSettings._iDisplayLength,
		"iTotal":         oSettings.fnRecordsTotal(),
		"iFilteredTotal": oSettings.fnRecordsDisplay(),
		"iPage":          Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
		"iTotalPages":    Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
	};
};


/* Bootstrap style pagination control */
$.extend( $.fn.dataTableExt.oPagination, {
	"bootstrap": {
		"fnInit": function( oSettings, nPaging, fnDraw ) {
			var oLang = oSettings.oLanguage.oPaginate;
			var fnClickHandler = function ( e ) {
				e.preventDefault();
				if ( oSettings.oApi._fnPageChange(oSettings, e.data.action) ) {
					fnDraw( oSettings );
				}
			};

			$(nPaging).addClass('pagination').append(
				'<ul>'+
					'<li class="first disabled"><a href="#">'+oLang.sFirst+'</a></li>'+//此处添加
					'<li class="prev disabled"><a href="#"><span class="hidden-480">'+oLang.sPrevious+'</span></a></li>'+
					'<li class="next disabled"><a href="#"><span class="hidden-480">'+oLang.sNext+'</span></a></li>'+
					'<li class="last disabled"><a href="#">'+oLang.sLast+'</a></li>'+//此处添加
					'<li class="total disabled"><a href="javascript:void(0);"> 共'+oSettings.oInstance.fnPagingInfo().iTotalPages+'页</a></li>'+//此处添加
				'</ul>'
			);
			var els = $('a', nPaging);
			$(els[0]).bind( 'click.DT', { action: "first" }, fnClickHandler );//此处添加
			$(els[1]).bind( 'click.DT', { action: "previous" }, fnClickHandler );
			$(els[2]).bind( 'click.DT', { action: "next" }, fnClickHandler );
			$(els[3]).bind( 'click.DT', { action: "last" }, fnClickHandler );//此处添加
		},

		"fnUpdate": function ( oSettings, fnDraw ) {
			var iListLength = 5;
			var oPaging = oSettings.oInstance.fnPagingInfo();
			var an = oSettings.aanFeatures.p;
			var i, j, sClass, iStart, iEnd, iHalf=Math.floor(iListLength/2);

			if ( oPaging.iTotalPages < iListLength) {
				iStart = 1;
				iEnd = oPaging.iTotalPages;
			}
			else if ( oPaging.iPage <= iHalf ) {
				iStart = 1;
				iEnd = iListLength;
			} else if ( oPaging.iPage >= (oPaging.iTotalPages-iHalf) ) {
				iStart = oPaging.iTotalPages - iListLength + 1;
				iEnd = oPaging.iTotalPages;
			} else {
				iStart = oPaging.iPage - iHalf + 1;
				iEnd = iStart + iListLength - 1;
			}
			$('li:last a').html('共'+oPaging.iTotalPages+'页');
			for ( i=0, iLen=an.length ; i<iLen ; i++ ) {
				// Remove the middle elements
				$('li:gt(1)', an[i]).filter(':lt(-3)').remove();//此处修改 $('li:gt(0)', an[i]).filter(':not(:last)').remove();

				// Add the new list items and their event handlers
				for ( j=iStart ; j<=iEnd ; j++ ) {
					sClass = (j==oPaging.iPage+1) ? 'class="active"' : '';
					$('<li '+sClass+'><a href="#">'+j+'</a></li>')
						.insertBefore( $('li:eq(-3)', an[i])[0] )//此处修改 .insertBefore( $('li:last', an[i])[0] )
						.bind('click', function (e) {
							e.preventDefault();
							oSettings._iDisplayStart = (parseInt($('a', this).text(),10)-1) * oPaging.iLength;
							fnDraw( oSettings );
						} );
				}

				// Add / remove disabled classes from the static elements
				if ( oPaging.iPage === 0 ) {
					$('li:lt(2)', an[i]).addClass('disabled'); //此处修改$('li:first', an[i]).addClass('disabled');
				} else {
					$('li:lt(2)', an[i]).removeClass('disabled'); //此处修改$('li:first', an[i]).removeClass('disabled');
				}

				if ( oPaging.iPage === oPaging.iTotalPages-1 || oPaging.iTotalPages === 0 ) {
					$('li:gt(-4)', an[i]).addClass('disabled'); //此处修改$('li:last', an[i]).addClass('disabled');
				} else {
					$('li:gt(-4)', an[i]).removeClass('disabled'); //此处修改$('li:last', an[i]).removeClass('disabled');
				}
				$('li:last', an[i]).addClass('disabled');
			}
		}
	}
} );


//define fate order
jQuery.fn.dataTableExt.oSort['number-fate-asc']  = function(s1,s2) {  
    s1 = s1.replace('%','');  
    s2 = s2.replace('%','');  
    return s1-s2;  
};  
  
jQuery.fn.dataTableExt.oSort['number-fate-desc'] = function(s1,s2) {  
    s1 = s1.replace('%','');  
    s2 = s2.replace('%','');  
    return s2-s1;  
};

/*
 * TableTools Bootstrap compatibility
 * Required TableTools 2.1+
 */
if ( $.fn.DataTable.TableTools ) {
	// Set the classes that TableTools uses to something suitable for Bootstrap
	$.extend( true, $.fn.DataTable.TableTools.classes, {
		"container": "DTTT btn-group",
		"buttons": {
			"normal": "btn",
			"disabled": "disabled"
		},
		"collection": {
			"container": "DTTT_dropdown dropdown-menu",
			"buttons": {
				"normal": "",
				"disabled": "disabled"
			}
		},
		"print": {
			"info": "DTTT_print_info modal"
		},
		"select": {
			"row": "active"
		}
	} );

	// Have the collection use a bootstrap compatible dropdown
	$.extend( true, $.fn.DataTable.TableTools.DEFAULTS.oTags, {
		"collection": {
			"container": "ul",
			"button": "li",
			"liner": "a"
		}
	} );
}