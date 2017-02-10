$(function(){
	var fregphone=false,fregpwd=false,fcodeValli=false;
	// 验证手机号
	$('.loginPhone input').blur(function(){
		testRegPhone()
	});

	function testRegPhone(){
		var  reg=/^1[3|5|7|8]\d{9}$/;
		if($(".loginPhone input").val()=='' || $(".loginPhone input").val()==undefined ){
			$('.loginPhone b').addClass('error').html('请输入手机号码').show();
			 setTimeout(function(){
                    $('.loginPhone b').fadeOut(500)
                },1000);
			fregphone=false;
		}
	 	else if(reg.test($(".loginPhone input").val())){
	 		$('.loginPhone b').removeClass('error').html('').hide();
			fregphone = true;
		}
		else{
			$('.loginPhone b').addClass('error').html('请输入正确的手机号').show();
			 setTimeout(function(){
                    $('.loginPhone b').fadeOut(500)
                },1000);
			fregphone=false;
		}
	};



	//...........验证密码
	$(".loginPwd input").blur(function(){
		testRegPwd();

	});
	function testRegPwd(){
		var reg=/^[A-Za-z0-9]{6,20}$/;
		if($(".loginPwd input").val()=='' || $(".loginPwd input").val()==undefined){
			$('.loginPwd b').addClass('error').html('请输入6-20位字符组合的密码').show();
			 setTimeout(function(){
                    $('.loginPwd b').fadeOut(500)
                },1000);
			fregpwd=false;
		}
		else if(reg.test($(".loginPwd input").val())){
			$('.loginPwd b').removeClass('error').html('').hide();
			fregpwd = true;
			 
		}else{
			$('.loginPwd b').addClass('error').html('密码只能为6-20位字符组合').show();
			setTimeout(function(){
                    $('.loginPwd b').fadeOut(500)
                },1000);
			fregpwd=false
		}
	};

	// 图形验证

	$(".LoginCode input").blur(function(){
		testCode();
	});

	function testCode(){
		if($(".LoginCode input").val()=='' || $(".LoginCode input").val()==undefined){
			$(".LoginCode b").addClass('error').html('请输入验证码').show();
			setTimeout(function(){
                    $('.LoginCode b').fadeOut(500)
                },1000);
			fcodeValli=false;
		}else{
			$(".LoginCode b").removeClass('error').html('').hide()
			fcodeValli=true;
		}
	}
	//登录
	$('.login').on('click',function(){
		testRegPhone()
		testRegPwd();
		testCode();
		if(fregphone&&fregpwd&&fcodeValli){
			//登录
			$.ajax({
				url:BASE_PATH+'/web/webLogin.htm',
				data:$("#loginForm").serialize(),
			    type:'post',
			    dataType:'json',
				success:function(data){
					if(data.success) {
						var returnStr = data.data.returnStr;
						var companyId = data.data.companyId;
						if (returnStr == "perfectInfo") {
							//跳转至完善信息页面
							window.location = BASE_PATH + "/web/jumpToCompanyInfoPage.htm?companyId="+companyId;
						}
						else if (returnStr == "collectInfo") {
							//跳转至信息收集页面
							window.location = BASE_PATH + "/web/jumpToWebCollectionInfoPage.htm?companyId=" + companyId;
						}
						else if (returnStr == 0) {
							//待审核
							window.location = BASE_PATH + "/web/jumpToPromptWait.htm";
						} else if (returnStr == 1) {
							//审核未通过
							var reason = data.data.reason;
							window.location = BASE_PATH + "/web/jumpToPromptDefeat.htm?reason=" + reason + "&companyId=" + companyId;
						} else if (returnStr == 2) {
							//审核通过
							window.location = BASE_PATH + "/web/jumpToPromptSuccess.htm";
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

	// 去注册
	$('.register').on('click',function(){
		window.location=BASE_PATH+"/web/jumpToRegisterPage.htm";
	});


	//刷新验证码
	$("#code").click(function(){
		var path = BASE_PATH+"/web/getImgCode.htm?time="+new Date().getTime();
		$("#code").attr("src",path);
	});

});
