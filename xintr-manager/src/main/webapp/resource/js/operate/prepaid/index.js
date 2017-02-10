$(function () {
    PrepaidExtend.init();
})


PrepaidExtend = {
    grid: 0,
    init: function () {
        PrepaidExtend.initDataGrid();
        window.addProtocolGrid = PrepaidExtend.grid;
    },
    initDataGrid: function () {
        PrepaidExtend.grid = new DataGrid({
                url: BASE_PATH + '/operate/prepaid/page.htm',
                columns: [
                    {field: 'company_id', title: '序号',
                        formatter: function (value, row, index) {
                            return index + 1;
                        }
                    },
                    {field: 'company_name', title: '公司名称',
                        formatter: function (value,row,index) {
                            return "<a href='#' onclick='PrepaidExtend.onShowCompanyDetail("+row.company_id+")' >"+value+"</a>";
                        }
                    },
                    {field: 'member_name', title: '超级管理员用户名',
                        formatter: function (value, row, index) {
                            if(value) {
                                return value;
                            }else{
                                return row.member_logname;
                            }
                        }},
                    {field: 'company_scale', title: '公司规模'},
                    {field: 'company_belong_industry', title: '所属行业'},
                    {field: 'company_isauth', title: '实名认证',
                        formatter: function (value, row, index) {
                            return $("select[name=companyIsauth] option[value='"+value+"']").html()
                        }
                    },
                    {field: 'company_datum_status', title: '垫付申请',
                        formatter: function (value, row, index) {
                            return $("select[name=companyDatumStatus] option[value='"+value+"']").html()
                        }
                    },
                    {
                        field: 'company_datum_status', title: '操作',
                        formatter: function (value, row, index) {
                            if (row.company_datum_status == 0) {

                                if(row.isCanSignDf){
                                    return '<a href="javascript:PrepaidExtend.showPass('+row.company_id+')">通过</a></br>' +
                                        '<a href="javascript:PrepaidExtend.showReject('+row.company_id+')">驳回</a>';
                                }else{
                                    return '<label>通过（未签代发无法签署垫发）</label></br>' +
                                        '<a href="javascript:PrepaidExtend.showReject('+row.company_id+')">驳回</a>';
                                }

                            } else {
                                return '/';
                            }
                        }
                    }
                ],
                requestParam: function () {
                    return {
                        'companyIsauth': $('#queryForm select[name=companyIsauth]').val(),
                        'companyDatumStatus': $('#queryForm select[name=companyDatumStatus]').val(),
                        'kw': $('#queryForm input[name=kw]').val()
                    }
                }
            }
        );
    },
    showReject : function (cId) {
        window.companyId = cId;
        layer.open({
            type: 2,
            title: '垫付驳回',
            shadeClose: false, //点击遮罩关闭层
            area: ['450px', '200px'],
            content: BASE_PATH + '/operate/prepaid/reject.htm',
        });
    },
    showPass:function(companyId){
            var index = layer.open({
                type: 2,
                title: '协议录入',
                maxmin: true,
                shadeClose: false, //点击遮罩关闭层
                area: ['650px', '380px'],
                content: BASE_PATH + "/protocolManage/toAddProtocolPage.htm?companyId="+companyId+"&operationShowState=2"
            });
            //窗口最大化
            layer.full(index);
    },
    onShowCompanyDetail:function(companyId) {
        var index = layer.open({
            type: 2,
            title: '企业信息',
            maxmin: true,
            shadeClose: false, //点击遮罩关闭层
            area: ['650px', '380px'],
            content: BASE_PATH + '/companyManage/companyManageDetail.htm?companyId='+companyId
        });
        //窗口最大化
        layer.full(index);
    }
}