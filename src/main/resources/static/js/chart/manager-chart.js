/**
 * Created by yuminchen on 2017/3/13.
 */
function initHotelTable(begin, end) {
    $table_hotel.bootstrapTable('destroy');
    $table_hotel.bootstrapTable({
        url: '/manager/getHotelStatistic',
        search: true,//是否搜索
        dataType: "json",//期待返回数据类型
        method: "post",//请求方式
        toolbar: '#toolbar_hotel', //工具按钮用哪个容器
        contentType : "application/x-www-form-urlencoded",
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: "asc", //排序方式
        pageNumber: 1, //初始化加载第一页，默认第一页
        pageSize: 10, //每页的记录行数（*）
        pageList: [10, 25, 50, 100], //可供选择的每页的行数（*）
        strictSearch: true,
        sidePagination: "client", //分页方式：client客户端分页，server服务端分页（*）
        showColumns: true, //是否显示所有的列
        showRefresh: true, //是否显示刷新按钮
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true, //是否启用点击选中行
        uniqueId: "ID", //每一行的唯一标识，一般为主键列
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        queryParams: function () {
            return {
                begin: begin,
                end:end
            };
        },
        queryParamsType: "limit", //参数格式,发送标准的RESTFul类型的参数请求


        columns: [

            {
                title: "全选",
                field: "select",
                checkbox: true
            },
            {
                field: 'hotelId',
                title: '客栈ID'
            },
            {
                field: 'number',
                title: '入住总量'
            },
            {
                field: 'money',
                title: '入住金额'
            }
        ]
    });
}

$(document).ready(function () {

    $table_hotel = $('#table_hotel');
    initHotelTable("2017-03-05","2017-03-25");

    $("#btn_show_chart").bind('click',show_hotel_chart);

    $("#method-hotel").val("周统计");
    $("#begin-hotel").val("2017-03-05");
    $("#end-hotel").val("2017-03-25");
    $line_hotel = echarts.init(document.getElementById('line-hotel'));
    $pie_hotel = echarts.init(document.getElementById('pie-hotel'));
    $("#begin-hotel").bind('input propertychange', get_hotel_data);
    $("#end-hotel").bind('input propertychange', get_hotel_data);

    $("#method-member").val("周统计");
    $("#begin-member").val("2017-03-05");
    $("#end-member").val("2017-03-25");
    $line_member_book = echarts.init(document.getElementById('line-member-book'));
    $pie_member_book = echarts.init(document.getElementById('pie-member-book'));
    $line_member_in = echarts.init(document.getElementById('line-member-in'));
    $pie_member_in = echarts.init(document.getElementById('pie-member-in'));
    get_member_data();
    $("#begin-member").bind('input propertychange', get_member_data);
    $("#end-member").bind('input propertychange', get_member_data);

    $("#method-finance").val("周统计");
    $("#begin-finance").val("2017-03-05");
    $("#end-finance").val("2017-03-25");
    $finance_recharge = echarts.init(document.getElementById('finance-recharge'));
    $finance_pay = echarts.init(document.getElementById('finance-pay'));
    get_finance_data();
    $("#begin-finance").bind('input propertychange', get_finance_data);
    $("#end-finance").bind('input propertychange', get_finance_data);

});

