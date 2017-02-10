var Grid_task;
var Grid_operation;
var Grid_exception;
$(function () {
    //初始化选项卡
    initTabs();
    //初始化任务日志列表
    initTaskList();
    //绑定任务日志刷新按钮事件
    $('#taskRefreshBtn').on('click', function () {
        Grid_task.onQuery();
    });
    //绑定操作日志刷新按钮事件
    $('#operationRefreshBtn').on('click', function () {
        Grid_operation.onQuery();
    });
    //绑定异常日志刷新按钮事件
    $('#exceptionRefreshBtn').on('click', function () {
        Grid_exception.onQuery();
    });
});

/**
 * 初始化选项卡
 */
function initTabs() {
//点击各个页签触发事件
    $("#logTabs li").each(function () {
        $(this).click(function () {
            if (this.id == "task" && isEmpty(Grid_task)) {
                //初始化任务日志列表
                initTaskList();
            } else if (this.id == "operation" && isEmpty(Grid_operation)) {
                //初始化操作日志列表
                initOperationList();
            } else if (this.id == "exception" && isEmpty(Grid_exception)) {
                //初始化异常日志列表
                initExceptionList();
            }
        })
    })
}

/**
 * 初始化操作日志列表
 */
function initOperationList() {
    //初始化表格
    Grid_operation = new DataGrid({
        url: 'operationData.htm',
        gridId: '#operation-list',
        toolbar: $('#operationBar'),
        deleteBtn: '#operationDeleteBtn',
        search: true, //不显示 搜索框
        searchTip: '操作或模块名称',
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
            {field: 'modelName', title: '模块名'},
            {field: 'operation', title: '操作'},
            {field: 'userName', title: '操作人'},
            {field: 'requestIp', title: '请求Ip'},
            {field: 'serverName', title: '服务器名'},
            {
                field: 'createTime', title: '操作时间',
                formatter: function (value, row, index) {
                    if (value) {
                        return unixToTime(value);
                    }
                    return '';
                }
            }
        ]
    });
}

/**
 * 初始化任务日志列表
 */
function initTaskList() {
    //初始化表格
    Grid_task = new DataGrid({
        url: 'taskData.htm',
        gridId: '#task-list',
        toolbar: $('#taskBar'),
        deleteBtn: '#taskDeleteBtn',
        search: true, //不显示 搜索框
        searchTip: '任务名称或任务Id',
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
            {field: 'taskName', title: '任务名称'},
            {field: 'taskId', title: '任务Id'},
            {
                field: 'type', title: '类型',
                formatter: function (value, row, index) {
                    if (value == '0') {
                        return '后台任务';
                    }
                    return '';
                }
            },
            {
                field: 'startTime', title: '开始时间',
                formatter: function (value, row, index) {
                    if (value) {
                        return unixToTime(value);
                    }
                    return '';
                }
            },
            {
                field: 'endTime', title: '结束时间',
                formatter: function (value, row, index) {
                    if (value) {
                        return unixToTime(value);
                    }
                    return '';
                }
            },
            {field: 'tookTime', title: '耗时'},
            {
                field: 'state', title: '状态',
                formatter: function (value, row, index) {
                    if (value == '0') {
                        return '<span class="label label-primary">成功</span>';
                    } else {
                        return ' <span class="label label-danger">失败</span>';
                    }
                }
            },
            {field: 'serverName', title: '服务器名'},
            {field: 'serverIp', title: '服务器ip'},
            {field: 'remarks', title: '说明'}
        ]
    });
}

/**
 * 初始化异常日志列表
 */
function initExceptionList() {
//初始化表格
    Grid_exception = new DataGrid({
        url: 'exceptionData.htm',
        gridId: '#exception-list',
        toolbar: $('#exceptionBar'),
        deleteBtn: '#exceptionDeleteBtn',
        search: true, //不显示 搜索框
        searchTip: '操作或模块名称',
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
            {field: 'modelName', title: '模块名'},
            {field: 'operation', title: '操作'},
            {field: 'userName', title: '操作人'},
            {
                field: 'createTime', title: '操作时间',
                formatter: function (value, row, index) {
                    if (value) {
                        return unixToTime(value);
                    }
                    return '';
                }
            },
            {field: 'content', title: '异常内容'}
        ]
    });
}