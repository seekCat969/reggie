package com.seekcat.reggie.common;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static Result<String> unknowError(){
        return Result.error("一个未知的错误发生了/(ㄒoㄒ)/~~");
    }


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

        return GlobalExceptionHandler.unknowError();
    }

    @ExceptionHandler(IOException.class)
    public Result<String> ioExceptionHandle(IOException e){
        log.error("errorCode:2; {}", e.getMessage());
        if (e.getMessage().contains("disk")){
            return Result.error("无法写入到磁盘");
        }

        return GlobalExceptionHandler.unknowError();
    }

    @ExceptionHandler(OSSException.class)
    public Result<String> oSSExceptionHandle(OSSException oe){
        log.error("errorCode:3; {}", oe.getMessage());
        if (oe.getMessage().contains("OSS")){
            return Result.error("OSS异常");
        }
        return GlobalExceptionHandler.unknowError();
    }

    @ExceptionHandler(ClientException.class)
    public Result<String> clientExceptionHandle(ClientException ce){
        log.error("errorCode:4; {}", ce.getMessage());
        if (ce.getMessage().contains("Client")){
            return Result.error("客户端异常");
        }
        return GlobalExceptionHandler.unknowError();
    }
}
