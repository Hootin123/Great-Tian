var validatorMsg = null;
var confirmPhone=null;
$(function(){
    //获取验证码
    $("#getCodeBtn").click(function(){
        validatorInfo(1);
        if(validatorMsg == "OK") {
            //验证码重新生成
            changeCodeImg();
            //显示验证码弹框
            hideCodeDialog(false);
        }
    });


    //弹框点击确认按钮,普通验证码验证,防止无限发信息
    $("#codeSureBtn").unbind("click").click(function(){
        //validatorInfo(5);
        //if(validatorMsg == "OK"){
            $.ajax({
                type: 'post',
                url: 'checkVcodeimgByPhone.htm',
                data: {"emailCode": $("[name=emailCode]").val(), "memberPhone": $("[name=memberPhone]").val()},
                async: false,
                success: function (data) {
                   // console.log(data);
                   // debugger;
                    if (data.success) {
                        confirmPhone=$("[name=memberPhone]").val();
                        $("#verifycodeTip").text("");
                        hideCodeDialog(true);
                        timeReduceBySeconds("getCodeBtn",60);
                    } else {
                        $("#verifycodeTip").text(data.message);
                    }
                }
            });
        //}
    });

    //修改密码
    $("#sureBtn").click(function(){
        var flag=true;//验证是否能修改密码,true能修改密码
        if(validatorInfo(1)!=="OK"){
            flag=false;
        }
        if(validatorInfo(2)!=="OK"){
            flag=false;
        }
        if(validatorInfo(3)!=="OK"){
            flag=false;
        }
        if(validatorInfo(4)!=="OK"){
            flag=false;
        }
        if(confirmPhone!==$("[name=memberPhone]").val()){
            flag=false;
            $("#showErrorMsg").text("手机号变更,请重新生成验证码");
        }else{
            $("#showErrorMsg").text("");
        }
       // console.log(flag);
        if(flag){
            $.ajax({
                type: 'post',
                url: 'modifyPwdByPhone.htm',
                data: $("#sign_form").serialize(),
                async:false,
                success: function (data) {
                    //console.log(data);
                    if (data.success) {
                        $("#showErrorMsg").text("");
                        //alert("修改成功");
                        //$('.pacity').css('display','block');
                        //$('.aboxbg').css('display','block');
                        //$("#verifycodeTip").text("");
                        $('.sign_shade').show();
                        $('.aboxbg').show(200);
                    } else {
                        //$("#verifycodeTip").text(data.message);
                        $("#showErrorMsg").text(data.message);
                        //alert(data.message);
                    }
                }
            });
        }
    });
    $('.aboxbgBtn1').click(function(){
        $('.sign_shade').hide();
        $('.aboxbg').hide(200);
    });

    $("[name=memberPhone]").blur(function(){
        validatorInfo(1);
    });

    $("[name=phoneCode]").blur(function(){
        validatorInfo(2);
    });

    $("[name=memberPassword]").blur(function(){
        validatorInfo(3);
        //validatorInfo(4);
    });

    $("[name=confirmMemberPassword]").blur(function(){
        //validatorInfo(3);
        validatorInfo(4);
    });

    //$("[name=emailCode]").blur(function(){
    //    validatorInfo(5);
    //});

    $("#sureBtnAgain").click(function(){
       // console.log(111);
        location.href = "/";
    });
});
/**
 * 更换验证码图片
 */
function changeCodeImg(){
    $("#emailCodeImg").attr("src",'generateMailValidatorImages.htm?time='+new Date().getTime());
}
/**
 * 验证数据
 */
function validatorFirstData(){
    var emailCode = $("[name=emailCode]").val();
    var validatorMsg = "OK";
    if (emailCode == null || emailCode == "" || emailCode == undefined) {
        validatorMsg = "请输入验证码";
    }
    if (validatorMsg == "OK") {
        $("#verifycodeTip").text("");
        return true;
    } else {
        //alert(validatorMsg);
        $("#verifycodeTip").text(validatorMsg);
        return false;
    }
}

