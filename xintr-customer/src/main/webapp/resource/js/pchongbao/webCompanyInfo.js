$(function(){

    // 下拉列表
    // 选择公司规模
    $('.companyScale input').on('click',function(e){
        e.stopPropagation()
        $('.list1').show();
        $('.list2').hide();
        $('.list3').hide();
        $('.companyScale input').css('border-radius','25px 25px 0 0')
    });
    $('.list1 p').on('click',function(){
        
        $('.companyScale input').val($(this).html());
        $('.list1').hide();
        $('.companyScale input').css('border-radius','20px')
    })

    $(document).on('click',function(){
        $('.list1').hide();
        $('.companyScale input').css('border-radius','20px')
    })
    $('.list1').on('click',function(e){
        e.stopPropagation()
    })
    // 选择行业
    $('.industry input').on('click',function(e){
        e.stopPropagation()
        $('.list2').show();
        $('.list1').hide();
        $('.list3').hide();
        $('.industry input').css('border-radius','25px 25px 0 0')
    });
    $('.list2 p').on('click',function(){

        $('.industry input').val($(this).html());
        $('.list2').hide();
        $('.industry input').css('border-radius','20px')
    })

    $(document).on('click',function(){
        $('.list2').hide();
        $('.industry input').css('border-radius','20px')
    })
    $('.list2').on('click',function(e){
        e.stopPropagation()
    })
    // 了解渠道
     $('.ditch input').on('click',function(e){
        e.stopPropagation()
        $('.list3').show();
        $('.list1').hide();
        $('.list2').hide();
        $('.ditch input').css('border-radius','25px 25px 0 0')
    });
    $('.list3 p').on('click',function(){
        
        $('#companyChannel').val($(this).html());
        $('.list3').hide();
        $('#companyChannel').css('border-radius','20px')
        //隐藏域赋值
        $("input[name='companyChannel']").val($(this).attr('index'));
    })

    $(document).on('click',function(){
        $('.list3').hide();
        $('.ditch input').css('border-radius','20px')
    })
    $('.list3').on('click',function(e){
        e.stopPropagation()
    })
    //..........验证

	var fcopname = false,faddress = false,fphone = false,fimg=false,fpresonal=false,fditch=false;
	// 公司名称验证

	$('.companyName input').blur(function(){
		testCompanyName()
	})
	function testCompanyName(){
		if($(".companyName input").val()=='' || $(".companyName input").val()==undefined ){
			$('.companyName b').addClass('error').html('请输入4-20位的公司名称').show();
            setTimeout(function(){
                    $('.companyName b').fadeOut(500)
                },1000);
			fcopname = false
		}
		else if($(".companyName input").val().length>=4&& $(".companyName input").val().length<=20){
            // alert($(".companyName input").val().length)
			$('.companyName b').removeClass('error').html('').hide();
			fcopname=true;
		}
		else{
			$('.companyName b').addClass('error').html('请输入正确的公司名称');
            setTimeout(function(){
                    $('.companyName b').fadeOut(500)
                },1000);
			fcopname = false
		}
	}

	// 验证公司地址
	$('.companyAddress input').blur(function(){
		testCompanyAddress()
	})
	function testCompanyAddress(){
		if($(".companyAddress input").val()=='' || $(".companyAddress input").val()==undefined ){
			$('.companyAddress b').addClass('error').html('请输入公司地址').show();
             setTimeout(function(){
                    $('.companyAddress b').fadeOut(500)
                },1000);
			faddress = false
		}
		else if($(".companyAddress input").val().length>=1&& $(".companyAddress input").val().length<=50){
			$('.companyAddress b').removeClass('error').html('').hide();
			faddress=true;
		}
		else{
			$('.companyAddress b').addClass('error').html('请输入正确的公司地址').show();
             setTimeout(function(){
                    $('.companyAddress b').fadeOut(500)
                },1000);
			faddress = false
		}
	}

	// 验证营业执照编号
	 $('.businessLicenseNumber input').blur(function(){
        testBusNum();
    });
    //营业执照15-18位
    function testBusNum(){
        var number = $(".businessLicenseNumber input").val();
        if(number=='' || number==undefined||number==null){
            $('.businessLicenseNumber b').addClass('error').html('请输入营业执照').show();
            setTimeout(function(){
                    $('.businessLicenseNumber b').fadeOut(500)
                },1000);
            faccount=false;
        }
        else if(number.length>=15&&number.length<=18){
            $('.businessLicenseNumber b').removeClass('error').html('').hide();
            faccount = true;
        }
        else{
            $('.businessLicenseNumber b').addClass('error').html('请输入正确的营业执照').show();
            setTimeout(function(){
                    $('.businessLicenseNumber b').fadeOut(500)
                },1000);
            faccount=false;
        }
    }

    //验证上传图片
    function testImg(){
        if($("[name=companyOrganizationImg]").val()=='' || $("[name=companyOrganizationImg]").val()==undefined ){
          $('.businessLicensePic b').addClass('error').html('请上传营业执照').show();
            setTimeout(function(){
                    $('.businessLicensePic b').fadeOut(500)
                },1000);
            fimg=false;
        }else{
           $('.businessLicensePic b').removeClass('error').html('').hide();
            fimg=true;
        }
    }
    // 验证法人代表
     $('.legalPerson input').blur(function(){
        testLegalPerson();
    });

    function testLegalPerson(){
         if($(".legalPerson input").val().length>=0&& $(".legalPerson input").val().length<=20){
			$('.legalPerson b').removeClass('error').html('').hide();
			fpresonal=true;
		}
		else{
			$('.legalPerson b').addClass('error').html('请输入正确的法人代表').show();
             setTimeout(function(){
                    $('.legalPerson b').fadeOut(500)
                },1000);
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
            $('.legalPhone b').addClass('error').html('请输入联系电话').show();
             setTimeout(function(){
                    $('.legalPhone b').fadeOut(500)
                },1000);
            fphone=false;
        }
        else if(reg.test($(".legalPhone input").val())){
            $('.legalPhone b').removeClass('error').html('').hide();
            fphone = true;
        }
        else{
            $('.legalPhone b').addClass('error').html('请输入正确的联系电话').show();
            setTimeout(function(){
                    $('.legalPhone b').fadeOut(500)
                },1000);
            fphone=false;
        }
    }
    // 验证了解渠道
     function testDitch(){
        if($(".ditch input").val()=='' || $(".ditch input").val()==undefined ){
          $('.ditch b').addClass('error').html('请选择渠道！').show();
            setTimeout(function(){
                    $('.ditch b').fadeOut(500)
                },1000);
            fditch=false;
        }else{
           $('.ditch b').removeClass('error').html('').hide();
            fditch=true;
        }
    }

    // 提交验证
    $('.companyBtn').on('click',function(){
    	testCompanyName();
    	testCompanyAddress();
    	testBusNum();
    	testImg();
    	testLegalPerson();
    	testLinkPhone();
        testDitch()
    	if(fcopname && faddress && fphone && fimg && fpresonal && fditch){
    		//资料提交接口
            $.ajax({
               url:BASE_PATH+'/web/activityApprove.htm',
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
                           window.location=BASE_PATH+"/web/jumpToWebCollectionInfoPage.htm?companyId="+companyId
                       }else{
                           //跳转至待审核页面
                           window.location =BASE_PATH+"/web/jumpToPromptWait.htm"
                       }
                   }else{
                       alert(data.message);
                   }
               },
               error:function(){
                   alert("网络错误，请刷新页面")
               }
            });
    	}
    });


    //上传公司logo
    $("#companyLogoFile").on('change',uploadLogoImg);
    //上传公司的营业执照
    $("#companyOrganizationImgFile").on('change',uploadOrganizationImg);


    //取消
    $(".companyClose").click(function(){
       history.back();
    });

});

