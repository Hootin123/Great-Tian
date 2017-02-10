$(function(){
    //从我的账户跳过来,则直接显示
    var salaryOrderFlag=$("#salaryOrderFlag").val();
    if(salaryOrderFlag==="myAccount"){
        $('.gpfdPay').hide();
        $('.indentPay').show();
    }
    //还需支付额度
    var rechargeRealAmount=parseFloat($("[name='rechargeMoney']").val()!=undefined && $("[name='rechargeMoney']").val()!=''?$("[name='rechargeMoney']").val():'0');
    //当前可用余额
    var cashAmout=parseFloat($("#cashAmout").text()!=undefined && $("#cashAmout").text()!=''?$("#cashAmout").text():'0');
    var rechargePaymentAmount=parseFloat($("#rechargePaymentAmount").text()!=undefined && $("#rechargePaymentAmount").text()!=''?$("#rechargePaymentAmount").text():'0');
    if(rechargeRealAmount>cashAmout){//如果余额不足或者帐户异常,则直接显示银行汇款
        $(".radio1").attr("checked","");
        $(".radio2").attr("checked","checked");
        $('.bankRemit').show();
        $('.accountPay').hide();
    }
    //点击前去付款按钮
    $('.gpfdPaymentBtn1').click(function(){
        $('.gpfdPay').hide();
        $('.indentPay').show();
    });
    //选择账户余额支付
    $('.radio1').click(function(){
        if($('.radio1').is(":checked")){
            $('.accountPay').show();
            $('.bankRemit').hide();
        }
    });
    //选择银行汇款支付
    $('.radio2').click(function(){
        if($('.radio2').is(":checked")){
            $('.bankRemit').show();
            $('.accountPay').hide();
        }
    });

    //取消按钮
    $(".gpfdPaymentBtn2").click(function(){
        //var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        //parent.layer.close(index);
        //if(salaryOrderFlag=="myAccount"){
        //    parent.allPopClose();
        //}else{
        //    parent.parent.allPopClose();
        //}
        onCancelDialog();
    });
    //取消按钮
    $(".indentBtn2").click(function(){
        //var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        //parent.layer.close(index);
        //parent.parent.allPopClose();
        //if(salaryOrderFlag==="myAccount"){
        //    parent.allPopClose();
        //}else{
        //    parent.parent.allPopClose();
        //}
        onCancelDialog();
    });
    //点击叉号
    $("#closeDetailBtn").click(function(){
        onCancelDialog();
    });


    //确认付款
    $("#surePayBtn").click(function(){
        if($('.radio1').is(":checked")){
            if(rechargeRealAmount>cashAmout){
                showWarning("余额不足");
            }else{
                $.ajax({
                    type: 'post',
                    url: '../monthlypayroll/commitSalaryOrder.htm',
                    data: {'payCycleId': $('#excelId').val(),'rechargeRealAmount':rechargeRealAmount,'canUserCashAmount':cashAmout,'rechargePaymentAmount':rechargePaymentAmount,'rechargeType':$('#rechargeType').val(),'rechargeId':$('#rechargeId').val()},
                    success: function (data) {
                        //跳转到我的账户页面
                        if (data.success) {
                            layer.alert('付款成功',{
                                title:'处理结果',
                                cancel:function(){
                                    $("#mainlful", window.parent.parent.parent.document).find("li[data-url='/account/index.htm']").click();
                                    var index = parent.parent.layer.getFrameIndex(parent.window.name); //获取窗口索引
                                    parent.parent.layer.close(index);
                                }
                            },function(index){
                                $("#mainlful", window.parent.parent.parent.document).find("li[data-url='/account/index.htm']").click();
                                var index = parent.parent.layer.getFrameIndex(parent.window.name); //获取窗口索引
                                parent.parent.layer.close(index);
                            });
                            // showConfirm('付款成功', function () {
                            //     if(salaryOrderFlag=="myAccount"){
                            //         $("#mainlful", window.parent.parent.parent.document).find("li[data-url='/account/index.htm']").click();
                            //         var index = parent.parent.layer.getFrameIndex(parent.window.name); //获取窗口索引
                            //         parent.parent.layer.close(index);
                            //     }else{
                            //         //onCancelDialog();
                            //         $("#mainlful", window.parent.parent.parent.document).find("li[data-url='/account/index.htm']").click();
                            //         var index = parent.parent.layer.getFrameIndex(parent.window.name); //获取窗口索引
                            //         parent.parent.layer.close(index);
                            //     }
                            //
                            // });
                        } else {
                            showWarning(data.message);
                        }
                    }
                });
            }

        }
        if($('.radio2').is(":checked")){
            if($('.tableText1').val()==""){
                $('.tableSpan1').show();
                return false;
            }else{
                if(!bank($('.tableText1').val())){
                    return false;
                }
            }

            if($('.tableText2').val()==""){
                $('.tableSpan2').show();
                return false;
            }else{
                if(!bankAccount($('.tableText2').val())){
                    return false;
                }
            }

            if($('.tableText3').val()==""){
                $('.tableSpan3').show();
                return false;
            }else{
                if(!bankDeal($('.tableText3').val())){
                    return false;
                }
            }

            if($('.tableText4').val()==""){
                $('.tableSpan4').show();
                return false;
            }else{
                if(!bakDeal($('.tableText4').val())){
                    return false;
                }
            }
            $.ajax({
                type: 'post',
                url: '../monthlypayroll/salaryRecharge.htm',
                data: $("#salaryOrderForm").serialize(),
                success: function (data) {
                    console.log("银行汇款");
                    console.log(data);
                    if (data.success) {
                        layer.alert('充值成功',{
                            title:'处理结果',
                            cancel:function(){
                                $("#mainlful", window.parent.parent.parent.document).find("li[data-url='/account/index.htm']").click();
                                var index = parent.parent.layer.getFrameIndex(parent.window.name); //获取窗口索引
                                parent.parent.layer.close(index);
                            }
                        },function(index){
                            $("#mainlful", window.parent.parent.parent.document).find("li[data-url='/account/index.htm']").click();
                            var index = parent.parent.layer.getFrameIndex(parent.window.name); //获取窗口索引
                            parent.parent.layer.close(index);
                        });
                        // showConfirm('充值成功', function () {
                        //     if(salaryOrderFlag=="myAccount"){
                        //         $("#mainlful", window.parent.parent.parent.document).find("li[data-url='/account/index.htm']").click();
                        //         var index = parent.parent.layer.getFrameIndex(parent.window.name); //获取窗口索引
                        //         parent.parent.layer.close(index);
                        //     }else{
                        //         $("#mainlful", window.parent.parent.parent.document).find("li[data-url='/account/index.htm']").click();
                        //         var index = parent.parent.layer.getFrameIndex(parent.window.name); //获取窗口索引
                        //         parent.parent.layer.close(index);
                        //     }
                        //
                        // });
                    } else {
                        showWarning(data.message);
                    }
                }
            });

        }
    });

    //银行名称判断
   $('.tableText1').blur(function(){
       bank($(this).val());
   });
    //银行账号判断
    $('.tableText2').blur(function(){
        bankAccount($(this).val());
    });
    //银行交易流水号
    $('.tableText3').blur(function(){
        bankDeal($(this).val());
    });
    //银行交易流水号
    $('.tableText4').blur(function(){
        bakDeal($(this).val());
    });




    function bank(obj){
        //var reg = /^[\u4e00-\u9fa5]{4,20}$/;
        if(obj.length>20 || obj.length<2){
            $('.tableSpan1').show();
            return false;
        }else{
            $('.tableSpan1').hide();
            return true;
        }
    }

    function bankAccount(obj){
        if(obj.length>20 || obj.length<12){
            $('.tableSpan2').show();
            return false;
        }else{
            $('.tableSpan2').hide();
            return true;
        }
    }
    function bankDeal(obj){
        if(obj.length>20 || obj.length<2){
            $('.tableSpan3').show();
            return false;
        }else{
            $('.tableSpan3').hide();
            return true;
        }
    }

    function bakDeal(obj){
        if(obj.length>50){
            $('.tableSpan4').show();
            return false;
        }else{
            $('.tableSpan4').hide();
            return true;
        }
    }
});
/**
 * 取消
 */
function onCancelDialog() {
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);
}
