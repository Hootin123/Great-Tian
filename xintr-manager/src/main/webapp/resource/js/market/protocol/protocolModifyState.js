$(function(){

    $("#sureBtn").click(function(){
        $.ajax({
            type: 'post',
            url: 'modifyProtocolState.htm',
            data: $("#protocolModifyForm").serialize(),
            async:false,
            success: function (data) {
                console.log(data);
                if (data.success) {
                    layer.alert('修改成功',{
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
                    //showTitleInfo("处理结果",data.message);
                }
            }
        });
    });
});
/**
 * 取消协议修改弹出框
 */
function cancelProtocolModify(){
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);
}