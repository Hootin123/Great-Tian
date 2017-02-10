/**
 * 提交前数据验证
 */
function checkSubmit() {
    var data = $('#repaymentForm').serializeArray();
    var flag = false;
    $.each(data, function (i, field) {
        var value = field.value;
        var name = field.name;
        
        if (isEmpty(value)) {
            //备注非必填
            if (name == 'rechargeBak') {
                return;
            } else {
                layer.tips('请填写此字段', '#' + name, {time: 3000});
                flag = false;
                return false;
            }

        }
        flag = true;
    });
    return flag;
}
