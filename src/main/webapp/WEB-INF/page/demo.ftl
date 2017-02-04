<#include "/sys/top.ftl">
<#--<#include "/sys/left.ftl">-->

<#--排序JS-->
<#--<script src="${cdnPath}/js/sort.js"></script>-->
<div class="container-fluid">

    <!-- BEGIN PAGE CONTENT-->

    <div class="row-fluid">

        <div class="span12">

            <!-- BEGIN EXAMPLE TABLE PORTLET-->

            <div class="portlet box blue">

                <div class="portlet-title">

                    <div class="caption"><i class="icon-edit"></i>Editable Table</div>



                </div>
                <form id="searchForm" action="${serverPath}/demo/ftl.htm" method="post">
                <@p.pageForm value=pagination/>
                </form>
                <div class="portlet-body">

                    <div class="clearfix">

                        <div class="btn-group">

                            <button class="btn green" id="sample_editable_1_new">

                                Add New <i class="icon-plus"></i>

                            </button>

                        </div>

                        <div class="btn-group pull-right">

                            <button data-toggle="dropdown" class="btn dropdown-toggle">Tools <i
                                    class="icon-angle-down"></i>

                            </button>

                            <ul class="dropdown-menu pull-right">

                                <li><a href="#">Print</a></li>

                                <li><a href="#">Save as PDF</a></li>

                                <li><a href="#">Export to Excel</a></li>

                            </ul>

                        </div>

                    </div>

                    <div role="grid" class="dataTables_wrapper form-inline" id="sample_editable_1_wrapper">
                        <div class="row-fluid">
                            <div class="span6">
                                <div id="sample_editable_1_length" class="dataTables_length"><label>
                                    <div class="select2-container m-wrap small" id="s2id_autogen1"><a tabindex="-1"
                                                                                                      class="select2-choice"
                                                                                                      onclick="return false;"
                                                                                                      href="javascript:void(0)">
                                        <span>5</span><abbr style="display:none;"
                                                            class="select2-search-choice-close"></abbr>
                                        <div><b></b></div>
                                    </a><input type="text" class="select2-focusser select2-offscreen"
                                               id="s2id_autogen2">
                                        <div style="display:none" class="select2-drop select2-with-searchbox">
                                            <div class="select2-search"><input type="text" class="select2-input"
                                                                               autocomplete="off"></div>
                                            <ul class="select2-results"></ul>
                                        </div>
                                    </div>
                                    <select name="sample_editable_1_length" size="1" aria-controls="sample_editable_1"
                                            class="m-wrap small select2-offscreen" tabindex="-1">
                                        <option value="5" selected="selected">5</option>
                                        <option value="15">15</option>
                                        <option value="20">20</option>
                                        <option value="-1">All</option>
                                    </select> records per page</label></div>
                            </div>
                            <div class="span6">
                                <div class="dataTables_filter" id="sample_editable_1_filter"><label>Search: <input
                                        type="text" aria-controls="sample_editable_1" class="m-wrap medium"></label>
                                </div>
                            </div>
                        </div>
                        <table id="sample_editable_1" class="table table-striped table-hover table-bordered dataTable"
                               aria-describedby="sample_editable_1_info">

                            <thead>

                            <tr role="row">
                                <th class="sorting_disabled" role="columnheader" rowspan="1" colspan="1"
                                    style="width: 301px;" aria-label="Username">Username
                                </th>
                                <@p.sort field="id" sortField="${(pagination.sortField)!}" sortOrder="${(pagination.sortOrder)!}" name="Full name" ></@p.sort>
                                <@p.sort field="columnheader" sortField="${(pagination.sortField)!}" sortOrder="${(pagination.sortOrder)!}" name="Points" ></@p.sort>
                                <th class="sorting" role="columnheader" tabindex="0" aria-controls="sample_editable_1"
                                    rowspan="1" colspan="1" style="width: 272px;"
                                    aria-label="Notes: activate to sort column ascending">Notes
                                </th>
                                <th class="sorting" role="columnheader" tabindex="0" aria-controls="sample_editable_1"
                                    rowspan="1" colspan="1" style="width: 142px;"
                                    aria-label="Edit: activate to sort column ascending">Edit
                                </th>
                                <th class="sorting" role="columnheader" tabindex="0" aria-controls="sample_editable_1"
                                    rowspan="1" colspan="1" style="width: 210px;"
                                    aria-label="Delete: activate to sort column ascending">Delete
                                </th>
                            </tr>

                            </thead>


                            <tbody role="alert" aria-live="polite" aria-relevant="all">
                            <tr class="odd">

                                <td class="  sorting_1">alex</td>

                                <td class=" ">Alex Nilson</td>

                                <td class=" ">1234</td>

                                <td class="center ">power user</td>

                                <td class=" "><a href="javascript:;" class="edit">Edit</a></td>

                                <td class=" "><a href="javascript:;" class="delete">Delete</a></td>

                            </tr>
                            <tr class="even">

                                <td class="  sorting_1">gist124</td>

                                <td class=" ">Nick Roberts</td>

                                <td class=" ">62</td>

                                <td class="center ">new user</td>

                                <td class=" "><a href="javascript:;" class="edit">Edit</a></td>

                                <td class=" "><a href="javascript:;" class="delete">Delete</a></td>

                            </tr>
                            <tr class="odd">

                                <td class="  sorting_1">goldweb</td>

                                <td class=" ">Sergio Jackson</td>

                                <td class=" ">132</td>

                                <td class="center ">elite user</td>

                                <td class=" "><a href="javascript:;" class="edit">Edit</a></td>

                                <td class=" "><a href="javascript:;" class="delete">Delete</a></td>

                            </tr>
                            <tr class="even">

                                <td class="  sorting_1">lisa</td>

                                <td class=" ">Lisa Wong</td>

                                <td class=" ">434</td>

                                <td class="center ">new user</td>

                                <td class=" "><a href="javascript:;" class="edit">Edit</a></td>

                                <td class=" "><a href="javascript:;" class="delete">Delete</a></td>

                            </tr>
                            <tr class="odd">

                                <td class="  sorting_1">nick12</td>

                                <td class=" ">Nick Roberts</td>

                                <td class=" ">232</td>

                                <td class="center ">power user</td>

                                <td class=" "><a href="javascript:;" class="edit">Edit</a></td>

                                <td class=" "><a href="javascript:;" class="delete">Delete</a></td>

                            </tr>
                            </tbody>
                        </table>
                        <@p.pagination pagination />
                    </div>

                </div>

            </div>

            <!-- END EXAMPLE TABLE PORTLET-->

        </div>

    </div>

    <!-- END PAGE CONTENT -->

</div>
<script>


</script>
<#include "/sys/bottom.ftl">


