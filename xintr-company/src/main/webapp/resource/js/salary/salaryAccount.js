$(function () {
    //初始化监听
    initListener();
    //初始化数据
    initData();
    $('.tableTitle').last().css('border-bottom','1px solid #999');
})

/**
 * 初始化监听
 */
function initListener() {

}

/**
 * 初始化数据
 */
function initData(param) {
    ajaxJson('dataList.htm', param, function (data) {
        //组装数据
        showData(data.data);
        //生成分页
        generPageHtml(data.paginator);
    })
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
            var attendanceNumberDay = isEmpty(payroll.attendanceNumberDay) ? '0' : payroll.attendanceNumberDay;//应出勤天数
            var absenceDayNumber = isEmpty(payroll.absenceDayNumber) ? '0' : payroll.absenceDayNumber;//实际出勤
            var attendanceDeduction = isEmpty(payroll.attendanceDeduction) ? '0' : payroll.attendanceDeduction;//缺勤扣款
            var deptName = isEmpty(payroll.deptName) ? "-" : payroll.deptName;//部门名称
            var totalAllowance = isEmpty(payroll.totalAllowance) ? '0' : payroll.totalAllowance;//津贴总额
            var totalBonus = isEmpty(payroll.totalBonus) ? '0' : payroll.totalBonus;//奖金总额
            var shouldAmount = isEmpty(payroll.shouldAmount) ? '0' : payroll.shouldAmount;//应发工资
            var socialSecurityFund = isEmpty(payroll.socialSecurityFund) ? '0' : payroll.socialSecurityFund;//社保公积金
            var socialSecurity = isEmpty(payroll.socialSecurity) ? '0' : payroll.socialSecurity;//社保
            var fund = isEmpty(payroll.fund) ? '0' : payroll.fund;//公积金
            var pretax = isEmpty(payroll.pretax) ? '0' : payroll.pretax;//税前调整（元）
            var personalTax = isEmpty(payroll.personalTax) ? '0' : payroll.personalTax;//个税（元）
            var afterTax = isEmpty(payroll.afterTax) ? '0' : payroll.afterTax;//税后调整
            var realWage = isEmpty(payroll.realWage) ? '0' : payroll.realWage;//实发工资（元）
            var appraisalsSupplement = isEmpty(payroll.appraisalsSupplement) ? '0' : payroll.appraisalsSupplement;//绩效补充
            var wipedAmount = isEmpty(payroll.wipedAmount) ? '0' : payroll.wipedAmount;//报销金额
            baseWages = (baseWages - appraisalsSupplement - wipedAmount).toFixed(2);
            html += '<ul class="tableTitle">' +
                //序号
                '<li>' + (i + 1) + '</li>' +
                //姓名
                '<li>' + payroll.userName + '</li>' +
                //手机号
                '<li class="subitem1">' + payroll.mobilePhone + '</li>' +
                //部门
                '<li class="subitem1">' + deptName + '</li>' +
                //基本工资（元）
                '<li id="baseWages' + payroll.id + '">' + baseWages + '</li>' +
                //津贴总额（元）
                '<li id="totalAllowance' + payroll.id + '">' + totalAllowance + '</li>';
            //津贴
            var allowanceList = payroll.allowanceList;
            if (!isEmpty(allowanceList)) {
                var length1 = allowanceList.length;
                for (var j = 0; j < length1; j++) {
                    var detailValue = isEmpty(allowanceList[j].detailValue) ? '0' : allowanceList[j].detailValue;
                    html += '<li class="subitem2" id="allowanceList' + payroll.id + allowanceList[j].detailName + '">' + detailValue + '</li>';
                }
            }
            //奖金总额（元）
            html += '<li id="totalBonus' + payroll.id + '">' + totalBonus + '</li>';
            var bonusList = payroll.bonusList;
            if (!isEmpty(bonusList)) {
                var length1 = bonusList.length;
                for (var j = 0; j < length1; j++) {
                    var detailValue = isEmpty(bonusList[j].detailValue) ? '0' : bonusList[j].detailValue;
                    html += '<li class="subitem3"><input type="text" value="' + detailValue + '" onkeypress = "return event.keyCode>=49||event.keyCode<=57||event.keyCode==46||event.keyCode==8" onchange="bonusUpdate(' + payroll.id + ',' + bonusList[j].id + ',this)" onpaste = "return !clipboardData.getData(\'text\').match(/\D/)" onkeyup></li>';
                }
            }
            html += '<li id="appraisalsSupplement' + payroll.id + '">' + appraisalsSupplement + '</li>';//绩效补充
            //应发工资（元）
            html += '<li id="shouldAmount' + payroll.id + '">' + shouldAmount + '</li>' +
                //考勤扣款（元）
                '<li id="attendanceDeduction' + payroll.id + '">' + attendanceDeduction + '</li>' +
                //应出勤天数
                '<li class="subitem4">' + attendanceNumberDay + '</li>' +
                //实际出勤
                '<li class="subitem4"><input type="text" value="' + absenceDayNumber + '" onkeyup="this.value=this.value.replace(/[^0-9.]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^0-9.]/g,\'\')" onchange="basePayUpdate(' + payroll.id + ',this,0)"/></li>';
            if ($('#isSocialSecurity').val() == '1') {
                //社保公积金（元）
                html += '<li>' + socialSecurityFund + '</li>' +
                    //社保代扣（元）
                    '<li class="subitem5"><input type="text" value="' + socialSecurity + '" id="socialSecurity' + payroll.id + '"  onkeyup="this.value=this.value.replace(/[^0-9.]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^0-9.]/g,\'\')" onchange="basePayUpdate(' + payroll.id + ',this,3)"/></li>' +
                    //公积金代扣（元）
                    '<li class="subitem5"><input type="text" value="' + fund + '" id="fund' + payroll.id + '"  onkeyup="this.value=this.value.replace(/[^0-9.]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^0-9.]/g,\'\')" onchange="basePayUpdate(' + payroll.id + ',this,4)"/></li>';
            }
            //税前调整（元）
            html += '<li><input type="text" value="' + pretax + '" id="pretax' + payroll.id + '"  onkeyup="this.value=this.value.replace(/[^0-9.\\-]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^0-9.\\-]/g,\'\')" onchange="basePayUpdate(' + payroll.id + ',this,1)"/></li>' +
                //个税（元）
                '<li id="personalTax' + payroll.id + '">' + personalTax + '</li>' +
                //税后调整
                '<li><input type="text" value="' + afterTax + '" id="afterTax' + payroll.id + '" onkeyup="this.value=this.value.replace(/[^0-9.\\-]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^0-9.\\-]/g,\'\')" onchange="basePayUpdate(' + payroll.id + ',this,2)"/></li>' +
                //报销金额
                '<li id="wipedAmount' + payroll.id + '">' + wipedAmount + '</li>' +
                //实发工资（元）
                '<li  id="rightBorder realWage' + payroll.id + '" style="border-right:1px solid #999;">' + realWage + '</li>' +
                '</ul>';
        }
        //添加分页
        $('#tableSalary').html(html);
        //初始化监听
        initTableListener();
    }
}

