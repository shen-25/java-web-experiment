package com.jingdong.manager.exception;

/**
 * @author word
 */
public class BusinessException extends RuntimeException{
    private Integer code;
    private String msg;

    public BusinessException(Integer code, String msg) {
        super(code + ":" + msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(BusinessExceptionEnum exceptionEnum) {
        this(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
