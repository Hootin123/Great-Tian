var companyOrganizationImg="";
$(function () {
    //文件上传
    onUpload();
});

/**
 * 文件上传
 */
function onUpload() {
    //事件绑定
    $("#upload").dropzone(
        {
            url: "payfor/upload.htm",
            maxFiles: 10,
            maxFilesize: 512,
            acceptedFiles: ".png,.jpg,.rar,.zip,.tar,jar",
            addedfile: function (file) {
                //更新文件上传进度
                $('.bar_1').css('width', 10);
            },
            complete: function (data) {
                var res = eval('(' + data.xhr.responseText + ')');
                if (res.success) {
                    companyOrganizationImg=res.message;
                    var urlImage='http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/'+ res.message;
                    $("#licenseImage").attr("src",urlImage);
                } else {
                    //上传失败
                    layer.alert(res.message, {title: '上传失败'}, function (index) {
                        layer.close(index);
                    });
                }
            }
        });
}

/**
 * 提交前数据验证
 */
function confirmLicense(companyId) {
    $.ajax({
        type: 'post',
        url: 'confirmLicense.htm',
        data: "companyId="+companyId+"&companyOrganizationImg="+companyOrganizationImg,
        error:function(a,b,c){
            console.log(a);
            console.log(b);
            console.log(c);

        },
        success: function (resultResponse) {

            if (resultResponse.success) {
                alert('执照修改成功!');
            } else {
                alert('执照修改失败!');
            }
        }
});
}
