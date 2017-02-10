var fimg=true;
$(function(){
    var  fcard = false,fbanknumber = false,fopenbank = false,flinkman = false,fchart = false,front = false,fbir=false,fname = false,fsex=false;
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
            $('.card span').html('请输入正确的号码').addClass('error').show();
            fcard = false;
        }
    }

    // 英文名
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

    //...验证银行卡号
    $('.banknumber input').blur(function(){
        testBanknumber();
    });

    function testBanknumber(){
        var  reg=/^[0-9]{16,19}$/;
        var banknumber = $(".banknumber input").val().replace(/\s+/g,"");
        if(banknumber=='' || banknumber==undefined ){
            $('.banknumber span').html('请输入工资卡卡号').addClass('error').show();
            fbanknumber = false;
        }
        else if(reg.test(banknumber)){
            $('.banknumber span').html('').removeClass('error').hide();
            fbanknumber = true;
        }
        else{
            $('.banknumber span').html('请输入正确的卡号').addClass('error').show();
            fbanknumber = false;
        }
    }

    //..验证开户行
    $('.openbank input').blur(function(){
        testOpenbank();
    });
    function testOpenbank(){
        var  reg=/^[\u4e00-\u9fa5]{2,16}$/;
        if($(".openbank input").val()=='' || $(".openbank input").val()==undefined ){
            $('.openbank span').html('请输入开户行').addClass('error').show();
            fopenbank = false;
        }
        else if($(".openbank input").val().length>=2 && $(".openbank input").val().length<=30){
            $('.openbank span').html('').removeClass('error').hide();
            fopenbank = true;
        }
        else{
            $('.openbank span').html('请输入正确的开户行').addClass('error').show();
            fopenbank = false;
        }
    }
    //...验证身份证图片
    function testFront(){
        if($('[name=customerIdcardImgFront]').val()=='' || $('[name=customerIdcardImgFront]').val()==undefined || $('[name=customerIdcardImgBack]').val()=='' || $('[name=customerIdcardImgBack]').val()==undefined){
            $('.idCard span').html('请添加身份证').addClass('error').css('top','1.3rem').show();
            front = false;
        }else {
            $('.idCard span').html('').removeClass('error').hide();
            front = true;
        }
    }
    // ...验证生日
    $('.birthday').change(function(){
        testBirthday()
    });
    function testBirthday(){
        if($('.birthday input').val()=='' || $('.birthday input').val()==undefined){
            $('.birthday span').html('请输入生日').addClass('error').show();
            fbir=false;
        }else{
            $('.birthday span').html('').removeClass('error').hide();
            fbir=true;
        }
    }

    //..验证紧急联系人

    $('.linkman input').blur(function(){
        testLinkman();
    });
    function testLinkman(){
        if($(".linkman input").val()=='' || $(".linkman input").val()==undefined ){
            $('.linkman span').html('请输入紧急联系人').addClass('error').show();
            flinkman = false;
        }
        else if($(".linkman input").val().length>=2 && $(".linkman input").val().length<=30){
            $('.linkman span').html('').removeClass('error').hide();
            flinkman = true;
        }
        else{
            $('.linkman span').html('请输入正确的联系人').addClass('error').show();
            flinkman = false;
        }
    }

    //....验证紧急联系人电话

    $('.emphone input').blur(function(){
        testEmPhone();
    });
    function testEmPhone(){
        var  reg=/^[0-9]{7,20}$/;
        if($(".emphone input").val()=='' || $(".emphone input").val()==undefined ){
            $('.emphone span').html('请输入联系电话').addClass('error').show();
            femphone = false;
        }
        else if(reg.test($(".emphone input").val())){
            $('.emphone span').html('').removeClass('error').hide();
            femphone = true;
        }
        else{
            $('.emphone span').html('请输入正确的电话').addClass('error').show();
            femphone = false;
        }
    }

    //..验证户籍地址

    $('.chart input').blur(function(){
        testChart();
    });

    function testChart(){
        var  reg=/^[\u4e00-\u9fa5]{2,16}$/;
        if($(".chart input").val()=='' || $(".chart input").val()==undefined ){
            $('.chart span').html('请输入户籍地址').addClass('error').show();
            fchart = false;
        }
        else if($(".chart input").val().length>=2 && $(".chart input").val().length<=50){
            $('.chart span').html('').removeClass('error').hide();
            fchart = true;
        }
        else{
            $('.chart span').html('请输入正确的地址').addClass('error').show();
            fchart = false;
        }
    }

    // 银行卡号四位加空格

    var t = document.getElementById("banknumber");
    t.onkeydown = change;
    t.onkeyup = change;
    t.onkeypress = change;

    function change() {
        this.value = this.value.replace(/\D/g, '').replace(/....(?!$)/g, '$& ');
    }


    //确认
    $("#wechatSureBtn").on('click',function(){
        testEnglishName();
        testCard();
        testBirthday();
        testBanknumber();
        testOpenbank();
        testLinkman();
        testEmPhone();
        testChart();
        testSex();
        testFront();
        if( fname&&fcard&&fsex&&fbanknumber&&fopenbank&&flinkman&&fchart&&front&&fbir && fimg){
            $.ajax({
                type: 'post',
                url: BASE_PATH+'/wechatCustomerInfo/modifyMainInfo.htm',
                async:false,
                data:$("#wechatMainInfoForm").serialize(),
                success: function (resultResponse) {
                    console.log(resultResponse);
                    if (resultResponse.success) {
                        layer.alert('修改成功',function(){
                            if(resultResponse.data ===1){//跳转到入职规范页面
                                //alert("入职规范");
                                window.location.href='../wechatCustomerBind/toJoinTask.htm?customerId='+$("[name='customerId']").val()+"&companyId="+$("#companyId").val();
                            }else{//跳转到首页
                                //alert("首页");
                                window.location.href='../wechatCustomerBind/toCustomerIndex.htm?customerId='+$("[name='customerId']").val()+"&companyId="+$("#companyId").val();
                            }
                        });

                    } else {
                        layer.alert(resultResponse.message);
                    }
                }
            });
        }

    })

});

