package com.jingdong.manager.common;

import com.jingdong.manager.exception.BusinessExceptionEnum;

/**
 * @author word
 */
public class ApiRestResponse<T> {

    private Integer code;
    private String msg;
    private T data;

    private static final int OK_CODE = 200;
    private static final int DEFAULT_ERROR_CODE = 500;
    private static final String OK_MSG = "SUCCESS";

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ApiRestResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ApiRestResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public ApiRestResponse() {
        this(OK_CODE, OK_MSG);
    }

    public static <T> ApiRestResponse<T> success() {
        return new ApiRestResponse<>();
    }

    public static <T> ApiRestResponse<T> success(T result){
        ApiRestResponse<T> response = new ApiRestResponse<>();
        response.setData(result);
        return response;
    }
    public static <T> ApiRestResponse<T> error(Integer code, String msg) {
        return new ApiRestResponse<>(code, msg);
    }

    public static <T> ApiRestResponse<T> error(BusinessExceptionEnum ex) {
        return new ApiRestResponse<>(ex.getCode(), ex.getMsg());
    }

    public static <T> ApiRestResponse<T> error(String msg) {
        return new ApiRestResponse<>(DEFAULT_ERROR_CODE, msg);
    }

}
