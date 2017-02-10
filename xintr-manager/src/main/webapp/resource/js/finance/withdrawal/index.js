var validator;
var Grid;
var validator = $("#auditForm").validate({
    submitHandler:function(form){
        var ii = layer.load();
        $.ajax({
            type: 'post',
            url: 'auditWithdrawals.htm',
            data: $('#auditForm').serialize(),
            success: function (data) {
                layer.close(ii);
                if (data.success) {
                    $('#modal-form').modal('hide');
                    alertInfo('提现审核成功！', function(){
                        window.location.reload();
                    });
                } else {
                    alertWarn(data.message);
                }
            }
        });
    }
});

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
                {field: 'rechargeNumber', title: '提现流水号'},
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
                //{field: 'rechargeMoney', title: '金额'},
                {field: 'rechargeBank', title: '提现银行'},
                {field: 'bankSubbranch', title: '提现银行支行'},
                {field: 'bankAccountName', title: '提现银行卡账户名'},
                {field: 'rechargeBanknumber', title: '提现银行卡号'},
                {field: 'rechargeSerialNumber', title: '银行交易流水'},
                {field: 'rechargeState', title: '状态', formatter:function(value,row,index){
                    
                    if(value == 0){
                        return '<span class="badge">提交申请</span>';
                    }
                    if(value == 1){
                        return '<span class="badge badge-danger">提现失败</span>';
                    }
                    if(value == 2){
                        return '<span class="badge badge-success">提现成功</span>';
                    }

                    return '';
                }},
                {field: 'nickName', title: '财务审核人'},
                {
                    field: 'opt', title: '提现处理',
                    formatter: function (value,row,index) {
                        if(row.rechargeState == 0){
                            return '<a href="index.htm#modal-form" rechargeId="'+ row.rechargeId +'" ' +
                                'onclick="showAudit(this)" class="btn btn-danger btn-sm audit-m" ' +
                                'data-toggle="modal"><i class="fa fa-check"></i>&nbsp;&nbsp;处理</a>';
                        }
                        return '<span class="label label-primary">已处理</span>';
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

});

function showAudit(obj) {
    var _this = $(obj);
    var rechargeId = _this.attr('rechargeId');
    validator.resetForm();
    $('#rechargeSerialNumber').val('');
    $('#rechargeBak').val('');
    $('#auditForm #rechargeId').val(rechargeId);
}