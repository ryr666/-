package com.sky.service.impl;

import com.alibaba.fastjson.serializer.BeanContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        //对前端的明文密码进行MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus().equals(StatusConstant.DISABLE) ) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }
    //新增员工


    @Override
    public void save(EmployeeDTO employeeDTO) {
        System.out.println("当前线程的id为:"+Thread.currentThread().getId());
        Employee employee = new Employee();
        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO,employee);
        //设置账号的状态，默认正常状态
        employee.setStatus(StatusConstant.ENABLE);//数字1不方便,所以调用StatusConstant方法
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        //设置创造时间和更新时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        //设置当前创建记录人ID和修改ID
//        //TODO 后期需要改为当前登录用户的ID
//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.insert(employee);

    }
    //分页查询

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //pageHelp插件
         PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
         Page<Employee> page=  employeeMapper.pageQuery(employeePageQueryDTO);
         long total = page.getTotal();
         List<Employee> records = page.getResult();
        return new PageResult(total,records);
    }
    //启用禁用员工账号

    @Override
    public void startOrStop(Integer status, Long id) {
//         Employee employee = new Employee();
//         employee.setStatus(status);
//         employee.setId(id);
         Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }
    //根据id查询员工

    @Override
    public Employee getById(Long id) {
         Employee employee = employeeMapper.getById(id);
         employee.setPassword("****");
        return employee;
    }
    //编辑员工信息

    @Override
    public void update(EmployeeDTO employeeDTO) {
         Employee employee = new Employee();
         //信息拷贝
         BeanUtils.copyProperties(employeeDTO,employee);

//         employee.setUpdateTime(LocalDateTime.now());
//         employee.setUpdateUser(BaseContext.getCurrentId());
         employeeMapper.update(employee);
    }
}
