package com.seekcat.reggie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.seekcat.reggie.mapper")
@EnableTransactionManagement
@EnableCaching
public class RegierApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegierApplication.class, args);
    }
}

















