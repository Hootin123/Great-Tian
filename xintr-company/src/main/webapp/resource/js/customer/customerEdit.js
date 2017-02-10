var fusername = false,fenglishName=false,fsex = false,fphone = false,fbirthday=false,fcard = false,fbanknumber = false,fopenbank = false,front = false;
var flinkman = false,femphone = false,fchart = false,fnation = false,fstate=false,fplace=false,fschool=false,fzy=false;
var fenterdata=false,fdepartment=false,fhire=false,fstation=false,fnumber=false;
$(function(){

    $("#isRegular").click(function(){
        if($("#isRegular").attr("checked")){
            var str = $("[name='stationRegularTime']").val();
            if(str!=null
                && str!="" && str!=undefined) {
                if ( compareTimeEdit(str)) {
                    $("[name='stationRegularTime']").val('');
                }
            }
            compareTimeEdit(str);
        }else{
            $("[name='stationRegularTime']").val('');
        }
    });


    //..验证用户名

    $('.username input').blur(function(){
        testUsername();
    });

    function testUsername(){
        if($(".username input").val()=='' || $(".username input").val()==undefined ){
            $('.username span').html('请输入用户名').addClass('error').show();
            fusername = false;
        }
        else if($(".username input").val().length>=2 && $(".username input").val().length<=16){
            $('.username span').html('').removeClass('error').hide();
            fusername = true;
        }
        else{
            $('.username span').html('请输入正确的用户名！').addClass('error').show();
            fusername = false;
        }
    }

    //验证英文名
    $('.englishName input').blur(function(){
        testEnglishName();
    });

    function testEnglishName(){
        if($(".englishName input").val()!='' && $(".englishName input").val()!=undefined ){
            if($(".englishName input").val().length>=2 && $(".englishName input").val().length<=16){
                $('.englishName span').html('').removeClass('error').hide();
                fenglishName = true;
            }else{
                $('.englishName span').html('请输入正确的英文名！').addClass('error').show();
                fenglishName = false;
            }
        }else{
            fenglishName = true;
        }
    }

    //...验证性别
    $('.sex input').change(function(){
        testSex()
    })
    function testSex(){
        if($('.sex input').get(0).checked || $('.sex input').get(1).checked){
            $('.sex span').html('').removeClass('error').hide();
            fsex=true;
        }else{
            $('.sex span').html('请选择性别！').addClass('error').show();
            fsex=false;
        }
    }


    //...验证身份证号码
    $('.card input').blur(function(){
        testCard();
    });
    function testCard(){
        var  reg=/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
        if($(".card input").val()=='' || $(".card input").val()==undefined ){
            $('.card span').html('请输入身份证号码').addClass('error').show();
            fcard = false;
        }
        else if(reg.test($(".card input").val())){
            $('.card span').html('').removeClass('error').hide();
            fcard = true;
        }
        else{
            $('.card span').html('请输入正确的身份证号码！').addClass('error').show();
            fcard = false;
        }
    }
    //....验证手机号

    $('.phone input').blur(function(){
        testPhone();
    });

    function testPhone(){
        var  reg=/^1[3|5|7|8]\d{9}$/;
        if($(".phone input").val()=='' || $(".phone input").val()==undefined ){
            $('.phone span').html('请输入手机号').addClass('error').show();
            fphone = false;
        }
        else if(reg.test($(".phone input").val())){
            $('.phone span').html('').removeClass('error').hide();
            fphone = true;
        }
        else{
            $('.phone span').html('请输入正确的手机号！').addClass('error').show();
            fphone = false;
        }
    }

    //...验证银行卡号


    $('.banknumber input').blur(function(){
        testBanknumber();
    });

    function testBanknumber(){
        var  reg=/^\d{15,20}$/;
        if($(".banknumber input").val()=='' || $(".banknumber input").val()==undefined ){
            $('.banknumber span').html('请输入工资卡卡号').addClass('error').show();
            fbanknumber = false;
        }
        else if(reg.test($(".banknumber input").val())){
            $('.banknumber span').html('').removeClass('error').hide();
            fbanknumber = true;
        }
        else{
            $('.banknumber span').html('请输入正确的工资卡卡号！').addClass('error').show();
            fbanknumber = false;
        }
    }

    //..验证开户行
    $('.openbank input').blur(function(){
        testOpenbank();
    });

    function testOpenbank(){
        var  reg=/^[\u4e00-\u9fa5]{2,16}$/;
        if($(".openbank input").val()=='' || $(".openbank input").val()==undefined ){
            $('.openbank span').html('请输入开户行').addClass('error').show();
            fopenbank = false;
        }
        else if($(".openbank input").val().length>=2 && $(".openbank input").val().length<=16){
            $('.openbank span').html('').removeClass('error').hide();
            fopenbank = true;
        }
        else{
            $('.openbank span').html('请输入正确的开户行！').addClass('error').show();
            fopenbank = false;
        }
    }

    //...验证身份证
    function testFront(){
        if($('.front').find("input[type='hidden']").val()=='' || $('.front').find("input[type='hidden']")==undefined){
            $('.front span').html('请添加身份证').addClass('error').show();
            front = false;
        }else {
            $('.front span').html('').removeClass('error').hide();
            front = true;
        }

    };

    //验证聘用形式
    function testHire(){
        if($('.hire input').get(0).checked || $('.hire input').get(1).checked){
            $('.hire span').html('').removeClass('error').hide();
            fhire=true;
        }else{
            $('.hire span').html('请选择聘用形式！').addClass('error').show();
            fhire=false;
        }
    }
    //...验证岗位

    $('.stations input').blur(function(){
        testStation();
    });

    function testStation(){
        if($(".stations input").val()=='' || $(".stations input").val()==undefined ){
            $('.stations span').html('请输入岗位').addClass('error').show();
            fstation = false;
        }
        else if($(".stations input").val().length>=2 && $(".stations input").val().length<=16){
            $('.stations span').html('').removeClass('error').hide();
            fstation = true;
        }
        else{
            $('.stations span').html('请输入正确的岗位！').addClass('error').show();
            fstation = false;
        }
    }

    //...验证工号

    $('.customerNumber input').blur(function(){
        testCustomerNumber();
    });

    function testCustomerNumber(){
        if($(".customerNumber input").val()=='' || $(".customerNumber input").val()==undefined ){
            if($(".customerNumber input").val().length>20){
                $('.customerNumber span').html('工号不能超过20位').addClass('error').show();
                fnumber = false;
            }else{
                $('.customerNumber span').html('').removeClass('error').hide();
                fnumber = true;
            }
        }else{
            fnumber = true;
        }
    }

    //..验证紧急联系人

    $('.linkman input').blur(function(){
        testLinkman();
    });
    function testLinkman(){
        if($(".linkman input").val()=='' || $(".linkman input").val()==undefined ){
            $('.linkman span').html('请输入紧急联系人').addClass('error').show();
            flinkman = false;
        }
        else if($(".linkman input").val().length>=2 && $(".linkman input").val().length<=16){
            $('.linkman span').html('').removeClass('error').hide();
            flinkman = true;
        }
        else{
            $('.linkman span').html('请输入正确的紧急联系人！').addClass('error').show();
            flinkman = false;
        }
    }

    //....验证紧急联系人电话

    $('.emphone input').blur(function(){
        testEmPhone();
    });
    function testEmPhone(){
        var  reg=/^1[3|5|7|8]\d{9}$/;
        if($(".emphone input").val()=='' || $(".emphone input").val()==undefined ){
            $('.emphone span').html('请输入联系电话').addClass('error').show();
            femphone = false;
        }
        else if(reg.test($(".emphone input").val())){
            $('.emphone span').html('').removeClass('error').hide();
            femphone = true;
        }
        else{
            $('.emphone span').html('请输入正确的联系电话！').addClass('error').show();
            femphone = false;
        }
    }

    //..验证户籍地址

    $('.chart input').blur(function(){
        testChart();
    });

    function testChart(){
        var  reg=/^[\u4e00-\u9fa5]{2,16}$/;
        if($(".chart input").val()=='' || $(".chart input").val()==undefined ){
            $('.chart span').html('请输入户籍地址').addClass('error').show();
            fchart = false;
        }
        else if($(".chart input").val().length>=2 && $(".chart input").val().length<=16){
            $('.chart span').html('').removeClass('error').hide();
            fchart = true;
        }
        else{
            $('.chart span').html('请输入正确的户籍地址！').addClass('error').show();
            fchart = false;
        }
    }

    $('.nation input').blur(function(){
        testNation();
    });
    function testNation(){
        var  reg=/^[\u4e00-\u9fa5]{2,16}$/;
        if($(".nation input").val()!='' && $(".nation input").val()!=undefined ){
            if($(".nation input").val().length>=2 && $(".nation input").val().length<=16){
                $('.nation span').html('').removeClass('error').hide();
                fnation = true;
            }else{
                $('.nation span').html('请输入正确的民族！').addClass('error').show();
                fnation = false;
            }
        }else{
            fnation = true;
        }
    }

    $('.state input').blur(function(){
        testState();
    });
    function testState(){
        if($(".state input").val()!='' && $(".state input").val()!=undefined ){
            if($(".state input").val().length>=2 && $(".state input").val().length<=16){
                $('.state span').html('').removeClass('error').hide();
                fstate = true;
            }else{
                $('.state span').html('请输入正确的政治面貌！').addClass('error').show();
                fstate = false;
            }
        }else{
            fstate = true;
        }
    }

    $('.place input').blur(function(){
        testPlace();
    });
    function testPlace(){
        if($(".place input").val()!='' && $(".place input").val()!=undefined ){
            if($(".place input").val().length>=2 && $(".place input").val().length<=16){
                $('.place span').html('').removeClass('error').hide();
                fplace = true;
            }else{
                $('.place span').html('请输入正确的现居住地！').addClass('error').show();
                fplace = false;
            }
        }else{
            fplace = true;
        }
    }

    $('.school input').blur(function(){
        testSchool();
    });
    function testSchool(){
        if($(".school input").val()!='' && $(".school input").val()!=undefined ){
            if($(".school input").val().length>=2 && $(".school input").val().length<=16){
                $('.school span').html('').removeClass('error').hide();
                fschool = true;
            }else{
                $('.school span').html('请输入正确的毕业学校！').addClass('error').show();
                fschool = false;
            }
        }else{
            fschool = true;
        }
    }

    $('.zy input').blur(function(){
        testZy();
    });
    function testZy(){
        if($(".zy input").val()!='' && $(".zy input").val()!=undefined ){
            if($(".zy input").val().length>=2 && $(".zy input").val().length<=16){
                $('.zy span').html('').removeClass('error').hide();
                fzy = true;
            }else{
                $('.zy span').html('请输入正确的毕业学校！').addClass('error').show();
                fzy = false;
            }
        }else{
            fzy = true;
        }
    }

    $("#baseInfoBtn").on("click",function(){
        testUsername();
        testEnglishName();
        testSex();
        testPhone();
        testBirthday();
        testFront();
        var ffCount=$("#ffCount").val();
        if(parseInt(ffCount)<=0){//没有代发协议,开户行,开户银行,身份证号不用必填
            if($(".openbank input").val()!='' && $(".openbank input").val()!=undefined ){//验证开户行
                if($(".openbank input").val().length>=2 && $(".openbank input").val().length<=16){
                    $('.openbank span').html('').removeClass('error').hide();
                    fopenbank = true;
                }else{
                    $('.openbank span').html('请输入正确的开户行！').addClass('error').show();
                    fopenbank = false;
                }
            }else{
                $('.openbank span').html('').removeClass('error').hide();
                fopenbank = true;
            }

            var  reg=/^\d{15,20}$/;
            if($(".banknumber input").val()!='' && $(".banknumber input").val()!=undefined ){//验证开户银行
                if(reg.test($(".banknumber input").val())){
                    $('.banknumber span').html('').removeClass('error').hide();
                    fbanknumber = true;
                }else{
                    $('.banknumber span').html('请输入正确的工资卡卡号！').addClass('error').show();
                    fbanknumber = false;
                }
            }else{
                $('.banknumber span').html('').removeClass('error').hide();
                fbanknumber = true;
            }


            var  reg=/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
            if($(".card input").val()!='' && $(".card input").val()!=undefined ){//验证身份证号
                $('.card span').html('请输入身份证号码').addClass('error').show();
                fcard = false;
                if(reg.test($(".card input").val())){
                    $('.card span').html('').removeClass('error').hide();
                    fcard = true;
                }else{
                    $('.card span').html('请输入正确的身份证号码！').addClass('error').show();
                    fcard = false;
                }
            }else{
                $('.card span').html('').removeClass('error').hide();
                fcard = true;
            }
        }else{//有代发协议,开户行,开户银行,身份证号必填
            testCard();
            testBanknumber();
            testOpenbank();
        }
        if( fusername & fenglishName & fsex & fphone  & fcard & fbanknumber & fopenbank & front & fbirthday){
            $.ajax({
                type: 'post',
                url: 'modifyCustomerBase.htm',
                async:false,
                data:$("#editBaseForm").serialize(),
                success: function (resultResponse) {
                    console.log(resultResponse);
                    if (resultResponse.success) {
                        layer.alert('修改成功',{
                            title:'处理结果',
                            cancel:function(){
                                parent.loadCustomerList();
                                var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                                parent.layer.close(index);
                            }
                        },function(index){
                            parent.loadCustomerList();
                            var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                            parent.layer.close(index);
                            //loadCustomerList();
                        });
                    } else {
                        layer.alert(resultResponse.message, {icon: 1});
                    }
                }
            });

        }
    });

    $("#baseInfoEditBtn").on("click",function(){
        console.log("cancel");
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
        //parent.popupboxclosed($('.popupbox'));
    });

    $("#stationForm .okBtn").on("click",function(){
        testEnterData();
        testHire();
        testDepartment();
        testStation();
        testCustomerNumber();
        if(fhire & fstation & fnumber & fenterdata & fdepartment){
            //获取转正信息
            var url='modifyCustomerStation.htm?isRegular=2';
            if($("#isRegular").is(':checked')){
                console.log("转正");
                url='modifyCustomerStation.htm?isRegular=1';
            }
            $.ajax({
                type: 'post',
                url: url,
                async:false,
                data:$("#stationForm").serialize(),
                success: function (resultResponse) {
                    console.log(resultResponse);
                    if (resultResponse.success) {
                        layer.alert('修改成功',{
                            title:'处理结果',
                            cancel:function(){
                                parent.loadCustomerList();
                                var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                                parent.layer.close(index);
                            }
                        },function(index){
                            parent.loadCustomerList();
                            var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                            parent.layer.close(index);
                            //loadCustomerList();
                        });
                    } else {
                        layer.alert(resultResponse.message, {icon: 1});
                    }
                }
            });
        }

    });
    $("#stationForm .cancelBtn").on("click",function(){
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
    });

    $("#personalForm .okBtn").on("click",function(){
        testLinkman();
        testEmPhone();
        testChart();
        testNation();
        testState();
        testPlace();
        testSchool();
        testZy();
        if(flinkman & femphone & fchart & fnation & fstate & fplace & fschool & fzy){
            $.ajax({
                type: 'post',
                url: 'modifyCustomerPersonal.htm',
                async:false,
                data:$("#personalForm").serialize(),
                success: function (resultResponse) {
                    console.log(resultResponse);
                    if (resultResponse.success) {
                        layer.alert('修改成功',{
                            title:'处理结果',
                            cancel:function(){
                                parent.loadCustomerList();
                                var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                                parent.layer.close(index);
                            }
                        },function(index){
                            parent.loadCustomerList();
                            var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                            parent.layer.close(index);
                            //loadCustomerList();
                        });
                    } else {
                        layer.alert(resultResponse.message, {icon: 1});
                    }
                }
            });
        }
    });
    $("#personalForm .cancelBtn").on("click",function(){
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
    });
    $("#preImg").dropzone(
        {
            url: "uploadBaseIdCardImg.htm?uploadType='img'",
            maxFiles: 99,
            maxFilesize: 2,
            acceptedFiles: ".jpg,.jpeg,.png,.bmp",
            addedfile: function (file) {
                //更新文件上传进度
                $('.bar_1').css('width', 10);
            },
            complete: function (data) {
                try {
                    console.log(data);
                    var res = eval('(' + data.xhr.responseText + ')');
                    console.log(res);
                    if (res.success) {
                        $("#preImg").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+res.data);
                        $("#customerIdcardImgFront").val(res.data);
                    } else {
                        layer.alert(res.message, {icon: 1});
                    }
                } catch (failed) {
                    layer.alert("上传文件不能超过2M", {icon: 1});
                }
            }
        });
    $("#backImg").dropzone(
        {
            url: "uploadBaseIdCardImg.htm?uploadType='img'",
            maxFiles: 99,
            maxFilesize: 2,
            acceptedFiles: ".jpg,.jpeg,.png,.bmp",
            addedfile: function (file) {
                //更新文件上传进度
                $('.bar_1').css('width', 10);
            },
            complete: function (data) {
                try {
                    console.log(data);
                    var res = eval('(' + data.xhr.responseText + ')');
                    console.log(res);
                    if (res.success) {
                        $("#backImg").attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+res.data);
                        $("#customerIdcardImgBack").val(res.data);
                    } else {
                        layer.alert(res.message, {icon: 1});
                    }
                } catch (failed) {
                    layer.alert("上传文件不能超过2M", {icon: 1});
                }
            }
        });

    $("#uploadContract").dropzone(
        {
            url: "uploadBaseIdCardImg.htm",
            maxFiles: 99,
            maxFilesize: 10,
            //acceptedFiles: ".jpg,.jpeg,.png,.bmp",
            addedfile: function (file) {
                //更新文件上传进度
                $('.bar_1').css('width', 10);
            },
            complete: function (data) {
                try {
                    var res = eval('(' + data.xhr.responseText + ')');
                    if (res.success) {
                        $("[name='stationContractFile']").val(res.data);
                        layer.alert("上传成功", {icon: 1});
                    } else {
                        layer.alert(res.message, {icon: 1});
                    }
                } catch (failed) {
                    layer.alert("上传文件不能超过10M", {icon: 1});
                }
            }
        });

    $("#uploadResume").dropzone(
        {
            url: "uploadBaseIdCardImg.htm",
            maxFiles: 99,
            maxFilesize: 10,
            //acceptedFiles: ".jpg,.jpeg,.png,.bmp",
            addedfile: function (file) {
                //更新文件上传进度
                $('.bar_1').css('width', 10);
            },
            complete: function (data) {
                try {
                    var res = eval('(' + data.xhr.responseText + ')');
                    if (res.success) {
                        $("[name='personalResume']").val(res.data);
                        layer.alert("上传成功", {icon: 1});
                    } else {
                        layer.alert(res.message, {icon: 1});
                    }
                } catch (failed) {
                    layer.alert("上传文件不能超过10M", {icon: 1});
                }
            }
        });

});
function beginDateCallBack(){
    testEnterData()

}
function birtDateCallBack(){
    testBirthday()
}
//验证生日
function testBirthday(){
    if($(".birthday input").val()=='' || $(".birthday input").val()==undefined ){
        $('.birthday span').html('请输入生日').addClass('error').show();
        fbirthday = false;
    }else{
        $('.birthday span').html('').removeClass('error').hide();
        fbirthday = true;
    }
};
//..验证入职日期
function testEnterData(){
    if($(".enterData input").val()!='' && $(".enterData input").val()!=undefined ){
        $('.enterData span').html('').removeClass('error').hide();
        fenterdata = true;
    }else{
        $('.enterData span').html('请输入入职日期').addClass('error').show();
        fenterdata = false;
    }
};
//..验证部门
function testDepartment(){
    if($("#stationDeptName").val()!='' &&  $("#stationDeptName").val()!=undefined ){
        $('#departmentSpan').html('').removeClass('error').hide();
        fdepartment = true;
    }else{
        $('#departmentSpan').html('请输入部门').addClass('error').show();
        fdepartment = false;
    }
}
function dateChange(dates){
    var str = $("[name='stationRegularTime']").val();
    if(str!=null
         && str!="" && str!=undefined) {
        if ( !compareTimeEdit(str)) {
            $("#isRegular").attr("checked", true);
        } else {
            $("#isRegular").removeAttr("checked");
        }
    }
}

function compareTimeEdit(str){
    var flag=false;
    if(str!=null) {
        str = str.replace(/-/g, "/");
        var endTime = new Date(str);
        $.ajax({
            type: 'post',
            url: 'queryNowTime.htm',
            async:false,
            //data:$("#personalForm").serialize(),
            success: function (resultResponse) {
                console.log(resultResponse);
                if (resultResponse.success) {
                    var nowTime = resultResponse.data;
                    var time = endTime.getTime() - nowTime;
                    if (time > 0) {
                        flag= true
                    } else {
                        flag= false;
                    }
                } else {
                    layer.alert(resultResponse.message, {icon: 1});
                }
            }
        });
    }
    return flag;
}