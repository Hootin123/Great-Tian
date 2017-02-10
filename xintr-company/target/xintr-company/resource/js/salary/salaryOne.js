$(function () {

    //初始化计薪开始日期事件
    initPayAllDown();

    //初始化计薪规则点击事件
    initPayRulesalary();

    //初始化社保公积金事件
    initPayRulesala();

    //初始化开始计算月份事件
    initPaySetTime();

    //初始化按钮事件
    initButton();
})


/**
 * 初始化计薪开始日期事件
 */
function initPayAllDown() {

    $('.payAllDown li').each(function () {
        $(this).click(function (event) {
            event.stopPropagation();
            $('.payAllTime2 span').html($(this).html());
            $('.payAllTime2 span').attr('value', $(this).html());
            $(this).parent('.payAllDown').hide();

            if (Number($(this).html()) == 1) {
                $('.payAllTi2 span').html('当月月底');
            } else {
                var val = Number($(this).html()) - 1;
                $('.payAllTi2 span').html('次月' + val + '日');
                $('.payAllTi2 span').attr('value', val);
            }
        })
    });

    $('.payAllTime2').click(function (event) {
        event.stopPropagation();
        $('.payAllDown').show()
    });
}

/**
 *初始化计薪规则点击事件
 */
function initPayRulesalary() {
//    计薪规则设置
    $('.payRulesalary2').click(function (event) {
        event.stopPropagation();
        $('.payRulesalaDown').hide();
        $('.payRulesalaryDown').show();
    })

    $('.payRulesalaryDown li').click(function (event) {
        event.stopPropagation();
        $('.payRulesalaryDown').hide();
        $('.payRulesalary2').html($(this).html())
        $('.payRulesalary2').attr('value', $(this).attr('value'));
    })
}

/**
 *初始化社保公积金事件
 */
function initPayRulesala() {
    $('.payRulesala22').click(function (event) {
        event.stopPropagation();
        $('.payRulesalaDown').show();
        $('.payRulesalaryDown').hide();
    })
    $('.payRulesalaDown li').click(function (event) {
        event.stopPropagation();
        $('.payRulesalaDown').hide();
        $('.payRulesala22').html($(this).html())
        $('.payRulesala22').attr('value', $(this).attr('value'));
    })
}

/**
 *初始化开始计算月份事件
 */
function initPaySetTime() {
    $('.paySetTime2').click(function (event) {
        event.stopPropagation();
        $('.paySetTimeSet').show();
    })
    $('.paySetTimeSet li').click(function (event) {
        event.stopPropagation();
        $('.paySetTimeSet').hide();
        $('.paySetTime2').html($(this).html());
        $('.paySetTime2').attr('value', $(this).attr('data-value'));
    })
}

/**
 * 初始化按钮事件
 */
function initButton() {

    //计薪周期下一步
    $('.payAllBtn').click(function () {
        $('.payAll').hide();
        $('.payRule').show();
    });

    //计薪规则设置上一步
    $('.payRuleBtn1').click(function () {
        $('.payAll').show();
        $('.payRule').hide();
    });

    //计薪规则设置下一步
    $('.payRuleBtn2').click(function () {
        $('.payAll').hide();
        $('.payRule').hide();
        $('.paySet').show();
    })

    //确定事件绑定
    $('.paySetBtn').click(function () {
        onOk();
    })

    $(document).click(function () {
        $('.payRulesalaDown').hide();
        $('.payRulesalaryDown').hide();
        $('.payAllDown').hide();
        $('.paySetTimeSet').hide();
    })
}

/**
 * 确定
 */
function onOk() {
    var param = {
        'startDay': $('.payAllTime2 span').attr('value'),
        'payWay': $('.payRulesalary2').attr('value'),
        'isSocialSecurity': $('.payRulesala22').attr('value'),
        'yearMonth': $('.paySetTime2').attr('value')
    };
    ajaxJson('../payCycle/initPayCycle.htm', param, function (data) {
        //跳转到薪酬核算界面
        window.location = '../salaryAccount/salaryAccount.htm';
    })
}