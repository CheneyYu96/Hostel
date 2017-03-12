/**
 * Created by yuminchen on 2017/3/11.
 */
function initInTable() {

    $table_in.bootstrapTable({
        url: '/hotel/getInRecord',
        search: true,//是否搜索
        dataType: "json",//期待返回数据类型
        // method: "post",//请求方式
        toolbar: '#toolbar_in', //工具按钮用哪个容器
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
                field: 'nameList',
                title: '客户名',
                formatter:function(value,row,index){
                    var result = "";
                   for (var i = 0; i<value.length; i++){
                       result = result + " " + value[i];
                   }
                   return result;
                }
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
                field: 'payByCard',
                title: '是否会员卡支付',
                formatter:function(value,row,index){
                    if(value){
                        return "是";
                    }
                    return "否";
                }
            }
        ]
    });
}

function initOutTable() {

    $table_out.bootstrapTable({
        url: '/hotel/getOutRecord',
        search: true,//是否搜索
        dataType: "json",//期待返回数据类型
        // method: "post",//请求方式
        toolbar: '#toolbar_out', //工具按钮用哪个容器
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
                field: 'roomNumber',
                title: '房间号'
            },
            {
                field: 'type',
                title: '房间类型'
            },
            {
                field: 'date',
                title: '开始日期',
                formatter:formatDate
            }
        ]
    });
}


$(document).ready(function () {

    $table_in = $("#table_in");
    initInTable();
    $table_out = $("#table_out");
    initOutTable();

    $("#orderLine").hide();
    $("#addGuest").bind("click", add_guest);
    $("#minusGuest").bind("click", minus_guest);
    $("#addIn").bind("click", add_in);
    $('#roomNumber').bind('input propertychange', showPrize);
    $('#begin').bind('input propertychange', showPrize);
    $('#end').bind('input propertychange', showPrize);
    $('#cardId').bind('input propertychange', showPrize);


    $guestNumber = 1;
    $prize = 0;
});

function addIn() {
    var map = {};
    for(var i = 1; i<=$guestNumber;i++){
        if($("#isMember"+i)=="会员"){
            map[$("#guest"+i)]="1";
        }
        else {
            map[$("#guest"+i)]="0";

        }
    }
    if($("#isOrder")=="是"){
        $.ajax({
            url: '/hotel/addRecordByOrder',
            dataType: "json",
            method: "post",//请求方式
            data:{
                "orderId":$("#orderId").val(),
                "nameMap":map
            },
            success:function(result){
                console.log(result);
                if(result.success){
                    showSuccess("添加成功");
                    $table_in.bootstrapTable('refresh', {url: '/hotel/getInRecord'});
                }
                else {
                    showFailure("添加失败, " + result.info);
                }

            },error:function(result){
                showFailure("添加失败");
            }
        });
    }
    else {
        $.ajax({
            url: '/hotel/addInRecord',
            dataType: "json",
            method: "post",//请求方式
            data:{
                "nameMap":map,

                "roomNumber":$("#roomNumber").val(),
                "cardId":$("#cardId").val(),
                "type":$("#type").val(),
                "begin":$("#begin").val(),
                "end":$("#end").val(),
                "pay":$prize,
                "payByCard":convert2Bool($("#isCardPay").val())
            },
            success:function(result){
                console.log(result);
                if(result.success){
                    showSuccess("添加成功");
                    $table_in.bootstrapTable('refresh', {url: '/hotel/getInRecord'});
                }
                else {
                    showFailure("添加失败, " + result.info);
                }

            },error:function(result){
                showFailure("添加失败");
            }

        });
    }

}
function isOrderChange() {
    $("#orderLine").toggle();
    $("#otherLine").toggle();
}

function isCardPayChange() {

    $("#cardId").val("");
    $("#cardLine").toggle();
    showPrize();
}

function memberChange(index) {
    if($("#isMember"+index).val()=='会员'){
        $("#guest"+index).attr('placeholder','会员ID');
    }
    else {
        $("#guest"+index).attr('placeholder','客户名字');
    }
}

function add_guest() {
    $guestNumber++;
    var html = "<div class='row' id='guest_row"+$guestNumber+"'> <div class='blank5'></div> <div class='col-sm-5'> <select class='form-control' id='isMember"+$guestNumber+"' onchange='memberChange("+$guestNumber+")'> <option>会员</option> <option>非会员</option> </select> </div> <div class='col-sm-7'> <input type='text' class='form-control' id='guest"+$guestNumber+"' placeholder='会员ID'/> </div></div>";
    $("#guest").append(html);
}

function minus_guest() {
    if($guestNumber==1){
        showInfo("当前行数为1，不可删除");
        return
    }
    $("#guest_row"+$guestNumber).remove();
    $guestNumber--;
}

function showPrize() {
    if( $('#roomNumber').val().length<3 || $("#begin").val().length<3 || $("#end").val().length<3){
        return
    }
    console.log($('#cardId').val());
    $.ajax({
        url: '/hotel/getPrize',
        dataType: "json",
        method: "post",//请求方式
        data:{
            "roomNumber":$('#roomNumber').val(),
            "begin":$("#begin").val(),
            "end":$("#end").val(),
            "cardId":$('#cardId').val()
        },
        success:function(result){
            // console.log(result);
            if(result.errorInfo!=null&&result.errorInfo.length>3){
                showFailure(result.errorInfo)
            }
            else {
                $("#prize").val("原价"+result.originPrize+"，折后"+result.nowPrize);
                $("#type").val(result.type);
                $prize = result.nowPrize;
            }

        },error:function(result){
        }
    });
}

function convert2Bool(value) {
    if(value=="是") {
        return true;
    }
    return false;
}