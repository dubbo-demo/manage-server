<!-- 代扣start -->
<div id="withholdShow" class="modal hide fade" tabindex="-1"
     data-width="760">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
        <h3>代扣</h3>
    </div>
    <div class="modal-body">
        <div class="row-fluid">
            <span class="control-label span3">代扣卡</span>
            <input type="text" name="payTypeName" id="payTypeName" maxlength="10" class="form-control span6" readonly
                   value="客户卡"/>
            <input type="hidden" name="billNo" id="billNo" class="form-control span6" value=""/>
            <input type="hidden" name="idCardNo" id="idCardNo" class="form-control span6" value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">卡类别</span>
            <input type="text" name="bankTypeName" id="bankTypeName" maxlength="15" class="form-control span6" readonly
                   value=""/>
            <input type="hidden" name="bankType" id="bankType" maxlength="15" class="form-control span6" readonly
                   value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">卡号</span>
            <input type="text" name="idBankNo" id="idBankNo" maxlength="20" class="form-control span6" readonly
                   value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">姓名</span>
            <input type="text" name="username" id="username" maxlength="50" class="form-control span6" readonly
                   value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">手机号</span>
            <input type="text" name="reservedPhone" id="reservedPhone" maxlength="11" class="form-control span6"
                   readonly value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">还款金额</span>
            <input type="text" name="payAmount" id="payAmount" maxlength="10" class="form-control span6" value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3"><a href="javascript:uploadFile('#withholdShow')">附件上传</a></span>
        </div>
        <div class="row-fluid">
            <span class="control-label span4" id="error"></span>
        </div>
    </div>
    <div class="modal-footer">
        <a class="btn blue" data-dismiss="modal" onclick="returnBack('#withholdShow');" class="btn">返回</a>
        <button type="button" class="btn blue" onclick="payMade(this, '#withholdShow', false, 4);">提交</button>
    </div>
</div>
<!-- 代扣end -->

<!-- 代偿start -->
<div id="compensateShow" class="modal hide fade" tabindex="-1"
     data-width="760">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
        <h3>代偿</h3>
    </div>
    <div class="modal-body">
        <div class="row-fluid">
            <span class="control-label span3">代扣卡</span>
            <input type="text" name="payTypeName" id="payTypeName" maxlength="10" class="form-control span6" readonly
                   value="代偿卡"/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">卡类别</span>
            <input type="text" name="bankTypeName" id="bankTypeName" maxlength="15" class="form-control span6" readonly
                   value=""/>
            <input type="hidden" name="bankType" id="bankType" maxlength="15" class="form-control span6" value=""/>
            <input type="hidden" name="billNo" id="billNo" class="form-control span6" value=""/>
            <input type="hidden" name="idCardNo" id="idCardNo" class="form-control span6" value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">卡号</span>
            <input type="text" name="idBankNo" id="idBankNo" maxlength="20" class="form-control span6" readonly
                   value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">姓名</span>
            <input type="text" name="username" id="username" maxlength="50" class="form-control span6" readonly
                   value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">手机号</span>
            <input type="text" name="reservedPhone" id="reservedPhone" maxlength="11" class="form-control span6"
                   readonly value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">还款金额</span>
            <input type="text" name="payAmount" id="payAmount" maxlength="10" class="form-control span6" value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3"><a href="javascript:uploadFile('#compensateShow')">附件上传</a></span>
        </div>
        <div class="row-fluid">
            <span class="control-label span4" id="error"></span>
        </div>
    </div>
    <div class="modal-footer">
        <a class="btn blue" data-dismiss="modal" onclick="returnBack('#compensateShow');" class="btn">返回</a>
        <button type="button" class="btn blue" onclick="payMade(this, '#compensateShow', false, 5);">提交</button>
    </div>
</div>
<!-- 代偿end -->

