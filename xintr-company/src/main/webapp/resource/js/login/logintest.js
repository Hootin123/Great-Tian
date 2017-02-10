$(function() {

	<!-- 初始化清除密码-->
		$("#memberPassword").val("");
	    $("#verifycode").val("");
	    $("#memberLogname").val("");
	var aInp = $('.logtext');

	/*单位名称和验证码*/
	function oIname(val) {

		if(eval(val)) {
			if($(this).val() != '') {
				//$(this).siblings('.user-wrrong').css('display', 'none');
				//$(this).siblings('.login-img1').css('display', 'none');
				//$(this).siblings('.login-img2').css('display', 'inline-block');

				$(this).attr('onoff', 1);

			} else {

				$(this).siblings('.login-img2').css('display', 'none');
				$(this).siblings('.login-img1').css('display', 'inline-block');
				$(this).siblings('.user-wrrong').css('display', 'inline-block');
				$(this).attr('onoff', -1);
			}
		}
	}

//			aInp.eq(0).blur(function() {
//				oIname.call($(this),"$(this).val()!=''")
//			})
//			aInp.eq(1).blur(function() {console.log($(this).siblings())
//				oIname.call($(this),"$(this).val()!=''")
//			})
//			aInp.eq(2).blur(function() {console.log($(this).siblings())
//				oIname.call($(this),"$(this).val()!=''")
//			})

	$('.logon_sbm').click(function(){
		oIname.call(aInp.eq(0),true);
		oIname.call(aInp.eq(1),true);
		oIname.call(aInp.eq(2),true);

		if(aInp.eq(0).attr('onoff') == 1 && aInp.eq(1).attr('onoff') == 1 && aInp.eq(2).attr('onoff') == 1){
			$.ajax({
				type: 'post',
				url: 'checkVerifycode.htm',
				data: $("#registerForm").serialize()+"&verifycode="+aInp.eq(2).val(),
				success:function(resultResponse){
					if(resultResponse.success){
						aInp.eq(2).siblings('.user-wrrong').css('display', 'none');
						aInp.eq(2).siblings('.login-img1').css('display', 'none');
						aInp.eq(2).siblings('.login-img2').css('display', 'inline-block');
						onLogin();
					}else{
						aInp.eq(2).siblings('.user-wrrong').css('display', 'inline-block');
						aInp.eq(2).siblings('.login-img1').css('display', 'inline-block');
						aInp.eq(2).siblings('.login-img2').css('display', 'none');
						$("#vcodeimg").click();
						return false;
					}

				}});
		}else{
			return false;
		}

	})


	/*end*/
	/*自适应*/
	window.onresize = function() {
		onWinResize($('#lgbdy'), {
			'height': $(document).height() - 60 - 70
		});

		onWinResize($('.sign_shade'), {
			'width': $(document).width(),
			'height': $(document).height()
		});
	}

	onWinResize($('#lgbdy'), {
		'height': $(document).height() - 60 - 70
	});

	onWinResize($('.sign_shade'), {
		'width': $(document).width(),
		'height': $(document).height()
	});

	function onWinResize(elem, json) {
		for(var attr in json) {
			elem.css(attr, json[attr]);
		}
	};

	/*end*/

})

/**
 * 登录
 *
 */
function onLogin() {
	var aInp = $('.logtext');
	$.ajax({
		type: 'post',
		url: BASE_PATH +'/login.htm',
		data: $("#loginForm").serialize(),
		success: function (resultResponse) {

			if (resultResponse.success) {
				if(resultResponse.msgCode=="1"){
					//跳转到默认修改登录名为手机号的页
					window.location=BASE_PATH+"/loginChangeLoginName.htm"
				}else {

					$("#memberLognameTip").text("");
					window.location=BASE_PATH +"/home.htm";
				}

			} else {
				if(resultResponse.msgCode=="0"){
					$("#verifycodeTip").text(resultResponse.message);
					$('#user_text').css('display','none');
					$('#user_text').html("");
				}else if (resultResponse.msgCode=="no-register"){
					$("#vcodeimg").click();

					$("#user_img_no").css('display','block');
					$('#user_text').css('display','block');
					$('#user_text').html('用户还未注册，请先去注册！');

					//其他不显示
					$("#poss_img_yes").css('display','none');
					$("#poss_text").html("");
                    $("#code_img_yes").css('display','none');
					$("#code_text").html("");
				}else{
					$("#vcodeimg").click();
					$('#user_img_no').css('display','none');
					$('#user_text').html("");
					aInp.eq(1).siblings('.login-img2').css('display','none');
					aInp.eq(1).siblings('.login-img1').css('display','inline-block');
					aInp.eq(1).siblings('.user-wrrong').css('display','inline-block');
					if(resultResponse.message=="该用户已关闭"){
						aInp.eq(1).siblings('.user-wrrong').html('该用户已关闭！');
					}else{
						aInp.eq(1).siblings('.user-wrrong').html('账户或密码错误！');
					}
					aInp.eq(2).val('');
					aInp.eq(2).siblings('.login-img2').css('display','none');
					aInp.eq(2).siblings('.user-wrrong').css('display','none');
				}
			}
		}
	});
}