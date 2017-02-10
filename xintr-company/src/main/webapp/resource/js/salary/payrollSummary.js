$(function () {
    //初始化界面
    initPage();
})

/**
 * 初始化界面
 */
function initPage() {
    var param = {'year': '2016'};
    //获取周期数据
    ajaxJson('getPayrollSummary.htm', param, function (data) {
        var payrollData = data.data;
        if (!isEmpty(payrollData) && payrollData.length > 0) {
            var length = payrollData.length;
            for (var i = 0; i < length; i++) {
                if (payrollData[i].type == '0') {
                    //薪资核算
                    $('.collectAll').append(generateSalary(payrollData[i]));
                } else {
                    //急速发工资
                    $('.collectAll').append(generateRapidly(payrollData[i]));
                }
            }
        }
    })
}

/**
 * 薪资核算
 * @param payroll
 */
function generateSalary(payroll) {
    var typeId = payroll.type + '' + payroll.id.toString();
    var html = '<div class="collectAllCon">' +
        '<div class="collectAllCon1" value="0" onclick="salaryOpen(' + this + ',' + typeId + ')">' +
        '</div>' +
        '<div class="collectAllConRight">' +
        '<div class="collectAllCon2">' +
        // '<div class="collect2Text"><span>6</span>月工资单</div>' +
        '<div class="collect2Text">' + payroll.excelTitle + '</div>' +
        // '<div class="collect2Figure">2016-07-05 <span>15:30</span></div>' +
        '<div class="collect2Figure">' + unixToTime(payroll.createTime, "y-m-d h:m") + '</div>' +
        '<div class="collectAllConPayroll" id="collectAllConPayroll' + typeId + '">' +
        '<p>工资单</p>' +
        '<img src="/resource/img/salary/sanjiao.png" alt="">' +
        '</div>' +
        '<div class="collectAllCon2Tax" id="collectAllCon2Tax' + typeId + '">' +
        '<p>个税申报表</p>' +
        '<img src="/resource/img/salary/sanjiao.png" alt="">' +
        '</div>' +
        '</div>' +
        '<div class="collectAllCon3">' +
        '<div class="collectConText">人工成本</div>' +
        '<div class="collectNum">' + formatMoney(payroll.laborCost, 0) + '</div>' +
        '</div>' +
        '<div class="collectAllCon4">' +
        '<div class="collectConText">税前工资</div>' +
        '<div class="collectNum">' + formatMoney(payroll.pretaxPayroll, 0) + '</div>' +
        '</div>' +
        '<div class="collectAllCon5">' +
        '<div class="collectConText">五险一金</div>' +
        '<div class="collectNum">' + formatMoney(payroll.socialSecurityFund, 0) + '</div>' +
        '</div>' +
        '<div class="collectAllCon6">';

    if (payroll.isPayOff == '3') {
        html += '<div class="collectConText">工资已发放</div>' +
            '<div class="collectNum">' + fastToDate(payroll.payDay) + '</div>';
    } else {
        html += '<div class="collectConPay">发工资</div>';
    }
    html += '</div>' +
        '<div class="collectDerive" id="collectDerive' + typeId + '">' +
        '<div class="collectDerive1">已离职<span>3</span><span>人</span></div>' +
        '<div class="collectDerive2">工资为负<span>2</span><span>人</span></div>' +
        '<div class="collectDerive3">导出当前工资表</div>' +
        '</div>' +
        '</div>' +
        '</div>';
    return html;
}

/**
 * 急速发工资
 * @param payroll
 */
function generateRapidly(payroll) {
    var typeId = payroll.type + '' + payroll.id.toString();
    var html = '<div class="quiAllCon" >' +
        '<div class="quiAllCon1" id="quiAllCon1' + typeId + '"  rapidlyFlag = "0" onclick="rapidlyOpen(' + typeId + ')">' +
        '</div>' +
        '<div class="quiAllCon2">' +
        '<div class="quiAll2Text">' + payroll.excelTitle + '<img src="/resource/img/salary/rapidHair.png" alt=""></div>' +
        '<div class="quiAll2Figure">' + unixToTime(payroll.createTime, "y-m-d h:m") + '</div>' +
        '</div>' +
        '<div class="quiAllCon3">' +
        '<div class="quiAll3Text">发薪人数</div>' +
        '<div class="quiAll3Figure">' + payroll.peopleNumber + '</div>' +
        '</div>' +
        '<div class="quiAllCon4">' +
        '<div class="quiAll3Text">发薪金额</div>' +
        '<div class="quiAll3Figure">' + formatMoney(payroll.totalWages, 0) + '</div>' +
        '</div>' +
        '<div class="quiAllCon5">';
    if (payroll.isPayOff == '3') {
        html += '<div class="quiAllCon5End">工资已发放</div>' +
            '<div class="quiAllCon5Time">' + fastToDate(payroll.payDay) + '</div>';
    } else if(payroll.isPayOff == '2'){
        html += '<div class="quiAllCon5End">发放中</div>';
    }
    else{
        html += '<div class="quiAllCon5Not">工资未发放</div>' +
            '<div class="quiAllCon5Fut">请到 <a href="../account/index.htm">账户中心</a> 完成付款</div>';
    }

    html += '</div></div><div class="rapidlyTable" style="display: none" id="rapidlyTable' + typeId + '"><table class="table" id="table' + typeId + '"></table>';
    html += '<div style="overflow: hidden;border:1px solid #b8dae5;border-top:none;"><div id="pagePayroll' + typeId + '"></div></div></div>';

    return html;
}

