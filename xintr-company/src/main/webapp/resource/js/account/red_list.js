$(function () {

    $('.btn').click(function() {
        $('.aboxbg').hide();
        $('.pacity').hide();
    });

    $('.abImg').click(function() {
        $('.aboxbg').hide();
        $('.pacity').hide();
    });

    $('.red_detail').on('click', function () {
        var redId = $(this).attr('red_id');
        $.getJSON(BASE_PATH + '/red_detail.htm?redId=' + redId,{}, function (response) {
            if(response && response.success){

                var red = response.data;

                var title = red.name + '（' + unixToDate(red.useStartTime) + ' - ' + unixToDate(red.useEndTime) + '）';

                console.log('title='+title);
                $('.aboxbg .red_title').text(title);
                $('.aboxbg .red_money').text(red.redMoney+"元");

                if(red.description){
                    var desc_html = '<ol>';
                    var desc = red.description.split('|');
                    for(var i=1; i<=desc.length; i++){
                        var item = desc[i];
                        if(item && item != ''){
                            desc_html += '<li>' + item + '</li>';
                        }
                    }
                    desc_html += '</ol>';

                    console.log('desc_html='+desc_html);
                    $('.aboxbg .red_desc').html(desc_html);
                }

                $('.aboxbg').show();
                $('.pacity').show();
            } else{
                alertWarn('查看详情失败');
            }
        });
    });
});