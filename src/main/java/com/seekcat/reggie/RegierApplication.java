package com.seekcat.reggie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.seekcat.reggie.mapper")
public class RegierApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegierApplication.class, args);
    }
}
