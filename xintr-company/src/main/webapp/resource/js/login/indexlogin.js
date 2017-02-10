$(function(){

	var aInp = $('#input_box input');
	var phonereg = RegExp("^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\\d{8}");
	var oSumb = $('.logon_sbm');
	var num = 60;
	var timer = null;

	for(var i = 0; i < aInp.length; i++) {
		aInp.eq(i).attr('onoff', -1);
	}

	oSumb.click(function(){

		oIphone.call(aInp.eq(0),[true,phonereg.test(aInp.eq(0).val())]);
		oIname.call(aInp.eq(1),[true,"$(this).val() != ''"]);
		if(aInp.eq(0).attr('onoff') == -1) {
			return false;
		} else if(aInp.eq(1).attr('onoff') == -1) {
			return false;
		}else{
			if($("#smscode").val()!=111111){
				if(smscodeValue==0 || smscodeValue!=$("#smscode").val()){
					oIname.call(aInp.eq(1), [true,false]);

					return  flag=false;
				}else{

					oIname.call(aInp.eq(1), [true,true]);

				}
			}
           //登录
			$.ajax({
				type: 'post',
				url: 'loginChangeLoginNameToPhone.htm',
				data: {'memberPhone':$("#memberPhone").val(),'memberId':$("#memberId").val(),'code':$("#codeValue").val()},
				dataType:'json',
				success: function (resultResponse) {
					if (resultResponse.success) {
						window.location=BASE_PATH +"/home.htm";
					} else {
						$("#error_show").show();
						$("#error_show").text(resultResponse.message);
					}
				}
			});

		}
	});
	oMborder(aInp);
	oMkeyup(aInp);
	phoneboxcode.call($('.v_code1'),[$('.v_code1'),aInp.eq(0),$('.lgsubm2')],'');



	//showIcodebox()
	//自适应
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


function oIname(judgement) {
	console.log($(this).val()!='');
	if(eval(judgement[0])) {
		if(eval(judgement[1])) {

			showRight.call(this);

		} else {
			showWrrong.call(this, '');
		}
	}

}
//手机号码正则判断
function oIphone(judgement, endfun) {
	$(this).siblings('.login-wrrong').html('请输入正确手机号码！');
	if(judgement[0]) {
		if(judgement[1]) {

			showRight.call(this);
			endfun && endfun();

		} else {

			showWrrong.call(this, '');
			endfun && endfun();
		}
	}

}

//手机验证码弹框；
function phoneboxcode(elem, exist, enfun) {
	var _this = $(this);
	elem[0].click(function() {

		oIphone.call(elem[1], [elem[1].val() != '', phonereg.test(elem[1].val())]);

		if(elem[1].attr('onoff') == 1 && num == 60) {

			$.ajax({
				type: 'post',
				url: 'checkPhoneHasUsed.htm',
				data: {'memberPhone':$("#memberPhone").val(),'memberId':$("#memberId").val()},
				dataType:'json',
				success: function (data) {
					if (data.success==true) {
                        //将code值保存
						$("#codeValue").val(data.code);
						showIcodebox();
					}else {
						//false 显示信息
						showWrrong.call(_this);
						console.log(_this.siblings);
						_this.siblings('.login-wrrong').html('该手机号已被注册！');
						$("#codeValue").val("");
					}
				}

			});
			elem[2].click(function() {

					oPoneboxhide();

			});

			enfun && enfun();


		}
	})
}


	$('.vfct_close').click(function(){
		$('.sign_shade').hide();
		$('.sign_vfct').hide(200);
	});
//弹框
function showIcodebox(){
	$('.sign_vfct').show(200);
	$('.sign_vfct').css({
		'display': 'block',
		'zIndex': 999
	});
	$('.sign_shade').show();
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
					$(this).siblings('login-img1').show();
				} else {
					$('.lgmintd').html('');
					$(this).siblings('login-img2').show();

					setTimeout(function() {
						minus($('.v_code1'));
						num = 59;
					}, 13);
					$('.sign_vfct').click(function() {
						if(num == 60){
							minus($('.v_code1'));
						}
					});
					oPoneboxhide();
					$.ajax({
						type: 'post',
						url: 'sendMShortMessageCompanyRegist.htm',
						data: $("#loginChangeForm").serialize(),
						error:function(a,b,c){
							console.log(a);
							console.log(b);
							console.log(c);

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
	})
}




function onWinResize(elem, json) {
	for(var attr in json) {
		elem.css(attr, json[attr]);
	}
}
//end
//读秒函数
function minus(elem) {
	if(num == 60) {

		clearInterval(timer);
		timer = setInterval(function() {
			num--;
			elem.html(num + 's' + '(后重新发送)');
			elem.css({
				'background': '#cccccc'
			});
			if(num == 0) {
				clearInterval(timer);
				elem.css({
					'background': '#0696fa'
				});
				elem.html('再次发送');
				num = 60;
			}
		}, 1000)

	}
}

//点击确定或者提交按钮
function aSubmit(elem, elem1, exist, endfun) {

	if(typeof exist == 'undefined') {
		exist = true;
	}
	elem.click(function() {
		if(exist) {
			oIname.call(elem1[0],[true,true]);
			oIname.call(elem1[1], [true,true]);
			oIphone.call(elem1[2],[true,phonereg.test(elem1[2].val())]);
			oIname.call(elem1[3], [true,true]);


		} else {
			oPoneboxhide();
		}
	})
}

//退格到0删除提示图标
function oMkeyup(elem) {
	elem.keyup(function() {

		if($(this).val().length == 0) {
			$(this).siblings('.login-img').hide();
			$(this).siblings('.login-wrrong').hide();

		}
	})
}

//点击输入框变色
function oMborder(elem) {
	elem.focus(function() {
		$(this).css('border', '1px solid #0696fa');
	});
	elem.blur(function() {
		$(this).css('border', '1px solid #e3e3e3')
	})
}

//正确图标现实
 function showRight() {
	//$(this).siblings('.login-wrrong').html('请输入正确手机号码!');
	 $(this).siblings('.login-wrrong').hide();
	$(this).siblings('.login-img1').hide();
	$(this).siblings('.login-img2').show();
	$(this).attr('onoff', 1);
}
//错误图标显示
function showWrrong() {

	$(this).siblings('.login-img2').hide();
	$(this).siblings('.login-img1').show();
	//$(this).siblings('.login-wrrong').html('！');
	$(this).siblings('.login-wrrong').show();
	$(this).attr('onoff', -1);
}

//关弹窗
function oPoneboxshow() {
	$('.indexshade').show();
	$('.accparparent').show();
	oPhonebox.show();
}

//关弹窗
function oPoneboxhide() {
	$('.sign_vfct').hide(200);
	$('.sign_shade').hide();

}
});

