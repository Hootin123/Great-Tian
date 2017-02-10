var validator;
$(function () {

    $.validator.setDefaults({
        highlight: function (e) {
            $(e).closest(".form-group").removeClass("has-success").addClass("has-error")
        },
        success: function (e) {
            e.closest(".form-group").removeClass("has-error").addClass("has-success")
        },
        errorElement: "span",
        errorPlacement: function (e, r) {
            e.appendTo(r.is(":radio") || r.is(":checkbox") ? r.parent().parent().parent() : r.parent())
        },
        errorClass: "help-block m-b-none",
        validClass: "help-block m-b-none"
    });
    var e = "<i class='fa fa-times-circle'></i> ";

    if ($('#isChild').val() == '0') {
        //系统url不可编辑
        $('#url').attr('disabled', true).attr('placeholder', '默认为空');
        //添加校验
        validator = $('#addMenuForm').validate({
            rules: {
                menuName: "required",
                menuIcon: "required"
            },
            messages: {
                menuName: {
                    required: e + "请输入菜单名称",
                    element: "#menuName-error"
                },
                menuIcon: {
                    required: e + "请选择图标",
                    element: "#menuIcon-error"
                }
            },
            submitHandler: function (form) {
                //新增菜单
                onAddMenu();
            }
        });
    } else {
        $('#url').attr('disabled', false).attr('placeholder', '');
        //添加校验
        validator = $('#addMenuForm').validate({
            rules: {
                menuName: "required",
                menuIcon: "required",
                url: "required"
            },
            messages: {
                menuName: {
                    required: e + "请输入菜单名称",
                    element: "#menuName-error"
                },
                menuIcon: {
                    required: e + "请选择图标",
                    element: "#menuIcon-error"
                },
                url: {
                    required: e + "请输入系统url",
                    element: "#url-error"
                }
            },
            submitHandler: function (form) {
                //新增菜单
                onAddMenu();
            }
        });
    }
});

/**
 * 新增菜单
 */
function onAddMenu() {
    if ($('#addMenuForm').valid) {
        $.ajax({
            type: 'post',
            url: 'addMenu.htm',
            data: $('#addMenuForm').serialize(),
            success: function (data) {
                if (data.success) {
                    swal({title: "太帅了", text: "菜单新增成功", type: "success"});
                    //关闭窗口
                    $('#myAddMenuModal').modal('hide');
                    //重置表单校验信息
                    validator.resetForm();
                    //刷新表格数据
                    //parent.menuTable.bootstrapTable('refresh');
                    window.location.reload();
                } else {
                    swal({title: "很抱歉", text: "data.message", type: "warning"});
                }
            }
        });
    }
}

/***
 * 选中图标
 * @param icon
 */
function selectIcon(icon) {
    $('#menuIcon').val(icon);
    $('#myMenuIconModal').modal('hide');
}

/**
 * 重置表单校验信息
 */
function resetMenuValidateForm() {
    validator.resetForm();
    $(".form-group").removeClass("has-error");
}


