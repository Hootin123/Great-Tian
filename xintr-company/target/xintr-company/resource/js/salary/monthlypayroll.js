$(function () {
    var param = {'payCycleId': $('#payCycleId').val()};
    //初始化数据
    initData(param);
    //初始化监听
    intiListener();
});


/**
 * 初始化数据
 */
function initData(param) {

    ajaxJson('../payrollAccount/dataList.htm', param, function (data) {
        showData(data.data);
        if (!isEmpty(data.data)) {
            //生成分页控件
            generPageHtml(data.paginator);
            //更新人数
            $('#peopleNumber').html(data.paginator.totalCount);
        }
    });
    // $.ajax({
    //     type: 'post',
    //     url: '../payrollAccount/dataList.htm',
    //     data: param,
    //     success: function (data) {
    //         //关闭
    //         layer.close(index);
    //         if (data.success) {
    //             showData(data.data);
    //             if (!isEmpty(data.data)) {
    //                 //生成分页控件
    //                 generPageHtml(data.paginator);
    //                 //更新人数
    //                 $('#peopleNumber').html(data.paginator.totalCount);
    //             }
    //         } else {
    //             showWarning(data.message);
    //         }
    //     }
    // });
}

/**
 * 初始化监听
 */
function intiListener() {
    //导出弹框
    $('.der').on('click', function () {
        //$(this).addClass('active').siblings().removeClass('active');
        $('.mengban').show();
        $('.deriver').show();
    });

    //导出取消
    $('.cBt').on('click', function () {
        $('.mengban').hide();
        $('.deriver').hide();
    });

    $('#close').on('click', function () {
        $('.mengban').hide();
        $('.deriver').hide();
    });

    //发工资事件绑定
    $('.paf').on('click', function () {
        onPayOff();
    });

    //审核完毕事件绑定
    $('#addCustomer').on('click', function () {
        //审核工资单
        onApprovalPayroll();
    });

    //审核取消事件绑定
    $('#cancelBtn').on('click', function () {
        //取消
        onCancelPayroll();
    })
    //取消关闭
    $('#closeDetailBtn').click(function () {
        parent.location.reload();
    })

}

/**
 * 初始化导出列表
 * @param rowData
 */
function initReportList(rowData) {
    var html = '<li><input type="checkbox" data-value="userName" data-type="0" checked="checked">姓名</li>';
    html += '<li><input type="checkbox" data-value="deptName" data-type="0" checked="checked">部门</li>';
    html += '<li><input type="checkbox" data-value="mobilePhone" data-type="0" checked="checked">手机号</li>';
    html += '<li><input type="checkbox" data-value="entryDate" data-type="0" checked="checked">入职日期</li>';
    html += '<li><input type="checkbox" data-value="employMethod" data-type="0" checked="checked">聘用形式</li>';
    html += '<li><input type="checkbox" data-value="idCard" data-type="0" checked="checked">身份证</li>';
    html += '<li><input type="checkbox" data-value="bankNumber" data-type="0" checked="checked">工资卡</li>';
    html += '<li><input type="checkbox" data-value="bankAccount" data-type="0" checked="checked">开户行</li>';
    html += '<li><input type="checkbox" data-value="baseWages" data-type="0" checked="checked">基本工资</li>';
    html += '<li><input type="checkbox" data-value="addWages" data-type="0" checked="checked">调薪后基本工资</li>';
    html += '<li><input type="checkbox" data-value="attendanceDeduction" data-type="0" checked="checked">缺勤扣款</li>';
    html += '<li><input type="checkbox" data-value="absenceDayNumber" data-type="0" checked="checked">缺勤天数</li>';

    //津贴
    var allowanceList = rowData.allowanceList;
    if (!isEmpty(allowanceList)) {
        var length = allowanceList.length;
        for (var i = 0; i < length; i++) {
            html += '<li><input type="checkbox" data-value="' + allowanceList[i].detailName + '"  data-type="1"  checked="checked">' + allowanceList[i].detailName + '</li>';
        }
    }
    //奖金
    var bonusList = rowData.bonusList;
    if (!isEmpty(bonusList)) {
        var length = bonusList.length;
        for (var i = 0; i < length; i++) {
            html += '<li><input type="checkbox" data-value="' + bonusList[i].detailName + '"data-type="2"  checked="checked">' + bonusList[i].detailName + '</li>';
        }
    }
    if ($('#isSocialSecurity').val() == '1') {
        html += '<li><input type="checkbox" data-value="socialSecurity" data-type="0" checked="checked">社保</li>';
        html += '<li><input type="checkbox" data-value="fund" data-type="0" checked="checked">公积金</li>';
    }
    html += '<li><input type="checkbox" data-value="pretax" data-type="0" checked="checked">税前补发/扣款</li>';
    html += '<li><input type="checkbox" data-value="appraisalsSupplement" data-type="0" checked="checked">绩效补充</li>';
    html += '<li><input type="checkbox" data-value="shouldAmount" data-type="0" checked="checked">应发工资</li>';
    html += '<li><input type="checkbox" data-value="personalTax" data-type="0" checked="checked">个税</li>';
    html += '<li><input type="checkbox" data-value="afterTax" data-type="0" checked="checked">税后补发/扣款</li>';
    html += '<li><input type="checkbox" data-value="wipedAmount" data-type="0" checked="checked">报销金额</li>';
    html += '<li><input type="checkbox" data-value="realWage" data-type="0" checked="checked">实发工资</li>';
    $('#exportList').html(html);
}

