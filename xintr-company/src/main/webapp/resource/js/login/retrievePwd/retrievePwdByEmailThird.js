$(function(){
    console.log($("[name=loginName]").val());
    $("#finishBtn").click(function(){
        if(validatorThirdData()) {
            $.ajax({
                type: 'post',
                url: 'modifyPwdAgain.htm',
                data: $("#pwdThirdForm").serialize(),
                async: false,
                success: function (data) {
                    console.log(data);
                    if (data.success) {
                        $("#verifycodeTip").text("");
                        $('.pacity').css('display','block');
                        $('.aboxbg').css('display','block');
                    } else {
                        $("#verifycodeTip").text(data.message);
                    }
                }
            });
        }
    });

    $("[name=newPwd]").blur(function(){
        validatorThirdData();
    });

    $("[name=confirmNewPwd]").blur(function(){
        validatorThirdData();
    });

    $("#sureBtnThird").click(function(){
        location.href = "/";
    });
});

/**
 * 验证数据
 */
function validatorThirdData() {
    var newPwd = $("[name=newPwd]").val();
    var confirmNewPwd = $("[name=confirmNewPwd]").val();
    var validatorMsg = "OK";
    if (newPwd == null || newPwd == "" || newPwd == undefined) {
        validatorMsg = "请输入新密码";
    } else if (confirmNewPwd == null || confirmNewPwd == "" || confirmNewPwd == undefined) {
        validatorMsg = "请输入确认密码";
    } else if (newPwd.length > 20) {
        validatorMsg = "新密码长度不能超过20位";
    } else if (confirmNewPwd.length > 20) {
        validatorMsg = "确认密码长度不能超过20位";
    } else if (newPwd.length < 6) {
        validatorMsg = "新密码长度不能小于6位";
    } else if (confirmNewPwd.length <6) {
        validatorMsg = "确认密码长度不能小于6位";
    } else if (!/^[A-Za-z0-9]+$/.test(newPwd)) {
        validatorMsg = "新密码只能输入数字或字母";
    } else if (!/^[A-Za-z0-9]+$/.test(confirmNewPwd)) {
        validatorMsg = "确认密码只能输入数字或字母";
    }else if(confirmNewPwd!=newPwd){
        validatorMsg = "新密码与确认密码不一致";
    }
    if (validatorMsg == "OK") {
        $("#verifycodeTip").text("");
        return true;
    } else {
        $("#verifycodeTip").text(validatorMsg);
        return false;
    }
}