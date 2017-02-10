$(function(){
    $("[name=dtype]").click(function(){
        if($(this).attr('checked')=='checked' ||$(this).attr('checked')==true){
            $(this).attr('checked',false);
        }else{
            $(this).attr('checked',true);
        }
    });
    $("[name=ftype]").click(function(){
        if($(this).attr('checked')=='checked' ||$(this).attr('checked')==true){
            $(this).attr('checked',false);
        }else{
            $(this).attr('checked',true);
        }
    });
    $("#sureBtn").click(function(){

        console.log($("[name=dtype]").attr('checked'));
        console.log($("[name=ftype]").attr('checked'));
        console.log($("#fno").val());
        console.log($("#dno").val());
        var params={};
        if($("[name=dtype]").attr('checked')=='checked' ||$("[name=dtype][name=dtype]").attr('checked')==true){
            if($("[name=ftype]").attr('checked')=='checked' ||$("[name=ftype]").attr('checked')==true){
                params={'dtype':1,'ftype':2,'dno':$("#dno").val(),'fno':$("#fno").val(),'companyId':$("#companyIdHid").val()};
            }else{
                params={'dtype':1,'ftype':0,'dno':$("#dno").val(),'fno':$("#fno").val(),'companyId':$("#companyIdHid").val()};
            }
        }else{
            if($("[name=ftype]").attr('checked')=='checked' ||$("[name=ftype]").attr('checked')==true){
                params={'dtype':0,'ftype':2,'dno':$("#dno").val(),'fno':$("#fno").val(),'companyId':$("#companyIdHid").val()};
            }else{
                params={'dtype':0,'ftype':0,'dno':$("#dno").val(),'fno':$("#fno").val(),'companyId':$("#companyIdHid").val()};
            }
        }
        $.ajax({
            type: 'post',
            url: 'modifyProtocol.htm',
            data: params,
            async:false,
            success: function (data) {
                console.log(data);
                console.log(document);
                document.close();
                if (data.success) {
                    //layer.close(companyProtocolModifyDialog);
                    layer.alert('协议修改成功',{
                        title:'处理结果上传成功',
                        cancel:function(){
                            var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                            parent.layer.close(index);
                        }
                    },function(index){
                        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                        parent.layer.close(index);
                    });
                } else {
                    //showTitleInfo("处理结果",data.message);
                    layer.alert(data.message,{title:'处理结果'});
                }
            }
        });
    });
});