var menuTable = $('#menuTable');
$(function () {
    menuTable.on('click-row.bs.table', function (e, row, $element) {
        $('.success').removeClass('success');
        $($element).addClass('success');
    }).on('check.bs.table', function (e, row, $element) {
        $('.success').removeClass('success');
        $($element).parent().parent().addClass('success');
    });

    //每次打开modal重新加载url
    $("#myAddMenuModal").on("hidden.bs.modal", function () {
        $(this).removeData("bs.modal");
    });

});

function onChildMenu($this, parentId) {
    //往上查找最近的一个iframe 并刷新页面
    window.location.href = "menu.htm?parentId=" + parentId;
}

/**
 * 返回选中行的数据
 */
function getTableData() {
    var index = menuTable.find('tr.success').data('index');
    return (menuTable.bootstrapTable('getData')[index]);
}

/**
 * 返回选中行的Id
 */
function getSelectMenuId() {
    var tableData = getTableData();
    return tableData == null ? '' : tableData.id;
}

/**
 * 修改菜单
 */
function onEdit() {
    var tableData = getTableData();
    if (tableData != null && tableData.state) {
        $('#editMenuBtn').attr('data-toggle', 'modal');
        $('#editMenuBtn').attr('data-target', '#myAddMenuModal');
        $('#editMenuBtn').attr('href', 'addMenuPage.htm?menuId=' + tableData.id);
    }
    else {
        layer.alert('请选中需要修改的数据');
        $('#editMenuBtn').removeAttr('data-toggle');
        $('#editMenuBtn').removeAttr('data-target');
        $('#editMenuBtn').removeAttr('href');
    }
}

/**
 * 删除
 */
function onDelete() {

    var tableData = getTableData();
    if (tableData != null && tableData.state) {
        var msg = tableData.parentId == null || tableData.parentId == '' ? '您将会删除该菜单以及该菜单下的所有子菜单,是否确认?' : '是否确认删除该数据';
        layer.confirm(msg, {icon: 3, title: '提示'}, function (index) {
            $.ajax({
                type: 'post',
                url: 'deleteMenu.htm',
                data: {'menuId': tableData.id},
                success: function (data) {
                    layer.close(index);
                    if (data.success) {
                        //将该数据移除
                        swal({title: "太帅了", text: "菜单删除成功", type: "success"});
                        showSuccess('菜单删除成功');
                        window.location.reload();
                        //menuTable.bootstrapTable('refresh');
                    } else {
                        //swal({title: "很抱歉", text: data.message, type: "warning"});
                        showWarning(data.message);
                    }
                }
            });
        });
    }
    else {
        layer.alert('请选中需要删除的数据');
    }
}