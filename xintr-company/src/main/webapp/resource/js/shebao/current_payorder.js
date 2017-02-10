$(function(){
    //获取账单列表
    queryCurrentOrderList({});
});
//查询当前账单列表
function queryCurrentOrderList(currentParams){
    $.ajax({
        type: "POST",
        url: BASE_PATH+"/shebao/queryCurrentOrderList.htm",
        dataType: "json",
        data:currentParams,
        success: function (data) {
            console.log(data);
            if(data.success){
                //待提交账单数量
                var counts=data.message.split("##");
                var waitApproveOrderCount=counts[0];
                var currentOrderCount=counts[1];
                $("#waitApproveOrder").text(waitApproveOrderCount);
                var companyShebaoOrders=data.data;
                $("#currentOrderCount").text(currentOrderCount);
                var html="";
                for(var i=0;i<companyShebaoOrders.length;i++){
                    var companyShebaoOrder=companyShebaoOrders[i];
                    html += "<li class='city-"+companyShebaoOrder.joinCityCode+"'>";
                    html += "<div class='current-main-top'>";
                    html += "<p><span>"+companyShebaoOrder.joinCityName+ unixYYYYMMToDate(companyShebaoOrder.orderDate)+"</span>账单</p>";
                    html += "<p class='submit'>订单编号：<span>"+companyShebaoOrder.orderNumber+"</span>提交时间：<span>"+unixCurrentOrderToTime(companyShebaoOrder.submitTime, 'y-m-d h:m:i')+"</span></p>";
                    if(companyShebaoOrder.diffDays!=undefined && companyShebaoOrder.diffDays>0){
                        html += "<p class='nosubmit'>";
                        if(companyShebaoOrder.status==1 || companyShebaoOrder.status==0){
                            html += "请在<span style='border:1px solid #ccc;padding: 0 4px;background: #fff;'><font color='red'>"+companyShebaoOrder.diffDays+"</span></font>天内提交订单，逾期账单自动关闭";
                        }
                        html += "</p>";
                    }
                    html += "</div>";
                    html += "<div class='current-main-table'>";
                    html += "<table>";
                    html += "<tr>";
                    html += "<td>缴费月份</td><td>缴费类型</td><td>详情</td><td>费用总额</td><td>账单状态</td><td>操作</td>";
                    html += "</tr>";
                    html += "<tr>";
                    html += "<td>"+unixYYYYMMToDate(companyShebaoOrder.orderDate)+"</td>";
                    html += "<td>社保</td>";
                    //详情
                    var orderSbDetail=companyShebaoOrder.orderSbDetail==undefined?'':companyShebaoOrder.orderSbDetail;
                    if(orderSbDetail!=''){
                        var detailSbJSON=JSON.parse(orderSbDetail);
                        console.log(detailSbJSON);
                        html += "<td>";
                        for(var j in detailSbJSON){
                            html += j+":";
                            html += detailSbJSON[j]+"人<br>";
                        }
                        html += "</td>";
                    }else{
                        html += "<td></td>";
                    }

                    html += "<td rowspan='2'>";
                    if(companyShebaoOrder.priceSum!=undefined && companyShebaoOrder.priceSum!=''){
                        html += "<p>"+companyShebaoOrder.priceSum+"元</p>";
                    }else{
                        html += "<p>0.00元</p>";
                    }
                    if(companyShebaoOrder.priceService!=undefined && companyShebaoOrder.priceService!=''){
                        html += "<p>含服务费：<span price_service='"+companyShebaoOrder.priceService+"' customer_count='"+companyShebaoOrder.customerCount+"'"
                            +"price_single='"+companyShebaoOrder.priceSingle+"' onclick='showService(this);'>"+companyShebaoOrder.priceService+"元</span></p>";
                    }
                    if(companyShebaoOrder.priceAddtion!=undefined && companyShebaoOrder.priceAddtion!=''){
                        // html += "<p>含补差费用：<a href='#' onclick='showAddtionPage('"+companyShebaoOrder.id+"','"+unixYYYYMMToDate(companyShebaoOrder.orderDate)+"');'>"+companyShebaoOrder.priceAddtion+"元</a></p>";
                        var paramDate  = companyShebaoOrder.id + ',"' + unixYYYYMMToDate(companyShebaoOrder.orderDate) + '"';
                        html += "<p>含补差费用：<a href='#' onclick='showAddtionPage("+paramDate+")'>"+companyShebaoOrder.priceAddtion+"元</a></p>";
                    }
                    html += "</td>";
                    html += "<td rowspan='2'>";
                    if(companyShebaoOrder.status===1){
                        html += "待提交";
                    }else if(companyShebaoOrder.status===2){
                        html += "待付款";
                    }else if(companyShebaoOrder.status===3){
                        html += "付款中";
                    }else if(companyShebaoOrder.status===4 || companyShebaoOrder.status===8){
                        html += "客户付款成功";
                    }else if(companyShebaoOrder.status===5){
                        html += "办理中";
                    }else if(companyShebaoOrder.status===6){
                        html += "办理完结";
                    }else if(companyShebaoOrder.status===7){
                        html += "订单关闭";
                    }else if(companyShebaoOrder.status===8){
                        html+="供应商付款成功";
                    }
                    html += "</td>";
                    html += "<td rowspan='2' class='showTd'>";
                    html += "<a href='#' onclick='showCurrentPayOrderDetail("+companyShebaoOrder.id+");'>账单详情</a>";
                    if(companyShebaoOrder.status===1){
                        html += "<br/><br/>";
                        html += "<span orderId='"+companyShebaoOrder.id+"' city='"+companyShebaoOrder.joinCityName+"' month='"+unixYYYYMMToDate(companyShebaoOrder.orderDate)+"'"
                            +" price_sb='"+companyShebaoOrder.priceSb+"' price_gjj='"+companyShebaoOrder.priceGjj+"' price_sum='"+companyShebaoOrder.priceSum+"'"
                            +" price_addtion='"+companyShebaoOrder.priceAddtion+"' customer_count='"+companyShebaoOrder.customerCount+"'"
                            +" price_single='"+companyShebaoOrder.priceSingle+"' onclick='showSubmitOrder(this)'>提交账单</span>";
                    }
                    if(companyShebaoOrder.number!=null&&companyShebaoOrder.number!=undefined&&companyShebaoOrder.number!=""){
                        html+="<br/><br/>"
                        html+="<a href='#' onclick=queryErrorCurrent('"+companyShebaoOrder.id+"') style='color: red'>异常账单</a>";
                    }
                    html += "</td>";
                    html += "</tr>";
                    html += "<tr>";
                    html += "<td>"+unixYYYYMMToDate(companyShebaoOrder.orderDate)+"</td>";
                    html += "<td>公积金</td>";
                    //详情
                    var orderGjjDetail=companyShebaoOrder.orderGjjDetail==undefined?'':companyShebaoOrder.orderGjjDetail;
                    if(orderGjjDetail!=''){
                        var detailGjjJSON=JSON.parse(orderGjjDetail);
                        console.log(detailGjjJSON);
                        html += "<td>";
                        for(var j in detailGjjJSON){
                            html += j+":";
                            html += detailGjjJSON[j]+"人<br>";
                        }
                        html += "</td>";
                    }else{
                        html += "<td></td>";
                    }
                    html += "</tr>";
                    html += "</table>";
                    html += "</div>";
                    html += "</li>";
                }
                $("#cities").html(html);
            }else{
                layer.alert('获取当前账单失败',{title:'提示信息'});
            }
        }
    });
}
//选择地区查询当前账单列表
function loadPage(obj){
    if($(obj).val() == ''){
        queryCurrentOrderList({});
    } else{
        var currentParams={"city":$(obj).val()};
        queryCurrentOrderList(currentParams);
    }
}
//根据当前账单的数量获取当前账单
$("#currentOrderCount").click(function(){
    queryCurrentOrderList({});
});
//获取当前待提交账单列表
$("#waitApproveOrder").click(function(){
    queryCurrentOrderList({"flag":'1'});
});
//显示服务费
function showService(obj) {
    var that = $(obj);
    $('.service-main table:eq(0)').find('tr:eq(1)').find('.price_service').text(that.attr('price_service'));
    $('.service-main table:eq(0)').find('tr:eq(1)').find('.price_single').text(that.attr('price_single'));
    $('.service-main table:eq(0)').find('tr:eq(1)').find('.customer_count').text(that.attr('customer_count'));
    $("#serviceSingleAmount").text(that.attr('price_single'));
    $('.mengban').show();
    $('.service').show();
    $('.service-title b ').unbind('click').on('click',function(){
        $('.mengban').hide();
        $('.service').hide();
        $("#serviceSingleAmount").text('');
    });
    //点击服务费确认按钮
    $("#serviceSureBtn").unbind('click').on('click',function(){
        $('.mengban').hide();
        $('.service').hide();
        $("#serviceSingleAmount").text('');
    });
}

