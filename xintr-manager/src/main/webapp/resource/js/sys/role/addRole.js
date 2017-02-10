$(function () {

    //重写校验方法
    parent.DataGrid.prototype.onCheck = function () {
        var roleName = $('#roleName').val();
        //角色名称
        if (isEmpty(roleName)) {
            layer.tips('请输入角色名称', '#roleName', {time: 3000, tips: 1});
            return false;
        }
        return true;
    };

    //重写保存参数
    parent.DataGrid.prototype.saveParam = function () {
        return $.param(
            {
                id: $('#id').val(),
                roleName: $('#roleName').val(),
                descr: $('#descr').val(),
                state: $('#state').val(),
                menuIds: getMenuOrBtnIds([0, 1]),
                btnIds: getMenuOrBtnIds([2]),
            }, true);

    }

    //初始化树
    initTree();

    //展开按钮事件绑定
    $('#expandedBtn').on('click', function () {
        onExpanded();
    })

    //收起按钮事件绑定
    $('#unExpandedBtn').on('click', function () {
        onUnExpanded()
    })

    //全选按钮事件绑定
    $('#checkedAllBtn').on('click', function () {
        onCheckAll();
    })

    //全消按钮事件绑定
    $('#unCheckedAllBtn').on('click', function () {
        onUnCheckAll();
    })
});

/**
 * 获取菜单获取按钮Id
 * @param type
 */
function getMenuOrBtnIds(type) {
    //获取所有选中的节点
    var checkeds = $('#treeview').treeview('getChecked');
    var menuIds = [];
    if (!isEmpty(checkeds)) {
        var length = checkeds.length;
        var typeLen = type.length;
        for (var i = 0; i < length; i++) {
            for (var j = 0; j < typeLen; j++) {
                if (checkeds[i].menuType == type[j]) {
                    menuIds.push(checkeds[i].id);
                }
            }
        }
    }
    return menuIds;
}

/**
 * 获取菜单树数据
 */
function initTree() {
    var roleId = isEmpty($('#id').val()) ? '' : $('#id').val();
    $.ajax({
        type: 'post',
        url: 'getMenuTree.htm?roleId=' + roleId,
        success: function (data) {
            $('#treeview').treeview({
                data: data,
                showCheckbox: true,
                onNodeChecked: function (event, node) {
                    //选中
                    onSelectChecked(node);
                },
                onNodeUnchecked: function (event, node) {
                    //取消选中
                    onUnSelectChecked(node);
                }
            });
        }
    });
}

/**
 * 选中节点
 */
function onSelectChecked(node) {
    //判断是否存在子节点
    var nodes = node.nodes;
    if (!isEmpty(nodes)) {
        var length = nodes.length;
        for (var i = 0; i < length; i++) {
            $('#treeview').treeview('checkNode', [nodes[i].nodeId, {silent: true}]);
        }

        if (!isEmpty(node.parentId)) {
            //获取所有的同级节点
            var sibNodes = $('#treeview').treeview('getSiblings', node);
            if (!isEmpty(sibNodes)) {
                var flag = true;
                for (var i = 0; i < length; i++) {
                    if (!sibNodes[i].state.checked) {
                        flag = false;
                        break;
                    }
                }

                if (flag) {
                    //自动勾选父级
                    var parentNode = $('#treeview').treeview('getParent', node);
                    $('#treeview').treeview('checkNode', [parentNode.nodeId, {silent: true}]);
                }
            }
        }


    }
}

/**
 * 取消选中节点
 * @param node
 */
function onUnSelectChecked(node) {
    //判断是否存在子节点
    var nodes = node.nodes;
    if (!isEmpty(nodes)) {
        var length = nodes.length;
        for (var i = 0; i < length; i++) {
            $('#treeview').treeview('uncheckNode', [nodes[i].nodeId, {silent: true}]);
        }
    }
}

/**
 * 展开
 */
function onExpanded(){
    $('#treeview').treeview('expandAll', { silent: true });
}

/**
 * 收起
 */
function onUnExpanded(){
    $('#treeview').treeview('collapseAll', { silent: true });
}

/**
 * 全选
 */
function onCheckAll(){
    $('#treeview').treeview('checkAll', { silent: true });
}

/**
 * 全消
 */
function onUnCheckAll(){
    $('#treeview').treeview('uncheckAll', { silent: true });
}



