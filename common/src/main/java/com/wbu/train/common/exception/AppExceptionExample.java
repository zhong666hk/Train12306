package com.wbu.train.common.exception;

public enum AppExceptionExample {
    SYSTEM_INNER_ERROR(500,"系统内部异常"),
    MEMBER_MOBILE_HAS_EXIST(10002,"该手机已注册过用户"),
    MEMBER_MOBILE_OR_CODE_ERROR(10000,"验证码或手机号错误"),
    MEMBER_CODE_HAS_USED(10003,"验证码已经使用过"),

    MEMBER_CODE_EXPIRE(10001,"验证码已经过期"),
    MEMBER_CODE_TYPE_ERROR(10005,"请输入有效的验证码"),
    PASSENGER_SAVE_ERROR(10006,"乘客保存异常"),

    NOT_LOGIN(10007,"未登录"),
    ;


    private int code;
    private String message;

    private AppExceptionExample(int code, String message){
        this.code=code;
        this.message=message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
