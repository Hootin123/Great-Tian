var Grid_recharge=null;
var Grid_deposit=null;
var Grid_payoff=null;
var Grid_protocol=null;
var Grid_note=null;
var companyProtocolModifyDialog=null;
$(function(){
    console.log("企业ID："+$('#companyIdHid').val());
    //$("#backBtn").click(function(){
    //
    //});
    var flag = $("input[name='flag']").val();
    var isReturnParamParam=$("#isReturnParamParam").val();
    if(isReturnParamParam==='3'||flag=="flag"){

        $("#company_note").addClass("active");
        $("#recharge").removeClass("active");
        Grid_note = new DataGrid({
            gridId: '#data-list5',
            url: BASE_PATH + '/companynote/page.htm',
            columns: [
                {
                    field: 'note_id',
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index + 1;
                    }
                },{
                    field: 'recordTime',
                    title: '小记时间',
                    formatter: function (value) {
                        return unixToTime(value, "y-m-d h:m:i");
                    }
                },{
                    field: 'noteTitle',
                    title: '小记标题'
                },{
                    field: 'noteContent',
                    title: '小记内容',
                    formatter: function (value) {
                        if(value) {
                            if (value.length > 25) {
                                return "<p style='width:600px;word-break: break-all'>" + value + "</p>";
                            }else{
                                return value;
                            }
                        }
                        return "";
                    }
                },{
                    field: 'sysUserName',
                    title: '记录人'
                }
            ],
            requestParam: function () {
                return {
                    'cId': $('#companyIdHid').val()
                }
            }
        });
    }else{
        Grid_recharge = new DataGrid({
            gridId:'#data-list1',
            url:'companyRechargeDetailSearch.htm',
            columns: [
                {
                    field: 'rowNo',
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index + 1;
                    }
                },
                {field: 'rechargeNumber', title: '充值订单号'},
                {field: 'rechargeAddtime', title: '创建时间',
                    formatter: function (value) {
                        return unixToDate(value);
                    }
                },
                {
                    field: 'rechargeMoney', title: '金额',
                    formatter: function (value, row, index) {
                        return formatCurrency(value,2);
                    }
                },
                //{field: 'rechargeMoney', title: '金额'},
                {field: 'rechargeState', title: '状态',
                    formatter: function (value,row,index) {
                        if(value=='0'){
                            return '待初审';
                        }else if(value=='1'){
                            return '初审失败';
                        }else if(value=='2'){
                            return '初审通过';
                        }else if(value=='3'){
                            return '复审失败';
                        }else if(value=='4'){
                            return '复审通过';
                        }else {
                            return '-';
                        }
                    }
                },
                {field: 'nickName', title: '财务审核人'},
                //{field: 'rechargeAuditFirstTime', title: '处理完结时间',
                //    formatter: function (value) {
                //        return unixToDate(value);
                //    }
                //},
                {field: 'rechargeBank', title: '付款银行'},
                {field: 'bankAccountName', title: '付款账户'},
                {field: 'rechargeBanknumber', title: '付款银行账号'},
                {field: 'rechargeSerialNumber', title: '银行交易流水'},
                {field: 'realAmount', title: '客户实付金额',
                    formatter: function (value,row,index) {
                        return formatCurrency(row.rechargeMoney,2);
                    }
                }
            ],
            requestParam: function () {
                return {
                    'rechargeCompanyId': $('#companyIdHid').val(),
                    'rechargeType':1
                }
            }
        });
    }


    //点击各个页签触发事件
    $("#companyManageDetailUl li").each(function(i){
        $(this).click(function(){
            if(this.id=="recharge"){
                console.log("企业ID："+$('#companyIdHid').val());
                if(Grid_recharge==null){
                    Grid_recharge = new DataGrid({
                        gridId:'#data-list1',
                        url:'companyRechargeDetailSearch.htm',
                        columns: [
                            {
                                field: 'rowNo',
                                title: '序号',
                                formatter: function (value, row, index) {
                                    return index + 1;
                                }
                            },
                            {field: 'rechargeNumber', title: '充值订单号'},
                            {field: 'rechargeAddtime', title: '创建时间',
                                formatter: function (value) {
                                    return unixToDate(value);
                                }
                            },
                            {
                                field: 'rechargeMoney', title: '金额',
                                formatter: function (value, row, index) {
                                    return formatCurrency(value,2);
                                }
                            },
                            //{field: 'rechargeMoney', title: '金额'},
                            {field: 'rechargeState', title: '状态',
                                formatter: function (value,row,index) {
                                    if(value=='0'){
                                        return '待初审';
                                    }else if(value=='1'){
                                        return '初审失败';
                                    }else if(value=='2'){
                                        return '初审通过';
                                    }else if(value=='3'){
                                        return '复审失败';
                                    }else if(value=='4'){
                                        return '复审通过';
                                    }else {
                                        return '-';
                                    }
                                }
                            },
                            {field: 'nickName', title: '财务审核人'},
                            //{field: 'rechargeAuditFirstTime', title: '处理完结时间',
                            //    formatter: function (value) {
                            //        return unixToDate(value);
                            //    }
                            //},
                            {field: 'rechargeBank', title: '付款银行'},
                            {field: 'bankAccountName', title: '付款账户'},
                            {field: 'rechargeBanknumber', title: '付款银行账号'},
                            {field: 'rechargeSerialNumber', title: '银行交易流水'},
                            {field: 'realAmount', title: '客户实付金额',
                                formatter: function (value,row,index) {
                                    return formatCurrency(row.rechargeMoney,2);
                                }
                            }
                        ],
                        requestParam: function () {
                            return {
                                'rechargeCompanyId': $('#companyIdHid').val(),
                                'rechargeType':1
                            }
                        }
                    });
                }else {
                    Grid_recharge.onQuery();
                }
            }else if(this.id=="deposit"){
                if(Grid_deposit==null){
                    console.log("企业ID："+$('#companyIdHid').val());
                    Grid_deposit = new DataGrid({
                        gridId:'#data-list2',
                        url:'companyRechargeDetailSearch.htm',
                        columns: [
                            {
                                field: 'rowNo',
                                title: '序号',
                                formatter: function (value, row, index) {
                                    return index + 1;
                                }
                            },
                            {field: 'rechargeNumber', title: '订单编号'},
                            {field: 'rechargeAddtime', title: '申请时间',
                                formatter: function (value) {
                                    return unixToDate(value);
                                }
                            },
                            {field: 'rechargeAuditFirstTime', title: '处理完结时间',
                                formatter: function (value) {
                                    return unixToDate(value);
                                }
                            },
                            {
                                field: 'rechargeMoney', title: '金额',
                                formatter: function (value, row, index) {
                                    return formatCurrency(value,2);
                                }
                            },
                             //{field: 'rechargeMoney', title: '金额'},
                            {field: 'rechargeBank', title: '提现银行'},
                            {field: 'bankAccountName', title: '提现账户名称'},
                            {field: 'rechargeBanknumber', title: '提现账号'},
                            {field: 'bankSubbranch', title: '开户支行'},
                            {field: 'rechargeSerialNumber', title: '银行交易流水'},
                            {field: 'rechargeState', title: '状态',
                                formatter: function (value,row,index) {
                                    if(value=='0'){
                                        return '待初审';
                                    }else if(value=='1'){
                                        return '初审失败';
                                    }else if(value=='2'){
                                        return '初审通过';
                                    }else if(value=='3'){
                                        return '复审失败';
                                    }else if(value=='4'){
                                        return '复审通过';
                                    }else {
                                        return '-';
                                    }
                                }
                            },
                            {field: 'nick_name', title: '财务处理人'},
                            {field: 'rechargeBak', title: '备注'}
                        ],
                        requestParam: function () {
                            return {
                                'rechargeCompanyId': $('#companyIdHid').val(),
                                'rechargeType':2
                            }
                        }
                    });
                }else{
                    console.log("企业ID："+$('#companyIdHid').val());
                    Grid_deposit.onQuery();
                }
            }else if(this.id=="payoff"){
                if(Grid_payoff==null){
                    console.log("企业ID："+$('#companyIdHid').val());
                    Grid_payoff = new DataGrid({
                        gridId:'#data-list3',
                        url:'companyPayoffDetailSearch.htm',
                        columns: [
                            {
                                field: 'rowNo',
                                title: '序号',
                                formatter: function (value, row, index) {
                                    return index + 1;
                                }
                            },
                            {field: 'rechargeNumber', title: '发薪订单号'},
                            {field: 'excelAddtime', title: '申请日期',
                                formatter: function (value) {
                                    return unixToDate(value);
                                }
                            },
                            {field: 'excelPayday', title: '发薪日',
                                formatter: function (value) {
                                    return unixToDate(value);
                                }
                            },
                            {
                                field: 'excelSalaryTotal', title: '发薪金额',
                                formatter: function (value, row, index) {
                                    return formatCurrency(value,2);
                                }
                            },
                            //{field: 'excelSalaryTotal', title: '发薪金额'},
                            {field: 'excelPayrollNumber', title: '发薪人数'},
                            {field: 'excelState', title: '状态',
                                formatter: function (value,row,index) {
                                    if(value=='0'){
                                        return '待初审';
                                    }else if(value=='1'){
                                        return '初审完成';
                                    }else if(value=='2'){
                                        return '复审完成';
                                    }else if(value=='3'){
                                        return '审批失败';
                                    }else if(value=='4'){
                                        return '审批成功';
                                    }else if(value=='5'){
                                        return '工资已发';
                                    }else {
                                        return '-';
                                    }
                                }
                            }
                        ],
                        requestParam: function () {
                            return {
                                'excelCompanyId': $('#companyIdHid').val()
                            }
                        }
                    });
                }else{
                    console.log("企业ID："+$('#companyIdHid').val());
                    Grid_payoff.onQuery();
                }
            }else if(this.id=="protocol"){
                if(Grid_protocol==null){
                    console.log("企业ID："+$('#companyIdHid').val());
                    Grid_protocol = new DataGrid({
                        gridId:'#data-list4',
                        url:'companyProtocolDetailSearch.htm',
                        columns: [
                            {
                                field: 'rowNo',
                                title: '序号',
                                formatter: function (value, row, index) {
                                    return index + 1;
                                }
                            },
                            //{field: 'protocolCode', title: '协议编号'},
                            {field: 'protocolContractType', title: '协议类型',
                                formatter: function (value) {
                                    if(value==1){
                                        return "代发协议";
                                    }else if(value==2){
                                        return "垫发协议";
                                    }else if(value==3){
                                        return "代缴社保协议";
                                    }else if(value==4){
                                        return "报销管理协议";
                                    }else{
                                        return "-";
                                    }
                                }
                            },
                            {field: 'protocolExpireTime', title: '签约--到期时间',
                                formatter: function (value,row,index) {
                                    return unixToDate(row.protocolContractTime)+"--"+unixToDate(row.protocolExpireTime);
                                }
                            },
                            //{field: 'protocolContractTime', title: '签约时间',
                            //    formatter: function (value) {
                            //        return unixToDate(value);
                            //    }
                            //},
                            //{field: 'protocolExpireTime', title: '到期时间',
                            //    formatter: function (value) {
                            //        return unixToDate(value);
                            //    }
                            //},
                            {field: 'protocolCurrentStatus', title: '协议状态',
                                formatter: function (value,row,index) {
                                    if(value=='1'){
                                        return '待审批';
                                    }else if(value=='2'){
                                        return '签约';
                                    }else if(value=='3'){
                                        return '即将到期';
                                    }else if(value=='4'){
                                        return '合约到期';
                                    }else if(value=='5'){
                                        return '冻结';
                                    }else {
                                        return '-';
                                    }
                                }
                            },
                            {field: 'protocolLinkmanName', title: '业务联系人'},
                            {field: 'protocolLinkmanPhone', title: '业务联系人手机'},
                            {field: 'protocolPaperNo', title: '纸质文件编号'},
                            {field: 'protocolBusinessManager', title: '业务经理'},
                            {field: 'protocolRate', title: '签约补充信息',
                                formatter: function (value,row,index) {
                                    var protocolContractType=row.protocolContractType;
                                    if(protocolContractType==2){
                                        return "垫付利率:"+value+",服务费利率:"+row.protocolServeRate+",垫付比例:"+row.protocolScale;
                                    }else{
                                        return "";
                                    }
                                }
                            }
                        ],
                        requestParam: function () {
                            return {
                                'protocolCompanyId': $('#companyIdHid').val()
                            }
                        }
                    });
                }else{
                    console.log("企业ID："+$('#companyIdHid').val());
                    Grid_protocol.onQuery();
                }
            }else if(this.id=="company_note") {
                if (Grid_note == null) {
                    console.log("企业ID：" + $('#companyIdHid').val());
                    Grid_note = new DataGrid({
                        gridId: '#data-list5',
                        url: BASE_PATH + '/companynote/page.htm',
                        columns: [
                            {
                                field: 'note_id',
                                title: '序号',
                                formatter: function (value, row, index) {
                                    return index + 1;
                                }
                            },{
                                field: 'recordTime',
                                title: '小记时间',
                                formatter: function (value) {
                                    return unixToTime(value, "y-m-d h:m:i");
                                }
                            },{
                                field: 'noteTitle',
                                title: '小记标题'
                            },{
                                field: 'noteContent',
                                title: '小记内容',
                                formatter: function (value) {
                                    if(value) {
                                        if (value.length > 25) {
                                            return "<p style='width:600px;word-break: break-all'>" + value + "</p>";
                                        }else{
                                            return value;
                                        }
                                    }
                                    return "";
                                }
                            },{
                                field: 'sysUserName',
                                title: '记录人'
                            }
                        ],
                        requestParam: function () {
                            return {
                                'cId': $('#companyIdHid').val()
                            }
                        }
                    });
                }else{
                    Grid_note.onQuery();
                }
            }
        });


    });


    //显示征信资料库
    $("#downloadFile").click(function(){
        $.ajax({
            type: 'post',
            url: '../protocolManage/companyCreditSearch.htm',
            data: {"companyId":$('#companyIdHid').val()},
            //data: {"companyId":259},
            //async: false,
            success: function (data) {
                console.log(data);
                if (data.success) {
                    location.href = "../protocolManage/downloadFile.htm?currentPath="+data.data;
                } else {
                    layer.alert(data.message,{title:'提示信息'});
                }
            }
        });
    });
});

