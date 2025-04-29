package com.seekcat.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seekcat.reggie.mapper.CategoryMapper;
import com.seekcat.reggie.entity.Category;
import com.seekcat.reggie.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
