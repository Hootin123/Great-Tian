
$(function () {
    // 绑定确定按钮事件
    $('#okBtn').on('click', function () {
        onMoveDept();
    });
    //绑定取消按钮事件
    $('#cancelBtn').on('click', function () {
        onCancel();
    });
});

/**
 * 取消关闭窗口
 */
function onCancel() {
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);
}

/**
 * 选中部门
 */
function onSelectDept(deptId, deptName) {
    $('#deptId').val(deptId);
    $('#deptName').html(deptName);
}

/**
 * 移动到其他部门
 */
function onMoveDept() {
    var deptId = $('#deptId').val();
    if (deptId != null) {
        //获取选中人员Id
        var customerIds = parent.getSelect();
        $.ajax({
            type: 'post',
            url: 'moveDept.htm',
            data: $.param({customerIds: customerIds, deptId: deptId}, true),
            success: function (data) {
                if (data.success) {
                    //重新加载组织树
                    parent.onLoadCompanyTree();
                    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                    parent.layer.close(index);
                } else {
                    showWarning(data.message);
                }
            }
        });
    } else {
        layer.alert('请选中需要移动到的部门')
    }
}