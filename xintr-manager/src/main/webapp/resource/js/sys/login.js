var formRule = {
    email: /^([a-z0-9A-Z]+[_-|\.]?)+[a-z0-9A-Z]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\.)+[a-zA-Z]{2,}$/i,
    phone: /^((13[0-9])|(15[^4,\D])|(18[0-9]|(17[0-9])))\d{8}$/i,
    password: /^[\@A-Za-z0-9\!\#\$\%\^\&\*\.\~]{6,22}$/,
    verify: /^\d{6}$/,
    vcode: /^[a-zA-Z0-9]{5}$/
};


var Gtips = {
    username: '请输入正确的手机号或邮箱地址',
    password: '密码错误请重新输入',
    verify: '动态码错误',
    vcode: '验证码错误'
}


//初始化
$(function () {
    //初始化校验
    initValidate();
})

/**
 * 初始化校验
 */
function initValidate() {
    $.validator.setDefaults({
        highlight: function (e) {
            $(e).closest(".form-group").removeClass("has-success").addClass("has-error")
        },
        success: function (e) {
            e.closest(".form-group").removeClass("has-error").addClass("has-success")
        },
        errorElement: "span",
        errorPlacement: function (e, r) {
            e.appendTo(r.is(":radio") || r.is(":checkbox") ? r.parent().parent().parent() : r.parent())
        },
        errorClass: "help-block m-b-none",
        validClass: "help-block m-b-none"
    });

    var e = "<i class='fa fa-times-circle'></i> ";
    // 用户名验证
    jQuery.validator.addMethod("phoneoremial", function (value, element) {
        return formRule.phone.test(value) || formRule.email.test(value);
    }, e + "请输入正确的手机号或邮箱地址");


    $("#loginForm").validate({
        rules: {
            username: {
                required: !0,
                phoneoremial: true,
                remote: {
                    url: "isUserExist.htm",     //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {                     //要传递的数据
                        username: function () {
                            return $("#username").val();
                        }
                    }
                }
            },
            password: {
                required: !0,
                minlength: 6
            }
        },
        messages: {
            username: {
                required: e + "请输入手机号码或邮箱地址",
                remote: e + "该账户不存在"
            },
            password: {
                required: e + "请输入您的密码",
                minlength: e + "密码必须5个字符以上"
            }
        },
        submitHandler: function (form) {
            //登录
            onLogin();
        }
    });
}


/**
 * 登录
 */
function onLogin() {
    if ($("#loginForm").valid) {
        $.ajax({
            type: 'post',
            url: 'toLogin.htm',
            data: {username: $('#username').val(), password: $('#password').val()},
            success: function (data) {
                if (data.success) {
                    window.location.href = "home.htm";
                } else {
                    showWarning(data.message);
                }
            }
        });
    }
}