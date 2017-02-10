
// 当前tab
var CUR_TAB = 1;
// 新增的奖金类型数据 | 删除奖金类型数据
var ADD_BONUS_DATA = [], DEL_BONUS_DATA = [];

// 部门id数组，部门名称数组，员工id数组，员工名称数组
var members_map = {}, dept_name_arr = [];

var rowid = 1;

$(function(){
    loadRuleData();
    //计薪规则
    $('.catalogue').find('li').click(function(){
        $(this).addClass('big').siblings('li').removeClass('big');
        $(this).find('div').show().parents('li').siblings('li').find('div').hide();
    });

    $('.data7').hover(function(){
    },function(){
        $('.data7').hide();
    });
    /*$('.data7').find('li').click(function(){
        $('.data3Span').html($(this).html());
        $('.data7').hide();
        if($('.data3Span').html()>1){
            $('.data5').html('次月');
            $('.data6Span').html(Number($('.data3Span').html())-1)
        }else{
            $('.data5').html('当月');
            $('.data6Span').html('月底')
        }
    });*/

    // 选择发薪日期
    $('.send2').click(function(){
        $('.send4').toggle();
    });
    $('.send4').find('li').click(function(){
        $('.send2').html($(this).html());
        $('.send4').hide();
    });
    $('.send4').hover(function(){
    },function(){
        $('.send4').hide();
    });
    $('.send3').click(function(){
        $('.send5').toggle();
    });
    $('.send5').find('li').click(function(){
        $('.send3Span').html($(this).html());
        $('.send5').hide();
    });
    $('.send5').hover(function(){
    },function(){
        $('.send5').hide();
    });
    //计薪方式
    $('.way2').click(function(){
        $('.way4').toggle()
    });
    $('.way4').find('li').click(function(){
        $('.way2').html($(this).html());
        $('.way4').hide();
        if($('.way2').html()==$('.way4 li').eq(1).html()){
            $('.way3').toggle();
        }else{
            $('.way3').toggle();
        }
    });


    $('.way4').hover(function(){
    },function(){
        $(this).hide();
    });
    $('.way3').click(function(){
        $('.way5').toggle()
    });
    $('.way5').find('li').click(function(){
        $('.way3').html($(this).html());
        $('.way5').hide();
    });
    $('.way5').hover(function(){
    },function(){
        $(this).hide();
    });

    //是否计算社保公积金
    $('.goldImg1').click(function(){
        $(this).hide();
        $('.goldImg2').show();
    });
    $('.goldImg2').click(function(){
        $(this).hide();
        $('.goldImg1').show();
    });

    // 津贴设置
    $('.btn').click(function(){
        $('.tryShow').html("");
        $('.set').fadeIn(100).animate({
            top:'0px'
        },500);
        $('.shade').show();
        // 重置数据
        cleanAllowanceData();
        $('.formcCheckbox').show();
        $('.span3').show();
        $('.dept input[type=checkbox]').attr('checked', true);
        $('.span3 input[type=checkbox]').attr('checked', true);
    });
    $('.cancel').click(function(){

        $('.set').hide().animate({
            top:'50px'
        },500);

        $('.shade').hide();
    });

    // 删除津贴行
    $('.nameShow .delete').live('click', function(){

        // 如果是没入库的，则删除SAVEDATA，并且给DELDATApush一行
        var id = $(this).parent('.nameShow').attr('id');
        var rowid = $(this).parent('.nameShow').attr('rowid');
        var _this = $(this);
        if(id && id != ''){
            var param = {
                'delData': JSON.stringify([id])
            };
            sendData(BASE_PATH+'/salary_setting/save_allowance.htm', param, function (result) {
                if(result && result.success){
                    alertInfo('删除成功',function () {
                        _this.parent('.nameShow').remove();
                    });
                } else{
                    if(result && result.message){
                        alertWarn(result.message);
                    } else{
                        alertWarn('删除失败');
                    }
                }
            });
        } else{

            _this.parent('.nameShow').remove();
        }
    });

    // 保存津贴
    $('.save').click(function(){
        $('.shade').hide();

        // 津贴名称
        var allowanceName = $('.allowanceName').val();
        // 津贴备注
        var allowanceDesc = $('.remark1').val();
        var amount = $('.money2').val();
        var mm = $('.month').text();
        // 津贴方式
        var allowanceType = mm == '每月' ? 0 : 1;
        // 是否启用适用范围 0:否 1:是
        var isApplyRange = $('div .scope .img .img2').css('display') == 'none' ? 0 : 1;
        // 缺勤天数大于时，不享受该津贴
        var absenceDay = $('div .scope input:eq(0)').val();

        var formally = $('div .type .dept input[type=checkbox]:eq(0)').attr('checked') ? 1 : 0;

        // 正式转正 0:不适合 1:适合
        var formallyPromotion = $('div .span3 .span3Inp1 input[type=checkbox]:eq(0)').attr('checked') ? 1 : 0;
        // 正式试用期 0:不适合 1:适合
        var formallyProbationPeriod = $('div .span3 .span3Inp2 input[type=checkbox]:eq(0)').attr('checked') ? 1 : 0;

        var labor = $('div .type .dept1 input[type=checkbox]:eq(0)').attr('checked') ? 1 : 0;

        // 劳务转正 0:不适合 1:适合
        var laborPromotion = 0;
        // 劳务试用期 0:不适合 1:适合
        var laborPromotionPeriod = 0;

        if($('div .span33Inp1 input[type=checkbox]:eq(0)').attr('checked')){
            laborPromotion = 1;
        }
        if($('div .span33Inp2 input[type=checkbox]:eq(0)').attr('checked')){
            laborPromotionPeriod = 1;
        }

        var members = {};
        members.all = false;

        var depts_ = [], members_ = [];

        if( $('.tryShow .company').length > 0 ){
            members.all = true;
        }

        $('.tryShow .tryShowCon').each(function (k, v) {

            var deptId = $(v).attr('depid');

            var memberId = $(v).attr('memberid');

            if(deptId && deptId != ''){
                depts_.push(deptId);
            }
            if(memberId && memberId != ''){
                members_.push(memberId);
            }
        });
        members.depts = depts_;
        members.members = members_;

        // console.log('津贴名称：' + allowanceName);
        // console.log('备注：' + allowanceDesc);
        // console.log('金额：' + amount);
        // console.log('每出勤日：' + mm);
        // console.log('是否启用适用范围：' + isApplyRange);
        // console.log('缺勤天数：' + absenceDay);
        // console.log('正式转正：' + formallyPromotion);
        // console.log('正式试用期：' + formallyProbationPeriod);
        // console.log('劳务：' + laborPromotion);
        // console.log('试用部门及员工:' + members);

        if(allowanceName==''){
            alertWarn('请输入津贴名称');
            $('.shade').show();
            return;
        }
        if(amount==''){
            alertWarn('请输入金额');
            $('.shade').show();
            return;
        }
        if(!isMoney(amount)){
            alertWarn('请输入正确的金额');
            $('.shade').show();
            return;
        }
        if(parseFloat(amount) <= 0){
            alertWarn('请输入大于0的金额');
            $('.shade').show();
            return;
        }
        if(allowanceName==''){
            alertWarn('请输入津贴名称');
            $('.shade').show();
            return;
        }

        // 缺勤天数不能为0
        if($('.img1').is(":hidden")){
            if($('.dataText').val()<1){
                alertWarn('缺勤天数必须大于等于1!');
                $('.shade').show();
                return false;
            }
        }

        if(formally == 0 && labor == 0){
            alertWarn('请选择适合类型');
            $('.shade').show();
            return;
        }

        // 适用部门以及员工不能为空
        if($('.tryShow').html()==""){
            alertWarn('请输入适用部门以及员工!');
            $('.shade').show();
            return false;
        }

        var id = $(this).parents('.set').attr('id');

        var setRowid = $(this).parents('.set:eq(0)').attr('rowid');
        var saveRowid = setRowid || rowid;

        if(id && id != ''){

            var updateData = [{
                id : id,
                allowanceName: allowanceName,
                isApplyRange: isApplyRange,
                absenceDay: absenceDay,
                allowanceDesc: allowanceDesc,
                formallyPromotion: formallyPromotion,
                formallyProbationPeriod: formallyProbationPeriod,
                laborPromotion: laborPromotion,
                laborPromotionPeriod: laborPromotionPeriod,
                amount: amount,
                allowanceType: allowanceType,
                members: members
            }];

            var div = $('.conShow div#' + id);
            div.find('.nameText').text(allowanceName);
            div.find('.nameMon span:eq(0)').text(amount);
            div.find('.nameMonth').text(mm);
            div.find('.nameRemark1').text(allowanceDesc);

            div.attr('rowid', setRowid);
            div.attr('isApplyRange', isApplyRange);
            div.attr('absenceDay', absenceDay);
            div.attr('formallyPromotion', formallyPromotion);
            div.attr('formallyProbationPeriod', formallyProbationPeriod);
            div.attr('allowancetype', allowanceType);
            div.attr('laborPromotion', laborPromotion);
            div.attr('laborPromotionPeriod', laborPromotionPeriod);

            var param = {
                'updateData': JSON.stringify(updateData)
            };
            sendData(BASE_PATH+'/salary_setting/save_allowance.htm', param, function (result) {
                if(result && result.success){
                    alertInfo('保存成功', function () {
                        $('.set').hide().animate({
                            top:'50px'
                        },500);
                        rowid++;
                    });
                } else{
                    if(result && result.message){
                        alertWarn(result.message);
                    } else{
                        alertWarn('保存失败');
                    }
                }
            });

        } else{

            var saveData = {
                allowanceName: allowanceName,
                isApplyRange: isApplyRange,
                absenceDay: absenceDay,
                allowanceDesc: allowanceDesc,
                formallyPromotion: formallyPromotion,
                formallyProbationPeriod: formallyProbationPeriod,
                laborPromotion: laborPromotion,
                laborPromotionPeriod: laborPromotionPeriod,
                amount: amount,
                allowanceType: allowanceType,
                members: members
            };

            // 已经存在行
            var nameShow = $('.conShow .nameShow[rowid='+ setRowid +']');

            if(setRowid && nameShow){
                nameShow.attr('rowid', setRowid);
                nameShow.attr('isApplyRange', isApplyRange);
                nameShow.attr('absenceDay', absenceDay);
                nameShow.attr('formallyPromotion', formallyPromotion);
                nameShow.attr('formallyProbationPeriod', formallyProbationPeriod);
                nameShow.attr('laborPromotion', laborPromotion);
                nameShow.attr('laborPromotionPeriod', laborPromotionPeriod);
                nameShow.find('.nameText').text(allowanceName);
                nameShow.find('.nameMon span').text(amount);
                nameShow.find('.nameMonth').text(mm);
                nameShow.find('.nameRemark1').text(allowanceDesc);
            } else{

                var param = {
                    'saveData': JSON.stringify(saveData)
                };
                sendData(BASE_PATH+'/salary_setting/save_allowance.htm', param, function (result) {
                    if(result && result.success){
                        alertInfo('保存成功', function () {

                            var html = '<div id="'+ result.data +'" rowid="'+ rowid +'" class="nameShow" isApplyRange="'+ isApplyRange +'" absenceDay="'+ absenceDay +'" ' +
                                'formallyPromotion="'+ formallyPromotion +'" formallyProbationPeriod="'+ formallyProbationPeriod +'" ' +
                                'laborPromotion="'+ laborPromotion +'" laborPromotionPeriod="'+ laborPromotionPeriod +'" allowancetype="'+ allowanceType + '">' +
                                '   <div class="alter"></div>' +
                                '   <div class="delete"></div>' +
                                '   <div class="nameText">'+allowanceName+'</div>' +
                                '   <div class="sum"><div class="nameMon"><span>'+amount+'</span>元/'+'</div>' +
                                '   <div class="nameMonth">'+mm+'</div>' +
                                '</div>' +
                                '<div class="nameRemark1">'+allowanceDesc+'</div>' +
                                '<div class="nameRemark">备注</div>' +
                                '<div class="nameMoney">金额</div>';

                            $('.conShow').prepend(html);
                            $('.set').hide().animate({
                                top:'50px'
                            },500);
                            rowid++;
                        });
                    } else{
                        if(result && result.message){
                            alertWarn(result.message);
                        } else{
                            alertWarn('保存失败');
                        }
                    }
                });
            }
        }

    });
    $('.month').click(function(){
        $('.monthSelect').toggle();
    });

    $('.monthSelect li').click(function(){
        $(this).parent().parent('.monthSelect').toggle();
        $('.month').html($(this).html());
    });

    $('.img1').click(function(){
        $('.img1').hide();
        $('.img2').show();
        $('.dataText').val(1);
    });

    $('.img2').click(function(){
        $('.img2').hide();
        $('.img1').show();
        $('.dataText').val(0);
    });

    $('.just').click(function(){
        if($(this).prop('checked')){
            $('.formcCheckbox').show();
            $('.span3').show();
            $('.span3 input[type=checkbox]').attr('checked', true);
        }else{
            $('.span3 input[type=checkbox]').attr('checked', false);
            $('.span3').hide();
        }
    });

    $('.service').click(function(){
        if($(this).prop('checked')){
            $('.formcCheckbox').show();
            $('.span33').show();
            $('.span33 input[type=checkbox]').attr('checked', true);
        }else{
            $('.span33 input[type=checkbox]').attr('checked', false);
            $('.span33').hide();
        }
    });

    $('.imgShow2').click(function(){
        var showText = $('.inpShow2').val();
        if(showText == ""){
            alertWarn('请输入奖金类型');
            return false;
        }
        var isRep = false;
        $('.add .addCon').each(function(k, v){
            if(showText == $(this).text()){
                alertWarn('奖金类型不能重复，请重新输入!');
                isRep = true;
                return false;
            }
        });

        if(!isRep){
            var bonusName = $('.inpShow2').val();
            $('.add').append('<div class="addDiv"><div class="addCon">'+ bonusName +'</div><div class="addImg" ></div></div>');
            ADD_BONUS_DATA.push(bonusName);
            $('.inpShow2').val("").parent().hide();
        }

    });
    $('.addImg').live('click', function(){
        var id = $(this).parent('.addDiv').attr('bonusid');
        DEL_BONUS_DATA.push(id);
        $(this).parent().remove();
    });

    $(document).click(function(){
        $(".bonusShow2").hide();
    });

    $('.bonusShow3').click(function(event){
        event.stopPropagation();
        $('.inpShow2').val("");
        $(this).siblings('.bonusShow2').show();
    });
    $('.bonusShow2').click(function(event){
        event.stopPropagation();
    });

    
    $('.use li').on('click',function(){
        $('.bonusShow2').hide();
        var isRep = false;
        var showText = $(this).text();
        $('.add .addCon').each(function(){
            if(showText == $(this).text()){
                alertWarn('奖金类型不能重复，请重新输入!');
                isRep = true;
                return false;
            }
        });

        if(!isRep){
            ADD_BONUS_DATA.push(showText);
            $('.add').append('<div class="addDiv"><div class="addCon">'+ showText +'</div><div class="addImg" ></div></div>');
        }

    });
    // 点击记薪规则
    $('.big').click(function(){
        $('.idea').show();
        $('.allowance').hide();
        $('.bonus').hide();
        CUR_TAB = 1;
        loadRuleData();
        $('.footerBtn').show();
    });
    // 点击津贴设置
    $('.subsidy').click(function(){
        $('.idea').hide();
        $('.allowance').show();
        $('.bonus').hide();
        CUR_TAB = 2;
        loadAllowanceData();
        $('.footerBtn').hide();
    });
    // 点击奖金设置
    $('.prize').click(function(){
        $('.idea').hide();
        $('.allowance').hide();
        $('.bonus').show();
        CUR_TAB = 3;
        ADD_BONUS_DATA = [];
        $('.add').html('');
        loadData(BASE_PATH + '/salary_setting/bonus_settings.htm',{},function(result){
            if(result && result.success && result.data && result.data.length > 0){
                for(i in result.data){
                    var item = result.data[i];
                    var html = '<div class="addDiv" bonusId='+ item.id +'>' +
                        '           <div class="addCon">'+ item.bonusName +'</div>' +
                        '           <div class="addImg"></div>' +
                        '       </div>';
                    $('.add').append(html);
                }
            }
        });
        $('.footerBtn').show();
    });

    //修改津贴设置
    $('.alter').live('click',function () {
        $('.shade').show();
        cleanAllowanceData();

        var nameShow = $(this).parent('.nameShow');

        // 适合出勤范围开关
        var isApplyRange = nameShow.attr('isapplyrange');
        // 缺勤天数
        var absenceDay = nameShow.attr('absenceday');
        // 正式转正
        var formallyPromotion = nameShow.attr('formallypromotion');
        // 正式试用期
        var formallyProbationPeriod = nameShow.attr('formallyprobationperiod');
        // 劳务转正
        var laborPromotion = nameShow.attr('laborpromotion');
        // 劳务试用期
        var laborPromotionPeriod = nameShow.attr('laborPromotionPeriod');

        // 津贴id
        var id = nameShow.attr('id');
        // 赋值数据
        $('.set').attr('id', id);
        $('.set').attr('rowid', nameShow.attr('rowid'));

        if(id && id != ''){

            loadData(BASE_PATH + '/salary_setting/allowancemembers/'+ id +'.htm',{}, function (result) {
                $('.tryShow').html("");
                if(result && result.success && result.data){
                    // 全部员工
                    if(result.data.all){
                        console.log(result.data.all);
                        var all = result.data.all;
                            $('.tryShow').append('<div class="tryShowCon company" companyid="' + all.companyId + '"><div class="tryShowText">' + all.companyName + '</div><div class="tryShowImg"></div></div>');
                    }
                     //部门
                    if(result.data.depts && result.data.depts.length > 0){
                        console.log(result.data.depts);
                        for(var b in result.data.depts){
                            var section = result.data.depts[b];
                                 $('.tryShow').append('<div class="tryShowCon" depid="'+ section.deptId +'"><div class="tryShowText">'+ section.deptName +'</div><div class="tryShowImg"></div></div>');
                        }
                    }
                    // 员工

                    if(result.data.members && result.data.members.length > 0){
                        console.log(result.data.members);
                        for(var c in result.data.members){
                            var staff = result.data.members[c];
                                $('.tryShow').append('<div class="tryShowCon" memberid="' + staff.customerId + '"><div class="tryShowText">' + staff.customerName + '</div><div class="tryShowImg"></div></div>');
                        }
                    }
                }
            });
        }

        // 津贴名称
        $('.set .allowanceName').val( nameShow.find('.nameText').text() );

        // 备注
        $('.set .remark1').val( nameShow.find('.nameRemark1').text() );

        // 金额
        $('.set .money2').val( nameShow.find('.nameMon span:eq(0)').text() );

        // 每月/每出勤日
        $('.set .month').text( nameShow.find('.nameMonth').text() );

        if(isApplyRange == 1){
            $('.set .scope .img1').css('display', 'none');
            $('.set .scope .img2').css('display', 'inline');
        } else{
            $('.set .scope .img2').css('display', 'none');
            $('.set .scope .img1').css('display', 'inline');
        }
        //缺勤天数
        $('.set .scope input').val(absenceDay);

        // 满足正式
        if(formallyPromotion==1||formallyProbationPeriod==1){
            // dagou
            $('.set .span3').css('display','block');
            $('.set .dept input[type=checkbox]').attr('checked', true);
            $('.set .formcCheckbox').show();
            $('.set .span3').show();
            if(formallyPromotion==1){
                $('.set .span3Inp1 input[type=checkbox]').attr('checked', true);
            }
            if(formallyProbationPeriod==1){
                $('.set .span3Inp2 input[type=checkbox]').attr('checked', true);
            }
        }

        // 满足劳务
        if(laborPromotion == 1 || laborPromotionPeriod == 1){

            $('.set .span33').css('display','block');
            $('.set .dept1 input[type=checkbox]').attr('checked', true);
            $('.set .formcCheckbox').show();
            $('.set .span33').show();
            if(laborPromotion==1){
                $('.set .span33Inp1 input[type=checkbox]').attr('checked', true);
            }
            if(laborPromotionPeriod==1){
                $('.set .span33Inp2 input[type=checkbox]').attr('checked', true);
            }
        }

        $('.set').fadeIn(100).animate({
            top:'0px'
        },500);

    });

    //alert(1)
    // 保存更新
    $('.footerBtn1').on('click', function () {
        // 记薪规则
        if(CUR_TAB == 1){
            var isOpen = $('.gold2 .goldImg2').css('display');
            var params = {
                'payStartType': 1,
                'payStartDay': $('.data3Span').text(),
                'payEndType': $('.data5').text() == '当月' ? 0 : 1,
                'payEndDay': $('.data6Span').text(),
                'payDayType': $('.send2').text() == '当月' ? 0 : 1,
                'payDay': $('.send3Span').text(),
                'payWay': $('.way2').text() == '实际工作日计薪法' ? 2 : 1,
                'payCriticalPoint': $('.way3').text() == '以11天为临界点' ? 0 : 2,
                'isSocialSecurity': isOpen == 'none' ? 0 : 1
            };
            sendData(BASE_PATH + '/salary_setting/save_payrule.htm', params, function (result) {
                if (result && result.success) {
                    alertInfo('保存成功', function () {
                        //allPop("../salary_setting/allowance.htm");
                        //$('.popupbox').hide();
                    });
                } else {
                    if (result && result.message) {
                        alertWarn(result.message);
                    } else {
                        alertWarn('保存失败');
                    }
                }
            });
        }

        // 保存奖金设置
        if(CUR_TAB == 3){
            if( (ADD_BONUS_DATA && ADD_BONUS_DATA.length > 0) || (DEL_BONUS_DATA && DEL_BONUS_DATA.length > 0) ){
                var param = {
                    'bonusNames': JSON.stringify(ADD_BONUS_DATA),
                    'delIds' : JSON.stringify(DEL_BONUS_DATA)
                };
                sendData(BASE_PATH+'/salary_setting/save_bonus_settings.htm', param, function (result) {
                    if(result && result.success){
                        alertInfo('保存成功', function () {
                            ADD_BONUS_DATA = [];
                            DEL_BONUS_DATA = [];
                        });
                    } else{
                        if(result && result.message){
                            alertWarn(result.message);
                        } else{
                            alertWarn('保存失败');
                        }
                    }
                });
            }
        }
    });

    // 取消按钮，关闭津贴设置ifarem窗口
    $('.footerBtn2').click(function(){
        // allPopClose();
        parent.location.reload();
        //var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        //parent.layer.close(index);

    });

    //取消关闭
    $('#closeDetailBtn').click(function () {
        parent.location.reload();
    });
});

