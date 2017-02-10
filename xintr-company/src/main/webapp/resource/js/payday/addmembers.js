$(function () {
    //初始化数据
    initData();
    // 绑定确定按钮事件
    $('.okBtn').on('click', function () {
        addMembers();
    });
    //绑定取消按钮事件
    $('.cancelBtn').on('click', function () {
        onCancel();
    });
});

/**
 * 初始化数据
 */
function initData(){
    $('#customerDepId').val(parent.getSelectDeptId());
    $('#customerCompanyId').val(parent.getCorpId());
    $('#customerCompanyName').val(parent.getCorpName());
    $('#customerDepName').val(parent.getSelectDeptName());
}

/**
 * 取消关闭窗口
 */
function onCancel() {
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);
}

/**
 * 新增企业员工
 */
function addMembers() {
    if (checkSubmit()) {
        $.ajax({
            type: 'post',
            url: 'addCustomers.htm',
            data: $('#addForm').serialize(),
            success: function (data) {
                if (data.success) {
                    //重新加载组织树
                    parent.onLoadCompanyTree(0);
                    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                    parent.layer.close(index);
                } else {
                    showWarning(data.message);
                }
            }
        });
    }
}

/**
 * 提交前数据验证
 */
function checkSubmit() {
    var data = $('#addForm').serializeArray();
    var flag = false;
    $.each(data, function (i, field) {
        var value = field.value;
        var name = field.name;
        if(name == "customerId") {
            flag = true;
        }else
        if (isEmpty(value)) {
            //入职时间非必填
            if (name == 'customerJoinTime') {
                return;
            }
            else if (name == 'customerSex') {
                layer.tips('请从这些选项中选择一个', '#' + name, {time: 3000});
                flag = false;
                return false;
            } else {
                layer.tips('请填写此字段', '#' + name, {time: 3000});
                flag = false;
                return false;
            }

        }
        //手机
        else if (name == 'customerPhone') {
            if (!isMobile(value)) {
                layer.tips('请输入正确的手机号码', '#' + name, {time: 3000});
                flag = false;
                return false;
            }
        }
        //工资卡号
        else if (name == 'customerBanknumber') {
            if (!isBankNumber(value)) {
                layer.tips('请输入正确的银行卡号', '#' + name, {time: 3000});
                flag = false;
                return false;
            }
        }
        //身份证号码
        else if (name == 'customerIdcard') {
            if (!isIdCardNo(value)) {
                layer.tips('请输入正确的身份证号码', '#' + name, {time: 3000});
                flag = false;
                return false;
            }
        }
        flag = true;
    });
    return flag;
}


