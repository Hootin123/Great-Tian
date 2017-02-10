$(function () {

    /**
     * 初始化页面
     */
    initPage();

    //新增成员
    $('#addMembers').on('click', function () {
        onAddMemebers();
    });

    //编辑成员
    $('#editMember').on('click', function () {
        onEditMemeber();
    });

    //移动到其他部门
    $('#moveDept').on('click', function () {
        onMoveDept();
    });

    //冻结企业员工账户
    $('#frozenCustomers').on('click', function () {
        frozenCustomers();
    });

    //启用企业员工账户
    $('#enableCustomers').on('click', function () {
        enableCustomers();
    });

    //新建子部门
    $('#addDept').on('click', function () {
        addDept();
    });

    //修改部门名称
    $('#updateDeptName').on('click', function () {
        updateDeptName();
    });

    //删除部门
    $('#deleteDept').on('click', function () {
        deleteDept();
    });

    /*全选按钮*/

    $('#checkbox1').click(function () {
        if ($(this).get(0).checked) {
            $('.paftwlst input').prop('checked', 'checked');
        } else {
            $('.paftwlst input').prop('checked', '');
        }
    });

    $('.paftwlst input').click(function () {
        if ($('.paftwlst input').eq(0).get(0).checked && $('.paftwlst input').eq(1).get(0).checked) {

        } else {
            $('#checkbox1').prop('checked', '');
        }
    })

});


/**
 * 点击公司树
 */
function onClickCompanyTree(_this) {
    $('.member dd').removeClass('dd_active');
    _this.addClass('dd_active');

    //更新部门详情信息
    updateDeptInfo();

    //根据选中的树节点更新组织成员
    onLoadCustomer(0);
}

/**
 * 更新部门详情信息
 */
function updateDeptInfo() {
    var customerCount = $('.dd_active').attr('data-count');
    $('#selectDeptName').html(getSelectDeptName() + '（<em>' + customerCount + '</em>人）<span class="payoff_two_p1">编号：<i>' + getSelectDeptId() + '</i></span>');
}

/**
 * 新增成员弹出框
 */
