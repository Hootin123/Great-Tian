

/**
 * 检查字典分类
 */
function checkDictType() {
    var dictName = $('#dictName').val();
    if (isEmpty(dictName)) {
        layer.tips('请输入分类名称', '#dictName', {time: 3000, tips: 1});
        return false;
    }
    var dictValue = $('#dictValue').val();
    if (isEmpty(dictValue)) {
        layer.tips('请输入关键字', '#dictValue', {time: 3000, tips: 1});
        return false;
    }
    var sort = $('#sort').val();
    if (isEmpty(sort)) {
        layer.tips('请输入排序', '#sort', {time: 3000, tips: 1});
        return false;
    }
    return true;
}