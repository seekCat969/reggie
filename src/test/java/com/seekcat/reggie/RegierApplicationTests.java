package com.seekcat.reggie;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seekcat.reggie.mapper.DishMapper;
import com.seekcat.reggie.mapper.EmployeeMapper;
import com.seekcat.reggie.entity.Dish;
import com.seekcat.reggie.service.impl.EmployeeServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.util.List;

@Slf4j
@SpringBootTest
class RegierApplicationTests {

    @Resource
    EmployeeMapper employeeMapper;
    @Resource
    EmployeeServiceImpl employeeServiceImpl;
    @Resource
    DishMapper dishMapper;

    /**
     * 条件构造器Wrapper
     * */
    @Test
    void contextLoads() {
//        log.warn(employeeMapper.selectById(1).toString());
        LambdaQueryWrapper<Dish> empQueryWrapper = new LambdaQueryWrapper<>();
        empQueryWrapper.select(Dish::getCode,Dish::getName,Dish::getPrice).ge(Dish::getPrice,8000);
        List<Dish> list1 = dishMapper.selectList(empQueryWrapper);
        list1.forEach(System.out::println);
    }

    @Test
    void testMD5Hex(){
        log.warn(DigestUtils.md5DigestAsHex("passwd".getBytes()));
    }

}
