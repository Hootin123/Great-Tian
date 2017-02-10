$(function(){
    var faddress = false,fphone = false,fname=false,faccount=false,fimg=false;
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
        var  reg=/^\d{15,20}$/;
        if($("#busNum input").val()=='' || $("#busNum input").val()==undefined ){
            $('#busNum b').html('请输入营业执照');
            $('#busNum em').removeClass('show');
            faccount=false;
        }
        else if(reg.test($("#busNum input").val())){
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

    $("#approveFile").on('change',uploadApproveImg);

    $(".sub").on("click",function(){
        testCompName();
        testCompAddress();
        testLinkPhone();
        testBusNum();
        testImg();
        if(faddress & fphone & fname & faccount & fimg){
            $.ajax({
                type: 'post',
                url: BASE_PATH +'/scanCode/h5ScanApprove.htm',
                data: $("#approveForm").serialize(),
                async:false,
                success: function (data) {
                    console.log(data);
                    if (data.success) {
                        var companyId=data.data;
                        console.log(companyId);
                        //跳转至成功页面
                        window.location.href= BASE_PATH +"/scanCode/toSuccessPage.htm";
                    } else {
                        alert(data.message);
                    }
                }
            });
        }
    });
});
//function uploadApproveImg(){
//    console.log("uploadImg");
//    $.ajaxFileUpload({
//        url: BASE_PATH+'scanCode/h5ScanUploadOrganizeImg.htm', //用于文件上传的服务器端请求地址
//        fileElementId: 'approveFile', //文件上传域的ID
//        dataType: 'json', //返回值类型 一般设置为json
//        success: function (data, status){
//            if(data.success) {
//                $("#upload").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
//                $("[name=companyOrganizationImg]").val(data.data);
//                //console.log(data.data);
//                $(".hint").text("");
//                $(".hint").css("display","none");
//                fimg=true;
//
//            }else{
//                $(".hint").text(data.message);
//                $(".hint").css("display","block");
//                fimg=false;
//            }
//            $("#approveFile").unbind('change').bind('change',uploadApproveImg);
//        },
//        error: function (data, status, e){
//            $(".hint").text("上传失败");
//            $(".hint").css("display","block");
//            fimg=false;
//            $("#approveFile").unbind('change').bind('change',uploadApproveImg);
//        }
//    });
//}

//function changeApproveFile() {
//    var uploadImg = document.getElementById("upload");
//    var file = document.getElementById("approveFile");
//    var ext=file.value.substring(file.value.lastIndexOf(".")+1).toLowerCase();
//    // gif在IE浏览器暂时无法显示
//    if(ext!='png'&&ext!='jpg'&&ext!='jpeg'){
//        alert("图片的格式必须为png或者jpg或者jpeg格式！");
//        return;
//    }
//    var isIE = navigator.userAgent.match(/MSIE/)!= null,
//        isIE6 = navigator.userAgent.match(/MSIE 6.0/)!= null;
//
//    if(isIE) {
//        file.select();
//        var reallocalpath = document.selection.createRange().text;
//
//        // IE6浏览器设置img的src为本地路径可以直接显示图片
//        if (isIE6) {
//            uploadImg.src = reallocalpath;
//        }else {
//            // 非IE6版本的IE由于安全问题直接设置img的src无法显示本地图片，但是可以通过滤镜来实现
//            uploadImg.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image',src=\"" + reallocalpath + "\")";
//            // 设置img的src为base64编码的透明图片 取消显示浏览器默认图片
//            uploadImg.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
//        }
//    }else {
//        html5Reader(file,ext);
//    }
//}
//
//function html5Reader(file,type){
//    var file = file.files[0];
//    var reader = new FileReader();
//    reader.readAsDataURL(file);
//    reader.onload = function(e){
//        var uploadImg = document.getElementById("upload");
//        uploadImg.src=this.result;
//
//        $.ajax({
//            type: 'post',
//            url: BASE_PATH+'/scanCode/queryScanUploadFileName.htm?',
//            data: {"type":type},
//            async:false,
//            success: function (data) {
//                console.log(data);
//                var fileName=data.data;
//                $("[name=companyOrganizationImg]").val(data.data);
//                $(".hint").text("");
//                $(".hint").css("display","none");
//                fimg=true;
//                $.ajaxFileUpload({
//                    url: BASE_PATH+'/scanCode/h5ScanUploadOrganizeImg.htm?fileName='+fileName, //用于文件上传的服务器端请求地址
//                    fileElementId: 'approveFile', //文件上传域的ID
//                    dataType: 'json', //返回值类型 一般设置为json
//                    success: function (data, status){
//                        if(data.success) {
//                            //$("#upload").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
//
//                            //console.log(data.data);
//
//
//                        }else{
//                            $(".hint").text(data.message);
//                            $(".hint").css("display","block");
//                            fimg=false;
//                        }
//                        //$("#approveFile").unbind('change').bind('change',uploadApproveImg);
//                    },
//                    error: function (data, status, e){
//                        $(".hint").text("上传失败");
//                        $(".hint").css("display","block");
//                        fimg=false;
//                        //$("#approveFile").unbind('change').bind('change',uploadApproveImg);
//                    }
//                });
//            }
//        });
//    }
//}

function uploadApproveImg(){
    console.log("uploadImg");
    var a = BASE_PATH+ "/resource/img/h5wallet/load.gif";
    $("#upload").attr('src',a);
    $.ajaxFileUpload({
        url:  BASE_PATH+'/scanCode/h5ScanUploadOrganizeImg.htm', //用于文件上传的服务器端请求地址
        fileElementId: 'approveFile', //文件上传域的ID
        dataType: 'json', //返回值类型 一般设置为json
        success: function (data, status){
            if(data.success) {
                $("#upload").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
                $("[name=companyOrganizationImg]").val(data.data);
                //console.log(data.data);
                $(".hint").text("");
                $(".hint").css("display","none");
                fimg=true;

            }else{
                $(".hint").text(data.message);
                $(".hint").css("display","block");
                fimg=false;
            }
            $("#approveFile").unbind('change').bind('change',uploadApproveImg);
        },
        error: function (data, status, e){
            $(".hint").text("上传失败");
            $(".hint").css("display","block");
            fimg=false;
            $("#approveFile").unbind('change').bind('change',uploadApproveImg);
        }
    });
}