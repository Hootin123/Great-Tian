$(function () {
	alert_click();
	ap_select();
	tabsb_click ();
	
	all_check ();
	$(".xtr-cutbar li").click(function () {
		$(this).addClass("active").siblings("li").removeClass("active");
	});
	to_top();
	
	nav_scroll();
	$(window).scroll(function(){
		nav_scroll();
	});
});
function addBlcol() {
	$(".nav li.split").show();
	$(".nav li.user").show();
	$(".nav li.loginout").show();
	$(".nav li.login").show();
	$(".nav li.try").show();
}
function to_top(){
	
	$("#to_top input").click(function () {
		if($(this).val()==$(this).attr("placeholder")){
			$(this).val('').focus();
		}
	});
	
}

function addcob(obj){

	if(obj.attr('ta')=='1')
		return;

	obj.attr('ta','1');
	
	$.ajax({
		type:"POST",  
        url:"addCollaboration.htm",
        dataType:"json",
        data:{
        	'phone':obj.prev().val()
        },
        async: true,
		success: function(data){
			obj.removeAttr('ta');

			if(data==null){
				window.console?window.console.log('data is null or empty'):'';
				return;
			}
			
			if(data.code!='0'){
				
				alert(data.msg);
				return;
			}
			
			alert('处理成功，请等待相关工作人员与您联系');
			
		}
	});
	
}
function nav_scroll(){
	//console.log($(window).scrollTop()+"sdf")
	if($(window).scrollTop()>100){
		if($(".x-nav-wrap").hasClass("nav-fixed")){return;}
		//$(".x-nav-wrap").hide().addClass("nav-fixed").show();//.slideDown(300);css("height","0")
		$("body").css("padding-top","120px");
		$(".x-nav-wrap").hide().addClass("nav-fixed").stop().slideDown(300);
	}else{
		$("body").css("padding-top","0px");
		$(".x-nav-wrap").removeClass("nav-fixed")//.removeAttr("style");
	}
}
function all_check () {
	//全选
	$(".all-check").click(function () {
		$(this).closest("table").find(".all").prop("checked",$(this).prop("checked"))
	});
}
function tabsb_click () {
	$(".tabsb-wrap ul li").click(function(){
		$(this).addClass("active").siblings("li").removeClass("active");
		var p = $(this).parent().parent();
		p.siblings(".tabsb-div").eq($(this).index()).stop().fadeIn(300).siblings(".tabsb-div").hide();
	});
	$(".xtr3-btns .ui-btn").click(function () {
		$(this).addClass("active").siblings(".ui-btn").removeClass("active");
	});
}
function ap_select() {
	$(".apply-select .input-div").click(function () {
		$(this).next().stop().fadeIn(200);
		$(this).closest('.apply-select').addClass("active");
	})
	$(".apply-select ul li").click(function () {
		$(this).parent().hide().prev(".input-div").find("input[type=hidden]").val($(this).attr("val")?$(this).attr("val"):$(this).html());
		$(this).parent().hide().prev(".input-div").find("input[type=text]").val($(this).html());
		$(this).closest('.apply-select').removeClass("active");
	})
}
function container_height(id){
	var i = $(id);
	i.css("height","auto");
	if($("body,html").height()<$(window).height()){
		i.css("height",($(window).height()-$("body,html").height()+i.height())+"px");
	}
}
function alert_click(){
	$(".alert .close").click(function () {
		$(this).closest(".alert").slideUp(300,function () {
			$(this).parent().hide();
		});
	});
}
function isLessIE9(){
	var browser=navigator.appName 
	var b_version=navigator.appVersion 
	var version=b_version.split(";"); 
	var trim_Version;
	try{
		trim_Version =version[1].replace(/[ ]/g,"");
	}catch(e){
		trim_Version =version[0].replace(/[ ]/g,"");
	}
	if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE6.0"
		||browser=="Microsoft Internet Explorer" && trim_Version=="MSIE7.0"
		||browser=="Microsoft Internet Explorer" && trim_Version=="MSIE8.0"){
		return true;
	}
	return false;
}
function isLessIE10(){
	var browser=navigator.appName 
	var b_version=navigator.appVersion 
	var version=b_version.split(";"); 
	var trim_Version;
	try{
		trim_Version =version[1].replace(/[ ]/g,"");
	}catch(e){
		trim_Version =version[0].replace(/[ ]/g,"");
	}
	if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE6.0"
		||browser=="Microsoft Internet Explorer" && trim_Version=="MSIE7.0"
		||browser=="Microsoft Internet Explorer" && trim_Version=="MSIE8.0"
		||browser=="Microsoft Internet Explorer" && trim_Version=="MSIE9.0"){
		return true;
	}
	return false;
}