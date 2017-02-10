$(function(){
    var  fname = false, fsex=false,faddress = false ,fnation = false , fstation = false,fres = false,fschool = false, fmajor = false;
    var frelationname=false,frelationphone=false;
    //.........table切换
    $('.top li').on('click',function(){
        $(this).addClass('active').siblings().removeClass('active')
    });
    $('.base').on('click',function(){
        $('.EditBase').show();
        $('.EditStation').hide();
        $('.EditPersonal').hide();
    });

    $('.station').on('click',function(){
        $('.EditBase').hide();
        $('.EditStation').show();
        $('.EditPersonal').hide();
    });
    $('.personal').on('click',function(){
        $('.EditBase').hide();
        $('.EditStation').hide();
        $('.EditPersonal').show();
    });


    // 验证英文名
    $('.EnglishName input').blur(function(){
        testEnglishName();
    });

    function testEnglishName(){
        var  reg=/^[\w]{0,50}$/;
        if(reg.test($(".EnglishName input").val())){
            $('.EnglishName span').html('').removeClass('error').hide();
            fname = true;
        }
        else{
            $('.EnglishName span').html('请输入正确的英文名').addClass('error').show();
            fname = false;
        }
    }
    // 验证性别
    $('.sex select').change(function(){
        testSex();
    });
    function testSex(){
        if($('.sex select').val()==''){
            $('.sex span').html('请输入性别').addClass('error').show();
            fsex=false;
        }else{
            $('.sex span').html('').removeClass('error');
            fsex=true;

        }

    }


    // 确认修改(基本信息)
    $('#baseInfoBtn').on('click',function(){
        console.log("baseinfoEdit");
        testEnglishName();
        testSex();
        if(fname&&fsex){
            $.ajax({
                type: 'post',
                url: BASE_PATH+'/wechatCustomerInfo/modifyEditBaseInfo.htm',
                async:false,
                data:$("#editBaseForm").serialize(),
                success: function (resultResponse) {
                    console.log(resultResponse);
                    if (resultResponse.success) {
                        layer.alert('修改成功');
                        console.log(BASE_PATH);
                                //去首页
                    } else {
                        layer.alert(resultResponse.message);
                    }
                }
            });
        }
    });

    //验证紧急联系人
    $('.linkman input').blur(function(){
        testLinkman();
    });
    function testLinkman(){
        if($('.linkman input').val()=='' || $('.linkman input').val()==undefined){
            $('.linkman span').html('请输入紧急联系人').addClass('error').show();
            frelationname=false;
        }else if($('.linkman input').val().length>30){
            $('.linkman span').html('最多30个字符').addClass('error').show();
            frelationname=false;
        }else{
            $('.linkman span').html('').removeClass('error');
            frelationname=true;
        }
    }

    //验证紧急联系人电话
    $('.emphone input').blur(function(){
        testEmphone();
    });
    function testEmphone(){
        if($('.emphone input').val()=='' || $('.emphone input').val()==undefined){
            $('.emphone span').html('请输入紧急联系人').addClass('error').show();
            frelationphone=false;
        }else if($('.emphone input').val().length>20 || $('.emphone input').val().length<7){
            $('.emphone span').html('只能输入7~20个字符').addClass('error').show();
            frelationphone=false;
        }else{
            $('.emphone span').html('').removeClass('error');
            frelationphone=true;
        }
    }

    // 验证户籍所在地
    $('.address input').blur(function(){
        testAddress();
    });

    function testAddress(){
        if($(".address input").val()=='' || $(".address input").val()==undefined ){
            $('.address span').html('请输入户籍地址').addClass('error').show();
            faddress = false;
        }
        else if($(".address input").val().length>=2 && $(".address input").val().length<=50){
            $('.address span').html('').removeClass('error').hide();
            faddress = true;
        }
        else{
            $('.address span').html('请输入正确的地址').addClass('error').show();
            faddress = false;
        }
    }
    // 验证民族
    $('.nation input').blur(function(){
        testNation();
    });
    function testNation(){
        if($(".nation input").val().length>=0 && $(".nation input").val().length<=20){
            $('.nation span').html('').removeClass('error').hide();
            fnation = true;
        }
        else{
            $('.nation span').html('请输入正确的民族').addClass('error').show();
            fnation = false;
        }
    }
    // 验证政治面貌
    $('.politicsStatus input').blur(function(){
        testPoliticsStatus();
    });
    function testPoliticsStatus(){
        if($(".politicsStatus input").val().length>=0 && $(".politicsStatus input").val().length<=20){
            $('.politicsStatus span').html('').removeClass('error').hide();
            fstation = true;
        }
        else{
            $('.politicsStatus span').html('请输入正确的政治面貌').addClass('error').show();
            fstation = false;
        }
    }
    // 验证居住地
    $('.residence input').blur(function(){
        testResidence();
    });
    function testResidence(){
        if($(".residence input").val().length>=0 && $(".residence input").val().length<=50){
            $('.residence span').html('').removeClass('error').hide();
            fres = true;
        }
        else{
            $('.residence span').html('请输入正确的地址').addClass('error').show();
            fres = false;
        }
    }
    // 验证学校
    $('.school input').blur(function(){
        testSchool();
    });
    function testSchool(){
        if($(".school input").val().length>=0 && $(".school input").val().length<=50){
            $('.school span').html('').removeClass('error').hide();
            fschool = true;
        }
        else{
            $('.school span').html('请输入正确的学校').addClass('error').show();
            fschool = false;
        }
    }
    // 验证专业
    $('.major input').blur(function(){
        testMajor();
    });
    function testMajor(){
        if($(".major input").val().length>=0 && $(".major input").val().length<=50){
            $('.major span').html('').removeClass('error').hide();
            fmajor = true;
        }
        else{
            $('.major span').html('请输入正确的专业').addClass('error').show();
            fmajor = false;
        }
    }

    // 确认修改2(个人信息)
    $('#editPersonalBtn').on('click',function(){
        testLinkman();
        testEmphone();
        testAddress();
        testNation();
        testPoliticsStatus();
        testResidence();
        testSchool();
        testMajor();
        if(frelationname && frelationphone && faddress&&fnation&&fstation&&fres&&fschool&&fmajor){
            $.ajax({
                type: 'post',
                url: BASE_PATH+'/wechatCustomerInfo/modifyEditPersonalInfo.htm',
                async:false,
                data:$("#personalForm").serialize(),
                success: function (resultResponse) {
                    console.log(resultResponse);
                    if (resultResponse.success) {
                        layer.alert('修改成功');
                            //window.location.href='../wechatCustomerBind/toCustomerIndex.htm?customerId='+$("#customerId").val()+"&companyId="+$("#companyId").val();

                        //alert('首页');

                    } else {
                        layer.alert(resultResponse.message);
                    }
                }
            });
        }
    })
});