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
                    field: 'orderEditMember', title: '经办人',
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
                        if(row.orderState == 0){
                            return  "<a data-toggle='modal' href='index.htm#modal-form' onclick='showAudit(this,"+row.orderId+")'>借款受理</a></br>" +
                                '融资处理';
                        }else if(row.orderState == 3){
                            return  '借款受理</br>' +
                                " <a data-toggle='modal' href='index.htm#modal-form1' onclick='showAudit(this,"+row.orderId+")'>融资处理</a>";
                        }else{
                            return  '借款受理</br>' +
                                ' 融资处理';
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
    $("input[name=equityType]").click(function(){
        showBorrow();
    });

});

function showCont(){
    switch($("input[name=borrowType]:checked").attr("id")){
        case "borrowType1":
            document.getElementById("orderAuditRemark").style.display = "none";
            break;
        case "borrowType2":
            document.getElementById("orderAuditRemark").style.display = "block";
            break;
        default:
            break;
    }
}

function showBorrow(){
    switch($("input[name=equityType]:checked").attr("id")){
        case "equityType1":
            document.getElementById("borrowShow").style.display = "block";
            document.getElementById("borrowNone").style.display = "none";
            break;
        case "equityType2":
            document.getElementById("borrowNone").style.display = "block";
            document.getElementById("borrowShow").style.display = "none";
            break;
        default:
            break;
    }
}

/**
 * 提交前数据验证
 */
function checkSubmit() {

    var aInp = $("input[name='borrowAccountMoneyValue']");
    var resul = 0;
    var isTrue = 1;

    document.getElementById("payOperatequery").disabled = true;
    var equityTypeValue=$("input[id='equityType1']:checked").val();
    if(equityTypeValue ==0){
        for(var i = 0;i<aInp.length;i++){
            if(!aInp.eq(i).val()){
                alert('请输入正确金额！');
                isTrue = 0;
                break;
            }else {
                resul += parseFloat(aInp.eq(i).val());
            }
        }
        if (isTrue == 1){
            if(resul!=parseFloat($('#orderMoneyShow').html())){
                alert('到账资金和借款总额不符！!');
            }else{
                $("#orderStateValue").val(5);
                $.ajax({
                    type:"POST",
                    url:"borrowInfoAdd.htm",
                    dataType:"json",
                    data: $("#borrowInfoForm").serialize(),
                    async: true,
                    success: function(data){
                        if (data.success) {
                            document.getElementById("payOperatequery").disabled = false;
                            window.location.reload();
                        }
                    }
                });
            }
        }
        else{
            document.getElementById("payOperatequery").disabled = false;
        }

        document.getElementById("payOperatequery").disabled = false;
    }else{
        $("#orderStateValue").val(4);
        $.ajax({
            type:"POST",
            url:"borrowInfoEdit.htm",
            dataType:"json",
            data: $("#borrowInfoForm").serialize(),
            async: true,
            success: function(data){
                if (data.success) {
                    document.getElementById("payOperatequery").disabled = false;
                    window.location.reload();
                }
            }
        });
        document.getElementById("payOperatequery").disabled = false;
    }

}

/**
 * 提交前数据验证
 */
function borrowTypeUpdateSubmit() {
    var equityTypeValue=$("input[id='borrowType1']:checked").val();
    if(equityTypeValue ==0){
        $("#orderState").val(3);
        $("#orderAuditRemark").val("");
    }else{
        $("#orderState").val(2);
    }
    $.ajax({
        type:"POST",
        url:"borrowInfoEdit.htm",
        dataType:"json",
        data: $("#auditForm").serialize(),
        async: true,
        success: function(data){
            if (data.success) {
                window.location.reload();
            }
        }
    });
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

function showAudit(obj,orderId) {

    var _this = $(obj);
    var orderNo = _this.parents('tr:eq(0)').find("td:eq(0)").text();
    var orderMoney = _this.parents('tr:eq(0)').find("td:eq(5)").text();
    var orderInterestType = _this.parents('tr:eq(0)').find("td:eq(6)").text();
    var orderInterestCycle =0;
    $("#borrowOrderNumber").val(orderNo);
    $("#orderId").val(orderId);
    $("#orderIdVlaue").val(orderId);
    $("#orderMoney").val(orderMoney);
    $("#orderMoneyShow").text(formatCurrency(orderMoney,2));
    if(orderInterestType.indexOf("天") > 0 ){
        $("#orderInterestType").val(1);
        orderInterestType = orderInterestType.replace("天","");
        $("#orderInterestCycle").val(orderInterestType);
    }
    else{
        $("#orderInterestType").val(2);
        orderInterestType = orderInterestType.replace("个月","");
        orderInterestType = orderInterestType.replace("月","");
        $("#orderInterestCycle").val(orderInterestType);

    }
    $('input[name=borrowType]').get(0).checked = true;
    document.getElementById("orderAuditRemark").style.display = "none";

}

