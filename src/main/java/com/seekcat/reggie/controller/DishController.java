package com.seekcat.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seekcat.reggie.common.OSSManage;
import com.seekcat.reggie.common.Result;
import com.seekcat.reggie.pojo.DTO.DishDto;
import com.seekcat.reggie.pojo.Dish;
import com.seekcat.reggie.pojo.DishFlavor;
import com.seekcat.reggie.service.impl.DishFlavorServiceImpl;
import com.seekcat.reggie.service.impl.DishServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Resource
    DishServiceImpl dishService;

    @Resource
    DishFlavorServiceImpl dishFlavorService;

    @Resource
    OSSManage ossManage;

    /**
     * 分页查询
     * */
    @GetMapping("/page")
    public Result<Page> selectDishWithIndividual(@RequestParam Long page, @RequestParam Long pageSize, @RequestParam(defaultValue = "") String name) {
        Page<Dish> p = new Page<>(page, pageSize);
        p = dishService.lambdaQuery().like(!name.isEmpty(), Dish::getName,name).page(p);
        return Result.success(p);
    }

    /**
     * 根据ID查询菜品
     * */
    @GetMapping("/{id}")
    public Result<Dish> selectDishById(@PathVariable Long id){
        return Result.success(dishService.getById(id));
    }

    /**
     * 添加菜品
     * */
    @PostMapping
    public Result<String> insertDish(@RequestBody DishDto dishDto){

//        dishFlavorService.lambdaUpdate().set(DishFlavor::getId,dishDto.getId()).;
//        dishService.save(dishService.DtoToDo(dishDto));
        return Result.success(null);
    }

    @DeleteMapping
    public Result<String> deleteDishById(@RequestParam(name = "ids") Long id){
        Dish d = dishService.getById(id);
        dishService.removeById(id);
        ossManage.deleteImage(d.getImage());
        dishFlavorService.lambdaUpdate().eq(DishFlavor::getDishId,id).remove();
        return Result.success(null);
    }

}
