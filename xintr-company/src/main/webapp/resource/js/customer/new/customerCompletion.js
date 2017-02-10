var paginator;
$(function(){
    departmentInit();
    var params={};
    queryInCompleteCustomerList(params);

    $("#inPlemeteSearch").click(function(){
        // var customerTurename=$("[name='customerTurename']").val();
        // var depId=$("#depId").val();
        var detailParams={"customerTurename":$("[name='customerTurename']").val(),"depId":$("#depId").val(),"pageIndex":0};
        queryInCompleteCustomerList(detailParams);
    });

    var uploader = WebUploader.create({

        auto: true,//自动上传
        // sendAsBinary:true,//以流的形式传到后台
        // swf文件路径
        // swf: BASE_PATH + '/resource/js/customer/new/Uploader.swf',

        // 文件接收服务端。
        server: BASE_PATH+'/customerManager/inCompleteUploadBatchNew.htm',

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        // pick: '#exportFileDiv',
        pick: {
            id: '#exportInCompleteFileDiv',
            multiple: false
        },
        // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
        resize: false,
        fileSizeLimit: 100*1024*1024,//
        // // 只允许选择图片文件。
        // accept: {
        //     title: 'Excels',
        //     extensions: 'gif,jpg,jpeg,bmp,png',
        //     mimeTypes: 'image/jpg,image/jpeg,image/png'
        // }
        // accept: {
        //     title: 'Excels',
        //     extensions: 'xls,xlsx'
        // }
    });
    var uploadFile=null;
    //某个文件开始上传前触发，一个文件只会触发一次
    uploader.on('uploadStart', function (file) {
        //显示进度条
        $('.preTan').show();
    })

    $("#cancelUploadFile").click(function(){
        if(uploadFile!=null){
            $('.preTan').hide();
            $('#' + uploadFile.id).find('.uploadProgres').fadeOut();//上传完成后隐藏进度条
            uploader.cancelFile( uploadFile );
            uploader.removeFile(uploadFile, true);
            // $("#showA").show();
            $("#showB").show();
            $("#showC").hide();
            $("#exportInCompleteFileDiv").show();
            $("#showSure").hide();
            $("#showCancel").hide();
            $('.mengban').show();
            $('.customerBatchImport').show();
        }
    });
    // 文件上传过程中创建进度条实时显示。
    uploader.on('uploadProgress', function (file, percentage) {
        uploadFile=file;
        $("#showSure").hide();
        $("#showA").hide();
        $("#showB").hide();
        $("#exportInCompleteFileDiv").hide();
        $("#showCancel").show();
        var progressNumber = (percentage * 100) + '%';
        $('#uploadProgres').html(progressNumber);
        $('.progress-bar').css('width', progressNumber);
        $('#exportInCompleteFileDiv').hide()
        $('#closeUpload').show()
    });

    uploader.on('error', function(errorMsg){
        if("Q_EXCEED_SIZE_LIMIT"==errorMsg){
            layer.alert("上传文件太大,不能超过10M", {title: '上传失败'}, function (index) {

                layer.close(index);
            });
        }else if("Q_TYPE_DENIED"==errorMsg){
            layer.alert("上传文件类型不正确", {title: '上传失败'}, function (index) {

                layer.close(index);
            });
        }else{
            layer.alert("上传异常", {title: '上传失败'}, function (index) {

                layer.close(index);
            });
        }
    });

    uploader.on('uploadSuccess', function (file, response) {
        if (response.success) {
            $("#showInPlemeteDiv").html("成功补全");
            $("#showInPlemeteDiv").show();
            setTimeout(function(){
                $("#showInPlemeteDiv").hide();
            },1000);
            closeSendMsg();
            $('.mengban').hide();
            $('.customerBatchUpload').hide();
            //刷新主页面
            var detailParams={"customerTurename":$("[name='customerTurename']").val(),"depId":$("#depId").val(),"pageIndex":0};
            queryInCompleteCustomerList(detailParams);
        } else {
            //上传失败
            var errorList=response.data;
            if(errorList!=null && errorList.length>0){
                $('.preTan').hide()
                $('#' + file.id).find('.uploadProgres').fadeOut();//上传完成后隐藏进度条
                uploader.removeFile(file, true);
                $("#showSure").show();
                $("#exportInCompleteFileDiv").hide();
                $("#showCancel").hide();

                var html="";
                for(var i=0;i<errorList.length;i++){
                    html += "<p>";
                    html += errorList[i];
                    html += "</p>";
                }
                $("#showC").html(html);
                $("#showC").show();
            }else{
                $("#showA").attr("data",2);
                // onUpload();
                $("#showA").show();
                $("#showB").show();
                $("#showC").hide();
                $("#exportInCompleteFileDiv").show();
                $("#showSure").hide();
                $("#showCancel").hide();
                $('.mengban').show();
                $('.customerBatchUpload').show();
                $('.preTan').hide()
                $('#' + file.id).find('.uploadProgres').fadeOut();//上传完成后隐藏进度条
                uploader.removeFile(file, true);
                layer.alert(response.message, {title: '上传失败'}, function (index) {
                    layer.close(index);
                });
            }
            // $("#showA").attr("data",2);
            // // onUpload();
            // $("#showA").show();
            // $("#showB").hide();
            // $('.mengban').show();
            // $('.customerBatchUpload').show();
            // $('#exportInCompleteFileDiv').show()
            // $('#closeUpload').hide()
            // layer.alert(response.message, {title: '上传失败'}, function (index) {
            //     layer.close(index);
            // });
        }
    });

    uploader.on('uploadComplete', function (file) {
        $('.preTan').hide()
        $('#' + file.id).find('.uploadProgres').fadeOut();//上传完成后隐藏进度条
        uploader.removeFile(file, true);
    });


    // 自动补全(发短信取消)
    $('.btnCloseShortNote').on('click',function(){
        $('.mengban').hide();
        $('.shortNote').hide();
    });

// 批量上传员工信息（取消）
    $('.btnCloseUpload').on('click',function(){
        $('.mengban').hide();
        $('.customerBatchUpload').hide();
    });

    /**
     * 点击确认按钮
     */
    $("#sureBatchUpload").click(function(){
        $('.mengban').hide();
        $("#showC").hide();
        $('.customerBatchUpload').hide();

    });

    //  清空部门
    $('.close').on('click',function(){
        console.log(222)
        $(this).hide();
        $('.section input').val('');

    })

});

