$(function(){
	var frpwd = false,fphone = false,fpwd = false;


	//......... 验证手机号

	$('#phone input').blur(function(){
		testPhone()
	});

	function testPhone(){
		var  reg=/^1[3|5|7|8]\d{9}$/;
		if($("#phone input").val()=='' || $("#phone input").val()==undefined ){
			$('#phone b').html('请输入手机号码').css('right','2.3rem');
			$('#phone em').removeClass('show1')
		}
	 	else if(reg.test($("#phone input").val())){
	 		$('#phone b').html('');
			$('#phone em').addClass('show1')
			fphone = true;
		}
		else{
			$('#phone b').html('请输入正确的手机号').css('right','2.3rem');
			$('#phone em').removeClass('show1')
		}
	};



	

	//........验证密码
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
	$("#repwd input").blur(function(){
		testRepwd();

	});
	function testRepwd(){
		var reg=/^[A-Za-z0-9]{6,20}$/;
		if($("#repwd input").val()=='' || $("#repwd input").val()==undefined){
			$('#repwd b').html('请再次输入密码');
			$('#repwd em').removeClass('show')
		}
		else if($("#repwd input").val()==$("#password input").val()){
			$('#repwd b').html('');
			$("#repwd em").addClass('show')
			frpwd = true;
			 
		}else{
			$('#repwd b').html('两次密码不一致');
			$('#repwd em').removeClass('show')
		}
	};

	//..........手机验证码
	var timer;
	var getCode=true;
		$('#phone p').on('click',function(){
			testPhone();
			if(getCode&&fphone){
				$.ajax({
					type: 'post',
					url: BASE_PATH+'/h5Hongbao/sendMsgByHongbao.htm',
					data: {'memberPhone':$('#memberPhone').val()},
					dataType:'json',
					success: function (data) {
						if (data.success) {
							$(".hint").text("");
							$(".hint").css("display","none");
							fphonecode=true;
							getCode=false;
							console.log(getCode);
							var num=60;
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
	
	

	//确认修改密码
	$(".affirm").on('click',function(){

			testPhone();
			testPassword();
			testRepwd();
		if(fphone&frpwd&fpwd){
			$.ajax({
				url:BASE_PATH+'/h5Hongbao/hongbaoResetPasswor.htm',
				data:$("#resetForm").serialize(),
				dataType:'json',
				type:'post',
				success:function(data){
					if(data.success){
						//直接跳到目录页面
						$(".hint").text("");
						$(".hint").css("display","none");
						window.location=BASE_PATH+'/h5Hongbao/toMenuPage.htm';
					}else{
						$(".hint").text(data.message);
						$(".hint").css("display","block");
					}
				}
			});
			
			

		}
	});
	//...........返回登录
	$('.returnLogin').on('click',function(){
		window.location =BASE_PATH+"/h5Hongbao/returnLogin.htm";

	})
})

	