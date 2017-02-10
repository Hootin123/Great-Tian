var Grid;
var addProtocolGrid;
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
            {field: 'protocolId', title: '协议ID',visible: false},
            {field: 'operationShowState', title: '是否显示签约',visible: false},
            {field: 'companyName', title: '企业名称',
                formatter: function (value,row,index) {
                    return "<a href='../companyManage/companyManageDetail.htm?isReturnParam=1&companyId="+row.companyId+"'>"+value+"</a>";
                }
            },
            {field: 'companyContactTel', title: '手机号'},
            {field: 'protocolCode', title: '协议编号'},
            {field: 'protocolContractType', title: '协议类型',
                formatter: function (value,row,index) {
                    if(value=='1'){
                        return '代发协议';
                    }else if(value=='2'){
                        return '垫发协议';
                    }else if(value=='3'){
                        return '代缴社保协议';
                    }else if(value=='4'){
                        return '报销管理协议';
                    }else {
                        return '-';
                    }
                }
            },
            {field: 'protocolContractTime', title: '签约时间',
                formatter: function (value) {
                    return unixToDate(value);
                }
            },
            {field: 'protocolExpireTime', title: '到期时间',
                formatter: function (value) {
                    return unixToDate(value);
                }
            },
            {field: 'protocolLinkmanName', title: '业务联系人'},
            {field: 'protocolLinkmanPhone', title: '业务联系人手机'},
            {field: 'protocolPaperNo', title: '纸质文件编号'},
            {field: 'protocolBusinessManager', title: '业务经理'},
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
            }
            /*{field: 'options', title: '操作',
                formatter: function (value,row,index) {
                    var protocolCurrentStatus=row.protocolCurrentStatus;//协议状态 1待审批 2签约 3即将到期 4合约到期 5冻结
                    var operationShowState=row.operationShowState;//后台获取的是否显示签约状态 1 显示代发签约 2显示垫发签约 3不显示签约
                    var showStr="";
                    if(protocolCurrentStatus==2||protocolCurrentStatus==3||protocolCurrentStatus==5){//冻结\签约\即将到期才能显示修改协议状态
                        showStr+="<a href='#' onclick='onShowProtocolModify("+row.protocolId+")' >修改协议状态</a><br/>";
                    }
                    if(operationShowState==1){
                        showStr+="<a href='#' onclick='onShowProtocolEntering("+row.companyId+","+operationShowState+")' >代发签约</a>"
                    }else if(operationShowState==2){
                        showStr+="<a href='#' onclick='onShowProtocolEntering("+row.companyId+","+operationShowState+")' >垫发签约</a>"
                    }
                    if(showStr!=null && showStr!=""){
                        return showStr;
                    }else{
                        return "-";
                    }
                }
            }*/
        ],
        requestParam: function () {
            return {
                'protocolCurrentStatus': $('#protocolCurrentStatus').val(),
                'protocolContractType': $('#protocolContractType').val(),
                'protocolBusinessManager': $('#protocolBusinessManager').val(),
                'companyName': $('#companyName').val()
            }
        }
    });

    addProtocolGrid = new DataGrid({
        gridId:'#addProtocolTab',
        url:'queryCompanysProtocol.htm',
        columns: [
            [
                {
                    field: 'rowNo',
                    title: '序号',
                    colspan: 1,
                    rowspan: 2,
                    valign:'middle',
                    formatter: function (value, row, index) {
                        return index + 1;
                    }
                },
                {field: 'companyName', title: '企业名称',colspan: 1, rowspan: 2,valign:'middle',
                    formatter: function (value,row,index) {
                        return "<a href='../companyManage/companyManageDetail.htm?isReturnParam=1&companyId="+row.companyId+"'>"+value+"</a>";
                    }
                },
                {field: 'companyId', title: '企业编号',colspan: 1, rowspan: 2,valign:'middle'},
                {field: 'companyContactTel', title: '手机号',colspan: 1, rowspan: 2,valign:'middle'},
                {title: '签约记录',colspan: 2, rowspan: 1,valign:'middle',align:'center'},
                {field: 'protocolLinkmanName', title: '业务联系人',colspan: 1, rowspan: 2,valign:'middle'},
                {field: 'protocolLinkmanPhone', title: '业务联系人手机',colspan: 1, rowspan: 2,valign:'middle'},
                {field: 'protocolPaperNo', title: '纸质文件编号',colspan: 1, rowspan: 2,valign:'middle'},
                {field: 'protocolBusinessManager', title: '业务经理',colspan: 1, rowspan: 2,valign:'middle'},
                {field: 'addProtocolShowState', title: '协议状态',colspan: 1, rowspan: 2,valign:'middle',
                    formatter: function (value,row,index) {
                        var protocolCurrentStatus=row.protocolCurrentStatus;
                        var showStr="";
                        if(protocolCurrentStatus==2||protocolCurrentStatus==3||protocolCurrentStatus==5){//冻结\签约\即将到期才能显示修改协议状态
                            showStr+="<a href='#' onclick='onShowProtocolModify("+row.protocolId+")' >修改协议状态</a><br/>";
                        }
                        if(value=='1'){
                            showStr+= "<a href='#' onclick='onShowAddProtocol("+row.companyId+","+row.protocolContractType+")'>显示未签约,点击签约</a>";
                        }else if(value=='2'){
                            showStr+= "<a style='color:#8c8c8c;text-decoration:none;'>显示未签约,点击签约</a>";
                        }else if(value=='3'){
                            if(protocolCurrentStatus=='1'){
                                showStr+= '待审批';
                            }else if(protocolCurrentStatus=='2'){
                                showStr+= '签约';
                            }else if(protocolCurrentStatus=='3'){
                                showStr+= '即将到期';
                            }else if(protocolCurrentStatus=='4'){
                                showStr+= '合约到期';
                            }else if(protocolCurrentStatus=='5'){
                                showStr+= '冻结';
                            }
                        }
                        if(showStr!=null && showStr!=""){
                            return showStr;
                        }else{
                            return "-";
                        }
                    }
                }],
            [{field: 'protocolContractType', title: '协议类型',colspan: 1, rowspan: 1,valign:'middle',
                formatter: function (value,row,index) {
                    if(value=='1'){
                        return '代发协议';
                    }else if(value=='2'){
                        return '垫发协议';
                    }else if(value=='3'){
                        return '代缴社保协议';
                    }else if(value=='4'){
                        return '报销管理协议';
                    }else {
                        return '-';
                    }
                }
            },
            {field: 'useFulTime', title: '有效期限',colspan: 1, rowspan: 1,valign:'middle',
                formatter: function (value,row,index) {
                    var addProtocolShowState=row.addProtocolShowState;
                    console.log(value);
                    console.log(addProtocolShowState);
                    if(addProtocolShowState===3 && value!=null && value!=undefined){
                        return "<a href='../companyManage/companyManageDetail.htm?isReturnParam=1&companyId="+row.companyId+"'>"+value+"</a>";
                    }else{
                        return "-";
                    }

                }
            }]
        ],
        requestParam: function () {
            return {
                'companyName': $('#companyNameByAddProtocol').val()
            }
        }
    });

    /**
     * 点击第二个搜索按钮
     */
    $("#addProtocolBtn").click(function(){
        addProtocolGrid.onQuery();
    });
});

/**
 * 协议录入1弹出框
 */
function onShowProtocolEntering(companyId,operationShowState) {
    operationShowState=operationShowState==null||operationShowState==undefined?0:operationShowState;
    var index = layer.open({
        type: 2,
        title: '录入协议一',
        maxmin: true,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: 'toProtocolEnteringFirstPage.htm?companyId='+companyId+'&operationShowState='+operationShowState
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 协议修改弹出框
 */
function onShowProtocolModify(protocolId) {
    var index = layer.open({
        type: 2,
        title: '协议修改',
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['450px', '400px'],
        content: 'toProtocolModifyPage.htm?protocolId='+protocolId
    });
    //窗口最大化
    //layer.full(index);
}

/**
 * 添加协议弹出框
 */
function onShowAddProtocol(companyId,protocolState) {
    var index = layer.open({
        type: 2,
        title: '协议录入',
        maxmin: true,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: "toAddProtocolPage.htm?companyId="+companyId+"&operationShowState="+protocolState
    });
    //窗口最大化
    layer.full(index);
}