/**
 * 查询未补全资料员工列表
 * @param params
 */
function queryInCompleteCustomerList(params){
    //获取列表
    $.ajax({
        type: 'post',
        url: BASE_PATH+'/customerManager/queryInCompleteCustomerList.htm',
        data: params,
        async:true,
        success: function (data) {
            if (data.success) {
                var customerDtos=data.data;
                var showCustomerTbody=$("#showInCompleteCustomerTab").find("tbody");
                var tbodyHtml="";
                paginator=data.paginator;
                for(var i=0;i<customerDtos.length;i++){
                    var customerDto=customerDtos[i];
                    tbodyHtml += "<tr>";
                    tbodyHtml += "<td>"+(i+1)+"</td>";//序号
                    tbodyHtml += "<td class='customerCompletionName' onclick='oncustomerCompletionName("+customerDto.customerId+");'>"+customerDto.customerTurename+"</td>";//姓名
                    tbodyHtml += "<td>"+customerDto.customerPhone+"</td>";//手机
                    tbodyHtml += "<td>"+(customerDto.depName==undefined?'':customerDto.depName)+"</td>";//部门
                    if(customerDto.customerBanknumber!=undefined && customerDto.customerBanknumber!=''){
                        tbodyHtml += "<td>"+customerDto.customerBanknumber+"</td>";//工资卡号
                    }else{
                        tbodyHtml += "<td><input type='text' customerId='"+customerDto.customerId+"' selType='1' onBlur='uptInCompleteCustomer(this);'></td>";//工资卡号
                    }

                    if(customerDto.customerBank!=undefined && customerDto.customerBank!=''){
                        tbodyHtml += "<td>"+customerDto.customerBank+"</td>";//工资卡开户行
                    }else{
                        tbodyHtml += "<td><input type='text' customerId='"+customerDto.customerId+"' selType='2' onBlur='uptInCompleteCustomer(this);'></td>";//工资卡开户行
                    }

                    if(customerDto.customerIdcard!=undefined && customerDto.customerIdcard!=''){
                        tbodyHtml += "<td>"+customerDto.customerIdcard+"</td>";//身份证号
                    }else{
                        tbodyHtml += "<td><input type='text' customerId='"+customerDto.customerId+"' selType='3' onBlur='uptInCompleteCustomer(this);'></td>";//身份证号
                    }

                    tbodyHtml += "<td class='lastTd1'>";//状态
                    tbodyHtml += "<div class='cardTd'>";
                    tbodyHtml += "<div class='pic front1'>+";
                    var errorUrl=BASE_PATH+"/resource/img/customer/new/2.2error1.png";
                    var preUrl="http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+customerDto.customerIdcardImgFront;
                    var preImgId="preImg"+customerDto.customerId;
                    var preFileId='approveFilePre'+customerDto.customerId;
                    var preHidId='customerIdcardImgFront'+customerDto.customerId;
                    if(customerDto.customerIdcardImgFront!=undefined && customerDto.customerIdcardImgFront!=''){
                        tbodyHtml += "<img src='"+preUrl+"' id='"+preImgId+"' >";
                        tbodyHtml += "<input type='file'  name='file' disabled='disabled' id='"+preFileId+"' onchange='uploadApproveImg(\"approveFilePre\",\"preImg\",\"customerIdcardImgFront\",\"4\","+customerDto.customerId+");'/ >";
                    }else{

                        tbodyHtml += "<img src='"+errorUrl+"' id='"+preImgId+"' >";
                        tbodyHtml += "<input type='file'  name='file' id='"+preFileId+"' onchange='uploadApproveImg(\"approveFilePre\",\"preImg\",\"customerIdcardImgFront\",\"4\","+customerDto.customerId+");'/ >";
                    }
                    tbodyHtml += "<input type='hidden' name='customerIdcardImgFront' id='"+preHidId+"' value='"+(customerDto.customerIdcardImgFront==undefined?'':customerDto.customerIdcardImgFront)+"'> ";
                    tbodyHtml += "<b>正面</b>";
                    tbodyHtml += "</div>";
                    tbodyHtml += "<div class='pic contrary'>+";
                    var backUrl="http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+customerDto.customerIdcardImgBack;
                    var backImgId="backImg"+customerDto.customerId;
                    var backFileId='approveFileBack'+customerDto.customerId;
                    var backHidId='customerIdcardImgBack'+customerDto.customerId;
                    if(customerDto.customerIdcardImgBack!=undefined && customerDto.customerIdcardImgBack!=''){
                        tbodyHtml += "<img src='"+backUrl+"' id='"+backImgId+"' >";
                        tbodyHtml += "<input type='file'  name='file' disabled='disabled' id='"+backFileId+"' onchange='uploadApproveImg(\"approveFileBack\",\"backImg\",\"customerIdcardImgBack\",\"5\","+customerDto.customerId+");'/ >";
                    }else{
                        tbodyHtml += "<img src='"+errorUrl+"' id='"+backImgId+"' >";
                        tbodyHtml += "<input type='file'  name='file' id='"+backFileId+"' onchange='uploadApproveImg(\"approveFileBack\",\"backImg\",\"customerIdcardImgBack\",\"5\","+customerDto.customerId+");'/ >";
                    }
                    tbodyHtml += "<input type='hidden' id='customerIdcardImgBack' name='"+backHidId+"' value='"+(customerDto.customerIdcardImgBack==undefined?'':customerDto.customerIdcardImgBack)+"'>";
                    tbodyHtml += "<b>反面</b>";
                    tbodyHtml += "</div>";
                    tbodyHtml += "</div>";
                    tbodyHtml += "</td>";
                    tbodyHtml += "</tr>";
                }
                showCustomerTbody.html(tbodyHtml);
                generPageHtml();
            } else {
                layer.alert(data.message, {icon: 1});
            }
        }
    });
}

