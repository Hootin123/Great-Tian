


$(function(){

   $(".complete").click(function(){

       var customerId =$("input[name='customerId']").val();
       var companyId = $("input[name='companyId']").val();
      //跳转到首页
      window.location= BASE_PATH+"/wechatCustomerBind/toCustomerIndex.htm?customerId="+customerId+"&companyId="+companyId;

   });


});