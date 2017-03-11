/**
 * Created by yuminchen on 2017/3/11.
 */
$(document).ready(function () {
    $("#orderLine").hide();
    $("#addGuest").bind("click", add_guest);
    $("#minusGuest").bind("click", minus_guest);

    $guestNumber = 1;
});

function isOrderChange() {
    $("#orderLine").toggle();
    $("#otherLine").toggle();
}

function isCardPayChange() {
    $("#cardLine").toggle();
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