


$(function(){
    //初始化加载
     queryHistoryBills();
});
//初始化无条件查询所有的历史账单
function queryHistoryBills(){
    $.ajax({
       url:BASE_PATH+'/shebao/queryHistoryPayOrder.htm',
       type:'post',
       dataType:'json',
       data:$("#queryForm").serialize(),
       success:function(data){
         if(data.success){
           var list = data.data;
           var html="";
           $.each(list,function (i,data) {
              html+="<li>";
              html+="<div class='historyBill-main-top'>";
              html+="<p><span>"+data.joinCityName+""+timeDateTostr(data.orderDate)+"</span>账单</p>";
              html+="<p class='submit'>订单编号：<span>"+data.orderNumber+"</span>提交时间：<span>"+timeDateTostrOther(data.submitTime)+"</span></p>";
              html+="</div>";
              html+="<div class ='historyBill-main-table'><table>";
              html+="<tr><td>缴费月份</td><td>缴费类型</td><td>详情</td><td>费用总额</td><td>账单状态</td><td>操作</td></tr>";
              html+="<tr><td>"+timeDateTostr(data.orderDate)+"</td><td>社保</td>";
               //详情
               var orderSbDetail=data.orderSbDetail==undefined?'':data.orderSbDetail;
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
              html+="<td rowspan='2'><p>"+formatCurrency(data.priceSum)+"元</p>";
              if(data.priceService!=null&&data.priceService!=""&&data.priceService!=undefined) {
                  html += "<p>含服务费：<span onclick='showSer(this);' price_service=" +data.priceService + " customer_count=" + data.customerCount + "  price_single=" + data.priceSingle + ">" +  formatCurrency(data.priceService) + "元</span></p>";
              }
              if(data.priceAddtion!=null&&data.priceAddtion!=""&&data.priceAddtion!=undefined){
                  html+=" <p>含补差费用：<span onclick=showAddtion('"+data.id+"');>-"+data.priceAddtion+"元</span></p>";
               }
              html+="</td>";
              html+="<td rowspan='2'>";
              if(data.status==0){
                  html+="初始化";
              }else if(data.status==1){
                  html+="待提交";
              }else if(data.status==2){
                  html+="待付款";
              }else if(data.status==3){
                  html+="付款中";
              }else if(data.status==4){
                  html+="客户付款成功";
              }else if(data.status==5){
                  html+="办理中";
              }else if(data.status==6){
                  html+="办理完成";
              }else if(data.status==7){
                 html+="订单关闭";
              }else if(data.status===8){
                  html+="供应商付款成功";
              }
              html+="</td>";
              html+=" <td rowspan='2' class='showTd1'><a  onclick='orderDetail("+data.id+")'>账单详情</a><br>";
               //判断该账单是否是异常账单
               if(data.number!=null && data.number!="" && data.number!=undefined){
                   html+="<a onclick=queryErrorBill("+data.id+") style='color:red'>异常账单</a>";
               }
              html+="</td>";
              html+="</tr>"
              html+="<tr><td>"+timeDateTostr(data.orderDate)+"</td><td>公积金</td>";

               var orderGjjDetail=data.orderGjjDetail==undefined?'':data.orderGjjDetail;
               if(orderGjjDetail!=''){
                   var detailSbJSON=JSON.parse(orderGjjDetail);
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
              html+="</tr>";
              html+="</table></div>";
              html+="</li>";
           });
           //console.log(html);
           $(".historyBill-main ul").html(html)

         }else{
             layer.alert("查询失败");
         }

       },
       error:function(){
           layer.alert("网络错误，刷新页面")
       }


    });
}
//查询异常
function queryHistoryErrorBills(){
    var error = "error";
    $.ajax({
        url:BASE_PATH+'/shebao/queryHistoryPayOrder.htm',
        type:'post',
        dataType:'json',
        data:$("#queryForm").serialize(),
        success:function(data){
            if(data.success){
                var list = data.data;
                var html="";
                $.each(list,function (i,data) {
                    if(data.number!=null&&data.number!=""&&data.number!=null){
                    html+="<li>";
                    html+="<div class='historyBill-main-top'>";
                    html+="<p><span>"+data.joinCityName+""+timeDateTostr(data.orderDate)+"</span>账单</p>";
                    html+="<p class='submit'>订单编号：<span>"+data.orderNumber+"</span>提交时间：<span>"+timeDateTostrOther(data.submitTime)+"</span></p>";
                    //判断该账单是否是异常账单
                    // if(data.number!=null && data.number!="" && data.number!=undefined){
                    //     html+="<p class='abnormal' onclick='queryErrorBill("+data.id+");'>账单异常详情</p>";
                    // }
                    html+="</div>";
                    html+="<div class ='historyBill-main-table'><table>";
                    html+="<tr><td>缴费月份</td><td>缴费类型</td><td>详情</td><td>费用总额</td><td>账单状态</td><td>操作</td></tr>";
                    html+="<tr><td>"+timeDateTostr(data.orderDate)+"</td><td>社保</td>";
                    //详情
                    var orderSbDetail=data.orderSbDetail==undefined?'':data.orderSbDetail;
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
                    html+="<td rowspan='2'><p>"+formatCurrency(data.priceSum)+"元</p>";
                    if(data.priceService!=null&&data.priceService!=""&&data.priceService!=undefined) {
                        html += "<p>含服务费：<span onclick='showSer(this);' price_service=" +data.priceService + " customer_count=" + data.customerCount + "  price_single=" + data.priceSingle + ">" +  formatCurrency(data.priceService) + "元</span></p>";
                    }
                    if(data.priceAddtion!=null&&data.priceAddtion!=""&&data.priceAddtion!=undefined){
                        html+=" <p>含补差费用：<span onclick=showAddtion('"+data.id+"');>-"+data.priceAddtion+"元</span></p>";
                    }
                    html+="</td>";
                    html+="<td rowspan='2'>";
                    if(data.status==0){
                        html+="初始化";
                    }else if(data.status==1){
                        html+="待提交";
                    }else if(data.status==2){
                        html+="待付款";
                    }else if(data.status==3){
                        html+="付款中";
                    }else if(data.status==4){
                        html+="客户付款成功";
                    }else if(data.status==5){
                        html+="办理中";
                    }else if(data.status==6){
                        html+="办理完成";
                    }else if(data.status==7){
                        html+="订单关闭";
                    }else if(data.status==8){
                        html+="供应商付款成功";
                    }
                    html+="</td>";
                    html+=" <td rowspan='2' class='showTd1'><a  onclick='orderDetail("+data.id+")'>账单详情</a><br>";
                    //判断该账单是否是异常账单
                    if(data.number!=null && data.number!="" && data.number!=undefined){
                        html+="<a onclick=queryErrorBill("+data.id+") style='color:red'>异常账单</a>";
                    }
                    html+="</td>";
                    html+="</tr>"
                    html+="<tr><td>"+timeDateTostr(data.orderDate)+"</td><td>公积金</td>";

                    var orderGjjDetail=data.orderGjjDetail==undefined?'':data.orderGjjDetail;
                    if(orderGjjDetail!=''){
                        var detailSbJSON=JSON.parse(orderGjjDetail);
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
                    html+="</tr>";
                    html+="</table></div>";
                    html+="</li>";}

                });
                //console.log(html);
                $(".historyBill-main ul").html(html)

            }else{
                layer.alert("查询失败");
            }

        },
        error:function(){
            layer.alert("网络错误，刷新页面")
        }


    });

}

//点击搜索
function search(){
  //查询
   queryHistoryBills();
}


//将date时间转成string类型
function timeDateTostr(da){
    if(da!=null&&da!=""&&da!=undefined){
    var date = new Date();
    date.setTime(da);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    //console.log(y+m);
    return y+"年"+m+"月";
    }else{
        return " ";
    }
}

function timeDateTostrOther(da){
    if(da!=null&&da!=""&&da!=undefined){
        var date = new Date();
        date.setTime(da);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        m = m < 10 ? '0' + m : m;
        var d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = date.getHours();
        var M = date.getMinutes();
        var s = date.getSeconds();
        return y+"-"+m+"-"+d+" "+h+":"+M+":"+s;
    }else{
        return " ";
    }
}


//转成金额数字表示
function formatCurrency(num) {
    if(num!=null&&num!=""&&num!=undefined){
    num = num.toString().replace(/\$|\,/g,'');
    if(isNaN(num))
        num = "0";
    sign = (num == (num = Math.abs(num)));
    num = Math.floor(num*100+0.50000000001);
    cents = num%100;
    num = Math.floor(num/100).toString();
    if(cents<10)
        cents = "0" + cents;
    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
        num = num.substring(0,num.length-(4*i+3))+','+
            num.substring(num.length-(4*i+3));
    return (((sign)?'':'-') + num + '.' + cents);}else{
        return 0;
    }
}
//跳转到账单详情页面
function orderDetail(id){
    var path = BASE_PATH+"/shebao/payorder_detail.htm?orderId="+id;
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

//弹出服务费用
function showSer(obj) {
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
//补差费用
function showAddtion(id){
    var path = BASE_PATH+"/shebao/addtion_detail.htm?supplementCompanyOrderId="+id;
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

//跳转至异常账单详情页面
function queryErrorBill(orderId){
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




