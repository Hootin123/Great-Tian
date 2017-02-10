
$(function () {
    // 绑定确定按钮事件
    $('.okBtn').on('click', function () {
        addRecharge();
    });
});

function addRecharge() {
    if (checkSubmit()) {
        var ii = layer.load();
        $.ajax({
            type: 'post',
            url: 'recharge.htm',
            data: $('#rechargeForm').serialize(),
            success: function (data) {
                layer.close(ii);
                if (data.success) {
                    window.location.href = BASE_PATH + '/rechargeSuccess.htm?rechargeId=' + data.data;
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
    var data = $('#rechargeForm').serializeArray();
    var flag = false;
    $.each(data, function (i, field) {
        var value = field.value;
        var name = field.name;

        if (isEmpty(value)) {
            layer.tips('请填写此字段', '#' + name, {time: 3000});
            flag = false;
            return false;
        }
        //银行卡号
        //else if (name == 'rechargeBanknumber') {
        //    if (!isBankNumber(value)) {
        //        layer.tips('请输入正确的银行卡号', '#' + name, {time: 3000});
        //        flag = false;
        //        return false;
        //    }
        //}
        //还款现金
        else if(name == 'rechargeSerialNumber'){
            if(!/^[A-Za-z0-9]+$/.test(value)){
                layer.tips('请输入正确的流水号', '#' + name, {time: 3000});
                flag = false;
                return false;
            }
        } else if (name == 'rechargeMoney') {
            if (!isMoney(value)) {
                layer.tips('请输入正确的金额', '#' + name, {time: 3000});
                flag = false;
                return false;
            }

            if(parseFloat(value) <= 0){
                layer.tips('请输入正确的金额', '#' + name, {time: 3000});
                flag = false;
                return false;
            }

        }
        flag = true;
    });
    return flag;
}
