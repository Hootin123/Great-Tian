
/*弹窗*/
var abox;

$(function(){

    // 发送搜索请求
    $('.MyOption li').on('click', function () {

        var rechargeType = '', rechargeState = '', dateType = '';

        if($(this).parents('div:eq(0)').attr('class') == 'MyOptionIndent'){
            rechargeType = $(this).attr('val');
            rechargeState = $('.MyOptionDeal .publictClass').attr('val');
            dateType = $('.MyOptionTime .publictClass').attr('val');
        }

        if($(this).parents('div:eq(0)').attr('class') == 'MyOptionTime'){
            rechargeType = $('.MyOptionIndent .publictClass').attr('val');
            rechargeState = $('.MyOptionDeal .publictClass').attr('val');
            dateType = $(this).attr('val');
        }

        if($(this).parents('div:eq(0)').attr('class') == 'MyOptionDeal'){
            rechargeType = $('.MyOptionIndent .publictClass').attr('val');
            rechargeState = $(this).attr('val');
            dateType = $('.MyOptionTime .publictClass').attr('val');
        }

        var params = {
            rechargeType : rechargeType,
            rechargeState : rechargeState,
            dateType : dateType
        };

        queryList(1, params);

    });

    $('.MyOptionIndent li').eq(0).addClass('publictClass').siblings().removeClass('publictClass');
    $('.MyOptionIndent li').click(function(){
        $(this).addClass('publictClass').siblings().removeClass('publictClass')
    });
    $('.MyOptionTime li').eq(0).addClass('publictClass').siblings().removeClass('publictClass');
    $('.MyOptionTime li').click(function(){
        $(this).addClass('publictClass').siblings().removeClass('publictClass')
    });
    $('.MyOptionDeal li').eq(0).addClass('publictClass').siblings().removeClass('publictClass');
    $('.MyOptionDeal li').click(function(){
        $(this).addClass('publictClass').siblings().removeClass('publictClass')
    });

    queryList(1, {});

    /*弹窗*/
    abox = $('.abox');

    $('.aboxbgBtn1').click(function() {
        closeAbox();
    });

});

/**
 * 渲染页面数据
 *
 * @param data
 */
function render(data){
    $('.Mytable tbody').html('');
    var html = '';
    if(data && data.length > 0){
        for (var i in data) {
            var item = data[i];
            var recharge_type = '', recharge_station = '', recharge_state = '';
            var optHtml = '';
            if(item.rechargeState == 0){
                recharge_state = '提交订单';
                //optHtml += '<a href="">付款</a>';
            }
            else if(item.rechargeState == 1){
                recharge_state = '交易失败';
            }
            else if(item.rechargeState == 2){
                recharge_state = '交易成功';
            }
            else if(item.rechargeState == 3){
                recharge_state = '已关闭';
            }

            if(item.rechargeType == 1){
                recharge_type = '充值';
                // var url = BASE_PATH +'/account/recharge_detail.htm?id='+ item.rechargeId;
                optHtml += "<a href='#' onclick='onShowRechargeDetail("+item.rechargeId+")' >详情</a>";
            }
            else if(item.rechargeType == 2){
                recharge_type = '提现';
                // var url = BASE_PATH +'/account/withdraw_detail.htm?id='+ item.rechargeId;
                optHtml += "<a href='#' onclick='onShowWithdrawDetail("+item.rechargeId+")' >详情</a>";
            }
            else if(item.rechargeType == 3){
                recharge_type = '发工资';
                optHtml += "<a href='#' onclick='onShowSalaryDetail("+item.rechargeId+")' >详情</a>";
            }
            else if(item.rechargeType == 4){
                recharge_type = '垫工资';
                // optHtml += "<a href='#' onclick='onShowSalaryDetail("+item.rechargeId+")' >详情</a>";
            }
            else if(item.rechargeType == 8){
                recharge_type = '社保公积金';
                // var url = BASE_PATH +'/account/social_detail.htm?id='+ item.rechargeId;
                // optHtml += '<a onclick=openWindow("'+ url +'") href="javascript:void(0);">详情</a>';
                optHtml += "<a href='#' onclick='onShowShebaoDetail("+item.rechargeId+")' >详情</a>";
            }
            else if(item.rechargeType == 9){
                recharge_type = '急速发工资';
                // var url = BASE_PATH +'/account/social_detail.htm?id='+ item.rechargeId;
                // optHtml += '<a onclick=openWindow("'+ url +'") href="javascript:void(0);">详情</a>';
                optHtml += "<a href='#' onclick='onShowSalaryDetail("+item.rechargeId+")' >详情</a>";
            }
            else if(item.rechargeType == 5){
                recharge_type = '借款';
                // var url = BASE_PATH +'/account/borrowDetail.htm?id='+ item.rechargeId;
                optHtml += "<a href='#' onclick='onShowBorrowDetail("+item.rechargeId+")' >详情</a>";
            }

            if(item.rechargeStation && item.rechargeStation == 1){
                recharge_station = '连连';
            }
            else if(item.rechargeStation && item.rechargeStation == 2){
                recharge_station = '京东';
            }
            else if(item.rechargeStation && item.rechargeStation == 3){
                recharge_station = '易宝';
            }
            else if(item.rechargeStation && item.rechargeStation == 4){
                recharge_station = '网银';
            }
            else if(item.rechargeStation && item.rechargeStation == 5){
                recharge_station = '账户余额支付';
            }
            else if(item.rechargeStation && item.rechargeStation == 6){
                recharge_station = '银行汇款';
            }

            html += '<tr>' +
                '   <td>'+show(item)+'</td>' +
                '   <td>'+ item.rechargeNumber +'</td>' +
                '   <td>'+ recharge_type +'</td>' +
                '   <td>'+ recharge_station +'</td>' +
                '   <td>'+ item.rechargeMoney +'</td>' +
                '   <td>'+ recharge_state +'</td>' +
                '   <td class="color">'+ optHtml +'</td>' +
                '</tr>';
        }
        $('.Mytable tbody').html(html);
    }
}

