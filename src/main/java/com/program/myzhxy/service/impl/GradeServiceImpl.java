package com.program.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.myzhxy.entity.Grade;
import com.program.myzhxy.mapper.GradeMapper;
import com.program.myzhxy.service.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Transactional
@Service("gradeServiceImpl")
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
    @Override
    public IPage<Grade> getGradeByOpr(Page<Grade> pageParam, String gradeName) {

        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(gradeName)) {
            queryWrapper.like("name", gradeName);
        }

        queryWrapper.orderByDesc("id");

        Page<Grade> page = baseMapper.selectPage(pageParam,queryWrapper);
        return page;
    }

    @Override
    public List<Grade> getGrades() {
        return  baseMapper.selectList(null);
    }


}
