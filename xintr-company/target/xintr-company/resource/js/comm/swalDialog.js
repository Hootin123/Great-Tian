
/**
 * 提示信息
 * @param msg
 * @param callback
 */
function alertInfo(msg, callback){
    layer.alert(msg, {
        icon: 1
    }, function(index){
        layer.close(index);
        if(callback){
            callback();
        }
    });
}

/**
 * 警告信息
 * @param msg
 * @param callback
 */
function alertWarn(msg, callback){
    layer.alert(msg, {
        icon: 0,
        title:'警告'
    }, function(index){
        layer.close(index);
        if(callback){
            callback();
        }
    });
}

/**
 * 错误信息
 * @param msg
 * @param callback
 */
function alertError(msg, callback){
    layer.alert(msg, {
        icon: 2,
        title:'错误'
    }, function(index){
        layer.close(index);
        if(callback){
            callback();
        }
    });
}

/**
 * 提示信息
 * @param title
 * @param msg
 */
function showInfo(msg,title) {
    //swal(title, msg);
    layer.alert(msg,
        {
            title: (isEmpty(title) ? "信息" : title)
        }
    )
}


/**
 * 提示信息
 * @param title
 * @param msg
 */
function showInfo(msg, title, callb) {
    //swal(title, msg);
    layer.open({
        title: "提示",
        content: msg,
        yes: function(index, layero){
            //do something
            layer.close(index); //如果设定了yes回调，需进行手工关闭
            callb()
        }
    });
}


/**
 * 成功对话框
 * @param msg
 */
function showSuccess(msg,title) {
    //swal(title, msg, "success");
    layer.alert(msg,
        {
            icon: 1,
            title: (isEmpty(title) ? "成功" : title)
        }
    )
}

/**
 * 警告
 * @param msg
 */
function showWarning(msg,title) {
    //swal(title, msg, "warning");
    layer.alert(msg,
        {
            icon: 0,
            title: (isEmpty(title) ? "警告" : title)
        }
    )
}


/**
 * 错误
 * @param msg
 */
function showError(msg,title) {
    //swal(title, msg, "error");
    //layer.alert(msg, {title: title, icon: 2});
    layer.alert(msg,
        {
            icon: 2,
            title: (isEmpty(title) ? "错误" : title)
        }
    )
}

/**
 * 确认框
 * @param msg
 * @param callBack
 */
function showConfirm(msg, callBack) {
    //swal({
    //    title: msg,
    //    //text: "删除后将无法恢复，请谨慎操作！",
    //    type: "warning",
    //    showCancelButton: true,
    //    cancelButtonText: "取消",
    //    confirmButtonColor: "#DD6B55",
    //    confirmButtonText: '确定',
    //    closeOnConfirm: false
    //}, function () {
    //    callBack();
    //})

    var index = layer.confirm(msg, {
        btn: ['确定', '取消'] //按钮
    }, function () {
        callBack();
    })
}



