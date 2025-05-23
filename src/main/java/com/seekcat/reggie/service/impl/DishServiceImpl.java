package com.seekcat.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seekcat.reggie.mapper.DishMapper;
import com.seekcat.reggie.entity.DTO.DishDto;
import com.seekcat.reggie.entity.Dish;
import com.seekcat.reggie.entity.DishFlavor;
import com.seekcat.reggie.service.DishService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Resource
    DishFlavorServiceImpl dishFlavorService;

    @Transactional
    @Override
    public void saveDto(DishDto dishDto) {
        this.save(dishDto);

        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.forEach((DishFlavor dishFlavor) -> {
            dishFlavor.setDishId(id);
        });
        dishFlavorService.saveBatch(flavors);
    }
}

