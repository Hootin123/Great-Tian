$(document).ready(function () {
    //初始化列表
    initPayOrderDetail({"companyShebaoOrderId":$("#companyShebaoOrderId").val()});

    $("#payorderDetailSureBtn").click(function(){
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
    });

    $("#detailSearch").click(function(){
        var deptId=$("#deptId").val();
        var memberName=$("#memberName").val();
        var detailParams={"deptId":deptId,"memberName":memberName,"companyShebaoOrderId":$("#companyShebaoOrderId").val()};
        initPayOrderDetail(detailParams);
    });

    //    select 颜色
    $('#deptId').change(function(){
        $(this).css('color','#333')
    })
});
//获取当前账单详细列表
function initPayOrderDetail(detailParams){
    // $.ajax({
    //     type: "POST",
    //     url: BASE_PATH + "/shebaoManager/payorderDetailList.htm",
    //     dataType: "json",
    //     data: detailParams,
    //     success: function (data) {
    //         console.log(data);
    //         if (data.success) {
    //             var customerShebaoOrderDtos=data.data;
    //             var html="";
    //             for(var i=0;i<customerShebaoOrderDtos.length;i++){
    //                 var customerShebaoOrderDto=customerShebaoOrderDtos[i];
    //
    //                 html += "<tr id='params' memberId='"+customerShebaoOrderDto.customerId+"' orderId='"+customerShebaoOrderDto.companyShebaoOrderId+"'>";
    //                 html += "<td rowspan='2'>"+(i+1)+"</td>";
    //                 html += "<td rowspan='2'>"+customerShebaoOrderDto.memberName+"</td>";
    //                 html += "<td rowspan='2'>"+(customerShebaoOrderDto.deptName==undefined?'':customerShebaoOrderDto.deptName)+"</td>";
    //                 html += "<td rowspan='2'>"+customerShebaoOrderDto.telphone+"</td>";
    //                 html += "<td>社保</td>";
    //                 html += "<td>";
    //                 if(customerShebaoOrderDto.shebaoBase!=undefined && customerShebaoOrderDto.shebaoBase!=""&&customerShebaoOrderDto.shebaoBase!=null){
    //                     html += customerShebaoOrderDto.shebaoBase+"</td>";
    //                 }else{
    //                     html += "无</td>";
    //                 }
    //
    //                 html += "<td>";
    //                 if(customerShebaoOrderDto.sbErrorReason==undefined|| customerShebaoOrderDto.sbErrorReason==""||customerShebaoOrderDto.sbErrorReason==null){
    //                     html += "<a>";
    //                     if(customerShebaoOrderDto.sbZyText!=undefined && customerShebaoOrderDto.sbZyText!=""){
    //                         html += "增员"+customerShebaoOrderDto.sbZyText+"<br>";
    //                     }
    //                     if(customerShebaoOrderDto.sbHjText!=undefined && customerShebaoOrderDto.sbHjText!=""){
    //                         html += "续缴"+customerShebaoOrderDto.sbHjText+"<br>";
    //                     }
    //                     if(customerShebaoOrderDto.sbTjText!=undefined && customerShebaoOrderDto.sbTjText!=""){
    //                         html += "调基"+customerShebaoOrderDto.sbTjText+"<br>";
    //                     }
    //                     if(customerShebaoOrderDto.sbStopText!=undefined && customerShebaoOrderDto.sbStopText!=""){
    //                         html += "停缴"+customerShebaoOrderDto.sbStopText+"<br>";
    //                     }
    //                     html += "</a>";
    //                     if(customerShebaoOrderDto.sbBjText!=undefined && customerShebaoOrderDto.sbBjText!=""){
    //                         html += "<a>补缴"+customerShebaoOrderDto.sbBjText+"</a>";
    //                     }
    //                 }else{
    //                     html += "无";
    //                 }
    //                 html += "</td>";
    //                 html += "<td onclick='showSheBaoDetail(this);' name='"+customerShebaoOrderDto.memberName+"' class='lastTd'>详情</td>";
    //                 html += "</tr>";
    //                 html += "<tr memberid='"+customerShebaoOrderDto.customerId+"' orderId='"+customerShebaoOrderDto.companyShebaoOrderId+"'>";
    //                 html += "<td>公积金</td>";
    //                 html += "<td>";
    //                 if(customerShebaoOrderDto.gjjBase!=undefined && customerShebaoOrderDto.gjjBase!=""){
    //                     html += customerShebaoOrderDto.gjjBase+"";
    //                 }else{
    //                     html += "无";
    //                 }
    //                 html += "</td>";
    //                 html += "<td>";
    //                 html += "<a>";
    //                 if(customerShebaoOrderDto.gjjHjText!=undefined && customerShebaoOrderDto.gjjHjText!=""){
    //                     html += "续缴"+customerShebaoOrderDto.gjjHjText+"<br>";
    //                 }
    //                 if(customerShebaoOrderDto.gjjTjText!=undefined && customerShebaoOrderDto.gjjTjText!=""){
    //                     html += "调基"+customerShebaoOrderDto.gjjTjText+"<br>";
    //                 }
    //                 if(customerShebaoOrderDto.gjjStopText!=undefined && customerShebaoOrderDto.gjjStopText!=""){
    //                     html += "停缴"+customerShebaoOrderDto.gjjStopText+"<br>";
    //                 }
    //                 html += "</a>";
    //                 if(customerShebaoOrderDto.gjjBjText!=undefined && customerShebaoOrderDto.gjjBjText!=""){
    //                     html += "<a>补缴"+customerShebaoOrderDto.gjjBjText+"</a>";
    //                 }
    //                 html += "</td>";
    //                 html += "<td onclick='showGjjDetail(this);' name='"+customerShebaoOrderDto.memberName+"' class='lastTd' >详情</td>";
    //                 html += "</tr>";
    //             }
    //             $("#detailTbody").html(html);
    //         } else {
    //             layer.alert('获取当前账单详细失败', {title: '提示信息'});
    //         }
    //     }
    // });

    $.ajax({
        type: "POST",
        url: BASE_PATH + "/shebaoManager/payorderDetailList.htm",
        dataType: "json",
        data: detailParams,
        success: function (data) {
            console.log(data);
            if (data.success) {
                var customerShebaoOrderDtos=data.data;
                var html="";
                for(var i=0;i<customerShebaoOrderDtos.length;i++){
                    var customerShebaoOrderDto=customerShebaoOrderDtos[i];

                    html += "<tr id='params' memberId='"+customerShebaoOrderDto.customerId+"' orderId='"+customerShebaoOrderDto.companyShebaoOrderId+"'>";
                    html += "<td rowspan='2'>"+(i+1)+"</td>";
                    html += "<td rowspan='2'>"+customerShebaoOrderDto.memberName+"</td>";
                    html += "<td rowspan='2'>"+(customerShebaoOrderDto.deptName==undefined?'':customerShebaoOrderDto.deptName)+"</td>";
                    html += "<td rowspan='2'>"+customerShebaoOrderDto.telphone+"</td>";
                    html += "<td>社保</td>";
                    html += "<td>";
                    if(customerShebaoOrderDto.shebaoBase!=undefined && customerShebaoOrderDto.shebaoBase!=""&&customerShebaoOrderDto.shebaoBase!=null){
                        if(customerShebaoOrderDto.isSubmitAccount===1){
                            if((customerShebaoOrderDto.sbErrorReason==undefined|| customerShebaoOrderDto.sbErrorReason==""||customerShebaoOrderDto.sbErrorReason==null)
                                ||(customerShebaoOrderDto.id!=undefined &&customerShebaoOrderDto.id!=null)){
                                html += customerShebaoOrderDto.shebaoBase+"</td>";
                            }else{
                                html += "无</td>";
                            }
                        }else {
                            html += customerShebaoOrderDto.shebaoBase + "</td>";
                        }
                    }else{
                        html += "无</td>";
                    }
                    html += "<td>";
                    if((customerShebaoOrderDto.isSubmitAccount===1 && (customerShebaoOrderDto.sbErrorReason==undefined|| customerShebaoOrderDto.sbErrorReason==""||customerShebaoOrderDto.sbErrorReason==null))
                        ||(customerShebaoOrderDto.isSubmitAccount===2)
                        ||(customerShebaoOrderDto.id!=undefined &&customerShebaoOrderDto.id!=null)){
                        html += "<a>";
                        if(customerShebaoOrderDto.sbZyText!=undefined && customerShebaoOrderDto.sbZyText!=""){
                            html += "增员"+customerShebaoOrderDto.sbZyText+"<br>";
                        }
                        if(customerShebaoOrderDto.sbHjText!=undefined && customerShebaoOrderDto.sbHjText!=""){
                            html += "续缴"+customerShebaoOrderDto.sbHjText+"<br>";
                        }
                        if(customerShebaoOrderDto.sbTjText!=undefined && customerShebaoOrderDto.sbTjText!=""){
                            html += "调基"+customerShebaoOrderDto.sbTjText+"<br>";
                        }
                        if(customerShebaoOrderDto.sbStopText!=undefined && customerShebaoOrderDto.sbStopText!=""){
                            html += "停缴"+customerShebaoOrderDto.sbStopText+"<br>";
                        }
                        html += "</a>";
                        if(customerShebaoOrderDto.sbBjText!=undefined && customerShebaoOrderDto.sbBjText!=""){
                            html += "<a>补缴"+customerShebaoOrderDto.sbBjText+"</a>";
                        }
                    }else{
                        html += "无";
                    }

                    html += "</td>";
                    if((customerShebaoOrderDto.isSubmitAccount===1 && (customerShebaoOrderDto.sbErrorReason==undefined|| customerShebaoOrderDto.sbErrorReason==""||customerShebaoOrderDto.sbErrorReason==null))
                        ||(customerShebaoOrderDto.isSubmitAccount===2)
                        ||(customerShebaoOrderDto.id!=undefined &&customerShebaoOrderDto.id!=null)){
                        html += "<td onclick='showSheBaoDetail(this);' name='"+customerShebaoOrderDto.memberName+"' class='lastTd'>详情</td>";
                    }else{
                        html += "<td></td>";
                    }

                    html += "</tr>";
                    html += "<tr memberid='"+customerShebaoOrderDto.customerId+"' orderId='"+customerShebaoOrderDto.companyShebaoOrderId+"'>";
                    html += "<td>公积金</td>";
                    html += "<td>";
                    if(customerShebaoOrderDto.gjjBase!=undefined && customerShebaoOrderDto.gjjBase!=""&&customerShebaoOrderDto.gjjBase!=null){
                        if(customerShebaoOrderDto.isSubmitAccount===1){
                            if((customerShebaoOrderDto.gjjErrorReason==undefined|| customerShebaoOrderDto.gjjErrorReason==""||customerShebaoOrderDto.gjjErrorReason==null)
                                ||(customerShebaoOrderDto.id!=undefined &&customerShebaoOrderDto.id!=null)){
                                html += customerShebaoOrderDto.gjjBase+"</td>";
                            }else{
                                html += "无";
                            }
                        }else {
                            html += customerShebaoOrderDto.gjjBase + "</td>";
                        }
                    }else{
                        html += "无";
                    }
                    html += "</td>";
                    html += "<td>";
                    if((customerShebaoOrderDto.isSubmitAccount===1 && (customerShebaoOrderDto.gjjErrorReason==undefined|| customerShebaoOrderDto.gjjErrorReason==""||customerShebaoOrderDto.gjjErrorReason==null))
                        ||(customerShebaoOrderDto.isSubmitAccount===2)
                        ||(customerShebaoOrderDto.id!=undefined &&customerShebaoOrderDto.id!=null)){
                        html += "<a>";
                        if(customerShebaoOrderDto.gjjZyText!=undefined && customerShebaoOrderDto.gjjZyText!=""){
                            html += "增员"+customerShebaoOrderDto.gjjZyText+"<br>";
                        }
                        if(customerShebaoOrderDto.gjjHjText!=undefined && customerShebaoOrderDto.gjjHjText!=""){
                            html += "续缴"+customerShebaoOrderDto.gjjHjText+"<br>";
                        }
                        if(customerShebaoOrderDto.gjjTjText!=undefined && customerShebaoOrderDto.gjjTjText!=""){
                            html += "调基"+customerShebaoOrderDto.gjjTjText+"<br>";
                        }
                        if(customerShebaoOrderDto.gjjStopText!=undefined && customerShebaoOrderDto.gjjStopText!=""){
                            html += "停缴"+customerShebaoOrderDto.gjjStopText+"<br>";
                        }
                        html += "</a>";
                        if(customerShebaoOrderDto.gjjBjText!=undefined && customerShebaoOrderDto.gjjBjText!=""){
                            html += "<a>补缴"+customerShebaoOrderDto.gjjBjText+"</a>";
                        }
                    }else{
                        html += "无";
                    }

                    html += "</td>";
                    if((customerShebaoOrderDto.isSubmitAccount===1 && (customerShebaoOrderDto.gjjErrorReason==undefined|| customerShebaoOrderDto.gjjErrorReason==""||customerShebaoOrderDto.gjjErrorReason==null))
                        ||(customerShebaoOrderDto.isSubmitAccount===2)
                        ||(customerShebaoOrderDto.id!=undefined &&customerShebaoOrderDto.id!=null)){
                        html += "<td onclick='showGjjDetail(this);' name='"+customerShebaoOrderDto.memberName+"' class='lastTd' >详情</td>";
                    }else{
                        html += "<td></td>";
                    }
                    html += "</tr>";
                }
                $("#detailTbody").html(html);
            } else {
                layer.alert('获取当前账单详细失败', {title: '提示信息'});
            }
        }
    });
}