/**
 * 薪资核算展开或收缩
 * @param $this
 */
function salaryOpen($this, typeId) {
    var value = $this.attr('value');
    if (value == '0') {
        $this.attr('value', '1');
        $('#tablePay' + typeId).show();
        $('#tableTax' + typeId).hide();
        $('#collectAllCon2Tax' + typeId).find('img').show();
        $this.addClass('collectAllCon11');
    } else {
        $this.attr('value', '0');
        $('#tablePay' + typeId).hide();
        $('#tableTax' + typeId).hide();
        $('#collectAllCon2Tax' + typeId).find('img').hide();
    }
    //改变箭头方向
    $this.toggle('collectAllCon11');
    //标题位置移动
    $this.next().find('.collectAllCon2').find('div').eq(0).toggleClass('collect2Text1');
    //时间位置移动
    $this.next().find('.collectAllCon2').find('div').eq(1).toggleClass('collect2Figure1');
    //工资单
    $('#collectAllConPayroll' + typeId).toggle();
    //个税申报表
    $('#collectAllCon2Tax' + typeId).toggle();
    //导出栏
    $('#collectDerive' + typeId).toggle();
}

/**
 * 薪资核算展开或收缩
 * @param $this
 * @param typeId
 */
function rapidlyOpen(typeId) {
    var rapidlyFlag = $('#quiAllCon1' + typeId).attr('rapidlyFlag');
    hidden();
    if (rapidlyFlag == '0') {
        $('#quiAllCon1' + typeId).addClass('quiAllCon11');
        $('#quiAllCon1' + typeId).attr('rapidlyFlag', 1);
        //展开
        if (isEmpty($('#table' + typeId).html())) {
            //加载急速发工资数据
            loadRapidlyData(typeId, 0);
        }
        $('#rapidlyTable' + typeId).show();
    } else {
        //收起
        $('#rapidlyTable' + typeId).hide()
    }

}

/**
 * 加载急速发工资数据
 * @param id
 */
function loadRapidlyData(typeId, pageIndex) {
    var id = typeId.toString().substring(1, typeId.toString().length);
    var param = {'rapidlyPayrollExcelId': id, 'pageIndex': pageIndex};
    ajaxJson('loadRapidlyData.htm', param, function (data) {
        var payrollData = data.data;
        if (!isEmpty(payrollData) && payrollData.length > 0) {
            var html = getRapidlyTableThead() + '<tbody>';
            for (var i = 0; i < payrollData.length; i++) {
                html += '<tr>' +
                    '<td>' + (i + 1) + '</td>' +
                    '<td>' + payrollData[i].customerName + '</td>' +
                    '<td>' + payrollData[i].idCard + '</td>' +
                    '<td>' + payrollData[i].bankAccount + '</td>' +
                    '<td>' + payrollData[i].bankNumber + '</td>' +
                    '<td>' + formatMoney(payrollData[i].realWage, 0) + '</td>' +
                    '</tr>';
            }
            html += '</tbody>';
            $('#table' + typeId).html(html);
            if (isEmpty($('#pagePayroll').html())) {
                //生成分页
                generPageHtml(data.paginator, typeId, function (page) {
                    loadRapidlyData(typeId, page);
                });
            }
        }
    })
}

/**
 * 获取急速发工资表头
 */
function getRapidlyTableThead() {
    return '<thead>' +
        '<tr>' +
        '<td>序号</td>' +
        '<td>姓名</td>' +
        '<td>身份证</td>' +
        '<td>开户行</td>' +
        '<td>工资卡号</td>' +
        '<td>实发金额(元)</td>' +
        '</tr>' +
        '</thead>';
}

/**
 * 收起所有显示
 */
function hidden() {
    //重置所有展开收缩标志
    $('.quiAllCon1').attr('rapidlyFlag',0);
    //重置所有展开收缩标志箭头标志
    $('.quiAllCon1').removeClass('quiAllCon11');
    //收缩所有表格
    $('.rapidlyTable').hide()
}

/**
 * 初始化分页
 * @param pageInfo
 */
function generPageHtml(paginator, typeId, callback) {
    laypage({
        cont: 'pagePayroll' + typeId, //容器。值支持id名、原生dom对象，jquery对象,
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
                if ($.isFunction(callback)) {
                    callback(obj.curr);
                }
            }
        }
    });
}