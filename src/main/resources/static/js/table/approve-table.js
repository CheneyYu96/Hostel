/**
 * Created by yuminchen on 2017/3/10.
 */

function initApproveTable() {

    $table_approve.bootstrapTable({
        url: '/manager/getApprove',
        search: true,//是否搜索
        dataType: "json",//期待返回数据类型
        // method: "post",//请求方式
        toolbar: '#toolbar_approve', //工具按钮用哪个容器
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
            if (row.type == "开店申请") {
                strclass = 'success';//还有一个active
            }
            else if (row.type == "信息修改") {
                strclass = 'info';
            }
            else {
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
                field: 'type',
                title: '审批类型'
            }
        ]
    });
}

function initRoomTable(hotelId) {
    $table_room.bootstrapTable('destroy');
    $table_room.bootstrapTable({
        url: '/manager/getHotelRoomInApprove',
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
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true, //是否启用点击选中行
        uniqueId: "ID", //每一行的唯一标识，一般为主键列
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        queryParams: function () {
            return {
                hotelId: hotelId
            };
        },
        queryParamsType: "limit", //参数格式,发送标准的RESTFul类型的参数请求

        columns: [

            {
                field: 'id',
                title: '房间ID'
            },
            {
                field: 'roomNumber',
                title: '房间号'
            },
            {
                field: 'type',
                title: '房间类型'
            },
            {
                field: 'prize',
                title: '价格'
            }
        ]
    });
}

$(document).ready(function () {
    $table_approve = $('#table_approve');
    initApproveTable();

    $table_room = $('#table_room');

    $open = $("#open");
    $info = $("#info");
    $room = $("#room");


    $('#btn_show_approve').bind('click', show_approve);

    $("#btn_pass_approve").bind("click",pass_approve);

    $("#btn_show").bind("click",showPlan);

    $("#btn_delete").bind("click",delValue);
    $("#deletePlan").bind("click",delPlan);

    $("#delete").keydown(function() {
        if (event.keyCode == "13"){
            $('#deleteRoom').click();
        }
    });

});

function show_approve() {
    var selections = $table_approve.bootstrapTable('getSelections');
    if(selections.length != 1){
        showInfo("请选择一行");
        return;
    }
    var content = selections[0];
    var apptype = content.type;
    if(apptype=="开店申请"){
        $.ajax({
            url: '/manager/getHotelInfoInApprove',
            dataType: "json",
            method: "post",//请求方式
            data:{
                "approveId":content.id
            },
            success:function(result){
                $open.show();
                $info.show();
                $room.hide();

                $("#hotelId").val(formatId(result.id));
                $("#hotelName").val(result.name);
                $("#hotelAddress").val(result.address);
                initRoomTable(result.id);

            },error:function(result){
            }
        })
    }
    else if(apptype=="信息修改"){
        $.ajax({
            url: '/manager/getHotelInfoInApprove',
            dataType: "json",
            method: "post",//请求方式
            data:{
                "approveId":content.id
            },
            success:function(result){
                $open.hide();
                $info.show();
                $room.hide();

                $("#hotelId").val(formatId(result.id));
                $("#hotelName").val(result.name);
                $("#hotelAddress").val(result.address);

            },error:function(result){
            }
        })
    }
    else if(apptype=="房间修改"){
        $.ajax({
            url: '/manager/getRoomInApprove',
            dataType: "json",
            method: "post",//请求方式
            data:{
                "approveId":content.id
            },
            success:function(result){
                $open.hide();
                $info.hide();
                $room.show();

                $("#roomId").val(result.id);
                $("#hotel_name").val(content.hotelName);
                $("#roomNumber").val(result.roomNumber);
                $("#type").val(result.type);
                $("#prize").val(result.prize);

            },error:function(result){
            }
        })
    }

    $("#show_approve").modal('show');

}

function pass_approve() {
    var selections = $table_approve.bootstrapTable('getSelections');
    if(selections.length<1){
        showInfo("请选择审批的行");
        return;
    }
    var isSuccess = true;
    for(var i = 0; i<selections.length&&isSuccess;i++){
        $.ajax({
            url: '/manager/approveItem',
            dataType: "json",
            method: "post",//请求方式
            data:{
                "approveId":selections[i].id
            },
            success:function(result){
                if(!result.success) {
                    isSuccess=false;
                }
                $table_approve.bootstrapTable('refresh', {url: '/manager/getApprove'});

            },error:function(result){
                isSuccess=false;
            }
        })
    }
    if(isSuccess){
        showSuccess("审批成功");
    }
}
function clearAdd() {
    $("#name").val("");
    $("#des").val("");
    $("#type").val("");
    $("#datetimepicker1").val("");
    $("#datetimepicker2").val("");
    $("#discount").val("");
    $addRoomTable.bootstrapTable('destroy');

}

function addPlan() {
    $.ajax({
        url: '/hotel/addPlan',
        dataType: "json",
        method: "post",//请求方式
        data:{
            "name":$("#name").val(),
            "des":$("#des").val(),
            "type":$("#type").val(),
            "begin":$("#datetimepicker1").val(),
            "end":$("#datetimepicker2").val(),
            "discount":$("#discount").val()
        },
        success:function(result){
            if(result.success){
                showSuccess("添加成功");
                $table.bootstrapTable('refresh', {url: '/hotel/getPlan'});
                clearAdd();
            }
            else {
                showFailure("添加失败, " + result.info);
            }

        },error:function(result){
            showFailure("添加失败");
        }

    });
}


function showPlan() {
    var selections = $table.bootstrapTable('getSelections');
    if(selections.length != 1){
        // $("#closeModal").click();
        showInfo("请选择一行");
        return;
    }
    var content = selections[0];

    $("#nameShow").val(content.name);
    $("#desShow").val(content.des);
    $("#datetimebegin").val(formatDate(content.beginDate));
    $("#datetimeend").val(formatDate(content.endDate));
    $("#typeShow").val(content.type);
    $("#discountShow").val(content.discount);
    roomTable($showRoomTable,showRoomParam);

    $("#show").modal('show');
}

function changeRoom(){

    if(!isNaN($('#discount').val())){
        roomTable($addRoomTable,addRoomParam);
    }
}

function delValue() {
    var selections = $table.bootstrapTable('getSelections');
    if(selections.length==0){
        showInfo("请选择要删除的行");
    }
    else {
        $("#delete").modal('show');
    }
}

function delPlan(){
    var selections = $table.bootstrapTable('getSelections');
    var isSuccess = true;
    for(var i = 0; i<selections.length&&isSuccess;i++){
        $.ajax({
            url: '/hotel/delPlan',
            dataType: "json",
            method: "post",//请求方式
            data:{
                "planId":selections[i].id
            },
            success:function(result){
                // console.log(result);
                $table.bootstrapTable('refresh', {url: '/hotel/getPlan'});
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
