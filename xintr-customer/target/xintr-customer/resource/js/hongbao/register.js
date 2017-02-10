$(function(){

	//判断返回类型
	var returnLogin = $("#returnLogin").val();
	if(returnLogin!=null&&returnLogin!=""&&returnLogin!=undefined){
		//返回登录跳转来的
		$('.mengban').show();
		$('.login-popup').show();

	}

	var fcopname = false,fname = false,fphone = false,fpwd=false,frpwd=false,fregphone=false,fregpwd=false;

	//......... 验证公司名称
	$('#companyname input').blur(function(){
		testCompanyname()
	})
	function testCompanyname(){
		//var reg=/^[\w\u4e00-\u9fa5]{4,20}$/
		if($("#companyname input").val()=='' || $("#companyname input").val()==undefined ){
			$('#companyname b').html('请输入4-20位的公司名称');
			$('#companyname em').removeClass('show')
		}
		else if(($("#companyname input").val()).length>=4 &&($("#companyname input").val()).length<=20){
			$('#companyname b').html('');
			$('#companyname em').addClass('show');
			fcopname=true;
		}
		else{
			$('#companyname b').html('请输入正确的公司名称');
			$('#companyname em').removeClass('show')
		}
	}
//......... 姓名
	$('#username input').blur(function(){
		testUsername()
	})
	function testUsername(){
		var reg=/^[\w\u4e00-\u9fa5]{2,20}$/
		if($("#username input").val()=='' || $("#username input").val()==undefined ){
			$('#username b').html('请输入姓名');
			$('#username em').removeClass('show')
		}
		else if(reg.test($("#username input").val())){
			$('#username b').html('');
			$('#username em').addClass('show');
			fname=true;
		}
		else{
			$('#username b').html('请输入正确的姓名');
			$('#username em').removeClass('show')
		}
	}

	//......... 验证手机号

	$('#phone input').blur(function(){
		testPhone()
	});

	function testPhone(){
		var  reg=/^1[3|5|7|8]\d{9}$/;
		if($("#phone input").val()=='' || $("#phone input").val()==undefined ){
			$('#phone b').html('请输入手机号码');
			$('#phone em').removeClass('show')
		}
	 	else if(reg.test($("#phone input").val())){
	 		$('#phone b').html('');
			$('#phone em').addClass('show')
			fphone = true;
		}
		else{
			$('#phone b').html('请输入正确的手机号');
			$('#phone em').removeClass('show')
		}
	};



	//...........验证密码
	$("#password input").blur(function(){
		testPassword();

	});
	function testPassword(){
		var reg=/^[A-Za-z0-9]{6,20}$/;
		if($("#password input").val()=='' || $("#password input").val()==undefined){
			$('#password b').html('请输入6-20位字符组合的密码');
			$('#password em').removeClass('show')
		}
		else if(reg.test($("#password input").val())){
			$('#password b').html('');
			$("#password em").addClass('show')
			fpwd = true;
			 
		}else{
			$('#password b').html('密码只能为6-20位字符组合');
			$('#password em').removeClass('show')
		}
	};


//...........验证确认密码
	$("#repassword input").blur(function(){
		testRepwd();

	});
	function testRepwd(){
		var reg=/^[A-Za-z0-9]{6,20}$/;
		if($("#repassword input").val()=='' || $("#repassword input").val()==undefined){
			$('#repassword b').html('请再次输入密码');
			$('#repassword em').removeClass('show')
		}
		else if($("#repassword input").val()==$("#password input").val()){
			$('#repassword b').html('');
			$("#repassword em").addClass('show')
			frpwd = true;
			 
		}else{
			$('#repassword b').html('两次密码不一致');
			$('#repassword em').removeClass('show')
		}
	};



	//..........手机验证码
	var timer;
	var getCode=true;
		
		$('#phonecode p').on('click',function(){
			testPhone();
			if(getCode&&fphone){

				$.ajax({
					type: 'post',
					url: BASE_PATH+'/h5Hongbao/sendMsgByHongbao.htm',
					data: {'memberPhone':$('#memberPhone').val()},
				    dataType:'json',
					success: function (data) {
						console.log(data);
						if (data.success) {
							$(".hint").text("");
							$(".hint").css("display","none");
							fphonecode=true;
							getCode=false;
							console.log(getCode);
							var num=60;
							$('#phonecode span').html(num+'s');
							timer=setInterval(function(){
								num--;
								$('#phonecode span').html(num+'s');
								if(num==0){
									clearInterval(timer);
									$('#phonecode span').html('获取验证码');
									getCode=true;
								}
							},1000);
						} else {
							$(".hint").text(data.message);
							$(".hint").css("display","block");
							fphonecode=false;
						}
					}
				});

			}
		});
	
	

	//注册
	$(".reg").on('click',function(){
		    testUsername();
			testPhone();
			testPassword();
			testRepwd();
			testCompanyname();
		if(fname&fcopname&fpwd&frpwd&fphone){
			$.ajax({
				url:BASE_PATH+'/h5Hongbao/hongbaoRegister.htm',
				data:$('#registerForm').serialize(),
                dataType:'json',
				type:'post',
				success:function(data){
                   if(data.success){

					   $(".hint").text("");
					   $(".hint").css("display","none");
					   //跳转到目录页面
					   window.location=BASE_PATH+'/h5Hongbao/toMenuPage.htm?registerFrom='+1;
				   }else{
					   //显示错误信息
					   $(".hint").text(data.message);
					   $(".hint").css("display","block");
				   }

				}

			});

		}
	});



	//跳转至登录页面
	$('.login').on('click',function(){

		$('.mengban').show();
		$('.login-popup').show();

	});
	$('.top em').on('click',function(){
		$('.mengban').hide();
		$('.login-popup').hide();

	})
	//返回注册

	$('.reg1').on('click',function(){
		$('.mengban').hide();
		$('.login-popup').hide();
	});

	//登录页面验证
	// 验证手机号
	$('#regPhone input').blur(function(){
		testRegPhone()
	});

	function testRegPhone(){
		var  reg=/^1[3|5|7|8]\d{9}$/;
		if($("#regPhone input").val()=='' || $("#regPhone input").val()==undefined ){
			$('#regPhone b').html('请输入手机号码');
			$('#regPhone em').removeClass('show')
		}
	 	else if(reg.test($("#regPhone input").val())){
	 		$('#regPhone b').html('');
			$('#regPhone em').addClass('show')
			fregphone = true;
		}
		else{
			$('#regPhone b').html('请输入正确的手机号');
			$('#regPhone em').removeClass('show')
		}
	};



	//...........验证密码
	$("#regPwd input").blur(function(){
		testRegPwd();

	});
	function testRegPwd(){
		var reg=/^[A-Za-z0-9]{6,20}$/;
		if($("#regPwd input").val()=='' || $("#regPwd input").val()==undefined){
			$('#regPwd b').html('请输入6-20位字符组合的密码');
			$('#regPwd em').removeClass('show')
		}
		else if(reg.test($("#regPwd input").val())){
			$('#regPwd b').html('');
			$("#regPwd em").addClass('show')
			fregpwd = true;
			 
		}else{
			$('#regPwd b').html('密码只能为6-20位字符组合');
			$('#regPwd em').removeClass('show')
		}
	};
// 忘记密码
	$('.forget').on('click',function(){
		//跳转到忘记密码页面
		window.location=BASE_PATH+"/h5Hongbao/toHongbaoForgertPasswordPage.htm"
	})

	//点击确定登录
	$('.footer').on('click',function(){
		testRegPhone()
		testRegPwd();
		if(fregphone&fregpwd){
			$.ajax({
				url: BASE_PATH+'/h5Hongbao/hongbaoLogin.htm',
				data:$("#loginForm").serialize(),
				dataType:'json',
				type:'post',
				success:function(data){
					if(data.success){
						//跳转到目录页面
						$(".login-error").text("");
						$(".login-error").css("display","none");
						window.location=BASE_PATH+'/h5Hongbao/toMenuPage.htm';
					}else{
						$(".login-error").text(data.message);
						$(".login-error").css("display","block");
					}
				}

			});
			

		}
	});
	


})

	