// ....社保详情
// $('.btnSocialDetails').on('click',function(){
function showSheBaoDetail(obj){
    var that = $(obj);

    $("#shebaoDetailTile").html(that.attr("name")+"社保详情<b></b>");

    var tr = that.parents('tr:eq(0)');
    // var remitTitle = tr.find('td:eq(5)').find('.jn-remit-title:eq(0)').text().trim();
    var remitTitle = that.prev().find('a:eq(0)').text().trim();
    console.log(remitTitle);
    $('.socialDetails-main .remit-title').html(remitTitle);

    // 有补缴
    // if(tr.find('.sbbj-remit-title').length > 0){
    if(that.prev().find('a').length > 1){
        // var fundPaymentTitle = tr.find('.sbbj-remit-title:eq(0)').text().trim();
        var fundPaymentTitle = that.prev().find('a:eq(1)').text().trim();
        console.log(fundPaymentTitle);
        $('.socialDetails-main-payment .payment-title').html(fundPaymentTitle);
        $('.socialDetails-main-payment').show();
    } else{
        $('.socialDetails-main-payment').hide();
    }

    $.getJSON(BASE_PATH + '/shebaoManager/shebao_detail.htm', {
        // memberId: that.parents('tr:eq(0)').attr('memberId'),
        // orderId: that.parents('tr:eq(0)').attr('orderId')
        memberId: that.parent('tr').attr('memberId'),
        orderId: that.parent('tr').attr('orderId')
    }, function (result) {
        if(result && result.success){

            var orderData = result.data.customberOrder;
            var bjList = result.data.bjList;

            var tbody = $('.socialDetails-main-payment table:eq(0)').find('tbody');
            tbody.html('');
            var orderDetail = orderData.orderDetail;
            if(orderDetail==null||orderDetail==undefined||orderDetail==""){
                layer.alert('社保详情无法查看',{title:'提示信息'});
            }
            var orderDetails = JSON.parse(orderData.orderDetail);
            var tab = $('.socialDetails-main-remit table:eq(0)');

            for(i in orderDetails){
                var item = orderDetails[i];
                var index = -1;
                if(item.code == 'yanglao'){
                    index = 1;
                }
                if(item.code == 'yiliao'){
                    index = 2;
                }
                if(item.code == 'shiye'){
                    index = 3;
                }
                if(item.code == 'gongshang'){
                    index = 4;
                }
                if(item.code == 'shengyu'){
                    index = 5;
                }
                if(item.code == 'canjijin'){
                    index = 6;
                }
                tab.find('tr:eq('+index+')').find('td:eq(1)').text((item.base==undefined||item.base==''?'0':item.base));
                tab.find('tr:eq('+index+')').find('td:eq(2)').text((item.orgprop==undefined||item.orgprop==''?'0%':item.orgprop+'%'));
                tab.find('tr:eq('+index+')').find('td:eq(3)').text((item.orgbase==undefined||item.orgbase==''?'0':item.orgbase));
                tab.find('tr:eq('+index+')').find('td:eq(4)').text((item.empprop==undefined||item.empprop==''?'0%':item.empprop+'%'));
                tab.find('tr:eq('+index+')').find('td:eq(5)').text((item.empbase==undefined||item.empbase==''?'0':item.empbase));
            }
            tab.find('tr:eq(7)').find('td:eq(1)').text('单位费用：'+ (orderData.orgSum||'0')+'元' );
            tab.find('tr:eq(7)').find('td:eq(2)').text('个人费用：'+ (orderData.empSum||'0')+'元' );

            var dwPay = 0, grPay = 0;

            // 补缴
            if(bjList && bjList.length > 0){

                for(i in bjList){
                    var item = bjList[i];
                    var orderDetails = JSON.parse(item.orderDetail);
                    var overdueAmount=item.shebaoOrderOverdue==undefined?0:item.shebaoOrderOverdue;

                    var yanglao = {}, yiliao = {}, shiye = {}, gongshang = {}, shengyu = {},canjijin={};
                    var dwTotal = 0, grTotal = 0;

                    for(j in orderDetails){
                        if(orderDetails[j].code == 'canjijin'){
                            canjijin=orderDetails[j];
                        }
                        if(orderDetails[j].code == 'yanglao'){
                            yanglao = orderDetails[j];
                        }
                        if(orderDetails[j].code == 'yiliao'){
                            yiliao = orderDetails[j];
                        }
                        if(orderDetails[j].code == 'shiye'){
                            shiye = orderDetails[j];
                        }
                        if(orderDetails[j].code == 'gongshang'){
                            gongshang = orderDetails[j];
                        }
                        if(orderDetails[j].code == 'shengyu'){
                            shengyu = orderDetails[j];
                        }

                        dwTotal += orderDetails[j].orgbase;
                        grTotal += orderDetails[j].empbase;
                    }
                    dwTotal += overdueAmount;
                    dwPay += dwTotal;
                    grPay += grTotal;

                    var mon = new Date(item.overdueMonth).format('Ym');
                    var tr = '<tr>' +
                        '<td rowspan="7">'+ mon +'</td>' +
                        '<td>养老</td>' +
                        '<td>'+ yanglao.base +'</td>' +
                        '<td>'+ yanglao.orgprop+'%' +'</td>' +
                        '<td>'+ yanglao.orgbase +'</td>' +
                        '<td rowspan="6">'+overdueAmount+'</td>' +
                        '<td>'+ yanglao.empprop+'%'+'</td>' +
                        '<td>'+ yanglao.empbase +'</td>' +
                        '</tr>' +
                        '<tr>' +
                        '<td>医疗</td>' +
                        '<td>'+ yiliao.base +'</td>' +
                        '<td>'+ yiliao.orgprop +'%'+'</td>' +
                        '<td>'+ yiliao.orgbase +'</td>' +
                        '<td>'+ yiliao.empprop +'%'+'</td>' +
                        '<td>'+ yiliao.empbase +'</td>' +
                        '</tr>'+
                        '<tr>' +
                        '<td>失业</td>' +
                        '<td>'+ shiye.base +'</td>' +
                        '<td>'+ shiye.orgprop +'%'+'</td>' +
                        '<td>'+ shiye.orgbase +'</td>' +
                        '<td>'+ shiye.empprop +'%'+'</td>' +
                        '<td>'+ shiye.empbase +'</td>' +
                        '</tr>'+
                        '<tr>' +
                        '<td>工伤</td>' +
                        '<td>'+ gongshang.base +'</td>' +
                        '<td>'+ gongshang.orgprop +'%'+'</td>' +
                        '<td>'+ gongshang.orgbase +'</td>' +
                        '<td>'+ gongshang.empprop +'%'+'</td>' +
                        '<td>'+ gongshang.empbase +'</td>' +
                        '</tr>'+
                        '<tr>' +
                        '<td>生育</td>' +
                        '<td>'+ shengyu.base +'</td>' +
                        '<td>'+ shengyu.orgprop+'%' +'</td>' +
                        '<td>'+ shengyu.orgbase +'</td>' +
                        '<td>'+ shengyu.empprop+'%' +'</td>' +
                        '<td>'+ shengyu.empbase +'</td>' +
                        '</tr>' +
                        '<tr>' +
                        '<td>残疾人保障金</td>' +
                        '<td>'+ (canjijin.base==undefined||canjijin.base==''?'0':canjijin.base)  +'</td>' +
                        '<td>'+ (canjijin.orgprop==undefined||canjijin.orgprop==''?'0%':canjijin.orgprop+'%') +'</td>' +
                        '<td>'+ (canjijin.orgbase==undefined||canjijin.orgbase==''?'0':canjijin.orgbase) +'</td>' +
                        '<td>'+ (canjijin.empprop==undefined||canjijin.empprop==''?'0%':canjijin.empprop+'%') +'</td>' +
                        '<td>'+ (canjijin.empbase==undefined||canjijin.empbase==''?'0':canjijin.empbase) +'</td>' +
                        '</tr>' +
                        '<tr>' +
                        '<td colspan="5">单位费用：'+ dwTotal.toFixed(3)+'元' +'</td>' +
                        '<td colspan="2">个人费用：'+ grTotal.toFixed(3) +'元'+'</td>' +
                        '</tr>';

                    tbody.append(tr);

                }

            }

            var tfoot = $('.socialDetails-main-payment table:eq(0)').find('tfoot');
            tfoot.find('td:eq(1)').text('单位费用总额：' + dwPay.toFixed(3)+'元');
            tfoot.find('td:eq(2)').text('个人费用总额：' + grPay.toFixed(3)+'元');

            $('.mengban').show();
            $('.socialDetails').show();
            $('.socialDetails-title b ').unbind('click').on('click',function(){
                $('.mengban').hide()
                $('.socialDetails').hide()
            });
            $("#shebaoDetailSureBtn").unbind('click').on('click',function(){
                $('.mengban').hide()
                $('.socialDetails').hide()
            });
        } else{
            if(result && result.msg){
                layer.alert(result.msg,{title:'提示信息'});
            } else{
                layer.alert('社保详情无法查看',{title:'提示信息'});
            }
        }
    });

}

