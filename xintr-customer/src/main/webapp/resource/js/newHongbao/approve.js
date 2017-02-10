$(function(){
	var fcopname = false,faddress = false,fphone = false,fimg=false,fpresonal=false;

	// 公司名称y验证
	$('.companyName input').blur(function(){
		testCompanyName()
	})
	function testCompanyName(){
		if($(".companyName input").val()=='' || $(".companyName input").val()==undefined ){
			$('.companyName b').html('请输入4-20位的公司名称');
			fcopname = false
		}
		else if($(".companyName input").val().length>=4&& $(".companyName input").val().length<=20){
			$('.companyName b').html('');
			fcopname=true;
		}
		else{
			$('.companyName b').html('请输入正确的公司名称');
			fcopname = false
		}
	}

	// 验证公司地址
	$('.companyAddress input').blur(function(){
		testCompanyAddress()
	})
	function testCompanyAddress(){
		if($(".companyAddress input").val()=='' || $(".companyAddress input").val()==undefined ){
			$('.companyAddress b').html('请输入公司地址');
			faddress = false
		}
		else if($(".companyAddress input").val().length>=1&& $(".companyAddress input").val().length<=50){
			$('.companyAddress b').html('');
			faddress=true;
		}
		else{
			$('.companyAddress b').html('请输入正确的公司地址');
			faddress = false
		}
	}

	// 验证营业执照编号
	 $('.businessLicenseNumber input').blur(function(){
        testBusNum();
    });

    function testBusNum(){
        // var  reg=/^\d{15}$/;
        if($(".businessLicenseNumber input").val()=='' || $(".businessLicenseNumber input").val()==undefined ){
            $('.businessLicenseNumber b').html('请输入营业执照');
            faccount=false;
        }
        else if($(".businessLicenseNumber input").val().length>=15&&$(".businessLicenseNumber input").val().length<=18){
            $('.businessLicenseNumber b').html('');
            faccount = true;
        }
        else{
            $('.businessLicenseNumber b').html('请输入正确的营业执照');
            faccount=false;
        }
    }

    //验证上传图片
    function testImg(){
        if($("[name=companyOrganizationImg]").val()=='' || $("[name=companyOrganizationImg]").val()==undefined ){
            $(".hint").text("请上传营业执照");
            $(".hint").css("display","block");
            fimg=false;
        }else{
            $(".hint").text("");
            $(".hint").css("display","none");
            fimg=true;
        }
    }
    // 验证法人代表
     $('.legalPerson input').blur(function(){
        testLegalPerson();
    });

    function testLegalPerson(){
        if($(".legalPerson input").val()=='' || $(".legalPerson input").val()==undefined ){
			$('.legalPerson b').html('请输入法人代表');
			fpresonal = false
		}
		else if($(".legalPerson input").val().length>=1&& $(".legalPerson input").val().length<=50){
			$('.legalPerson b').html('');
			fpresonal=true;
		}
		else{
			$('.legalPerson b').html('请输入正确的法人代表');
			fpresonal = false
		}
    };

    //........联系电话
    $('.legalPhone input').blur(function(){
        testLinkPhone();
    });

    function testLinkPhone(){
        var  reg=/^1[3|5|7|8]\d{9}$/;
        if($(".legalPhone input").val()=='' || $(".legalPhone input").val()==undefined ){
            $('.legalPhone b').html('请输入联系电话');
            fphone=false;
        }
        else if(reg.test($(".legalPhone input").val())){
            $('.legalPhone b').html('');
            fphone = true;
        }
        else{
            $('.legalPhone b').html('请输入正确的联系电话');
            fphone=false;
        }
    }



	//上传公司logo
	$("#companyLogoFile").on('change',uploadLogoImg);
	//上传公司的营业执照
	$("#companyOrganizationImgFile").on('change',uploadOrganizationImg);


    // 提交验证
    $('.subApprove').on('click',function(){
    	testCompanyName();
    	testCompanyAddress();
    	testBusNum();
    	testImg();
    	testLegalPerson();
    	testLinkPhone();
    	if(fcopname && faddress && fphone && fimg && fpresonal){
    		//提交认证信息
			$.ajax({
				url:BASE_PATH+'/newHongbao/activityApprove.htm',
				data:$('#companyForm').serialize(),
				dataType:'json',
				type:'post',
				success:function(data){
                     if(data.success){
                        //跳转至信息收集页面
						 var companyId = data.data.companyId;
						 var collectInfoStatus = data.data.isCollect;
						 if(collectInfoStatus!=1){
							 //跳转至信息收集页面
							 window.location=BASE_PATH+"/newHongbao/infoCollect.htm?companyId="+companyId
						 }else{
                             //跳转至待审核页面
							 window.location =BASE_PATH+"/newHongbao/toPromptWait.htm"
						 }
					 }
				},
				error:function(){
					layer.alert("网络错误");
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
		url:BASE_PATH+'/newHongbao/uploadFile.htm', //用于文件上传的服务器端请求地址
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
		url:  BASE_PATH+'/newHongbao/uploadFile.htm', //用于文件上传的服务器端请求地址
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