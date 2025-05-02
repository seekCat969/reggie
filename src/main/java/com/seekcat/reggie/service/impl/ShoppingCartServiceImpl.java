package com.seekcat.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seekcat.reggie.entity.ShoppingCart;
import com.seekcat.reggie.mapper.ShoppingCartMapper;
import com.seekcat.reggie.service.ShoppingCartService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Resource
    ShoppingCartMapper shoppingCartMapper;
}
