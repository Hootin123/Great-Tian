$(function(){
		// 下拉列表
    // 选择公司规模
    $('.companyScale').on('tap',' input',function(e){
        e.stopPropagation()
        $('.list1').show();
        $('.list2').hide();
        $('.list3').hide();
    });
    $('.list1').on('tap','p',function(){
        
        $('.companyScale input').val($(this).html());

        $('.list1').hide();
    })

    $(document).on('tap',function(){
        $('.list1').hide();
    })
    $('.list1').on('tap',function(e){
        e.stopPropagation()
    })

     // 选择行业
    $('.industry').on('touchstart','input',function(e){
        e.stopPropagation()
        $('.list2').show();
        $('.list1').hide();
        $('.list3').hide();
    });
    $('.list2 ').on('tap','p',function(){
        
        $('.industry input').val($(this).html());
        $('.list2').hide();
    })

    $(document).on('tap',function(){
        $('.list2').hide();
    })
    $('.list2').on('tap',function(e){
        e.stopPropagation()
    })
    // 了解渠道
     $('.ditch ').on('touchstart','input',function(e){
        e.stopPropagation()
        $('.list3').show();
        $('.list1').hide();
        $('.list2').hide();
    });
    $('.list3 ').on('tap','p',function(){
        $('#companyChannel').val($(this).html());
        $('.list3').hide();
        // $('#companyChannel').css('border-radius','20px')
        //隐藏域赋值
        $("input[name='companyChannel']").val($(this).attr('index'));
    })

    $(document).on('tap',function(){
        $('.list3').hide();
    })
    $('.list3').on('tap',function(e){
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
        var  reg=/^\d{18}$/;
        var busValue=$(".businessLicenseNumber input").val();
        if($(".businessLicenseNumber input").val()=='' || $(".businessLicenseNumber input").val()==undefined ){
            $('.businessLicenseNumber b').html('请输入营业执照');
            faccount=false;
        }
        else if(busValue.length>=15&&busValue<=18){
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
          $('.businessLicensePic b').html('请上传营业执照');
            fimg=false;
        }else{
           $('.businessLicensePic b').html('');
            fimg=true;
        }
    }
    // 验证法人代表
     $('.legalPerson input').blur(function(){
        testLegalPerson();
    });

    function testLegalPerson(){
         if($(".legalPerson input").val().length>=0&& $(".legalPerson input").val().length<=20){
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
    // 验证了解渠道
     function testDitch(){
        if($(".ditch input").val()=='' || $(".ditch input").val()==undefined ){
          $('.ditch b').html('请选择渠道！');
            fditch=false;
        }else{
           $('.ditch b').html('');
            fditch=true;
        }
    }



    //上传公司logo
    $("#companyLogoFile").on('change',uploadLogoImg);
    //上传公司的营业执照
    $("#companyOrganizationImgFile").on('change',uploadOrganizationImg);


    //取消
    $(".companyClose").click(function(){
        history.back();
    });

    // 提交验证
    $('.companyBtn').on('touchstart',function(){
    	testCompanyName();
    	testCompanyAddress();
    	testBusNum();
    	testImg();
    	testLegalPerson();
    	testLinkPhone();
        testDitch()
    	if(fcopname && faddress && fphone && fimg && fpresonal && fditch){
            $.ajax({
                url:BASE_PATH+'/lastHongbao/activityApprove.htm',
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
                            window.location=BASE_PATH+"/lastHongbao/jumpToWebCollectionInfoPage.htm?companyId="+companyId
                        }else{
                            //跳转至待审核页面
                            window.location =BASE_PATH+"/lastHongbao/jumpToPromptWait.htm"
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
    })
})


//上传公司logo
function uploadLogoImg(){
    var a = BASE_PATH+ "/resource/img/h5wallet/load.gif";
    $("#uploadLogo").attr('src',a);
    $.ajaxFileUpload({
        url:BASE_PATH+'/lastHongbao/uploadFile.htm', //用于文件上传的服务器端请求地址
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
        url:  BASE_PATH+'/lastHongbao/uploadFile.htm', //用于文件上传的服务器端请求地址
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