/**
 * 加载记薪规则数据
 */
function loadRuleData(){
    loadData(BASE_PATH + '/salary_setting/payrule.htm',{}, function (result) {
        if(result && result.success && result.data){
            var item = result.data;
            // $('.data3Span').text(item.payStartDay);
            // $('.data5').text(item.payEndType==0 ? '当月':'次月');
            // $('.data6Span').text(item.payEndDay);
            $('.send2').text(item.payDayType==0 ? '当月':'次月');
            $('.send3Span').text(item.payDay);
            $('div.way2').text(item.payWay==1? '21.75天标准计薪法' : '实际工作日计薪法');
            if(item.payWay==2){
                $('.way3').hide();
            }
            $('div.way3').text(item.payCriticalPoint==0? '以11天为临界点' : '以21.75天为临界点');
            if(item.isSocialSecurity == 1){
                $('img.goldImg1').css('display', 'none');
                $('img.goldImg2').css('display', 'inline');
            } else{
                $('img.goldImg1').css('display', 'inline');
                $('img.goldImg2').css('display', 'none');
            }
        } else{
            //选择计薪开始日期
            $('.data3').click(function(){
                $('.data7').show();
            });
        }
    });
}

/**
 * 加载津贴设置数据
 */
function loadAllowanceData(){
    $('.conShow').html('');
    loadData(BASE_PATH + '/salary_setting/get_allowances.htm',{}, function (result) {
        if(result && result.success && result.data){

            for(i in result.data){
                var item = result.data[i];

                var nameMon = item.allowanceType == 0 ? '每月' : '每出勤日';

                var html = '<div rowid="'+ rowid +'" id="'+ item.id +'" class="nameShow" isApplyRange="'+ item.isApplyRange +'" absenceDay="'+ item.absenceDay +'" ' +
                    'formallyPromotion="'+ item.formallyPromotion +'" formallyProbationPeriod="'+ item.formallyProbationPeriod +'" ' +
                    'laborPromotion="'+ item.laborPromotion +'" laborPromotionPeriod="'+ item.laborPromotionPeriod +'">' +
                    '           <div class="alter"></div>' +
                    '           <div class="delete"></div>   ' +
                    '           <div class="nameText">'+ item.allowanceName +'</div>   ' +
                    '           <div class="sum">' +
                    '               <div class="nameMon"><span>'+ item.amount +'</span>元/</div>   ' +
                    '               <div class="nameMonth">'+ nameMon +'</div>' +
                    '           </div>' +
                    '           <div class="nameRemark1">'+ item.allowanceDesc +'</div>' +
                    '           <div class="nameRemark">备注</div>' +
                    '           <div class="nameMoney">金额</div>' + '' +
                    '       </div>';

                $('.conShow').append(html);
                rowid++;
            }

        }
    });
}

