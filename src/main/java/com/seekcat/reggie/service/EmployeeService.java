package com.seekcat.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seekcat.reggie.entity.Employee;
import jakarta.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {

    Employee updateEmployee(HttpServletRequest httpServletRequest, Employee employeeMode);
}
