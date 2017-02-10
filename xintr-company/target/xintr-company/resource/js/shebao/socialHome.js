$(function () {
     var billCount=$("input[name='orderCount']").val();
    if(billCount!=0){
        $("#atBill").show();
    }

    $('.navRight2 , .seek').click(function (event) {
        event.stopPropagation();
        $('.screen').hide();
        $('.batch').hide();
        $('.seek').show();
    });
    $(document).click(function () {
        $('.seek').hide();
        $('.screen').hide();
        $('.batch').hide();
        $('.form1').hide();
    });
    $('.navRight1 ,.screen').click(function (event) {
        event.stopPropagation();
        $('.seek').hide();
        $('.batch').hide();
        $('.screen').show();
    });
    $('.form').click(function (event) {
        event.stopPropagation();
        $(this).next('ul').toggle();
        if(!$('.form1').eq(3).is(':hidden')||$('.section1').html()==""){
            $('.form1').eq(3).hide();
        }
    });

    $('.form1').children('li').click(function (event) {
        event.stopPropagation();
        $(this).parents('.form1').prev('.form').html($(this).html()).css('color', '#666').end().end().parents('.form1').hide();
        $(this).parents('.form1').prev('.form').attr("data-value", $(this).attr("data-value"))
        //$(this).parents('.form1').prev('.section').value($(this).html()).css('color', '#666').end().end().parents('.form1').hide();

    });
    $('.navRight3').click(function (event) {
        event.stopPropagation();
        $('.seek').hide();
        $('.screen').hide();
        $('.batch').show();
    });

    if (!$('.result').html() == "") {
        $('.cancel').click(function () {
            $(this).parents('.seekResult').hide();
            resetQuery();
            initData();
        })
    } else {
        // $('.cancel').hide()
    }

    $(".seekBtn").click(function(){
        $('.seek').hide();
        initData();
    });

    /**
     * 重置查询条件
     */
    resetQuery = function () {
        $('.form').eq(0).removeAttr('data-value');
        $('.form').eq(0).html('聘用形式');

        $('.form').eq(1).removeAttr('data-value');
        $('.form').eq(1).html('员工状态');

        $('.form').eq(2).removeAttr('data-value');
        $('.form').eq(2).html('参保地区');

        $('.form1').eq(3).removeAttr('data-value');
        $('.form').eq(3).val('');
        $('.seekText').val("");
        $('.form1,.form').css('color', '');
        //关闭弹框
        $('.screen').hide();

        isNew = isKeepSb = isKeepGjj = isFailed = 0;
    }

    $('.formBtn').click(function () {
        $('.screen').hide(100);
        initData();

    });

    $('.section1').on('click', 'li', function () {

        $('.section').val($(this).html()).css('color', '#666');
        $('.section1').attr("data-value", $(this).attr("data-value"));
        $('.section1').hide();
    });
    //获取标题
    $('thead').append(getTitle());

    function getTitle() {
        var html = '<tr class="tableNav">';
        html += ' <td style="width:4%;">序号</td>';
        html += '<td style="width:10%;">姓名</td>';
        html += '<td style="width:10%;">手机号</td>';
        html += '<td style="width:10%;">部门</td>';
        html += '<td style="width:10%;">参保地区</td>';
        html += '<td style="width:10%;">参保类型</td>';
        html += '<td style="width:10%;">基数（元）</td>';
        html += '<td style="width:10%;">状态</td>';
        html += '<td>详情</td>';
        html += '<td style="width:12%;">操作</td>';
        html += '</tr>';
        return html;
    }

    initData = function(para) {
        $("#searchTj").empty();
        //组装参数
        para = para || {};
        para.stationEmployMethod = $('.form').eq(0).attr('data-value');
        para.stationCustomerState = $('.form').eq(1).attr('data-value');
        para.joinCityCode = $('.form').eq(2).attr('data-value');
        para.depName= $('.section').val();
        para.userName = $('.seekText').val();

        if(isNew) {
            $('.seekResult').show();
            $("#searchTj").append("<li><span>新入职</span></li>");
            para.isNew = true;
        }

        if(isKeepSb) {
            $('.seekResult').show();
            $("#searchTj").append("<li><span>社保在缴</span></li>");
            para.isKeepSb = true;
        }
        if(isKeepGjj) {
            $('.seekResult').show();
            $("#searchTj").append("<li><span>公积金在缴</span></li>");
            para.isKeepGjj = true;
        }

        if(isFailed) {
            $('.seekResult').show();
            $("#searchTj").append("<li><span>资料审核失败</span></li>");
            para.isFailed = true;
        }

        if(para.stationEmployMethod){
            $('.seekResult').show();
            $("#searchTj").append("<li><span>" + $('.form').eq(0).html() + "</span></li>");
        }
        if(para.stationCustomerState){
            $('.seekResult').show();
            $("#searchTj").append("<li><span>" + $('.form').eq(1).html() + "</span></li>");
        }
        if(para.joinCityCode){
            $('.seekResult').show();
            $("#searchTj").append("<li><span>" + $('.form').eq(2).html() + "</span></li>");
        }
        if(para.depName){
            $('.seekResult').show();
            $("#searchTj").append("<li><span>" + para.depName + "</span></li>");
        }
        if(para.userName) {
            $('.seekResult').show();
            $("#searchTj").append("<li><span>" + para.userName + "</span></li>");
        }
        
        $('.homeAll tbody').empty();
        $.ajax({
            type: 'post',
            url: BASE_PATH + '/shebao/dataList.htm',
            data: para,
            dataType: 'json',
            success: function (data) {
                data = eval("(" + data + ")");
                var html = "";
                var a = 1;
                if (data.data.length <= 0){
                    html = "<tr><td colspan='10'>该员工不存在</td></tr>";
                }
                else{
                    for (var i = 0; i < data.data.length; i++) {
                        var item = data.data[i];
                        console.log(item)
                        item.is_sb_keep = (item.is_sb_keep || 0);
                        item.is_gjj_keep = (item.is_gjj_keep || 0);
                        if(i%2==1){
                            html += '<tr class="alltr allTr" title="点击查看员工缴纳详情" style="cursor: pointer"  onclick=infoDetails(event,'+item.customer_id+');>';
                        }else{
                            html += '<tr class="alltr" title="点击查看员工缴纳详情" style="cursor: pointer"  onclick=infoDetails(event,'+item.customer_id+');>';
                        }
                        html += '<td rowspan=2>' + (a++) + '</td>';
                        html += '<td rowspan=2 style="position: relative;color:#2e9ef4;" ><div>' + data.data[i].customer_turename;
                        if(item.isNew == 1) {
                            html += "<span style='color:#fa5f5f;position:absolute;top:5px;right:7px;'>&nbsp;新</span>";
                        }
                        html += '</div>';
                        if(item.approveState == 4){
                            html += "<div onclick=showApprroveFail("+item.customer_id+") style='color:red'>资料审核失败</div>";
                        }else{
                            html += '<div></div>';
                        }

                        html += '</td>';
                        html += '<td rowspan=2>' + data.data[i].customer_phone + '</td>';
                        html += '<td rowspan=2>' + (data.data[i].dep_name || '-') + '</td>';
                        html += '<td rowspan=2>' + (data.data[i].join_city_name || '-') + '</td>';
                        html += '<td>社保</td>';
                        html += '<td>' + (data.data[i].sb_base || '') + '</td>';
                        html += '<td>' + (data.data[i].is_sb_keep ? '缴纳中' : '未缴纳') + '</td>';

                        //社保详情
                        html += '<td style="text-align: left;padding-left:40px;">';
                        if(item.currentDesc) {
                            var desc = item.currentDesc;
                            if(desc.sbZyText && !item.isTimeOut) {
                                html += "增员：" + desc.sbZyText + "<br/>";
                            }
                            if(desc.sbHjText && !item.isTimeOut) {
                                html += "续缴：" + desc.sbHjText + "<br/>";
                            }
                            if(desc.sbBjText && !item.isTimeOut) {
                                html += "补缴：" + desc.sbBjText + "<br/>";
                            }
                            if(desc.sbTjText && !item.isTimeOut) {
                                html += "调基：" + desc.sbTjText + "<br/>";
                            }
                            if(desc.sbStopText && item.station_customer_state < 3 && !item.isTimeOut) {
                                html += "停缴：" + desc.sbStopText + "<br/>";
                            }else if(item.sb_stop_date) {
                                html += "停缴：" + unixToTime(item.sb_stop_date, 'ym') + "<br/>";
                            }

                        }else{
                            //html += "-";
                        }

                        //社保上月缴纳状态
                        if(item.lastResult && item.lastResult["1"]) {
                            $.each(item.lastResult["1"], function(index, data) {
                                data = eval("(" + data + ")");
                                html += '<span style="color:#fa5f5f;" onclick=showFaildMsg("'+ data.reason +'",this)>' + data.text + '</span><br/>';
                            })
                        }

                        //当前账单失败信息
                        if(item.currentResult && item.currentResult["1"]) {
                            $.each(item.currentResult["1"], function(index, data) {
                                data = eval("(" + data + ")");
                                html += '<span style="color:#fa5f5f;" onclick=showFaildMsg("'+ data.reason +'",this)>' + data.text + '</span><br/>';
                            })
                        }
                        html += '</td>';

                        var mesgSbOrderTimeOut = "";
                        mesgSbOrderTimeOut += "您好：" + item.descOrderLastTime + "--" + item.descLastTime + "是''" + item.join_city_name + "''的社保账单处理期，此期间无法受理该地区的需求，请于" + item.descLastTime + "后再提交"
                        //alert(mesgSbOrderTimeOut);
                        //社保操作
                        html += '<td class="tdColor">';
                        if(item.is_sb_keep == 0) {
                            if(item.isWaitDismissing) {
                                html += '<a href="javascript:showInfo(\'该员工即将离职，不能修改设置，否则有可能造成离职后仍然缴社保的情况\')">缴纳</a>';
                            }else if(item.companyOrderStatus && item.companyOrderStatus > 1){
                                html += '<a href="javascript:showInfo(\'该地区企业订单已提交\')">缴纳</a>&nbsp;';
                            }else if(item.isOrderTimeOut){

                                // html += '<a href="javascript:showInfo(\'已过订单截止日\')"> 缴纳</a>&nbsp;';
                                html += '<a href="javascript:showInfo(&#34;' + mesgSbOrderTimeOut + '&#34;)">缴纳</a>&nbsp;';
                            }else{
                                html += '<a href="' +BASE_PATH+ '/shebao/setShebao.htm?customerId=' + data.data[i].customer_id + '">缴纳</a>';
                            }

                        }else{
                            if(item.sb_shebaotong_status == 2) {//社保通处于未缴纳或者缴纳失败可以进行调基
                                //html += '<a href="' +BASE_PATH+ '/shebao/setShebao.htm?customerId=' + data.data[i].customer_id + '">调基</a><br/>';
                                if(item.isWaitDismissing){
                                    html += '<a href="javascript:showInfo(\'该员工即将离职，不能修改设置，否则有可能造成离职后仍然缴社保的情况\')">调基</a>';
                                }else if(item.companyOrderStatus && item.companyOrderStatus > 1){
                                    html += '<a href="javascript:showInfo(\'该地区企业订单已提交\')">调基</a>&nbsp;';
                                }else if(item.isOrderTimeOut){
                                    // html += '<a href="javascript:showInfo(\'已过订单截止日\')"> 调基</a>&nbsp;';
                                    html += '<a href="javascript:showInfo(&#34;' + mesgSbOrderTimeOut + '&#34;)">调基</a>&nbsp;';
                                }else{
                                    html += "<a onclick='adjustBaseValidator(1,"+data.data[i].customer_id+");' href='#'>调基</a>&nbsp;";
                                }

                            }

                            if(item.sb_shebaotong_status != 2){//社保通处于未缴纳或者缴纳失败可以进行补缴
                                if(item.isWaitDismissing){
                                    html += '<a href="javascript:showInfo(\'该员工即将离职，不能修改设置，否则有可能造成离职后仍然缴社保的情况\')"> 补缴</a>';
                                } else if(item.companyOrderStatus && item.companyOrderStatus > 1){
                                    html += '<a href="javascript:showInfo(\'该地区企业订单已提交\')"> 补缴</a>&nbsp;';
                                }else if(item.isOrderTimeOut){
                                    // html += '<a href="javascript:showInfo(\'已过订单截止日\')"> 补缴</a>&nbsp;';
                                    html += '<a href="javascript:showInfo(&#34;' + mesgSbOrderTimeOut + '&#34;)">补缴</a>&nbsp;';
                                }else{
                                    html += "<a  onclick='payment("+ data.data[i].current_company_order_id + ","+data.data[i].customer_id+",1);' class='payment'>补缴</a>&nbsp;";
                                }

                            }
                            //html += '<a href="' +BASE_PATH+ '/shebao/setShebao.htm?customerId=' + data.data[i].customer_id + '">停缴</a><br/>';
                            if(item.companyOrderStatus && item.companyOrderStatus > 1){
                                html += "<a onclick=showInfo(\'该地区企业订单已提交\')> 停缴</a>&nbsp;";
                            }else if(item.isOrderTimeOut){
                                // html += '<a href="javascript:showInfo(\'已过订单截止日\')"> 停缴</a>&nbsp;';
                                html += '<a href="javascript:showInfo(&#34;' + mesgSbOrderTimeOut + '&#34;)">停缴</a>&nbsp;';
                            }else{
                                html += "<a onclick='paymentValidator(1,"+data.data[i].customer_id+");' href='#'>停缴</a>&nbsp;";
                            }

                        }
                        html += '</td>';


                        html += '</tr>';
                        html += '<tr title="点击查看员工缴纳详情" style="cursor: pointer" onclick=infoDetails(event,'+item.customer_id+');>';
                        html += '<td>公积金</td>';
                        html += '<td>' + (data.data[i].gjj_base || '') + '</td>';
                        html += '<td>' + (data.data[i].is_gjj_keep ? '缴纳中' : '未缴纳') + '</td>';

                        //公积金详情
                       html += '<td style="text-align: left;padding-left:40px;">';
                        if(item.currentDesc) {
                            var desc = item.currentDesc;
                            if(desc.gjjZyText && !item.isTimeOut) {
                                html += "增员：" + desc.gjjZyText + "<br/>";
                            }
                            if(desc.gjjHjText && !item.isTimeOut) {
                                html += "续缴：" + desc.gjjHjText + "<br/>";
                            }
                            if(desc.gjjBjText && !item.isTimeOut) {
                                html += "补缴：" + desc.gjjBjText + "<br/>";
                            }
                            if(desc.gjjTjText && !item.isTimeOut) {
                                html += "调基：" + desc.gjjTjText + "<br/>";
                            }
                            if(desc.gjjStopText && !item.isTimeOut) {
                                html += "停缴：" + desc.gjjStopText + "<br/>";
                            }else if(item.gjj_stop_date) {
                                html += "停缴：" + unixToTime(item.gjj_stop_date, 'ym') + "<br/>";
                            }
                        }else{
                            //html += "-";
                        }

                        //公积金上月缴纳状态
                        if(item.lastResult && item.lastResult["2"]) {
                            $.each(item.lastResult["2"], function(index, data) {
                                data = eval("(" + data + ")");
                                html += '<span style="color:#fa5f5f;" onclick=showFaildMsg("'+ data.reason +'",this)>' + data.text + '</span><br/>';
                            })
                        }

                        //当前账单失败信息
                        if(item.currentResult && item.currentResult["2"]) {
                            $.each(item.currentResult["2"], function(index, data) {
                                data = eval("(" + data + ")");
                                html += '<span style="color:#fa5f5f;" onclick=showFaildMsg("'+ data.reason +'",this)>' + data.text + '</span><br/>';
                            })
                        }

                        html += '</td>';

                        //公积金操作
                        var mesgGjjOrderTimeOut = "";
                        mesgGjjOrderTimeOut += "您好：" + item.descOrderLastTime + "--" + item.descLastTime + "是''" + item.join_city_name + "''的公积金账单处理期，此期间无法受理该地区的需求，请于" + item.descLastTime + "后再提交"
                        html += '<td  class="tdColor">';
                        if((item.is_gjj_keep == 0)) {
                            if(item.is_sb_keep == 1) {
                                if(item.isWaitDismissing){
                                    html += '<a href="javascript:showInfo(\'该员工即将离职，不能修改设置，否则有可能造成离职后仍然缴社保的情况\')"> 缴纳</a>';
                                }else if(item.companyOrderStatus && item.companyOrderStatus > 1){
                                    html += '<a href="javascript:showInfo(\'该地区企业订单已提交\')"> 缴纳</a>&nbsp;';
                                }else if(item.isOrderTimeOut){
                                    // html += '<a href="javascript:showInfo(\'已过订单截止日\')"> 缴纳</a>&nbsp;';
                                    html += '<a href="javascript:showInfo(&#34;' + mesgGjjOrderTimeOut + '&#34;)">缴纳</a>&nbsp;';
                                }else{
                                    html += '<a href="' +BASE_PATH+ '/shebao/setGjj.htm?customerId=' + data.data[i].customer_id + '">缴纳</a>';
                                }
                            }else{
                                html += '<a href="javascript:showInfo(\'缴纳公积金时必须缴纳社保，请先设置缴纳社保\')">缴纳</a>';
                            }
                        }else if(item.is_gjj_keep == 1 && item.is_sb_keep == 1){
                            if(item.gjj_shebaotong_status == 2) {
                                //html += '<a href="' +BASE_PATH+ '/shebao/setShebao.htm?customerId=' + data.data[i].customer_id + '">调基</a><br/>';
                                if(item.isWaitDismissing){
                                    html += '<a href="javascript:showInfo(\'该员工即将离职，不能修改设置，否则有可能造成离职后仍然缴社保的情况\')">调基</a>';
                                }else if(item.companyOrderStatus && item.companyOrderStatus > 1){
                                    html += '<a href="javascript:showInfo(\'该地区企业订单已提交\')">调基</a>&nbsp;';
                                }else if(item.isOrderTimeOut){
                                    // html += '<a href="javascript:showInfo(\'已过订单截止日\')"> 调基</a>&nbsp;';
                                    html += '<a href="javascript:showInfo(&#34;' + mesgGjjOrderTimeOut + '&#34;)">调基</a>&nbsp;';
                                }else{
                                    html += "<a onclick='adjustBaseValidator(2,"+data.data[i].customer_id+");' href='#'>调基</a>&nbsp;";
                                }

                            }
                            if(item.gjj_shebaotong_status != 2) {
                                if(item.isWaitDismissing){
                                    html += '<a href="javascript:showInfo(\'该员工即将离职，不能修改设置，否则有可能造成离职后仍然缴社保的情况\')">补缴</a>';
                                }else if(item.companyOrderStatus && item.companyOrderStatus > 1){
                                    html += '<a href="javascript:showInfo(\'该地区企业订单已提交\')"> 补缴</a>&nbsp;';
                                }else if(item.isOrderTimeOut){
                                    // html += '<a href="javascript:showInfo(\'已过订单截止日\')"> 补缴</a>&nbsp;';
                                    html += '<a href="javascript:showInfo(&#34;' + mesgGjjOrderTimeOut + '&#34;)">补缴</a>&nbsp;';
                                }else{
                                    // html += '<a href="' +BASE_PATH+ '/shebao/toBj.htm?shebaoType=2&customerId=' + data.data[i].customer_id + '">补缴</a>&nbsp;';
                                    html += "<a  onclick='payment("+ data.data[i].current_company_order_id + ","+data.data[i].customer_id+",2);' class='payment'>补缴</a>&nbsp;";
                                }
                            }
                            //html += '<a href="' +BASE_PATH+ '/shebao/setShebao.htm?customerId=' + data.data[i].customer_id + '">停缴</a><br/>';
                            if(item.companyOrderStatus && item.companyOrderStatus > 1){
                                html += '<a href="javascript:showInfo(\'该地区企业订单已提交\')"> 停缴</a>&nbsp;';
                            }else if(item.isOrderTimeOut){
                                // html += '<a href="javascript:showInfo(\'已过订单截止日\')"> 停缴</a>&nbsp;';
                                html += '<a href="javascript:showInfo(&#34;' + mesgGjjOrderTimeOut + '&#34;)">停缴</a>&nbsp;';
                            }else{
                                html += "<a onclick='paymentValidator(2,"+data.data[i].customer_id+");' href='#'>停缴</a>&nbsp;";
                            }

                        }else{
                            html += '<a href="javascript:showInfo(\'缴纳公积金时必须缴纳社保，请先设置缴纳社保\')">缴纳</a>';
                        }
                        html += '</td>';

                        html += '</tr>';
                    }
}


                $(' .homeAll tbody').append(html);
                //划过变色
                $('tbody .tdColor').on('click',function(e){
                    e.stopPropagation();
                    // e.preventDefault()
                })
                $('tbody tr').hover(
                    function(){

                        if(($(this).index())%2==0){
                            $(this).addClass('activeTr');
                            $(this).next('tr').addClass('activeTr');
                        }else{
                            $(this).addClass('activeTr');
                            $(this).prev('tr').addClass('activeTr');
                        }

                    },
                    function () {

                        if(($(this).index())%2==0) {
                            $(this).removeClass('activeTr');
                            $(this).next('tr').removeClass('activeTr');
                        }else{
                            $(this).removeClass('activeTr');
                            $(this).prev('tr').removeClass('activeTr');
                        }

                    }
                )
                $('tbody .tdColor').hover(
                    function(){
                        $('tbody tr').attr('title','');
                    },
                    function(){
                        $('tbody tr').attr('title','点击查看员工缴纳详情');
                    }
                )
                //end
                generPageHtml(data.paginator)
                $("#count").html(data.paginator.totalCount || 0);
            }
        });
    }

    /**
     * 生成分页控件
     */
    function generPageHtml(paginator) {
        //生成分页控件
        // kkpager.generPageHtml({
        //     pno: paginator.page,
        //     mode: 'click', //可选，默认就是link
        //     //总页码
        //     total: paginator.totalPages,
        //     //总数据条数
        //     totalRecords: paginator.totalCount,
        //     //链接前部
        //     hrefFormer: '',
        //     //链接尾部
        //     hrefLatter: '',
        //     isShowFirstPageBtn: false,
        //     isShowLastPageBtn: false,
        //     click: function (n) {
        //         var param = {'pageIndex': n};
        //         //alert(n);
        //         initData(param);
        //     },
        //     //getHref是在click模式下链接算法，一般不需要配置，默认代码如下
        //     getHref: function (n) {
        //         return '#';
        //     }
        // }, true);
        laypage({
            cont: 'pagePayroll', //容器。值支持id名、原生dom对象，jquery对象,
            pages: paginator.totalPages, //总页数
            skin: 'molv', //皮肤
            first: 1, //将首页显示为数字1,。若不显示，设置false即可
            last: paginator.totalPages, //将尾页显示为总页数。若不显示，设置false即可
            prev: '上一页', //若不显示，设置false即可
            next: '下一页', //若不显示，设置false即可
            skip: true, //是否开启跳页
            curr: paginator.page || 1, //当前页
            jump: function (obj, first) { //触发分页后的回调
                if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                    // console.info(obj.curr);
                    // showData(obj.curr)
                    var param = {'pageIndex': obj.curr};
                    initData(param);
                }
            }
        });
    }

    initData();

    var deptData = null;

    //$('.section').keyup(function () {
    //    if(!$(this).val()==""){
    //        $('.section1').show();
    //    }else {
    //        $('.section1').hide();
    //    }
    //    $('.section1').empty();
    //    var kw = $(this).val();
    //    if (!isEmpty(kw)) {
    //        if(deptData == null) {
    //            $.ajax({
    //                type: 'post',
    //                url: BASE_PATH + '/dept/tree.htm',
    //                success: function (data) {
    //
    //                    if(data.success) {
    //                        deptData = data.data;
    //                        loadChildren(deptData, kw)
    //                    }
    //                }
    //            });
    //        }else{
    //            loadChildren(deptData, kw)
    //        }
    //    }
    //});
    //
    //function loadChildren(deptData, keyword) {
    //    if(deptData) {
    //        if(deptData.depName.indexOf(keyword) != -1) {
    //            $('.section1').append('<li data-value="' + deptData.depId + '">' + deptData.depName + '</li>');
    //        }
    //        if(deptData.children) {
    //            $.each(deptData.children, function(index, data){
    //                loadChildren(data, keyword);
    //            })
    //        }
    //    }
    //}

    //加载部门数据
    function section(data) {


    }

    //点击停缴社保确认按钮
    $(".aboxbg .aboxbgBtn1").click(function(){
        $('.laydate_box').hide();
        //验证
        if($(".aboxbg #stopSocial").val()==undefined || $(".aboxbg #stopSocial").val()==""){
            layer.alert("请选择开始停缴月份", {icon: 1});
            return false;
        }
        paymentSureOperation(1,$("#tjCustomerId").val(),$(".aboxbg #stopSocial").val(),".aboxbg",".aboxbg #stopSocial");
    });
    //点击停缴社保取消按钮
    $(".aboxbg .aboxbgBtn2").click(function(){
        $.ajax({
            type: 'post',
            url: BASE_PATH + '/shebao/cancelStopPayment.htm',
            data: {"relationType": 1,"customerId":$("#tjCustomerId").val()},
            async: false,
            success: function (data) {
                console.log(data);
                if (data.success) {
                    initData({"pageIndex":kkpager.pno});
                    layer.alert('已取消停缴',{
                        title:'处理结果',
                        cancel:function(){
                            $(".pacity").hide();
                            $(".aboxbg").hide();
                            $("#tjCustomerId").val("");
                        }
                    },function(index){
                        $(".pacity").hide();
                        $(".aboxbg").hide();
                        $("#tjCustomerId").val("");
                        layer.close(index);
                    });
                }else{
                    var message=data.message;
                    if(message==="cancel"){
                        layer.alert('没有停缴可以取消',{
                            title:'处理结果',
                            cancel:function(){
                                $(".pacity").hide();
                                $(".aboxbg").hide();
                                $("#tjCustomerId").val("");
                            }
                        },function(index){
                            $(".pacity").hide();
                            $(".aboxbg").hide();
                            $("#tjCustomerId").val("");
                            layer.close(index);
                        });
                    }else{
                        layer.alert(data.message, {icon: 1});
                    }
                }
            }
        });

    });

    //点击停缴公积金确认按钮
    $(".aboxbg1 .aboxbgBtn1").click(function(){
        //验证
        if($(".aboxbg1 #stopSocial2").val()==undefined || $(".aboxbg1 #stopSocial2").val()==""){
            layer.alert("请选择开始停缴月份", {icon: 1});
            return false;
        }
        paymentSureOperation(2,$("#tjCustomerId").val(),$(".aboxbg1 #stopSocial2").val(),".aboxbg1",".aboxbg1 #stopSocial2");
    });

    $("#shebaoCloseBtn").click(function(){
        $(".pacity").hide();
        $(".aboxbg").hide();
        $("#tjCustomerId").val("");
    });

    $("#gjjCloseBtn").click(function(){
        $(".pacity").hide();
        $(".aboxbg1").hide();
        $("#tjCustomerId").val("");
    });
    //点击停缴公积金取消按钮
    $(".aboxbg1 .aboxbgBtn2").click(function(){

        $.ajax({
            type: 'post',
            url: BASE_PATH + '/shebao/cancelStopPayment.htm',
            data: {"relationType": 2,"customerId":$("#tjCustomerId").val()},
            async: false,
            success: function (data) {
                console.log(data);
                if (data.success) {
                    initData({"pageIndex":kkpager.pno});
                    layer.alert('已取消停缴',{
                        title:'处理结果',
                        cancel:function(){
                            $(".pacity").hide();
                            $(".aboxbg1").hide();
                            $("#tjCustomerId").val("");
                        }
                    },function(index){
                        $(".pacity").hide();
                        $(".aboxbg1").hide();
                        $("#tjCustomerId").val("");
                        layer.close(index);
                    });
                }else{
                    var message=data.message;
                    if(message==="cancel"){
                        layer.alert('没有停缴可以取消',{
                            title:'处理结果',
                            cancel:function(){
                                $(".pacity").hide();
                                $(".aboxbg1").hide();
                                $("#tjCustomerId").val("");
                            }
                        },function(index){
                            $(".pacity").hide();
                            $(".aboxbg1").hide();
                            $("#tjCustomerId").val("");
                            layer.close(index);
                        });
                    }else{
                        layer.alert(data.message, {icon: 1});
                    }
                }
            }
        });
    });

    //点击调基社保确认按钮
    $(".sociaAdjust .sociaBtn1").click(function(){
        var adjustAmount=$(".sociaAdjust #adjustAmount").val();
        //验证
        if(adjustAmount==undefined || adjustAmount==""){
            layer.alert("请输入正确基数", {icon: 1});
            return false;
        }
        if(!(/^[0-9]+(.[0-9]{1,2})?$/.test(adjustAmount))){
            layer.alert('基数输入格式不正确',{title:'提示信息'});
            return false;
        }
        var shebaoAdjustMinBase=$("#shebaoAdjustMinBase").val()==undefined || $("#shebaoAdjustMinBase").val()==""?0:parseFloat($("#shebaoAdjustMinBase").val());
        var shebaoAdjustMaxBase=$("#shebaoAdjustMaxBase").val()==undefined || $("#shebaoAdjustMaxBase").val()==""?0:parseFloat($("#shebaoAdjustMaxBase").val());
        if(adjustAmount<shebaoAdjustMinBase || adjustAmount>shebaoAdjustMaxBase){
            layer.alert('请输入正确的基数',{title:'提示信息'});
            return false;
        }
        adjustBaseSureOperation(1,$("#tjCustomerId").val(),$(".sociaAdjust #adjustAmount").val(),".sociaAdjust",".sociaAdjust #adjustAmount");
    });
    //点击调基社保取消按钮
    $(".sociaAdjust .sociaBtn2").click(function(){
        $(".pacity").hide();
        $(".sociaAdjust").hide();
        $("#tjCustomerId").val("");
    });

    //点击调基公积金确认按钮
    $(".fundAdjust .sociaBtn1").click(function(){
        var adjustAmount2=$(".fundAdjust #adjustAmount2").val();
        //验证
        if(adjustAmount2==undefined || adjustAmount2==""){
            layer.alert("请输入基数", {icon: 1});
            return false;
        }
        if(!(/^[0-9]+(.[0-9]{1,2})?$/.test(adjustAmount2))){
            layer.alert('基数输入格式不正确',{title:'提示信息'});
            return false;
        }
        var gjjAdjustMinBase=$("#gjjAdjustMinBase").val()==undefined || $("#gjjAdjustMinBase").val()==""?0:parseFloat($("#gjjAdjustMinBase").val());
        var gjjAdjustMaxBase=$("#gjjAdjustMaxBase").val()==undefined || $("#gjjAdjustMaxBase").val()==""?0:parseFloat($("#gjjAdjustMaxBase").val());
        if(adjustAmount2<gjjAdjustMinBase || adjustAmount2>gjjAdjustMaxBase){
            layer.alert('请输入正确的基数',{title:'提示信息'});
            return false;
        }
        adjustBaseSureOperation(2,$("#tjCustomerId").val(),$(".fundAdjust #adjustAmount2").val(),".fundAdjust",".fundAdjust #adjustAmount2");
    });
    //点击调基公积金取消按钮
    $(".fundAdjust .sociaBtn2").click(function(){
        $(".pacity").hide();
        $(".fundAdjust").hide();
        $("#tjCustomerId").val("");
    });
});

