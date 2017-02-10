
$(function(){

	//更换手机动画效果
	var aInp = $('.accountset1 input');
	var oPhonebox = $('.accountset1');
	var aLi = $('.aselist1 li');
	var aPhcode = $('.phcode');
	var aAsebtn = $('.asebtnbox');
	var oSumb = $('.aseip11');
	var phonereg = RegExp("^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\\d{8}$");
	var num = 60;
	var num1 = 60;
	var timer = null;
	var timer1 = null;

	//点击修改手机按钮
	aPhcode.eq(0).click(function(){
		oPhonebox.animate({'height':490});
		
		for(var i=2;i<aLi.length-1;i++){
			aLi.eq(i).css('display','block');
		}
		//minus($('.phcode'));
		$.ajax({
			type: 'post',
			url: 'sendMShortMessageCompanyModifyPhone.htm',
			data: {'memberPhone':$("#memberPhone").val()},
			error:function(a,b,c){
			},
			success: function (resultResponse) {
				if (resultResponse.success) {

				} else {
					minus($('.phcode'));
					smscodeValue=resultResponse.message;
				}
			}
		});
	});

	$('#changePhone').click(function(){
		$('.indexshade').show();
		$('.accouparent1').show(200);
	});

	$('.phcode1').click(function(){
		$('.login-wrrong').eq(2).html('请输入正确的手机号码！');
		oIphone.call(aInp.eq(5),[true,phonereg.test(aInp.eq(5).val())],function(){
			//登录
			$.ajax({
				type: 'post',
				url: 'checkPhoneHasUsed.htm',
				data: {'memberPhone':$("#newMemberPhone").val(),'memberId':$("#memberId").val()},
				dataType:'json',
				success: function (data) {

					if (data.success) {
						 if(data.code=="200"){
							//alert("手机未被使用");

							 $.ajax({
								 type: 'post',
								 url: 'sendMShortMessageCompanyModifyPhone.htm',
								 data: {'memberPhone':$("#newMemberPhone").val()},
								 error:function(a,b,c){
								 },
								 success: function (resultResponse) {
									 if (resultResponse.success) {

									 } else {
										 minus1($('.phcode1'));
										 showRight.call(aInp.eq(5));
										 //oIphone.call(aInp.eq(5),[true,true]);
										 smscodeValue1=resultResponse.message;
									 }
								 }
							 });

						 }
						 else if (data.code=="201") {

							 showWrrong.call(aInp.eq(5));
							 $('.login-wrrong').eq(2).html('新手机号码不能与原手机号码相同');

						 }
						 else {

							 showWrrong.call(aInp.eq(5));

						 }
					} else {

						showWrrong.call(aInp.eq(5));
						//layer.alert("新的手机号已被注册");
						$('.login-wrrong').eq(2).html('新的手机号已被注册');
					}
				}
			});

		});
	});
	
	
	/*关闭弹框*/
	$('.aseclose1,.aseip21').click(function(){
		phoneclose();
	});

	function phoneclose() {
		$('.indexshade').hide();
		$('.accouparent1').hide(200);
		$('.aselist1 li input').eq(3).val('');
		$('.aselist1 li input').eq(4).val('');
		$('.aselist1 li input').eq(6).val('');
		oPhonebox.css({'height':275});
		for(var i=2;i<aLi.length-1;i++){
			aLi.eq(i).css('display','none');
		}

		$('.phcode').val('修改');
		$('.phcode1').val('获取验证码');
		$('.phcode').css('background','#0696fa');
		//aInp.
		num = 60;
		num1 = 60;
		aInp.siblings('.login-img1').hide();
		aInp.siblings('.login-img2').hide();
		clearInterval(timer);
		clearInterval(timer1);
	}

	for(var i = 0; i < aInp.length; i++) {
		aInp.eq(i).attr('onoff', -1);
	}

	oSumb.click(function(){
		if(oPhonebox.height() == 490){
			
			oIname.call(aInp.eq(1),[true,"$(this).val() != ''"]);
			oIname.call(aInp.eq(4),[true,"$(this).val() != ''"]);
			oIphone.call(aInp.eq(5),[true,phonereg.test(aInp.eq(5).val())]);
			oIname.call(aInp.eq(7),[true,"$(this).val() != ''"]);

			if(aInp.eq(1).attr('onoff') == -1) {
				return false;
			} else if(aInp.eq(4).attr('onoff') == -1) {
				return false;
			}else if(aInp.eq(5).attr('onoff') == -1){
				return false;
			}else if(aInp.eq(7).attr('onoff') == -1){
				return false;
			}


			if($('#smscode1').val() != smscodeValue ){
				oIname.call(aInp.eq(4), [true,false]);

				if(smscodeValue == '该手机号当天注册验证短信条数已用完'){
					$('.login-wrrong').eq(0).html('短信发送失败！');
					$('.login-wrrong').eq(0).show();
				}

				return  false;
				$('.login-wrrong').eq(1).html('短信验证码错误！');
				$('.login-wrrong').eq(1).show();
			}else{
				oIname.call(aInp.eq(4), [true,true]);
				$('.login-wrrong').eq(1).html('请输入正确的短信验证码！');
				$('.login-wrrong').eq(1).hide();
			}

			if(typeof smscodeValue1 == 'undefined'){
				smscodeValue1 = '';
				$('.login-wrrong').eq(2).html('请点击获取验证码');
				$('.login-wrrong').eq(2).show();
				return false;
			}else {
				$('.login-wrrong').eq(2).html('请输入正确的手机号码！');
				$('.login-wrrong').eq(2).hide();
			}

			if(smscodeValue1 == '该手机号当天注册验证短信条数已用完'){
				$('.login-wrrong').eq(2).html('短信发送失败！');
				$('.login-wrrong').eq(2).show();
			}else {
				$('.login-wrrong').eq(2).html('请输入正确的手机号码！');
				$('.login-wrrong').eq(2).hide();
			}




			if($('#smscode2').val() != smscodeValue1 ){
				oIname.call(aInp.eq(6), [true,false]);
				$('.login-wrrong').eq(2).html('请点击获取验证码');
				$('.login-wrrong').eq(2).show();
				return false;
			}else{
				oIname.call(aInp.eq(6), [true,true]);
				$('.login-wrrong').eq(2).html('请输入正确手机号码！');
				$('.login-wrrong').eq(2).hide();
			}

			$.ajax({
				type: 'post',
				url: 'changeMemberPhone.htm',
				data: $("#changePhoneForm").serialize(),
				dataType:'json',
				success:function(resultResponse){
					if(resultResponse.success){

						$("#userName").val(resultResponse.data);
						$("#phoneNume").html(resultResponse.msgCode);
						$("#phoneNume2").html(resultResponse.msgCode);
						$("#memberPhone").val(resultResponse.msgCode);
						//alert(resultResponse.msgCode)
						layer.alert("修改成功");
						phoneclose();
						$('.accparparent1').hide(200);
						$('.indexshade').hide();
						//window.location=BASE_PATH +"/home.htm";
					}else{
						layer.alert(resultResponse.message)
					}
				}
			});


		}else {

			$.ajax({
				type: 'post',
				url: 'changeMemberPhone.htm',
				data: $("#changePhoneForm").serialize(),
				dataType:'json',
				success:function(resultResponse){
					if(resultResponse.success){
						$("#userName").val(resultResponse.data);
						layer.alert("修改成功");
						phoneclose();
						$('.accparparent1').hide(200);
						$('.indexshade').hide();
					}else{
						layer.alert(resultResponse.message);
					}
				}
			});

		}


		return false;
	});
	oMkeyup(aInp);
	//phoneboxcode.call($('.v_code1'),[$('.v_code1'),aInp.eq(0),$('.lgsubm2')],'')


function oIname(judgement) {
	//console.log($(this).val()!='')
	if(eval(judgement[0])) {
		if(eval(judgement[1])) {

			showRight.call(this);
			//$('this').attr('onoff',1);
			
		} else {
			showWrrong.call(this, '');
			//$('this').attr('onoff',-1)
		}
	}

}
//手机号码正则判断
function oIphone(judgement, endfun) {

	if(judgement[0]) {
		if(judgement[1]) {

			$(this).attr('onoff',1);
			endfun && endfun();
			
		} else {
			showWrrong.call(this, '');
		}
	}

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
			elem.val(num + 's' + '(后重新发送)');
			elem.css({
				'background': '#cccccc'
			});
			if(num == 0) {
				clearInterval(timer);
				elem.css({
					'background': '#0696fa'
				});
				elem.val('再次发送');
				num = 60;
			}
		}, 1000)

	}
}

function minus1(elem) {
	if(num1 == 60) {

		clearInterval(timer1);
		timer1 = setInterval(function() {
			num1--;
			elem.val(num1 + 's' + '(后重新发送)');
			elem.css({
				'background': '#cccccc'
			});
			if(num1 == 0) {
				clearInterval(timer1);
				elem.css({
					'background': '#0696fa'
				});
				elem.val('再次发送');
				num1 = 60;
			}
		}, 1000)

	}
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
	$('.sign_vfct').hide();
	$('.sign_shade').hide();

}
});

