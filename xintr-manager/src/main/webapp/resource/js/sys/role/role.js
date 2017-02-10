var Grid;
$(function () {

    //初始化表格
    Grid = new DataGrid({
        pkFiled: 'id',
        singleSelect: true,
        editWidth: '600px',//弹窗宽度
        editHeith: '680px',//弹窗高度
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
            {field: 'roleName', title: '角色名称'},
            {
                field: 'state', title: '状态',
                formatter: function (value, row, index) {
                    if (value == '0') {
                        return '可用';
                    }
                    else {
                        return '禁用';
                    }
                }
            },
            {field: 'descr', title: '角色描述'},
            {field: 'createUser', title: '创建人', visible: false},
            {
                field: 'createTime', title: '创建时间',
                formatter: function (value, row, index) {
                    if (value) {
                        return unixToTime(value);
                    }
                    return '';
                }
            },
            {field: 'updateUser', title: '修改人', visible: false},
            {
                field: 'updateTime', title: '修改时间',
                formatter: function (value, row, index) {
                    if (value) {
                        return unixToTime(value);
                    }
                    return '';
                }
            }
        ]
    });

    //重写校验方法
    DataGrid.prototype.onCheck = function () {
        return true;
    };

    //重写保存参数
    DataGrid.prototype.saveParam = function () {
        return '';
    }


});




