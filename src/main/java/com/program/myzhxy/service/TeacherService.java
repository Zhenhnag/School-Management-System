package com.program.myzhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.program.myzhxy.entity.LoginForm;
import com.program.myzhxy.entity.Teacher;

public interface TeacherService extends IService<Teacher> {

    Teacher login(LoginForm loginForm);


    Teacher getTeacherById(Long userId);

    IPage<Teacher> getTeacherByOpr(Page<Teacher> pageParam, Teacher teacher);

}