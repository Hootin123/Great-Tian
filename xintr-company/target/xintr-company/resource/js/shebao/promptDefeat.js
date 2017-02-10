


$(function(){
    var fusername = false,fsex = false,fphone = false,fcard = false,front = false,fimg=false,imgFront=false,imgBack=false;
    //..验证用户名

    $('.username input').blur(function(){
        testUsername();
    });

    function testUsername(){
        if($(".username input").val()=='' || $(".username input").val()==undefined ){
            $('.username span').html('请输入用户名').addClass('error').show();
            fusername = false;
        }
        else if($(".username input").val().length>=2 && $(".username input").val().length<=16){
            $('.username span').html('').removeClass('error').hide();
            fusername = true;
        }
        else{
            $('.username span').html('请输入正确的用户名！').addClass('error').show();
            fusername = false;
        }
    }

    //...验证性别
    function testSex(){
        if($('.sex input').get(0).checked || $('.sex input').get(1).checked){
            $('.sex span').html('').removeClass('error').hide();
            fsex=true;
        }else{
            $('.sex span').html('请选择性别！').addClass('error').show();
            fsex=false;
        }
    }


    //...验证身份证号码
    $('.card input').blur(function(){
        testCard();
    });
    function testCard(){
        var  reg=/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
        if($(".card input").val()=='' || $(".card input").val()==undefined ){
            $('.card span').html('请输入身份证号码').addClass('error').show();
            fcard = false;
        }
        else if(reg.test($(".card input").val())){
            $('.card span').html('').removeClass('error').hide();
            fcard = true;
        }
        else{
            $('.card span').html('请输入正确的身份证号码！').addClass('error').show();
            fcard = false;
        }
    }
    //....验证手机号

    $('.phone input').blur(function(){
        testPhone();
    });

    function testPhone(){
        var  reg=/^1[3|5|7|8]\d{9}$/;
        if($(".phone input").val()=='' || $(".phone input").val()==undefined ){
            $('.phone span').html('请输入手机号').addClass('error').show();
            fphone = false;
        }
        else if(reg.test($(".phone input").val())){
            $('.phone span').html('').removeClass('error').hide();
            fphone = true;
        }
        else{
            $('.phone span').html('请输入正确的手机号！').addClass('error').show();
            fphone = false;
        }
    }


    //...验证身份证
    function testFront(){
        if($('.front').find("input[type='hidden']").val()=='' || $('.front').find("input[type='hidden']")==undefined){
            $('.front span').html('请添加身份证').addClass('error').show();
            front = false;
        }else {
            $('.front span').html('').removeClass('error').hide();
            front = true;
        }

    }
    //验证身份证正面照片是否上传
    function testImgFront(){
        var customerIdcardImgFront =$("input[name='customerIdcardImgFront']").val();
        if(customerIdcardImgFront==""||customerIdcardImgFront==undefined||customerIdcardImgFront==null){
            imgFront=false;
        }else{
            imgFront=true;
        }

    }
    //验证身份证背面照片是否上传
    function testImgBack(){
        var customerIdcardImgBack =$("input[name='customerIdcardImgBack']").val();
        if(customerIdcardImgBack==""||customerIdcardImgBack==undefined||customerIdcardImgBack==null){
            imgBack=false;
        }else{
            imgBack=true;
        }
    }


    // 重新提交
    $('.submit').on('click',function(){
        testUsername();
        testSex();
        testPhone();
        testCard()
        testFront();
        if(fusername&&fsex&&fphone&&fcard&&front){
              $.ajax({
                  url:BASE_PATH+'/shebao/shebaoApproveSubmit.htm',
                  data:$("#approveForm").serialize(),
                  dataType:'json',
                  type:'post',
                  success:function(data){
                     if(data.success){
                         parent.location.reload();
                       //操作成功
                         var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                         parent.layer.close(index);


                     }else{
                         alert(data.message);
                     }
                  },
                  error:function(){
                      alert("网络错误，请刷新页面")
                  }

              })
        }
    })
    //上传身份证正面
    $("#imgFrontFile").on('change',uploadFrontImg);
    //上传省份证背面
    $("#imgBackFile").on('change',uploadBackImg);


    //关闭页面
    $(".promptDefeatHeader b").click(function(){
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
    });
});


//上传身份证正面
function uploadFrontImg(){
    var a = BASE_PATH+ "/resource/img/load.gif";
    $("#imgFront").attr('src',a);
    $.ajaxFileUpload({
        url:BASE_PATH+'/shebao/shebaoUploadFile.htm', //用于文件上传的服务器端请求地址
        fileElementId: 'imgFrontFile', //文件上传域的ID
        dataType: 'json', //返回值类型 一般设置为json
        success: function (data, status){
            if(data.success) {
                $("#imgFront").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
                $("[name=customerIdcardImgFront]").val(data.data);
                fimg=true;
            }else{
                fimg=false;
                alert(data.message);
            }
            $("#imgFrontFile").unbind('change').bind('change',uploadFrontImg);
        },
        error: function (data, status, e){
            fimg=false;
            $("#imgFrontFile").unbind('change').bind('change',uploadFrontImg);
        }
    });
}

//上传身份证背面
function uploadBackImg(){
    var a = BASE_PATH+ "/resource/img/load.gif";
    $("#imgBack").attr('src',a);
    $.ajaxFileUpload({
        url:  BASE_PATH+'/shebao/shebaoUploadFile.htm', //用于文件上传的服务器端请求地址
        fileElementId: 'imgBackFile', //文件上传域的ID
        dataType: 'json', //返回值类型 一般设置为json
        success: function (data, status){
            if(data.success) {
                $("#imgBack").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
                $("[name=customerIdcardImgBack]").val(data.data);
                fimg=true;
            }else{
                fimg=false;
                alert(data.message);
            }
            $("#imgBackFile").unbind('change').bind('change',uploadBackImg);
        },
        error: function (data, status, e){
            fimg=false;
            $("#imgBackFile").unbind('change').bind('change',uploadBackImg);
        }
    });
}



