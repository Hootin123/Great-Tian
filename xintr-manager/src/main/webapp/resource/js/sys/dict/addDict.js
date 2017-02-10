$(function () {


//重写校验方法
    parent.DataGrid.prototype.onCheck = function () {
        var dictdataValue = $('#dictdataValue').val();
        //关键字
        if (isEmpty(dictdataValue)) {
            layer.tips('请输入关键字', '#dictdataValue', {time: 3000, tips: 1});
            return false;
        }
        var dictdataName = $('#dictdataName').val();
        //关键字
        if (isEmpty(dictdataName)) {
            layer.tips('请输入值', '#dictdataName', {time: 3000, tips: 1});
            return false;
        }
        var sort = $('#sort').val();
        //关键字
        if (isEmpty(sort)) {
            layer.tips('请输入排序', '#sort', {time: 3000, tips: 1});
            return false;
        }
        return true;
    };


});