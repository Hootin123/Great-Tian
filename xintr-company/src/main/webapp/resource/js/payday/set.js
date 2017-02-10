$(function(){
	showPayInfo();
	 fComname = false,faddress = false,fnumber = false,fUsename=false,fphone = false,fname=false;
	//.........table切换
	$('#info').on('click',function(){
		$('.content').show();
		$('.compent').hide();
		$('.log').hide();
		$('.text').show();
		$('.add').hide();
		$('.search').hide();
		$('.sign').hide();
		//$(this).css({'background':'#5b7798','color': '#fff'}).siblings().css({'background':'#fff','color': '#5b7798'});
		$(this).addClass('active').siblings().removeClass('active');
		$("footer").show();
	});
	$('#operat').on('click',function(){
		$('.content').hide();
		$('.compent').show();
		$('.log').hide();
		$('.text').hide();
		$('.add').show();
		$('.search').hide();
		$('.sign').hide();
		//$(this).css({'background':'#5b7798','color': '#fff'}).siblings().css({'background':'#fff','color': '#5b7798'});
		$(this).addClass('active').siblings().removeClass('active');
		//取消按钮
		$("footer").hide();
		//将之前生成的节点取消
		$("#powerFirstChild").nextAll().remove();
		//调用接口
		$.ajax({
			url:BASE_PATH+'/queryPowersManager.htm',
			dataType:'json',
			type:'post',
			success:function(resultResponse){
				if(resultResponse.success){

					var list =resultResponse.data.listCmember;
					var listRole = resultResponse.data.listRole;
					var html5 ="";
					$.each(list,function(i,data){
						var html="";
						var html1 ="";
						var html2="";
						var html3="";
						//判断
						html = "<tr><td>"+(i+1)+"</td><td>"+data.memberName+"</td><td>"+data.memberPhone+"</td>";
						if(data.memberIsdefault==1){
							html1 ="<td>"+resultResponse.data.listRoleStr+"</td><td><div class='state' id='showdiv"+data.memberId+"' onclick=alert('管理员不能关闭')><p value='1'></p></div></td>";
							html2="<td></td></tr>";
							html3 =html1+html2;
						}else{
							html2="<td><span class='amend' onclick=memberUpdate("+data.memberId+","+"'"+data.memberName+"'"+","+"'"+data.idValue+"'"+");>修改</span>"+"<span class='deleat' onclick=memberGetById("+data.memberId+","+"'"+data.memberPhone+"'"+"); >删除</span></td></tr>";
							if(data.memberStatus==1){
								html1="<td>"+data.roleName+"</td><td><div class='state' id='showdiv"+data.memberId+"' onclick='memberOpenButton("+data.memberId+")'><p value='2'></p></div></td>";
							}else{
								html1 ="<td>"+data.roleName+"</td><td><div class='state' style='background:#ccc;' id='showdiv"+data.memberId+"' onclick=memberOpenButton("+data.memberId+")><p style='left:0' value='1'></p></div></td>";
							}
							html3 = html1+html2;
						}

						html5=html+html3;
						$(".compent table").append(html5);
					});

				}else{
					layer.alert("未知错误")
				}
			},
			error:function(){
				layer.alert("网络错误");
			}

		});


	});


    //点击操作日志进行分页查询
	$('#log').on('click',function(){
		$('.content').hide();
		$('.compent').hide();
		$('.log').show();
		$('.text').hide();
		$('.add').hide();
		$('.search').show();
		$('.sign').hide();
		//$(this).css({'background':'#5b7798','color': '#fff'}).siblings().css({'background':'#fff','color': '#5b7798'})
		$(this).addClass('active').siblings().removeClass('active');
		$("footer").hide();
        //点击请求后台查询接口

		//清除之前的节点
         $("#logFirstChild").nextAll().remove();
		$.ajax({
           url:BASE_PATH+'/logsManagers.htm',
		   dataType:'json',
		   type:'post',
           success:function(resultResponse){
               if(resultResponse.success){
                    var ordersData = resultResponse.data.ordersData;
				    var paginator = resultResponse.data.paginator;
				   var html ="";
				   $.each(ordersData,function(i,data){
                       html="<tr><td>"+data.dateStr+"</td><td>"+data.userName+"</td><td>"+data.operation+"</td><td>"+data.typeStr+"</td></tr>";
                       $(".log table").append(html);
				   });
                    //调用分页方法
				   logPage(resultResponse);

			   }else{
				   layer.alert("查询失败")
			   }
		   },
		 error:function(){
			layer.alert("网络错误")
		 }
		});
	});



    //签约记录
	$('#sign').on('click',function(){
		$('.content').hide();
		$('.compent').hide();
		$('.log').hide();
		$('.text').hide();
		$('.add').hide();
		$('.search').hide();
		$('.sign').show();
		//$(this).css({'background':'#5b7798','color': '#fff'}).siblings().css({'background':'#fff','color': '#5b7798'});
		$(this).addClass('active').siblings().removeClass('active');
		$("footer").hide();
		//获取签约记录
		//去除之前生成的节点
		$("#firstChild").nextAll().remove();
		$.ajax({
			url:BASE_PATH+'/querySignRecord.htm',
			dataType:'json',
			type:'post',
			success:function(data){
                 if(data.success){

					 //循环json
                     var html ="";
					 var list = data.list;
					 $.each(list,function(i,item){
						 html="<tr><td>"+item.protocolContractTypeStr+"</td><td>"+item.produceComment+"</td><td>"+item.applyStateStr+"</td><td>"+item.applyTimeStr+"</td><td><span onclick='toApplySign("+item.protocolContractType+","+item.applyState+")'>"+item.isNeedApply+"</span></td>";
						 $(".sign table").append(html);
					 });
				 }
			},
			error:function(){
				alert("网络错误，请刷新");
			}

		});
	});


	//点击签约操作进行签约







	//...............公司名称验证

	$('#companyName input').blur(function(){
		testCompanyName()
	});
	function testCompanyName(){
		var reg=/^[\w\u4e00-\u9fa5]{4,20}$/;
		if($("#companyName input").val()=='' || $("#companyName input").val()==undefined ){
			$('#companyName span').addClass('error').html('公司名称不能为空！');

		}
		else if(reg.test($("#companyName input").val())){
			console.log(22);
			$('#companyName span').removeClass('error').html('');
			fComname=true;
		}
		else{
			$('#companyName span').addClass('error').html('请输入正确的公司名称');
		}
	}
	//........上传图片格式验证


	//.......公司地址验证
	$('#compAddress input').blur(function(){
		testCompAddress()
	});


	function testCompAddress(){
		var reg=/^[\w\u4e00-\u9fa5]{2,20}$/;
		if($("#compAddress input").val()=='' || $("#compAddress input").val()==undefined ){
			$('#compAddress span').addClass('error').html('请输入公司地址');
		}
		else if(reg.test($("#compAddress input").val())){
			$('#compAddress span').removeClass('error').html('');
			faddress=true;
		}
		else{
			$('#compAddress span').addClass('error').html('请输入正确的公司地址')
		}
	}

	//............验证营业执照编号
	$('#busNum input').blur(function(){
		testBusNum()
	});

	function testBusNum(){
		var  Num=RegExp("^\\{15}$");
		var  Num2=RegExp("^\\{18}$");
		if($("#busNum input").val()=='' || $("#busNum input").val()==undefined ){
			$('#busNum span').addClass('error').html('请输入营业执照编号！');
		}
	 	else if($("#busNum input").val().length == 15 || $("#busNum input").val().length == 18){
	 		$('#busNum b').removeClass('error').html('');
			fnumber = true;
		}
		else{
			$('#busNum b').addClass('error').html('请输入正确的营业执照编号');
		}
	}
	//.............验证法人代表

	$('#compCorpration input').blur(function(){
		testCompCorpration()
	});

	function testCompCorpration(){
		var reg=/^[\u4e00-\u9fa5]{2,5}$/;
		if($("#compCorpration input").val()=='' || $("#compCorpration input").val()==undefined ){
			$('#compCorpration span').addClass('error').html('法人代表人姓名不能为空！');
		}
		else if(reg.test($("#compCorpration input").val())){
			$('#compCorpration span').removeClass('error').html('');
			fUsename=true;
		}
		else{
			$('#compCorpration span').addClass('error').html('请输入正确的法人代表人姓名')
		}
	}


	//.......验证(添加操作员)手机号

	$('.phone input').blur(function(){
		testPhone()
	});
    // .......验证(添加操作员)姓名
    $('.username input').blur(function(){
        testUserName()
    })

	//.......公司规模选择
	$('.checkt1').on('click',function(){
		$('#show1').show();
		$('#show2').hide();
		$('#show3').hide();
		$('#show1 li').on('mouseover',function(){
			$(this).css('background','#0696fa').siblings().css('background','none')
		});
		$('#show1 li').on('click',function(){
			var content=$(this).html();
			$('.checkt1').html(content);
			$('#show1').hide();
		})
	});

	//.......行业选择
	$('.checkt2').on('click',function(){
		$('#show1').hide();
		$('#show3').hide();
		$('#show2').show();
		$('#show2 li').on('mouseover',function(){
			$(this).css('background','#0696fa').siblings().css('background','none')
		});
		$('#show2 li').on('click',function(){
			var content=$(this).html();
			$('.checkt2').html(content);
			$('#show2').hide();
		})
	});

	//.......渠道选择
	$('.checkt3').on('click',function(){
		$('#show1').hide();
		$('#show2').hide();
		$('#show3').show();
		$('#show3 li').on('mouseover',function(){
			$(this).css('background','#0696fa').siblings().css('background','none')
		});
		$('#show3 li').on('click',function(){
			var content=$(this).html();
			$('.checkt3').html(content);
			$('#show3').hide();
		})
	});


	//更新设置
	$(".btnPre").on('click',function(){
		    testCompanyName();
		    testCompCorpration();
		    testBusNum();
			testPhone();
			testCompAddress();
		if(fComname&faddress&fnumber&fUsename&fphone){

				alert('更新成功')

		}
	});




	//...........添加操作员
	$('.hint button').on('click',function(){
		//封装
		$.ajax({
			url:BASE_PATH+'/showHasPowers.htm',
			dataType:'json',
			type:'post',
			success:function(resultResponse){
                if(resultResponse.success){
					//拼接参数
                    var listRole = resultResponse.data;
                    //先清除节点
					$("#memberPowerIds").nextAll().remove();
					$.each(listRole,function(i,data){
						var html="";
						html="<li dat='"+data.id+"'><input type='checkbox' name=''/>"+data.roleName+"</li>";
                        $("#check").append(html);
					});
					$('.mengban').show();
					$('.add-user').show();
				}else{
					layer.alert("操作失败")
				}
			},
			error:function(){
				layer.alert("网络错误，请刷新");
			}

		});





		//.....关闭
		$('.add-user b').on('click',function(){
			$('.mengban').hide();
			$('.add-user').hide();
		})
	});



	//................添加用户页面的传值
	$(".affirm").on('click',function(){
		var arr=[];
		var obj=$('.check input');
		for(var i=1;i<obj.length;i++ ){
			if(obj[i].checked==true){
				var Va=$(obj[i]).parent().attr('dat');
				arr.push(Va);
				$('.ident').val(arr)
			}else{
				$('.ident').val(arr)


			}

		}

		addRoleButton();
		//console.log($('.ident').val())
	});


//................修改用户页面的传值

	$(".affirm1").on('click',function(){
		var arr1=[];
		var obj1=$('.check1 input');
		for(var i=1;i<obj1.length;i++ ){
			if(obj1[i].checked==true){
				var Va=$(obj1[i]).parent().attr('sat');
				arr1.push(Va);
				$('.ident1').val(arr1)
			}else{
				$('.ident1').val(arr1)
			}

		}
		console.log($('.ident1').val());
		memberSave();
	});
	$(".btnqx1").on('click',function(){
		$('.mengban').hide();
		$('.change-pre').hide();
	});

// 取消
	$(".btnqx1").on('click',function(){
		$('.mengban').hide();
		$('.change-pre').hide();
		$('.close-user').hide();
		$('.open-user').hide();
		$('.deleat-user').hide();
	});

	$('.open-user b').on('click',function(){
		$('.mengban').hide();
		$('.open-user').hide();
	});

	$('.close-user b').on('click',function(){
		$('.mengban').hide();
		$('.close-user').hide();
	});


	$(".btnqx").on('click',function(){
		$('.mengban').hide();
		$('.add-user').hide();
	});
	//...........删除操作员
	$('.deleat').on('click',function(){
		$('.mengban').show();
		$('.deleat-user').show();
	});
     //....关闭
	$('#delete-user').on('click',function(){
		$('.mengban').hide();
		$('.deleat-user').hide();
	})
});
function change(){
	//先全部取消
	$(".check1 li").find("input").removeAttr("checked");
	var ident=$('.ident1').val().split('');
	console.log(ident);
	//console.log($('.check1 li').eq(1).attr('sat'))
	for(var i=0;i<ident.length;i++){
		$('.check1 li').eq(ident[i]-1).find('input').attr('checked', 'checked')
	}
}

