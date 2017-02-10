var Gtips = {
    username: '请输入正确的手机号或邮箱地址',
    password: '密码错误请重新输入',
    verify: '动态码错误',
    vcode: '验证码错误'
};


$(function () {

    //重写校验方法
    parent.DataGrid.prototype.onCheck = function () {
        if (isEmpty($('#id').val())) {
            var mobilPhone = $('#mobilePhone').val();
            //新增
            //手机号码或邮箱地址
            if (isEmpty(mobilPhone)) {
                layer.tips('请输入手机号码或邮箱地址', '#mobilePhone', {time: 3000, tips: 1});
                return false;
            }
            if (!isEmail(mobilPhone) && !isMobile(mobilPhone)) {
                layer.tips('请输入手机号码或邮箱地址', '#mobilePhone', {time: 3000, tips: 1});
                return false;
            }
            if (isEmpty($('#pwd').val())) {
                layer.tips('请输入密码', '#pwd', {time: 3000, tips: 1});
                return false;
            } else if (isEmpty($('#js_pwd').val())) {
                layer.tips('请再次输入密码', '#js_pwd', {time: 3000, tips: 1});
                return false;
            } else if ($('#pwd').val() != $('#js_pwd').val()) {
                layer.tips('两次输入的密码不一致', '#js_pwd', {time: 3000, tips: 1});
                return false;
            }
        } else {
            //修改
            if (isEmpty($('#mobilePhone').val()) && isEmpty($('#email').val())) {
                layer.tips('手机号码和邮箱地址至少一个不为空', '#mobilePhone', {time: 3000, tips: 1});
                return false;
            }
        }
        return true;
    };
});

