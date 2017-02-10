var smscodeValue=0;
$(function() {

	//shi
	var registerFlag=true;


		function code() {
			var _this = this;
			this.oDiv = $('.v_code1');
			this.oDiv1 = $('.lgip1');
			this.oDiv2 = $('.lgip2');
			this.shad = $('.sign_shade');
			this.sigvfct = $('.sign_vfct');
			this.oClo = $('.vfct_close');
			this.num = 60;
			this.oReg = RegExp("^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\\d{8}");

			$('.lgsubm1').click(function () {
				var oVal = $('.lgip1').val();
				$.ajax({
					type: "POST",
					url: "getCheckCode.htm",
					dataType: "json",
					data: {
						'vcodeimg': oVal
					},
					async: true,
					success: function (data) {
						if (data.success==false) {
							$('.lgmintd').html('请输入正确的验证码！');
							$(this).siblings('img').css('display', 'inline-block');
							$(this).siblings('img').attr('src', 'resource/img/wrrong.png');
						} else {
							$('.lgmintd').html('');
							$(this).siblings('img').css('display', 'inline-block');
							$(this).siblings('img').attr('src', 'resource/img/right.png');
							_this.cLose();
							setTimeout(function() {
								_this.minus();
							}, 13);
							obj.cLose();
							$.ajax({
								type: 'post',
								url: 'sendMShortMessageCompanyRegist.htm',
								data: $("#registerForm").serialize(),
								error:function(a,b,c){
									//console.log(a);
									//console.log(b);
									//console.log(c);

								},
								success: function (resultResponse) {

									if (resultResponse.success) {
										//   $("#memberPhoneTip").text("");
										//  window.location="activateInfo.htm";
									} else {
										smscodeValue=resultResponse.message;
										//$("#memberPhoneTip").text(resultResponse.message);
										//  return flag=false;

									}
								}
							});
						}
					}
				});
			});

			this.oClo.click(function () {
				_this.cLose();
			});
			$('.lgsubm2').click(function () {
				_this.cLose();
			})

		}
		code.prototype = {
			'minus': function() {
				if(this.num == 60) {
					var _this = this;
					clearInterval(this.timer);
					this.timer = setInterval(function() {

						_this.num--;
						_this.oDiv.html(_this.num + 's' + '(后重新发送)');
						_this.oDiv.css({
							'background': '#cccccc'
						});
						if(_this.num == 0) {
							clearInterval(_this.timer);
							_this.oDiv.css({
								'background': '#0696fa'
							});
							_this.oDiv.html('再次发送');
							_this.num = 60;
						}
					}, 1000)

				}
			},
			'abx': function() {

			},
			'cLose': function() {
				this.shad.css('display', 'none');
				this.sigvfct.hide(200);
				clearInterval(this.timer);
				$('.lgmintd').html('');
				//this.num = 60;
//			this.oDiv.css({
//				'background': '#0696fa'
//			});
//			this.oDiv.val('获取验证码');
//			$('.lgmintd').html('');
			}
		};
		var obj = new code();
		//alert(code)
		var phonereg = RegExp("^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\\d{8}");
		var psswordreg = /^\w{6,20}$/;
		var aInp = $('#input_box input');

		/*添加开关*/
		for(var i = 0; i < aInp.length; i++) {
			aInp.eq(i).attr('onoff', -1);
		}
		/*end*/

		/*弹框*/
		$('.v_code1').click(function() {
			oIphone.call($(this), "$(this).val() != ''");
			if(aInp.eq(4).attr('onoff') != 1){
				return false;
			}
			var _this = $(this);

			$('.lgip1').val('');
			$.ajax({
				type: 'post',
				url: 'registerByPhone.htm',
				data: $("#registerForm").serialize(),
				success: function (resultResponse) {
					if (resultResponse.success) {
						anotherImage();
						oIphone.call(aInp.eq(4), "aInp.eq(4).val() != ''");
						if(aInp.eq(4).attr('onoff') == 1 && obj.num == 60) {
							$('.sign_shade').show();
							$('.sign_vfct').show(200);

							$('.lgsubm1').click(function() {
								if($('.lgip1').val() == $('.lgip2').val()) {

								}
							})
						}
					} else {
						checkPhone();
					}
				}
			});
		});
		/*end*/
		function checkPhone(){
			$('.v_code1').siblings('.login-img').css('display','inline-block');
			$('.v_code1').siblings('.login-img').attr('src','resource/img/wrrong.png');
			$('.login-wrrong').eq(4).css('display','inline-block');
			$('.login-wrrong').eq(4).html('该手机已被注册！')
		}
		/*手机*/
		function oIphone(abtn) {

			if(eval(abtn)) {

				if(phonereg.test(aInp.eq(4).val())) {

					$(this).siblings('.login-wrrong').css('display', 'none');
					$(this).siblings('.login-img').attr('src', 'resource/img/right.png');
					$(this).siblings('.login-wrrong').html('请输入手机号码！');
					$(this).siblings('.login-img').css('display', 'inline-block');
					$(this).attr('onoff', 1);

				} else {
					$(this).attr('onoff', -1);
					$(this).siblings('.login-img').attr('src', 'resource/img/wrrong.png');
					$(this).siblings('.login-img').css('display', 'inline-block');
					$(this).siblings('.login-wrrong').css('display', 'inline-block');
				}
			}else {
				//$(this).siblings('.login-wrrong').css('display', 'inline-block');
				//$(this).siblings('.login-wrrong').html('请输入手机号码！');
			}
		}
		/*end*/

		/*密码*/
		function oIpassword(abtn1) {
			if(eval(abtn1)) {
				if(psswordreg.test($(this).val())) {

					$(this).siblings('.login-wrrong').css('display', 'none');
					$(this).siblings('.login-img').attr('src', 'resource/img/right.png');
					$(this).siblings('.login-img').css('display', 'inline-block');
					$(this).attr('onoff', 1);
					oIpassword1.call(aInp.eq(3), "$(this).val() != ''");
					aInp.eq(3).blur(function() {
						oIpassword1.call($(this), "$(this).val() != ''");
					})

				} else {

					$(this).siblings('.login-img').attr('src', 'resource/img/wrrong.png');
					$(this).siblings('.login-img').css('display', 'inline-block');
					$(this).siblings('.login-wrrong').css('display', 'inline-block');
					$(this).attr('onoff', -1);
				}
			}

		}
		/*end*/

		/*确认密码*/
		function oIpassword1(abtn2) {

			if(psswordreg.test(aInp.eq(2).val()) && eval(abtn2)) {

				if($(this).val() == aInp.eq(2).val()) {
					$(this).siblings('.login-wrrong').css('display', 'none');
					$(this).siblings('.login-img').attr('src', 'resource/img/right.png');
					$(this).siblings('.login-img').css('display', 'inline-block');
					$(this).attr('onoff', 1);
				} else {
					$(this).siblings('.login-img').attr('src', 'resource/img/wrrong.png');
					$(this).siblings('.login-img').css('display', 'inline-block');
					$(this).siblings('.login-wrrong').css('display', 'inline-block');
					$(this).attr('onoff', -1);
				}
			}
		}
		/*end*/



		/*手机点击事件*/
		aInp.eq(4).blur(function() {
			oIphone.call($(this), "$(this).val() != ''");
		});

		/*退格到0*/
		oMkeyup(aInp);

		function oMkeyup(elem) {

			elem.keyup(function() {
				if($(this).val().length == 0) {
					$(this).siblings('.login-img').css('display', 'none');
					$(this).siblings('.login-wrrong').css('display', 'none');

				}
			})
		}
		/*end*/

		/*密码点击事件*/
		aInp.eq(2).blur(function() {
			oIpassword.call($(this), "$(this).val() != ''")
		});
		/*end*/

		/*边框样式*/
		oMborder(aInp);

		function oMborder(elem) {
			elem.focus(function() {
				$(this).css('border', '1px solid #0696fa');
			});
			elem.blur(function() {
				$(this).css('border', '1px solid #e3e3e3');
			})
		}
		/*end*/

		/*单位名称点击事件*/
		aInp.eq(0).blur(function() {
			oIname.call($(this), ["$(this).val() !=''","$(this).val() !=''"]);
		});
		/*end*/

		/*姓名点击事件*/
		aInp.eq(1).blur(function() {
			oIname.call($(this),[ "$(this).val() !=''","$(this).val() !=''"]);
		});
		/*end*/
	//校验单位长度4-20位
	function  checkName(abn){
		var mylenth = abn.val().length;
		//alert(abn.val().length);
		if(mylenth >=4 && mylenth<= 20){
			$(this).siblings('.login-wrrong').css('display', 'none');
			$(this).siblings('.login-img').attr('src', 'resource/img/right.png');
			$(this).siblings('.login-img').css('display', 'inline-block');
			//endfn && endfn();
			$(this).attr('onoff', 1);
		}else {
			$(this).siblings('.login-img').attr('src', 'resource/img/wrrong.png');
			$(this).siblings('.login-img').css('display', 'inline-block');
			$(this).siblings('.login-wrrong').css('display', 'inline-block');
			$(this).attr('onoff', -1);
		}
	}
	function checkUserName(abn){
		var lenth = abn.val().length;
		if(lenth >=2 && lenth<= 20){
			$(this).siblings('.login-wrrong').css('display', 'none');
			$(this).siblings('.login-img').attr('src', 'resource/img/right.png');
			$(this).siblings('.login-img').css('display', 'inline-block');
			//endfn && endfn();
			$(this).attr('onoff', 1);
		}else {
			$(this).siblings('.login-img').attr('src', 'resource/img/wrrong.png');
			$(this).siblings('.login-img').css('display', 'inline-block');
			$(this).siblings('.login-wrrong').css('display', 'inline-block');
			$(this).attr('onoff', -1);
		}
	}


	/*单位名称和验证码*/
	function oIname(abtn3, endfn) {

		if(eval(abtn3[0])) {
			if(eval(abtn3[1])) {
				$(this).siblings('.login-wrrong').css('display', 'none');
				$(this).siblings('.login-img').attr('src', 'resource/img/right.png');
				$(this).siblings('.login-img').css('display', 'inline-block');
				endfn && endfn();
				$(this).attr('onoff', 1);

			} else {
				$(this).siblings('.login-img').attr('src', 'resource/img/wrrong.png');
				$(this).siblings('.login-img').css('display', 'inline-block');
				$(this).siblings('.login-wrrong').css('display', 'inline-block');
				$(this).attr('onoff', -1);
			}
		}

	}
	/*end*/

		$('.logon_sbm').click(function() {

			checkName.call(aInp.eq(0),aInp.eq(0));
			//oIname.call(aInp.eq(0), [true,"$(this).val() !=''"]);
			checkUserName.call(aInp.eq(1),aInp.eq(1));
			//oIname.call(aInp.eq(1), [true,"$(this).val() !=''"]);
			//oIname.call(aInp.eq(5),	[true,"$(this).val() !=''"]);
			oIpassword.call(aInp.eq(2), true);
			oIpassword1.call(aInp.eq(3), true);
			oIphone.call(aInp.eq(4), true);

			if(aInp.eq(0).attr('onoff') == -1) {

				return false;
			} else if(aInp.eq(1).attr('onoff') == -1) {

				return false;
			} else if(aInp.eq(2).attr('onoff') == -1) {

				return false;
			} else if(aInp.eq(3).attr('onoff') == -1) {

				return false;
			} else if(aInp.eq(4).attr('onoff') == -1) {

				return false;
			} else {
				onRegister();
			}
		});
		/*end*/
		function  onRegister(){
			///** 正式环境加的**/
			//if ($("#smscode").val()==111111){
			//	oIname.call(aInp.eq(5), [true,false]);
	         //   return  flag=false;
             // }
				if(smscodeValue==0 || smscodeValue!=$("#smscode").val()){
					oIname.call(aInp.eq(5), [true,false]);

					return  flag=false;
				}else{

					oIname.call(aInp.eq(5), [true,true]);

				}
			$.ajax({
				type: 'post',
				url: 'register.htm',
				data: $("#registerForm").serialize(),
				success: function (resultResponse) {
					if (resultResponse.success) {
						var email = resultResponse.data.memberLogname;
						var phone = resultResponse.data.memberPhone;
						window.location="activateInfo.htm?email="+email+"&phone="+phone;
					} else {
						checkPhone();
					}
				}
			});
		}
		window.onresize = function() {
			onWinResize($('#lgbdy'), {
				'height': $(document).height() - 60 - 70
			});

			onWinResize($('.sign_shade'), {
				'width': $(document).width(),
				'height': $(document).height()
			});
		};

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
		}
	//end


	$("#yzcode").change(function(){
		if($("#yzcode").length!=4){
			$("#yzcode").text("验证码输入不正确");
			return false;
		}else{
			$("#yzcode").text("");
		}
	});

	$("#vcodeimg").click(function(event){
		$("#vcodeimg").attr('src',BASE_PATH + '/generateImages.htm?'+(new Date()).getTime())
	});

	$('#verifycode').change(function(event){
		var verifycode =$('#verifycode').val();
		$("#verifycodeTip").text("");
		if(registerFlag){
			if(verifycode.length!=4){
				$("#verifycodeTip").text("图形验证码长度不对，请重新输入");
				//registerFlag = false;
				// return registerFlag;
			}
			else{
				$.ajax({      type: 'post',
					url: 'checkVerifycode.htm',
					data: $("#registerForm").serialize()+"&verifycode="+verifycode,
					//async:false,
					success:function(resultResponse){
						//console.log(resultResponse.success);
						if(resultResponse.success){
							$("#verifycodeTip").text("");
						}else{
							$("#verifycodeTip").text(resultResponse.message);
							//$("#vcodeimg").click();
							//registerFlag=false;
							// return registerFlag;
						}

					}});
			}
		}
	});
    //shi
	$('.sign_shade').css('height',$(window).height() );
	$('.sign_shade').css('width',$(window).width() );
    //end
	$('#login').click(function(){
		var memberLogname =$("#memberLogname").val();
		var memberPassword =$("#memberPassword").val();
		var verifycode =$("#verifycode").val();
		$('#memberLognameTip').text("");
		$("#memberPasswordTip").text("");
		$("#verifycodeTip").text("");
		if(memberLogname==""){
			$("#memberLognameTip").text("用户名不能为空");
			return false;
		}else{
			$('#memberLognameTip').text("");
		}
		if(memberPassword ==""){
			$("#memberPasswordTip").text("密码不能为空");
			return false;
		}else{
			$("#memberPasswordTip").text("");
		}
		if(verifycode==""){
			$("#verifycodeTip").text("图形验证码不能为空");
			return false;
		}else{
			if(verifycode.length!=4){
				$("#verifycodeTip").text("图形验证码长度不对，请重新输入");
				registerFlag = false;
				return false;
			}
			else{
				$.ajax({      type: 'post',
					url: 'checkVerifycode.htm',
					data: $("#registerForm").serialize()+"&verifycode="+verifycode,
					//测试
					async:false,
					success:function(resultResponse){
						//console.log(resultResponse.success);
						if(resultResponse.success){
							$("#verifycodeTip").text("");
							onLogin();
						}else{
							$("#verifycodeTip").text(resultResponse.message);
							$("#vcodeimg").click();
							registerFlag=false;
							// return registerFlag;
							return false;
						}

					}});
			}
		}

		// onLogin();
		return false;
	});
});
/**
 * 登录
 *
 */