//点击停缴,验证
function paymentValidator(relationType,csutomerId){
    $.ajax({
        type: 'post',
        url: BASE_PATH + '/shebao/paymentValidator.htm',
        data: {"relationType": relationType,"customerId":csutomerId},
        async: false,
        success: function (data) {
            console.log(data);
            if (data.success) {
                var alreadyStopDateStr=data.data.alreadyStopDateStr;
                $(".pacity").show();
                if(relationType===1){
                    $(".aboxbg").show();
                    $("#tjCustomerId").val(csutomerId);
                    if(alreadyStopDateStr!=undefined && alreadyStopDateStr!=''){
                        $("#stopSocial").val(alreadyStopDateStr);
                    }
                }else if(relationType===2){
                    $(".aboxbg1").show();
                    $("#tjCustomerId").val(csutomerId);
                    if(alreadyStopDateStr!=undefined && alreadyStopDateStr!=''){
                        $("#stopSocial2").val(alreadyStopDateStr);
                    }
                }
            }else{
                if(data.message==="sureMsg"){
                    var alreadyStopDateStr=data.data.alreadyStopDateStr;
                    var alertMesg = "";
                    if(relationType===1){
                        alertMesg = "该员工已设置社保停缴";
                    }
                    else{
                        alertMesg = "该员工已设置公积金停缴";
                    }
                    layer.alert(alertMesg+alreadyStopDateStr,{
                        title:'提示信息',
                        cancel:function(){
                            $(".pacity").show();
                            if(relationType===1){
                                $(".aboxbg").show();
                                $("#tjCustomerId").val(csutomerId);
                                if(alreadyStopDateStr!=undefined && alreadyStopDateStr!=''){
                                    $("#stopSocial").val(alreadyStopDateStr.substring(0,4)+"-"+alreadyStopDateStr.substring(4,6));
                                }
                            }else if(relationType===2){
                                $(".aboxbg1").show();
                                $("#tjCustomerId").val(csutomerId);
                                if(alreadyStopDateStr!=undefined && alreadyStopDateStr!=''){
                                    $("#stopSocial2").val(alreadyStopDateStr.substring(0,4)+"-"+alreadyStopDateStr.substring(4,6));
                                }
                            }
                        }
                    },function(index){
                        $(".pacity").show();
                        if(relationType===1){
                            $(".aboxbg").show();
                            $("#tjCustomerId").val(csutomerId);
                            if(alreadyStopDateStr!=undefined && alreadyStopDateStr!=''){
                                $("#stopSocial").val(alreadyStopDateStr.substring(0,4)+"-"+alreadyStopDateStr.substring(4,6));
                            }
                        }else if(relationType===2){
                            $(".aboxbg1").show();
                            $("#tjCustomerId").val(csutomerId);
                            if(alreadyStopDateStr!=undefined && alreadyStopDateStr!=''){
                                $("#stopSocial2").val(alreadyStopDateStr.substring(0,4)+"-"+alreadyStopDateStr.substring(4,6));
                            }
                        }
                        layer.close(index);
                    });
                }else{
                    layer.alert(data.message, {icon: 1});
                }
            }
        }
    });
}
//点击调基,验证
function adjustBaseValidator(relationType,csutomerId){
    //$(".pacity").show();
    //if(relationType===1){
    //    $(".sociaAdjust").show();
    //}else if(relationType===2){
    //    $(".fundAdjust").show();
    //}
    console.log(relationType);
    console.log(csutomerId);
        $.ajax({
            type: 'post',
            url: BASE_PATH + '/shebao/adjustBaseValidator.htm',
            data: {"relationType": relationType,"customerId":csutomerId},
            async: false,
            success: function (data) {
                console.log(data);
                if (data.success) {
                    var tjDto=data.data;
                    $(".pacity").show();
                    if(relationType===1){
                        $(".sociaAdjust").show();
                        $("#tjCustomerId").val(csutomerId);
                        $("#shebaoOldBase").text(tjDto.oldBase);
                        $("#shebaoAdjustMinBase").val(tjDto.minBase);
                        $("#shebaoAdjustMaxBase").val(tjDto.maxBase);
                        $("#shebaoAdjustRange").text("基数范围："+tjDto.minBase+"元-"+tjDto.maxBase+"元");
                        $("#shebaoAdjustMonth").text(tjDto.month);
                    }else if(relationType===2){
                        $(".fundAdjust").show();
                        $("#tjCustomerId").val(csutomerId);
                        $("#gjjOldBase").text(tjDto.oldBase);
                        $("#gjjAdjustMinBase").val(tjDto.minBase);
                        $("#gjjAdjustMaxBase").val(tjDto.maxBase);
                        $("#gjjAdjustRange").text("基数范围："+tjDto.minBase+"元-"+tjDto.maxBase+"元");
                        $("#gjjAdjustMonth").text(tjDto.month);
                    }
                }else{
                    layer.alert(data.message, {icon: 1});
                }
            }
        });
}
//停缴确认按钮操作详细
function paymentSureOperation(relationType,customerId,stopDataParam,hideElement,clearElement){
    $.ajax({
        type: 'post',
        url: BASE_PATH + '/shebao/stopPayment.htm',
        data: {"relationType": relationType,"customerId":customerId,"stopDataParam":stopDataParam},
        async: false,
        success: function (data) {
            console.log(data);
            if (data.success) {
                initData({"pageIndex":kkpager.pno});
                layer.alert('已设置停缴',{
                    title:'处理结果',
                    cancel:function(){
                        $(".pacity").hide();
                        $(""+hideElement).hide();
                        $(""+clearElement).val("");
                    }
                },function(index){
                    $(".pacity").hide();
                    $(""+hideElement).hide();
                    $(""+clearElement).val("");
                    layer.close(index);
                });
            }else{
                if(data.message==="sureMsg"){
                    initData({"pageIndex":kkpager.pno});
                    layer.alert('已设置停缴',{
                        title:'处理结果',
                        cancel:function(){
                            $(".pacity").hide();
                            $(""+hideElement).hide();
                            $(""+clearElement).val("");
                        }
                    },function(index){
                        $(".pacity").hide();
                        $(""+hideElement).hide();
                        $(""+clearElement).val("");
                        layer.close(index);
                    });
                }else{
                    layer.alert(data.message, {icon: 1});
                }
            }
        }
    });
}