/**
 * 导出
 */
function onExport() {
    //var index = layer.load();
    var customerPayrollExportDtos = [];
    $('#exportList input').each(function () {
        if ($(this).prop("checked")) {
            var param = {
                'key': $(this).attr('data-value'),
                'type': $(this).attr('data-type')
            };
            customerPayrollExportDtos.push(param);
        }
    });
    if (customerPayrollExportDtos.length <= 0) {
        showWarning('请选择需要导出的项');
    } else {
        //导出
        $('.exportBtn').attr('href', 'onExport.htm?payCycleId=' + $('#payCycleId').val() + '&customerPayrollExportDtos=' + JSON.stringify(customerPayrollExportDtos));
        //隐藏弹出框
        $('.mengban').hide();
        $('.deriver').hide();
    }
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
            var param = {'pageIndex': n};
            initData(param);
        },
        //getHref是在click模式下链接算法，一般不需要配置，默认代码如下
        getHref: function (n) {
            return '#';
        }
    }, true);
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
                html += '<ul class="salaset-body clear red">';
            } else {
                html += '<ul class="salaset-body clear">';
            }
            html += '<ul class="salaset-body clear">';
            html += '<li style="width: 40px">' + (i + 1) + '</li>';//序号
            html += '<li>' + payroll.userName + '</li>';//姓名
            var deptName = isEmpty(payroll.deptName) ? "-" : payroll.deptName;
            html += '<li>' + deptName + '</li>';//部门
            html += '<li>' + payroll.mobilePhone + '</li>';//手机号码
            html += '<li >' + fastToDate(payroll.entryDate) + '</li>';//入职日期
            var employMethod = payroll.employMethod;
            employMethod = employMethod == '1' ? '正式' : '劳务';
            html += '<li >' + employMethod + '</li>';//聘用方式
            var idCard = isEmpty(payroll.idCard) ? '-' : payroll.idCard;
            html += '<li class="idCard">' + idCard + '</li>';//身份证号码
            var bankNumber = isEmpty(payroll.bankNumber) ? '-' : payroll.bankNumber;
            html += '<li class=" bankNumber">' + bankNumber + '</li>';//银行卡号
            var bankAccount = isEmpty(payroll.bankAccount) ? '-' : payroll.bankAccount;
            html += '<li >' + bankAccount + '</li>';//开户行
            var appraisalsSupplement = isEmpty(payroll.appraisalsSupplement) ? '0' : payroll.appraisalsSupplement;//绩效补充
            var wipedAmount = isEmpty(payroll.wipedAmount) ? '0' : payroll.wipedAmount;//报销金额
            baseWages = (baseWages - appraisalsSupplement - wipedAmount).toFixed(2);
            html += '<li class="btn2"  id="baseWages' + payroll.id + '">' + baseWages + '</li>';//基本工资
            var addWages = isEmpty(payroll.addWages) ? '-' : payroll.addWages;
            html += '<li>' + addWages + '</li>';//调薪后基本工资

            html += '<li>' + attendanceDeduction + '</li>';//缺勤扣款
            var absenceDayNumber = isEmpty(payroll.absenceDayNumber) ? '0' : payroll.absenceDayNumber;
            html += '<li >' + absenceDayNumber + '</li>';//缺勤天数
            //var totalAllowance = isEmpty(payroll.totalAllowance) ? '0' : payroll.totalAllowance;
            //html += '<li>' + totalAllowance + '</li>';//津贴总额
            //津贴
            var allowanceList = payroll.allowanceList;
            if (!isEmpty(allowanceList)) {
                var length1 = allowanceList.length;
                for (var j = 0; j < length1; j++) {
                    var detailValue = isEmpty(allowanceList[j].detailValue) ? '0' : allowanceList[j].detailValue;
                    html += '<li >' + detailValue + '</li>';
                }
            }
            //var totalBonus = isEmpty(payroll.totalBonus) ? '0' : payroll.totalBonus;
            //html += '<li>' + totalBonus + '</li>';//总奖金
            //奖金
            var bonusList = payroll.bonusList;
            if (!isEmpty(bonusList)) {
                var length1 = bonusList.length;
                for (var j = 0; j < length1; j++) {
                    var detailValue = isEmpty(bonusList[j].detailValue) ? '0' : bonusList[j].detailValue;
                    html += '<li >' + detailValue + '</li>';
                }
            }
            //var socialSecurityFund = isEmpty(payroll.socialSecurityFund) ? '0' : payroll.socialSecurityFund;
            //html += '<li>' + socialSecurityFund + '</li>';//社保公积金
            if ($('#isSocialSecurity').val() == '1') {
                var socialSecurity = isEmpty(payroll.socialSecurity) ? '0' : payroll.socialSecurity;
                html += '<li>' + socialSecurity + '</li>';//社保
                var fund = isEmpty(payroll.fund) ? '0' : payroll.fund;
                html += '<li>' + fund + '</li>';//公积金
            }
            var pretax = isEmpty(payroll.pretax) ? '0' : payroll.pretax;
            html += '<li>' + pretax + '</li>';//税前补发/扣款
            html += '<li>' + appraisalsSupplement + '</li>';//绩效补充
            var shouldAmount = isEmpty(payroll.shouldAmount) ? '0' : payroll.shouldAmount;
            html += '<li>' + shouldAmount + '</li>';//应发工资
            var personalTax = isEmpty(payroll.personalTax) ? '0' : payroll.personalTax;
            html += '<li>' + personalTax + '</li>';//个税
            var afterTax = isEmpty(payroll.afterTax) ? '0' : payroll.afterTax;
            html += '<li>' + afterTax + '</li>';//税后补发/扣款
            html += '<li>' + wipedAmount + '</li>';//报销金额
            var realWage = isEmpty(payroll.realWage) ? '0' : payroll.realWage;
            html += '<li>' + realWage + '</li>';//实发工资
            html += '</ul>';
        }
        $('.salaset-list').html(html);
        //初始化导出列表
        initReportList(data[0]);
    } else {
        $('.salaset-list').html('');
        $('#kkpager').html('');
    }
}

