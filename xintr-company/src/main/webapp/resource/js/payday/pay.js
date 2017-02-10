$(function(){

	$('.identity-span').eq(0).click(function(){
		$('.identity-tab ol').hide();
		$('.identity-tab1 ol').show();
		return false;
	})


	$('.identity-span').eq(1).click(function(){
		$('.identity-tab ol').hide();
		$('.identity-tab2 ol').show();
		return false;
	})

	$('.identity-span').eq(2).click(function(){
		$('.identity-tab ol').hide();
		$('.identity-tab3 ol').show();
		return false;
	})

	$('body').click(function(){
		$('.identity-tab ol').hide();
	})

	/*变色*/
	$('.identity-tab ol li').mouseover(function () {
		$('.identity-tab ol li').css({'background':'#ffffff','color':'#333333'});
		$(this).css({'background':'#0696fa','color':'#ffffff'});
	})


	/*点击li关闭ol*/
	$('.identity-tab1 ol li').click(function(){
		var oVal = $(this).html();
		$('.identity-tab1 span').eq(0).html(oVal);
		$('.identity-tab1 ol').hide();
		$('.identity-hiden').eq(0).val(oVal);
	})

	$('.identity-tab2 ol li').click(function(){
		var oVal = $(this).html();
		$('.identity-tab2 span').eq(0).html(oVal);
		$('.identity-tab2 ol').hide();
		$('.identity-hiden').eq(1).val(oVal);
	})



	//$('.identity-tab2 ol li').click(function(){
	//	var oVal = $(this).html();
	//	$('.identity-tab2 span').eq(0).html(oVal);
	//	$('.identity-tab2 ol').hide();
	//	$('.identity-hiden').eq(2).val(oVal)
	//})

	$('.identity-tab3 ol li').click(function(){
		var oVal = $(this).html();
		$('.identity-tab3 span').eq(0).html(oVal);
		$('.identity-tab3 ol').hide();
		$('.identity-hiden').eq(2).val($(this).attr('inde'))

	})


	/*数据回传*/


	/*数据回传*/

	if($('#companyScale').val()){
		$('.identity-span').eq(0).html($('#companyScale').val());

	}else{
		$('.identity-span').eq(0).html('请选择');

	}
	$('.identity-tab2 span').eq(0).html('请选择');
	if($('#companyBelongIndustry').val()){
		$('.identity-span').eq(1).html($('#companyBelongIndustry').val());

	}else{
		$('.identity-span').eq(1).html('请选择');
	}

	if($('#companyChannel').val()){
		var oVal1 = $('.identity-tab3 ol li').eq($('#companyChannel').val() - 1).html();
	}else {
		var oVal1 = '请选择';
	}

	$('.identity-tab3 span').eq(0).html(oVal1);

})
