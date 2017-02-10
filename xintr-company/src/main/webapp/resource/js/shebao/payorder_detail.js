$(document).ready(function () {
    // $("#table").freezeHeader({ 'height': '600px' });
    //初始化列表
    initPayOrderDetail({"companyShebaoOrderId":$("#companyShebaoOrderId").val()});

    $("#payorderDetailSureBtn").click(function(){
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
    });
    // var options = {
    //     valueNames: [ 'memberName', 'deptName' ]
    // };
    //
    // var userList = new List('orders', options);

    /*$('.seach').on('click', function () {
        var memberName = $('#memberName').val() || '';
        var deptName = $('#deptId').val() || '';

        userList.filter(function (item) {
            if (item.values().deptName == deptName || item.values().memberName.indexOf(memberName) != -1) {
                return true;
            } else {
                return false;
            }
        });
    });*/

    // $('#deptId').change(function () {
        // var selection = this.value;
        // userList.filter(function (item) {
        //     if (item.values().deptName == selection || selection == '') {
        //         return true;
        //     } else {
        //         return false;
        //     }
        // });
    // });

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
    $.ajax({
        type: "POST",
        url: BASE_PATH + "/shebao/payorderDetailList.htm",
        dataType: "json",
        data: detailParams,
        success: function (data) {
            console.log(data);
            if (data.success) {
                var customerShebaoOrderDtos=data.data;
                var html="";
                if (customerShebaoOrderDtos.length <= 0){
                    html = "<tr><td colspan='8'>该员工不存在</td></tr>";
                }
                else{
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
                        html += "<td style='text-align: left;'>";
                        if((customerShebaoOrderDto.isSubmitAccount===1 && (customerShebaoOrderDto.sbErrorReason==undefined|| customerShebaoOrderDto.sbErrorReason==""||customerShebaoOrderDto.sbErrorReason==null))
                            ||(customerShebaoOrderDto.isSubmitAccount===2)
                            ||(customerShebaoOrderDto.id!=undefined &&customerShebaoOrderDto.id!=null)){
                            html += "&nbsp;&nbsp;<a>";
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
                                html += "&nbsp;&nbsp;<a>补缴"+customerShebaoOrderDto.sbBjText+"</a>";
                            }
                        }else{
                            html += "&nbsp;&nbsp;无";
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
                        html += "<td style='text-align: left;'>";
                        if((customerShebaoOrderDto.isSubmitAccount===1 && (customerShebaoOrderDto.gjjErrorReason==undefined|| customerShebaoOrderDto.gjjErrorReason==""||customerShebaoOrderDto.gjjErrorReason==null))
                            ||(customerShebaoOrderDto.isSubmitAccount===2)
                            ||(customerShebaoOrderDto.id!=undefined &&customerShebaoOrderDto.id!=null)){
                            html += "&nbsp;&nbsp;<a>";
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
                                html += "&nbsp;&nbsp;<a>补缴"+customerShebaoOrderDto.gjjBjText+"</a>";
                            }
                        }else{
                            html += "&nbsp;&nbsp;无";
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

                }

                $("#detailTbody").html(html);
            } else {
                layer.alert('获取当前账单详细失败', {title: '提示信息'});
            }
        }
    });
}

// ....社保详情
function showSheBaoDetail(obj){
    var that = $(obj);

    $("#shebaoDetailTile").html(that.attr("name")+"社保详情<b></b>");
    //添加标题
    var tr = that.parents('tr:eq(0)');
    var remitTitle = that.prev().find('a:eq(0)').text().trim();
    // $('.socialDetails-main .remit-title').html(remitTitle);
    $('.socialDetails-main .remit-title').html("社保计算详情");

    // 有补缴
    // if(that.prev().find('a').length > 1){
    //     var fundPaymentTitle = that.prev().find('a:eq(1)').text().trim();
    //     $('.socialDetails-main-payment .payment-title').html(fundPaymentTitle);
    //     $('.socialDetails-main-payment').show();
    // } else{
    //     $('.socialDetails-main-payment').hide();
    // }

    $.getJSON(BASE_PATH + '/shebao/shebao_detail.htm', {
        memberId: that.parent('tr').attr('memberId'),
        orderId: that.parent('tr').attr('orderId')
    }, function (result) {
        if(result && result.success){

            var orderData = result.data.customberOrder;

            var orderDetail = orderData.orderDetail;
            if(orderDetail==null||orderDetail==undefined||orderDetail==""){
                layer.alert('社保详情无法查看',{title:'提示信息'});
            }
            var orderDetails = JSON.parse(orderData.orderDetail);
            var baseTbody=$('.socialDetails-main-remit table:eq(0)').find('tbody');
            //当前缴纳月
            var nowPayMonth=orderData.overdueMonth;
            var baseTbodyHtml="";
            for(i in orderDetails){
                var item = orderDetails[i];
                baseTbodyHtml += "<tr>";
                // if(item.code == 'yanglao'){
                //     baseTbodyHtml += "<td>养老</td>";
                // }else if(item.code == 'yiliao'){
                //     baseTbodyHtml += "<td>医疗</td>";
                // }else if(item.code == 'shiye'){
                //     baseTbodyHtml += "<td>失业</td>";
                // }else if(item.code == 'gongshang'){
                //     baseTbodyHtml += "<td>工伤</td>";
                // }else if(item.code == 'shengyu'){
                //     baseTbodyHtml += "<td>生育</td>";
                // }else if(item.code == 'canjijin'){
                //     baseTbodyHtml += "<td>残疾人保障金</td>";
                // }else if(item.code == 'dabing_yiliao'){
                //     baseTbodyHtml += "<td>大病医疗</td>";
                // }else{
                //     baseTbodyHtml += "<td></td>";
                // }
                baseTbodyHtml += "<td>"+(item.name==undefined||item.name==''?'':item.name)+"</td>";
                baseTbodyHtml += "<td>"+(item.rowBase==undefined||item.rowBase==''?'0':item.rowBase)+"</td>";
                if(item.orgbase!=undefined && item.orgbase!='' && item.orgbase!==0){
                    baseTbodyHtml += "<td>"+(item.orgprop==undefined||item.orgprop==''?'-':item.orgprop+'')+"</td>";
                }else{
                    baseTbodyHtml += "<td>"+(item.orgprop==undefined||item.orgprop==''||item.orgprop=='-'?'0%':item.orgprop+'')+"</td>";
                }
                baseTbodyHtml += "<td>"+(item.orgbase==undefined||item.orgbase==''?'0':item.orgbase)+"</td>";
                if(item.empbase!=undefined && item.empbase!='' && item.empbase!==0){
                    baseTbodyHtml += "<td>"+(item.empprop==undefined||item.empprop==''?'-':item.empprop+'')+"</td>";
                }else{
                    baseTbodyHtml += "<td>"+(item.empprop==undefined||item.empprop=='' || item.empprop=='-'?'0%':item.empprop+'')+"</td>";
                }
                baseTbodyHtml += "<td>"+(item.empbase==undefined||item.empbase==''?'0':item.empbase)+"</td>";
                baseTbodyHtml += "</tr>";
            }
            // baseTbodyHtml += "<tr>";
            // baseTbodyHtml += "<td>总计</td>";
            // baseTbodyHtml += "<td colspan='3'>单位费用："+(orderData.orgSum||'0')+"元</td>";
            // baseTbodyHtml += "<td colspan='2'>个人费用："+(orderData.empSum||'0')+"元</td>";
            // baseTbodyHtml += "</tr>";
            baseTbody.html(baseTbodyHtml);

            // var tbody = $('.socialDetails-main-payment table:eq(0)').find('tbody');
            // tbody.html('');
            // var bjList = result.data.bjList;
            // var dwPay = 0, grPay = 0;
            // // 补缴
            //
            // if(bjList && bjList.length > 0){
            //     var bjTbodyHtml="";
            //     for(i in bjList){
            //         var item = bjList[i];
            //         var orderDetails = JSON.parse(item.orderDetail);
            //         var overdueAmount=item.shebaoOrderOverdue==undefined?0:item.shebaoOrderOverdue;
            //
            //         var dwTotal = 0, grTotal = 0;
            //         var mon = new Date(item.overdueMonth).format('Ym');
            //         //获取总数
            //         for(j in orderDetails){
            //             bjTbodyHtml += "<tr>";
            //             if(j==0){
            //                 bjTbodyHtml += "<td rowspan='"+orderDetails.length+"'>"+mon+"</td>";
            //             }
            //             var bjItem=orderDetails[j];
            //             if(bjItem.code == 'yanglao'){
            //                 bjTbodyHtml += "<td>养老</td>";
            //             }else if(bjItem.code == 'yiliao'){
            //                 bjTbodyHtml += "<td>医疗</td>";
            //             }else if(bjItem.code == 'shiye'){
            //                 bjTbodyHtml += "<td>失业</td>";
            //             }else if(bjItem.code == 'gongshang'){
            //                 bjTbodyHtml += "<td>工伤</td>";
            //             }else if(bjItem.code == 'shengyu'){
            //                 bjTbodyHtml += "<td>生育</td>";
            //             }else if(bjItem.code == 'canjijin'){
            //                 bjTbodyHtml += "<td>残疾人保障金</td>";
            //             }else if(bjItem.code == 'dabing_yiliao'){
            //                 bjTbodyHtml += "<td>大病医疗</td>";
            //             }else{
            //                 bjTbodyHtml += "<td></td>";
            //             }
            //             bjTbodyHtml += "<td>"+(bjItem.rowBase==undefined||bjItem.rowBase==''?'0':bjItem.rowBase)+"</td>";
            //             if(bjItem.orgbase!=undefined && bjItem.orgbase!='' && bjItem.orgbase!==0){
            //                 bjTbodyHtml += "<td>"+(bjItem.orgprop==undefined||bjItem.orgprop==''?'-':bjItem.orgprop+'')+"</td>";
            //             }else{
            //                 bjTbodyHtml += "<td>"+(bjItem.orgprop==undefined||bjItem.orgprop=='' ?'0%':bjItem.orgprop+'')+"</td>";
            //             }
            //             bjTbodyHtml += "<td>"+(bjItem.orgbase==undefined||bjItem.orgbase==''?'0':bjItem.orgbase)+"</td>";
            //             if(j==0){
            //                 bjTbodyHtml += "<td rowspan='"+orderDetails.length+"'>"+(overdueAmount)+"</td>";
            //             }
            //             if(bjItem.empbase!=undefined && bjItem.empbase!='' && bjItem.empbase!==0){
            //                 bjTbodyHtml += "<td>"+(bjItem.empprop==undefined||bjItem.empprop==''?'-':bjItem.empprop+'')+"</td>";
            //             }else{
            //                 bjTbodyHtml += "<td>"+(bjItem.empprop==undefined||bjItem.empprop==''?'0%':bjItem.empprop+'')+"</td>";
            //             }
            //             bjTbodyHtml += "<td>"+(bjItem.empbase==undefined||bjItem.empbase==''?'0':bjItem.empbase)+"</td>";
            //             bjTbodyHtml += "</tr>";
            //
            //             dwTotal += bjItem.orgbase;
            //             grTotal += bjItem.empbase;
            //         }
            //
            //         dwTotal += overdueAmount;
            //         bjTbodyHtml += "<tr>";
            //         bjTbodyHtml += "<td colspan='6'>单位费用："+ dwTotal.toFixed(3) +"元</td>";
            //         bjTbodyHtml += "<td colspan='2'>个人费用："+ grTotal.toFixed(3) +"元</td>";
            //         bjTbodyHtml += "</tr>";
            //         dwPay += dwTotal;
            //         grPay += grTotal;
            //     }
            //     tbody.html(bjTbodyHtml);
            //     var tfoot = $('.socialDetails-main-payment table:eq(0)').find('tfoot');
            //     tfoot.find('td:eq(1)').text('单位费用总额：' + dwPay.toFixed(3)+'元');
            //     tfoot.find('td:eq(2)').text('个人费用总额：' + grPay.toFixed(3)+'元');
            // }

            var tbody = $('.socialDetails-main-payment table:eq(0)').find('tbody');
            tbody.html('');
            var bjList = result.data.bjList;
            var orgTotalAmount=0;
            var empTotalAmount=0;
            var overdueTotalAmount=0;
            var totalAmount=0;
            if(bjList && bjList.length > 0){
                var detailTbodyHtml="";
                for(i in bjList){
                    var detailItem=bjList[i];
                    var mon = new Date(nowPayMonth).format('Ym');
                    var orderType=detailItem.orderType;
                    var payMonth=new Date(detailItem.overdueMonth).format('Ym');
                    var orgSum=0;
                    if(detailItem.shebaotongOrgPay!=undefined && detailItem.shebaotongOrgPay!='' ){
                        orgSum=detailItem.shebaotongOrgPay;
                    }else{
                        orgSum=detailItem.orgSum==undefined || detailItem.orgSum==''?0:detailItem.orgSum;
                    }

                    var empSum=0;
                    if(detailItem.shebaotongEmpPay!=undefined && detailItem.shebaotongEmpPay!='' ){
                        empSum=detailItem.shebaotongEmpPay;
                    }else{
                        empSum=detailItem.empSum==undefined || detailItem.empSum==''?0:detailItem.empSum;
                    }

                    var shebaoOrderOverdue=detailItem.shebaoOrderOverdue==undefined || detailItem.shebaoOrderOverdue==''?0:detailItem.shebaoOrderOverdue;
                    detailTbodyHtml += "<tr>";
                    detailTbodyHtml += "<td>"+mon+"</td>";
                    if(orderType==1){
                        detailTbodyHtml += "<td>增员"+payMonth+"</td>";
                    }else if(orderType==2){
                        detailTbodyHtml += "<td>续缴"+payMonth+"</td>";
                    }else if(orderType==3){
                        detailTbodyHtml += "<td>调基"+payMonth+"</td>";
                    }else if(orderType==4){
                        detailTbodyHtml += "<td>停缴"+payMonth+"</td>";
                    }else if(orderType==5){
                        detailTbodyHtml += "<td>补缴"+payMonth+"</td>";
                    }else{
                        detailTbodyHtml += "<td></td>";
                    }
                    detailTbodyHtml += "<td>"+orgSum+"</td>";
                    detailTbodyHtml += "<td>"+empSum+"</td>";
                    detailTbodyHtml += "<td>"+shebaoOrderOverdue+"</td>";
                    detailTbodyHtml += "<td>"+((orgSum+empSum+shebaoOrderOverdue).toFixed(3))+"</td>";
                    detailTbodyHtml += "</tr>";
                    orgTotalAmount += orgSum;
                    empTotalAmount += empSum;
                    overdueTotalAmount += shebaoOrderOverdue;
                    totalAmount += (orgSum+empSum+shebaoOrderOverdue);
                }
            }
            tbody.html(detailTbodyHtml);
            var tfoot = $('.socialDetails-main-payment table:eq(0)').find('tfoot');
            tfoot.find('td:eq(2)').text(orgTotalAmount.toFixed(2));
            tfoot.find('td:eq(3)').text(empTotalAmount.toFixed(2));
            tfoot.find('td:eq(4)').text(overdueTotalAmount.toFixed(2));
            tfoot.find('td:eq(5)').text(totalAmount.toFixed(2));
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
function showGjjDetail(obj){
    var that = $(obj);

    $("#gjjDetailTile").html(that.attr("name")+"公积金详情<b></b>");
    var fundRemitTitle = that.prev().find('a:eq(0)').text().trim();
    $('.fundDetails-main-payment table:eq(0)').find('tbody').html('');
    // $('.fundDetails-main-remit .fundRemit-title').html(fundRemitTitle);
    $('.fundDetails-main-remit .fundRemit-title').html("公积金计算详情");
    // 有补缴
    // if(that.prev().find('a').length > 1){
    //     var fundPaymentTitle = that.prev().find('a:eq(1)').text().trim();
    //     console.log(fundPaymentTitle);
    //     $('.fundDetails-main-payment .fundPayment-title').html(fundPaymentTitle);
    //
    //     $('.fundDetails-main-payment').show();
    // } else{
    //     $('.fundDetails-main-payment').hide();
    // }
    $.getJSON(BASE_PATH + '/shebao/gjj_detail.htm', {
        memberId: that.parent('tr').attr('memberId'),
        orderId: that.parent('tr').attr('orderId')
    }, function (result) {
        if(result && result.success){

            var orderData = result.data.customberOrder;
            var orderDetails = JSON.parse(orderData.orderDetail);
            var baseTbody=$('.fundDetails-main-remit table:eq(0)').find('tbody');
            //当前缴纳月
            var nowPayMonth=orderData.overdueMonth;
            var baseTbodyHtml="";
            baseTbody.html("");
            if(orderDetails!=null && orderDetails.length>0){
                for(i in orderDetails){
                    var item = orderDetails[i];
                    baseTbodyHtml += "<tr>";
                    // if(item.code == 'gongjijin'){
                    //     baseTbodyHtml += "<td>公积金</td>";
                    // }else if(item.code == 'buchong_gongjijin'){
                    //     baseTbodyHtml += "<td>补充公积金</td>";
                    // }else{
                    //     baseTbodyHtml += "<td></td>";
                    // }
                    baseTbodyHtml += "<td>"+(item.name==undefined||item.name==''?'':item.name)+"</td>";
                    baseTbodyHtml += "<td>"+(item.rowBase==undefined||item.rowBase==''?'0':item.rowBase)+"</td>";
                    if(item.orgbase!=undefined && item.orgbase!='' && item.orgbase!==0){
                        baseTbodyHtml += "<td>"+(item.orgprop==undefined||item.orgprop==''?'-':item.orgprop+'')+"</td>";
                    }else{
                        baseTbodyHtml += "<td>"+(item.orgprop==undefined||item.orgprop==''||item.orgprop=='-'?'0%':item.orgprop+'')+"</td>";
                    }
                    baseTbodyHtml += "<td>"+(item.orgbase==undefined||item.orgbase==''?'0':item.orgbase)+"</td>";
                    if(item.empbase!=undefined && item.empbase!='' && item.empbase!==0){
                        baseTbodyHtml += "<td>"+(item.empprop==undefined||item.empprop==''?'-':item.empprop+'')+"</td>";
                    }else{
                        baseTbodyHtml += "<td>"+(item.empprop==undefined||item.empprop==''|| item.empprop=='-'?'0%':item.empprop+'')+"</td>";
                    }
                    baseTbodyHtml += "<td>"+(item.empbase==undefined||item.empbase==''?'0':item.empbase)+"</td>";
                    baseTbodyHtml += "</tr>";
                }
            }
            // baseTbodyHtml += "<tr>";
            // baseTbodyHtml += "<td>总计</td>";
            // baseTbodyHtml += "<td colspan='3'>单位费用："+(orderData.orgSum||'0')+"元</td>";
            // baseTbodyHtml += "<td colspan='2'>个人费用："+(orderData.empSum||'0')+"元</td>";
            // baseTbodyHtml += "</tr>";
            baseTbody.html(baseTbodyHtml);

            // var bjList = result.data.bjList;
            // var dwPay = 0, grPay = 0;
            // if(bjList && bjList.length > 0){
            //     var tbody = $('.fundDetails-main-payment table:eq(0)').find('tbody');
            //     var bjTbodyHtml="";
            //     for(i in bjList){
            //         var item = bjList[i];
            //         var mon = new Date(item.overdueMonth).format('Ym');
            //         // var orderDetail = JSON.parse(item.orderDetail)[0];
            //         var orderDetails2 = JSON.parse(item.orderDetail);
            //         var overdueAmount=item.shebaoOrderOverdue==undefined?0:item.shebaoOrderOverdue;
            //         var grTotal = 0, dwTotal = 0;
            //         for(var j in orderDetails2){
            //             var bjItem=orderDetails2[j];
            //             bjTbodyHtml += "<tr>";
            //             if(j==0){
            //                 bjTbodyHtml += "<td rowspan='"+orderDetails2.length+"'>"+mon+"</td>";
            //             }
            //             if(bjItem.code == 'gongjijin'){
            //                 bjTbodyHtml += "<td>公积金</td>";
            //             }else if(bjItem.code == 'buchong_gongjijin'){
            //                 bjTbodyHtml += "<td>补充公积金</td>";
            //             }else{
            //                 bjTbodyHtml += "<td></td>";
            //             }
            //             bjTbodyHtml += "<td>"+(bjItem.rowBase==undefined||bjItem.rowBase==''?'0':bjItem.rowBase)+"</td>";
            //             if(bjItem.orgbase!=undefined && bjItem.orgbase!='' && bjItem.orgbase!==0){
            //                 bjTbodyHtml += "<td>"+(bjItem.orgprop==undefined||bjItem.orgprop==''?'-':bjItem.orgprop+'')+"</td>";
            //             }else{
            //                 bjTbodyHtml += "<td>"+(bjItem.orgprop==undefined||bjItem.orgprop==''?'0%':bjItem.orgprop+'')+"</td>";
            //             }
            //             bjTbodyHtml += "<td>"+(bjItem.orgbase==undefined||bjItem.orgbase==''?'0':bjItem.orgbase)+"</td>";
            //             if(j==0){
            //                 bjTbodyHtml += "<td rowspan='"+orderDetails2.length+"'>"+(overdueAmount)+"</td>";
            //             }
            //             if(bjItem.empbase!=undefined && bjItem.empbase!='' && bjItem.empbase!==0){
            //                 bjTbodyHtml += "<td>"+(bjItem.empprop==undefined||bjItem.empprop==''?'-':bjItem.empprop+'')+"</td>";
            //             }else{
            //                 bjTbodyHtml += "<td>"+(bjItem.empprop==undefined||bjItem.empprop==''?'0%':bjItem.empprop+'')+"</td>";
            //             }
            //             bjTbodyHtml += "<td>"+(bjItem.empbase==undefined||bjItem.empbase==''?'0':bjItem.empbase)+"</td>";
            //             bjTbodyHtml += "</tr>";
            //             dwTotal += bjItem.orgbase;
            //             grTotal += bjItem.empbase;
            //         }
            //         dwTotal += overdueAmount;
            //         dwPay += dwTotal;
            //         grPay += grTotal;
            //     }
            //
            //     tbody.html(bjTbodyHtml);
            //     var tfoot = $('.fundDetails-main-payment table:eq(0)').find('tfoot');
            //     tfoot.find('tr:eq(0)').find('td:eq(1)').text('单位费用总额：'+(dwPay.toFixed(3))+'元');
            //     tfoot.find('tr:eq(0)').find('td:eq(2)').text('个人费用总额：'+grPay.toFixed(3)+'元');
            // }

            var tbody = $('.fundDetails-main-payment table:eq(0)').find('tbody');
            tbody.html('');
            var bjList = result.data.bjList;
            var orgTotalAmount=0;
            var empTotalAmount=0;
            var overdueTotalAmount=0;
            var totalAmount=0;
            if(bjList && bjList.length > 0){
                var detailTbodyHtml="";
                for(i in bjList){
                    var detailItem=bjList[i];
                    var mon = new Date(nowPayMonth).format('Ym');
                    var orderType=detailItem.orderType;
                    var payMonth=new Date(detailItem.overdueMonth).format('Ym');
                    // var orgSum=detailItem.orgSum==undefined || detailItem.orgSum==''?0:detailItem.orgSum;
                    // var empSum=detailItem.empSum==undefined || detailItem.empSum==''?0:detailItem.empSum;

                    var orgSum=0;
                    if(detailItem.shebaotongOrgPay!=undefined && detailItem.shebaotongOrgPay!='' ){
                        orgSum=detailItem.shebaotongOrgPay;
                    }else{
                        orgSum=detailItem.orgSum==undefined || detailItem.orgSum==''?0:detailItem.orgSum;
                    }

                    var empSum=0;
                    if(detailItem.shebaotongEmpPay!=undefined && detailItem.shebaotongEmpPay!='' ){
                        empSum=detailItem.shebaotongEmpPay;
                    }else{
                        empSum=detailItem.empSum==undefined || detailItem.empSum==''?0:detailItem.empSum;
                    }

                    var shebaoOrderOverdue=detailItem.shebaoOrderOverdue==undefined || detailItem.shebaoOrderOverdue==''?0:detailItem.shebaoOrderOverdue;
                    detailTbodyHtml += "<tr>";
                    detailTbodyHtml += "<td>"+mon+"</td>";
                    if(orderType==1){
                        detailTbodyHtml += "<td>增员"+payMonth+"</td>";
                    }else if(orderType==2){
                        detailTbodyHtml += "<td>续缴"+payMonth+"</td>";
                    }else if(orderType==3){
                        detailTbodyHtml += "<td>调基"+payMonth+"</td>";
                    }else if(orderType==4){
                        detailTbodyHtml += "<td>停缴"+payMonth+"</td>";
                    }else if(orderType==5){
                        detailTbodyHtml += "<td>补缴"+payMonth+"</td>";
                    }else{
                        detailTbodyHtml += "<td></td>";
                    }
                    detailTbodyHtml += "<td>"+orgSum+"</td>";
                    detailTbodyHtml += "<td>"+empSum+"</td>";
                    detailTbodyHtml += "<td>"+shebaoOrderOverdue+"</td>";
                    detailTbodyHtml += "<td>"+((orgSum+empSum+shebaoOrderOverdue).toFixed(3))+"</td>";
                    detailTbodyHtml += "</tr>";
                    orgTotalAmount += orgSum;
                    empTotalAmount += empSum;
                    overdueTotalAmount += shebaoOrderOverdue;
                    totalAmount += (orgSum+empSum+shebaoOrderOverdue);
                }
            }
            tbody.html(detailTbodyHtml);
            var tfoot = $('.fundDetails-main-payment table:eq(0)').find('tfoot');
            tfoot.find('td:eq(2)').text(orgTotalAmount.toFixed(2));
            tfoot.find('td:eq(3)').text(empTotalAmount.toFixed(2));
            tfoot.find('td:eq(4)').text(overdueTotalAmount.toFixed(2));
            tfoot.find('td:eq(5)').text(totalAmount.toFixed(2));
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

