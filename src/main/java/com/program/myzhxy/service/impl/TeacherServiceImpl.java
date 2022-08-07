package com.program.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.myzhxy.entity.LoginForm;
import com.program.myzhxy.entity.Teacher;
import com.program.myzhxy.mapper.TeacherMapper;
import com.program.myzhxy.service.TeacherService;
import com.program.myzhxy.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional
@Service("teacherServiceImpl")
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.eq("name",loginForm.getUsername());
        teacherQueryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Teacher teacher = baseMapper.selectOne(teacherQueryWrapper);
        return teacher;
    }

    @Override
    public Teacher getTeacherById(Long userId) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.eq("id",userId);
        return baseMapper.selectOne(teacherQueryWrapper);
    }

    @Override
    public IPage<Teacher> getTeacherByOpr(Page<Teacher> pageParam, Teacher teacher) {

        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(teacher.getName())){
            teacherQueryWrapper.like("name",teacher.getName());
        }
        if (!StringUtils.isEmpty(teacher.getClazzName())){
            teacherQueryWrapper.eq("clazz_name",teacher.getClazzName());
        }
        teacherQueryWrapper.orderByDesc("id");

        Page<Teacher> teacherPage = baseMapper.selectPage(pageParam, teacherQueryWrapper);
        return teacherPage;
    }

}
