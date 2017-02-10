$(function () {
    UnPayOrderExtend.init();
})

//formatCurrency(value,2)
// unixToDate(value);
// ,
//formatter: function (value) {
//    return unixToDate(value);
//}
//formatter: function (value, row, index) {
//    return $("select[name=companyIsauth] option[value='"+value+"']").html()
//}
UnPayOrderExtend = {
    grid: 0,
    init: function () {
        UnPayOrderExtend.initDataGrid();
        window.addProtocolGrid = UnPayOrderExtend.grid;
    },
    initDataGrid: function () {
        UnPayOrderExtend.grid = new DataGrid({
                url: BASE_PATH + '/customerPay/page.htm',
                columns: [
                    {field: 'company_id', title: '序号',
                        formatter: function (value, row, index) {
                            return index + 1;
                        }
                    },
                    {field: 'company_name', title: '企业名称',
                        formatter: function (value,row,index) {
                            return "<a href='#' onclick='UnPayOrderExtend.onShowCompanyDetail("+row.company_id+")' >"+value+"</a>";
                        }
                    },
                    {field: 'recharge_number', title: '发工资单号'},
                    {field: 'pay_day', title: '发薪时间',
                        formatter: function (value) {
                            return unixToDate(value);
                        }
                    },
                    {field: 'customer_turename', title: '员工姓名'},
                    {field: 'customer_phone', title: '员工手机'},
                    {field: 'customer_idcard', title: '身份证号'},
                    {field: 'customer_bank', title: '银行'},
                    {field: 'customer_banknumber', title: '工资卡号'},
                    {field: 'real_wage', title: '工资金额',
                        formatter: function (value) {
                            return formatCurrencyDate(value,2);
                        }
                    },
                    {field: 'remark', title: '备注'},
                    {field: 'fail_msg', title: '失败原因'},
                    {field: 'recharge_state', title: '处理状态',
                        formatter: function (value, row, index) {
                            var opeationType=row.opeationType;
                            if(opeationType==1 || opeationType==3){
                                if(value == 2) {
                                    if(row.recharge_recall_result == 0) {
                                        return "待回调";
                                    }else{
                                        return "处理成功";
                                    }
                                }else if (value == 0){
                                    return "待发送";
                                }else{
                                    return $("select[name=rechargeState] option[value='"+value+"']").html();
                                }
                            }else{
                                if(value==1){
                                    if(row.recharge_recall_result == 0) {
                                        return "待回调";
                                    }else if(row.recharge_recall_result == 1){
                                        return "处理成功";
                                    }else{
                                        return "发薪异常";
                                    }
                                }
                                else if(value==0){
                                    return "待发送";
                                }else{
                                    return "-";
                                    // return $("select[name=rechargeState] option[value='"+value+"']").html();
                                }
                            }
                        }
                    },
                    {field: 'finance_user', title: '财务处理人'},
                    {
                        field: 'recharge_state', title: '操作',
                        formatter: function (value, row, index) {
                            //if(row.recharge_state == 0) {
                            //    return "";
                            //}else
                            var opeationType=row.opeationType;
                            if(opeationType==1 || opeationType==3){
                                if(row.recharge_state == 1) {
                                    return "<a href='javascript:UnPayOrderExtend.showSysMakeUp(" + row.recharge_id + ","+opeationType+")'>系统补发</a><br/><a href='javascript:UnPayOrderExtend.showHandMakeUp(" + row.recharge_id + ","+opeationType+")'>手工补发</a>";
                                }else if(row.recharge_state == 2) {
                                    return "/";
                                }else{
                                    return '/';
                                }
                            }else{
                                if(row.recharge_state == 1 && row.recharge_recall_result==2) {
                                    return "<a href='javascript:UnPayOrderExtend.showSysMakeUp(" + row.recharge_id + ","+opeationType+")'>系统补发</a><br/><a href='javascript:UnPayOrderExtend.showHandMakeUp(" + row.recharge_id + ","+opeationType+")'>手工补发</a>";
                                }else{
                                    return '/';
                                }
                            }

                        }
                    }
                ],
                requestParam: function () {
                    return {
                        'rechargeState': $('#queryForm select[name=rechargeState]').val(),
                        'payStatus': $('#queryForm select[name=payStatus]').val(),
                        'rechargeBumber': $('#queryForm input[name=rechargeBumber]').val(),
                        'financeUser': $('#queryForm input[name=financeUser]').val(),
                        'companyName': $('#queryForm input[name=companyName]').val(),
                        'opeationType': $('#queryForm select[name=opeationType]').val(),
                    }
                }
            }
        );
    },
    showHandMakeUp : function (rechargeId,opeationType) {
        if(opeationType==1){
            var index = layer.confirm("<p style='width: 500px'>请在完成手工转账后，填写备注（渠道+流水号）：</p><br/>备注：<input id='remark' style='width: 300px'/>", {
                btn: ['确定', '取消'], //按钮
                area:['540px', '250px']
            }, function () {
                $.post(BASE_PATH + "/customerPay/handMakeUp.htm", {"rechargeId": rechargeId, "remark":$("#remark").val()}, function(data){
                    if (data.success) {
                        UnPayOrderExtend.grid.onQuery()
                        layer.closeAll('dialog');
                    } else {
                        showWarning(data.message);
                    }
                })

            });
        }else if(opeationType==2){
            var index = layer.confirm("<p style='width: 500px'>请在完成手工转账后，填写备注（渠道+流水号）：</p><br/>备注：<input id='remark' style='width: 300px'/>", {
                btn: ['确定', '取消'], //按钮
                area:['540px', '250px']
            }, function () {
                $.post(BASE_PATH + "/customerPay/handMakeUpRapidly.htm", {"rechargeId": rechargeId, "remark":$("#remark").val()}, function(data){
                    if (data.success) {
                        UnPayOrderExtend.grid.onQuery()
                        layer.closeAll('dialog');
                    } else {
                        showWarning(data.message);
                    }
                })

            });
        }else if(opeationType==3){
            var index = layer.confirm("<p style='width: 500px'>请在完成手工转账后，填写备注（渠道+流水号）：</p><br/>备注：<input id='remark' style='width: 300px'/>", {
                btn: ['确定', '取消'], //按钮
                area:['540px', '250px']
            }, function () {
                $.post(BASE_PATH + "/customerPay/handMakeUpShebao.htm", {"rechargeId": rechargeId, "remark":$("#remark").val()}, function(data){
                    if (data.success) {
                        UnPayOrderExtend.grid.onQuery()
                        layer.closeAll('dialog');
                    } else {
                        showWarning(data.message);
                    }
                })

            });
        }


    },//系统补发
    showSysMakeUp:function(rechargeId,opeationType){
        if(opeationType==1){
            showConfirm("确认系统补发工资？<br/>如果员工姓名、银行名称、工资卡信息有误，则不可能补发成功的！", function(index){

                $.post(BASE_PATH + "/customerPay/sysMakeUp.htm", {"rechargeId": rechargeId}, function(data){
                    if (data.success) {
                        UnPayOrderExtend.grid.onQuery()
                        layer.closeAll('dialog');
                    } else {
                        showWarning(data.message);
                    }
                })
            });
        }else if(opeationType==2){
            showConfirm("确认系统补极速发工资？<br/>如果员工姓名、银行名称、工资卡信息有误，则不可能补发成功的！", function(index){

                $.post(BASE_PATH + "/customerPay/sysMakeUpRapidly.htm", {"rechargeId": rechargeId}, function(data){
                    if (data.success) {
                        UnPayOrderExtend.grid.onQuery()
                        layer.closeAll('dialog');
                    } else {
                        showWarning(data.message);
                    }
                })
            });
        }else if(opeationType==3){
            showConfirm("确认系统补退个人社保公积金费用？<br/>如果员工姓名、银行名称、工资卡信息有误，则不可能补发成功的！", function(index){

                $.post(BASE_PATH + "/customerPay/sysMakeUpShebao.htm", {"rechargeId": rechargeId}, function(data){
                    if (data.success) {
                        UnPayOrderExtend.grid.onQuery()
                        layer.closeAll('dialog');
                    } else {
                        showWarning(data.message);
                    }
                })
            });
        }
    },
    onShowCompanyDetail:function(companyId) {
        var index = layer.open({
            type: 2,
            title: '企业信息',
            maxmin: true,
            shadeClose: false, //点击遮罩关闭层
            area: ['650px', '380px'],
            content: BASE_PATH + '/companyManage/companyManageDetail.htm?companyId='+companyId
        });
        //窗口最大化
        layer.full(index);
    }
}

function formatCurrencyDate(s, n) {
    var t;
    t = s;
    if ($.isNumeric(t)){
        t = parseFloat(t).toFixed(n);
    }

    return t;
}