/**
 * Created by yuminchen on 2017/3/10.
 */

function showInfo(msg) {
    showTip(msg, 'info');
}
function showSuccess(msg) {
    showTip(msg, 'success');
}
function showFailure(msg) {
    showTip(msg, 'danger');
}


function showTip(tip, type) {
    var $tip = $('#tip');
    $tip.attr('class', 'alert alert-' + type).text(tip).css('margin-left', - $tip.outerWidth()/2);
    $tip.fadeIn(500).delay(1000).fadeOut(500);
}

function formatDate(value) {
    return value.year+'/'+value.monthValue+'/'+value.dayOfMonth;
}
