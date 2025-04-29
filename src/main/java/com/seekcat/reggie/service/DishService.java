package com.seekcat.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seekcat.reggie.entity.DTO.DishDto;
import com.seekcat.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    void saveDto(DishDto dishDto);
}
