package com.seekcat.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seekcat.reggie.pojo.DTO.DishDto;
import com.seekcat.reggie.pojo.Dish;

public interface DishService extends IService<Dish> {
    void saveDto(DishDto dishDto);
}
