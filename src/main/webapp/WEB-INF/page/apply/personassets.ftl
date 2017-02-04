<form action="#" id="personassets_form" class="form-horizontal">
	<#if record??>
	  <div class="row-fluid">
	  		<div class="control-group span4">
	
				<label class="control-label">房产总数量</label>
	
				<div class="controls">
	
					<input type="number" class="span9 m-wrap" name="houseNum"  value="${record.houseNum!}"/>
	
	
				</div>
	
			</div>
	  		<div class="control-group span4">
	
				<label class="control-label">房产按揭数量</label>
	
				<div class="controls">
	
					<input type="number" class="span9 m-wrap" name="loanHouseNum" value="${record.loanHouseNum!}"/>
	
	
				</div>
	
			</div>
	  		<div class="control-group span4">
	
				<label class="control-label">房产按揭金额</label>
	
				<div class="controls">
	
					<input type="text" class="span9 m-wrap" name="loanHouseAmount" value="${record.loanHouseAmount!}" />
	
	
				</div>
	
			</div>
	  		
		</div>
	  <div class="row-fluid">
	  		<div class="control-group span4">
	
				<label class="control-label">车辆总数量</label>
	
				<div class="controls">
	
					<input type="number" class="span9 m-wrap" name="carNum"  value="${record.carNum!}" />
	
	
				</div>
	
			</div>
	  		<div class="control-group span4">
	
				<label class="control-label">车辆按揭数量</label>
	
				<div class="controls">
	
					<input type="number" class="span9 m-wrap" name="loanCarNum" value="${record.loanCarNum!}"/>
	
	
				</div>
	
			</div>
	  		<div class="control-group span4">
	
				<label class="control-label">车辆按揭金额</label>
	
				<div class="controls">
	
					<input type="text" class="span9 m-wrap" name="loanCarAmount" value="${record.loanCarAmount!}"/>
	
				</div>
	
			</div>
		</div>
	<#else>
	  <div class="row-fluid">
	  		<div class="control-group span4">
	
				<label class="control-label">房产总数量</label>
	
				<div class="controls">
	
					<input type="number" class="span9 m-wrap" name="houseNum"  />
	
	
				</div>
	
			</div>
	  		<div class="control-group span4">
	
				<label class="control-label">房产按揭数量</label>
	
				<div class="controls">
	
					<input type="number" class="span9 m-wrap" name="loanHouseNum" />
	
	
				</div>
	
			</div>
	  		<div class="control-group span4">
	
				<label class="control-label">房产按揭金额</label>
	
				<div class="controls">
	
					<input type="text" class="span9 m-wrap" name="loanHouseAmount" />
	
	
				</div>
	
			</div>
	  		
		</div>
	  <div class="row-fluid">
	  		<div class="control-group span4">
	
				<label class="control-label">车辆总数量</label>
	
				<div class="controls">
	
					<input type="number" class="span9 m-wrap" name="carNum" />
	
				</div>
	
			</div>
	  		<div class="control-group span4">
	
				<label class="control-label">车辆按揭数量</label>
	
				<div class="controls">
	
					<input type="number" class="span9 m-wrap" name="loanCarNum" />
	
	
				</div>
	
			</div>
	  		<div class="control-group span4">
	
				<label class="control-label">车辆按揭金额</label>
	
				<div class="controls">
	
					<input type="text" class="span9 m-wrap" name="loanCarAmount" />
	
				</div>
	
			</div>
		</div>
	</#if>
</form>