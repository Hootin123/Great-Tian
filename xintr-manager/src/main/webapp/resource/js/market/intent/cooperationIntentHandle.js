$(function(){

    $("[name='sign']").click(function(){
        var signArray=$("[name='sign']");
        for(var i=0;i<signArray.length;i++){
            var sign=signArray[i];
            if(this.value==sign.value){
                $(this).attr('checked',true);
            }else{
                $(sign).attr('checked',false);
            }
        }

    });

    $("#sureBtn").click(function(){
        var signValue=0;
        var signArray=$("[name='sign']");
        for(var i=0;i<signArray.length;i++){
            var sign=signArray[i];
            if($(sign).attr('checked')=='checked' ||$(sign).attr('checked')==true){
                signValue=$(sign).val();
            }
        }
        console.log(signValue);
        //var formDataStr="{'itemId':'"+$('[name=itemId]').val()
        //    +"','sign':'"+signValue
        //    +"'}";
        var itemName=$('[name=itemName]').val();
        var itemMobile=$('[name=itemMobile]').val();
        var collaborationContractAddress=$('[name=collaborationContractAddress]').val();
        var collaborationRelationerName=$('[name=collaborationRelationerName]').val();
        var collaborationRelationerPhone=$('[name=collaborationRelationerPhone]').val();
        var formDataStr={'itemId':$('[name=itemId]').val(),'sign':signValue};
        if(itemName!=null && itemName!="" && itemName!=undefined){
            formDataStr['itemName']=itemName;
        }
        if(itemMobile!=null && itemMobile!="" && itemMobile!=undefined){
            formDataStr['itemMobile']=itemMobile;
        }
        if(collaborationContractAddress!=null && collaborationContractAddress!="" && collaborationContractAddress!=undefined){
            formDataStr['collaborationContractAddress']=collaborationContractAddress;
        }
        if(collaborationRelationerName!=null && collaborationRelationerName!="" && collaborationRelationerName!=undefined){
            formDataStr['collaborationRelationerName']=collaborationRelationerName;
        }
        if(collaborationRelationerPhone!=null && collaborationRelationerPhone!="" && collaborationRelationerPhone!=undefined){
            formDataStr['collaborationRelationerPhone']=collaborationRelationerPhone;
        }
        //var formDataStr={'itemId':$('[name=itemId]').val(),'sign':signValue,'itemName':$('[name=itemName]').val(),'itemMobile':$('[name=itemMobile]').val()};
        console.log(formDataStr);
        //console.log(JSON.parse(formDataStr));
        $.ajax({
            type: 'post',
            url: 'handleIntent.htm',
            data: JSON.stringify(formDataStr),
            async:false,
            dataType: "json",
            contentType : 'application/json',
            success: function (data) {
                console.log(data);
                if (data.success) {
                    layer.alert('处理成功',{
                        title:'处理结果',
                        cancel:function(){
                            parent.dataGrid.onQuery();
                            var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                            parent.layer.close(index);
                        }
                    },function(index){
                        parent.dataGrid.onQuery();
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
 * 取消合作意向弹出框
 */
function cancelCooperationIntentHandle(){
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);
}