$(function () {


});

/**
 * 提交前数据验证
 */
function checkSubmit() {
    if ($('.text1').val() == "") {
        alert("请输入垫付金额");
        return false;
    }
    if(isNaN($('.text1').val())){
        return false;
    }
    if ($('#text3').val() == "") {
        alert("请输入垫付期限");
        return false;
    }

    if ($('.text5').val() == "") {
        alert("请选择日期");
        return false;
    }
    if ($('.text6').val() == "") {
        alert("请输入借款用途");
        return false;
    }

    var str = $('#hello').val();
    str = str.replace(/-/g, "/");
    var endTime = new Date(str);
    var nowTime = new Date();
    var time = endTime.getTime() - nowTime.getTime();
    console.log(time);

    if(endTime.getTime()<=nowTime.getTime()){
        alert("请您重新选择时间");
        return false;
    }

    if ($('#text3').attr('placeholder') == '请输入天数 1~180 天') {
        if ($('#text3').val() > 180) {
            return false;
        } else if(isNaN($('#text3').val())){
            return false;
        }
    } else if ($('#text3').attr('placeholder') == '请输入月数 1~6 个月') {
        if ($('#text3').val() > 6) {
            return false;
        } else if(isNaN($('#text3').val())){
            return false;
        }
    }
    return true;
}

function amountNumber (amount){
    $.ajax({
        type:"POST",
        url:"amountnumber.htm",
        dataType:"json",
        data:{
            'amountNumber':amount
        },
        async: true,
        success: function(data){
        }
    });
}
