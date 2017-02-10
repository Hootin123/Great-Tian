$(function(){

	//初始化赋值
	$(".checkt1").html($("#companyScale").val());
	$(".checkt2").html($("#companyBelongIndustry").val());
	//$(".checkt3").html($("#companyChannel").val());

var fComname=false,faddress=false,fnumber = false,fnumber = false,fphone = false;
		//...............公司名称验证

	$('#companyName input').blur(function(){
		testCompanyName()
	})
	function testCompanyName(){
		var reg=/^[\w\u4e00-\u9fa5]{4,20}$/;
		if($("#companyName input").val()=='' || $("#companyName input").val()==undefined ){
			$('#companyName b').html('公司名称不能为空');
			$('#companyName i').removeClass('show')
			fComname=false
		}
		else if(reg.test($("#companyName input").val())){
			$('#companyName b').html('');
			$('#companyName i').addClass('show');
			fComname=true;
		}
		else{
			$('#companyName b').html('请输入正确的公司名称');
			$('#companyName i').removeClass('show');
			fComname=false
		}
	}
	//........上传图片格式验证


	//.......公司地址验证
	$('#companyAddress input').blur(function(){
		testCompAddress()
	})


	function testCompAddress(){
		var reg=/^[\w\u4e00-\u9fa5]{2,20}$/
		if($("#companyAddress input").val()=='' || $("#companyAddress input").val()==undefined ){
			$('#compAddress b').html('请输入公司地址');
			$('#compAddress i').removeClass('show');
			faddress=false
		}
		else if(reg.test($("#companyAddress input").val())){
			$('#companyAddress i').addClass('show')
			$('#companyAddress b').html('');
			faddress=true;
		}
		else{
			$('#companyAddress b').html('请输入正确的公司地址');
			$('#companyAddress i').removeClass('show');
			faddress=false
		}
	}

	//............验证营业执照编号
	$('#companyNumber input').blur(function(){
		testCompanyNum()
	});

	function testCompanyNum(){
		var  reg=/^\d{15,18}$/;
		if($("#companyNumber input").val()=='' || $("#companyNumber input").val()==undefined ){
			$('#companyNumber b').html('请输入营业执照编号');
			$('#companyNumber i').removeClass('show');
			fnumber = false

		}
	 	else if(reg.test($("#companyNumber input").val())){
	 		$('#companyNumber b').html('');
	 		$('#companyNumber i').addClass('show')
			fnumber = true;
		}
		else{
			$('#companyNumber b').html('请输入正确的营业执照编号');
			$('#companyNumber i').removeClass('show');
			fnumber = false
		}
	};

	
	//.......验证手机号
	
	$('#linkPhone input').blur(function(){
		testLinkPhone()
	});

	function testLinkPhone(){
		var  reg=/^1[3|5|7|8]\d{9}$/;
		if($("#linkPhone input").val()=='' || $("#linkPhone input").val()==undefined ){
			$('#linkPhone b').html('请输入联系电话');
			$('#linkPhone i').removeClass('show');
			fphone = false
		}
	 	else if(reg.test($("#linkPhone input").val())){
	 		$('#linkPhone b').html('');
	 		$('#linkPhone i').addClass('show')
			fphone = true;
		}
		else{
			$('#linkPhone b').html('请输入正确的联系电话！')
			$('#linkPhone i').removeClass('show');
			fphone = false
		}
	};
//...........验证渠道是否为空
var channelFlag=false;
function testChannel(){
	var companyChannel =$("#companyChannel").val();
	if(companyChannel==null||companyChannel==""||companyChannel==undefined){
		$('#channel b').html('请选择了解渠道');
		 channelFlag =false;
	}else{
		$('#channel b').html("");
		channelFlag = true;
	}

}



	//.......公司规模选择
	$('.checkt1').on('click',function(){
		$('#show1').show();
		$('#show2').hide();
		$('#show3').hide();
		$('#show1 li').on('click',function(){
			$(this).css('background','#0696fa').siblings().css('background','none')
		});
		$('#show1 li').on('click',function(){
			var content=$(this).html();
			$('.checkt1').html(content);
			$("#companyScale").val(content);
			$('#show1').hide();
		})
	});


	//.......行业选择
	$('.checkt2').on('click',function(){
		$('#show1').hide();
		$('#show3').hide();
		$('#show2').show();
		$('#show2 li').on('click',function(){
			$(this).css('background','#0696fa').siblings().css('background','none')
		});
		$('#show2 li').on('click',function(){
			var content=$(this).html();
			$('.checkt2').html(content);
			$("#companyBelongIndustry").val(content);
			$('#show2').hide();
		})
	});

	//.......渠道选择
	$('.checkt3').on('click',function(){
		$('#show1').hide();
		$('#show2').hide();
		$('#show3').show();
		$('#show3 li').on('click',function(){
			$(this).css('background','#0696fa').siblings().css('background','none')
		});
		$('#show3 li').on('click',function(){
			var content=$(this).attr("inde");
			$('.checkt3').html($(this).html());
			$("#companyChannel").val(content);
			$('#show3').hide();
		})
	});


 	



   //上传公司logo
    $("#companyLogoFile").on('change',uploadLogoImg);
	//上传公司的营业执照
	$("#companyOrganizationImgFile").on('change',uploadOrganizationImg);

	// 提交审核
	$('.submit').on('click',function(){
		testCompanyName();
		testCompAddress();
		testCompanyNum();
		testLinkPhone();
		testChannel();
		if(fComname&&faddress&&fnumber&&fnumber&&fphone&&channelFlag){
			//提交审核过后 跳转到目录页
			$.ajax({
				url: BASE_PATH+'/h5Hongbao/perfectInformation.htm',
				data:$("#perfectInfoForm").serialize(),
				dataType:'json',
				type:'post',
				success:function(data){
					if(data.success){
						//跳转到目录页面
						window.location=BASE_PATH+'/h5Hongbao/toMenuPage.htm';
					}else{
						layer.alert(data.message);
					}
				}

			});

		}
	})


})