/**
 * 获取标题
 */
function getTitle(rowData) {
    var html = '<ul class="tableTitle tableTitle1">' +
        '<li>序号</li>' +
        '<li>姓名<span class="tableBasic1">+</span></li>' +
        '<li class="subitem1">手机号</li>' +
        '<li class="subitem1">部门</li>' +
        '<li>基本工资（元）</li>';

    html += '<li>津贴总额（元）<span class="tableBasic2">+</span></li>';
    //津贴
    var allowanceList = rowData.allowanceList;
    if (!isEmpty(allowanceList)) {
        var length = allowanceList.length;
        for (var i = 0; i < length; i++) {
            html += '<li class="subitem2">' + allowanceList[i].detailName + '</li>';
        }
    }
    html += '<li>奖金总额（元）<span class="tableBasic3">+</span></li>';
    //奖金
    var bonusList = rowData.bonusList;
    if (!isEmpty(bonusList)) {
        var length = bonusList.length;
        for (var i = 0; i < length; i++) {
            html += '<li class="subitem3"> ' + bonusList[i].detailName + '</li>';
        }
    }
    html += '<li>绩效补充</li>';
    html += '<li>应发工资（元）</li>' +
        '<li>考勤扣款（元）<span class="tableBasic4">+</span></li>' +
        '<li class="subitem4">应出勤天数</li>' +
        '<li class="subitem4">实际出勤</li>';
    if ($('#isSocialSecurity').val() == '1') {
        html += '<li>社保公积金（元）<span class="tableBasic5">+</span></li>' +
            '<li class="subitem5">社保代扣（元）</li>' +
            '<li class="subitem5">公积金代扣（元）</li>';
    }
    html += '<li>税前调整（元）</li>' +
        '<li>个税（元）</li>' +
        '<li>税后调整</li>' +
        '<li>报销金额</li>' +
        '<li style="border-right:1px solid #999;">实发工资（元）</li>' +
        '</ul>';
    return html;
}

