$(function(){
    $("#protocolContractTime").datetimepicker({
        language: 'zh-CN',
        format: "yyyy-mm-dd",
        //format: "yyyy-mm-dd hh:ii",
        //startDate:new Date(),
        autoclose: true,
        todayBtn: true,
        minView: 2,
        todayHighlight: true
    });

    $("#protocolExpireTime").datetimepicker({
        language: 'zh-CN',
        format: "yyyy-mm-dd",
        //format: "yyyy-mm-dd hh:ii",
        //startDate:new Date(),
        autoclose: true,
        todayBtn: true,
        minView: 2,
        todayHighlight: true
    });

    $("#addPtorocolSureBtn").click(function(){
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
        var startTime=new Date($("[name='protocolContractTime']").val().replace(/-/g,   "/"));
        var endTime=new Date($("[name='protocolExpireTime']").val().replace(/-/g,   "/"));
        if(endTime.getTime()<startTime.getTime()){
            layer.alert('到期时间不能在生效时间之前',{title:'提示信息'});
            return false;
        }
        //垫发协议三个比率必填,且不能为0和超过100且最多为两位小数
        var operationShowState=$("#operationShowState").val();
        if(operationShowState=="2"){
            var protocolRate=$("[name=protocolRate]").val();
            var protocolServeRate=$("[name=protocolServeRate]").val();
            var protocolScale=$("[name=protocolScale]").val();
            console.log(operationShowState);
            console.log(parseFloat(operationShowState)>100);
            if(protocolRate==null || protocolRate==undefined || protocolRate==""){
                layer.alert('垫付利率不能为空',{title:'提示信息'});
                return false;
            }
            if(protocolServeRate==null || protocolServeRate==undefined || protocolServeRate==""){
                layer.alert('服务费利率不能为空',{title:'提示信息'});
                return false;
            }
            if(protocolScale==null || protocolScale==undefined || protocolScale==""){
                layer.alert('垫付比例不能为空',{title:'提示信息'});
                return false;
            }
            if(!(/^[0-9]+(.[0-9]{1,2})?$/.test(protocolRate))){
                layer.alert('垫付利率,百分比保留两位小数，如3.25是指3.25%',{title:'提示信息'});
                return false;
            }
            if(!(/^[0-9]+(.[0-9]{1,2})?$/.test(protocolServeRate))){
                layer.alert('服务费利率,百分比保留两位小数，如3.25是指3.25%',{title:'提示信息'});
                return false;
            }
            if(!(/^[0-9]+(.[0-9]{1,2})?$/.test(protocolScale))){
                layer.alert('垫付比例,百分比保留两位小数，如3.25是指3.25%',{title:'提示信息'});
                return false;
            }
            if(parseFloat(protocolRate)<=0 || parseFloat(protocolRate)>100){
                layer.alert('垫付利率不能大于100或者小于等于0',{title:'提示信息'});
                return false;
            }
            if(parseFloat(protocolServeRate)<=0 || parseFloat(protocolServeRate)>100){
                layer.alert('服务费利率不能大于100或者小于等于0',{title:'提示信息'});
                return false;
            }
            if(parseFloat(protocolScale)<=0 || parseFloat(protocolScale)>100){
                layer.alert('垫付比例不能大于100或者小于等于0',{title:'提示信息'});
                return false;
            }
        }

        $.ajax({
            type: 'post',
            url: 'addCompanyProtocol.htm',
            data: $("#addProtocolForm").serialize(),
            async:false,
            success: function (data) {
                console.log(data);
                if (data.success) {
                    layer.alert('签约成功',{
                        title:'处理结果',
                        cancel:function(){
                            parent.addProtocolGrid.onQuery();
                            var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                            parent.layer.close(index);
                        }
                    },function(index){
                        parent.addProtocolGrid.onQuery();
                        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                        parent.layer.close(index);
                    });
                } else {
                    layer.alert(data.message,{title:'处理结果'});
                }
            }
        });
    });
});
/**
 * 取消录入协议二弹出框
 */
function cancelAddProtocol(){
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);
}

/**
 * 企业详情弹出框
 */
function onShowCompanyDetail(companyId) {
    var index = layer.open({
        type: 2,
        title: '企业信息',
        maxmin: true,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: '../companyManage/companyManageDetail.htm?isReturnParam=2&companyId='+companyId
    });
    //窗口最大化
    layer.full(index);
}