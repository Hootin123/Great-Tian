var Grid;
$(function () {

    //初始化表格
    Grid = new DataGrid({
        pkFiled: 'jobId',
        singleSelect: true,
        editWidth: '400px',//弹窗宽度
        editHeith: '600px',//弹窗高度
        columns: [
            {field: 'checkbox', checkbox: true},
            {
                field: 'rowNo',
                title: '序号',
                formatter: function (value, row, index) {
                    return index + 1;
                }
            },
            {field: 'jobId', title: '主键', visible: false},
            {field: 'jobName', title: '任务名'},
            {field: 'jobGroup', title: '任务组'},
            {
                field: 'jobStatus', title: '状态',
                formatter: function (value, row, index) {
                    if (value == '0') {
                        return '<span class="badge badge-warning">计划中</span>';
                    } else if (value == '1') {
                        return '<span class="badge badge-primary">运行中</span>';
                    } else {
                        return '<span class="badge badge-danger">已暂停</span>';
                    }

                }
            },
            {field: 'cronExpression', title: 'cron表达式'},
            {field: 'description', title: '描述'},
            {
                field: 'isConcurrent', title: '是否同步',
                formatter: function (value, row, index) {
                    if (value == '1') {
                        return '否';
                    } else {
                        return '是';
                    }
                }
            },
            //{field: 'beanClass', title: '类路径'},
            //{field: 'methodName', title: '方法名', visible: false},
            {field: 'springId', title: 'springId'},
            {
                field: 'opt', title: '操作',
                formatter: function (value, row, index) {
                    var html = '<button type="button" class="btn btn-sm btn-primary" onclick="onSoonRunJob(' + row.jobId + ')">立即运行</button>&nbsp;';
                    if (row.jobStatus == '0') {
                        html += '<button type="button" class="btn btn-sm btn-info" onclick="onStartJob(' + row.jobId + ')">启动</button>&nbsp;';
                    } else if (row.jobStatus == '2') {
                        html += '<button type="button" class="btn btn-sm btn-info" onclick="onResumeJob(' + row.jobId + ')">恢复</button>&nbsp;';
                    } else {
                        html += '<button type="button" class="btn btn-sm btn-warning" onclick="onStopJob(' + row.jobId + ')">暂停</button>&nbsp;'
                    }
                    html += '<button type="button" class="btn btn-sm btn-danger" onclick="onDeleteJob(' + row.jobId + ')">删除</button>';
                    return html;
                }
            }
        ]
    });

    //重写校验方法
    DataGrid.prototype.onCheck = function () {
        return true;
    };

    //重写保存参数
    //DataGrid.prototype.saveParam = function () {
    //    return '';
    //}

});

/**
 * 暂停任务
 * @param jobId
 */
function onStopJob(jobId) {
    var index = layer.confirm('确定要暂停该任务吗？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        $.ajax({
            type: 'post',
            url: 'pauseJob.htm?jobId=' + jobId,
            success: function (data) {
                //关闭
                layer.close(index);
                if (!data.success) {
                    showWarning(data.message);
                } else {
                    //刷新数据
                    Grid.onQuery();
                }
            }
        });
    }, function () {
        layer.close(index);
    });
}

/**
 * 恢复任务
 * @param jobId
 */
function onResumeJob(jobId) {
    var index = layer.confirm('确定要恢复该任务吗？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        $.ajax({
            type: 'post',
            url: 'resumeJob.htm?jobId=' + jobId,
            success: function (data) {
                //关闭
                layer.close(index);
                if (data.success) {
                    //刷新数据
                    Grid.onQuery();
                    showSuccess("恢复成功")
                } else {
                    showWarning(data.message);
                }
            }
        });
    }, function () {
        layer.close(index);
    });
}

/**
 * 立即执行
 */
function onSoonRunJob(jobId) {
    var index = layer.confirm('确定要立即执行该任务吗？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        var index1 = layer.load();
        $.ajax({
            type: 'post',
            url: 'runAJobNow.htm?jobId=' + jobId,
            success: function (data) {
                //关闭
                layer.close(index);
                layer.close(index1);
                if (data.success) {
                    //刷新数据
                    //Grid.onQuery();
                    showSuccess("执行成功")
                } else {
                    showWarning(data.message);
                }
            }
        });
    }, function () {
        layer.close(index);
    });
}

/**
 * 启动
 */
function onStartJob(jobId) {
    var index = layer.confirm('确定要启动该任务吗？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        $.ajax({
            type: 'post',
            url: 'startJob.htm?jobId=' + jobId,
            success: function (data) {
                //关闭
                layer.close(index);
                if (data.success) {
                    //刷新数据
                    Grid.onQuery();
                    showSuccess("启动成功")
                } else {
                    showWarning(data.message);
                }
            }
        });
    }, function () {
        layer.close(index);
    });
}

/**
 * 删除任务
 * @param jobId
 */
function onDeleteJob(jobId) {
    var index = layer.confirm('删除后改任务状态将变为计划中,确定要删除该任务吗？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        $.ajax({
            type: 'post',
            url: 'deleteJob.htm?jobId=' + jobId,
            success: function (data) {
                //关闭
                layer.close(index);
                if (data.success) {
                    //刷新数据
                    Grid.onQuery();
                    showSuccess("删除成功")
                } else {
                    showWarning(data.message);
                }
            }
        });
    }, function () {
        layer.close(index);
    });
}



