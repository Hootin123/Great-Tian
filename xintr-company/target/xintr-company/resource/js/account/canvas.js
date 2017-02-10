function move(obj, json, Boolean,endfun) {
	
	var num = json.start;
	var onoff = true;
	var iSpeed = 12;

	obj.timer = setInterval(function() {
		
		num > json.target ? num += -iSpeed : num += iSpeed;
		
		if(Math.abs(json.target - num)<iSpeed){
			canvas({
			'beginRadian': Math.PI / 180 * json.start,
			'overRadin': Math.PI / 180 * json.target
			});
			clearInterval(obj.timer);
			endfun && endfun();
		}else{
			canvas({
			'beginRadian': Math.PI / 180 * json.start,
			'overRadin': Math.PI / 180 * num
			});	
		}

	}, 13);

	function canvas(radin) {

		var context = obj.getContext('2d');
		context.clearRect(0, 0, obj.width, obj.height);
		/*绘制外圈圆*/
		context.beginPath();
		context.arc(obj.width / 2, (obj.height / 2), 130, 0, Math.PI * 2, Boolean);
		context.lineWidth = 40;
		context.strokeStyle = '#dddddd';
		context.stroke();
		context.closePath();
		/*绘制内圆圈*/
		context.beginPath();
		var mColor = context.createLinearGradient(90, 345, 345, 90);
		mColor.addColorStop(0, json.beginColor);
		mColor.addColorStop(1, json.overColor);
		context.strokeStyle = mColor;
		context.lineWidth = 40;
		context.lineCap = 'round';
		context.arc(obj.width / 2, (obj.height / 2), 130, radin.beginRadian, radin.overRadin, Boolean);
		context.stroke();
		context.closePath();
	}
	
}