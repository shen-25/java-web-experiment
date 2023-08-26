package com.jingdong.manager.exception;

public enum BusinessExceptionEnum {
    /**
     * 10000-19999为 用户异常
     * 20001-3000
     */
    NEED_USER_NAME(10001, "用户名不能为空"),
    NEED_PASSWORD(10002, "密码不能为空"),
    NEED_CODE(10003, "验证码不能为空"),
    ERROR_USER_NAME_OR_PASSWORD(10004, "账号或密码错误!"),
    ERROR_PASSWORD(10004, "原密码错误!"),
    ERROR_VERIFY_CODE(10004, "验证码错误!"),
    REQUEST_PARAM_ERROR(10005, "输入参数错误"),
    NEED_ROLE(10006, "用户角色不能为空"),
    NEED_USER(10007, "用户不能为空"),
    USER_EXISTS(10008, "该用户已存在"),
    PASSWORD_NOT_EQUALS(10010, "新密码与确认的密码不一样"),
    OLD_PASSWORD_NOT_EQUALS(10012, "新密码与旧密码不能一样"),

    NEED_PERMISSION_LOGIN(10009, "您没有权限登录"),
    ROLE_NOT_EXISTS(10010, "角色不存在"),
    ROLE_NAME_CANNOT_BE_THE_SAME(10011, "角色名称不能相同"),
    ROLE_NAME_CANNOT_EDIT(10012, "角色名称不能编辑"),
    USER_ROLE_IS_EMPTY(10013, "该用户角色为空"),
    NEED_MENU_NAME(10014, "用户角色名称不能为空"),
    MENU_NAME_CANNOT_BE_THE_SAME(10015, "菜单名称已存在"),
    MENU_URL_EXISTS(10016, "菜单URL已存在"),
    BRAND_NAME_EXISTS(10017, "品牌名称已经存在"),
    BRAND_NOT_EXISTS(10018, "品牌不存在"),
    CATEGORY_NAME_EXIST(10019, "商品分类名称已存在"),
    CATEGORY_NOT_EXIST(10020, "商品分类不存在"),
    DEPT_NAME_EXIST(10021, "部门名称已存在"),
    DEPT_NOT_EXIST(10022, "部门不存在"),
    PRODUCT_NAME_EXIST(10023, "商品名称已存在"),
    PRODUCT_NOT_EXIST(10024, "商品不存在"),
    PRODUCT_NOT_SALE(10024, "该商品已下架"),
    PRODUCT_NOT_COUNT(10024, "商品库存不够"),

    COMMENT_NOT_EXIST(10025, "评论不存在"),
    USE_NOT_EXIST(10026, "用户不存在"),
    NOTICE_NOT_EXIST(10027, "公告不存在"),
    NOTICE_TITLE_EXIST(10028, "公告标题已存在"),
    ORDER_NOT_EXIST(10028, "订单不存在"),
    ORDER_ITEM_NOT_EXITS(213232, "订单对应的商品不存在"),
    CART_NOT_EXITS(213232, "购物车不存在"),
    ADVERT_NAME_EXIST(23423, "广告名称已存在"),
    ADVERT_EXIST(2432432, "广告已存在"),
    ADVERT_NOT_EXIST(323423, "广告不存在"),
    AFTER_SALES_ID_NOT_EXIST(3223, "服务单号不存在"),

    SYSTEM_ERROR(20000, "系统异常,请联系管理员");

    private Integer code;
    private String msg;

    private BusinessExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
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
