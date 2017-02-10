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
            {field: 'companyId', title: '企业编号'},
            {field: 'companyName', title: '企业名称',
                formatter: function (value,row,index) {
                    return "<a href='#' onclick='onShowCompanyDetail("+row.companyId+")' >"+value+"</a>";
                }
            },
            {field: 'companyContactTel', title: '手机号'},
            {field: 'companyAddime', title: '注册时间',
                formatter: function (value) {
                    return unixToTime(value);
                }
            },
            {field: 'companyAddress', title: '单位地址'},
            {field: 'companyEmployCount', title: '员工人数'},
            //{field: 'totalAmount', title: '账户余额'},
            {
                field: 'totalAmount', title: '账户余额',
                formatter: function (value, row, index) {
                    if(value!=undefined && value!=''){
                        return formatCurrencyTwoPoint(value,2);
                    }else{
                        return "-";
                    }
                }
            },
            {field: 'totalAmount1', title: '授信额度', visible: false},
            {field: 'totalAmount2', title: '可用额度', visible: false},
            {field: 'protocolState', title: '签约状态'},
            {field: 'companyIsauth', title: '实名状态',
                formatter: function (value,row,index) {
                    if(value==null || value==undefined ||value=="" ||value==0 ){
                        var companyCertificationAmount=row.companyCertificationAmount;
                        var showStr="";
                        if(companyCertificationAmount!=null && companyCertificationAmount!=undefined && companyCertificationAmount!="" && companyCertificationAmount!=0){
                            showStr="未认证("+companyCertificationAmount.toFixed(2)+")";
                        }else{
                            showStr="未认证";
                        }
                        return showStr;
                        //return "未认证";
                    }else if(value==1){
                        return "已认证";
                    }else if(value==2){
                        var companyCertificationAmount=row.companyCertificationAmount;
                        var showStr="";
                        if(companyCertificationAmount!=null && companyCertificationAmount!=undefined && companyCertificationAmount!="" && companyCertificationAmount!=0){
                            showStr="待认证("+companyCertificationAmount.toFixed(2)+")";
                        }else{
                            showStr="待认证";
                        }
                        return showStr;
                    }else if(value==3){
                        return "认证失败";
                    }else{
                        return "-";
                    }
                }
            },
            {field: 'companyAuditStatus', title: '审核状态',
                formatter: function (value,row,index) {
                    if(value==0){
                        return "审核中";
                    }else if(value==1){
                        return "审核驳回";
                    }else if(value==2){
                        return "审核通过";
                    }else{
                        return "-";
                    }
                }
            },
            {field: 'companyContactPlace', title: '邀请码'},
            {field: 'aaa', title: '操作',
                formatter: function (value,row,index) {
                    var companyAuditStatus=row.companyAuditStatus;
                    var a="";
                    if(companyAuditStatus==null || companyAuditStatus==undefined || companyAuditStatus==0 ||companyAuditStatus==1){
                        a = "<a href='#' onclick='onShowApproveCompany("+row.companyId+")' >审核</a><br/>";
                    }else{
                        a = "<a style='color:#8c8c8c;'>审核</a><br/>";
                    }
                    a += "<a href='#' data-cid='"+row.companyId+"' data-cname="+row.companyName+" onclick='onShowAddCompanyNote(this)' >联系小记</a>";
                    return a;
                }
            }
        ],
        requestParam: function () {
            return {
                'companyName': $('#companyManageInfo_companyName').val(),
                'protocolStateInt': $('#protocolStateInt').val(),
                'companyAuditStatus': $('#companyAuditStatus').val(),
                'companyIsauth': $('#companyIsauth').val(),
            }
        }
    });
});

/**
 * 企业详情弹出框
 */
function onShowCompanyDetail(companyId) {
    var index = layer.open({
        type: 2,
        title: '企业信息',
        maxmin: true,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: 'companyManageDetail.htm?isReturnParam=2&companyId='+companyId
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 联系小记弹出框
 */
function onShowAddCompanyNote(t) {
    window.companyDetail = {};
    window.companyDetail.id = $(t).data("cid");
    window.companyDetail.name = $(t).data("cname");
    var index = layer.open({
        type: 2,
        title: '添加联系小记',
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['400px', '470px'],
        content: BASE_PATH + '/companynote/add.htm'
    });
}

/**
 * 审核弹出框
 */
function onShowApproveCompany(companyId) {
    var index = layer.open({
        type: 2,
        title: '企业资料审核',
        maxmin: true,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: 'toCompanyApprovePage.htm?companyId='+companyId
    });
    //窗口最大化
    layer.full(index);
}

function formatCurrencyTwoPoint(s, n) {
    var t;
    t = s;
    if ($.isNumeric(t)){
        t = parseFloat(t).toFixed(n);
    }

    return t;
}