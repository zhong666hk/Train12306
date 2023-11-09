package com.wbu.train.common.exception;

import lombok.Data;

@Data
public class MyException extends RuntimeException{
    private int code;

    private String message;

    public MyException(){}

    public MyException(int code,String message){
        this.code=code;
        this.message=message;
    }
    public MyException(AppExceptionExample appExceptionExample){
        this(appExceptionExample.getCode(), appExceptionExample.getMessage());
    }
}
