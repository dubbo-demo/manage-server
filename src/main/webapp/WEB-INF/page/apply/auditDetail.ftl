<#include "/sys/top.ftl"> <#include "/sys/left.ftl">

	<!-- BEGIN CONTAINER -->   



		<!-- BEGIN PAGE -->

		<div class="page-content">

			<!-- BEGIN SAMPLE PORTLET CONFIGURATION MODAL FORM-->

			<div id="portlet-config" class="modal hide">

				<div class="modal-header">

					<button data-dismiss="modal" class="close" type="button"></button>

					<h3>portlet Settings</h3>

				</div>

				<div class="modal-body">

					<p>Here will be a configuration form</p>

				</div>

			</div>

			<!-- END SAMPLE PORTLET CONFIGURATION MODAL FORM-->

			<!-- BEGIN PAGE CONTAINER-->

			<div class="container-fluid">

				<!-- BEGIN PAGE HEADER-->

				<div class="row-fluid">

					<div class="span12">


						<!-- BEGIN PAGE TITLE & BREADCRUMB-->

						<h3 class="page-title">

							User Profile <small>user profile sample</small>

						</h3>

						<ul class="breadcrumb">

							<li>

								<i class="icon-home"></i>

								<a href="index.html">Home</a> 

								<i class="icon-angle-right"></i>

							</li>

							<li>

								<a href="#">Extra</a>

								<i class="icon-angle-right"></i>

							</li>

							<li><a href="#">User Profile</a></li>

						</ul>

						<!-- END PAGE TITLE & BREADCRUMB-->

					</div>

				</div>

				<!-- END PAGE HEADER-->

				<!-- BEGIN PAGE CONTENT-->

				<div class="row-fluid profile">

					<div class="span12">

						<!--BEGIN TABS-->

						<div class="tabbable tabbable-custom tabbable-full-width">

							<ul class="nav nav-tabs">

								<li class="active"><a href="#tab_1" data-toggle="tab">Overview</a></li>

								<li><a href="#tab_2" data-toggle="tab">Profile Info</a></li>

								<li><a href="#tab_3" data-toggle="tab">Account</a></li>

								<li><a href="#tab_4" data-toggle="tab">Projects</a></li>
								
								<li><a href="#tab_5" data-toggle="tab">Projects</a></li>

								<li><a href="#tab_6" data-toggle="tab">Help</a></li>

							</ul>

							<div class="tab-content">

								<div class="tab-pane row-fluid active" id="tab_1">

								</div>

								<!--end tab-pane-->

								<div class="tab-pane profile-classic row-fluid" id="tab_2">

									<div class="span2"><img src="media/image/profile-img.png" alt="" /> <a href="#" class="profile-edit">edit</a></div>


								</div>

								<!--tab_1_2-->

								<div class="tab-pane row-fluid profile-account" id="tab_3">

										tab3
								</div>

								<!--end tab-pane-->

								<div class="tab-pane" id="tab_4">

										tab4

								</div>

								<!--end tab-pane-->
								<div class="tab-pane row-fluid" id="tab_5">

									<div class="row-fluid">

										tab5
									</div>

								</div>

								<!--end tab-pane-->
								<div class="tab-pane row-fluid" id="tab_6">

									<div class="row-fluid">

										tab6
									</div>

								</div>

								<!--end tab-pane-->

							</div>

						</div>

						<!--END TABS-->

					</div>

				</div>

				<!-- END PAGE CONTENT-->

			</div>

			<!-- END PAGE CONTAINER--> 

		</div>

		<!-- END PAGE -->    


	<!-- END CONTAINER -->
<!-- END PAGE CONTAINER-->
<#include "/sys/bottom.ftl">