function validatorFirstDataSure(){
    var memberPassword = $("[name=memberPassword]").val();
    var confirmMemberPassword = $("[name=confirmMemberPassword]").val();
    var memberPhone = $("[name=memberPhone]").val();
    var phoneCode = $("[name=phoneCode]").val();
    var validatorMsg = "OK";
    if (memberPhone == null || memberPhone == "" || memberPhone == undefined) {
        validatorMsg = "请输入手机号";
    } else if (phoneCode == null || phoneCode == "" || phoneCode == undefined) {
        validatorMsg = "请输入短信验证码";
    } else if (memberPassword == null || memberPassword == "" || memberPassword == undefined) {
        validatorMsg = "请输入新密码";
    } else if (confirmMemberPassword == null || confirmMemberPassword == "" || confirmMemberPassword == undefined) {
        validatorMsg = "请输入确认密码";
    }  else if (!isMobile(memberPhone)) {
        validatorMsg = "手机号格式不正确";
    } else if (memberPassword.length > 20) {
        validatorMsg = "新密码长度不能超过20位";
    } else if (confirmMemberPassword.length > 20) {
        validatorMsg = "确认密码长度不能超过20位";
    } else if (memberPassword.length < 6) {
        validatorMsg = "新密码长度不能小于6位";
    } else if (confirmMemberPassword.length <6) {
        validatorMsg = "确认密码长度不能小于6位";
    } else if (!/^[A-Za-z0-9]+$/.test(memberPassword)) {
        validatorMsg = "新密码只能输入数字或字母";
    } else if (!/^[A-Za-z0-9]+$/.test(confirmMemberPassword)) {
        validatorMsg = "确认密码只能输入数字或字母";
    }else if(confirmMemberPassword!=memberPassword){
        validatorMsg = "新密码与确认密码不一致";
    }
    if (validatorMsg == "OK") {
        //$("#verifycodeTip").text("");
        return true;
    } else {
        alert(validatorMsg);
        //$("#verifycodeTip").text(validatorMsg);
        return false;
    }
}
/**
 * 隐藏验证码弹框
 */
function hideCodeDialog(flag){
    if(flag){
        $('.sign_shade').hide();
        $('.sign_vfct').hide(200)
    }else{
        $('.sign_shade').show();
        $('.sign_vfct').show(200)
    }

}
/**
 * 隐藏错误信息
 * @param flag
 */
function hideErrorInfo(flag,field,msg){
    if(flag){
        $("[name='"+field+"']").siblings('.login-wrrong').css('display', 'none');
        $("[name='"+field+"']").siblings('.login-img').attr('src', '../resource/img/retrievepwd/right.png');
        $("[name='"+field+"']").siblings('.login-img').css('display', 'inline-block');
        $("[name='"+field+"']").attr('onoff', 1);
    }else{
        $("[name='"+field+"']").attr('onoff', -1);
        $("[name='"+field+"']").siblings('.login-img').attr('src', '../resource/img/retrievepwd/wrrong.png');
        $("[name='"+field+"']").siblings('.login-img').css('display', 'inline-block');
        $("[name='"+field+"']").siblings('.login-wrrong').text(msg);
        $("[name='"+field+"']").siblings('.login-wrrong').css('display', 'inline-block');
    }
}
/**
 * 验证信息
 * @param flag 1手机号码验证 2短信验证码验证 3新密码验证 4确认密码验证 5弹框中的验证码
 */
