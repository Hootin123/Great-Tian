var dataGrid;
$(function () {
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
                {field: 'customerId', title: '主键', visible: false},
                {field: 'customerTurename', title: '姓名'},
                {field: 'customerPhone', title: '手机号码'},
                {field: 'customerIdcard', title: '身份证号码'},
                {field: 'customerBanknumber', title: '工资卡'},
                {field: 'customerCompanyName', title: '公司'},
                //{field: 'totalAmount', title: '账户余额'},
                {
                    field: 'totalAmount', title: '账户余额',
                    formatter: function (value, row, index) {
                        return formatCurrencyForCustomer(value,2);
                    }
                },
                {field: 'customerTruenameIsauthentication', title: '实名认证状态',
                    formatter: function (value) {
                        if(value=="0"){
                            return '未认证';
                        }else if(value=="1"){
                            return '已认证';
                        }else if(value=="2"){
                            return '待认证';
                        }else if(value=="3"){
                            return '认证失败';
                        }
                        else{
                            return "-";
                        }
                    }
                },
                {
                    field: 'updateDateTime', title: '操作',
                    formatter: function (value,row,index) {
                        return "<a href='#' onclick='onShowStaffDetail("+row.customerId+")' >详情</a>";
                        //return "<a href='staffManageDetail.htm?customerId="+row.customerId+"'>详情</a>";
                    }
                }
            ],
            requestParam: function () {
                return {
                    'customerTruenameIsauthentication': $('#staffmanageinfo_customerTruenameIsauthentication').val(),
                    'customerCompanyName': $('#customerCompanyName').val(),
                    'customerTurename': $('#customerTurename').val(),
                }
            }
        }
    );
});

/**
 * 成员详情弹出框
 */
function onShowStaffDetail(staffId) {
    var index = layer.open({
        type: 2,
        title: '员工信息',
        maxmin: true,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: 'staffManageDetail.htm?customerId='+staffId
    });
    //窗口最大化
    layer.full(index);
}
//格式化金额，不带千位符
function formatCurrencyForCustomer(s, n) {
    var t;
    t = s;
    if ($.isNumeric(t)){
        t = parseFloat(t).toFixed(n);
    }

    return t;
}