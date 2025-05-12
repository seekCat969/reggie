package com.seekcat.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seekcat.reggie.common.Result;
import com.seekcat.reggie.entity.Category;
import com.seekcat.reggie.entity.DTO.SetmealDto;
import com.seekcat.reggie.entity.Setmeal;
import com.seekcat.reggie.entity.SetmealDish;
import com.seekcat.reggie.exception.statusException.SetmealStatusException;
import com.seekcat.reggie.service.impl.CategoryServiceImpl;
import com.seekcat.reggie.service.impl.SetmealDishServiceImpl;
import com.seekcat.reggie.service.impl.SetmealServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("/setmeal")
@RestController
public class SetmealController {
    @Resource
    private SetmealServiceImpl setmealService;

    @Resource
    private SetmealDishServiceImpl setmealDishService;

    @Resource
    private CategoryServiceImpl categoryService;

    @Resource
    private SetmealServiceImpl setmealServiceImpl;


    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<Page<SetmealDto>> selectSetmealWithIndividualPage(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam(required = false) String name) {
        Page<SetmealDto> p1 = new Page<>(page, pageSize);
        Page<Setmeal> p2 = new Page<>(page, pageSize);

        setmealService.lambdaQuery().like(name != null, Setmeal::getName, name).orderByDesc(Setmeal::getCreateTime).page(p2);
        BeanUtils.copyProperties(p2, p1, "records");

        List<Setmeal> Setmeals = p2.getRecords();
        List<SetmealDto> setmealDtos = new ArrayList<>();

        Setmeals.forEach(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            setmealDto.setCategoryName(categoryService.getById(setmeal.getCategoryId()).getName());
            setmealDto.setSetmealDishes(setmealDishService.lambdaQuery()
                    .eq(SetmealDish::getSetmealId, setmeal.getId())
                    .list());
            setmealDtos.add(setmealDto);
        });
        p1.setRecords(setmealDtos);
        return Result.success(p1);
    }

    /**
     * 新建套餐
     */
    @PostMapping
    public Result<String> insertSetmeal(@RequestBody SetmealDto setmealDto) {
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto, setmeal, "setmealDishes", "categoryName");
        setmealService.save(setmeal);

        Long setmealId = setmeal.getId();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));

        setmealDishService.saveBatch(setmealDishes);

        return Result.success(null);
    }

    /**
     * 根据ID查询setmeal
     */
    @GetMapping("/{id}")
    public Result<SetmealDto> selectSetmealWithId(@PathVariable String id) {
        /*
          1.数据库查询Stemeal和StemealDish
          2.封装DTO
          3.return
          */
        Setmeal setmeal = setmealService.getById(id);
        Category categoryName = categoryService.getById(setmeal.getCategoryId());
        List<SetmealDish> setmealDishes = setmealDishService.lambdaQuery().eq(SetmealDish::getSetmealId, id).orderByDesc(SetmealDish::getPrice).list();

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        setmealDto.setSetmealDishes(setmealDishes);
        setmealDto.setCategoryName(categoryName.getName());

        return Result.success(setmealDto);
    }

    /**
     * 更新setmeal
     */
    @Transactional
    @PutMapping
    public Result<String> updateSetmeial(@RequestBody SetmealDto setmealDto) {
        /*
          1.将DTO转换为DO Setmeal和SetmealDish
          2.按照setmealId删光所有数据
          3.对于setmealDish的setmealID字段要进行处理
          4.将新的setmealDish存进去
          */

        Setmeal setmeal = new Setmeal();

        BeanUtils.copyProperties(setmealDto, setmeal);

        setmealDto.getSetmealDishes().forEach(setmealDish -> setmealDish.setSetmealId(setmealDto.getId()));

        setmealService.updateById(setmeal);
        setmealDishService.lambdaUpdate().eq(SetmealDish::getSetmealId, setmeal.getId()).remove();
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());

        return Result.success(null);
    }

    /**
     * 调整套餐状态
     */
    @PostMapping("/status/{status}")
    public Result<String> switchSetmeal(@PathVariable Integer status, @RequestParam List<Long> ids) {
        ids.forEach(id -> setmealService.lambdaUpdate().set(Setmeal::getStatus, status).eq(Setmeal::getId, id).update());
        return Result.success(null);
    }

    /**
     * 删除套餐
     */
    @Transactional
    @DeleteMapping
    public Result<String> deleteSetmeal(@RequestParam List<Long> ids) {

        /*
         * 1.删除在oss上的图片
         * 2.删除setmeal，根据id
         * 3.删除setmealDish，根据setmealId
         * */

        ids.forEach(id -> {
            if (!setmealService.getById(id).getStatus().equals(1)) {
                throw new SetmealStatusException(setmealService.getById(id).getName());
            }
            setmealDishService.lambdaUpdate().eq(SetmealDish::getSetmealId, id).remove();
        });
        setmealService.removeBatchByIds(ids);
        ids.forEach(id -> setmealDishService.lambdaUpdate().eq(SetmealDish::getSetmealId, id).remove());

        return Result.success(null);
    }

    /**
     *
     */
    @GetMapping("/list")
    @Cacheable(value = "SetmealByCategory",key = "#root.args[0]")
    public Result<List<SetmealDto>> selectList(@RequestParam Long categoryId, @RequestParam Integer status) {
        List<Setmeal> setmeals = setmealServiceImpl.lambdaQuery().eq(Setmeal::getCategoryId, categoryId).eq(Setmeal::getStatus,1).list();
        List<SetmealDto> setmealDtos = new ArrayList<>();

        setmeals.forEach((setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);

            setmealDto.setSetmealDishes(setmealDishService.lambdaQuery().eq(SetmealDish::getSetmealId, setmealDto.getId()).list());
            setmealDto.setCategoryName(categoryService.getById(setmeal.getCategoryId()).getName());
            setmealDtos.add(setmealDto);
        }));

        return Result.success(setmealDtos);

    }
}