//调基确认按钮操作详细
function adjustBaseSureOperation(relationType,customerId,adjustAmount,hideElement,clearElement){
    $.ajax({
        type: 'post',
        url: BASE_PATH + '/shebao/adjustBase.htm',
        data: {"relationType": relationType,"customerId":customerId,"adjustAmount":adjustAmount},
        async: false,
        success: function (data) {
            console.log(data);
            if (data.success) {
                initData({"pageIndex":kkpager.pno});
                layer.alert('已设置调基',{
                    title:'处理结果',
                    cancel:function(){
                        $(".pacity").hide();
                        $(""+hideElement).hide();
                        $(""+clearElement).val("");
                    }
                },function(index){
                    $(".pacity").hide();
                    $(""+hideElement).hide();
                    $(""+clearElement).val("");
                    layer.close(index);
                });
            }else{
                layer.alert(data.message, {icon: 1});
            }
        }
    });
}
//显示当前账单页面
function showCurrentPayOrder(){
    var index = layer.open({
        type: 2,
        title: false,
        maxmin: false,
        closeBtn: 0,
        shadeClose: false, //点击遮罩关闭层
        //area: ['650px', '380px'],
        content: BASE_PATH+'/shebao/current_payorder.htm'
    });
    //窗口最大化
    layer.full(index);
}


