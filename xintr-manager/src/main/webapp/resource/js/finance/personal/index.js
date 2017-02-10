var Grid;
$(function () {

    $('.dateTime').datetimepicker({
        language: 'zh-CN',
        format: "yyyy-mm-dd hh:ii",
        startDate: '2016-01-01',
        autoclose: true,
        todayBtn: true,
        minView: 1,
        todayHighlight: true
    });
    
    Grid = new DataGrid({
            columns: [
                {
                    field: 'rowNo',
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index + 1;
                    }
                },
                {field: 'firstTime', title: '最早申请时间', formatter:function(value,row,index){
                    if(value){
                        return unixToTime(value);
                    }
                    return '';
                }},
                {field: 'endTime', title: '最晚申请时间', formatter:function(value,row,index){
                    if(value){
                        return unixToTime(value);
                    }
                    return '';
                }},
                //{field: 'batchMoney', title: '批次总金额'},
                {
                    field: 'batchMoney', title: '批次总金额',
                    formatter: function (value, row, index) {
                        return formatCurrency(value,2);
                    }
                },
                {field: 'batchCount', title: '批次总请求'},
                {field: 'rechargeState', title: '状态', formatter:function (value) {
                    if(value == 0){
                        return '未审批';
                    }
                    if(value == 1){
                        return '审批失败';
                    }
                    if(value == 2){
                        return '已审批';
                    }
                    return '';
                }},
                {field: 'auditName', title: '财务经办人'},
                {field: 'opt', title: '操作', formatter:function (value, row, index) {
                    if(row.rechargeState == 2){
                        return '<a href="download.htm?batchNumber='+row.batchNumber+'">下载</a> | ' + '<a href="javascript:void(0)" onclick="onShowRechargeDetail(\''+row.batchNumber+'\')">详情</a>';
                    }
                    if(row.rechargeState == 0){
                        return '<a href="download.htm?batchNumber='+row.batchNumber+'">下载</a> | <a href="index.htm#audit-form" data-toggle="modal" onclick="showAudit(\''+ row.batchNumber +'\')">审批</a> | <a href="#" onclick="onShowRechargeDetail(\''+row.batchNumber+'\')">详情</a>';
                    }
                    return '';
                }}
            ],
            requestParam: function () {
                return {
                    'auditName': $('#auditName').val(),
                    'date_str': $('#date_str').val(),
                    'state': $('#state').val()
                }
            }
        }
    );

    
});

function onShowRechargeDetail(batchNumber) {
    var index = layer.open({
        type: 2,
        title: '提现详情',
        maxmin: true,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: 'detail.htm?batchNumber='+batchNumber
    });
    //窗口最大化
    layer.full(index);
}

function showAudit(batchNumber){
    $('#batchNumber').val(batchNumber);
}

/**
 * 审核单个
 */
function auditWith(){
    var batchNumber = $('#batchNumber').val();
    if(batchNumber && batchNumber != ''){
        var ii = layer.load();
        $.ajax({
            type: 'post',
            url: 'audit.htm',
            data: {
                "auditType": 1,
                "batchNumber": batchNumber
            },
            success: function (data) {
                layer.close(ii);
                if (data.success) {
                    alertInfo("审批成功!", function () {
                        window.location.reload();
                    });
                } else {
                    alertWarn(data.message);
                }
            }
        });
    }
}

/**
 * 关闭审核
 */
function closeAudit(){
    var ii = layer.load();
    $.ajax({
        type: 'post',
        url: 'audit.htm',
        data: {
            "auditType": 0,
            "batchNumber": 0
        },
        success: function (data) {
            layer.close(ii);
            if (data.success) {
                alertInfo("审批成功!", function () {
                    window.location.reload();
                });
            } else {
                alertWarn(data.message);
            }
        }
    });
}