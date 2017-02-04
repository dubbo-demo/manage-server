<form action="#" id="compositeOpinion_form" class="form-horizontal">
	<div class="row-fluid">
		<div class="control-group span6">
			<label class="control-label text-right">结论<span
				class="required">*</span></label>

			<div class="controls">
				<select tabindex="1" name="state"  class="span6 m-wrap">
					<option value="0">请选择</option>
					<option value="${FINISH}" 
						<#if FINISH == record.subState>
							selected = 'selected'
						</#if>
					>同意</option>

					<option value="${REFUSE}"
					<#if REFUSE == record.subState>
							selected = 'selected'
					</#if>
					>拒绝</option>

				</select>

			</div>

		</div>
	</div>
	<div class="row-fluid">
		<div class="control-group">

			<label class="control-label text-right">描述</label>

			<div class="controls">
				<textarea rows="3" name="desc" class="span9 m-wrap" data-placeholder="描述一下理由">${record.applyRemark!""}</textarea>
			</div>

		</div>
	</div>
</form>