package com.seekcat.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.seekcat.reggie.interceptor.LoginCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
/**
 * 元数据公共字段自动填充
 * */
public class MPMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", LoginCheckInterceptor.getUserID());
        metaObject.setValue("updateUser", LoginCheckInterceptor.getUserID());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", LoginCheckInterceptor.getUserID());
    }
}