function validatorInfo(flag){
    var memberPassword = $("[name=memberPassword]").val();
    var confirmMemberPassword = $("[name=confirmMemberPassword]").val();
    var memberPhone = $("[name=memberPhone]").val();
    var phoneCode = $("[name=phoneCode]").val();
    validatorMsg = "OK";
    if(flag===1){
        if (memberPhone == null || memberPhone == "" || memberPhone == undefined) {
            validatorMsg = "请输入手机号";
        }else if (!isMobile(memberPhone)) {
            validatorMsg = "手机号格式不正确";
        }
        if(validatorMsg == "OK") {
            hideErrorInfo(true,"memberPhone",validatorMsg);
        } else {
            hideErrorInfo(false,"memberPhone",validatorMsg);
        }
    }else if(flag===2){
        if (phoneCode == null || phoneCode == "" || phoneCode == undefined) {
            validatorMsg = "请输入短信验证码";
        }
        if(validatorMsg == "OK") {
            hideErrorInfo(true,"phoneCode",validatorMsg);
        } else {
            hideErrorInfo(false,"phoneCode",validatorMsg);
        }
    }else if(flag===3){
        if (memberPassword == null || memberPassword == "" || memberPassword == undefined) {
            validatorMsg = "请输入新密码";
        }else if (memberPassword.length > 20) {
            validatorMsg = "新密码长度不能超过20位";
        }else if (memberPassword.length < 6) {
            validatorMsg = "新密码长度不能小于6位";
        }else if (!/^[A-Za-z0-9]+$/.test(memberPassword)) {
            validatorMsg = "新密码只能输入数字或字母";
        }
        //else if(confirmMemberPassword!=memberPassword){
        //    validatorMsg = "新密码与确认密码不一致";
        //}
        if(validatorMsg == "OK") {
            hideErrorInfo(true,"memberPassword",validatorMsg);
        } else {
            hideErrorInfo(false,"memberPassword",validatorMsg);
        }
        if(!(confirmMemberPassword == null || confirmMemberPassword == "" || confirmMemberPassword == undefined) && confirmMemberPassword!=memberPassword){
            validatorMsg = "新密码与确认密码不一致";
            hideErrorInfo(false,"confirmMemberPassword",validatorMsg);
        }
    }else if(flag===4){
        if (confirmMemberPassword == null || confirmMemberPassword == "" || confirmMemberPassword == undefined) {
            validatorMsg = "请输入确认密码";
        }else if (confirmMemberPassword.length > 20) {
            validatorMsg = "确认密码长度不能超过20位";
        }else if (confirmMemberPassword.length <6) {
            validatorMsg = "确认密码长度不能小于6位";
        }else if (!/^[A-Za-z0-9]+$/.test(confirmMemberPassword)) {
            validatorMsg = "确认密码只能输入数字或字母";
        }else if(!(memberPassword == null || memberPassword == "" || memberPassword == undefined) && confirmMemberPassword!=memberPassword){
            validatorMsg = "新密码与确认密码不一致";
        }
        if(validatorMsg == "OK") {
            hideErrorInfo(true,"confirmMemberPassword",validatorMsg);
        } else {
            hideErrorInfo(false,"confirmMemberPassword",validatorMsg);
        }
    }else if(flag===5){
        var emailCode = $("[name=emailCode]").val();
        if (emailCode == null || emailCode == "" || emailCode == undefined) {
            validatorMsg="请输入验证码";
            $("#verifycodeTip").text(validatorMsg);
        }else {
            $("#verifycodeTip").text("");
        }
    }
    return validatorMsg;
}

/**
 * 时间倒计时
 * @param divId 要倒计时的DIV组件ID
 * @param num 秒
 */
function timeReduceBySeconds(divId,num){
    var newNum=num;
    var onDiv=$("#"+divId);
    $(onDiv).unbind('click');
    var reduceBySecondsTool={
        reduceBySeconds:function(){
            newNum--;
            $(onDiv).text(newNum+'s ' + '(后重新发送)');
            $(onDiv).css({
                'background': '#cccccc'
            });
            if(newNum == 0) {
                // $(onDiv).attr('yky','abc');
                clearInterval(reduceBySecondsTool.reduceInterval);
                //clearInterval(timer);
                $(onDiv).css({
                    'background': '#0696FA'
                });
                $(onDiv).text('再次发送');

                newNum = num;

                $(onDiv).bind('click', function(){
                    hideCodeDialog(false)
                });
            }

        },
        close: function() {
            //this.shad.css('display', 'none');
            //this.sigvfct.css('display', 'none');
            clearInterval(reduceBySecondsTool.reduceInterval);
            newNum = num;
            $(onDiv).css({
                'background': '#0696fa'
            });
            $(onDiv).text('获取验证码');
            //$('.lgmintd').html('');
        }
    };
    reduceBySecondsTool.reduceInterval=setInterval(reduceBySecondsTool.reduceBySeconds,1000);
}