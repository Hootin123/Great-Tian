$(function() {
	/*选项卡样式变化*/
	$('#mainlful li').click(function() {
		var a = $(this).attr('data-url');
		$('#J_iframe').attr('src', a);
	});

	$('#mainlful li').attr('index', -1);
	$('#mainlful li').eq(0).attr('index', 1);
	$('#mainlful li').click(function() {
		$('#mainlful li').attr('index', -1);
		$(this).attr('index', 1);
		for(var i = 0; i < $('#mainlful li').length; i++) {
			var mysrc = $('#mainlful li').eq(i).find('img').attr('src');
			var mYsrc = mysrc.substring(mysrc.length - 6, mysrc.length - 4);
			if(mYsrc > 1) {
				mysrc = mysrc.replace(mYsrc, mYsrc.charAt(0));
				$('#mainlful li').eq(i).find('img').attr('src', mysrc);
			}
		}

		$('#mainlful li').removeClass('mulactive');
		oMysrc.call(this, false);

	});

	$('#mainlful li').mouseover(function() {
		if($(this).attr('index') == -1) {
			oMysrc.call(this, true);
		}

	});

	$('#mainlful li').mouseout(function() {
		if($(this).attr('index') == -1) {
			oMysrc.call(this, true);
		}

	});

	function oMysrc(boolen) {

		var mysrc = $(this).find('img').attr('src');
		var mYsrc = mysrc.substring(mysrc.length - 6, mysrc.length - 4);
		if(mYsrc > 1 && boolen) {
			mysrc = mysrc.replace(mYsrc, mYsrc.charAt(0));
			//consoleconsole.log(mysrc);
			$(this).removeClass('mulactive');
		} else {

			mYsrc = mYsrc.replace(/[a-z]/gi, '');
			if(mYsrc.length >= 2) {
				var mYsrc1 = mYsrc.charAt(0);
				mysrc = mysrc.replace(mYsrc, mYsrc1 + mYsrc1);
			} else {
				mysrc = mysrc.replace(mYsrc, mYsrc + mYsrc);
			}

			$(this).addClass('mulactive');
		}
		$(this).find('img').attr('src', mysrc);

	}

	/*end*/

	//$('#mainlful li').mouseout(function() {
	//		if($(this).prop('onoff') == true) {
	//			$(this).find('img').attr('src', BASE_PATH + '/resource/' + aSrc[$(this).index()]);
	//			$(this).removeClass('mulactive');
	//		}
	//	});
	/*end*/

	/*实名认证*/
	$('.inxtest').click(function() {
		$('#J_iframe').attr('src', $(this).attr('data-url'))
	});
	/*end*/


	/*消息内容动态添加*/
	/*var newjson = ['您好，您50000元提现至**** **** **** 9890账户的申请已处理成功，请耐心等待银行处理。',
	 '您好，您50000元提现至**** **** **** 9891账户的申请已处理成功，请耐心等待银行处理。  ',
	 '您好，您50000元提现至**** **** **** 9892账户的申请已处理成功，请耐心等待银行处理。  ',
	 '您好，您50000元提现至**** **** **** 9893账户的申请已处理成功，请耐心等待银行处理。  ',
	 '您好，您50000元提现至**** **** **** 9894账户的申请已处理成功，请耐心等待银行处理。  ',
	 '您好，您50000元提现至**** **** **** 9895账户的申请已处理成功，请耐心等待银行处理。  '
	 ]*/
    //
	function loadNewMsg(){
		$.ajax({
			type: 'post',
			url: BASE_PATH + "/message/all",
			success: function (data) {
				if (data.success && data["data"].length>0) {
					var obj = new iScroll([]);
					obj.newata(data["data"]);
				} else {
					$('.oveFlow').hide();
				}
			}
		});
	}
	//loadNewMsg();
	/*end*/

	var memberDd = $('.member dd');

	memberDd.click(function() {

		memberDd.removeClass('dd_active');
		$(this).addClass('dd_active');

	});

	onWinResize();
	$('#main_left_ul li').click(function() {

	});

	$('.insclos').click(function() {
		$('.indexshade').css('display', 'none');
		$('.indexbox').css('display', 'none')
	});

	/*大弹窗*/

	$('.popupbox-payfor').click(function(){
		document.getElementById('popupbox').innerHTML = '';

		$('.popupbox').show(0,function(){
			$('.popupbox').load('payforInfo.htm?type=info',function () {

				popupboxclosed($('.popupbox'))

			});
		})
	});





	/*end*/


});

window.onload = function() {
	(function() {
		window.onresize = function() {
			onWinResize();
		}
	})();
};

function onWinResize() {
	var height = $(window).height() - 60 - 70 - 60; //中间部分减去头部和顶部
	var width = $(window).width();

	if(height <= 740) {
		height = 740;
	}
	if(width < 950) {
		width = 950
	}

	$('.right').css({
		'width': width - 200,
		'height': height + 60
	});

	$('#mainlful').css('height', height);
	$('.indexshade').click('width', width);
	$('.popupbox').css('height',$(document).height());
}

function allPop(url,callback){
	document.getElementById('popupbox').innerHTML = '';
	$('.popupbox').show(0,function(){
		$('.popupbox').load(url,function () {
			popupboxclosed($('.popupbox'),callback)
		});
	})
}

function allPopClose(){
	$('.popupbox').hide();

}

function popupboxclosed(elem){
	var oI = document.createElement('i');
	oI.className = 'popupbox-closed';
	elem.append(oI);
	$('.popupbox-closed').click(function () {
		// callback();
		allPopClose();

	});

	$('.obtnclose').click(function () {
		$('.popupbox').hide()
	});
}
