$(function () {


});

/**
 * 提交前数据验证
 */
function checkSubmit() {
    if($('.bankImport1').val()==""){
        alert("请输入银行开户名");
        return false;
    } else if($('.bankImport2').val()==""){
        alert("请输入银行名");
        return false;
    } else if($('.bankImport3').val()==""){
        alert("请输入对公银行账号");
        return false;
    } else if($('.bankImport5').val()==""){
        alert("请输入联行号");
        return false;
    } else if($('.bankImport4').val()==""){
        alert("请输入开户支行名称号");
        return false;
    }
    var flag = false;
    flag=true;
    return flag;
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
