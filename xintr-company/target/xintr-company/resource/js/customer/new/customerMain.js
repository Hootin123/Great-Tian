//是否转正取值
var isRegular=1;//默认为1代表试用期
var paginator;
// table 切换
var flastTime=false,fsocial=false,ffund=false,fbaseSalary = false,ftakeSalary = false,fchangeDate=false, fusername = false,fphone = false,fenterdata = false,ftakedata = false, ftryPay = false,ftakePay = false;
var faddDept=false,fstation=false;
$(function(){

    //初始化
    if($("input[name='indexFrom']").val()=="index"){
        $(".mengban").show();
        $(".customerAdd").show();
    }




    $("#enterShowTr1").show();
    $("#enterShowTr2").show();
    $("#regularShowTr").hide();


    //部门初始化
    departmentInit();
    var params={"type":2,"isSortEnterTime":$("#isSortEnterTime").val()};
    queryListByCondition(params);


    $('.customer-title-left span').on('click',function(){
        $(this).addClass('active').siblings().removeClass('active')
    });
    $('.customer-main-top span').on('click',function(){
        $(this).addClass('active1').siblings().removeClass('active1');
        //赋值类型,为了点击搜索时知道是哪个页签
        $("#typeHid").val($(this).attr("param"));
        var paramsType={"type":$(this).attr("param"),"customerTurename":$("#customerTurename").val(),"pageIndex":0,"isSortEnterTime":$("#isSortEnterTime").val()};
        queryListByCondition(paramsType);
    });

    //员工状态点击事件
    $("[name='operationType']").click(function(){
        if($('.AddState input').get(0).checked){
            isRegular=1;//试用期
            $("#stationEnterTime1").val('');
            $("#customerProbationSalary1").val('');
            $("#stationRegularTime1").val('');
            $("#customerRegularSalary1").val('');
            $("#stationEnterTime2").val('');
            $("#customerRegularSalary2").val('');
            $("#enterShowTr1").show();
            $("#enterShowTr2").show();
            $("#regularShowTr").hide();
        }else if($('.AddState input').get(1).checked){
            isRegular=2;//已转正
            $("#stationEnterTime1").val('');
            $("#customerProbationSalary1").val('');
            $("#stationRegularTime1").val('');
            $("#customerRegularSalary1").val('');
            $("#stationEnterTime2").val('');
            $("#customerRegularSalary2").val('');
            $("#regularShowTr").show();
            $("#enterShowTr1").hide();
            $("#enterShowTr2").hide();
        }
    });
    //点击搜索按钮
    $("#conditionSearch").click(function(){
        var paramsType={"type":$("#typeHid").val(),"customerTurename":$("#customerTurename").val(),"pageIndex":0,"isSortEnterTime":$("#isSortEnterTime").val()};
        queryListByCondition(paramsType);
    });


    // 社保
    $('.checkSocial input').change(function(){
        testSocial()
    });

    function testSocial(){
        if($('.checkSocial input').get(0).checked || $('.checkSocial input').get(1).checked){
            $('.checkSocial b').html('');
            fsocial=true;
        }else{
            $('.checkSocial b').html('请选择是否缴纳离职月社保！');
            fsocial=false;
        }
    }
    // 公积金
    $('.checkFund input').change(function(){
        testFund()
    });

    function testFund(){
        if($('.checkFund input').get(0).checked || $('.checkFund input').get(1).checked){
            $('.checkFund b').html('')
            ffund=true;
        }else{
            $('.checkFund b').html('请选择是否缴纳离职月公积金！');
            ffund=false;
        }
    }

    // 离职确认
    // $('.onCustomerDimission').on('click',function(){
    //     testLastTime();
    //     testSocial();
    //     testFund();
    //     if(flastTime&&fsocial&&ffund){
    //
    //     }
    // })
    // 离职取消
    $('.btnCloseDimission').on('click',function(){
        $('.mengban').hide();
        $('.customerDimission').hide();
    })




// 调薪
    // 调薪后基本工资
    $('.baseUpdateSalary input').blur(function(){
        testBaseUpdateSalary();
    })
    function testBaseUpdateSalary(){
        if($(".baseUpdateSalary input").val()=='' || $(".baseUpdateSalary input").val()==undefined ){
            $('.baseUpdateSalary b').html('请输入调薪后基本工资');
            $(".baseUpdateSalary input").addClass('error');
            fbaseSalary = false;
        }else if(!/^(?!0+(?:\.0+)?$)(?:[1-9]\d*|0)(?:\.\d{1,2})?$/.test($(".baseUpdateSalary input").val())){
            $('.baseUpdateSalary b').html('请输入正确的调薪后基本工资');
            $(".baseUpdateSalary input").addClass('error');
            fbaseSalary = false;
        }else{
            $('.baseUpdateSalary b').html('');
            $(".baseUpdateSalary input").removeClass('error');
            fbaseSalary = true;
        }
    }
    // 生效日期
    $('.takeDate input').blur(function(){
        testTakeSalary();
    })
    // function testTakeSalary(){
    //     if($(".takeDate input").val()=='' || $(".takeDate input").val()==undefined ){
    //         $('.takeDate b').html('请选择生效日期');
    //         $(".takeDate input").addClass('error');
    //         ftakeSalary = false;
    //     }else{
    //         $('.takeDate b').html('');
    //         $(".takeDate input").removeClass('error')
    //         ftakeSalary = true;
    //     }
    // }
    // 调薪确认
    // $('.onCustomerUpdateSalary').on('click',function(){
    //     testBaseUpdateSalary();
    //     testTakeSalary();
    //     if(fbaseSalary&&ftakeSalary){
    //         if($("input[name='salary']").val()==$("input[name='customerOldSalary']").val()){
    //             layer.alert("金额未发生变化", {icon: 1});
    //             return false;
    //         }
    //         var assemParams={"date":$("input[name='date']").val(),"salary":$("input[name='salary']").val(),"customerId":$("input[name='updateSalaryCustomerId']").val()};
    //         $.ajax({
    //             type: 'post',
    //             url: BASE_PATH + '/customerSalary/updateSalary.htm',
    //             data: assemParams,
    //             success: function(data) {
    //                 if (data.success) {
    //                     $("#showDialogDiv").html("员工<span>"+($("#updateSalaryCustomerName").text())+"</span><i>调薪设置成功</i>");
    //                     $("#showDialogDiv").show();
    //                     setTimeout(function(){
    //                         $("#showDialogDiv").hide();
    //                     },1000);
    //                     $('.mengban').hide();
    //                     $('.customerUpdateSalary').hide();
    //                     //刷新主页面
    //                     var paramsType={"type":$("#typeHid").val(),"customerTurename":$("#customerTurename").val(),"pageIndex":0,"isSortEnterTime":$("#isSortEnterTime").val()};
    //                     queryListByCondition(paramsType);
    //                 } else {
    //                     layer.alert(data.message, {icon: 1});
    //                 }
    //             }
    //         });
    //     }
    // });
    // 调薪取消
    $('.btnCloseUpdateSalary').on('click',function(){
        $('.mengban').hide();
        $('.customerUpdateSalary').hide();
    });




// 转正
    // 转正取消
    $('.btnCloseRegular').on('click',function(){
        $('.mengban').hide();
        $('.customerRegular').hide();
    });

// 新增员工
    // 姓名
    $('.AddUserName input').blur(function(){
        testUsername();
    });

    function testUsername(){
        if($(".AddUserName input").val()=='' || $(".AddUserName input").val()==undefined ){
            $('.AddUserName b').html('请输入正确的姓名');
            $('.AddUserName input').addClass('error');
            fusername = false;
        }
        else if($(".AddUserName input").val().length>=1 && $(".AddUserName input").val().length<=20){
            $('.AddUserName b').html('');
            $('.AddUserName input').removeClass('error');
            fusername = true;
        }
        else{
            $('.AddUserName b').html('请输入正确的姓名');
            $('.AddUserName input').addClass('error');
            fusername = false;
        }
    };
    //....验证手机号

    $('.AddPhone input').blur(function(){
        testPhone();
    });

    function testPhone(){
        var  reg=/^1[3|5|7|8]\d{9}$/;
        if($(".AddPhone input").val()=='' || $(".AddPhone input").val()==undefined ){
            $('.AddPhone b').html('请输入正确的手机号');
            $(".AddPhone input").addClass('error');
            fphone = false;
        }
        else if(reg.test($(".AddPhone input").val())){
            $('.AddPhone b').html('');
            $(".AddPhone input").removeClass('error');
            fphone = true;
        }
        else{
            $('.AddPhone b').html('请输入正确的手机号！');
            $(".AddPhone input").addClass('error');
            fphone = false;
        }
    };
    //验证部门
    $('.customerDepName input').blur(function(){
        testAddDepartment();
    });

    function testAddDepartment(){
        if($(".customerDepName #depId").val()=='' || $(".customerDepName #depId").val()==undefined ){
            $('.customerDepName b').html('请选择部门');
            $(".customerDepName #customerDepName").addClass('error');
            faddDept = false;
        }
        else{
            $('.customerDepName b').html('');
            $(".customerDepName #customerDepName").removeClass('error');
            faddDept = true;
        }
    };

    //验证岗位
    $('.AddStation input').blur(function(){
        testStation();
    });

    function testStation(){
        if($(".AddStation input").val()=='' || $(".AddStation input").val()==undefined ){
            $('.AddStation b').html('请输入岗位');
            $(".AddStation input").addClass('error');
            fstation = false;
        }
        else{
            $('.AddStation b').html('');
            $(".AddStation input").removeClass('error');
            fstation = true;
        }
    };

    // 员工状态
    $('.AddState input').change(function(){
        testState();
    })
    function testState(){
        if($('.AddState input').get(0).checked || $('.AddState input').get(1).checked){
            $('.AddState b').html('');
            fsex=true;
        }else{
            $('.AddState b').html('请选择员工状态！');
            fsex=false;
        }
    }
    // ..验证试用期工资

    $('.AddTryPay input').blur(function(){
        testTryPay();
    });
    function testTryPay(){

        if(isRegular==1){
            if($(".AddTryPay input").val()=='' || $(".AddTryPay input").val()==undefined ){
                $('.AddTryPay b').html('请输入试用期基本工资金额');
                $(".AddTryPay input").addClass('error');
                ftryPay = false;
            }else if($(".AddTryPay input").val()<0){
                $('.AddTryPay b').html('基本工资不可为负数');
                $(".AddTryPay input").addClass('error');
                ftryPay = false;
            }else{
                $('.AddTryPay b').html('');
                $(".AddTryPay input").removeClass('error');
                ftryPay = true;
            }
        }else if(isRegular==2){
            ftryPay = true;
        }

    };
    // ..验证转正后工资

    $('.AddTakePay input').blur(function(){
        testTakePay();
    });
    function testTakePay(){

        if(isRegular==1){
            if($(".AddTakePay input").val()=='' || $(".AddTakePay input").val()==undefined ){
                $('.AddTakePay b').html('请输入转正后基本工资金额');
                $(".AddTakePay input").addClass('error');
                ftakePay = false;
            }else if($(".AddTakePay input").val()<0){
                $('.AddTakePay b').html('基本工资不可为负数');
                $(".AddTakePay input").addClass('error');
                ftakePay = false;
            }else if(parseFloat($(".AddTakePay input").val()) <parseFloat($(".AddTryPay input").val()) ){
                $('.AddTakePay b').html('转正后工资低于试用期工资');
                $(".AddTakePay input").addClass('error');
                ftakePay = false;
            }
            else{
                $('.AddTakePay b').html('');
                $(".AddTakePay input").removeClass('error');
                ftakePay = true;
            }
        }else if(isRegular==2){
            if($(".AddTakePayRegular input").val()=='' || $(".AddTakePayRegular input").val()==undefined ){
                $('.AddTakePayRegular b').html('请输入转正后基本工资金额');
                $(".AddTakePayRegular input").addClass('error');
                ftakePay = false;
            }else if($(".AddTakePay input").val()<0){
                $('.AddTakePayRegular b').html('基本工资不可为负数');
                $(".AddTakePayRegular input").addClass('error');
                ftakePay = false;
            }else{
                $('.AddTakePayRegular b').html('');
                $(".AddTakePayRegular input").removeClass('error');
                ftakePay = true;
            }
        }

    }
    // 新增确认
    $('.onCustomerAdd').on('click',function(){
        testUsername();
        testPhone();
        testState();
        testEnterData();
        testTakeData();
        testTryPay();
        testTakePay();
        testAddDepartment();
        testStation();
        if(fusername && fphone && fenterdata && ftakedata && ftryPay && ftakePay && fstation && faddDept){
            var dataParams;
            if(isRegular==1){
                dataParams={"customerTurename":$("[name='customerTurename']").val(),"customerDepName":$("[name='customerDepName']").val(),"customerDepId":$("[name='customerDepId']").val(),"customerPhone":$("[name='customerPhone']").val()
                    ,"stationStationName":$("[name='stationStationName']").val(),"operationType":isRegular,"stationEnterTime":$("#stationEnterTime1").val(),"customerProbationSalary":$("#customerProbationSalary1").val()
                ,"stationRegularTime":$("#stationRegularTime1").val(),"customerRegularSalary":$("#customerRegularSalary1").val()};
            }else if(isRegular==2){
                dataParams={"customerTurename":$("[name='customerTurename']").val(),"customerDepName":$("[name='customerDepName']").val(),"customerDepId":$("[name='customerDepId']").val(),"customerPhone":$("[name='customerPhone']").val()
                    ,"stationStationName":$("[name='stationStationName']").val(),"operationType":isRegular,"stationEnterTime":$("#stationEnterTime2").val(),"customerRegularSalary":$("#customerRegularSalary2").val()};
            }

            $.ajax({
                type: 'post',
                url: BASE_PATH+'/customerManager/cusomerAddNew.htm',
                data: dataParams,
                async:false,
                success: function (data) {
                    if (data.success) {
                        $("#showDialogDiv").html("员工<span>"+($("[name='customerTurename']").val())+"</span><i>添加成功</i>");
                        $("#showDialogDiv").show();
                        setTimeout(function(){
                            $("#showDialogDiv").hide();
                        },1000);
                        $('.mengban').hide();
                        $('.customerAdd').hide();
                        //刷新主页面
                        var paramsType={"type":$("#typeHid").val(),"customerTurename":$("#customerTurename").val(),"pageIndex":0,"isSortEnterTime":$("#isSortEnterTime").val()};
                        queryListByCondition(paramsType);
                    }else{
                        layer.alert(data.message, {icon: 1});
                    }
                }
            });
        }
    });
    // 新增取消
    $('.btnCloseAdd').on('click',function(){
        $('.mengban').hide();
        $('.customerAdd').hide();
    });

    //调薪确认
    $('.onCustomerUpdateSalary').on('click',function(){
        testBaseUpdateSalary();
        testTakeSalary();
        if(fbaseSalary&&ftakeSalary) {
            if ($("input[name='salary']").val() == $("input[name='customerOldSalary']").val()) {
                layer.alert("金额未发生变化", {icon: 1});
                return false;
            }
            var assemParams = {
                "date": $("input[name='date']").val(),
                "salary": $("input[name='salary']").val(),
                "customerId": $("input[name='updateSalaryCustomerId']").val()
            };
            $.ajax({
                type: 'post',
                url: BASE_PATH + '/customerSalary/updateSalary.htm',
                data: assemParams,
                success: function (data) {
                    if (data.success) {
                        $("#showDialogDiv").html("员工<span>" + ($("#updateSalaryCustomerName").text()) + "</span><i>调薪设置成功</i>");
                        $("#showDialogDiv").show();
                        setTimeout(function () {
                            $("#showDialogDiv").hide();
                        }, 1000);
                        $('.mengban').hide();
                        $('.customerUpdateSalary').hide();
                        //刷新主页面
                        var paramsType = {
                            "type": $("#typeHid").val(),
                            "customerTurename": $("#customerTurename").val(),
                            "pageIndex": 0,
                            "isSortEnterTime": $("#isSortEnterTime").val()
                        };
                        queryListByCondition(paramsType);
                    } else {
                        showWarning(data.message);
                    }
                }
            });
        }
    });
// 批量上传员工
    // 批量上传员工取消
    $('#sureUploadFile').on('click',function(){
        $('.mengban').hide();
        $('.customerBatchImport').hide();
        // $('.preTan').hide()
    });
    //关闭上传文件窗口
    $("#closeUploadFile").click(function(){
        $('.mengban').hide();
        $('.customerBatchImport').hide();
    });
    // 进度条
    // function progressBar(){
    //     var speed=1;
    //     var timer=setInterval(function(){
    //         var width=$('.progressBar-box').width();
    //         console.log(width)
    //         $('.progressBar-box').css('width',(width+speed)+'px');
    //         $('.progressBar-number span').html((parseInt(width/400*100))+'%');
    //         if(width==400){
    //             clearInterval(timer);
    //             $('.progressBar-number').html('上传完成')
    //         };
    //
    //     },200)
    // }
    //
    // // 点击导入
    // $('.into').on('click',function(){
    //     $('.upload').hide();
    //     $('.into').hide();
    //     $('.text').hide();
    //     $('.progressBar').show();
    //     progressBar();
    // })

    var uploader = WebUploader.create({

        auto: true,//自动上传
        // sendAsBinary:true,//以流的形式传到后台
        // swf文件路径
        swf: BASE_PATH + '/resource/js/customer/new/Uploader.swf',

        // 文件接收服务端。
        server: BASE_PATH+'/customerManager/customerUploadBatchNew.htm',

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        // pick: '#exportFileDiv',
        pick: {
            id: '#exportFileDiv',
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
        accept: {
            title: 'Excels',
            extensions: 'xls,xlsx'
        }
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
            $("#exportFileDiv").show();
            $("#showSure").hide();
            $("#showCancel").hide();
            $('.mengban').show();
            $('.customerBatchImport').show();
        }
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on('uploadProgress', function (file, percentage) {
        uploadFile=file;
        $("#showA").hide();
        $("#showB").hide();
        $("#showSure").hide();
        $("#exportFileDiv").hide();
        $("#showCancel").show();
        var progressNumber = (percentage * 100) + '%';
        $('#uploadProgres').html(progressNumber);
        $('.progress-bar').css('width', progressNumber);
    });

    uploader.on('uploadSuccess', function (file, response) {
        if (response.success) {
            $("#showDialogDiv").html(response.message+"<i>上传成功</i>");
            $("#showDialogDiv").show();
            setTimeout(function(){
                $("#showDialogDiv").hide();
            },1000);
            $('.mengban').hide();
            $('.customerBatchImport').hide();
            //刷新主页面
            var paramsType={"type":$("#typeHid").val(),"customerTurename":$("#customerTurename").val(),"pageIndex":0,"isSortEnterTime":$("#isSortEnterTime").val()};
            queryListByCondition(paramsType);
        } else {
            var errorList=response.data;
            if(errorList!=null && errorList.length>0){
                $('.preTan').hide();
                $('#' + file.id).find('.uploadProgres').fadeOut();//上传完成后隐藏进度条
                uploader.removeFile(file, true);
                $("#showSure").show();
                $("#exportFileDiv").hide();
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
                $("#exportFileDiv").show();
                $("#showSure").hide();
                $("#showCancel").hide();
                $('.mengban').show();
                $('.customerBatchImport').show();
                $('.preTan').hide();
                $('#' + file.id).find('.uploadProgres').fadeOut();//上传完成后隐藏进度条
                uploader.removeFile(file, true);
                layer.alert(response.message, {title: '上传失败'}, function (index) {

                        layer.close(index);
                    });
            }
        }
    });

    uploader.on( 'uploadError', function( file ) {
        layer.alert(response.message, {title: '上传失败'}, function (index) {
            layer.close(index);
        });
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
    uploader.on('uploadComplete', function (file) {
        $('.preTan').hide();
        $('#' + file.id).find('.uploadProgres').fadeOut();//上传完成后隐藏进度条
        uploader.removeFile(file, true);
    });

    $("#showSure").click(function(){
        $('.mengban').hide();
        $('.customerBatchImport').hide();
    });
});
//..........................................日期验证
//调薪
    // 生效日期
    function testTakeSalary(){
        if($(".takeDate input").val()=='' || $(".takeDate input").val()==undefined ){
            $('.takeDate b').html('请输入调薪后基本工资');
            $(".takeDate input").addClass('error');
            ftakeSalary = false;
        }else{
            $('.takeDate b').html('');
            $(".takeDate input").removeClass('error');
            ftakeSalary = true;
        }
    }

// 离职验证
    // 最后工作日
    function testLastTime(){
        if($(".lastTime input").val()=='' || $(".lastTime input").val()==undefined ){
            $('.lastTime b').html('请选择最后工作日');
            $(".lastTime input").addClass('error');
            flastTime = false;
        }else{
            $('.lastTime b').html('');
            $(".lastTime input").removeClass('error');
            flastTime = true;
        }
    };

// 转正
    // 更改后的时间
    function testChangeDate(){
        if($(".changeDate input").val()=='' || $(".changeDate input").val()==undefined ){
            $('.changeDate b').html('请选择更改后的转正时间');
            $(".changeDate input").addClass('error');
            fchangeDate = false;
        }else{
            $('.changeDate b').html('');
            $(".changeDate input").removeClass('error');
            fchangeDate = true;
        }
    }
//新增员工
    //..验证入职日期
    function testEnterData(){
        if(isRegular==1){
            if($(".AddEnterDate input").val()!='' && $(".AddEnterDate input").val()!=undefined ){
                $('.AddEnterDate b').html('');
                $(".AddEnterDate input").removeClass('error');
                fenterdata = true;
            }else{
                $('.AddEnterDate b').html('请选择员工入职时间');
                $(".AddEnterDate input").addClass('error');
                fenterdata = false;
            }
        }else if(isRegular==2){
            if($(".AddEnterDateRegular input").val()!='' && $(".AddEnterDateRegular input").val()!=undefined ){
                $('.AddEnterDateRegular b').html('');
                $(".AddEnterDateRegular input").removeClass('error');
                fenterdata = true;
            }else{
                $('.AddEnterDateRegular b').html('请选择员工入职时间');
                $(".AddEnterDateRegular input").addClass('error');
                fenterdata = false;
            }
        }

    };
    //..验证转正日期
    function testTakeData(){
        if(isRegular==1){
            if($(".AddTakeDate input").val()!='' && $(".AddTakeDate input").val()!=undefined ){
                $('.AddTakeDate b').html('');
                $(".AddTakeDate input").removeClass('error');
                ftakedata = true;
            }else{
                $('.AddTakeDate b').html('请选择员工转正时间');
                $(".AddTakeDate input").addClass('error');
                ftakedata = false;
            }
        }else if(isRegular==2){
            ftakedata = true;
        }

    };
// 离职(弹窗出现)
function onCustomerDimission(obj){
    var customerId=$(obj).attr("customerId");
    $("#dimissCustomerId").val(customerId);
    $("#dimissionForm input[name='customerId']").val(customerId);
    $("#dimissionCustomerName").val($(obj).attr("dimissionCustomerName"));
    $('.customerDimission-title').find('span').html($(obj).attr("dimissionCustomerName"));
    $("#shebaoDimissTypeFirst").hide();
    $("#shebaoDimissTypeSecond").hide();
    $("#shebaoDimissTypeSecond").attr("disabled","disabled");
    $("#gjjDimissTypeFirst").hide();
    $("#gjjDimissTypeSecond").hide();
    $("#gjjDimissTypeSecond").attr("disabled","disabled");
    $("#stationDimissingTime").val('');
    $('.mengban').show();
    $('.customerDimission').show();
}
//点击离职确认按钮
function dimissionSureBtn(){
    testLastTime();
    // testSocial();
    // testFund();
    if(flastTime){
        $.ajax({
            type: 'post',
            url: BASE_PATH+'/customerManager/customerDimissionNew.htm',
            data:$("#dimissionForm").serialize(),
            async:false,
            success: function (data) {
                if (data.success) {
                    $("#showDialogDiv").html("员工<span>"+$("#dimissionCustomerName").val()+"</span>离职<i>设置成功</i>");
                    $("#showDialogDiv").show();
                    setTimeout(function(){
                        $("#showDialogDiv").hide();
                    },1000);
                    $('.mengban').hide();
                    $('.customerDimission').hide();
                    //刷新主页面
                    var paramsType={"type":$("#typeHid").val(),"customerTurename":$("#customerTurename").val(),"pageIndex":0,"isSortEnterTime":$("#isSortEnterTime").val()};
                    queryListByCondition(paramsType);
                } else {
                    layer.alert(data.message, {icon: 1});
                }
            }
        });
    }

}

//调薪(弹窗出现)
function onCustomerUpdateSalary(customerId){
    $(".baseUpdateSalary input").val('');
    $(".takeDate input").val('');
    $('.baseUpdateSalary b').html('');
    $(".baseUpdateSalary input").removeClass('error');
    $('.takeDate b').html('');
    $(".takeDate input").removeClass('error');
    $("#fd").text(0);
    $.ajax({
        type: 'post',
        url: BASE_PATH+'/customerManager/updateSalaryInit.htm',
        data:{"customerId":customerId},
        async:false,
        success: function (data) {
            if(data.success){
                var futureSalary=data.data.futureSalary;
                var futureDate=data.data.futureDate;
                var customersBean=data.data.customersBean;
                var payrollId=data.data.payrollId;
                var customerStation=data.data.customerStation;
                var currentSalary=data.data.currentSalary;
                $("#updateSalaryCustomerName").html(customersBean.customerTurename);
                $("input[name='customerOldSalary']").val(currentSalary);
                $("input[name='updateSalaryCustomerId']").val(customerId);
                // if(futureSalary!=undefined && futureSalary!=''){
                //     $("input[name='salary']").val(futureSalary);
                // }else{
                //     $("input[name='salary']").val('');
                // }
                // if(futureDate!=undefined && futureDate!=''){
                //     $("input[name='date']").val(futureDate);
                // }else{
                //     $("input[name='date']").val('');
                // }
                $('.mengban').show();
                $('.customerUpdateSalary').show();
            }else{
                layer.alert(data.message, {icon: 1});
            }

        }
    });
}

//删除
function onCustomerDeleteSalary(customerId){
    $.ajax({
        type: 'post',
        url: BASE_PATH+'/customerManager/customerDelete.htm',
        data:{"customerId":customerId},
        async:false,
        success: function (data) {
            if (data.success) {
                $("#showDialogDiv").html("删除成功");
                $("#showDialogDiv").show();
                setTimeout(function(){
                    $("#showDialogDiv").hide();
                },1000);
                // $('.mengban').hide();
                // $('.customerAdd').hide();
                //刷新主页面
                var paramsType={"type":$("#typeHid").val(),"customerTurename":$("#customerTurename").val(),"pageIndex":0,"isSortEnterTime":$("#isSortEnterTime").val()};
                queryListByCondition(paramsType);
            } else {
                layer.alert(data.message, {icon: 1});
            }
        }
    });
}

//转正(弹窗出现)
function onCustomerRegular(obj){
    var regularTime=$(obj).attr("regularTime");
    var customerId=$(obj).attr("customerId");
    var customerName=$(obj).attr("customerName");
    $("#regularTimeOld").val(regularTime);
    $("#regularCustomerId").val(customerId);
    $("#regularCustomerName").val(customerName);
    $("#regularTimeNew").val('');
    $('.mengban').show();
    $('.customerRegular').show();
}
/**
 * 转正确认按钮
 */
function regularSureBtn(){
    testChangeDate();
    if(fchangeDate){
        $.ajax({
            type: 'post',
            url: BASE_PATH+'/customerManager/customerRegularNew.htm',
            data:{"customerId":$("#regularCustomerId").val(),"stationRegularTime":$("#regularTimeNew").val(),"regularTimeOld":$("#regularTimeOld").val()},
            async:false,
            success: function (data) {
                if (data.success) {
                    $("#showDialogDiv").html("员工<span>"+$("#regularCustomerName").val()+"</span>更改转正时间<i>转正成功</i>");
                    $("#showDialogDiv").show();
                    setTimeout(function(){
                        $("#showDialogDiv").hide();
                    },1000);
                    $('.mengban').hide();
                    $('.customerRegular').hide();
                    //刷新主页面
                    var paramsType={"type":$("#typeHid").val(),"customerTurename":$("#customerTurename").val(),"pageIndex":0,"isSortEnterTime":$("#isSortEnterTime").val()};
                    queryListByCondition(paramsType);
                } else {
                    layer.alert(data.message, {icon: 1});
                }
            }
        });
    }
}

// 新增员工(弹窗出现)
function onCustomerAdd(){
    $("[name='customerTurename']").val('');
    $("[name='customerDepName']").val('');
    $("[name='customerDepId']").val('');
    $("[name='customerPhone']").val('');
    $("[name='stationStationName']").val('');
    $("#stationEnterTime1").val('');
    $("#customerProbationSalary1").val('');
    $("#stationRegularTime1").val('');
    $("#customerRegularSalary1").val('');
    $("#stationEnterTime2").val('');
    $("#customerRegularSalary2").val('');
    isRegular=1;//试用期
    $("#enterShowTr1").show();
    $("#enterShowTr2").show();
    $("#regularShowTr").hide();
    $('.AddState input').eq(0).attr("checked","checked");
    $('.AddState input').eq(1).removeAttr("checked");
    $('.mengban').show();
    $('.customerAdd').show();
    $('.noUserName').hide();
}

// // 批量上传员工
// function onUpload(){
//     var data=$('.upload').attr('data');
//     if(data==1){
//         $('.customerBatchImportShow').animate({'top':'20px'},150);
//         // $('.customerBatchImport-hint').hide()
//         $('.text').toggle(150,function(){
//             $('.upload').attr('data','2');
//
//         });
//         $('.upload span').html('点击关闭上传要求').parent().find('i').html('<')
//     };
//     if(data==2){
//         $('.customerBatchImportShow').animate({'top':'140px'},150);
//         // $('.customerBatchImport-hint').hide()
//         $('.text').toggle(150,function(){
//             $('.upload').attr('data','1').find('i').html('>')
//         })
//         $('.upload span').html('点击展开上传要求').parent().find('i').html('>')
//     };
// }
// 批量上传员工(弹窗出现)
function onCustomerBatchImport(){
    $("#showA").attr("data",2);
    // onUpload();
    $("#showA").show();
    $("#showB").show();
    $("#showC").hide();
    $("#exportFileDiv").show();
    $("#showSure").hide();
    $("#showCancel").hide();
    $('.mengban').show();
    $('.customerBatchImport').show();
}

// 个人信息
function onCustomerName(customerId){
    window.location.href=BASE_PATH+'/customerManager/toCustomerEditPageNew.htm?customerId='+customerId;
}
/**
 * 更新查询的列表
 * @param params
 */
function queryListByCondition(params){
    //获取数量
    $.ajax({
        type: 'post',
        url: BASE_PATH+'/customerManager/queryCountByCondition.htm',
        async:true,
        success: function (data) {
            var totalCount=data.totalCount;
            var enterCount=data.enterCount;
            var tryCount=data.tryCount;
            var willCount=data.willCount;
            var leaveCount=data.leaveCount;
            $(".customer-main-top span[param='1'] a").text(totalCount);
            $(".customer-main-top span[param='2'] a").text(enterCount);
            $(".customer-main-top span[param='3'] a").text(tryCount);
            $(".customer-main-top span[param='4'] a").text(willCount);
            $(".customer-main-top span[param='5'] a").text(leaveCount);
        }
    });
    //获取列表
    $.ajax({
        type: 'post',
        url: BASE_PATH+'/customerManager/queryListByCondition.htm',
        data: params,
        async:true,
        success: function (data) {
            if (data.success) {
                var customerDtos=data.data;
                paginator=data.paginator;
                //表格头部
                var showCustomerThead=$("#showCustomerTab").find("thead");
                var theadHtml="";
                theadHtml += "<tr>";
                theadHtml += "<td>序号</td>";
                theadHtml += "<td>姓名</td>";
                theadHtml += "<td>手机</td>";
                theadHtml += "<td>部门</td>";
                if($("#isSortEnterTime").val()==1){
                    theadHtml += "<td onclick='sortEnterTime(this);' style='cursor: pointer'>入职日期 <img src='../../resource/img/customer/new/rank-top.png' alt=''></td>";
                }else{
                    theadHtml += "<td onclick='sortEnterTime(this);' style='cursor: pointer'>入职日期 <img src='../../resource/img/customer/new/rank-bottom.png' alt=''></td>";
                }
                theadHtml += "<td>转正日期</td>";
                theadHtml += "<td>最新基本工资</td>";
                theadHtml += "<td>津贴方案</td>";

                if(params.type ==4){
                    theadHtml += "<td>离职日期</td>";
                }else{
                    theadHtml += "<td class='lastTd'>状态</td>";
                }
                theadHtml += "</tr>";
                showCustomerThead.html(theadHtml);

                var showCustomerTbody=$("#showCustomerTab").find("tbody");
                var tbodyHtml="";
                for(var i=0;i<customerDtos.length;i++){
                    var customerDto=customerDtos[i];
                    tbodyHtml += "<tr>";
                    tbodyHtml += "<td>"+(i+1)+"</td>";//序号
                    tbodyHtml += "<td class='customerName' onclick='onCustomerName("+customerDto.customerId+");'>"+customerDto.customerTurename+"</td>";//姓名
                    tbodyHtml += "<td>"+customerDto.customerPhone+"</td>";//手机
                    tbodyHtml += "<td>"+(customerDto.depName==undefined?'':customerDto.depName)+"</td>";//部门
                    tbodyHtml += "<td>"+fastToDateCustomerMain(customerDto.stationEnterTime)+"</td>";//入职日期
                    tbodyHtml += "<td>"+fastToDateCustomerMain(customerDto.stationRegularTime)+"</td>";//转正日期
                    if (typeof(customerDto.currentSalary) === "undefined" || !customerDto.currentSalary){
                        tbodyHtml += "<td>&nbsp;</td>";//最新基本工资
                    }
                    else{
                        tbodyHtml += "<td>"+customerDto.currentSalary+"</td>";//最新基本工资
                    }
                    if (typeof(customerDto.allowanceWay) === "undefined" || !customerDto.allowanceWay){
                        tbodyHtml += "<td>&nbsp;</td>";//津贴方案
                    }
                    else{
                        tbodyHtml += "<td>"+customerDto.allowanceWay+"</td>";//津贴方案
                    }

                    tbodyHtml += "<td class='lastTd1'>";//状态
                    if(params.type ==1){//全部
                        tbodyHtml += "<b>在职</b>";
                    }else if(params.type ==2){//正式
                        tbodyHtml += "<span>";
                        tbodyHtml += "<a onclick='onCustomerDimission(this)' customerId='"+(customerDto.customerId)+"' dimissionCustomerName='"+customerDto.customerTurename+"'>离职</a>";
                        tbodyHtml += "<a onclick='onCustomerUpdateSalary("+customerDto.customerId+")'>调薪</a>";
                        tbodyHtml += "</span>";
                    }else if(params.type ==3){//试用期
                        tbodyHtml += "<span>";
                        tbodyHtml += "<a onclick='onCustomerDimission(this)' customerId='"+(customerDto.customerId)+"' dimissionCustomerName='"+customerDto.customerTurename+"'>离职</a>";
                        var regularTime=fastToDateCustomerMain(customerDto.stationRegularTime);
                        regularTime=regularTime==undefined?'':regularTime;
                        tbodyHtml += "<a onclick='onCustomerRegular(this)' regularTime='"+regularTime+"' customerId='"+(customerDto.customerId)+"' customerName='"+customerDto.customerTurename+"'>提前转正</a>";
                        tbodyHtml += "</span>";
                    }else if(params.type ==4){//即将离职,无操作
                        tbodyHtml += "<b>"+fastToDateCustomerMain(customerDto.stationDimissingTime)+"</b>";
                    }else if(params.type ==5){//已离职
                        tbodyHtml += "<span>";
                        tbodyHtml += "<a onclick='onCustomerDeleteSalary("+customerDto.customerId+")'>删除</a>";
                        tbodyHtml += "</span>";
                    }
                    tbodyHtml += "</td>";
                    tbodyHtml += "</tr>";
                }
                showCustomerTbody.html(tbodyHtml);
                if(params.type ==1){
                    $('.lastTd').html('状态');
                }else{
                    $('.lastTd').html('操作');
                }
                generPageHtml();
            } else {
                layer.alert(data.message, {icon: 1});
            }
        }
    });

    //分页
    // $(".tcdPageCode").createPage({
    //     pageCount: 10, //总页数
    //     current:1, //当前页
    //     backFn:function(p){
    //         //单击回调方法，p是当前页码
    //     }
    // });
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
                // var i=$("<i class='show'>")
                // li.append(i)
                dg(li, item)

                ul.append(li);
//                $('li:has(ul) i').addClass('ali')
//

            });
            parent.append(ul);
            // $('li').not(':has(ul)').find('i').removeClass('show')
            // $('.show').html('+');
            // $('.show').parent().find('ul').hide();
//            $('.branch .dep-item:first').removeClass('dep-item')
        }
        // $('.branch span:first').removeClass('dep-item')
        $('.dep-item').on('click',function(){
            $('#customerDepName').val($(this).html());
            $('#depId').val($(this).data("depid"));
            $('.branch').hide();
            $('.show').off('click')
            //去掉显示的错误验证
            $('.customerDepName b').html('');
            $(".customerDepName #customerDepName").removeClass('error');
            faddDept = true;
        })

        //调用树形菜单
        $("#dep-base").treeview({
            animated: "fast",
            collapsed: true,
            unique: true,
            // persist: "cookie",
            toggle: function() {
                // window.console && console.log("%o was toggled", this);
            }
        });

    }



    $('.customerDepName input').on('click',function(e){
        e.stopPropagation();
        $('.show').off('click');
        $('.branch').toggle();
        // 判断 是否有子元素
        // $('.show').on('click',function(){
        //
        //     if($(this).html()=='+'){
        //         $(this).siblings('ul').toggle(200)
        //         $(this).html('-');
        //     }else{
        //         $(this).siblings('ul').toggle(200);
        //         $(this).html('+');
        //     }
        //
        // })
    });


    // 去除冒泡
    $(document).on('click',function(){
        $('.branch').hide();
        $('.show').off('click');
    })
    $('.branch').on('click',function(e){
        e.stopPropagation();
    })

}
//日期控件回调验证
function lastDatachange(){
    testLastTime();
}
function takeDateChange(){
    testTakeSalary();
}
function enterDataChange(){
    testEnterData();
}
function takeDataChange(){
    testTakeData();
}
function changeDateChange(){
    testChangeDate();
}
function dateChange(dates){
    var str = $("#stationDimissingTime").val();
    if(str!=null
        ||str!=""||str!=undefined) {
        // if ( !compareTime(str)) {
        //     $("#isRegular").attr("checked", true);
        //     //添加颜色
        //     $("#customerCurrentSalary").css({
        //         "background-color":"#A9A9A9"
        //     });
        //     $("#customerCurrentSalary").attr("readonly","readonly");
        //
        // } else {
            $("#isRegular").removeAttr("checked");
            //移除样式
            $("#customerCurrentSalary").removeAttr("style");
            //移除readOnly
            $("#customerCurrentSalary").removeAttr("readonly");

            // var dimissTime=$(obj).val();//离职时间
            console.log(str);
            if(str!=undefined && str!=''){
                $.ajax({
                    type: 'post',
                    url: BASE_PATH+'/customerManager/toCustomerDimissionPageNew.htm',
                    data:{"customerId":$("#dimissCustomerId").val(),"dimissTime":str},
                    async:false,
                    success: function (data) {
                        if (data.success) {
                            var customerResponse=data.data;
                            var shebaoDimissShowType=customerResponse.shebaoDimissShowType;
                            var gjjDimissShowType=customerResponse.gjjDimissShowType;
                            if(shebaoDimissShowType==1){
                                $("#shebaoDimissTypeFirst").hide();
                                $("#shebaoDimissTypeSecond").hide();
                                $("#shebaoDimissTypeSecond").attr("disabled","disabled");
                            }else if(shebaoDimissShowType==2){
                                $("#shebaoDimissTypeFirst").html("<em>*</em>缴纳离职月社保：");
                                var html="";
                                html += "<input type='radio'  value='1' name='isSbKeepState'>缴纳 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                                html += "<input type='radio'  value='0' name='isSbKeepState'>不缴纳";
                                html += "<b></b>";
                                $("#shebaoDimissTypeSecond").html(html);
                                $("#shebaoDimissTypeSecond").removeAttr("disabled");
                                $("#shebaoDimissTypeFirst").show();
                                $("#shebaoDimissTypeSecond").show();
                            }else if(shebaoDimissShowType==3){
                                $("#shebaoDimissTypeFirst").html("<em>*</em>提示：");
                                $("#shebaoDimissTypeSecond").html("离职月社保已缴纳,系统将自动停缴:"+customerResponse.willTjMonth);
                                $("#shebaoDimissTypeSecond").removeAttr("disabled");
                                $("#shebaoDimissTypeFirst").show();
                                $("#shebaoDimissTypeSecond").show();
                            }else if(shebaoDimissShowType==4){
                                $("#shebaoDimissTypeFirst").html("<em>*</em>提示：");
                                $("#shebaoDimissTypeSecond").html("离职月账单已提交,系统将自动停缴:"+customerResponse.willTjMonth);
                                $("#shebaoDimissTypeSecond").removeAttr("disabled");
                                $("#shebaoDimissTypeFirst").show();
                                $("#shebaoDimissTypeSecond").show();
                            }else if(shebaoDimissShowType==5){
                                $("#shebaoDimissTypeFirst").html("<em>*</em>提示：");
                                $("#shebaoDimissTypeSecond").html("已设置社保停缴:"+customerResponse.shebaoTjMonth);
                                $("#shebaoDimissTypeSecond").removeAttr("disabled");
                                $("#shebaoDimissTypeFirst").show();
                                $("#shebaoDimissTypeSecond").show();
                            }

                            if(gjjDimissShowType==1){
                                $("#gjjDimissTypeFirst").hide();
                                $("#gjjDimissTypeSecond").hide();
                                $("#gjjDimissTypeSecond").attr("disabled","disabled");
                            }else if(gjjDimissShowType==2){
                                $("#gjjDimissTypeFirst").html("<em>*</em>缴纳离职月公积金：");
                                var html="";
                                html += "<input type='radio'  value='1' name='isGjjKeepState'>缴纳 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                                html += "<input type='radio'  value='0' name='isGjjKeepState'>不缴纳";
                                html += "<b></b>";
                                $("#gjjDimissTypeSecond").html(html);
                                $("#gjjDimissTypeSecond").removeAttr("disabled");
                                $("#gjjDimissTypeFirst").show();
                                $("#gjjDimissTypeSecond").show();
                            }else if(gjjDimissShowType==3){
                                $("#gjjDimissTypeFirst").html("<em>*</em>提示：");
                                $("#gjjDimissTypeSecond").html("离职月公积金已缴纳,系统将自动停缴:"+customerResponse.willTjMonth);
                                $("#gjjDimissTypeSecond").removeAttr("disabled");
                                $("#gjjDimissTypeFirst").show();
                                $("#gjjDimissTypeSecond").show();
                            }else if(gjjDimissShowType==4){
                                $("#gjjDimissTypeFirst").html("<em>*</em>提示：");
                                $("#gjjDimissTypeSecond").html("离职月账单已提交,系统将自动停缴:"+customerResponse.willTjMonth);
                                $("#gjjDimissTypeSecond").removeAttr("disabled");
                                $("#gjjDimissTypeFirst").show();
                                $("#gjjDimissTypeSecond").show();
                            }else if(gjjDimissShowType==5){
                                $("#gjjDimissTypeFirst").html("<em>*</em>提示：");
                                $("#gjjDimissTypeSecond").html("已设置公积金停缴:"+customerResponse.shebaoTjMonth);
                                $("#gjjDimissTypeSecond").removeAttr("disabled");
                                $("#gjjDimissTypeFirst").show();
                                $("#gjjDimissTypeSecond").show();
                            }
                        } else {
                            layer.alert(data.message, {icon: 1});
                        }
                    }
                });
            }
        // }
    }
}

