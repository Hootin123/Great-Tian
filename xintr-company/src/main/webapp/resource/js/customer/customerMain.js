var customerParams={};
var customerType=null;

$(function () {
    //初始化结果列表
    queryList(customerParams);
    if($("#customerVisitFirst").val()=="customerVisitFirst"){
        parent.guideFramework();
        $("#customerVisitFirst").val("");
    }

});
///**
// * 弹框
// * @param excelId
// */
//function cancelSalaryExcel(excelId) {
//    showConfirm('您确定要撤销这条信息吗', function () {
//        $.ajax({
//            type: 'post',
//            url: 'cancelSalaryExcel.htm?excelId=' + excelId,
//            //data: {'excelId': excelId},
//            success: function (resultResponse) {
//                if (resultResponse.success) {
//                    window.location.reload();
//                } else {
//                    showWarning(resultResponse.message);
//                }
//            }
//        });
//    });
//}
/**
 * 跳转到新增员工页面
 */
function onAddCustomer() {
    var index = layer.open({
        type: 2,
        title: false,
        maxmin: false,
        closeBtn: 0,
        shadeClose: false, //点击遮罩关闭层
        //area: ['650px', '380px'],
        content: 'toCustomerAddPage.htm'
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 跳转到批量导入页面
 */
function onAddCustomerBatch() {
    var index = layer.open({
        type: 2,
        title: false,
        closeBtn: 0,
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        //area: ['650px', '380px'],
        content: 'toCustomerBatchImportPage.htm'
    });
    //窗口最大化
    layer.full(index);
}
/**
 * 跳转到筛选页面
 */
function onCustomerSelCondition() {
    var index = layer.open({
        type: 2,
        title: '筛选',
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['700px', '500px'],
        content: 'toCustomerSelectorConditionPage.htm'
    });
    //窗口最大化
    //layer.full(index);
}

/**
 * 跳转到搜索页面
 */
function onCustomerSelName() {
    var index = layer.open({
        type: 2,
        title: '搜索',
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '500px'],
        content: 'toCustomerSelectorNamePage.htm'
    });
    //窗口最大化
    //layer.full(index);
}
/**
 * 跳转到入职需知页面
 */
function onCustomerRequire() {
    var index = layer.open({
        type: 2,
        title: false,
        closeBtn:0,
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: 'toCustomerEnterRequirePage.htm'
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 跳转到转正页面
 */
function onCustomerRegular(customerId) {
    console.log(customerId);
    var index = layer.open({
        type: 2,
        title: false,
        closeBtn:0,
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: 'toCustomerRegular.htm?customerId='+customerId
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 跳转到离职页面
 */
function onCustomerDimission(customerId) {
    console.log(customerId);
    var index = layer.open({
        type: 2,
        title: false,
        maxmin: false,
        closeBtn:0,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: 'toCustomerDimission.htm?customerId='+customerId
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 跳转到调岗页面
 */
function onCustomerTransferPosition(customerId) {
    console.log(customerId);
    var index = layer.open({
        type: 2,
        title: false,
        maxmin: false,
        closeBtn:0,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: 'toCustomerTransferPosition.htm?customerId='+customerId
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 跳转到编辑页面
 */
function onCustomerEdit(customerId) {
    var url= 'toCustomerEditPage.htm?customerId='+customerId;
    var index = layer.open({
        type: 2,
        title: false,
        maxmin: false,
        closeBtn: 0,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: url
    });
    //窗口最大化
    layer.full(index);
}
/**
 * 跳转到查看员工详情页面
 */
//function onCustomerEditBase(customerId) {
//    var url= 'toCustomerEditBasePage.htm?customerId='+customerId;
//    var index = layer.open({
//        type: 2,
//        title: '编辑',
//        maxmin: true,
//        shadeClose: false, //点击遮罩关闭层
//        area: ['650px', '380px'],
//        content: url
//    });
//    //窗口最大化
//    layer.full(index);
//}


//function onCustomerEdit(customerId) {
//    console.log(customerId);
//    parent.allPop('../customerManager/toCustomerEditPage.htm?customerId='+customerId);
//    //$('.popupbox').show(0,function(){
//    //    //$('.popupbox').load('toCustomerEditPage.htm?customerId='+customerId,function () {
//    //        $('.popupbox').load('toCustomerEditPage.htm',function () {
//    //            alert(1)
//    //           // var oDiv = document.getElementsByClassName('obtnclose')[0];
//    //
//    //           //console.log(oDiv)
//    //       // popupboxclosed($('.popupbox'))
//    //    });
//    //});
//}
//
//function popupboxclosed(elem){
//    var oI = document.createElement('i');
//    oI.className = 'popupbox-closed';
//    elem.append(oI);
//    //console.log( $('.popupbox-closed'))
//    $('.popupbox-closed').click(function () {
//        alert(1)
//        $('.popupbox').hide();
//    });
//
//    $('.obtnclose').click(function () {alert(1)
//        $('.popupbox').hide()
//    });
//}

//var oDiv = document.getElementsByClassName('obtnclose')[0];
//
//console.log(oDiv)

//$('.obtnclose').click(function(){
//    //alert(1)
//});


/**
 * 跳转到删除页面
 */
function onCustomerDelete(customerId,customerPhone) {
    console.log(customerId);
    console.log(customerPhone);
    var params="";
    if(customerPhone!=null && customerPhone!=undefined && customerPhone!=""){
        params='customerId='+customerId+'&customerPhone='+customerPhone;
    }else{
        params='customerId='+customerId;
    }
    var index = layer.open({
        type: 2,
        title: '删除员工',
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['500px', '380px'],
        content: 'toCustomerDelete.htm?'+params
    });
    //窗口最大化
    //layer.full(index);
}

/**
 * 筛选重新加载查询数据
 * @param stationCustomerState
 * @param stationEmployMethod
 * @param customerSex
 */
function loadCustomerTab(stationCustomerState,stationEmployMethod,customerSex,stationDeptId,stationDeptName){
    customerParams={};
    if(stationCustomerState!=null && stationCustomerState!=undefined && stationCustomerState!=""){
        customerParams["stationCustomerState"]=stationCustomerState;
    }
    if(stationEmployMethod!=null && stationEmployMethod!=undefined && stationEmployMethod!=""){
        customerParams["stationEmployMethod"]=stationEmployMethod;
    }
    if(customerSex!=null && customerSex!=undefined && customerSex!=""){
        customerParams["customerSex"]=customerSex;
    }
    if(stationDeptId!=null && stationDeptId!=undefined && stationDeptId!=""){
        customerParams["stationDeptId"]=stationDeptId;
        customerParams["stationDeptName"]=stationDeptName;
    }
    customerType=5;
    queryList(customerParams,customerType);
}
/**
 * 搜索用户
 * @param customerTurename
 */
function loadCustomerTabByName(customerTurename){
    customerParams={};
    if(customerTurename!=null && customerTurename!=undefined && customerTurename!=""){
        customerParams["customerTurename"]=customerTurename;
    }
    customerType=6;
    queryList(customerParams,6);
}
/**
 * 文字筛选
 * @param selState
 */
function loadCustomerTabByType(selState){
    customerParams={};
    if(selState!=null && selState!=undefined && selState!=""){
        customerParams["selState"]=selState;
        if(selState==1){
            //customerParams["startTime"]=$("#startTime").val();
            //customerParams["endTime"]=$("#endTime").val();
        }else if(selState==2){
            //customerParams["startTime"]=$("#startTime").val();
            //customerParams["endTime"]=$("#endTime").val();
        }else if(selState==3){
        }else if(selState==4){
            customerParams["customerIsComplement"]=2;//未补全
        }
        customerType=selState;
        queryList(customerParams,selState);
    }



}
/**
 * 查询数据列表
 * @param params
 * @param type  1入职 2离职 3待转正 4未补全资料 5条件筛选 6名称筛选
 */
function queryList(params,type){
    if($("#startTime").val()!=null && $("#startTime").val()!=undefined && $("#startTime").val()!=""){
        params["startTime"]=$("#startTime").val();
        params["endTime"]=$("#endTime").val();
    }
    //显示员工人数
    $.ajax({
        type: 'post',
        url: 'queryCount.htm',
        async:true,
        data:params,
        success: function (data) {
            $("#totalCount").text(data.totalCount);
            $("#enterCount").text(data.enterCount);
            $("#leaveCount").text(data.leaveCount);
            $("#willCount").text(data.willCount);
            $("#lessCount").text(data.lessCount);
        }
    });
    //展示列表
    $.ajax({
        type: 'post',
        url: 'queryList.htm',
        async:true,
        data:params,
        success: function (data) {
           // console.log(resultResponse);
            var resultResponse=data.resultResponse;
            //var totalCount=data.totalCount;
            //var enterCount=data.enterCount;
            //var leaveCount=data.leaveCount;
            //var willCount=data.willCount;
            //var lessCount=data.lessCount;
            if (resultResponse.success) {
                //$("#totalCount").text(totalCount);
                //$("#enterCount").text(enterCount);
                //$("#leaveCount").text(leaveCount);
                //$("#willCount").text(willCount);
                //$("#lessCount").text(lessCount);

                var list = resultResponse.data;
                var paginator=resultResponse.paginator;
              //  console.log(list);
                //更新筛选条件显示
                $("#showSelectorDiv").empty();
                var count=paginator.totalCount;
                var selHtml="";
                if(type===1){
                    selHtml="找到"+count+"个关键词为：<span>入职</span><a onclick='cancelSelector();'>取消筛选</a>";
                }else if(type===2){
                    selHtml="找到"+count+"个关键词为：<span>离职</span><a onclick='cancelSelector();'>取消筛选</a>";
                }else if(type===3){
                    selHtml="找到"+count+"个关键词为：<span>待转正</span><a onclick='cancelSelector();'>取消筛选</a>";
                }else if(type===4){
                    selHtml="找到"+count+"个关键词为：<span>未补全资料员工</span><a onclick='cancelSelector();'>取消筛选</a>";
                }else if(type===5){
                    var selectorStr="";
                    if(params["stationCustomerState"]!=null && params["stationCustomerState"]!=undefined && params["stationCustomerState"]!=""){
                        if(params["stationCustomerState"]==='1'){
                            selectorStr+="在职";
                        }else if(params["stationCustomerState"]==='3'){
                            selectorStr+="离职";
                        }
                    }
                    if(params["stationEmployMethod"]!=null && params["stationEmployMethod"]!=undefined && params["stationEmployMethod"]!=""){
                        if(params["stationEmployMethod"]==='1'){
                            selectorStr+="正式";
                        }else if(params["stationEmployMethod"]==='2'){
                            selectorStr+="劳务";
                        }
                    }
                    if(params["customerSex"]!=null && params["customerSex"]!=undefined && params["customerSex"]!=""){
                        if(params["customerSex"]==='0'){
                            selectorStr+="女";
                        }else if(params["customerSex"]==='1'){
                            selectorStr+="男";
                        }
                    }
                    if(params["stationDeptId"]!=null && params["stationDeptId"]!=undefined && params["stationDeptId"]!=""){
                        //params["stationDeptId"]=stationDeptId;
                        selectorStr+=params["stationDeptName"];
                    }
                    selHtml="找到"+count+"个关键词为<span>"+selectorStr+"</span><a onclick='cancelSelector();'>取消筛选</a>";
                }else if(type===6){
                    selHtml="找到"+count+"个关键词为"+(params['customerTurename'])+"<a onclick='cancelSelector();'>取消筛选</a>";
                }
                $("#showSelectorDiv").html(selHtml);

                $("#customerListDiv").empty();
                var html="<table class='payoff_two_list'>";
                html+="<tr class='first_list'><td>序号</td><td>姓名</td><td>手机号</td><td>工号</td><td>性别</td><td>聘用形式</td><td>部门</td><td>岗位</td><td>入职日期</td><td>操作</td></tr>";
                var length = list.length;
                for (var i = 0; i < length; i++) {
                    var custmoerDto = list[i];
                    var customerSex="";
                    var stationEmployMethod="";
                    var stationCustomerState="";
                    if(custmoerDto.customerSex==0){
                        customerSex="女";
                    }else if(custmoerDto.customerSex==1){
                        customerSex="男";
                    }
                    if(custmoerDto.stationEmployMethod==1){
                        stationEmployMethod="正式";
                    }else if(custmoerDto.stationEmployMethod==2){
                        stationEmployMethod="劳务";
                    }
                    //员工ID
                    var customerId=custmoerDto.customerId;
                    //员工手机号
                    var customerPhone=custmoerDto.customerPhone;
                    if(custmoerDto.willLeaveState==1){//待离职
                        //不显示内容
                    }else if(custmoerDto.stationCustomerState==1){
                        //stationCustomerState+="<a onclick='onCustomerEdit("+customerId+");' >编辑</a>";
                        stationCustomerState+="<a onclick='onCustomerRegular("+customerId+");' >转正</a>";
                        stationCustomerState+="<a onclick='onCustomerTransferPosition("+customerId+");' >调岗</a>";
                        stationCustomerState+="<a onclick='onCustomerDimission("+customerId+");' >离职</a>";
                        //stationCustomerState+="<a onclick='onCustomerDelete("+customerId+","+customerPhone+");' >删除</a>";
                    }else if(custmoerDto.stationCustomerState==2){
                        //stationCustomerState+="<a onclick='onCustomerEdit("+customerId+");' >编辑</a>";
                        stationCustomerState+="<a onclick='onCustomerTransferPosition("+customerId+");' >调岗</a>";
                        stationCustomerState+="<a onclick='onCustomerDimission("+customerId+");' >离职</a>";
                        //stationCustomerState+="<a onclick='onCustomerDelete("+customerId+","+customerPhone+");' >删除</a>";
                    }else if(custmoerDto.stationCustomerState==3){
                        stationCustomerState+="<a onclick='onCustomerDelete("+customerId+","+customerPhone+");' >删除</a>";
                    }
                    html+="<tr class='first_last'>";
                    html+="<td>"+(i+1)+"</td>";
                    html+="<td><a onclick='onCustomerEdit("+customerId+");'>"+(custmoerDto.customerTurename==undefined?'':custmoerDto.customerTurename)+"</a></td>";
                    html+="<td>"+(custmoerDto.customerPhone==undefined?'':custmoerDto.customerPhone)+"</td>";
                    html+="<td>"+(custmoerDto.stationCustomerNumber==undefined?'':custmoerDto.stationCustomerNumber)+"</td>";
                    html+="<td>"+customerSex+"</td>";
                    html+="<td>"+stationEmployMethod+"</td>";
                    html+="<td>"+(custmoerDto.depName==undefined?'':custmoerDto.depName)+"</td>";
                    html+="<td>"+(custmoerDto.stationStationName==undefined?'':custmoerDto.stationStationName)+"</td>";
                    html+="<td>"+(custmoerDto.stationEnterTime==undefined?'':formatDate(new Date(custmoerDto.stationEnterTime)))+"</td>";
                    html+="<td>"+stationCustomerState+"</td>";
                    html+="</tr>";
                }
                html+="</table>";
                $('#customerListDiv').html(html);
                newPageHtml(resultResponse,params,type);


            } else {
                layer.alert(resultResponse.message, {icon: 1});
            }
        }
    });
}
function newPageHtml(resultResponse,params,type) {
    //生成分页控件
    kkpager.generPageHtml({
        pno: resultResponse.paginator.page,
        mode: 'click', //可选，默认就是link
        //总页码
        total: '' + resultResponse.paginator.totalPages,
        //总数据条数
        totalRecords: '' + resultResponse.paginator.totalCount,
        //链接前部
        hrefFormer: '' + resultResponse.hrefFormer,
        //链接尾部
        hrefLatter: '' + resultResponse.hrefLatter,
        //链接算法
        isShowFirstPageBtn:false,
        isShowLastPageBtn:false,
        currPageBeforeText:'第',
//        getLink: function (n) {
//            return 'orgmember.htm?pageIndex=' + n;
//        }
        click: function (n) {
            //这里可以做自已的处理
            //...
            //处理完后可以手动条用selectPage进行页码选中切换
            params["pageIndex"]=n;
            queryList(params,type);
        },
        //getHref是在click模式下链接算法，一般不需要配置，默认代码如下
        getHref: function (n) {
            return '#';
        }
    }, true);
}

function formatDate(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? '0' + m : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    return y + '-' + m + '-' + d;
}

function cancelSelector(){
    $("#showSelectorDiv").empty();
    customerParams={};
    customerType=null;
    queryList(customerParams);
}

function loadCustomerList(){
    queryList(customerParams,customerType);
}