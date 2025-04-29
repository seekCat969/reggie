package com.seekcat.reggie.exception;

public abstract class StatusException extends RuntimeException{
    private final Integer code;

    public StatusException(int code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