<!-- 减免start -->
<div id="reductionShow" class="modal hide fade" tabindex="-1"
     data-width="760">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
        <h3>减免</h3>
    </div>
    <div class="modal-body">
        <div class="row-fluid ">
            <span class="control-label span3">减免类型</span>
            <select name="isAdvanceSettle" class="form-control span6" id="isAdvanceSettle">
                <option value="0" selected>当期减免</option>
                <option value="1">提前结清减免</option>
            </select>
            <input type="hidden" name="billNo" id="billNo" class="form-control span6" value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">还款金额</span>
            <input type="text" name="payAmount" id="payAmount" maxlength="10" class="form-control span6" value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3"><a href="javascript:uploadFile('#reductionShow')">附件上传</a></span>
        </div>
        <div class="row-fluid">
            <span class="control-label span4" id="error"></span>
        </div>
    </div>
    <div class="modal-footer">
        <a class="btn blue" data-dismiss="modal" onclick="returnBack('#reductionShow');" class="btn">返回</a>
        <button type="button" class="btn blue" data-isredution="0" onclick="payMade(this, '#reductionShow', false, 2);">提交</button>
    </div>
</div>
<!-- 减免end -->

<!-- 提前结清start -->
<div id="earlySettlementShow" class="modal hide fade" tabindex="-1"
     data-width="760">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
        <h3>提前结清代扣</h3>
    </div>
    <div class="modal-body">
        <div class="row-fluid ">
            <span class="control-label span3">代扣卡</span>
            <select name="payType" class="form-control span6" id="payType"
                    onchange="changeCardInfo(this,'#earlySettlementShow')">
                <option value="2" selected>客户卡</option>
                <option value="3">代偿卡</option>
            </select>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">卡类别</span>
            <input type="text" name="bankTypeName" id="bankTypeName" maxlength="15" class="form-control span6" readonly
                   value=""/>
            <input type="hidden" name="bankType" id="bankType" maxlength="15" class="form-control span6" readonly
                   value=""/>
            <input type="hidden" name="billNo" id="billNo" class="form-control span6" value=""/>
            <input type="hidden" name="idCardNo" id="idCardNo" class="form-control span6" value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">卡号</span>
            <input type="text" name="idBankNo" id="idBankNo" maxlength="20" class="form-control span6" readonly
                   value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">姓名</span>
            <input type="text" name="username" id="username" maxlength="50" class="form-control span6" readonly
                   value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">手机号</span>
            <input type="text" name="reservedPhone" id="reservedPhone" maxlength="11" class="form-control span6"
                   readonly value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3">还款金额</span>
            <input type="text" name="payAmount" id="payAmount" maxlength="10" class="form-control span6" value=""/>
        </div>
        <div class="row-fluid">
            <span class="control-label span3"><a href="javascript:uploadFile('#earlySettlementShow')">附件上传</a></span>
        </div>
        <div class="row-fluid">
            <span class="control-label span4" id="error"></span>
        </div>
    </div>
    <div class="modal-footer">
        <a class="btn blue" data-dismiss="modal" onclick="returnBack('#earlySettlementShow');" class="btn">返回</a>
        <button type="button" class="btn blue" data-isredution="0" onclick="payMade(this, '#earlySettlementShow', true, 14);">提交</button>
    </div>
</div>
<!-- 提前结清end -->

<!-- 对公start -->
<div id="toPublicShow" class="modal hide fade" tabindex="-1"
     data-width="760">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
        <h3>对公还款</h3>
    </div>
    <div class="modal-body">
        <div class="row-fluid">
            <span class="control-label span3">还款金额</span>
            <input type="text" name="payAmount" id="payAmount" maxlength="10" class="form-control span6" value=""/>
            <input type="hidden" name="billNo" id="billNo" class="form-control span6" value=""/>
        </div>
    <div class="row-fluid">
    <span class="control-label span3"><a href="javascript:uploadFile('#toPublicShow')">附件上传</a></span>
    </div>
        <div class="row-fluid">
            <span class="control-label span4" id="error"></span>
        </div>
    </div>
    <div class="modal-footer">
        <a class="btn blue" data-dismiss="modal" onclick="returnBack('#toPublicShow');" class="btn">返回</a>
        <button type="button" class="btn blue" onclick="payMade(this, '#toPublicShow', false, 1);">提交</button>
    </div>
</div>
<!-- 对公end -->

