window.onload = function () {
   var html=document.getElementById('html');
        var width=document.documentElement.clientWidth;
        html.style.fontSize=width/750*100+'px';
        window.onresize=function(){
            var width=document.documentElement.clientWidth;
            html.style.fontSize=width/750*100+'px';
        }
}