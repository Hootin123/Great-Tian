
var data = [
	{'type': '公司简介',
		'title':'请上传公司历史沿革、企业文化介绍、团队及建设介绍、公司主要的经营范围、产品性能及近期的规划及长远打算<br />支持word、excel、支持图片.jpg .jpeg .bmp .gif .png格式照片，图片大小不超过5M',
		'zip':['.doc','.xls','.jpg','.jpeg','.bmp','.gif','.png']},
	{'type': '公司相关证件',
		'title':'请上传营业执照、税务登记证、组织机构代码证、开户许可证，证件需在有效期内且年检齐全（当成立可无年检）<br />支持图片原件照片、扫描件或加盖公章的复印件，支持jpg、jpeg、bmp、gif、png格式照片，图片大小不超过5M',
		'zip':['.jpg','.jpeg','.bmp','.gif','.png']},
	{'type': '法人、股东资料',
		'title':'请上传法人代表、主要股东身份证明<br />支持图片.jpg、.jpeg、.bmp、.gif、.png格式照片，大小不超过5M',
		'zip':['.jpg,.jpeg,.bmp,.gif,.png']},
	{'type': '签字样本',
		'title':'请上传申请人所在公司的董事（股东）会决议，及董事（股东）会成员签字样本<br />支持word、支持图片.jpg .jpeg .bmp .gif .png格式照片，图片大小不超过5M',
		'zip':['.doc,.jpg,.jpeg,.bmp,.gif,.png']},
	{'type': '贷款卡及企业信用报告',
		'title':'请上传贷款卡正反面照片，基本信息主要有概况信息、出资人信息、财务报表信息、关注信息、诉讼信息等；信贷信息包括未结清信贷信息、未结清不良负债等银行信用信息；非银行信息包括法院、公积金、电信、社保等信息<br />支持word、支持图片.jpg .jpeg .bmp .gif .png格式照片，图片大小不超过6M',
		'zip':['.doc,.jpg,.jpeg,.bmp,.gif,.png']},
	{'type': '报表','title':'请上传近三年财务报告及近三个月财务报表<br />支持excel',
		'zip':['.xls']},
	{'type': '近期合同','title':'请上传近期主要购销合同、合作协议<br />支持图片.jpg .jpeg .bmp .gif .png格式照片，图片大小不超过5M',
		'zip':['.jpg,.jpeg,.bmp,.gif,.png']},
	{'type': '账目',
		'title':'请上传主要存货明细、固定资产明细及负担情况、应收账明细及账龄<br />支持excel、支持图片.jpg .jpeg .bmp .gif .png格式照片，图片大小不超过5M',
		'zip':['.xls,.jpg,.jpeg,.bmp,.gif,.png']},
	{'type': '缴纳表',
		'title':'请上传缴纳申报表及缴税单<br />支持excel',
		'zip':['.xls']},
	{'type': '日常开销',
		'title':'请上传近三个月水、电等费用缴费单<br />支持图片.jpg .jpeg .bmp .gif .png格式照片，图片大小不超过5M',
		'zip':['.jpg,.jpeg,.bmp,.gif,.png']},
	{'type': '融资明细','title':'请上传银行贷款、银票、融资租赁、商业保理<br />支持图片.jpg .jpeg .bmp .gif .png格式照片，图片大小不超过5M',
		'zip':['.jpg,.jpeg,.bmp,.gif,.png']},
	{'type': '银行对公流水','title':'基本户及主要一般户<br />支持图片.jpg .jpeg .bmp .gif .png格式照片，图片大小不超过5M',
		'zip':['.jpg,.jpeg,.bmp,.gif,.png']},
	{'type': '个人流水','title':'（大、中型企业可不用提供）<br />支持图片.jpg .jpeg .bmp .gif .png格式照片，图片大小不超过5M',
		'zip':['.jpg,.jpeg,.bmp,.gif,.png']},
	{'type': '其他相关','title':'可上传生产经营或投资项目取得的环保许可证明，特殊行业持有的相关权威部门颁发的生产、经营许可证明等<br />支持word、excel、支持图片.jpg .jpeg .bmp .gif .png格式照片，图片大小不超过5M',
		'zip':['.doc,.xls,.jpg,.jpeg,.bmp,.gif,.png']}
]


