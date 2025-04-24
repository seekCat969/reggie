package com.seekcat.reggie.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seekcat.reggie.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