/**
 * 显示提交弹框
 * @param obj
 */
function showSubmitOrder(obj) {
    var that = $(obj);
    var title = that.attr('month') + that.attr('city') + '账单费用总额：' + that.attr('price_sum') + '元';
    $('.submitOrder-main-title').text(title);

    var tr = $('.submitOrder-main table:eq(0)').find('tr:eq(1)');
    if(that.attr('price_sb')!=undefined &&that.attr('price_sb')!='' && that.attr('price_sb')!='undefined'){
        tr.find('.price_sb').text(that.attr('price_sb'));
    }else{
        tr.find('.price_sb').text('');
    }
    if(that.attr('price_gjj')!=undefined &&that.attr('price_gjj')!='' && that.attr('price_gjj')!='undefined'){
        tr.find('.price_gjj').text(that.attr('price_gjj'));
    }else{
        tr.find('.price_gjj').text('');
    }
    if(that.attr('price_addtion')!=undefined &&that.attr('price_addtion')!='' && that.attr('price_addtion')!='undefined'){
        tr.find('.price_addtion').text(that.attr('price_addtion'));
    }else{
        tr.find('.price_addtion').text('');
    }
    if(that.attr('customer_count')!=undefined &&that.attr('customer_count')!='' && that.attr('customer_count')!='undefined'){
        tr.find('.price_service').text(that.attr('customer_count') + '人*' + that.attr('price_single') + '/人');
    }else{
        tr.find('.price_service').text('');
    }
    $('.submitOrder-bottom input:eq(0)').attr('orderId', that.attr('orderId'));

    $('.mengban').show();
    $('.submitOrder').show();
    $('.submitOrder-title b ').on('click',function(){
        hideSubmitOrder();
    });
}

