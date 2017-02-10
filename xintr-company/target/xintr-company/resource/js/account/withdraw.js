

$(function () {
    // 绑定确定按钮事件
    $('.okBtn').on('click', function () {
        addWithdraw();
    });
});

function addWithdraw() {
    if (checkSubmit()) {
        var ii = layer.load();
        $.ajax({
            type: 'post',
            url: BASE_PATH + '/withdraw.htm',
            data: $('#postalForm').serialize(),
            success: function (data) {
                layer.close(ii);
                if (data.success) {
                    window.location.href = BASE_PATH + '/withdrawSuccess.htm?rechargeId=' + data.data;
                } else {
                    alertWarn(data.message);
                }
            }
        });
    }
}

/**
 * 提交前数据验证
 */
function checkSubmit() {
    var data = $('#postalForm').serializeArray();
    var flag = false;
    $.each(data, function (i, field) {
        var value = field.value;
        var name = field.name;

        //if (isEmpty(value)) {
        //    //备注非必填
        //    layer.tips('请填写此字段', '#' + name, {time: 3000});
        //    flag = false;
        //    return false;
        //}
        ////银行卡号
        //else if (name == 'rechargeBanknumber') {
        //    if (!isBankNumber(value)) {
        //        layer.tips('请输入正确的银行卡号', '#' + name, {time: 3000});
        //        flag = false;
        //        return false;
        //    }
        //}
        //提现金额
        //else
        if (name == 'rechargeMoney') {
            if (!isNumber(value)) {
                layer.tips('请输入正确的金额', '#' + name, {time: 3000,tips:3});
                flag = false;
                return false;
            }
            
            if(parseFloat(value) <= 0){
                layer.tips('请输入正确的金额', '#' + name, {time: 3000,tips:3});
                flag = false;
                return false;
            }
            
            var curMoney = $('#curMoney').text().trim().replace(/,/g,'');
            if(parseFloat(curMoney) < parseFloat(value)){
                layer.tips('您的提现金额大于可用余额，请您重新填写', '#' + name, {time: 3000,tips:3});
                flag = false;
                return false;
            }
        }
        flag = true;
    });
    return flag;
}
