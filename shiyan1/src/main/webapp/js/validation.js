function switchValid(onOff, input, errorSelector, message) {
    if (onOff == false) {
        $(errorSelector).text(message);
        $(input).addClass("error_input");
        $(errorSelector).addClass("error_message");
    } else {
        $(errorSelector).text("");
        $(input).removeClass("error_input");
        $(errorSelector).removeClass("error_message");
    }
}

function checkEmpty(input, errorSelector){

    var val = $(input).val()
    if($.trim(val) === ""){
        msg = "请输入内容"
        if(input === "#userName"){
            msg = "请输入用户名"
        } else if(input === "#password"){
            msg = "请输入密码"
        } else if(input === "#verifyCode"){
            msg = "请输入验证码"
        }
        switchValid(false, input, errorSelector, msg)
        return false;
    }  else{
        switchValid(true, input, errorSelector)
        return true
    }
}
function onInput(input){
    $(input).removeClass("error_input");

}