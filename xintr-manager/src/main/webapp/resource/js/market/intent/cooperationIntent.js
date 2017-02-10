var dataGrid;
$(function(){
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
                {field: 'itemId', title: '主键', visible: false},
                {field: 'itemType', title: '意向类型',
                    formatter: function (value) {
                        if(value=="1"){
                            return '商务合作';
                        }else if(value=="2"){
                            return '新用户注册';
                        }else if(value=="3"){
                            return '提交工资单';
                        }else if(value=="4"){
                            return '签约到期前30天';
                        }else if(value=="5"){
                            return '融资意向';
                        }else if(value=="6"){
                            return '明确期望签约';
                        }else{
                            return "-";
                        }
                    }
                },
                {field: 'collaborationCompanyId', title: '企业编号'},
                {field: 'itemName', title: '企业名称',
                    formatter: function (value,row) {
                        var companyId=row.collaborationCompanyId;
                        if(companyId!=null && companyId!=undefined && companyId!="" && companyId!=0){
                            return row.companyName;
                        }else{
                            return row.itemName;
                        }
                    }
                },
                {field: 'itemMobile', title: '联系手机',
                    formatter: function (value,row) {
                        var companyId=row.collaborationCompanyId;
                        if(companyId!=null && companyId!=undefined && companyId!="" && companyId!=0){
                            return row.memberPhone;
                        }else{
                            return row.itemMobile;
                        }
                    }
                },
                {field: 'adddate', title: '首次发起时间',
                    formatter: function (value) {
                        return unixYYYYMMDDToDate(value);
                    }
                },
                {field: 'itemTimes', title: '发起次数'},
                {field: 'recallTime', title: '处理时间',
                    formatter: function (value) {
                        return unixYYYYMMDDToDate(value);
                    }
                },
                {field: 'sign', title: '状态',
                    formatter: function (value) {
                        if(value=="1"){
                            return '待联系';
                        }else if(value=="2"){
                            return '已联系';
                        }else if(value=="3"){
                            return '已签约';
                        }else if(value=="4"){
                            return '关闭';
                        }else{
                            return "-";
                        }
                    }
                },
                {field: 'businessMaName', title: '业务经理'},
                {
                    field: 'options', title: '操作',
                    formatter: function (value,row,index) {
                        //console.log(row);
                        var sign=row.sign;
                        var a = "";
                        if(sign==1 || sign==2){
                            a = "<a href='#' onclick='onShowIntentHandle("+row.itemId+")' >处理意向</a>";
                        }else{
                            a = "<font color='gray'>处理意向</font>";
                        }
                        if (row.itemType != "1"){
                            a += "<br/><a href='#' onclick='onShowCompanyDetail("+row.collaborationCompanyId+")' >查看联系小记</a>";
                        }

                        return a;
                    }
                }
            ],
            requestParam: function () {
                return {
                    'itemType': $('#itemType').val(),
                    'sign': $('#sign').val(),
                    'businessMaName': $('#businessMaName').val(),
                    'itemName': $('#itemName').val(),
                }
            }
        }
    );

    $("#exportBtn").click(function(){
        //location.href = "batchExportCollaboration.htm";
        onShowExportCondition();
    });
});

/**
 * 合作意向处理弹出框
 */
function onShowIntentHandle(itemId) {
    var index = layer.open({
        type: 2,
        title: '处理合作意向',
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['520px', '580px'],
        content: 'toCompanyIntentHandlePage.htm?itemId='+itemId
    });
    //窗口最大化
    //layer.full(index);
}

/**
 * 企业详情弹出框
 */
function onShowCompanyDetail(companyId) {
    var flag = "contactSubtotal";
    var index = layer.open({
        type: 2,
        title: '企业信息',
        maxmin: true,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: BASE_PATH + '/companyManage/companyManageDetail.htm?isReturnParam=3&companyId='+companyId+"&flag="+flag
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 导出过滤条件
 */
function onShowExportCondition() {
    var index = layer.open({
        type: 2,
        title: '导出',
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: BASE_PATH + '/companyIntent/cooperationIntentExport.htm'
    });
    //窗口最大化
    //layer.full(index);
}

function unixYYYYMMDDToDate(UnixTime) {
    if(UnixTime){
        var dateObj = new Date(UnixTime);
        return ''+dateObj.getFullYear() + '-' +(dateObj.getMonth() +1 )+'-'+dateObj.getDate();
    }
    return "";
}