/**
 * <p>表格初始化插件</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/10 14:26
 */
function DataGrid(config) {

    var dataGrid = this;

    //是否自动查询
    this.isAutoQuery = false;

    this.config = config || {};

    //Grid DataList
    this.Grid = $(config.gridId || '#data-list');

    //初始化行选中的背景色
    this.Grid.on('click-row.bs.table', function (e, row, $element) {
        $('.success').removeClass('success');
        $($element).addClass('success');
    }).on('check.bs.table', function (e, row, $element) {
        $('.success').removeClass('success');
        $($element).parent().parent().addClass('success');
    });

    //ToolBar
    this.ToolBar = $('#toolbar');

    //Form
    this.Form = {
        search: $("#searchForm"),
        list: $("#listForm"),
        edit: $("#editForm")
    };

    //Grid 工具类
    this.Utils = {
        checkSelect: function (tableData) {//检查grid是否有勾选的行, 有返回 true,没有返回false
            if (tableData != null && tableData.checkbox) {
                return true;
            }
            layer.alert('未选中行', {icon: 0, title: "提示"});
            return false;
        },
        getSelectRowData: function () {//返回选中行的数据  只返回最后一条数据
            var index = dataGrid.Grid.find('tr.success').data('index');
            return (dataGrid.Grid.bootstrapTable('getData')[index]);
        },
        getSelectionsIds: function () {//返回选中的所有数据的主键
            var selects = dataGrid.Grid.bootstrapTable('getSelections');
            var ids = [];
            if (!isEmpty(selects)) {
                var length = selects.length;
                for (var i = 0; i < length; i++) {
                    ids.push(selects[i].id);
                }
            }
            return ids;
        }
    };

    //初始化Grid参数
    dataGrid.Grid.bootstrapTable({
        method: 'post',
        url: config.url || 'dataList.htm',
        striped: true,
        dataType: 'json',
        pagination: true,
        queryParamsType: 'limit',
        singleSelect: config.singleSelect || false,//是否单选
        contentType: 'application/x-www-form-urlencoded',
        pageSize: config.pageSize || 10,
        pageNumber: config.pageNumber || 1,
        search: config.search || false, //不显示 搜索框
        showColumns: config.showColumns || false, //不显示下拉框（选择显示的列）
        showRefresh: config.showRefresh || false, //是否显示刷新按钮
        sidePagination: 'server', //服务端请求
        responseHandler: dataGrid.responseHandler,//查询结果返回响应处理
        toolbar: config.toolbar || dataGrid.ToolBar,
        columns: config.columns,
        clickToSelect: true,
        formatSearch: function () {
            return config.searchTip || "查询"
        },
        queryParams: function (params) {
            var param = {
                    pageIndex: params.offset / params.limit + 1,
                    pageSize: params.limit,
                    search: params.search,
                    sort: params.sort
                };
            dataGrid.isAutoQuery = true;
            return $.extend({}, param, dataGrid.config.requestParam ? dataGrid.config.requestParam() : '');
        }
    });


    //绑定新增按钮事件
    $("#addBtn").on('click', function () {
        dataGrid.onAdd();
    });
    //绑定修改按钮事件
    $("#editBtn").on('click', function () {
        dataGrid.onEdit();
    });
    //绑定删除按钮事件
    $(config.deleteBtn || "#deleteBtn").on('click', function () {
        dataGrid.onDelete();
    });
    $("#queryBtn").on('click', function () {
        dataGrid.onQuery();
    });
}

/**
 * 新增
 */
DataGrid.prototype.onAdd = function (url) {
    var dataGrid = this;
    var cfg = this.config;
    var index = layer.open({
        maxmin: cfg.maxmin || false, //开启最大化最小化按钮
        closeBtn: 1, //不显示关闭按钮
        area: [cfg.editWidth || '400px', cfg.editHeith || '480px'],
        title: "新增",
        type: 2,
        content: url || 'add.htm', //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
        btn: ['保存', '取消'],
        yes: function (index, layero) { //或者使用btn1
            //保存
            dataGrid.onSave(index);
        },
        cancel: function (index) {
            layer.close(index);
        }
    });

    //是否默认最大化
    if (cfg.isfull)
        layer.full(index);
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
        this.onAdd("add.htm?id=" + id);
    }
};

