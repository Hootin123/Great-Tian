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
};

//初始化
$(function () {
    //是否同意
    $(".i-checks").iCheck({checkboxClass: "icheckbox_square-green", radioClass: "iradio_square-green",});

    //初始化校验
    initValidate();

    //注册回车提交表单事件
    $('#js_phone').on('keyup', function (event) {
        if (event.keyCode == '13') onRegister();
    });
    $('#js_pwd1').on('keyup', function (event) {
        if (event.keyCode == '13') onRegister();
    });
    $('#js_pwd2').on('keyup', function (event) {
        if (event.keyCode == '13') onRegister();
    });
});


/**
 * 初始化校验
 */
function initValidate() {
    //$.validator.setDefaults({
    //    highlight: function (e) {
    //        $(e).closest(".form-group").removeClass("has-success").addClass("has-error")
    //    },
    //    success: function (e) {
    //        e.closest(".form-group").removeClass("has-error").addClass("has-success")
    //    },
    //    errorElement: "span",
    //    errorPlacement: function (e, r) {
    //        e.appendTo(r.is(":radio") || r.is(":checkbox") ? r.parent().parent().parent() : r.parent())
    //    },
    //    errorClass: "help-block m-b-none",
    //    validClass: "help-block m-b-none"
    //});

    //var e = "<i class='fa fa-times-circle'></i> ";
    // 用户名验证
    jQuery.validator.addMethod("phoneoremial", function (value, element) {
        return formRule.phone.test(value) || formRule.email.test(value);
    }, e + Gtips.username);


    $("#signupForm").validate({
        rules: {
            js_pwd1: {
                required: !0,
                minlength: 6
            },
            js_pwd2: {
                required: !0,
                minlength: 6,
                equalTo: "#js_pwd1"
            },
            agree: "required",
            js_phone: {
                required: !0,
                phoneoremial: true,
                remote: {
                    url: "isRegister.htm",     //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {                     //要传递的数据
                        js_phone: function () {
                            return $("#js_phone").val();
                        }
                    }
                }
            }
        },
        messages: {
            js_pwd1: {
                required: e + "请输入您的密码",
                minlength: e + "密码必须5个字符以上"
            },
            js_pwd2: {
                required: e + "请再次输入密码",
                minlength: e + "密码必须5个字符以上",
                equalTo: e + "两次输入的密码不一致"
            },
            agree: {
                required: e + "必须同意协议后才能注册",
                element: "#agree-error"
            },
            js_phone: {
                required: e + "请输入手机号码或邮箱地址",
                remote: e + "该账户已被注册"
            }
        },
        submitHandler: function (form) {
            //注册
            onRegister();
        }
    });
}


/**
 * 注册
 */
function onRegister() {
    if ($("#signupForm").valid) {
        $.ajax({
            type: 'post',
            url: 'registerUser.htm',
            data: {js_phone: $('#js_phone').val(), js_pwd1: $('#js_pwd1').val(), js_pwd2: $('#js_pwd2').val()},
            success: function (data) {
                if (data.success) {
                    swal({title: "太帅了", text: "注册成功", type: "success"});
                } else {
                    swal({title: "很抱歉", text: data.message, type: "warning"});
                }
            }
        });
    }
}

