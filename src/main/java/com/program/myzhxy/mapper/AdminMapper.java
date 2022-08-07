package com.program.myzhxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.myzhxy.entity.Admin;
import org.springframework.stereotype.Repository;

@Repository //方便Spring识别，扫描到该接口
public interface AdminMapper extends BaseMapper<Admin> {

}
