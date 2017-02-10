var Grid;
var dictValue;
var dictId;
$(function () {
    //初始化树
    initTree();

    //初始化列表
    initDataList();

    //初始化右键菜单
    initRightMenu();

    /**
     * 新增
     */
    /**
     * 新增
     */
    DataGrid.prototype.onAdd = function (url) {
        if (isEmpty(dictValue)) {
            showWarning("请选择字典分类");
            return;
        }
        var dataGrid = this;
        var cfg = this.config;
        var index = layer.open({
            maxmin: cfg.maxmin || false, //开启最大化最小化按钮
            closeBtn: 1, //不显示关闭按钮
            area: [cfg.editWidth || '400px', cfg.editHeith || '480px'],
            title: "新增",
            type: 2,
            content: url || 'add.htm?dictValue=' + dictValue, //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
            btn: ['保存', '取消'],
            yes: function (index, layero) { //或者使用btn1
                //保存
                dataGrid.onSave(index);
            },
            cancel: function (index) {
                layer.close(index);
            }
        });

    };

    /**
     * 修改
     */
    DataGrid.prototype.onEdit = function () {
        //是否选中行
        var data = this.Utils.getSelectRowData();
        if (this.Utils.checkSelect(data)) {
            var pkFiled = this.config.pkFiled || 'id';
            var id = eval('data.' + pkFiled);
            this.onAdd("add.htm?id=" + id + '&dictValue=' + dictValue);
        }
    };

    //重写校验方法
    DataGrid.prototype.onCheck = function () {
        return true;
    };
});

/**
 * 初始化树
 */
function initTree() {
    $.ajax({
        type: 'post',
        url: 'getDictTree.htm',
        success: function (data) {
            $('#treeview').treeview({
                data: data,
                //showCheckbox: true,
                onNodeSelected: function (event, node) {
                    //选中
                    dictValue = node.value;
                    dictId = node.id;
                    Grid.onQuery();
                },
            });
        }
    });
}

/**
 * 初始化列表
 */
function initDataList() {
//初始化表格
    Grid = new DataGrid({
        pkFiled: 'id',
        singleSelect: true,
        editWidth: '400px',//弹窗宽度
        editHeith: '480px',//弹窗高度
        columns: [
            {field: 'checkbox', checkbox: true},
            {
                field: 'rowNo',
                title: '序号',
                formatter: function (value, row, index) {
                    return index + 1;
                }
            },
            {field: 'id', title: '主键', visible: false},
            {field: 'dictdataValue', title: '关键字'},
            {field: 'dictdataName', title: '值'},
            {field: 'isfixed', title: '是否固定'},
            {field: 'sort', title: '排序'}
        ],
        requestParam: function () {
            return {
                'dictValue': dictValue
            }
        }
    });
}

/**
 * 初始化右键菜单
 */
function initRightMenu() {
    var menu = new BootstrapMenu('.list-group li',
        {
            fetchElementData: function ($treeview) {     //fetchElementData获取元数据
                var nodeid = $treeview.data('nodeid');
                $('#treeview').treeview('selectNode', [nodeid, {silent: true}]);
                var dict = $('#treeview').treeview('getSelected');
                dictValue = dict[0].value;
                dictId = dict[0].id;
                //设置菜单样式
                setMenuStyle();
                return dictValue;
            },
            actions: [{
                name: '新增',
                width: '10',
                iconClass: 'fa-plus',
                onClick: function (dictValue) {
                    //新增字典分类
                    addDictType();
                }
            }, {
                name: '编辑',
                width: '10',
                class: 'ceshi',
                iconClass: 'fa-edit',
                onClick: function (dictValue) {
                    if (dictValue != 'totalType') {
                        //编辑字典分类
                        editDictType();
                    }
                }
            }, {
                name: '删除',
                width: '10',
                iconClass: 'fa-trash',
                onClick: function (dictValue) {
                    if (dictValue != 'totalType') {
                        //删除字典分类
                        deleteDictType();
                    }
                }
            }]
        });
}

/**
 * 新增字典分类
 */
function addDictType(url) {
    var index = layer.open({
        maxmin: false, //开启最大化最小化按钮
        closeBtn: 1, //不显示关闭按钮
        area: ['400px', '480px'],
        title: "新增",
        type: 2,
        content: url || 'addDictType.htm?id=&parentId=' + dictId, //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
        btn: ['保存', '取消'],
        yes: function (index, layero) { //或者使用btn1
            //保存
            onSaveDictType(index);
        },
        cancel: function (index) {
            layer.close(index);
        }
    });
}

/**
 * 编辑字典分类
 */
function editDictType() {
    addDictType('addDictType.htm?id=' + dictId + '&parentId=');
}


/**
 * 保存字典分类
 */
function onSaveDictType(index) {
    if (window['layui-layer-iframe' + index].checkDictType()) {
        $.ajax({
            type: 'post',
            url: 'addDict.htm',
            data: layer.getChildFrame('form', index).serialize(),
            success: function (data) {
                if (data.success) {
                    //关闭弹框
                    layer.close(index);
                    layer.alert("新增成功", {icon: 1, title: "提示"}, function (index1) {
                        //刷新界面
                        layer.close(index1);
                        //刷新字典分类
                        initTree();
                        Grid.onQuery();
                    });
                } else {
                    layer.alert(data.message, {icon: 0, title: "很抱歉"});
                }
            }
        });
    }
}

/**
 * 删除字典分类
 */
function deleteDictType() {
    layer.confirm('是否确认删除该分类?', {icon: 3, title: '提示'}, function (index) {
        $.ajax({
            type: 'post',
            url: 'deleteDict.htm?id=' + dictId,
            success: function (data) {
                if (data.success) {
                    //关闭弹框
                    layer.close(index);
                    layer.alert("删除成功", {icon: 1, title: "提示"}, function (index1) {
                        //刷新界面
                        layer.close(index1);
                        //刷新字典分类
                        initTree();
                        Grid.onQuery();
                    });
                } else {
                    layer.alert(data.message, {icon: 0, title: "很抱歉"});
                }
            }
        });
    });
}

/**
 * 设置菜单样式
 */
function setMenuStyle() {
    if (dictValue == 'totalType') {
        $('.dropdown-menu>li>a').eq(2).css({
            'background': '#ccc',
            'color': '#fff',
            'cursor': 'default'
        });
        $('.dropdown-menu>li>a').eq(3).css({
            'background': '#ccc',
            'color': '#fff',
            'cursor': 'default'
        })
    } else {
        $('.dropdown-menu>li>a').eq(2).css({
            'background': '#fff',
            'color': 'inherit',
            'cursor': 'pointer'
        });
        $('.dropdown-menu>li>a').eq(3).css({
            'background': '#fff',
            'color': 'inherit',
            'cursor': 'pointer'
        })
    }
}
