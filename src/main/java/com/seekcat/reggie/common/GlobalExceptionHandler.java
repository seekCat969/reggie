package com.seekcat.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获sql异常
     * */
    @ExceptionHandler(java.sql.SQLIntegrityConstraintViolationException.class)
    public Result<String> userNameRepeated(java.sql.SQLIntegrityConstraintViolationException e){
        log.error(e.toString());

        /**
         * 捕获用户名重复
         * */
        if (e.toString().contains("Duplicate entry")){
            return Result.error( e.getMessage().split("'")[1] + "已存在");
        }

        return Result.error("一个未知的错误发生了/(ㄒoㄒ)/~~");
    }
}
