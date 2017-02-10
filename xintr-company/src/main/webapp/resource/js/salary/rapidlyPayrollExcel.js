/**
 * 工资单文件上传
 **/
var state = 'pending';
var uploader;
$(function () {
    //初始化上传控件
    initUploader();
});

/**
 * 初始化上传
 */
function initUploader() {
    uploader = WebUploader.create({
        auto: true,
        // 不压缩image
        resize: false,
        // swf文件路径
        swf: '/resource/js/plugins/webuploader/Uploader.swf',
        // 文件接收服务端。
        server: '../rapidlyPayroll/upload.htm',
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: {
            id: '#picker',
            //是否开起同时选择多个文件能力
            multiple: false,
        },
        // 只允许选择excel文件。
        accept: {
            title: 'Excels',
            extensions: 'xls,xlsx',
            // mimeTypes: 'excel/*'
        }
    });

    //某个文件开始上传前触发，一个文件只会触发一次
    uploader.on('uploadStart', function (file) {
        //显示进度条
        $('.preTan').show();
    })

    // 文件上传过程中创建进度条实时显示。
    uploader.on('uploadProgress', function (file, percentage) {
        var progressNumber = (percentage * 100) + '%';
        $('#uploadProgres').html(progressNumber);
        $('.progress-bar').css('width', progressNumber);
    });

    uploader.on('uploadSuccess', function (file, response) {
        if (response.success) {
            //上传成功，刷新页面
            layer.alert(response.message, {
                title: '上传成功',
            }, function (index) {
                layer.close(index);
                window.location = 'rapidlyPayroll.htm?excelPath=' + response.data;
            });
        } else {
            //上传失败
            layer.alert(response.message, {title: '上传失败'}, function (index) {
                layer.close(index);
            });
        }
    });

    uploader.on('uploadComplete', function (file) {
        $('.preTan').hide()
        $('#' + file.id).find('.uploadProgres').fadeOut();//上传完成后隐藏进度条
        uploader.removeFile(file, true);
    });

}