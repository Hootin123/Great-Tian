$(function(){
    var orderGjjDetail=$("#orderGjjDetail").text()==undefined?'':$("#orderGjjDetail").text();
    var html='';
    if(orderGjjDetail!='' && orderGjjDetail!='{}'){
        // orderGjjDetail="{"+orderGjjDetail+"}";
        console.log(orderGjjDetail);
        var detailGjjJSON=JSON.parse(orderGjjDetail);
        console.log(detailGjjJSON);
        for(var j in detailGjjJSON){
            html += "<p>";
            html += j+":";
            html += "<span>";
            html += detailGjjJSON[j];
            html += "</span>";
            html += "人";
            html += "</p>";
        }
    }
    $("#gjjTdDtail").html(html);

    var orderShebaoDetail=$("#orderSbDetail").text()==undefined?'':$("#orderSbDetail").text();
    var html='';
    if(orderShebaoDetail!='' && orderShebaoDetail!='{}'){
        // orderShebaoDetail="{"+orderShebaoDetail+"}";
        console.log(orderShebaoDetail);
        var detailShebaoJSON=JSON.parse(orderShebaoDetail);
        console.log(detailShebaoJSON);
        for(var j in detailShebaoJSON){
            html += "<p>";
            html += j+":";
            html += "<span>";
            html += detailShebaoJSON[j];
            html += "</span>";
            html += "人";
            html += "</p>";
        }
    }
    $("#shebaoTdDtail").html(html);

    $("#closeDetailBtn").click(function(){
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
    });

    $("#cancelGoPayBtn").click(function(){
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
    });

    $("#cancelSurePayBtn").click(function(){
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
    });

    $(".footerBtn3").click(function(){
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
    });

    //还需支付额度
    var rechargeRealAmount=parseFloat($("[name='rechargeMoney']").val()!=undefined && $("[name='rechargeMoney']").val()!=''?$("[name='rechargeMoney']").val():'0');
    //当前可用余额
    var cashAmout=parseFloat($("#cashAmout").text()!=undefined && $("#cashAmout").text()!=''?$("#cashAmout").text():'0');redWalletAmount
    var rechargePaymentAmount=parseFloat($("#rechargePaymentAmount").text()!=undefined && $("#rechargePaymentAmount").text()!=''?$("#rechargePaymentAmount").text():'0');
    var redWalletAmount=parseFloat($("#redWalletAmount").text()!=undefined && $("#redWalletAmount").text()!=''?$("#redWalletAmount").text():'0');



    // $('.gpfdPaymentBtn1').click(function(){
    //     // $('.gpfdPay').hide();
    //     $('.indentPay').show();
    //     if(rechargeRealAmount>cashAmout){//如果余额不足或者帐户异常,则直接显示银行汇款
    //         $(".radio1").attr("checked","");
    //         $(".radio2").attr("checked","checked");
    //         $('.bankRemit').show();
    //         $('.accountPay').hide();
    //     }
    // });
    //点击前去付款按钮
    $('.footerBtn1').click(function(){
        $('.indentPay').show();
        $('.indent').show();
        $('.bankRemit').hide();
        $('.footer').hide();
        if(rechargeRealAmount>cashAmout){//如果余额不足或者帐户异常,则直接显示银行汇款
            $(".radio1").attr("checked","");
            $(".radio2").attr("checked","checked");
            $('.bankRemit').show();
            $('.accountPay').hide();
        }
    })
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

    //确认付款
    $("#surePayBtn").click(function(){
        if($('.radio1').is(":checked")){
            if(rechargeRealAmount>cashAmout){
                showWarning("余额不足");
            }else{
                $.ajax({
                    type: 'post',
                    url: BASE_PATH+'/account/shebaoOrderDebit.htm',
                    data: {'companyRechargeId': $('#rechargeId').val(),'rechargeRealAmount':rechargeRealAmount,'canUserCashAmount':cashAmout,'rechargePaymentAmount':rechargePaymentAmount,'redWalletAmount':redWalletAmount},
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
                            //     $("#mainlful", window.parent.parent.parent.document).find("li[data-url='/account/index.htm']").click();
                            //     var index = parent.parent.layer.getFrameIndex(parent.window.name); //获取窗口索引
                            //     parent.parent.layer.close(index);
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
                url: BASE_PATH+'/account/shebaoRecharge.htm',
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
                        //     $("#mainlful", window.parent.parent.parent.document).find("li[data-url='/account/index.htm']").click();
                        //     var index = parent.parent.layer.getFrameIndex(parent.window.name); //获取窗口索引
                        //     parent.parent.layer.close(index);
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

function showRedDetail(){
    location.href = BASE_PATH+"/red_list.htm";
}