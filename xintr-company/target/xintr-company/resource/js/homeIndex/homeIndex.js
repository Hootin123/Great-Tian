

$(function(){

    //判断快捷按钮的权限
    var  customerAuth =$("input[name='customerAuth']").val();
    var  shebaoAuth =$("input[name='shebaoAuth']").val();
    var  expenseAuth =$("input[name='expenseAuth']").val();

    if(customerAuth==null||customerAuth==undefined||customerAuth==""){
        //没有员工管理权限
        $("#addCustomer-li,#orgFramework-li").hide();
    }
    if(shebaoAuth==null||shebaoAuth==undefined||shebaoAuth==""){
        //没有社保公积金权限
        $("#sbOrder-li").hide();
    }
    if(expenseAuth==null||expenseAuth==undefined||expenseAuth==""){
        //没有报销权限
        $("#expenseInput-li").hide();
    }


  //添加员工
   $("#addCustomer").click(function(){
       var menu = "员工管理";
       //判断是否有权限
       $.ajax({
           url: BASE_PATH + '/hasAuthority.htm',
           type: 'POST',
           dataType: 'json',
           data: {'menu': menu},
           success: function (data) {
               if (data.success) {
                   if (data.data) {
                       //有权限
                       var index = layer.open({
                           type: 2,
                           title: false,
                           maxmin: false,
                           closeBtn: 0,
                           shadeClose: false, //点击遮罩关闭层
                           area: ['650px', '380px'],
                           content: BASE_PATH + '/customerManager/toCustomerMainPageNew.htm?from=index'
                       });

                       //窗口最大化
                       layer.full(index);

                   } else {
                       //无权限
                       window.location = BASE_PATH + "/toNoAuthorityPage.htm";
                   }
               } else {
                   layer.alert(data.message);
               }
           }
       });
   });

  //组织架构
  $("#orgFramework").click(function(){
      var menu = "员工管理";
      //判断是否有权限
      $.ajax({
          url: BASE_PATH + '/hasAuthority.htm',
          type: 'POST',
          dataType: 'json',
          data: {'menu': menu},
          success: function (data) {
              if (data.success) {
                  if (data.data) {
                      var index = layer.open({
                          type: 2,
                          title: false,
                          maxmin: false,
                          closeBtn: 0,
                          shadeClose: false, //点击遮罩关闭层
                          area: ['650px', '380px'],
                          content: BASE_PATH + "/dept/deptIndex.htm"
                      });

                      //窗口最大化
                      layer.full(index);
                  } else {
                      //无权限
                      window.location = BASE_PATH + "/toNoAuthorityPage.htm";
                  }
              } else {
                  layer.alert(data.message);
              }
          }
      });

  });

  //发工资
  $("#sendPay").click(function(){
      var menu = "工资核算";
      var payCycleId = $("#payCycleId").val();
      //判断是否有权限
      $.ajax({
          url: BASE_PATH + '/hasAuthority.htm',
          type: 'POST',
          dataType: 'json',
          data: {'menu': menu},
          success: function (data) {
              if (data.success) {
                  if (data.data) {
                      //有权限
                      //判断当前账户的记薪周期有没有设置
                      if (payCycleId != null && payCycleId != undefined && payCycleId != "") {
                          var index = layer.open({
                              type: 2,
                              title: false,
                              closeBtn: 0,
                              maxmin: false,
                              shadeClose: false, //点击遮罩关闭层
                              area: ['650px', '380px'],
                              content: BASE_PATH + "/monthlypayroll/monthlypayroll.htm"
                          });
                          //窗口最大化
                          layer.full(index);
                      } else {
                          //跳转到薪资设置页面
                          window.location = BASE_PATH + "/payrollAccount/toOneenter.htm"
                      }
                  } else {
                      //无权限
                      window.location = BASE_PATH + "/toNoAuthorityPage.htm";
                  }
              } else {
                  layer.alert(data.message);
              }
          }
      });
  });

  //设置奖金津贴
  $("#setBonus").click(function(){
      //判断是否有权限
      var menu = "工资核算";
      $.ajax({
          url: BASE_PATH + '/hasAuthority.htm',
          type: 'POST',
          dataType: 'json',
          data: {'menu': menu},
          success: function (data) {
              if (data.success) {
                  if (data.data) {
                      var a = BASE_PATH + "/salary_setting/allowance.htm";
                      var index = layer.open({
                          type: 2,
                          title: false,
                          closeBtn: 0,
                          maxmin: false,
                          shadeClose: false, //点击遮罩关闭层
                          area: ['650px', '380px'],
                          content:a
                      });
                      //窗口最大化
                      layer.full(index);
                  } else {
                      //无权限
                      window.location = BASE_PATH + "/toNoAuthorityPage.htm";
                  }
              } else {
                  layer.alert(data.message);
              }
          }

      });
  });

  //社保账单
 $("#sbOrder").click(function(){

     //社保的当前账单列表
     //判断是否有权限
     var menu = "社保公积金";
     $.ajax({
         url: BASE_PATH + '/hasAuthority.htm',
         type: 'POST',
         dataType: 'json',
         data: {'menu': menu},
         success: function (data) {
             if (data.success) {
                 if (data.data) {
                     var a = BASE_PATH + "/shebao/current_payorder.htm";
                     var index = layer.open({
                         type: 2,
                         title: false,
                         closeBtn: 0,
                         maxmin: false,
                         shadeClose: false, //点击遮罩关闭层
                         area: ['650px', '380px'],
                         content:a
                     });
                     //窗口最大化
                     layer.full(index);
                 } else {
                     //无权限
                     window.location = BASE_PATH + "/toNoAuthorityPage.htm";
                 }
             } else {
                 layer.alert(data.message);
             }
         }

     });
 });


  //报销录入
    $("#expenseInput").click(function(){
   //判断是否有权限
        var menu = "报销管理";
        $.ajax({
            url: BASE_PATH + '/hasAuthority.htm',
            type: 'POST',
            dataType: 'json',
            data: {'menu': menu},
            success: function (data) {
                if (data.success) {
                    if (data.data) {
                        var a = BASE_PATH + "/expense/expense.htm";
                        var index = layer.open({
                            type: 2,
                            title: false,
                            closeBtn: 0,
                            maxmin: false,
                            shadeClose: false, //点击遮罩关闭层
                            area: ['650px', '380px'],
                            content:a
                        });
                        //窗口最大化
                        layer.full(index);
                    } else {
                        //无权限
                        window.location = BASE_PATH + "/toNoAuthorityPage.htm";
                    }
                } else {
                    layer.alert(data.message);
                }
            }

        });
    });

    //充值
    $(".indexConFundSpan").click(function(){
       //1.判断是否有充值的权限

       //2、判断是否公司有签约了
        var  menu ="我的账户";
        $.ajax({
            url: BASE_PATH + '/newRecharge.htm',
            type: 'post',
            dataType: 'json',
            data: {'menu': menu},
            success: function (data) {
                if (data.success) {
                   var  map = data.data;
                   var  flag = map.flag;
                   var hasProto = map.hasProto;
                    if (flag) {
                        if(hasProto){
                            //判断是否已签约
                            var a = BASE_PATH + "/recharge.htm";
                            var index = layer.open({
                                type: 2,
                                title: false,
                                closeBtn: 0,
                                maxmin: false,
                                shadeClose: false, //点击遮罩关闭层
                                area: ['650px', '380px'],
                                content:a
                            });
                            //窗口最大化
                            layer.full(index);
                        }else{
                            //弹出框提示
                            showInfo("签署服务协议后才能进行充值操作，如果已经签署请联系薪太软客服","未签约不能充值")
                        }
                    } else {
                        //无权限
                        showInfo("您当前无此操作权限，请联系管理员","无权限禁止操作");
                    }
                } else {

                     showInfo("","操作失败")
                }
            }

        });

    });

});