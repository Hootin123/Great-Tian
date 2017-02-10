$(function () {

    var aLi = $('.main_left ul li');

    aLi.click(function (index) {

        aLi.each(function (idex) {

            aLi.removeClass('btn_a' + idex);

        })

        var a_index = $(this).index();

        $(this).addClass('btn_a' + a_index);

    })

    var aPagea = $('.payoff_two_page a');

    aPagea.click(function () {

        aPagea.removeClass('page_active');
        $(this).addClass('page_active');

    });

    var aOli = $('.payoff_two_nav li');

    aOli.click(function () {

        aOli.removeClass('payoff_two_active');
        $(this).addClass('payoff_two_active');

    })

    var memberDd = $('.member dd');

    memberDd.click(function () {

        memberDd.removeClass('dd_active');
        $(this).addClass('dd_active');

    })

    onWinResize();

    $('#main_left_ul li').click(function () {
        var a = $(this).attr('data-url');
        $('#J_iframe').attr('src', a);
    });



})

window.onload = function () {
    (function () {
        window.onresize = function () {
            onWinResize();
        }
    })();
}

function onWinResize() {
    var height = $(window).height() - 60 - 70 - 60;
    var width = $(window).width();

    if (height <= 740) {

        height = 740;

    }

    $('.right').css({
        'width': width - 250,
        'height': height
    });
    $('#main_left_ul').css('height', height)
};

