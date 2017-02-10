$(function () {
    laydate({
        elem: '#payday',
        event: 'focus',
        istime: false,
        format: 'YYYY-MM',
        choose: function (dates) { //选择好日期的回调
            //更新计薪开始日期
//                onChangeDate($('#payday').val());
            initDay();
        }
    });
    $('.oneBegin3').click(function () {
        $('.oneBeginLi').show()
    })

    $('.oneBeginLi').hover(function () {
    }, function () {
        $(this).hide()
    })

    //初始化日期
    initDay();

    //绑定立即使用事件
    $('.onfou').on('click', function () {
        onUse();
    })
});


/**
 * 初始化日期
 */
function initDay() {
    var payday = $('#payday').val();
    onChangeDate(payday);
    $('.oneBeginLi li').click(function () {
        $('.oneBegin3Span').html($(this).html())
        //更新计薪结束日期
        updateEndDay($(this).html());
        $('.oneBeginLi').hide()
    })
}

/**
 * 立即使用
 **/
function onUse() {
    var payday = $('#payday').val();
    if (isEmpty(payday)) {
        showWarning('开始算工资日期不能为空');
        return;
    }
    var year = parseInt(payday.substr(0, 4));
    var month = parseInt(payday.substr(5, 2));
    var day = $('.oneBegin3Span').html();
    var endDay = $('.oneFinish3').html();
    var yearMonth = year + '-' + month;
    var endDate = '';
    if (day == '1') {
        endDay = getCalcSalary(year + '-' + month + '-' + day);
        endDate = yearMonth + '-' + endDay;
    } else {
        if (month == '12') {
            endDate = (year + 1) + '-01-' + endDay;
        } else {
            endDate = year + '-' + (month + 1) + '-' + endDay;
        }
    }
    var param = {'yearMonth': yearMonth, 'startDate': yearMonth + '-' + day, 'endDate': endDate};
    $.ajax({
        type: 'post',
        url: BASE_PATH + '/payrollAccount/initPayCycle.htm',
        data: param,
        success: function (data) {
            if (data.success) {
                //页面跳转
                location.reload(reloadUrl);
            } else {
                showWarning(data.message);
            }
        }
    });
}

/***
 * 更新计薪开始日期
 */
function onChangeDate(date) {
    // var payday = $('#payday').val();
    // var year = payday.substr(0, 4);
    // var month = payday.substr(5, 2);
    // var days = getFirstAndLastMonthDay(year, month);
    var days = 14;
    if (days != null && days > 0) {
        var html = '';
        for (var i = 1; i <= days; i++) {
            html += '<li>' + i + '</li>';
        }
        $('.oneBeginLi').html(html);
    }
}

/**
 * 更新计薪结束日期
 * @param startDay
 */
function updateEndDay(startDay) {
    if (startDay != '1') {
        var payday = $('#payday').val();
        var year = payday.substr(0, 4);
        var month = payday.substr(5, 2);
        //获取计薪结束日
        var endDay = getCalcSalary(year + '-' + month + '-' + startDay);
        $('.oneFinish2').html('次月');
        $('.oneFinish3').html(endDay);
        $('#endDaySpan').html('日');
    } else {
        $('.oneFinish2').html('当月');
        $('.oneFinish3').html('月底');
        $('#endDaySpan').html('');
    }
}