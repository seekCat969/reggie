package com.seekcat.reggie.exception.statusException;

import com.seekcat.reggie.exception.StatusException;

public class SetmealStatusException extends StatusException {
    public SetmealStatusException(String message) {
        super(101, "套餐" + message + "在售，无法删除");
    }
}
