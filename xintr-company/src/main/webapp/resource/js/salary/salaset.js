//是否筛选
var isSearch = false;
//刷选条件
var searchParameter = '';
$(function () {
    //初始化数据
    initData();

    //初始化查询
    initSearch();

    $(document).click(function () {
        seachclose();
    });


});

/**
 * 初始化数据
 */
function initData(param) {
    isSearch = false;
    if (!isEmpty(param) && param.isSalary) {
        searchParameter = param;
    } else {
        searchParameter = '';
    }
    ajaxJson('dataList.htm', param, function (data) {
        showData(data.data);
        //组装筛选条件
        if (!isEmpty(searchParameter)) {
            var totalCount = data.paginator ? data.paginator.totalCount : 0;
            var html = '<div>找到 <span>' + totalCount + '</span>个关键词为 <span class="screenCon">未定薪</span> <a onclick="cancelScreen()">取消筛选</a></div>';
            $('.screen').html(html);
        }
        //生成分页控件
        if (!isEmpty(data.data)) {
            generPageHtml(data.paginator);
        }
    });
    // $.ajax({
    //     type: 'post',
    //     url: 'dataList.htm',
    //     data: param,
    //     success: function (data) {
    //         //关闭
    //         layer.close(index);
    //         if (data.success) {
    //             showData(data.data);
    //             //生成分页控件
    //             if (!isEmpty(data.data)) {
    //                 //组装筛选条件
    //                 if (!isEmpty(searchParameter)) {
    //                     var html = '<div>找到 <span>' + data.paginator.totalCount + '</span>个关键词为 <span class="screenCon">未定薪</span> <a onclick="cancelScreen()">取消筛选</a></div>';
    //                     $('.screen').html(html);
    //                 }
    //                 generPageHtml(data.paginator);
    //             }
    //         } else {
    //             if (data.message) {
    //                 showWarning(data.message);
    //             } else {
    //                 $('head').append('<script>' + data.documentElement.innerHTML + '</script>');
    //             }
    //         }
    //     }
    // });
}

/**
 * 生成分页控件
 */
function generPageHtml(paginator) {
    //生成分页控件
    kkpager.generPageHtml({
        pno: paginator.page,
        mode: 'click', //可选，默认就是link
        //总页码
        total: paginator.totalPages,
        //总数据条数
        totalRecords: paginator.totalCount,
        //链接前部
        hrefFormer: '',
        //链接尾部
        hrefLatter: '',
        isShowFirstPageBtn: false,
        isShowLastPageBtn: false,
        currPageBeforeText: '第',
        click: function (n) {
            if (isSearch) {
                onScreen(n, 10);
            } else {
                if (!isEmpty(searchParameter)) {
                    searchParameter.pageIndex = n;
                    initData(searchParameter);
                } else {
                    var param = {'pageIndex': n};
                    initData(param);
                }
            }
        },
        //getHref是在click模式下链接算法，一般不需要配置，默认代码如下
        getHref: function (n) {
            return '#';
        }
    }, true);
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
        if ($(this).attr('dataValue') == 'list') {
            loadAllowance(oLi)
        } else {
            loadListener(oLi);
        }

        return false;

    });

    $(document).click(function () {
        var aLi = $('.salaset-seach .list').siblings('li');
        aLi.hide();
    });

    $('.salaset-seach-btn').click(function () {
        //筛选
        onScreen(0, 10);

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
    /*end*/

    /*批量下拉*/
    $('.salaset-co').click(function () {
        seachclose();
        $('.salaset-operate').show();
        return false;
    });
    $('.salaset-operate li').mouseover(function () {
        $('.salaset-operate li').removeClass('sopactive');
        $(this).addClass('sopactive');
        $(this).click(function () {
            $('.salaset-operate').hide();
        })
    });
    /*end*/


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
    $('.list1 input').keyup(function () {
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
            });
        }
    })
}

/**
 * 加载事件
 */