function onAddMemebers() {
    var index = layer.open({
        type: 2,
        title: '新增成员',
        maxmin: true,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: 'addMembers.htm'
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 新增成员弹出框
 */
function onEditMemeber() {
    if (getSelect().length != 1) {
        layer.alert('请先选择一条记录');
        return false;
    }
    var index = layer.open({
        type: 2,
        title: '编辑成员',
        maxmin: true,
        shadeClose: false, //点击遮罩关闭层
        area: ['650px', '380px'],
        content: 'editMembers.htm?id='+getSelect()[0]
    });
    //窗口最大化
    layer.full(index);
}

/**
 * 移动到其他部门
 */
function onMoveDept() {
    if (getSelect().length > 0) {
        var index = layer.open({
            type: 2,
            title: '设置成员所在部门',
            maxmin: false,
            shadeClose: false, //点击遮罩关闭层
            area: ['780px', '650px'],
            content: 'moveDeptPage.htm'
        });
    } else {
        layer.alert('请先勾选数据');
    }
}

/**
 * 冻结企业员工账户
 */
function frozenCustomers() {
    var arrayObj = getSelect();
    if (arrayObj.length > 0) {
        var index = layer.confirm('您确定要冻结勾选的账户吗？', {
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.ajax({
                type: 'post',
                url: 'frozenCustomers.htm',
                data: $.param({customerIds: arrayObj}, true),
                success: function (data) {
                    if (data.success) {
                        //刷新窗口
                        //window.location.reload();
                        onLoadCustomer(0);
                        layer.close(index);
                    } else {
                        layer.alert(data.message);
                    }
                }
            });
        }, function () {
            layer.close(index);
        });
    } else {
        layer.alert('请先勾选数据');
    }
}

/**
 * 启动员工账户
 */
function enableCustomers() {
    var arrayObj = getSelect();
    if (arrayObj.length > 0) {
        $.ajax({
            type: 'post',
            url: 'enableCustomers.htm',
            data: $.param({customerIds: arrayObj}, true),
            success: function (data) {
                if (data.success) {
                    //刷新窗口
                    //window.location.reload();
                    onLoadCustomer(0);
                    layer.close(index);
                } else {
                    layer.alert(data.message);
                }
            }
        });
    } else {
        layer.alert('请先勾选数据');
    }
}

/**
 * 获取选中的行
 */
function getSelect() {
    var arrayObj = [];
    $('.paftwlst input').each(function () {
        if ($(this).prop("checked")) {
            arrayObj.push(this.value);
        }
    });
    return arrayObj;
}

/**
 * 获取选中的部门
 * @returns {*|jQuery}
 */
function getSelectDeptId() {
    return $('.dd_active').attr('data-deptId');
}

/**
 * 获取选中的部门名称
 * @returns {*|jQuery}
 */
function getSelectDeptName() {
    return $('.dd_active').attr('data-deptName');
}

/**
 * 获取公司主键
 * @returns {*|jQuery}
 */
function getCorpId() {
    return $('#companyId').attr('data-companyId')
}

/**
 * 获取公司名称
 * @returns {*|jQuery}
 */
function getCorpName() {
    return $('#companyId').attr('data-companyName')
}

/**
 * 新增部门
 */
function addDept() {
    var index = layer.open({
        type: 2,
        title: '新增部门',
        maxmin: false,
        shadeClose: false, //点击遮罩关闭层
        area: ['450px', '300px'],
        content: 'addDeptPage.htm'
    });
}

/**
 * 删除部门
 */
function deleteDept() {
    var deptId = getSelectDeptId();
    if (!isEmpty(deptId)) {
        var index = layer.confirm('您确定要删除选中的部门吗？', {
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.ajax({
                type: 'post',
                url: 'deleteDept.htm',
                data: {'deptId': deptId},
                success: function (data) {
                    if (data.success) {
                        $('.member dd').removeClass('dd_active');
                        //关闭窗口
                        layer.close(index);
                        //重新加载组织树
                        onLoadCompanyTree();

                    } else {
                        layer.alert(data.message);
                    }
                }
            });
        }, function () {
            layer.close(index);
        });
    } else {
        layer.alert('请选中部门名称');
    }
}

/**
 * 修改部门名称
 */
function updateDeptName() {
    var deptId = $('.dd_active').attr('data-deptId');
    if (!isEmpty(deptId)) {
        layer.prompt(
            {title: '请输入新的部门名称'},
            function (value, index) {
                $.ajax({
                    type: 'post',
                    url: 'updateDeptName.htm',
                    data: {'deptId': deptId, 'deptName': value},
                    success: function (resultResponse) {
                        if (resultResponse.success) {
                            layer.close(index);
                            //重新加载组织树
                            onLoadCompanyTree();
                        } else {
                            layer.alert(resultResponse.message, {icon: 1})
                        }
                    }
                });
            });
    } else {
        layer.alert('请选中部门名称');
    }
}


/**
 * 重新加载组织树
 */
function onLoadCompanyTree() {
    //$('#companyTree').clear;
    var deptId = $('.dd_active').attr('data-deptId');
    $.ajax({
            type: 'post',
            url: 'getCompanyTree.htm',
            success: function (resultResponse) {
                if (resultResponse.success) {
                    var list = resultResponse.data;
                    var html = '<dt><span id="companyId" data-companyId="' + list[0].companyId + '" data-companyName="' + list[0].companyName + '"></span>' + list[0].companyName + '</dt>';
                    var lastStyle = '';
                    var length = list.length;
                    for (var i = 0; i < length; i++) {
                        var companyTree = list[i];
                        if (companyTree.depId != null) {
                            if (i == length - 1) {
                                lastStyle = 'style="position:relative; top: 9px; margin-top: 23px;"';
                            }
                            if ((i == 0 && isEmpty(deptId)) || (companyTree.depId == deptId)) {
                                html += '<dd class="dd_active" ' + lastStyle + ' data-deptName="' + companyTree.depName + '" ' +
                                    'data-deptId="' + companyTree.depId + '" data-count="' + companyTree.customerCount + '" ' +
                                    'onclick="onClickCompanyTree($(this))"> <em></em><span></span>' + companyTree.depName + '(' + companyTree.customerCount + ')</dd>';
                            } else {
                                html += '<dd  ' + lastStyle + 'data-deptName="' + companyTree.depName + '" data-deptId="' + companyTree.depId + '" ' +
                                    'data-count="' + companyTree.customerCount + '"  ' +
                                    'onclick="onClickCompanyTree($(this))"> <em></em><span></span>' + companyTree.depName + '(' + companyTree.customerCount + ')</dd>';
                            }
                        }
                    }
                    html += '</dt>';
                    $('#companyTree').html(html);

                    //重新加载组织成员
                    onLoadCustomer(0);

                    //更新部门信息
                    updateDeptInfo();
                } else {
                    layer.alert(resultResponse.message, {icon: 1})
                }
            }
        }
    )
    ;
}

/**
 * 加载组织成员信息
 */
function onLoadCustomer(pageIndex) {
    var deptId = $('.dd_active').attr('data-deptId');
    $.ajax({
            type: 'post',
            url: 'getCustomerByDeptId.htm',
            data: {'deptId': deptId, 'pageIndex': pageIndex},
            success: function (resultResponse) {
                if (resultResponse.success) {
                    var list = resultResponse.data;
                    var html = '<table class="paftwlst">';
                    var length = list.length;
                    for (var i = 0; i < length; i++) {
                        var customer = list[i];
                        html += '<tr>';
                        html += '<td><input type="checkbox" id="checkbox1" value="' + customer.customerId + '"></td>';
                        html += '<td><img src="resource/img/man.png"/>' + customer.customerTurename + '</td>';
                        html += '<td>' + customer.customerCompanyName + ' / ' + customer.customerDepName + '</td>';
                        html += '<td>' + customer.customerPlace + '</td>';
                        html += '<td>联系电话：' + customer.customerPhone + '</td>';
                        if (customer.customerSign == 1) {
                            html += '<td>已启用</td>';
                        } else {
                            html += '<td>未启用</td>';
                        }
                    }
                    html += '</table>';
                    $('#customerTable').html(html);

                    //if (length > 0) {
                    //生成分页控件
                    createPageHtml(resultResponse);
                    //}
                } else {
                    layer.alert(resultResponse.message, {icon: 1})
                }
            },
            error: function (a, b, c) {
                console.info(b);
            }
        }
    )
    ;
}

/**
 * 生成分页控件
 */
function createPageHtml(resultResponse) {
    //生成分页控件
    kkpager.generPageHtml({
        pno: resultResponse.paginator.page,
        mode: 'click', //可选，默认就是link
        //总页码
        total: '' + resultResponse.paginator.totalPages,
        //总数据条数
        totalRecords: '' + resultResponse.paginator.totalCount,
        //链接前部
        hrefFormer: '' + resultResponse.hrefFormer,
        //链接尾部
        hrefLatter: '' + resultResponse.hrefLatter,
        //链接算法
        isShowFirstPageBtn:false,
        isShowLastPageBtn:false,
        currPageBeforeText:'第',
//        getLink: function (n) {
//            return 'orgmember.htm?pageIndex=' + n;
//        }
        click: function (n) {
            //这里可以做自已的处理
            //...
            //处理完后可以手动条用selectPage进行页码选中切换
            onLoadCustomer(n);
        },
        //getHref是在click模式下链接算法，一般不需要配置，默认代码如下
        getHref: function (n) {
            return '#';
        }
    }, true);
}


function initPage() {
    $('.paoff_twhd_lf a').eq(1).click(function () {
        $('.pacity').css('display', 'block');
        $('.pacity').css('left', 0);
    });

    $('.addbox').find(':button').click(function () {
        $('.pacity').css('display', 'none');
    });

    /*member滑动效果*/
    $('.member').animate({'left': 0});
    /*帮助弹窗*/
    $('.payoff_two_logo').click(function () {
        window.parent.document.getElementById('indexshade').style.display = 'block';
        window.parent.document.getElementById('indexbox').style.display = 'block';
    })
}

window.onload = function () {

    window.onresize = function () {
        resize();
    };
    resize();
};

function resize() {
    //var navWidth = $('.main_right').width() - 250;
    //$('.pwrit').width(navWidth); //选项卡宽度;
    $('.pacity').width($(window).width());
    $('.pacity').height($(window).height());
    $('.main_right').height($(window).height() - 2);
    $('.member').height($(window).height() - 2);

}
