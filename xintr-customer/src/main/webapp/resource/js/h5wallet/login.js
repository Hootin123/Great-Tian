$(function(){
    var phoneVali=false;
	var pwdVali=false;
	var codeValli=false;
	$('#phone input').blur(function(){
		testPhone();
	});

	function testPhone(){
		var  reg=/^1[3|5|7|8]\d{9}$/;
		if($("#phone input").val()=='' || $("#phone input").val()==undefined ){
			$('#phone b').html('请输入手机号码');
			$('#phone em').removeClass('show');
			phoneVali=false;
		}
		else if(reg.test($("#phone input").val())){
			$('#phone b').html('');
			$('#phone em').addClass('show');
			phoneVali = true;
		}
		else{
			$('#phone b').css('width','2.2rem').html('请输入正确的手机号');
			$('#phone em').removeClass('show');
			phoneVali=false;
		}
	}
	$("#pwd input").blur(function(){
		testPassword();
	});
	function testPassword(){
		var reg=/^[A-Za-z0-9]{6,20}$/;
		if($("#pwd input").val()=='' || $("#pwd input").val()==undefined){
			$('#pwd b').html('请输入6-20位字符组合的密码');
			$('#pwd em').removeClass('show');
			pwdVali=false;
		}
		else if(reg.test($("#pwd input").val())){
			$('#pwd b').html('');
			$("#pwd em").addClass('show');
			pwdVali = true;

		}else{
			$('#pwd b').html('密码只能为6-20位字符组合');
			$('#pwd em').removeClass('show');
			pwdVali=false;
		}
	}
	$("#code input").blur(function(){
		testCode();
	});

	function testCode(){
		if($("#code input").val()=='' || $("#code input").val()==undefined){
			$(".hint").text("请输入验证码");
			$(".hint").css("display","block");
			codeValli=false;
		}else{
			$(".hint").text("");
			$(".hint").css("display","none");
			codeValli=true;
		}
	}
	//...........去注册
	$('.reg').on('click',function(){
		window.location.href='toH5walletRegisterPage.htm';
	});

	//...........登录
	$('.login').on('click',function(){
		testPhone();
		testPassword();
		testCode();
		if(phoneVali && pwdVali && codeValli){
			$.ajax({
				type: 'post',
				url: 'h5Login.htm',
				data: $("#loginForm").serialize(),
				async:false,
				success: function (data) {
					console.log(data);
					if (data.success) {
						var companyId=data.data;
						console.log(companyId);
						$(".hint").text("");
						$(".hint").css("display","none");
						window.location.href = "toH5walletHomePage.htm?companyId="+companyId;
					} else {
						$(".hint").text(data.message);
						$(".hint").css("display","block");
					}
				}
			});
		}
	})
});
function changeCodeImg(){
	$("#imageCode").attr("src",'generateH5ValidatorImages.htm?time='+new Date().getTime());
}