function openWindow(url){
    parent.allPop(url);
}
function pager(paginator){
    //分页
//     kkpager.generPageHtml({
//         pno: paginator.page,
//         mode: 'click', //可选，默认就是link
//         //总页码
//         total: paginator.totalPages,
//         //总数据条数
//         totalRecords: paginator.totalCount,
//         //链接前部
//         hrefFormer: '',
//         //链接尾部
//         hrefLatter: '',
//         //链接算法
//         isShowFirstPageBtn:false,
//         isShowLastPageBtn:false,
//         currPageBeforeText:'第',
// //        getLink: function (n) {
// //            return 'orgmember.htm?pageIndex=' + n;
// //        }
//         click: function (n) {
//             var rechargeType = $('.MyOptionIndent .publictClass').attr('val');
//             var rechargeState = $('.MyOptionDeal .publictClass').attr('val');
//             var dateType = $('.MyOptionTime .publictClass').attr('val');
//             var params = {
//                 rechargeType : rechargeType,
//                 rechargeState : rechargeState,
//                 dateType : dateType
//             };
//             queryList(n, params);
//         },
//         //getHref是在click模式下链接算法，一般不需要配置，默认代码如下
//         getHref: function (n) {
//             return '#';//封装好把时间戳转换成正常时间
//         }
//     }, true);


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
            var rechargeType = $('.MyOptionIndent .publictClass').attr('val');
            var rechargeState = $('.MyOptionDeal .publictClass').attr('val');
            var dateType = $('.MyOptionTime .publictClass').attr('val');
            var params = {
                rechargeType : rechargeType,
                rechargeState : rechargeState,
                dateType : dateType
            };
            var n=obj.curr;
            queryList(n, params);
            }
        }
    });
}

function queryList(p, params){
    loadData(BASE_PATH + '/account/orders.htm?pageIndex='+p, params, function (result) {
        var data = result.data;
        // 渲染数据
        render(data);
        // 分页
        pager(result.paginator);
    });
}

function show(obj){
    var time = new Date(obj.rechargeAddtime);
    var a = time.getFullYear();
    var b = time.getMonth()+1;
    var c = time.getDate();
    var d= time.getHours();
    var e = time.getMinutes();
    var f = time.getSeconds();
    if(b<10){
        b = "0"+b
    }
    if(c<10){
        c = "0"+c
    }
    if(d<10){
        d = "0"+d
    }
    if(e<10){
        e="0"+e
    }
    if(f<10){
        f="0"+f
    }
    var showTime = a+ '.' + b + '.' + c + ' ' + d +':'+ e;
    return showTime;
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

function alertBox(){
    $('.pacity').show();
    $('.aboxbg').show(200);
}


function closeAbox() {
    $('.pacity').hide();
    $('.aboxbg').hide(200);
}




/**
 * 发工资详情弹框
 * @param rechargeId
 */
function onShowSalaryDetail(rechargeId){
    //parent.allPop('../account/salary_detail.htm?id='+rechargeId);
    var url= '../account/salary_detail.htm?id='+rechargeId;
    var index = layer.open({
        type: 2,
        title: false,
        closeBtn:0,
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: url
    });
    //窗口最大化
    layer.full(index);
}

function onShowShebaoDetail(rechargeId){
    var url= BASE_PATH+'/account/social_detail_new.htm?companyRechargeId='+rechargeId;
    var index = layer.open({
        type: 2,
        title: false,
        closeBtn:0,
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: url
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 充值弹框
 * @param rechargeId
 */
function onShowRechargeDetail(rechargeId){
    //parent.allPop('../account/salary_detail.htm?id='+rechargeId);
    var url = BASE_PATH +'/account/recharge_detail.htm?id='+ rechargeId;
    //var url= '../account/salary_detail.htm?id='+rechargeId;
    var index = layer.open({
        type: 2,
        title: false,
        closeBtn:0,
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: url
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 提现弹框
 * @param rechargeId
 */
function onShowWithdrawDetail(rechargeId){
    //parent.allPop('../account/salary_detail.htm?id='+rechargeId);
    var url = BASE_PATH +'/account/withdraw_detail.htm?id='+ rechargeId;
    //var url= '../account/salary_detail.htm?id='+rechargeId;
    var index = layer.open({
        type: 2,
        title: false,
        closeBtn:0,
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: url
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 借款弹框
 * @param rechargeId
 */
function onShowBorrowDetail(rechargeId){
    //parent.allPop('../account/salary_detail.htm?id='+rechargeId);
    var url = BASE_PATH +'/account/borrowDetail.htm?id='+ rechargeId;
    //var url= '../account/salary_detail.htm?id='+rechargeId;
    var index = layer.open({
        type: 2,
        title: false,
        closeBtn:0,
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: url
    });
    //窗口最大化
    layer.full(index);
}