//权限修改
function memberUpdate(memberId,memberName,idValue){
	$("#memberPowerIdsValue").val(idValue);
	$("#memberIdVlaue").val(memberId);
	change();
	$("#member_name").text(memberName);
	$('.mengban').show();
	$('.change-pre').show();
	//....关闭
	$('.change-pre b').on('click',function(){
		$('.mengban').hide();
		$('.change-pre').hide();
	})
}

function memberSave(){
	$.ajax({
		type: 'post',
		url: 'companyMemberEdit.htm',
		data: $("#roleEditForm").serialize(),
		success: function (resultResponse) {
			if (resultResponse.success) {
				$(".change-pre").hide();
				alert("操作成功");
				//添加成功
				$('.mengban').hide();
				$('.add-user').hide();
				//返回信息
				$('.content').hide();
				$('.compent').show();
				$('.log').hide();
				$('.text').hide();
				$('.add').show();
				$('.search').hide();
				$('.sign').hide();
				//$(this).css({'background':'#5b7798','color': '#fff'}).siblings().css({'background':'#fff','color': '#5b7798'});
				$(this).addClass('active').siblings().removeClass('active');
				//取消按钮
				$(".bottom").hide();
				//将之前生成的节点取消
				powerManager();

			} else {
				alert("权限修改失败!");
			}
		}
	});
}
//删除操作员
function memberDel(){
	$.ajax({
		type: 'post',
		url: 'companyMemberDel.htm',
		data: $("#roleDelForm").serialize(),
		success: function (resultResponse) {
			if (resultResponse.success) {
				$('.mengban').hide();
				$('.deleat-user').hide();
				powerManager();
			} else {
				alert("权限删除失败!");
			}
		}
	});
}

    function testPhone(){
        var  reg=/^1[3|5|7|8]\d{9}$/;
        if($(".phone input").val()=='' || $(".phone input").val()==undefined ){
            $('.phone em').html('请输入手机号');
            $('.phone input').addClass('show');
            fphone=false;
        }
        else if(reg.test($(".phone input").val())){
            $('.phone em').html('');
            $('.phone input').removeClass('show')
            fphone = true;
        }
        else{
            $('.phone em').html('请输入正确的手机号！')
            $('.phone input').addClass('show');
            fphone=false;
        }
    }

    function testUserName(){
        if($(".username input").val()=='' || $(".username input").val()==undefined ){
            $('.username em').html('请输入姓名');
            $('.username input').addClass('show');
            fname = false;
        }
        else if($(".username input").val().length>=1 && $(".username input").val().length<=20){
            $('.username em').html('');
            $('.username input').removeClass('show')
            fname = true;
        }
        else{
            $('.username em').html('姓名不能超过20位！')
            $('.username input').addClass('show');
            fname = false;
        }
    }

