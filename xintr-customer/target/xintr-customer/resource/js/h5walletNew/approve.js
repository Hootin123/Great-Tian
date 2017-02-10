var fimg=false;
$(function(){

    var faddress = false,fphone = false,fname=false,faccount=false;
    //......... 验证公司名称
    $('#compName input').blur(function(){
        testCompName();
    });
    function testCompName(){
        var reg=/^[\w\u4e00-\u9fa5]{4,20}$/;
        if($("#compName input").val()=='' || $("#compName input").val()==undefined ){
            $('#compName b').html('请输入4-20位的公司名称');
            $('#compName em').removeClass('show');
            fname=false;
        }
        else if(reg.test($("#compName input").val())){
            $('#compName b').html('');
            $('#compName em').addClass('show');
            fname=true;
        }
        else{
            $('#compName b').html('请输入正确的公司名称');
            $('#compName em').removeClass('show');
            fname=false;
        }
    }

    //..........验证公司地址
    $('#compAddress input').blur(function(){
        testCompAddress();
    });
    function testCompAddress(){
        var reg=/^[\w\u4e00-\u9fa5]{2,20}$/;
        if($("#compAddress input").val()=='' || $("#compAddress input").val()==undefined ){
            $('#compAddress b').html('请输入公司地址');
            $('#compAddress em').removeClass('show');
            faddress=false;
        }
        else if(reg.test($("#compAddress input").val())){
            $('#compAddress b').html('');
            $('#compAddress em').addClass('show');
            faddress=true;
        }
        else{
            $('#compAddress b').html('请输入正确的公司地址');
            $('#compAddress em').removeClass('show');
            faddress=false;
        }
    }

    //........验证电话号码
    $('#linkPhone input').blur(function(){
        testLinkPhone();
    });

    function testLinkPhone(){
        var  reg=/^1[3|5|7|8]\d{9}$/;
        if($("#linkPhone input").val()=='' || $("#linkPhone input").val()==undefined ){
            $('#linkPhone b').html('请输入手机号码');
            $('#linkPhone em').removeClass('show');
            fphone=false;
        }
        else if(reg.test($("#linkPhone input").val())){
            $('#linkPhone b').html('');
            $('#linkPhone em').addClass('show');
            fphone = true;
        }
        else{
            $('#linkPhone b').html('请输入正确的手机号');
            $('#linkPhone em').removeClass('show');
            fphone=false;
        }
    }
//.............验证营业执照

    $('#busNum input').blur(function(){
        testBusNum();
    });

    function testBusNum(){
        if($("#busNum input").val()=='' || $("#busNum input").val()==undefined ){
            $('#busNum b').html('请输入营业执照');
            $('#busNum em').removeClass('show');
            faccount=false;
        }
        else if($("#busNum input").val().length>=15 && $("#busNum input").val().length<=20){
            $('#busNum b').html('');
            $('#busNum em').addClass('show');
            faccount = true;
        }
        else{
            $('#busNum b').html('请输入正确的营业执照');
            $('#busNum em').removeClass('show');
            faccount=false;
        }
    }
    //验证上传图片
    function testImg(){
        if($("[name=companyOrganizationImg]").val()=='' || $("[name=companyOrganizationImg]").val()==undefined ){
            $(".hint").text("请上传营业执照");
            $(".hint").css("display","block");
            fimg=false;
        }else{
            $(".hint").text("");
            $(".hint").css("display","none");
            fimg=true;
        }
    }
    $(".sub").on("click",function(){
        testCompName();
        testCompAddress();
        testLinkPhone();
        testBusNum();
        testImg();
        if(faddress & fphone & fname & faccount & fimg){
            $.ajax({
                type: 'post',
                url: BASE_PATH+ '/h5walletNew/h5Approve.htm',
                data: $("#approveForm").serialize(),
                async:false,
                success: function (data) {
                    console.log(data);
                    if (data.success) {
                        var companyId=data.data;
                        console.log(companyId);
                        window.location.href = BASE_PATH+"/h5walletNew/toH5walletApproveWaitPage.htm?companyId="+companyId;
                    } else {
                        alert(data.message);
                    }
                }
            });
        }
    });
});
