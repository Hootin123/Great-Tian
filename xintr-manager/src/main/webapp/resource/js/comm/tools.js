/**
 * unix时间戳转换为yyyy-MM-dd日期格式
 * @param time
* @returns {string}
*/
function unixToDate(UnixTime) {
    if(UnixTime){
        var dateObj = new Date(UnixTime);
        return dateObj.getUTCFullYear() + '-' + (dateObj.getUTCMonth() +1 ) + '-' + dateObj.getUTCDate();
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
    var t,y,m,d,h,i,s;
    t = ts ? new Date(ts) : new Date();
    y = t.getFullYear();
    m = t.getMonth()+1;
    d = t.getDate();
    h = t.getHours();
    i = t.getMinutes();
    s = t.getSeconds();

    if(pattern){
        // 可根据需要在这里定义时间格式
        if(pattern == 'y-m-d h'){
            return y+'-'+(m<10?'0'+m:m)+'-'+(d<10?'0'+d:d)+' '+(h<10?'0'+h:h);
        }
        if(pattern == 'y-m-d h:m'){
            return y+'-'+(m<10?'0'+m:m)+'-'+(d<10?'0'+d:d)+' '+(h<10?'0'+h:h)+':'+(i<10?'0'+i:i);
        }
        if(pattern == 'y-m-d h:m:i'){
            return y+'-'+(m<10?'0'+m:m)+'-'+(d<10?'0'+d:d)+' '+(h<10?'0'+h:h)+':'+(i<10?'0'+i:i)+':'+(s<10?'0'+s:s);
        }
    }

    return y+'-'+(m<10?'0'+m:m)+'-'+(d<10?'0'+d:d)+' '+(h<10?'0'+h:h)+':'+(i<10?'0'+i:i)+':'+(s<10?'0'+s:s);
}

//格式化金额，带千位符
function formatCurrencyWithThousand(s, n) {
    n = n > 0 && n <= 20 ? n : 2;
    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
    var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
    t = "";
    for (i = 0; i < l.length; i++) {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return t.split("").reverse().join("") + "." + r;
}

//格式化金额，不带千位符
function formatCurrency(s, n) {
    var t;
    t = s;
    if ($.isNumeric(t)){
        t = parseFloat(t).toFixed(n);
    }

    return t;
}