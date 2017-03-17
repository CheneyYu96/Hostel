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

function initPayTable() {

    $table_pay.bootstrapTable({
        url: '/manager/getPay',
        search: true,//是否搜索
        dataType: "json",//期待返回数据类型
        // method: "post",//请求方式
        toolbar: '#toolbar_pay', //工具按钮用哪个容器
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
                field: 'hotelId',
                title: '客栈ID'
            },
            {
                field: 'name',
                title: '客栈名'
            },
            {
                field: 'payIndex',
                title: '待结算数',
                formatter:function(value,row,index){
                    if(value!=null) {
                        return value.length;
                    }

                }
            }
        ]
    });
}

function initPayMemberTable(hotelId) {
    $table_pay_member.bootstrapTable('destroy');
    $table_pay_member.bootstrapTable({
        url: '/manager/getPayWithMember',
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
                title: '结算单ID'
            },
            {
                field: 'memberId',
                title: '会员ID'
            },
            {
                field: 'name',
                title: '会员名'
            },
            {
                field: 'pay',
                title: '结算金额'
            },
            {
                field: 'isOrder',
                title: '结算类型',
                formatter:function(value,row,index){
                    if(value){
                        return "订单结算";
                    }
                    return "入住单结算";
                }
            }
        ]
    });
}

$(document).ready(function () {
    $table_approve = $('#table_approve');
    initApproveTable();

    $table_pay = $('#table_pay');
    initPayTable();

    $table_room = $('#table_room');
    $table_pay_member = $('#table_pay_member');

    $open = $("#open");
    $info = $("#info");
    $room = $("#room");

    $('#btn_show_approve').bind('click', show_approve);
    $("#btn_pass_approve").bind("click",pass_approve);

    $('#btn_show_pay').bind('click', show_pay);
    $("#btn_pay").bind("click",pay_item);


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

function show_pay() {
    var selections = $table_pay.bootstrapTable('getSelections');
    if(selections.length != 1){
        showInfo("请选择一行");
        return;
    }
    var content = selections[0];

    $("#hotel_id_pay").val(content.hotelId);
    $("#hotel_name_pay").val(content.name);
    initPayMemberTable(content.hotelId);
    $("#show_pay").modal('show');

}

function pay_item() {
    var selections = $table_pay.bootstrapTable('getSelections');
    if(selections.length<1){
        showInfo("请选择结算的客栈");
        return;
    }
    var isSuccess = true;
    for(var i = 0; i<selections.length&&isSuccess;i++){
        for(var j = 0; j<selections[i].payIndex.length; j++){
            $.ajax({
                url: '/manager/payItem',
                dataType: "json",
                method: "post",//请求方式
                data:{
                    "payId":selections[i].payIndex[j]
                },
                success:function(result){
                    if(!result.success) {
                        isSuccess=false;
                    }
                    $table_pay.bootstrapTable('refresh', {url: '/manager/getPay'});

                },error:function(result){
                    isSuccess=false;
                }
            })
        }
    }

    if(isSuccess){
        showSuccess("结算成功");
    }
}



