package com.jingdong.manager.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author word
 */
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

//    @ExceptionHandler(BusinessException.class)
//    @ResponseBody
//    public ApiRestResponse handlerBusinessException(BusinessException e) {
//        log.error("handlerBusinessException Exception: " + e);
//        return ApiRestResponse.error(e.getCode(), e.getMsg());
//    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public Object handlerException(Exception e) {
//        log.error("Default Exception: " + e);
//        return ApiRestResponse.error(BusinessExceptionEnum.SYSTEM_ERROR);
//    }
//
//
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseBody
//    public ApiRestResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        log.error("MethodArgumentNotValidException: " + e);
//        return handleBindResult(e.getBindingResult());
//    }
//
//    private ApiRestResponse handleBindResult(BindingResult result) {
//        List<String> list = new ArrayList<>();
//        if (result.hasErrors()) {
//            List<ObjectError> allErrors = result.getAllErrors();
//            for (int i = 0; i < allErrors.size(); i++) {
//                ObjectError objectError = allErrors.get(i);
//                String message = objectError.getDefaultMessage();
//                list.add(message);
//            }
//        }
//        if (list.size() == 0) {
//            return ApiRestResponse.error(BusinessExceptionEnum.REQUEST_PARAM_ERROR);
//        }
//        return ApiRestResponse.error(BusinessExceptionEnum.REQUEST_PARAM_ERROR.getCode(), list.toString());
//
//    }


}
