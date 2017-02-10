function iMove(id) {
	var _this = this;
	this.oDiv = document.getElementById(id);
	this.diX = 0;
	this.diY = 0;

	this.oDiv.onmousedown = function(ev) {
		_this.down(ev);
	}
}

iMove.prototype = {

	constructor: iMove,

	'getStyle': function(obj, attr) {
		return obj.currentStyle ? parseInt(obj.currentStyle[attr]) : parseInt(getComputedStyle(obj)[attr]);
	},

	'down': function(ev) {
		ev = ev || window.event;

		function isIE() { //判断是否ie? ie和谷歌浏览器在ifarme标签下会出现ev.clientX差异的;
			if(!!window.ActiveXObject || "ActiveXObject" in window)
				return true;
			else
				return false;
		}

		this.diX = ev.clientX - this.getStyle(this.oDiv, 'left');
		this.diY = ev.clientY - this.getStyle(this.oDiv, 'top');

		if(isIE() === true && typeof onoff == 'undefined') { //ie浏览器情况下的特殊diX赋值。
			this.diX = this.diX + 200;
			this.diY = this.diY + 75;
			onoff = true;
		} else {
			this.diX = this.diX;
			this.diY = this.diY;
		}
		var _this = this;
		document.onmousemove = function(ev) {
			_this.move(ev);
			return false;
		};
		this.oDiv.onmouseup = function() {
			_this.up();
		}
	},

	'move': function(ev) {
		ev = ev || window.event;
		this.oDiv.style.left = ev.clientX - (this.diX) + 'px';
		this.oDiv.style.top = ev.clientY - this.diY + 'px';
		return false;
	},

	'up': function() {
		document.onmousemove = this.oDiv.onmouseup = null;
	},

	'closeBox': function(id) {
		this.oClose = document.getElementById(id);
		var _this = this;
		this.oClose.onclick = function() {
			//alert($(this))
			_this.oClose.animate({
				'height': 0,
				'top': _this.getStyle(_this.oDiv, 'height') / 2 // + _this.oDiv.position().top
			}, 300, function() {
				_this.oDiv.style.overflow = 'hidden';
				//$('.pacity').css('display', 'none');
			})
		}

	}
};

//var abox = $('.abox');
//
//		abox.find('i').click(function() {
//			closeAbox();
//		})
//
//		function closeAbox() {
//			abox.animate({
//				'height': 0,
//				'top': abox.height() / 2 + abox.position().top
//			}, 300, function() {
//				abox.css('overflow', 'hidden');
//				$('.pacity').css('display', 'none');
//			})
//		}
//
//		$('.cuta').click(function() {
//			$('.pacity').css('display', 'block');
//			$('.aboxbg').css('display', 'block');
//			abox.css({
//				'height': 250,
//				'left': -abox.width() / 2,
//				'top': -125
//			}, 300, function() {
//				abox.css('overflow', 'scroll');
//			})
//		})