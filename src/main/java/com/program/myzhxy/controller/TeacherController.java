package com.program.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.myzhxy.entity.Teacher;
import com.program.myzhxy.service.TeacherService;
import com.program.myzhxy.util.MD5;
import com.program.myzhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "TeacherController")
@RestController
/*
 不是@Controller：前后端异步交互。每一个Controller中的方法都要加@ResponseBody，
 为了减少写@ResponseBody，使用@RestController，让类中每个方法都是异步交互的方法，直接返回数据的方法
 */
@RequestMapping("/sms/teacherController")
//请求映射路径
//sms:smart campus manage system
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    /**
     * 分页带条件查询教师的方法
     * @param pageNo
     * @param pageSize
     * @param teacher
     * @return
     */
    /*
     * get  sms/teacherController/getTeachers/1/3
     *      sms/teacherController/getTeachers/1/3?name=小&clazzName=一年一班
     *      请求数据
     *      响应Result  data= 分页
     * */
    @ApiOperation("Pagination with conditions to query teacher information")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询条件") Teacher teacher
    ){
        Page<Teacher> paraParam = new Page<>(pageNo,pageSize);

        IPage<Teacher> page = teacherService.getTeacherByOpr(paraParam,teacher);

        return Result.ok(page);
    }


    /**
     * 添加或修改老师信息
     * @param teacher
     * @return
     * @ApiParam 要保存或修改的JSON格式的Teacher
     */
    /*
     * post sms/teacherController/saveOrUpdateTeacher
     *      请求数据 Teacher
     *      响应Result data= null OK
     * */
    @ApiOperation("addOrUpdateTeacherInfo")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacherInfo(
            @ApiParam("JSON format of add or update Teacher info")@RequestBody Teacher teacher
    ){
        //如果是新增教师，需要对密码加密
        if (teacher.getId() == null || teacher.getId() == 0){
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }

    /*
     * DELETE sms/teacherController/deleteTeacher
     *   请求的数据 JSON 数组 [1,2,3]
     *   响应Result data =null  OK
     * */
    @ApiOperation("delete single or multiple teacher info")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(
            @ApiParam("The id of the teacher info needs to be deleted")@RequestBody List<Integer> ids
    ){
        teacherService.removeByIds(ids);
        return Result.ok();
    }
}