var data1 = [];
var data2 = [];
var res = null;
	function test2() {
		var titleclass = '';
		var html = '';
		var oInp = '';
		var aBtn = '';


		for(var i = 0; i < data.length; i++) {

			aBtn = '上传文件';
			titleclass = '';
			for(var j=0;j<data2.length;j++){
				if(i==data2[j].file_type-1){
					aBtn = '查看文件';
					titleclass = 'identity-content-box1';
				}
			}

			html += '<div class="identity-content-boxs">' +
					'<div class="identity-content-box ' + titleclass + '">' +
					'<a href="completecredit.htm?typeId='+(i+1)+'">' + aBtn + '</a>' +
					oInp +
					'</div>' +
					'<p>' + data[i].type + '</p>' +
					'</div>';

		}
		$('.identity-content-boxes').html(html);

	}

	function showImageCount(){
		$.ajax({
			type:"POST",
			url:"fileresourcesCount.htm",
			dataType:"json",
			data:{},
			async: true,
			success: function(data){
				data2 = $.parseJSON(data.data);
				test2();
			}
		});
	}

	function showImage(){
		$.ajax({
			type:"POST",
			url:"fileresources.htm",
			dataType:"json",
			data:{
				'fileType':typeId
			},
			async: true,
			success: function(data){
				data1= $.parseJSON(data.data);
				test3();
			}
		});
	}

	function test3() {

		var html2 = '';
		var inptype2 = '';
		var oImg = '';
		for(var i = 0; i <= data1.length; i++) {

			if(i == data1.length) {
				oImg = 'resource/img/add.png';
				inptype2 = '<a class="identity-page-file"  id="upload"></a>';
			} else {

				inptype2 = '<i class="identity-page-delebg"></i>' +
						'<i class="identity-page-delete">删除</i>' +
						'<div class="identity-page-upload">' +
						'<div class="identity-page-upload1"></div>' +
						'</div>';

				if(data1[i].file_url){
					var oUr = data1[i].file_url.lastIndexOf('.');
					var oExe = data1[i].file_url.substring(oUr,data1[i].file_url.length);

					if(oExe == '.doc'){
						oImg = 'resource/img/doc.png'
					}else if(oExe == '.xls'){
						oImg = 'resource/img/exl.png'
					}else if(oExe == '.pdf'){
						oImg = 'resource/img/pdf.png'
					}else {
						oImg = 'http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/'+ data1[i].file_url;
					}

				}else {
					var oUr = data1[i].fileUrl.lastIndexOf('.');
					var oExe = data1[i].fileUrl.substring(oUr,data1[i].fileUrl.length);
					if(oExe == '.doc'){
						oImg = 'resource/img/doc.png'
					}else if(oExe == '.xls'){
						oImg = 'resource/img/exl.png'
					}else if(oExe == '.pdf'){
						oImg = 'resource/img/pdf.png'
					}else {
						oImg = 'http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/' + data1[i].fileUrl;
					}
				}
			}

			html2 += '<div class="identity-content-page">' +
					'<img src="' + oImg + '"/>' +
					inptype2 +
					'</div>';

		}

		$('.identity-content-boxes1').html(html2);

		$('.xtr2-h2-type').html(data[typeId-1].type);
		$('.xtr3-btns').html(data[typeId-1].title);


		//事件绑定
		$("#upload").dropzone(
				{
					url: "payfor/upload.htm?type=1&fileType="+typeId,
					maxFiles: 10,
					maxFilesize: 512,
					acceptedFiles: data[typeId-1].zip.join(),
					addedfile: function (file) {
						//更新文件上传进度
						//$('.bar_1').css('width', 10);
					},
					complete: function (data) {
						try {
							res = eval('(' + data.xhr.responseText + ')');
							if (res.success) {
								//alert(res.message);
								data1.push($.parseJSON(res.message));
								test3();
							} else {
								//上传失败
								alert('上传失败,文件格式不对!');
							}
						} catch (failed) {
							alert('上传失败,文件格式不对!');
						}
					}
				});

		/*删除按钮的开关*/
		var aPage = $('.identity-content-page');
		var aDeletebg = $('.identity-page-delebg');
		var aDelete = $('.identity-page-delete');



		for(var j = 0; j < aPage.length - 1; j++) {
			aPage.eq(j).attr('index', j);
		}

		aPage.mouseover(function() {
			aDeletebg.css('display', 'none');
			aDelete.css('display', 'none');
			aDeletebg.eq($(this).attr('index')).css('display', 'block');
			aDelete.eq($(this).attr('index')).css('display', 'block');
		})

		aPage.mouseout(function() {
			aDeletebg.css('display', 'none');
			aDelete.css('display', 'none');
		})



		/*删除图片*/

		aDelete.click(function() {

            var id= data1[$(this).parent().attr('index')].id;
			var fileUrl = '';
			if(data1[$(this).parent().attr('index')].file_url){
				fileUrl = data1[$(this).parent().attr('index')].file_url;
			}else {
				fileUrl = data1[$(this).parent().attr('index')].fileUrl;
			}

			var _this = $(this);
			$.ajax({
				type:"POST",
				url:"filedelete.htm",
				dataType:"json",
				data:{
					'id':id
					,'fileUrl':fileUrl
				},
				async: true,
				success: function(data){

					if(data.success){
						$(this).parent().attr('index')
						$(this).parents('td').remove();
						//alert(data1.length)
						data1.splice(_this.parent().attr('index'), 1);
						$('.identity-content-tb1').empty();
						test3();
					}else{
						alert('删除失败!');
					}
				}
			});
		})

	}