/**
 * 企业协议修改弹出框
 */
function onModifyCompanyProtocol(companyId) {
    companyProtocolModifyDialog = layer.open({
        type: 2,
        title: '修改协议',
        //maxmin: true,
        shadeClose: false, //点击遮罩关闭层
        area: ['450px', '300px'],
        content: 'companyModifyProtocol.htm?companyId='+companyId
    });
    //窗口最大化
    //layer.full(index);
}

function queryPayStatus(orderId, companyId){
    $("#paystatus").html("查询中...");
    $.ajax({
        type: 'post',
        url: BASE_PATH + '/companyManage/queryPayStatus.htm',
        data: {"orderId":orderId, "companyId":companyId},
        //async: false,
        success: function (data) {
            if (data.success) {
                data =data.data;
                if(data.data.companyPayStatus == 0){
                    $("#paystatus").html("等待中")
                }else if(data.data.companyPayStatus == 1){
                    $("#paystatus").html("成功")
                }else if(data.data.companyPayStatus == 2){
                    $("#paystatus").html("失败("+ data.data.companyPayReason +")")
                }else if(data.data.companyPayStatus == 3){
                    $("#paystatus").html("退款")
                }else{
                    $("#paystatus").html("请重试")
                }
                console.log(data.data);
            } else {
                layer.alert(data.message,{title:'提示信息'});
            }
        },
        error: function () {
            $("#paystatus").html("查询失败")
        }
    });
}