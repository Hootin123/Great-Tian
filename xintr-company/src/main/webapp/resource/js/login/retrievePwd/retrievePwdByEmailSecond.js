$(function(){
    console.log($("[name=loginName]").val());
    console.log($("[name=memberEmail]").val());
    $("#secondNextBtn").click(function(){
        if(validatorSecondData()) {
            $.ajax({
                type: 'post',
                url: 'emailCodeValidator.htm',
                data: $("#pwdSecondForm").serialize(),
                async: false,
                success: function (data) {
                    console.log(data);
                    if (data.success) {
                        $("#verifycodeTip").text("");
                        location.href = "toRetrievePwdThirdPage.htm?loginName=" + $("[name=loginName]").val();
                    } else {
                        $("#verifycodeTip").text(data.message);
                    }
                }
            });
        }
    });

    $("[name=emailCode]").blur(function(){
        validatorSecondData();
    });
});

/**
 * 验证数据
 */
function validatorSecondData() {
    var emailCode = $("[name=emailCode]").val();
    var validatorMsg = "OK";
    if (emailCode == null || emailCode == "" || emailCode == undefined) {
        validatorMsg = "请输入邮件验证码";
    }
    if (validatorMsg == "OK") {
        $("#verifycodeTip").text("");
        return true;
    } else {
        $("#verifycodeTip").text(validatorMsg);
        return false;
    }
}