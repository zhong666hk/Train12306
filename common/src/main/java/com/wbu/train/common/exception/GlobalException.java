package com.wbu.train.common.exception;

import com.wbu.train.common.respon.CommonRespond;
import jakarta.servlet.ServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalException {
    private final Logger logger = LoggerFactory.getLogger(GlobalException.class);
    /**
        第一个T表示<T>是一个泛型 --声明参数
        第二个T表示方法返回的是T类型的数据
    */
    @ExceptionHandler(MyException.class)
    public <T>CommonRespond<T> MyExceptionHandler(MyException e, ServletRequest request){
        logger.error(e.toString());
        return new CommonRespond<>(e.getCode(),e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public <T>CommonRespond<T> ExceptionHandler(Exception e, ServletRequest request){

        return CommonRespond.error(AppExceptionExample.SYSTEM_INNER_ERROR);
    }

}
