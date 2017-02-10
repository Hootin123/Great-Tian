$(function () {
    //初始化数据
    onScreen();

    //初始化查询
    initSearch();

    $(document).click(function () {
        seachclose();
    })
});

/**
 * 生成分页控件
 */
function generPageHtml(paginator) {
    //生成分页控件
    // kkpager.generPageHtml({
    //     pno: paginator.page,
    //     mode: 'click', //可选，默认就是link
    //     //总页码
    //     total: paginator.totalPages,
    //     //总数据条数
    //     totalRecords: paginator.totalCount,
    //     //链接前部
    //     hrefFormer: '',
    //     //链接尾部
    //     hrefLatter: '',
    //     isShowFirstPageBtn:false,
    //     isShowLastPageBtn:false,
    //     currPageBeforeText:'第',
    //     click: function (n) {
    //         var param = {'pageIndex': n};
    //         onScreen(param);
    //     },
    //     //getHref是在click模式下链接算法，一般不需要配置，默认代码如下
    //     getHref: function (n) {
    //         return '#';
    //     }
    // }, true);
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
                var param = {'pageIndex': obj.curr};
                onScreen(param);
            }
        }
    });
}

/**
 * 关闭弹框
 */
function seachclose() {
    $('.salaset-seach,.salaset-seach1,.salaset-operate').hide();
}


/**
 * 初始化查询
 */
function initSearch() {
    /*筛选js*/
    $('.salaset-sh').click(function () {
        seachclose();
        $('.salaset-seach').show();
        return false;
    });

    // $('.salaset-seach .list').attr('index', -1);
    $('.salaset-seach .list').click(function () {
        $('.salaset-seach .list').siblings('li').hide();
        var oLi = $(this);
        var aLi = $(this).siblings('li');
        var aSpan = $('.salaset-down-list span');
        aLi.show();
        $('.salaset-down-list').hide();
        aLi.mouseover(function () {
            aLi.css({
                'background': '#ffffff',
                'color': '#333333'
            });
            if ($(this)[0].className != 'salaset-down-list') {
                $(this).css({
                    'background': '#0696FA',
                    'color': '#ffffff'
                });
            }

        });
        aLi.click(function () {
            if ($(this)[0].className != 'salaset-down-list') {
                //  $(this).siblings('.list').attr('index', 1);
                aLi.hide();
                oLi.html($(this).html());
                oLi.attr('data-value', $(this).attr('data-value'));
                oLi.css('color', '#333333');
            }

        });
        return false;
    });
    $(document).click(function () {
        var aLi = $('.salaset-seach .list').siblings('li');
        aLi.hide();
    });

    $('.salaset-seach-btn').click(function () {
        //筛选
        onScreen();

        /*关闭弹窗*/
        $('.salaset-seach').hide();

    });
    /*end*/

    /*搜索js*/
    $('.salaset-ss').click(function () {
        seachclose();
        $('.salaset-seach1').show();
        return false;
    });


    /*批量下拉*/
    $('.salaset-co').click(function () {
        $('.salaset-seach').hide();
        $('.salaset-seach1').hide();
        openBatchUpdateExpense();
        return false;
    });
    $('.salaset-operate li').mouseover(function () {
        $('.salaset-operate li').removeClass('sopactive');
        $(this).addClass('sopactive');
        $(this).click(function () {
            $('.salaset-operate').hide();
        });
    });



    /*关闭弹框*/
    $('.salaset-seach,.salaset-seach1,.salaset-operate').click(function () {
        return false;
    });

    function seachclose() {
        $('.salaset-seach,.salaset-seach1,.salaset-operate').hide();
    }

    $(document).click(function () {
        seachclose();
    });
    /*end*/


    /*部门*/
    $('.list input').keyup(function () {
        $(this).parent('li').siblings('li').show();
        if (!isEmpty($(this).val())) {
            var param = {'deptName': $(this).val()};

            $.ajax({
                type: 'post',
                url: '../dept/selectByDeptName.htm',
                data: param,
                success: function (data) {
                    //加载部门数据
                    loadDeptData(data);
                }
            })
        }
    })
}