//添加操作员
function addRoleButton(){
	testPhone();
    testUserName()
	if(fphone&&fname){
	$.ajax({
		type: 'post',
		url: 'companyRoleAdd.htm',
		data: $("#roleForm").serialize(),
		success: function (resultResponse) {
			if (resultResponse.success) {
				//添加成功
				$('.mengban').hide();
				$('.add-user').hide();
                //返回信息
				$('.content').hide();
				$('.compent').show();
				$('.log').hide();
				$('.text').hide();
				$('.add').show();
				$('.search').hide();
				$('.sign').hide();
				//$(this).css({'background':'#5b7798','color': '#fff'}).siblings().css({'background':'#fff','color': '#5b7798'});
				$(this).addClass('active').siblings().removeClass('active');
				//取消按钮
				$(".bottom").hide();
				//将之前生成的节点取消
				powerManager();
			} else {
				alert(resultResponse.message);
			}
		}
	});
	}
}

function popupboxclosed(elem){
	var oI = document.createElement('i');
	oI.className = 'popupbox-closed';
	elem.append(oI);
	$('.popupbox-closed').click(function () {
		$('.popupbox').hide();
	});

	$('.obtnclose').click(function () {
		$('.popupbox').hide()
	});
}

function memberGetById(memberId,memberPhone){
	$("#memberIdDelVlaue").val(memberId);
	$("#member_phone").text(memberPhone);
	$('.mengban').show();
	$('.deleat-user').show();

	//....关闭
	$('.close-user b').on('click',function(){
		$('.mengban').hide();
		$('.deleat-user').hide();
	})
}

