$(function(){

var fregphone=false,fregpwd=false;

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
	$('.login').on('touchstart',function(){
		testRegPhone()
		testRegPwd();
		if(fregphone&&fregpwd){
			//登录
			$.ajax({
				url:BASE_PATH+'/lastHongbao/webLogin.htm',
				data:$("#loginForm").serialize(),
				type:'post',
				dataType:'json',
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
					alert("网路错误，请刷新页面")
				}
			});
		}
	});
	// 跳转到注册页
	$('.goRegister').on('touchstart',function(){
		window.location.href=BASE_PATH+"/lastHongbao/jumpToRegisterPage.htm";
	})
	// 忘记密码
	$('.forget').on('touchstart',function(){
		window.location.href=BASE_PATH+"/lastHongbao/toHongbaoForgertPasswordPage.htm";
	})
})