//上传公司logo
function uploadLogoImg(){
    var a = BASE_PATH+ "/resource/img/h5wallet/load.gif";
    $("#uploadLogo").attr('src',a);
    $.ajaxFileUpload({
        url:BASE_PATH+'/web/uploadFile.htm', //用于文件上传的服务器端请求地址
        fileElementId: 'companyLogoFile', //文件上传域的ID
        dataType: 'json', //返回值类型 一般设置为json
        success: function (data, status){
            if(data.success) {
                $("#uploadLogo").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
                $("[name=companyLogo]").val(data.data);
                //console.log(data.data);
                fimg=true;

            }else{

                fimg=false;
                alert(data.message);
            }
            $("#companyLogoFile").unbind('change').bind('change',uploadLogoImg);
        },
        error: function (data, status, e){
            fimg=false;
            $("#companyLogoFile").unbind('change').bind('change',uploadLogoImg);
            alert("上传失败")
        }
    });
}

//上传公司营业执照
function uploadOrganizationImg(){
    var a = BASE_PATH+ "/resource/img/h5wallet/load.gif";
    $("#uploadOrgImage").attr('src',a);
    $.ajaxFileUpload({
        url:  BASE_PATH+'/web/uploadFile.htm', //用于文件上传的服务器端请求地址
        fileElementId: 'companyOrganizationImgFile', //文件上传域的ID
        dataType: 'json', //返回值类型 一般设置为json
        success: function (data, status){
            if(data.success) {
                $("#uploadOrgImage").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
                $("[name=companyOrganizationImg]").val(data.data);
                //console.log(data.data);
                fimg=true;
            }else{
                fimg=false;
                alert(data.message);
            }
            $("#companyOrganizationImgFile").unbind('change').bind('change',uploadOrganizationImg);
        },
        error: function (data, status, e){
            fimg=false;
            $("#companyOrganizationImgFile").unbind('change').bind('change',uploadOrganizationImg);
            alert("上传失败");
        }
    });
}



