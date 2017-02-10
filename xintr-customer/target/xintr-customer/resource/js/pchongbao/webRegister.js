$(function(){
	
// ..............验证


	var fcopname = false,fname = false,fphone = false,fpwd=false,frpwd=false,fprot=false,fcode=false;

	//......... 验证公司名称
	$('.companyName input').blur(function(){
		testCompanyName()
	})
	function testCompanyName(){

		if($(".companyName input").val()=='' || $(".companyName input").val()==undefined ){
			$('.companyName b').addClass('error').html('请输入4-20位的公司名称').show();
            setTimeout(function(){
                    $('.companyName b').fadeOut(500)
                },1000);
			fcopname = false
		}
		else if($(".companyName input").val().length>=4&& $(".companyName input").val().length<=20){
			$('.companyName b').removeClass('error').html('').hide();
			fcopname=true;
		}
		else{
			$('.companyName b').addClass('error').html('请输入正确的公司名称');
            setTimeout(function(){
                    $('.companyName b').fadeOut(500)
                },1000);
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
			$('.userName b').addClass('error').html('请输入姓名').show();
			 setTimeout(function(){
                    $('.userName b').fadeOut(500)
                },1000);
			fname = false
		}
		else if(reg.test($(".userName input").val())){
			$('.userName b').removeClass('error').html('').hide();
			fname=true;
		}
		else{
			$('.userName b').addClass('error').html('请输入正确的姓名').show();
			 setTimeout(function(){
                    $('.userName b').fadeOut(500)
                },1000);
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
			$('.phone b').addClass('error').html('请输入手机号码').show();
			 setTimeout(function(){
                    $('.phone b').fadeOut(500)
                },1000);
			fphone = false
		}
	 	else if(reg.test($(".phone input").val())){
	 		$('.phone b').removeClass('error').html('').hide();
			fphone = true;
		}
		else{
			$('.phone b').addClass('error').html('请输入正确的手机号').show();
			 setTimeout(function(){
                    $('.phone b').fadeOut(500)
                },1000);

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
			$('.pwd b').addClass('error').html('请输入6-20位字符组合的密码').show();
			 setTimeout(function(){
                    $('.pwd b').fadeOut(500)
                },1000);
			fpwd=false
		
		}
		else if(reg.test($(".pwd input").val())){
			$('.pwd b').removeClass('error').html('').hide();
			fpwd = true;
			 
		}else{
			$('.pwd b').addClass('error').html('密码只能为6-20位字符组合').show();
			 setTimeout(function(){
                    $('.pwd b').fadeOut(500)
                },1000);
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
			$('.rePwd b').addClass('error').html('请再次输入密码').show();
			setTimeout(function(){
                    $('.rePwd b').fadeOut(500)
                },1000);
			frpwd=false;
		}
		else if($(".rePwd input").val()==$(".rePwd input").val()){
			$('.rePwd b').removeClass('error').html('').hide();
			frpwd = true;
			 
		}else{
			$('.rePwd b').addClass('error').html('两次密码不一致').show();
			setTimeout(function(){
                    $('.rePwd b').fadeOut(500)
                },1000);
			frpwd=false;
		}
	};
	// 验证协议
	function testProtocol(){
		if($('.protocol input').is(':checked')){
			$('.protocol b').removeClass('error').html('').hide();
			fprot=true;
		}else{
			$('.protocol b').addClass('error').html('请同意协议').show();
			setTimeout(function(){
                    $('.protocol b').fadeOut(500)
                },1000);
			fprot=false;
		}
	}
	// 验证短信验证码
	function testPhoneCode(){
		if($('.phoneCode input').val()=='' || $('.phoneCode input').val()==undefined){
			$('.phoneCode b').addClass('error').html('请输入短信验证码').show();
			setTimeout(function(){
                    $('.phoneCode b').fadeOut(500)
                },1000);
			fcode=false;
		}else{
			$('.phoneCode b').removeClass('error').html('').hide();
			fcode=true;
		}
	}

	//..........手机验证码
	var timer;
	var getCode=true;
		$('.phone p').on('click',function(){
			testPhone();
			if(getCode&&fphone){

				$.ajax({
					type: 'post',
					url: BASE_PATH+'/web/judgePhoneNumber.htm',
					data: {'phoneNumber':$("input[name='phoneNumber']").val()},
					dataType:'json',
					async:false,
					success: function (data) {
						if (data.success) {
							getCode=false;
							console.log(getCode)
							var num=60;
							$('.phone p').html(num+'s 后重新发送');
							timer=setInterval(function(){
								num--;
								$('.phone p').html(num+'s 后重新发送');
								if(num==0){
									clearInterval(timer)
									$('.phone p').html('重新获取验证码');
									getCode=true;					}
							},1000)
						} else {
							alert(data.message)
							getCode=true;
						}
					}
				});


			}
		});



	//注册
	$(".register").on('click',function(){
		    testUsername();
			testPhone();
			testPassword();
			testRepwd();
			testCompanyName();
			testProtocol();
			testPhoneCode();
		if(fname&&fcopname&&fpwd&&frpwd&&fphone&&fprot&&fcode){
			  //注册
			  $.ajax({
				 url:BASE_PATH+'/web/webRegister.htm',
				 data:$("#registerForm").serialize(),
				 dataType:'json',
				 type:'post',
				 success:function(data){
                   if(data.success){
					   //跳转至公司信息填写页面
					   var companyId = data.data.companyId;
					   // alert(companyId);
					   window.location=BASE_PATH+"/web/jumpToCompanyInfoPage.htm?companyId="+companyId;
				   }else{
					   alert(data.message);//提示错误信息
				   }
				 },
				 error:function(){
					 alert("网络错误，请刷新页面")
				 }




			  });
		}
	})

	// 去登录
	$('.login').on('click',function(){
		window.location=BASE_PATH+"/web/jumpToLoginPage.htm";
	})



})

