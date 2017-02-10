$(function () {
    //文件上传
    onUpload();
})

/**
 * 文件上传
 */
function onUpload() {
    //事件绑定
    $("#upload").dropzone({
        url: "customers/upload.htm",
        maxFiles: 10,
        maxFilesize: 512,
        acceptedFiles: ".xlsx,.xls",
        uploadprogress: function (file, progress, bytesSent) {
            //更新文件上传进度
            //$('.bar_1').css('width', ($('.bar').width() * progress) / 100);
        },
        addedfile: function (file) {
        },
        complete: function (data) {
            var res = eval('(' + data.xhr.responseText + ')');
            if (res.success) {
                //上传成功，刷新页面
                showInfo('文件上传成功');
            } else {
                //上传失败
                showError(res.message);
            }
        }
    });
}