function show_hotel_chart() {
    var selections = $table_hotel.bootstrapTable('getSelections');
    if(selections.length!=1){
        showInfo("请选择查看的客栈");
        return;
    }

    if($("#begin-hotel").val().length<7||$("#end-hotel").val().length<7){
        showInfo("选择类型以及时间段");
        return;
    }

    var hotelId = selections[0].hotelId;

    $.ajax({
        url: '/manager/getHotelInLine',
        dataType: "json",
        method: "post",//请求方式
        // contentType : "application/x-www-form-urlencoded",
        data:{
            "hotelId":hotelId,
            "method":$("#method-hotel").val(),
            "begin":$("#begin-hotel").val(),
            "end":$("#end-hotel").val()
        },
        success:function(result){
            var x_data = new Array();
            var s1_data = new Array();
            var s2_data = new Array();
            var y1_max = 0;
            var y2_max = 0;
            for(var i = 0; i < result.length; i++){
                x_data.push(formatDate(result[i].date));
                if(y1_max<result[i].amount){
                    y1_max = result[i].amount;
                }
                if(y2_max<result[i].money){
                    y2_max = result[i].money;
                }

                s1_data.push(result[i].amount);
                s2_data.push(result[i].money);
            }
            line_chart($line_hotel,x_data,y1_max,y2_max,s1_data,s2_data);
        },error:function(result){
            showFailure("柱状图失败");
        }
    });

    $.ajax({
        url: '/manager/getHotelInPie',
        dataType: "json",
        method: "post",//请求方式
        // contentType : "application/x-www-form-urlencoded",
        data:{
            "hotelId":hotelId,
            "method":$("#method-hotel").val(),
            "begin":$("#begin-hotel").val(),
            "end":$("#end-hotel").val()
        },
        success:function(result){
            var data = new Array();

            var map_single = {};
            map_single['value'] = result.singleRoom;
            map_single['name'] = '单床房';
            data.push(map_single);

            var map_double = {};
            map_double['value'] = result.doubleRoom;
            map_double['name'] = '双床房';
            data.push(map_double);

            var map_big = {};
            map_big['value'] = result.bigRoom;
            map_big['name'] = '大床房';
            data.push(map_big);

            var map_whole = {};
            map_whole['value'] = result.wholeRoom;
            map_whole['name'] = '套间';
            data.push(map_whole);

            pie_chart($pie_hotel,data);
        },error:function(result){
            showFailure("饼图失败");
        }
    });



}
function get_finance_data() {
    if($("#begin-finance").val().length<7||$("#end-finance").val().length<7){
        return;
    }

    $.ajax({
        url: '/manager/getHostelFinance',
        dataType: "json",
        method: "post",//请求方式
        // contentType : "application/x-www-form-urlencoded",
        data:{
            "method":$("#method-finance").val(),
            "begin":$("#begin-finance").val(),
            "end":$("#end-finance").val()
        },
        success:function(result){
            var date = new Array();
            var pay = new Array();
            var consume = new Array();
            var consume_max = 0;

            var number = new Array();
            var amount = new Array();
            var number_max = 0;
            var amount_max = 0;

            for(var i = 0; i < result.length; i++){
                date.push(formatDate(result[i].date));
                pay.push(result[i].pay);
                consume.push(result[i].consume);
                number.push(result[i].rechargeNumber);
                amount.push(result[i].rechargeAmount);

                if(consume_max<result[i].consume){
                    consume_max = result[i].consume;
                }
                if(number_max<result[i].rechargeNumber){
                    number_max = result[i].rechargeNumber;
                }
                if(amount_max<result[i].rechargeAmount){
                    amount_max = result[i].rechargeAmount;
                }
            }
            var option_recharge = {
                tooltip: {
                    trigger: 'axis'
                },
                toolbox: {
                    feature: {
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                legend: {
                    data:['充值笔数','充值金额']
                },
                xAxis: [
                    {
                        type: 'category',
                        data: date
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        name: '充值笔数',
                        min: 0,
                        max: number_max * 6/5,
                        interval: number_max/5,
                        axisLabel: {
                            formatter: '{value} '
                        }
                    },
                    {
                        type: 'value',
                        name: '充值金额',
                        min: 0,
                        max: amount_max * 6/5,
                        interval: amount_max/5,
                        axisLabel: {
                            formatter: '{value} ￥'
                        }
                    }
                ],
                series: [
                    {
                        name:'充值笔数',
                        type:'bar',
                        data:number
                    },
                    {
                        name:'充值金额',
                        type:'bar',
                        yAxisIndex: 1,
                        data:amount
                    }
                ]
            };

            $finance_recharge.setOption(option_recharge);

            var option_pay = {
                tooltip: {
                    trigger: 'axis'
                },
                toolbox: {
                    feature: {
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                legend: {
                    data:['支付总金额','已结算金额']
                },
                xAxis: [
                    {
                        type: 'category',
                        data: date
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        name: '支付总金额',
                        min: 0,
                        max: consume_max * 6/5,
                        interval: consume_max/5,
                        axisLabel: {
                            formatter: '{value} '
                        }
                    },
                    {
                        type: 'value',
                        name: '已结算金额',
                        min: 0,
                        max: consume_max * 6/5,
                        interval: consume_max/5,
                        axisLabel: {
                            formatter: '{value} ￥'
                        }
                    }
                ],
                series: [
                    {
                        name:'支付总金额',
                        type:'bar',
                        data:consume
                    },
                    {
                        name:'已结算金额',
                        type:'bar',
                        yAxisIndex: 1,
                        data:pay
                    }
                ]
            };

            $finance_pay.setOption(option_pay);
        },error:function(result){
            showFailure("财务图失败");
        }
    });
}

function get_member_data(){
    if($("#begin-member").val().length<7||$("#end-member").val().length<7){
        return;
    }
    $.ajax({
        url: '/manager/getAllMemberBookLine',
        dataType: "json",
        method: "post",//请求方式
        // contentType : "application/x-www-form-urlencoded",
        data:{
            "method":$("#method-member").val(),
            "begin":$("#begin-member").val(),
            "end":$("#end-member").val()
        },
        success:function(result){
            var x_data = new Array();
            var s1_data = new Array();
            var s2_data = new Array();
            var y1_max = 0;
            var y2_max = 0;
            for(var i = 0; i < result.length; i++){
                x_data.push(formatDate(result[i].date));
                if(y1_max<result[i].amount){
                    y1_max = result[i].amount;
                }
                if(y2_max<result[i].money){
                    y2_max = result[i].money;
                }

                s1_data.push(result[i].amount);
                s2_data.push(result[i].money);
            }
            line_chart($line_member_book,x_data,y1_max,y2_max,s1_data,s2_data);
        },error:function(result){
            showFailure("柱状图失败");
        }
    });

    $.ajax({
        url: '/manager/getAllMemberBookPie',
        dataType: "json",
        method: "post",//请求方式
        // contentType : "application/x-www-form-urlencoded",
        data:{
            "method":$("#method-member").val(),
            "begin":$("#begin-member").val(),
            "end":$("#end-member").val()
        },
        success:function(result){
            var data = new Array();

            var map_single = {};
            map_single['value'] = result.singleRoom;
            map_single['name'] = '单床房';
            data.push(map_single);

            var map_double = {};
            map_double['value'] = result.doubleRoom;
            map_double['name'] = '双床房';
            data.push(map_double);

            var map_big = {};
            map_big['value'] = result.bigRoom;
            map_big['name'] = '大床房';
            data.push(map_big);

            var map_whole = {};
            map_whole['value'] = result.wholeRoom;
            map_whole['name'] = '套间';
            data.push(map_whole);

            pie_chart($pie_member_book,data);
        },error:function(result){
            showFailure("饼图失败");
        }
    });

    $.ajax({
        url: '/manager/getAllMemberInLine',
        dataType: "json",
        method: "post",//请求方式
        // contentType : "application/x-www-form-urlencoded",
        data:{
            "method":$("#method-member").val(),
            "begin":$("#begin-member").val(),
            "end":$("#end-member").val()
        },
        success:function(result){
            var x_data = new Array();
            var s1_data = new Array();
            var s2_data = new Array();
            var y1_max = 0;
            var y2_max = 0;
            for(var i = 0; i < result.length; i++){
                x_data.push(formatDate(result[i].date));
                if(y1_max<result[i].amount){
                    y1_max = result[i].amount;
                }
                if(y2_max<result[i].money){
                    y2_max = result[i].money;
                }

                s1_data.push(result[i].amount);
                s2_data.push(result[i].money);
            }
            line_chart($line_member_in,x_data,y1_max,y2_max,s1_data,s2_data);
        },error:function(result){
            showFailure("柱状图失败");
        }
    });

    $.ajax({
        url: '/manager/getAllMemberInPie',
        dataType: "json",
        method: "post",//请求方式
        // contentType : "application/x-www-form-urlencoded",
        data:{
            "method":$("#method-member").val(),
            "begin":$("#begin-member").val(),
            "end":$("#end-member").val()
        },
        success:function(result){
            var data = new Array();

            var map_single = {};
            map_single['value'] = result.singleRoom;
            map_single['name'] = '单床房';
            data.push(map_single);

            var map_double = {};
            map_double['value'] = result.doubleRoom;
            map_double['name'] = '双床房';
            data.push(map_double);

            var map_big = {};
            map_big['value'] = result.bigRoom;
            map_big['name'] = '大床房';
            data.push(map_big);

            var map_whole = {};
            map_whole['value'] = result.wholeRoom;
            map_whole['name'] = '套间';
            data.push(map_whole);

            pie_chart($pie_member_in,data);
        },error:function(result){
            showFailure("饼图失败");
        }
    });

}

function get_hotel_data(){
    var selections = $table_hotel.bootstrapTable('getSelections');
    if(selections.length!=1){
        return;
    }
    if($("#begin-hotel").val().length<7||$("#end-hotel").val().length<7){
        return;
    }
    show_hotel_chart();

    initHotelTable($("#begin-hotel").val(),$("#end-hotel").val());


}

function line_chart(chart, x_data,y1_max,y2_max,s1_data,s2_data){
    var option = {
        tooltip: {
            trigger: 'axis'
        },
        toolbox: {
            feature: {
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        legend: {
            data:['次数','消费']
        },
        xAxis: [
            {
                type: 'category',
                data: x_data
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '次数',
                min: 0,
                max: y1_max * 6/5,
                interval: y1_max/5,
                axisLabel: {
                    formatter: '{value} '
                }
            },
            {
                type: 'value',
                name: '消费',
                min: 0,
                max: y2_max * 6/5,
                interval: y2_max/5,
                axisLabel: {
                    formatter: '{value} ￥'
                }
            }
        ],
        series: [
            {
                name:'次数',
                type:'bar',
                data:s1_data
            },
            {
                name:'消费',
                type:'bar',
                yAxisIndex: 1,
                data:s2_data
            }
        ]
    };
    chart.setOption(option);

}

function pie_chart(chart,value) {

    var option = {
        title : {
            x:'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: ['单床房','双床房','大床房','套间']
        },
        series : [
            {
                name: '来源',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:value,
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };

    chart.setOption(option);

}
