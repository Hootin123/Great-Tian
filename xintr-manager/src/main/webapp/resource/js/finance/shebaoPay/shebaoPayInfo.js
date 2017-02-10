var dataGrid;
$(function(){
    initData();
    dataGrid = new DataGrid({
            columns: [
                //{field: 'checkbox', checkbox: true},
                {
                    field: 'rowNo',
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index + 1;
                    }
                },
                {field: 'companyName', title: '企业名称', visible: false},
                // {field: 'orderNumber', title: '订单编号'},
                {field: 'rechargeNumber', title: '订单编号'},
                {field: 'shebaotongServiceNumber', title: '服务商编号'},
                {field: 'joinCityName', title: '参保地区'},
                {field: 'orderDate', title: '缴纳日期',
                    formatter: function (value) {
                        return unixYYYYLineMMToDate(value);
                    }
                },
                {field: 'lastTime', title: '订单关闭时间',
                    formatter: function (value) {
                        return unixToTime(value,'y-m-d h:m:i');
                    }
                },
                {field: 'shebaotongTotalAmount', title: '向供应商付款'},
                {field: 'shebaotongPayBankName', title: '开户行'},
                {field: 'shebaotongPayCompanyAccount', title: '企业账户'},
                {field: 'shebaotongPayBankNo', title: '企业账号'},
                {field: 'shebaotongPayBatch', title: '银行交易流水号'},
                {field: 'shebaotongPayState', title: '状态',
                    formatter: function (value,row) {
                        var status=row.status;
                        if(status==4){
                            return '待付款';
                        }else if(status==5 || status==6 || status==8){
                            return '已付款';
                        }else{
                            return "-";
                        }
                    }},
                {field: 'userName', title: '财务处理人'},
                {field: 'options', title: '操作',
                    formatter: function (value,row,index) {
                        var status=row.status;
                        if(status==4){
                            return  "<a href='#' onclick='onShowAddPayInfo("+row.id+")'  style='color: #00b7ee !important;'>付款</a>";
                        }else{
                            return "-";
                        }
                    }
                }
            ],
            requestParam: function () {
                return {
                    'orderNumber': $('[name=orderNumber]').val(),
                    'companyId': $('[name=companyId]').val(),
                    'selType': $('[name=selType]').val()
                }
            }
        }
    );

    $("#selBtn").click(function(){
        $('[name=selType]').val('0');
        initData();
        dataGrid.onQuery();
    });
});



function unixYYYYLineMMToDate(UnixTime) {
    if(UnixTime){
        var dateObj = new Date(UnixTime);
        return ''+dateObj.getFullYear() + '-' +(dateObj.getMonth() +1 );
    }
    return "";
}

function initData(){
    $.ajax({
        type: 'post',
        url: BASE_PATH+'/shebaoPayInfo/queryCount.htm',
        // data: queryParams,
        async:true,
        success: function (data) {
            console.log(data);
            if (data.success) {
                $("#allInfoCount").text(data.data);
                $("#noPayCount").text(data.message);
            } else {
                // layer.alert(data.message,{title:'处理结果'});
            }
        }
    });
}
/**
 * 展示所有账单列表
 */
function showAllPayInfo(){
    $('[name=selType]').val('2');
    initData();
    dataGrid.onQuery();
}
/**
 * 展示未付款列表
 */
function showNoPayInfo(){
    $('[name=selType]').val('1');
    initData();
    dataGrid.onQuery();
}
/**
 * 添加付款信息页面
 * @param protocolId
 */
function onShowAddPayInfo(companyOrderId) {
    var index = layer.open({
        type: 2,
        title: '社保供应商付款--社保通',
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['500px', '550px'],
        content: BASE_PATH+'/shebaoPayInfo/toShebaoAddPage.htm?companyOrderId='+companyOrderId
    });
    //窗口最大化
    //layer.full(index);
}