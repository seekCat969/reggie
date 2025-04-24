package com.seekcat.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seekcat.reggie.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
