/**
 * unix时间戳转换为yyyy-MM-dd日期格式
 * @param time
 * @returns {string}
 */
function unixToDate(UnixTime) {
    if (UnixTime) {
        var dateObj = new Date(UnixTime);
        return dateObj.getUTCFullYear() + '-' + (dateObj.getUTCMonth() + 1 ) + '-' + dateObj.getUTCDate();
    }
    return "";
}

/**
 * pattern
 *
 * y-m-d
 * y-m-d h
 * y-m-d h:m
 * y-m-d h:m:i
 *
 * @param ts
 * @param pattern
 * @returns {string}
 */
function unixToTime(ts, pattern) {
    var t, y, m, d, h, i, s;
    t = ts ? new Date(ts) : new Date();
    y = t.getFullYear();
    m = t.getMonth() + 1;
    d = t.getDate();
    h = t.getHours();
    i = t.getMinutes();
    s = t.getSeconds();

    if (pattern) {
        // 可根据需要在这里定义时间格式
        if (pattern == 'y-m-d h') {
            return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + (h < 10 ? '0' + h : h);
        }
        if (pattern == 'y-m-d h:m') {
            return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + (h < 10 ? '0' + h : h) + ':' + (i < 10 ? '0' + i : i);
        }
        if (pattern == 'y.m.d h:m') {
            return y + '.' + (m < 10 ? '0' + m : m) + '.' + (d < 10 ? '0' + d : d) + ' ' + (h < 10 ? '0' + h : h) + ':' + (i < 10 ? '0' + i : i);
        }
        if (pattern == 'y-m-d h:m:i') {
            return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + (h < 10 ? '0' + h : h) + ':' + (i < 10 ? '0' + i : i) + ':' + (s < 10 ? '0' + s : s);
        }
        if(pattern == 'ym'){
            return y + '' + (m < 10 ? '0' + m : m);
        }
    }

    return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + (h < 10 ? '0' + h : h) + ':' + (i < 10 ? '0' + i : i) + ':' + (s < 10 ? '0' + s : s);
}

/**
 * fastJson时间戳转换为yyyy-MM-dd日期格式
 * @param time
 * @returns {string}
 */
function fastToDate(fastDate) {
    if (fastDate) {
        var date = new Date(fastDate);
        Y = date.getFullYear() + '-';
        M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
        D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate() + '';
        return Y + M + D;
    }
    return '';
}


/**
 * 增加月
 * @param date
 * @param value
 * @constructor
 */
function AddMonths(ts, value) {
    var date = new Date(ts);
    var year = date.getFullYear();
    var month = date.getMonth();
    date.setMonth(date.getMonth() + value);
    if (date.getMonth() > month + 1) {
        date.setMonth(month + 1);
        //已经跨月份了 获取下个月的最后一天
        var lastDay = getFirstAndLastMonthDay(year, month + 2);
        date.setDate(lastDay);
    }
    return unixToTime(year + '-' + (date.getMonth() + 1) + '-' + date.getDate());
}

/**
 * 获取增加月份的日
 * @param ts
 * @param value
 * @returns {number}
 */
function getLastMonthDay(ts, value) {
    var date = new Date(ts);
    var year = date.getFullYear();
    var month = date.getMonth();
    date.setMonth(date.getMonth() + value);
    if (date.getMonth() > month + 1) {
        date.setMonth(month + 1);
        //已经跨月份了 获取下个月的最后一天
        var lastDay = getFirstAndLastMonthDay(year, month + 2);
        date.setDate(lastDay);
    }
    return date.getDate();
}

/**
 * 获取计薪结束日
 * @param ts
 * @returns {number}
 */
function getCalcSalary(ts) {
    var date = new Date(ts);
    var year = date.getFullYear();
    var month = date.getMonth();
    var day = date.getDate();
    if (day == 1) {
        var lastDay = getFirstAndLastMonthDay(year, month + 1);
        date.setDate(lastDay);
    } else {
        date.setMonth(date.getMonth() + 1);
        if (date.getMonth() > month + 1) {
            date.setMonth(month + 1);
            //已经跨月份了 获取下个月的最后一天
            var lastDay = getFirstAndLastMonthDay(year, month + 2);
            date.setDate(lastDay);
        } else {
            date.setDate(day - 1);
        }
    }
    return date.getDate();
}

/**
 * 获取给定年、月份的最后一天
 * @param year
 * @param month
 * @returns {string}
 */
function getFirstAndLastMonthDay(year, month) {
    //var firstdate = year + '-' + month + '-01';
    var day = new Date(year, month, 0);
    //var lastdate = year + '-' + month + '-' + day.getDate();
    return day.getDate();//获取当月最后一天日期
}

/*
 * formatMoney(s,type)
 * 功能：金额按千位逗号分割
 * 参数：s，需要格式化的金额数值.
 * 参数：type,判断格式化后的金额是否需要小数位.
 * 返回：返回格式化后的数值字符串.
 */
function formatMoney(s, type) {
    if (/[^0-9\.]/.test(s))
        return "0";
    if (s == null || s == "")
        return "0";
    s = s.toString().replace(/^(\d*)$/, "$1.");
    s = (s + "00").replace(/(\d*\.\d\d)\d*/, "$1");
    s = s.replace(".", ",");
    var re = /(\d)(\d{3},)/;
    while (re.test(s))
        s = s.replace(re, "$1,$2");
    s = s.replace(/,(\d\d)$/, ".$1");
    if (type == 0) {// 不带小数位(默认是有小数位)
        var a = s.split(".");
        if (a[1] == "00") {
            s = a[0];
        }
    }
    return s;
}
/*
 * 通用DateAdd(interval,number,date) 功能:实现javascript的日期相加功能.
 * 参数:interval,字符串表达式，表示要添加的时间间隔. 参数:number,数值表达式，表示要添加的时间间隔的个数. 参数:date,时间对象.
 * 返回:新的时间对象. var now = new Date(); var newDate = DateAdd("day",5,now);
 */
function DateAdd(interval, number, date) {
    if (date == null)
        return "";
    switch (interval) {
        case "day":
            date = new Date(date);
            date = date.valueOf();
            date += number * 24 * 60 * 60 * 1000;
            date = new Date(date);
            return date;
            break;
        default:
            return "";
            break;
    }
}

/**
 *
 * 社保金额格式化
 */
function formatterSbMoney(money) {
    money = money+"";
    var indexOf = money.indexOf(".");
    if(indexOf != -1) {
        var last = money.substr(indexOf + 1);
        if(last.length >= 2) {
            var index1 = last.substr(1, 1);
            if(index1 != '0') {
                return parseFloat(money.substr(0, indexOf)+"."+(parseInt(last.substr(0, 1)) + 1));
            }
            return parseFloat(money);
        }
    }
    return parseFloat(money);
}