/**
 * 清空添加津贴div数据
 */
function cleanAllowanceData(){
    $('.allowanceName').val('');
    $('.set input[type=text]').val('');
    $('.set').removeAttr('id');
    $('.set').removeAttr('rowid');
    $('.set .scope input[type=text]').val('0');
    $('.set input[type=checkbox]').attr('checked', false);
    $('.set .scope .img .img1').css('display', 'inline');
    $('.set .scope .img .img2').css('display', 'none');
    $('.set .span3').hide();
    $('.set .span33').hide();
}

/**
 * 加载数据
 *
 * @param url       请求url
 * @param params    参数列表
 * @param callback  回调函数
 */
function loadData(url, params, callback){
    //$.getJSON(url, params, callback);
    $.ajax({
        type:"GET",
        url:url,
        data:params,
        cache:false,
        dataType:"json",
        success:callback
    });
}

/**
 * 发送数据
 *
 * @param url       请求url
 * @param params    参数列表
 * @param callback  回调函数
 */
function sendData(url, params, callback){
    $.post(url, params, callback);
}

// 添加部门及员工
$('.tryOutAdd').on('click',function(){
    $('.trySeek').animate({
        'top':0
    },500);

    $('.trySeekCont').html('');
    $("#append").html('');

    members_map = {}, dept_name_arr = [];

    loadData(BASE_PATH + '/salary_setting/get_orgmembers.htm',{},function(result){
        if(result && result.success && result.data){
            var data = result.data;
            var html = '<ul>';

            depts = data.depts;

            html += '<li class="sectionLi pl-20" companyid="'+ data.companyId +'">'+ data.companyName +'</li>';

            for( i in data.depts ){
                var dept = data.depts[i];

                html += '<li class="sectionLi " depId="'+ dept.depId +'">'+ dept.depName +'</li>';

                var members = dept.members;
                if(members && members.length > 0){
                    html += '<div class="memberName">';

                    for(j in members){

                        var member = members[j];

                        members_map[member.customerId] = member;

                        html += '<dt class="memberDt" customerid="'+ member.customerId +'">'+ member.customerName +'</dt>';
                    }
                    html += '</div>';
                }
            }
            html += '</ul>';
            $('.trySeekCont').append(html);
            $('.sectionLi').click(function(){

                var compamyId = $(this).attr('companyid');
                var showHtml = $(this).text();

                if(compamyId && compamyId != ''){
                    $('.tryShow').html("");
                    $('.tryShow').append('<div class="tryShowCon company" companyid="'+ compamyId +'"><div class="tryShowText">'+ showHtml +'</div><div class="tryShowImg"></div></div>');
                    $('.trySeek').animate({
                        'top':'280px'
                    },500);
                    $('#kw').val("")

                } else{
                    var isRep1 = false;
                    $('.tryShowCon .tryShowText').each(function(){
                        if(showHtml == $(this).text()){
                            alertWarn('部门不能重复!');
                            isRep1 = true;
                            return false;
                        }
                    });

                    if(isRep1==false){
                        if( $('.tryShow .company').length==0){
                            var depid = $(this).attr('depid');
                            $('.tryShow').append('<div class="tryShowCon" depid="'+ depid +'"><div class="tryShowText">'+ showHtml +'</div><div class="tryShowImg"></div></div>');
                            $('.trySeek').animate({
                                'top':'280px'
                            },500);
                            $('#kw').val("")
                        }
                    }
                }

            });
        }
    });
});

