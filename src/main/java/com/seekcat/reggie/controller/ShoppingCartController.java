package com.seekcat.reggie.controller;

import com.seekcat.reggie.common.Result;
import com.seekcat.reggie.entity.ShoppingCart;
import com.seekcat.reggie.service.impl.ShoppingCartServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/shoppingCart")
@RestController
public class ShoppingCartController {
    @Resource
    private ShoppingCartServiceImpl shoppingCartService;

    @GetMapping("/list")
    public Result<List<ShoppingCart>> selectShoppingCart(){
        return Result.success(shoppingCartService.list());
    }
}
