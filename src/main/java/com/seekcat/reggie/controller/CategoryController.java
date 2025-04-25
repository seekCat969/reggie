package com.seekcat.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seekcat.reggie.common.Result;
import com.seekcat.reggie.pojo.Category;
import com.seekcat.reggie.service.impl.CategoryServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequestMapping("/category")
@RestController
public class CategoryController {

    @Resource
    CategoryServiceImpl categoryService;

    /**
     * 分页查询
     * */
    @GetMapping("/page")
    public Result<Page> selectCategoryIndividualWithPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize){
        Page<Category> p = new Page<>(page,pageSize);
        p = categoryService.lambdaQuery().orderByAsc(Category::getSort).page(p);
        return Result.success(p);
    }

    /**
     * 添加菜品分类
     * */
    @PostMapping
    public Result<String> insertCategroy(@RequestBody Category category){
        categoryService.save(category);
        return Result.success("添加成功");
    }

    /**
     * 删除分类
     * */
    @DeleteMapping
    public Result<String> deleteCategroy(@RequestParam(name = "ids") Long id){
        categoryService.removeById(id);
        return Result.success("删除成功");
    }

    /**
     * 修改分类
     * */
    @PutMapping
    public Result<String> updateCategroy(@RequestBody Category category){
        categoryService.updateById(category);
        return Result.success("更新成功");
    }
}
