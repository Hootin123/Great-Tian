Date.prototype.format = function(fmt)
{ //author: meizz
	var o = {
		"M+" : this.getMonth()+1,                 //月份
		"d+" : this.getDate(),                    //日
		"h+" : this.getHours(),                   //小时
		"m+" : this.getMinutes(),                 //分
		"s+" : this.getSeconds(),                 //秒
		"q+" : Math.floor((this.getMonth()+3)/3), //季度
		"S"  : this.getMilliseconds()             //毫秒
	};
	if(/(y+)/.test(fmt))
		fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	for(var k in o)
		if(new RegExp("("+ k +")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
	return fmt;
}
function iScroll(data) {
	var _this = this;
	this.html = '';
	this.time = '';
	this.newata(data); //加入数据
	this.timer = null;
	this.num = 0;
	this.num1 = 0;
	this.oveF = $('.oveFlow');
	this.aLi = $('.newlist li');
	this.oLi = $('.newlist li').eq(0);
	this.oUl = $('.newlist');
	this.run(); //运行run
	this.oveF.mouseover(function() {
		_this.mouseover();
	})
	this.oveF.mouseout(function() {
		_this.mouseout();
	})

}

iScroll.prototype = {

	'newata': function(data) {

		for(var i = 0; i < data.length; i++) {

			this.html += '<li title="'+data[i]["msgCont"]+'"><p style="height: 30px;line-height: 30px;overflow: hidden;">' + data[i]["msgCont"] + '</p><a>' + new Date(data[i]["msgAddtime"]).format("yyyy-MM-dd") + '</a></li>';
		}
		$('.newlist').html(this.html);
		$('.ilst em').html(data.length);

	},
	'run': function() {

		var _this = this;
		clearInterval(this.timer);
		$('.newlist li').addClass('nlfst');
		this.timer = setInterval(function() {
			_this.oRolling();
		}, 2000)

	},
	'oRolling': function() {

		var _this = this;

		if(this.num == this.aLi.length - 1) {

			this.oLi.css({
				'position': 'relative',
				'top': this.aLi.length * 50
			})
			this.num = 0;

		} else {
			this.num++;
		}

		this.num1++;

		this.oUl.animate({
			'top': -50 * this.num1
		}, function() {
			if(_this.num == 0) {

				_this.oLi.css({
					'position': 'static'
				});
				_this.oUl.css('top', 0);
				_this.num1 = 0;
			}
		})
	},

	'mouseover': function() {
		this.oveF.css({
			'height': 600
		})
		this.aLi.removeClass('nlfst');
		clearInterval(this.timer);
		this.oUl.css('top', 0);
		this.oLi.addClass('nlfst');
		this.num = 0;
		this.num1 = 0;
	},

	'mouseout': function() {
		this.oveF.css({
			'height': 50
		})
		this.run();
	}

}

