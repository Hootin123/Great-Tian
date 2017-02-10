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
            {field: 'companyId', title: '企业编号',visible: false},
            {field: 'companyName', title: '公司名称',
                formatter: function (value,row,index) {
                    return "<a href='#' onclick='onShowCompanyDetail("+row.companyId+")' >"+value+"</a>";
                }
            },
            {field: 'memberLogName', title: '超级管理员用户名'},
            {field: 'depositBankName', title: '提现银行'},
            {field: 'depositBankAccountName', title: '提现账户名'},
            {field: 'depositBankNumber', title: '提现账号'},
            {field: 'depositSubBankName', title: '开户支行名称'},
            {field: 'aaa', title: '操作',
                formatter: function (value,row,index) {

                    var depositId=row.depositId;
                    console.log(depositId);
                    var showStr="";
                    if(depositId==null || depositId==undefined || depositId==0 ||depositId==""){
                        showStr = "<a href='#' onclick='onShowAddDepositCompany("+row.companyId+","+row.memberId+")' >添加</a><br/>";
                    }else{
                        showStr = "<a href='#' onclick='onShowEditDepositCompany("+depositId+")' >修改</a><br/>";
                    }
                    //a += "<a href='#' data-cid='"+row.companyId+"' data-cname="+row.companyName+" onclick='onShowAddCompanyNote(this)' >联系小记</a>";
                    return showStr;
                }
            }
        ],
        requestParam: function () {
            return {
                'approveState': $('#approveState').val(),
                'ptotocolState': $('#ptotocolState').val(),
                'authState': $('#authState').val(),
                'companyName': $('#companyName').val()
            }
        }
    });
});

/**
 * 企业详情弹出框
 */
function onShowCompanyDetail(companyId) {
    console.log(companyId);
    var index = layer.open({
        type: 2,
        title: '企业信息',
        maxmin: true,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: '../companyManage/companyManageDetail.htm?isReturnParam=2&companyId='+companyId
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 提现账户添加弹出框
 */
function onShowAddDepositCompany(companyId,memberId) {
    var index = layer.open({
        type: 2,
        title: '添加提现账户',
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: 'toCompanyDepositCreatePage.htm?memberId='+memberId+'&companyId='+companyId
    });
    //窗口最大化
    //layer.full(index);
}

/**
 * 提现账户修改弹出框
 */
function onShowEditDepositCompany(depositId) {
    var index = layer.open({
        type: 2,
        title: '修改提现账户',
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: 'toCompanyDepositEditPage.htm?depositId='+depositId
    });
    //窗口最大化
    //layer.full(index);
}