/**
 * 加载部门数据
 */
function loadDeptData(deptData) {
    if (!isEmpty(deptData)) {
        var html = '';
        for (var i = 0; i < deptData.length; i++) {
            html += '<span data-value="' + deptData[i].depId + '">' + deptData[i].depName + '</span>';
        }
        $('.salaset-down-list').html(html);
        //重置事件
        mySpan();
    } else {
        $('.salaset-down-list').html('');
    }
}

/**
 * 筛选部门绑定事件
 */
function mySpan() {
    var aSpan = $('.salaset-down-list span');
    aSpan.mouseover(function () {
        aSpan.css({
            'background': '#ffffff',
            'color': '#333333'
        });
        $(this).css({
            'background': '#0696FA',
            'color': '#ffffff'
        });
    });

    aSpan.click(function () {
        $(this).parent().prev().attr('index', 1);
        $(this).parents('li').hide();
        $('.salaset-down-input').val($(this).html());
        $('.salaset-down-input').attr('data-value', $(this).attr('data-value'));
        $(this).css('color', '#333333')
    });
}


/**
 * 筛选
 */
function onScreen(param) {
    param = param || {};
    $("#searchTj").empty();
    //组装参数
    param.payCycleId = $('#payCycleId').val();
    param.stationEmployMethod = $('.list').eq(0).attr('data-value');
    param.stationCustomerState = $('.list').eq(1).attr('data-value');
    param.isExpense = $('.list').eq(2).attr('data-value');
    param.deptId = $('.salaset-down-input').attr('data-value');
    //param.allowanceId = $('.list').eq(3).attr('data-value');
    param.userName = $('#userName').val();

    if(param.stationEmployMethod){
        $('.seekResult').show();
        $("#searchTj").append("<li><span>" + $('.list').eq(0).html() + "</span></li>")
    }
    if(param.stationCustomerState){
        $('.seekResult').show();
        $("#searchTj").append("<li><span>" + $('.list').eq(1).html() + "</span></li>")
    }
    if(param.isExpense){
        $('.seekResult').show();
        $("#searchTj").append("<li><span>" + $('.list').eq(2).html() + "</span></li>")
    }
    if(param.deptId) {
        $('.seekResult').show();
        $("#searchTj").append("<li><span>" + $('.salaset-down-input').val() + "</span></li>")
    }
    if(param.userName){
        $('.seekResult').show();
        $("#searchTj").append("<li><span>" + $('#userName').val() + "</span></li>")
    }

    var index = layer.load();
    $.ajax({
        type: 'post',
        url: 'list.htm',
        data: param,
        success: function (data) {
        data = eval("(" + data + ")");
        //关闭
            layer.close(index);
            if (data.success) {
                showData(data.data);
                //生成分页控件
                if (!isEmpty(data.data)){
                    generPageHtml(data.paginator);

                }
                $("#count").html(data.paginator.totalCount || 0);

            } else {
                showWarning(data.message);
            }
        }
    })
}

/**
 * 重置查询条件
 */
function resetQuery() {
    $('.seekResult').hide()
    $('.list').eq(0).removeAttr('data-value', '');
    $('.list').eq(0).html('聘用形式');
    $('.list').eq(1).removeAttr('data-value', '');
    $('.list').eq(1).html('员工状态');
    $('.salaset-down-input').removeAttr('data-value', '');
    $('.salaset-down-input').val('');
    $('.list').eq(2).removeAttr('data-value', '');
    $('.list').eq(2).html('报销状态');
    $('.list').css('color', '');
    $('#userName').val('');
    //关闭弹框
    seachclose();
    onScreen();
}

/**
 * 显示数据
 * @param data
 */
