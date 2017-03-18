/**
 * Created by yuminchen on 2017/3/13.
 */

$(document).ready(function () {

    $("#method-hotel").val("周统计");
    $("#begin-hotel").val("2017-03-05");
    $("#end-hotel").val("2017-03-25");
    $line_hotel = echarts.init(document.getElementById('line-hotel'));
    $pie_hotel = echarts.init(document.getElementById('pie-hotel'));
    get_book_data();
    $("#begin-book").bind('input propertychange', get_hotel_data);
    $("#end-book").bind('input propertychange', get_hotel_data);

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
            var pay_max = 0;
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
                if(pay_max<result[i].pay){
                    pay_max = result[i].pay;
                }
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
                        max: pay_max * 6/5,
                        interval: pay_max/5,
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
    if($("#begin-hotel").val().length<7||$("#end-hotel").val().length<7){
        return;
    }
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