<script>
    function returnBack(target) {
//        window.location.href = window.location;
        $(target).modal('hide');
    }

    function uploadFile(target) {
        var this_billNo = $("" + target + " input[name=billNo]").val();
        if(null == this_billNo) {
            BootstrapDialog.alert("账单号不能为空");
            return;
        }
        window.open( serverPath+"/productFile/fileUploadForRepay.htm?billNo="+this_billNo,"_blank");
    }
    function getPayData(target, payType) {
        var data = {};
        // 如果是代扣，代偿
        if (payType == 4 || payType == 5) { // 代扣
            data = {
                "billNo": $("" + target + " input[name=billNo]").val(),
                "bankName": $("" + target + " input[name=bankTypeName]").val(), //卡类别
                "bankType": $("" + target + " input[name=bankType]").val(),
                "idBankNo": $("" + target + " input[name=idBankNo]").val(),//卡号
                "username": $("" + target + " input[name=username]").val(),//姓名
                "payType": payType,// 还款方式
                "idCardNo": $("" + target + " input[name=idCardNo]").val(),//身份证号
                "payAmount": $("" + target + " input[name=payAmount]").val(),//金额
                "reservedPhone": $("" + target + " input[name=reservedPhone]").val(),//手机号
                "Time": new Date().getMilliseconds()
            };
        } else if(payType == 14) { //提前结清
            data = {
                "billNo": $("" + target + " input[name=billNo]").val(),
                "bankName": $("" + target + " input[name=bankTypeName]").val(), //卡类别
                "bankType": $("" + target + " input[name=bankType]").val(),
                "idBankNo": $("" + target + " input[name=idBankNo]").val(),//卡号
                "username": $("" + target + " input[name=username]").val(),//姓名
                "payType": $("" + target + " #payType").val(),// 还款方式
                "idCardNo": $("" + target + " input[name=idCardNo]").val(),//身份证号
                "payAmount": $("" + target + " input[name=payAmount]").val(),//金额
                "reservedPhone": $("" + target + " input[name=reservedPhone]").val(),//手机号
                "Time": new Date().getMilliseconds()
            };
        }else if(payType == 2) { //减免
            data = {
                "billNo": $("" + target + " input[name=billNo]").val(),
                "isAdvanceSettle": $("" + target + " #isAdvanceSettle").val(),
                "initialAmount": $("" + target + " input[name=payAmount]").val(),//金额
                "Time": new Date().getMilliseconds()
            };
        } else  { // 对公
            data = {
                "billNo": $("" + target + " input[name=billNo]").val(),
                "payType": payType,// 还款方式
                "payAmount": $("" + target + " input[name=payAmount]").val(),//金额
                "Time": new Date().getMilliseconds()
            };
        }
        return data;
    }

    /**
     *
     * @param e this
     * @param target model标记
     * @param isAdvanceSettleEnum 是否提前结清
     * @param payType 支付类型
     */
    function payMade(e, target, isAdvanceSettleEnum, payType) {
        var url_this;

        if (payType == 4) { // 代扣
            url_this = serverPath + '/repayMade/manMadeRepay.htm';
        } else if (payType == 5) { // 代偿
            url_this = serverPath + '/repayMade/userMadeRepay.htm';
        } else if (payType == 2) { // 减免
            var isAdvanceSettle_this = $("" + target + " #isAdvanceSettle").val();
            if(isAdvanceSettle_this == 1) {
                isAdvanceSettleEnum = true;
            }
            url_this = serverPath + '/repayMade/redutionMadeRepay.htm';
        } else if (payType == 14) { //提前结清代扣
            url_this = serverPath + '/repayMade/advanceSettleMadeRepay.htm';
        } else if (payType == 1) { //对公
            url_this = serverPath + '/repayMade/businessRepay.htm';
        }
        // 禁用按钮
        $(target + ' .blue').attr('disabled', "true");
        var data = getPayData(target, payType);
//        console.log(data);
        if(isAdvanceSettleEnum) {
            data.isAdvanceSettle = 1;
        } else {
            data.isAdvanceSettle = 0;
        }
//        console.log(data);
//        returnBack(target);
//        return;
        $.post(url_this,data , function (res) {
            // 释放按钮
            $(target + ' .blue').removeAttr("disabled");
            if (res.code == '0') {
                BootstrapDialog.alert(res.message, function () {
                    returnBack(target);
                });
            } else {
                $("" + target + " #error").html('<font color="red">'+res.message+'</font>');
                $("" + target + " #error").css("display", "block");
            }
        }, "json");
    }

    $(function () {
        $("*[data-paytype]").click(function (e) {
            var billNo = $(this).data("billno");
            var payType = $(this).data("paytype");
            var target = $(this).data("target");
            if (payType == 4 || payType == 5 || payType == 14) { // 代扣
                removeCardInfo(target);
            }
            $("" + target + " #error").html('');
            $("" + target + " #error").css("display", "none");
            $("" + target + " input[name=billNo]").val("");
//            console.log("billNo",billNo);
            $("" + target + " input[name=billNo]").val(billNo);
            // 释放按钮
            $(target + ' .blue').removeAttr("disabled");

            if (payType == 4 || payType == 5 || payType == 14) { // 代扣 ;代偿;提前结清代扣
                var payType2 = $(this).data("paytype2");
                getCardInfo(payType2, target);
            }
        });
    });


    function addDataDetail() {
        var addDataDetailResult = '';
        if ($("#addNum").val() == '' || $("#addCode").val() == '' || $("#addName").val() == '') {
            $("#error").html('<font color="red">请输入必选项</font>');
            $("#error").css("display", "block");
            return false;
        }
        //校验重复
        $.ajax({
            url: serverPath + "/dataDetail/addDataDetail.htm",
            type: "post",
            data: {
                "Time": new Date().getMilliseconds()
            },
            dataType: "json",
            success: function (result) {
                if (addDataDetailResult == 7) {
                    $("" + target + " #error").html('<font color="red">名称和编号和位号重复</font>');
                    $("" + target + " #error").css("display", "block");
                    return false;
                }
            },
            error: function () {
                BootstrapDialog.alert("操作失败");
            }
        });
    }

    function changeCardInfo(e, target) {
        var paytype_this = $(e).val();
        removeCardInfo(target);
        getCardInfo(paytype_this, target);
    }
    function removeCardInfo(target) {
        //卡类别
        $("" + target + " input[name=bankTypeName]").val("");

        $("" + target + " input[name=bankType]").val("");

        //卡号
        $("" + target + " input[name=idBankNo]").val("");

        //姓名
        $("" + target + " input[name=username]").val("");

        //手机号
        $("" + target + " input[name=reservedPhone]").val("");

        $("" + target + " input[name=idCardNo]").val("");//身份证号
    }
    function getCardInfo(payType, target) {
        var billNo_this = $("" + target + " input[name=billNo]").val();
        console.log(target,billNo_this);
        var url = serverPath + "/repayMade/manMadeRepayCard.htm";
        var data = {
            "billNo": billNo_this,
            "payType": payType,
            "Time": new Date().getMilliseconds()
        };
        $.getJSON(url, data, function (result) {
            if(result.code == '1') {
//                BootstrapDialog.alert(result.message);
                $("" + target + " #error").html('<font color="red">'+result.message+'</font>');
                $("" + target + " #error").css("display", "block");
            } else {
                var cardData = result.data;
                addInfoToDiv(payType,cardData,target)
            }
        });
    }

    function addInfoToDiv(payType,cardData,target) {
        if (payType == 2) {
            //卡类别
            $("" + target + " input[name=bankTypeName]").val(cardData.bankTypeName);

            $("" + target + " input[name=bankType]").val(cardData.bankType);

            //卡号
            $("" + target + " input[name=idBankNo]").val(cardData.bankCardNo);

            //姓名
            $("" + target + " input[name=username]").val(cardData.memberName);

            //手机号
            $("" + target + " input[name=reservedPhone]").val(cardData.reservedPhone);

            $("" + target + " input[name=idCardNo]").val(cardData.idCard);//身分证号
        } else if (payType == 3) {
            if(null == cardData) {
                return;
            }
            if("1" != cardData.authStatus) {
                $("" + target + " #error").html('<font color="red">门店副理已经绑定代偿卡，但没有鉴权!</font>');
                $("" + target + " #error").css("display", "block");
                // 禁用按钮
                $(target + ' .blue').attr('disabled', "true");
                return;
            }

            //姓名
            $("" + target + " input[name=username]").val(cardData.accountName);

            //手机号
            $("" + target + " input[name=reservedPhone]").val(cardData.mobile);

            $("" + target + " input[name=idCardNo]").val(cardData.idCardNo);//身分证号

            //卡类别
            $("" + target + " input[name=bankTypeName]").val(cardData.bankTypeName);

            $("" + target + " input[name=bankType]").val(cardData.bankNo);

            //卡号
            $("" + target + " input[name=idBankNo]").val(cardData.bankCardNo);

        }
    }
</script>
