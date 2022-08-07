package com.program.myzhxy.entity;

import lombok.Data;

/**
 * 在登陆时需要将登陆信息传递给后端，所以创建该类接收登录信息
 * 为了接收数据方便
 */
@Data
public class LoginForm {
    private String username;
    private String password;
    private String verifiCode;
    private Integer userType;
}
