

$(function(){

    var  fimg =false;

//上传身份证正面
    $("#customerFrontFile").on('change',uploadLogoImg);


//上传身份证反面
    $("#customerBackFile").on('change',uploadOrganizationImg);


})


//上传身份证正面
function uploadLogoImg(){
    var a = BASE_PATH+ "/resource/img/h5wallet/load.gif";
    $("#uploadFront").attr('src',a);
    $.ajaxFileUpload({
        url:BASE_PATH+'/wechatCustomer/customerBindUpload.htm', //用于文件上传的服务器端请求地址
        fileElementId: 'customerFrontFile', //文件上传域的ID
        dataType: 'json', //返回值类型 一般设置为json
        success: function (data, status){
            if(data.success) {
                $("#uploadFront").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
                $("[name=customerIdcardImgFront]").val(data.data);
                //console.log(data.data);
                // $(".hint").text("");
                // $(".hint").css("display","none");
                fimg=true;

            }else{
                $("#uploadFront").attr('src',BASE_PATH+"/resource/img/wechatCustomerNew/error.png");
                alert(data.message)
                // $(".hint").text(data.message);
                // $(".hint").css("display","block");
                fimg=false;
            }
            $("#customerFrontFile").unbind('change').bind('change',uploadLogoImg);
        },
        error: function (data, status, e){
            $("#uploadFront").attr('src',BASE_PATH+"/resource/img/wechatCustomerNew/error.png");
            alert("上传失败");
            // $(".hint").text("上传失败");
            // $(".hint").css("display","block");
            fimg=false;
            $("#customerFrontFile").unbind('change').bind('change',uploadLogoImg);
        }
    });
}

//上传身份证反面
function uploadOrganizationImg(){
    var a = BASE_PATH+ "/resource/img/h5wallet/load.gif";
    $("#uploadBack").attr('src',a);
    $.ajaxFileUpload({
        url:  BASE_PATH+'/wechatCustomer/customerBindUpload.htm', //用于文件上传的服务器端请求地址
        fileElementId: 'customerBackFile', //文件上传域的ID
        dataType: 'json', //返回值类型 一般设置为json
        success: function (data, status){
            if(data.success) {
                $("#uploadBack").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
                $("[name=customerIdcardImgBack]").val(data.data);
                //console.log(data.data);
                // $(".hint").text("");
                // $(".hint").css("display","none");
                fimg=true;
            }else{
                $("#uploadFront").attr('src',BASE_PATH+"/resource/img/wechatCustomerNew/error.png");
                alert(data.message);
                // $(".hint").text(data.message);
                // $(".hint").css("display","block");
                fimg=false;
            }
            $("#customerBackFile").unbind('change').bind('change',uploadOrganizationImg);
        },
        error: function (data, status, e){
            $("#uploadFront").attr('src',BASE_PATH+"/resource/img/wechatCustomerNew/error.png");
            // $(".hint").text("上传失败");
            // $(".hint").css("display","block");
            alert("上传失败")
            fimg=false;
            $("#customerBackFile").unbind('change').bind('change',uploadOrganizationImg);
        }
    });
}
