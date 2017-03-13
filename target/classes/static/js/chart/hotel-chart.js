/**
 * Created by yuminchen on 2017/3/13.
 */

$(document).ready(function () {
    $book_line = echarts.init(document.getElementById('book-line'));
    $book_line.setOption(option_line);

    $book_pie = echarts.init(document.getElementById('book-pie'));
    $book_pie.setOption(option_pie);

});

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
            name: '访问来源',
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
