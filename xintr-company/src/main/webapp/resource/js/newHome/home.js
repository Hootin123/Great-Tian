    $(function() {


        onWinResize();
        window.onresize = function() {
            onWinResize();
        }
        function onWinResize() {
            var height = $(document).height()-40-60-60;
            var width = $(document).width();
            $('.content').css('height', height);
            $('.contentLeftCon').css('height', height);
            $('.contentRight').css('width',width-180);
            $('.right').css('width',width-180);
            $('#main').css('width',180);
        }


        
        $('.contentLi').each(function () {
            $(this).siblings('.contentLi').click(function () {
                $('.contentLi').next().hide();
            })
        })
        function hide(){
            $('.divLi').hide();
        }

        
        $('.contentLi').each(function () {
            $(this).click(function () {
                $(this).next().toggle();
            })
        })

        //退出
        $("#login-out").click(function(){
            window.location.href=BASE_PATH+"/logout.htm";
        })

        //公司设置
        $("#company_setting").click(function(){

        var a =BASE_PATH+"/payforInfo.htm?type=info";
             $('#J_iframe').attr('src', a);
        });


      // 去除冒泡
        $(document).on('click',function(){
            $('.newsBox').hide();

        })
        $('.newsBox').on('click',function(e){
            e.stopPropagation()
        })

        //消息盒子 start
        $(".topNews").click(function(e){
            e.stopPropagation()
             //消息盒子列表显示
            $(".newsBox").toggle();
            $(".newsDetails").css("display","none");
        });


        $('.newsTop').find('li').eq(0).css('background','#3a6587');
        $('.newsTop').find('li').eq(0).click(function () {
            $(this).css('background','#3a6587');
            $(this).siblings('li').css('background','none')
            $(".newsDetails").hide();
            $('.newsNot').show();
            $('.newsEnd').hide();

        })
        //查询所有的已读消息
        $('.newsTop').find('li').eq(1).click(function () {
            $(this).css('background','#3a6587');
            $(this).siblings('li').css('background','none')
            $(".newsDetails").hide();
            $(".newsEnd ul").empty();
            // //动态加载
            var  bath = BASE_PATH + '/messageBox/seeAllReadMsgs.htm';
            $.ajax({
                url: bath,
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        var list = data.data;
                        var html = "";
                        $.each(list, function (i, msg) {
                            html += "<li  onclick='showDetail(this);' li-id="+msg.msgId+">";
                            html += "<p class='newsEndText'>" + msg.msgTitle + "</p>";
                            html += "<p class='newsEndTime'>" + timeDateTostr(msg.msgAddtime) + "</p>";
                            html += "<img class='newsEndImg'  onclick=deleteMsg(this);   src='/resource/img/newHome/2.2newsShut.png' alt=''/>";
                            html += "</li>";
                        });
                      $(".newsEnd ul").append(html);
                    }
                }
            });
            $('.newsNot').hide();
            $('.newsEnd').show();
        })
       //消息盒子 end

    });
    //将date时间转成string类型
    function timeDateTostr(da){
        if(da!=null&&da!=""&&da!=undefined){
            var date = new Date();
            date.setTime(da);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            m = m < 10 ? ('0' + m) : m;
            var d = date.getDay();
            //console.log(y+m);
            return y+"-"+m+"-"+d;
        }else{
            return " ";
        }
    }

    //逻辑删除消息
    function deleteMsg(obj){
        var msgId =$(obj).parent().attr("li-id");
        $.ajax({
            url:BASE_PATH+'/messageBox/deleteMessage.htm',
            data:{'msgId':msgId},
            dataType:'json',
            type:'post',
            success:function(data){
                 if(data.success){
                     //移除子元素
                     $(obj).parent().remove();
                     alert("删除成功")
                 }else{
                     showInfo(data.message,"");
                 }
            },
            error:function(){
                showInfo("网络错误，请刷新页面","");
            }

        });

    }

    //查看详情
  function showDetail(obj){
      var msgId = $(obj).attr("li-id");
      if(msgId!=null &&msgId!="" && msgId!=undefined){
          $.ajax({
            url:BASE_PATH+'/messageBox/findMessageDetail.htm',
            data:{"msgId":msgId},
            dataType:'json',
            type:'post',
            success:function(data){
               if(data.success){
                var  title= data.data.msgTitle;
                var context=data.data.msgCont;
                //赋值
                $(".newsDetailsTitle").text(title);
                $(".newsDetailsCon").text(context);
                $(".newsDetails").css("display","block");
               }else{
                   showInfo("查询失败")
               }
            },
            error:function(){
                showInfo("网络错误，请刷新页面");
            }
          });
      }
  }

  //关闭详情
  function closeDetail(){
     $(".newsDetails").css("display","none");
  }



    //重置密码
    $(function(){
        $("#modifyPwdBtn").click(function(){
            //初始化
            $("[name=memberId]").val("");
            $(".aselstfirst [name='memberLogname']").text("我的账户：");
            $(".aselstfirst [name='companyName']").text("公司名称：");
            $("[name=memberPassword]").val("");
            $("[name=newPwd]").val("");
            $("[name=confirmNewPwd]").val("");
            $.ajax({
                type: 'post',
                url: 'getModifyPwdInfo.htm',
//            data: $("#protocolModifyForm").serialize(),
                async:false,
                success: function (data) {
                    console.log(data);
                    if (data.success) {
                        var companyMemberBean=data.data;
                        if(companyMemberBean!=null&& companyMemberBean!=undefined){
                            $("[name=memberId]").val(companyMemberBean.memberId);
                            $(".aselstfirst [name='memberLogname']").text("我的账户："+companyMemberBean.memberLogname);
                            $(".aselstfirst [name='companyName']").text("公司名称："+(companyMemberBean.companyName==null||companyMemberBean.companyName==undefined?"":companyMemberBean.companyName));
                        }
                        $('.indexshade').css('display','block');
                        $('.accouparent').show(200);
                    } else {
                        alert("用户登录超时,请重新登录");
                        location.href = "/";
                    }
                }
            });

        });
        $("#aseclose").click(function(){
            $('.indexshade').css('display','none');
            $('.accouparent').hide(200);
        });
        $("#cacelBtn").click(function(){
            $('.indexshade').css('display','none');
            $('.accouparent').hide(200);
        });
        $("#sureBtn").click(function(){
            if(validatorFirstData()) {
                var formDataStr = {
                    'memberPassword': $('[name=memberPassword]').val(),
                    'memberId': $('[name=memberId]').val(),
                    'newPwd': $('[name=newPwd]').val(),
                    'confirmNewPwd': $('[name=confirmNewPwd]').val()
                };
                $.ajax({
                    type: 'post',
                    url: "modifyPwdOperation.htm",
                    data: JSON.stringify(formDataStr),
                    async: false,
                    dataType: "json",
                    contentType: 'application/json',
                    success: function (data) {
                        if (data.success) {
                            $("#verifycodeTip").text("");
                            $('.pacity').css('display','block');
                            $('.aboxbg').show(200);
                            $('.indexshade').css('display', 'none');
                            $('.accouparent').hide(200);
                        } else {
                            $("#verifycodeTip").text(data.message);
                        }
                    }
                });
            }
        });

        $("[name=memberPassword]").blur(function(){
            validatorFirstData();
        });

        $("[name=newPwd]").blur(function(){
            validatorFirstData();
        });

        $("[name=confirmNewPwd]").blur(function(){
            validatorFirstData();
        });

        $("#sureBtnThird").click(function(){
            $('.pacity').css('display','none');
            $('.aboxbg').css('display','none');
        });
    });



