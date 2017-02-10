$(function(){

	//初始化
	var registerFrom = $("#registerFrom").val();
	if(registerFrom!=null&&registerFrom!=""&&registerFrom!=undefined){
		$(".blind-success").show();
		$(".audit").hide();
		$(".unapprove").hide();
		$('.approve').hide();

	}

//判断完善信息审核状态 及 红包领取等情况
	var auditValue = $("#auditStatus").val();
	if(auditValue==0){
		//待审核
		$(".audit").show();
		$(".blind-success").hide();
		$(".unapprove").hide();
		$('.approve').hide();

	}else if(auditValue==1){
		//审核驳回
		$(".unapprove").show();
		$(".blind-success").hide();
		$('.approve').hide();
		$(".audit").hide();

	}else if(auditValue==2){
		//审核通过
		$('.approve').show();
		$(".blind-success").hide();
		$(".audit").hide();
		$(".unapprove").hide();
	}else{
		$(".audit").hide();
		$(".unapprove").hide();
		$('.approve').hide();
		$(".blind-success").show();
	}
	//去免费指南
	$('header').on('click',function(){
		var path = BASE_PATH +'/h5Hongbao/toFreeGuidePage.htm';
		window.location=path;
	});

	// 去注册
	$('#packet-reg').on('click',function(){
		window.location.href=BASE_PATH+"/h5Hongbao/toHongbaoRegisterPage.htm";
	})

	// 去完善信息
	$('#packet-info').on('click',function(){
		//判断是否已完善信息
		$.ajax({
			url:BASE_PATH+'/h5Hongbao/hasPerfectInfo.htm',
			type:'post',
			dataType:'json',
			success:function(data){
				if(data.success){
                   layer.alert(data.message);
				}else{
					window.location=BASE_PATH+"/h5Hongbao/toPerfectInformationPage.htm";
				}
			}
		});

	})


	// 领5元红包
	var flg = true;
	if(flg){
	$('.packet-five span').on('click',function(){
		flg =false;
		var activityReceive ="1";//红包为5元的类型
		//领红包接口

				$.ajax({
					url: BASE_PATH + '/h5Hongbao/receiveHongbao.htm',
					type: 'post',
					data: {'activityReceive': activityReceive},
					dataType: 'json',
					success: function (data) {
						if (data.success) {
							//从公众号那里点击领奖，跳转至登录页面
							if (data.msgCode == "returnLogin") {
								window.location = BASE_PATH + "/h5Hongbao/returnLogin.htm";
							} else if (data.msgCode == "no-wechat") {
								$(".audit").hide();
								$(".unapprove").hide();
								$('.approve').hide();
								$(".blind-success").hide();
								//非微信端
								$("#first-one").html("请扫码");
								$("#first-two").html("请在薪太软公众号下点击“领取红包”");
								$(".unbound").show();
							} else if (data.msgCode == "no-subscribe") {
								$(".audit").hide();
								$(".unapprove").hide();
								$('.approve').hide();
								$(".blind-success").hide();
								$("#first-one").html("未关注");
								$("#first-two").html("领取红包，请先关注薪太软公众号.");
								$(".unbound").show();
							} else if (data.msgCode == "send-reds") {
								//隐藏其他的div
								$(".audit").hide();
								$(".unapprove").hide();
								$('.approve').hide();
								$(".blind-success").hide();
								$('.packet-five').css({'background': 'url(../resource/img/hongbao/packet-after.png) center 0 no-repeat','background-size': '1.62rem 2.52rem'});
								$("#five").css({'background': '#bbb2b4', 'color': '#fff'}).html('已领取');
								//发红包
								if (data.data == "1") {
									$("#picketRedInfo").html("5")
								} else {
									$("#picketRedInfo").html("20");
								}
								$(".getPacket").show();
							}
                            flag=true;
						} else {
							layer.alert(data.message);
                            flag=true;
						}

					},
					error: function () {
						layer.alert("网络错误");
                        flg=true;
					}


				});



	});
	}

	// 领20元红包
	var flg1 = true;
	if(flg1){
	$('.packet-twenty span').on('click',function(){
		flg1 =false;
		var activityReceive ="2";//红包为5元的类型
		var flag = false;
		//领红包接口
		$.ajax({
			url:BASE_PATH+'/h5Hongbao/receiveHongbao.htm',
			type:'post',
			data:{'activityReceive':activityReceive},
			dataType:'json',
			success:function(data){
				if(data.success){
					//从公众号那里点击领奖，跳转至登录页面
					if(data.msgCode=="returnLogin"){
						flg1=true;
						window.location =BASE_PATH+"/h5Hongbao/returnLogin.htm";
					}else if(data.msgCode=="no-wechat"){

						$(".audit").hide();
						$(".unapprove").hide();
						$('.approve').hide();
						$(".blind-success").hide();
						//非微信端
						$("#first-one").html("请扫码");
						$("#first-two").html("请在薪太软公众号下点击“领取红包”");
						$(".unbound").show();
						flg1=true;
					}else if(data.msgCode=="no-subscribe"){
						$(".audit").hide();
						$(".unapprove").hide();
						$('.approve').hide();
						$(".blind-success").hide();
						$("#first-one").html("未关注");
						$("#first-two").html("领取红包，请先关注薪太软公众号.");
						$(".unbound").show();
						flg1=true;
					}else if(data.msgCode=="send-reds"){
						//隐藏其他的div
						$(".audit").hide();
						$(".unapprove").hide();
						$('.approve').hide();
						$(".blind-success").hide();
						$('.packet-five').css({'background':'url(../resource/img/hongbao/packet-after.png) center 0 no-repeat','background-size':'1.62rem 2.52rem'});
						$("#twenty").css({'background':'#bbb2b4','color':'#fff'}).html('已领取')
						//发红包
						if(data.data=="1") {
							$("#picketRedInfo").html("5")
						}else{
							$("#picketRedInfo").html("20");
						}
						$(".getPacket").show();
						flg1=true;
					}
				}else{
					layer.alert(data.message);
					flg1=true;
				}

			},
			error:function(){
				layer.alert("网络错误");
				flg1=true;
			}


		});

	})

	}


})