$(function () {
    initMonth();
    //文件上传
    onUpload();
});
/**
 * 文件上传
 */
function onUpload() {
    Dropzone.prototype.submitRequest = function (xhr, formData, files) {
        formData.append('payday', $('#payday').val());
        formData.append('year', $('.payoffShade ul li').eq(0).attr('data-year'));
        formData.append('month', $('.payoffShade ul li').eq(0).attr('data-month'));
        return xhr.send(formData);
    };
    //事件绑定
    $("#upload").dropzone(
        {
            url: "salary/upload.htm",
            maxFiles: 10,
            maxFilesize: 512,
            acceptedFiles: ".xlsx,.xls",
            uploadprogress: function (file, progress, bytesSent) {
                //$('.bar').css('display', ('block'));
                //更新文件上传进度
                $('.bar_1').css('width', ($('.bar_1').width() * progress) / 100);
                if (progress >= 100) {
                    //$('.bar_1').css('display', ('none'));
                }
            },
            addedfile: function (file) {
            },
            complete: function (data) {
                var res = eval('(' + data.xhr.responseText + ')');
                if (res.success) {
                    //上传成功，刷新页面
                    layer.alert(res.message, {
                        title: '上传成功',
                    }, function (index) {
                        layer.close(index);
                        window.location.reload();
                    });
                    //layer.alert('上传成功', function (index) {
                    //    layer.close(index);
                    //    window.location.reload();
                    //});
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
 * 取消工资单
 * @param excelId
 */
function cancelSalaryExcel(excelId) {
    showConfirm('您确定要撤销这条信息吗', function () {
        $.ajax({
            type: 'post',
            url: 'cancelSalaryExcel.htm?excelId=' + excelId,
            //data: {'excelId': excelId},
            success: function (resultResponse) {
                if (resultResponse.success) {
                    window.location.reload();
                } else {
                    showWarning(resultResponse.message);
                }
            }
        });
    });
}

/**
 * 初始化月份
 */
function initMonth() {
    var oSha = $('.payoffShade');
    var aLi = $('.payoffShade ul li');
    var oUl = $('.payoffShade ul');
    var iH = aLi.eq(5).height();
    //aLi.eq(1).css('display', 'none');
    oSha.mouseover(function () {
        $(this).css('height', oUl.height());
    });
    oUl.mouseover(function () {
        oSha.css('height', oUl.height());
    });
    oUl.mouseout(function () {
        oSha.css('height', 24);
    });
    aLi.mouseover(function () {
        if ($(this).index() == 0) return;
        aLi.css({
            'background': '',
            'color': '#666666'
        });
        $(this).css({
            'background': '#5b7798',
            'color': '#ffffff'
        });
    });

    aLi.click(function () {
        $(this).css('background', '');
        aLi.eq(0).html($(this).html());
        aLi.eq(0).attr('data-year', $(this).attr('data-year'));
        aLi.eq(0).attr('data-month', $(this).attr('data-month'));
        aLi.css('display', 'block');
        $(this).css('display', 'none');
        oSha.css('height', 24);
    });
    /*帮助弹窗*/
    $('.payoff_two_logo').click(function () {
        window.parent.document.getElementById('indexshade').style.display = 'block';
        window.parent.document.getElementById('indexbox').style.display = 'block';
    });

    //var oUl1 = $('.payoff_kalendar');
    //var okal = oUl1.find('li').eq(2);
    //var oLi2 = oUl1.find('li').eq(1);

}
