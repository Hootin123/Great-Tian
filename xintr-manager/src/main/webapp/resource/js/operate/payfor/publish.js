
var ue;

function getCurDate() {
    var d = new Date();
    var week;
    switch (d.getDay()){
        case 1: week="星期一"; break;
        case 2: week="星期二"; break;
        case 3: week="星期三"; break;
        case 4: week="星期四"; break;
        case 5: week="星期五"; break;
        case 6: week="星期六"; break;
        default: week="星期天";
    }
    var years = d.getFullYear();
    var month = add_zero(d.getMonth()+1);
    var days = add_zero(d.getDate());
    var hours = add_zero(d.getHours());
    var minutes = add_zero(d.getMinutes());
    var seconds=add_zero(d.getSeconds());
    var ndate = years+"年"+month+"月"+days+"日 " + week + " " + hours+":"+minutes+":"+seconds;
    $('#divTime').text(ndate);
}

function add_zero(temp) {
    if(temp<10) return "0"+temp;
    else return temp;
}

setInterval("getCurDate()",100);

$(function () {

    Dropzone.autoDiscover = false;

    // 上传完成
    var myDropzone = new Dropzone("div#myDrop", {
        url: BASE_PATH + "/upload/oss.htm",
        acceptedFiles: ".jpg,.png,.bmp,.jpeg",
        addRemoveLinks: true,
        maxFiles: 1,
        dictInvalidFileType: "错误的图片格式",
        dictRemoveFile: "移除文件",
        dictMaxFilesExceeded: "您只可以上传1个文件"
    });
    
    myDropzone.on('error', function (file, errorMessage) {
        if(errorMessage){
            alertWarn(errorMessage);
        }
    });

    myDropzone.on('success', function (file, xhr) {
        console.log(" >>>> success :" + xhr);
        if(xhr){
            $("#newsImg").val(xhr.url);
        }
    });

    ue = UE.getEditor('newsContent');

    $('.dateTime').datetimepicker({
        language: 'zh-CN',
        format: "yyyy-mm-dd hh:ii",
        // startDate:new Date(),
        autoclose: true,
        todayBtn: true,
        minView: 0,
        todayHighlight: true
    });

    var validator = $("#publishForm").validate({
        rules: {
            newsTitle: "required",
            publisTime: "required",
            newsKeywords: "required",
            newsClassName: "required"
        },
        messages: {
            newsTitle: "请输入新闻标题",
            publisTime: "请选择发布时间",
            newsKeywords: "请输入关键词",
            newsClassName: "请输入子类型名称"
        },
        submitHandler:function(form){

            if($("#newsImg").val() == ''){
                showWarning('请上传缩略图', '警告');
                return false;
            }

            if(ue.getContent() == ''){
                showWarning('请输入新闻内容', '警告');
                return false;
            }
            var ii = layer.load();
            $.ajax({
                type: 'post',
                url: 'publish.htm',
                data: $('#publishForm').serialize(),
                success: function (data) {
                    layer.close(ii);
                    if (data.success) {
                        alertInfo('发布成功！', function(){
                            window.location.href = BASE_PATH + '/operate/news/index.htm';
                        });
                    } else {
                        alertWarn(data.message);
                    }
                }
            });
        }
    });

});

