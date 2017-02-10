/**
 * <p>数据字典工具类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/4 14:26
 */
var DictUtil = new dd();
function dd() {
}

/**
 * 根据关键字获取数据字典
 * @param key
 */
dd.prototype.getDictData = function (dictValue) {
    if (!isEmpty(dictValue)) {
        var dictData = null;
        $.ajax({
            type: 'post',
            async: false,//同步
            url: 'getDictData.htm?dictValue=' + dictValue,
            success: function (data) {
                if (data.success) {
                    dictData = data.data;
                }
            }
        });
        return dictData;
    } else {
        return null;
    }

};


/**
 * 根据字典关键字获取值
 * @param dictDataValue
 */
dd.prototype.getDictDataName = function (dictValue, dictDataValue) {
    if (!isEmpty(dictDataValue)) {
        var dictData = null;
        $.ajax({
            type: 'post',
            async: false,//同步
            url: 'getDictDataName.htm?dictValue=' + dictValue + '&dictDataValue=' + dictDataValue,
            success: function (data) {
                if (data.success) {
                    dictData = data.data;
                }
            }
        });
        return dictData;
    } else {
        return null;
    }
};