function showHistoryPayOrder(){
    var index = layer.open({
        type: 2,
        title: false,
        maxmin: false,
        closeBtn: 0,
        shadeClose: false, //点击遮罩关闭层
        //area: ['650px', '380px'],
        content: BASE_PATH+"/shebao/history_payorder.htm"
    });
    //窗口最大化
    layer.full(index);

}

//查看审核失败
function showApprroveFail(obj){
    var index = layer.open({
        type: 2,
        title: false,
        maxmin: false,
        closeBtn: 0,
        shadeClose: false, //点击遮罩关闭层
        //area: ['650px', '380px'],
        content: BASE_PATH+"/shebao/jumpToShebaoApprovePage.htm?customerId="+obj
    });
    //窗口最大化
    layer.full(index);
}
//跳转至个人社保公积金详情页面
function infoDetails(event, obj){
    if($(event.toElement).attr("onclick")) {
        return;
    }
    //event.preventDefault();
    var index = layer.open({
        type: 2,
        title: false,
        maxmin: false,
        closeBtn: 0,
        shadeClose: false, //点击遮罩关闭层
        //area: ['650px', '380px'],
        content: BASE_PATH+"/shebao/jumpToInfoDetailPage.htm?customerId="+obj
    });
    //窗口最大化
    layer.full(index);
}

