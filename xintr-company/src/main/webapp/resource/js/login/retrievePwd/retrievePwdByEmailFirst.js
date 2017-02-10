$(function(){

    $("#firstNextBtn").click(function(){
        if(validatorFirstData()){
            $.ajax({
                type: 'post',
                url: 'sendEmailByRetrievePwd.htm',
                data: $("#pwdFirstForm").serialize(),
                async:false,
                success: function (data) {
                    console.log(data);
                    if (data.success) {
                        $("#verifycodeTip").text("");
                        var memberEmail=data.data.memberEmail;
                        console.log(memberEmail);
                        location.href = "toRetrievePwdSecondPage.htm?loginName="+$("[name=loginName]").val()+"&memberEmail="+memberEmail;
                    } else {
                        $("#verifycodeTip").text(data.message);
                    }
                }
            });
        }

    });

    $("[name=loginName]").blur(function(){
        validatorFirstData();
    });

    $("[name=emailCode]").blur(function(){
        validatorFirstData();
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
    var loginName=$("[name=loginName]").val();
    var emailCode=$("[name=emailCode]").val();
    var validatorMsg="OK";
    if(loginName==null || loginName=="" || loginName==undefined){
        validatorMsg="请输入用户名";
    }else if(emailCode==null || emailCode=="" || emailCode==undefined){
        validatorMsg="请输入图形验证码";
    }else if(loginName.length>20){
        validatorMsg="用户名长度不能超过20位";
    }else if(emailCode.length!=4){
        validatorMsg="验证码长度不是4位";
    }
    if(validatorMsg=="OK"){
        $("#verifycodeTip").text("");
        return true;
    }else{
        $("#verifycodeTip").text(validatorMsg);
        return false;
    }
}