package com.seekcat.reggie.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seekcat.reggie.common.OSSManage;
import com.seekcat.reggie.common.Result;
import com.seekcat.reggie.entity.DTO.DishDto;
import com.seekcat.reggie.entity.Dish;
import com.seekcat.reggie.entity.DishFlavor;
import com.seekcat.reggie.exception.statusException.DishStatusException;
import com.seekcat.reggie.service.impl.CategoryServiceImpl;
import com.seekcat.reggie.service.impl.DishFlavorServiceImpl;
import com.seekcat.reggie.service.impl.DishServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Resource
    DishServiceImpl dishService;

    @Resource
    DishFlavorServiceImpl dishFlavorService;

    @Resource
    CategoryServiceImpl categoryService;

    @Resource
    OSSManage ossManage;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    ObjectMapper objectMapper;

    @Resource
    RedisCacheManager cacheManager;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<Page<DishDto>> selectDishWithIndividual(@RequestParam Long page, @RequestParam Long pageSize, @RequestParam(defaultValue = "") String name) {
        Page<Dish> p = new Page<>(page, pageSize);
        p = dishService.lambdaQuery().like(!name.isEmpty(), Dish::getName, name).orderByAsc(Dish::getCreateTime).page(p);

        Page<DishDto> p2 = new Page<>();
        BeanUtils.copyProperties(p, p2, "records");

        List<Dish> records = p.getRecords();
        List<DishDto> records2 = new ArrayList<>();

        records.forEach(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            dishDto.setCategoryName(categoryService.getById(dish.getCategoryId()).getName());
            records2.add(dishDto);
        });
        p2.setRecords(records2);

        return Result.success(p2);
    }

    /**
     * 根据ID查询菜品
     */
    @GetMapping("/{id}")
    @Cacheable(value = "DishByCategory",key = "#root.args[0]")
    public Result<DishDto> selectDishById(@PathVariable Long id) {
        Dish dish = dishService.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(dishFlavorService.lambdaQuery().eq(DishFlavor::getDishId, dish.getId()).list());
        return Result.success(dishDto);
    }

    /**
     * 添加菜品
     */
    @PostMapping
    @CacheEvict(value = "DishByCategory",allEntries = true)
    public Result<String> insertDish(@RequestBody DishDto dishDto) {

        dishService.saveDto(dishDto);
        return Result.success(null);
    }

    /**
     * 删除菜品
     */
    @Transactional
    @DeleteMapping
    @CacheEvict(value = "DishByCategory",allEntries = true)
    public Result<String> deleteDishById(@RequestParam(name = "ids") List<Long> ids) {
        ids.forEach(id -> {
            Dish d = dishService.getById(id);
            if (!d.getStatus().equals(1)) {
                throw new DishStatusException(d.getName());
            }
            dishService.removeById(id);
            ossManage.deleteImage(d.getImage());
            dishFlavorService.lambdaUpdate().eq(DishFlavor::getDishId, id).remove();

        });
        return Result.success(null);
    }

    /**
     * 修改菜品
     */
    @Transactional
    @PutMapping
    @CacheEvict(value = "DishByCategory",allEntries = true)
    public Result<String> updateDish(@RequestBody DishDto dishDto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto, dish);
        dishService.updateById(dish);
        //删光dish_flavor中关于这道菜的所有口味，再将新请求直接insert
        dishFlavorService.lambdaUpdate().eq(DishFlavor::getDishId, dish.getId()).remove();

        dishDto.getFlavors().forEach((DishFlavor dishFlavor) -> {
            dishFlavor.setDishId(dish.getId());
            dishFlavorService.save(dishFlavor);
        });
        return Result.success(null);
    }

    /**
     * 调整菜品状态
     */
    @PostMapping("/status/{status}")
    @CacheEvict(value = "DishByCategory",allEntries = true)
    public Result<String> switchDish(@RequestParam List<Long> ids, @PathVariable Integer status) {
        ids.forEach((Long id) -> dishService.lambdaUpdate().eq(Dish::getId, id).set(Dish::getStatus, status).update());

        return Result.success(null);
    }

    /**
     * 根据种类查询菜品
     */
    @GetMapping("/list")
    @Cacheable(value = "DishByCategory",key = "#root.args[0]")
    public Result<List<Dish>> selectDishWithCategory(@RequestParam String categoryId){
        List<Dish> dishes = dishService.lambdaQuery().eq(Dish::getCategoryId, categoryId).eq(Dish::getStatus, 1).list();
        return Result.success(dishes);
    }
}
