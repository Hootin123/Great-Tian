/**
 * ajax 请求
 * @param url
 * @param option
 * @param callback
 */
function ajaxJson(url, option, callback) {
    var index = layer.load();
    $.ajax(url, {
        type: 'post',
        dataType: 'json',
        data: option,
        success: function (data) {
            //关闭
            layer.close(index);
            if (data.success) {
                if ($.isFunction(callback)) {
                    callback(data);
                }
            } else {
                if (data.message) {
                    showWarning(data.message);
                } else {
                    $('head').append('<script>' + data.documentElement.innerHTML + '</script>');
                }
            }
        },
        error: function (response, textStatus, errorThrown) {
            //关闭
            layer.close(index);
            var responseText = response.responseText;
            if (responseText.indexOf('<script>') == 0){
                $('head').append(responseText);
            }else{
                showError('请求出现异常，请联系管理员');
            }

        },
        complete: function () {

        }
    });
}