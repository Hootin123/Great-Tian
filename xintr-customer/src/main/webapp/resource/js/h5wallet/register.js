var companyId=0;
$(function(){


		var oMyForm = new FormData();
 
oMyForm.append("username", "Groucho");
oMyForm.append("accountnum", 123456); // 数字123456被立即转换成字符串"123456"


console.log(oMyForm)










	var fcode = false,faccount = false,fpwd = false,fname=false,fphonecode=false;

	//......... 验证公司名称
	$('#username input').blur(function(){
		testUsername();
	});
	function testUsername(){
		var reg=/^[\w\u4e00-\u9fa5]{4,20}$/;
		if($("#username input").val()=='' || $("#username input").val()==undefined ){
			$('#username b').html('请输入4-20位的公司名称');
			$('#username em').removeClass('show');
			fname=false;
		}
		else if(reg.test($("#username input").val())){
			$('#username b').html('');
			$('#username em').addClass('show');
			fname=true;
		}
		else{
			$('#username b').html('请输入正确的公司名称');
			$('#username em').removeClass('show');
			fname=false;
		}
	}

	// 验证手机号
	$('#phone input').blur(function(){
		testPhone();
	});
	function testPhone(){
		var  reg=/^1[3|5|7|8]\d{9}$/;
		if($("#phone input").val()=='' || $("#phone input").val()==undefined ){
			$('#phone b').html('请输入手机号码');
			$('#phone em').removeClass('show');
			faccount = false;
		}
		else if(reg.test($("#phone input").val())){
			$('#phone b').html('');
			$('#phone em').addClass('show');
			faccount = true;
		}
		else{
			$('#phone b').html('请输入正确的手机号');
			$('#phone em').removeClass('show');
			faccount = false;
		}
	}
	//验证密码
	$("#password input").blur(function(){
		testPassword();
	});
	function testPassword(){
		var reg=/^[A-Za-z0-9]{6,20}$/;
		if($("#password input").val()=='' || $("#password input").val()==undefined){
			$('#password b').html('请输入6-20位字符组合的密码');
			$('#password em').removeClass('show');
			fpwd = false;
		}
		else if(reg.test($("#password input").val())){
			$('#password b').html('');
			$("#password em").addClass('show');
			fpwd = true;

		}else{
			$('#password b').html('密码只能为6-20位字符组合');
			$('#password em').removeClass('show');
			fpwd = false;
		}
	}
	//验证验证码
	$("#code input").blur(function(){
		testCode();
	});
	function testCode(){
		if($("#code input").val()=='' || $("#code input").val()==undefined){
			$(".hint").text("请输入验证码");
			$(".hint").css("display","block");
			fcode=false;
		}else{
			$(".hint").text("");
			$(".hint").css("display","none");
			fcode=true;
		}
	}
	//验证手机验证码
	$("#phonecode input").blur(function(){
		testPhoneCode();
	});
	function testPhoneCode(){
		if($("#phonecode input").val()=='' || $("#phonecode input").val()==undefined){
			$(".hint").text("请输入手机验证码");
			$(".hint").css("display","block");
			fphonecode=false;
		}else{
			$(".hint").text("");
			$(".hint").css("display","none");
			fphonecode=true;
		}
	}
	//注册
	$(".reg").on('click',function(){
		testUsername();
		testPhone();
		testPassword();
		testCode();
		testPhoneCode();
		if(fcode & faccount & fpwd & fname &fphonecode){
			$.ajax({
				type: 'post',
				url: 'h5Register.htm',
				data: $("#registerForm").serialize(),
				async:false,
				success: function (data) {
					console.log(data);
					if (data.success) {
						companyId=data.data;
						console.log(companyId);
						$(".hint").text("");
						$(".hint").css("display","none");
						$(".promt").css("display","block");
						//window.location.href = "toH5walletHomePage.htm?companyId="+companyId;
					} else {
						$(".hint").text(data.message);
						$(".hint").css("display","block");
					}
				}
			});
		}
	});

	//跳转登录界面
	$(".login").on('click',function(){
		window.location.href='toH5walletLoginPage.htm';
	});

	//发送验证码
	var timer;
	var getCode=true;
	$('#phonecode p').on('click',function(){
		testPhone();
		if(getCode && faccount){
			$.ajax({
				type: 'post',
				url: 'sendPhoneMsgByH5.htm',
				data: {"memberPhone":$("[name=memberPhone]").val()},
				async:false,
				success: function (data) {
					console.log(data);
					if (data.success) {
						$(".hint").text("");
						$(".hint").css("display","none");
						fphonecode=true;
						getCode=false;
						console.log(getCode);
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
						fphonecode=false;
					}
				}
			});

		}
	});

	//点击确定按钮
    $("#sureBtn").on("click",function(){
		$(".promt").css("display","none");
		window.location.href = "toH5walletHomePage.htm?companyId="+companyId;
	});
});
function changeCodeImg(){
	$("#imageCode").attr("src",'generateH5ValidatorImages.htm?time='+new Date().getTime());
}

	