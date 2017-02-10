$(function () {
    //初始化树
    initTree();
})


/**
 * 获取角色列表
 */
function initTree() {
    var userId = parent.Grid.Utils.getSelectRowData().id;
    $.ajax({
        type: 'post',
        url: 'roleList.htm?userId=' + userId,
        success: function (data) {
            $('#treeview').treeview({
                data: data,
                showCheckbox: true,
            });
        }
    });
}

/**
 * 获取选中的角色
 */
function getSelectRole() {
    //获取所有选中的节点
    var checkeds = $('#treeview').treeview('getChecked');
    var roleIds = [];
    if (!isEmpty(checkeds)) {
        var length = checkeds.length;
        for (var i = 0; i < length; i++) {
            roleIds.push(checkeds[i].id);
        }
    }
    return roleIds;
}

