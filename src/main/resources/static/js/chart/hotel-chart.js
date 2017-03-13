/**
 * Created by yuminchen on 2017/3/13.
 */

$(document).ready(function () {
    $book_line = echarts.init(document.getElementById('book-line'));
    $book_line.setOption(option_line);

    $book_pie = echarts.init(document.getElementById('book-pie'));
    $book_pie.setOption(option_pie);

    $("#begin-book").bind('input propertychange', get_book_data());
    $("#end-book").bind('input propertychange', get_book_data());

});

function get_book_data(){
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
                max: y1_max,
                interval: y1_max/5,
                axisLabel: {
                    formatter: '{value} '
                }
            },
            {
                type: 'value',
                name: '营销额',
                min: 0,
                max: y2_max,
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

option_line = {
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
            data: ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
        }
    ],
    yAxis: [
        {
            type: 'value',
            name: '数量',
            min: 0,
            max: 50,
            interval: 10,
            axisLabel: {
                formatter: '{value} '
            }
        },
        {
            type: 'value',
            name: '营销额',
            min: 0,
            max: 5000,
            interval: 1000,
            axisLabel: {
                formatter: '{value} ￥'
            }
        }
    ],
    series: [
        {
            name:'数量',
            type:'bar',
            data:[2.0, 4.9, 7.0, 3.2, 5.6, 7.7, 15.6, 16.2, 32.6, 20.0, 6.4, 3.3]
        },
        {
            name:'营销额',
            type:'bar',
            yAxisIndex: 1,
            data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
        },

    ]
};

option_pie = {
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
        data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
    },
    series : [
        {
            name: '来源',
            type: 'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:[
                {value:335, name:'直接访问'},
                {value:310, name:'邮件营销'},
                {value:234, name:'联盟广告'},
                {value:135, name:'视频广告'},
                {value:158, name:'搜索引擎'}
            ],
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