function showData(data) {
    if (!isEmpty(data) && data.length > 0) {
        var length = data.length;
        //获取标题
        var html = getTitle(data[0]);
        for (var i = 0; i < length; i++) {
            var user = data[i];
            var v1 = parseFloat(user.customer_current_expense || 0);
            var v2 = parseFloat(user.customer_current_salary || 0);
            var v3 = parseFloat(user.expense_sum || 0);
            var value = Math.min(v1, v2);
            var syed = Math.max(0, value - v3);
            html += '<ul class="salaset-body clear">';
            html += '<li style="width: 40px">' + (i + 1) + '</li>';//序号
            html += '<li>' + user.customer_turename + '</li>';//姓名
            html += '<li>' + user.customer_phone + '</li>';//手机
            html += '<li>' + (user.dep_name || '-') + '</li>';//部门
            html += '<li>' + (user.customer_current_salary || 0) + '</li>';//基本工资
            //html += '<li>' + user.real_wage + '</li>';//应发工资
            if(Math.min((user.customer_current_salary || 0), (user.real_wage || 0)) >= v1) {
                html += '<li>' + v1.toFixed(0) + '</li>';//报销额度
            }else{
                html += '<li>' + v1.toFixed(0) + '</li>';//报销额度
            }

            html += '<li>' + parseFloat(syed).toFixed(2) + '</li>';//剩余额度
            // html += '<li>' + parseFloat(syed).toFixed(0) + '</li>';//剩余额度 四舍五入
            html += '<li class="salaset-bodylast">';
            html += '<i onclick=\"onCustomerRequire(\'' + BASE_PATH + '/expense/toUpdateExpense.htm?customerId=' + user.customer_id + '\', \'调额\', 600, 300)\" >' + (user.customer_is_expense == 1 ? '调额':'定额' ) + '</i>&nbsp;';
            //if(parseFloat(syed).toFixed(0) > 0) {
                html += '<i onclick=\"openExpense(' + user.customer_id + ')\">报销</i>&nbsp;';
            //}
            html += '</li>';
            html += '</ul>';
        }
        $('.salaset-list').html(html);
        //初始化监听
        initListener();
    } else {
        $('.salaset-list').html('');
        $('#kkpager').html('');
    }
}

/**
 * 获取标题
 */
function getTitle(rowData) {
    var html = '<ul class="salaset-ttle clear">';
    html += '<li style="width: 40px">序号</li>';
    html += '<li>姓名</li>';
    html += '<li>手机号</li>';
    html += '<li>部门</li>';
    html += '<li>基本工资</li>';
    //html += '<li>应发工资</li>';
    html += '<li>报销额度</li>';
    html += '<li>剩余额度</li>';
    html += '<li class="salaset-bodylast">操作</li>';
    html += '</ul>';
    return html;
}

/**
 * 初始化监听事件
 */
function initListener() {
    $('.salaset-list input').focus(function () {
        $(this).css('text-align', 'left')
    });

    $('.salaset-list input').blur(function () {
        $(this).css('text-align', 'center')
    })
}


function te(id){
    var index = layer.confirm(
        "<div style='width:300px;padding-left: 70px;'>姓名：王某</div>" +
        "<div style='width:300px;margin-top: 15px;padding-left: 70px;'>*报销额度：<input id='ed' style='width: 100px;height: 25px'/></div>", {
        btn: ['确定', '取消'], //按钮
        area:['440px', '250px']
    }, function () {
        $.post(BASE_PATH + "/expense/updateExpense.htm", {"customerId": id, "expense":$("#ed").val()}, function(data){
            if (data.success) {
                layer.closeAll('dialog');
            } else {
                showWarning(data.message);
            }
        })

    })
}

function onCustomerRequire(url, title, width, height) {
    var index = layer.open({
        type: 2,
        title: false,
        closeBtn:0,
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: [width + "px", height + "px"],
        content: url
    });
    //窗口最大化
    //layer.full(index);
}

function openBatchUpdateExpense() {
    onCustomerRequire(BASE_PATH + '/expense/toBatchUpdateExpense.htm', '批量调额', 550, 400);
}

function openExpense(id) {
    onCustomerRequire(BASE_PATH + '/expense/toExpense.htm?customerId='+id, '报销', 600, 330);
}

function searchAll(){
    resetQuery();
    onScreen();
}

function searchWDE(){
    //$("#un_expense_li").click();
    $('.list').eq(2).attr('data-value', '0');
    $('.list').eq(2).html('未定额');
    $('.list').eq(2).css('color', '#333333');
    onScreen();
}