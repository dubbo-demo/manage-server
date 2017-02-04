<#include "/sys/top.ftl"> 
<#include "/sys/left.ftl">
<div class="page-content">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				
				<!--页面标题-->
				<h3 class="page-title">
				</h3>
				<!--面包屑导航-->
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="${ctx}/index.htm">首页</a> <i
						class="icon-angle-right"></i></li>
					<li><a href="#">风险管理</a><i class="icon-angle-right"></i></li>
					<li><a href="#">内部黑名单</a></li>
				</ul>
			</div>
		</div>
		<div>
			<div class="portlet-body form">
				<form id="searchForm" action="#" class="form-horizontal"
					method="post">
					<div class="row-fluid">
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">姓名：</label> <input
								type="text" class="m-wrap span8" name="memberName"
								value="${(queryDto.memberName)!""}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">手机号：</label> <input
								type="text" class="m-wrap span8" name="phone"
								value="${(queryDto.phone)!""}">
						</div>
						<div class="control-group span4 ">
							<label class="help-inline text-right span4">身份证：</label> <input
								type="text" class="m-wrap span8" name="idCard"
								value="${(queryDto.idCard)!""}">
						</div>
					</div>
					<div class="form-actions" style="background: #fff; border: 0;">
						<button type="button" class="btn blue" onclick="search()">查询</button>
						<shiro:hasPermission name="innerBlack:add">
						<a class="btn blue" href="${ctx}/innerBlack/addUI.htm">新增黑名单</a> 
						</shiro:hasPermission>
						<shiro:hasPermission name="innerBlack:import">
						<a	href="#import" data-toggle="modal" class="btn blue">导入</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="innerBlack:export">
						<button type="button" class="btn blue" onclick="export_excel()">导出</button>
						</shiro:hasPermission>
					</div>
				</form>
			</div>

			<div class="tabbable tabbable-custom tabbable-custom-profile">
				<table id="table" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th>姓名</th>
							<th>身份证号</th>
							<th>出生日期</th>
							<th>手机号</th>
							<th>户籍地址</th>
							<th>现住地址</th>
							<th>有无子女</th>
							<th>是否有逾期</th>
							<th>逾期天数</th>
							<th>创建日期</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<tr  id="4">
							<td>1</td>
							<td>**8</td>
							<td>11000019760926****</td>
							<td>1976-09-26</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td></td>
							<td>2016-09-18</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=4">查看详情</a></td>
						</tr>
						<tr  id="8">
							<td>2</td>
							<td>**8</td>
							<td>11000019760926****</td>
							<td>1976-09-26</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td>2%</td>
							<td>
								无 </td>
							<td></td>
							<td>2016-09-18</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=8">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="12">
							<td>3%</td>
							<td>**8</td>
							<td>11000019760926****</td>
							<td>1976-09-26</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td>1%</td>
							<td>
								无 </td>
							<td></td>
							<td>2016-09-18</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=12">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="16">
							<td>4</td>
							<td>**8</td>
							<td>11000019760926****</td>
							<td>1976-09-26</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td>22%</td>
							<td>
								无 </td>
							<td></td>
							<td>2016-09-18</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=16">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="20">
							<td>5</td>
							<td>****s</td>
							<td>11000019760926****</td>
							<td>1976-09-26</td>
							<td></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td></td>
							<td>2016-09-18</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=20">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="24">
							<td>6</td>
							<td>****s</td>
							<td>11000019760926****</td>
							<td>1976-09-26</td>
							<td></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td></td>
							<td>2016-09-18</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=24">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="28">
							<td>7</td>
							<td>****s</td>
							<td>11000019760926****</td>
							<td>1976-09-26</td>
							<td></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td></td>
							<td>2016-09-18</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=28">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="3">
							<td>8</td>
							<td>**8</td>
							<td>37021119810613****</td>
							<td>1981-06-13</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td></td>
							<td>2016-09-13</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=3">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="7">
							<td>9</td>
							<td>**8</td>
							<td>37021119810613****</td>
							<td>1981-06-13</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td></td>
							<td>2016-09-13</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=7">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="11">
							<td>10</td>
							<td>**8</td>
							<td>37021119810613****</td>
							<td>1981-06-13</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td></td>
							<td>2016-09-13</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=11">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="15">
							<td>11</td>
							<td>**8</td>
							<td>37021119810613****</td>
							<td>1981-06-13</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td></td>
							<td>2016-09-13</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=15">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="19">
							<td>12</td>
							<td>**w</td>
							<td>37021119810613****</td>
							<td>1981-06-13</td>
							<td></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td></td>
							<td>2016-09-13</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=19">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="23">
							<td>13</td>
							<td>**w</td>
							<td>37021119810613****</td>
							<td>1981-06-13</td>
							<td></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td></td>
							<td>2016-09-13</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=23">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="27">
							<td>14</td>
							<td>**w</td>
							<td>37021119810613****</td>
							<td>1981-06-13</td>
							<td></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title=""></span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td></td>
							<td>2016-09-13</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=27">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="2">
							<td>15</td>
							<td>*******************十</td>
							<td>37130219880325****</td>
							<td>1988-03-25</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十">一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十</span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六">一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六</span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td>9999</td>
							<td>2016-09-07</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=2">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="6">
							<td>16</td>
							<td>*******************十</td>
							<td>37130219880325****</td>
							<td>1988-03-25</td>
							<td>136****5127</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十">一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十</span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六">一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六</span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td>9999</td>
							<td>2016-09-07</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=6">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="10">
							<td>17</td>
							<td>*******************十</td>
							<td>37130219880325****</td>
							<td>1988-03-25</td>
							<td>136****5127</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十">一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十</span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六">一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六</span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td>9999</td>
							<td>2016-09-07</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=10">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="14">
							<td>18</td>
							<td>*******************十</td>
							<td>37130219880325****</td>
							<td>1988-03-25</td>
							<td>136****5127</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十">一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十</span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六">一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六</span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td>9999</td>
							<td>2016-09-07</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=14">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="18">
							<td>19</td>
							<td>*******************十</td>
							<td>37130219880325****</td>
							<td>1988-03-25</td>
							<td>136****5127</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十">一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十</span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六">一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六</span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td>9999</td>
							<td>2016-09-07</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=18">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="22">
							<td>20</td>
							<td>*******************十</td>
							<td>37130219880325****</td>
							<td>1988-03-25</td>
							<td>136****5127</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十">一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十</span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六">一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六</span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td>9999</td>
							<td>2016-09-07</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=22">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="26">
							<td>21</td>
							<td>*******************十</td>
							<td>37130219880325****</td>
							<td>1988-03-25</td>
							<td>136****5127</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十">一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十</span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六">一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十ss一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六</span></td>
							<td>3%</td>
							<td>
								无 </td>
							<td>9999</td>
							<td>2016-09-07</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=26">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="1">
							<td>22%</td>
							<td>**8</td>
							<td>51250119720303****</td>
							<td>1972-03-03</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="南京市">南京市</span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="建邺区">建邺区</span></td>
							<td>3%</td>
							<td>
								有 </td>
							<td>11</td>
							<td>2016-09-07</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=1">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="5">
							<td>23</td>
							<td>**8</td>
							<td>51250119720303****</td>
							<td>1972-03-03</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="南京市">南京市</span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="建邺区">建邺区</span></td>
							<td>3%</td>
							<td>
								有 </td>
							<td>11</td>
							<td>2016-09-07</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=5">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="9">
							<td>24</td>
							<td>**8</td>
							<td>51250119720303****</td>
							<td>1972-03-03</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="南京市">南京市</span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="建邺区">建邺区</span></td>
							<td>3%</td>
							<td>
								有 </td>
							<td>11</td>
							<td>2016-09-07</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=9">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="13">
							<td>25</td>
							<td>**8</td>
							<td>51250119720303****</td>
							<td>1972-03-03</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="南京市">南京市</span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="建邺区">建邺区</span></td>
							<td>3%</td>
							<td>
								有 </td>
							<td>11</td>
							<td>2016-09-07</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=13">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="17">
							<td>26</td>
							<td>**8</td>
							<td>51250119720303****</td>
							<td>1972-03-03</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="南京市">南京市</span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="建邺区">建邺区</span></td>
							<td>3%</td>
							<td>
								有 </td>
							<td>11</td>
							<td>2016-09-07</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=17">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="21">
							<td>27</td>
							<td>**8</td>
							<td>51250119720303****</td>
							<td>1972-03-03</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="南京市">南京市</span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="建邺区">建邺区</span></td>
							<td>3%</td>
							<td>
								有 </td>
							<td>11</td>
							<td>2016-09-07</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=21">查看详情</a></td>
						</tr>
						<tr class="odd gradeX" id="25">
							<td>28</td>
							<td>**8</td>
							<td>51250119720303****</td>
							<td>1972-03-03</td>
							<td>131****1118</td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="南京市">南京市</span></td>
							<td><span class="span2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin-left:0px" title="建邺区">建邺区</span></td>
							<td>3%</td>
							<td>
								有 </td>
							<td>11</td>
							<td>2016-09-07</td>
							<td><a href="/manage-server/innerBlack/detailUI.htm?id=25">查看详情</a></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<#include "/sys/bottom.ftl">
<script>
$(function(){

	$("#table").dataTable(
	{
		   "aoColumnDefs":[ { "bSortable": false, "aTargets": [0,1,2] },{"sType": "number-fate","aTargets": [7]}],//设置不需要排序的列
	 		"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
		             $('td:eq(0)', nRow).html(iDisplayIndexFull+1);
		    },
	}
	);
})

</script>