function uptInCompleteCustomer(element){
    var customerId=$(element).attr("customerId");
    var type=$(element).attr("selType");
    var value=$(element).val();
    var params={};
    if(type==1){
        params={"customerId":customerId,"type":type,"customerBanknumber":value};
    }else if(type==2){
        params={"customerId":customerId,"type":type,"customerBank":value};
    }else if(type==3){
        params={"customerId":customerId,"type":type,"customerIdcard":value};
    }

    $.ajax({
        type: 'post',
        url: BASE_PATH+'/customerManager/uptInCompleteCustomer.htm',
        data: params,
        async:false,
        success: function (data) {
            if (data.success) {

            } else {
                if(type==1 || type==2 || type==3){
                    $(element).val('');
                }
                layer.alert(data.message, {icon: 1});
            }
        }
    });
}

/**
 * 上传身份证图片
 * @param fileId
 * @param showId
 * @param hideId
 */
function uploadApproveImg(fileId,showId,hideId,type,customerId){
    $.ajaxFileUpload({
        url:BASE_PATH+ '/customerManager/uploadIdCardImgNew.htm', //用于文件上传的服务器端请求地址
        fileElementId: fileId+customerId, //文件上传域的ID
        dataType: 'json', //返回值类型 一般设置为json
        success: function (data){
            if(data.success) {
                $("#"+showId+customerId).attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
                $("#"+hideId+customerId).val(data.data);
                var params={};
                if(type==4){
                    params={"customerId":customerId,"type":type,"customerIdcardImgFront":data.data};
                }else if(type==5){
                    params={"customerId":customerId,"type":type,"customerIdcardImgBack":data.data};
                }
                $.ajax({
                    type: 'post',
                    url: BASE_PATH+'/customerManager/uptInCompleteCustomer.htm',
                    data: params,
                    async:false,
                    success: function (data) {
                        if (data.success) {

                        } else {
                            layer.alert(data.message, {icon: 1});
                        }
                    }
                });
            }
        }
    });
}