function onLogin() {
	$.ajax({
		type: 'post',
		url: BASE_PATH +'/login.htm',
		data: $("#loginForm").serialize(),
		success: function (resultResponse) {
			//console.log(resultResponse.success);
			if (resultResponse.success) {
				if(resultResponse.msgCode=="1"){
					  //跳转到默认修改登录名为手机号的页
					  window.location=BASE_PATH+"/loginChangeLoginName.htm"
				}else {

					$("#memberLognameTip").text("");
					// window.location=BASE_PATH +"/newHome.htm";
					window.location=BASE_PATH +"/home.htm";
				}

			} else {
				if(resultResponse.msgCode=="0"){
					$("#verifycodeTip").text(resultResponse.message);
				}else{
					$("#memberLognameTip").text(resultResponse.message);
				}
			}
		}
	});
}
function againregister(){
	$.ajax({
		type:"POST",
		url:"againregister.htm",
		dataType:"json",
		data:{
			'memberLogname':memberLogname
			,'memgerPhone':memgerPhone
		},
		async: true,
		success: function(data){
			if(data.success){
				alert('激活邮件已发送至您的注册邮箱：472255062@qq.com请进入邮箱查看邮件并激活。请在48小时内完成激活!');
			}else{
				alert('激活邮件失败!');
			}
		}
	});
}

function anotherImage(){
	$("#vcodeimg").click();
}

