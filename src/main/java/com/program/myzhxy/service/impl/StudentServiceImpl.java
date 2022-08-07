package com.program.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.myzhxy.entity.LoginForm;
import com.program.myzhxy.entity.Student;
import com.program.myzhxy.mapper.StudentMapper;
import com.program.myzhxy.service.StudentService;
import com.program.myzhxy.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional
@Service("studentServiceImpl")
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("name",loginForm.getUsername());
        studentQueryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Student student = baseMapper.selectOne(studentQueryWrapper);
        return student;
    }

    @Override
    public Student getStudentById(Long userId) {
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("id",userId);
         return baseMapper.selectOne(studentQueryWrapper);
    }

    @Override
    public IPage<Student> getStudentByOpr(Page<Student> pageParam, Student student) {
        //1.p判断条件是否为空
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(student.getName())){
            studentQueryWrapper.like("name",student.getName());
        }
        if(!StringUtils.isEmpty(student.getClazzName())){
            studentQueryWrapper.like("clazz_name",student.getClazzName());
        }
        studentQueryWrapper.orderByDesc("id");
        Page<Student> studentPage = baseMapper.selectPage(pageParam, studentQueryWrapper);

        return studentPage;
    }


}
