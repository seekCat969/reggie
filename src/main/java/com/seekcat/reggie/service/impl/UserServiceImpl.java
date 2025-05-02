package com.seekcat.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seekcat.reggie.entity.User;
import com.seekcat.reggie.mapper.UserMapper;
import com.seekcat.reggie.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
