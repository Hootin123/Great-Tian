var paginator;
var payrollList;
$(function () {

    //重新上传
    $('.preNav41').on('click', function () {
        window.location = 'rapidlyPayrollExcel.htm';
    })
    //发工资
    $('.preNav42').on('click', function () {
        payOff();
    })
})

/**
 * 加载界面数据
 * @param listData
 */
function loadData(resultResponse) {
    if (!isEmpty(resultResponse) && !isEmpty(resultResponse.data)) {
        paginator = resultResponse.paginator;
        payrollList = resultResponse.data.payrollList;
        //初始化分页
        generPageHtml();
        //加载数据
        showData(1);
        //excel名称
        $('#excelTitle').val(resultResponse.data.excelTitle);
        //excel路径
        $('#excelPath').val(resultResponse.data.excelPath);
        //人数
        $('.preNav22').html(resultResponse.data.payrollNumber);
        //发薪总额
        $('.preNav32').html(resultResponse.data.totalWages);
    }
}

/**
 * 初始化分页
 * @param pageInfo
 */
function generPageHtml() {
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
                showData(obj.curr)
            }
        }
    });
}

/**
 * 数据加载
 * @param listData
 */
function showData(page) {
    if (!isEmpty(payrollList)) {
        var length = payrollList.length;
        var limit = paginator.limit;
        var html = getTableThead() + '<tbody>';
        page = page == 0 ? 1 : page;
        for (var i = 0; i < limit; i++) {
            var row = (page - 1) * limit + i;
            if (row >= length)break;
            var payroll = payrollList[row];
            html += '<tr>' +
                '<td>' + (i + 1) + '</td>' +
                '<td>' + payroll.customerName + '</td>' +
                '<td>' + payroll.idCard + '</td>' +
                '<td>' + payroll.bankAccount + '</td>' +
                '<td>' + payroll.bankNumber + '</td>' +
                '<td>' + formatMoney(payroll.realWage, 0) + '</td>' +
                '</tr>';
        }
        html += '</tbody>';
        $('#tablePayroll').html(html);
    }
}

/**
 * 获取表格标题
 */
function getTableThead() {
    return '<thead> ' +
        '<tr>' +
        '<td>序号</td>' +
        '<td>姓名</td>' +
        '<td>身份证</td> ' +
        '<td>开户行</td>' +
        '<td>工资卡号</td>' +
        '<td>实发金额</td>' +
        '</tr>' +
        '</thead>';
}

/**
 * 发工资
 */
function payOff() {
    layer.alert('工资发放后将不可修改，您确定根据此工资表发工资？', {
        icon: 0,
        title: '发工资',
    }, function (index) {
        layer.close(index);
        var param = {'excelPath': $('#excelPath').val(),'excelTitle':$('#excelTitle').val()}
        ajaxJson('payOff.htm', param, function (data) {
            //根据返回值类型进行页面跳转
            if (data.data == '1') {//有访问首页的权限
                window.location = '../account/index.htm';
            } else {
                //跳转到工资单汇总界面
            }
        })
    });
}