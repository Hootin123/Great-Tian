$(function(){

    $("#addPayInfoSureBtn").click(function(){
        $.ajax({
            type: 'post',
            url: BASE_PATH+'/shebaoPayInfo/addCompanyOrderPayInfo.htm',
            data: $("#addPayInfoForm").serialize(),
            async:false,
            success: function (data) {
                if (data.success) {
                    layer.alert('添加成功',{
                        title:'处理结果',
                        cancel:function(){
                            parent.initData();
                            parent.dataGrid.onQuery();
                            var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                            parent.layer.close(index);
                        }
                    },function(index){
                        parent.initData();
                        parent.dataGrid.onQuery();
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

function cancelAddPayInfo(){
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);
}

function uploadApproveImg(){
    // $("#upload").attr('src','/resource/img/h5wallet/load.gif')
    $.ajaxFileUpload({
        url:BASE_PATH+ '/shebaoPayInfo/uploadShebaoPayImg.htm', //用于文件上传的服务器端请求地址
        fileElementId: 'shebaotongPayImg', //文件上传域的ID
        dataType: 'json', //返回值类型 一般设置为json
        success: function (data, status){
            if(data.success) {
                $("#showPayImg").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
                $("[name=shebaotongPayImg]").val(data.data);
            }
        }
    });
}