/**
 * 部门初始化
 */
function departmentInit(){
    /**部门的数据接口 start**/
    $.get(BASE_PATH + '/dept/tree.htm', {}, function(data){
        if (data.success) {
            // DeptExtend.initTree(data.data);
            dg($("#dep-base"), data.data)
        } else {
            showWarning(data.message);
        }
    });
    /**部门的数据接口 end**/
    function dg(parent, data){
        var title = '<span class="dep-item" data-depid="'+data.depId+'" style="display: block; width: 100%;">' + data.depName + '</span>';
        parent.append(title);
        if(data.children && data.children.length > 0){
            var ul = $("<ul>");
            $.each(data.children, function(index, item){
                var li =$("<li>")
                dg(li, item)
                ul.append(li);
            });
            parent.append(ul);

        }
        $('.dep-item').on('click',function(){
            $('#customerDepName').val($(this).html());
            $('#depId').val($(this).data("depid"));
            $('.close').show();
            $('.branch').hide();
            $('.show').off('click');
        });

        //调用树形菜单
        $("#dep-base").treeview({
            animated: "fast",
            collapsed: true,
            unique: true,
            toggle: function() {
            }
        });
    }



    $('.bumen').on('click',function(e){
        e.stopPropagation();
        $('.show').off('click');
        $('.branch').toggle();
    });


    // 去除冒泡
    $(document).on('click',function(){
        $('.branch').hide();
        $('.show').off('click')
    })
    $('.branch').on('click',function(e){
        e.stopPropagation()
    })

}


