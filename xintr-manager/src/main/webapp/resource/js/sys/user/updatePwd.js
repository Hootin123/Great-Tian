/**
 * 检查密码输入
 */
function checkUpdatePwd() {
    var oldPwd = $('#oldPwd').val();
    if (isEmpty(oldPwd)) {
        layer.tips('请输入原密码', '#oldPwd', {time: 3000, tips: 1});
        return false;
    }
    var newPwd = $('#newPwd').val();
    if (isEmpty(newPwd)) {
        layer.tips('请输入新密码', '#newPwd', {time: 3000, tips: 1});
        return false;
    }
    var configPwd = $('#configPwd').val();
    if (isEmpty(configPwd)) {
        layer.tips('请输入确认密码', '#configPwd', {time: 3000, tips: 1});
        return false;
    }
    if (newPwd != configPwd) {
        layer.tips('新密码和确认密码不一致', '#configPwd', {time: 3000, tips: 1});
        return false;
    }
    return true;
}