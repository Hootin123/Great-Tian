$(function(){
    departmentInit();
    //基本信息编辑及保存 
$('.onEditBase').on('click',function(){
    var data=$(this).attr('data');
    var element=$(this);
    if(data==0){
        // 编辑
        $('.baseInfo-main input').prop('disabled','').addClass('active');
        $(this).attr('data','1').html('保存');


    };
    //保存
    if(data==1){
            testUsername();
            // testSex();
            testPhone();
            // testBirthday()
        
        if( fusername&&fphone){

            $.ajax({
                type: 'post',
                url: BASE_PATH+'/customerManager/modifyCustomerBaseNew.htm',
                async:false,
                data:$("#editBaseForm").serialize(),
                success: function (resultResponse) {
                    console.log(resultResponse);
                    if (resultResponse.success) {
                        $('.baseInfo-main input').prop('disabled','true').removeClass('active');
                        $(element).attr('data','0').html('编辑');
                    } else {
                        layer.alert(resultResponse.message, {icon: 1});
                    }
                }
            });
        }
    }
    
});
// 岗位信息编辑及保存
$('.onEditStation').on('click',function(){
    var data=$(this).attr('data');
    var element=$(this);
    // 编辑
    if(data==0){
        $('.bit input').prop('disabled','').addClass('active');
        $(this).attr('data','1').html('保存');
    };
    //保存
    if(data==1){
        testCustomerNumber();
        testPositiveData()
        testStation();
        testDepartment()
        if(fnumber&&fpositivedata&&fstation&&fdepartment){
            $.ajax({
                type: 'post',
                url: BASE_PATH+'/customerManager/modifyCustomerStationNew.htm',
                async:false,
                data:$("#editStationForm").serialize(),
                success: function (resultResponse) {
                    console.log(resultResponse);
                    if (resultResponse.success) {
                        $('.bit input').prop('disabled','true').removeClass('active');
                        $(element).attr('data','0').html('编辑');
                    } else {
                        layer.alert(resultResponse.message, {icon: 1});
                    }
                }
            });
        }
    }
});
// 身份证编辑及保存
$('.onEditCard').on('click',function(){
    var data=$(this).attr('data');
    var element=$(this);
    // 编辑
    if(data==0){
        $('.cardInfo-main input').prop('disabled','').addClass('active');
        $(this).attr('data','1').html('保存');

    };
    //保存
    if(data==1){
        testCard();
        testOpenbank();
        testBanknumber();
        testFront();
        if(fcard&&fbanknumber&&fopenbank&&front){

            $.ajax({
                type: 'post',
                url: BASE_PATH+'/customerManager/modifyCustomerIdcardNew.htm',
                async:false,
                data:$("#editIdcardForm").serialize(),
                success: function (resultResponse) {
                    console.log(resultResponse);
                    if (resultResponse.success) {
                        $('.cardInfo-main input').prop('disabled','true').removeClass('active');
                        $(element).attr('data','0').html('编辑');
                    } else {
                        layer.alert(resultResponse.message, {icon: 1});
                    }
                }
            });


        }
        
    }
});
// 验证
var fusername = false,fenglishName=false,fsex = false,fphone = false,fbirthday=false,fcard = false,fbanknumber = false,fopenbank = false,front = false;
var flinkman = false,femphone = false,fchart = false,fnation = false,fstate=false,fplace=false,fschool=false,fzy=false;
 fpositivedata=false,fdepartment=false,fhire=false,fstation=false,fnumber=false;
// 验证用户名
$('.userName input').blur(function(){
    testUsername();
});

function testUsername(){
    if($(".userName input").val()=='' || $(".userName input").val()==undefined ){
        $('.userName b').html('请输入用户名');
        $('.userName input').addClass('error');
        fusername = false;
    }
    else if($(".userName input").val().length>=1 && $(".userName input").val().length<=20){
        $('.userName b').html('')
          $('.userName input').removeClass('error');
        fusername = true;
    }
    else{
        $('.userName b').html('请输入正确的用户名！');
        $('.userName input').addClass('error');
        fusername = false;
    }
}
// 验证性别
//     $('.sex input').change(function(){
//     testSex()
// })
// function testSex(){
//     if($('.sex input').get(0).checked || $('.sex input').get(1).checked){
//         $('.sex b').html('')
//         fsex=true;
//     }else{
//         $('.sex b').html('请选择性别！')
//         fsex=false;
//     }
// }

 //....验证手机号

$('.phone input').blur(function(){
    testPhone();
});

function testPhone(){
    var  reg=/^1[3|5|7|8]\d{9}$/;
    if($(".phone input").val()=='' || $(".phone input").val()==undefined ){
        $('.phone b').html('请输入手机号');
        $(".phone input").addClass('error');
        fphone = false;
    }
    else if(reg.test($(".phone input").val())){
        $('.phone b').html('');
        $(".phone input").removeClass('error');
        fphone = true;
    }
    else{
        $('.phone b').html('请输入正确的手机号！');
        $(".phone input").addClass('error');
        fphone = false;
    }
}


 //...验证工号

$('.customerNumber input').blur(function(){
    testCustomerNumber();
});

function testCustomerNumber(){
   
        if($(".customerNumber input").val().length>20){
            $('.customerNumber b').html('工号不能超过20位');
            $(".customerNumber input").addClass('error');
            fnumber = false;
        }else{
            $('.customerNumber b').html('');
             $(".customerNumber input").removeClass('error');
            fnumber = true;
        }

}


//验证生日
// function testBirthday(){
//     if($(".birthday input").val()=='' || $(".birthday input").val()==undefined ){
//         $('.birthday b').html('请输入生日');
//         $(".birthday input").css('border','1px solid #f00');
//         fbirthday = false;
//     }else{
//         $('.birthday b').html('');
//          $(".birthday input").css('border','1px solid #cce1e9')
//         fbirthday = true;
//     }
// };

 //...验证岗位

$('.stations input').blur(function(){
    testStation();
});

function testStation(){
    if($(".stations input").val()=='' || $(".stations input").val()==undefined ){
        $('.stations b').html('请输入岗位');
        $(".stations input").addClass('error');
        fstation = false;
    }
    else if($(".stations input").val().length>=1 && $(".stations input").val().length<=16){
        $('.stations b').html('');
        $(".stations input").removeClass('error');
        fstation = true;
    }
    else{
        $('.stations b').html('请输入正确的岗位！');
        $(".stations input").addClass('error');
        fstation = false;
    }
}
 //...验证身份证号码
$('.card input').blur(function(){
    testCard();
});
function testCard(){
    var  reg=/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
    if($(".card input").val()=='' || $(".card input").val()==undefined ){
        $('.card b').html('请输入身份证号码');
        $(".card input").addClass('error');
        fcard = false;
    }
    else if(reg.test($(".card input").val())){
        $('.card b').html('');
        $(".card input").removeClass('error');
        fcard = true;
    }
    else{
        $('.card b').html('请输入正确的身份证号码！');
         $(".card input").addClass('error');
        fcard = false;
    }
}

//...验证银行卡号


$('.banknumber input').blur(function(){
    testBanknumber();
});

function testBanknumber(){
    var  reg=/^\d{13,23}$/;
    if($(".banknumber input").val()=='' || $(".banknumber input").val()==undefined ){
        $('.banknumber b').html('请输入工资卡卡号');
        $(".banknumber input").addClass('error');
        fbanknumber = false;
    }
    else if(reg.test($(".banknumber input").val())){
        $('.banknumber b').html('');
        $(".banknumber input").removeClass('error');
        fbanknumber = true;
    }
    else{
        $('.banknumber b').html('请输入正确的工资卡卡号！');
        $(".banknumber input").addClass('error');
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
        $('.openbank b').html('请输入工资卡开户行');
        $(".openbank input").addClass('error');
        fopenbank = false;
    }
    else if($(".openbank input").val().length>=2 && $(".openbank input").val().length<=16){
        $('.openbank b').html('');
        $(".openbank input").removeClass('error');
        fopenbank = true;
    }
    else{
        $('.openbank b').html('请填写正确的工资卡开户行');
        $(".openbank input").addClass('error');
        fopenbank = false;
    }
}

//...验证身份证
function testFront(){
    if($('.cardPhoto').find("input[type='hidden']").val()=='' || $('.cardPhoto').find("input[type='hidden']")==undefined){
        $('#idCardImagId b').html('请添加身份证');
        front = false;
    }else {
        $('#idCardImagId b').html('');
        front = true;
    }

};

});

