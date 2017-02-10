$(function(){
	var frpwd = false,fphone = false,fpwd = false;


	//......... 验证手机号

	$('.forgetPhone input').blur(function(){
		testPhone()
	});

	function testPhone(){
		var  reg=/^1[3|5|7|8]\d{9}$/;
		if($(".forgetPhone input").val()=='' || $(".forgetPhone input").val()==undefined ){
			$('.forgetPhone b').html('请输入手机号码').css('right','2.2rem');
			fphone = false;
			
		}
	 	else if(reg.test($(".forgetPhone input").val())){
	 		$('.forgetPhone b').html('');
			fphone = true;
		}
		else{
			$('.forgetPhone b').html('手机号错误').css('right','2.2rem');
			fphone = false;
		}
	};




	//........验证密码
	$(".forgetPwd input").blur(function(){
			testPassword();

		});
		function testPassword(){
			var reg=/^[A-Za-z0-9]{6,20}$/;
			if($(".forgetPwd input").val()=='' || $(".forgetPwd input").val()==undefined){
				$('.forgetPwd b').html('请输入6-20位字符组合的密码');
				fpwd = false;
			}
			else if(reg.test($(".forgetPwd input").val())){
				$('.forgetPwd b').html('');
				fpwd = true;
				 
			}else{
				$('.forgetPwd b').html('密码只能为6-20位字符组合');
				fpwd = false;
			}
		};


//...........验证确认密码
	$(".forgetRepwd input").blur(function(){
		testRepwd();

	});
	function testRepwd(){
		var reg=/^[A-Za-z0-9]{6,20}$/;
		if($(".forgetRepwd input").val()=='' || $(".forgetRepwd input").val()==undefined){
			$('.forgetRepwd b').html('请再次输入密码');
			frpwd = false
		}
		else if($(".forgetRepwd input").val()==$(".forgetPwd input").val()){
			$('.forgetRepwd b').html('');
			frpwd = true;
			 
		}else{
			$('.forgetRepwd b').html('两次密码不一致');
			frpwd = false
		}
	};

//忘记密码的短信验证
//..........手机验证码
	var timer;
	var getCode=true;
	$('.forgetPhone span').on('click',function(){
		var forget ="forget";
		testPhone();
		if(getCode&&fphone){

			$.ajax({
				type: 'post',
				url: BASE_PATH+'/lastHongbao/judgePhoneNumber.htm?forget='+forget,
				data: {'phoneNumber':$("input[name='forgetPhone']").val()},
				dataType:'json',
				async:false,
				success: function (data) {
					if (data.success) {
						getCode=false;
						console.log(getCode)
						var num=60;
						$('.forgetPhone span').html( '重新发送('+num+')').css({'background':'#ccc','color':'#666'});
						timer=setInterval(function(){
							num--;
							$('.forgetPhone span').html('重新发送('+num+')').css({'background':'#ccc','color':'#666'});
							if(num==0){
								clearInterval(timer)
								$('.forgetPhone span').html('重新获取验证码').css({'background':'#f04848','color':'#fff'});
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
	

	//确认
	$(".reset").on('touchstart',function(){
			testPhone();
			testPassword();
			testRepwd();
		if(fphone&frpwd&fpwd){
			$.ajax({
				url:BASE_PATH+'/lastHongbao/hongbaoResetPasswor.htm',
				type:'post',
				dataType:'json',
				data:$("#forgetForm").serialize(),
				success:function(data){
					if(data.success) {
						var returnStr = data.data.returnStr;
						var companyId = data.data.companyId;
						if (returnStr == "perfectInfo") {
							//跳转至完善信息页面
							window.location = BASE_PATH + "/lastHongbao/jumpToCompanyInfoPage.htm?companyId="+companyId;
						}
						else if (returnStr == "collectInfo") {
							//跳转至信息收集页面
							window.location = BASE_PATH + "/lastHongbao/jumpToWebCollectionInfoPage.htm?companyId=" + companyId;
						}
						else if (returnStr == 0) {
							//待审核
							window.location = BASE_PATH + "/lastHongbao/jumpToPromptWait.htm";
						} else if (returnStr == 1) {
							//审核未通过
							var reason = data.data.reason;
							window.location = BASE_PATH + "/lastHongbao/jumpToPromptDefeat.htm?reason=" + reason + "&companyId=" + companyId;
						} else if (returnStr == 2) {
							//审核通过
							window.location = BASE_PATH + "/lastHongbao/jumpToPromptSuccess.htm";
						}
					}else{
						alert(data.message);
					}
				},
				error:function(){

				}
			});
		}
	})
	//...........返回登录
	$('.returnLogin').on('touchstart',function(){
		window.location.href=BASE_PATH+"/lastHongbao/jumpToLoginPage.htm";
	})
})

	