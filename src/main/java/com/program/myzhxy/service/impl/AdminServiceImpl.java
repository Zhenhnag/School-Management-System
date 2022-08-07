package com.program.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.myzhxy.entity.Admin;
import com.program.myzhxy.entity.LoginForm;
import com.program.myzhxy.mapper.AdminMapper;
import com.program.myzhxy.service.AdminService;
import com.program.myzhxy.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/*
ServiceImpl需要传两个泛型
1.正常应该在类中注入一个Mapper，现在只需要在泛型中声明需要使用哪个Mapper
2.需要在泛型中写入需要对哪个数据库进行CRUD操作,用哪个实体类进行封装
 */

@Service("adminServiceImpl")
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Override
    public Admin login(LoginForm loginForm) {
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("name",loginForm.getUsername());
        //注意：loginForm中的密码获得的是明文，需要使用MD5转换为密文
        adminQueryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Admin admin = baseMapper.selectOne(adminQueryWrapper);
        return admin;
    }

    @Override
    public Admin getAdminById(Long userId) {
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("id",userId);
        return baseMapper.selectOne(adminQueryWrapper);
    }

    @Override
    public IPage<Admin> getAdminByOpr(Page<Admin> pageParam, String adminName) {

        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(adminName)){
            adminQueryWrapper.like("name",adminName);
        }
        adminQueryWrapper.orderByDesc("id");

        Page<Admin> adminPage = baseMapper.selectPage(pageParam, adminQueryWrapper);
        return adminPage;
    }
}