//..验证部门
function testDepartment(){
    if($(".customerDepName input").val()!='' &&  $(".customerDepName input").val()!=undefined ){
        $('.customerDepName b').html('');
        $('.customerDepName input').removeClass('error');
        fdepartment = true;
    }else{
        $('.customerDepName b').html('请输入部门');
        $('.customerDepName input').addClass('error');
        fdepartment = false;
    }
}

//..验证转正日期
function testPositiveData(){
    var stationState=$("[name='stationCustomerState']").val();
    if(stationState!=2){
        if($(".positiveData input").val()!='' && $(".positiveData input").val()!=undefined ){
            $('.positiveData b').html('');
            $(".positiveData input").removeClass('error');
            fpositivedata = true;
        }else{
            $('.positiveData b').html('请输入转正日期');
            $(".positiveData input").addClass('error');
            fpositivedata = false;
        }
    }else{
        fpositivedata = true;
    }

};
/**
/**
/**
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
            testDepartment()
            $('.show').off('click')
        });
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

function dateChange(dates){
    testPositiveData();
    var str = $("#stationRegularTime").val();
    if(str!=null
        ||str!=""||str!=undefined) {
        if ( !compareTime(str)) {
            $("#isRegular").attr("checked", true);
            //添加颜色
            $("#customerCurrentSalary").css({
                "background-color":"#A9A9A9"
            });
            $("#customerCurrentSalary").attr("readonly","readonly");

        } else {
            $("#isRegular").removeAttr("checked");
            //移除样式
            $("#customerCurrentSalary").removeAttr("style");
            //移除readOnly
            $("#customerCurrentSalary").removeAttr("readonly");

        }
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
/**
 * 上传身份证图片
 * @param fileId
 * @param showId
 * @param hideId
 */
function uploadApproveImg(fileId,showId,hideId){
    $.ajaxFileUpload({
        url:BASE_PATH+ '/customerManager/uploadIdCardImgNew.htm', //用于文件上传的服务器端请求地址
        fileElementId: fileId, //文件上传域的ID
        dataType: 'json', //返回值类型 一般设置为json
        success: function (data){
            if(data.success) {
                $("#"+showId).attr("src","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+data.data);
                $("#"+hideId).val(data.data);
            }
        }
    });
}
