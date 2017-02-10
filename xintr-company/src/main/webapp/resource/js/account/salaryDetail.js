$(function(){

    //根据订单状态显示不同的内容
    var rechargeState=$("#rechargeState").val();
    var isDedit=$("#isDedit").val();
    if(rechargeState==="0"){
        if(isDedit=="yes"){
            $("#rechargePaymentAmount").text("还需支付：");
            $("#myAccountDeailBtn").text("前去付款");
        }else{
            $("#rechargePaymentAmount").text("还需支付：");
            $("#myAccountDeailBtn").text("确定");
        }

    }else if(rechargeState==="2"){
        $("#rechargePaymentAmount").text("实付款：");
        $("#myAccountDeailBtn").text("确定");
    }
    //前去付款
    $("#myAccountDeailBtn").click(function(){
        console.log(rechargeState);
        if(rechargeState==="0"){
            if(isDedit=="yes"){
                onShowSalaryOrderDetail();
            }else{
                onCancelDialog();
            }
        }else if(rechargeState==="2"){
            onCancelDialog();
        }
    });

    //取消前去付款
    $("#myAccountDeailCancelBtn").click(function(){
        onCancelDialog();
    });

    $("#closeSalaryDetail").click(function(){
        onCancelDialog();
    });
});

/**
 * 订单详情页面
 */
    function onShowSalaryOrderDetail() {
    var url= "../monthlypayroll/salaryOrderDetail.htm?rechargeId="+$("#rechargeId").val();
    var index = layer.open({
        type: 2,
        title: false,
        closeBtn:0,
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: url
    });
    //窗口最大化
    layer.full(index);
}
/**
 * 取消
 */
function onCancelDialog() {
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);
}