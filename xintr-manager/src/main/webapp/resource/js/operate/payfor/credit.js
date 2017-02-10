var Grid;
$(function () {

    Grid = new DataGrid({
            columns: [
                {field: 'orderNumber', title: '订单编号'},
                {field: 'orderId', title: '主键', visible: false},
                {field: 'companyName', title: '申请企业'},
                {
                    field: 'orderAddtime', title: '申请时间',
                    formatter: function (value, row, index) {
                        var date = new Date(parseInt(value));
                        var a = date.getFullYear() + '.' + (date.getMonth()+1<10?'0':'') + (date.getMonth() + 1) + '.' + (date.getDate()<10?'0':'') + date.getDate() + ' ' +date.getHours()+':'+date.getMinutes()+':'+date.getSeconds();
                        return a;
                    }
                },
                {
                    field: 'orderExpectDay', title: '期望到账时间',
                    formatter: function (value, row, index) {
                        var date = new Date(parseInt(value));
                        var a = date.getFullYear() + '.' + (date.getMonth()+1<10?'0':'') + (date.getMonth() + 1) + '.' + (date.getDate()<10?'0':'') + date.getDate();
                        return a;
                    }
                },
                {
                    field: 'orderExpireTime', title: '到期时间',
                    formatter: function (value, row, index) {
                        if (value != null){
                            var date = new Date(parseInt(value));
                            var a = date.getFullYear() + '.' + (date.getMonth()+1<10?'0':'') + (date.getMonth() + 1) + '.' + (date.getDate()<10?'0':'') + date.getDate();
                            return a;
                        }
                        else{
                            return "-";
                        }
                    }
                },
                {
                    field: 'orderMoney', title: '垫付金额',
                    formatter: function (value, row, index) {
                        return formatCurrency(value,2);
                    }
                },
                {
                    field: 'orderInterestCycle', title: '垫付期限',
                    formatter: function (value, row, index) {
                        if(row.orderInterestType==1){
                            return value+'天';
                        }else{
                            return value+'个月';
                        }
                    }
                },
                {
                    field: 'orderRepayType', title: '还款方式',
                    formatter: function (value, row, index) {
                        if (value == 1) {
                            return '一次性还本付息';
                        } else if (value == 2) {
                            return '一次性还本按月付息';
                        } else if (value == 3) {
                            return '一次性还本按天付息';
                        } else if (value == 4) {
                            return '按月分期还本付息';
                        } else if (value == 5) {
                            return '按天还本分期付息';
                        } else {
                            return value;
                        }
                    }
                },
                {
                    field: 'orderInterestType', title: '计息方式',
                    formatter: function (value, row, index) {
                        if(row.orderInterestType==1){
                            return '按天';
                        }else{
                            return '按月';
                        }
                    }
                },
                {
                    field: 'orderRate', title: '利率',
                    formatter: function (value, row, index) {
                        return value + "%";
                    }
                },
                {
                    field: 'orderActualServer', title: '服务费',
                    formatter: function (value, row, index) {
                        return formatCurrency(value,2);
                    }
                },
                {field: 'companyContactTel', title: '联系电话'},
                {
                    field: 'orderState', title: '状态',
                    formatter: function (value, row, index) {
                        if(value==0){
                            return '审核中';
                        }else if(value==1){
                            return '撤销';
                        }else if(value==2){
                            return '驳回';
                        }else if(value==3){
                            return '已处理';
                        }else if(value==4){
                            return '融资失败';
                        }else if(value==5){
                            return '待出款';
                        }else if(value==6){
                            return '已放款';
                        }else{
                            return value;
                        }
                    }
                },
                {
                    field: 'orderEditMember', title: '经办人2',
                    formatter: function (value, row, index) {
                        var inputUser;
                        inputUser = "借款受理人：";
                        if (row.orderAuditUser != null){
                            inputUser += row.orderAuditUser
                        }
                        inputUser += "<br>融资处理人：" ;
                        if (row.orderFinanceUser != null){
                            inputUser += row.orderFinanceUser
                        }
                        inputUser += "<br>财务审核人：";
                        if (row.orderLendUser != null){
                            inputUser += row.orderLendUser
                        }
                        return inputUser;
                    }
                },
                {
                    field: 'orderState', title: '操作',
                    formatter: function (value, row, index) {
                        if(row.orderState == 5){
                            return ' <a href="#" onclick="onShowIntentHandle(this,'+row.orderId+','+row.orderMoney+','+row.orderCompanyId+')" >放款审核</a>';
                        }else {
                            return "";
                        }
                    }
                }
            ],
            requestParam: function () {
                return {
                    // 'newsTitle': $('#newsTitle').val(),
                    'companyName': $('#companyName').val(),
                    'orderState': $('#orderStateType').val()
                }
            }
        }
    );

    $("input[name=borrowType]").click(function(){
        showCont();
    });

});

function showCont(){
    switch($("input[name=borrowType]:checked").attr("id")){
        case "borrowType1":
            document.getElementById("rechargeBak").style.display = "none";
            break;
        case "borrowType2":
            document.getElementById("rechargeBak").style.display = "block";
            break;
        default:
            break;
    }
}

/**
 * 提交前数据验证
 */
function checkSubmit() {

}

function republish(newsId, state){
    if(newsId){
        var ii = layer.load();
        $.ajax({
            type: 'post',
            url: BASE_PATH + '/operate/news/republish.htm',
            data: {
                newsId:newsId,
                state: state
            },
            success: function (data) {
                layer.close(ii);
                if (data.success) {
                    if(state == 0){
                        alertInfo('取消发布成功！', function(){
                            window.location.href = BASE_PATH + '/operate/news/index.htm';
                        });
                    }
                    if(state == 1){
                        alertInfo('发布成功！', function(){
                            window.location.href = BASE_PATH + '/operate/news/index.htm';
                        });
                    }
                } else {
                    alertWarn(data.message);
                }
            }
        });
    }
}

/**
 * 放款审核处理弹出框
 */
function onShowIntentHandle(obj,orderId,orderMoney,orderCompanyId) {
    var _this = $(obj);
    var orderNo = _this.parents('tr:eq(0)').find("td:eq(0)").text();
    var companyName = _this.parents('tr:eq(0)').find("td:eq(1)").text();
    var orderAddtime = _this.parents('tr:eq(0)').find("td:eq(2)").text();
    var companyContactTel = _this.parents('tr:eq(0)').find("td:eq(11)").text();
    var index = layer.open({
        type: 2,
        title: '放款审核',
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        move: false,
        area: ['450px', '400px'],
        content: 'getborrowInfo.htm?itemId='+orderNo+'&orderId='+orderId+'&orderMoney='+orderMoney+'&orderCompanyId='+orderCompanyId+'&companyName='+companyName+'&orderAddtime='+orderAddtime+'&companyContactTel='+companyContactTel
    });
}