/**
 * 保存
 */
DataGrid.prototype.onSave = function (index) {

    if (this.Form.edit.valid && this.onCheck()) {
        var grid = this.Grid;
        var dataGrid = this;
        $.ajax({
            type: 'post',
            url: 'save.htm',
            data: isEmpty(dataGrid.saveParam()) ? layer.getChildFrame('form', index).serialize() : dataGrid.saveParam(),
            success: function (data) {
                if (data.success) {
                    //关闭弹框
                    layer.close(index);
                    layer.alert("保存成功", {icon: 1, title: "提示"}, function (index1) {
                        //刷新界面
                        layer.close(index1);
                        grid.bootstrapTable('refresh');
                    });
                } else {
                    layer.alert(data.message, {icon: 0, title: "很抱歉"});
                }
            }
        });
    }
};

/**
 * 删除
 */
DataGrid.prototype.onDelete = function () {
    //是否选中行
    var selections = this.Grid.bootstrapTable('getSelections');
    var data = this.config.singleSelect ? this.Utils.getSelectRowData() : (isEmpty(selections) ? null : selections[0]);
    if (this.Utils.checkSelect(data)) {
        var cfg = this.config;
        var grid = this.Grid;
        var util = this.Utils;
        layer.confirm('是否确认删除该数据?', {icon: 3, title: '提示'}, function (index) {
            var pkFiled = cfg.pkFiled || 'id';
            var id = eval('data.' + pkFiled);
            var url = cfg.singleSelect ? 'delete.htm?id=' + id : 'delete.htm';
            var data1 = cfg.singleSelect ? '' : $.param({id: util.getSelectionsIds()}, true);
            $.ajax({
                type: 'post',
                url: url,
                data: data1,
                success: function (data) {
                    if (data.success) {
                        //关闭弹框
                        layer.close(index);
                        layer.alert("删除成功", {icon: 1, title: "提示"}, function (index1) {
                            //刷新界面
                            layer.close(index1);
                            grid.bootstrapTable('refresh');
                        });
                    } else {
                        //swal({title: "很抱歉", text: "data.message", type: "warning"});
                        layer.alert(data.message, {icon: 0, title: "很抱歉"});
                    }
                }
            });
        });
    }
};

/**
 * 查询
 */
DataGrid.prototype.onQuery = function () {

    var param = {
        'pageIndex': 0,
        'pageSize': this.Grid.bootstrapTable('getOptions').pageSize,
    }
    // if (this.isAutoQuery) {
    //     param = {
    //         'pageIndex': 0,
    //         'pageSize': this.Grid.bootstrapTable('getOptions').pageSize,
    //     }
    // } else {
    //     param = {
    //         'pageIndex': this.Grid.bootstrapTable('getOptions').pageNumber,
    //         'pageSize': this.Grid.bootstrapTable('getOptions').pageSize,
    //     }
    // }
    this.Grid.bootstrapTable('getOptions').pageNumber = 1;
    var requestParam = this.config.requestParam ? this.config.requestParam() : '';
    param = $.extend({}, param, requestParam);
    this.isAutoQuery = false;
    this.Grid.bootstrapTable('refresh', {query: param});

};

/**
 * 校验
 */
DataGrid.prototype.onCheck = function () {
    return true;
};

/**
 * 保存参数
 * @returns {string}
 */
DataGrid.prototype.saveParam = function () {
    return '';
};


/**
 * Grid 查询响应结果
 * @param res
 * @returns {*}
 */
DataGrid.prototype.responseHandler = function (response) {
    if (response.success) {
        return {
            //"data":response.data,
            "rows": response.data,
            "total": response.paginator.totalCount
        };

    } else {
        return {
            "rows": [],
            "total": 0
        };
    }
};