// ....公积金详情
// $('.btnFundDetails').on('click',function(){
function showGjjDetail(obj){
    var that = $(obj);

    $("#gjjDetailTile").html(that.attr("name")+"公积金详情<b></b>");
    // var tr = that.parents('tr:eq(0)');
    // var fundRemitTitle = tr.find('td:eq(5)').find('.jn-remit-title:eq(0)').text().trim();
    var fundRemitTitle = that.prev().find('a:eq(0)').text().trim();
    console.log(fundRemitTitle);
    $('.fundDetails-main-payment table:eq(0)').find('tbody').html('');
    $('.fundDetails-main-remit .fundRemit-title').html(fundRemitTitle);

    // 有补缴
    // if(tr.find('.gjjbj-remit-title').length > 0){
    if(that.prev().find('a').length > 1){
        // var fundPaymentTitle = tr.find('td:eq(5)').find('.gjjbj-remit-title:eq(0)').text().trim();
        var fundPaymentTitle = that.prev().find('a:eq(1)').text().trim();
        console.log(fundPaymentTitle);
        $('.fundDetails-main-payment .fundPayment-title').html(fundPaymentTitle);

        $('.fundDetails-main-payment').show();
    } else{
        $('.fundDetails-main-payment').hide();
    }
    $.getJSON(BASE_PATH + '/shebaoManager/gjj_detail.htm', {
        // memberId: that.parents('tr:eq(0)').attr('memberId'),
        // orderId: that.parents('tr:eq(0)').attr('orderId')
        memberId: that.parent('tr').attr('memberId'),
        orderId: that.parent('tr').attr('orderId')
    }, function (result) {
        if(result && result.success){
            var bjList = result.data.bjList;
            var orderData = result.data.customberOrder;
            var orderDetails = JSON.parse(orderData.orderDetail);
            var tab = $('.fundDetails-main-remit table:eq(0)');
            if(orderDetails && orderDetails[0]){
                var item = orderDetails[0];
                tab.find('tr:eq(1)').find('td:eq(1)').text(item.base);
                tab.find('tr:eq(1)').find('td:eq(2)').text(item.orgprop+'%');
                tab.find('tr:eq(1)').find('td:eq(3)').text(item.orgbase);
                tab.find('tr:eq(1)').find('td:eq(4)').text(item.empprop+'%');
                tab.find('tr:eq(1)').find('td:eq(5)').text(item.empbase);
                tab.find('tr:eq(2)').find('td:eq(1)').text('单位费用：'+ (orderData.orgSum||'0')+'元' );
                tab.find('tr:eq(2)').find('td:eq(2)').text('个人费用：'+ (orderData.empSum||'0')+'元' );
            }

            if(bjList && bjList.length > 0){
                var tbody = $('.fundDetails-main-payment table:eq(0)').find('tbody');
                var grTotal = 0, dwTotal = 0;
                for(i in bjList){
                    var item = bjList[i];
                    var mon = new Date(item.overdueMonth).format('Ym');
                    var orderDetail = JSON.parse(item.orderDetail)[0];
                    var overdueAmount=item.shebaoOrderOverdue==undefined?0:item.shebaoOrderOverdue;

                    var html = '<tr><td>'+mon+'</td>' +
                        '<td>'+ (orderDetail.name || '公积金') +'</td>' +
                        '<td>'+ (orderDetail.base || 0) +'</td>' +
                        '<td>'+ (orderDetail.orgprop+'%' || 0) +'</td>' +
                        '<td>'+ (orderDetail.orgbase || 0) +'</td>' +
                        '<td>'+ (overdueAmount || 0) +'</td>' +
                        '<td>'+ (orderDetail.empprop+'%' || 0) +'</td>' +
                        '<td>'+ (orderDetail.empbase || 0) +'</td>' +
                        '</tr>';

                    dwTotal += (orderDetail.orgbase || 0);
                    grTotal += (orderDetail.empbase || 0);
                    tbody.append(html);
                }

                var tfoot = $('.fundDetails-main-payment table:eq(0)').find('tfoot');
                tfoot.find('tr:eq(0)').find('td:eq(1)').text('单位费用总额：'+(dwTotal+overdueAmount)+'元');
                tfoot.find('tr:eq(0)').find('td:eq(2)').text('个人费用总额：'+grTotal+'元');
            }

            $('.mengban').show();
            $('.fundDetails').show();
            $('.fundDetails-title b ').unbind('click').on('click',function(){
                $('.mengban').hide();
                $('.fundDetails').hide()
            });
            $("#gjjDetailSureBtn").unbind('click').on('click',function(){
                $('.mengban').hide();
                $('.fundDetails').hide()
            });
        } else{
            if(result && result.msg){
                layer.alert(result.msg,{title:'提示信息'});
            } else{
                layer.alert('公积金详情无法查看',{title:'提示信息'});
            }
        }
    });

}

//关闭当前账单用户详情
function cancelPayorderDetail(){
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);
}