function showFaildMsg(reason, _this){
    var bjBtn = $(_this).parent().next().find(":contains('补缴')");
    layer.open({
        content: '失败原因：' + reason + '<br/><span style="color:green">失败款项将在下月账单中进行抵扣</span>'
        ,btn: [(bjBtn.lenght ? '补缴' : '确定'), '取消']
        ,yes: function(index, layero){
            //按钮【按钮一】的回调
            if(bjBtn.lenght) {
                bjBtn[0].click();
            }
            layer.close(index);
        },btn2: function(index, layero){
            //按钮【按钮二】的回调
        }
        ,cancel: function(){
            //右上角关闭回调
        }
    });
}

var isNew, isKeepSb, isKeepGjj, isFailed;

function payment(companyShebaoOrderId,csutomerId,requireType){
    var url;
    if (requireType == "1"){
        url = BASE_PATH + '/shebao/getSbOverDetail.htm';
    }
    else{
        url = BASE_PATH + '/shebao/getGjjOverDetail.htm';
    }
    $.ajax({
        type: 'post',
        url: url,
        data: {"companyShebaoOrderId":companyShebaoOrderId,"customerId":csutomerId},
        async: false,
        success: function (data) {
            data=data.data
            console.log(data)
            if(data!=''&& data!==null){
                $('.xtrHint').show();
                $('.pacity').show();
                $('.xtrHint-main table').empty();
                $('.xtrHint-footer').empty();
                for(i=0;i<data.length;i++){

                    $('.xtrHint-main table').append("<tr><td>补缴月份"+(i+1)+"</td> <td>"+(data[i].overdueMonth).replace(/,/,"-")+"</td> <td>补缴基数"+(i+1)+"</td> <td>"+data[i].orderBase+"元</td></tr>")
                }
                //跳转
                $('.xtrHint-footer').append('<a href="' +BASE_PATH+ '/shebao/toBj.htm?shebaoType=1&customerId=' + csutomerId+ '">确认</a>')
                if (requireType == "1"){
                    $('.xtrHint-text').html('该员工已经设置社保补缴');
                }
                else{
                    $('.xtrHint-text').html('该员工已经设置公积金补缴');
                }

                //取消
                $('.xtrHint-title b').on('click',function(){
                    $('.xtrHint').hide();
                    $('.pacity').hide();
                })
            }else{
                $('.payment').attr('href',BASE_PATH+"/shebao/toBj.htm?shebaoType=1&customerId="+csutomerId);
            }

        },
        error:function(){
            alert('请求失败')
        }
    })

}
