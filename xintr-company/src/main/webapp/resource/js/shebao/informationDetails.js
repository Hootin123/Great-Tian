

$(function(){

    var customerId =$("input[name='customerId']").val();
    queryData(customerId);
    //请求后台接口

});

//关闭页面
function  gunbi(){
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);

}
function confirm(){
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
}

//查询
function queryData(customerId){
     $.ajax({
       url:BASE_PATH+'/shebao/searchInfoDetails.htm',
       dataType:'json',
       type:'post',
       data:{'customerId':customerId},
       success:function(data){
           if(data.success){
               //赋值基本信息
               var returnMap = data.data;
               var infoMap = returnMap.infoMap;
              //赋值详情信息
              //用户姓名、部门、手机号、身份证号、参保地区、
              $("#trueName").text(infoMap.customer_turename);
              $("#depName").text(infoMap.dep_name);
              $("#customerPhone").text(infoMap.customer_phone);
              $("#joinCityName").text(infoMap.join_city_name);
              $("#customerIdcard").text(infoMap.customer_idcard);
               //判断用户是否交过社保
               if(returnMap.isSbKeep==0){
                   //未缴纳
                   $("#sbJiaonaState").text("未缴纳");
                   $("#sbBase").text("未设置");
               }else{
                   $("#sbJiaonaState").text("缴纳中");
                   $("#sbBase").text((infoMap.sb_base==null?"未设置":infoMap.sb_base+'元'));
                   //社保缴纳起始月
                   var shebaoFirstMonth=returnMap.shebaoFirstMonth;
                   if(shebaoFirstMonth==undefined||shebaoFirstMonth==null){
                       shebaoFirstMonth="";
                   }
                    var sbMonthHtml ="<p><span style='float: none'>缴纳起始年月："+changeTime(shebaoFirstMonth)+"</span></p>";
                   $("#shebaoParent").append(sbMonthHtml);
                   //社保当前月缴纳详情
                   var sbOrders= JSON.parse(returnMap.orderDetailSb);
                   var html ="";
                   html+="<div class='social-title'>社保费用详情：</div>";
                   html+="<table>";
                   html+="<thead>";
                   html+="<tr>";
                   html+="<td>险种</td><td>基数（元）</td><td>单位比例</td><td>单位费用（元）</td><td>个人比例</td><td>个人费用（元）</td>";
                   html+="</tr>";
                   html+="</thread>";
                   html+="<tbody>";
                   var orgSum=0;//公司部分总和
                   var emSum=0; //个人总和
                   for (var i=0;i<sbOrders.length;i++){

                         //养老保险

                         //if(sbOrders[i].code=="yanglao"){
                             html+="<tr>";
                             html+="<td>" +sbOrders[i].name+ "</td><td>"+sbOrders[i].rowBase+"</td><td>"+sbOrders[i].orgprop+"</td>";
                             html+="<td>"+sbOrders[i].orgbase+"</td><td>"+sbOrders[i].empprop+"</td><td>"+sbOrders[i].empbase+"</td>";
                             html+="</tr>";
                             orgSum+=sbOrders[i].orgbase;
                             emSum+=sbOrders[i].empbase;
                       //  }
                       // //医疗
                       //  if(sbOrders[i].code=="yiliao"){
                       //      html+="<tr>";
                       //      html+="<td>医疗</td><td>"+sbOrders[i].base+"</td><td>"+sbOrders[i].orgprop+"</td>";
                       //      html+="<td>"+sbOrders[i].orgbase+"</td><td>"+sbOrders[i].empprop+"</td><td>"+sbOrders[i].empbase+"</td>";
                       //      html+="</tr>";
                       //      orgSum+=sbOrders[i].orgbase;
                       //      emSum+=sbOrders[i].empbase;
                       //  }
                       // //失业
                       //  if(sbOrders[i].code=="shiye"){
                       //      html+="<tr>";
                       //      html+="<td>失业</td><td>"+sbOrders[i].base+"</td><td>"+sbOrders[i].orgprop+"</td>";
                       //      html+="<td>"+sbOrders[i].orgbase+"</td><td>"+sbOrders[i].empprop+"</td><td>"+sbOrders[i].empbase+"</td>";
                       //      html+="</tr>";
                       //      orgSum+=sbOrders[i].orgbase;
                       //      emSum+=sbOrders[i].empbase;
                       //  }
                       ////工商
                       //if(sbOrders[i].code=="gongshang"){
                       //    html+="<tr>";
                       //    html+="<td>工商</td><td>"+sbOrders[i].base+"</td><td>"+sbOrders[i].orgprop+"</td>";
                       //    html+="<td>"+sbOrders[i].orgbase+"</td><td>"+sbOrders[i].empprop+"</td><td>"+sbOrders[i].empbase+"</td>";
                       //    html+="</tr>";
                       //    orgSum+=sbOrders[i].orgbase;
                       //    emSum+=sbOrders[i].empbase;
                       //}
                       ////生育
                       //if(sbOrders[i].code=="shengyu"){
                       //    html+="<tr>";
                       //    html+="<td>生育</td><td>"+sbOrders[i].base+"</td><td>"+sbOrders[i].orgprop+"</td>";
                       //    html+="<td>"+sbOrders[i].orgbase+"</td><td>"+sbOrders[i].empprop+"</td><td>"+sbOrders[i].empbase+"</td>";
                       //    html+="</tr>";
                       //    orgSum+=sbOrders[i].orgbase;
                       //    emSum+=sbOrders[i].empbase;
                       //}
                       //
                       ////残疾人保障金
                       //if(sbOrders[i].code=="canjjin"){
                       //    html+="<tr>";
                       //    html+="<td>残疾人保障金</td><td>"+sbOrders[i].base+"</td><td>"+sbOrders[i].orgprop+"</td>";
                       //    html+="<td>"+sbOrders[i].orgbase+"</td><td>"+sbOrders[i].empprop+"</td><td>"+sbOrders[i].empbase+"</td>";
                       //    html+="</tr>";
                       //    orgSum+=sbOrders[i].orgbase;
                       //    emSum+=sbOrders[i].empbase;
                       //}

                   }
                   //总计
                   html+="<tr>";
                   html+="<td>总计</td>";
                   html+="<td colspan='3'>单位费用："+parseFloat(orgSum).toFixed(2)+"（元）</td>";
                   html+="<td colspan='2'>个人费用："+parseFloat(emSum).toFixed(2)+"（元）</td>"
                   html+="</tr>";
                   html+="</tbody>";
                   html+="</table>";
                   html+="</div>";
                   $("#shebaoParent").append(html);
               }

               //判断用户是否交过公积金
               if(returnMap.isGjjKeep==0){
                   $("#gjjJiaonaState").text("未缴纳");
                   $("#gjjBase").text("未设置");
               }else{
                   $("#gjjJiaonaState").text("缴纳中");
                   $("#gjjBase").text((infoMap.gjj_base==null?"未设置":infoMap.gjj_base+'元'));
                   //公积金缴纳起始月
                   var gjjFirstMonth = returnMap.gjjFirstMonth;
                   if(gjjFirstMonth==undefined||gjjFirstMonth==null){
                       gjjFirstMonth="";
                   }
                   var gjjMonthHtml ="<p><span style='float: none'>缴纳起始年月："+changeTime(gjjFirstMonth)+"</span></p>";
                   $("#gjjParent").append(gjjMonthHtml);
                   //公积金当前月缴纳详情
                   var gjjOrders= JSON.parse(returnMap.orderDetailGjj);
                   var  html ="";
                   html+="<div class='fund-title'>公积金费用详情：</div>";
                   html+="<table>";
                   html+="<thead>";
                   html+="<tr>";
                   html+="<td>险种</td><td>基数（元）</td><td>单位比例</td><td>单位费用（元）</td><td>个人比例</td><td>个人费用（元）</td>";
                   html+="</tr>";
                   html+="</thread>";
                   html+="<tbody>";
                   for(var i=0;i<gjjOrders.length;i++){
                       if(gjjOrders[i].code=="gongjijin"){
                           html+="<tr><td>" +gjjOrders[i].name+ "</td><td>"+gjjOrders[i].base+"</td><td>"+gjjOrders[i].orgprop+"</td>";
                           html+="<td>"+gjjOrders[i].orgbase+"</td><td>"+gjjOrders[i].empprop+"</td><td>"+gjjOrders[i].empbase+"</td></tr>"
                           html+="<tr><td>总计</td><td colspan='3'>单位费用："+parseFloat(gjjOrders[i].orgbase).toFixed(2)+"（元）</td><td colspan='2'>个人费用："+parseFloat(gjjOrders[i].empbase).toFixed(2)+"（元）</td></tr></tbody></table></div>"
                       }
                   }

                   $("#gjjParent").append(html);

               }

           }else{
               alert(data.message);
           }
       },
       error:function(){
           alert("网络错误,请刷新页面");
       }

     });
}


function toDecimal(x) {
    if(x!=null&&x!=""&&x!=undefined){
    var val = Number(x)
    if(!isNaN(parseFloat(val))) {
        val = val.toFixed(1);
    }
    return  val;
    }
}

function changeTime(str){
    if(str!=null&&str!=""&&str!=undefined)
    var value = str.substr(0,4)+"年"+str.substr(4,6)+"月";
    return value;
}