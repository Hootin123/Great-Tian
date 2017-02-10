$(function(){
	
// ..............验证
	var fcopname = false,fname = false,fphone = false,fpwd=false,frpwd=false,fregphone=false,fregpwd=false;
	//......... 验证公司名称
	$('.companyName input').blur(function(){
		testCompanyName()
	})
	function testCompanyName(){
		if($(".companyName input").val()=='' || $(".companyName input").val()==undefined ){
			$('.companyName b').html('请输入4-20位的公司名称');
			fcopname = false
		}
		else if($(".companyName input").val().length>=4&& $(".companyName input").val().length<=20){
			$('.companyName b').html('');
			fcopname=true;
		}
		else{
			$('.companyName b').html('请输入正确的公司名称');
			fcopname = false
		}
	}
//......... 姓名
	$('.userName input').blur(function(){
		testUsername()
	})
	function testUsername(){
		var reg=/^[\w\u4e00-\u9fa5]{2,20}$/
		if($(".userName input").val()=='' || $(".userName input").val()==undefined ){
			$('.userName b').html('请输入姓名');
			fname = false
		}
		else if(reg.test($(".userName input").val())){
			$('.userName b').html('');
			fname=true;
		}
		else{
			$('.userName b').html('请输入正确的姓名');
			fname = false
		}
	}

	//......... 验证手机号

	$('.phone input').blur(function(){
		testPhone()
	});

	function testPhone(){
		var  reg=/^1[3|5|7|8]\d{9}$/;
		if($(".phone input").val()=='' || $(".phone input").val()==undefined ){
			$('.phone b').html('请输入手机号码');
			fphone = false
		}
	 	else if(reg.test($(".phone input").val())){
	 		$('.phone b').html('');
			fphone = true;
		}
		else{
			$('.phone b').html('请输入正确的手机号');
			fphone = false

		}
	};



	//...........验证密码
	$(".pwd input").blur(function(){
		testPassword();
	});
	function testPassword(){
		var reg=/^[A-Za-z0-9]{6,20}$/;
		if($(".pwd input").val()=='' || $(".pwd input").val()==undefined){
			$('.pwd b').html('请输入6-20位字符组合的密码');
			fpwd=false
		
		}
		else if(reg.test($(".pwd input").val())){
			$('.pwd b').html('');
			fpwd = true;
			 
		}else{
			$('.pwd b').html('密码只能为6-20位字符组合');
			fpwd=false

		}
	};


//...........验证确认密码
	$(".rePwd input").blur(function(){
		testRepwd();

	});
	function testRepwd(){
		var reg=/^[A-Za-z0-9]{6,20}$/;
		if($(".rePwd input").val()=='' || $(".rePwd input").val()==undefined){
			$('.rePwd b').html('请再次输入密码');
			frpwd=false;
		}
		else if($(".rePwd input").val()==$(".rePwd input").val()){
			$('.rePwd b').html('');
			frpwd = true;
			 
		}else{
			$('.rePwd b').html('两次密码不一致');
			frpwd=false;
		}
	};

	

	//..........手机验证码
	var timer;
	var getCode=true;
		$('.phoneCode span').on('click',function(){
			testPhone();
			//判断手机号是否已存在
			if(getCode&&fphone){
				if(fphone&&getCode){
					$.ajax({
						type: 'post',
						url: BASE_PATH+'/newHongbao/judgePhoneNumber.htm',
						data: {'phoneNumber':$("input[name='phoneNumber']").val()},
						dataType:'json',
						async:false,
						success: function (data) {
							console.log(data);
							if (data.success) {
								$("#registertError").text("");
								$("#registerError").css("display","none");
								getCode=false;
								console.log(getCode);
								var num=60;
								$('.phoneCode span').html(num+'s');
								timer=setInterval(function(){
									num--;
									$('.phoneCode span').html(num+'s');
									if(num==0){
										clearInterval(timer);
										$('.phoneCode span').html('获取验证码');
										getCode=true;
									}
								},1000);
							} else {
								$(".hint").text(data.message);
								$(".hint").css("display","block");
								fphone=true;
							}
						}
					});
				}

			}
		});
	
	

	//注册
	$(".goRegister").on('click',function(){
		    testUsername();
			testPhone();
			testPassword();
			testRepwd();
			testCompanyName();
		if(fname&&fcopname&&fpwd&&frpwd&&fphone){
			var mmtoken = $('#mmtoken').val();
			var companyContactPlace = $('#companyContactPlace').val();
			$.ajax({
				url:BASE_PATH+'/newHongbao/activityRegister.htm?mmtoken='+mmtoken+'&companyContactPlace='+companyContactPlace,
				type:'post',
				dataType:'json',
				data:$("#newRegisterForm").serialize(),
				success:function(data) {
					if (data.success) {
						$("#registerError").text("");
						$("#registerError").hide();
						layer.alert("注册成功");
						//跳转至认证页面
						window.location = BASE_PATH + "/newHongbao/toActivityApprove.htm"
					} else {
						$("#registerError").text(data.message);
						$("#registerError").show();
					}
				},
				error:function(){
					layer.alert("网络错误");
				}
			});
		}
	})


	// 切换
		$('.register').on('click',function(){

			$(this).addClass('active');
			$('.login').removeClass('active')
			//清空表单
			$("input[name='companyName']").val("");
			$("input[name='userName']").val("");
			$("input[name='password']").val("");
			$("input[name='rePassword']").val("");
			$("input[name='phoneNumber']").val("");
			$("input[name='phoneCode']").val("");

			$('.register-main').show();
			$('.login-main').hide();

		})
		$('.login').on('click',function(){

			$(this).addClass('active');
			$('.register').removeClass('active')
			//清空表单
			$("input[name='loginPhoneNumber']").val("");
			$("input[name='loginPassword']").val("");
			$("input[name='code']").val("");
			$('.login-main').show();
			$('.register-main').hide();
		})



	//登录页面验证
	// 验证手机号
	$('.loginPhone input').blur(function(){
		testRegPhone()
	});

	function testRegPhone(){
		var  reg=/^1[3|5|7|8]\d{9}$/;
		if($(".loginPhone input").val()=='' || $(".loginPhone input").val()==undefined ){
			$('.loginPhone b').html('请输入手机号码');
			fregphone=false
		}
	 	else if(reg.test($(".loginPhone input").val())){
	 		$('.loginPhone b').html('');
			fregphone = true;
		}
		else{
			$('.loginPhone b').html('请输入正确的手机号');
			fregphone=false
		}
	};



	//...........验证密码
	$(".loginPwd input").blur(function(){
		testRegPwd();

	});
	function testRegPwd(){
		var reg=/^[A-Za-z0-9]{6,20}$/;
		if($(".loginPwd input").val()=='' || $(".loginPwd input").val()==undefined){
			$('.loginPwd b').html('请输入6-20位字符组合的密码');
			fregpwd=false
		}
		else if(reg.test($(".loginPwd input").val())){
			$('.loginPwd b').html('');
			fregpwd = true;
			 
		}else{
			$('.loginPwd b').html('密码只能为6-20位字符组合');
			fregpwd=false
		}
	};
	//确定
	$('.goLogin').on('click',function(){
		testRegPhone()
		testRegPwd();
		if(fregphone&&fregpwd){
			$.ajax({
				url:BASE_PATH+'/newHongbao/activityLogin.htm',
				type:'post',
				dataType:'json',
				data:$("#loginForm").serialize(),
				success:function(data){
                   if(data.success){
					   $("#loginError").hide();
					   var returnStr = data.data.returnStr;
                       var companyId = data.data.companyId;
					   if(returnStr=="perfectInfo"){
						   //跳转至完善信息页面
						   window.location=BASE_PATH+"/newHongbao/toActivityApprove.htm";
					   }
					  else if(returnStr=="collectInfo"){
						   //跳转至信息收集页面
						   window.location =BASE_PATH+"/newHongbao/infoCollect.htm?companyId="+companyId;
					   }
					   else if(returnStr==0){
						   //待审核
						   window.location=BASE_PATH+"/newHongbao/toPromptWait.htm";
					   }else if(returnStr==1){
                           //审核未通过
						   var reason = data.data.reason;
						   window.location=BASE_PATH+"/newHongbao/toPromptDefeat.htm?reason="+reason+"&companyId="+companyId;
					   }else if(returnStr==2){
						   //审核通过
						   window.location=BASE_PATH+"/newHongbao/toPromptSuccess.htm";
					   }

				   }else{
                      $("#loginError").text(data.message);
					  $("#loginError").show();
				   }
				},
				error:function(){
					layer.alert("网络错误");
				}

			});
		}
	})
    //刷新验证码
	$("#code").click(function(){
		var path = BASE_PATH+"/newHongbao/getImgCode.htm?time="+new Date().getTime();
		$("#code").attr("src",path);
	});




})

