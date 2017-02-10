var Grid;
$(function () {

    //初始化表格
    initDataGrid();

    //绑定授权按钮事件
    $('#authBtn').on('click', function () {
        if (Grid.Utils.checkSelect(Grid.Utils.getSelectRowData())) {
            //授权列表页面
            onAuth();
        }
    })

    //绑定修改密码按钮事件
    $('#resetPwdBtn').on('click', function () {
        if (Grid.Utils.checkSelect(Grid.Utils.getSelectRowData())) {
            //重置密码
            onResetPwd();
        }
    })

});

/**
 * 修改用户密码
 */
function onResetPwd() {
    showConfirm('是否确认重置该用户的密码', function () {
        //获取选中的用户id
        var userId = Grid.Utils.getSelectRowData().id;
        if (!isEmpty(userId)) {
            $.ajax({
                type: 'post',
                url: 'resetPwd.htm',
                data: {'userId': userId},
                success: function (data) {
                    if (data.success) {
                        showInfo('新密码为:' + data.data);
                    } else {
                        showWarning(data.message);
                    }
                }
            });
        }
    });

}

/**
 * 授权列表
 */
function onAuth() {
    layer.open({
        maxmin: false, //开启最大化最小化按钮
        closeBtn: 1, //不显示关闭按钮
        area: ['400px', '580px'],
        title: "授权",
        type: 2,
        content: 'userAuth.htm', //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
        btn: ['保存', '取消'],
        yes: function (index, layero) { //或者使用btn1
            //保存
            onUserRoleSave(index);
            //关闭弹窗
            layer.close(index);
        },
        cancel: function (index) {
            layer.close(index);
        }
    });
}

/**
 * 初始化表格
 */
function initDataGrid() {
//初始化表格
    Grid = new DataGrid({
        pkFiled: 'id',
        search: true, //不显示 搜索框
        searchTip: '手机号码或邮箱地址',
        editHeith: '530px',
        singleSelect: true,
        columns: [
            {field: 'checkbox', checkbox: true},
            {
                field: 'rowNo',
                title: '序号',
                formatter: function (value, row, index) {
                    return index + 1;
                }
            },
            {field: 'id', title: '主键', visible: false},
            {field: 'nickName', title: '昵称'},
            {field: 'mobilePhone', title: '手机号码'},
            {field: 'email', title: '邮箱地址'},
            {field: 'pwd', title: '密码', visible: false},
            {
                field: 'state', title: '状态',
                formatter: function (value) {
                    return stateFormatter(value);
                }
            },
            {field: 'loginCount', title: '登录总次数'},
            {
                field: 'loginTime', title: '最后登录时间',
                formatter: function (value, row, index) {
                    if (value) {
                        return unixToTime(value);
                    }
                    return '';
                }
            },
            {field: 'isDelete', title: '是否删除', visible: false},
            {
                field: 'createTime', title: '创建时间',
                formatter: function (value, row, index) {
                    if (value) {
                        return unixToTime(value);
                    }
                    return '';
                }
            },
            {
                field: 'updateTime', title: '修改时间',
                formatter: function (value, row, index) {
                    if (value) {
                        return unixToTime(value);
                    }
                    return '';
                }
            },
            {field: 'createsUer', title: '创建人', visible: false},
            {field: 'updateUser', title: '修改人', visible: false},
            {field: 'superAdmin', title: '是否超级管理员', visible: false},
            {field: 'salt', title: 'salt', visible: false}
        ]
    });

    //重写校验方法
    DataGrid.prototype.onCheck = function () {
        return true;
    };
}

/**
 * 保存给用户分配的角色
 */
function onUserRoleSave(index) {
    var roleIds = window['layui-layer-iframe' + index].getSelectRole();
    $.ajax({
        type: 'post',
        url: 'addUserRole.htm',
        data: $.param({userId: Grid.Utils.getSelectRowData().id, roleIds: roleIds}, true),
        success: function (data) {
            if (data.success) {
                showSuccess('授权成功');
            } else {
                showWarning(data.message);
            }
        }
    });
}


function stateFormatter(value) {
    if (value == '0') {
        return '可用';
    }
    else {
        return '禁用';
    }
}




