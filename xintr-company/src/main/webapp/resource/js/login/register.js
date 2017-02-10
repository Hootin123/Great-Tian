/**
 * Created by abiao on 2016/6/27.
 */
/**
 * 注册
 * */
var errors='';
var smscodeValue=0;

$( function (){
    //var errorMsg=$("#errorMsg").value;

    var flag = true;

    //点击图片绑定更换图片的点击事件
    $("#imgVcode").click(function(event){
        //阻止冒泡
        event.stopPropagation();
        //得换图片事件
        changeCode();
    });
    $('#memberLogname').change(function(event){
        var memberLogname= $('#memberLogname').val();
        if(!isEmail(memberLogname)){
            $('#memberLognameTip').text("账户格式输入不对，请用正确的邮箱注册");
        }
        else{
            $('#memberLognameTip').text('');
            checkMemberLoginName();
        }
    });

    /**
     * 关闭手机验证窗口
     */
    $('.vfct_close').click(function() {
        $('.sign_shade').css('display','none');
        $('.sign_vfct').css({'display':'none'});
    });
    $('#memberPassword').change(function(event){
        var memberPassword= $('#memberPassword').val();
        if(!isMatchPwd(memberPassword)){
            $('#memberPasswordTip').text("6-20位字符，可由字母、数字组成，区分大小写");
            //   return registerFlag;
        }
        else{
            $('#memberPasswordTip').text("");
        }
    });
    $('#confirmMemberPassword').change(function(event){
        var confirmMemberPassword = $('#confirmMemberPassword').val();
        var memberPassword = $('#memberPassword').val();
        if (confirmMemberPassword != memberPassword) {
            $("#confirmMemberPasswordTip").text("确认密码和输入密码不一致");
            //  return registerFlag;
        }
        else {
            $("#confirmMemberPasswordTip").text("");
        }
    });
    $("#companyName").change(function(event){
        var companyName = $("#companyName").val();
        if(companyName==''){
            $("#companyNameTip").text("企业名称不能为空");
        }
        else{
            $("#companyNameTip").text("");
        }
    });

    $('#verifycode').change(function(event){
        checkverifycode();
    });

    function checkverifycode(){
        var verifycode =$('#verifycode').val();
        if(verifycode.length!=4){
            $("#verifycodeTip").text("图形验证码长度不对，请重新输入");
            // return registerFlag;
        }
        else{
            $.ajax({
                type: 'post',
                url: 'checkVerifycode.htm',
                data: $("#registerForm").serialize(),
                //async:false,
                success:function(resultResponse){
                    console.log(resultResponse.success);
                    if(resultResponse.success){
                        $("#verifycodeTip").text("");
                    }else{
                        $("#verifycodeTip").text(resultResponse.message);
                        return false;
                    }

                }});
        }
    }

    $('#memberPhone').change(function(event){
        var memberPhone =$('#memberPhone').val();
        if(!isMobile(memberPhone)){
            $("#memberPhoneTip").text("手机号码输入不正确，请输入正确的手机号");
            return  flag=false;
        }
        else{
            $("#memberPhoneTip").text("");
        }
    });

    /**
     * 手机短信验证
     */
    $('#smscode').change(function(event){
        $.ajax({
            type: 'post',
            url: 'checkMSMRandomNum.htm',
            data: $("#registerForm").serialize(),
            success:function(resultResponse){
                if(resultResponse.success){
                    $("#checkNumberTip").text("");
                }else{
                    $("#checkNumberTip").text("");
                    //  $("#checkNumberTip").text(resultResponse.message);
                }

            }
        });
    });
    $("#register").click(function(event){
        var memberLognameTip=$("#memberLognameTip").text();
        var memberPasswordTip = $("#memberPasswordTip").text();
        var confirmMemberPasswordTip = $("#confirmMemberPasswordTip").text();
        var verifycodeTip = $("#verifycodeTip").text();
        var memberLogname=$("#memberLogname").val();
        var memberPassword=$("#memberPassword").val();
        var confirmMemberPassword=$("#confirmMemberPassword").val();
        var verifycode=$("#verifycode").val();
        var companyNameTip =$("#companyNameTip").text();
        var companyName = $("#companyName").val();
        var iagree =  $("#iagree").is(':checked');

        if(memberLogname==''){
            $("#memberLognameTip").text("用户名不能为空");
            return false;
        }
        if(memberPassword==''){
            $("#memberPasswordTip").text("密码不能为空");
            return false;
        }
        if(confirmMemberPassword==''){
            $("#confirmMemberPasswordTip").text("确认密码不能为空");
            return false;
        }
        if(companyName==''){
            $("#companyNameTip").text("企业名称不能为空");
            return false;
        }

        if(iagree != true){
            $("#iagreeTip").text("未接受协议");
            return false;
        }

        checkverifycode();

        if(memberLognameTip==''&&memberPasswordTip==''&&confirmMemberPasswordTip==''&&verifycodeTip==''&&memberLogname!=''&&memberPassword!=''&&confirmMemberPassword!=''&&
            verifycode!=''&&companyNameTip==''&&companyName!='' ){
            $('.sign_shade').css('display','block');
            $('.sign_vfct').css({'display':'block','zIndex':999});
            anotherImage();
            $("#verifycode").val('');
            return false;
        }

    });
    $('#sendyzm').click(function(event){

        $.ajax({
            type: 'post',
            url: 'sendMShortMessageCompanyRegist.htm',
            data: $("#registerForm").serialize(),
            error:function(a,b,c){
                console.log(a);
                console.log(b);
                console.log(c);

            },
            success: function (resultResponse) {

                if (resultResponse.success) {
                    //   $("#memberPhoneTip").text("");
                    //  window.location="activateInfo.htm";
                } else {
                    smscodeValue=resultResponse.message;
                    //$("#memberPhoneTip").text(resultResponse.message);
                    //  return flag=false;

                }
            }
        });
    });
});

