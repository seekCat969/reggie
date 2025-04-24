package com.seekcat.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seekcat.reggie.mapper.EmployeeMapper;
import com.seekcat.reggie.pojo.Employee;
import com.seekcat.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
