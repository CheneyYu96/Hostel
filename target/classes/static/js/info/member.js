/**
 * Created by yuminchen on 2017/3/14.
 */
$(document).ready(function () {

    $("#editInfo").bind("click",edit_info);
    $("#exchange_credit").bind("click",exchange_credit);
    $("#stop").bind("click",stop_qlf);
    $("#pay_fee").bind("click",pay_fee);

});

function edit_info() {
    $.ajax({
        url: '/member/editInfo',
        dataType: "json",
        method: "post",//请求方式
        data:{
            "name":$("#memberName").val(),
            "phone":$("#phone").val(),
            "bankCard":$("#bankCard").val()
        },
        success:function(result){
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

function exchange_credit() {
    $.ajax({
        url: '/member/translateCredit',
        dataType: "json",
        method: "post",//请求方式
        data:{
            "credit":$("#credit").val(),
        },
        success:function(result){
            if(result.info.success){
                showSuccess("兑换成功");
                $("#credit_show").val(result.credit);
                $("#balance").val(result.balance);
                $("#credit").val("");

            }
            else {
                showFailure("兑换失败，"+ result.info.info);
            }

        },error:function(result){
            showFailure("兑换失败");
        }

    });
}
function stop_qlf() {
    $.ajax({
        url: '/member/stopQlf',
        success:function(result){
            if(result.success){
                showSuccess("资格已取消");
                $("#status").val("停止");
            }
            else {
                showFailure("失败"+ result.info);
            }

        },error:function(result){
            showFailure("失败");
        }

    });
}

function pay_fee() {
    $.ajax({
        url: '/member/payFee',
        dataType: "json",
        method: "post",//请求方式
        data:{
            "bank":$("#bank").val(),
            "money":$("#money").val()
        },
        success:function(result){
            if(result.info.success){
                showSuccess("充值成功");
                $("#balance").val(result.balance);
                $("#bank").val("");
                $("#money").val("");

            }
            else {
                showFailure("充值成功，"+ result.info.info);
            }

        },error:function(result){
            showFailure("充值成功");
        }

    });
}