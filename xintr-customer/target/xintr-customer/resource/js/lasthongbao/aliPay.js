$(function(){
	var fliveName=false,fnumber=false;

//......... 姓名
	$('.liveUserName input').blur(function(){
		testLiveUsername()
	})

	function testLiveUsername(){
		var reg=/^[\w\u4e00-\u9fa5]{2,20}$/
		if($(".liveUserName input").val()=='' || $(".liveUserName input").val()==undefined ){
			$('.liveUserName b').html('请输入姓名');
			fliveName = false
		}
		else if(reg.test($("#username input").val())){
			$('.liveUserName b').html('');
			fliveName=true;
		}
		else{
			$('.liveUserName b').html('请输入正确的姓名');
			fliveName = false
		}
	}

	//......... 支付宝账号

	$('.aliPayNumber input').blur(function(){
		testAliPayNumber()
	});

	function testAliPayNumber(){
		reg1=/^1[3|5|7|8]\d{9}$/;
		reg2=/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/ ;
		if($(".aliPayNumber input").val()=='' || $(".aliPayNumber input").val()==undefined ){
			$('.aliPayNumber b').html('请输入支付宝账号');
			fnumber = false
		}
	 	else if(reg1.test($(".aliPayNumber input").val()) || reg2.test($(".aliPayNumber input").val())){
	 		$('.aliPayNumber b').html('');
			fnumber = true;
		}
		else{
			$('.aliPayNumber b').html('请输入正确支付宝账号');
			fnumber = false

		}
	};

	// 提交
	$(".submit").on('touchstart',function(){
		 	testLiveUsername()
			testAliPayNumber()
		if(fliveName&&fnumber){

			$.ajax({
				url:BASE_PATH+'/lastHongbao/infoCollectSubmit.htm',
				data:$("#collectForm").serialize(),
				dataType:'json',
				type:'post',
				success:function(data){
					if(data.success){
						var map = data.data;
						var perInfo = map.perInfo;
						if(perInfo=="wait"){
							//正在审核
							window.location=BASE_PATH+"/lastHongbao/jumpToPromptWait.htm";
						}else if(perInfo=="noPass"){
							//审核未通过
							var reason = map.reason;
							var companyId = map.companyId;
							window.location =BASE_PATH+"/lastHongbao/jumpToPromptDefeat.htm?reason="+reason+"&companyId="+companyId;
						}else if(perInfo=="pass"){
							//审核通过
							window.location = BASE_PATH+"/lastHongbao/jumpToPromptSuccess.htm"
						}
					}else{
						alert(data.message);
					}
				},
				error:function(){
					layer.alert("网络错误");
				}
			});

		}
	})
})