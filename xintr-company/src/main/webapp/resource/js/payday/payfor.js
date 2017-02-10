// var msgCode=0;
$(function () {
    //文件上传
    onUpload();
    uploadlogo();
    var aInp = $('.contRight input');
    var aLi = $('.conRight-list li');
    var oAup = $('.tesfile');
    var phonereg = RegExp("^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\\d{8}$");
    var  Num=RegExp("^\\d{15}$");
    var  Num2=RegExp("^\\d{18}$");

    $('#sendyzm').click(function(event){

        $.ajax({
            type: 'post',
            url: 'sendpayformessage.htm',
            data: $("#payforForm").serialize(),
            error:function(a,b,c){


            },
            success: function (resultResponse) {

                if (resultResponse.success) {
                    msgCode=resultResponse.message;
                } else {

                }
            }
        });
    });


/**
 * 提交前数据验证
 */
function checkSubmit() {
    $.ajax({
        type:"POST",
        url:"payforsave.htm?paytype="+paytypeValue,
        dataType:"json",
        data: $("#payforForm").serialize(),
        async: true,
        success: function(data){
            if (data.success) {
                document.getElementById("payforNext").style.display = "block";
            }
        }
    });
}

/**
 * 提交前数据验证
 */
function getEditSubmit() {
    $.ajax({
        type:"POST",
        url:"payforsave.htm?paytype=1",
        dataType:"json",
        data: $("#payforForm").serialize(),
        async: true,
        success: function(data){
            if (data.success) {
                showSuccess("更新成功","");
                // $("#popupbox",window.parent.document).load('payforInfo.htm?type=info',function () {
                //
                //     popupboxclosed($('.popupbox'))
                //
                // });
                // $(".topText [name='auditStatusDesc1']",window.parent.document).html("（");
                // $(".topText [name='auditStatusDesc']",window.parent.document).html("资料审核中");
                // $(".topText [name='auditStatusDesc2']",window.parent.document).html("）");
            }else{
                showError('更新设置失败!',"");
            }
        }
    });
}

/**
 文件上传
 */
function onUpload() {
    //事件绑定
    $("#upload").dropzone(
        {
            url: "payfor/upload.htm?type=0&fileType=-1",
            maxFiles: 10,
            maxFilesize: 512,
            acceptedFiles: ".jpg,.jpeg,.png,.bmp",
            addedfile: function (file) {
                //更新文件上传进度
                $('.bar_1').css('width', 10);
                $('.load').show()
            },
            complete: function (data) {
                try {
                    var res = eval('(' + data.xhr.responseText + ')');
                    if (res.success) {
                        $("#companyOrganizationImgUrl").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+res.message);
                        $("#companyOrganizationImg").val(res.message);
                        $('.load').hide()
                    } else {
                        //上传失败
                        $("#uploadTip").text('上传失败');
                    }
                } catch (failed) {
                    $('.tesfile').eq(1).parent().next('.error').fadeIn();
                }
            }
        });
}

function uploadlogo(){

    $("#uploadlogo").dropzone(
        {
            url: "payfor/upload.htm?type=0&fileType=-1",
            maxFiles: 10,
            maxFilesize: 512,
            acceptedFiles: ".jpg,.jpeg,.png,.bmp",
            addedfile: function (file) {

            //更新文件上传进度
                $('.bar_1').css('width', 10);
                $('.logoLoad').show()
            },
            complete: function (data) {
                try {
                    var res = eval('(' + data.xhr.responseText + ')');
                    if (res.success) {
                        $("#companyLogoImage").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+res.message);
                        parent.window.document.getElementById("companyLogoId").setAttribute("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+res.message);
                        $("#companyLogo").val(res.message);
                        $('.logoLoad').hide()
                    } else {
                        //上传失败

                        $("#uploadTip").text('上传失败');
                    }
                } catch (failed) {

                    // $('.tesfile').eq(0).parent().next('.error').fadeIn();

                }
            }
        });
}

/*点击取消错误提示*span最后一个*/

    $('.identity-span').eq(2).click(function(){
        hidenWrrong.call(this)
    })

    for(var i = 0; i < aInp.length; i++) {
        aInp.eq(i).attr('index', i);
    }

    for(var j = 0;j<oAup.length;j++){
        oAup.eq(j).attr('index',i);
    }

    oMborder(aInp);
    oAup.click(function () {
        hidenWrrong.call(this);
    })
    aInp.focus(function() {
        hidenWrrong.call(this);
    })
    aInp.eq(4).blur(function() {
        oIname.call(this, [true, "$(this).val().length == 15 || $(this).val().length == 18"]);
    })
    aInp.eq(1).blur(function() {
        oIname.call(this, [true, "phonereg.test($(this).val())"]);
    })

    //提交按钮判断
    $('.obtnsubmit').click(function() {


        oRemind(function(){
            checkSubmit();
        });

    })

    //提交按钮判断
    $('.obtnsubmit1').click(function() {

        oRemind(function(){
            getEditSubmit();
        });

    })


    function  oRemind(endfun) {
        for(var j = 0; j < aInp.length; j++) {
            if(j == 6 || j == 7 || j == 8 || j == 10){
                continue;
            }
            if(j == 1) {
                oIname.call(aInp.eq(j), [true, "phonereg.test($(this).val())"]);
            } else {
                oIname.call(aInp.eq(j), [true, "$(this).val()!=''"]);
            }
            if(j == 4) {
                oIname.call(aInp.eq(j), [true, "$(this).val().length == 15 || $(this).val().length == 18"]);
            } else {
                oIname.call(aInp.eq(j), [true, "$(this).val()!=''"]);
            }
        }

        oIname.call( $('.identity-span').eq(2), [true, "$(this).html()!=''"]);

        for(var i = 0; i < aInp.length; i++) {

            if(j == 6 || j == 7 || j == 8 || j == 10){
                continue;
            }


            if(aInp.eq(i).attr('onoff') == -1) {
                return false;
            }
        }

        endfun()

    }

    //空判断
    function oIname(judgement) {

        if(eval(judgement[0])) {
            if(eval(judgement[1])) {

                $(this).attr('onoff', 1);

            } else {
                showWrrong.call(this);
                $(this).attr('onoff', -1)
            }
        }

    }

    //框变色
    oMborder(aInp);
    function oMborder(elem) {
        elem.focus(function() {
            aLi.removeClass('mouseactive mousetopactive mousebtomactive');
            aInp.attr('data-color', -1);
            $(this).attr('data-color', 1);
            iColor.call(this)
        })
        elem.mouseover(function() {
            iColor.call(this)

        })
        elem.mouseout(function() {
            if($(this).attr('data-color') != 1) {

                if($(this).attr('index') == aInp.length - 1) {
                    aLi.eq($(this).attr('index')).removeClass('mousebtomactive');
                } else {
                    aLi.eq($(this).attr('index')).removeClass('mouseactive');
                    aLi.eq(parseInt($(this).attr('index')) + 1).removeClass('mousetopactive');
                }

            }
        })
        elem.blur(function() {
            aLi.removeClass('mouseactive mousetopactive mousebtomactive');
            aInp.attr('data-color', -1);
        })

        function iColor() {

            if($(this).attr('index') == aInp.length - 1) {
                aLi.eq($(this).attr('index')).addClass('mousebtomactive');
            } else {
                aLi.eq($(this).attr('index')).addClass('mouseactive');
                aLi.eq(parseInt($(this).attr('index')) + 1).addClass('mousetopactive');

            }
        }

    }

    //提示错误

    function showWrrong() {
        $(this).next('.error').fadeIn();
        $(this).parent().next('.error').fadeIn();
    }

    function hidenWrrong() {
        $(this).next('.error').fadeOut();
        $(this).parent().next('.error').fadeOut();
    }
});