package com.program.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.myzhxy.entity.Clazz;
import com.program.myzhxy.service.ClazzService;
import com.program.myzhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "ClazzController")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    @ApiOperation("Query all clazzs info")
    @GetMapping("/getClazzs")
    public Result getClazzs(){
        List<Clazz> clazzes = clazzService.getClazzs();

        return Result.ok(clazzes);
    }

    /**
     * 删除单个或多个班级的方法
     * @param ids
     * @ApiParam 要删除的班级的id数组
     */
    @ApiOperation("Delete single or multiple classes")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(
            @ApiParam("Array of ids of the classes to delete")@RequestBody List<Integer> ids){

        clazzService.removeByIds(ids);
        return Result.ok();
    }

    /**
     * 增加或修改班级信息
     * @param clazz
     * @return
     */
    @ApiOperation("Create or Update Clazz")
    @PostMapping("saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
            @ApiParam("Clazz Info for JSON format")@RequestBody Clazz clazz
    ){
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }


    /**
     * 分页带条件查询班级信息
     * @return
     */
    @ApiOperation("Pagination with conditions to query class info")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzByOpr(
            @ApiParam("Page number of pagination query") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("Total number of pages for pagination queries") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("Query conditions for paging query") Clazz clazz
    ){
        Page<Clazz> page = new Page<>(pageNo,pageSize);

        IPage<Clazz> iPage = clazzService.getClazzsByOpr(page,clazz);

        return Result.ok(iPage);
    }
}
