/**
 * Created by yuminchen on 2017/3/15.
 */

function initOrderTable() {

    $table_order.bootstrapTable({
        url: '/member/getOrder',
        search: true,//是否搜索
        dataType: "json",//期待返回数据类型
        // method: "post",//请求方式
        toolbar: '#toolbar_mine', //工具按钮用哪个容器
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
        rowStyle: function (row, index) {
            //这里有5个取值代表5中颜色['active', 'success', 'info', 'warning', 'danger'];
            var strclass = "";
            if (row.status == "完成") {
                strclass = 'success';//还有一个active
            }
            else if (row.status == "入住") {
                strclass = 'info';
            }
            else if (row.status == "预订") {
                strclass = 'active';
            }

            return {classes: strclass}
        },

        columns: [

            {
                title: "全选",
                field: "select",
                checkbox: true
            },
            {
                field: 'id',
                title: 'ID'
            },
            {
                field: 'hotelName',
                title: '客栈名'
            },
            {
                field: 'roomNumber',
                title: '预定房间号'
            },
            {
                field: 'type',
                title: '房间类型'
            },
            {
                field: 'begin',
                title: '开始日期',
                formatter:formatDate
            },
            {
                field: 'end',
                title: '结束日期',
                formatter:formatDate
            },
            {
                field: 'pay',
                title: '支付金额'
            },
            {
                field: 'status',
                title: '当前状态'
            }
        ]
    });
}

function initHotelTable() {

    $table_hotel.bootstrapTable({
        url: '/member/getHotel',
        search: true,//是否搜索
        dataType: "json",//期待返回数据类型
        // method: "post",//请求方式
        toolbar: '#toolbar_book', //工具按钮用哪个容器
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

        columns: [

            {
                title: "全选",
                field: "select",
                checkbox: true
            },
            {
                field: 'id',
                title: 'ID'
            },
            {
                field: 'name',
                title: '客栈名'
            },
            {
                field: 'address',
                title: '客栈地址'
            },
            {
                field: 'plan',
                title: '活动数量',
                formatter:function(value,row,index){
                    return value.length;
                }
            }
        ]
    });
}

function initPlanTable() {
    $table_plan.bootstrapTable('destroy');
    $table_plan.bootstrapTable({
        url: '/hotel/getPlanByMember',
        search: true,//是否搜索
        dataType: "json",//期待返回数据类型
        method: "post",//请求方式
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
        queryParams:function (params){
            var temp = {
                "hotelId": hotelId
            };
            return temp;
        },
        columns: [

            {
                title: "全选",
                field: "select",
                checkbox: true
            },
            {
                field: 'name',
                title: '计划名'
            },
            {
                field: 'des',
                title: '计划描述'
            },
            {
                field: 'beginDate',
                title: '开始日期',
                formatter:formatDate
            },
            {
                field: 'endDate',
                title: '结束日期',
                formatter:formatDate
            },
            {
                field: 'type',
                title: '房间类型'
            },
            {
                field: 'discount',
                title: '折扣',
                formatter:function(value,row,index){
                    return value+'%';
                }
            }
        ]
    });
}


$(document).ready(function () {


    $table_order = $('#table_order');
    initOrderTable();

    $table_hotel = $('#table_hotel');
    initHotelTable();

    $table_plan = $('#table_plan');

    $('#btn_delete').bind('click', delValue);
    $('#deleteOrder').bind('click', del_order);

    $("#btn_show").bind("click",showValue);

    $("#btn_book").bind("click",bookValue);
    $("#make-order").bind("click",make_order);
    $("#roomLine").hide();


    $('#begin').bind('input propertychange', count_pay);
    $('#end').bind('input propertychange', count_pay);
    // $("#delete").keydown(function() {
    //     if (event.keyCode == "13"){
    //         $('#deleteRoom').click();
    //     }
    // });
    hotelId = 0;
    prize=0;

});
function clearAdd() {
    $("#roomNumber").val("");
    $("#type").val("");
    $("#begin").val("");
    $("#end").val("");
    $("#prize").val("");
    $("#roomLine").hide();

}

function make_order() {
    if($("#roomNumber").val().length<3){
        return;
    }
    $.ajax({
        url: '/member/makeOrder',
        dataType: "json",
        method: "post",//请求方式
        data:{
            "hotelId":hotelId,
            "roomNumber":$("#roomNumber").val(),
            "type":$("#type").val(),
            "begin":$("#begin").val(),
            "end":$("#end").val(),
            "pay":prize
        },
        success:function(result){
            if(result.info!=null&&result.info.length>1){
                showFailure("预定失败, " + result.info);
            }
            else {
                showSuccess("预定成功，预定房间号为：" + result.roomNumber);
                $table_order.bootstrapTable('refresh', {url: '/member/getOrder'});
                clearAdd();
            }

        },error:function(result){
            showFailure("预定失败");
        }

    });
}


function count_pay(){

    if($("#type").val().length<2||$("#begin").val()<5||$("#end").val()<5){
        return;
    }
    $.ajax({
        url: '/member/countPay',
        dataType: "json",
        method: "post",//请求方式
        data:{
            "hotelId":hotelId,
            "type":$("#type").val(),
            "begin":$("#begin").val(),
            "end":$("#end").val()
        },
        success:function(result){
            if(result.errorInfo!=null&&result.errorInfo.length>3){
                $("#roomLine").hide();
                showFailure(result.errorInfo);
            }
            else {
                $("#prize").val("原价"+result.originPrize+"，折后"+result.nowPrize);
                $("#roomNumber").val(result.roomNumber);
                $("#roomLine").show();
                prize = result.nowPrize;
            }

        },error:function(result){
            showFailure("失败");
        }
    })

}

function delValue() {
    var selections = $table_order.bootstrapTable('getSelections');
    if(selections.length==0){
        showInfo("请选择要删除的行");
    }
    else {
        $("#delete").modal('show');
    }
}

function showValue() {
    var selections = $table_hotel.bootstrapTable('getSelections');
    if(selections.length!=1){
        showInfo("请选择一行");
        return;
    }
    var content = selections[0];
    hotelId = content.id;

    $("#hotel_name").val(content.name);
    $("#hotel_address").val(content.address);
    initPlanTable();

    $("#show").modal('show');
}

function bookValue() {
    var selections = $table_hotel.bootstrapTable('getSelections');
    if(selections.length!=1){
        showInfo("请选择一个需要预定的酒店");
    }
    else {
        hotelId = selections[0].id;
        $("#name").val(selections[0].name);
        $("#bookRoom").modal('show');
    }
}

function del_order(){
    var selections = $table_order.bootstrapTable('getSelections');
    var isSuccess = true;
    for(var i = 0; i<selections.length&&isSuccess;i++){
        $.ajax({
            url: '/member/cancelOrder',
            dataType: "json",
            method: "post",//请求方式
            data:{
                "orderId":selections[i].id
            },
            success:function(result){
                // console.log(result);
                $table_order.bootstrapTable('refresh', {url: '/member/getOrder'});
                if(!result.success) {
                    isSuccess=false;
                }

            },error:function(result){
            }
        })
    }
    if(isSuccess){
        showSuccess("删除成功");
    }

}