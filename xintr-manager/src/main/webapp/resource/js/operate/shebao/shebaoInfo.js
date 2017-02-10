$(function(){

    //初始化数据
    initData();
});

function initData(){
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
}