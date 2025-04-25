package com.seekcat.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seekcat.reggie.mapper.EmployeeMapper;
import com.seekcat.reggie.pojo.Employee;
import com.seekcat.reggie.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Override
    public Employee updateEmployee(HttpServletRequest httpServletRequest, Employee employee) {
        employee = this.lambdaQuery().eq(Employee::getId, employee.getId()).one();
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser((Long) httpServletRequest.getSession().getAttribute("employee"));

        if (employee.getStatus().equals(0)) {
            employee.setStatus(1);
            this.updateById(employee);
            return employee;
        }

        employee.setStatus(0);
        this.updateById(employee);
        return employee;
    }

}
