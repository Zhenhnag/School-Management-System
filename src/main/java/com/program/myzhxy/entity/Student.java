package com.program.myzhxy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_student")
public class Student {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String sno; //studentNumber
    private String name;
    private String gender;
    private String password;
    private String email;
    private String telephone;
    private String address;
    private String introducation;
    private String portraitPath;    //头像图片路径
    private String clazzName;
}
