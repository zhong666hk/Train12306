package com.wbu.train.common.respon;
//这个枚举类中定义的都是跟业务有关的异常

/**
 * 10000 关于用户
 */
public enum RespondExample{
    // 10000 验证码或者电话错误
//    10002  spring的校验异常
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
