
var arr = [
    {'title':'付款银行名称','name':'borrowBankName','id':'borrowBankName'},
    {'title':'付款银行账号','name':'borrowBankNo','id':'borrowBankNo'},
    {'title':'银行交易流水号','name':'borrowSerialNumber','id':'borrowSerialNumber'},
    {'title':'到账金额','name':'borrowAccountMoneyValue','id':'borrowAccountMoneyValue'},
    {'title':'备注','name':'borrowRemark','id':'borrowRemark'}
]
var num1 = 0;

function create(num){
    var oTr = '';
    var oDiv ='';

    for(var i=0; i<arr.length;i++){
        oTr += '<tr><td>' + arr[i].title + '：</td><td><input type="text" name="'+ arr[i].name +'" id="'+ (arr[i].id+ num1) +'" /></td></tr>';
    }

    for (var i=0;i<1;i++){
        oDiv += '<div class="s-box clear"><div>渠道'+ num +'</div><table>' + oTr + '</table></div>'
    }

    $('.sparent-box').append(oDiv);
    num1++;

}

$(function(){

    var num = 1;
    create(num);

    $('.add-sbox').click(function(){
        num++;
        create(num);

    })

    $('.add-sboxclose').click(function () {
        $('.sparent-box').children().remove();
        num = 1;
        create(1);

    })


})