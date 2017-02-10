$(function(){

    var fphone = false,fphoneCode=false;
    //验证手机号
    $('.phone input').blur(function(){
        testPhone()
    });

    function testPhone(){
        var  reg=/^1[3|5|7|8]\d{9}$/;
        if($(".phone input").val()=='' || $(".phone input").val()==undefined ){
            $('.phone b').html('请输入手机号码');
            $('.phone i').removeClass('show');
            fphone = false;
        }
        else if(reg.test($(".phone input").val())){
            $('.phone b').html('');
            $('.phone i').addClass('show');
            fphone = true;
        }
        else{
            $('.phone b').html('请输入正确的手机号');
            $('.phone i').removeClass('show');
            fphone = false;
        }
    }
    //手机验证码
    function testPhoneCode(){
        var code = $("input[name='phoneCode']").val();
        if(code==""||code==null||code==undefined){
            //请输入验证码
            $(".hint").show().html("请输入验证码");
            fphoneCode=false;
        }else{
            $(".hint").hide().html("");
            fphoneCode=true;
        }
    }



    //..........手机验证码
    var timer;
    var getCode=true;

    $('.phone span').on('click',function(){
        var path =  BASE_PATH+'/wechatCustomerBind/sendPhoneMsg.htm';
        if(path.indexOf("jsessionid")>0){
            path =path.substr(0,path.indexOf(";"))+'/wechatCustomerBind/sendPhoneMsg.htm';
        }
        testPhone();
        if(fphone&&getCode){
            // $('#bindImg').show();
            // $('.phone span').html('发送中')
            $.ajax({
                type: 'post',
                url: path,
                data: {'memberPhone':$("input[name='phone']").val()},
                dataType:'json',
                async:false,
                success: function (data) {
                    console.log(data);
                    if (data.success) {
                        $(".hint").text("");
                        $(".hint").css("display","none");

                        console.log(getCode);
                        fphonecode=true;
                        getCode=false;

                        var num=60;
                        $('.phone span').html(num+'s');
                        timer=setInterval(function(){
                            num--;
                            // $('#bindImg').hide();
                            $('.phone span').html(num+'s');
                            if(num==0){
                                clearInterval(timer);
                                $('.phone span').html('获取验证码');
                                getCode=true;
                            }
                        },1000);

                    } else {
                        // $('#bindImg').hide();
                        $(".hint").text(data.message);
                        $(".hint").css("display","block");
                        fphoneCode=true;
                    }
                }
            });

        }
    });





    //员工绑定
    $('.bindPhone').on('click',function(){
        testPhone();
        testPhoneCode();
        if(fphone&&fphoneCode){
              //员工绑定
            $.ajax({
                url:BASE_PATH+'/wechatCustomerBind/customerBind.htm',
                type:'post',
                dataType:'json',
                data:$('#bindForm').serialize(),
                async:false,
                success:function(data){
                    if(data.success){
                        layer.alert("绑定成功");
                        //跳转到首页
                        var customerId = data.data.customerId;
                        var companyId =data.data.companyId;
                        //跳转到员工个人信息页面
                         window.location = BASE_PATH+"/wechatCustomerInfo/toWechatCustomerMainInfoPage.htm?customerId="+customerId+"&companyId="+companyId;
                    }else{
                        layer.alert(data.message);
                    }
                },
                error:function(){
                    layer.alert("网络错误，请刷新");
                }

            });


        }
    })




});


$('li').on('click',function(){
    $(this).find('div').css('height','auto').parent().siblings('li').find('div').css('height','0')
});
