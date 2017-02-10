var Grid;
$(function () {

    Grid = new DataGrid({
            columns: [
                {
                    field: 'rowNo',
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index + 1;
                    }
                },
                {field: 'newsId', title: '主键', visible: false},
                {field: 'newsTitle', title: '新闻标题'},
                {field: 'newsChick', title: '点击次数'},
                {field: 'newsKeywords', title: '关键词'},
                {field: 'newsType', title: '发布类型', formatter:function(value, row, index){
                    if(value == 1){
                        return '系统公告';
                    }
                    if(value == 2){
                        return '系统通知单';
                    }
                    if(value == 3){
                        return '媒体报道';
                    }
                    if(value == 4){
                        return '平台活动';
                    }
                    if(value == 5){
                        return 'HR知识库';
                    }
                    if(value == 6){
                        return '政策薪解';
                    }
                    if(value == 7){
                        return '动态推送';
                    }
                    return '';
                }},
                {field: 'newsIstop', title: '是否置顶', formatter:function (value) {
                    if(value == 0){
                        return '否';
                    }
                    if(value == 1){
                        return '是';
                    }
                    return '';
                }},
                {field: 'newsState', title: '发布状态', formatter:function (value) {
                    if(value == 0){
                        return '待发布';
                    }
                    if(value == 1){
                        return '已发布';
                    }
                    return '';
                }},
                {field: 'newsReleasetime', title: '发布时间', formatter:function(value,row,index){
                    if(value){
                        return unixToTime(value, 'y-m-d h:m');
                    }
                    return '';
                }},
                {
                    field: 'opt', title: '操作',
                    formatter: function (value, row, index) {
                        var state, text;
                        if(row.newsState == 0){
                            state = 1;
                            text = "发布新闻";
                        }
                        if(row.newsState == 1){
                            state = 0;
                            text = "取消发布";
                        }

                        return  '<a class="btn btn-info" href="edit/'+ row.newsId +'.htm"><i class="fa fa-paste"></i> 编辑</a>' +
                                ' <button class="btn btn-danger" type="button" onclick="republish('+ row.newsId +', '+ state +');">'+ text +'</button>';
                    }
                }
            ],
            requestParam: function () {
                return {
                    'newsTitle': $('#newsTitle').val(),
                    'newsState': $('#newsState').val(),
                    'newsType': $('#newsType').val()
                }
            }
        }
    );

});

function republish(newsId, state){
    if(newsId){
        var ii = layer.load();
        $.ajax({
            type: 'post',
            url: BASE_PATH + '/operate/news/republish.htm',
            data: {
                newsId:newsId,
                state: state
            },
            success: function (data) {
                layer.close(ii);
                if (data.success) {
                    if(state == 0){
                        alertInfo('取消发布成功！', function(){
                            window.location.href = BASE_PATH + '/operate/news/index.htm';
                        });
                    }
                    if(state == 1){
                        alertInfo('发布成功！', function(){
                            window.location.href = BASE_PATH + '/operate/news/index.htm';
                        });
                    }
                } else {
                    alertWarn(data.message);
                }
            }
        });
    }
}