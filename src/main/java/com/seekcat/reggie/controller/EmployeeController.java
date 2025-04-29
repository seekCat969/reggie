package com.seekcat.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seekcat.reggie.common.Result;
import com.seekcat.reggie.entity.Employee;
import com.seekcat.reggie.service.impl.EmployeeServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    EmployeeServiceImpl employeeServiceImpl;

    /**
     * 员工登录接口
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest httpServletRequest, @RequestBody Employee employee) {

        Employee dbEmployee = employeeServiceImpl.lambdaQuery()
                .eq(Employee::getUsername, employee.getUsername())
                .one();

        if (dbEmployee == null) {
            return Result.error("登录失败");
        }

        String inputPwdMd5 = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        if (!dbEmployee.getPassword().equals(inputPwdMd5)) {
            return Result.error("登录失败");
        }

        if (dbEmployee.getStatus() != 1) {
            return Result.error("用户已禁用");
        }
        httpServletRequest.getSession().setAttribute("employee", dbEmployee.getId());
        return Result.success(dbEmployee); // 登录成功
    }

    /**
     * 员工退出接口
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    /**
     * 新增员工
     */
    @PostMapping
    public Result<Employee> insertEmployee(@RequestBody Employee employee) {
        String idNumber = employee.getIdNumber();
        employee.setPassword(DigestUtils.md5DigestAsHex(idNumber.substring(idNumber.length() - 6).getBytes()));
        employeeServiceImpl.save(employee);
        return Result.success(employee);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<Page> selectEmployeeIndividualPage(@RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                     @RequestParam(required = false) String name) {
        Page<Employee> p1 = new Page<>(page, pageSize);
        employeeServiceImpl.lambdaQuery().like(name != null, Employee::getName, name).page(p1);

        return Result.success(p1);
    }

    /**
     * 禁用-启用员工
     * 0禁用状态 1正常状态
     * <p>
     * 更新员工信息
     */
    @PutMapping
    public Result<String> updateEmployee(@RequestBody Employee employee) {
        employeeServiceImpl.updateById(employee);

        return Result.success("更新成功");
    }

    /**
     * 根据id查询员工
     */
    @GetMapping("/{id}")
    public Result<Employee> selectById(@PathVariable Long id) {
        return Result.success(employeeServiceImpl.getById(id));
    }
}