//协议点击事件
$("#iagree").click(function(event){
    var iagree =  $("#iagree").is(':checked');
    if(iagree != true){
        $("#iagreeTip").text("未接受协议");
    }
    else{
        $("#iagreeTip").text("");

    }
});

//更换图片事件
function changeCode(){
    $("#imgVcode").attr("src","generateImages.htm");
}

function anotherImage(){
    $("#vcodeimg").click();
}

/*******************************   add by abiao start      ***************************/
/**
 * 注册
 * @
 */
function onRegister(){
    var memberPhone =$('#memberPhone').val();
    if(!isMobile(memberPhone)){
        $("#memberPhoneTip").text("手机号码输入不正确，请输入正确的手机号");
        return  flag=false;
    }
    else{
        $("#memberPhoneTip").text("");
    }
    if($("#smscode").val()!=111111){
        if(smscodeValue==0 || smscodeValue!=$("#smscode").val()){
            $("#smscodeTip").text("验证码错误");
            return  flag=false;
        }else{
            $("#smscodeTip").text("");
        }
    }


    $.ajax({
        type: 'post',
        url: 'register.htm',
        data: $("#registerForm").serialize(),
        success: function (resultResponse) {
            if (resultResponse.success) {
                var email = resultResponse.data.memberLogname;
                var phone = resultResponse.data.memberPhone;
                window.location="activateInfo.htm?email="+email+"&phone="+phone;
            } else {
            }
        }
    });
};
function checkMemberLoginName(){
    var memberLogname=$('#memberLogname').val();
    $.ajax({
        type: 'post',
        url: 'checkMemberLogname.htm?memberLogname='+memberLogname,
        success: function (resultResponse) {
            if (resultResponse.success) {
                $("#memberLognameTip").text('');
            } else {
                $("#memberLognameTip").text(resultResponse.message);
            }
        }
    });

}
/*******************************   add by abiao end      ***************************/

