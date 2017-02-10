$(function(){

    $("#approveBtn").click(function(){
        $.ajax({
            type: 'post',
            url: 'companyApprove.htm',
            data: {"companyId": $("#companyApproveId").val(), "status": 2},
            async: false,
            success: function (data) {
                console.log(data);
                if (data.success) {
                    layer.alert('审核成功',{
                        title:'审核结果',
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

    $("#noApproveBtn").click(function(){//审核驳回
        var companyAuditRemark=$("[name=companyAuditRemark]").val();
        if(companyAuditRemark==null || companyAuditRemark==undefined || companyAuditRemark==""){
            layer.alert("请输入驳回原因",{title:'处理结果'});
            return false;
        }
        $.ajax({
            type: 'post',
            url: 'companyApprove.htm',
            data: {"companyId": $("#companyApproveId").val(), "status": 1,"companyAuditRemark":companyAuditRemark},
            async: false,
            success: function (data) {
                console.log(data);
                if (data.success) {
                    layer.alert('审核驳回成功',{
                        title:'审核结果',
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