/**
 * Created by yuminchen on 2017/3/9.
 */
function initTable() {

    $table_room.bootstrapTable({
        url: '/hotel/getRooms',
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


        rowStyle: function (row, index) {
            //这里有5个取值代表5中颜色['active', 'success', 'info', 'warning', 'danger'];
            var strclass = "";
            if (row.status == "已批准") {
                strclass = 'success';//还有一个active
            }
            else if (row.status == "待审批") {
                strclass = 'warning';
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
            },
            {
                field: 'status',
                title: '审批状态'
            }
        ]
    });
}

function editInfo() {
    $.ajax({
        url: '/hotel/editInfo',
        dataType: "json",
        method: "post",//请求方式
        data:{
            "name":$("#hotelName").val(),
            "address":$("#address").val()
        },
        success:function(result){
            console.log(result);
            if(result.success){
                showSuccess("修改成功");
            }
            else {
                showFailure("修改失败");
            }

        },error:function(result){
            showFailure("修改失败");
        }

    });
}

$(document).ready(function () {

    $("#editInfo").bind("click",editInfo);

    $table_room = $('#table-room');
    //调用函数，初始化表格
    initTable();
    $("#addRoom").bind("click",addRoom);

    $("#modifyRoom").bind("click",modifyRoom);
    $("#btn_edit").bind("click",editValue);

    $("#btn_delete").bind("click",delValue);
    $("#deleteRoom").bind("click",delRoom);

    $("#add").keydown(function() {
        if (event.keyCode == "13"){
            $('#addRoom').click();
        }
    });

    $("#edit").keydown(function() {
        if (event.keyCode == "13"){
            $('#modifyRoom').click();
        }
    });

    $("#delete").keydown(function() {
        if (event.keyCode == "13"){
            $('#deleteRoom').click();
        }
    });

});

function addRoom() {
    $.ajax({
        url: '/hotel/addRoom',
        dataType: "json",
        method: "post",//请求方式
        data:{
            "type":$("#type").val(),
            "roomNumber":$("#roomNumber").val(),
            "prize":$("#prize").val()
        },
        success:function(result){
            console.log(result);
            if(result.success){
                showSuccess("添加成功");
                $table.bootstrapTable('refresh', {url: '/hotel/getRooms'});
            }
            else {
                showFailure("添加失败, " + result.info);
            }

        },error:function(result){
            showFailure("添加失败");
        }

    });
}

function modifyRoom() {
    var selections = $table.bootstrapTable('getSelections');
    console.log("id" + selections[0].id);
    console.log($("#prize").val());
    $.ajax({
        url: '/hotel/modifyRoom',
        dataType: "json",
        method: "post",//请求方式
        data:{
            "roomId":selections[0].id,
            "type":$("#typeEdit").val(),
            "roomNumber":$("#roomNumberEdit").val(),
            "prize":$("#prizeEdit").val()
        },
        success:function(result){
            // console.log('tip: '+$("#tip").text());

            if(result.success){
                showSuccess("修改成功");
                $table.bootstrapTable('refresh', {url: '/hotel/getRooms'});
            }
            else {
                showFailure("修改失败, "+result.info);
            }

        },error:function(result){
            showFailure("修改失败");
        }

    });
}

function delRoom(){
    var selections = $table.bootstrapTable('getSelections');
    var isSuccess = true;
    for(var i = 0; i<selections.length&&isSuccess;i++){
        $.ajax({
            url: '/hotel/delRoom',
            dataType: "json",
            method: "post",//请求方式
            data:{
                "roomId":selections[i].id
            },
            success:function(result){
                // console.log(result);
                $table.bootstrapTable('refresh', {url: '/hotel/getRooms'});
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

function editValue() {
    var selections = $table.bootstrapTable('getSelections');
    if(selections.length != 1){
        // $("#closeModal").click();
        showInfo("请选择一行");
    }
    else {
        var content = selections[0];
        $("#roomNumberEdit").val(content.roomNumber);
        $("#prizeEdit").val(content.prize);
        $("#typeEdit").val(content.type);
        $("#edit").modal('show');
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