function memberOpenButton(memberId){
	if($("#showdiv"+memberId).find("p").attr("value")==2){
		$('.mengban').show();
		$('.close-user').show();
		$('.clo').on('click',function(){
			$("#showdiv"+memberId).find("p").animate({left:0},200,function(){
				$("#showdiv"+memberId).find("p").attr("value",1);
				$("#showdiv"+memberId).css('background','#ccc');
			});
			$('.clo').off('click');
			$('.mengban').hide();
			$('.close-user').hide();
		});
	}else{
		$('.mengban').show();
		$('.open-user').show();
		$('.ope').on('click',function(){
			$("#showdiv"+memberId).find("p").animate({left:'23px'},200,function(){
				$("#showdiv"+memberId).find("p").attr("value",2);
				$("#showdiv"+memberId).css('background','#0696fa');
			});
			$('.ope').off('click');
			$('.mengban').hide();
			$('.open-user').hide();
		})
	}
	$.ajax({
		type: 'post',
		url: 'companyMemberDel.htm',
		data: {'memberId':memberId,'memberStatus':$("#showdiv"+memberId).find("p").attr("value")},
			success: function (resultResponse) {
				if (resultResponse.success) {
				}
			}
		});

}

//显示下一页  上一页
function pageShowNext(){
	$('.popupbox').show(0,function(){
		alert('bb');
		$('.popupbox').load('payforInfo.htm?type=log',function () {
			popupboxclosed($('.popupbox'));
		});
	})
}

