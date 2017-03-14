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
    if(value == null){
        return null;
    }
    return value.year+'/'+value.monthValue+'/'+value.dayOfMonth;
}

$(function () {
    $('form').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            username: {
                message: '用户名验证失败',
                validators: {
                    notEmpty: {
                        message: '用户名不能为空'
                    },

                    regexp: {
                        regexp: /^[a-zA-Z0-9_]+$/,
                        message: '用户名只能包含大写、小写、数字和下划线'
                    }
                }
            },
            hotelname: {
                message: '客栈名验证失败',
                validators: {
                    notEmpty: {
                        message: '客栈名不能为空'
                    }
                }
            },
            id:{
                message: 'ID验证失败',
                validators: {
                    notEmpty: {
                        message: 'ID不能为空'
                    },
                    stringLength: {
                        min: 7,
                        max: 7,
                        message: 'ID长度为7位'
                    }
                }
            },
            originPassword: {
                validators: {
                    notEmpty: {
                        message: '密码不能为空'
                    },
                    stringLength: {
                        min: 6,
                        max: 18,
                        message: '密码长度必须在6到18位之间'
                    }
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: '密码不能为空'
                    },
                    stringLength: {
                        min: 6,
                        max: 18,
                        message: '密码长度必须在6到18位之间'
                    }
                }
            },
            passwordTwice:{
                validators: {
                    notEmpty: {
                        message: '确认密码不能为空'
                    },
                    identical: {
                        field: 'password',
                        message: '两次输入密码不一致'
                    }
                }
            },
            phone:{
                validators:{
                    notEmpty: {
                        message: '手机号不能为空'
                    },
                    stringLength: {
                        min: 11,
                        max: 11,
                        message: '手机号长度为11位'
                    }
                }
            },
            bank:{
                validators:{
                    notEmpty: {
                        message: '银行卡号不能为空'
                    },
                    stringLength: {
                        min: 16,
                        max: 16,
                        message: '银行卡号长度为16位'
                    }
                }
            },
            money:{
                validators:{
                    notEmpty: {
                        message: '金额不能为空'
                    },
                    numeric: {
                        message: '发票金额只能输入数字'
                    },
                }
            }

        }
    });
});