/**
 * 生成分页控件
 */
function generPageHtml(paginator) {
    laypage({
        cont: 'salaryPage', //容器。值支持id名、原生dom对象，jquery对象,
        pages: paginator.totalPages, //总页数
        skin: 'molv', //皮肤
        skip: true, //是否开启跳页
        first: 1, //将首页显示为数字1,。若不显示，设置false即可
        last: paginator.totalPages, //将尾页显示为总页数。若不显示，设置false即可
        prev: '上一页', //若不显示，设置false即可
        next: '下一页', //若不显示，设置false即可
        skip: true, //是否开启跳页
        curr: paginator.page || 1, //当前页
        jump: function (obj, first) { //触发分页后的回调
            if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                // console.info(obj.curr);
                initData({'pageIndex': obj.curr});
            }
        }
    });
}

/**
 * 初始化监听事件
 */
function initTableListener() {
    for (var i = 1; i < 6; i++) {
        $('.tableBasic' + (i)).attr('onoff', 0);
        if (!isEmpty($('.tableBasic' + (i))[0])) {
            $('.subitem' + i).hide();
            $('.tableBasic' + (i))[0].index = i;
            $('.tableBasic' + (i)).on('click', function () {
                if ($(this).attr('onoff') == 0) {
                    $(this).attr('onoff', 1);
                    $('.subitem' + this.index).show();
                    $(this).html('-')
                } else {
                    $(this).attr('onoff', 0);
                    $('.subitem' + this.index).hide();
                    $(this).html('+')
                }
            })
        }
    }
}


/**
 * 奖金
 * @param id
 * @param $this
 */
function bonusUpdate(parentId, id, $this) {
    var index = layer.load();
    if (isEmpty($this.value)) {
        $this.value = $this.defaultValue;
    } else {
        $this.value = parseFloat($this.value);
    }
    var param = {'parentId': parentId, 'id': id, 'detailValue': $this.value};
    $.ajax({
        type: 'post',
        url: '../payrollAccount/bonusUpdate.htm',
        data: param,
        success: function (data) {
            //关闭
            layer.close(index);
            if (data.success) {
                updateRowData(parentId, data.data);
            } else {
                showWarning(data.message);
            }
        }
    });
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
 * 更新基本工资
 * @param id
 * @param param
 * @param type
 */
function updateBasePay(id, param) {
    var index = layer.load();
    $.ajax({
        type: 'post',
        url: '../payrollAccount/updateBasePay.htm',
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
    // //调薪后工资
    // $('#addWages' + parentId).html(data.addWages);
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
    //更新字体颜色
    // if (data.customerCurrentExpense > data.baseWages - data.attendanceDeduction) {
    //     $('#salaset' + parentId).addClass('red');
    // } else {
    //     $('#salaset' + parentId).removeClass('red');
    // }
}

/**
 * 搜索
 */
function onSearch() {
    //组装参数
    var param = {
        'payCycleId': $('#payCycleId').val(),
        'userName': $('#userName').val()
    };
    var index = layer.load();
    $.ajax({
        type: 'post',
        url: '../payrollAccount/queryCustomerPayroll.htm',
        data: param,
        success: function (data) {
            //关闭
            layer.close(index);
            if (data.success) {
                isSearch = true;
                showData(data.data);
                //生成分页控件
                if (!isEmpty(data.data)) {
                    generPageHtml(data.paginator);
                }
            } else {
                showWarning(data.message);
            }
        }
    });
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