/**
 * 获取标题
 */
function getTitle(rowData) {
    //设置计薪周期主键
    //$('#payCycleId').val(rowData.payCycleId);
    var html = '<ul class="salaset-ttle clear">';
    html += '<li style="width: 40px">序号</li>';
    html += '<li  ><span>员</span>姓名</li>';
    html += '<li>部门</li>';
    html += '<li>手机号</li>';
    html += '<li >入职日期</li>';
    html += '<li >聘用形式</li>';
    html += '<li class="idCard">身份证</li>';
    html += '<li class="bankNumber">工资卡</li>';
    html += '<li >开户行</li>';
    html += '<li >基本工资</li>';
    html += '<li >调薪后基本工资</li>';
    html += '<li>缺勤扣款</li>';
    html += '<li >缺勤天数</li>';
    //html += '<li >津贴总额</li>';
    //津贴
    var allowanceList = rowData.allowanceList;
    if (!isEmpty(allowanceList)) {
        var length = allowanceList.length;
        for (var i = 0; i < length; i++) {
            html += '<li>' + allowanceList[i].detailName + '</li>';
        }
    }
    //奖金
    //html += '<li>奖金总额</li>';
    var bonusList = rowData.bonusList;
    if (!isEmpty(bonusList)) {
        var length = bonusList.length;
        for (var i = 0; i < length; i++) {
            html += '<li>' + bonusList[i].detailName + '</li>';
        }
    }
    //html += '<li>社保公积金</li>';
    if ($('#isSocialSecurity').val() == '1') {
        html += '<li >社保</li>';
        html += '<li >公积金</li>';
    }
    html += '<li>税前补发/扣款</li>';
    html += '<li>绩效补充</li>';
    html += '<li>应发工资</li>';
    html += '<li>个税</li>';
    html += '<li>税后补发/扣款</li>';
    html += '<li>报销金额</li>';
    html += '<li>实发工资</li>';
    html += '</ul>';
    return html;
}

