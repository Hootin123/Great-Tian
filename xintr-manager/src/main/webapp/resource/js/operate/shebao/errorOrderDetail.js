

$(function(){

    // $("#table").freezeHeader({ 'height': '600px' });
    //初始化列表
     queryErrorCustomerOrders({"companyShebaoOrderId":$("#companyShebaoOrderId").val()});
    $("#payorderDetailSureBtn").click(function(){
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
    });
});

function queryErrorCustomerOrders(detailParams){
    $.ajax({
        type: "POST",
        url: BASE_PATH + "/shebaoManager/queryAllErrorBillCustomers.htm",
        dataType: "json",
        data: detailParams,
        success: function (data) {
            //console.log(data);
            if (data.success) {
                var customerShebaoOrderDtos=data.data;
                var html="";
                for(var i=0;i<customerShebaoOrderDtos.length;i++){
                    var customerShebaoOrderDto=customerShebaoOrderDtos[i];
                    html += "<tr memberid='"+customerShebaoOrderDto.customerId+"'>";
                    html += "<td rowspan='2'><p>"+(i+1)+"</p></td>";
                    html += "<td class='memberName' rowspan='2'>"+customerShebaoOrderDto.trueName+"</td>";
                    html += "<td class='deptName' rowspan='2'>"+customerShebaoOrderDto.depName+"</td>";
                    html += "<td rowspan='2'>"+customerShebaoOrderDto.phone+"</td>";
                    // html += "<td>社保</td>";
                    html += "<td>";
                    if(customerShebaoOrderDto.sbBase!=null&&customerShebaoOrderDto.sbBase!=undefined&&customerShebaoOrderDto.sbBase!=""){
                        html += "社保"+customerShebaoOrderDto.sbBase+"</td>";
                    }else{
                        html += "社保：无</td>";
                    }
                    html += "<td>";

                    // if(customerShebaoOrderDto.gjjBase!=undefined && customerShebaoOrderDto.gjjBase!=""&&customerShebaoOrderDto.gjjBase!=null){
                    //     html += "<p class='list-border'>公积金:"+customerShebaoOrderDto.gjjBase+"</p>";
                    // }else{
                    //     html += "<p class='list-border'>公积金：无</p>";
                    // }
                    // html += "</td>";
                    // html += "<td>";
                    // html += "<p>";
                    //社保异常详情
                    if(customerShebaoOrderDto.sbText!=null&&customerShebaoOrderDto.sbText!=""&&customerShebaoOrderDto.sbText!=undefined){
                        html+=parseStr(customerShebaoOrderDto.sbText);
                    }else{
                        html+="";
                    }
                    html += "</td>";
                    html += "<td>";
                    html+= parseStr((customerShebaoOrderDto.sbReason==null?'':customerShebaoOrderDto.sbReason))+"</td>"
                    html += "</tr>";
                    html += "<tr memberid='"+customerShebaoOrderDto.customerId+"'>";
                    // html += "<td>公积金</td>";
                    html += "<td>";
                    if(customerShebaoOrderDto.gjjBase!=undefined && customerShebaoOrderDto.gjjBase!=""&&customerShebaoOrderDto.gjjBase!=null){
                        html += "公积金"+customerShebaoOrderDto.gjjBase+"</td>";
                    }else{
                        html += "公积金：无</td>";
                    }
                    html += "<td>";
                    if(customerShebaoOrderDto.gjjText!=null&&customerShebaoOrderDto.gjjText!=""&&customerShebaoOrderDto.gjjText!=undefined){
                        html+=parseStr(customerShebaoOrderDto.gjjText);
                    }else{
                        html+="";
                    }
                    html += "</td>";
                    // html += "</p>";
                    html += "<td >";
                    html+= parseStr((customerShebaoOrderDto.gjjReason==null?'':customerShebaoOrderDto.gjjReason))+"</td>"
                    // html += "<td class='lastTd'><p  class='btnSocialDetails'>"+parseStr((customerShebaoOrderDto.sbReason==null?'':customerShebaoOrderDto.sbReason))+"</p><p class='list-border btnFundDetails'>"+parseStr((customerShebaoOrderDto.gjjReason==null?'':customerShebaoOrderDto.gjjReason))+"</p></td>";
                    html += "</tr>";
                }
                $("#detailTbody").html(html);
            } else {
                layer.alert('获取异常账单详细失败', {title: '提示信息'});
            }
        }
    });

}
//关闭页面窗口
function cancelErrorOrderDetail(){
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);
}


function parseStr(obj){

    if(obj!=null&&obj!=""&&obj!=undefined){
        //去除最后的“，”号
        obj=obj.substring(0,obj.length-1);
        var array = obj.split(",");
        if(array.length==1){
            return obj;
        }else{
            var html ="";
            for(var i=0;i<array.length;i++){
                html+=array[i]+"<br/>"
            }
            return html;
        }

    }else{
        return "";
    }
}