/**
 * 提交账单
 */
function submitOrder(obj) {
    var that = $(obj);
    var orderId = that.attr('orderId');
    $.post(BASE_PATH + '/shebao/submit_order.htm',{
        orderId : orderId,
        type:''
    },function (result) {
            if(result.success){
                layer.alert('订单提交成功',{
                    title:'处理结果',
                    cancel:function(){
                        queryCurrentOrderList({});//刷新界面
                        hideSubmitOrder();
                    }
                },function(index){
                    queryCurrentOrderList({});//刷新界面
                    layer.close(index);
                    hideSubmitOrder();
                });
            } else{
                if("fail"===result.message){//跳转到异常信息
                    // window.location.href = BASE_PATH + '/shebao/order_error.htm?is_current=1&orderId=' + orderId;
                    showErrorInfoOrder(orderId);
                }else{
                    layer.alert(result.message,{title:'提示信息'});
                }
            }
    });
}
//提交账单隐藏
function hideSubmitOrder() {
    $('.mengban').hide();
    $('.submitOrder').hide()
}

function cancelCurrentPayOrder(){
    parent.location.reload();
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);

}
//将日期转换成yyyyMM格式
function unixYYYYMMToDate(UnixTime) {
    if(UnixTime){
        var dateObj = new Date(UnixTime);
        return ''+dateObj.getFullYear()+"年"+  ((dateObj.getMonth() +1)<10?'0'+(dateObj.getMonth() +1):(dateObj.getMonth() +1) )+"月";
    }
    return "";
}
//转换日期格式
function unixCurrentOrderToTime(ts, pattern) {
    if(ts==undefined || ts==''){
        return '';
    }
    var t,y,m,d,h,i,s;
    t = ts ? new Date(ts) : new Date();
    y = t.getFullYear();
    m = t.getMonth()+1;
    d = t.getDate();
    h = t.getHours();
    i = t.getMinutes();
    s = t.getSeconds();

    if(pattern){
        // 可根据需要在这里定义时间格式
        if(pattern == 'y-m-d h'){
            return y+'-'+(m<10?'0'+m:m)+'-'+(d<10?'0'+d:d)+' '+(h<10?'0'+h:h);
        }
        if(pattern == 'y-m-d h:m'){
            return y+'-'+(m<10?'0'+m:m)+'-'+(d<10?'0'+d:d)+' '+(h<10?'0'+h:h)+':'+(i<10?'0'+i:i);
        }
        if(pattern == 'y-m-d h:m:i'){
            return y+'-'+(m<10?'0'+m:m)+'-'+(d<10?'0'+d:d)+' '+(h<10?'0'+h:h)+':'+(i<10?'0'+i:i)+':'+(s<10?'0'+s:s);
        }
    }

    return y+'-'+(m<10?'0'+m:m)+'-'+(d<10?'0'+d:d)+' '+(h<10?'0'+h:h)+':'+(i<10?'0'+i:i)+':'+(s<10?'0'+s:s);
}
//显示账单详情
function showCurrentPayOrderDetail(companyShebaoOrderId){
    var index = layer.open({
        type: 2,
        title: false,
        maxmin: false,
        closeBtn: 0,
        shadeClose: false, //点击遮罩关闭层
        //area: ['650px', '380px'],
        content: BASE_PATH+'/shebao/payorder_detail.htm?orderId='+companyShebaoOrderId
    });
    //窗口最大化
    layer.full(index);
}

