package com.wbu.train.common.respon;

import com.wbu.train.common.exception.AppExceptionExample;
import lombok.Data;

@Data
public class CommonRespond <T>{
    //响应状态码
    private int code;
    // 响应信息
    private String message;

    // 返回结果
    private T data;

    public CommonRespond(){}

    public CommonRespond(int code,String message,T data){
        this.code=code;
        this.message=message;
        this.data=data;
    }

    public CommonRespond(int code,String message){
        this(code,message,null);
    }

    public CommonRespond(int code,T data){
        this(code,null,data);
    }

    public static <T>CommonRespond<T> error(AppExceptionExample appExceptionExample){
        return new CommonRespond<>(appExceptionExample.getCode(), appExceptionExample.getMessage());
    }

    public static <T>CommonRespond<T> error(RespondExample respondExample){
        return new CommonRespond<>(respondExample.getCode(),respondExample.getMessage());
    }



    public static <T>CommonRespond<T> succeed(String message, T data){
        return new CommonRespond<>(200,message,data);
    }

    public static <T>CommonRespond<T> succeed(T data){
        return new CommonRespond<>(200,null,data);
    }
}