//调用分页重新查询
function logPageList(pageIndex){
     //userName
	 var userNameValue=$("#userNameValue").val();
	if(userNameValue!=null&&userNameValue!=""&&userNameValue!=undefined){
		var userName=userNameValue;
	}
	//清除之前的节点
	$("#logFirstChild").nextAll().remove();
	$.ajax({
		url:BASE_PATH+'/logsManagers.htm',
		dataType:'json',
		type:'post',
		data:{'pageIndex':pageIndex,'userName':userName},
		success:function(resultResponse){
			if(resultResponse.success){
				var ordersData = resultResponse.data.ordersData;
				 paginator = resultResponse.data.paginator;
				var html ="";
				$.each(ordersData,function(i,data){
					html="<tr><td>"+data.dateStr+"</td><td>"+data.userName+"</td><td>"+data.operation+"</td><td>"+data.typeStr+"</td></tr>";
					$(".log table").append(html);
				});
				//调用分页方法
				logPage(resultResponse);

			}else{
				layer.alert("查询失败")
			}
		},
		error:function(){
			layer.alert("网络错误")
		}

	});
}

function logPage(resultResponse){
	//生成分页控件
	// kkpager.generPageHtml({
	// 	pno: resultResponse.paginator.page,
	// 	mode: 'click',
	// 	//总页码
	// 	total:''+resultResponse.paginator.totalPages ,
	// 	//总数据条数
	// 	totalRecords:''+resultResponse.paginator.totalCount,
	// 	//链接前部
	// 	hrefFormer:''+resultResponse.hrefFormer,
	// 	//链接尾部
	// 	hrefLatter:''+resultResponse.hrefLatter,
	// 	//链接算法
	// 	isShowFirstPageBtn:false,
	// 	isShowLastPageBtn:false,
	// 	currPageBeforeText:'第',
	// 	click: function (n) {
	// 		var pageIndex =n;
	// 		logPageList(pageIndex);
	// 		this.selectPage(n);
	// 	},
	// 	getHref:function(n){
	// 		return '#';
	// 	}
	// },true);
	laypage({
		cont: 'pagePayroll', //容器。值支持id名、原生dom对象，jquery对象,
		pages: resultResponse.paginator.totalPages, //总页数
		skin: 'molv', //皮肤
		first: 1, //将首页显示为数字1,。若不显示，设置false即可
		last: resultResponse.paginator.totalPages, //将尾页显示为总页数。若不显示，设置false即可
		prev: '上一页', //若不显示，设置false即可
		next: '下一页', //若不显示，设置false即可
		skip: true, //是否开启跳页
		curr: resultResponse.paginator.page || 1, //当前页
		jump: function (obj, first) { //触发分页后的回调
			if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
				// console.info(obj.curr);
				// showData(obj.curr)
				var pageIndex=obj.curr;
				logPageList(pageIndex);
			}
		}
	});
}


//查询日志
function serchLogShow(){
	var userName=$("#userNameValue").val();
	$("#logFirstChild").nextAll().remove();
	$.ajax({
		url:BASE_PATH+'/logsManagers.htm',
		dataType:'json',
		data:{'userName':userName},
		type:'post',
		success:function(result){
               if(result.success){
				   //拼接参数
				   var ordersData = result.data.ordersData;
				    paginator = result.data.paginator;
				   var html ="";
				   $.each(ordersData,function(i,data){
					   html="<tr><td>"+data.dateStr+"</td><td>"+data.userName+"</td><td>"+data.operation+"</td><td>"+data.typeStr+"</td></tr>";
					   $(".log table").append(html);
				   });
				  //调用分页
				   logPage(result);
			   }
		},
		error:function(){
			layer.alert("网络错误，请刷新");
		}

	});
}