function loadListener(oLi) {
    var aLi = oLi.siblings('li');

    aLi.show();
    $('.salaset-down-list').hide();
    aLi.each(function () {

        $(this).hover(function () {
            $(this).css({'background': '#0696FA', 'color': '#fff'});
        }, function () {
            $(this).css({'background': '#fff', 'color': '#333'});
        });
    })


    aLi.click(function () {
        if (oLi[0].className != 'salaset-down-list') {
            //  $(this).siblings('.list').attr('index', 1);
            aLi.hide();
            oLi.html($(this).html());
            oLi.attr('data-value', $(this).attr('data-value'));
            oLi.css('color', '#333333');
        }

    });
}

/**
 * 加载津贴数据
 */
function loadAllowance($this) {
    $.ajax({
        type: 'post',
        url: '../payrollAccount/getCompanyAllowanceSettingList.htm',
        success: function (data) {
            $this.siblings('li').remove();
            if (!isEmpty(data)) {
                var html = '';
                for (var i = 0; i < data.data.length; i++) {
                    // <li data-value="$!{allowance.id}">$!{allowance.allowanceName}</li>
                    html += '<li data-value="' + data.data[i].id + '">' + data.data[i].allowanceName + '</li>';
                }
                $this.after(html);
                loadListener($this);
            }
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
        $('.salaset-down-list1').html(html);
        //重置事件
        mySpan();
    } else {
        $('.salaset-down-list1').html('');
    }
}

/**
 * 筛选部门绑定事件
 */
function mySpan() {
    var aSpan = $('.salaset-down-list1 span');
    aSpan.each(function () {
        $(this).hover(function () {
            $(this).css({'background': '#0696FA', 'color': '#fff'});
        }, function () {
            $(this).css({'background': '#fff', 'color': '#333'});
        });
    });

    aSpan.click(function () {
        $(this).parent().prev().attr('index', 1);
        $(this).parents('li').hide();
        $('.salaset-down-input').val($(this).html());
        $('.salaset-down-input').attr('data-value', $(this).attr('data-value'));
        $(this).css('color', '#333333');
    })
}


/**
 * 筛选
 */
function onScreen(pageIndex, pageSize, type) {

    //组装参数
    var param = {
        'payCycleId': $('#payCycleId').val(),
        'stationEmployMethod': $('.list').eq(0).attr('data-value'),
        'stationCustomerState': $('.list').eq(1).attr('data-value'),
        'deptId': $('.salaset-down-input').attr('data-value'),
        'allowanceId': $('.list').eq(2).attr('data-value'),
        'userName': $('#userName').val(),
        'pageIndex': pageIndex || 0,
        'pageSize': pageSize || 10
    };
    var index = layer.load();
    $.ajax({
        type: 'post',
        url: 'queryCustomerPayroll.htm',
        data: param,
        success: function (data) {
            //关闭
            layer.close(index);
            if (data.success) {
                isSearch = true;
                showData(data.data);
                //生成分页控件
                if (!isEmpty(data.data)) {
                    //组装筛选条件
                    assembleScreen(data.paginator.totalCount);
                    generPageHtml(data.paginator);
                } else {
                    //组装筛选条件
                    assembleScreen(0);
                }

            } else {
                showWarning(data.message);
            }
        }
    });
}
/**
 * 组装筛选条件
 */
function assembleScreen(length) {
    if (!isEmpty($('.list').eq(0).attr('data-value'))
        || !isEmpty($('.list').eq(1).attr('data-value'))
        || !isEmpty($('.salaset-down-input').attr('data-value'))
        || !isEmpty($('.list').eq(2).attr('data-value'))
        || !isEmpty($('#userName').val())) {
        // <div>找到 <span>1</span>个关键词为 <span class="screenCon">正式</span> <a>取消筛选</a></div>
        var html = '<div>找到 <span>' + length + '</span>个关键词为 ';
        if (!isEmpty($('.list').eq(0).attr('data-value'))) {
            html += '<span class="screenCon">' + $('.list').eq(0).html() + '</span>';
        }
        if (!isEmpty($('.list').eq(1).attr('data-value'))) {
            html += '<span class="screenCon">' + $('.list').eq(1).html() + '</span>';
        }
        if (!isEmpty($('.salaset-down-input').attr('data-value'))) {
            html += '<span class="screenCon">' + $('.salaset-down-input').val() + '</span>';
        }
        if (!isEmpty($('.list').eq(2).attr('data-value'))) {
            html += '<span class="screenCon">' + $('.list').eq(2).html() + '</span>';
        }
        if (!isEmpty($('#userName').val())) {
            html += '<span class="screenCon">' + $('#userName').val() + '</span>';
        }
        html += '<a onclick="cancelScreen()">取消筛选</a></div>';
        $('.screen').html(html);
    } else {
        $('.screen').html('');
    }
}

/**
 * 取消筛选
 */
function cancelScreen() {
    $('.screen').html('');
    //重置查询条件
    resetQuery();
    //重新加载数据
    initData(null);
}

/**
 * 重置查询条件
 */
function resetQuery() {
    $('.list').eq(0).attr('data-value', '');
    $('.list').eq(0).html('聘用形式');
    $('.list').eq(0).attr('style', '');
    $('.list').eq(1).attr('data-value', '');
    $('.list').eq(1).html('员工状态');
    $('.list').eq(1).attr('style', '');
    $('.salaset-down-input').attr('data-value', '');
    $('.salaset-down-input').val('');
    $('.list').eq(2).attr('data-value', '');
    $('.list').eq(2).html('津贴方案');
    $('.list').eq(2).attr('style', '');
    $('#userName').val('');
    searchParameter = '';
    //关闭弹框
    seachclose();
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
            var payroll = data[i];
            var baseWages = isEmpty(payroll.baseWages) ? '0' : payroll.baseWages;//基本工资
            var attendanceDeduction = isEmpty(payroll.attendanceDeduction) ? '0' : payroll.attendanceDeduction;//缺勤扣款
            var customerCurrentExpense = isEmpty(payroll.customerCurrentExpense) ? '0' : payroll.customerCurrentExpense;//报销额度
            if (customerCurrentExpense > baseWages - attendanceDeduction) {
                html += '<ul id="salaset' + payroll.id + '" class="salaset-body clear red">';
            } else {
                html += '<ul id="salaset' + payroll.id + '" class="salaset-body clear">';
            }
            html += '<li style="width: 40px">' + (i + 1) + '</li>';//序号
            html += '<li>' + payroll.userName + '</li>';//姓名
            var deptName = isEmpty(payroll.deptName) ? "-" : payroll.deptName;
            html += '<li>' + deptName + '</li>';//部门
            html += '<li>' + payroll.mobilePhone + '</li>';//手机号码
            html += '<li class="subitem subitem1">' + fastToDate(payroll.entryDate) + '</li>';//入职日期
            var employMethod = payroll.employMethod;
            employMethod = employMethod == '1' ? '正式' : '劳务';
            html += '<li class="subitem  subitem1">' + employMethod + '</li>';//聘用方式
            var idCard = isEmpty(payroll.idCard) ? '-' : payroll.idCard;
            html += '<li class="subitem subitem1 idCard">' + idCard + '</li>';//身份证号码
            var bankNumber = isEmpty(payroll.bankNumber) ? '-' : payroll.bankNumber;
            html += '<li class="subitem subitem1 bankNumber">' + bankNumber + '</li>';//银行卡号
            var bankAccount = isEmpty(payroll.bankAccount) ? '-' : payroll.bankAccount;
            html += '<li class="subitem subitem1">' + bankAccount + '</li>';//开户行
            var appraisalsSupplement = isEmpty(payroll.appraisalsSupplement) ? '0' : payroll.appraisalsSupplement;//绩效补充
            var wipedAmount = isEmpty(payroll.wipedAmount) ? '0' : payroll.wipedAmount;//报销金额
            baseWages = (baseWages - appraisalsSupplement - wipedAmount).toFixed(2);
            html += '<li class="btn2"  id="baseWages' + payroll.id + '">' + baseWages + '</li>';//基本工资
            var addWages = isEmpty(payroll.addWages) ? '-' : payroll.addWages;
            html += '<li class="subitem subitem2" id="addWages' + payroll.id + '">' + addWages + '</li>';//调薪后基本工资

            html += '<li class="btn3" id="attendanceDeduction' + payroll.id + '">' + attendanceDeduction + '</li>';//缺勤扣款
            var absenceDayNumber = isEmpty(payroll.absenceDayNumber) ? '0' : payroll.absenceDayNumber;
            //html += '<li class="subitem subitem3"><input value="' + absenceDayNumber + '" onchange="absenceDayNumberUpdate(' + payroll.id + ',this)"/></li>';//缺勤天数
            html += '<li class="subitem subitem3"><input value="' + absenceDayNumber + '" onkeyup="this.value=this.value.replace(/[^0-9.]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^0-9.]/g,\'\')" onchange="basePayUpdate(' + payroll.id + ',this,0)"/></li>';//缺勤天数
            var totalAllowance = isEmpty(payroll.totalAllowance) ? '0' : payroll.totalAllowance;
            html += '<li class="btn4" id="totalAllowance' + payroll.id + '">' + totalAllowance + '</li>';//津贴总额
            //津贴
            var allowanceList = payroll.allowanceList;
            if (!isEmpty(allowanceList)) {
                var length1 = allowanceList.length;
                for (var j = 0; j < length1; j++) {
                    var detailValue = isEmpty(allowanceList[j].detailValue) ? '0' : allowanceList[j].detailValue;
                    //html += '<li class="subitem subitem4"><input value="' + detailValue + '"/></li>';
                    html += '<li class="subitem subitem4" id="allowanceList' + payroll.id + allowanceList[j].detailName + '">' + detailValue + '</li>';
                }
            }
            var totalBonus = isEmpty(payroll.totalBonus) ? '0' : payroll.totalBonus;
            html += '<li class="btn5" id="totalBonus' + payroll.id + '">' + totalBonus + '</li>';//总奖金
            //奖金
            var bonusList = payroll.bonusList;
            if (!isEmpty(bonusList)) {
                var length1 = bonusList.length;
                for (var j = 0; j < length1; j++) {
                    var detailValue = isEmpty(bonusList[j].detailValue) ? '0' : bonusList[j].detailValue;
                    html += '<li class="subitem subitem5"><input value="' + detailValue + '"  onkeypress = "return event.keyCode>=49||event.keyCode<=57||event.keyCode==46||event.keyCode==8" onchange="bonusUpdate(' + payroll.id + ',' + bonusList[j].id + ',this)" onpaste = "return !clipboardData.getData(\'text\').match(/\D/)" onkeyup/></li>';
                }
            }
            //html += '<li class="subitem subitem5"><input value="200"/></li>';
            if ($('#isSocialSecurity').val() == '1') {
                var socialSecurityFund = isEmpty(payroll.socialSecurityFund) ? '0' : payroll.socialSecurityFund;
                html += '<li class="btn6" id="socialSecurityFund' + payroll.id + '">' + socialSecurityFund + '</li>';//社保公积金
                var socialSecurity = isEmpty(payroll.socialSecurity) ? '0' : payroll.socialSecurity;
                html += '<li class="subitem subitem6"><input value="' + socialSecurity + '" id="socialSecurity' + payroll.id + '"  onkeyup="this.value=this.value.replace(/[^0-9.]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^0-9.]/g,\'\')" onchange="basePayUpdate(' + payroll.id + ',this,3)"/></li>';//社保
                var fund = isEmpty(payroll.fund) ? '0' : payroll.fund;
                html += '<li class="subitem subitem6"><input value="' + fund + '" id="fund' + payroll.id + '"  onkeyup="this.value=this.value.replace(/[^0-9.]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^0-9.]/g,\'\')" onchange="basePayUpdate(' + payroll.id + ',this,4)"/></li>';//公积金
            }
            var pretax = isEmpty(payroll.pretax) ? '0' : payroll.pretax;
            html += '<li><input value="' + pretax + '" id="pretax' + payroll.id + '"  onkeyup="this.value=this.value.replace(/[^0-9.\\-]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^0-9.\\-]/g,\'\')" onchange="basePayUpdate(' + payroll.id + ',this,1)"/></li>';//税前补发/扣款
            html += '<li id="appraisalsSupplement' + payroll.id + '">' + appraisalsSupplement + '</li>';//绩效补充
            var shouldAmount = isEmpty(payroll.shouldAmount) ? '0' : payroll.shouldAmount;
            html += '<li id="shouldAmount' + payroll.id + '">' + shouldAmount + '</li>';//应发工资
            var personalTax = isEmpty(payroll.personalTax) ? '0' : payroll.personalTax;
            html += '<li  id="personalTax' + payroll.id + '">' + personalTax + '</li>';//个税
            var afterTax = isEmpty(payroll.afterTax) ? '0' : payroll.afterTax;
            html += '<li><input value="' + afterTax + '" id="afterTax' + payroll.id + '" onkeyup="this.value=this.value.replace(/[^0-9.\\-]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^0-9.\\-]/g,\'\')" onchange="basePayUpdate(' + payroll.id + ',this,2)"/></li>';//税后补发/扣款
            html += '<li id="wipedAmount' + payroll.id + '">' + wipedAmount + '</li>';//报销金额
            var realWage = isEmpty(payroll.realWage) ? '0' : payroll.realWage;
            html += '<li id="realWage' + payroll.id + '">' + realWage + '</li>';//实发工资
            html += '<li id="salaset-bodylast' + payroll.id + '" class="salaset-bodylast">';
            //员工状态 1入职 2转正 3离职 4删除  是否定薪 0:未定  1:已定
            if ((payroll.stationCustomerState == '1' || payroll.stationCustomerState == '2') && payroll.isSalary == '0') {
                html += '<i onclick=onCustomerRequire2("../customerSalary/toSetSalary.htm?payrollId=' + payroll.id + '&customerId=' + payroll.customerId + '","定薪",600,400)>定薪</i>&nbsp;';
            }
            else if ((payroll.stationCustomerState == '1' || payroll.stationCustomerState == '2') && payroll.isSalary == '1') {
                html += '<i onclick=onCustomerRequire2("../customerSalary/toUpdateSalary.htm?lastDate=' + payCycleEndDate + '&payrollId=' + payroll.id + '&customerId=' + payroll.customerId + '","调薪",550,500);>调薪</i>&nbsp;';
            } else {
                html += '<p>-</p>';
            }
            html += '</li>';
            html += '</ul>';
        }
        $('.salaset-list').html(html);
        //初始化监听
        initListener()
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
    html += '<li  class="btn1" ><span>员</span>姓名</li>';
    html += '<li>部门</li>';
    html += '<li>手机号</li>';
    html += '<li class="subitem subitem1">入职日期</li>';
    html += '<li class="subitem subitem1">聘用形式</li>';
    html += '<li class="subitem  subitem1 idCard">身份证</li>';
    html += '<li class="subitem subitem1 bankNumber">工资卡</li>';
    html += '<li class="subitem subitem1">开户行</li>';
    html += '<li class="btn2"><span>薪</span>基本工资</li>';
    html += '<li class="subitem subitem2">调薪后基本工资</li>';
    html += '<li class="btn3"><span>勤</span>缺勤扣款</li>';
    html += '<li class="subitem subitem3">缺勤天数</li>';
    html += '<li class="btn4"><span>津</span>津贴总额</li>';
    //津贴
    var allowanceList = rowData.allowanceList;
    if (!isEmpty(allowanceList)) {
        var length = allowanceList.length;
        for (var i = 0; i < length; i++) {
            html += '<li class="subitem subitem4">' + allowanceList[i].detailName + '</li>';
        }
    }
    //奖金
    html += '<li class="btn5"><span>奖</span>奖金总额</li>';
    var bonusList = rowData.bonusList;
    if (!isEmpty(bonusList)) {
        var length = bonusList.length;
        for (var i = 0; i < length; i++) {
            html += '<li class="subitem subitem5">' + bonusList[i].detailName + '</li>';
        }
    }
    //html += '<li class="subitem subitem5">鼓励费</li>';
    if ($('#isSocialSecurity').val() == '1') {
        html += '<li class="btn6"><span>社</span>社保公积金</li>';
        html += '<li class="subitem subitem6">社保</li>';
        html += '<li class="subitem subitem6">公积金</li>';
    }
    html += '<li>税前补发/扣款</li>';
    html += '<li>绩效补充</li>';
    html += '<li>应发工资</li>';
    html += '<li>个税</li>';
    html += '<li>税后补发/扣款</li>';
    html += '<li>报销金额</li>';
    html += '<li>实发工资</li>';
    html += '<li>操作</li>';
    html += '</ul>';
    return html;
}

/**
 * 初始化监听事件
 */
function initListener() {
    for (var i = 1; i < 7; i++) {
        $('.btn' + (i)).attr('onoff', 0);
        if (!isEmpty($('.btn' + (i))[0])) {
            $('.btn' + (i))[0].index = i;
            $('.btn' + (i)).click(function () {

                if ($(this).attr('onoff') == 0) {
                    $('.subitem' + this.index).removeClass('subitem');
                    $(this).attr('onoff', 1);
                    $(this).find('span').animate({
                        "width": "100px"
                    }, 300).css({
                        "color": "#fff",
                        "background": "#b0ccd7 url(" + BASE_PATH + "/resource/img/salary/opention2.png) no-repeat right"
                    });
                } else {
                    $('.subitem' + this.index).addClass('subitem');
                    $(this).attr('onoff', 0);
                    $(this).find('span').animate({
                        "width": "40px"
                    }, 300).css({
                        'color': '#333',
                        "background": "#dbe9ee url(" + BASE_PATH + "/resource/img/salary/opention1.png) no-repeat right"
                    });
                }
            })
        }
    }

    $('.salaset-list input').focus(function () {
        $(this).css('text-align', 'left')
    });

    $('.salaset-list input').blur(function () {
        $(this).css('text-align', 'center')
    })
}


/**
 * 基本工资
 */
function basePayUpdate(id, $this, type) {
    if (isEmpty($this.value)) {
        $this.value = $this.defaultValue;
    } else {
        $this.value = parseFloat($this.value);
    }
    var param = '';
    if (type == '0') {//缺勤天数
        param = {'absenceDayNumber': $this.value, 'id': id, 'type': 0};
    } else if (type == '1') {//税前补发/扣款
        param = {'pretax': $this.value, 'id': id, 'type': 1};
    } else if (type == '2') {//税后补发/扣款
        param = {'afterTax': $this.value, 'id': id, 'type': 2};
    } else if (type == '3') {//社保
        param = {'socialSecurity': $this.value, 'id': id, 'type': 3};
    } else if (type == '4') {//公积金
        param = {'fund': $this.value, 'id': id, 'type': 4};
    }
    //更新社保公积金
    updateBasePay(id, param);
}

/**
 * 奖金
 * @param id
 * @param $this
 */
function bonusUpdate(parentId, id, $this) {
    if (isEmpty($this.value)) {
        $this.value = $this.defaultValue;
    } else {
        $this.value = parseFloat($this.value);
    }
    var param = {'parentId': parentId, 'id': id, 'detailValue': $this.value};
    $.ajax({
        type: 'post',
        url: 'bonusUpdate.htm',
        data: param,
        success: function (data) {
            if (data.success) {
                updateRowData(parentId, data.data);
            } else {
                showWarning(data.message);
            }
        }
    });
}

/**
 * 更新基本工资
 * @param id
 * @param param
 * @param type
 */
function updateBasePay(id, param) {
    var index = layer.load();
    $.ajax({
        type: 'post',
        url: 'updateBasePay.htm',
        data: param,
        success: function (data) {
            //关闭
            layer.close(index);
            if (data.success) {
                updateRowData(id, data.data);
            } else {
                showWarning(data.message);
            }
        }
    });
}

/**
 * 更新行数据
 * @param id
 * @param data
 */
function updateRowData(parentId, data) {
    var appraisalsSupplement = isEmpty(data.appraisalsSupplement) ? 0 : data.appraisalsSupplement;//绩效补充
    var wipedAmount = isEmpty(data.wipedAmount) ? 0 : data.wipedAmount;//报销金额
    var baseWages = isEmpty(data.baseWages) ? 0 : data.baseWages;
    baseWages = (baseWages - appraisalsSupplement - wipedAmount).toFixed(2);
    //基本工资
    $('#baseWages' + parentId).html(baseWages);
    //调薪后工资
    $('#addWages' + parentId).html(data.addWages);
    //社保公积金
    $('#socialSecurityFund' + parentId).html(data.socialSecurityFund);
    //缺勤扣款
    $('#attendanceDeduction' + parentId).html(data.attendanceDeduction);
    //税前补发/扣款
    $('#pretax' + parentId).val(data.pretax);
    //税后补发/扣款
    $('#afterTax' + parentId).val(data.afterTax);
    //应发
    $('#shouldAmount' + parentId).html(data.shouldAmount);
    //实发
    $('#realWage' + parentId).html(data.realWage);
    //个税
    $('#personalTax' + parentId).html(data.personalTax);
    //奖金总额
    $('#totalBonus' + parentId).html(data.totalBonus);
    //津贴总额
    $('#totalAllowance' + parentId).html(data.totalAllowance);
    //绩效补充
    $('#appraisalsSupplement' + parentId).html(data.appraisalsSupplement);
    //报销金额
    $('#wipedAmount' + parentId).html(data.wipedAmount);

    //更新津贴明细
    var allowanceList = data.allowanceList;
    if (!isEmpty(allowanceList)) {
        var length1 = allowanceList.length;
        for (var i = 0; i < length1; i++) {
            var detailValue = isEmpty(allowanceList[i].detailValue) ? '0' : allowanceList[i].detailValue;
            $('#allowanceList' + parentId + allowanceList[i].detailName).html(detailValue)
        }
    }
    //更新操作项
    if (!isEmpty(data.userName)) {
        var html = '';
        //员工状态 1入职 2转正 3离职 4删除  是否定薪 0:未定  1:已定
        if ((data.stationCustomerState == '1' || data.stationCustomerState == '2') && data.isSalary == '0') {
            html += '<i onclick=onCustomerRequire2("../customerSalary/toSetSalary.htm?payrollId=' + data.id + '&customerId=' + data.customerId + '","定薪",600,400)>定薪</i>&nbsp;';
        }
        else if ((data.stationCustomerState == '1' || data.stationCustomerState == '2') && data.isSalary == '1') {
            html += '<i onclick=onCustomerRequire2("../customerSalary/toUpdateSalary.htm?lastDate=' + payCycleEndDate + '&payrollId=' + data.id + '&customerId=' + data.customerId + '","调薪",550,500);>调薪</i>&nbsp;';
        } else {
            html += '<p>-</p>';
        }
        $('#salaset-bodylast' + parentId).html(html);
    }

    //更新字体颜色
    if (data.customerCurrentExpense > data.baseWages - data.attendanceDeduction) {
        $('#salaset' + parentId).addClass('red');
    } else {
        $('#salaset' + parentId).removeClass('red');
    }
}

/**
 * 跳转到当月工资页面
 */
function onMonthlyWage(url, title) {
    var index = layer.open({
        type: 2,
        title: false,
        closeBtn: 0,
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: url,
        cancel: function () {
            //initData();
            location.reload();
        }
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 跳转到薪资设置页面
 */
function onAllowance(url, title) {
    var index = layer.open({
        type: 2,
        title: false,
        closeBtn: 0,
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: url,
        cancel: function () {
            //initData();
            location.reload();
        }
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 跳转到批量操作页面
 */
function onBatchUpload(url, title) {
    var index = layer.open({
        type: 2,
        title: false,
        closeBtn: 0,
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: url,
        cancel: function () {
            //initData();
            location.reload();
        }
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 跳转到入职需知页面
 */
function onCustomerRequire(url, title) {
    var index = layer.open({
        type: 2,
        title: false,
        closeBtn: 0,
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: url
    });
    //窗口最大化
    layer.full(index);
}

function onCustomerRequire2(url, title, width, height) {
    var index = layer.open({
        type: 2,
        title: false,
        maxmin: false,
        closeBtn: 0,
        shadeClose: false, //点击遮罩关闭层
        area: [width + "px", height + "px"],
        content: url
    });
    //窗口最大化
    //layer.full(index);
}
