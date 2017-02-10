var Grid;
$(function () {

    Grid = new DataGrid({
            columns: [
                {
                    field: 'rowNo',
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index + 1;
                    }
                },
                {field: 'rechargeId', title: '主键', visible: false},
                {field: 'companyName', title: '企业名称'},
                {field: 'rechargeNumber', title: '充值流水号'},
                {field: 'rechargeAddtime', title: '申请时间', formatter:function(value,row,index){
                    if(value){
                        return unixToTime(value);
                    }
                    return '';
                }},
                {field: 'rechargeAuditFirstTime', title: '处理完结时间', formatter:function(value,row,index){
                    if(value){
                        return unixToTime(value);
                    }
                    return '';
                }},
                {
                    field: 'rechargeMoney', title: '金额',
                    formatter: function (value, row, index) {
                        return formatCurrency(value,2);
                    }
                },
                {field: 'rechargeBank', title: '付款银行名称'},
                {field: 'rechargeBanknumber', title: '付款银行账号'},
                {field: 'rechargeSerialNumber', title: '银行交易流水'},
                {field: 'rechargeBak', title: '客户备注'},
                {field: 'rechargeState', title: '状态', formatter:function(value,row,index){
                    if(value == 0){
                        return '<span class="badge">提交申请</span>';
                    }
                    if(value == 1){
                        return '<span class="badge badge-danger">充值失败</span>';
                    }
                    if(value == 2){
                        return '<span class="badge badge-success">充值成功</span>';
                    }
                    return '';
                }},
                {field: 'nickName', title: '财务审核人'},
                {
                    field: 'opt', title: '操作',
                    formatter: function (value, row, index) {
                        if(row.rechargeState == 0){
                            return '<a href="index.htm#modal-form" rechargeId="'+ row.rechargeId +'" ' +
                                'onclick="showAudit(this)" ' +
                                'class="btn btn-danger btn-sm audit-m" ' +
                                'data-toggle="modal"><i class="fa fa-check"></i>&nbsp;&nbsp;审核</a>';
                        }
                        return '<span class="label label-primary">已审核</span>';
                    }
                }
            ],
            requestParam: function () {
                return {
                    'state': $('#state').val(),
                    'auditName': $('#auditName').val(),
                    'companyName': $('#companyName').val()
                }
            }
        }
    );

    // 审核
    $('.audit-btn').on('click', function () {
        var ii = layer.load();
        $.ajax({
            type: 'post',
            url: 'auditRecharge.htm',
            data: $('#auditForm').serialize(),
            success: function (data) {
                layer.close(ii);
                if (data.success) {
                    $('#modal-form').modal('hide');
                    alertInfo('充值审核成功！', function(){
                        window.location.reload();
                    });
                } else {
                    alertWarn(data.message);
                }
            }
        });
    });

});

function showAudit(obj) {

    var _this = $(obj);
    var companyName = _this.parents('tr:eq(0)').find("td:eq(1)").text();
    var rechargeBank = _this.parents('tr:eq(0)').find("td:eq(6)").text();
    var rechargeBanknumber = _this.parents('tr:eq(0)').find("td:eq(7)").text();
    var rechargeSerialNumber = _this.parents('tr:eq(0)').find("td:eq(8)").text();
    var rechargeMoney = _this.parents('tr:eq(0)').find("td:eq(5)").text();
    var rechargeBak = _this.parents('tr:eq(0)').find("td:eq(9)").text();
    var rechargeId = _this.attr('rechargeId');

    $('#tabInfo tr:eq(0)').find('td:eq(1)').text(companyName);
    $('#tabInfo tr:eq(1)').find('td:eq(1)').text(rechargeBank);
    $('#tabInfo tr:eq(2)').find('td:eq(1)').text(rechargeBanknumber);
    $('#tabInfo tr:eq(3)').find('td:eq(1)').text(rechargeSerialNumber);
    $('#tabInfo tr:eq(4)').find('td:eq(1)').text(rechargeMoney);
    $('#tabInfo tr:eq(5)').find('td:eq(1)').text(rechargeBak);

    $('#auditForm #rechargeId').val(rechargeId);
}

//格式化金额，不带千位符
function formatCurrency(s, n) {
    var t;
    t = s;
    if ($.isNumeric(t)){
        t = parseFloat(t).toFixed(n);
    }

    return t;
}