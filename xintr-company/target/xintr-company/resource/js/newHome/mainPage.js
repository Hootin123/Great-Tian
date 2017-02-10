$(function () {

    //点击选项卡一级标签
    $('.content li').click(function () {
        var menu = $(this).attr("second-menu");
        if (menu == 0) {
            setTitle('<div>' + $(this).find('span').html() + '</div>');
            var a = $(this).attr('data-url');
            $('#J_iframe').attr('src', a);
        }else{
            $('.contentDiv1').each(function () {
                $(this).find('.divLi').eq(0).css('color','#4cdbc4');
            });
            setTitle('<div>' + $(this).find('span').html() + '</div>');
            var aa = $(this).attr('data-url');
            $('#J_iframe').attr('src', aa);
        }

    });
    //点击选项卡二级标签
    $('.content li').click(function () {
        var b = $(this).attr('class');
        if (b == "divLi") {
            //二级菜单赋值
            setTitle('<div>' + $(this).find('span').html() + '</div>');
            var a = $(this).attr('data-url');
            $('#J_iframe').attr('src', a);
        }

    });



});
//页面加载完 加载柱形菜单
function loadIndexPage() {
    $('#J_iframe').attr('src', '/newHomePage.htm');
}

/**
 * 设置标题
 * @param title
 */
function setTitle(titleHtml) {
    $(".topConTitleCon").html(titleHtml);
    $(".topConTitleCon1").html("");
}

/**
 * 设置标题年份
 * @param title
 */
function yearTitle(yearHtml) {
    $(".topConTitleCon1").html(yearHtml);
}


