/**
 * Created by yuminchen on 2017/3/13.
 */

$(document).ready(function () {

    $("#method-book").val("日统计");
    $("#begin-book").val("2017-03-05");
    $("#end-book").val("2017-03-25");
    $book_line = echarts.init(document.getElementById('book-line'));
    $book_pie = echarts.init(document.getElementById('book-pie'));
    get_book_data();
    $("#begin-book").bind('input propertychange', get_book_data);
    $("#end-book").bind('input propertychange', get_book_data);

    $("#method-in").val("日统计");
    $("#begin-in").val("2017-03-05");
    $("#end-in").val("2017-03-25");
    $in_line = echarts.init(document.getElementById('in-line'));
    $in_pie = echarts.init(document.getElementById('in-pie'));
    get_in_data();
    $("#begin-in").bind('input propertychange', get_in_data);
    $("#end-in").bind('input propertychange', get_in_data);

    $("#method-finance").val("日统计");
    $("#begin-finance").val("2017-03-05");
    $("#end-finance").val("2017-03-25");
    $finance_line = echarts.init(document.getElementById('finance-line'));
    get_finance_data();
    $("#begin-finance").bind('input propertychange', get_finance_data);
    $("#end-finance").bind('input propertychange', get_finance_data);

});
function get_finance_data() {
    if($("#begin-finance").val().length<7||$("#end-finance").val().length<7){
        return;
    }
    $.ajax({
        url: '/hotel/getFinance',
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
            var y_max = 0;
            for(var i = 0; i < result.length; i++){
                date.push(formatDate(result[i].begin));
                pay.push(result[i].pay);
                if(y_max<result[i].pay){
                    y_max = result[i].pay;
                }
            }
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
                    data:['总营销额']
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
                        name: '总营销额',
                        min: 0,
                        max: y_max * 6/5,
                        interval: y_max/5,
                        axisLabel: {
                            formatter: '{value} ￥'
                        }
                    }
                ],
                series: [
                    {
                        name:'总营销额',
                        type:'bar',
                        data:pay
                    }
                ]
            };

            $finance_line.setOption(option);
        },error:function(result){
            showFailure("柱状图失败");
        }
    });
}
function get_in_data(){
    if($("#begin-in").val().length<7||$("#end-in").val().length<7){
        return;
    }
    $.ajax({
        url: '/hotel/getInLine',
        dataType: "json",
        method: "post",//请求方式
        // contentType : "application/x-www-form-urlencoded",
        data:{
            "method":$("#method-in").val(),
            "begin":$("#begin-in").val(),
            "end":$("#end-in").val()
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
            line_chart($in_line,x_data,y1_max,y2_max,s1_data,s2_data);
        },error:function(result){
            showFailure("柱状图失败");
        }
    });

    $.ajax({
        url: '/hotel/getInPie',
        dataType: "json",
        method: "post",//请求方式
        // contentType : "application/x-www-form-urlencoded",
        data:{
            "method":$("#method-in").val(),
            "begin":$("#begin-in").val(),
            "end":$("#end-in").val()
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

            pie_chart($in_pie,data);
        },error:function(result){
            showFailure("饼图失败");
        }
    });

}
function get_book_data(){
    // console.log($("#method-book").val());
    if($("#begin-book").val().length<7||$("#end-book").val().length<7){
        return;
    }
    $.ajax({
        url: '/hotel/getBookLine',
        dataType: "json",
        method: "post",//请求方式
        // contentType : "application/x-www-form-urlencoded",
        data:{
            "method":$("#method-book").val(),
            "begin":$("#begin-book").val(),
            "end":$("#end-book").val()
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
            line_chart($book_line,x_data,y1_max,y2_max,s1_data,s2_data);
        },error:function(result){
            showFailure("柱状图失败");
        }
    });

    $.ajax({
        url: '/hotel/getBookPie',
        dataType: "json",
        method: "post",//请求方式
        // contentType : "application/x-www-form-urlencoded",
        data:{
            "method":$("#method-book").val(),
            "begin":$("#begin-book").val(),
            "end":$("#end-book").val()
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

            pie_chart($book_pie,data);
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
            data:['数量','营销额']
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
                name: '数量',
                min: 0,
                max: y1_max * 6/5,
                interval: y1_max/5,
                axisLabel: {
                    formatter: '{value} '
                }
            },
            {
                type: 'value',
                name: '营销额',
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
                name:'数量',
                type:'bar',
                data:s1_data
            },
            {
                name:'营销额',
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
