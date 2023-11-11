package com.wbu.train.common.exception;

import com.wbu.train.common.respon.CommonRespond;
import jakarta.servlet.ServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @ExceptionHandler(BindException.class)
    public <T>CommonRespond<T> MyExceptionHandler(BindException e, ServletRequest request){
        e.printStackTrace();
        return new CommonRespond<>(10002, e.getAllErrors().stream().map(ex->ex.getDefaultMessage()).collect(Collectors.joining("\n")));
    }

    @ExceptionHandler(Exception.class)
    public <T>CommonRespond<T> ExceptionHandler(Exception e, ServletRequest request){
        return CommonRespond.error(AppExceptionExample.SYSTEM_INNER_ERROR);
    }

}
