package com.seekcat.reggie.controller;

import com.seekcat.reggie.common.Result;
import com.seekcat.reggie.entity.User;
import com.seekcat.reggie.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    UserServiceImpl userService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody User user, HttpServletRequest request) {
        if (!userService.lambdaQuery().eq(User::getPhone,user.getPhone()).exists()){

        }
        request.getSession().setAttribute("user", userService.lambdaQuery().eq(User::getPhone, user.getPhone()).one().getId());
        return Result.success("登录成功");
    }
}
