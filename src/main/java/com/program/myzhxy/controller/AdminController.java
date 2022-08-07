package com.program.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.myzhxy.entity.Admin;
import com.program.myzhxy.service.AdminService;
import com.program.myzhxy.util.MD5;
import com.program.myzhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 分页带条件查询所有AdminInfo的方法
     * @param pageNo
     * @param pageSize
     * @param adminName
     * @return
     */
    @ApiOperation("Paging with conditions to query administrator information")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @ApiParam("pageNumber") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("pageSize") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("ManagerName") String adminName
    ){
        Page<Admin> pageParam = new Page<>(pageNo,pageSize);

        IPage<Admin> iPage = adminService.getAdminByOpr(pageParam,adminName);
        return Result.ok(iPage);
    }

    /**
     * 添加或修改Admin Info的方法
     * @param admin
     * @return
     */
    @ApiOperation("add or Update Admin Info")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(
            @ApiParam("Admin Object of JSON format")@RequestBody Admin admin
    ){
        Integer id = admin.getId();
        if (null == id || 0 == id){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    @ApiOperation("Delete Single or Multiple Admin Info")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(
            @ApiParam("JSON format Array of AdminInfo need delete")@RequestBody List<Integer> ids
    ){
        adminService.removeByIds(ids);
        return Result.ok();
    }
}