$('.tryShowImg').live('click',function(){
    $(this).parents('.tryShowCon').remove();
});


$('.trySeekImg').on('click',function(){
    $('.trySeek').animate({
        'top':'280px'
    },500);
    $('#kw').val("")
});

$('.first #kw').click(function(){

    $("#append").html('');

    if(dept_name_arr.length > 0){
        var temp = '';
        for(var i in dept_name_arr){
            var dept = dept_name_arr[i];
            if(dept && dept.depName){
                temp += "<div class='item' deptId='"+ dept.depId +"' onmouseenter='getFocus(this)' onClick='getCon(this);'>" + dept.depName + "</div>";
            }
        }
        $("#append").append(temp);

    }

    $(document).ready(function(){
       //$(document).keydown(function(e){
       //     e = e || window.event;
       //     var keycode = e.which ? e.which : e.keyCode;
       //     if(keycode == 38){
       //         if(jQuery.trim($("#append").html())==""){
       //             return;
       //         }
       //         movePrev();
       //     }else if(keycode == 40){
       //         if(jQuery.trim($("#append").html())==""){
       //             return;
       //         }
       //         $("#kw").blur();
       //         if($(".item").hasClass("addbg")){
       //             moveNext();
       //         }else{
       //             $(".item").removeClass('addbg').eq(0).addClass('addbg');
       //         }
       //     }
       // });

        var movePrev = function(){
            $("#kw").blur();
            var index = $(".addbg").prevAll().length;
            if(index == 0){
                $(".item").removeClass('addbg').eq($(".item").length-1).addClass('addbg');
            }else{
                $(".item").removeClass('addbg').eq(index-1).addClass('addbg');
            }
        };

        var moveNext = function(){
            var index = $(".addbg").prevAll().length;
            if(index == $(".item").length-1){
                $(".item").removeClass('addbg').eq(0).addClass('addbg');
            }else{
                $(".item").removeClass('addbg').eq(index+1).addClass('addbg');
            }

        }

    });

});

