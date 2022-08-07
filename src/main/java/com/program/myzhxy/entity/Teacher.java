package com.program.myzhxy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_teacher")
public class Teacher {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String tno; //teacherNumber
    private String name;
    private String gender;
    private String password;
    private String email;
    private String telephone;
    private String address;
    private String clazzName;
    private String portraitPath;    //头像图片路径

}
