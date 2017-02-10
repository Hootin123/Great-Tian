$(function(){
    console.log($('#companyIdHid').val());
    //$.ajax({
    //    type: 'post',
    //    url: 'companyCreditSearch.htm',
    //    data: {"companyId":$('#companyIdHid').val()},
    //    //async: false,
    //    success: function (data) {
    //        console.log(data);
    //        if (data.success) {
    //            //$("#verifycodeTip").text("");
    //            //$('.pacity').css('display','block');
    //            //$('.aboxbg').css('display','block');
    //        } else {
    //            //$("#verifycodeTip").text(data.message);
    //        }
    //    }
    //});

    //添加时间控件
    $("#companyOpenEndtime").datetimepicker({
        language: 'zh-CN',
        format: "yyyy-mm-dd",
        //format: "yyyy-mm-dd hh:ii",
        startDate:new Date(),
        autoclose: true,
        todayBtn: true,
        minView: 2,
        todayHighlight: true
    });

    //更改充值通道
    $("[name='companyRechargeChannel']").click(function(){
        var companyRechargeChannelArray=$("[name='companyRechargeChannel']");
        for(var i=0;i<companyRechargeChannelArray.length;i++){
            var companyRechargeChannel=companyRechargeChannelArray[i];
            //console.log(this.value);
            //console.log(companyRechargeChannel.value);
            if(this.value==companyRechargeChannel.value){
                $(this).attr('checked',true);
            }else{
                $(companyRechargeChannel).attr('checked',false);
            }
        }

    });

    //点击下一步按钮
    $("#nextBtn").click(function(){
        //验证
        var companyEmployCount=$("[name='companyEmployCount']").val();
        var companySalaryDivide=$("[name='companySalaryDivide']").val();
        var companyCorporationIdcard=$("[name='companyCorporationIdcard']").val();
        if(companyEmployCount!=null && companyEmployCount!="" && !(/^(0|[1-9][0-9]*)$/.test(companyEmployCount))){
            layer.alert('员工人数只能为整数',{title:'提示信息'});
            return false;
        }
        if(companySalaryDivide!=null && companySalaryDivide!="" && !(/^\d+(\.(\d{1,2}))?$/.test(companySalaryDivide))){
            layer.alert('平均每月发工资金额只能是最多两位小数或整数',{title:'提示信息'});
            return false;
        }
        if(companyCorporationIdcard!=null && companyCorporationIdcard!="" && !(/^[1-9](\d{13,16})([a-zA-Z0-9]{1})$/.test(companyCorporationIdcard))){
            layer.alert('身份证号码格式不正确',{title:'提示信息'});
            return false;
        }
        var companyRechargeChannelValue=0;
        var companyRechargeChannelArray=$("[name='companyRechargeChannel']");
        for(var i=0;i<companyRechargeChannelArray.length;i++){
            var companyRechargeChannel=companyRechargeChannelArray[i];
            if($(companyRechargeChannel).attr('checked')=='checked' ||$(companyRechargeChannel).attr('checked')==true){
                companyRechargeChannelValue=$(companyRechargeChannel).val();
            }
        }
        console.log(companyRechargeChannelValue);
        var formDataStr="{'companyName':'"+$('[name=companyName]').val()
            +"','companyOpenEndtime':'"+$('[name=companyOpenEndtime]').val()
            +"','companyId':'"+$('#companyIdHid').val()
            //+"','companyNumber':'"+$("[name='companyNumber']").val()
            +"','companyNumber':'"+$("[name='companyNumber']").val()
            +"','companyAddress':'"+$("[name='companyAddress']").val()
            +"','companyCorporation':'"+$("[name='companyCorporation']").val()
            +"','companyCorporationIdcard':'"+$("[name='companyCorporationIdcard']").val()
            +"','companyEmployCount':'"+$("[name='companyEmployCount']").val()
            +"','companySalaryDivide':'"+$("[name='companySalaryDivide']").val()
            +"','companyBank':'"+$("[name='companyBank']").val()
            +"','companyBanknumber':'"+$("[name='companyBanknumber']").val()
            +"','companyDepositBankAccountName':'"+$("[name='companyDepositBankAccountName']").val()
            +"','companyDepositBankNo':'"+$("[name='companyDepositBankNo']").val()
            +"','companyBankaddress':'"+$("[name='companyBankaddress']").val()
            +"','companyRechargeChannel':'"+companyRechargeChannelValue
            +"'}";
        //var formDataStr="{'companyName':'"+$('[name=companyName]').val()+"'}";
        var index = layer.open({
            type: 2,
            title: '录入协议二',
            maxmin: true,
            shadeClose: false, //点击遮罩关闭层
            area: ['650px', '380px'],
            //content: 'companyManageDetail.htm?companyId='+companyId
            content: 'toProtocolEnteringSecondPage.htm?formDataStr='+encodeURI(encodeURI(formDataStr))+'&operationShowState='+$('#operationShowStateHid').val()
        });
        //窗口最大化
        layer.full(index);
    });

    //显示征信资料库
    $("#downloadFile").click(function(){
        $.ajax({
            type: 'post',
            url: 'companyCreditSearch.htm',
            data: {"companyId":$('#companyIdHid').val()},
            //data: {"companyId":259},
            //async: false,
            success: function (data) {
                console.log(data);
                if (data.success) {
                    location.href = "downloadFile.htm?fileName="+data.data;
                } else {
                    layer.alert(data.message,{title:'提示信息'});
                }
            }
        });
    });


});
///**
// * 协议录入2弹出框
// */
//function onShowProtocolEnteringSecond(companyId) {
//    var index = layer.open({
//        type: 2,
//        title: '录入协议二',
//        maxmin: true,
//        shadeClose: false, //点击遮罩关闭层
//        area: ['650px', '380px'],
//        //content: 'companyManageDetail.htm?companyId='+companyId
//        content: 'toProtocolEnteringSecondPage.htm'
//    });
//    //窗口最大化
//    layer.full(index);
//}
/**
 * 取消协议录入一弹出框
 */
function cancelProtocolEnteringFirst(){
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);
}