function compareTime(str){
    if(str!=null) {
        str = str.replace(/-/g, "/");
        var endTime = new Date(str);
        var nowTime = new Date();
        var time = endTime.getTime() - nowTime.getTime();
        if (time > 0) {
            return true
        } else {
            return false;
        }
    }
}

function onCustomerCompletion(){
    window.location.href=BASE_PATH+'/customerManager/toCustomerCompletionPageNew.htm';
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
                var params={"type":$("#typeHid").val(),"pageIndex":obj.curr,"isSortEnterTime":$("#isSortEnterTime").val()};
                queryListByCondition(params);
            }
        }
    });
}

function changeUpdateSalary(){
    // var mq = parseFloat("$!{customer.customerCurrentSalary}");

    var mq = parseFloat($("[name='customerOldSalary']").val());
    var tx = parseFloat($("input[name='salary']").val());

    // if(tx === mq) {
    //     $("#salary").next("span").css("display", "block")
    // }else{
    //     $("#salary").next("span").css("display", "none")
    // }
    if($("input[name='salary']").val() == '' || isNaN($("input[name='salary']").val()) || mq == 0){
        $("#fd").html("0");
    }else {
        $("#fd").html(parseInt((tx - mq) / mq * 100)+'%');
    }
}

function sortEnterTime(obj){
    var isSortEnterTime=$("#isSortEnterTime").val();
    if(isSortEnterTime==1){
        isSortEnterTime=2;
        $(obj).find('img').attr('src','../../resource/img/customer/new/rank-bottom.png')
        $("#isSortEnterTime").val(isSortEnterTime)

    }else if(isSortEnterTime==2){
        isSortEnterTime=1;
        $(obj).find('img').attr('src','../../resource/img/customer/new/rank-top.png')
        $("#isSortEnterTime").val(isSortEnterTime)

    }
    var params={"type":$("#typeHid").val(),"pageIndex":0,"isSortEnterTime":isSortEnterTime};
    queryListByCondition(params);
}

function fastToDateCustomerMain(fastDate) {
    if (fastDate) {
        var date = new Date(fastDate);
        Y = date.getFullYear() + '-';
        M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
        D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate() + '';
        return Y + M + D;
    }
    return '';
}