//上传公司logo
function uploadLogoImg(){
	var a = BASE_PATH+ "/resource/img/h5wallet/load.gif";
	$("#uploadLogo").attr('src',a);

	$.ajaxFileUpload({

		url:  BASE_PATH+'/h5Hongbao/h5HongbaoUploadOrganizeImg.htm', //用于文件上传的服务器端请求地址
		fileElementId: 'companyLogoFile', //文件上传域的ID
		dataType: 'json', //返回值类型 一般设置为json
		success: function (data, status){
			if(data.success) {
				$("#uploadLogo").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
				$("[name=companyLogo]").val(data.data);
				//console.log(data.data);
				$(".hint").text("");
				$(".hint").css("display","none");
				fimg=true;

			}else{
				$(".hint").text(data.message);
				$(".hint").css("display","block");
				fimg=false;
			}
			$("#companyLogoFile").unbind('change').bind('change',uploadLogoImg);
		},
		error: function (data, status, e){
			$(".hint").text("上传失败");
			$(".hint").css("display","block");
			fimg=false;
			$("#companyLogoFile").unbind('change').bind('change',uploadLogoImg);
		}
	});
}
//上传公司营业执照
function uploadOrganizationImg(){
	var a = BASE_PATH+ "/resource/img/h5wallet/load.gif";
	$("#uploadOrgImage").attr('src',a);
	$.ajaxFileUpload({
		url:  BASE_PATH+'/h5Hongbao/h5HongbaoUploadOrganizeImg.htm', //用于文件上传的服务器端请求地址
		fileElementId: 'companyOrganizationImgFile', //文件上传域的ID
		dataType: 'json', //返回值类型 一般设置为json
		success: function (data, status){
			if(data.success) {
				$("#uploadOrgImage").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
				$("[name=companyOrganizationImg]").val(data.data);
				//console.log(data.data);
				$(".hint").text("");
				$(".hint").css("display","none");
				fimg=true;

			}else{
				$(".hint").text(data.message);
				$(".hint").css("display","block");
				fimg=false;
			}
			$("#companyOrganizationImgFile").unbind('change').bind('change',uploadOrganizationImg);
		},
		error: function (data, status, e){
			$(".hint").text("上传失败");
			$(".hint").css("display","block");
			fimg=false;
			$("#companyOrganizationImgFile").unbind('change').bind('change',uploadOrganizationImg);
		}
	});
}