/**
 * 上传身份证正反面
 */
function uploadWebchatIdCardImg(type){
    var a ="/resource/img/h5wallet/load.gif";
    var customerIdcardImgUrl="";//原先的图片
    var fileElementId='';
    if(type===1){
        $("#frontIdCard").attr('src',a);
        fileElementId='frontFile';
        customerIdcardImgUrl=$("[name=customerIdcardImgFront]").val();
    }else if(type===2){
        $("#backIdCard").attr('src',a);
        fileElementId='backFile';
        customerIdcardImgUrl=$("[name=customerIdcardImgBack]").val();
    }
    if(customerIdcardImgUrl=='' || customerIdcardImgUrl==undefined){
        customerIdcardImgUrl="/resource/img/wechatCustomer/error.png";
    }else{
        customerIdcardImgUrl="http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+customerIdcardImgUrl;
    }
    var uploadImg = document.getElementById("frontIdCard");
    var file = document.getElementById("frontFile");
    if(type===2){
        uploadImg = document.getElementById("backIdCard");
        file = document.getElementById("backFile");
    }
    var reader = new FileReader();
    reader.readAsDataURL(file.files[0]);
    reader.onload = function(e){
        $.ajaxFileUpload({
            url:  '../wechatCustomerInfo/wechatCustomerUploadIdCard.htm', //用于文件上传的服务器端请求地址
            fileElementId: fileElementId, //文件上传域的ID
            dataType: 'json', //返回值类型 一般设置为json
            success: function (data, status){
                console.log(data);
                if(data.success) {
                    if(type===1){
                        $("[name=customerIdcardImgFront]").val(data.data);
                        //$("#frontIdCard").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
                    }else if(type===2){
                        $("[name=customerIdcardImgBack]").val(data.data);
                        //$("#backIdCard").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
                    }
                    fimg=true;
                    uploadImg.src=reader.result;//显示本地图片
                }else{
                    uploadImg.src=customerIdcardImgUrl;
                    alert(data.message);
                    fimg=false;
                }
            },
            error: function (data, status, e){
                fimg=false;
                uploadImg.src=customerIdcardImgUrl;
                layer.alert("上传图片异常");
            }
        });
    }

}

//function changeUploadWebchatFile(type) {
//    var uploadImg = document.getElementById("frontIdCard");
//    var file = document.getElementById("frontFile");
//    if(type===2){
//        uploadImg = document.getElementById("backIdCard");
//        file = document.getElementById("backFile");
//    }
//    var isIE = navigator.userAgent.match(/MSIE/)!= null,
//        isIE6 = navigator.userAgent.match(/MSIE 6.0/)!= null;
//
//    if(isIE) {//IE浏览器
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
//    }else {//其它浏览器
//        var reader = new FileReader();
//        reader.readAsDataURL(file.files[0]);
//        reader.onload = function(e){
//            uploadImg.src=this.result;//显示本地图片
//        }
//    }
//}
