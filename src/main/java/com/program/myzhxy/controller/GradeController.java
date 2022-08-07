package com.program.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.myzhxy.entity.Grade;
import com.program.myzhxy.service.GradeService;
import com.program.myzhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Grade Controller")
@RequestMapping("/sms/gradeController")
@RestController
public class GradeController {
    @Autowired
    private GradeService gradeService;

    /**
     * 查询所有年级信息(未实现)
     * @return
     *//*
    @ApiOperation("Query all grades")
    @GetMapping("/GetGrades")
    public Result getGrades(){
        List<Grade> gradesList = gradeService.list();
        return Result.ok(gradesList);
    }*/

    /**
     * 查询所有年级信息(未实现)
     * @return
     */
    @ApiOperation("Query all grades")
    @GetMapping("/getGrades")
    public Result getGrades(){
        List<Grade> grades =gradeService.getGrades();
        return Result.ok(grades);
    }

    /**
     * 删除年级
     * @param ids
     * @return
     * @ApiParam 介绍参数的含义
     */
    @ApiOperation("Delete Grade Info")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(
            @ApiParam("json collection of all ids to delete") @RequestBody List<Integer> ids){

        gradeService.removeByIds(ids);
        return  Result.ok();
    }
    /**
     * 新加或修改操作
     * @param grade
     * @return
     */
    @ApiOperation("Create or Update Grade,If there is an id attribute, " +
            "it will be modified,if not, it will be added.")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(
            @ApiParam("Grade object in json format,")
            @RequestBody Grade grade){

        //1.接收参数
        /*2.调用Service层方法完成增删或修改操作
            注意：前端是以json的格式发送数据(请求体中的字符串)
               后端用Grade对象来接收,因为Grade中的属性，正好与表单中的属性一样
               使用@RequestBody 将json字符串转换为 Grade对象*/

        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    /**
     * 分页带条件查询
     * @param pageNo
     * @param pageSize
     * @param gradeName
     * @return
     */
    @ApiOperation("Fuzzy query based on grade name with pagination")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @ApiParam("Page number of pagination query") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("Total number of pages for pagination queries") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("Paginated query fuzzy matching name") String gradeName    //获取请求中的参数
    ){
        //1.分页 带条件查询
        Page<Grade> page = new Page<>(pageNo,pageSize);

        //2.通过Service层查询
        IPage<Grade> pageRs = gradeService.getGradeByOpr(page,gradeName);

        //封装Result对象并返回
        return Result.ok(pageRs);
    }
}
