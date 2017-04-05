<!--左边导航栏开始-->
		<div class="page-sidebar nav-collapse collapse">
			<ul class="page-sidebar-menu">
				<li>
					<div class="sidebar-toggler hidden-phone"></div>
				</li>
				<!--<li class="start active ">
					<a href="index.html">
						<i class="icon-home"></i> 
						<span class="title">Dashboard</span>
						<span class="selected"></span>
					</a>
				</li>-->
				<#if Session.menus??>
					<#if Session.menu_index??>
						<#assign  _menu_index="${Session.menu_index}"/>
					<#else>
						<#assign  _menu_index="0"/>
					</#if>
					<#list Session.menus as menu>
							<#if menu_index == _menu_index?number>
								<li class="active">
							<#else>
								<li class="">
							</#if>
							<a href="javascript:;">
								<i class="${menu.menuIcon}"></i>
								<span class="title">${menu.menuName}</span>
								<span class="arrow "></span>
							</a>
							<ul class="sub-menu">
								<#if Session.child_index??>
									<#assign  _child_index="${Session.child_index}"/>
								<#else>
									<#assign  _child_index="0"/>
								</#if>
								<#assign exists_child="false"/>
								<#if menu.chirdlen??>
									 <#list menu.chirdlen as child>
									 	 <#if menu_index == _menu_index?number>
											<#if child_index == _child_index?number>
    											    <#if child.menuUrl?index_of("http")!=-1>
    												    <li class="active"><a href="${child.menuUrl}?menu_index=${menu_index}&child_index=${child_index}">${child.menuName}</a></li>
    												<#else>
    												    <li class="active"><a href="${serverPath}${child.menuUrl}?menu_index=${menu_index}&child_index=${child_index}">${child.menuName}</a></li>
                                                    </#if>
											 	<#assign exists_child="true"/>
											 <#else>
											    <#assign exists_child="false"/>
											 </#if>										
										</#if>
										<#if exists_child != "true">
										    <#if child.menuUrl?index_of("http")!=-1>
											    <li><a href="${child.menuUrl}?menu_index=${menu_index}&child_index=${child_index}">${child.menuName}</a></li>
											<#else>
                                                <li><a href="${serverPath}${child.menuUrl}?menu_index=${menu_index}&child_index=${child_index}">${child.menuName}</a></li>
                                            </#if>
										</#if>
									 </#list>
								</#if>
							</ul>
						</li>
				 	</#list>
				</#if>
			</ul>
		</div>
		<!--左边导航栏结束-->