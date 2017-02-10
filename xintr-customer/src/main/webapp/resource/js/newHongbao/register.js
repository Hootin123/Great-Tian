$(function(){
	var fcompname = false,fphone = false,fpwd = false,fname=false,frpwd = false;

	//......... 验证公司名称
	$('#companyname input').blur(function(){
		testCompanyname()
	})
	function testCompanyname(){
		if($("#companyname input").val()=='' || $("#companyname input").val()==undefined ){
			$('#companyname b').html('请输入4-20位的公司名称');
			fcompname = false
		}
		else if($("#companyname input").val().length>=4 && $("#companyname input").val().length<=20){
			$('#companyname b').html('');
			fcompname=true;
		}
		else{
			$('#companyname b').html('请输入正确的公司名称');
			fcompname = false
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
			fname = false
		}
		else if(reg.test($("#username input").val())){
			$('#userName b').html('');
			fname=true;
		}
		else{
			$('#userName b').html('请输入正确的姓名');
			fname = false
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
			fphone = false
		}
	 	else if(reg.test($("#phone input").val())){
	 		$('#phone b').html('');
			fphone = true;
		}
		else{
			$('#phone b').html('请输入正确的手机号');
			fphone = false
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
			fpwd = false
		}
		else if(reg.test($("#password input").val())){
			$('#password b').html('');
			fpwd = true;
			 
		}else{
			$('#password b').html('密码只能为6-20位字符组合');
			fpwd = false
		}
	};


	//...........验证重复密码
	$("#repassword input").blur(function(){
		testRePassword();

	});
	function testRePassword(){
		if($("#repassword input").val()=='' || $("#repassword input").val()==undefined){
			$('#repassword b').html('请再次密码');
			frpwd = false;
		}
		else if($("#password input").val()==$("#repassword input").val()){
			$('#repassword b').html('');
			frpwd = true;
			 
		}else{
			$('#repassword b').html('两次输入的密码不一致');
			frpwd = false;
		}
	};


	


	//..........手机验证码
	var timer;
	var getCode=true;
	$('#phonecode span').on('click',function(){
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
						//console.log(data);
						if (data.success) {
							$("#registertError").text("");
							$("#registerError").css("display","none");
							getCode=false;
							//console.log(getCode);
							var num=60;
							$('#phonecode span').css({'margin-left':'0.8rem','color':'#444'}).html(num+'s');
							timer=setInterval(function(){
								num--;
								$('#phonecode span').css({'margin-left':'0.8rem','color':'#444'}).html(num+'s');
								if(num==0){
									clearInterval(timer);
									$('#phonecode span').css({'margin-left':'0.26rem','color':'#0696fa'}).html('获取验证码');
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
	$(".reg").on('click',function(){
		  testCompanyname()
		    testUsername();
			testPhone();
			testPassword();
			testRePassword();
		if(fcompname&&fname&&fphone&&fpwd&&frpwd){
			$.ajax({
				url:BASE_PATH+'/newHongbao/activityRegister.htm',
				data:$("#registerForm").serialize(),
				dataType:'json',
				type:'post',
				success:function(data) {
                  if(data.success){
					  layer.alert("注册成功");
					  window.location =BASE_PATH+"/newHongbao/toRegisterSuccess.htm"
				  }else{
					  layer.alert(data.message)
				  }
				}
			});
		}
	})
	// //...........去登录
	// $('.login').on('click',function(){
	//
	// })
})

	