$(function(){

    $("#addDepositSureBtn").click(function(){
        //验证
        if($("[name='depositBankName']").val()==null || $("[name='depositBankName']").val()==undefined || $("[name='depositBankName']").val()==""){
            layer.alert('银行不能为空',{title:'提示信息'});
            return false;
        }
        if($("[name='depositBankAccountName']").val()==null || $("[name='depositBankAccountName']").val()==undefined || $("[name='depositBankAccountName']").val()==""){
            layer.alert('账户名称不能为空',{title:'提示信息'});
            return false;
        }
        if($("[name='depositBankNumber']").val()==null || $("[name='depositBankNumber']").val()==undefined || $("[name='depositBankNumber']").val()==""){
            layer.alert('账号不能为空',{title:'提示信息'});
            return false;
        }
        if($("[name='depositSubBankName']").val()==null || $("[name='depositSubBankName']").val()==undefined || $("[name='depositSubBankName']").val()==""){
            layer.alert('开户支行不能为空',{title:'提示信息'});
            return false;
        }
        if(!/^[0-9]{6,30}$/.test($("[name='depositBankNumber']").val())){
            layer.alert('请输入6到30位数字的账号',{title:'提示信息'});
            return false;
        }
        $.ajax({
            type: 'post',
            url: 'createCompanyDeposit.htm',
            data: $("#addDepositForm").serialize(),
            async:false,
            success: function (data) {
                if (data.success) {
                    layer.alert('添加成功',{
                        title:'处理结果',
                        cancel:function(){
                            parent.Grid.onQuery();
                            var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                            parent.layer.close(index);
                        }
                    },function(index){
                        parent.Grid.onQuery();
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
function cancelAddDeposit(){
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);
}