package com.seekcat.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seekcat.reggie.common.Result;
import com.seekcat.reggie.pojo.DTO.SetmealDto;
import com.seekcat.reggie.pojo.Setmeal;
import com.seekcat.reggie.pojo.SetmealDish;
import com.seekcat.reggie.service.impl.CategoryServiceImpl;
import com.seekcat.reggie.service.impl.SetmealDishServiceImpl;
import com.seekcat.reggie.service.impl.SetmealServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("/setmeal")
@RestController
public class SetmealController {
    @Resource
    SetmealServiceImpl setmealService;

    @Resource
    SetmealDishServiceImpl setmealDishService;

    @Resource
    CategoryServiceImpl categoryService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<Page<SetmealDto>> selectSetmealWithIndividualPage(@RequestParam Integer page, @RequestParam Integer pageSize) {
        Page<SetmealDto> p1 = new Page<>(page, pageSize);
        Page<Setmeal> p2 = new Page<>(page, pageSize);

        setmealService.page(p2);
        BeanUtils.copyProperties(p2, p1,"records");

        List<Setmeal> Setmeals = p2.getRecords();
        List<SetmealDto> setmealDtos = new ArrayList<>();

        Setmeals.forEach(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal,setmealDto);
            setmealDto.setCategoryName(categoryService.getById(setmeal.getCategoryId()).getName());
            setmealDto.setSetmealDishes(setmealDishService.lambdaQuery().eq(SetmealDish::getSetmealId, setmeal.getId()).list());
            setmealDtos.add(setmealDto);
        });
        p1.setRecords(setmealDtos);
        return Result.success(p1);
    }

    /**
     * 新建套餐
     * */
    @PostMapping
    public Result<String> insertSetmeal(@RequestBody SetmealDto setmealDto){
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal,"setmealDishes","categoryName");
        setmealService.save(setmeal);

        Long setmealId = setmeal.getId();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });

        setmealDishService.saveBatch(setmealDishes);

        return Result.success(null);
    }
}
