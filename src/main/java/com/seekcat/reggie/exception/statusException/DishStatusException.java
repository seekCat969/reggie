package com.seekcat.reggie.exception.statusException;

import com.seekcat.reggie.exception.StatusException;

public class DishStatusException extends StatusException {

    public DishStatusException(String message) {
        super(100, "菜品" + message + "在售，无法删除");
    }
}
