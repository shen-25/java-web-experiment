function switchValid(onOff, input, errorSelector, msg){
  if(onOff == false){
    $(errorSelector).text(msg)
  } else{
    $(errorSelector).text("")
  }
}
function regular(val){
  let num = 0;
  let bigLetter = 0;
  let smallLetter = 0;
  if(val.length < 8){
    return false;
  }
  for(let i = 0; i < val.length; i++){
   let ch = val.charAt(i);
   if(ch >='a' && ch <='z'){
    smallLetter += 1;
   } else if(ch >='A' && ch <='Z'){
     bigLetter += 1;
   } else if(ch >= '0' && ch <= '9'){
     num += 1;
   }
  }
    if(bigLetter < 1 || smallLetter < 1){
        return false;
    }

  if(num >= 2){
    return true;
  }
  if(bigLetter + smallLetter < 2){
   return false;
  }

  return true;
}

function checkValid(input,errorSelector){
  let val = $(input).val()
  if(input === "#username"){
    if(regular(val)){
       switchValid(true, input, errorSelector)
    } else{
      let msg = "用户名至少8个字符,至少包含2个字母或2个数字,至少一个大写字母和一个小写字母"
      switchValid(false, input, errorSelector, msg);
        return false;
    }
  } else if(input === "#password"){
    if(regular(val)){
       switchValid(true, input, errorSelector);

    } else{
     let msg = "密码至少8个字符,至少包含2个字母或2个数字,至少一个大写字母和一个小写字母"
     switchValid(false, input, errorSelector, msg);
     return false;
    }
  }
  return true;
}

