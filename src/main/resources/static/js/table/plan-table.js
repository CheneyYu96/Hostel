/**
 * Created by yuminchen on 2017/3/10.
 */

function initTable() {

    $table.bootstrapTable({
        url: '/hotel/getPlan',
        search: true,//是否搜索
        dataType: "json",//期待返回数据类型
        // method: "post",//请求方式
        toolbar: '#toolbar', //工具按钮用哪个容器
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

function roomTable(tablevar, query) {
    tablevar.bootstrapTable('destroy');
    tablevar.bootstrapTable({
        url: '/hotel/getRelateRoom',
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
        queryParams: query, //参数
        queryParamsType: "limit", //参数格式,发送标准的RESTFul类型的参数请求

        columns: [

            {
                field: 'roomNumber',
                title: '房间号'
            },
            {
                field: 'type',
                title: '房间类型'
            },
            {
                field: 'originPrize',
                title: '原价'
            },
            {
                field: 'nowPrize',
                title: '现价'
            },
            {
                field: 'discount',
                title: '折扣'
            }
        ]
    });
}

addRoomParam = function (params){
    var temp = {
        type: $("#type").val(),
        discount: $("#discount").val()
    };

    return temp;
};


function showRoomParam() {
    var selections = $table.bootstrapTable('getSelections');

    var temp = {
        type: selections[0].type,
        discount: selections[0].discount
    };
    return temp;
}

$(document).ready(function () {


    $table = $('#table');
    $addRoomTable = $('#addRoomTable');
    $showRoomTable = $('#showRoomTable');
    initTable();

    $('#discount').bind('input propertychange', changeRoom);

    $("#addPlan").bind("click",addPlan);

    $("#btn_show").bind("click",showPlan);

    $("#btn_delete").bind("click",delValue);
    $("#deletePlan").bind("click",delPlan);

    $("#delete").keydown(function() {
        if (event.keyCode == "13"){
            $('#deleteRoom').click();
        }
    });

});
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
            console.log(result);
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