// 批量上传员工信息（要求）
// function onUpload(){
//     var data=$('.upload').attr('data');
//     if(data==1){
//         $('.customerBatchUpload-mainShow').animate({'top':'10px'},150);
//         // $('.customerBatchImport-hint').hide()
//         $('.text').toggle(150,function(){
//             $('.upload').attr('data','2');
//
//         });
//         $('.upload').html('点击关闭上传要求<')
//     };
//     if(data==2){
//         $('.customerBatchUpload-mainShow').animate({'top':'140px'},150);
//         // $('.customerBatchImport-hint').hide()
//         $('.text').toggle(150,function(){
//             $('.upload').attr('data','1')
//         })
//         $('.upload').html('点击展开上传要求>')
//     };
// }
// 自动补全(发短信 弹窗出现)
function onShortNote(){
    $.ajax({
        type: 'post',
        url: BASE_PATH+'/customerManager/querySendInPlemeteMsgCount.htm',
        // data: params,
        async:false,
        success: function (data) {
            if (data.success) {
                var count=data.message;
                $("#customerCount").text(count);
            } else {
                layer.alert(data.message, {icon: 1});
            }
        }
    });
    $('.mengban').show();
    $('.shortNote').show();
}

function closeSendMsg(){
    $('.mengban').hide();
    $('.shortNote').hide();
}
/**
 * 发送未补全资料员工短信
 */
function sendMsgSure(){
    if($("#customerCount").text()=='0'){
        layer.alert("无员工需要通知", {icon: 1});
        return false;
    }
    $.ajax({
        type: 'post',
        url: BASE_PATH+'/customerManager/sendInPlemeteMsg.htm',
        // data: params,
        async:false,
        success: function (data) {
            if (data.success) {
                $("#showInPlemeteDiv").html("短信发送成功");
                $("#showInPlemeteDiv").show();
                setTimeout(function(){
                    $("#showInPlemeteDiv").hide();
                },1000);
                closeSendMsg();
                //刷新主页面
                var detailParams={"customerTurename":$("[name='customerTurename']").val(),"depId":$("#depId").val(),"pageIndex":0};
                queryInCompleteCustomerList(detailParams);
            } else {
                layer.alert(data.message, {icon: 1});
            }
        }
    });
}
// 批量上传员工信息(弹窗出现)
function onCustomerBatchUpload(){
    $("#showA").attr("data",2);
    // onUpload();
    $("#showA").show();
    $("#showB").show();
    $("#showC").hide();
    $('.mengban').show();
    $("#showSure").hide();
    $("#showCancel").hide();
    $('.customerBatchUpload').show();
    $('#exportInCompleteFileDiv').show();
}

function generPageHtml() {
    laypage({
        cont: 'pagePayroll', //容器。值支持id名、原生dom对象，jquery对象,
        pages: paginator.totalPages, //总页数
        skin: 'molv', //皮肤
        first: 1, //将首页显示为数字1,。若不显示，设置false即可
        last: paginator.totalPages, //将尾页显示为总页数。若不显示，设置false即可
        prev: '上一页', //若不显示，设置false即可
        next: '下一页', //若不显示，设置false即可
        skip: true, //是否开启跳页
        curr: paginator.page || 1, //当前页
        jump: function (obj, first) { //触发分页后的回调
            if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                // console.info(obj.curr);
                // showData(obj.curr)
                var detailParams={"customerTurename":$("[name='customerTurename']").val(),"depId":$("#depId").val(),"pageIndex":obj.curr};
                queryInCompleteCustomerList(detailParams);
            }
        }
    });
}

function oncustomerCompletionName(customerId){
    window.location.href=BASE_PATH+'/customerManager/toCustomerEditPageNew.htm?customerId='+customerId;
}