//显示异常提交异常信息
function showErrorInfoOrder(orderId){
    var index = layer.open({
        type: 2,
        title: false,
        maxmin: false,
        closeBtn: 0,
        shadeClose: false, //点击遮罩关闭层
        //area: ['650px', '380px'],
        content: BASE_PATH + '/shebao/order_error.htm?is_current=1&orderId=' + orderId
    });
    //窗口最大化
    layer.full(index);
}

//显示补收补退详细
function showAddtionPage(orderId,orderDateStr){
    var index = layer.open({
        type: 2,
        title: false,
        maxmin: false,
        closeBtn: 0,
        shadeClose: false, //点击遮罩关闭层
        //area: ['650px', '380px'],
        content: BASE_PATH + '/shebao/addtion_detail.htm?supplementCompanyOrderId=' + orderId+'&orderDateStr='+encodeURIComponent(encodeURIComponent(orderDateStr))
    });
    //窗口最大化
    layer.full(index);
}

//跳转至异常账单详情页面
function queryErrorCurrent(orderId){
    var path = BASE_PATH+"/shebao/errorBillDetail.htm?orderId="+orderId
    var index = layer.open({
        type: 2,
        title: false,
        maxmin: false,
        closeBtn: 0,
        shadeClose: false, //点击遮罩关闭层
        //area: ['650px', '380px'],
        content:path
    });
    //窗口最大化
    layer.full(index);
}

