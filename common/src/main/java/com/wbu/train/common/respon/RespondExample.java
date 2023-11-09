package com.wbu.train.common.respon;
//这个枚举类中定义的都是跟业务有关的异常

/**
 * 10000 关于用户
 */
public enum RespondExample{
    INVALID_CODE(10000,"验证码无效"),
    USERNAME_NOT_EXISTS(10001,"用户名不存在"),
    USER_ROLE_NOT_PERMISSION(10003,"用户权限不足"),
    REQUEST_PARAMETER_IS_ILLEGAL(10004,"参数不合法");

    private int code;
    private String message;

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

    RespondExample(){}
    RespondExample(int code,String message){
        this.code=code;
        this.message=message;
    }
}
