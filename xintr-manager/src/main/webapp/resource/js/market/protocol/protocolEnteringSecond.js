$(function(){
    console.log($("[name='companyEmployCount']").val());
    console.log($("[name='companySalaryDivide']").val());
    console.log($("[name='protocolBusinessManager']").val());
    console.log($("[name='protocolCode']").val());
    $("#protocolContractTime").datetimepicker({
        language: 'zh-CN',
        format: "yyyy-mm-dd",
        //format: "yyyy-mm-dd hh:ii",
        startDate:new Date(),
        autoclose: true,
        todayBtn: true,
        minView: 2,
        todayHighlight: true
    });

    $("#protocolExpireTime").datetimepicker({
        language: 'zh-CN',
        format: "yyyy-mm-dd",
        //format: "yyyy-mm-dd hh:ii",
        startDate:new Date(),
        autoclose: true,
        todayBtn: true,
        minView: 2,
        todayHighlight: true
    });

    $("#sureBtn").click(function(){
        //验证协议类型\生效时间\到期时间必填
        if($("[name='protocolContractType']").val()==null || $("[name='protocolContractType']").val()==undefined || $("[name='protocolContractType']").val()==""){
            layer.alert('协议类型不能为空',{title:'提示信息'});
            return false;
        }
        if($("[name='protocolContractTime']").val()==null || $("[name='protocolContractTime']").val()==undefined || $("[name='protocolContractTime']").val()==""){
            layer.alert('生效时间不能为空',{title:'提示信息'});
            return false;
        }
        if($("[name='protocolExpireTime']").val()==null || $("[name='protocolExpireTime']").val()==undefined || $("[name='protocolExpireTime']").val()==""){
            layer.alert('到期时间不能为空',{title:'提示信息'});
            return false;
        }
        //业务联系人手机号格式
        var protocolLinkmanPhone=$("[name='protocolLinkmanPhone']").val();
        if(protocolLinkmanPhone!=null && protocolLinkmanPhone!="" && !(/^(\+\d+)?1[3458]\d{9}$/.test(protocolLinkmanPhone))){
            layer.alert('业务联系人手机格式不正确',{title:'提示信息'});
            return false;
        }
        $.ajax({
            type: 'post',
            url: 'commitProtocol.htm',
            data: $("#protocolForm").serialize(),
            async:false,
            success: function (data) {
                console.log(data);
                if (data.success) {
                    layer.alert('签约成功',{
                        title:'处理结果上传成功',
                    },function(index){
                        //var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                        //parent.layer.close(index);
                        //var index2 = parent.parent.layer.getFrameIndex(window.name); //获取窗口索引
                        //parent.parent.layer.close(index2);
                        parent.parent.Grid.onQuery();
                        var index = parent.parent.layer.getFrameIndex(parent.window.name); //获取窗口索引
                        parent.parent.layer.close(index);
                    });
                } else {
                    //showTitleInfo("处理结果",data.message);
                    layer.alert(data.message,{title:'处理结果'});
                }
            }
        });
    });
});
/**
 * 取消录入协议二弹出框
 */
function cancelProtocolEnteringSecond(){
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);
}