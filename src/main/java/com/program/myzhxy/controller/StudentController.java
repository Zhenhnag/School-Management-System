package com.program.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.myzhxy.entity.Student;
import com.program.myzhxy.service.StudentService;
import com.program.myzhxy.util.MD5;
import com.program.myzhxy.util.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/sms/studentController")
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @ApiOperation("delete single or multiple student info ")
    @DeleteMapping("/delStudentById")
    public Result deleteStudentById(@ApiParam("Json Array of deleteStudentNumber")@RequestBody List<Integer> ids){

        studentService.removeByIds(ids);
        return Result.ok();
    }

    /**
     * 添加或修改学生信息的方法
     * @param student
     * @return
     */
    @ApiOperation("addOrUpdateStudent")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
            @ApiParam("Student information needs to be saved or modified")@RequestBody Student student){

        /*1.增加学生时，密码是以明文形式上传的，
            但修改时不用转
            判断是否需要转换密码*/
        Integer id = student.getId();
        if (null == id || 0 == id){
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    /**
     * 分页条件查询学生信息的方法
     * @param pageNo
     * @param pageSize
     * @param student
     * @return
     */
    @ApiOperation("Paging conditional query student paging information")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("pageNumber") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("pageSize")@PathVariable("pageSize") Integer pageSize,
            @ApiParam("queryCondition") Student student
    ){
        //1.分页信息封装Page对象
        Page<Student> pageParam = new Page(pageNo, pageSize);

        IPage<Student> studentPage = studentService.getStudentByOpr(pageParam,student);

        //2.封装Result返回
        return Result.ok(studentPage);
    }
}