Object.size = function(obj) {
    var size = 0, key;
    for (key in obj) {
        if (obj.hasOwnProperty(key)) size++;
    }
    return size;
};

function getContent(obj){
    var kw = jQuery.trim($(obj).val());
    if(kw == ""){
        $("#append").hide().html("");
        return false;
    }
    $("#append").hide().html("");

    // members_map
    if(Object.size(members_map) > 0){
        var html = "";

        for(var i in members_map){
            var member = members_map[i];

            if (member.customerName.indexOf(kw) >= 0) {
                var memberId = member.customerId;
                var memberState = member.customerState;
                var memberName = member.customerName;
                var memberMethod = member.customerMethod;
                html = html + "<div class='item' membermethod ='" + memberMethod+ "' memberstate='"+ memberState +"' memberid='"+ memberId +"' onmouseenter='getFocus(this)' onClick='getCon(this);'>" + memberName + "</div>"
            }
        }

        $("#append").show().html(html);
    }
}
function getFocus(obj){
    $(".item").removeClass("addbg");
    $(obj).addClass("addbg");
}
function getCon(obj){
    var value = $(obj).text();
    $('#kw').val(value);

    var isRep11 = false;
    var showHtml1 = $(obj).text();
    console.log(showHtml1);
    $('.tryShowCon .tryShowText').each(function(){
        if(showHtml1 == $(this).text()){
            alertWarn('名字不能重复!');
            isRep11 = true;
            return false;
        }
    });
    if(isRep11==false) {
        var memberid = $(obj).attr('memberid');
        var membermethod = $(obj).attr('membermethod');
        var memberstate = $(obj).attr('memberstate');

        if( $('.tryShow .company').length==0) {

            $('.formcCheckbox').hide();
            $('.span3').hide();
            $('.span33').hide();

            $(obj).parents('.set').find('.dept input[type=checkbox]').attr('checked', false);
            $(obj).parents('.set').find('.dept1 input[type=checkbox]').attr('checked', false);
            $(obj).parents('.set').find('.span3 .span3Inp1 input[type=checkbox]').attr('checked', false);
            $(obj).parents('.set').find('.span3 .span3Inp2 input[type=checkbox]').attr('checked', false);
            $(obj).parents('.set').find('.span33Inp1 input[type=checkbox]').attr('checked', false);
            $(obj).parents('.set').find('.span33Inp2 input[type=checkbox]').attr('checked', false);

            $('.tryShow').append('<div class="tryShowCon" memberstate="'+ memberstate +'" memberid="' + memberid + '">' +
                '<div class="tryShowText">' + value + '</div>' +
                '<div class="tryShowImg"></div>' +
                '</div>');

            // 1正式
            /*if(membermethod == 1){
                var memberstate = $(obj).attr('memberstate');
                // 1试用
                if(memberstate == 1){
                    $(obj).parents('.set').find('.type .dept input[type=checkbox]').attr('checked', 'true');
                    $(obj).parents('.set').find('.span3 .span3Inp2 input[type=checkbox]').attr('checked', 'true');
                    $('.formcCheckbox').show();
                    $('.span3').show();
                }
                // 2正式转正
                if(memberstate == 2){
                    $(obj).parents('.set').find('.type .dept input[type=checkbox]').attr('checked', 'true');
                    $(obj).parents('.set').find('.span3 .span3Inp2 input:eq(0)').attr('checked', 'true');
                    $('.formcCheckbox').show();
                    $('.span3').show();
                }
            }
            // 2劳务
            if(membermethod == 2 && $('.tryShow').text() == ''){
                var memberstate = $(obj).attr('memberstate');
                // 1试用
                if(memberstate == 1){
                    $(obj).parents('.set').find('.dept1 input[type=checkbox]').attr('checked', 'true');
                    $(obj).parents('.set').find('.span33Inp1 input[type=checkbox]').attr('checked', 'true');
                    $('.formcCheckbox').show();
                    $('.span33').show();
                }
                // 2转正
                if(memberstate == 2){
                    $(obj).parents('.set').find('.dept1 input[type=checkbox]').attr('checked', 'true');
                    $(obj).parents('.set').find('.span33Inp2 input[type=checkbox]').attr('checked', 'true');
                    $('.formcCheckbox').show();
                    $('.span33').show();
                }
            }

            $('.tryShow').append('<div class="tryShowCon" memberstate="'+ memberstate +'" memberid="' + memberid + '">' +
                '<div class="tryShowText">' + value + '</div>' +
                '<div class="tryShowImg"></div>' +
                '</div>');
             */
            $('.trySeek').animate({
                'top': '280px'
            }, 500);
            $('#kw').val("");
        }
    }
    $("#append").hide().html("");
}

function converToArray(obj){
    var arr = [];
    for(var i in obj){
        arr.push(obj[i]);
    }
    return arr;
}