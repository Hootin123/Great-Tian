
/*弹窗*/
var abox;

$(function() {
    var oCav = document.getElementById('cav');
    var oCav1 = document.getElementById('cav1');

    var start1 = 180;
    var range1 = $("#blockPercent").val();
    var end1 = -(range1 - 50) * 3.6;

    var start2 = 90;
    var range2 = $("#availablePercent").val();
    var end2 = 90 + (range2 * 3.6);

    move(oCav, {
        'start': start1,
        'target': end1,
        'beginColor': '#80c9fc',
        'overColor': '#0696fa'
    }, true);

    move(oCav1, {
        'start': start2,
        'target': end2,
        'beginColor': '#8df8d4',
        'overColor': '#35e7ab'
    }, false);

    /*弹窗*/
    abox = $('.abox');

    abox.find('i').click(function() {
        closeAbox();
    });

});


function closeAbox() {
    abox.animate({
        'height': 0,
        'top': abox.height() / 2 + abox.position().top
    }, 300, function() {
        abox.css('overflow', 'hidden');
        $('.pacity').css('display', 'none');
    })
}

function alertBox(){
    $('.pacity').css('display', 'block');
    $('.aboxbg').css('display', 'block');
    abox.css({
        'height': 250,
        'left': -abox.width() / 2,
        'top': -125
    }, 300, function() {
        abox.css('overflow', 'scroll');
    })
}

/*弹窗背景自适应*/
window.onload = function() {
    window.onresize = function() {
        navlasLi();
    }
    navlasLi();
    var obj = new iMove('abox');
}

function navlasLi() {
    $('.pacity').width($(document).width());
    $('.pacity').height($(document).height());
    $('.main_right').width($(window).width());
    $('.main_right').height($(window).height());

}