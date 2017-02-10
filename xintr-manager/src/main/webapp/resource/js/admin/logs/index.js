var Grid;
$(function () {

    $('.dateTime').datepicker({
        language:  'zh-CN',
        format: "yyyy-mm-dd",
        todayHighlight: true,
        clearBtn: true,
        autoclose: true
    });
    
    Grid = new DataGrid({
            columns: [
                {field: 'checkbox', checkbox: true},
                {
                    field: 'rowNo',
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index + 1;
                    }
                },
                {field: 'logMemberLogintime', title: '登录时间', formatter:function(value,row,index){
                    if(value){
                        return unixToTime(value);
                    }
                    return '';
                }},
                {field: 'memberLogname', title: '账号'},
                {field: 'logIp', title: 'IP'},
                {field: 'userName', title: '姓名'},
                {field: 'jobName', title: '职务'},
                {field: 'opt', title: '操作动作', formatter:function (value, row, index) {
                    return '<a href="javascript:void(0)" onclick="searchMember('+ row.logMemberId +')">点击查看详情</a>';
                }},
                {field: 'logMemberLogouttime', title: '退出时间', formatter:function(value,row,index){
                    if(value){
                        return unixToTime(value);
                    }
                    return '';
                }}
            ],
            requestParam: function () {
                return {
                    'uname': $('#uname').val(),
                    'memberId': $('#memberId').val(),
                    'date_str': $('#date_str').val()
                }
            }
        }
    );

    $('#cleanBtn').on('click', function () {
        $('#searchForm #uname').val('');
        $('#searchForm #memberId').val('');
        $('#searchForm #date_str').val('');
        Grid.onQuery();
    });
});

function searchMember(memberId){
    $('#memberId').val(memberId);
    Grid.onQuery();
}