//查询异常账单
function ycQuery(){
    $.ajax({
        type: "POST",
        url: BASE_PATH+"/shebao/queryCurrentOrderList.htm",
        dataType: "json",
        data:{},
        success: function (data) {
            console.log(data);
            if(data.success){
                //待提交账单数量
                var counts=data.message.split("##");
                var waitApproveOrderCount=counts[0];
                var currentOrderCount=counts[1];
                $("#waitApproveOrder").text(waitApproveOrderCount);
                var companyShebaoOrders=data.data;
                $("#currentOrderCount").text(currentOrderCount);
                    var html="";
                    for(var i=0;i<companyShebaoOrders.length;i++) {
                        var companyShebaoOrder = companyShebaoOrders[i];
                        if(companyShebaoOrder.number!=null&&companyShebaoOrder.number!=""&&companyShebaoOrder.number!=undefined){
                        html += "<li class='city-" + companyShebaoOrder.joinCityCode + "'>";
                        html += "<div class='current-main-top'>";
                        html += "<p><span>" + companyShebaoOrder.joinCityName + unixYYYYMMToDate(companyShebaoOrder.orderDate) + "</span>账单</p>";
                        html += "<p class='submit'>订单编号：<span>" + companyShebaoOrder.orderNumber + "</span>提交时间：<span>" + unixCurrentOrderToTime(companyShebaoOrder.submitTime, 'y-m-d h:m:i') + "</span></p>";
                        if (companyShebaoOrder.diffDays != undefined && companyShebaoOrder.diffDays > 0) {
                            html += "<p class='nosubmit'>";
                            if (companyShebaoOrder.status == 1 || companyShebaoOrder.status == 0) {
                                html += "请在<span style='border:1px solid #ccc;padding: 0 4px;background: #fff;'><font color='red'>" + companyShebaoOrder.diffDays + "</span></font>天内提交订单，逾期账单自动关闭";
                            }
                            html += "</p>";
                        }
                        html += "</div>";
                        html += "<div class='current-main-table'>";
                        html += "<table>";
                        html += "<tr>";
                        html += "<td>缴费月份</td><td>缴费类型</td><td>详情</td><td>费用总额</td><td>账单状态</td><td>操作</td>";
                        html += "</tr>";
                        html += "<tr>";
                        html += "<td>" + unixYYYYMMToDate(companyShebaoOrder.orderDate) + "</td>";
                        html += "<td>社保</td>";
                        //详情
                        var orderSbDetail = companyShebaoOrder.orderSbDetail == undefined ? '' : companyShebaoOrder.orderSbDetail;
                        if (orderSbDetail != '') {
                            var detailSbJSON = JSON.parse(orderSbDetail);
                            console.log(detailSbJSON);
                            html += "<td>";
                            for (var j in detailSbJSON) {
                                html += j + ":";
                                html += detailSbJSON[j] + "人<br>";
                            }
                            html += "</td>";
                        } else {
                            html += "<td></td>";
                        }

                        html += "<td rowspan='2'>";
                        if (companyShebaoOrder.priceSum != undefined && companyShebaoOrder.priceSum != '') {
                            html += "<p>" + companyShebaoOrder.priceSum + "元</p>";
                        } else {
                            html += "<p>0.00元</p>";
                        }
                        if (companyShebaoOrder.priceService != undefined && companyShebaoOrder.priceService != '') {
                            html += "<p>含服务费：<span price_service='" + companyShebaoOrder.priceService + "' customer_count='" + companyShebaoOrder.customerCount + "'"
                                + "price_single='" + companyShebaoOrder.priceSingle + "' onclick='showService(this);'>" + companyShebaoOrder.priceService + "元</span></p>";
                        }
                        if (companyShebaoOrder.priceAddtion != undefined && companyShebaoOrder.priceAddtion != '') {
                            html += "<p>含补差费用：<a href='#' onclick='showAddtionPage(" + companyShebaoOrder.id + "," + unixYYYYMMToDate(companyShebaoOrder.orderDate) + ");'>" + companyShebaoOrder.priceAddtion + "元</a></p>";
                        }
                        html += "</td>";
                        html += "<td rowspan='2'>";
                        if (companyShebaoOrder.status === 1) {
                            html += "待提交";
                        } else if (companyShebaoOrder.status === 2) {
                            html += "待付款";
                        } else if (companyShebaoOrder.status === 3) {
                            html += "付款中";
                        } else if (companyShebaoOrder.status === 4 || companyShebaoOrder.status === 8) {
                            html += "客户付款成功";
                        } else if (companyShebaoOrder.status === 5) {
                            html += "办理中";
                        } else if (companyShebaoOrder.status === 6) {
                            html += "办理完结";
                        } else if (companyShebaoOrder.status === 7) {
                            html += "订单关闭";
                        }
                        html += "</td>";
                        html += "<td rowspan='2' class='showTd'>";
                        html += "<a href='#' onclick='showCurrentPayOrderDetail(" + companyShebaoOrder.id + ");'>账单详情</a>";
                        if (companyShebaoOrder.status === 1) {
                            html += "<br/><br/>";
                            html += "<span orderId='" + companyShebaoOrder.id + "' city='" + companyShebaoOrder.joinCityName + "' month='" + unixYYYYMMToDate(companyShebaoOrder.orderDate) + "'"
                                + " price_sb='" + companyShebaoOrder.priceSb + "' price_gjj='" + companyShebaoOrder.priceGjj + "' price_sum='" + companyShebaoOrder.priceSum + "'"
                                + " price_addtion='" + companyShebaoOrder.priceAddtion + "' customer_count='" + companyShebaoOrder.customerCount + "'"
                                + " price_single='" + companyShebaoOrder.priceSingle + "' onclick='showSubmitOrder(this)'>提交账单</span>";
                        }
                        if (companyShebaoOrder.number != null && companyShebaoOrder.number != undefined && companyShebaoOrder.number != "") {
                            html += "<br/><br/>"
                            html += "<a href='#' onclick=queryErrorCurrent('" + companyShebaoOrder.id + "') style='color: red'>异常账单</a>";
                        }
                        html += "</td>";
                        html += "</tr>";
                        html += "<tr>";
                        html += "<td>" + unixYYYYMMToDate(companyShebaoOrder.orderDate) + "</td>";
                        html += "<td>公积金</td>";
                        //详情
                        var orderGjjDetail = companyShebaoOrder.orderGjjDetail == undefined ? '' : companyShebaoOrder.orderGjjDetail;
                        if (orderGjjDetail != '') {
                            var detailGjjJSON = JSON.parse(orderGjjDetail);
                            console.log(detailGjjJSON);
                            html += "<td>";
                            for (var j in detailGjjJSON) {
                                html += j + ":";
                                html += detailGjjJSON[j] + "人<br>";
                            }
                            html += "</td>";
                        } else {
                            html += "<td></td>";
                        }
                        html += "</tr>";
                        html += "</table>";
                        html += "</div>";
                        html += "</li>";

                       }
                    }
                    $("#cities").html(html);
                }else{
                    layer.alert('获取当前账单失败',{title:'提示信息'});
                }

            }



    });
}