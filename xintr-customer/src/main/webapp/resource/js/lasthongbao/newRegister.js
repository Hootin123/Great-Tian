$(function(){

	//清空表单
	$("input[name='companyName']").val("");
	$("input[name='password']").val("");

// ..............验证


	var fcopname = false,fphone = false,fpwd=false,fregphone=false;

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


	//..........手机验证码
	var timer;
	var getCode=true;
	$('.phoneCode span').on('click',function(){
		testPhone();
		if(getCode&&fphone){

			$.ajax({
				type: 'post',
				url: BASE_PATH+'/lastHongbao/judgePhoneNumber.htm',
				data: {'phoneNumber':$("input[name='phoneNumber']").val()},
				dataType:'json',
				async:false,
				success: function (data) {
					if (data.success) {
						getCode=false;
						console.log(getCode)
						var num=60;
						$('.phoneCode span').html( '重新发送('+num+')').css({'background':'#ccc','color':'#666'});
						timer=setInterval(function(){
							num--;
							$('.phoneCode span').html( '重新发送('+num+')').css({'background':'#ccc','color':'#666'});
							if(num==0){
								clearInterval(timer)
								$('.phoneCode span').html('重新获取验证码').css({'background':'#f04848','color':'#fff'});
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
	$(".register").on('touchstart',function(){
			testPhone();
			testPassword();
			testCompanyName();
		if(fcopname&&fpwd&&fphone){
			$.ajax({
				url:BASE_PATH+'/lastHongbao/webRegister.htm',
				data:$("#registerForm").serialize(),
				dataType:'json',
				type:'post',
				success:function(data){
					if(data.success){
						//跳转至公司信息填写页面
						$(".hint").text("").hide();
						var companyId = data.data.companyId;
						// alert(companyId);
						window.location=BASE_PATH+"/lastHongbao/jumpToCompanyInfoPage.htm?companyId="+companyId;
					}else{
						$(".hint").text(data.message).show();
					}
				},
				error:function(){
					alert("网络错误，请刷新页面")
				}

			});
		}
	});
	// 跳转到登录页
	$('.goRegister').on('touchstart',function(){
		window.location.href=BASE_PATH+"/lastHongbao/jumpToLoginPage.htm";
	})

	

})