/**
 * 审核工资单
 */
function onApprovalPayroll() {
    if ($('#isStart').val() == '0') {
        showWarning('当前日期小于计薪周期开始日期，不能审批工资单');
        return;
    }
    var msg = '';
    if ($('#isEnd').val() == '1') {
        msg = $('#currentPayCycle').val() + '的计薪周期还没结束，审核通过后，将不能再计算本月工资，您确定要继续吗？';
    } else {
        msg = '您确定要审批该工资单吗？';
    }
    showConfirm(msg, function () {
        var index = layer.load();
        $.ajax({
            type: 'post',
            url: '../monthlypayroll/approvalPayroll.htm',
            data: {'payCycleId': $('#payCycleId').val()},
            success: function (data) {
                //关闭
                layer.close(index);
                if (data.success) {
                    var index1 = layer.alert('审批成功', {
                        title: '处理结果',
                    }, function (index) {
                        //刷新页面
                        layer.close(index1);
                        var html = $('#title').html() + '（已审核）';
                        $('#title').html(html);
                        $('#foot').hide();
                    });
                } else {
                    showWarning(data.message);
                }
            }
        });
    });
}

/**
 * 发工资
 */
function onPayOff() {
    //获取当前状态  工资单状态
    var state = $('isPayOff').attr('data-value');
    if (state == '0') {
        showWarning('该工资单还未审批，不能进行工资发放');
        return;
    }
    $.ajax({
        type: 'post',
        url: 'payOff.htm',
        data: {'payCycleId': $('#payCycleId').val()},
        success: function (data) {
            if (data.success) {
                var isPayRole = data.data;
                if (isPayRole == 1) {//有访问我的账户权限
                    onShowSalaryOrderDetail();
                } else if (isPayRole == 2) {//无访问我的账户权限
                    showConfirm('您好，代发工资申请已经创建，点击“确定”通知财务付款,点击"取消"取消订单', function () {
                        $.ajax({
                            type: 'post',
                            url: '../monthlypayroll/generateSalaryOrder.htm',
                            data: {'payCycleId': $('#payCycleId').val()},
                            success: function (data) {
                                if (data.success) {
                                    //parent.parent.$('#J_iframe').attr('src',"./account/index.htm");
                                    //$("#mainlful", window.parent.parent.document).find("li[data-url='/account/index.htm']").click();
                                    $("#mainlful", window.parent.parent.parent.document).find("li[data-url='/account/index.htm']").click();
                                    var index = parent.parent.layer.getFrameIndex(parent.window.name); //获取窗口索引
                                    parent.parent.layer.close(index);
                                } else {
                                    showWarning(data.message);
                                }
                            }
                        });
                    });
                }
            } else {
                if (data.message.indexOf("代发协议") >= 0) {
                    showWarning("您好，您还未签约代发工资，不能提交此订单。咨询代发工资服务，请联系客服：400-175-8090");
                } else {
                    showWarning(data.message);
                }
            }
        }
    });
}

/**
 * 取消
 */
function onCancelPayroll() {
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);
}

/**
 * 订单详情页面
 */
function onShowSalaryOrderDetail() {
    var url = "../monthlypayroll/salaryOrderDetail.htm?payCycleId=" + $("#payCycleId").val();
    var index = layer.open({
        type: 2,
        title: "订单详情",
        maxmin: true,
        //closeBtn: 0,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: url
    });
    //窗口最大化
    layer.full(index);
}
