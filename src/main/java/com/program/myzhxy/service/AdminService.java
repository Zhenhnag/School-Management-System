package com.program.myzhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.program.myzhxy.entity.Admin;
import com.program.myzhxy.entity.LoginForm;

//IService接口由MyBatis-Plus提供，里面定义了一些基本的CRUD方法，可以不需要自己再定义
//该类剧有泛型，需要根据情况自己定义
public interface AdminService extends IService<Admin> {
    Admin login(LoginForm loginForm);

    Admin getAdminById(Long userId);

    IPage<Admin> getAdminByOpr(Page<Admin> pageParam, String adminName);
}