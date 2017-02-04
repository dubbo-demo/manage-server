<div class="tab-pane row-fluid active" id="reciptionDetail">
	<div class="portlet box">
		<div class="portlet-body form">
				<div>
					<h4 style="text-align:center">
						<strong>放款情况</strong>
					</h4>
				</div>
			<hr>
			<!-- edit form -->
				<div class="well">
					<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="help-inline text-right span4">贷款类型：</label> <input readonly
										type="text" class="m-wrap span8" name="applyLoanNo"
										value="信用贷款">
								</div>
								<div class="control-group span4 ">
									<label class="help-inline text-right span4">门店：</label> <input readonly
										type="text" class="m-wrap span8" name="memberName"
										value="${(loanInfo.storeName)!}">
								</div>
								<div class="control-group span4 ">
									<label class="help-inline text-right span4">大区：</label> <input readonly
										type="text" class="m-wrap span8" name="idCard"
										value="${(loanInfo.areaName)!}">
								</div>
					</div>
					<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="help-inline text-right span4">合同编号：</label> <input readonly
										type="text" class="m-wrap span8" name="applyLoanNo"
										value="${(loanInfo.contractNo)!}">
								</div>
								<div class="control-group span4 ">
									<label class="help-inline text-right span4">客户姓名：</label> <input readonly
										type="text" class="m-wrap span8" name="memberName"
										value="${(loanInfo.memberName)!}">
								</div>
								<div class="control-group span4 ">
									<label class="help-inline text-right span4">手机号：</label> <input readonly
										type="text" class="m-wrap span8" name="idCard"
										value="${(loanInfo.phone)!}">
								</div>
					</div>
					<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="help-inline text-right span4">产品名称：</label> <input readonly
										type="text" class="m-wrap span8" name="applyLoanNo"
										value="${(loanInfo.productName)!}">
								</div>
								<div class="control-group span4 ">
									<label class="help-inline text-right span4">期数：</label> <input readonly
										type="text" class="m-wrap span8" name="memberName"
										value="${(loanInfo.loanPeriods)!}">
								</div>
								<div class="control-group span4 ">
									<label class="help-inline text-right span4">签约金额：</label> <input readonly
										type="text" class="m-wrap span8" name="idCard"
										value="${(loanInfo.contractAmount)!}">
								</div>
					</div>
					<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="help-inline text-right span4">放款日期：</label> <input id="applyLoanNo" readonly
										type="text" class="m-wrap span8" name="applyLoanNo"
										value="${(loanInfo.loanTime?string("yyyy-MM-dd"))!}">
								</div>
								<div class="control-group span4 ">
									<label class="help-inline text-right span4">实际放款金额：</label> <input readonly
										type="text" class="m-wrap span8" name="memberName"
										value="${(loanInfo.repayMoney)!}">
								</div>
								<div class="control-group span4 ">
									<label class="help-inline text-right span4">服务费：</label> <input readonly
										type="text" class="m-wrap span8" name="idCard"
										value="${(loanInfo.serviceRate)!}">
								</div>
					</div>
					<div class="row-fluid">
								<div class="control-group span4 ">
									<label class="help-inline text-right span4">应收总利息：</label> <input id="applyLoanNo" readonly
										type="text" class="m-wrap span8" name="applyLoanNo"
										value="${(loanInfo.interestAmount)!}">
								</div>
								<div class="control-group span4 ">
									<label class="help-inline text-right span4">还款总额：</label> <input readonly
										type="text" class="m-wrap span8" name="memberName"
										value="${(loanInfo.repaymentAmount)!}">
								</div>
					</div>
			</div>
		</div>
	</div>
</div>	